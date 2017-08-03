package com.fsll.app.common.service;

import java.util.List;

import com.fsll.app.common.vo.AppSysParamVO;
import com.fsll.app.investor.me.vo.AppIfaSearchItemVO;
import com.fsll.common.util.JsonPaging;

/***
 * 业务接口类：语言设置管理
 * 
 * @author simon
 * @date 2016-3-17
 */
public interface AppSysParamService {
	
	/**
	 * 分页获取基础参数列表
	 * @author michael
	 * @param jsonPaging
	 * @param langCode
	 * @param typeCode
	 * @return
	 */
	public JsonPaging getParamList(JsonPaging jsonPaging, String langCode, String typeCode);
	
	/***
	 * 根据站点名称查询站点对象
	 * @author mjhuang
	 * @param langCode
	 * @param configCode
	 * @return 
	 */
	public AppSysParamVO findParamByCode(String langCode, String configCode);
	/**
	 * 根据参数得到基础数据列表，并转换成vo
	 * @param langCode  //语言
	 * @param typeCode  //类型
	 * @return
	 */
	public List<AppIfaSearchItemVO> getParamListByType(String langCode, String typeCode);

	
}
