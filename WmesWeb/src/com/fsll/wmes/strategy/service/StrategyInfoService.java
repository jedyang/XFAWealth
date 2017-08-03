/**
 * 
 */
package com.fsll.wmes.strategy.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.StrategyAllocation;
import com.fsll.wmes.entity.StrategyCount;
import com.fsll.wmes.entity.StrategyInfo;
import com.fsll.wmes.entity.StrategyProduct;
import com.fsll.wmes.entity.WebInvestorVisit;
import com.fsll.wmes.entity.WebPush;
import com.fsll.wmes.entity.WebPushDetail;
import com.fsll.wmes.entity.WebView;
import com.fsll.wmes.entity.WebViewDetail;
import com.fsll.wmes.strategy.vo.CriteriaVO;
import com.fsll.wmes.strategy.vo.StrategyAllocMethodVO;
import com.fsll.wmes.strategy.vo.StrategyAllocationTypeVO;
import com.fsll.wmes.strategy.vo.StrategyAllocationVO;
import com.fsll.wmes.strategy.vo.StrategyForSelectVO;
import com.fsll.wmes.strategy.vo.StrategyInfoWebVO;
import com.fsll.wmes.strategy.vo.StrategyWebPushVO;
import com.fsll.wmes.strategy.vo.StrategyWebViewVO;
import com.fsll.wmes.strategy.vo.WebInvestorVisitVO;

/**
 * 投资策略信息查询服务接口
 * @author tan
 * @date 2016-8-17
 */
public interface StrategyInfoService {

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
	public StrategyInfo findById(String id);
	
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
	public JsonPaging findByUser(JsonPaging jsonPaging, CriteriaVO criteria,String langCode);
	
	/**
	 * 保存投资策略
	 * @author michael
	 * @param info
	 * @return
	 */
	public StrategyInfo saveOrUpdate(StrategyInfo info);

	/**
	 * 保存投资策略的产品
	 * @author michael
	 * @param strategyInfo
	 * @param fundId
	 * @return
	 */
	public StrategyProduct saveOrUpdateStrategyProduct(StrategyInfo strategyInfo, String fundId);
	
	
	/**
	 * 重置用户所有已置顶的记录
	 * @author michael
	 * @param userId 
	 */
	public void clearOverhead(String userId);
	
	/**
	 * 删除投资策略
	 */
	public void delete(StrategyInfo info);
	
	/**
	 * 保存查看权限
	 * @author michael
	 * @param info
	 * @return
	 */
	public WebView saveOrUpdate(WebView info);
	
	/**
	 * 保存查看权限明细
	 * @author michael
	 * @param info
	 * @return
	 */
	public WebViewDetail saveOrUpdate(WebViewDetail info);
	
	/**
	 * 保存推送权限
	 * @author michael
	 * @param info
	 * @return
	 */
	public WebPush saveOrUpdate(WebPush info);
	
	/**
	 * 保存推送权限明细
	 * @author michael
	 * @param info
	 * @return
	 */
	public WebPushDetail saveOrUpdate(WebPushDetail info);
	
	/**
	 * 获取投资策略关联的基金产品
	 * @param strategyId
	 * @return
	 */
	public List<FundInfo> findFundListByStrategy(String strategyId);
	
	/**
	 *  获取投资策略关联的基金产品Id
	 *  @author mqzou 2016-11-08
	 * @param strategyId
	 * @return
	 */
	public List<String> findFundIdByStrategy(String strategyId);
	
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
	 * @param strategyId
	 * @return
	 */
	public StrategyWebViewVO findWebViewByStrategy(String strategyId);
	
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
	 * @param strategyId
	 * @return
	 */
	public StrategyWebPushVO findWebPushByStrategy(String strategyId);
	
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
	
    /**
     * 保存查看权限明细
     * @author michael
     * @param webView
     * @param type
     * @param toMembers
     */
	public void saveWebViewDetail(WebView webView, String type, String toMembers);
	
    /**
     * 保存推送权限明细
     * @author michael
     * @param webPush
     * @param type
     * @param toMembers
     */
    public void saveWebPushDetail(WebPush webPush, String type, String toMembers);
	
    /**
     * 保存查看权限信息
     * @author michael
     * @param webView
     * @param clients
     * @param prospects
     * @param buddies
     * @param colleagues
     * @return WebView
     */
    public WebView saveWebView(WebView webView, String clients, String prospects, String buddies, String colleagues);
    
     
    
    /**
     * 保存推送权限信息
     * @author michael
     * @param webPush
     * @param clients
     * @param prospects
     * @param buddies
     * @param colleagues
     * @return WebPush
     */
    public WebPush saveWebPush(WebPush webPush, String clients, String prospects, String buddies, String colleagues);
    
	public List<StrategyProduct> findStrategyProductByStrategyId(String id);
	
	public List<ProductInfo> findProductInfoList( List<StrategyProduct> list );
	
	/**
	 * 获取投资策略分配比例
     * @author mqzou
     * @date 2016-09-08 
     * modify by mqzou 2016-10-27 
     * @param webPush
	 * @param strategyId
	 * @param methodType 分配方法,G:Geographical,S:Sector,F:Funds Type
	 * @return
	 */
	public List<StrategyAllocationVO> findStrategyAllocationData(String strategyId,String langCode,String methodType);
	
	/**
	 * 获取投资策略分配比例(按类型)
     * @author mqzou
     * @date 2016-11-08
	 * @param strategyId
	 * @param langCode
	 * @param methodType
	 * @param type
	 * @return
	 */
	public List<StrategyAllocationVO> findStrategyAllocation(String strategyId,String langCode,String methodType,String type);
	
