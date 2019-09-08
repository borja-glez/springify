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
import com.borjaglez.springify.utils.MatcherUtils;

/**
 * @author Borja González Enríquez
 */
public class AdvancedPageFilter extends BasicPageFilter {

	public AdvancedPageFilter() {
		super();
	}

	@Override
	public Filter toFilter() {
		if (getFields() == null || getFields().isEmpty()) {
			return null;
		}
		List<Filter> filters = new LinkedList<>();
		List<String> values = MatcherUtils.matcherQuoteString(getValue());
		for (String value : values) {
			List<Filter> filtersValue = new LinkedList<>();
			for (String field : getFields()) {
				Filter f = new Filter(field, getOperator(), value);
				f.setIgnoreCase(getIgnoreCase());
				filtersValue.add(f);
			}
			filters.add(new Filter(Logic.OR, filtersValue));

		}
		return new Filter(Logic.AND, filters);
	}
}
