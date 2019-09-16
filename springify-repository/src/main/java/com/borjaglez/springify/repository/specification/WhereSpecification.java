/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.borjaglez.springify.repository.specification;

import org.hibernate.query.criteria.internal.path.PluralAttributePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.borjaglez.springify.repository.filter.impl.Filter;
import com.borjaglez.springify.repository.filter.Logic;
import com.borjaglez.springify.repository.filter.Operator;
import com.borjaglez.springify.repository.specification.exception.SpecificationException;

import javax.persistence.criteria.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhongjun on 6/18/17.
 * 
 * @author Borja González Enríquez
 */
@SuppressWarnings("serial")
public class WhereSpecification<T> implements Specification<T> {
	private static Logger logger = LoggerFactory.getLogger(WhereSpecification.class);
	private SimpleDateFormat defaultDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	private SimpleDateFormat dateFormat;
	private static final JoinType defaultJoinType = JoinType.LEFT;
	private JoinType joinType;
	private transient Filter filter;
	private transient Root<?> root;
	private transient Join<?, ?> join;

	public WhereSpecification(Filter filter) {
		this.filter = filter;
	}

	public WhereSpecification(Filter filter, SimpleDateFormat dateFormat) {
		this(filter);
		this.dateFormat = dateFormat;
	}

	public WhereSpecification(Filter filter, SimpleDateFormat dateFormat, JoinType joinType) {
		this(filter, dateFormat);
		this.joinType = joinType;
	}

	public WhereSpecification(Filter filter, JoinType joinType) {
		this(filter);
		this.joinType = joinType;
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		this.root = root;
		return getPredicate(filter, root, cb);
	}

	private Predicate getPredicate(Filter filter, Path<T> root, CriteriaBuilder cb) {
		if (isInValidFilter(filter))
			return null;
		if (filter.getLogic() == null) {// one filter
			return doGetPredicate(filter, root, cb);
		} else {// logic filters
			if (filter.getLogic().equals(Logic.AND)) {
				Predicate[] predicates = getPredicateList(filter, root, cb);
				return cb.and(predicates);
			} else if (filter.getLogic().equals(Logic.OR)) {
				Predicate[] predicates = getPredicateList(filter, root, cb);
				return cb.or(predicates);
			} else {
				throw new SpecificationException("Unknown filter logic" + filter.getLogic());
			}
		}
	}

	private Predicate[] getPredicateList(Filter filter, Path<T> root, CriteriaBuilder cb) {
		List<Predicate> predicateList = new LinkedList<>();
		for (Filter f : filter.getFilters()) {
			join = null;
			Predicate predicate = getPredicate(f, root, cb);
			if (predicate != null)
				predicateList.add(predicate);
		}
		return predicateList.toArray(new Predicate[predicateList.size()]);
	}

	private boolean isInValidFilter(Filter filter) {
		return filter == null || (filter.getField() == null && filter.getFilters() == null && filter.getLogic() == null
				&& filter.getValue() == null && filter.getOperator() == null);
	}

	private Predicate doGetPredicate(Filter filter, Path<T> root, CriteriaBuilder cb) {
		String field = filter.getField();
		Boolean ignoreCase = filter.getIgnoreCase();
		Path<?> path = null;
		try {
			logger.debug("Parsing child path {} of root {}", field, root.getJavaType());
			path = parsePath(root, field);
		} catch (Exception e) {
			throw new SpecificationException(
					"Met problem when parse field path: " + field + ", this path does not exist. " + e.getMessage(), e);
		}
		Operator operator = filter.getOperator();
		Object value = filter.getValue();
		try {
			logger.debug("Working on root {}, field {}, java type {}, operator {}, value {}", root.getJavaType(), field,
					path.getJavaType(), operator, value);
			return doGetPredicate(cb, path, operator, value, ignoreCase);
		} catch (Exception e) {
			throw new SpecificationException(
					"Unable to filter by: " + filter + ", value type:" + value.getClass() + ", operator: " + operator
							+ ", entity type:" + path.getJavaType() + ", message: " + e.getMessage(),
					e);
		}
	}

