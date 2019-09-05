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

import java.util.Arrays;
import java.util.List;

/**
 * @author Jon (Zhongjun Tian)
 * @author Borja González Enríquez
 */
public class Filter {
	// Logic
	public static final String AND = "and";
	public static final String OR = "or";
	
	// Operators
	public static final String EQUAL = "eq";
	public static final String NOT_EQUAL = "neq";
	public static final String IS_NULL = "isnull";
	public static final String IS_NOT_NULL = "isnotnull";
	public static final String IS_EMPTY = "isempty";
	public static final String IS_NOT_EMPTY = "isnotempty";
	public static final String CONTAINS = "contains";
	public static final String NOT_CONTAINS = "doesnotcontain";
	public static final String START_WITH = "startswith";
	public static final String END_WITH = "endswith";
	public static final String GREATER_THAN = "gt";
	public static final String LESS_THAN = "lt";
	public static final String GREATER_THAN_OR_EQUAL = "gte";
	public static final String LESS_THAN_OR_EQUAL = "lte";
	public static final String IN = "in";
	
	// Sorting
	public static final String ASC = "asc";
	public static final String DESC = "desc";

	// delimiter for crossing table search
	public static final String PATH_DELIMITER = ".";
	public static final String LEFT_BRACKET = "(";
	public static final String RIGHT_BRACKET = ")";

	String field;
	String operator;
	Object value;
	String logic;
	String sort;
	Integer sortOrder;
	boolean group;
	List<Filter> filters;

	public Filter() {
	}

	public Filter(String field, String operator, Object value) {
		this.field = field;
		this.operator = operator;
		this.value = value;
	}

	public Filter(String logic, Filter... filters) {
		this.logic = logic;
		this.filters = Arrays.asList(filters);
	}

	public Filter and(Filter filters) {
		return new Filter(AND, filters);
	}

	public Filter or(Filter filters) {
		return new Filter(OR, filters);
	}

	public static Filter and(Filter... filters) {
		return new Filter(AND, filters);
	}

	public static Filter or(Filter... filters) {
		return new Filter(OR, filters);
	}

	public String getLogic() {
		return logic;
	}

	public void setLogic(String logic) {
		this.logic = logic;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	public String getSort() {
		return sort;
	}
	
	public void setSort(String sort) {
		this.sort = sort;
	}
	
	public Integer getSortOrder() {
		return sortOrder;
	}
	
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	public boolean isGroup() {
		return group;
	}
	
	public void setGroup(boolean group) {
		this.group = group;
	}

	public List<Filter> getFilters() {
		return filters;
	}

	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}
}
