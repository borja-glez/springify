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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.borjaglez.springify.repository.filter.Filter;
import com.borjaglez.springify.repository.specification.exception.SpecificationException;

import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhongjun on 6/18/17.
 * @author Borja González Enríquez
 */
@SuppressWarnings("serial")
public class WhereSpecification<T> implements Specification<T> {
	private static Logger logger = LoggerFactory.getLogger(WhereSpecification.class);
	private SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private SimpleDateFormat dateFormat;
	private transient Filter filter;

	public WhereSpecification(Filter filter) {
		this.filter = filter;
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		return getPredicate(filter, root, cb);
	}

	private Predicate getPredicate(Filter filter, Path<T> root, CriteriaBuilder cb) {
		if (isInValidFilter(filter))
			return null;
		if (filter.getLogic() == null) {// one filter
			return doGetPredicate(filter, root, cb);
		} else {// logic filters
			if (filter.getLogic().equals(Filter.AND)) {
				Predicate[] predicates = getPredicateList(filter, root, cb);
				return cb.and(predicates);
			} else if (filter.getLogic().equals(Filter.OR)) {
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
		Path<T> path = null;
		try {
			logger.debug("Parsing child path {} of root {}", field, root.getJavaType());
			path = parsePath(root, field);
		} catch (Exception e) {
			throw new SpecificationException(
					"Met problem when parse field path: " + field + ", this path does not exist. " + e.getMessage(), e);
		}
		String operator = filter.getOperator();
		Object value = filter.getValue();
		try {
			logger.debug("Working on root {}, field {}, java type {}, operator {}, value {}", root.getJavaType(), field,
					path.getJavaType(), operator, value);
			return doGetPredicate(cb, path, operator, value);
		} catch (Exception e) {
			throw new SpecificationException(
					"Unable to filter by: " + filter + ", value type:" + value.getClass() + ", operator: " + operator
							+ ", entity type:" + path.getJavaType() + ", message: " + e.getMessage(),
					e);
		}
	}

	private Predicate doGetPredicate(CriteriaBuilder cb, Path<T> path, String operator, Object value) {
		Predicate p = null;
		// look at Hibernate Mapping types
		// we only support primitive types and data/time types
		if (!(value instanceof Comparable || value instanceof Collection)) {
			throw new IllegalStateException(
					"This library only support primitive types and date/time types in the list: "
							+ "Integer, Long, Double, Float, Short, BidDecimal, Character, String, Byte, Boolean"
							+ ", Date, Time, TimeStamp, Calendar");
		}

		switch (operator) {
		/*
		 * Operator for Comparable type
		 */
		case Filter.EQUAL:
			value = parseValue(path, value);
			p = cb.equal(path, (value));
			break;
		case Filter.NOT_EQUAL:
			value = parseValue(path, value);
			p = cb.notEqual(path, (value));
			break;
		/*
		 * Operator for any type
		 */
		case Filter.IS_NULL:
			p = cb.isNull(path);
			break;
		case Filter.IS_NOT_NULL:
			p = cb.isNotNull(path);
			break;
		/*
		 * Operator for String type
		 */
		case Filter.IS_EMPTY:
			p = cb.equal(path, "");
			break;
		case Filter.IS_NOT_EMPTY:
			p = cb.notEqual(path, "");
			break;
		case Filter.CONTAINS:
			p = cb.like(path.as(String.class), "%" + value + "%");
			break;
		case Filter.NOT_CONTAINS:
			p = cb.notLike(path.as(String.class), "%" + value + "%");
			break;
		case Filter.START_WITH:
			p = cb.like(path.as(String.class), value + "%");
			break;
		case Filter.END_WITH:
			p = cb.like(path.as(String.class), "%" + value);
			break;
		/*
		 * Operator for Comparable type; does not support Calendar
		 */
		case Filter.GREATER_THAN:
			value = parseValue(path, value);
			if (value instanceof Date) {
				p = cb.greaterThan(path.as(Date.class), (Date) (value));
			} else {
				p = cb.greaterThan(path.as(String.class), (value).toString());
			}
			break;
		case Filter.GREATER_THAN_OR_EQUAL:
			value = parseValue(path, value);
			if (value instanceof Date) {
				p = cb.greaterThanOrEqualTo(path.as(Date.class), (Date) (value));
			} else {
				p = cb.greaterThanOrEqualTo(path.as(String.class), (value).toString());
			}
			break;
		case Filter.LESS_THAN:
			value = parseValue(path, value);
			if (value instanceof Date) {
				p = cb.lessThan(path.as(Date.class), (Date) (value));
			} else {
				p = cb.lessThan(path.as(String.class), (value).toString());
			}
			break;
		case Filter.LESS_THAN_OR_EQUAL:
			value = parseValue(path, value);
			if (value instanceof Date) {
				p = cb.lessThanOrEqualTo(path.as(Date.class), (Date) (value));
			} else {
				p = cb.lessThanOrEqualTo(path.as(String.class), (value).toString());
			}
			break;
		case Filter.IN:
			p = path.in(value);
			break;
		default:
			logger.error("unknown operator: {}", operator);
			throw new IllegalStateException("unknown operator: " + operator);
		}
		return p;
	}

	private Object parseValue(Path<T> path, Object value) {
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

	private Path<T> parsePath(Path<? extends T> root, String field) {
		if (!field.contains(Filter.PATH_DELIMITER)) {
			return root.get(field);
		}
		int i = field.indexOf(Filter.PATH_DELIMITER);
		String firstPart = field.substring(0, i);
		String secondPart = field.substring(i + 1, field.length());
		Path<T> p = root.get(firstPart);
		return parsePath(p, secondPart);
	}

	public void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}
}
