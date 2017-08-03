/**
 * 
 */
package com.fsll.wmes.ifafirm.service;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.IfafirmFee;

/**
 * 公司费用接口
 * @author Yan
 * @date 2016-12-19
 */
public interface IfafirmFeeService {
	
	/**
	 * 通过ID查找一条信息
	 * @param id
	 * @return
	 */
	public IfafirmFee findById(String id);
	
	/**
	 * 查找ifafirm的费用设置信息(取有效数据)
	 * @param ifafirmId
	 * @param ifafirmName
	 * @param langCode
	 * @return
	 */
	//@Transactional(readOnly = true)
	public JsonPaging getList(JsonPaging jsonPaging, String ifafirmId, String ifafirmName, String langCode);
	
	/**
	 * 查找ifafirm的费用设置
	 * @param jsonPaging
	 * @param ifafirmId
	 * @param ifafirmName
	 * @param langCode
	 * @param status
	 * @return
	 */
	public JsonPaging getList(JsonPaging jsonPaging, String ifafirmId, String ifafirmName, String langCode, String status);
	
	/**
	 * 保存信息
	 * @param info
	 * @return
	 */
	public IfafirmFee saveOrUpdate(IfafirmFee info, boolean isAdd);
	
	/**
	 * 通过ID删除一条记录
	 * @param id
	 * @return
	 */
	public boolean deleteById(String id);
}
