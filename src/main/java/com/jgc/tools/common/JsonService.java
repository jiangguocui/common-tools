package com.jgc.tools.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;
import java.util.regex.Pattern;

public class JsonService {
	
	private final static Pattern REGEX_PATTERN= Pattern.compile("正则");
	


	
	/**
	 * :转换语言包注册资源
	 * @return
	 */
	public Object transform(Long tenantId, Object input) {

		if (input == null) {
			return null;
		}

		if (input instanceof JSONArray) {
			final JSONArray jsonArray = (JSONArray) input;
			final JSONArray newJSONArray = new JSONArray();
			for (int i = 0, len = jsonArray.size(); i < len; i++) {
				newJSONArray.add(transform(tenantId, jsonArray.get(i)));
			}

			return newJSONArray;
		}

		if (input instanceof JSONObject) {

			final JSONObject json = (JSONObject) input;
			// TODO 替换处理
//
//			boolean resourceJSON = true;
//			List<String> lansClone = Arrays.asList(Constants.LANS);
//			for (String key : json.keySet()) {
//				if (!lansClone.contains(key)) {
//					resourceJSON = false;
//					break;
//				}
//			}
//
//			if (resourceJSON) {
//				return resourcesService.register(tenantId, json);
//			}

			final JSONObject newJSON = new JSONObject();
			for (String key : json.keySet()) {
				newJSON.put(key, transform(tenantId, json.get(key)));
			}

			return newJSON;

		}
		return input;
	}


	
	/**
	 * 还原资源包
	 * @return
	 */
	public Object restore(Long tenantId, Object input) {

		if (input == null) {
			return null;
		}

		if (input instanceof JSONArray) {
			final JSONArray jsonArray = (JSONArray) input;
			final JSONArray newJSONArray = new JSONArray();
			for (int i = 0, len = jsonArray.size(); i < len; i++) {
				newJSONArray.add(restore(tenantId, jsonArray.get(i)));
			}

			return newJSONArray;
		}

		if (input instanceof JSONObject) {

			final JSONObject json = (JSONObject) input;
			
			final JSONObject newJSON = new JSONObject();
			for (String key : json.keySet()) {
				newJSON.put(key, restore(tenantId, json.get(key)));
			}

			return newJSON;

		}

		if ( input instanceof String) {

			String value = (String) input;
			//TODO 还原处理
//
//			if (value.matches(Constants.REGEX_RID)) {
//
//				String content = resourcesService.getContentByRid(value);
//
//				return content == null ? value : JSONObject.parse(content);
//			}

			return value;
		}

		return input;
	}

	public void found(String node, Object input, Map<String, Boolean> item) {

		if (input == null) {
			return;
		}

		if (node == null) {

			node = "";
		}

		if (input instanceof JSONArray) {
			final JSONArray jsonArray = (JSONArray) input;

			for (int i = 0, len = jsonArray.size(); i < len; i++) {
				if (REGEX_PATTERN.matcher(jsonArray.get(i).toString()).find()) {
					found(String.format("%s[%d]", node, i), jsonArray.get(i), item);
				}
			}
		}

		if (input instanceof JSONObject) {

			final JSONObject json = (JSONObject) input;

			for (String key : json.keySet()) {

				if (REGEX_PATTERN.matcher(json.get(key).toString()).find()) {
					
					String newNode = "".equals(node) ? key : String.format("%s.%s", node, key);
					
					found(newNode, json.get(key), item);
				}
			}

		}

		if (input instanceof String) {
			String value = (String) input;
			if(item.containsKey(node) && value.matches(/*Constants.REGEX_RID*/)){
				item.put(node, true);
			}
		}
	}
}