	private Predicate doGetPredicate(CriteriaBuilder cb, Path<?> path, Operator operator, Object value,
			Boolean ignoreCase) {
		Predicate p = null;
		// look at Hibernate Mapping types
		// we only support primitive types and data/time types
		if (!(value instanceof Comparable || value instanceof Collection)) {
			throw new IllegalStateException(
					"This library only support primitive types and date/time types in the list: "
							+ "Integer, Long, Double, Float, Short, BigDecimal, Character, String, Byte, Boolean"
							+ ", Date, Time, TimeStamp, Calendar");
		}

		switch (operator) {
		/*
		 * Operator for Comparable type
		 */
		case EQUAL:
			value = parseValue(path, value);
			p = equal(cb, path, value, ignoreCase);
			break;
		case NOT_EQUAL:
			value = parseValue(path, value);
			p = notEqual(cb, path, value, ignoreCase);
			break;
		/*
		 * Operator for any type
		 */
		case IS_NULL:
			p = cb.isNull(path);
			break;
		case IS_NOT_NULL:
			p = cb.isNotNull(path);
			break;
		/*
		 * Operator for String type
		 */
		case IS_EMPTY:
			p = cb.equal(path, "");
			break;
		case IS_NOT_EMPTY:
			p = cb.notEqual(path, "");
			break;
		case CONTAINS:
			p = contains(cb, path, value, ignoreCase);
			break;
		case NOT_CONTAINS:
			p = notContains(cb, path, value, ignoreCase);
			break;
		case STARTS_WITH:
			p = startsWith(cb, path, value, ignoreCase);
			break;
		case ENDS_WITH:
			p = endsWith(cb, path, value, ignoreCase);
			break;
		/*
		 * Operator for Comparable type; does not support Calendar
		 */
		case GREATER_THAN:
			value = parseValue(path, value);
			if (value instanceof Date) {
				p = cb.greaterThan(path.as(Date.class), (Date) (value));
			} else {
				p = greaterThan(cb, path, value, ignoreCase);
			}
			break;
		case GREATER_THAN_OR_EQUAL:
			value = parseValue(path, value);
			if (value instanceof Date) {
				p = cb.greaterThanOrEqualTo(path.as(Date.class), (Date) (value));
			} else {
				p = greaterThanOrEqualTo(cb, path, value, ignoreCase);
			}
			break;
		case LESS_THAN:
			value = parseValue(path, value);
			if (value instanceof Date) {
				p = cb.lessThan(path.as(Date.class), (Date) (value));
			} else {
				p = lessThan(cb, path, value, ignoreCase);
			}
			break;
		case LESS_THAN_OR_EQUAL:
			value = parseValue(path, value);
			if (value instanceof Date) {
				p = cb.lessThanOrEqualTo(path.as(Date.class), (Date) (value));
			} else {
				p = lessThanOrEqualTo(cb, path, value, ignoreCase);
			}
			break;
		case IN:
			p = path.in(value);
			break;
		default:
			logger.error("unknown operator: {}", operator);
			throw new IllegalStateException("unknown operator: " + operator);
		}
		return p;
	}

	private Predicate equal(CriteriaBuilder cb, Path<?> path, Object value, Boolean ignoreCase) { // NOSONAR
		if (Boolean.TRUE.equals(ignoreCase))
			return cb.equal(cb.upper(path.as(String.class)), value.toString().toUpperCase());
		else
			return cb.equal(path, value);
	}

	private Predicate notEqual(CriteriaBuilder cb, Path<?> path, Object value, Boolean ignoreCase) {
		if (Boolean.TRUE.equals(ignoreCase))
			return cb.notEqual(cb.upper(path.as(String.class)), value.toString().toUpperCase());
		else
			return cb.notEqual(path, value);
	}

	private Predicate contains(CriteriaBuilder cb, Path<?> path, Object value, Boolean ignoreCase) {
		if (Boolean.TRUE.equals(ignoreCase))
			return cb.like(cb.upper(path.as(String.class)), "%" + value.toString().toUpperCase() + "%");
		else
			return cb.like(path.as(String.class), "%" + value + "%");
	}

