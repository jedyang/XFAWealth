package com.fsll.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.fsll.common.CommonConstants;

/**
 * @author mjhuang
 */
public class PropertyUtils implements BeanFactoryAware {
	
	/**
	 * 获取以prefix开头的所有配置信息的值的列表
	 * @param prefix
	 * @return
	 */
	public List<String> getList(String prefix) {
		if (properties == null || prefix == null) {
			return Collections.emptyList();
		}
		List<String> list = new ArrayList<String>();
		Enumeration<?> en = properties.propertyNames();
		String key;
		while (en.hasMoreElements()) {
			key = (String) en.nextElement();
			if (key.startsWith(prefix)) {
				list.add(properties.getProperty(key));
			}
		}
		return list;
	}
	
	/**
	 * 获取以prefix开头的所有配置信息的值的集合
	 * @param prefix
	 * @return
	 */
	public Set<String> getSet(String prefix) {
		if (properties == null || prefix == null) {
			return Collections.emptySet();
		}
		Set<String>set=new TreeSet<String>();
		Enumeration<?> en = properties.propertyNames();
		String key;
		while (en.hasMoreElements()) {
			key = (String) en.nextElement();
			if (key.startsWith(prefix)) {
				set.add(properties.getProperty(key));
			}
		}
		return set;
	}
	
	/**
	 * 获取以prefix开头的所有配置信息的映射表（关键字去掉了prefix）
	 * @param prefix
	 * @return
	 */
	public Map<String, String> getMap(String prefix) {
		if (properties == null || prefix == null) {
			return Collections.emptyMap();
		}
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<?> en = properties.propertyNames();
		String key;
		int len = prefix.length();
		while (en.hasMoreElements()) {
			key = (String) en.nextElement();
			if (key.startsWith(prefix)) {
				map.put(key.substring(len), properties.getProperty(key));
			}
		}
		return map;
	}

	/**
	 * 获取以prefix开头的所有配置信息的映射表，值从字符串转换为对象（关键字去掉了prefix）
	 * @param prefix
	 * @return
	 */
	public Map<String, Object> getBeanMap(String prefix) {
		Map<String, String> keyMap = getMap(prefix);
		if (keyMap.isEmpty()) {
			return Collections.emptyMap();
		}
		Map<String, Object> resultMap = new HashMap<String, Object>(keyMap.size());
		String key, value;
		for (Map.Entry<String, String> entry : keyMap.entrySet()) {
			key = entry.getKey();
			value = entry.getValue();
			resultMap.put(key, beanFactory.getBean(value, Object.class));
		}
		return resultMap;
	}
	
	/**
	 * 获取以prefix开头的所有配置信息（关键字去掉了prefix）
	 * @param prefix
	 * @return
	 */
	public Properties getProperties(String prefix) {
		Properties props = new Properties();
		if (properties == null || prefix == null) {
			return props;
		}
		Enumeration<?> en = properties.propertyNames();
		String key;
		int len = prefix.length();
		while (en.hasMoreElements()) {
			key = (String) en.nextElement();
			if (key.startsWith(prefix)) {
				props.put(key.substring(len), properties.getProperty(key));
			}
		}
		return props;
	}
	
	/**
	 * 获取一个配置的值
	 * @param key
	 * @return
	 */
	public String getPropertiesString(String key) {
		String property = "";
		if (properties == null || key == null) {
			return property;
		}
		Enumeration<?> en = properties.propertyNames();
		while (en.hasMoreElements()) {
			if (key.equals((String) en.nextElement())) {
				return properties.getProperty(key);
			}
		}
		return property;
	}

