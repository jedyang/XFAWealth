package com.fsll.wmes.portfolio.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.PortfolioArena;
import com.fsll.wmes.portfolio.vo.CorePortfolioVO;
import com.fsll.wmes.portfolio.vo.PortfolioArenaVO;

public interface PortfolioArenaService {

	/**
	 * 获取组合的列表分页
	 * @author mqzou 2017-06-02
	 * @param jsonPaging
	 * @param keyWord
	 * @param riskLevel
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	public JsonPaging findPortfolioList(JsonPaging jsonPaging,String keyWord,String riskLevel, String langCode);
	
	/**
	 * 获取组合详情
	 * @author mqzou  2017-06-05
	 * @param id
	 * @return
	 */
	public PortfolioArenaVO findPortfolioDetial(String id,String langCode);
	
	/**
	 * 获取组合信息
	 * @author mqzou  2017-06-05
	 * @param id
	 * @return
	 */
	public PortfolioArena findById(String id);
	
	/**
	 * 获取指定时间范围内的组合累计收益数据
	 * @param portfolioId 组合id
	 * @param frequencyType 频率  1W=1周,2W=2周,1M=1月,3M=3月,6M=6月,YTD=年初至今,1Y=1年,3Y=3年,5Y=5年
	 * @param 货币
	 * @return
	 */
	public List<CorePortfolioVO> getPortfolioReturnRate(String portfolioId,String frequencyType,String currency);
	
	/**
	 * 获取投资组合产品比重列表（用于图表显示）
	 * @author mqzou 
	 * @date 2016-09-13
	 * @param type
	 * @param portfolioId
	 * @return
	 */
	public List findPortfolioProductRate(String type,String portfolioId,String langCode);

}
