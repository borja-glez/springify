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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;

import com.borjaglez.springify.repository.filter.Filter;
import com.borjaglez.springify.repository.filter.Filter.Operator;
import com.borjaglez.springify.repository.filter.PageFilter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Jon (Zhongjun Tian)
 * @author Borja González Enríquez
 */
public class SpecificationBuilder<T> {
	private JpaSpecificationExecutor<T> repository;
	private SpecificationImpl<T> specification;

	public static <T, R extends JpaRepository<T, ?> & JpaSpecificationExecutor<T>> SpecificationBuilder<T> selectFrom(
			R repository) {
		SpecificationBuilder<T> builder = new SpecificationBuilder<>();
		builder.repository = repository;
		builder.specification = new SpecificationImpl<>();
		return builder;
	}

	public static <T, R extends JpaRepository<T, ?> & JpaSpecificationExecutor<T>> SpecificationBuilder<T> selectDistinctFrom(
			R repository) {
		SpecificationBuilder<T> builder = selectFrom(repository);
		builder.distinct();
		return builder;
	}

	private SpecificationBuilder<T> distinct() {
		specification.add(((root, criteriaQuery, criteriaBuilder) -> criteriaQuery.distinct(true).getRestriction()));
		return this;
	}

	public SpecificationBuilder<T> where(Filter filter) {
		if (this.repository == null) {
			throw new IllegalStateException("Did not specify which repository, please use from() before where()");
		}
		specification.add(new WhereSpecification<T>(filter));
		return this;
	}

	public SpecificationBuilder<T> where(PageFilter pageFilter) {
		if (this.repository == null) {
			throw new IllegalStateException("Did not specify which repository, please use from() before where()");
		}
		specification.add(new WhereSpecification<T>(pageFilter.getFilter()));
		return this;
	}

	public SpecificationBuilder<T> where(String field, Operator operator, Object value) {
		Filter filter = new Filter(field, operator, value);
		return where(filter);
	}

	public SpecificationBuilder<T> leftJoin(String... tables) {
		specification.add(new JoinSpecification<T>().setLeftJoinFetchTables(Arrays.asList(tables)));
		return this;
	}

	public SpecificationBuilder<T> innerJoin(String... tables) {
		specification.add(new JoinSpecification<T>().setInnerJoinFetchTables(Arrays.asList(tables)));
		return this;
	}

	public SpecificationBuilder<T> rightJoin(String... tables) {
		specification.add(new JoinSpecification<T>().setRightJoinFetchTables(Arrays.asList(tables)));
		return this;
	}

	public SpecificationBuilder<T> groupBy(String... fields) {
		specification.add(((root, criteriaQuery, criteriaBuilder) -> criteriaQuery
				.groupBy(Arrays.asList(fields).stream().map(root::get).collect(Collectors.toList()))
				.getRestriction()));
		return this;
	}

	public Long count() {
		return repository.count(specification);
	}

	public Optional<T> findOne() {
		return repository.findOne(specification);
	}

	public List<T> findAll() {
		return repository.findAll(specification);
	}

	public Page<T> findAll(Pageable page) {
		return repository.findAll(specification, page);
	}

	public List<T> findAll(Sort sort) {
		return repository.findAll(specification, sort);
	}

	public Page<T> findAll(@NonNull PageFilter pageFilter) {
		if (isInValidPageFilter(pageFilter)) {
			throw new IllegalArgumentException("Page Filter require ar least the page index and the page size.");
		}
		if (pageFilter.getPageIndex() < 0) {
			throw new IllegalArgumentException("Page index must not be less than zero!");
		}
		if (pageFilter.getPageSize() < 1) {
			throw new IllegalArgumentException("Page size must not be less than one!");
		}
		return findAll(pageFilter.toPageable());
	}

	private boolean isInValidPageFilter(PageFilter pageFilter) {
		return pageFilter == null || pageFilter.getPageIndex() == null || pageFilter.getPageSize() == null;
	}
}
