package com.fsll.wmes.ifa.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.CornerInfo;
import com.fsll.wmes.entity.IfaDistributor;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.WebReadToDo;
import com.fsll.wmes.entity.WebRecommended;
import com.fsll.wmes.ifa.vo.IfaClientVO;
import com.fsll.wmes.ifa.vo.IfaCorner;
import com.fsll.wmes.ifa.vo.IfaCountryVO;
import com.fsll.wmes.ifa.vo.IfaInfoVO;
import com.fsll.wmes.ifa.vo.IfaListfiltersVO;
import com.fsll.wmes.ifa.vo.IfaSearchItemVO;
import com.fsll.wmes.ifa.vo.IfafirmVO;
import com.fsll.wmes.ifa.vo.MyFavoritesPortfolios;
import com.fsll.wmes.ifa.vo.MyFavoritesStrategy;
import com.fsll.wmes.ifa.vo.MyFavoritesWatchingTypeVOList;


/**
 * 基金信息查询服务接口
 * @author zpzhou
 * @date 2016-7-25
 */
public interface IfaInfoService {
	
	/**
	 * 得到distributor信息
	 * @return
	 */
	public List<IfaSearchItemVO> getDistributorMess();
	
	/**
	 * @param jsonPaging 分页参数
	 * @param langCode 语言
	 * @param userId 用户ID
	 * @param serviceRegion 服务区域
	 * @param expertiseType 擅长领域 
	 * @param country 居住地
	 * @param distributor 分销商
	 * @return
	 */
	public JsonPaging findIfaList(JsonPaging jsonPaging,String langCode,String userId,String serviceRegion,String expertiseType, String country,String distributor);
	
	/**
	 * 查找国家列表
	 * @author lww
	 * @param langCode 语言
	 * @return
	 */
	public List<IfaCountryVO> findCountryList(String langCode);
	
	/**
	 * 获取IFA列表
	 * @author 林文伟
	 * @param langCode 语言
	 * @return
	 */
	public List<IfafirmVO> loadIFAFirmList(String langCode);
	
	/**
	 * 获取IFA列表
	 * @author 林文伟
	  * @param jsonPaging 分页参数
	 * @param filters 过滤条件
	 * @return
	 */
	public JsonPaging loadIFAList(JsonPaging jsonPaging,IfaListfiltersVO filter,MemberIndividual investor);
	
	public List<WebRecommended> getIfaRecommendFundList( MemberBase memberBase );
	
	/**
	 * 通过memberId获取投资者信息
	 * @author 林文伟
	 * @param memberId
	 * @return
	 */
	public MemberIndividual getMemberIndividual( String memberId );
	
	/**
	 * 获取ifa推荐的基金的Id集合
	 * @author mqzou
	 * @date 2016-10-18
	 * @param memberBase
	 * @param begin
	 * @param end
	 * @param sectorCode
	 * @param geoCode
	 * @return
	 */
	public List<WebRecommended> getIfaRecommendFundIds(MemberBase memberBase,String begin,String end,String sectorCode,String geoCode,String langCode);
	
	public WebRecommended getIfaRecommendFund( MemberBase memberBase, String id );
	
	public WebRecommended updateWebRecommended( WebRecommended webRecommended );
	/**
	 * 删除推荐信息
	 * @author mqzou 2017-04-18
	 * @param webRecommended
	 */
	public void deleteWebRecommend(WebRecommended webRecommended);

	/**
	 * 获取推荐信息
	 * @author mqzou 2017-04-18
	 * @param id
	 * @return
	 */
	public WebRecommended findRecommendedById(String id);
	
	/*****************************************我收藏的策略、组合、watchlist相关功能*********************************************/
	/**
	 * 获取我收藏的策略
	 * @author 林文伟
	 * @param memberId 会员ID
	 * @return
	 */
	public List<MyFavoritesStrategy> loadIFAMyFavoritesStrategyList(String memberId,String lang,String defFormatDate, Integer maxResults,String langCode) ;
	
	/**
	 * 获取我收藏的组合数据
	 * @author 林文伟
	 * @param memberId 会员ID
	 * @return
	 */
	public List<MyFavoritesPortfolios> loadIFAMyFavoritesPortfoliosList(String memberId, Integer maxResults,String langCode);
	
	/**
	 * 获取我自选的基金列表
	 * @author 林文伟
	 * @param memberId 会员ID
	 * @return
	 */
	public List<MyFavoritesWatchingTypeVOList> loadIFAMyFavoritesWatchingList(String langCode,String memberId,String toCurrency,String watchingTypeId);
	
	/***
     * 通过类型删除所有自选基金
     * @author 林文伟
     * @date 2016-09-18
     */
	public Boolean delMyWatchlist(String memberid,String typeId);
	
	/***
     * 更新自选基金的备注信息
     * @author 林文伟
     * @date 2016-09-18
     */
	public Boolean saveMyWatchlistRemark(String id,String remark);
	
	/***
     * 更新自选基金类型的名称
     * @author 林文伟
     * @date 2016-09-18
     */
	public Boolean modifyMyWatchTypeName(String id,String name);
	
