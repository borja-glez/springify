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

import java.util.LinkedList;
import java.util.List;

import com.borjaglez.springify.repository.filter.Filter.Logic;
import com.borjaglez.springify.repository.filter.Filter.Operator;
import com.borjaglez.springify.utils.MatcherUtils;

/**
 * @author Borja González Enríquez
 */
public class AnyPageFilter extends AbstractPageFilter {

	private String value = "";
	private List<AnyField> fields;
	private Boolean ignoreCase = true;

	public AnyPageFilter() {
		super();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<AnyField> getFields() {
		return fields;
	}

	public void setFields(List<AnyField> fields) {
		this.fields = fields;
	}

	public Boolean getIgnoreCase() {
		return ignoreCase;
	}

	public void setIgnoreCase(Boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}
	
	@Override
	public Filter toFilter() {
		if (fields == null || getFields().isEmpty()) {
			return null;
		}
		List<Filter> filters = new LinkedList<>();
		List<String> values = MatcherUtils.matcherQuoteString(value);
		for (String v : values) {
			List<Filter> filtersValue = new LinkedList<>();
			for (AnyField field : fields) {
				Filter f = new Filter(field.getField(), field.getOperator(), v);
				f.setIgnoreCase(getIgnoreCase());
				filtersValue.add(f);
			}
			filters.add(new Filter(Logic.OR, filtersValue));

		}
		if(fields != null && fields.size() <= 1) {
			return new Filter(Logic.OR, filters);
		}
		return new Filter(Logic.AND, filters);
	}

	public static class AnyField {
		private String field;
		private Operator operator = Operator.CONTAINS;

		public AnyField() {
			super();
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
	}
}
