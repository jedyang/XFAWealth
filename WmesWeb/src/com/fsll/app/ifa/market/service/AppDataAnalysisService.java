package com.fsll.app.ifa.market.service;

import java.util.List;

import com.fsll.app.ifa.market.vo.AppPieChartItemVO;

/**
 * IFA市场——统计与分析
 * @author zxtan
 * @date 2017-04-01
 */
public interface AppDataAnalysisService {

	/**
	 * 获取客户回报分析
	 * @author zxtan
	 * @date 2017-04-01
	 * @param ifaMemberId
	 * @param langCode
	 * @return
	 */
	public List<AppPieChartItemVO> findReturnAnalysis(String ifaMemberId,String langCode);
	
	/**
	 * 获取客户资产分析
	 * @author zxtan
	 * @date 2017-04-01
	 * @param ifaMemberId
	 * @param langCode
	 * @return
	 */
	public List<AppPieChartItemVO> findAssetAnalysis(String ifaMemberId,String langCode);
	
	/**
	 * 获取客户地域分析
	 * @author zxtan
	 * @date 2017-04-01
	 * @param ifaMemberId
	 * @param langCode
	 * @return
	 */
	public List<AppPieChartItemVO> findGeoAnalysis(String ifaMemberId,String langCode);
}
