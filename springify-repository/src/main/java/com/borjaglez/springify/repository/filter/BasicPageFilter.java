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

/**
 * @author Borja González Enríquez
 */
public class BasicPageFilter extends AbstractPageFilter {

	private String value = "";
	private List<String> fields;
	private Logic logic = Logic.OR;
	private Operator operator = Operator.CONTAINS;
	private Boolean ignoreCase = true;

	public BasicPageFilter() {
		super();
	}

	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public Logic getLogic() {
		return logic;
	}

	public void setLogic(Logic logic) {
		this.logic = logic;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}
	
	public Boolean getIgnoreCase() {
		return ignoreCase;
	}
	
	public void setIgnoreCase(Boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	@Override
	public Filter toFilter() {
		if(fields == null || fields.isEmpty()) {
			return null;
		}
		List<Filter> filters = new LinkedList<>();
		for (String field : fields) {
			Filter f = new Filter(field, operator, value);
			f.setIgnoreCase(ignoreCase);
			filters.add(f);
		}
		return new Filter(logic, filters);
	}
}
