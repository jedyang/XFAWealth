/**
 * 
 */
package com.fsll.wmes.fund.service;


import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.FundFeesItem;
import com.fsll.wmes.entity.FundFeesItemEn;
import com.fsll.wmes.entity.FundFeesItemSc;
import com.fsll.wmes.entity.FundFeesItemTc;
import com.fsll.wmes.fund.vo.FundChargesDataVO;
import com.fsll.wmes.fund.vo.FundFeesItemVO;

/**
 * @author tan
 * 基金管理费用信息接口
 * @date 20160629
 */
public interface FundFeesItemService {

	/**
	 * 根据id查找英文实体内容
	 * @param id
	 * @return
	 */
	public Object findById(String id,String langCode); 
	
	
//	/**
//	 * 根据id查找英文实体内容
//	 * @param id
//	 * @return
//	 */
//	public FundFeesItemEn findById4En(String id); 
//	
//	/**
//	 * 根据id查找简体实体内容
//	 * @param id
//	 * @return
//	 */
//	public FundFeesItemSc findById4Sc(String id); 
//	
//	/**
//	 * 根据id查找繁体实体内容
//	 * @param id
//	 * @return
//	 */
//	public FundFeesItemTc findById4Tc(String id); 
	
    /***
     * 分页查询记录
     * @param jsonpaging 分页参数
     * @param fundInfo 查询参数
	 * @return
     */
	public JsonPaging findAll(JsonPaging jsonpaging,String fundId,String feesItem,String langCode);

	/**
	 * 修改状态
	 * @param ids
	 * @param status
	 * @return
	 */
	public Boolean saveStatus(String ids, String status);

	
	
//	/**
//	 * 创建或更新
//	 * @param obj 实体对象
//	 * @return
//	 */
//	public FundFeesItem saveOrUpdate(FundFeesItem obj); 
//	
//	/**
//	 * 创建或更新
//	 * @param obj 实体对象
//	 * @return
//	 */
//	public FundFeesItemEn saveOrUpdate4En(FundFeesItemEn obj,Boolean isAdd); 
//	
//	/**
//	 * 创建或更新
//	 * @param obj 实体对象
//	 * @return
//	 */
//	public FundFeesItemSc saveOrUpdate4Sc(FundFeesItemSc obj,Boolean isAdd); 
//	
//	/**
//	 * 创建或更新
//	 * @param obj 实体对象
//	 * @return
//	 */
//	public FundFeesItemTc saveOrUpdate4Tc(FundFeesItemTc obj,Boolean isAdd);


	public Object saveOrUpdate(Object obj, Boolean isAdd); 

	/**3.1.8	获取基金管理费用信息
	 * @author scshi
	 * @param fundId 资金id
	 * @param langCode 语言
	 * @return	<List>FundChargesDataVO	基金管理费用信息
	 */
	public List<FundChargesDataVO> fundChargesInfo(String fundId,String langCode);

}
