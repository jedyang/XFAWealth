package com.fsll.core.service;

import java.io.IOException;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysLangConfig;
import com.fsll.core.vo.SysMutilLangVo;

public interface SysMutilLangService {
	/**
	 * 多语言列表
	 * @author scshi_u330p
	 * @param jsonPaging 
	 * @param sVo 
	 * @date 20161229
	 * */
	public JsonPaging loadMutilLangPage(JsonPaging jsonPaging, SysLangConfig sVo);
	
	/**
	 *初始化 sys_lang_config表
	 *@author scshi_u330p
	 *@date 20161229
	 * */
	public void readPropertyToDb();
	
	/**
	 * 到处数据表到配置文件
	 * @author stxxx
	 * @date 20161229
	 * */
	public void exportDbToProperty();

	/**
	 * 配置文件增改
	 * @author scshi_u330p
	 * @date 20170102
	 * */
	public SysLangConfig loadLangConfigById(String id);
	
	/**
	 * 配置文件保存
	 * @author scshi_u330p
	 * @date 20170102
	 * */
	public void propSave(SysLangConfig langObj);

}
