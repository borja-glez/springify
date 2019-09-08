package com.borjaglez.springify.repository.filter;

import java.util.LinkedList;
import java.util.List;

import com.borjaglez.springify.repository.filter.Filter.Logic;
import com.borjaglez.springify.utils.MatcherUtils;

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
