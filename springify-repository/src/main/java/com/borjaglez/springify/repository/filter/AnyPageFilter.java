package com.borjaglez.springify.repository.filter;

import java.util.LinkedList;
import java.util.List;

import com.borjaglez.springify.repository.filter.Filter.Logic;
import com.borjaglez.springify.repository.filter.Filter.Operator;
import com.borjaglez.springify.utils.MatcherUtils;

public class AnyPageFilter extends AbstractPageFilter {

	private String value = "";
	private List<AnyField> fields;
	private Boolean ignoreCase = true;

	public AnyPageFilter() {
		super();
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
		return new Filter(Logic.AND, filters);
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
