package com.fsll.wmes.web.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysCountry;
import com.fsll.core.vo.SysCountryVO;
import com.fsll.wmes.fund.vo.GeneralDataVO;

/***
 * 业务接口类：国家管理
 * @author wwluo
 * @date 2016-08-03
 */
public interface CountryService {
	
	/***
     * 查询国家列表
     * @author wwluo
     * @date 2016-08-03
     */
	public JsonPaging findAll(JsonPaging jsonpaging, String keyWord);
	
	/***
     * 保存国家信息（save or update）
     * @author wwluo
     * @date 2016-08-03
     */
	public boolean saveOrUpdate(SysCountry sysCountry);
	
	/***
     * 根据id查找sysCountry实体
     * @author wwluo
     * @date 2016-08-03
     */
	public SysCountryVO findById(String id);
	
	/***
     * 根据id删除sysCountry实体
     * @author wwluo
     * @date 2016-08-03
     */
	public void deleteById(String id);
	
	/**
	 * 查找国家列表
	 * @param keyWord 关键字
	 * @param langCode 语言
	 * @author qgfeng
	 * @return
	 */
	public List<GeneralDataVO> findCountryList(String keyWord, String langCode);

}
