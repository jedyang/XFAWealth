package com.fsll.wmes.stock.service;

import java.util.List;

import com.fsll.wmes.entity.StockInfo;
import com.fsll.wmes.entity.StockInfoEn;
import com.fsll.wmes.entity.StockInfoSc;
import com.fsll.wmes.entity.StockInfoTc;
import com.fsll.wmes.stock.vo.StockInfoVO;
import com.fsll.wmes.stock.vo.StockProductVO;

/**
 * 股票信息管理
 * @author michael
 * @date 2016-12-09
 */
public interface StockInfoService {
	
	/**
	 * 获取股票基础数据列表
	 * @return
	 */
	public List<StockProductVO> getStockProductVoList(String langCode);

	/**
	 * 通过ID查找一条股票基本信息
	 * @param id
	 * @return
	 */
	public StockInfo findStockInfoById(String id);
	
	/**
	 * 通过ID查找一条股票附加英文信息
	 * @param id
	 * @return
	 */
	public StockInfoEn findStockInfoEnById(String id);
	
	/**
	 * 通过ID查找一条股票附加简体中文信息
	 * @param id
	 * @return
	 */
	public StockInfoSc findStockInfoScById(String id);
	
	/**
	 * 通过ID查找一条股票附加繁体中文信息
	 * @param id
	 * @return
	 */
	public StockInfoTc findStockInfoTcById(String id);
	
	/**
	 * 通过ID查找股票全部信息
	 * @param id
	 * @param langCode 语言
	 * @return
	 */
	public StockInfoVO findStockFullInfoById(String id, String langCode);
	
	/**
	 * 通过ID查找股票全部信息（含多语言）
	 * @param id
	 * @return
	 */
	public StockInfoVO findStockFullInfoById(String id);
	
	/**
	 * 根据产品ID获取股票信息（股票的基表信息与股票的多语言信息）
	 * @author 林文伟
	 * @param productId 产品ID
	 * @return
	 */
	public StockInfo getStockInfoByProductId(String productId,String langCode);
}
