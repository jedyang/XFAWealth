/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.base.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.fsll.common.CommonConstants;
import com.fsll.common.mongodb.MongoDao;
import com.fsll.common.persistence.BaseDao;
import com.fsll.common.persistence.SpringJdbcQueryManager;
import com.fsll.core.CoreConstants;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.entity.SysAdmin;
import com.fsll.core.entity.SysCountry;
import com.fsll.core.vo.AccessoryFileVO;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.WebExchangeRate;

/**
 * 业务实现基类：通用模块
 * 
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0 Created On: 2016-2-19
 */
public class BaseService {

	@Autowired
	public BaseDao baseDao;

	@Autowired
	public SpringJdbcQueryManager springJdbcQueryManager;

	@Resource
	public MongoDao mongoDao;
	
	/**
	 * 获得远程主机ip地址
	 * 
	 * @param request
	 * @return
	 */
	protected String getRemoteHost(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
	}

	/**
	 * 获得当前的登陆用户语言
	 * 
	 * @param request
	 * @return
	 */
	protected String getLoginLangFlag(HttpServletRequest request) {
		Object obj = request.getSession().getAttribute(CoreConstants.LANG_CODE);
		if (obj != null) {
			return (String) obj;
		}
		return null;
	}

	/**
	 * 获得当前的登陆用户
	 * 
	 * @param request
	 * @return
	 */
	protected SysAdmin getLoginUser(HttpServletRequest request) {
		Object memberBase = request.getSession().getAttribute(CoreConstants.USER_LOGIN);
		SysAdmin sysAdmin = null;
		if (memberBase != null) {
			sysAdmin = (SysAdmin) sysAdmin;
		}
		return sysAdmin;
	}

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
	 * 查询附件
	 * 
	 * @param relateId
	 *            accessory_file表relate_id
	 * @param moduleId
	 *            模块Id
	 * */
	public List<AccessoryFile> findFileAttr(String relateId, String moduleId) {
		String hql = "from AccessoryFile t where t.relateId=? and t.moduleType=?";
		List args = new ArrayList();
		args.add(relateId);
		args.add(moduleId);
		List<AccessoryFile> fileList = this.baseDao.find(hql, args.toArray(),false);
		return fileList;
	}

	/**
	 * 查询附件
	 * 
	 * @param relateId accessory_file表relate_id
	 * @param moduleId 模块Id
	 * */
	public List<AccessoryFileVO> findFileAttrVO(String relateId, String moduleId) {
		String hql = "from AccessoryFile t where t.relateId=? and t.moduleType=?";
		List args = new ArrayList();
		args.add(relateId);
		args.add(moduleId);
		List<AccessoryFile> fileList = this.baseDao.find(hql, args.toArray(), false);
		List<AccessoryFileVO> rList = new ArrayList<AccessoryFileVO>();
		if (fileList != null && !fileList.isEmpty()) {
			for (AccessoryFile vo : fileList) {
				AccessoryFileVO tmpVO = new AccessoryFileVO();
				BeanUtils.copyProperties(vo, tmpVO);
				rList.add(tmpVO);
			}
		}
		return rList;
	}

