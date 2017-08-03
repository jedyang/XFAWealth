package com.fsll.wmes.bond.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.bond.vo.BondAskListVO;
import com.fsll.wmes.bond.vo.BondProductVO;
import com.fsll.wmes.entity.BondAsk;
import com.fsll.wmes.stock.vo.StockProductVO;

/**
 * 债券信息管理
 * @author Yan
 * @date 2016-12-09
 */
public interface BondInfoService {
	
	/**
	 * 获取债券基础数据列表
	 * @return
	 */
	public List<BondProductVO> getBondProductVoList(String langCode);

	/**
	 * 获取债券报价列表（未报价）
	 * @author mqzou 2017-06-28
	 * @param jsonPaging
	 * @param currency
	 * @param keyword
	 * @return
	 */
	public JsonPaging  findBondAskList(JsonPaging jsonPaging,String keyword,String langCode);
	
	/**
	 * 获取债券报价历史列表
	 * @author mqzou 2017-06-28
	 * @param jsonPaging
	 * @param currency
	 * @param keyword
	 * @param status
	 * @param orderType
	 * @return
	 */
	public JsonPaging findBondAskHistoryList(JsonPaging jsonPaging,String keyword,String status,String orderType,String langCode,String startDate,String endDate);
	
	/**
	 * 获取债券报价详情
	 * @author mqzou 2017-06-28
	 * @param id
	 * @param langCode
	 * @return
	 */
	public BondAskListVO findBondAskDetail(String id,String langCode);
	
	/**
	 * 获取债券报价内容
	 * @author mqzou 2017-06-28
	 * @param id
	 * @return
	 */
	public BondAsk findById(String id);
	
	/**
	 * 保存债券报价信息
	 * @author mqzou 2017-06-28
	 * @param ask
	 * @return
	 */
	public BondAsk saveBondAsk(BondAsk ask);
}
