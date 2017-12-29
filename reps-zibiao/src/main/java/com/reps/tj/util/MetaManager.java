package com.reps.tj.util;

import static com.reps.tj.enums.Meta.*;
import static com.reps.tj.util.Constants.INDICATOR_META_INFO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.reps.core.exception.RepsException;
import com.reps.core.web.HttpSessionManager;

import net.sf.json.JSONObject;

/**
 * 指标元数据session管理
 * @author qianguobing
 * @date 2017年12月19日 下午5:41:45
 */
public class MetaManager {
	
	public static String getMetaDatasFromSession(HttpServletRequest request) throws RepsException {
		HttpSessionManager currentSession = HttpSessionManager.setCurrent(request);
		Map<String, Object> metaMap = new HashMap<>();
		Map<String, Object> dataMap = new HashMap<>();
		for (String key : META_CODES) {
			dataMap.put(key, currentSession.getAttribute(key));
		}
		metaMap.put(INDICATOR_META_INFO, dataMap);
		return JSONObject.fromObject(metaMap).toString();
	}
	
	public static void removeMetaDatasFromSession() throws RepsException {
		HttpSessionManager currentSession = HttpSessionManager.getCurrent();
		for (String key : META_CODES) {
			currentSession.remove(key);
		}
	}
	
	public static void removeCopyMetaDatasFromSession() throws RepsException {
		HttpSessionManager currentSession = HttpSessionManager.getCurrent();
		for (String key : COPY_META_CODES) {
			currentSession.remove(key);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> getValuesFromSession(HttpServletRequest request, String key) throws RepsException {
		HttpSessionManager currentSession = HttpSessionManager.setCurrent(request);
		return (List<T>) currentSession.getAttribute(key);
	}
	
	public static <T> void setValuesToSession(HttpServletRequest request, String key, List<T> values) throws RepsException {
		HttpSessionManager currentSession = HttpSessionManager.setCurrent(request);
		currentSession.setAttribute(key, values);
	}
	
}