	/***
     * 删除我关注的web_follow信息
     * @author 林文伟
     * @date 2016-12-18
     */
	public Boolean deleteMyWebFollow(String followId);
	
	/**
	 * 通过会员ID,待阅类型获取所有待阅信息
	 * @author 林文伟
	 * @param memberId
	 * @return
	 */
	public List<WebReadToDo> findWebReadTodo(String memberId,String typeCode,String status,String langCode);
	
	/**
	 * 获取所有圈子分享主题列表
	 * @author 林文伟
	 * @param portfolioId
	 * @return
	 */
	public List<CornerInfo> findCornerInfoList(MemberBase loginUser,String langCode);
	
	/**
	 * 获取某个IFA会员的圈子分享主题列表，每个主题有点赞评论等各方 面信息
	 * @author 林文伟
	 * @param portfolioId
	 * @return
	 */
	public IfaCorner findIfaCornerInfo(String memberId,String langCode);
	
	/***
     * 评论圈子主题
     * @author 林文伟
     * @date 2016-09-18
     */
	public Boolean replyCornerInfo(String cornerId,String memberId,String content);
	
	/***
     * 对主题进行点赞
     * @author 林文伟
     * @date 2016-09-18
     */
	public Boolean likedCornerInfo(String cornerId,String memberId);
	
	/***
     * 转载圈子主题
     * @author 林文伟
     * @date 2016-09-18
     */
	public Boolean reprintCornerInfo(String cornerId,String memberId,String title,String content,String url);
	
	/***
     * 发表圈子主题
     * @author 林文伟
     * @date 2016-10-18
     */
	public Boolean saveCornerInfo(String memberId,String title,String content,String imgpath );
	
	/***
     * 分享主题
     * @author 林文伟
     * @date 2016-09-18
     */
	public Boolean shareInfo(String memberId,String title,String content,String url);

	
	/**
	 * 获取ifa的客户沟通记录
	 * @author mqzou 2016-12-01
	 * @param jsonPaging
	 * @param ifaId
	 * @param beginDate
	 * @param endDate
	 * @param clientName
	 * @return
	 */
	public JsonPaging findCommunicationRecord(JsonPaging jsonPaging,String ifaId,String beginDate,String endDate,String clientName,String keyWord);
	
	/**
	 * IFA查看表现最好或最差的5个投资组合
	 * @author mqzou 2016-12-21
	 * @param jsonPaging
	 * @param ifaId
	 * @param statistic
	 * @param currency
	 * @return
	 */
	public JsonPaging findCustomerTopPortfolio(JsonPaging jsonPaging,String ifaId,String statistic,String currency,String langCode);
	
	/**
	 * 获取ifa客户
	 * @author zxtan 2017-01-04
	 * @param memberId
	 * @param clientType
	 * @return
	 */
	public List<IfaClientVO> findMyClientList(String memberId,String clientType,String langCode);

	/**
	 * 获取ifa同事
	 * @author zxtan 2017-01-04
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	public List<IfaClientVO> findMyColleagueList(String memberId, String langCode);
	
	/**
	 * 通过会员ID,待阅类型获取所有待阅信息
	 * @author 林文伟
	 * @return
	 */
	public List<WebReadToDo> findWaitWebReadTodo(String memberId,String typeCode,String langCode);
	
	/**
	 * 获取ifa的AE帐号
	 * @author mqzou 2017-01-04
	 * @param ifaId
	 * @param distributorId
	 * @return
	 */
	public String findIfaAE(String ifaId,String distributorId);
	
	/**
	 * 获取ifa持仓客户对应的AE帐号
	 * @author zxtan 2017-01-06
	 * @param ifaId
	 * @return
	 */
	public List<String> findIfaAEList(String ifaId);
	
	/**
	 * 获取IfaDistributor帐号
	 * @author zxtan 2017-01-11
	 * @param aeCode
	 * @return
	 */
	public IfaDistributor findIfaDistributorInfo(String aeCode);

	
	/**
	 * 获取投资者与IFA之间的匹配度
	 * @author linwenwei
	 */
	public int getInvestorIfaMatchDegree(MemberBase investor,MemberBase ifa);
	
	/***
     * 删除待办信息
     * @author 林文伟
     * @date 2017-02-18
     */
	public Boolean delWebReadToDo(String id);
	
	/**
	 * 更新系统消息读状态
	 * @author 林文伟 
	 * @date 2016-10-19
	 * @return
	 */
	public boolean updateMsgReadStatus(String id, String status);
	
	/**
	 * web前台获取系统消息JSON数据
	 * @author 林文伟  
	 * @param jsonPaging 分页实体
	 * 
	 * */
	public JsonPaging findDiscoverMsgForWeb(JsonPaging jsonPaging,MemberBase baseMem, String typeCode,String readStatus,String langCode,String keyWords);
	
	/**
	 * 获取ifa的基本信息
	 * @author mqzou  2017-06-16
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	public IfaInfoVO findIfaInfo(String memberId,String langCode);
}
