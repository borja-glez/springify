package com.borjaglez.springify.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherUtils {

	private static final Pattern QUOTE_STRING_REGEX = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"");

	private MatcherUtils() {
		super();
	}

	public static List<String> matcherQuoteString(CharSequence input) {
		List<String> matcherList = new LinkedList<>();
		Matcher regexMatcher = QUOTE_STRING_REGEX.matcher(input);
		while (regexMatcher.find()) {
			if (regexMatcher.group(1) != null) {
				// Add double-quoted string without the quotes
				matcherList.add(regexMatcher.group(1));
			} else {
				// Add unquoted word
				matcherList.add(regexMatcher.group());
			}
		}
		return matcherList;
	}

}
