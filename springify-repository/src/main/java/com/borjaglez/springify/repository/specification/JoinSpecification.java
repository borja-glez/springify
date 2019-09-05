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

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;

/**
 * Created by zhongjun on 6/18/17.
 * 
 * @author Jon (Zhongjun Tian)
 * @author Borja González Enríquez
 */
@SuppressWarnings("serial")
public class JoinSpecification<T> implements Specification<T> {
	transient List<String> leftJoinFetchTables;
	transient List<String> innnerJoinFetchTables;
	transient List<String> rightJoinFetchTables;

	public JoinSpecification() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		if (isCountCriteriaQuery(cq))
			return null;

		join(root, leftJoinFetchTables, JoinType.LEFT);
		join(root, innnerJoinFetchTables, JoinType.INNER);
		join(root, rightJoinFetchTables, JoinType.RIGHT);
		((CriteriaQuery<T>) cq).select(root);
		return null;
	}

	private boolean isCountCriteriaQuery(CriteriaQuery<?> cq) {
		return cq.getResultType().toString().contains("java.lang.Long");
	}

	private void join(Root<T> root, List<String> joinFetchTables, JoinType type) {
		if (joinFetchTables != null && (joinFetchTables.isEmpty())) {
			for (String table : joinFetchTables) {
				if (table != null)
					root.fetch(table, type);
			}
		}
	}

	public JoinSpecification<T> setLeftJoinFetchTables(List<String> leftJoinFetchTables) {
		this.leftJoinFetchTables = leftJoinFetchTables;
		return this;
	}

	public JoinSpecification<T> setInnerJoinFetchTables(List<String> innerJoinFetchTables) {
		this.innnerJoinFetchTables = innerJoinFetchTables;
		return this;
	}

	public JoinSpecification<T> setRightJoinFetchTables(List<String> rightJoinFetchTables) {
		this.rightJoinFetchTables = rightJoinFetchTables;
		return this;
	}
}
