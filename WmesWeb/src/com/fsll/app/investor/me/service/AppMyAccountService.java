package com.fsll.app.investor.me.service;

import java.util.List;

import net.sf.json.JSONArray;

import com.fsll.app.investor.me.vo.AppMyAccountBaseInfoVO;
import com.fsll.app.investor.me.vo.AppMyAccountDocInfoVO;
import com.fsll.app.investor.me.vo.AppMyAccountInfoVO;
import com.fsll.app.investor.me.vo.AppMyAccountRpqInfoVO;
import com.fsll.app.investor.me.vo.AppMyAccountVO;
import com.fsll.app.investor.me.vo.AppPortfolioOrderHistoryVO;
import com.fsll.app.investor.me.vo.AppRpqExamQuestVO;
import com.fsll.app.investor.me.vo.AppRpqExamResultVO;

public interface AppMyAccountService {
	
	/**
	 * 获取账户信息列表
	 * @author zxtan
	 * @date 2017-01-12
	 * @return
	 */
	public List<AppMyAccountVO> findMyAccountList(String memberId,String langCode,String toCurrency,String openStatus);
	
	/**
	 * 获取账户信息
	 * @author zxtan
	 * @date 2017-01-12
	 * @return
	 */
	public AppMyAccountInfoVO findAccountInfo(String accountId,String langCode,String toCurrency);
	
	/**
	 * 获取账户RPQ信息
	 * @author zxtan
	 * @date 2017-01-16
	 * @return
	 */
	public AppMyAccountRpqInfoVO findAccountRPQInfo(String accountId,String langCode);
	
	/**
	 * 获取账户DOC信息
	 * @author zxtan
	 * @date 2017-01-12
	 * @return
	 */
	public List<AppMyAccountDocInfoVO> findAccountDocCheckInfo(String accountId,String langCode);
	
	/**
	 * 获取账户基本信息
	 * @author zxtan
	 * @date 2017-01-16
	 * @return
	 */
	public AppMyAccountBaseInfoVO findAccountBaseInfo(String accountId,String langCode);
	
	/**
	 * 得到投资账户的交易记录列表
	 * @author zxtan
	 * @date 2017-01-13
	 * @param accountId 账户ID
	 * @return
	 */
	public List<AppPortfolioOrderHistoryVO> findAccountOrderHistoryList(String accountId, String toCurrency,String langCode);
	
	/**
	 * 生成RPQ问卷
	 * @author zxtan
	 * @date 2017-03-08
	 * @param accountId
	 * @param langCode
	 * @return
	 */
	public List<AppRpqExamQuestVO> createRpqExamQuest(String accountId,String langCode);
	
	/**
	 * RPQ问卷评测结果
	 * @author zxtan
	 * @date 2017-03-08
	 * @param answerArray
	 * @param langCode
	 * @return
	 */
	public AppRpqExamResultVO saveRpqExamAnswer(JSONArray answerArray,String langCode);
}
