package com.fsll.wmes.ifa.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.NewsInfo;
import com.fsll.wmes.entity.WebRecommended;
import com.fsll.wmes.ifa.vo.IfaSpaceFundVO;
import com.fsll.wmes.ifa.vo.IfaSpaceLiveVO;
import com.fsll.wmes.ifa.vo.IfaSpacePortfoliosVO;
import com.fsll.wmes.ifa.vo.IfaSpaceStrategyInfoVO;
import com.fsll.wmes.ifa.vo.IfaSpaceVO;
import com.fsll.wmes.ifa.vo.RecommendInfoVO;
import com.fsll.wmes.strategy.vo.StrategyWebPushVO;

/**
 * 个人空间信息查询服务接口
 * @author 林文伟
 * @date 2016-8-19
 */
public interface IfaSpaceService {
	/**
	 * 通过IFA的ID获取IFA信息，用于空间数据展示
	 * @author 林文伟
	 * @param langCode 语言
	 * @return
	 */
	public IfaSpaceVO loadIfaSpaceData(String loginMemberId,String ifaId,String langCode,String regionId,String curDefCurrency) ;
	
	/**
	 * 检测用户是否查看了ifa的观点
	 * @author mqzou	
	 * @date 2016-09-27
	 * @param ifaId
	 * @param memberId
	 * @return
	 */
	public boolean checkInsightRead(String ifaMemberId,String memberId);
	
	/**
	 * 查询ifa和investor是否有共同的投资类型
	 * @author mqzou	
	 * @date 2016-09-27
	 * @param ifaMemberId
	 * @param memberId
	 * @return
	 */
	public int checkIfaInvSameStyle(String ifaMemberId,String memberId);
	
	/**
	 * 查询investor是否有ifa
	 * @author mqzou	
	 * @date 2016-09-27
	 * @param memberId
	 * @return
	 */
	public boolean checkInvestorWithIfa(String memberId);
	
	/**
	 * 获取ifa空间访客列表
	 * @author mqzou	
	 * @date 2016-09-27
	 * @param ifaId
	 * @return
	 */
	public List findSpaceVisit(String ifaId);
	
	/**
	 * 获取ifa推荐的新闻列表
	 * @author mqzou	
	 * @date 2016-09-30
	 * @param jsonPaging
	 * @param newsInfo
	 * @param webRecommended
	 * @return
	 */
	public JsonPaging findIfaRecommendNews(JsonPaging jsonPaging,NewsInfo newsInfo,WebRecommended webRecommended,String beginDate,String endDate);
	
	/**
	 * 获取新闻的访客列表
	 * @author mqzou	
	 * @date 2016-09-30
	 * @param memberId
	 * @return
	 */
	public List findNewsVisitorList(String memberId,String newsId);
	
	/**
	 * 获取新闻关联的推送权限设置
	 * @author mqzou 
	 * @date 2016-10-08
	 * @param newsId
	 * @return
	 */
	public StrategyWebPushVO findWebPushByNews(String newsId,String moduleType);
	
	/**
	 * 获取新闻推荐的实体信息
	 * @author mqzou 
	 * @date 2016-10-08
	 * @param id
	 * @return
	 */
    public WebRecommended getIfaRecommendNews(String id );
	
    /**
     * 修改新闻推荐的实体信息
	 * @author mqzou 
	 * @date 2016-10-08
     * @param webRecommended
     * @return
     */
	public WebRecommended updateWebRecommended( WebRecommended webRecommended );
	
	/**
	 * 获取推荐的访客信息
	 * @author mqzou 
	 * @date 2016-10-19
	 * @param memberId
	 * @param relateId
	 * @param moduleType
	 * @return
	 */
	public RecommendInfoVO getRecommendInfo(String memberId,String relateId,String moduleType);
	
	/**
	 * 推荐置顶操作
	 * @author mqzou 
	 * @date 2016-10-19
	 * @param memberId
	 * @param id
	 * @param overhead
	 * @return
	 */
	public boolean checkRecommendOverhead(String memberId,String id,String overhead,String moduleType); 
	
	/**
	 * 删除推荐
	 * @author mqzou 
	 * @date 2016-11-03
	 * @param webRecommended
	 */
	public void delRecommended(WebRecommended webRecommended);

	/**
	 * 更新ifa人气
	 * @author wwluo
	 * @date 2016-11-03
	 * @param memberId
	 */
	public Boolean updateIfaPopularityTotal(String memberId,String visitMemberId);
	
	/**
	 * IFA个人空间推荐区域，根据搜索条件【区域与投资领域板块】获取策略、组合、基金、观点、新闻
	 * @author 林文伟
	 * @param langCode 语言
	 * @return
	 */
	public IfaSpaceVO loadIfaSpaceFilterRecommendData(String loginMemberId,String memberId,String langCode,String regionCodeType,String sectorCodeType);
	
	/**
	 * IFA空间更新hightlight
	 * @author 林文伟 
	 * @date 2016-10-19
	 * @param ifaId
	 * @param hightLight
	 * @return
	 */
	public boolean updateHightLight(String ifaId, String hightLight) ;
	
	/**
	 * 获取IFA空间直播左侧列表
	 * @author 林文伟
	 * @return
	 */
	public List<IfaSpaceLiveVO> getSpaceLiveLeftList(String memberId,String langCode);
	
	/**
	 * 获取IFA空间将要直播左侧列表，已播的不显示
	 * @author 林文伟
	 * @return
	 */
	public List<IfaSpaceLiveVO> getSpaceWillLiveLeftList(String memberId,String langCode);
	
	/**
	 * 通过不同的货币转换
	 * @author 林文伟 
	 * @date 2016-10-19
	 * @return
	 */
	public String exchangeCurrency(double aum,String fromCurrency,String toCurrency) ;
	
	/**
	 * 判断member是否可访问的某个观点
	 * @author 林文伟
	 * @param ifaId 
	 * @return
	 */
	public  Boolean getMemberCanViewModule(String memberId,String relateId,String moduleType) ;
	
	/**
	 * 设置是否显示AUM给投资者看
	 * @author wwlin 
	 * @date 2016-10-19
	 * @return
	 */
	public boolean updateIfaIsShowAum(String memberId, String isShowAum) ;
	
	/**
	 * 设置是否在IFA空间显示个人业绩
	 * @author wwlin 
	 * @date 2016-10-19
	 * @return
	 */
	public boolean updateIfaIsShowFerformance(String memberId, String isShowFerformance);
	
	/**
	 * 获取推荐模块之策略实体
	 * @author 林文伟
	 * @param memberId 
	 * @param regionTypeCode 
	 * @param sectorTypeCode 
	 * @return
	 */
	public List<IfaSpaceStrategyInfoVO> getRecommendedStrategies(String memberId,String regionTypeCode,String sectorTypeCode,String viewMemberId);
	
	/**
	 * 获取推荐列表模块
	 * @author 林文伟
	 * @param memberI 
	 * @return
	 */
	public List<IfaSpacePortfoliosVO> getRecommendedPortfolios(String memberId,String regionTypeCode,String sectorTypeCode,String viewMemberId);
	
	/**
	 * 获取IFA空间推荐的观点列表
	 * @author 林文伟
	 * @return
	 */
	public List<IfaSpaceFundVO> getSpaceFundList(String memberId,String langCode,String regionCodeType,String sectorCodeType);
}
