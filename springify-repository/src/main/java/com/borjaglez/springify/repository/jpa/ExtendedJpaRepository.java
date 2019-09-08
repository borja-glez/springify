package com.borjaglez.springify.repository.jpa;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;

import com.borjaglez.springify.repository.filter.AbstractPageFilter;
import com.borjaglez.springify.repository.filter.Filter;
import com.borjaglez.springify.repository.filter.Filter.Operator;

@NoRepositoryBean
public interface ExtendedJpaRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> { // NOSONAR

	ExtendedJpaRepository<T, ID> select();

	ExtendedJpaRepository<T, ID> distinct();

	ExtendedJpaRepository<T, ID> where(Filter filter);

	ExtendedJpaRepository<T, ID> where(AbstractPageFilter pageFilter);

	ExtendedJpaRepository<T, ID> where(String field, Operator operator, Object value);

	ExtendedJpaRepository<T, ID> leftJoin(String... tables);

	ExtendedJpaRepository<T, ID> innerJoin(String... tables);

	ExtendedJpaRepository<T, ID> rightJoin(String... tables);

	ExtendedJpaRepository<T, ID> groupBy(String... fields);
	
	public Optional<T> findOne();
	
	public Page<T> findAll(@NonNull AbstractPageFilter pageFilter);
}
