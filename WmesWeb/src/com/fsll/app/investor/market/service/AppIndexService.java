/**
 * 
 */
package com.fsll.app.investor.market.service;


import java.util.List;

import com.fsll.app.investor.market.vo.AppIndexBbsTopicVO;
import com.fsll.app.investor.market.vo.AppIndexHotVO;
import com.fsll.app.investor.market.vo.AppIndexNoMissVo;
import com.fsll.app.investor.market.vo.AppIndexTopCategoryVO;
import com.fsll.app.investor.market.vo.AppNoticeItemVO;


/**
 * 首页接口服务
 * @author zpzhou
 * @date 2016-10-31
 */
public interface AppIndexService {
	
	/**
	 * 得到首页组合中用到最多的基金的行情图表数据
	 * @param periodCode 分析时间段编码：1WK：一周，1Mth：一个月...，1Yr：一年 ...
	 * @param langCode 显示语言
	 * @param num 返回条数
	 * @return	<List>AppIndexHotVO	基金数据
	 */
	public List<AppIndexHotVO> findIndexHotFund(String langCode,String periodCode,int num);
	
	/**
	 * 得到首页热门投资中收益最多的组合
	 * @param periodCode 分析时间段编码：1WK：一周，1Mth：一个月...，1Yr：一年 ...
	 * @param langCode 显示语言
	 * @param num 返回条数
	 * @return	<List>AppIndexHotVO	组合数据
	 */
	public List<AppIndexHotVO> findIndexHotPortfolio(String memberId,String langCode,String periodCode,int num);
	/**
	 * 得到首页不容错过信息
	 * @param periodCode 分析时间段编码：1WK：一周，1Mth：一个月...，1Yr：一年 ...
	 * @param langCode 语言
	 * @return
	 */
	public List<AppIndexNoMissVo> findIndexNoMiss(String memberId,String langCode,String period);
	/**
	 * 你的选择
	 * @param langCode  语言
	 * @return
	 */
	public List<AppIndexTopCategoryVO> findIndexTopCategory(String langCode);
	/**
	 * 得到首页热门话题列表信息
	 * @param typeId 类型ID  0:最热门  1：推荐  2:模块
	 * @param moduleId 模块ID  当typeId=2时不能为空
	 * @param num 返回条数
	 * @return
	 */
	public List<AppIndexBbsTopicVO> findIndexTopicList(String typeId,String moduleId,int num);
	
	/**
	 * 获取公告列表
	 * @author zxtan
	 * @date 2017-04-07
	 * @param memberId
	 * @return
	 */
	public List<AppNoticeItemVO> findNoticeList(String memberId,int num);
}
