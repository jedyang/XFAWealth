/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.base.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.fsll.common.CommonConstants;
import com.fsll.common.mongodb.MongoDao;
import com.fsll.common.persistence.BaseDao;
import com.fsll.common.persistence.SpringJdbcQueryManager;
import com.fsll.core.entity.SysCountry;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.dao.entity.WebExchangeRate;

/**
 * 业务实现基类：通用模块
 * 
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0 Created On: 2016-2-19
 */
public class CommonDao {

	@Autowired
	public BaseDao baseDao;

	@Autowired
	public SpringJdbcQueryManager springJdbcQueryManager;

	@Resource
	public MongoDao mongoDao;

	/**
	 * 处理字符串类型的id,添加单引号
	 * 
	 * @param request
	 * @return
	 */
	protected String getNewStrId(String ids) {
		String newIds = "";
		if (!"".equals(ids)) {
			String tmpStr = ids;
			if (tmpStr.endsWith(","))
				tmpStr = tmpStr.substring(0, tmpStr.length() - 1);
			String[] arr = tmpStr.split(",");
			for (String id : arr) {
				if ("".equals(newIds)) {
					newIds = "'" + id + "'";
				} else {
					newIds += ",'" + id + "'";
				}
			}
		}
		return newIds;
	}

	/**
	 * 将语言标识的第一个字母变成大写
	 * 
	 * @param langCode
	 * @return
	 */
	protected String getLangFirstCharUpper(String langCode) {
		String firstChar = langCode.substring(0, 1).toUpperCase();
		String lastStr = langCode.substring(1, langCode.length());
		return firstChar + lastStr;
	}

	/**
	 * 获取语言字符串(首字母大写，用于hql)
	 * 
	 * @param prefix 前缀
	 * @param langCode 语言
	 * @return
	 */
	protected String getLangString(String prefix, String langCode) {
		String result = prefix;
		if (null != langCode && !"".equals(langCode)) {// 按语言参数
			result += getLangFirstCharUpper(langCode);
		} else {// 默认语言
			result += getLangFirstCharUpper(CommonConstants.DEF_LANG_CODE);
		}
		return result;
	}
	
	/**
	 * 根据语言获取国家名称
	 * @author mqzou	
	 * @date 2016-07-07
	 * @param sysCountry
	 * @param langCode
	 */
	public String getCountryString(SysCountry sysCountry,String langCode){
		if(null!=sysCountry){
			if("sc".equalsIgnoreCase(langCode)){
				return sysCountry.getNameSc();
			}else if ("tc".equalsIgnoreCase(langCode)) {
				return sysCountry.getNameTc();
			}else if ("en".equalsIgnoreCase(langCode)) {
				return sysCountry.getNameEn();
			}else {
				return "";
			}
		}else {
			return "";
		}
	}
    
	/**
	 * 获取兑换率
	 * @author zpzhou
	 * @param fromCurrency
	 * @param toCurrency
	 * @return
	 */
	public Double getExchangeRate(String fromCurrency, String toCurrency) {
		if( StringUtils.isBlank(fromCurrency) || StringUtils.isBlank(toCurrency) ){
    		return null;
    	}else if (fromCurrency.equals(toCurrency)){
			return 1d;
		}else{
			String hql = "from WebExchangeRate t where t.isValid='1' and fromCurrency=? and toCurrency=? order by t.fromCurrency,t.toCurrency  ";
			List params = new ArrayList();
			params.add(fromCurrency);
			params.add(toCurrency);
			List<WebExchangeRate> list = this.baseDao.find(hql, params.toArray(), false);
			if (null!=list && !list.isEmpty()) return list.get(0).getRate();
			return 0d;
		}
	}
    
	/**
	 * 获取基础数据名称
	 * @author wwluo
	 * @param langCode 
	 * @param configCode 
	 * @param typeCode 
	 * @return
	 */
	public String getParamConfigName(String langCode, String configCode,String typeCode) {
		String name = null;
		if(StringUtils.isNotBlank(configCode) 
				&& StringUtils.isNotBlank(langCode)
					&& StringUtils.isNotBlank(typeCode)){
			String lang = langCode.substring(0, 1).toUpperCase()+langCode.substring(1, 2);
			StringBuffer hql = new StringBuffer("" +
					" SELECT" +
					" c.name" + lang +
					" FROM" +
					" SysParamConfig c" +
					" WHERE" +
					" c.type.typeCode=?" +
					" AND" +
					" c.configCode=?");
			List<Object> params = new ArrayList<Object>();
			params.add(typeCode);
			params.add(configCode);
			List<String> sysParamConfigs = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(sysParamConfigs != null && !sysParamConfigs.isEmpty()){
				name = sysParamConfigs.get(0);
			}
		}
		return name;
	}
	
	
	/**
	 * 获取基础数据名称
	 * @author zxtan
	 * @param langCode 语言
	 * @param configCodeList 以逗号隔开的code串
	 * @return
	 */
	public String getParamConfigNameList(String langCode, String configCodeList) {
		String name = null;
		if(StringUtils.isNotBlank(configCodeList) && StringUtils.isNotBlank(langCode)){
			String hql = " SELECT fn_getconfigname(?,?) from SysParamConfig ";
			List<Object> params = new ArrayList<Object>();
			params.add(configCodeList);
			params.add(langCode);
			List list = this.baseDao.find(hql.toString(), params.toArray(),1,2, false);
			if(list != null && !list.isEmpty()){
				name = (String) list.get(0);
			}
		}
		return name;
	}
	
	/**
	 * 获取基础数据
	 * @param configCode 
	 * @return
	 */
	public SysParamConfig getParamConfigByCode(String configCode) {
		if(StringUtils.isNotBlank(configCode)){
			StringBuffer hql = new StringBuffer("FROM SysParamConfig c WHERE c.configCode=?");
			List<Object> params = new ArrayList<Object>();
			params.add(configCode);
			List<SysParamConfig> sysParamConfigs = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(sysParamConfigs != null && !sysParamConfigs.isEmpty()){
				return sysParamConfigs.get(0);
			}
		}
		return null;
	}
	
	/**
	 * 获取基础数据
	 * @param configCode 
	 * @return
	 */
	public List<SysParamConfig> getParamConfigListByTypeCode(String typeCode) {
		if(StringUtils.isNotBlank(typeCode)){
			StringBuilder hql = new StringBuilder();
			hql.append(" FROM SysParamConfig c ");
			hql.append(" WHERE c.isValid='1' ");
			hql.append(" and exists ( select s.id from SysParamType s where s.id=c.type.id and s.typeCode=? ) ");
			hql.append(" order by c.orderBy ");
			List<Object> params = new ArrayList<Object>();
			params.add(typeCode);
			List<SysParamConfig> list = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(list != null && !list.isEmpty()){
				return list;
			}
		}
		return null;
	}
	
}
