package com.fsll.wmes.strategy.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.StrategyAllocation;
import com.fsll.wmes.entity.StrategyCount;
import com.fsll.wmes.entity.StrategyInfo;
import com.fsll.wmes.entity.StrategyProduct;
import com.fsll.wmes.entity.WebInvestorVisit;
import com.fsll.wmes.entity.WebPush;
import com.fsll.wmes.entity.WebPushDetail;
import com.fsll.wmes.entity.WebReadToDo;
import com.fsll.wmes.entity.WebView;
import com.fsll.wmes.entity.WebViewDetail;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.ifa.service.IfaInfoService;
import com.fsll.wmes.ifa.vo.IfaClientVO;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.strategy.service.StrategyInfoService;
import com.fsll.wmes.strategy.vo.CharDataVO;
import com.fsll.wmes.strategy.vo.CriteriaVO;
import com.fsll.wmes.strategy.vo.StrategyAllocMethodVO;
import com.fsll.wmes.strategy.vo.StrategyAllocationTypeVO;
import com.fsll.wmes.strategy.vo.StrategyAllocationVO;
import com.fsll.wmes.strategy.vo.StrategyForSelectVO;
import com.fsll.wmes.strategy.vo.StrategyInfoWebVO;
import com.fsll.wmes.strategy.vo.StrategyWebPushVO;
import com.fsll.wmes.strategy.vo.StrategyWebViewVO;
import com.fsll.wmes.strategy.vo.WebInvestorVisitVO;
import com.fsll.wmes.web.service.WebFollowStatusService;
import com.fsll.wmes.web.service.WebFriendService;
import com.fsll.wmes.web.service.WebReadToDoService;

