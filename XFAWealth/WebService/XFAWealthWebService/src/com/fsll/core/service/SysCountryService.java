package com.fsll.core.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysCountry;
import com.fsll.core.vo.IfaSearchItemVO;
/***
 * 业务接口类：国家管理
 * @author michael
 * @date 2016-7-11
 */
public interface SysCountryService {
    /***
     * 查找所有
     * @author michael
     * @date 2016-07-11
	 * @param code
	 * @param name
	 * @return jsonpaging
     */
	public JsonPaging findAll(JsonPaging jsonpaging,String code, String name);

	 /***
     * 查询所有有效的类型
     * @author michael
     * @date 2016-07-11
     * @return list
     */
	public List<SysCountry> findAll();
	
	/***
	 * 保存对象的方法
	 * @author michael
	 * @date 2016-7-11
	 * @param country
	 * @param isAdd
	 * @return SysCountry
	 */
	public SysCountry saveOrUpdate(SysCountry country,boolean isAdd);
	
	/***
	 * 删除对象的方法
	 * @author michael
	 * @date 2016-7-11
	 * @param ids
	 * @return 
	 */
	public boolean deleteObject(String ids);
	
	/***
	 * 根据id查询对象
	 * @author michael
	 * @date 2016-7-11
	 * @param id
	 * @return SysCountry
	 */
	public SysCountry findById(String id);
	
	/***
	 * 根据编码查询对象
	 * @author michael
	 * @date 2016-7-11
	 * @param code
	 * @return SysCountry
	 */
	public SysCountry findByCode(String code);
    
	 /***
     * 通过地区编码查国家信息
     * @author michael
     * @date 2016-07-11
	 * @param code
     */
	public List<SysCountry> findByAreaCode(String code);
	
	/**
	 * 查询所有国家信息，并转换成VO
	 * @param langCode
	 * @return
	 * @author zpzhou
     * @date 2016-07-25
	 */
	public List<IfaSearchItemVO> findAllCountryList(String langCode);
	
	/***
     * 通过id获取Country对象
     * @author wwluo
     * @date 2016-08-22
	 * @param countryId
     */
	public SysCountry findBycountryId(String countryId);
	
	/***
	 * 通过  id、lang 获取国家名称countryName
	 * @author wwluo
	 * @date 2016-08-22
	 * @param countryId
	 * @param lang
	 */
	public String findBycountryId(String countryId ,String lang);
}
