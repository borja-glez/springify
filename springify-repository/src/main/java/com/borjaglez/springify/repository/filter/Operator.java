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

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author borjaglez
 */
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