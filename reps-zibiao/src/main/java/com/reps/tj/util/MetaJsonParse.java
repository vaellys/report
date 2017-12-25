package com.reps.tj.util;

import static com.reps.tj.util.Constants.INDICATOR_META_INFO;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.reps.core.exception.RepsException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 指标元数据解析，查询
 * 
 * @author qianguobing
 * @date 2017年12月20日 上午10:46:17
 */
public class MetaJsonParse {

	private MetaJsonParse() {
	}

	/**
	 * 从json数据中根据相应的KEY返回列表
	 * 
	 * @param json
	 *            数据源
	 * @param key
	 * @param clazz
	 *            数据类型
	 * @return List<T>
	 * @throws RepsException
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getSpecialValuesFromJson(JSONObject json, String key, Class<T> clazz) throws RepsException {
		try {
			JSONObject dataJson = getJsonObject(json);
			JSONArray jsonArray = getJsonArray(key, dataJson);
			JsonConfig config = new JsonConfig();
			return (List<T>) JSONArray.toList(jsonArray, clazz.newInstance(), config);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RepsException("解析指标元数据异常", e);
		}
	}

	private static JSONArray getJsonArray(String key, JSONObject dataJson) {
		if(!dataJson.containsKey(key)) {
			JSONArray jsonArray = new JSONArray();
			dataJson.put(key, jsonArray);
		}
		return dataJson.optJSONArray(key);
	}

	private static JSONObject getJsonObject(JSONObject json) {
		JSONObject dataJson = json.optJSONObject(INDICATOR_META_INFO);
		if(null == dataJson) {
			dataJson = new JSONObject();
			json.put(INDICATOR_META_INFO, dataJson);
		}
		return dataJson;
	}

	/**
	 * 从列表中寻找值为id的对象
	 * 
	 * @param list
	 *            数据列表
	 * @param id
	 *            字段值
	 * @param fieldName
	 *            字段名称
	 * @return T
	 * @throws RepsException
	 */
	public static <T> T getSpecialValueFromList(List<T> list, String id, String fieldName) throws RepsException {
		try {
			for (T t : list) {
				String property = BeanUtils.getProperty(t, fieldName);
				if (id.equals(property)) {
					return t;
				}
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RepsException("查询特定元数据信息异常", e);
		}
	}

	/**
	 * 替换json中相应位置的值
	 * 
	 * @param json
	 *            数据列表
	 * @param key
	 *            json数据中相应的key
	 * @param newVal
	 *            替换的值
	 * @return String
	 * @throws RepsException
	 */
	public static <T> String replaceSpecialValueFromJson(JSONObject json, String key, List<T> newVal) throws RepsException {
		JSONObject dataJson = getJsonObject(json);
		dataJson.put(key, newVal);
		return json.toString();
	}

	/**
	 * 往相应的json列表中添加对应的元素
	 * 
	 * @param json
	 *            数据列表
	 * @param key
	 * @param newVal
	 *            添加对象
	 * @return
	 * @throws RepsException
	 */
	public static <T> String addSpecialValueFromJson(JSONObject json, String key, T newVal) throws RepsException {
		JSONObject dataJson = getJsonObject(json);
		JSONArray jsonArray = getJsonArray(key, dataJson);
		jsonArray.add(newVal);
		return json.toString();
	}

	/**
	 * 往相应的json列表中移除对应的元素
	 * 
	 * @param json
	 *            数据列表
	 * @param key
	 * @param newVal
	 *            移除对象
	 * @return
	 * @throws RepsException
	 */
	public static <T> String removeSpecialValueFromJson(JSONObject json, String key, T newVal) throws RepsException {
		JSONObject dataJson = getJsonObject(json);
		JSONArray jsonArray = getJsonArray(key, dataJson);
		jsonArray.remove(JSONObject.fromObject(newVal));
		return json.toString();
	}

}
