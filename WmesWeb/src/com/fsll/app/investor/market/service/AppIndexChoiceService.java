/**
 * 
 */
package com.fsll.app.investor.market.service;


import java.util.List;

import com.fsll.app.investor.market.vo.AppIndexFundTopVO;
import com.fsll.app.investor.market.vo.AppIndexTopCategoryVO;

/**
 * 首页你的选择接口服务
 * @author zpzhou
 * @date 2016-9-26
 */
public interface AppIndexChoiceService {
	/**
	 * 你的选择
	 * @param langCode  语言
	 * @return
	 */
	public List<AppIndexTopCategoryVO> findIndexTopCategory(String langCode);
	
	/**
	 * 得到首页最佳基金数据
	 * @param period 时间类型
	 * @param langCode  语言
	 * @param num 返回条数
	 * @return
	 */
	public List<AppIndexFundTopVO> findIndexFundTop(String period,String langCode,int num);
}