	/**
	 * 获取投资策略分配比例汇总(按类型)
	 * @param strategyId
	 * @return
	 */
	public List<StrategyAllocationTypeVO> findStrategyAllocationType(String strategyId);
	
	/**
	 * 获取投资策略分配方式
	 * @author mqzou
     * @date 2016-11-08
	 * @param strategyId
	 * @return
	 */
	public List<StrategyAllocMethodVO> findAllocMethod(String strategyId,String type);

	/**
	 * 保存策略权重分配
	 * @author michael
	 * @param info
	 * @return
	 */
	public StrategyAllocation saveOrUpdate(StrategyAllocation info);
	
	/**
	 * 通过多条件获取列表
	 * @author michael
	 * @param strategyId
	 * @param layer 所属层级
	 * @param type 分配方法,G:Geographical,S:Sector,F:Funds Type
	 * @return
	 */
	public List<StrategyAllocation> findStrategyAllocation(String strategyId,String layer,String type);
	
	/**
	 * 删除策略分配设置
	 * @author michael
	 * @param strategyId
	 * @param method 分配方法,G:Geographical,S:Sector,F:Funds Type
	 * @return
	 */
	public void deleteStrategyAllocationByMethod(String strategyId, String method);
	
	/**
	 * 删除策略分配设置(methodType='P')
	 * @author michael
	 * @param strategyId
	 * @param type 分配置类型,fund,stock,bond
	 * @param method 分配方法,G:Geographical,S:Sector,F:Funds Type
	 * @return
	 */
	public void deleteStrategyAllocationByType(String strategyId, String type);
	
	/**
	 * 删除策略统计记录
	 * @author michael
	 * @param strategyId
	 * @return
	 */
	public void deleteStrategyCount(String strategyId);
	
	/**
	 * 通过ID获取内容信息
	 * @author michael
	 * @param id
	 */
	public StrategyCount findStrategyCountById(String id);
	
	/**
	 * 查找策略的产品
	 * @param strategyId
	 * @param type 产品类型:fund基金,stock股票,futures期货
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProductInfo> findProductInfoList(String strategyId, String type);
	
	/**
	 * 删除策略产品
	 * @author michael
	 * @param strategyId
	 * @return
	 */
	public void deleteStrategyProduct(String strategyId);
	
    /**
     * 保存策略配置
     * @author michael
     * @param info 策略信息
     * @param type 分配类型
     * @param level 分配层级
     * @param codes 配置编码
     * @param weights 配置编码对应的比例
     */
    public void saveStrategyAllocation(StrategyInfo info, String type, String level, String codes, String weights);
    
    /**
     * 获取策略列表（提供选择）
     * @author mqzou
     * @date 2016-10-26
     * @param memberId
     * @return
     */
    public List<StrategyForSelectVO>  findStrategyListForSelect(String memberId);

    /**
	 * 获取我的策略
	 * @author wwluo
	 * @param keyWord 过滤名称关键字关键字
	 * @param isPublic 是否公开
	 * @param status 是否草稿
	 * @date 2016-11-18
	 */
	public JsonPaging getMyStrategys(JsonPaging jsonPaging,
			MemberBase loginUser, String keyWord,String isPublic,String status);

	/**
	 * 根据关联id获取访问记录
	 * @author wwluo
	 * @date 2016-11-18
	 */
	public List<WebInvestorVisit> getWebInvestorVisitByRelateId(String relateId);

	/**
	 * 获取客户访问记录
	 * @author wwluo
	 * @date 2016-11-18
	 * @param relateId 关联Id
	 * @param longinUser 当前用户
	 */
	public JsonPaging getCustomerVisitStrategy(JsonPaging jsonPaging,String relateId,String moduleType,
			MemberBase loginUser);

	/**
	 * 删除我的策略（物理删除）
	 * @author wwluo
	 * @date 2016-11-18
	 */
	public void delStrategy(StrategyInfo info);
	
	/**
	 * 获取ifa推荐的策略列表
	 * @author mqzou	
	 * @date 2016-11-29
	 * @param jsonPaging
	 * @param ifaMemberId
	 * @param keyWord
	 * @return
	 */
	public JsonPaging findByIfaRecommend(JsonPaging jsonPaging,String ifaMemberId,String keyWord,String langCode,MemberBase loginUser);

	/**
	 * 获取推送信息
	 * @author wwluo	
	 * @date 2016-12-21
	 * @param id 关联Id relateId
	 * @param moduleType 
	 * @return
	 */
	public WebPush getWebPushByRelateId(String id,
			String moduleType);

	/**
	 * 删除权限信息
	 * @author wwluo	
	 * @date 2016-12-21
	 * @return
	 */
	public void delWebViewDetail(String id);
	
	/**
	 * 获取策略的分配项（按比例从大到小取前几个）
	 * @author mqzou 2017-04-25
	 * @param strategyId
	 * @param type
	 * @param top
	 * @return
	 */
	public String getStrategyAllocationItems(String strategyId,String type,int top );
	
	/**
	 * 获取策略详情
	 * @author mqzou 2017-05-15
	 * @param id
	 * @param langCode
	 * @return
	 */
	public StrategyInfoWebVO findStrategyDetail(String id,String langCode,String memberId,String dateFormat);

	/**
	 * 获取投资策略分配比例信息
	 * @author wwluo
	 * @param strategyId
	 * @param methodType 分配方法
	 * @return
	 */
	public List<StrategyAllocation> getStrategyAllocation(String strategyId,
			String methodType);

}
