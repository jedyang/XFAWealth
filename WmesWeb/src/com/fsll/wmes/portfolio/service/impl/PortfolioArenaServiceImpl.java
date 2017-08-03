package com.fsll.wmes.portfolio.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.script.Invocable;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.buscore.fund.service.CoreFundService;
import com.fsll.buscore.fund.vo.CoreFundNavVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.PortfolioArena;
import com.fsll.wmes.entity.PortfolioArenaAip;
import com.fsll.wmes.entity.PortfolioArenaCount;
import com.fsll.wmes.entity.PortfolioArenaCumperf;
import com.fsll.wmes.entity.PortfolioArenaProduct;
import com.fsll.wmes.entity.PortfolioArenaReturn;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.StrategyAllocation;
import com.fsll.wmes.entity.StrategyInfo;
import com.fsll.wmes.entity.StrategyProduct;
import com.fsll.wmes.entity.WebInvestorVisit;
import com.fsll.wmes.entity.WebPush;
import com.fsll.wmes.entity.WebPushDetail;
import com.fsll.wmes.entity.WebView;
import com.fsll.wmes.entity.WebViewDetail;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.vo.FundInfoDataVO;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.portfolio.service.PortfolioArenaService;
import com.fsll.wmes.portfolio.vo.CriteriaVO;
import com.fsll.wmes.portfolio.vo.PortfolioArenaVO;
import com.fsll.wmes.portfolio.vo.PortfolioFundListVO;
import com.fsll.wmes.portfolio.vo.PortfolioWebPushVO;
import com.fsll.wmes.portfolio.vo.PortfolioWebViewVO;
import com.fsll.wmes.strategy.vo.WebInvestorVisitVO;
import com.fsll.wmes.web.service.WebFollowStatusService;
import com.sun.org.apache.xpath.internal.operations.And;

