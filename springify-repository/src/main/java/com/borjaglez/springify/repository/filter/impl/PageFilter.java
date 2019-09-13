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
package com.borjaglez.springify.repository.filter.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.StringUtils;

import com.borjaglez.springify.repository.filter.Direction;
import com.borjaglez.springify.repository.filter.IPageFilter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author borjaglez
 */
public class PageFilter implements IPageFilter {

	Integer pageNumber;
	Integer pageSize;
	Object order;
	Filter filter;

	public PageFilter() {
		super();
	}

	public PageFilter(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public PageFilter(Integer pageNumber, Integer pageSize) {
		this(pageNumber);
		this.pageSize = pageSize;
	}

	public PageFilter(Integer pageNumber, Integer pageSize, Object order) {
		this(pageNumber, pageSize);
		this.order = order;
	}

	public PageFilter(Integer pageNumber, Integer pageSize, Object order, Filter filter) {
		this(pageNumber, pageSize, order);
		this.filter = filter;
	}

	@Override
	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	@Override
	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Object getOrder() {
		return order;
	}

	public void setOrder(Object order) {
		this.order = order;
	}

	public Filter getFilter() {
		return filter;
	}

	@Override
	public Filter toFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public static class OrderFilter {
		private String field;
		private Direction direction = Direction.ASC;

		public OrderFilter() {
			super();
		}

		public OrderFilter(String field) {
			this.field = field;
		}

		public OrderFilter(String field, Direction direction) {
			this.field = field;
			this.direction = direction;
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
				throw new IllegalArgumentException("[sortFilters] -> field must not be null or empty!");

			}
			return new Order(org.springframework.data.domain.Sort.Direction.fromString(direction.getValue()), field);
		}
	}

	public Pageable toPageable() {
		Pageable result = Pageable.unpaged();
		Sort sort = Sort.unsorted();
		if (order != null) {
			if (order instanceof Iterable) {
				List<OrderFilter> orderFilters = new Gson().fromJson(order.toString(),
						new TypeToken<List<OrderFilter>>() {
						}.getType());
				sort = org.springframework.data.domain.Sort
						.by(orderFilters.stream().map(OrderFilter::toOrder).collect(Collectors.toList()));
			} else if (order instanceof Map) {
				OrderFilter orderFilter = new Gson().fromJson(order.toString(), OrderFilter.class);
				sort = org.springframework.data.domain.Sort.by(orderFilter.toOrder());
			} else if (order instanceof String) {
				sort = org.springframework.data.domain.Sort.by(order.toString());
			}
		}
		if (pageNumber != null && pageSize != null) {
			result = PageRequest.of(pageNumber, pageSize, sort);
		}
		return result;
	}
}
