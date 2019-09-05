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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

/**
 * @author Jon (Zhongjun Tian)
 * @author Borja González Enríquez
 */
@SuppressWarnings("serial")
public class SpecificationImpl<T> implements Specification<T> {
	private List<Specification<T>> specifications = new LinkedList<>();

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<>();
		for (Specification<T> specification : specifications) {
			Predicate p = specification.toPredicate(root, query, criteriaBuilder);
			if (p != null)
				predicates.add(p);
		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}

	public void add(Specification<T> specification) {
		specifications.add(specification);
	}
}
