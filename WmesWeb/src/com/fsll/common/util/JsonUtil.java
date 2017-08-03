package com.fsll.common.util;

import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("unchecked")
public class JsonUtil {
	
	private static ObjectMapper om = new ObjectMapper();
	
	static{
		om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
	}
	
	public static void setFormatYYY(){
		om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
	}
	
	public static void setFormatYYYSS(){
		om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}


	public static ObjectMapper getOm() {
		return om;
	}

	public static String toJson(Object ob) {
		try {
			return om.writeValueAsString(ob);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static List<Map> toListMap(String ob) {
		try {
			return om.readValue(ob, List.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Map toMap(String ob) {
		try {
			return om.readValue(ob, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Map toLinkedHashMap(String ob) {
		try {
			return om.readValue(ob, LinkedHashMap.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void toMap(Object ob, Writer w) {
		try {
			om.writeValue(w, ob);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void toWriter(Object ob, HttpServletResponse resp) {
		try {
			resp.setContentType("application/json;charset=UTF-8");
			resp.setCharacterEncoding("UTF-8");
			om.writeValue(resp.getWriter(),ob);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 
     * JAVA对象转换成JSON字符串 
     * @param obj 
     * @return 
     */   
    public static String objectToJsonStr(Object obj) {  
    	JSONObject result = JSONObject.fromObject(obj);   
        return result.toString();  
    }  
      
	/** 
     * JAVA对象列表转换成JSON字符串 
     * @param obj 
     * @return 
     */   
    public static String objectToJsonStr(List list) {  
    	JSONArray array = JSONArray.fromObject(list);
        String jsonstr = array.toString(); 
        return jsonstr;  
    }
}