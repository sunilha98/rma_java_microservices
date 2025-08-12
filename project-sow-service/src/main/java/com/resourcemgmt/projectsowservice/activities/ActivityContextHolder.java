package com.resourcemgmt.projectsowservice.activities;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ActivityContextHolder {
	private static final ThreadLocal<Map<String, String>> context = new ThreadLocal<>();

	public static void setDetail(String key, String value) {
		if (context.get() == null) {
			context.set(new HashMap<>());
		}
		context.get().put(key, value);
	}

	public static String getDetail(String key) {
		return context.get() != null ? context.get().get(key) : null;
	}

	public static Map<String, String> getAllDetails() {
		return context.get() != null ? context.get() : Collections.emptyMap();
	}

	public static void clear() {
		context.remove();
	}
}
