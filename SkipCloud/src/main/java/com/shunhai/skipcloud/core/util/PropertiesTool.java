package com.shunhai.skipcloud.core.util;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Properties工具类
 */
public class PropertiesTool {
	
	private static final Pattern PATTERN = Pattern
			.compile("\\$\\{([^\\}]+)\\}");
	
	/**
	 * 在Properties文件中通过${key}转译为对应key的value值
	 * @param properties 传入的Properties文件
	 * @param key 需要转译为对应value的key
	 * @return key转译后的value
	 */
	public static String get(Properties properties, String key) {
		String value = properties.getProperty(key);
		Matcher matcher = PATTERN.matcher(value);
		StringBuffer buffer = new StringBuffer();
		while (matcher.find()) {
			String matcherKey = matcher.group(1);
			String matchervalue = properties.getProperty(matcherKey);
			if (matchervalue != null) {
				matcher.appendReplacement(buffer, matchervalue);
			}
		}
		matcher.appendTail(buffer);
		return buffer.toString();
	}
}
