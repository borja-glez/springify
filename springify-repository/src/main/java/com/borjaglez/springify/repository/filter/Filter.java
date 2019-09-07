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

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Jon (Zhongjun Tian)
 * @author Borja González Enríquez
 */
public class Filter {

	// delimiter for crossing table search
	public static final String PATH_DELIMITER = ".";
	public static final String LEFT_BRACKET = "(";
	public static final String RIGHT_BRACKET = ")";

	String field;
	Operator operator;
	Object value;
	Logic logic;
	Boolean ignoreCase = false;
	List<Filter> filters;

	public Filter() {
	}

	public Filter(String field, Operator operator, Object value) {
		this.field = field;
		this.operator = operator;
		this.value = value;
	}

	public Filter(Logic logic, Filter... filters) {
		this.logic = logic;
		this.filters = Arrays.asList(filters);
	}

	public static Filter and(Filter filter) {
		return new Filter(Logic.AND, filter);
	}

	public static Filter or(Filter filter) {
		return new Filter(Logic.OR, filter);
	}

	public static Filter and(Filter... filters) {
		return new Filter(Logic.AND, filters);
	}

	public static Filter or(Filter... filters) {
		return new Filter(Logic.OR, filters);
	}

	public Logic getLogic() {
		return logic;
	}

	public void setLogic(Logic logic) {
		this.logic = logic;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public List<Filter> getFilters() {
		return filters;
	}

	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}
	
	public Boolean getIgnoreCase() {
		return ignoreCase;
	}
	
	public void setIgnoreCase(Boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public enum Logic {
		AND("and"), OR("or");

		private String value;

		private Logic(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}
	}

	public enum Operator {
		EQUAL("eq"), NOT_EQUAL("neq"), IS_NULL("isnull"), IS_NOT_NULL("isnotnull"), IS_EMPTY("isempty"),
		IS_NOT_EMPTY("isnotempty"), CONTAINS("contains"), NOT_CONTAINS("notcontains"), STARTS_WITH("startswith"),
		ENDS_WITH("endswith"), GREATER_THAN("gt"), LESS_THAN("lt"), GREATER_THAN_OR_EQUAL("gte"),
		LESS_THAN_OR_EQUAL("lte"), IN("in");

		private String value;

		private Operator(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}
	}
}
