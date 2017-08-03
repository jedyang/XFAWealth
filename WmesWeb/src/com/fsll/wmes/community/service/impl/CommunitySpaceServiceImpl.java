package com.fsll.wmes.community.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.ehcache.pool.impl.BalancedAccessEvictor;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.StrUtils;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.community.service.CommunityService;
import com.fsll.wmes.community.service.CommunitySpaceService;
import com.fsll.wmes.community.vo.CommunityNewsListVO;
import com.fsll.wmes.community.vo.CommunitySpaceVO;
import com.fsll.wmes.community.vo.FocusMemberVO;
import com.fsll.wmes.community.vo.FrontCommunityTopicVO;
import com.fsll.wmes.community.vo.QuestionAnswerListVO;
import com.fsll.wmes.community.vo.QuestionListVO;
import com.fsll.wmes.entity.CommunityBehavior;
import com.fsll.wmes.entity.CommunityComment;
import com.fsll.wmes.entity.CommunityFocus;
import com.fsll.wmes.entity.CommunityQuestion;
import com.fsll.wmes.entity.CommunitySection;
import com.fsll.wmes.entity.CommunityTopic;
import com.fsll.wmes.entity.IfaExtInfo;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioHoldAccount;
import com.fsll.wmes.entity.WebExchangeRate;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.ifa.service.IfaSpaceService;
import com.fsll.wmes.ifa.vo.IfaSpaceStrategyInfoVO;
import com.fsll.wmes.ifa.vo.IfaSpaceVO;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.news.vo.NewsInfoForListVO;

@Service("communitySpaceService")
public class CommunitySpaceServiceImpl extends BaseService implements CommunitySpaceService {

	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
	private SysParamService sysParamService;
	@Autowired
	private FundInfoService fundInfoService;
	@Autowired
	private IfaSpaceService ifaSpaceService;
	@Autowired
	private CommunityService communityService;
	/**
	 * 获取ifa空间的基本数据
	 * @author mqzou 2017-03-16
	 * @param memberId
	 * @return
	 */
	@Override
	public CommunitySpaceVO getIfaSpaceVO(String memberId, String langCode,String currency,String loginMemberId) {
		CommunitySpaceVO vo = new CommunitySpaceVO();
		MemberIfa ifa=memberBaseService.findIfaMember(memberId);
		if(null!=ifa){
			//个人基础信息，包括头像、昵称等
			vo.setMemberId(memberId);
			vo.setGender(ifa.getGender());
			String ifaHeadImg = PageHelper.getUserHeadUrl(ifa.getMember().getIconUrl(), ifa.getGender());
			vo.setIconUrl(ifaHeadImg);
			if(null == ifa.getSpaceShowAum()||"0".equals(ifa.getSpaceShowAum())) vo.setIsShowAumNum(false); 
			else vo.setIsShowAumNum(true); 
			if(null == ifa.getSpaceShowPerformance()||"0".equals(ifa.getSpaceShowPerformance())) vo.setIsSpaceShowPerformance(false); 
			else vo.setIsSpaceShowPerformance(true); 
			
			if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==ifa.getMember().getSubMemberType()){
				String nickName=getCommonMemberName(ifa.getMember().getId(),langCode, "2");
				vo.setNickName(nickName);
			}else {
				vo.setNickName(ifa.getMember().getNickName());
			}
			
			//vo.setNickName(ifa.getMember().getNickName());
			//获取IFA的个人特征
			List<String> charaterList = this.getIfaCharacterList(ifa.getMember(), langCode);
			vo.setCharacterList(charaterList);
			//获取个人最新心情
			//String ifaHightlight = this.getIfaHightlight(ifaId);
			vo.setHightlight(ifa.getMember().getHighlight());
			//获取观点
			//获取推荐策略
			List<IfaSpaceStrategyInfoVO> recommendedStrategies = ifaSpaceService.getRecommendedStrategies(vo.getMemberId(),"","",loginMemberId);
			vo.setRecommendedStrategies(recommendedStrategies);
			//获取推荐组合
			vo.setRecommendedPortfolios(ifaSpaceService.getRecommendedPortfolios(vo.getMemberId(),"","",loginMemberId));
			//获取推荐的基金
			vo.setFundList(ifaSpaceService.getSpaceFundList(vo.getMemberId(),langCode,"",""));
			//获取我的业绩【Performance】 
			double aumNum = this.getIfaAUM(ifa.getId(),currency);
			vo.setAum((int)aumNum);
			double totalReturnValue = this.getIfaTotalReturnValue(ifa.getId());
			vo.setTotalReturn(totalReturnValue);
			//获取贴子数量等普通属性
			vo.setFocusCount(getFocusCount(memberId));
			vo.setFollowCount(getFollowCount(memberId));
			vo.setTopciCount(getTopciCount(memberId));

			
			return vo;
		}
		
