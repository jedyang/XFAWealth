package com.fsll.wmes.portfolio.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.crm.vo.ProposalNumberVO;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioHoldAccount;
import com.fsll.wmes.entity.PortfolioHoldCumperf;
import com.fsll.wmes.entity.PortfolioHoldProduct;
import com.fsll.wmes.entity.PortfolioHoldProductCumperf;
import com.fsll.wmes.entity.PortfolioInfo;
import com.fsll.wmes.entity.ProductType;
import com.fsll.wmes.fund.vo.FundIncomDetailVO;
import com.fsll.wmes.ifa.vo.IfaSpaceVisitVO;
import com.fsll.wmes.investor.vo.AccountNumberVO;
import com.fsll.wmes.portfolio.vo.AssetsTotalVo;
import com.fsll.wmes.portfolio.vo.AssetsVO;
import com.fsll.wmes.portfolio.vo.FavouryPortfolioVO;
import com.fsll.wmes.portfolio.vo.FavouryWatchlistsVO;
import com.fsll.wmes.portfolio.vo.HoldProductVO;
import com.fsll.wmes.portfolio.vo.InvestorPortfolioDataVO;
import com.fsll.wmes.portfolio.vo.PortfolioAssetsVO;
import com.fsll.wmes.portfolio.vo.PortfolioHoldCumperfSimpleVO;
import com.fsll.wmes.portfolio.vo.PortfolioHoldCumperfVO;
import com.fsll.wmes.portfolio.vo.PortfolioHoldProductCumperfVO;
import com.fsll.wmes.portfolio.vo.PortfolioProductPLVO;
import com.fsll.wmes.portfolio.vo.PortfolioProductListVO;
import com.fsll.wmes.strategy.vo.FavouryStrategyVO;

public interface PortfolioHoldService {

	/**
	 * 获取投资人的资产
	 * @author mqzou
	 * @date 2016-10-09
	 * @param memberId
	 * @param portfolioId
	 * @return
	 */
	public List<AssetsVO> getMyAssets(String memberId,String portfolioId,String ifaId);
	
	/**
	 * 获取统计某段时间内的资产收益报表
	 * @author mqzou
	 * @date 2016-10-09
	 * @param memberId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<PortfolioHoldCumperf> getCountPortfolioHoldCumperfs(String memberId,String startDate,String endDate,String portfolioId);
	
	/**
	 * 获取投资组合的资产情况
	 * @author mqzou
	 * @date 2016-10-09
	 * @param memberId
	 * @return
	 */
	public List<PortfolioAssetsVO> getPortfolioAssets(String memberId,String portfolioId,String ifaId,String period,String currency);
	
	/**
	 * 获取用户的账户统计数量
	 * @author mqzou
	 * @date 2016-10-10
	 * @param memberId
	 * @return
	 */
	public AccountNumberVO findAccountNumberOfMember(String memberId);
	
	/**
	 * 获取用户的方案统计数量
	 * @author mqzou
	 * @date 2016-10-10
	 * @param memberId
	 * @return
	 */
	public ProposalNumberVO findProposalNumberOfMember(String memberId);
	
	/**
	 * 获取用户收藏的投资组合统计
	 * @author mqzou
	 * @date 2016-10-10
	 * @param memberId
	 * @return
	 */
	public FavouryPortfolioVO findFavouryPortfolio(String memberId);
	
	/**
	 * 获取用户收藏的策略统计
	 * @author mqzou
	 * @date 2016-10-10
	 * @param memberId
	 * @return
	 */
	public FavouryStrategyVO findFavouryStrategy(String memberId);
	
	/**
	 * 获取用户收藏的Watchlists
	 * @author mqzou
	 * @date 2016-10-21
	 * @param memberId
	 * @return
	 */
	public FavouryWatchlistsVO findFavouryWatchlists(String memberId);
	
	/**
	 * 获取用户查看过空间的IFA
	 * @author mqzou
	 * @date 2016-10-10
	 * @param memberId
	 * @return
	 */
	public List<IfaSpaceVisitVO> findFollowingSpace(String memberId);
	
	/**
	 * 获取组合的产品列表
	 * @author mqzou
	 * @date 2016-10-10
	 * @param portfolioId
	 * @return
	 */
	public List<PortfolioProductListVO> findPortfolioProductList(String portfolioId,String langCode,String currency);
	
	/**
	 * 获取组合产品关联表（持仓）
	 * @author mqzou
	 * @date 2016-10-12
	 * @param id
	 * @return
	 */
	public PortfolioHoldProduct findPortfolioHoldProductById(String id);
	
	/**
	 * 获取持仓产品的收益详情
	 * @author mqzou
	 * @date 2016-10-12
	 * @param id 组合产品关联表id
	 * @return
	 */
	public FundIncomDetailVO findFundIncomDetail(String id,String langCode);
	
	/**
	 * 获取统计某段时间内的产品收益报表
	 * @author mqzou
	 * @date 2016-10-12
	 * @return
	 */
	public List<PortfolioHoldProductCumperf> findPortfolioHoldProductCumperf(String pofolioId,String productId,String accountNo,String startDate,String endDate );
	
