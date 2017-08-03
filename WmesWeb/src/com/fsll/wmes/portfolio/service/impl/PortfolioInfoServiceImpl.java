package com.fsll.wmes.portfolio.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.inject.spi.Bean;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PropertyUtils;
import com.fsll.core.CoreConstants;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.crm.vo.ClientsPortfolioVO;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.PortfolioInfo;
//import com.fsll.wmes.entity.PortfolioProduct;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.IfaMigrateHist;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.PortfolioInfo;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.PortfolioArena;
import com.fsll.wmes.entity.PortfolioArenaCount;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioHoldProduct;
import com.fsll.wmes.entity.PortfolioInfoAip;
import com.fsll.wmes.entity.PortfolioInfoProduct;
import com.fsll.wmes.entity.PortfolioInfoProductDetail;
import com.fsll.wmes.entity.WebPush;
import com.fsll.wmes.entity.WebPushDetail;
import com.fsll.wmes.entity.WebView;
import com.fsll.wmes.entity.WebViewDetail;
import com.fsll.wmes.ifa.service.IfaMigrateHistService;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.portfolio.service.PortfolioHoldService;
import com.fsll.wmes.portfolio.service.PortfolioInfoService;
import com.fsll.wmes.portfolio.vo.CriteriaVO;
import com.fsll.wmes.portfolio.vo.PortfolioInfoVO;
import com.fsll.wmes.portfolio.vo.PortfolioWebPushVO;
import com.fsll.wmes.portfolio.vo.PortfolioWebViewVO;

@Service("portfolioInfoService")
//@Transactional
public class PortfolioInfoServiceImpl extends BaseService implements PortfolioInfoService {

	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
	private IfaMigrateHistService ifaMigrateHistService;
	
	@Autowired
	private PortfolioHoldService portfolioHoldService;
	/**
	 * 获取内容列表
	 */
	@Override
	public JsonPaging findAll(JsonPaging jsonPaging, String keyword) {
		// TODO Auto-generated method stub
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder(" from PortfolioInfo l where 1=1 ");
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
		StringBuilder hql = new StringBuilder(" from PortfolioInfo l where 1=1 ");
		if(null != criteria.getUserId() && !"".equals(criteria.getUserId())){
			hql.append(" and creator.id=?");
			params.add(criteria.getUserId());
		}
		if(null != criteria.getKeyword() && !"".equals(criteria.getKeyword())){
			hql.append(" and ( l.portfolioName like ? )");
			params.add("%"+criteria.getKeyword()+"%");
		}
//		if(null != sector && !"".equals(sector)){
//			hql.append(" and ( l.sector like ? )");
//			params.add("%"+sector+"%");
//		}
		
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
		if(null != criteria.getStartDate() && !"".equals(criteria.getStartDate())){
			hql.append(" and lastUpdate>=?");
			params.add(DateUtil.StringToDate(criteria.getStartDate(),CoreConstants.DATE_FORMAT));
		}
		if(null != criteria.getEndDate() && !"".equals(criteria.getEndDate())){
			hql.append(" and lastUpdate<=?");
			params.add(DateUtil.StringToDate(criteria.getEndDate(),CoreConstants.DATE_FORMAT));
		}
//		if(null != criteria.getStatus() && !"".equals(criteria.getStatus())){
//			hql.append(" and status=?");
//			params.add(criteria.getStatus());
//		}
		
		if(null!=criteria.getOrderBy() && !"".equals(criteria.getOrderBy())){
			hql.append(" order by "+criteria.getOrderBy());
		}else{//默认排序
			hql.append(" order by lastUpdate desc ");
		}
		
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		return jsonPaging;
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
		String hql = " from FundInfo t where t.id in (select i.id from PortfolioInfoProduct s,ProductInfo i where s.product.id=i.id and i.type='fund' and s.portfolio.id=?) ";
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
		String hql = "from WebView t where t.relateId =? and t.moduleType='"+CommonConstantsWeb.WEB_VIEW_MODULE_PORTFOLIO_INFO+"' ";
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
		String hql = " from WebPush t where t.relateId =? and t.moduleType='"+CommonConstantsWeb.WEB_PUSH_MODULE_PORTFOLIO_INFO+"' ";
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
	public PortfolioInfo findById(String id) {
		// TODO Auto-generated method stub
		PortfolioInfo obj = (PortfolioInfo) this.baseDao.get(PortfolioInfo.class, id);
		PortfolioArenaCount count=findPortfolioCountById(id);
		if (null!=count && null!=count.getViews())
			obj.setClick(count.getViews());
		else {
			obj.setClick(0);
		}
		return obj;
	}
	
	/**
	 * @author mqzou
	 * @date 2016-11-15
	 * @param id
	 */
	@Override
	public PortfolioArenaCount findPortfolioCountById(String id) {
		// TODO Auto-generated method stub
		PortfolioArenaCount obj = (PortfolioArenaCount) this.baseDao.get(PortfolioArenaCount.class, id);
		return obj;
	}
	
	/**
	 * 删除投资组合
	 * @author michael
	 */
	public void delete(PortfolioInfo info) {
		this.baseDao.delete(info);
	}
	

	/**
	 * 保存投资组合
	 * @author michael
	 * @param info
	 * @return
	 */
	public PortfolioInfo saveOrUpdate(PortfolioInfo info){
		return (PortfolioInfo)this.baseDao.saveOrUpdate(info);
	}


//	/**
//	 * 保存投资组合的产品
//	 * @author michael
//	 * @param strategyInfo
//	 * @param fundId
//	 * @return
//	 */
//	public PortfolioProduct saveOrUpdate(PortfolioInfo strategyInfo, String fundId){
//		ProductInfo productInfo = (ProductInfo)this.baseDao.get(ProductInfo.class, fundId);
//		if (null==productInfo || null==productInfo.getId() || "".equals(productInfo.getId())){
//			productInfo = new ProductInfo();
//			productInfo.setId(fundId);
//			productInfo.setType("fund");
//			productInfo.setTableNameSql("fund_info");
//			productInfo.setTableNameHql("fundInfo");
//			productInfo.setLastUpdate(new Date());
//			productInfo.setCreateTime(new Date());
//			productInfo.setIsValid("1");
//			
//			productInfo = (ProductInfo)this.baseDao.saveOrUpdate(productInfo);
//		}
//		
//		PortfolioProduct product = new PortfolioProduct();
//		product.setPortfolio(strategyInfo);
//		product.setProduct(productInfo);
//		
//		return (PortfolioProduct)this.baseDao.saveOrUpdate(product);
//	}
	
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
	 * 获取Ifa管理的投资组合
	 * @author zxtan
	 * @date 2016-09-06
	 */
	@Override
	public JsonPaging findPortfolioListForIfa(JsonPaging jsonPaging,String ifaMemberId, String customerMemberId) {
		// TODO Auto-generated method stub
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder(" ");
		hql.append(" from PortfolioHold l ");
		hql.append("inner join MemberIfa m on l.ifa.id=m.id ");
		hql.append(" where  l.member.id=? and m.member.id=? ");//
		params.add(customerMemberId);
		params.add(ifaMemberId);
				
		jsonPaging.setOrder(" desc");
		jsonPaging.setSort(" l.createTime");
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging , false);

		return jsonPaging;
	}

