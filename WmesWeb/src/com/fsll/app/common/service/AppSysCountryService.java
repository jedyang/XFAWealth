package com.fsll.app.common.service;

import java.util.List;

import com.fsll.app.investor.me.vo.AppIfaSearchItemVO;
import com.fsll.common.util.JsonPaging;
/***
 * 业务接口类：国家管理
 * @author zpzhou
 * @date 2016-7-11
 */
public interface AppSysCountryService {
    /***
     * 查找所有
	 * @param code
	 * @param name
	 * @return jsonpaging
     */
	public JsonPaging findAll(JsonPaging jsonpaging,String code, String name);
	
	/**
	 * 查询所有国家信息，并转换成VO
	 * @param langCode
	 * @return
	 */
	public List<AppIfaSearchItemVO> findAllCountryList(String langCode);

	
}