	private Predicate notContains(CriteriaBuilder cb, Path<?> path, Object value, Boolean ignoreCase) {
		if (Boolean.TRUE.equals(ignoreCase))
			return cb.notLike(cb.upper(path.as(String.class)), "%" + value.toString().toUpperCase() + "%");
		else
			return cb.notLike(path.as(String.class), "%" + value + "%");
	}

	private Predicate startsWith(CriteriaBuilder cb, Path<?> path, Object value, Boolean ignoreCase) {
		if (Boolean.TRUE.equals(ignoreCase))
			return cb.like(cb.upper(path.as(String.class)), value.toString().toUpperCase() + "%");
		else
			return cb.like(path.as(String.class), value + "%");
	}

	private Predicate endsWith(CriteriaBuilder cb, Path<?> path, Object value, Boolean ignoreCase) {
		if (Boolean.TRUE.equals(ignoreCase))
			return cb.like(cb.upper(path.as(String.class)), "%" + value.toString().toUpperCase());
		else
			return cb.like(path.as(String.class), "%" + value);
	}

	private Predicate greaterThan(CriteriaBuilder cb, Path<?> path, Object value, Boolean ignoreCase) {
		if (Boolean.TRUE.equals(ignoreCase))
			return cb.greaterThan(cb.upper(path.as(String.class)), value.toString().toUpperCase());
		else
			return cb.greaterThan(path.as(String.class), value.toString());
	}

	private Predicate greaterThanOrEqualTo(CriteriaBuilder cb, Path<?> path, Object value, Boolean ignoreCase) {
		if (Boolean.TRUE.equals(ignoreCase))
			return cb.greaterThanOrEqualTo(cb.upper(path.as(String.class)), value.toString().toUpperCase());
		else
			return cb.greaterThanOrEqualTo(path.as(String.class), value.toString());
	}

	private Predicate lessThan(CriteriaBuilder cb, Path<?> path, Object value, Boolean ignoreCase) {
		if (Boolean.TRUE.equals(ignoreCase))
			return cb.lessThan(cb.upper(path.as(String.class)), value.toString().toUpperCase());
		else
			return cb.lessThan(path.as(String.class), value.toString());
	}

	private Predicate lessThanOrEqualTo(CriteriaBuilder cb, Path<?> path, Object value, Boolean ignoreCase) {
		if (Boolean.TRUE.equals(ignoreCase))
			return cb.lessThanOrEqualTo(cb.upper(path.as(String.class)), value.toString().toUpperCase());
		else
			return cb.lessThanOrEqualTo(path.as(String.class), value.toString());
	}

	private Object parseValue(Path<?> path, Object value) {
		if (Date.class.isAssignableFrom(path.getJavaType())) {
			try {
				SimpleDateFormat tmpDateFormat = this.dateFormat != null ? this.dateFormat : defaultDateFormat;
				value = tmpDateFormat.parse(value.toString());
			} catch (ParseException e) {
				throw new SpecificationException("Illegal date format: " + value + ", required format is "
						+ (this.dateFormat != null ? this.dateFormat : defaultDateFormat).toPattern());
			}
		}
		return value;
	}

	private Path<?> parsePath(Path<?> path, String field) {
		if (!field.contains(Filter.PATH_DELIMITER)) {
			return path.get(field);
		}
		int i = field.indexOf(Filter.PATH_DELIMITER);
		String firstPart = field.substring(0, i);
		String secondPart = field.substring(i + 1, field.length());
		Path<?> p = path.get(firstPart);
		if (Iterable.class.isAssignableFrom(p.getJavaType()) && p instanceof PluralAttributePath) {
			PluralAttributePath<?> pluralAttributePath = (PluralAttributePath<?>) p;
			JoinType tmpJoinType = this.joinType != null ? this.joinType : defaultJoinType;
			if (join == null)
				join = root.join(pluralAttributePath.getAttribute().getName(), tmpJoinType);
			else
				join = join.join(pluralAttributePath.getAttribute().getName(), tmpJoinType);
			return parsePath(join, secondPart);
		}
		return parsePath(p, secondPart);
	}

	public void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}
}