@Service("strategyInfoService")
//@Transactional
public class StrategyInfoServiceImpl extends BaseService implements
		StrategyInfoService {

	@Autowired
	private MemberBaseService memberBaseService;
	
	@Autowired
    private FundInfoService fundInfoService;
	
	@Autowired
	private WebFollowStatusService followStatusService; 
	
	@Autowired
	private SysParamService sysParamService;
	@Autowired
	private WebReadToDoService webReadToDoService;
	
	/*@Autowired
	private IfaInfoService ifaInfoService;
	
	@Autowired
	private WebFriendService webFriendService;*/
	@Autowired
	private WebFollowStatusService webFollowService;
	/**
	 * 获取内容列表
	 */
	@Override
	public JsonPaging findAll(JsonPaging jsonPaging, String keyword) {
		// TODO Auto-generated method stub
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder(" from StrategyInfo l where 1=1 ");
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
		String countHql="select Count(distinct l.id) from StrategyInfo l";
		StringBuilder hql1=new StringBuilder();
		StringBuilder hql=new StringBuilder();
		
		hql1.append(" select distinct l from StrategyInfo l ");
		hql.append(" left join  WebView v on l.id=v.relateId");
		hql.append(" left join WebViewDetail d on v.id=d.view.id");
		hql.append(" left join MemberIfa i on l.creator.id=i.member.id");
		hql.append(" left join CrmCustomer c on i.id=c.ifa.id");
		hql.append(" left join WebFriend f on l.creator.id=f.fromMember.id ");
		hql.append(" left join IfafirmTeamIfa t on i.id=t.ifa.id");
		hql.append(" where 1=1 and l.creator.id is not null and l.isValid='1' and l.status='1'");
		
		if(null != criteria.getUserId() && !"".equals(criteria.getUserId())
			&& ("mylist".equals(StrUtils.getString(criteria.getModule()))
					||"Me".equals(StrUtils.getString(criteria.getSource())))){
			hql.append(" and creator.id=?");
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
		
		/*hql.append(" and case l.isPublic when '0' then creator.id=? end   ");
		params.add(criteria.getUserId());*/
		
		if(null != criteria.getKeyword() && !"".equals(criteria.getKeyword())){
			hql.append(" and ( l.strategyName like ? or l.investmentGoal like ? or l.suitability like ? )");
			params.add("%"+criteria.getKeyword()+"%");
			params.add("%"+criteria.getKeyword()+"%");
			params.add("%"+criteria.getKeyword()+"%");
		}
		if(null != criteria.getSector() && !"".equals(criteria.getSector())){
			String[] sectors=criteria.getSector().split(",");
			hql.append(" and ( 1=2 ");
			for (String string : sectors) {
				if("".equals(string))
					continue;
			     hql.append(" or l.sector like ?");
			     params.add("%"+string+"%");
			}
			hql.append("  )");
			
		}
		
		if(null != criteria.getGeoAllocation() && !"".equals(criteria.getGeoAllocation())){//可多选，用逗号分隔
//			hql.append(" and ( l.geoAllocation like ? )");
//			params.add("%"+region+"%");
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
		
		if(null != criteria.getRiskLevel() && !"".equals(criteria.getRiskLevel())){
//			if ("L".equals(criteria.getRiskLevel())){
//				hql.append(" and ( l.riskLevel < 3 )");
//			}else if ("M".equals(criteria.getRiskLevel())){
//				hql.append(" and ( l.riskLevel >= 3 and l.riskLevel <= 5 )");
//			}else if ("H".equals(criteria.getRiskLevel())){
//				hql.append(" and ( l.riskLevel >5 )");
//			}
			hql.append(" and l.riskLevel=? ");
			params.add(criteria.getRiskLevel());
		}
		
//		if(null != criteria.getSource() && !"".equals(criteria.getSource())){
//			if ("IFA".equals(criteria.getSource())){
//				hql.append(" and ( l.riskLevel < 3 )");
//			}else if ("Distributor".equals(criteria.getSource())){
//				hql.append(" and ( l.riskLevel >= 3 and l.riskLevel <= 5 )");
//			}else if ("System".equals(criteria.getSource())){
//				hql.append(" and ( l.riskLevel >5 )");
//			}
//		}
		hql1.append(hql);
		if(null!=criteria.getOrderBy() && !"".equals(criteria.getOrderBy())){
			hql1.append(" order by "+criteria.getOrderBy());
		}else{//默认排序
			hql1.append(" order by l.overhead desc,l.lastUpdate desc ");
		}
		
		
		jsonPaging = this.baseDao.selectJsonPaging(hql1.toString(), params.toArray(), jsonPaging, false);
		List countList=this.baseDao.find(countHql+hql.toString(), params.toArray(), false);
		
		
//		if (criteria.isCheckCount() || criteria.isCheckProduct()){
			List<StrategyInfoWebVO> result = new ArrayList<StrategyInfoWebVO>();
			List<StrategyInfo> list = (List<StrategyInfo>)jsonPaging.getList();
			if (null!=list && !list.isEmpty()){
				for (StrategyInfo info: list){
					StrategyInfoWebVO infoVo = new StrategyInfoWebVO(info);
					//设置ifa信息
					try {
						infoVo.setIfa(memberBaseService.findIfaMember(info.getCreator().getId()));
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					//获取统计数据
					if (criteria.isCheckCount()){
						StrategyCount count = findStrategyCountById(info.getId());
						if (null!=count && null!=count.getViews()) info.setClick(count.getViews());
					}
					if (criteria.isCheckProduct()){
						List<ProductInfo> prods = findProductInfoList(info.getId(), CommonConstantsWeb.WEB_PRODUCT_TYPE_FUND);
						if (null==prods || prods.isEmpty()) info.setProductCount(0);
						else info.setProductCount(prods.size());
					}
					if(criteria.isCheckIsMyFollow()){
						String isFollow = followStatusService.getWebFollowStatus(info.getId(), criteria.getUserId(), "strategy");
						infoVo.setIsFollow(isFollow);
					}
					String goe=getStrategyAllocationItems(info.getId(), CommonConstantsWeb.WEB_ALLOCATION_METHOD_GEOGRAPHICAL, 2);    //info.getGeoAllocation();
					infoVo.setGeoAllocation(sysParamService.findNameByCode(goe, langCode));
					String sector=getStrategyAllocationItems(info.getId(), CommonConstantsWeb.WEB_ALLOCATION_METHOD_SECTOR, 2);//info.getSector();
					infoVo.setSector(sysParamService.findNameByCode(sector, langCode));
					
					/*String risk=info.getRiskLevel();
					String riskName=sysParamService.findNameByValue("strategy_risk_rating", risk, langCode);*/
					infoVo.setRiskLevel(info.getRiskLevel());
					
					List<CharDataVO> allocation=new ArrayList<CharDataVO>();
					List<StrategyAllocationTypeVO> typeList=findStrategyAllocationType(info.getId());
					if(null!=typeList){
						Iterator iterator=typeList.iterator();
						while (iterator.hasNext()) {
							StrategyAllocationTypeVO object = (StrategyAllocationTypeVO) iterator.next();
							CharDataVO vo=new CharDataVO();
							vo.setName(object.getTypeName());
							vo.setValue(Double.valueOf(object.getWeight()) );
							vo.setDisplayColor(object.getDisplayColor());
							allocation.add(vo);
						}
					}
					//String str="[{name:'Stock 65%',value:65},{name:'Fund 35%',value:35}]";
					String allocationJson=JsonUtil.toJson(allocation);
					
					allocationJson.replace("\"", "'");
					infoVo.setAllocationData(allocationJson);
					//infoVo.getCreator().setIconUrl(PageHelper.getUserHeadUrl(infoVo.getCreator().getIconUrl(), infoVo.getIfa().getGender()));
					String creatorName=getCommonMemberName(info.getCreator().getId(), langCode, "2");
					infoVo.setCreatorName(creatorName);
					result.add(infoVo);
				}
			}
			jsonPaging.setList(result);
//		}
			
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
		return jsonPaging;
	}

	
	
	/**
	 * 获取投资策略关联的基金产品
	 * @author michael
	 * @param strategyId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FundInfo> findFundListByStrategy(String strategyId) {
		List<String> params = new ArrayList<String>();
		String hql = " from FundInfo t where t.product.id in (select i.id from StrategyProduct s,ProductInfo i where s.product.id=i.id and i.type='fund' and s.strategy.id=?) ";
		params.add(strategyId);
		
		return (List<FundInfo>)this.baseDao.find(hql, params.toArray(), false);
	}
	
	/**
	 *  获取投资策略关联的基金产品Id
	 *  @author mqzou 2016-11-08
	 * @param strategyId
	 * @return
	 */
	@Override
	public List<String> findFundIdByStrategy(String strategyId) {
		List<String> params = new ArrayList<String>();
		String hql = " select t.id from FundInfo t where t.product.id in (select i.id from StrategyProduct s,ProductInfo i where s.product.id=i.id and i.type='fund' and s.strategy.id=?) ";
		params.add(strategyId);
		List resultList=this.baseDao.find(hql, params.toArray(), false);
		List<String> list=new ArrayList<String>();
		if(null!=resultList){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				Object object = (Object) it.next();
				list.add(object.toString());
				
			}
		}
		return list;
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
	 * 获取投资策略关联的查看权限设置
	 * @author michael
	 * @param strategyId
	 * @return
	 */
	public StrategyWebViewVO findWebViewByStrategy(String strategyId) {
		List params = new ArrayList();
		String hql = "from WebView t where t.relateId =? and t.moduleType='"+CommonConstantsWeb.WEB_VIEW_MODULE_STRATEGY+"' ";
		params.add(strategyId);
		
		List list = this.baseDao.find(hql, params.toArray(), false);
		if (!list.isEmpty()){
			WebView view = (WebView)list.get(0);
			List<WebViewDetail> details = findWebViewDetailList(view.getId(), null);
			StrategyWebViewVO vo = new StrategyWebViewVO();
			BeanUtils.copyProperties(view,vo);//拷贝信息
			vo.setDetails(details);
			
			/*if (null==vo.getBuddyFlag()) vo.setBuddyFlag("0");//无
			vo.setBuddies("1".equals(vo.getBuddyFlag())?"ALL":"");
			
			if (null==vo.getClientFlag()) vo.setClientFlag("0");//无
			vo.setClients("1".equals(vo.getClientFlag())?"ALL":"");
			
			if (null==vo.getProspectFlag()) vo.setProspectFlag("0");//无
			vo.setProspects("1".equals(vo.getProspectFlag())?"ALL":"");
			
			if (null==vo.getColleagueFlag()) vo.setColleagueFlag("0");//无
			vo.setColleagues("1".equals(vo.getColleagueFlag())?"ALL":"");*/
			
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
			//List idList= Arrays.asList(vo.getClients());
			String ids=vo.getClients();// StrUtils.seperateWithQuote(vo.getClients(), "'");
			String names=getClientName(view.getFromMember().getId(), ids, "1");
			vo.setClientNames(names);
			
			
			ids=vo.getBuddies();// StrUtils.seperateWithQuote(vo.getBuddies(), "'");
			 names=getMemberName(ids);
			vo.setBuddyNames(names);
			
			//idList= Arrays.asList(vo.getClients());
			ids= vo.getProspects();//StrUtils.seperateWithQuote(vo.getClients(), "'");
			 names=getClientName(view.getFromMember().getId(), ids, "0");
			vo.setProspectNames(names);
			
			ids=vo.getColleagues();// StrUtils.seperateWithQuote(vo.getColleagues(), "'");
			 names=getMemberName(ids);
			vo.setColleagueNames(names);
			
			return vo;
		}
		return null;
	}
	
	/**
	 * 获取ifa客户的名字
	 * @author mqzou 2017-02-10
	 * @param ifaMemberId
	 * @param ids
	 * @param type
	 * @return
	 */
	private String getClientName(String ifaMemberId,String ids,String type){
		if(null==ids || "".equals(ids))
			return "";
		StringBuilder hql=new StringBuilder();
		hql.append("select nickName from CrmCustomer r where r.ifa.member.id=? and r.member.id in ( ");
		List<Object> params=new ArrayList<Object>();
		params.add(ifaMemberId);
		String[] idlist=StrUtils.splitAndTrim(ids, ",", "");
		String strWhere="";
		for (String object : idlist) {
			strWhere+="?,";
			params.add(object.toString());
		}
		if(strWhere.endsWith(","))
			strWhere=strWhere.substring(0,strWhere.length()-1);
		hql.append(strWhere+") and clientType=?");
		params.add(type);
		List list=baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			Iterator it=list.iterator();
			String names="";
			while (it.hasNext()) {
				Object object = (Object) it.next();
				if(null!=object)names+=object.toString()+",";
			}
			if(names.endsWith(","))
				names=names.substring(0,names.length()-1);
			return names;
		}
		return "";
	}
	
	/**
	 * 获取用户的名字
	 * @author mqzou 2017-02-10
	 * @param ids
	 * @return
	 */
	private String getMemberName(String ids){
		if(null==ids || "".equals(ids))
			return "";
		String[] idStrings=StrUtils.splitAndTrim(ids, ",", "");
		StringBuilder hql=new StringBuilder();
		hql.append(" from MemberBase r where 1=1 ");
		List<Object> params=new ArrayList<Object>();
		if(null!=idStrings && idStrings.length>0){
			
			String str="";
			for (String id : idStrings) {
				str+="?,";
				params.add(id);
			}
			if(str.endsWith(","))
				str=str.substring(0, str.length()-1);
			hql.append(" and  r.id in ("+str+")");
		}
		//params.add(ids);
		List list=baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			Iterator it=list.iterator();
			String names="";
			while (it.hasNext()) {
				MemberBase object = (MemberBase) it.next();
				if(null!=object )names+=object.getNickName().toString()+",";
			}
			if(names.endsWith(","))
				names=names.substring(0,names.length()-1);
			return names;
		}
		return "";
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
	 * 获取投资策略关联的推送权限设置
	 * @author michael
	 * @param strategyId
	 * @return
	 */
	public StrategyWebPushVO findWebPushByStrategy(String strategyId) {
		List<String> params = new ArrayList<String>();
		String hql = " from WebPush t where t.relateId =? and t.moduleType='"+CommonConstantsWeb.WEB_PUSH_MODULE_STRATEGY+"' ";
		params.add(strategyId);
		
		List list = this.baseDao.find(hql, params.toArray(), false);
		if (!list.isEmpty()){
			WebPush push = (WebPush)list.get(0);
			List<WebPushDetail> details = findWebPushDetailList(push.getId(), null);
			StrategyWebPushVO vo = new StrategyWebPushVO();
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
	public StrategyInfo findById(String id) {
		// TODO Auto-generated method stub
		StrategyInfo obj = (StrategyInfo) this.baseDao.get(StrategyInfo.class, id);
		StrategyCount count = findStrategyCountById(id);
		if (null!=count && null!=count.getViews()) obj.setClick(count.getViews());
		
		return obj;
	}
	
	/**
	 * 通过ID获取内容信息
	 * @author michael
	 * @param id
	 */
	public StrategyCount findStrategyCountById(String id) {
		// TODO Auto-generated method stub
		StrategyCount obj = (StrategyCount) this.baseDao.get(StrategyCount.class, id);
		return obj;
	}
	
	/**
	 * 删除投资策略
	 * @author michael
	 */
	public void delete(StrategyInfo info) {
		//删除关系表数据
		deleteStrategyProduct(info.getId());
		deleteStrategyAllocationByMethod(info.getId(), null);
		deleteStrategyCount(info.getId());
		this.baseDao.delete(info);
	}
	
	/**
	 * 重置用户所有已置顶的记录
	 * @author michael
	 * @param userId 
	 */
	public void clearOverhead(String userId){
		String hql = " update StrategyInfo i set overhead='0', overheadTime = NOW() where i.creator.id=?";
		List<String> params = new ArrayList<String>();
		params.add(userId);
		this.baseDao.updateHql(hql, params.toArray());
	}
	
	/**
	 * @author tejay zhu
	 */
	@Override
	//@Transactional(readOnly=true)
	public List<StrategyProduct> findStrategyProductByStrategyId(String id){
		String hql = " from StrategyProduct t where t.strategy.id = ?";
		List<String> params = new ArrayList<String>();
		params.add(id);
		List<StrategyProduct> list = this.baseDao.find(hql, params.toArray(), false);
		if( null != list && !list.isEmpty() ){
			return list;
		}else{
			return new ArrayList<StrategyProduct>();
		}
	}
	
	/**
	 * 查找策略产品
	 * @author michael
	 * @param strategyId 
	 * @param strategyId 
	 * @return 
	 */
	public StrategyProduct findStrategyProduct(String strategyId, String prodId){
		String hql = " from StrategyProduct t where t.strategy.id = ? and t.product.id=?";
		List<String> params = new ArrayList<String>();
		params.add(strategyId);
		params.add(prodId);
		List<StrategyProduct> list = this.baseDao.find(hql, params.toArray(), false);
		if( null != list && !list.isEmpty() ){
			return list.get(0);
		}
		return null;
	}
	
	public List<ProductInfo> findProductInfoList( List<StrategyProduct> list ) {
		String hql = " from ProductInfo t where 1=1 ";
		List<String> params = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			hql += " or t.id = '"+list.get(i).getProduct().getId()+"' ";
		}
		List<ProductInfo> productInfoList = this.baseDao.find(hql, params.toArray(), false);
		return productInfoList;
	}
	

	/**
	 * 保存投资策略
	 * @author michael
	 * @param info
	 * @return
	 */
	public StrategyInfo saveOrUpdate(StrategyInfo info){
		return (StrategyInfo)this.baseDao.saveOrUpdate(info);
	}

	/**
	 * 保存查看权限
	 * @author michael
	 * @param info
	 * @return
	 */
	public WebView saveOrUpdate(WebView info){
		return (WebView)this.baseDao.saveOrUpdate(info);
	}
	
	/**
	 * 保存查看权限明细
	 * @author michael
	 * @param info
	 * @return
	 */
	public WebViewDetail saveOrUpdate(WebViewDetail info){
		return (WebViewDetail)this.baseDao.saveOrUpdate(info);
	}
	
	/**
	 * 保存推送权限
	 * @author michael
	 * @param info
	 * @return
	 */
	public WebPush saveOrUpdate(WebPush info){
		return (WebPush)this.baseDao.saveOrUpdate(info);
	}
	
	/**
	 * 保存推送权限明细
	 * @author michael
	 * @param info
	 * @return
	 */
	public WebPushDetail saveOrUpdate(WebPushDetail info){
		return (WebPushDetail)this.baseDao.saveOrUpdate(info);
	}
	
	/**
	 * 保存投资策略的产品
	 * @author michael
	 * @param strategyInfo
	 * @param fundId
	 * @return
	 */
	public StrategyProduct saveOrUpdateStrategyProduct(StrategyInfo strategyInfo, String fundId){
		ProductInfo productInfo = null;
		FundInfo fundInfo = fundInfoService.findFundInfoById(fundId);
		if (null!=fundInfo && null!=fundInfo.getProduct() && null!=fundInfo.getProduct().getId() && !"".equals(fundInfo.getProduct().getId()))
			productInfo = fundInfo.getProduct();
		
		//生成product记录
		if (null==productInfo || null==productInfo.getId() || "".equals(productInfo.getId())){
			productInfo = new ProductInfo();
//			productInfo.setId(fundId);
			productInfo.setType(CommonConstantsWeb.WEB_PRODUCT_TYPE_FUND);
			//productInfo.setTableNameSql("fund_info");
			//productInfo.setTableNameHql("fundInfo");
			productInfo.setLastUpdate(new Date());
			productInfo.setCreateTime(new Date());
			productInfo.setIsValid("1");
			
			productInfo = (ProductInfo)this.baseDao.saveOrUpdate(productInfo);
			
			//保存基金与产品的关系
			fundInfo.setProduct(productInfo);
			fundInfoService.saveOrUpdate(fundInfo);
		}
		
		//保存策略与基金产品的关系
		StrategyProduct product = new StrategyProduct();
		product = findStrategyProduct(strategyInfo.getId(),productInfo.getId());
		if (null==product || null==product.getId() || "".equals(product.getId())){
			product=new StrategyProduct();
			product.setStrategy(strategyInfo);
			product.setProduct(productInfo);
			product = (StrategyProduct)this.baseDao.saveOrUpdate(product);
		}
		return product;
	}
	
	/**
	 * 获取投资策略关联的产品信息
	 * @author michael
	 * @param strategyId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ProductInfo findProductInfoByStrategy(String strategyId) {
		List<String> params = new ArrayList<String>();
		String hql = " from ProductInfo t where t.type='fund' and t.id in (select s.product.id from StrategyProduct s where s.strategy.id=?) ";
		params.add(strategyId);
		List list = this.baseDao.find(hql, params.toArray(), false);
		if (null!=list && !list.isEmpty()){
			return (ProductInfo)list.get(0);
		}
		return null;
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

    /**
     * 保存查看权限明细
     * @author michael
     * @param webView
     * @param type
     * @param toMembers
     */
	public void saveWebViewDetail(WebView webView, String type, String toMembers){
		if (StringUtils.isNotBlank(toMembers)) {
			toMembers = StrUtils.reHeavy(toMembers);
			Object[] idArrStrings = toMembers.split(",");
	    	clearWebViewDetail(webView.getId(), type);
	        if (idArrStrings!=null && idArrStrings.length>0){
	        	for(int k=0;k<idArrStrings.length;k++){
	        		String id = idArrStrings[k].toString();
	        		if(id!=null && id.length()>0){
	        			MemberBase member = memberBaseService.findById(id);
	        			if (null!=member){
		        			WebViewDetail detail = new WebViewDetail();
		        			detail.setToMember(member);
		        			detail.setView(webView);
		        			detail.setType(type);
		           			saveOrUpdate(detail);
		           			
	        			}
	        		}
	        	}
	        }
		}
    }
	
	/**
	 * 发送通知
	 * @author mqzou 2017-02-10
	 * @param relateId
	 * @param fromUser
	 * @param toUser
	 */
	private void saveWebReadToDo(String relateId,MemberBase fromUser,String toMembers){
		Object[] idArrStrings = toMembers.split(",");
    	if(idArrStrings != null && idArrStrings.length > 1){
    		Set<Object> idSet = new TreeSet<Object>();
    		for (int i = 0; i < idArrStrings.length; i++) {
    			idSet.add(idArrStrings[i]);
			}
    		idArrStrings = idSet.toArray();
    	}
    	 if (idArrStrings!=null && idArrStrings.length>0){
    		 for (Object object : idArrStrings) {
				String id=object.toString();
				if("".equals(id))
					continue;
				MemberBase toUser=memberBaseService.findById(id);
				webReadToDoService.sendToRead(CommonConstantsWeb.WEB_READ_MESSAGE_TYPE_NORMAL, CommonConstantsWeb.WEB_READ_MODULE_STRATEGY, relateId, fromUser.getNickName()+"向您推荐了策略，请查看", "/front/strategy/info/strategiesdetail.do", "id="+relateId, "0", fromUser, toUser);
			}
    	 }
	}
    
    /**
     * 保存推送权限明细
     * @author michael
     * @param webView
     * @param type
     * @param toMembers
     */
    public void saveWebPushDetail(WebPush webPush, String type, String toMembers){
    	// modify by wwluo
    	String[] idArrStrings = null;
    	if(StringUtils.isNotBlank(toMembers) && toMembers.indexOf(",") > -1){
    		idArrStrings = toMembers.split(",");
    	}
    	// end
    	clearWebPushDetail(webPush.getId(), type);
        if (idArrStrings!=null && idArrStrings.length>0){
        	for(int k=0;k<idArrStrings.length;k++){
        		String id = idArrStrings[k].toString();
        		if(id!=null && id.length()>0){
        			MemberBase member = memberBaseService.findById(id);
        			if (null!=member && null!=member.getIconUrl()){
	        			WebPushDetail detail = new WebPushDetail();
	        			detail.setToMember(member);
	        			detail.setPush(webPush);
	        			detail.setType(type);
	           			saveOrUpdate(detail);
	           		
        			}
        		}
        	}
        }
    }
    
    /**
     * 保存查看权限信息
     * @author michael
     * @param webView
     * @param clients
     * @param prospects
     * @param buddies
     * @param colleagues
     * @return WebPush
     */
    public WebView saveWebView(WebView webView, String clients, String prospects, String buddies, String colleagues){
    	WebView result = saveOrUpdate(webView);
    	if("0".equals(webView.getBuddyFlag())){
    		buddies="";
    	}
    	if("0".equals(webView.getClientFlag())){
    		clients="";
    	}
    	if("0".equals(webView.getProspectFlag())){
    		prospects="";
    	}
    	if("0".equals(webView.getColleagueFlag())){
    		colleagues="";
    	}
    	
    	saveWebViewDetail(result, CommonConstantsWeb.WEB_VIEW_DETAIL_TYPE_BUDDY, buddies);
       	saveWebViewDetail(result, CommonConstantsWeb.WEB_VIEW_DETAIL_TYPE_CLIENT, clients);
       	saveWebViewDetail(result, CommonConstantsWeb.WEB_VIEW_DETAIL_TYPE_PROSPECT, prospects);
       	saveWebViewDetail(result, CommonConstantsWeb.WEB_VIEW_DETAIL_TYPE_TEAM, colleagues);
       	
       	//modify by mqzou 发送消息提醒
       //	saveWebReadToDo(webView.getRelateId(), webView.getFromMember(), buddies+","+clients+","+prospects+","+colleagues);
		return result;
    }
    
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
    public WebPush saveWebPush(WebPush webPush, String clients, String prospects, String buddies, String colleagues){
    	WebPush result = saveOrUpdate(webPush);

    	
       	saveWebPushDetail(result, CommonConstantsWeb.WEB_PUSH_DETAIL_TYPE_BUDDY, buddies);
       	saveWebPushDetail(result, CommonConstantsWeb.WEB_PUSH_DETAIL_TYPE_CLIENT, clients);
       	saveWebPushDetail(result, CommonConstantsWeb.WEB_PUSH_DETAIL_TYPE_PROSPECT, prospects);
       	saveWebPushDetail(result, CommonConstantsWeb.WEB_PUSH_DETAIL_TYPE_TEAM, colleagues);
       	return result;
    }

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
	@Override
	public List<StrategyAllocationVO> findStrategyAllocationData(String strategyId,String langCode,String methodType) {
		String sql=" select r.*,s.name_"+langCode+" from strategy_allocation r left join sys_param_config s on r.item_code=s.config_code where r.strategy_id=?";
		sql+=" and r.method_type=?";
		List params=new ArrayList();
		params.add(strategyId);
		params.add(methodType);
		List reList=this.springJdbcQueryManager.springJdbcQueryForList(sql, params.toArray());
		List<StrategyAllocationVO> list=new ArrayList<StrategyAllocationVO>();
		Iterator it=reList.iterator();
		while (it.hasNext()) {
			HashMap map = (HashMap) it.next();
			StrategyAllocationVO vo=new StrategyAllocationVO();
			vo.setId(getMapObject(map, "id"));
			vo.setItemCode(getMapObject(map, "item_code"));
			vo.setItemName(getMapObject(map, "name_"+langCode));
			vo.setItemWeight(getMapObject(map, "item_weight"));
			vo.setMethodType(getMapObject(map, "method_type"));
			vo.setStrategyId(getMapObject(map, "strategy_id"));
			list.add(vo);
		}
		return list;
	}
	
	private String getMapObject(Map map, String key) {
		return map.get(key) == null ? "" : map.get(key).toString();
	}
	
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
	@Override
	public List<StrategyAllocationVO> findStrategyAllocation(String strategyId, String langCode, String methodType, String type) {
		StringBuilder hql=new StringBuilder();
		hql.append(" select r,s."+getLangString("name", langCode)+",t.displayColor from StrategyAllocation r left join SysParamConfig s on r.itemCode=s.configCode ");
		hql.append(" left join ProductType t on r.type=t.id");
		hql.append( " where r.strategy.id=? and r.methodType=?");
		hql.append(" and r.itemWeight>0");
		
		List params=new ArrayList();
		params.add(strategyId);
		params.add(methodType);
		
		if(null!=type && !"".equals(type)){
			hql.append(" and r.type=?");
			params.add(type);
		}
		List resultList=this.baseDao.find(hql.toString(), params.toArray(), false);
		List<StrategyAllocationVO> list=new ArrayList<StrategyAllocationVO>();
		if(null!=resultList){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				Object[] object = (Object[]) it.next();
				StrategyAllocation allocation=(StrategyAllocation)object[0];
				Object name=object[1];
				String displayColor=null!=object[2]?object[2].toString():"";
				StrategyAllocationVO vo=new StrategyAllocationVO();
				vo.setId(allocation.getId());
				vo.setItemCode(allocation.getItemCode());
				vo.setItemName(null!=name?name.toString():"");
				vo.setItemWeight(allocation.getItemWeight().toString());
				vo.setMethodType(allocation.getMethodType());
				vo.setStrategyId(allocation.getStrategy().getId());
				vo.setDisplayColor(displayColor);
				list.add(vo);
			}
		}
		
		
		return list;
	}
	
	/**
	 * 获取投资策略分配比例汇总(按类型)
	 * @param strategyId
	 * @return
	 */
	@Override
	public List<StrategyAllocationTypeVO> findStrategyAllocationType(String strategyId) {
		StringBuilder hql=new StringBuilder();
		hql.append(" select r.type,sum(r.itemWeight),t.displayColor from StrategyAllocation r ");
		hql.append(" left join  ProductType t on r.type=t.id");
		hql.append(" where r.strategy.id=? and r.layerLevel=1");
		hql.append(" group by  r.type having sum(r.itemWeight)>0");
		hql.append(" order by t.orderBy ");
		List params=new ArrayList();
		params.add(strategyId);
		List resultList=this.baseDao.find(hql.toString(), params.toArray(), false);
		List<StrategyAllocationTypeVO> list=new ArrayList<StrategyAllocationTypeVO>();
		
		if(null!=resultList){
		    Iterator it=resultList.iterator();
		    while (it.hasNext()) {
				Object[] object = (Object[]) it.next();
				String type= null!=object[0]?object[0].toString():"";
				if("".equals(type))
					continue;
				String weight=null!=object[1]?object[1].toString():"0";
				String displayColor=null!=object[2]?object[2].toString():"";
				StrategyAllocationTypeVO vo=new StrategyAllocationTypeVO();
				vo.setTypeName(type);
				vo.setWeight(weight);
				vo.setDisplayColor(displayColor);
				list.add(vo);
			}
		    
		}
		
		return list;
		
	}
	
	/**
	 * 获取投资策略分配方式
	 * @author mqzou
     * @date 2016-11-08
	 * @param strategyId
	 * @return
	 */
	@Override
	public List<StrategyAllocMethodVO> findAllocMethod(String strategyId,String type) {
		String hql=" select distinct r.methodType,r.layerLevel  from StrategyAllocation r where r.strategy.id=? and r.type=? and r.methodType !=null and r.methodType!='' and r.layerLevel>1";
		hql+="  group by r.methodType HAVING sum(r.itemWeight)>0 ";
		hql+=" order by r.layerLevel asc";
		List params=new ArrayList();
		params.add(strategyId);
		params.add(type);
		List resultList=this.baseDao.find(hql, params.toArray(), false);
		List list=new ArrayList();
	    if(null!=resultList){
	    	Iterator it=resultList.iterator();
	    	while (it.hasNext()) {
				Object[] object = (Object[]) it.next();
				Object methodType=object[0];
				Object  layerLevel=object[1];
				StrategyAllocMethodVO vo=new StrategyAllocMethodVO();
				vo.setTypeName(type);
				vo.setLayerLevel(null!=layerLevel?layerLevel.toString():"1");
				vo.setMethodType(null!=methodType?methodType.toString():"");
				if("G".equals(vo.getMethodType())){
					vo.setMethodTypeName("Geographical");
				}else if ("S".equals(vo.getMethodType())) {
					vo.setMethodTypeName("Sector");
				}else if ("F".equals(vo.getMethodType())) {
					vo.setMethodTypeName("Funds Type");
				}else if ("P".equals(vo.getMethodType())) {
					vo.setMethodTypeName("Product Type");
				}
				list.add(vo);
			}
	    }
		return list;
	}
	
	/**
	 * 保存策略权重分配
	 * @author michael
	 * @param info
	 * @return
	 */
	public StrategyAllocation saveOrUpdate(StrategyAllocation info){
		return (StrategyAllocation)this.baseDao.saveOrUpdate(info);
	}
	
	/**
	 * 通过多条件获取列表
	 * @author michael
	 * @param strategyId
	 * @param layer 所属层级
	 * @param type 分配方法,G:Geographical,S:Sector,F:Funds Type
	 * @return
	 */
	public List<StrategyAllocation> findStrategyAllocation(String strategyId,String layer,String type) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder(" from StrategyAllocation l where l.strategy.id=? ");
	//	hql.append(" and r.itemWeight>0");
		params.add(strategyId);
		if(null != layer && !"".equals(layer)){
			hql.append(" and l.layerLevel=?");
			params.add(layer);
		}
		if(null != type && !"".equals(type)){
			hql.append(" and l.methodType=?");
			params.add(type);
		}
		List<StrategyAllocation> list = (List<StrategyAllocation>)this.baseDao.find(hql.toString(), params.toArray(), false);
		return list;
	}
	
	/**
	 * 删除策略分配设置
	 * @author michael
	 * @param strategyId
	 * @param method 分配方法,G:Geographical,S:Sector,F:Funds Type
	 * @return
	 */
	public void deleteStrategyAllocationByMethod(String strategyId, String method){
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder(" delete from StrategyAllocation l where l.strategy.id=? ");
		params.add(strategyId);
		if(null != method && !"".equals(method)){
			hql.append(" and l.methodType=?");
			params.add(method);
		}
		this.baseDao.updateHql(hql.toString(), params.toArray());
	}
	
	/**
	 * 删除策略分配设置(methodType='P')
	 * @author michael
	 * @param strategyId
	 * @param type 分配置类型,fund,stock,bond
	 * @return
	 */
	public void deleteStrategyAllocationByType(String strategyId, String type){
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder(" delete from StrategyAllocation l where l.methodType=? and l.strategy.id=? ");
		params.add(CommonConstantsWeb.WEB_ALLOCATION_METHOD_PRODUCT);
		params.add(strategyId);
		if(null != type && !"".equals(type)){
			hql.append(" and l.type=?");
			params.add(type);
		}
		this.baseDao.updateHql(hql.toString(), params.toArray());
	}
	
	/**
	 * 删除策略统计记录
	 * @author michael
	 * @param strategyId
	 * @return
	 */
	public void deleteStrategyCount(String strategyId){
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder(" delete from StrategyCount l where l.id=? ");
		params.add(strategyId);

		this.baseDao.updateHql(hql.toString(), params.toArray());
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
		String hql = " from ProductInfo t where t.type=? and t.id in (select s.product.id from StrategyProduct s where s.strategy.id=?) ";
		params.add(type);
		params.add(strategyId);
		return (List<ProductInfo>)this.baseDao.find(hql, params.toArray(), false);
	}
	
	/**
	 * 删除策略产品
	 * @author michael
	 * @param strategyId
	 * @return
	 */
	public void deleteStrategyProduct(String strategyId){
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder(" delete from StrategyProduct l where l.strategy.id=? ");
		params.add(strategyId);
		this.baseDao.updateHql(hql.toString(), params.toArray());
	}
	
    /**
     * 保存策略配置
     * @author michael
     * @param info 策略信息
     * @param method 分配类型 G:Geographical,S:Sector,F:Funds Type,P:Product Type
     * @param level 分配层级
     * @param codes 配置编码
     * @param weights 配置编码对应的比例
     */
    public void saveStrategyAllocation(StrategyInfo info, String method, String level, String codes, String weights){
    	if (StrUtils.getString(method).length()>0 && StrUtils.getString(codes).length()>0 
    			&& StrUtils.getString(weights).length()>0){
	    	String[] codeArr = codes.split(",");
	    	String[] weightArr = weights.split(",");
	        if (codeArr.length==weightArr.length && weightArr.length>0){
	        	for(int k=0;k<codeArr.length;k++){
	        		if (StrUtils.getString(codeArr[k]).length()>0){
		        		StrategyAllocation allocation = new StrategyAllocation();
		        		allocation.setStrategy(info);
		        		
		        		//分配置类型,fund,stock,bond
		        		if (CommonConstantsWeb.WEB_ALLOCATION_METHOD_PRODUCT.equalsIgnoreCase(method))
		        			allocation.setType(StrUtils.getString(codeArr[k]));
		        		else
		        			allocation.setType(CommonConstantsWeb.WEB_ALLOCATION_TYPE_FUND);
		        		
		        		allocation.setMethodType(method);
		        		allocation.setLayerLevel(StrUtils.getInteger(level));
		        		allocation.setItemCode(StrUtils.getString(codeArr[k]));
		        		allocation.setItemWeight(StrUtils.getInteger(weightArr[k]));
		        		saveOrUpdate(allocation);
	        		}
	        	}
	        }
    	}
    }
    
    /**
     * 获取策略列表（提供选择）
     * @author mqzou
     * @date 2016-10-26
     * @param memberId
     * @return
     */
    @Override
    public List<StrategyForSelectVO> findStrategyListForSelect(String memberId) {
    	String hql="select r.id, r.riskLevel,r.strategyName from StrategyInfo r where r.isValid='1' and r.status='1'";
    	List params=new ArrayList();
    	if(null!=memberId && !"".equals(memberId)){
    		hql+=" and  r.creator.id=?";
    		params.add(memberId);
    	}
    	List resultList=this.baseDao.find(hql, params.toArray(), false);
    	List<StrategyForSelectVO> list=new ArrayList<StrategyForSelectVO>();
    	if(null!=resultList){
    		Iterator it=resultList.iterator();
    		while (it.hasNext()) {
				Object[] obj=(Object[])it.next();
				String id=obj[0].toString();
				String riskLevel=null!=obj[1]?obj[1].toString():"0";
				String name=null!=obj[2]?obj[2].toString():"";
				StrategyForSelectVO vo=new StrategyForSelectVO();
				vo.setId(id);
				vo.setRiskLevel(riskLevel);
				vo.setStrategyName(name);
				List<CharDataVO> dataList=getAllocationByType(id, CommonConstantsWeb.WEB_ALLOCATION_METHOD_PRODUCT);//getTypeAllocation(id);
				String jsonString=JsonUtil.toJson(dataList).replace('"', '\'');
				vo.setAllocationData(jsonString);
				list.add(vo);
			}
    	}
    
    	return list;
    }
    /**
     * 获取策略的分配列表，按照产品类型统计
     * @author mqzou
     * @date 2016-10-26
     * @param strategyId
     * @return
     */
    private List<CharDataVO> getTypeAllocation(String strategyId){
    	List<CharDataVO> list=new ArrayList<CharDataVO>();
    	String hql="select r.type,sum(r.itemWeight) from StrategyAllocation r where r.strategy.id='"+strategyId+"'";
    	List resultList=this.baseDao.find(hql, null, false);
    	if(null!=resultList){
    		Iterator it=resultList.iterator();
    		while (it.hasNext()) {
				Object[] obj=(Object[])it.next();
				Object typeName=obj[0];
				Object weight=obj[1];
				
				CharDataVO vo=new CharDataVO();
				vo.setName(null!=typeName?typeName.toString():"");
				vo.setValue(Double.valueOf(null!=weight?weight.toString():"0"));
				list.add(vo);
			}
    	}
    	
    	return list;
    }
    
    private List<CharDataVO> getAllocationByType(String strategyId,String methodType){
    	List<CharDataVO> list=new ArrayList<CharDataVO>();
    	String hql="select r.type,r.itemWeight from StrategyAllocation r where r.strategy.id='"+strategyId+"' and r.methodType='"+methodType+"'";
    	List resultList=this.baseDao.find(hql, null, false);
    	if(null!=resultList){
    		Iterator it=resultList.iterator();
    		while (it.hasNext()) {
				Object[] obj=(Object[])it.next();
				Object typeName=obj[0];
				Object weight=obj[1];
				
				CharDataVO vo=new CharDataVO();
				//allocation.
				vo.setName(null!=typeName?typeName.toString():"");
				vo.setValue(Double.valueOf(null!=weight?weight.toString():"0"));
				list.add(vo);
			}
    	}
    	
    	return list;
    }

    /**
	 * 获取我的策略
	 * @author wwluo
	 * @param keyWord 过滤名称关键字关键字
	 * @param isPublic 是否公开
	 * @param status 是否草稿
	 * @date 2016-11-18
	 */
	@Override
	public JsonPaging getMyStrategys(JsonPaging jsonPaging,
			MemberBase loginUser,String keyWord,String isPublic,String status) {
		if(loginUser != null){
			StringBuffer hql = new StringBuffer("" +
					" from" +
					" StrategyInfo s" +
					" where" +
					" s.isValid=1 and" +
					" s.creator=?" +
					"");
			List<Object> params = new ArrayList<Object>();
			params.add(loginUser);
			if (StringUtils.isNotBlank(keyWord)) {
				hql.append(" and s.strategyName like ?");
				params.add("%"+keyWord+"%");
			}
			if (StringUtils.isNotBlank(isPublic)) {
				hql.append(" and s.isPublic=?");
				params.add(isPublic);
			}
			if (StringUtils.isNotBlank(status)) {
				hql.append(" and s.status=?");
				params.add(status);
			}
			hql.append(" order by s.overhead desc,s.lastUpdate desc");
			jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		}
		return jsonPaging;
	}

	/**
	 * 根据关联id获取访问记录
	 * @author wwluo
	 * @date 2016-11-18
	 */
	@Override
	public List<WebInvestorVisit> getWebInvestorVisitByRelateId(String relateId) {
		List<WebInvestorVisit> list = null;
		if(StringUtils.isNotBlank(relateId)){
			StringBuffer hql = new StringBuffer("" +
					" select" +
					" distinct w.member" +
					" from" +
					" WebInvestorVisit w" +
					" where" +
					" w.relateId=?" +
					" group by" +
					" w.member" +
					" order by" +
					" w.vistiTime desc");
			List<Object> params = new ArrayList<Object>();
			params.add(relateId);
			list = this.baseDao.find(hql.toString(), params.toArray(), false);
		}
		return list;
	}
	
	/**
	 * 获取客户访问记录
	 * @author wwluo
	 * @date 2016-11-18
	 * @param relateId 关联Id
	 * @param longinUser 当前用户
	 */
	@Override
	public JsonPaging getCustomerVisitStrategy(JsonPaging jsonPaging,String relateId,String moduleType,
			MemberBase loginUser) {
		List<WebInvestorVisitVO> list = null;
		boolean isAll=jsonPaging.getRows()==null?true:false;
		if(loginUser != null && StringUtils.isNotBlank(relateId)&& StringUtils.isNotBlank(moduleType)){
			StringBuffer hql = new StringBuffer("" +
					" select" +
					" distinct w.member,w,c" +
					" from" +
					" WebInvestorVisit w" +
					" left join" +
					" CrmCustomer c" +
					" on" +
					" c.member.id=w.member.id" +
					" left join" +
					" MemberIfa i" +
					" on" +
					" i.id=c.ifa.id" +
					" where" +
					" w.relateId=? and" +
					" w.moduleType=? and" +
					" i.member=?" +
					" group by" +
					" w.member" +
					" order by" +
					" w.vistiTime desc");
			List<Object> params = new ArrayList<Object>();
			params.add(relateId);
			params.add(moduleType);
			params.add(loginUser);
			jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
			//List obj = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(!jsonPaging.getList().isEmpty()){
				list = new ArrayList<WebInvestorVisitVO>();
				for (Object object : jsonPaging.getList()) {
					Object[] temp = (Object[]) object;
					WebInvestorVisitVO visitVO = new WebInvestorVisitVO();
					WebInvestorVisit webInvestorVisit = (WebInvestorVisit) temp[1];
					CrmCustomer customer=(CrmCustomer)temp[2];
					BeanUtils.copyProperties(webInvestorVisit, visitVO);
					if(webInvestorVisit.getMember() != null){
						
						visitVO.setIconUrl(PageHelper.getUserHeadUrl(webInvestorVisit.getMember().getIconUrl(), null));
					}
					Date visitTime = webInvestorVisit.getVistiTime();
					Calendar calendar = Calendar.getInstance();
					Long diffValue = calendar.getTimeInMillis()-visitTime.getTime();
					if(diffValue<86400000 && diffValue>3600000){
						visitVO.setVistiPeriodType("H");
						diffValue = diffValue/3600000;
						visitVO.setVistiPeriodTime(Integer.parseInt(diffValue.toString()));
					}else if(diffValue<86400000 && diffValue>60000){
						visitVO.setVistiPeriodType("M");
						diffValue = diffValue/60000;
						visitVO.setVistiPeriodTime(Integer.parseInt(diffValue.toString()));
					}
					visitVO.setNickName(null!=customer?customer.getNickName():"");
					list.add(visitVO);
				}
				jsonPaging.getList().clear();
				jsonPaging.getList().addAll(list);
				
				
				if(isAll)
				  jsonPaging.setTotal(jsonPaging.getList().size());
				else {
					hql=new StringBuffer();
					hql.append("select count(distinct m) from WebInvestorVisit w left join" +
						" CrmCustomer c on c.member.id=w.member.id left join MemberIfa i on i.id=c.ifa.id" +
						" left join MemberBase m on w.member.id=m.id" +
						" where" +
						" w.relateId=? and" +
						" w.moduleType=? and" +
						" i.member=?");
				    List countList=this.baseDao.find(hql.toString(), params.toArray(), false);
				    if(null!=countList && !countList.isEmpty()){
				    	Object object=countList.get(0);
				    	if(null!=object && !"".equals(object.toString())){
				    		jsonPaging.setTotal(Integer.parseInt(object.toString()));
				    	}
				    }
				}
			}
		}
		return jsonPaging;
	}

	/**
	 * 删除我的策略（物理删除）
	 * @author wwluo
	 * @date 2016-11-18
	 */
	@Override
	public void delStrategy(StrategyInfo info) {
		info.setIsValid("0");
		this.baseDao.update(info);
	}
	
	/**
	 * 获取ifa推荐的策略列表
	 * @author mqzou	
	 * @date 2016-11-29
	 * @param jsonPaging
	 * @param ifaMemberId
	 * @param keyWord
	 * @return
	 */
	@Override
	public JsonPaging findByIfaRecommend(JsonPaging jsonPaging, String ifaMemberId, String keyWord,String langCode,MemberBase loginUser) {
		StringBuilder hql=new StringBuilder();
		List<Object> params=new ArrayList<Object>();
		hql.append("  from  StrategyInfo s  where 1=1 ");
		hql.append(" and s.creator.id=? and s.isValid='1' and s.status='1'");
		params.add(ifaMemberId);
		if(null!=keyWord && !"".equals(keyWord)){
			hql.append(" and s.strategyName like ?");
			params.add("%"+keyWord+"%");
		}
		jsonPaging.setOrder("desc");
		jsonPaging.setSort("s.lastUpdate");
		jsonPaging=this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List<StrategyInfoWebVO> result = new ArrayList<StrategyInfoWebVO>();
		List list = jsonPaging.getList();
		if (null!=list && !list.isEmpty()){
			Iterator it=list.iterator();
			while (it.hasNext()) {
				//Object[] map = (Object[]) it.next();
				StrategyInfo info=(StrategyInfo) it.next();
				//Object reason=map[1];
				StrategyInfoWebVO infoVo = new StrategyInfoWebVO(info);
				infoVo.setRecommendReason(info.getReason());
				//设置ifa信息
				try {
					infoVo.setIfa(memberBaseService.findIfaMember(info.getCreator().getId()));
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				//获取统计数据
				StrategyCount count = findStrategyCountById(info.getId());
				if (null!=count && null!=count.getViews()) info.setClick(count.getViews());
				
				/*if (criteria.isCheckCount()){
					
				}
				if (criteria.isCheckProduct()){
					List<ProductInfo> prods = findProductInfoList(info.getId(), CommonConstantsWeb.WEB_PRODUCT_TYPE_FUND);
					if (null==prods || prods.isEmpty()) info.setProductCount(0);
					else info.setProductCount(prods.size());
				}*/
				//if(criteria.isCheckIsMyFollow()){
					String isFollow = followStatusService.getWebFollowStatus(info.getId(), loginUser.getId(), "strategy");
					infoVo.setIsFollow(isFollow);
				//}
				String goe=info.getGeoAllocation();
				infoVo.setGeoAllocation(sysParamService.findNameByCode(goe, langCode));
				String sector=info.getSector();
				infoVo.setSector(sysParamService.findNameByCode(sector, langCode));
				
				/*String risk=info.getRiskLevel();
				String riskName=sysParamService.findNameByValue("strategy_risk_rating", risk, langCode);
				infoVo.setRiskLevel(riskName);*/
				
				List<CharDataVO> allocation=new ArrayList<CharDataVO>();
				List<StrategyAllocationTypeVO> typeList=findStrategyAllocationType(info.getId());
				if(null!=typeList){
					Iterator iterator=typeList.iterator();
					while (iterator.hasNext()) {
						StrategyAllocationTypeVO object = (StrategyAllocationTypeVO) iterator.next();
						CharDataVO vo=new CharDataVO();
						vo.setName(object.getTypeName()+" "+String.valueOf(object.getWeight())+"%");
						vo.setValue(Double.valueOf(object.getWeight()) );
						allocation.add(vo);
					}
				}
				String allocationJson=JsonUtil.toJson(allocation);
				
				allocationJson.replace("\"", "'");
				infoVo.setAllocationData(allocationJson);
				
				result.add(infoVo);
			}
			
		}
		jsonPaging.setList(result);
		
		return jsonPaging;
	}

	/**
	 * 获取推送信息
	 * @author wwluo	
	 * @date 2016-12-21
	 * @param id 关联Id relateId
	 * @param moduleType 
	 * @return
	 */
	@Override
	public WebPush getWebPushByRelateId(String id, String moduleType) {
		WebPush webPush = null;
		if(StringUtils.isNotBlank(id) && StringUtils.isNotBlank(moduleType)){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" WebPush" +
					" WHERE" +
					" relateId=?" +
					" AND" +
					" moduleType=? ");
			List<Object> params = new ArrayList<Object>();
			params.add(id);
			params.add(moduleType);
			List<WebPush> webPushs = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(webPushs != null && !webPushs.isEmpty()){
				webPush = webPushs.get(0);
			}
		}
		return webPush;
	}

	/**
	 * 删除权限信息
	 * @author wwluo	
	 * @date 2016-12-21
	 * @return
	 */
	@Override
	public void delWebViewDetail(String id) {
		if(StringUtils.isNotBlank(id)){
			StringBuffer hql = new StringBuffer(" DELETE FROM WebViewDetail w WHERE w.view.id=? ");
			List<Object> params = new ArrayList<Object>();
			params.add(id);
			this.baseDao.updateHql(hql.toString(), params.toArray());
		}
	}
	
	/**
	 * 获取策略的分配项（按比例从大到小取前几个）
	 * @author mqzou 2017-04-25
	 * @param strategyId
	 * @param methodType
	 * @param top
	 * @return
	 */
	@Override
	public String getStrategyAllocationItems(String strategyId, String methodType, int top) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from StrategyAllocation r  where 1=1 ");
		List<Object> params=new ArrayList<Object>();
		if(null!=strategyId && !"".equals(strategyId)){
			hql.append(" and r.strategy.id=?");
			params.add(strategyId);
		}
		if(null!=methodType && !"".equals(methodType)){
			hql.append(" and r.methodType=?");
			params.add(methodType);
		
		}
		JsonPaging jsonPaging=new JsonPaging();
		jsonPaging.setPage(1);
		jsonPaging.setRows(top);
		hql.append(" order by r.itemWeight desc");
		jsonPaging=baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		if(null!=jsonPaging && null!=jsonPaging.getList()){
			Iterator it=jsonPaging.getList().iterator();
			List<String> list=new ArrayList<String>();
			while (it.hasNext()) {
				StrategyAllocation vo = (StrategyAllocation) it.next();
				list.add(vo.getItemCode());
			}
			String str=StrUtils.arrayListToString(list, ",");
			return str;
		}
		
		return null;
	}

	/**
	 * 获取策略详情
	 * @author mqzou 2017-05-15
	 * @param id
	 * @param langCode
	 * @return
	 */
	@Override
	public StrategyInfoWebVO findStrategyDetail(String id, String langCode,String memberId,String dateFormat) {
		StrategyInfo info=(StrategyInfo)baseDao.get(StrategyInfo.class, id);
		if(null!=info){
			StrategyInfoWebVO strategyInfoWebVO = new StrategyInfoWebVO(info);
			strategyInfoWebVO.setLastUpdate(DateUtil.dateToDateString(DateUtil.StringToDate(strategyInfoWebVO.getLastUpdate(), DateUtil.DEFAULT_DATE_TIME_FORMAT), dateFormat));
			strategyInfoWebVO.setIsFollow( webFollowService.getWebFollowStatus(strategyInfoWebVO.getId(), memberId , "strategy") );
			String geo=getStrategyAllocationItems(id, CommonConstantsWeb.WEB_ALLOCATION_METHOD_GEOGRAPHICAL, 2);
			String geoAllocation=sysParamService.findNameByCode(geo, langCode);
			strategyInfoWebVO.setGeoAllocation(geoAllocation);
			String set=getStrategyAllocationItems(id, CommonConstantsWeb.WEB_ALLOCATION_METHOD_SECTOR, 2);
			String sector=sysParamService.findNameByCode(set, langCode);
			strategyInfoWebVO.setSector(sector);
			
            
            MemberIfa ifa=memberBaseService.findIfaMember(strategyInfoWebVO.getCreator().getId());
            strategyInfoWebVO.setIfa(ifa);
            String creatorName=getCommonMemberName(strategyInfoWebVO.getCreator().getId(), langCode, "2");
            strategyInfoWebVO.setCreatorName(creatorName);
            return strategyInfoWebVO;
		}
		return null;
	}

	/**
	 * 获取投资策略分配比例信息
	 * @author wwluo
	 * @param strategyId
	 * @param methodType 分配方法
	 * @return
	 */
	@Override
	public List<StrategyAllocation> getStrategyAllocation(String strategyId,
			String methodType) {
		List<StrategyAllocation> allocations = null;
		if (StringUtils.isNotBlank(strategyId)) {
			StringBuffer hql = new StringBuffer(" FROM StrategyAllocation s WHERE s.strategy.id=?");
			List<Object> params = new ArrayList<Object>();
			params.add(strategyId);
			if (StringUtils.isNotBlank(methodType)) {
				hql.append(" AND s.methodType=?");
				params.add(methodType);
			}
			allocations = this.baseDao.find(hql.toString(), params.toArray(), false);
		}
		return allocations;
	}
}