	/**
	 * 获取投资组合的盈亏
	 * @author mqzou 2016-10-24
	 * @param portfolioId
	 * @return
	 */
	public List<PortfolioProductPLVO> findPortfolioPL(String portfolioId,String langCode);
	
	/**
	 * 获取IFA负责的组合列表
	 *  @author mqzou 2016-11-28
	 * @param ifaId
	 * @return
	 */
	public List<PortfolioInfo> findPortfolioByIFA(String ifaId);
	
	/**
	 * 获取投资者的的组合列表
	 *  @author zxtan 2017-02-21
	 * @param memberId
	 * @return
	 */
	public List<PortfolioHold> findMyPortfolioHoldList(String memberId);
	
	/**
	 * 获取投资人的组合产品类型分配
	 * @author mqzou
	 * @date 2016-12-16
	 * @param memberId
	 * @return
	 */
	public List findPortfolioAllocation(String memberId);
	
	/**
	 * 获取IFA需要进行回顾的组合
	 * @author mqzou
	 * @date 2016-12-16
	 * @param critical
	 * @param ifaId
	 * @param period
	 * @return
	 */
	public List findCriticalPortfolioByIfa(String critical,String ifaId,String period);
	
	/**
	 * 获取组合的总资产
	 * @author mqzou
	 * @date 2016-12-21
	 * @param portfolioId
	 * @param currency
	 * @return
	 */
	public double findPortfolioAuM(String portfolioId,String currency); 
	
	/**
	 * 获取ifa创建的portfolio
	 * @author mqzou
	 * @date 2016-12-27
	 * @param jsonPaging
	 * @param ifaMemberId
	 * @param currency
	 * @param keyWord
	 * @return
	 */
	public JsonPaging findPortfolioForIfa(JsonPaging jsonPaging,String ifaMemberId,String currency,String keyWord);
	
	/**
	 * 获取PortfolioHold实体
	 * @author mqzou
	 * @date 2017-01-03
	 * @param id
	 * @return
	 */
	public PortfolioHold findById(String id);
	
	/**
	 * 查询组合是否定投
	 * @author mqzou
	 * @date 2017-01-04
	 * @param id
	 * @return
	 */
	public boolean checkPortfolioAip(String id);
	
	/**
	 * 保存PortfolioHoldAccount
	 * @author mqzou
	 * @date 2017-01-07
	 * @param holdAccount
	 * @return
	 */
	public PortfolioHoldAccount saveHoldAccount(PortfolioHoldAccount holdAccount);
	
	/**
	 * 获取持仓组合的收益数据
	 * @author mqzou 2017-01-10
	 * @param holdId
	 * @param currency
	 * @param strCondition
	 * @return
	 */
	public InvestorPortfolioDataVO getPortfolioHoldCumperf(String holdId,String currency,String strCondition,String memberId,String dateFormat);
	
	/**
	 * 获取组合的风险值
	 * @author mqzou 2017-01-12
	 * @param portfolioHoldId
	 * @return
	 */
	public int getPortfolioHoldRisk(String portfolioHoldId);
	
	/**
	 * 根据ifa的memberId查找客户组合持仓
	 * @author michael
	 * @param memberId
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public List<PortfolioHold> findPortfolioHoldByIfa(String memberId);
	
	/**
	 * 检测客户组合持仓是否存在
	 * @author michael
	 * @param ifaMemberId
	 * @param memberId
	 * @return
	 */
	public boolean checkIfExistPortfolioHold(String ifaMemberId, String memberId);
	
	/**
	 * 修改IFA的客户组合持仓到另一IFA
	 * @author michael
	 * @date 2017-3-1
	 * @param fromMemberId 源IFA
	 * @param toMemberId 目标IFA
	 * @return Boolean
	 */
	public Boolean migrateIfaPortfolioHold(String fromMemberId,String toMemberId,MemberBase createBy);
	
	/**
	 * 根据ifa的memberId查找客户组合持仓账户
	 * @author michael
	 * @param memberId
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public List<PortfolioHoldAccount> findPortfolioHoldAccountByIfa(String memberId);
    
	/**
	 * 检测客户组合持仓账户是否存在
	 * @author michael
	 * @param ifaMemberId
	 * @param memberId
	 * @return
	 */
	public boolean checkIfExistPortfolioHoldAccount(String ifaMemberId, String memberId);
	
	/**
	 * 修改IFA的客户组合持仓账户到另一IFA
	 * @author michael
	 * @date 2017-3-1
	 * @param fromMemberId 源IFA
	 * @param toMemberId 目标IFA
	 * @return Boolean
	 */
	public Boolean migrateIfaPortfolioHoldAccount(String fromMemberId,String toMemberId,MemberBase createBy);
	
	/**
	 * 获取产品类型表数据
	 * @author mqzou 2017-03-24
	 * @return
	 */
	public List<ProductType> findProductType();
	
	/**
	 * 获取产品比例图表数据
	 * @author mqzou 2017-03-31
	 * @param memberId 组合持仓所属的投资人id
	 * @param ifaId 组合持仓所属的ifaId
	 * @param holdId 组合持仓id
	 * @param currency 货币
	 * @return
	 */
	public List getProductChartData(String memberId,String ifaId,String holdId,String currency);
	
}

