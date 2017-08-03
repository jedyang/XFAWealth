package com.fsll.core.service.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PropertyUtils;
import com.fsll.core.entity.SysLangConfig;
import com.fsll.core.service.SysMutilLangService;

@Service("sysMutilLangService")
//@Transactional
public class SysMutilLangServiceImpl extends BaseService implements SysMutilLangService {
	
	/**SysMutilLangVo
	 * 多语言列表
	 * @author scshi_u330p
	 * @date 20161229
	 * */
	@Override
	public JsonPaging loadMutilLangPage(JsonPaging jsonPaging,SysLangConfig sVo) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(" from SysLangConfig t where 1=1 ");
		
		if(StringUtils.isNotBlank(sVo.getKeyCode())){
			sb.append(" and t.keyCode like ? ");
			params.add("%"+sVo.getKeyCode()+"%");
		}
		
		if(StringUtils.isNotBlank(sVo.getValueEn())){
			sb.append(" and t.valueEn like ? ");
			params.add("%"+sVo.getValueEn()+"%");
		}
		
		if(StringUtils.isNotBlank(sVo.getValueSc())){
			sb.append(" and t.valueSc like ? ");
			params.add("%"+sVo.getValueSc()+"%");
		}
		
		if(StringUtils.isNotBlank(sVo.getValueTc())){
			sb.append(" and t.valueTc like ? ");
			params.add("%"+sVo.getValueTc()+"%");
		}
		
		if(StringUtils.isNotBlank(sVo.getNullEn())){
			if("1".equals(sVo.getNullEn())){
				sb.append(" and t.valueEn is null ");
			}else{
				sb.append(" and t.valueEn is not null");
			}
		}
		
		if(StringUtils.isNotBlank(sVo.getNullSc())){
			if("1".equals(sVo.getNullSc())){
				sb.append(" and t.valueSc is null");
			}else{
				sb.append(" and t.valueSc is not null");
			}
		}
		
		if(StringUtils.isNotBlank(sVo.getNullTc())){
			if("1".equals(sVo.getNullTc())){
				sb.append(" and t.valueTc is null");
			}else{
				sb.append(" and t.valueTc is not null");
			}
		}
		
		sb.append(" order by t.orderBy,t.lastUpdate desc ");
		
		jsonPaging = this.baseDao.selectJsonPaging(sb.toString(), params.toArray(), jsonPaging, false);
		