@Service("portfolioArenaService")
//@Transactional
public class PortfolioArenaServiceImpl extends BaseService implements
		PortfolioArenaService {

	@Autowired
	private MemberBaseService memberBaseService;
	
	@Autowired
	private WebFollowStatusService followStatusService; 
	@Autowired
	private SysParamService sysParamService;
	@Autowired
	private CoreFundService coreFundService;
	
	@Autowired
	private FundInfoService fundInfoService;
	/**
	 * 获取内容列表
	 */
	@Override
	public JsonPaging findAll(JsonPaging jsonPaging, String keyword) {
		// TODO Auto-generated method stub
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder(" from PortfolioArena l where 1=1 ");
		if(null != keyword && !"".equals(keyword)){
			hql.append(" and ( l.strategyName like ? or investmentGoal like ? or suitability like ? )");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
		}
		
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		return jsonPaging;
	}

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
	public JsonPaging findAll(JsonPaging jsonPaging, String keyword, String sector, String region, String fromDate, String toDate) {
		CriteriaVO criteria  = new CriteriaVO();
		criteria.setKeyword(keyword);
		criteria.setSector(sector);
		criteria.setGeoAllocation(region);
		criteria.setStartDate(fromDate);;
		criteria.setEndDate(toDate);
		return findByUser(jsonPaging, criteria,"");
	}

		
	/**
	 * 通过多条件获取列表
	 * @author michael
	 * modify by mqzou 2016-11-09
	 * @param jsonPaging
	 * @param criteria
	 * @return
	 */
	public JsonPaging findByUser(JsonPaging jsonPaging, CriteriaVO criteria,String langCode) {
		// TODO Auto-generated method stub
		List<Object> params = new ArrayList<Object>();
		String countHql="select  count(distinct l.id) from PortfolioArena l";
		StringBuilder hql = new StringBuilder("  ");
		StringBuilder hql1=new StringBuilder();
		hql1.append("select  distinct l from PortfolioArena l ");
		
		hql.append(" left join  WebView v on l.id=v.relateId");
		hql.append(" left join WebViewDetail d on v.id=d.view.id");
		hql.append(" left join MemberIfa i on l.creator.id=i.member.id");
		hql.append(" left join CrmCustomer c on i.id=c.ifa.id");
		hql.append("  left join WebFriend f on l.creator.id=f.fromMember.id ");
		hql.append(" left join IfafirmTeamIfa t on i.id=t.ifa.id");
		hql.append(" where 1=1 and l.creator.id is not null and l.isValid='1'");
		
		if(null != criteria.getUserId() && !"".equals(criteria.getUserId())
			&& ("mylist".equals(StrUtils.getString(criteria.getModule()))
					||"Me".equals(StrUtils.getString(criteria.getSource())))){
			hql.append(" and l.creator.id=?");
			params.add(criteria.getUserId());
		}
		
		hql.append(" AND  (( l.creator.id=? )");
		hql.append("  OR (v.scopeFlag='3'  AND (");
		hql.append(" (v.clientFlag='1' AND c.clientType='1'  AND c.member.id=?) ");
		hql.append(" OR  (v.clientFlag='0' AND 1=0)");
		hql.append("  OR  (v.clientFlag='-1' AND d.toMember.id=?)");
		hql.append(" OR (v.prospectFlag='1' AND c.clientType='0'  AND c.member.id=?) ");
		hql.append(" OR  (v.prospectFlag='0' AND 1=0)");
		hql.append(" OR  (v.prospectFlag='-1' AND  d.toMember.id=?)");
		hql.append(" OR (v.buddyFlag='1' AND  f.toMember.id=?)");
		hql.append(" OR  (v.buddyFlag='0' AND 1=0)");
		hql.append(" OR  (v.buddyFlag='-1' AND   d.toMember.id=?)");
		hql.append(" OR (v.colleagueFlag='1' AND t.team.id IN  (SELECT b.team.id FROM MemberIfa a INNER JOIN IfafirmTeamIfa b ON a.id=b.ifa.id  AND a.member.id=? ))");
		hql.append(" OR  (v.colleagueFlag='0' AND 1=0)");
		hql.append("  OR  (v.colleagueFlag='-1' AND   d.toMember.id=?)");
		hql.append(" ))OR v.scopeFlag='2')");
		params.add(criteria.getUserId());
		params.add(criteria.getUserId());
		params.add(criteria.getUserId());
		params.add(criteria.getUserId());
		params.add(criteria.getUserId());
		params.add(criteria.getUserId());
		params.add(criteria.getUserId());
		params.add(criteria.getUserId());
		params.add(criteria.getUserId());
		
		if(null != criteria.getKeyword() && !"".equals(criteria.getKeyword())){
			hql.append(" and ( l.portfolioName like ? )");
			params.add("%"+criteria.getKeyword()+"%");
		}
		if(null != criteria.getSector() && !"".equals(criteria.getSector())){
			hql.append(" and ( 1=2 ");
			String[] strings=criteria.getSector().split(",");
			for (String string : strings) {
				if("".equals(string))
					continue;
				hql.append(" or l.sector like ? ");
				params.add("%"+string+"%");
			}
			hql.append(")");
		}
		
		if(null != criteria.getGeoAllocation() && !"".equals(criteria.getGeoAllocation())){//可多选，用逗号分隔
			String[] idArrStrings = criteria.getGeoAllocation().split(",");
			hql.append(" and ( " );
			if (idArrStrings!=null && idArrStrings.length>0){
            	for(int k=0;k<idArrStrings.length;k++){
            		String cd = idArrStrings[k].toString();
            		hql.append("l.geoAllocation like '%"+ cd +"%'");
            		if (k<idArrStrings.length-1)  hql.append(" or ");
            	}
			}
			hql.append(" )");
		}
		
		if(null != criteria.getRiskLevel() && !"".equals(criteria.getRiskLevel())){
			hql.append(" and l.riskLevel=? ");
			params.add(StrUtils.getInteger(criteria.getRiskLevel()));
		}
		
		if(null != criteria.getTotalReturn() && !"".equals(criteria.getTotalReturn())){
			if ("1".equals(criteria.getTotalReturn())){
				hql.append(" and ( l.totalReturn < 0.1 )");
			}else if ("2".equals(criteria.getTotalReturn())){
				hql.append(" and ( l.totalReturn >= 0.1 and l.totalReturn < 0.2 )");
			}else if ("3".equals(criteria.getTotalReturn())){
				hql.append(" and ( l.totalReturn >= 0.2 and l.totalReturn < 0.3 )");
			}else if ("4".equals(criteria.getTotalReturn())){
				hql.append(" and ( l.totalReturn >= 0.3 and l.totalReturn < 0.4 )");
			}else if ("5".equals(criteria.getTotalReturn())){
				hql.append(" and ( l.totalReturn >= 0.4 and l.totalReturn < 0.5 )");
			}else if ("6".equals(criteria.getTotalReturn())){
				hql.append(" and ( l.totalReturn >=0.5 )");
			}
		}
		if(null != criteria.getStartDate() && !"".equals(criteria.getStartDate())){
			hql.append(" and l.lastUpdate>=?");
			params.add(DateUtil.StringToDate(criteria.getStartDate(),CoreConstants.DATE_FORMAT));
		}
		if(null != criteria.getEndDate() && !"".equals(criteria.getEndDate())){
			hql.append(" and l.lastUpdate<=?");
			params.add(DateUtil.StringToDate(criteria.getEndDate(),CoreConstants.DATE_FORMAT));
		}
		if(null != criteria.getStatus() && !"".equals(criteria.getStatus())){
			hql.append(" and l.status=?");
			params.add(criteria.getStatus());
		}
		if(null!=criteria.getPeriod() && !"".equals(criteria.getPeriod()) && null!=criteria.getDataFrom() && !"".equals(criteria.getDataFrom())
				 && null!=criteria.getDataTo() && !"".equals(criteria.getDataTo())){
			hql.append(" and l.id in (select r.portfolio.id from PortfolioArenaReturn r where r.periodCode=? and r.increase between ? and ?  )");
			//hql.append(" and r.periodCode=? and r.increase between ? and ? ");
			params.add("return_period_code_"+criteria.getPeriod());
			params.add(Double.valueOf(criteria.getDataFrom())/100);
			params.add(Double.valueOf(criteria.getDataTo())/100);
		}
		if(null!=criteria.getPrefData() && !"".equals(criteria.getPrefData())){
			JSONArray array= JSONArray.fromObject(criteria.getPrefData());
			if(null!=array && !array.isEmpty()){
				hql.append(" and l.id in (select r.portfolio.id from PortfolioArenaReturn r where ");
				for (int i = 0; i < array.size(); i++) {
					JSONObject object=array.getJSONObject(i);
					if(null!=object){
						String period=object.getString("period");
						String dataFrom=object.getString("dataFrom");
						String dataTo=object.getString("dataTo");
						
						if(i>0){
							hql.append(" or ");
						}
						if(StringUtils.isNotBlank(dataFrom) && StringUtils.isNotBlank(dataTo)){
							hql.append("  r.periodCode=? and r.increase between ? and ?");
							params.add("return_period_code_"+period);
							params.add(Double.valueOf(dataFrom)/100);
							params.add(Double.valueOf(dataTo)/100);
						}else if(StringUtils.isNotBlank(dataFrom) && !StringUtils.isNotBlank(dataTo)){
							hql.append("  r.periodCode=? and r.increase >?");
							params.add("return_period_code_"+period);
							params.add(Double.valueOf(dataFrom)/100);
							
						}else if (!StringUtils.isNotBlank(dataFrom) && StringUtils.isNotBlank(dataTo)) {
							hql.append("  r.periodCode=? and r.increase <?");
							params.add("return_period_code_"+period);
							params.add(Double.valueOf(dataTo)/100);
						}
						
						
						
					}
				}
				hql.append(" )");
			}
		}
		hql1.append(hql);
		jsonPaging = this.baseDao.selectJsonPaging( hql1.toString(), params.toArray(), jsonPaging, false);
		List<PortfolioArenaVO> result = new ArrayList<PortfolioArenaVO>();
		List<PortfolioArena> list = (List<PortfolioArena>)jsonPaging.getList();
		List countList=this.baseDao.find(countHql+hql.toString(), params.toArray(), false);
		
		if (null!=list && !list.isEmpty()){
			for (PortfolioArena info: list){
				PortfolioArenaVO infoVo = new PortfolioArenaVO(info);
				infoVo.setTotalReturn(null!=info.getTotalReturn()?info.getTotalReturn()*100:0);
				//设置ifa信息
				try {
					MemberIfa ifa=memberBaseService.findIfaMember(info.getCreator().getId());
					infoVo.setIfa(ifa);
					//ifa.getMember().setIconUrl(getUserHeadUrl(ifa.getMember().getIconUrl(), ifa.getGender()));
					infoVo.setIfaMember(ifa.getMember());
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				//获取统计数据
				if (criteria.isCheckCount()){
					PortfolioArenaCount count = findPortfolioArenaCountById(info.getId());
					if (null!=count && null!=count.getViews()) infoVo.setClick(count.getViews());
				}
				if (criteria.isCheckProduct()){
					List<ProductInfo> prods = findProductInfoList(info.getId(), CommonConstantsWeb.WEB_PRODUCT_TYPE_FUND);
					if (null==prods || prods.isEmpty()) infoVo.setProductCount(0);
					else infoVo.setProductCount(prods.size());
				}
				if(criteria.isCheckIsMyFollow()){
					String isFollow = followStatusService.getWebFollowStatus(info.getId(), criteria.getUserId(), "portfolio_arena");
					infoVo.setIsFollow(isFollow);
				}
				String goe=info.getGeoAllocation();
				infoVo.setGeoAllocation(sysParamService.findNameByCode(goe, langCode));
				String sector=info.getSector();
				infoVo.setSector(sysParamService.findNameByCode(sector, langCode));
				infoVo.setMonReturn3(findPortfolioReturn(infoVo.getId(), "return_period_code_3M")*100);
				MemberBase member=memberBaseService.findById(criteria.getUserId());
				String displayColor=member.getDefDisplayColor();
				if(null==displayColor || "".equals(displayColor))
					displayColor=CommonConstants.DEF_DISPLAY_COLOR;
				infoVo.setChartUrl(getPerformanceChartImage(info.getId(), CommonConstantsWeb.CHART_IMAGE_SIZE_MIDDL, displayColor));
				String creatorName=getCommonMemberName(info.getCreator().getId(), langCode, "2");
				infoVo.setCreatorName(creatorName);
				//infoVo.getCreator().setIconUrl(PageHelper.getUserHeadUrl(infoVo.getCreator().getIconUrl(),infoVo.getIfa().getGender()));
				result.add(infoVo);
			}
		}
		if(null!=countList && !countList.isEmpty()){
			 Object object=countList.get(0);
			 if(null!=object && !"".equals(object.toString()))
				 jsonPaging.setTotal(Integer.valueOf(object.toString()));
			 else {
				 jsonPaging.setTotal(0);
			}
			}else {
				jsonPaging.setTotal(0);
			}
		jsonPaging.setList(result);		
		return jsonPaging;
	}
	
	
	
	/**
	 * 获取投资组合的收益
	 * @author mqzou 2016-12-13
	 * @param portfolioId
	 * @param periodCode
	 * @return
	 */
	private double findPortfolioReturn(String portfolioId,String periodCode){
		String hql=" from PortfolioArenaReturn r where r.portfolio.id=? and r.periodCode=? ";
		List<Object> params=new ArrayList<Object>();
		params.add(portfolioId);
		params.add(periodCode);
		List resultList=this.baseDao.find(hql, params.toArray(), false);
		if(null!=resultList && !resultList.isEmpty()){
			PortfolioArenaReturn ret=(PortfolioArenaReturn)resultList.get(0);
			return ret.getIncrease();
		}else {
			return 0;
		}
	}

	/**
	 * 获取投资组合关联的基金产品
	 * @author michael
	 * @param portfolioId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FundInfo> findFundListByPortfolio(String portfolioId) {
		List<String> params = new ArrayList<String>();
		String hql = " from FundInfo t where t.product.id in (select i.id from PortfolioArenaProduct s,ProductInfo i where s.product.id=i.id and i.type='fund' and s.portfolio.id=?) ";
		params.add(portfolioId);
		return (List<FundInfo>)this.baseDao.find(hql, params.toArray(), false);
	}
	
	/**
	 * 通过ID获取查看权限设置
	 * @author michael
	 * @param id
	 * @return
	 */
	public WebView findWebViewById(String id) {
		WebView obj = (WebView) this.baseDao.get(WebView.class, id);
		return obj;
	}
	
	/**
	 * 获取投资组合关联的查看权限设置
	 * @author michael
	 * @param portfolioId
	 * @return
	 */
	public PortfolioWebViewVO findWebViewByPortfolio(String portfolioId) {
		List params = new ArrayList();
		String hql = "from WebView t where t.relateId =? and t.moduleType='"+CommonConstantsWeb.WEB_VIEW_MODULE_PORTFOLIO_ARENA+"' ";
		params.add(portfolioId);
		
		List list = this.baseDao.find(hql, params.toArray(), false);
		if (!list.isEmpty()){
			WebView view = (WebView)list.get(0);
			List<WebViewDetail> details = findWebViewDetailList(view.getId(), null);
			PortfolioWebViewVO vo = new PortfolioWebViewVO();
			BeanUtils.copyProperties(view,vo);//拷贝信息
			vo.setDetails(details);
			
			if (null==vo.getBuddyFlag()) vo.setBuddyFlag("0");//无
			vo.setBuddies("1".equals(vo.getBuddyFlag())?"ALL":"");
			
			if (null==vo.getClientFlag()) vo.setClientFlag("0");//无
			vo.setClients("1".equals(vo.getClientFlag())?"ALL":"");
			
			if (null==vo.getProspectFlag()) vo.setProspectFlag("0");//无
			vo.setProspects("1".equals(vo.getProspectFlag())?"ALL":"");
			
			if (null==vo.getColleagueFlag()) vo.setColleagueFlag("0");//无
			vo.setColleagues("1".equals(vo.getColleagueFlag())?"ALL":"");
			
			if (!details.isEmpty()){
				for (int i=0;i<details.size();i++){
					WebViewDetail x = details.get(i);
					if (CommonConstantsWeb.WEB_VIEW_DETAIL_TYPE_CLIENT.equals(x.getType())){
						vo.setClients(vo.getClients()+","+x.getToMember().getId());
					}
					if (CommonConstantsWeb.WEB_VIEW_DETAIL_TYPE_BUDDY.equals(x.getType())){
						vo.setBuddies(vo.getBuddies()+","+x.getToMember().getId());
					}
					if (CommonConstantsWeb.WEB_VIEW_DETAIL_TYPE_PROSPECT.equals(x.getType())){
						vo.setProspects(vo.getProspects()+","+x.getToMember().getId());
					}
					if (CommonConstantsWeb.WEB_VIEW_DETAIL_TYPE_TEAM.equals(x.getType())){
						vo.setColleagues(vo.getColleagues()+","+x.getToMember().getId());
					}
				}
			}
			return vo;
		}
		return null;
	}
	
	/**
	 * 通过ID获取推送权限设置
	 * @author michael
	 * @param id
	 * @return
	 */
	public WebPush findWebPushById(String id) {
		WebPush obj = (WebPush) this.baseDao.get(WebPush.class, id);
		return obj;
	}
	
	/**
	 * 获取投资组合关联的推送权限设置
	 * @author michael
	 * @param portfolioId
	 * @return
	 */
	public PortfolioWebPushVO findWebPushByPortfolio(String portfolioId) {
		List<String> params = new ArrayList<String>();
		String hql = " from WebPush t where t.relateId =? and t.moduleType='"+CommonConstantsWeb.WEB_PUSH_MODULE_PORTFOLIO_ARENA+"' ";
		params.add(portfolioId);
		
		List list = this.baseDao.find(hql, params.toArray(), false);
		if (!list.isEmpty()){
			WebPush push = (WebPush)list.get(0);
			List<WebPushDetail> details = findWebPushDetailList(push.getId(), null);
			PortfolioWebPushVO vo = new PortfolioWebPushVO();
			BeanUtils.copyProperties(push, vo);//拷贝信息
			vo.setDetails(details);
			
			if (null==vo.getBuddyFlag()) vo.setBuddyFlag("0");//无
			vo.setBuddies("1".equals(vo.getBuddyFlag())?"ALL":"");
			
			if (null==vo.getClientFlag()) vo.setClientFlag("0");//无
			vo.setClients("1".equals(vo.getClientFlag())?"ALL":"");
			
			if (null==vo.getProspectFlag()) vo.setProspectFlag("0");//无
			vo.setProspects("1".equals(vo.getProspectFlag())?"ALL":"");
			
			if (null==vo.getColleagueFlag()) vo.setColleagueFlag("0");//无
			vo.setColleagues("1".equals(vo.getColleagueFlag())?"ALL":"");
			
			if (!details.isEmpty()){
				for (int i=0;i<details.size();i++){
					WebPushDetail x = details.get(i);
					if (CommonConstantsWeb.WEB_VIEW_DETAIL_TYPE_CLIENT.equals(x.getType())){
						vo.setClients(vo.getClients()+","+x.getToMember().getId());
					}
					if (CommonConstantsWeb.WEB_VIEW_DETAIL_TYPE_BUDDY.equals(x.getType())){
						vo.setBuddies(vo.getBuddies()+","+x.getToMember().getId());
					}
					if (CommonConstantsWeb.WEB_VIEW_DETAIL_TYPE_PROSPECT.equals(x.getType())){
						vo.setProspects(vo.getProspects()+","+x.getToMember().getId());
					}
					if (CommonConstantsWeb.WEB_VIEW_DETAIL_TYPE_TEAM.equals(x.getType())){
						vo.setColleagues(vo.getColleagues()+","+x.getToMember().getId());
					}
				}
			}
			return vo;
		}
		return null;
	}
	
	/**
	 * 获取查看权限明细
	 * @author michael
	 * @param viewId
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WebViewDetail> findWebViewDetailList(String viewId, String type) {
		List<String> params = new ArrayList<String>();
		String hql = " from WebViewDetail t where t.view.id=?  ";
		params.add(viewId);
		
		if (null!=type && !"".equals(type)){
			hql += " and t.type=? ";
			params.add(type);
		}
		return (List<WebViewDetail>)this.baseDao.find(hql, params.toArray(), false);
	}
	
	/**
	 * 获取推送权限明细
	 * @author michael
	 * @param pushId
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WebPushDetail> findWebPushDetailList(String pushId, String type) {
		List<String> params = new ArrayList<String>();
		String hql = " from WebPushDetail t where t.push.id=?  ";
		params.add(pushId);
		
		if (null!=type && !"".equals(type)){
			hql += " and t.type=? ";
			params.add(type);
		}
		return (List<WebPushDetail>)this.baseDao.find(hql, params.toArray(), false);
	}
	
	/**
	 * 通过ID获取内容信息
	 */
	@Override
	public PortfolioArena findById(String id) {
		// TODO Auto-generated method stub
		PortfolioArena obj = (PortfolioArena) this.baseDao.get(PortfolioArena.class, id);
		PortfolioArenaCount count=(PortfolioArenaCount)this.baseDao.get(PortfolioArenaCount.class, id);
		if(null!=count)
			obj.setClick(count.getViews());
		return obj;
	}
	
	/**
	 * 删除投资组合
	 * @author michael
	 */
	public void delete(PortfolioArena info) {
		this.baseDao.delete(info);
	}
	
	/**
	 * 重置用户所有已置顶的记录
	 * @author michael
	 * @param userId 
	 */
	public void clearOverhead(String userId){
		String hql = " update PortfolioArena i set overhead='0', overheadTime = NOW() where i.creator.id=?";
		List<String> params = new ArrayList<String>();
		params.add(userId);
		this.baseDao.updateHql(hql, params.toArray());
	}
	
	/**
	 * 保存投资组合
	 * @author michael
	 * @param info
	 * @return
	 */
	public PortfolioArena saveOrUpdate(PortfolioArena info){
		return (PortfolioArena)this.baseDao.saveOrUpdate(info);
	}


	
	/**
	 * 重置查看权限明细记录
	 * @author michael
	 * @param viewId 
	 * @param type
	 */
	public void clearWebViewDetail(String viewId, String type){
		String hql = " delete from WebViewDetail w where w.view.id=? and w.type=? ";
		List<String> params = new ArrayList<String>();
		params.add(viewId);
		params.add(type);
		this.baseDao.updateHql(hql, params.toArray());
	}

	/**
	 * 重置推送权限明细记录
	 * @author michael
	 * @param pushId 
	 * @param type
	 */
	public void clearWebPushDetail(String pushId, String type){
		String hql = " delete from WebPushDetail w where w.push.id=? and w.type=? ";
		List<String> params = new ArrayList<String>();
		params.add(pushId);
		params.add(type);
		this.baseDao.updateHql(hql, params.toArray());
	}
	@Override
	public double findPortfolioInfoProductRate(String portfolioId, String productId) {
		String sql=" select * from portfolio_info_product r where r.portfolio_id=? and r.product_id=?";
		List<String> params = new ArrayList<String>();
		params.add(portfolioId);
		params.add(productId);
		List list=this.springJdbcQueryManager.springJdbcQueryForList(sql, params.toArray());
		if(null!=list && !list.isEmpty()){
			HashMap map=(HashMap)list.get(0);
			Object rate=map.get("allocation_rate");
			if (null!=rate && !"".equals(rate)) {
				return Double.parseDouble(rate.toString());
			}
			
		}
		return 0;
	}
	/**
	 * 获取投资组合产品比重列表（用于图表显示）
	 * @author mqzou 
	 * @date 2016-09-13
	 * @param type
	 * @param portfolioId
	 * @return
	 */
	@Override
	public List findPortfolioProductRate(String type, String portfolioId,String langCode) {
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT "+type+" name,SUM(p.`allocation_rate`) value FROM `portfolio_arena_product` p ");
		sql.append(" LEFT JOIN fund_info i on p.product_id=i.product_id ");
		sql.append(" LEFT JOIN `fund_info_"+langCode+"` f ON i.id=f.`id` WHERE p.`portfolio_id`='"+portfolioId+"'");
		sql.append(" GROUP BY "+type);
		List list=this.springJdbcQueryManager.springJdbcQueryForList(sql.toString());
		return list;
	}
	/**
	 * 查找策略的产品
	 * @param strategyId
	 * @param type 产品类型:fund基金,stock股票,futures期货
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProductInfo> findProductInfoList(String strategyId, String type) {
		List<Object> params = new ArrayList<Object>();
		String hql = " from ProductInfo t where t.type=? and t.id in (select s.product.id from PortfolioArenaProduct s where s.portfolio.id=?) ";
		params.add(type);
		params.add(strategyId);
		return (List<ProductInfo>)this.baseDao.find(hql, params.toArray(), false);
	}	
	
	/**
	 * 通过ID获取内容信息
	 * @author michael
	 */
	public PortfolioArenaCount findPortfolioArenaCountById(String id) {
		PortfolioArenaCount obj = (PortfolioArenaCount) this.baseDao.get(PortfolioArenaCount.class, id);
		return obj;
	}

	/**
	 * 创建投资组合--获取我的投资策略
	 * @author wwluo
	 * @data 2016-09-20
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@Override
	public List<StrategyInfo> getMyStrategy(String memberId) {
		List<StrategyInfo> strategyInfos = new ArrayList<StrategyInfo>();
		if (StringUtils.isNotBlank(memberId)) {
			StringBuffer hql = new StringBuffer(" from StrategyInfo where isValid=1 and creator_id=?");
			List params = new ArrayList();
			params.add(memberId);
			strategyInfos = this.baseDao.find(hql.toString(), params.toArray(), false);
		}
		return strategyInfos;
	}

	/**
	 * 根据id获取StrategyInfo实体
	 * @author wwluo
	 * @data 2016-09-20
	 * @return
	 */
	@Override
	public StrategyInfo getStrategyInfoById(String strategyId) {
		StrategyInfo strategyInfo = new StrategyInfo();
		if (StringUtils.isNotBlank(strategyId)) {
			strategyInfo = (StrategyInfo) this.baseDao.get(StrategyInfo.class, strategyId);
		}
		return strategyInfo;
	}

	/**
	 * 根据id获取PortfolioArena实体
	 * @author wwluo
	 * @data 2016-09-20
	 * @return
	 */
	@Override
	public PortfolioArena getPortfolioArenaById(String portfolioId) {
		PortfolioArena portfolioArena = new PortfolioArena();
		if (StringUtils.isNotBlank(portfolioId)) {
			portfolioArena = (PortfolioArena) this.baseDao.get(PortfolioArena.class, portfolioId);
		}
		return portfolioArena;
	}

	/**
	 * 保存组合与产品信息
	 * @author wwluo
	 * @data 2016-09-20
	 * @return
	 */
	@Override
	public PortfolioArenaProduct saveOrUpdate(PortfolioArenaProduct product) {
		return (PortfolioArenaProduct)this.baseDao.saveOrUpdate(product);
	}

	/**
	 * 保存定投信息
	 * @author wwluo
	 * @data 2016-09-29
	 * @return
	 */
	@Override
	public PortfolioArenaAip saveOrUpdate(PortfolioArenaAip arenaAip) {
		return (PortfolioArenaAip)this.baseDao.saveOrUpdate(arenaAip);
	}

	/**
	 * 获取策略分配比例
	 * @author wwluo
	 * @data 2016-10-25
	 * @param id 策略id
	 * @return 
	 */
	@Override
	public List<StrategyAllocation> getStrategyAllocation(String id) {
		List<StrategyAllocation> strategyAllocations = new ArrayList<StrategyAllocation>();
		if(StringUtils.isNotBlank(id)){
			StringBuffer hql = new StringBuffer(" from StrategyAllocation where layerLevel=1 and strategy_id=?");
			List params = new ArrayList();
			params.add(id);
			strategyAllocations = this.baseDao.find(hql.toString(), params.toArray(), false);
		}
		return strategyAllocations;
	}

	/**
	 * 投资策略产品
	 * @author wwluo
	 * @data 2016-10-25
	 * @param strategyId 策略Id
	 * @return 
	 */
	@Override
	public List<StrategyProduct> getStrategyProducts(String strategyId) {
		List<StrategyProduct> strategyProducts = new ArrayList<StrategyProduct>();
		if(StringUtils.isNotBlank(strategyId)){
			StringBuffer hql = new StringBuffer(" from StrategyProduct where strategy_id=?");
			List params = new ArrayList();
			params.add(strategyId);
			strategyProducts = this.baseDao.find(hql.toString(), params.toArray(), false);
		}
		return strategyProducts;
	}

	/**
	 * 获取投资组合的每日盈亏
	 * @author mqzou
	 * @data 2016-10-27
	 * @param portfolioId
	 * @return
	 */
	@Override
	public List<PortfolioArenaCumperf> findPortfolioArenaCumperfById(String portfolioId) {
		String hql=" from PortfolioArenaCumperf r where r.portfolio.id=? order by r.valuationDate asc";
		List params=new ArrayList();
		params.add(portfolioId);
		List resultList=this.baseDao.find(hql, params.toArray(), false);
		List<PortfolioArenaCumperf> list=new ArrayList<PortfolioArenaCumperf>();
		if(null!=resultList){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				PortfolioArenaCumperf object = (PortfolioArenaCumperf) it.next();
				list.add(object);
			}
		}
		return list;
	}
	/**
	 * 获取投资组合的盈亏
	 * @author mqzou
	 * @data 2016-11-15
	 * @param portfolioId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Override
	public List<PortfolioArenaCumperf> findPortfolioArenaCumperf(String portfolioId, String startDate, String endDate) {
		String hql=" from PortfolioArenaCumperf r where r.portfolio.id=? ";
		List params=new ArrayList();
		params.add(portfolioId);
		if(null!=startDate && !"".equals(startDate)){
			hql+=" and r.valuationDate between ? and ?";
			params.add(DateUtil.getDate(startDate));
			params.add(DateUtil.getDate(endDate));
		}
		hql +=" order by r.valuationDate asc";
		List resultList=this.baseDao.find(hql, params.toArray(), false);
		List<PortfolioArenaCumperf> list=new ArrayList<PortfolioArenaCumperf>();
		if(null!=resultList){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				PortfolioArenaCumperf object = (PortfolioArenaCumperf) it.next();
				list.add(object);
			}
		}
		return list;
	}

	/**
	 * 获取投资组合最新的一条盈亏记录
	 * @author mqzou
	 * @data 2016-10-27
	 * @param portfolioId
	 * @return
	 */
	@Override
	public PortfolioArenaCumperf findLastPortfolioArenaCumperf(String portfolioId) {
		PortfolioArenaCumperf vo=new PortfolioArenaCumperf();
		String hql="  from PortfolioArenaCumperf r where r.portfolio.id=?  order by r.valuationDate desc";
		List params=new ArrayList();
		params.add(portfolioId);
		List resultList=this.baseDao.find(hql, params.toArray(), false);
		//List<PortfolioArenaCumperf> list=new ArrayList<PortfolioArenaCumperf>();
		if(null!=resultList && !resultList.isEmpty()){
			vo=(PortfolioArenaCumperf)resultList.get(0);
		}
		return vo;
	}

	/**
	 * 根据id获取组合定投信息
	 * @author wwluo
	 * @data 2016-10-25
	 * @param id  投资组合Id
 	 * @return 
	 */
	@Override
	public PortfolioArenaAip getAipByPortfolioId(String id) {
		PortfolioArenaAip arenaAip = null;
		if (StringUtils.isNotBlank(id)) {
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" PortfolioArenaAip a" +
					" WHERE" +
					" a.portfolio.id=?" +
					" ORDER BY" +
					" a.lastUpdate" +
					" DESC");
			List params = new ArrayList();
			params.add(id);
			List<PortfolioArenaAip> list = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(list!=null && !list.isEmpty()){
				arenaAip = list.get(0);
			}
		}
		return arenaAip;
	}

	/**
	 * 获取我的投资组合（arena）
	 * @author wwluo
	 * @data 2016-11-18
	 * @param keyWord 组合名称关键字
	 * @param isPublic 是否公开 
	 * @param status 是否草稿 
 	 * @return 
	 */
	@Override
	public JsonPaging getMyArenas(JsonPaging jsonPaging, MemberBase loginUser,
			String keyWord,String isPublic,String status) {
		if(loginUser != null){
			StringBuffer hql = new StringBuffer("" +
					" from" +
					" PortfolioArena a" +
					" where" +
					" a.isValid=1 and" +
					" a.creator=?" +
					"");
			List<Object> params = new ArrayList<Object>();
			params.add(loginUser);
			if (StringUtils.isNotBlank(keyWord)) {
				hql.append(" and a.portfolioName like ?");
				params.add("%"+keyWord+"%");
			}
			if (StringUtils.isNotBlank(isPublic)) {
				hql.append(" and a.isPublic=?");
				params.add(isPublic);
			}
			if (StringUtils.isNotBlank(status)) {
				hql.append(" and a.status=?");
				params.add(status);
			}
			hql.append(" order by a.overhead desc,a.overheadTime desc");
			jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		}
		return jsonPaging;
	}

	/**
	 * 删除投资组合（物理删除）
	 * @author wwluo
	 * @data 2016-11-18
	 * @param info PortfolioArena 投资组合实体
 	 * @return 
	 */
	@Override
	public void delPortfolio(PortfolioArena info) {
		info.setIsValid("0");
		this.baseDao.update(info);
	}
	
	/**
	 * 获取ifa推荐的投资组合列表
	 * @author mqzou
	 * @data 2016-11-30
	 * @param jsonPaging
	 * @param ifaMemberId
	 * @param keyWord
	 * @param langCode
	 * @param loginUser
	 * @return
	 */
	@Override
	public JsonPaging findByIfaRecommend(JsonPaging jsonPaging, String ifaMemberId, String keyWord, String langCode, MemberBase loginUser) {
		StringBuilder hql=new StringBuilder();
		List<Object> params=new ArrayList<Object>();
		hql.append("  from PortfolioArena p   where 1=1");
		hql.append(" and p.creator.id=? and p!=null and p.isValid='1' and p.status='1'");
		params.add(ifaMemberId);
		if(null!=keyWord && !"".equals(keyWord)){
			hql.append(" and p.portfolioName like ?");
			params.add("%"+keyWord+"%");
		}
		jsonPaging.setOrder("desc");
		jsonPaging.setSort("p.lastUpdate");
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List<PortfolioArenaVO> result = new ArrayList<PortfolioArenaVO>();
		List<PortfolioArena> list = (List<PortfolioArena>)jsonPaging.getList();
		if (null!=list && !list.isEmpty()){
			for (PortfolioArena info: list){
				if(null==info)
					continue;
				PortfolioArenaVO infoVo = new PortfolioArenaVO(info);
				//设置ifa信息
				try {
					infoVo.setIfa(memberBaseService.findIfaMember(info.getCreator().getId()));
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				String isFollow = followStatusService.getWebFollowStatus(info.getId(), loginUser.getId(), "portfolio_arena");
				infoVo.setIsFollow(isFollow);
				PortfolioArenaCount count = findPortfolioArenaCountById(info.getId());
				if (null!=count && null!=count.getViews()) infoVo.setClick(count.getViews());
				String goe=info.getGeoAllocation();
				infoVo.setGeoAllocation(sysParamService.findNameByCode(goe, langCode));
				String sector=info.getSector();
				infoVo.setSector(sysParamService.findNameByCode(sector, langCode));
				MemberBase member=memberBaseService.findById(ifaMemberId);
				if(null!=ifaMemberId){
					String displayColor=member.getDefDisplayColor();
					if(null==displayColor || "".equals(displayColor))
						displayColor=CommonConstants.DEF_DISPLAY_COLOR;
					infoVo.setChartUrl(getPerformanceChartImage(info.getId(), CommonConstantsWeb.CHART_IMAGE_SIZE_MIDDL, displayColor));
				}
				result.add(infoVo);
			}
		}
		jsonPaging.setList(result);		
		return jsonPaging;
	}
	
	/**
	 * 获取Memberbase信息
	 * @author wwluo
	 * @data 2016-09-20
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@Override
	public List<MemberBase> getMemberBaseByIds(String ids) {
		List<MemberBase> bases = null;
		if (StringUtils.isNotBlank(ids)) {
			String[] arrayResult = null;
			if(ids.indexOf(",") > -1){
				ids = StrUtils.reHeavy(ids);
				arrayResult = ids.split(",");
			}else{
				arrayResult = new String[]{ids};
			}
	        for (String id : arrayResult) {
	        	MemberBase base = (MemberBase) this.baseDao.get(MemberBase.class, id);
	        	if(base!=null){
	        		bases.add(base);
	        	}
			}
		}
		return bases;
	}
	
	/**
	 * 删除arena原产品
	 * @author wwluo
	 * @data 2016-09-20
	 * @return
	 */
	@Override
	public void delArenaProduct(String portfolioId) {
		if (StringUtils.isNotBlank(portfolioId)) {
			StringBuffer hql = new StringBuffer("" +
					" DELETE FROM" +
					" PortfolioArenaProduct p" +
					" WHERE" +
					" p.portfolio.id=?");
			List<Object> params = new ArrayList<Object>();
			params.add(portfolioId);
			this.baseDao.updateHql(hql.toString(), params.toArray());
		}
	}

	/**
	 * 获取产品权重
	 * @author wwluo
	 * @data 2016-09-20
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FundInfoDataVO> getProductWeight(String portfolioId,
			List<FundInfoDataVO> fundInfoList) {
		if (StringUtils.isNotBlank(portfolioId) && fundInfoList != null) {
			for (FundInfoDataVO fundInfoDataVO : fundInfoList) {
				StringBuffer hql = new StringBuffer("" +
						" SELECT" +
						" s" +
						" FROM" +
						" PortfolioArenaProduct s,ProductInfo i" +
						" WHERE" +
						" s.product.id=?" +
						" AND" +
						" i.type='fund'" +
						" AND" +
						" s.portfolio.id=?" +
						"");
				List<String> params = new ArrayList<String>();
				params.add(fundInfoDataVO.getProductId());
				params.add(portfolioId);
				List<PortfolioArenaProduct> arenaProducts = this.baseDao.find(hql.toString(), params.toArray(), false);
				if(arenaProducts != null && !arenaProducts.isEmpty()){
					PortfolioArenaProduct arenaProduct = arenaProducts.get(0);
					fundInfoDataVO.setProductWeight(arenaProduct.getAllocationRate()*100);
				}
			}
		}
		return fundInfoList;
	}
	
	/**
	 * 获取组合的基金列表（用户组合详情显示）
	 * @author mqzou 2014-04-17
	 * @param id
	 * @return
	 */
	@Override
	public List<PortfolioFundListVO> findPortfolioFundList(String id) {
		List<String> params = new ArrayList<String>();
		StringBuilder hql=new StringBuilder();
		hql.append(" from PortfolioArenaProduct p left join ProductInfo i on p.product.id=i.id ");
		hql.append(" left join FundInfo f on f.product.id=i.id ");
		hql.append(" where i.type='fund' and p.portfolio.id=? and f is not null");
		//String hql = " from FundInfo t where t.product.id in (select i.id from PortfolioArenaProduct s,ProductInfo i where s.product.id=i.id and i.type='fund' and s.portfolio.id=?) ";
		params.add(id);
		List resultList=baseDao.find(hql.toString(), params.toArray(), false);
		List<PortfolioFundListVO> list=new ArrayList<PortfolioFundListVO>();
		if(null!=resultList && !resultList.isEmpty()){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				Object[] objects = (Object[]) it.next();
				PortfolioArenaProduct product=(PortfolioArenaProduct)objects[0];
				FundInfo fundInfo=(FundInfo)objects[2];
				if(null==fundInfo)
					continue;
				PortfolioFundListVO vo=new PortfolioFundListVO();
				vo.setFundId(fundInfo.getId());
				vo.setWeight(product.getAllocationRate());
				list.add(vo);
			}
		}
		return list;
	}

	@Override
	public String[] getProductWeight(String id) {
		String[] result = null;
		StringBuffer hql = new StringBuffer("SELECT i.id,p.allocationRate FROM PortfolioArenaProduct p"
				+ " LEFT JOIN"
				+ " FundInfo i"
				+ " ON"
				+ " i.product.id=p.product.id"
				+ " WHERE p.portfolio.id=?");
		List<Object> params = new ArrayList<Object>();
		params.add(id);
		List<Object[]> arenaProducts = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(arenaProducts != null && !arenaProducts.isEmpty()){
			result = new String[2];
			String fundIds = "";
			String allocationRates = "";
			for (Object[] objs : arenaProducts) {
				String fundId = (String) objs[0];
				Double allocationRate = (Double) objs[1];
				if(allocationRate != null){
					allocationRate = allocationRate*100;
				}
				fundIds += fundId + ",";
				allocationRates += allocationRate.toString() + ",";
			}
			result[0] = fundIds;
			result[1] = allocationRates;
		}
		return result;
	}
}
