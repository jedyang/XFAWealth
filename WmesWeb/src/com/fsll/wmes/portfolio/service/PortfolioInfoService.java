/**
 * 
 */
package com.fsll.wmes.portfolio.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.PortfolioInfo;
//import com.fsll.wmes.entity.PortfolioProduct;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.PortfolioArena;
import com.fsll.wmes.entity.PortfolioArenaCount;
import com.fsll.wmes.entity.PortfolioHoldProduct;
import com.fsll.wmes.entity.PortfolioInfoAip;
import com.fsll.wmes.entity.PortfolioInfoProduct;
import com.fsll.wmes.entity.WebPush;
import com.fsll.wmes.entity.WebPushDetail;
import com.fsll.wmes.entity.WebView;
import com.fsll.wmes.entity.WebViewDetail;
import com.fsll.wmes.portfolio.vo.CriteriaVO;
import com.fsll.wmes.portfolio.vo.PortfolioWebPushVO;
import com.fsll.wmes.portfolio.vo.PortfolioWebViewVO;

/**
 * 投资组合信息查询服务接口
 * @author michael
 * @date 2016-8-17
 */
public interface PortfolioInfoService {

	/**
	 * 获取列表
	 * @param jsonPaging
	 * @param keyword
	 * @return
	 */
	public JsonPaging findAll(JsonPaging jsonPaging,String keyword);
	
	/**
	 * 获取内容信息
	 * @param id
	 * @return
	 */
	public PortfolioInfo findById(String id);
	
	/**
	 * 通过多条件获取列表
	 * @author michael
	 * @param jsonPaging
	 * @param keyword
	 * @param sector
	 * @param region
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public JsonPaging findAll(JsonPaging jsonPaging, String keyword, String sector, String region, String fromDate, String toDate);
	
	/**
	 * 通过多条件获取列表
	 * @author michael
	 * @param jsonPaging
	 * @param criteria
	 * @return
	 */
	public JsonPaging findByUser(JsonPaging jsonPaging, CriteriaVO criteria);
	
	/**
	 * 保存投资策略
	 * @author michael
	 * @param info
	 * @return
	 */
	public PortfolioInfo saveOrUpdate(PortfolioInfo info);

	/**
	 * 保存投资策略的产品
	 * @author michael
	 * @param strategyInfo
	 * @param fundId
	 * @return
	 */
//	public PortfolioProduct saveOrUpdate(PortfolioInfo strategyInfo, String fundId);
	
	

	/**
	 * 删除投资策略
	 */
	public void delete(PortfolioInfo info);
	

	/**
	 * 获取投资策略关联的基金产品
	 * @param portfolioId
	 * @return
	 */
	public List<FundInfo> findFundListByPortfolio(String portfolioId);
	
	/**
	 * 通过ID获取查看权限设置
	 * @author michael
	 * @param id
	 * @return
	 */
	public WebView findWebViewById(String id);
	
	/**
	 * 获取投资策略关联的查看权限设置
	 * @author michael
	 * @param portfolioId
	 * @return
	 */
	public PortfolioWebViewVO findWebViewByPortfolio(String portfolioId);
	
	/**
	 * 通过ID获取推送权限设置
	 * @author michael
	 * @param id
	 * @return
	 */
	public WebPush findWebPushById(String id);
	
	/**
	 * 获取投资策略关联的推送权限设置
	 * @author michael
	 * @param portfolioId
	 * @return
	 */
	public PortfolioWebPushVO findWebPushByPortfolio(String portfolioId);
	
	/**
	 * 获取查看权限明细
	 * @author michael
	 * @param viewId
	 * @param type
	 * @return
	 */
	public List<WebViewDetail> findWebViewDetailList(String viewId, String type);
	
	/**
	 * 获取推送权限明细
	 * @author michael
	 * @param pushId
	 * @param type
	 * @return
	 */
	public List<WebPushDetail> findWebPushDetailList(String pushId, String type);
	
	/**
	 * 重置查看权限明细记录
	 * @author michael
	 * @param viewId 
	 * @param type
	 */
	public void clearWebViewDetail(String viewId, String type);
	
	/**
	 * 重置推送权限明细记录
	 * @author michael
	 * @param pushId 
	 * @param type
	 */
	public void clearWebPushDetail(String pushId, String type);
	
//	public List<PortfolioProduct> findPortfolioProductByPortfolioId(String id);
//	
//	public List<ProductInfo> findProductInfoList( List<PortfolioProduct> list );
	
	/**
	 * 获取ifa管理的投资组合
	 * @param jsonPaging
	 * @param ifaMemberId
	 * @param customerMemberId
	 * @return
	 */
	public JsonPaging findPortfolioListForIfa(JsonPaging jsonPaging,String ifaMemberId, String customerMemberId);

	/**
	 * 根据组合Id获取组合定投信息
	 * @author wwluo
	 * @param portfolioId PortfolioInfo ID
	 * @return
	 */
	public PortfolioInfoAip findAipByPortfolioId(
			String portfolioId);

	/**
	 * 根据Id获取组合定投信息
	 * @author wwluo
	 * @param portfolioId PortfolioInfo ID
	 * @return
	 */
	public PortfolioInfoAip findPortfolioInfoAipById(String id);

	/**
	 * 删除组合下的产品
	 * @author wwluo
	 * @param id PortfolioInfo ID
	 * @return
	 */
	public void deleteProduct(String id);

	/**
	 * 获取我的客户的投资组合
	 * @author wwluo
	 * @date 2016-09-14
	 */
	public JsonPaging getPortfolio(JsonPaging jsonPaging, MemberBase loginUser,
			String period,String fromDate,String toDate, String riskLevel, String totalReturn, String keyWord,String status,String langCode,String baseCurrency);
	
	/**
	 * @author mqzou
	 * @date 2016-11-15
	 * @param id
	 */
	public PortfolioArenaCount findPortfolioCountById(String id);

	/**
	 * 删除组合下的产品detail
	 * @author wwluo
	 * @param id PortfolioInfo ID
	 * @return
	 */
	public void deleteProductDetail(String id);
	
	/**
	 * 根据ifa的memberId查找客户组合
	 * @author michael
	 * @param memberId
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public List<PortfolioInfo> findPortfolioInfoByIfa(String memberId);
	
	/**
	 * 检测客户组合是否存在
	 * @author michael
	 * @param ifaMemberId
	 * @param memberId
	 * @return
	 */
	public boolean checkIfExistPortfolio(String ifaMemberId, String memberId);
	
	/**
	 * 修改IFA的客户组合到另一IFA
	 * @author michael
	 * @date 2017-3-1
	 * @param fromMemberId 源IFA
	 * @param toMemberId 目标IFA
	 * @return Boolean
	 */
	public Boolean migrateIfaPortfolio(String fromMemberId,String toMemberId,MemberBase createBy);
}