		return jsonPaging;
	}
	
	
	/**
	 *初始化 sys_lang_config表
	 *@author scshi_u330p
	 *@date 20161229
	 * */
	public void readPropertyToDb(){
		//清空表数据
		StringBuffer clearHql = new StringBuffer("delete from SysLangConfig ");
		this.baseDao.updateHql(clearHql.toString(),null);
		Properties properties = PropertyUtils.getPropertyByLang(CommonConstants.LANG_CODE_EN, "");
		Map<String,String> map = PropertyUtils.getPropertyMap(properties);
		if(!map.isEmpty()){
			for(String key:map.keySet()){
				SysLangConfig langObj = new SysLangConfig();
				String enValue = map.get(key);
				String scValue = PropertyUtils.getPropertyValue("sc",key,null);
				String tcValue = PropertyUtils.getPropertyValue("tc", key, null);
				langObj.setId(null);
				langObj.setCreateTime(new Date());
				langObj.setIsValid("1");
				langObj.setKeyCode(key);
				langObj.setLastUpdate(new Date());
				langObj.setValueEn("".equals(enValue)?null:enValue);
				langObj.setValueSc("".equals(scValue)?null:scValue);
				langObj.setValueTc("".equals(tcValue)?null:tcValue);
				this.baseDao.saveOrUpdate(langObj);
			}
		}
	}
	
	/**
	 * 配置文件增改
	 * @author scshi_u330p
	 * @date 20170102
	 * */
	public SysLangConfig loadLangConfigById(String id){
		SysLangConfig obj = (SysLangConfig)this.baseDao.get(SysLangConfig.class, id);
		return obj;
	}
	
	/**
	 * 配置文件保存--生成实体
	 * @author scshi_u330p
	 * @date 20170102
	 * */
	public void propSave(SysLangConfig langObj){
		if(StringUtils.isBlank(langObj.getId())){
			 langObj.setId(null);
		} 
		langObj.setCreateTime(StringUtils.isBlank(langObj.getCreateTimeStr())?new Date():DateUtil.StringToDateFormat(langObj.getCreateTimeStr(), "yyyy-MM-dd HH:mm:ss"));
		langObj.setLastUpdate(new Date());
		langObj.setIsValid("1");
		this.baseDao.saveOrUpdate(langObj);
		
		String basePath = PropertyUtils.class.getResource("/").getPath(); 
		Properties prop = new Properties();
		//英文配置文件保存
		this.propSaveSub(basePath,prop,"en",langObj);
		//繁体配置文件保存
		this.propSaveSub(basePath,prop,"tc",langObj);
		//简体配置文件保存
		this.propSaveSub(basePath,prop,"sc",langObj);
		
	}
	
	/**
	 * 配置文件保存--写入数据到文件
	 * @author scshi_u330p
	 * @date 20170102
	 * */
	private void propSaveSub(String basePath, Properties prop, String lang,SysLangConfig langObj) {
		// TODO Auto-generated method stub
		String propValue = langObj.getValueEn();
		String filePath = CommonConstants.LANG_PATH_EN;
		try{
			if(lang.equalsIgnoreCase(CommonConstants.LANG_CODE_SC)){
				filePath = CommonConstants.LANG_PATH_SC;
				propValue = URLDecoder.decode(langObj.getValueSc().replaceAll("%", "%25"), "utf-8");
			}else if(lang.equalsIgnoreCase(CommonConstants.LANG_CODE_TC)){
				filePath = CommonConstants.LANG_PATH_TC;
				propValue = URLDecoder.decode(langObj.getValueTc().replaceAll("%", "%25"), "utf-8");
			}
			Map toSaveMap = new HashMap();
			InputStream in = new FileInputStream(basePath+filePath);
			prop.load(in);
			OutputStream propOut = new FileOutputStream(basePath+filePath);
			Set keys = prop.keySet();
			for(Iterator itr = keys.iterator(); itr.hasNext();){
				String key = (String) itr.next();
				Object value = prop.get(key);
				toSaveMap.put(key, value);
			}
			toSaveMap.put(langObj.getKeyCode(), propValue);
			prop.clear();
			prop.putAll(toSaveMap);
			prop.store(propOut, "====after modify or save====");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 导出数据表到配置文件
	 * @author stxxx
	 * @date 20161229
	 * */
	public void exportDbToProperty(){
		StringBuffer expHql = new StringBuffer(" from SysLangConfig t where t.isValid='1' ");
		List<SysLangConfig> list = this.baseDao.find(expHql.toString(), null, false);
		Map toSaveScProp = new HashMap();
		Map toSaveEnProp = new HashMap();
		Map toSaveTcProp = new HashMap();
		if(!list.isEmpty()){
			for(int x=0;x<list.size();x++){
				SysLangConfig obj = list.get(x);
				String keyCode = obj.getKeyCode();
				String valueSc = StringUtils.isBlank(obj.getValueSc())?"":URLDecoder.decode(obj.getValueSc().replaceAll("%", "%25"));
				String valueTc = StringUtils.isBlank(obj.getValueTc())?"":URLDecoder.decode(obj.getValueTc().replaceAll("%", "%25"));
				String valueEn = obj.getValueEn();
				toSaveScProp.put(keyCode, valueSc);
				toSaveEnProp.put(keyCode, valueEn);
				toSaveTcProp.put(keyCode, valueTc);
			}
			String basePath = PropertyUtils.class.getResource("/").getPath(); 
			this.exportToProp(toSaveScProp,basePath,"sc");
			this.exportToProp(toSaveEnProp,basePath,"en");
			this.exportToProp(toSaveTcProp,basePath,"tc");
		}
	}
	
	
	/**
	 * 导出数据表到配置文件--子方法
	 * @author stxxx
	 * @date 20161229
	 * */
	private void  exportToProp(Map toSaveProp,String basePath,String lang){
		String filePath = CommonConstants.LANG_PATH_EN;
		if(lang.equalsIgnoreCase(CommonConstants.LANG_CODE_SC)){
			filePath = CommonConstants.LANG_PATH_SC;
		}else if(lang.equalsIgnoreCase(CommonConstants.LANG_CODE_TC)){
			filePath = CommonConstants.LANG_PATH_TC;
		}
		Properties prop = new Properties();
		try{
			OutputStream propOut = new FileOutputStream(basePath+filePath);
			prop.putAll(toSaveProp);
			prop.store(propOut, "==== after export====");
		}catch(Exception e ){
			e.printStackTrace();
		}
	}

}