	/**
	 * 清空附件
	 * 
	 * @param relateId
	 * @param module
	 * */
	public void clearFileAttr(String relateId, String module) {
		String hql = "delete from AccessoryFile t where t.relateId=? and t.moduleType=?";
		List args = new ArrayList();
		args.add(relateId);
		args.add(module);
		this.baseDao.updateHql(hql, args.toArray());
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
     * 获得用户的名称----简单方法
     * @author mjhuang 2017-05-09
     * @param memberId=member_base.id
     * @param 语言编码
     * @param 类别 0或者null = 默认,1=呢称 2=真名 
     * @return 用户名称
     */
    public String getCommonMemberName(String memberId,String langCode,String type){
    	String returnName = "";
    	MemberBase memberBase = (MemberBase)this.baseDao.get(MemberBase.class, memberId);
    	if (memberBase == null) return returnName;
    	if(type != null && "1".equals(type)){//返回呢称
    		returnName = memberBase.getNickName();
    	}else if(type != null && "2".equals(type)){//返回真名
        	if(memberBase.getSubMemberType() != null && 11==memberBase.getSubMemberType()){//当为investor 个人时
    			String hqlTmp = "from MemberIndividual r where r.member.id = ? ";
    			List paramsTmp = new ArrayList();
    			paramsTmp.add(memberBase.getId());
    			List<MemberIndividual> listTmp = this.baseDao.find(hqlTmp, paramsTmp.toArray(), false);
    			if(null!=listTmp && !listTmp.isEmpty()){
    				MemberIndividual objTmp = listTmp.get(0);
    				String enName = objTmp.getFirstName() == null ? "":objTmp.getFirstName();
    				if(objTmp.getLastName() != null && !"".equals(objTmp.getLastName())){
    					enName +=" "+objTmp.getLastName();
    				}
    				String chiName = objTmp.getNameChn();
    				if(chiName == null || "".equals(chiName)){
    					chiName = enName;
    				}
    				if(langCode.equals(CommonConstants.LANG_CODE_EN)){
    					returnName = enName;
    				}else{// 中文版：显示中文名，如果没，则显示英文名
    					returnName = chiName;
    				}
    				if("".equals(returnName)){
    					if(memberBase.getNickName() != null && !"".equals(memberBase.getNickName())){
    						returnName = memberBase.getNickName();
    					}else{
    						returnName = memberBase.getLoginCode();
    					}
    				}
    			}	
        	}else if(memberBase.getSubMemberType() != null && 21==memberBase.getSubMemberType()){//当为ifa时
    			String hqlTmp = "from MemberIfa r where r.member.id = ? ";
    			List paramsTmp = new ArrayList();
    			paramsTmp.add(memberBase.getId());
    			List<MemberIfa> listTmp = this.baseDao.find(hqlTmp, paramsTmp.toArray(), false);
    			if(null!=listTmp && !listTmp.isEmpty()){
    				MemberIfa objTmp = listTmp.get(0);
    				String enName = objTmp.getFirstName() == null ? "":objTmp.getFirstName();
    				if(objTmp.getLastName() != null && !"".equals(objTmp.getLastName())){
    					enName +=" "+objTmp.getLastName();
    				}
    				String chiName = objTmp.getNameChn();
    				if(chiName == null || "".equals(chiName)){
    					chiName = enName;
    				}
    				if(langCode.equals(CommonConstants.LANG_CODE_EN)){
    					returnName = enName;
    				}else{// 中文版：显示中文名，如果没，则显示英文名
    					returnName = chiName;
    				}
    				if("".equals(returnName)){
    					if(memberBase.getNickName() != null && !"".equals(memberBase.getNickName())){
    						returnName = memberBase.getNickName();
    					}else{
    						returnName = memberBase.getLoginCode();
    					}
    				}
    			}
        	}
    	}else{
        	if(memberBase.getSubMemberType() != null && 11==memberBase.getSubMemberType()){//当为investor 个人时
    			String hqlTmp = "from MemberIndividual r where r.member.id = ? ";
    			List paramsTmp = new ArrayList();
    			paramsTmp.add(memberBase.getId());
    			List<MemberIndividual> listTmp = this.baseDao.find(hqlTmp, paramsTmp.toArray(), false);
    			if(null!=listTmp && !listTmp.isEmpty()){
    				MemberIndividual objTmp = listTmp.get(0);
    				String enName = objTmp.getFirstName() == null ? "":objTmp.getFirstName();
    				if(objTmp.getLastName() != null && !"".equals(objTmp.getLastName())){
    					enName +=" "+objTmp.getLastName();
    				}
    				String chiName = objTmp.getNameChn();
    				if(chiName == null || "".equals(chiName)){
    					chiName = enName;
    				}
    				if(langCode.equals(CommonConstants.LANG_CODE_EN)){
    					if(!"".equals(enName) && !"".equals(chiName)){//同时有中文和英文
    						returnName = enName+"("+chiName+")";
    					}else if(!"".equals(enName) && "".equals(chiName)){//只有英文
    						returnName = enName;
    					}else if("".equals(enName) && !"".equals(chiName)){//只有中文
    						returnName = chiName;
    					}
    				}else{// 中文版：显示中文名，如果没，则显示英文名
    					if(!"".equals(chiName)){
    						returnName = chiName;
    					}else{
    						returnName = enName;
    					}
    				}
    				if("".equals(returnName)){
    					if(memberBase.getNickName() != null && !"".equals(memberBase.getNickName())){
    						returnName = memberBase.getNickName();
    					}else{
    						returnName = memberBase.getLoginCode();
    					}
    				}
    			}	
        	}else if(memberBase.getSubMemberType() != null && 21==memberBase.getSubMemberType()){//当为ifa时
    			String hqlTmp = "from MemberIfa r where r.member.id = ? ";
    			List paramsTmp = new ArrayList();
    			paramsTmp.add(memberBase.getId());
    			List<MemberIfa> listTmp = this.baseDao.find(hqlTmp, paramsTmp.toArray(), false);
    			if(null!=listTmp && !listTmp.isEmpty()){
    				MemberIfa objTmp = listTmp.get(0);
    				String enName = objTmp.getFirstName() == null ? "":objTmp.getFirstName();
    				if(objTmp.getLastName() != null && !"".equals(objTmp.getLastName())){
    					enName +=" "+objTmp.getLastName();
    				}
    				String chiName = objTmp.getNameChn();
    				if(chiName == null || "".equals(chiName)){
    					chiName = enName;
    				}
    				if(langCode.equals(CommonConstants.LANG_CODE_EN)){
    					if(!"".equals(enName) && !"".equals(chiName)){//同时有中文和英文
    						returnName = enName+"("+chiName+")";
    					}else if(!"".equals(enName) && "".equals(chiName)){//只有英文
    						returnName = enName;
    					}else if("".equals(enName) && !"".equals(chiName)){//只有中文
    						returnName = chiName;
    					}
    				}else{// 中文版：显示中文名，如果没，则显示英文名
    					if(!"".equals(chiName)){
    						returnName = chiName;
    					}else{
    						returnName = enName;
    					}
    				}
    				if("".equals(returnName)){
    					if(memberBase.getNickName() != null && !"".equals(memberBase.getNickName())){
    						returnName = memberBase.getNickName();
    					}else{
    						returnName = memberBase.getLoginCode();
    					}
    				}
    			}
        	}
    	}
    	return returnName;
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
}
