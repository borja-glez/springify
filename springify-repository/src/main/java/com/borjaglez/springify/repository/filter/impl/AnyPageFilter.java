package com.borjaglez.springify.repository.filter.impl;

import java.util.LinkedList;
import java.util.List;

import com.borjaglez.springify.repository.filter.Logic;
import com.borjaglez.springify.repository.filter.Operator;
import com.borjaglez.springify.utils.MatcherUtils;

public class AnyPageFilter extends PageFilter {

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
			filters.add(new Filter(Logic.OR, filtersValue.toArray(new Filter[filtersValue.size()])));

		}
		if (fields != null && fields.size() <= 1) {
			return new Filter(Logic.OR, filters.toArray(new Filter[filters.size()]));
		}
		return new Filter(Logic.AND, filters.toArray(new Filter[filters.size()]));
	}

	public static class AnyField {
		private String field;
		private Operator operator = Operator.CONTAINS;

		public AnyField() {
			super();
		}

		public AnyField(String field) {
			this.field = field;
		}

		public AnyField(String field, Operator operator) {
			this(field);
			this.operator = operator;
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
