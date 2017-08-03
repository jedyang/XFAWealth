package com.fsll.wmes.stock.service;

import java.util.List;
import com.fsll.wmes.stock.vo.StockProductVO;

/**
 * 股票信息管理
 * @author Yan
 * @date 2016-12-09
 */
public interface StockInfoService {
	
	/**
	 * 获取股票基础数据列表
	 * @return
	 */
	public List<StockProductVO> getStockProductVoList(String langCode);

}
