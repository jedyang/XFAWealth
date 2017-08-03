package com.fsll.wmes.strategy.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.StrategyAllocation;
import com.fsll.wmes.entity.StrategyCount;
import com.fsll.wmes.entity.StrategyInfo;
import com.fsll.wmes.entity.StrategyProduct;
import com.fsll.wmes.entity.WebPush;
import com.fsll.wmes.entity.WebPushDetail;
import com.fsll.wmes.entity.WebView;
import com.fsll.wmes.entity.WebViewDetail;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.strategy.service.StrategyInfoService;
import com.fsll.wmes.strategy.vo.CriteriaVO;
import com.fsll.wmes.strategy.vo.StrategyAllocationTypeVO;
import com.fsll.wmes.strategy.vo.StrategyAllocationVO;
import com.fsll.wmes.strategy.vo.StrategyDetailVO;
import com.fsll.wmes.strategy.vo.StrategyListVO;
import com.fsll.wmes.strategy.vo.StrategyWebPushVO;
import com.fsll.wmes.strategy.vo.StrategyWebViewVO;

@Service("strategyInfoService")
//@Transactional
public class StrategyInfoServiceImpl extends BaseService implements
		StrategyInfoService {

	@Autowired
	private MemberBaseService memberBaseService;
	
	@Autowired
    private FundInfoService fundInfoService;
	@Autowired
	private SysParamService sysParamService;
	
	/**
	 * 获取内容列表
	 */
	@Override
	public JsonPaging findAll(JsonPaging jsonPaging, String keyword,String langCode) {
		// TODO Auto-generated method stub
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder(" from StrategyInfo l where l.isValid='1' ");
		if(null != keyword && !"".equals(keyword)){
			hql.append(" and ( l.strategyName like ? or investmentGoal like ? or suitability like ? )");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
		}
		
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		if(null!=jsonPaging && null!=jsonPaging.getList()){
			List<StrategyListVO> list=new ArrayList<StrategyListVO>();
			Iterator it=jsonPaging.getList().iterator();
			while (it.hasNext()) {
				StrategyInfo info = (StrategyInfo) it.next();
				StrategyListVO vo=new StrategyListVO(info);
				if(null!=vo){
					String name=getCommonMemberName(info.getCreator().getId(), langCode, "2");
					vo.setCreatorName(name);
					
					String goe=getStrategyAllocationItems(info.getId(), CommonConstantsWeb.WEB_ALLOCATION_METHOD_GEOGRAPHICAL, 2);    //info.getGeoAllocation();
					vo.setGeoAllocationName(sysParamService.findNameByCode(goe, langCode));
					String sector=getStrategyAllocationItems(info.getId(), CommonConstantsWeb.WEB_ALLOCATION_METHOD_SECTOR, 2);//info.getSector();
					vo.setSectorName(sysParamService.findNameByCode(sector, langCode));
					list.add(vo);
				}
			}
			jsonPaging.setList(list);
		}
		return jsonPaging;
	}

	
	/**
	 * 获取内容列表
	 */
	@Override
	public JsonPaging findAllList(JsonPaging jsonPaging,String keyword,String langCode,String riskLevel) {
		// TODO Auto-generated method stub
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder(" from StrategyInfo l where l.isValid='1' ");
		if(null != keyword && !"".equals(keyword)){
			hql.append(" and ( l.strategyName like ? or investmentGoal like ? or suitability like ? )");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
		}
		if(null!=riskLevel && !"".equals(riskLevel)){
			hql.append(" and l.riskLevel=?");
			params.add(riskLevel);
		}
		
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		if(null!=jsonPaging && null!=jsonPaging.getList()){
			List<StrategyListVO> list=new ArrayList<StrategyListVO>();
			Iterator it=jsonPaging.getList().iterator();
			while (it.hasNext()) {
				StrategyInfo info = (StrategyInfo) it.next();
				StrategyListVO vo=new StrategyListVO(info);
				if(null!=vo){
					String name=getCommonMemberName(info.getCreator().getId(), langCode, "2");
					vo.setCreatorName(name);
					
					String goe=getStrategyAllocationItems(info.getId(), CommonConstantsWeb.WEB_ALLOCATION_METHOD_GEOGRAPHICAL, 2);    //info.getGeoAllocation();
					vo.setGeoAllocationName(sysParamService.findNameByCode(goe, langCode));
					String sector=getStrategyAllocationItems(info.getId(), CommonConstantsWeb.WEB_ALLOCATION_METHOD_SECTOR, 2);//info.getSector();
					vo.setSectorName(sysParamService.findNameByCode(sector, langCode));
					list.add(vo);
				}
			}
			jsonPaging.setList(list);
		}
		return jsonPaging;
	}
	
	/**
	 * 获取策略的分配项（按比例从大到小取前几个）
	 * @author mqzou 2017-04-25
	 * @param strategyId
	 * @param methodType
	 * @param top
	 * @return
	 */
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
		return findByUser(jsonPaging, criteria);
	}

		
	/**
	 * 通过多条件获取列表
	 * @author michael
	 * @param jsonPaging
	 * @param criteria
	 * @return
	 */
	public JsonPaging findByUser(JsonPaging jsonPaging, CriteriaVO criteria) {
		// TODO Auto-generated method stub
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder(" from StrategyInfo l where 1=1 ");
		
		if(null != criteria.getUserId() && !"".equals(criteria.getUserId())
			&& ("mylist".equals(StrUtils.getString(criteria.getModule()))
					||"Me".equals(StrUtils.getString(criteria.getSource())))){
			hql.append(" and creator.id=?");
			params.add(criteria.getUserId());
		}
		if(null != criteria.getKeyword() && !"".equals(criteria.getKeyword())){
			hql.append(" and ( l.strategyName like ? or investmentGoal like ? or suitability like ? )");
			params.add("%"+criteria.getKeyword()+"%");
			params.add("%"+criteria.getKeyword()+"%");
			params.add("%"+criteria.getKeyword()+"%");
		}
		if(null != criteria.getSector() && !"".equals(criteria.getSector())){
			hql.append(" and ( l.sector like ? )");
			params.add("%"+criteria.getSector()+"%");
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
			hql.append(" and lastUpdate>=?");
			params.add(DateUtil.StringToDate(criteria.getStartDate(),CoreConstants.DATE_FORMAT));
		}
		if(null != criteria.getEndDate() && !"".equals(criteria.getEndDate())){
			hql.append(" and lastUpdate<=?");
			params.add(DateUtil.StringToDate(criteria.getEndDate(),CoreConstants.DATE_FORMAT));
		}
		if(null != criteria.getStatus() && !"".equals(criteria.getStatus())){
			hql.append(" and status=?");
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
			
		if(null!=criteria.getOrderBy() && !"".equals(criteria.getOrderBy())){
			hql.append(" order by "+criteria.getOrderBy());
		}else{//默认排序
			hql.append(" order by overhead desc,lastUpdate desc ");
		}
		
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
//		if (criteria.isCheckCount() || criteria.isCheckProduct()){
			List<StrategyInfo> result = new ArrayList<StrategyInfo>();
			List<StrategyInfo> list = (List<StrategyInfo>)jsonPaging.getList();
			if (null!=list && !list.isEmpty()){
				for (StrategyInfo info: list){
					//设置ifa信息
					try {
						info.setIfa(memberBaseService.findIfaMember(info.getCreator().getId()));
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
					result.add(info);
				}
			}
			jsonPaging.setList(result);
//		}
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
		deleteStrategyAllocation(info.getId(), null);
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
    	String[] idArrStrings = toMembers.split(",");
    	clearWebViewDetail(webView.getId(), type);
        if (idArrStrings!=null && idArrStrings.length>0){
        	for(int k=0;k<idArrStrings.length;k++){
        		String id = idArrStrings[k].toString();
        		if(id!=null && id.length()>0){
        			MemberBase member = memberBaseService.findById(id);
        			if (null!=member && null!=member.getIconUrl()){
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
    
    /**
     * 保存推送权限明细
     * @author michael
     * @param webView
     * @param type
     * @param toMembers
     */
    public void saveWebPushDetail(WebPush webPush, String type, String toMembers){
    	String[] idArrStrings = toMembers.split(",");
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
    	saveWebViewDetail(result, CommonConstantsWeb.WEB_VIEW_DETAIL_TYPE_BUDDY, buddies);
       	saveWebViewDetail(result, CommonConstantsWeb.WEB_VIEW_DETAIL_TYPE_CLIENT, clients);
       	saveWebViewDetail(result, CommonConstantsWeb.WEB_VIEW_DETAIL_TYPE_PROSPECT, prospects);
       	saveWebViewDetail(result, CommonConstantsWeb.WEB_VIEW_DETAIL_TYPE_TEAM, colleagues);
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

	@Override
	public List<StrategyAllocationVO> findStrategyAllocation(String strategyId,String langCode) {
		String sql=" select r.*,s.name_"+langCode+" from strategy_allocation r left join sys_param_config s on r.item_code=s.config_code where r.strategy_id=?";
		String[] params=new String[1];
		params[0]=strategyId;
		List reList=this.springJdbcQueryManager.springJdbcQueryForList(sql, params);
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
	 * @param type 分配方法,G:Geographical,S:Sector,F:Funds Type
	 * @return
	 */
	public void deleteStrategyAllocation(String strategyId, String type){
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder(" delete from StrategyAllocation l where l.strategy.id=? ");
		params.add(strategyId);
		if(null != type && !"".equals(type)){
			hql.append(" and l.methodType=?");
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
     * @param type 分配类型
     * @param level 分配层级
     * @param codes 配置编码
     * @param weights 配置编码对应的比例
     */
    public void saveStrategyAllocation(StrategyInfo info, String type, String level, String codes, String weights){
    	if (StrUtils.getString(type).length()>0 && StrUtils.getString(codes).length()>0 
    			&& StrUtils.getString(weights).length()>0){
	    	String[] codeArr = codes.split(",");
	    	String[] weightArr = weights.split(",");
	        if (codeArr.length==weightArr.length && weightArr.length>0){
	        	for(int k=0;k<codeArr.length;k++){
	        		if (StrUtils.getString(codeArr[k]).length()>0){
		        		StrategyAllocation allocation = new StrategyAllocation();
		        		allocation.setStrategy(info);
		        		allocation.setType("fund");
		        		allocation.setMethodType(type);
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
     * 获取策略详情
     * @author mqzou 2017-06-02
     * @param id
     * @param langCode
     * @return
     */
    @Override
    public StrategyDetailVO findStrategyDetail(String id, String langCode) {
    	StrategyInfo info=findById(id);
    	if(null!=info){
    		StrategyDetailVO vo=new StrategyDetailVO(info);
    		String geo=getStrategyAllocationItems(id, CommonConstantsWeb.WEB_ALLOCATION_METHOD_GEOGRAPHICAL, 2);
			String geoAllocation=sysParamService.findNameByCode(geo, langCode);
			vo.setGeoAllocation(geoAllocation);
			String set=getStrategyAllocationItems(id, CommonConstantsWeb.WEB_ALLOCATION_METHOD_SECTOR, 2);
			String sector=sysParamService.findNameByCode(set, langCode);
			vo.setSector(sector);
			String creatorName=getCommonMemberName(info.getCreator().getId(), langCode, "2");
			vo.setCreator(creatorName);
    		return vo;
    	}
    	return null;
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
    public List<StrategyAllocationVO> findStrategyAllocationData(String strategyId, String langCode, String methodType) {
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
    
}
