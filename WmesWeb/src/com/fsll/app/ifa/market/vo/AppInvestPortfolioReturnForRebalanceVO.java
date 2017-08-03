package com.fsll.app.ifa.market.vo;

import java.util.List;

import com.fsll.buscore.fund.vo.CoreFundNavVO;

/**
 * 组合分析 实体类VO
 * @author zxtan
 * @date 2017-02-26
 */
public class AppInvestPortfolioReturnForRebalanceVO {
	private List<CoreFundNavVO> holdReturnList;//组合持仓回报
	private List<CoreFundNavVO> adjustReturnList;//组合调整后回报
		

	public List<CoreFundNavVO> getHoldReturnList() {
		return holdReturnList;
	}
	public void setHoldReturnList(List<CoreFundNavVO> holdReturnList) {
		this.holdReturnList = holdReturnList;
	}
	public List<CoreFundNavVO> getAdjustReturnList() {
		return adjustReturnList;
	}
	public void setAdjustReturnList(List<CoreFundNavVO> adjustReturnList) {
		this.adjustReturnList = adjustReturnList;
	}		
}