	/**
	 * 获取文件中的所有配置信息
	 * @param file
	 * @return
	 */
	public static Properties getProperties(File file) {
		Properties props = new Properties();
		InputStream in;
		try {
			in = new FileInputStream(file);
			props.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}
	
	/**
	 * 获取配置文件中的一个配置信息的值
	 * @param file
	 * @param key
	 * @return
	 */
	public static String getPropertyValue(File file,String key) {
		if (null==key) return null;
		Properties props=getProperties(file);
		return (String) props.get(key);
	}
    
	/**
	 * 根据语言编码获取配置文件中的一个配置信息的值
	 * 如果配置是作为模板使用：返回结果中的参数{?}用params中的值来填充
	 * @param file
	 * @param key
	 * @param params
	 * @return
	 */
    public static String getPropertyValue(String lang, String key, Object[] params){  
    	if (null==key) return null;
		String filePath = CommonConstants.LANG_PATH_SC;//默认简体中文
		if(lang != null){
			if(lang.equalsIgnoreCase(CommonConstants.LANG_CODE_SC)){
				filePath = CommonConstants.LANG_PATH_SC;
			}else if(lang.equalsIgnoreCase(CommonConstants.LANG_CODE_TC)){
				filePath = CommonConstants.LANG_PATH_TC;
			}else if(lang.equalsIgnoreCase(CommonConstants.LANG_CODE_EN)){
				filePath = CommonConstants.LANG_PATH_EN;
			}
		}
        Properties props = new Properties();  
        InputStream in = null;  
        try {
        	
        	String basePath = PropertyUtils.class.getResource("/").getPath(); 
        	File f = new File(basePath+filePath);
        	in = new FileInputStream(f);
            // 如果将in改为下面的方法，必须要将.Properties文件和此class类文件放在同一个包中  
            //in = propertiesTools.class.getResourceAsStream(fileNamePath);  
            props.load(in);  
            String value = props.getProperty(key);  
            // 有乱码时要进行重新编码  
            // new String(props.getProperty("name").getBytes("ISO-8859-1"), "GBK");  
            String keyValue = value == null ? "" : value;
    		for (int i = 0; i < (params != null ? params.length : 0); i++) {
    			keyValue = StringUtils.replace(keyValue, "{"+i+"}",params[i]+"");
    		}
            return keyValue;  
        } catch (IOException e) {  
            e.printStackTrace();  
            return null;  
        } finally {  
        	try {
                if (null != in)in.close();  
        	} catch (IOException e) {
        		e.printStackTrace();  
            }
        }  
    }
    
    /**
     * 获取数据库配置文件参数jdbc.properties
     * @param key
     * @return
     */
    public static String getConfDbPropertyValue(String key){  
		String filePath = CommonConstants.MONGO_CONFIG_PATH;
        Properties props = new Properties();  
        InputStream in = null;  
        try {
        	
        	String basePath = PropertyUtils.class.getResource("/").getPath(); 
        	File f = new File(basePath+filePath);
        	in = new FileInputStream(f);
            // 如果将in改为下面的方法，必须要将.Properties文件和此class类文件放在同一个包中  
            //in = propertiesTools.class.getResourceAsStream(fileNamePath);  
            props.load(in);  
            String value = props.getProperty(key);  
            // 有乱码时要进行重新编码  
            // new String(props.getProperty("name").getBytes("ISO-8859-1"), "GBK");  
            String keyValue = value == null ? "" : value;
            return keyValue;  
        } catch (IOException e) {  
            e.printStackTrace();  
            return null;  
        } finally {  
        	try {
                if (null != in)in.close();  
        	} catch (IOException e) {
        		e.printStackTrace();  
            }
        }  
    }
    
    /**
     * 获取系统配置文件参数 config.properties
     * @param key
     * @return
     */
    public static String getConfPropertyValue(String key){  
		String filePath = CommonConstants.CONFIG_PATH;
        Properties props = new Properties();  
        InputStream in = null;  
        try {
        	
        	String basePath = PropertyUtils.class.getResource("/").getPath(); 
        	File f = new File(basePath+filePath);
        	in = new FileInputStream(f);
            // 如果将in改为下面的方法，必须要将.Properties文件和此class类文件放在同一个包中  
            //in = propertiesTools.class.getResourceAsStream(fileNamePath);  
            props.load(in);  
            String value = props.getProperty(key);  
            // 有乱码时要进行重新编码  
            // new String(props.getProperty("name").getBytes("ISO-8859-1"), "GBK");  
            String keyValue = value == null ? "" : value;
            return keyValue;  
        } catch (IOException e) {  
            e.printStackTrace();  
            return null;  
        } finally {  
        	try {
                if (null != in)in.close();  
        	} catch (IOException e) {
        		e.printStackTrace();  
            }
        }  
    }
    
	private BeanFactory beanFactory;
	private Properties properties;

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	/**
	 * 通过语言编码获取多语言配置文件
	 * @author michael
	 * @param lang
	 * @param prefix
	 * @return
	 */
    public static Properties getPropertyByLang(String lang, String prefix){  
		String filePath = CommonConstants.DEF_LANG_CODE;//默认简体中文
		if(lang != null){
			if(lang.equalsIgnoreCase(CommonConstants.LANG_CODE_SC)){
				filePath = CommonConstants.LANG_PATH_SC;
			}else if(lang.equalsIgnoreCase(CommonConstants.LANG_CODE_TC)){
				filePath = CommonConstants.LANG_PATH_TC;
			}else if(lang.equalsIgnoreCase(CommonConstants.LANG_CODE_EN)){
				filePath = CommonConstants.LANG_PATH_EN;
			}
		}

        try {
        	//获取参数文件
        	String basePath = PropertyUtils.class.getResource("/").getPath(); 
        	File f = new File(basePath+filePath);
        	Properties prop = getProperties(f);
        	prop = getProperties(prop, prefix);
        	return prop;
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        } 
    }
    
	/**
	 * 获取以prefix开头的所有配置信息的映射表（关键字去掉了prefix）
	 * @author michael
	 * @param prop
	 * @param prefix
	 * @return
	 */
	public static Map<String, String> getPropertyMap(Properties prop, String prefix) {
		if (prop == null || prefix == null) {
			return Collections.emptyMap();
		}
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<?> en = prop.propertyNames();
		String key;
		while (en.hasMoreElements()) {
			key = (String) en.nextElement();
			if (key.startsWith(prefix)) {
				map.put(key, prop.getProperty(key));
			}
		}
		return map;
	}
	
	/**
	 * 获取以prefix开头的所有配置信息
	 * @author michael
	 * @param prop
	 * @param prefix
	 * @return
	 */
	public static Properties getProperties(Properties prop, String prefix) {
		Properties props = new Properties();
		if (prop == null || prefix == null) {
			return props;
		}
		Enumeration<?> en = prop.propertyNames();
		String key;
		while (en.hasMoreElements()) {
			key = (String) en.nextElement();
			if (key.startsWith(prefix)) {
				props.put(key, prop.getProperty(key));
			}
		}
		return props;
	}
	
	/**
	 * 转换配置信息为映射表
	 * @author michael
	 * @param prop
	 * @return
	 */
	public static Map<String, String> getPropertyMap(Properties prop) {
		if (prop == null) {
			return Collections.emptyMap();
		}
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<?> en = prop.propertyNames();
		String key;
		while (en.hasMoreElements()) {
			key = (String) en.nextElement();
			map.put(key, prop.getProperty(key));
		}
		return map;
	}
	
    /**
     * 获取消息模板文件指定内容
     * mjhuang 2016-12-07
     * @param key
     * @param lang 语言,sc,tc,en
     * @return
     */
    public static String getSmsPropertyValue(String key,String lang, Object[] params){
    	String filePath = PropertyUtils.class.getResource("/").getPath()+"sys/smsTemplate.properties"; 
    	//String filePath = UploadUtil.UPLOADDIR+UploadUtil.SPT+"sys"+UploadUtil.SPT+"smsTemplate.properties";
        Properties props = new Properties();  
        InputStream in = null;  
        try {
        	File f = new File(filePath);
        	in = new FileInputStream(f);
            props.load(in);  
            String value = props.getProperty(key+"."+lang);  
            String keyValue = value == null ? "" : value;
    		for (int i = 0; i < (params != null ? params.length : 0); i++) {
    			keyValue = StringUtils.replace(keyValue, "{"+i+"}",params[i]+"");
    		}
            return keyValue;  
        } catch (IOException e) {  
            e.printStackTrace();  
            return null;  
        } finally {  
        	try {
                if (null != in)in.close();  
        	} catch (IOException e) {
        		e.printStackTrace();  
            }
        }  
    }
    
    /**
     * 获取消息模板文件指定内容
     * mjhuang 2016-12-07
     * @param request
     * @param key
     * @param lang 语言,sc,tc,en
     * @return
     */
    public static String getEmailValue(HttpServletRequest request,String key,String lang, Object[] params){
    	String ctxPath = request.getSession().getServletContext().getRealPath("/");
    	String filePath = ctxPath+UploadUtil.SPT+"t"+UploadUtil.SPT+key+UploadUtil.SPT+lang+".html";
    	InputStreamReader read = null;
    	try {
    		File file = new File(filePath);
    		read = new InputStreamReader(new FileInputStream(file),"UTF-8");
    		BufferedReader br = new BufferedReader(read);
    		String fileContent = "";
    		String temp = "";
    		while ((temp = br.readLine()) != null) {
    			fileContent = fileContent + temp;
    		}
    		br.close();
    		fileContent =  fileContent == null ? "" : fileContent;
    		for (int i = 0; i < (params != null ? params.length : 0); i++) {
    			fileContent = StringUtils.replace(fileContent, "{"+i+"}",params[i]+"");
    		}
    		return fileContent;
        } catch (IOException e) {  
            e.printStackTrace();  
            return null;  
        } finally {  
        	try {
                if (null != read)read.close();  
        	} catch (IOException e) {
        		e.printStackTrace();  
            }
        }   
    }
    
}
