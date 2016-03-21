package com.lsj.test.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author 
 * JSON格式字符串和Java Bean互操作工具类
 */
public class JSONUtil {

	/**
	 * 将Java实例转化成JSON格式字符串
	 * @param t Java实例
	 * @return JSON格式字符串
	 */
	public static <T> String parseToString(T t){
		try {
			return new ObjectMapper().writeValueAsString(t);
		} catch (JsonGenerationException e) {
			return null;
		} catch (JsonMappingException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * 将JSON格式字符串转化成对应的Java实例
	 * @param json JSON格式字符串
	 * @param t Java类
	 * @return Java实例
	 */
	public static <T> T  parseToBean(String json,Class<T> t){
		try {
			return new ObjectMapper().readValue(json,t);
		} catch (JsonParseException e) {
			return null;
		} catch (JsonMappingException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * 根据大系统提交的参数，返回JSON格式字符串
	 * 此方法为大系统专用
	 * @param parameter
	 * @return
	 */
	public static String getJSONArray(String parameter) {
		if(parameter==null||parameter.trim().length()==0||"".equals(parameter)) return "{}";
		List<Map<String, String>> table = getSoftwares(parameter);
		return parseToString(table);
	}

	/**
	 * @param parameter
	 * @return
	 */
	public static List<Map<String, String>> getSoftwares(String parameter) {
		if(parameter==null||parameter.trim().length()==0||"".equals(parameter)) return null;
		String[] array = org.apache.commons.lang3.StringUtils.split(parameter, ";");
		List<Map<String,String>> table = new ArrayList<Map<String,String>>();
		for (String string : array) {
			if(string!=null&&string.trim().length()>0){
				Map<String,String> rowMap = new HashMap<String, String>();
				String[] rows = org.apache.commons.lang3.StringUtils.split(string, ",");
				for (int i = 0; i < rows.length; i++) {
					rowMap.put("key_"+i,"undefined".equals(rows[i])?"":rows[i]);
				}
				table.add(rowMap);
			}								 
		}
		return table;
	}
	
	/**
	 * 将JSON数组格式字符串转换成键值对集合
	 * @param json
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static List<Map<String,String>> parseToBeanList(String json) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, new TypeReference<List<HashMap<String,String>>>(){});
	}
	
	/**
	 * 将JSON数组格式字符串转换成指定对象集合
	 * @param json
	 * @param T
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> List<T> parseToBeanList(String json,Class<T> t) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, new TypeReference<ArrayList<T>>(){});
	}

	
	/**
	 * 将JSON格式字符串转换成Map结构
	 * @param json
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static Map<String,String> parseToBean(String json) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, new TypeReference<HashMap<String,String>>(){});
	}
	
	public static <T> List<T> parseToBean2List(String json, Class<T> t) throws IOException {

		ObjectMapper mapper = new ObjectMapper();
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, t);
		// 如果是Map类型
		// mapper.getTypeFactory().constructParametricType(HashMap.class,String.class,
		// Bean.class);
		List<T> lst = (List<T>) mapper.readValue(json, javaType);

		return lst;
	}
}
