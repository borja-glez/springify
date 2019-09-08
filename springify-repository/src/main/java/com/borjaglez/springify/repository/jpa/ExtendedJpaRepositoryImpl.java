package com.borjaglez.springify.repository.jpa;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import com.borjaglez.springify.repository.filter.AbstractPageFilter;
import com.borjaglez.springify.repository.filter.Filter;
import com.borjaglez.springify.repository.filter.Filter.Operator;
import com.borjaglez.springify.repository.specification.JoinSpecification;
import com.borjaglez.springify.repository.specification.SpecificationImpl;
import com.borjaglez.springify.repository.specification.WhereSpecification;

public class ExtendedJpaRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> // NOSONAR
		implements ExtendedJpaRepository<T, ID> {

	private SpecificationImpl<T> specification;
	@SuppressWarnings("unused")
	private final EntityManager em;

	public ExtendedJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.em = entityManager;
	}

	public ExtendedJpaRepositoryImpl(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		this.em = em;
	}

	@Override
	@Transactional
	public ExtendedJpaRepository<T, ID> select() {
		specification = new SpecificationImpl<>();
		return this;
	}

	@Override
	@Transactional
	public ExtendedJpaRepository<T, ID> distinct() {
		specification = new SpecificationImpl<>();
		specification.add(((root, criteriaQuery, criteriaBuilder) -> criteriaQuery.distinct(true).getRestriction()));
		return this;
	}

	@Override
	@Transactional
	public ExtendedJpaRepository<T, ID> where(Filter filter) {
		if (this.specification == null) {
			throw new IllegalStateException("Use from() before where()");
		}
		specification.add(new WhereSpecification<T>(filter));
		return this;
	}

	@Override
	@Transactional
	public ExtendedJpaRepository<T, ID> where(AbstractPageFilter pageFilter) {
		if (this.specification == null) {
			throw new IllegalStateException("Use from() before where()");
		}
		specification.add(new WhereSpecification<T>(pageFilter.toFilter()));
		return this;
	}

	@Override
	@Transactional
	public ExtendedJpaRepository<T, ID> where(String field, Operator operator, Object value) {
		Filter filter = new Filter(field, operator, value);
		return where(filter);
	}

	@Override
	@Transactional
	public ExtendedJpaRepository<T, ID> leftJoin(String... tables) {
		specification.add(new JoinSpecification<T>().setLeftJoinFetchTables(Arrays.asList(tables)));
		return this;
	}

	@Override
	@Transactional
	public ExtendedJpaRepository<T, ID> innerJoin(String... tables) {
		specification.add(new JoinSpecification<T>().setInnerJoinFetchTables(Arrays.asList(tables)));
		return this;
	}

	@Override
	@Transactional
	public ExtendedJpaRepository<T, ID> rightJoin(String... tables) {
		specification.add(new JoinSpecification<T>().setRightJoinFetchTables(Arrays.asList(tables)));
		return this;
	}

	@Override
	@Transactional
	public ExtendedJpaRepository<T, ID> groupBy(String... fields) {
		specification.add(((root, criteriaQuery, criteriaBuilder) -> criteriaQuery
				.groupBy(Arrays.asList(fields).stream().map(root::get).collect(Collectors.toList())).getRestriction()));
		return this;
	}

	@Override
	public long count() {
		return super.count(specification);
	}

	@Override
	@Transactional
	public Optional<T> findOne() {
		return super.findOne(specification);
	}

	@Override
	public List<T> findAll() {
		return super.findAll(specification);
	}

	@Override
	public Page<T> findAll(Pageable page) {
		return super.findAll(specification, page);
	}

	@Override
	public List<T> findAll(Sort sort) {
		return super.findAll(specification, sort);
	}

	@Override
	@Transactional
	public Page<T> findAll(@NonNull AbstractPageFilter pageFilter) {
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

	private boolean isInValidPageFilter(AbstractPageFilter pageFilter) {
		return pageFilter == null || pageFilter.getPageIndex() == null || pageFilter.getPageSize() == null;
	}
}