	/**
	 * 根据组合Id获取组合定投信息
	 * @author wwluo
	 * @param portfolioId PortfolioInfo ID
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PortfolioInfoAip findAipByPortfolioId(String portfolioId) {
		PortfolioInfoAip portfolioInfoAip = null;
		if (StringUtils.isNotBlank(portfolioId)) {
			StringBuffer hql = new StringBuffer(" from PortfolioInfoAip where isValid=1 and portfolio_id=?");
			List params = new ArrayList();
			params.add(portfolioId);
			List<PortfolioInfoAip> portfolioInfoAips = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(portfolioInfoAips !=null && !portfolioInfoAips.isEmpty()){
				portfolioInfoAip = portfolioInfoAips.get(0);
			}
		}
		return portfolioInfoAip;
	}

	/**
	 * 根据Id获取组合定投信息
	 * @author wwluo
	 * @param portfolioId PortfolioInfo ID
	 * @return
	 */
	@Override
	public PortfolioInfoAip findPortfolioInfoAipById(String id) {
		PortfolioInfoAip aip = new PortfolioInfoAip();
		if (StringUtils.isNotBlank(id)) {
			aip = (PortfolioInfoAip) this.baseDao.get(PortfolioInfoAip.class, id);
		}
		return aip;
	}

	/**
	 * 删除组合下的产品
	 * @author wwluo
	 * @param id PortfolioInfo ID
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void deleteProduct(String id) {
		if (StringUtils.isNotBlank(id)) {
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" PortfolioInfoProduct p" +
					" WHERE" +
					" p.portfolio.id=?");
			List params = new ArrayList();
			params.add(id);
			List<PortfolioInfoProduct> list = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(list != null && !list.isEmpty()){
				for (PortfolioInfoProduct portfolioInfoProduct : list) {
					this.baseDao.delete(portfolioInfoProduct);
				}
			}
		}
	}

	/**
	 * 获取我的客户的投资组合
	 * @author wwluo
	 * @date 2016-09-14
	 */
	@Override
	public JsonPaging getPortfolio(JsonPaging jsonPaging, MemberBase loginUser,
			String period,String fromDate,String toDate, String riskLevel, String totalReturn, String keyWord,String status,String langCode,String baseCurrency) {
		if(loginUser != null){
			List<Object> params = new ArrayList<Object>();
			StringBuilder hql=new StringBuilder();
			hql.append(" select  r,z");//select  r,z,l.finishStatus
			hql.append(" from PortfolioHold r left join MemberBase m on m.id=r.member.id");
			hql.append(" left join MemberIfa f on r.ifa.id=f.id");
			hql.append(" left join PortfolioInfo c on c.id=r.portfolio.id");//left join PortfolioInfo c on m.id=c.member.id and f.id=c.memberIfa.id
			//hql.append(" left join OrderPlan l on r.id=l.portfolioHold.id");
			hql.append(" left join CrmCustomer z on z.member.id=r.member.id and z.ifa.id=f.id");
			hql.append("  where 1=1");
			//过滤   期间
			if (StringUtils.isNotBlank(period)) {
				Calendar calendarc = Calendar.getInstance();
				switch (Integer.parseInt(period)) {
					case 1:calendarc.add(Calendar.MONTH, -1);
						break;
					case 3:calendarc.add(Calendar.MONTH, -3);
						break;
					case 6:calendarc.add(Calendar.MONTH, -6);
						break;
					case 12:calendarc.add(Calendar.YEAR, -1);
						break;
					case 36:calendarc.add(Calendar.YEAR, -3);
						break;
				}
				hql.append(" and r.createTime between ? and CURDATE()");
				params.add(calendarc.getTime());
			}
			//过滤  时间
			if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
				hql.append(" and r.createTime between ? and ?");
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				try {
					params.add(dateFormat.parse(fromDate));
					params.add(dateFormat.parse(toDate));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			//过滤  等级
			if (StringUtils.isNotBlank(riskLevel)) {
				hql.append(" and r.portfolio.riskLevel=?");
				params.add(Integer.parseInt(riskLevel));
			}
			//过滤  回报率
			if (StringUtils.isNotBlank(totalReturn)) {
				switch (Integer.parseInt(totalReturn)) {
				case 0:hql.append(" and r.totalReturnRate<0");
					break;
				case 1:hql.append(" and r.totalReturnRate between 0 and 0.1");
					break;
				case 2:hql.append(" and r.totalReturnRate between 0.1 and 0.2");
					break;
				case 3:hql.append(" and r.totalReturnRate between 0.2 and 0.3");
					break;
				case 4:hql.append(" and r.totalReturnRate between 0.3 and 0.4");
					break;
				case 5:hql.append(" and r.totalReturnRate > 0.5");
					break;
				}
			}
			//过滤   关键字
			if (StringUtils.isNotBlank(keyWord)) {
				hql.append(" and c.nickName like ?");
				params.add("%"+keyWord+"%");
			}
			/*if(StringUtils.isNoneBlank(status)){
				hql.append(" and (1=0");
				String[] statu=status.split(",");
				for (String string : statu) {
					hql.append(" or l.finishStatus=?");
					params.add(string);
				}
				hql.append(" and l.finishStatus=?");
				params.add(status);
				hql.append(")");
			}*/
			hql.append(" and f.member=?");
			params.add(loginUser);
			//hql.append(" group by r");
			jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
			
			List<ClientsPortfolioVO> list=new ArrayList<ClientsPortfolioVO>();
			if (null!=jsonPaging.getList()){
				Iterator it=jsonPaging.getList().iterator();
				while (it.hasNext()) {
					Object[] objects = (Object[]) it.next();
					PortfolioHold hold=(PortfolioHold)objects[0];
					CrmCustomer customer=(CrmCustomer)objects[1];
				//	Object statu=objects[2];
				    ClientsPortfolioVO vo=new ClientsPortfolioVO();
				    boolean result=portfolioHoldService.checkPortfolioAip(hold.getId());
				    if(result)
				    	vo.setAipFlag("1");
				    else {
						vo.setAipFlag("0");
					}
				    vo.setClientId(null!=customer?customer.getId():"");
				    String nickName=getCommonMemberName(customer.getMember().getId(), langCode, "2");
				    vo.setClientName(nickName);
				    vo.setId(hold.getId());
				    vo.setLastUpdate(DateUtil.getDateStr(hold.getLastUpdate()));
				    vo.setPortfolioName(hold.getPortfolioName());
				    double assets= portfolioHoldService.findPortfolioAuM(hold.getId(), baseCurrency);
				    vo.setTotalAssets(String.valueOf(assets));
				    double returnValue=null!=hold.getTotalReturnValue()?hold.getTotalReturnValue():0;
				    vo.setTotalReturn(String.valueOf(returnValue));
				    double returnRate=null!=hold.getTotalReturnRate()?hold.getTotalReturnRate()*100:0;
				    vo.setTotalReturnRate(String.valueOf(returnRate));
				    vo.setBaseCurrency(baseCurrency);
				   /* if(null!=statu){
				    	vo.setStatus(PropertyUtils.getPropertyValue(langCode,"order.plan.list.finish.status."+statu,null));
				    }else {
						vo.setStatus("");
					}*/
				    list.add(vo);
				}
				jsonPaging.setList(list);
			}
		}
		return jsonPaging;
	}

	/**
	 * 删除组合下的产品
	 * @author wwluo
	 * @param id PortfolioInfo ID
	 * @return
	 */
	@Override
	public void deleteProductDetail(String id) {
		if (StringUtils.isNotBlank(id)) {
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" PortfolioInfoProductDetail p" +
					" WHERE" +
					" p.portfolio.id=?");
			List params = new ArrayList();
			params.add(id);
			List<PortfolioInfoProductDetail> list = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(list != null && !list.isEmpty()){
				for (PortfolioInfoProductDetail detail : list) {
					this.baseDao.delete(detail);
				}
			}
		}
	}
	
