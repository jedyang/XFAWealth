package com.fsll.app.ifa.client.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fsll.app.ifa.client.service.AppIndexInfoService;
import com.fsll.app.ifa.client.vo.AppClientCountVO;
import com.fsll.app.ifa.client.vo.AppClientItemVO;
import com.fsll.app.ifa.client.vo.AppCrmCustomerGroupVO;
import com.fsll.app.ifa.client.vo.AppEventItemVO;
import com.fsll.app.ifa.client.vo.AppEventVO;
import com.fsll.app.ifa.client.vo.AppPieChartItemVO;
import com.fsll.app.ifa.client.vo.AppProspectEventVO;
import com.fsll.app.ifa.client.vo.ClientSearchVO;
import com.fsll.app.investor.me.vo.AppProductInfoVO;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.wmes.entity.CrmCustomerGroup;
import com.fsll.wmes.entity.CrmSchedule;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioHoldProduct;
import com.fsll.wmes.entity.ProductInfo;

@Service("appIfaClientIndexService")
public class AppIndexInfoServiceImpl extends BaseService implements
		AppIndexInfoService {

	/**
	 * 获取IFA的分组列表
	 * @author zxtan
	 * @date 2017-03-16
	 */
	public List<AppCrmCustomerGroupVO> findMyClientGroupList(String memberId){
		List<AppCrmCustomerGroupVO> voList = new ArrayList<AppCrmCustomerGroupVO>();
		StringBuilder hql = new StringBuilder();
		hql.append("from CrmCustomerGroup m ");
		hql.append(" inner join MemberIfa i on i.id=m.ifa.id ");
		hql.append(" where m.isValid='1' and i.member.id=? ");
		hql.append(" order by m.groupName ");
		List<Object> params = new ArrayList<Object>();
		params.add(memberId);
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				CrmCustomerGroup group = (CrmCustomerGroup) objs[0];
				AppCrmCustomerGroupVO vo = new AppCrmCustomerGroupVO();
				vo.setGroupId(group.getId());
				vo.setGroupName(group.getGroupName());
				voList.add(vo);
			}
		}

		return voList;
	}

	/**
	 * 获取IFA 的客户首页信息
	 * @author zxtan
	 * @date 2017-03-16
	 */
	public AppClientCountVO findClientCount(String memberId, String toCurrency) {
		
		AppClientCountVO vo = new AppClientCountVO();
		int totalNum =0;
		int profitNum =0;
		int lossNum = 0;
		int notYetInvestNum =0;
		
		//统计总客户数
		StringBuilder hql = new StringBuilder();
		hql.append(" from CrmCustomer c inner join MemberIfa i on i.id = c.ifa.id ");
		hql.append(" inner join MemberBase b on b.id=i.member.id ");
		hql.append(" where b.id=? and c.clientType='1' ");
		List<Object> params = new ArrayList<Object>();
		params.add(memberId);
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			totalNum = list.size();		
		}
		
		
		//统计盈亏客户数
		hql = new StringBuilder();
		hql.append("select c.id,sum(get_exchange_rate(h.baseCurrency,?,h.totalReturnValue)) ");
		hql.append(" from CrmCustomer c inner join MemberIfa i on i.id = c.ifa.id ");
		hql.append(" inner join MemberBase b on b.id=i.member.id ");
		hql.append(" inner join PortfolioHold h on c.member.id=h.member.id and h.ifa.id=c.ifa.id ");
		hql.append(" where b.id=? and c.clientType='1' ");
		hql.append(" group by c.id");
		params = new ArrayList<Object>();
		params.add(toCurrency);
		params.add(memberId);
		list = baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				double totalReturnValue = objs[1]==null?0:Double.parseDouble(objs[1].toString());
				if(totalReturnValue <0){
					lossNum++;
				}else {
					profitNum++;
				}
			}			
		}
		
		//统计未投资客户数
		hql = new StringBuilder();
		hql.append(" from CrmCustomer c inner join MemberIfa i on i.id = c.ifa.id ");
		hql.append(" inner join MemberBase b on b.id=i.member.id ");
		hql.append(" where b.id=? and c.clientType='1' ");
		hql.append(" and not exists (select hp.id from PortfolioHoldProduct hp inner join PortfolioHold h on hp.portfolioHold.id=h.id where h.member.id=c.member.id and h.ifa.id=c.ifa.id ) ");
		params = new ArrayList<Object>();
		params.add(memberId);
		list = baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			notYetInvestNum = list.size();			
		}

		vo.setTotalNum(String.valueOf(totalNum));
		vo.setProfitNum(String.valueOf(profitNum));
		vo.setLossNum(String.valueOf(lossNum));
		vo.setNotYetInvestNum(String.valueOf(notYetInvestNum));
		
		return vo;
	}
	
	/**
	 * 获取IFA的客户列表
	 * @author zxtan
	 * @date 2017-03-16
	 * @param clientSearchVO
	 * @return
	 */
	public List<AppClientItemVO> findMyClientList(ClientSearchVO clientSearchVO,Date startDate,Date endDate) {
		
		int days=0;
		try {
			 days=DateUtil.daysBetween(startDate, endDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sql=" CALL pro_getclients(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		List<Object> params=new ArrayList<Object>();
		params.add(clientSearchVO.getIfaId());
		params.add(clientSearchVO.getClientId());
		params.add(clientSearchVO.getClientName());
		params.add(clientSearchVO.getMinReturnRate());
		params.add(clientSearchVO.getMaxReturnRate());
		params.add(clientSearchVO.getMinAuM());
		params.add(clientSearchVO.getMaxAuM());
		params.add(clientSearchVO.getClientGroup());
		params.add(clientSearchVO.getCurrency());
		params.add(clientSearchVO.getStatistic());
		
		params.add(clientSearchVO.getOrder());
		params.add(clientSearchVO.getSort());
		params.add(clientSearchVO.getPage());
		params.add(clientSearchVO.getRows());
		params.add(clientSearchVO.getReCondition());
		List resultList=this.springJdbcQueryManager.springJdbcQueryForList(sql, params.toArray());
		List<AppClientItemVO> list=new ArrayList<AppClientItemVO>();
		if(null!=resultList && !resultList.isEmpty()){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				HashMap map = (HashMap) it.next();
				AppClientItemVO vo=new AppClientItemVO();
				String id=getMapValue(map, "id");
				String memberId = getMapValue(map, "member_id");
				String nickName = getMapValue(map, "nick_name");
				String email = getMapValue(map, "email");
				String mobileCode = getMapValue(map, "mobile_code");
				String mobileNumber = getMapValue(map, "mobile_number");
				String iconUrl = getMapValue(map, "icon_url");
				String createTime = getMapValue(map, "create_time");
				String totalRate = getMapValue(map, "total_return_rate");
				String totalValue = getMapValue(map, "total_return_value");
				String totalAuM = getMapValue(map, "totalAuM");
				
				try {
					totalValue = getFormatNum(Double.parseDouble(totalValue),clientSearchVO.getCurrency());
					totalAuM = getFormatNum(Double.parseDouble(totalAuM),clientSearchVO.getCurrency());
				} catch (Exception e) {
					// TODO: handle exception
					totalValue = "0.00";
					totalAuM = "0.00";
				}
				
				String riskLevel = findInvestorMaxRiskLevel(memberId, clientSearchVO.getIfaId());
				
				vo.setId(id);
				vo.setMemberId(memberId);
				vo.setNickName(nickName);
				vo.setEmail(email);
				vo.setMobileCode(mobileCode);
				vo.setMobileNumber(mobileNumber);
				vo.setIconUrl(PageHelper.getUserHeadUrlForWS(iconUrl, null));
				vo.setRiskLevel(riskLevel);
				
				boolean result = checkFacaOrCies(memberId, "faca");
				if(result){
					vo.setFaca("1");
				}else {
					vo.setFaca("0");
				}
				result = checkFacaOrCies(memberId, "cies");
				if(result){
					vo.setCies("1");
				}else {
					vo.setCies("0");
				}
				
				String clientId = findBirthDayCustomer(clientSearchVO.getIfaId(), memberId, days);
				if(null != clientId && !"null".equalsIgnoreCase(clientId)){
					vo.setIsBirthDay("1");
				}else {
					vo.setIsBirthDay("0");
				}
				
				clientId = findSchedultCustomer(clientSearchVO.getIfaId(), memberId, startDate, endDate);
				if(null != clientId && !"null".equalsIgnoreCase(clientId)){
					vo.setHasSchedule("1");
				}else {
					vo.setHasSchedule("0");
				}
				
				vo.setTotalReturnRate(null!=totalRate && !"".equals(totalRate)? getFormatNumByPer(Double.parseDouble(totalRate)):"0");
				vo.setTotalReturnValue(totalValue);
				vo.setTotalAssets(totalAuM);
				vo.setCurrency(clientSearchVO.getCurrency());
				if(null!=createTime && !"".equals(createTime)){
					Date createDate=DateUtil.getDate(createTime);
					try {
						int investDays=DateUtil.daysBetween(createDate, DateUtil.getCurDate());
						vo.setInvestDays(String.valueOf(investDays));
					} catch (ParseException e) {
						vo.setInvestDays("0");
						e.printStackTrace();
					}					
				}else {
					vo.setInvestDays("0");
				}
				
				List<AppPieChartItemVO> allocationList = findProductAllocationList(memberId, clientSearchVO.getIfaId(), clientSearchVO.getCurrency());
				vo.setAllocationList(allocationList);
				
				list.add(vo);
				
			}
		}
		
		return list;

	}
	
	
	/**
	 * 检查投资人是否FACA或CIES
	 * @author zxtan
	 * @date 2017-03-16
	 * @param memberId
	 * @return
	 */
	private boolean checkFacaOrCies(String memberId,String type) {
		String hql=" from InvestorAccount r where r.member.id=? and r."+type+"='1' and r.isValid='1'";
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		List list=this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			return true;
		}
		return false;
	}
	
	/**
	 * 获取投资人rpq中的最高风险系数
	 * @author zxtan
	 * @date 2017-03-16
	 * @param memberId
	 * @param ifaId
	 * @return
	 */
	private String findInvestorMaxRiskLevel(String memberId,String ifaId) {
		String hql="select max(r.riskLevel) from RpqExam r inner join InvestorAccount a on r.relateId=a.id  where a.member.id=? and a.ifa.id=? and r.isValid='1'";
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		params.add(ifaId);
		List list=this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			Object object=(Object)list.get(0);
			if(null!=object)
				return object.toString();
		}
		return "";
	}
	
	/**
	 * 获取IFA个人信息
	 * @author zxtan
	 * @date 2017-03-16
	 * @param memberId	
	 * */
	public MemberIfa findMemberIfa(String memberId) {
		String hql = "from MemberIfa t where t.member.id=? ";
		List<MemberIfa> list = this.baseDao.find(hql, new String[]{memberId}, false);
		if(null != list && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	
	

	
	/**
	 * 检查客户是是否生日
	 * @author zxtan
	 * @date 2017-03-16
	 * @param ifaId
	 * @param memberId
	 * @param days
	 * @return
	 */
	public String findBirthDayCustomer(String ifaId, String memberId, int days) {
		String sql="call pro_getclientbirthdayremind(?,?,?,?)";
		List<Object> params=new ArrayList<Object>();
		params.add(ifaId);
		params.add(memberId);
		params.add(days);
		params.add("1");
		List list=this.springJdbcQueryManager.springJdbcQueryForList(sql, params.toArray());
		String clientId = "";
		if(null!=list && !list.isEmpty()){
			Iterator it=list.iterator();
			while (it.hasNext()) {
				HashMap map = (HashMap) it.next();
				clientId+="'"+map.get("member_id")+"',";
				
			}
			if(clientId.endsWith(",")){
				clientId=clientId.substring(0,clientId.length()-1);
			}
		}else {
			clientId = "null";
		}
		return clientId;
	}
	
	/**
	 * 检查客户是否有日程
	 * @author zxtan
	 * @date 2017-03-16
	 * @param ifaId
	 * @param memberId
	 * @return
	 */
	public String findSchedultCustomer(String ifaId, String memberId, Date startDate, Date endDate) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from CrmSchedule r where r.isValid='1' ");
		List<Object> params=new ArrayList<Object>();
		if(null!=ifaId && !"".equals(ifaId)){
			hql.append(" and r.customer.ifa.id=?");
			params.add(ifaId);
		}
		if(null!=memberId && !"".equals(memberId)){
			hql.append(" and r.customer.member.id=?");
			params.add(memberId);
		}
		if(null!=startDate ){
		    hql.append(" and (r.startDate between ? and ? or r.endDate between ? and ? or ? between r.startDate and r.endDate  )");
			params.add(startDate);
			params.add(endDate);
			params.add(startDate);
			params.add(endDate);
			params.add(startDate);
		}
		List list=this.baseDao.find(hql.toString(), params.toArray(), false);
		String clientId = "";
		if(null!=list && !list.isEmpty()){
			Iterator it=list.iterator();
			while (it.hasNext()) {
				CrmSchedule schedule = (CrmSchedule) it.next();
				clientId+="'"+schedule.getCustomer().getMember().getId()+"',";
				
			}
			if(clientId.endsWith(",")){
				clientId=clientId.substring(0,clientId.length()-1);
			}
			
		}else {
			clientId = "null";
		}
		return clientId;
	}
	
	
	/**
	 * 得到资产分析的产品配置信息
	 * @author zxtan
	 * @date 2017-03-17
	 * @param memberId 会员Id
	 * @param langCode 语言
	 * @param toCurrency 货币
	 * @return
	 */
	public List<AppPieChartItemVO> findProductAllocationList(String memberId,String ifaId,String toCurrency){
		List<AppPieChartItemVO>  voList = new ArrayList<AppPieChartItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldProduct m ");
		hql.append(" inner join PortfolioHold h on h.id=m.portfolioHold.id ");
		hql.append(" inner join ProductInfo f on f.id=m.product.id ");		
		hql.append(" where h.member.id=? and h.ifa.id=? ");
		
		List params = new ArrayList();
		params.add(memberId);
		params.add(ifaId);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		
		
		//市值总额
		double fundTotal = 0;
		double bondTotal = 0;
		double stockTotal = 0;
		double insureTotal = 0;
		
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			PortfolioHoldProduct product = (PortfolioHoldProduct) objs[0];
//			PortfolioHold hold = (PortfolioHold) objs[1];					
			ProductInfo info = (ProductInfo) objs[2];
			String productType = info.getType();
			String productId = product.getProduct().getId();
						
			AppProductInfoVO productInfoVO = findProductInfo(productId, "en");
			Double rate = getExchangeRate(product.getBaseCurrency(), toCurrency);
			if(null != rate){
				double lastNav = productInfoVO.getLastNav()==null?0:productInfoVO.getLastNav();
				double unit = product.getHoldingUnit()==null?0:product.getHoldingUnit();
				double totalMarketValue = lastNav* unit * rate; 
								
				if("fund".equalsIgnoreCase(productType)){
					fundTotal += totalMarketValue;
				}else if ("bond".equalsIgnoreCase(productType)) {
					bondTotal += totalMarketValue;
				}else if ("stock".equalsIgnoreCase(productType)) {
					stockTotal += totalMarketValue;
				}else if ("insure".equalsIgnoreCase(productType)) {
					insureTotal += totalMarketValue;
				}
			}						
		}
			
		double total = fundTotal+bondTotal+stockTotal+insureTotal;
		
		if(fundTotal>0){
			AppPieChartItemVO vo = genPipeChartItem("fund", fundTotal, total);
			voList.add(vo);
		}
		
		if(bondTotal>0){	
			AppPieChartItemVO vo = genPipeChartItem("bond", bondTotal, total);
			voList.add(vo);
		}
		
		if(stockTotal>0){
			AppPieChartItemVO vo = genPipeChartItem("stock", stockTotal, total);
			voList.add(vo);
		}

		if(insureTotal>0){
			AppPieChartItemVO vo = genPipeChartItem("insure", insureTotal, total);
			voList.add(vo);
		}
		
		return voList;
	}
	
	/**
	 * 生成数据项
	 * @author zxtan
	 * @date 2017-03-17
	 * @param itemName
	 * @param itemValue
	 * @param total
	 * @return
	 */
	private AppPieChartItemVO genPipeChartItem(String itemName,double itemValue,double total){
		AppPieChartItemVO chartItemVO = new AppPieChartItemVO();
		chartItemVO.setItemName(itemName);
		double weight = total>0?itemValue/total : 0;
		weight = new BigDecimal(weight).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
		chartItemVO.setItemWeight(String.valueOf(weight));
		return chartItemVO;
	}
	
	/**
	 * 获取IFA正式客户的事件统计
	 * @author zxtan
	 * @date 2017-03-17
	 * @param memberId
	 * @return
	 */
	public AppEventVO findExistingEventList(String memberId){		
		List<AppEventItemVO> openAccountEventList = findInvestorAccountEventList(memberId);
		List<AppEventItemVO> proposalEventList = findProposalEventList(memberId);
		List<AppEventItemVO> portfolioEventList = findPortfolioEventList(memberId);
		AppEventItemVO portfolioRemindEvent = findPortfolioReviewEvent(memberId);
		AppEventItemVO kycRemindEvent = findKycReviewEvent(memberId);
		AppEventItemVO notYetInvestEvent = findNotYetInvestEvent(memberId);
		
		AppEventVO vo = new AppEventVO();
		vo.setOpenAccountEventList(openAccountEventList);
		vo.setProposalEventList(proposalEventList);
		vo.setPortfolioEventList(portfolioEventList);
		vo.setPortfolioRemindEvent(portfolioRemindEvent);
		vo.setKycRemindEvent(kycRemindEvent);
		vo.setNotYetInvestEvent(notYetInvestEvent);
		
		return vo;
	}

	/**
	 * 获取IFA的客户开户情况统计
	 * @author zxtan
	 * @date 2017-03-17
	 * @param memberId
	 * @return
	 */
	public List<AppEventItemVO> findInvestorAccountEventList(String memberId){
		List<AppEventItemVO> voList = new ArrayList<AppEventItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" select a.openStatus,count(0) from InvestorAccount a ");
		hql.append(" inner join CrmCustomer c on c.member.id=a.member.id ");
		hql.append(" inner join MemberIfa i on i.id=a.ifa.id");
		hql.append(" where c.clientType='1' and a.openStatus !='3' and i.member.id=? ");
		hql.append(" group by a.openStatus ");
		List<Object> params = new ArrayList<Object>();
		params.add(memberId);
		List list = baseDao.find(hql.toString(), params.toArray(), false);

		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				AppEventItemVO itemVO = new AppEventItemVO();
				itemVO.setItemName(objs[0].toString());
				itemVO.setItemValue(objs[1].toString());
				
				voList.add(itemVO);
			}
		}
		return voList; 		
	} 
	
	/**
	 * 获取IFA的客户开户情况统计
	 * @author zxtan
	 * @date 2017-03-17
	 * @param memberId
	 * @return
	 */
	public List<AppEventItemVO> findProposalEventList(String memberId){
		List<AppEventItemVO> voList = new ArrayList<AppEventItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" select a.status,count(0) from CrmProposal a ");
		hql.append(" inner join MemberIfa i on i.id=a.ifaMember.id ");
		hql.append(" inner join CrmCustomer c on c.member.id=a.member.id ");
		hql.append(" where c.clientType='1' and a.status !='2' and i.member.id=? ");
		hql.append(" group by a.status ");
		List<Object> params = new ArrayList<Object>();
		params.add(memberId);
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				AppEventItemVO itemVO = new AppEventItemVO();
				itemVO.setItemName(objs[0].toString());
				itemVO.setItemValue(objs[1].toString());
				
				voList.add(itemVO);
			}
		}
		return voList; 		
	} 
	
	
	/**
	 * 获取IFA的客户开户情况统计
	 * @author zxtan
	 * @date 2017-03-17
	 * @param memberId
	 * @return
	 */
	public List<AppEventItemVO> findPortfolioEventList(String memberId){
		List<AppEventItemVO> voList = new ArrayList<AppEventItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" select a.finishStatus,count(0) from OrderPlan a ");
		hql.append(" inner join PortfolioHold h on h.id=a.portfolioHold.id ");
		hql.append(" inner join MemberIfa i on i.id=h.ifa.id ");
		hql.append(" inner join CrmCustomer c on c.member.id=h.member.id ");
		hql.append(" where c.clientType='1' and a.finishStatus !='5' and i.member.id=? ");
		hql.append(" group by a.finishStatus ");
		List<Object> params = new ArrayList<Object>();
		params.add(memberId);
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				AppEventItemVO itemVO = new AppEventItemVO();
				itemVO.setItemName(objs[0].toString());
				itemVO.setItemValue(objs[1].toString());
				
				voList.add(itemVO);
			}
		}
		return voList; 		
	} 
	
	/**
	 * 获取IFA的客户组合收益提醒统计
	 * @author zxtan
	 * @date 2017-03-17
	 * @param memberId
	 * @return
	 */
	public AppEventItemVO findPortfolioReviewEvent(String memberId) {
		MemberIfa ifa = findMemberIfa(memberId);
		if(null == ifa) return null;
		String minValue = ifa.getPortfolioCriticalValue();
		String maxValue = ifa.getPortfolioReturnValue();
		if(null == minValue || "".equals(minValue)){
			minValue = "0";
		}
		
		if(null == maxValue || "".equals(maxValue)){
			maxValue = "0";
		}
		
		AppEventItemVO vo = new AppEventItemVO();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHold h ");
		hql.append(" inner join MemberIfa i on i.id = h.ifa.id");
		hql.append(" inner join CrmCustomer c on c.member.id = h.member.id ");
		hql.append(" where c.clientType='1' and i.member.id=? ");
		hql.append(" and ( h.totalReturnRate < ? or h.totalReturnRate > ? ) ");
		List<Object> params = new ArrayList<Object>();
		params.add(memberId);
		params.add(Double.parseDouble(minValue));
		params.add(Double.parseDouble(maxValue));
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		
		vo.setItemName("review");
		vo.setItemValue(String.valueOf(list.size()));
		return vo;
	}
	
	/**
	 * 获取IFA的客户组合收益提醒统计
	 * @author zxtan
	 * @date 2017-03-17
	 * @param memberId
	 * @return
	 */
	public AppEventItemVO findKycReviewEvent(String memberId) {
		AppEventItemVO vo = new AppEventItemVO();
		StringBuilder hql = new StringBuilder();
		hql.append(" from CrmCustomer c ");
		hql.append(" inner join MemberIfa i on i.id = c.ifa.id");
		hql.append(" inner join InvestorAccount a on a.member.id=c.member.id and a.ifa.id=i.id ");
		hql.append(" inner join RpqExam e on e.relateId=a.id ");
		hql.append(" where c.clientType='1' and a.subFlag='0' and e.status='1' and i.member.id=? ");
		
		List<Object> params = new ArrayList<Object>();
		params.add(memberId);
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		
		vo.setItemName("review");
		vo.setItemValue(String.valueOf(list.size()));
		return vo;
	}
	
	/**
	 * 获取IFA的客户组合收益提醒统计
	 * @author zxtan
	 * @date 2017-03-17
	 * @param memberId
	 * @return
	 */
	public AppEventItemVO findNotYetInvestEvent(String memberId) {
		AppEventItemVO vo = new AppEventItemVO();
		StringBuilder hql = new StringBuilder();
		hql.append(" from InvestorAccount ia ");
		hql.append(" inner join MemberIfa i on i.id = ia.ifa.id");
		hql.append(" inner join CrmCustomer c on c.member.id = ia.member.id and c.ifa.id=i.id ");
		hql.append(" where c.clientType='1' and ia.openStatus='3' and i.member.id=? ");
		hql.append(" and not exists ( SELECT hd.id FROM PortfolioHoldProduct hd INNER JOIN PortfolioHold h ON h.id = hd.portfolioHold.id WHERE hd.account.id= ia.id ) ");
		List<Object> params = new ArrayList<Object>();
		params.add(memberId);
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		
		vo.setItemName("review");
		vo.setItemValue(String.valueOf(list.size()));
		return vo;
	}
	
	/**
	 * 获取IFA的潜在客户统计
	 * @author zxtan
	 * @date 2017-03-20
	 * @param memberId
	 * @return
	 */
	public AppProspectEventVO findProspectEventList(String memberId) {
		AppProspectEventVO vo = new AppProspectEventVO();
		AppEventItemVO newClientEvent = findProspectNewEvent(memberId);
		List<AppEventItemVO> contactEventList = findProspectContactEventList(memberId);
		List<AppEventItemVO> proposalEventList = findProspectProposalEventList(memberId);
		AppEventItemVO closeEvent = findProspectCloseEvent(memberId);
		
		vo.setNewClientEvent(newClientEvent);
		vo.setContactEventList(contactEventList);
		vo.setProposalEventList(proposalEventList);
		vo.setCloseEvent(closeEvent);
		
		return vo;
	}
	
	/**
	 * 获取IFA新的潜在客户数量统计
	 * @author zxtan
	 * @date 2017-03-22
	 * @param memberId
	 * @return
	 */
	private AppEventItemVO findProspectNewEvent(String memberId) {
		AppEventItemVO vo = new AppEventItemVO();
		StringBuilder hql = new StringBuilder();
		hql = new StringBuilder();
		hql.append(" from CrmCustomer c ");
		hql.append(" inner join MemberIfa i on i.id = c.ifa.id");
		hql.append(" where c.clientType='0' and c.salesStageId='sales_new' and i.member.id=? ");
		
		List<Object> params = new ArrayList<Object>();
		params.add(memberId);
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		
		vo.setItemName("new");
		vo.setItemValue(String.valueOf(list.size()));
		return vo;
	}
	
	/**
	 * 获取IFA新的潜在客户数量统计
	 * @author zxtan
	 * @date 2017-03-22
	 * @param memberId
	 * @return
	 */
	private List<AppEventItemVO> findProspectContactEventList(String memberId){
		List<AppEventItemVO> voList = new ArrayList<AppEventItemVO>();
		AppEventItemVO vo = findProspectContactEvent(memberId, "1week");
		voList.add(vo);
		vo = findProspectContactEvent(memberId, "1month");
		voList.add(vo);
		return voList;
	}
	
	/**
	 * 获取IFA新的潜在客户数量统计
	 * @author zxtan
	 * @date 2017-03-22
	 * @param memberId
	 * @return
	 */
	private AppEventItemVO findProspectContactEvent(String memberId,String period) {
		AppEventItemVO vo = new AppEventItemVO();
		StringBuilder hql = new StringBuilder();
		Date startDate = new Date();
		if("1week".equalsIgnoreCase(period)){
			startDate = DateUtil.getInternalDateByDay(DateUtil.getCurDate(), -7);
		}else {
			//1month
			startDate = DateUtil.getInternalDateByMon(DateUtil.getCurDate(), -1);
		}
		hql.append(" from CrmCustomer c ");
		hql.append(" inner join MemberIfa i on i.id = c.ifa.id");
		hql.append(" where c.clientType='0' and c.salesStageId='sales_contact' and i.member.id=? ");
		hql.append(" and not exists (select s.id from CrmMemo s where s.ifa.id=i.id and s.member.id=c.member.id and s.createTime>?  ) ");
		List<Object> params = new ArrayList<Object>();
		params.add(memberId);
		params.add(startDate);
		
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		
		vo.setItemName(period);
		vo.setItemValue(String.valueOf(list.size()));
		return vo;
	}
	
	/**
	 * 获取IFA新的潜在客户数量统计
	 * @author zxtan
	 * @date 2017-03-22
	 * @param memberId
	 * @return
	 */
	private List<AppEventItemVO> findProspectProposalEventList(String memberId) {
		List<AppEventItemVO> voList = new ArrayList<AppEventItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" select p.status,count(p.id) from CrmProposal p ");
		hql.append(" inner join CrmCustomer c on c.member.id=p.member.id ");
		hql.append(" inner join MemberIfa i on i.id = c.ifa.id and i.id=p.ifaMember.id ");
		hql.append(" where ifnull(p.status,'') != '' and c.clientType='0' and c.salesStageId='sales_proposal' and i.member.id=? ");
		hql.append(" group by p.status ");
		List<Object> params = new ArrayList<Object>();
		params.add(memberId);
		
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				AppEventItemVO vo = new AppEventItemVO();
				Object[] objs = (Object[]) list.get(i);
				vo.setItemName(objs[0].toString());
				vo.setItemValue(objs[1].toString());
			}
		}		
		return voList;
	}
	
	/**
	 * 获取IFA新的潜在客户数量统计
	 * @author zxtan
	 * @date 2017-03-22
	 * @param memberId
	 * @return
	 */
	private AppEventItemVO findProspectCloseEvent(String memberId) {
		AppEventItemVO vo = new AppEventItemVO();
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from CrmCustomer c ");
		hql.append(" inner join MemberIfa i on i.id = c.ifa.id");
		hql.append(" where c.clientType='0' and c.salesStageId='sales_close' and i.member.id=? ");
		List<Object> params = new ArrayList<Object>();
		params.add(memberId);
		
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);		
		vo.setItemName("close");
		vo.setItemValue(String.valueOf(list.size()));
		return vo;
	}

}