		return null;
	}
	
	/**
	 * 获取投资人空间的基本数据
	 * @author mqzou 2017-03-21
	 * @param memberId
	 * @param langCode
	 * @param loginMemberId
	 * @return
	 */
	@Override
	public CommunitySpaceVO getInvestorSpaceVO(String memberId, String langCode, String loginMemberId) {
		CommunitySpaceVO vo = new CommunitySpaceVO();
		MemberIndividual individual=memberBaseService.findIndividualMember(memberId);
		if(null!=individual){
			//String ifaId = individual.getId();
			//个人基础信息，包括头像、昵称等
			vo.setMemberId(memberId);
			vo.setGender(individual.getGender());
			String ifaHeadImg = PageHelper.getUserHeadUrl(individual.getMember().getIconUrl(), individual.getGender());
			vo.setIconUrl(ifaHeadImg);
			vo.setNickName(individual.getMember().getNickName());
			//获取IFA的个人特征
			List<String> charaterList = this.getIfaCharacterList(individual.getMember(), langCode);
			vo.setCharacterList(charaterList);
			
			//获取个人最新心情
			//String ifaHightlight = this.getIfaHightlight(ifaId);
			vo.setHightlight(individual.getMember().getHighlight());
			
			//获取贴子数量等普通属性
			vo.setFocusCount(getFocusCount(memberId));
			vo.setFollowCount(getFollowCount(memberId));
			vo.setTopciCount(getTopciCount(memberId));

			
			return vo;
		}
		
		return null;
	}
	
	/**
	 * 获取IFA的个人
	 * @author wwlin
	 */
	public List<String> getIfaCharacterList(MemberBase member,String langCode) {
		String liveRegion=null!=member.getLiveRegion()? member.getLiveRegion():"";
		String langSpoken=null!=member.getLanguageSpoken()?member.getLanguageSpoken():"";
		String investField=null!=member.getInvestField()?member.getInvestField():"";
		String investStyle=null!=member.getInvestStyle()?member.getInvestStyle():"";
		
		liveRegion=sysParamService.findNameByCode(liveRegion, langCode);
		langSpoken=sysParamService.findNameByCode(langSpoken, langCode);
		investField=sysParamService.findNameByCode(investField, langCode);
		investStyle=sysParamService.findNameByCode(investStyle, langCode);
		List<String> charaterList=new ArrayList<String>();
		String[] regions=liveRegion.split(",");
		String[] langs=langSpoken.split(",");
		String[] field=investField.split(",");
		String[] style=investStyle.split(",");
		charaterList.addAll(Arrays.asList(regions));
		charaterList.addAll(Arrays.asList(langs));
		charaterList.addAll(Arrays.asList(field));
		charaterList.addAll(Arrays.asList(style));
		
		return charaterList;
	}
	
	/**
	 * 获取IFA的个人心情
	 * @author wwlin
	 */
	public String getIfaHightlight(String ifaId) {
		//获取个人最新心情 
		String hql1 = "from IfaExtInfo t where t.ifa.id='"+ifaId+"'";
		List<IfaExtInfo> list1 = this.baseDao.find(hql1, null, false);
		if(!list1.isEmpty()){
			IfaExtInfo extVO = list1.get(0);
				String hightlight = extVO.getHighlight();
				return hightlight;
		}
		return "";
	}
	
	/**
	 * 获取ifa个人所管理的所有资产的总值 总资产=总市值+总现金
		portfolio_hold里面只有总市值
		portfolio_hold_account这张表有总现金
	 * @author mqzou
	 * @date 2017-03-16
	 * @param ifaId
	 * @param hightLight
	 * @return
	 */
	public double getIfaAUM(String ifaId,String toCurrency) {
		//double aum = 0;
		double totalMarketValue = 0;
		String hql="from PortfolioHold r where r.ifa.id=? ";
		List params=new ArrayList();
		params.add(ifaId);
		List<PortfolioHold> list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=list && !list.isEmpty()){
				for(PortfolioHold each : list){
					if(null!=each.getTotalMarketValue()){
						String eachfromCurrency = each.getBaseCurrency();
						Double marketValue = each.getTotalMarketValue();
						
						/*BigDecimal bg=new BigDecimal(marketValue);  
						String aa = bg.toPlainString();
						
						Double www = Double.parseDouble(aa);
						
						
						System.out.println(www);*/
						
						
					//  DecimalFormat df = new DecimalFormat("0.##"); // ##表示2位小数  
						  DecimalFormat df = new DecimalFormat("########.00"); 
						  String aa =  df.format(marketValue);
						  //Double d = new Double(marketValue);  
						  System.out.println(aa);  
						
						  double tt = StrUtils.getDoubleVal("100000000.00"); 
						  double ttt = tt*10;
						  String aaa =  df.format(ttt);
						
						java.text.NumberFormat nf = java.text.NumberFormat.getInstance();   
						nf.setGroupingUsed(false);  
						Double newMarketValue = StrUtils.getDoubleVal(aa); 
						Double rate = 1.0;
						if(marketValue>0){
							WebExchangeRate exchangeRate = fundInfoService.findExchangeRate(eachfromCurrency, toCurrency);
							if (null != exchangeRate) {
								rate = exchangeRate.getRate();
								marketValue = marketValue*rate;
							}
						}
						totalMarketValue += marketValue;
					}
						
				}
		}
		//总现金
		double totalCashAvailable = 0;
		hql="from PortfolioHoldAccount r where r.ifa.id=? ";
		List params1=new ArrayList();
		params1.add(ifaId);
		List<PortfolioHoldAccount> list1 = this.baseDao.find(hql.toString(), params1.toArray(), false);
		if(null!=list1 && !list1.isEmpty()){
				for(PortfolioHoldAccount each : list1){
					if(null!=each.getCashAvailable()){
						String eachfromCurrency = each.getBaseCurrency();
						double cashAvailable = each.getCashAvailable();
						//System.out.println(cashAvailable);  
						double rate = 1;
						if(cashAvailable>0){
							WebExchangeRate exchangeRate = fundInfoService.findExchangeRate(eachfromCurrency, toCurrency);
							if (null != exchangeRate) {
								rate = exchangeRate.getRate();
								cashAvailable = cashAvailable*rate;
							}
						}
						totalCashAvailable += cashAvailable;
					}
						
				}
		}
		
		
		double total = totalMarketValue+totalCashAvailable;
		return total;
	}
	
	/**
	 * 获取ifa个人total_return_value 
	 * @author mqzou
	 * @date 2017-03-16
	 * @param ifaId
	 * @param hightLight
	 * @return
	 */
	public double getIfaTotalReturnValue(String ifaId) {
		String hql="from PortfolioHold r where r.ifa.id=? order by r.totalReturnRate desc";
		List params=new ArrayList();
		params.add(ifaId);
		List<PortfolioHold> list = this.baseDao.find(hql.toString(), params.toArray(), false);
		//double totalReturnValue = 0; //
		double totalReturnRate = 0;
	//	double result = 0;
		if(null!=list&&!list.isEmpty()){
			PortfolioHold hold=(PortfolioHold)list.get(0);
			totalReturnRate = null!=hold.getTotalReturnRate()?hold.getTotalReturnRate():0;
		}
		
		return totalReturnRate;//result
	}
	
	/**
	 * 获取粉丝的数量
	 * @author mqzou 2017-03-16
	 * @param memberId
	 * @return
	 */
	@Override
	public int getFollowCount(String memberId){
		String hql=" select count(*) from CommunityFocus f where f.focus.id=?";
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		List list=this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			Object object=list.get(0);
			if(null!=object && !"".equals(object.toString())){
				return Integer.parseInt(object.toString());
			}
		}
		return 0;
	}
	
	/**
	 * 获取关注的数量
	 * @author mqzou 2017-03-16
	 * @param memberId
	 * @return
	 */
	@Override
	public int getFocusCount(String memberId){
		String hql=" select count(*) from CommunityFocus f where f.creator.id=?";
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		List list=this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			Object object=list.get(0);
			if(null!=object && !"".equals(object.toString())){
				return Integer.parseInt(object.toString());
			}
		}
		return 0;
	}
	
	/**
	 * 获取帖子的数量
	 * @author mqzou 2017-03-16
	 * @param memberId
	 * @return
	 */
	@Override
	public int getTopciCount(String memberId) {
		String hql="select count(*) from CommunityTopic r where r.creator.id=? and (r.status=1 and r.sourceId is null or r.sourceId='')";
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		List list=this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			Object object=list.get(0);
			if(null!=object && !"".equals(object.toString())){
				return Integer.parseInt(object.toString());
			}
		}
		return 0;
	}
	
	/**
	 * 获取ifa推荐的新闻列表
	 * @author mqzou 2017-03-17
	 * @param jsonPaging
	 * @param member 当前登录人
	 * @param ifaMemberId
	 * @return
	 */
	@Override
	public JsonPaging findIfaRecommendNews(JsonPaging jsonPaging, String loginMemberId, String ifaMemberId,String keyWord) {
		StringBuilder sql=new StringBuilder();
		List<Object> params=new ArrayList<Object>();
		sql.append(" SELECT r.*,c.content,t.id as topicId,t.`comment_count`,t.`like_count`,t.`unlike_count`,t.`read_count`  ");
		sql.append(" FROM news_info r  inner join  community_topic t on  t.`source_id`=r.id and t.status=1 ");
		// modify by wwluo 2017-03-30
		if (StringUtils.isNotBlank(ifaMemberId)) {
			sql.append(" and t.creator_id=?");
			params.add(ifaMemberId);
		}
		// modify end
		sql.append(" left join community_content c on t.id=c.id  where 1=1 ");
		if(!loginMemberId.equals(ifaMemberId)){
			sql.append(" and  CASE t.`visitor` WHEN 'friend' THEN ? IN(SELECT f.`to_member_id` FROM web_friend f WHERE f.`from_member_id`=?)");
			sql.append(" WHEN 'client' THEN ? IN (SELECT c.`member_id` FROM crm_customer c left join member_ifa i on c.ifa_id=i.id WHERE i.`member_id`=?) ELSE 1=1 END");
			
			params.add(loginMemberId);
			params.add(ifaMemberId);
			params.add(loginMemberId);
			params.add(ifaMemberId);
		}
		if(null!=keyWord && !"".equals(keyWord)){
			sql.append(" and (c.content like ? or r.title like ?)");
			params.add("%"+keyWord+"%");
			params.add("%"+keyWord+"%");
		}
		jsonPaging=springJdbcQueryManager.springJdbcQueryForPaging(sql.toString(), params.toArray(), jsonPaging);
		long total=springJdbcQueryManager.springJdbcQueryForTotal(sql.toString(), params.toArray());
		jsonPaging.setTotal(Integer.valueOf(String.valueOf(total)));
		List<CommunityNewsListVO> list=new ArrayList<CommunityNewsListVO>();
		if(null!=jsonPaging.getList()){
			Iterator it=jsonPaging.getList().iterator();
			while (it.hasNext()) {
				HashMap<String, Object> map = (HashMap<String, Object>) it.next();
				String id=getMapValue(map, "id");
				String title=getMapValue(map, "title");
				String description=getMapValue(map, "description");
				String url=getMapValue(map, "lit_pic");
				String pubDate=getMapValue(map, "pub_date");
				String content=getMapValue(map, "content");
				String commentCount=getMapValue(map, "comment_count");
				String likeCount=getMapValue(map, "like_count");
				String unlikeCount=getMapValue(map, "unlike_count");
				String readCount=getMapValue(map, "read_count");
				String topicId=getMapValue(map, "topicId");
				//String lastUpdate=getMapValue(map, "create_time");
				
				
				CommunityNewsListVO vo=new CommunityNewsListVO();
				vo.setTopicId(topicId);
				vo.setTitle(StringEscapeUtils.unescapeHtml(title));
				vo.setDescription(description);
				vo.setDateTime(pubDate);
				vo.setNewsUrl(url);
				vo.setContent(content);
				vo.setNewsId(id);
				vo.setLikeCount(null!=likeCount && !"".equals(likeCount)?Integer.valueOf(likeCount):0);
				vo.setCommentCount(null!=commentCount && !"".equals(commentCount)?Integer.valueOf(commentCount):0);
				vo.setUnlikeCount(null!=unlikeCount && !"".equals(unlikeCount)?Integer.valueOf(unlikeCount):0);
				vo.setReadCount(null!=readCount && !"".equals(readCount)?Integer.valueOf(readCount):0);
				

				//查看的人是否点赞或踩
				CommunityBehavior behavior=communityService.findBehavior(topicId, loginMemberId, CommonConstantsWeb.COMUNITY_BEHAVIOR_LIKE, CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_TOPIC);
				if(null!=behavior){
					vo.setIsLike(1);
				}
				 behavior=communityService.findBehavior(topicId, loginMemberId, CommonConstantsWeb.COMUNITY_BEHAVIOR_UNLIKE, CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_TOPIC);
				 if(null!=behavior){
					vo.setIsUnlike(1);
				}
				
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	
	/**
	 * 获取观点列表
	 * @author wwlin 2017-03-17
	 * @param jsonPaging
	 * @param member 当前登录人
	 * @param ifaMemberId
	 * @return
	 */
	@Override
	public JsonPaging findIfaRecommendInsight(JsonPaging jsonPaging, String loginMemberId, String ifaMemberId,String lang) {
		/*StringBuilder hql=new StringBuilder();
		hql.append(" from NewsInfo r where r.id in (select t.sourceId from CommunityTopic t where t.creator.id=? and t.status=1 ");
		hql.append(" and case  when t.visitor= 'friend'  then ? in (SELECT f.toMember.id FROM WebFriend f WHERE f.fromMember.id=?)");
		hql.append(" when t.visitor='client' then ? in (SELECT c.member.id FROM CrmCustomer c left join MemberIfa i on c.ifa.id=i.id WHERE i.member.id=?) else 1=1 end");
		hql.append(" )");*/
		StringBuilder sql=new StringBuilder();
		sql.append(" SELECT t.*,e.content FROM `community_topic` t,`community_content` e  WHERE e.id=t.id and t.`is_insight`=1 AND t.`creator_id`=? and t.status=1 ");
		sql.append("AND CASE t.`visitor` WHEN 'friend' THEN ? IN(SELECT f.`to_member_id` FROM web_friend f WHERE f.`from_member_id`=?)");
		sql.append(" WHEN 'client' THEN ? IN (SELECT c.`member_id` FROM crm_customer c left join member_ifa i on c.ifa_id=i.id WHERE i.`member_id`=?) ELSE 1=1 END");
		List<Object> params=new ArrayList<Object>();
		params.add(ifaMemberId);
		params.add(loginMemberId);
		params.add(ifaMemberId);
		params.add(loginMemberId);
		params.add(ifaMemberId);
		jsonPaging=springJdbcQueryManager.springJdbcQueryForPaging(sql.toString(), params.toArray(), jsonPaging);
		List<FrontCommunityTopicVO> list=new ArrayList<FrontCommunityTopicVO>();
		if(null!=jsonPaging.getList()){
			Iterator it=jsonPaging.getList().iterator();
			while (it.hasNext()) {
				HashMap<String, Object> map = (HashMap<String, Object>) it.next();
				String id=getMapValue(map, "id");
				String title=getMapValue(map, "title");
				String createTime=getMapValue(map, "create_time");
				String sectionId=getMapValue(map, "section_id");
				String readCount=getMapValue(map, "read_count");
				String commentCount=getMapValue(map, "comment_count");
				String content=getMapValue(map, "content");
				if(!StringUtils.isNotBlank(readCount)){readCount="0";}
				if(!StringUtils.isNotBlank(commentCount)){commentCount="0";}
				FrontCommunityTopicVO vo=new FrontCommunityTopicVO();
				CommunitySection section =  communityService.getCommunitySectionInfo(sectionId); 
				if(null!=section) vo.setSectionName(section.getSectionNameEn());
				vo.setId(id);
				vo.setTitle(StringEscapeUtils.unescapeHtml(title));
				vo.setCommentCount(commentCount);
				vo.setReadCount(readCount);
				
				String newContent = delHTMLTag(content);
				if(newContent.length()>150)newContent = newContent.substring(0, 150);
				vo.setContent(newContent);
				
				//格式化时间格式
				if(!"".equals(createTime)){
					Date createDateTime = DateUtil.StringToDate(createTime, DateUtil.DEFAULT_DATE_TIME_FORMAT);
					vo.setPublishTimeFormat(DateUtil.getDateTimeGoneFormatStr(createDateTime,lang,""));
				}
				
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	
	public String delHTMLTag(String htmlStr){ 
        String regExStyle="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式 
        String regExHtml="<[^>]+>"; //定义HTML标签的正则表达式 
         
        Pattern pStyle=Pattern.compile(regExStyle,Pattern.CASE_INSENSITIVE); 
        Matcher mStyle=pStyle.matcher(htmlStr); 
        htmlStr=mStyle.replaceAll(""); //过滤style标签 
         
        Pattern pHtml=Pattern.compile(regExHtml,Pattern.CASE_INSENSITIVE); 
        Matcher mHtml=pHtml.matcher(htmlStr); 
        htmlStr=mHtml.replaceAll(""); //过滤html标签 
        
        htmlStr=htmlStr.replace(" ","");
        htmlStr=htmlStr.replaceAll("\\s*|\t|\r|\n","");
        htmlStr=htmlStr.replace("“","");
        htmlStr=htmlStr.replace("”","");
        htmlStr=htmlStr.replaceAll("　","");
          
        return htmlStr.trim(); //返回文本字符串 
    }

	/**
	 * 获取贴子列表
	 * @author mqzou 2017-03-17
	 * @param jsonPaging
	 * @param member 当前登录人
	 * @param ifaMemberId
	 * @return
	 */
	@Override
	public JsonPaging findIfaRecommendTopic(JsonPaging jsonPaging, MemberBase member, String ifaMemberId,String lang) {
		/*StringBuilder hql=new StringBuilder();
		hql.append(" from NewsInfo r where r.id in (select t.sourceId from CommunityTopic t where t.creator.id=? and t.status=1 ");
		hql.append(" and case  when t.visitor= 'friend'  then ? in (SELECT f.toMember.id FROM WebFriend f WHERE f.fromMember.id=?)");
		hql.append(" when t.visitor='client' then ? in (SELECT c.member.id FROM CrmCustomer c left join MemberIfa i on c.ifa.id=i.id WHERE i.member.id=?) else 1=1 end");
		hql.append(" )");*/
		StringBuilder sql=new StringBuilder();
		sql.append(" SELECT t.*,e.content FROM `community_topic` t  ");
		sql.append(" left join community_content e on t.id=e.id");
		sql.append("  WHERE  t.`creator_id`=? and t.status=1 ");
		List<Object> params=new ArrayList<Object>();
		params.add(ifaMemberId);
		if(!member.getId().equals(ifaMemberId)){
			sql.append(" AND CASE t.`visitor` WHEN 'friend' THEN ? IN(SELECT f.`to_member_id` FROM web_friend f WHERE f.`from_member_id`=?)");
			sql.append(" WHEN 'client' THEN ? IN (SELECT c.`member_id` FROM crm_customer c left join member_ifa i on c.ifa_id=i.id WHERE i.`member_id`=?) ELSE 1=1 END");
			params.add(member.getId());
			params.add(ifaMemberId);
			params.add(member.getId());
			params.add(ifaMemberId);
		}
		sql.append(" and (t.source_id is null or t.source_id='')");
		jsonPaging=springJdbcQueryManager.springJdbcQueryForPaging(sql.toString(), params.toArray(), jsonPaging);
		long total=springJdbcQueryManager.springJdbcQueryForTotal(sql.toString(), params.toArray());
		jsonPaging.setTotal(Integer.valueOf(String.valueOf(total)));
		List<FrontCommunityTopicVO> list=new ArrayList<FrontCommunityTopicVO>();
		if(null!=jsonPaging.getList()){
			Iterator it=jsonPaging.getList().iterator();
			while (it.hasNext()) {
				HashMap<String, Object> map = (HashMap<String, Object>) it.next();
				String id=getMapValue(map, "id");
				String title=getMapValue(map, "title");
				String createTime=getMapValue(map, "create_time");
				String sectionId=getMapValue(map, "section_id");
				String readCount=getMapValue(map, "read_count");
				String commentCount=getMapValue(map, "comment_count");
				String content=getMapValue(map, "content");
				if(!StringUtils.isNotBlank(readCount)){readCount="0";}
				if(!StringUtils.isNotBlank(commentCount)){commentCount="0";}
				FrontCommunityTopicVO vo=new FrontCommunityTopicVO();
				CommunitySection section =  communityService.getCommunitySectionInfo(sectionId); 
				//获取版块名称
				if(StringUtils.isNotBlank(sectionId)&&null!=section&&StringUtils.isNotBlank(section.getId())){
					//if(null!=section&&StringUtils.isNotBlank(section.getId())){
						vo.setSectionId(section.getId());
						if("en".equals(lang))vo.setSectionName(section.getSectionNameEn());
						else if("sc".equals(lang))vo.setSectionName(section.getSectionNameSc());
						else if("tc".equals(lang))vo.setSectionName(section.getSectionNameTc());
					//}
				}
				vo.setId(id);
				vo.setTitle(StringEscapeUtils.unescapeHtml(title));
				vo.setCommentCount(commentCount);
				vo.setReadCount(commentCount);
				vo.setContent(StringEscapeUtils.unescapeHtml(content));
				
				//格式化时间格式
				if(!"".equals(createTime)){
					Date createDateTime = DateUtil.StringToDate(createTime, DateUtil.DEFAULT_DATE_TIME_FORMAT);
					vo.setPublishTimeFormat(DateUtil.getDateTimeGoneFormatStr(createDateTime,lang,member.getDateFormat()));
				}
				
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		//List list=baseDao.find(hql.toString(), params.toArray(), false);
		
		//jsonPaging=baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		return jsonPaging;
	}
	
	/**
	 * 获取ifa空间的Q&A 列表
	 * @author mqzou 2017-03-20
	 * @param jsonPaging
	 * @param ifaMemberId
	 * @param memberId
	 * @return
	 */
	@Override
	public JsonPaging findIfaQuestionList(JsonPaging jsonPaging, String ifaMemberId, String memberId,String langCode) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from CommunityQuestion r where r.answerer.id=? and r.status=1");
		List<Object> params=new ArrayList<Object>();
		params.add(ifaMemberId);
		jsonPaging=baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List<QuestionListVO> list=new ArrayList<QuestionListVO>();
		if(null!=jsonPaging.getList()){
			Iterator it=jsonPaging.getList().iterator();
			while (it.hasNext()) {
				CommunityQuestion question= (CommunityQuestion) it.next();
				QuestionListVO vo=new QuestionListVO();
				vo.setCommentCount(null!=question.getCommentCount()?question.getCommentCount():0);
				vo.setContent(StringEscapeUtils.unescapeHtml(question.getTitle()));
				vo.setIsAnswer(question.getIsAnswer());
				vo.setId(question.getId());
				vo.setLikeCount(null!=question.getLikeCount()?question.getLikeCount():0);
				vo.setMemberId(question.getCreator().getId());
				
				if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==question.getCreator().getSubMemberType()){
					String nickName=getCommonMemberName(question.getCreator().getId(),langCode, "2");
					vo.setMemberName(nickName);
				}else {
					vo.setMemberName(question.getCreator().getNickName());
				}
				
				//vo.setMemberName(question.getCreator().getNickName());
				vo.setMemberUrl(question.getCreator().getIconUrl());
				vo.setMemberType(question.getCreator().getMemberType().toString());
				String gender="";
				if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==question.getCreator().getSubMemberType()){
					MemberIfa ifa=memberBaseService.findIfaMember(question.getCreator().getId());
					gender=ifa.getGender();
				}else if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_INDIVIDUAL==question.getCreator().getSubMemberType()){
					MemberIndividual individual=memberBaseService.findIndividualMember(question.getCreator().getId());
					gender=individual.getGender();
				}
				vo.setMemberGender(gender);
				vo.setUnlikeCount(null!=question.getUnlikeCount()?question.getUnlikeCount():0);
				vo.setCommentCount(null!=question.getCommentCount()?question.getCommentCount():0);
				CommunityBehavior  behavior=communityService.findBehavior(vo.getId(), memberId, CommonConstantsWeb.COMUNITY_BEHAVIOR_LIKE,CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_QUESTION);
			    if(null!=behavior)
			    	vo.setIsLike(1);
			    behavior=communityService.findBehavior(vo.getId(), memberId, CommonConstantsWeb.COMUNITY_BEHAVIOR_UNLIKE,CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_QUESTION);
			    if(null!=behavior){
			    	vo.setIsUnlike(1);
			    }
			    vo.setAnswerList(findQuestionAnswerList(vo.getId(), langCode, memberId));
				vo.setCreateDate(question.getCreateTime());
				vo.setCreateDateStr(DateUtil.dateToDateString(question.getCreateTime(), DateUtil.DEFAULT_DATE_TIME_FORMAT));
			    list.add(vo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	
	/**
	 * 获取投资人空间的Q&A 列表
	 * @author mqzou 2017-03-21
	 * @param jsonPaging
	 * @param invMemberId
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	@Override
	public JsonPaging findInvestorQuestionList(JsonPaging jsonPaging, String invMemberId, String memberId, String langCode) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from CommunityQuestion r where r.creator.id=? and r.status=1");
		List<Object> params=new ArrayList<Object>();
		params.add(invMemberId);
		jsonPaging=baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List<QuestionListVO> list=new ArrayList<QuestionListVO>();
		if(null!=jsonPaging.getList()){
			Iterator it=jsonPaging.getList().iterator();
			while (it.hasNext()) {
				CommunityQuestion question= (CommunityQuestion) it.next();
				QuestionListVO vo=new QuestionListVO();
				vo.setCommentCount(null!=question.getCommentCount()?question.getCommentCount():0);
				vo.setContent(StringEscapeUtils.unescapeHtml(question.getTitle()));
				vo.setIsAnswer(question.getIsAnswer());
				vo.setId(question.getId());
				vo.setLikeCount(null!=question.getLikeCount()?question.getLikeCount():0);
				vo.setMemberId(question.getCreator().getId());
				
				if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==question.getCreator().getSubMemberType()){
					String nickName=getCommonMemberName(question.getCreator().getId(),langCode, "2");
					vo.setMemberName(nickName);
				}else {
					vo.setMemberName(question.getCreator().getNickName());
				}
				
				//vo.setMemberName(question.getCreator().getNickName());
				vo.setMemberUrl(question.getCreator().getIconUrl());
				String gender="";
				if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==question.getCreator().getSubMemberType()){
					MemberIfa ifa=memberBaseService.findIfaMember(question.getCreator().getId());
					gender=ifa.getGender();
				}else if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_INDIVIDUAL==question.getCreator().getSubMemberType()){
					MemberIndividual individual=memberBaseService.findIndividualMember(question.getCreator().getId());
					gender=individual.getGender();
				}
				vo.setMemberGender(gender);
				vo.setMemberType(question.getCreator().getMemberType().toString());
				
				vo.setUnlikeCount(null!=question.getUnlikeCount()?question.getUnlikeCount():0);
				vo.setCommentCount(null!=question.getCommentCount()?question.getCommentCount():0);
				CommunityBehavior  behavior=communityService.findBehavior(vo.getId(), memberId, CommonConstantsWeb.COMUNITY_BEHAVIOR_LIKE,CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_QUESTION);
			    if(null!=behavior)
			    	vo.setIsLike(1);
			    behavior=communityService.findBehavior(vo.getId(), memberId, CommonConstantsWeb.COMUNITY_BEHAVIOR_UNLIKE,CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_QUESTION);
			    if(null!=behavior){
			    	vo.setIsUnlike(1);
			    }
			    vo.setAnswerList(findQuestionAnswerList(vo.getId(), langCode, memberId));
				
			    MemberIfa ifa=memberBaseService.findIfaMember(question.getAnswerer().getId());
			    String url = PageHelper.getUserHeadUrl(ifa.getMember().getIconUrl(), ifa.getGender());
			    vo.setAnswererId(question.getAnswerer().getId());
			    vo.setAnswererName(question.getAnswerer().getNickName());
			    vo.setAnswererUrl(url);
			    vo.setAnswerMemberType(question.getAnswerer().getMemberType().toString());
			    vo.setCreateDate(question.getCreateTime());
			    list.add(vo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	
	/**
	 * 获取问题的所有回复列表
	 * @author mqzou 2017-03-20
	 * @param questionId
	 * @return
	 */
	@Override
	public List<QuestionAnswerListVO> findQuestionAnswerList(String questionId,String langCode,String memberId) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from CommunityComment r where r.targetId=? and r.target=? and r.status=1 and r.commentType='answer'");
		List<Object> params=new ArrayList<Object>();
		params.add(questionId);
		params.add(CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_QUESTION);
		List resultList=baseDao.find(hql.toString(), params.toArray(), false);
		List<QuestionAnswerListVO> list=new ArrayList<QuestionAnswerListVO>();
		if(null!=resultList && !resultList.isEmpty()){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				CommunityComment comment = (CommunityComment) it.next();
				QuestionAnswerListVO vo=new QuestionAnswerListVO();
				vo.setContent(StringEscapeUtils.unescapeHtml(comment.getContent()));
				vo.setCommentCount(null!=comment.getCommentCount()?comment.getCommentCount():0);
				vo.setDateTime(DateUtil.getDateTimeGoneFormatStr(comment.getCreateTime(), langCode, ""));
				vo.setId(comment.getId());
				vo.setLikeCount(null!=comment.getLikeCount()?comment.getLikeCount():0);
				vo.setUnlikeCount(null!=comment.getUnlikeCount()?comment.getUnlikeCount():0);
				CommunityBehavior  behavior=communityService.findBehavior(vo.getId(), memberId, CommonConstantsWeb.COMUNITY_BEHAVIOR_LIKE,CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_COMMENT);
			    if(null!=behavior)
			    	vo.setIsLike(1);
			    behavior=communityService.findBehavior(vo.getId(), memberId, CommonConstantsWeb.COMUNITY_BEHAVIOR_UNLIKE,CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_COMMENT);
			    if(null!=behavior){
			    	vo.setIsUnlike(1);
			    }
			    list.add(vo);
			}
		}
		
		return list;
	}
	
	/**
	 * 获取ifa的收藏列表
	 * @author mqzou 2017-03-20
	 * @param jsonPaging
	 * @param ifaMemberId
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	@Override
	public JsonPaging findIfaFavoriteList(JsonPaging jsonPaging, String ifaMemberId, String memberId, String langCode,String dateFormat) {
		StringBuilder sql=new StringBuilder();
		sql.append(" SELECT t.*,e.content FROM `community_topic` t  inner join community_behavior b on t.id=b.target_id and b.behavior_type='favorite' ");
		sql.append(" left join community_content e on t.id=e.id");
		sql.append(" WHERE t.`is_insight`!=1 AND b.`creator_id`=? and t.status=1");
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		if(!memberId.equals(ifaMemberId)){
			sql.append(" AND(t.creator_id=? or   CASE t.`visitor` WHEN 'friend' THEN ? IN(SELECT f.`to_member_id` FROM web_friend f WHERE f.`from_member_id`=?)");
			sql.append(" WHEN 'client' THEN ? IN (SELECT c.`member_id` FROM crm_customer c left join member_ifa i on c.ifa_id=i.id WHERE i.`member_id`=?) ELSE 1=1 END)");
			params.add(ifaMemberId);
			params.add(memberId);
			params.add(ifaMemberId);
			params.add(memberId);
			params.add(ifaMemberId);
		}
		
		jsonPaging=springJdbcQueryManager.springJdbcQueryForPaging(sql.toString(), params.toArray(), jsonPaging);
		long total=springJdbcQueryManager.springJdbcQueryForTotal(sql.toString(), params.toArray());
		jsonPaging.setTotal(Integer.valueOf(String.valueOf(total)));
		List<FrontCommunityTopicVO> list=new ArrayList<FrontCommunityTopicVO>();
		if(null!=jsonPaging.getList()){
			Iterator it=jsonPaging.getList().iterator();
			while (it.hasNext()) {
				HashMap<String, Object> map = (HashMap<String, Object>) it.next();
				String id=getMapValue(map, "id");
				String title=getMapValue(map, "title");
				String createTime=getMapValue(map, "create_time");
				String sectionId=getMapValue(map, "section_id");
				String readCount=getMapValue(map, "read_count");
				String commentCount=getMapValue(map, "comment_count");
				String content=getMapValue(map, "content");
				String createMemberId=getMapValue(map, "creator_id");
				if(!StringUtils.isNotBlank(readCount)){readCount="0";}
				if(!StringUtils.isNotBlank(commentCount)){commentCount="0";}
				FrontCommunityTopicVO vo=new FrontCommunityTopicVO();
				CommunitySection section =  communityService.getCommunitySectionInfo(sectionId); 
				if(null!=section){
					if(CommonConstants.LANG_CODE_TC.equals(langCode)){
						vo.setSectionName(section.getSectionNameTc());
					}else if (CommonConstants.LANG_CODE_SC.equals(langCode)) {
						vo.setSectionName(section.getSectionNameSc());
					}else {
						vo.setSectionName(section.getSectionNameEn());
					}
					
				}
				vo.setMemberId(createMemberId);
				if(StringUtils.isNotBlank(createMemberId)){
					MemberBase mb = memberBaseService.findById(createMemberId);
					if(null!=mb&&StringUtils.isNotBlank(mb.getId())){
						if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==mb.getSubMemberType()){
							String nickName=getCommonMemberName(mb.getId(),langCode, "2");
							vo.setNickName(nickName);
						}else {
							vo.setNickName(mb.getNickName());
						}
					}
				}

				
				vo.setId(id);
				vo.setTitle(StringEscapeUtils.unescapeHtml(title));
				vo.setCommentCount(commentCount);
				vo.setReadCount(readCount);
				vo.setContent(content);
				//格式化时间格式
				if(!"".equals(createTime)){
					Date createDateTime = DateUtil.StringToDate(createTime, DateUtil.DEFAULT_DATE_TIME_FORMAT);
					vo.setPublishTimeFormat(DateUtil.getDateTimeGoneFormatStr(createDateTime,langCode,dateFormat));
				}
				
				CommunityBehavior  behavior=communityService.findBehavior(vo.getId(), memberId, CommonConstantsWeb.COMUNITY_BEHAVIOR_LIKE,CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_TOPIC);
			    if(null!=behavior)
			    	vo.setIsLike(1);
			    behavior=communityService.findBehavior(vo.getId(), memberId, CommonConstantsWeb.COMUNITY_BEHAVIOR_UNLIKE,CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_TOPIC);
			    if(null!=behavior){
			    	vo.setIsUnlike(1);
			    }
			    behavior=communityService.findBehavior(vo.getId(), memberId, CommonConstantsWeb.COMUNITY_BEHAVIOR_FAVORITE,CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_TOPIC);
			    if(null!=behavior){
			    	vo.setIsFavourite(1);
			    }
				
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	
	/**
	 * 获取所关注的人的列表
	 * @author mqzou 2017-03-20
	 * @param memberId
	 * @return
	 */
	@Override
	public List<FocusMemberVO> findFocusMemberList(String memberId,String curMemberId,String langCode) {
		String hql="select r,a  from  CommunityFocus r left join  CommunityFocus a on r.focus.id=a.focus.id and a.creator.id=? where r.creator.id=?";
		List<Object> params=new ArrayList<Object>();
		params.add(curMemberId);
		params.add(memberId);
		List resultList=baseDao.find(hql, params.toArray(), false);
		List<FocusMemberVO>  list=new ArrayList<FocusMemberVO>();
		if(null!=resultList && !resultList.isEmpty()){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				Object[] objs=(Object[])it.next();
				CommunityFocus focus = (CommunityFocus) objs[0];
				CommunityFocus isFocus= (CommunityFocus) objs[1];
				FocusMemberVO vo=new FocusMemberVO();
				vo.setMemberId(focus.getFocus().getId());
				vo.setFollowerCount(getFollowCount(vo.getMemberId()));
				vo.setIconUrl(focus.getFocus().getIconUrl());
				
				if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==focus.getFocus().getSubMemberType()){
					String nickName=getCommonMemberName(focus.getFocus().getId(),langCode, "2");
					vo.setNickName(nickName);
				}else {
					vo.setNickName(focus.getFocus().getNickName());
				}
				
				//vo.setNickName(focus.getFocus().getNickName());
				vo.setTopicCount(getTopciCount(vo.getMemberId()));
				vo.setMemberType(focus.getFocus().getMemberType());
				if(curMemberId.equals(focus.getFocus().getId())){
					vo.setIsFocused(3);//自己
				}else {
					if(null!=isFocus){
						vo.setIsFocused(1);
				    }else {
				    	vo.setIsFocused(0);
					}
					
				}
				MemberBase member=focus.getFocus();
				if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==member.getSubMemberType()){
					MemberIfa ifa=memberBaseService.findIfaMember(member.getId());
					vo.setGender(ifa.getGender());
				}else if (CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_INDIVIDUAL==member.getSubMemberType()) {
					MemberIndividual individual=memberBaseService.findIndividualMember(member.getId());
					vo.setGender(individual.getGender());
				}
				
				list.add(vo);
			}
		}
		return list;
	}
	
	/**
	 * 获取所有粉丝列表
	 * @author mqzou 2017-03-20
	 * @param memberId
	 * @return
	 */
	@Override
	public List<FocusMemberVO> findFollowerList(String memberId,String curMemberId,String langCode) {
		String hql="select r,a from  CommunityFocus r left join  CommunityFocus a on r.creator.id=a.focus.id and a.creator.id=? where r.focus.id=? ";
		List<Object> params=new ArrayList<Object>();
		params.add(curMemberId);
		params.add(memberId);
		List resultList=baseDao.find(hql, params.toArray(), false);
		List<FocusMemberVO>  list=new ArrayList<FocusMemberVO>();
		if(null!=resultList && !resultList.isEmpty()){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				Object[] objs=(Object[])it.next();
				CommunityFocus focus = (CommunityFocus) objs[0];
				CommunityFocus isFocus= (CommunityFocus) objs[1];
				FocusMemberVO vo=new FocusMemberVO();
				vo.setMemberId(focus.getCreator().getId());
				vo.setFollowerCount(getFollowCount(vo.getMemberId()));
				vo.setIconUrl(focus.getCreator().getIconUrl());
				
				if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==focus.getCreator().getSubMemberType()){
					String nickName=getCommonMemberName(focus.getCreator().getId(),langCode, "2");
					vo.setNickName(nickName);
				}else {
					vo.setNickName(focus.getCreator().getNickName());
				}
				
				//vo.setNickName(focus.getCreator().getNickName());
				vo.setTopicCount(getTopciCount(vo.getMemberId()));
				vo.setMemberType(focus.getCreator().getMemberType());
				if(curMemberId.equals(focus.getCreator().getId())){
					vo.setIsFocused(3);//自己
				}else {
					if(null!=isFocus){
						vo.setIsFocused(1);
				    }else {
				    	vo.setIsFocused(0);
					}
					
				}
				
				MemberBase member=focus.getCreator();
				if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==member.getSubMemberType()){
					MemberIfa ifa=memberBaseService.findIfaMember(member.getId());
					vo.setGender(ifa.getGender());
				}else if (CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_INDIVIDUAL==member.getSubMemberType()) {
					MemberIndividual individual=memberBaseService.findIndividualMember(member.getId());
					vo.setGender(individual.getGender());
				}
				list.add(vo);
			}
		}
		return list;
	}

	/**
	 * 获取关注实体
	 * @author mqzou 2017-03-21
	 * @param loginMemberId
	 * @param targetMemberId
	 * @return
	 */
	@Override
	public CommunityFocus findCommunityFocus(String curMemberId, String targetMemberId) {
		String hql=" from CommunityFocus r where r.creator.id=? and r.focus.id=? ";
		List<Object> params=new ArrayList<Object>();
		params.add(curMemberId);
		params.add(targetMemberId);
		List list=baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			CommunityFocus focus=(CommunityFocus)list.get(0);
			return focus;
		}
		return null;
	}

	/**
	 * 加关注
	 * @author mqzou 2017-03-21
	 * @param focus
	 * @return
	 */
	@Override
	public CommunityFocus saveFocus(CommunityFocus focus) {
		focus =(CommunityFocus)baseDao.saveOrUpdate(focus);
		
		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA==focus.getFocus().getMemberType()){
			updateIfaPopularity(focus.getFocus().getId(), CommonConstantsWeb.IFA_POPULARITY_FOCUS_ADD);
		}
		return focus;
	}

	/**
	 * 取消关注
	 * @author mqzou 2017-03-21
	 * @param focus
	 * @return
	 */
	@Override
	public boolean deleteFocus(CommunityFocus focus) {
		try {
			if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA==focus.getFocus().getMemberType()){
				updateIfaPopularity(focus.getFocus().getId(), CommonConstantsWeb.IFA_POPULARITY_FOCUS_CANCEL);
			}
			baseDao.delete(focus);
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}
	
	/**
	 * 保存问题
	 * @author mqzou 2017-03-21
	 * @param question
	 * @return
	 */
	@Override
	public CommunityQuestion saveCommunityQuestion(CommunityQuestion question) {
		question=(CommunityQuestion)baseDao.saveOrUpdate(question);
		return question;
	}
}
