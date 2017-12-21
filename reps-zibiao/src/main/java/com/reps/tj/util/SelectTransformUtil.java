package com.reps.tj.util;

import java.util.Iterator;
import java.util.Map;

public class SelectTransformUtil {
	
	public static String transSelectOptionStrFromMap(Map<String, String> map, boolean isSelectOption) {
		StringBuilder sb = new StringBuilder("");
		Iterator<String> keyIterator = map.keySet().iterator();
		String id = "";
		String text = "";
		while (keyIterator.hasNext()) {
			id = keyIterator.next();
			text = map.get(id);
			sb.append("<option value='" + id + "'> " + text + "  </option>\r\n");
		}
		if (isSelectOption) {
			return "<option value=''></option>\r\n" + sb.toString();
		} else {
			return sb.toString();
		}
	}
}
