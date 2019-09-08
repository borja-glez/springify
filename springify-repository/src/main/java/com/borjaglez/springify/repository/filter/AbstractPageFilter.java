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
package com.borjaglez.springify.repository.filter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Borja González Enríquez
 */
public abstract class AbstractPageFilter {

	Integer pageIndex;
	Integer pageSize;
	List<SortFilter> sortFilters;

	public AbstractPageFilter() {
		super();
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public List<SortFilter> getSortFilters() {
		return sortFilters;
	}

	public void setSortFilters(List<SortFilter> sortFilters) {
		this.sortFilters = sortFilters;
	}

	public Pageable toPageable() {
		if(sortFilters == null || sortFilters.isEmpty()) {
			return PageRequest.of(pageIndex, pageSize);
		}
		return PageRequest.of(pageIndex, pageSize, org.springframework.data.domain.Sort
				.by(sortFilters.stream().map(SortFilter::toOrder).collect(Collectors.toList())));
	}
	
	public abstract Filter toFilter();

	public static class SortFilter {
		private String field;
		private Direction direction = Direction.ASC;

		public SortFilter() {
			super();
		}

		public String getField() {
			return field;
		}

		public void setField(String field) {
			this.field = field;
		}

		public Direction getDirection() {
			return direction;
		}

		public void setDirection(Direction direction) {
			this.direction = direction;
		}

		public Order toOrder() {
			if (!StringUtils.hasText(field)) {
				throw new IllegalArgumentException("[sortFilters] -> field must not null or empty!");

			}
			return new Order(org.springframework.data.domain.Sort.Direction.fromString(direction.getValue()), field);
		}
	}

	public enum Direction {
		ASC("asc"), DESC("desc");

		private String value;

		private Direction(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}
	}
}