	/**
	 * 根据ifa的memberId查找客户组合
	 * @author michael
	 * @param memberId
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public List<PortfolioInfo> findPortfolioInfoByIfa(String memberId) {
		String hql=" from PortfolioInfo r where r.memberIfa.member.id=? and r.isValid='1' ";
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		List<PortfolioInfo> list= (List<PortfolioInfo>)this.baseDao.find(hql, params.toArray(), false);
		return list;
	}
	
	/**
	 * 检测客户组合是否存在
	 * @author michael
	 * @param ifaMemberId
	 * @param memberId
	 * @return
	 */
	public boolean checkIfExistPortfolio(String ifaMemberId, String memberId) {
		String hql=" from PortfolioInfo r where r.isValid='1' and r.memberIfa.member.id=? and r.member.id=? ";
		List<Object> params=new ArrayList<Object>();
		params.add(ifaMemberId);
		params.add(memberId);
		List list=this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			return true;
		}
		return false;
	}
	
	/**
	 * 修改IFA的客户组合到另一IFA
	 * @author michael
	 * @date 2017-3-1
	 * @param fromMemberId 源IFA
	 * @param toMemberId 目标IFA
	 * @return Boolean
	 */
	public Boolean migrateIfaPortfolio(String fromMemberId,String toMemberId,MemberBase createBy) {
		List<PortfolioInfo> list = findPortfolioInfoByIfa(fromMemberId);
		if (null!=list && !list.isEmpty()){
			MemberIfa ifa = memberBaseService.findIfaMember(toMemberId);
			for (PortfolioInfo f: list){
				boolean status = false;
				//客户类型client_type有没要求，已存在的不加？
				if (!checkIfExistPortfolio(toMemberId, f.getMemberIfa().getMember().getId())){
					try {
	
						//更新方式
						f.setMemberIfa(ifa);
						this.baseDao.update(f);
						
						//复制方式
	//					PortfolioInfo nCustomer = new PortfolioInfo();
	//					BeanUtils.copyProperties(f, nCustomer);
	//					nCustomer.setId(null);
	//					nCustomer.setCreateTime(new Date());
	//					nCustomer.setLastUpdate(new Date());
	//					this.baseDao.saveOrUpdate(nCustomer);
	//					
	//					//旧记录设为冻结状态
	//					f.setIsValid("0");
	//					this.baseDao.update(f);
						status = true;
		            } catch (Exception e) {
		                // TODO: handle exception
		            }
		            
					//保存日志
					IfaMigrateHist hist = new IfaMigrateHist();
					hist.setCreateBy(createBy);
					hist.setCreateTime(new Date());
					hist.setFromMember(memberBaseService.findById(fromMemberId));
					hist.setToMember(ifa.getMember());
					hist.setCusMember(f.getMember());
					hist.setDataType("PortfolioInfo");
					hist.setStatus(status?"1":"0");
					ifaMigrateHistService.saveOrUpdate(hist);
				}
				
			}
			return true;
		}
		return false;
	}
}
