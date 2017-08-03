/**
 * 
 */
package com.fsll.app.investor.me.service;

import java.util.List;

import com.fsll.app.investor.me.vo.AppCrmProposalMessVo;
import com.fsll.app.investor.me.vo.AppPortfolioMessVo;



/**
 * 我的客户的投资方案接口服务接口类
 * @author zpzhou
 * @date 2016-9-22
 */
public interface AppCrmProposalMessService {
	
	/**
	 * 得到我的投资方案列表
	 * @param memberId 用户ID
	 * @param ifaId 
	 * @param keyWord 搜索关键词
	 * @param toCurrency 货币转换参数
	 * @return
	 */
	public List<AppCrmProposalMessVo> findMyProposalList(String memberId,String ifaId,String keyWord,String toCurrency,String langCode);
	/**
	 * 得到投资方案基本信息
	 * @param proposalId 方案ID
	 * @return
	 */
	public AppCrmProposalMessVo findMyProposalMess(String proposalId,String toCurrency,String langCode);
	
	/**
	 * 得到某个方案下的投资组合列表
	 * @param proposalId 方案ID
	 * @return
	 */
	public List<AppPortfolioMessVo> findProposalPortfolioList(String proposalId,String toCurrency,String langCode);
	
	/**
	 * 【我的投资顾问】得到投资方案列表
	 * @author zxtan
	 * @date 2017-01-17
	 * @param memberId 用户ID
	 * @param ifaId 
	 * @param toCurrency 货币转换参数
	 * @return
	 */
	public List<AppCrmProposalMessVo> findMyIfaProposalList(String memberId,String ifaId,String toCurrency,String langCode);
	
	/**
	 * 更新方案状态
	 * @author zxtan
	 * @date 2017-03-06
	 * @param proposalId
	 * @return
	 */
	public boolean updateProposalConfirm(String proposalId,String status,String content,String ip);
}
