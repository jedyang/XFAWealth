package com.fsll.app.ifa.client.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.fsll.app.ifa.client.service.AppPipelineService;
import com.fsll.app.ifa.client.vo.AppPipelineAccountItemVO;
import com.fsll.app.ifa.client.vo.AppPipelineClientItemVO;
import com.fsll.app.ifa.client.vo.AppPipelineHoldItemVO;
import com.fsll.app.ifa.client.vo.AppPipelineInvestorDetailVO;
import com.fsll.app.ifa.client.vo.AppPipelineInvestorVO;
import com.fsll.app.ifa.client.vo.AppPipelineKycItemVO;
import com.fsll.app.ifa.client.vo.AppPipelineNotYetInvestItemVO;
import com.fsll.app.ifa.client.vo.AppPipelineOrderPlanVO;
import com.fsll.app.ifa.client.vo.AppPipelineProposalItemVO;
import com.fsll.app.ifa.client.vo.AppPipelineProspectItemVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.StrUtils;
import com.fsll.core.entity.SysCountry;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.wmes.crm.vo.CrmPortfolioHoldVO;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.CrmMemo;
import com.fsll.wmes.entity.CrmProposal;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.InvestorDoc;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.OrderPlan;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioHoldAlert;
import com.fsll.wmes.entity.RpqExam;
import com.fsll.wmes.ifa.vo.IfaInvestorVO;

@Service("appIfaPipelineService")
public class AppPipelineServiceImpl extends BaseService implements
		AppPipelineService {
	
	public JsonPaging findAccountList(JsonPaging jsonPaging, String ifaMemberId,String keyword,String groupId,String openStatus) {
		List<AppPipelineAccountItemVO> voList = new ArrayList<AppPipelineAccountItemVO>();
		StringBuilder hql= new StringBuilder();
		hql.append("from CrmCustomer c ");
		hql.append(" inner join MemberIfa i on i.id=c.ifa.id ");
		hql.append(" inner join InvestorAccount a on a.ifa.id=i.id and a.member.id=c.member.id ");
		if(!"".equals(groupId)){
			hql.append(" left join CrmCustomerGroupRelationship s on s.customer.id=c.id ");
		}
		hql.append(" where c.clientType='1' and c.isValid='1'and a.openStatus !='3' and i.member.id=? ");
		List<Object> params = new ArrayList<Object>();
		params.add(ifaMemberId);
		
		if(!"".equals(keyword)){
			hql.append(" and c.nickName like ? ");
			params.add("%"+keyword+"%");
		}
		
		if(!"".equals(groupId)){
			hql.append(" and s.group.id = ? ");
			params.add(groupId);
		}
		
		if(!"".equals(openStatus)){
			hql.append(" and a.openStatus = ? ");
			params.add(openStatus);
		}		

		hql.append(" order by c.nickName ");
		jsonPaging = baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List list = jsonPaging.getList();
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				CrmCustomer customer = (CrmCustomer) objs[0];
				MemberBase member = customer.getMember();
//				MemberIfa ifa = (MemberIfa) objs[1];
				InvestorAccount account = (InvestorAccount) objs[2];
				
				AppPipelineAccountItemVO vo = new AppPipelineAccountItemVO();
				vo.setCustomerId(customer.getId());
				vo.setMemberId(member.getId());
				vo.setNickName(customer.getNickName());
				vo.setMobileCode(member.getMobileCode());
				vo.setMobileNumber(member.getMobileNumber());
				
				vo.setAccountId(account.getId());
				vo.setAccountNo(account.getAccountNo());
				vo.setOpenStatus(account.getOpenStatus());
				vo.setSubFlag(account.getSubFlag());
				vo.setlastUpdate(DateUtil.dateToDateString(account.getLastUpdate(), "yyyy-MM-dd HH:mm:ss"));
								
				voList.add(vo);
			}
			jsonPaging.setList(voList);
		}
		return jsonPaging;
	}
	
	
	/**
	 * 得到我的投资方案列表
	 * @author zxtan
	 * @date 2017-03-29
	 * @param jsonPaging 分页
	 * @param ifaMemberId Ifa member id
	 * @param clientMemberId Client member id
	 * @param clientType 客户类型
	 * @param keyword 搜索关键词
	 * @param status 状态
	 * @param minAmount 初始投资额下限
	 * @param maxAmount 初始投资额上限
	 * @return
	 */
	public JsonPaging findProposalList(JsonPaging jsonPaging, String ifaMemberId,String clientType,String keyword,String status,String minAmount,String maxAmount,String groupId,String langCode){
		List<AppPipelineProposalItemVO>  voList = new ArrayList<AppPipelineProposalItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append("from CrmCustomer c ");
		hql.append(" inner join MemberIfa i on i.id=c.ifa.id ");
		hql.append(" inner join CrmProposal p on p.ifaMember.id=i.id and p.member.id=c.member.id ");
		if(!"".equals(groupId)){
			hql.append(" left join CrmCustomerGroupRelationship s on s.customer.id=c.id ");
		}
		hql.append(" where c.isValid='1' and p.isValid='1' and c.clientType=? and i.member.id=? ");
		
		List params = new ArrayList();
		params.add(clientType);
		params.add(ifaMemberId);
		
		
		if(!"".equals(keyword)){
			hql.append(" and c.nickName like ? ");
			params.add("%"+keyword+"%");
		}

		if(!"".equals(groupId)){
			hql.append(" and s.group.id = ? ");
			params.add(groupId);
		}

		if(!"".equals(status)){
			hql.append(" and p.status = ? ");
			params.add(status);
		}
		
		if(!"".equals(minAmount)){
			hql.append(" and p.initialInvestAmount >= ? ");
			params.add(Double.parseDouble(minAmount));
		}
		
		if(!"".equals(maxAmount)){
			hql.append(" and p.initialInvestAmount <= ? ");
			params.add(Double.parseDouble(maxAmount));
		}
		
		hql.append(" order by c.createTime desc ");

		jsonPaging = baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);				
		List list = jsonPaging.getList();
		if(null != list && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[]) list.get(i);
				CrmCustomer customer = (CrmCustomer) objs[0];
				MemberBase member = customer.getMember();
				CrmProposal info = (CrmProposal) objs[2];
				
				AppPipelineProposalItemVO vo =new AppPipelineProposalItemVO();
				vo.setProposalId(info.getId());
				vo.setProposalName(info.getProposalName());
				vo.setBaseCurrId(info.getBaseCurrId());
				vo.setInitialInvestAmount(getFormatNum(info.getInitialInvestAmount(),info.getBaseCurrId()));
				vo.setStatus(info.getStatus());
				vo.setLastUpdate(DateUtil.dateToDateString(info.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
				vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
				
				vo.setCustomerId(customer.getId());
				vo.setMemberId(member.getId());
				if("1".equals(customer.getClientType())){
					vo.setNickName(getCommonMemberName(customer.getMember().getId(), langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
				}else {
					if(StringUtils.isNotBlank(customer.getNickName())){
						vo.setNickName(customer.getNickName());
					}else {
						vo.setNickName(getCommonMemberName(customer.getMember().getId(), langCode, CommonConstants.MEMBER_NAME_NICKNAME));
					}
				}
				vo.setMobileCode(member.getMobileCode());
				vo.setMobileNumber(member.getMobileNumber());				
				
				voList.add(vo);
			}
			jsonPaging.setList(voList);
		}
		return jsonPaging;
	}
	
	/**
	 * 得到我的投资组合交易计划列表
	 * @author zxtan
	 * @date 2017-03-29
	 * @param jsonPaging 分页
	 * @param ifaMemberId Ifa member id
	 * @param keyword 搜索关键词
	 * @param status 状态
	 * @param minAmount 投资额下限
	 * @param maxAmount 投资额上限
	 * @param groupId 分组Id
	 * @param toCurrency 货币
	 * @return
	 */
	public JsonPaging findPortfolioOrderList(JsonPaging jsonPaging, String ifaMemberId,String keyword,String status,String minAmount,String maxAmount,String groupId,String toCurrency,String langCode){
		List<AppPipelineOrderPlanVO>  voList = new ArrayList<AppPipelineOrderPlanVO>();
		StringBuilder hql = new StringBuilder();
		hql.append("from CrmCustomer c ");
		hql.append(" inner join MemberIfa i on i.id=c.ifa.id ");
		hql.append(" inner join PortfolioHold h on h.ifa.id=i.id and h.member.id=c.member.id ");
		hql.append(" inner join OrderPlan p on p.portfolioHold.id=h.id");
		if(!"".equals(groupId)){
			hql.append(" left join CrmCustomerGroupRelationship s on s.customer.id=c.id ");
		}
		hql.append(" where c.isValid='1' and p.finishStatus !='5' and c.clientType='1' and i.member.id=? ");
		
		List params = new ArrayList();
		params.add(ifaMemberId);
		
		
		if(!"".equals(keyword)){
			hql.append(" and c.nickName like ? ");
			params.add("%"+keyword+"%");
		}

		if(!"".equals(groupId)){
			hql.append(" and s.group.id = ? ");
			params.add(groupId);
		}

		if(!"".equals(status)){
			hql.append(" and p.finishStatus = ? ");
			params.add(status);
		}
		
		if(!"".equals(minAmount)){
			hql.append(" and get_exchange_rate(p.baseCurrency,?,p.totalBuy) >= ? ");
			params.add(toCurrency);
			params.add(Double.parseDouble(minAmount));
		}
		
		if(!"".equals(maxAmount)){
			hql.append(" and get_exchange_rate(p.baseCurrency,?,p.totalBuy) <= ? ");
			params.add(toCurrency);
			params.add(Double.parseDouble(maxAmount));
		}
		
		hql.append(" order by c.createTime desc ");

		jsonPaging = baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);				
		List list = jsonPaging.getList();
		String toCurrencyName = getParamConfigName(langCode, toCurrency, CommonConstants.SYS_PARAM_TYPE_CURRENCY);
		if(null != list && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[]) list.get(i);
				CrmCustomer customer = (CrmCustomer) objs[0];
				MemberBase member = customer.getMember();
				OrderPlan info = (OrderPlan) objs[3];
				String holdId = info.getPortfolioHold().getId();
				AppPipelineOrderPlanVO vo =new AppPipelineOrderPlanVO();
				vo.setPlanId(info.getId());
				vo.setHoldId(holdId);
				if(StringUtils.isBlank(toCurrency)){
					vo.setBaseCurrency(getParamConfigName(langCode, info.getBaseCurrency(), CommonConstants.SYS_PARAM_TYPE_CURRENCY));
					vo.setTotalBuy(getFormatNum(info.getTotalBuy(),info.getBaseCurrency()));
				}else {
					vo.setBaseCurrency(toCurrencyName);
					vo.setTotalBuy(getFormatNumByCurrency(info.getTotalBuy(),info.getBaseCurrency(),toCurrency));
				}
				
				vo.setFinishStatus(info.getFinishStatus());
				vo.setLastUpdate(DateUtil.dateToDateString(info.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
				vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
				
				if("4".equals(info.getFinishStatus())){
					String sql = "CALL pro_getOrderPlanCount(?)";
					
					List listCount = this.springJdbcQueryManager.springJdbcQueryForList(sql,new Object[]{holdId});
					Iterator it = (Iterator) listCount.iterator();
				    while (it.hasNext()) {
				    	HashMap map = (HashMap) it.next();										
						int successCount,pendCount,failCount;
						successCount = Integer.parseInt(getMapValue(map, "success_count"));
						pendCount = Integer.parseInt(getMapValue(map,"pend_count"));
						failCount = Integer.parseInt(getMapValue(map,"fail_count"));
						vo.setSuccessCount(String.valueOf(successCount));
						vo.setPendCount(String.valueOf(pendCount));
						vo.setFailCount(String.valueOf(failCount));
				    }
				}
								
								
				vo.setCustomerId(customer.getId());
				vo.setMemberId(member.getId());
				vo.setNickName(customer.getNickName());
				vo.setMobileCode(member.getMobileCode());
				vo.setMobileNumber(member.getMobileNumber());				
				
				voList.add(vo);
			}
			jsonPaging.setList(voList);
		}
		return jsonPaging;
	}
	
	/**
	 * 得到我的投资组合列表
	 * @author zxtan
	 * @date 2017-03-29
	 * @param jsonPaging 分页
	 * @param ifaMemberId Ifa member id
	 * @param keyword 搜索关键词
	 * @param status 状态
	 * @param minAmount 投资额下限
	 * @param maxAmount 投资额上限
	 * @param groupId 分组Id
	 * @param toCurrency 货币
	 * @return
	 */
	public JsonPaging findPortfolioHoldList(JsonPaging jsonPaging, String ifaMemberId,String keyword,String groupId,String toCurrency,String langCode){
		List<AppPipelineClientItemVO>  voList = new ArrayList<AppPipelineClientItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append("from CrmCustomer c ");
		hql.append(" inner join MemberIfa i on i.id=c.ifa.id ");
		if(!"".equals(groupId)){
			hql.append(" left join CrmCustomerGroupRelationship s on s.customer.id=c.id ");
		}
		hql.append(" where c.isValid='1' and c.clientType='1' and i.member.id=? ");
		hql.append(" and exists( SELECT h.id FROM PortfolioHold h inner join PortfolioHoldProduct hp on hp.portfolioHold.id=h.id  WHERE h.member.id = c.member.id AND h.ifa.id = c.ifa.id  ) ");
		
		List params = new ArrayList();
		params.add(ifaMemberId);
		
		
		if(!"".equals(keyword)){
			hql.append(" and c.nickName like ? ");
			params.add("%"+keyword+"%");
		}

		if(!"".equals(groupId)){
			hql.append(" and s.group.id = ? ");
			params.add(groupId);
		}
		
		hql.append(" order by c.createTime desc ");

		jsonPaging = baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);				
		List list = jsonPaging.getList();
		if(null != list && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[]) list.get(i);
				CrmCustomer customer = (CrmCustomer) objs[0];
				MemberBase member = customer.getMember();
				MemberIfa ifa = (MemberIfa) objs[1];
				
				AppPipelineClientItemVO vo =new AppPipelineClientItemVO();								
								
				vo.setCustomerId(customer.getId());
				vo.setMemberId(member.getId());
				vo.setNickName(customer.getNickName());
				vo.setMobileCode(member.getMobileCode());
				vo.setMobileNumber(member.getMobileNumber());	
				List<AppPipelineHoldItemVO> holdList = getPortfolioHoldList(ifa.getId(), member.getId(), toCurrency,langCode);
				vo.setDataList(holdList);
				
				voList.add(vo);
			}
			jsonPaging.setList(voList);
		}
		return jsonPaging;
	}
	
	
	/**
	 * 获取持仓列表
	 * @author zxtan
	 * @date 2016-12-19
	 * @return
	 */
	private List<AppPipelineHoldItemVO> getPortfolioHoldList(String ifaId,String memberId,String toCurrency,String langCode){
		List<AppPipelineHoldItemVO> voList = new ArrayList<AppPipelineHoldItemVO>();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" From PortfolioHold h ");
		hql.append(" LEFT JOIN PortfolioHoldAlert a on h.id=a.portfolioHold.id and a.alertType='ifa_asc' ");
		hql.append(" LEFT JOIN PortfolioHoldAlert d on h.id=d.portfolioHold.id and d.alertType='ifa_desc' ");
		hql.append(" WHERE IFNULL(h.totalAsset,0)>0 and h.ifa.id=? and h.member.id=? ");
		hql.append(" and exists (select pd.id from PortfolioHoldProduct pd where pd.portfolioHold.id=h.id ) ");
		
		params.add(ifaId);
		params.add(memberId);
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		String toCurrencyName = getParamConfigName(langCode, toCurrency, CommonConstants.SYS_PARAM_TYPE_CURRENCY);
		if(null != list && !list.isEmpty()){
			Double totalAsset =  0.0;	
			Double totalReturnValue =  0.0;	
			Boolean ifAscAlert = false;
			Boolean ifDescAlert = false;
			
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				PortfolioHold hold = (PortfolioHold) objs[0];
				AppPipelineHoldItemVO vo = new AppPipelineHoldItemVO();
				
				vo.setPortfolioHoldId(hold.getId());
				String fromCurrency = hold.getBaseCurrency();	
				if(StringUtils.isBlank(toCurrency)){
					vo.setBaseCurrency(getParamConfigName(langCode, fromCurrency, CommonConstants.SYS_PARAM_TYPE_CURRENCY));
				}else {
					vo.setBaseCurrency(toCurrencyName);
				}
				
				vo.setPortfolioName(hold.getPortfolioName());
				Double tempAsset = getNumByCurrency(hold.getTotalAsset(), fromCurrency, toCurrency);
				Double tempreturnValue = getNumByCurrency(hold.getTotalReturnValue(), fromCurrency, toCurrency);
				tempAsset = tempAsset== null?0:tempAsset;
				tempreturnValue = tempreturnValue==null?0:tempreturnValue;
				totalAsset += tempAsset;
				totalReturnValue += tempreturnValue;
				
				vo.setTotalAsset(getFormatNum(tempAsset,toCurrency));
				vo.setTotalReturnValue(getFormatNum(tempreturnValue,toCurrency));
				
				double tempReturnRate = hold.getTotalReturnRate()==null?0.0:hold.getTotalReturnRate();
				vo.setTotalReturnRate(getFormatNumByPer(tempReturnRate));
				if(null != objs[1]){
					PortfolioHoldAlert ascAlert = (PortfolioHoldAlert) objs[2];
					if(hold.getTotalReturnRate() > ascAlert.getAlertValue()){
						vo.setAscAlert("1");
						ifAscAlert = true;
					}
				}
				if(null != objs[2]){
					PortfolioHoldAlert descAlert = (PortfolioHoldAlert) objs[3];
					if(hold.getTotalReturnRate() < descAlert.getAlertValue()){
						vo.setDescAlert("1");
						ifDescAlert = true;
					}
				}
				
				voList.add(vo);
			}
			
			if(voList.size()>1){
				Double returnRate = 0.0;
				AppPipelineHoldItemVO vo = new AppPipelineHoldItemVO();
				
				for (int i = 0; i < voList.size(); i++) {
//					Object[] objs = (Object[]) list.get(i);
//					PortfolioHold hold = (PortfolioHold) objs[0];
					double value = Double.parseDouble( voList.get(i).getTotalAsset().replace(",", ""));
					double rate = Double.parseDouble( voList.get(i).getTotalReturnRate().replace("%", ""))/100.0;
					returnRate += value* rate / totalAsset;
				}
				vo.setTotalAsset( getFormatNum(totalAsset,toCurrency));
				vo.setTotalReturnRate(getFormatNumByPer(returnRate));
				vo.setTotalReturnValue(getFormatNum(totalReturnValue,toCurrency));
				vo.setBaseCurrency(toCurrencyName);
				vo.setAscAlert(ifAscAlert == true? "1":"0" );
				vo.setDescAlert(ifDescAlert == true? "1":"0");
				vo.setIfSummary("1");
				voList.add(0, vo);
			}
		}
		return voList;
	}
	
	
	/**
	 * 得到我的KYC列表
	 * @author zxtan
	 * @date 2017-03-29
	 * @param jsonPaging 分页
	 * @param ifaMemberId Ifa member id
	 * @param keyword 搜索关键词
	 * @param status 状态
	 * @param minAmount 投资额下限
	 * @param maxAmount 投资额上限
	 * @param groupId 分组Id
	 * @param toCurrency 货币
	 * @return
	 */
	public JsonPaging findKYCList(JsonPaging jsonPaging, String ifaMemberId,String keyword,String distributorId,String groupId){
		List<AppPipelineKycItemVO>  voList = new ArrayList<AppPipelineKycItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append("from CrmCustomer c ");
		hql.append(" inner join MemberIfa i on i.id=c.ifa.id ");
		hql.append(" inner join InvestorAccount a on a.ifa.id=i.id and a.member.id=c.member.id ");
		hql.append(" inner join MemberDistributor d on d.id=a.distributor.id ");
		hql.append(" left join RpqExam r on r.relateId=a.id and r.status='1' ");
		if(!"".equals(groupId)){
			hql.append(" left join CrmCustomerGroupRelationship s on s.customer.id=c.id ");
		}
		hql.append(" where c.isValid='1' and c.clientType='1' and a.subFlag='0' and a.openStatus='3' ");
		hql.append(" and i.member.id=? ");		
		
		List<Object> params = new ArrayList<Object>();
		params.add(ifaMemberId);		
		
		if(!"".equals(keyword)){
			hql.append(" and c.nickName like ? ");
			params.add("%"+keyword+"%");
		}
		
		if(!"".equals(distributorId)){
			hql.append(" and d.id = ? ");
			params.add(distributorId);
		}

		if(!"".equals(groupId)){
			hql.append(" and s.group.id = ? ");
			params.add(groupId);
		}
			
		hql.append(" order by c.createTime desc ");

		jsonPaging = baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);				
		List list = jsonPaging.getList();
		if(null != list && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[]) list.get(i);
				CrmCustomer customer = (CrmCustomer) objs[0];
				MemberBase member = customer.getMember();
				InvestorAccount account = (InvestorAccount) objs[2];
				MemberDistributor distributor = (MemberDistributor) objs[3];
				
				AppPipelineKycItemVO vo =new AppPipelineKycItemVO();

				vo.setCustomerId(customer.getId());
				vo.setMemberId(member.getId());
				vo.setNickName(customer.getNickName());
				vo.setMobileCode(member.getMobileCode());
				vo.setMobileNumber(member.getMobileNumber());
				
				vo.setDistributorId(distributor.getId());
				vo.setCompanyName(distributor.getCompanyName());
				vo.setLogofile(distributor.getLogofile());
				if(null != objs[4]){
					RpqExam rpqExam = (RpqExam) objs[4];
					if(null != rpqExam.getExpireDate()){
			    		try {
							int days = daysBetween(new Date(), rpqExam.getExpireDate());
							vo.setNextRpqDate(String.valueOf(days));
						} catch (ParseException e) {
							e.printStackTrace();
						}
			    	}
				}
				
				Date docExpireDate = getDocExpireDate(account.getId());
		    	if(null != docExpireDate){
		    		try {
						int days = daysBetween(new Date(), docExpireDate);
						vo.setNextDocDate(String.valueOf(days));
					} catch (ParseException e) {
						e.printStackTrace();
					}
		    	}								
				
				voList.add(vo);
			}
			jsonPaging.setList(voList);
		}
		return jsonPaging;
	}
	
	
	/**
	 * 获取文档过期时间
	 * @author zxtan
	 * @date 2016-12-19
	 * @return
	 */
	private Date getDocExpireDate(String accountId){
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" From InvestorDoc d ");
		hql.append(" WHERE d.account.id=? Order By d.expireDate ");		
		params.add(accountId);
		Date expireDate = null;
		List<InvestorDoc> list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			expireDate = list.get(0).getExpireDate();
		}
		return expireDate;
	}
	
	
	/**
	 * 得到我的未投资账户列表
	 * @author zxtan
	 * @date 2017-03-29
	 * @param jsonPaging 分页
	 * @param ifaMemberId Ifa member id
	 * @param keyword 搜索关键词
	 * @param groupId 分组Id
	 * @return
	 */
	public JsonPaging findNotYetInvestList(JsonPaging jsonPaging, String ifaMemberId,String keyword,String groupId){
		List<AppPipelineNotYetInvestItemVO>  voList = new ArrayList<AppPipelineNotYetInvestItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append("from CrmCustomer c ");
		hql.append(" inner join MemberIfa i on i.id=c.ifa.id ");
		hql.append(" inner join InvestorAccount a ON a.ifa.id=i.id AND a.member.id=c.member.id ");
		hql.append(" inner join MemberDistributor d ON d.id=a.distributor.id ");
		if(!"".equals(groupId)){
			hql.append(" left join CrmCustomerGroupRelationship s on s.customer.id=c.id ");
		}
		hql.append(" where c.isValid='1' and c.clientType='1' and i.member.id=? ");
		hql.append(" and not exists( SELECT h.id FROM PortfolioHold h inner join PortfolioHoldProduct hp on hp.portfolioHold.id=h.id  WHERE h.member.id = c.member.id AND h.ifa.id = c.ifa.id  ) ");
		
		List params = new ArrayList();
		params.add(ifaMemberId);
		
		
		if(!"".equals(keyword)){
			hql.append(" and c.nickName like ? ");
			params.add("%"+keyword+"%");
		}

		if(!"".equals(groupId)){
			hql.append(" and s.group.id = ? ");
			params.add(groupId);
		}
		
		hql.append(" order by c.createTime desc ");

		jsonPaging = baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);				
		List list = jsonPaging.getList();
		if(null != list && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[]) list.get(i);
				CrmCustomer customer = (CrmCustomer) objs[0];
				MemberBase member = customer.getMember();
//				MemberIfa ifa = (MemberIfa) objs[1];
				InvestorAccount account = (InvestorAccount) objs[2];
				MemberDistributor distributor = (MemberDistributor) objs[3];
				
				AppPipelineNotYetInvestItemVO vo =new AppPipelineNotYetInvestItemVO();								
								
				vo.setCustomerId(customer.getId());
				vo.setMemberId(member.getId());
				vo.setNickName(customer.getNickName());
				vo.setMobileCode(member.getMobileCode());
				vo.setMobileNumber(member.getMobileNumber());	
				vo.setAccountId(account.getId());
				vo.setAccountNo(account.getAccountNo());
				vo.setDistributorId(distributor.getId());
				vo.setCompanyName(distributor.getCompanyName());
				vo.setLogofile(PageHelper.getLogoUrlForWS(distributor.getLogofile(), "D"));
				
				voList.add(vo);
			}
			jsonPaging.setList(voList);
		}
		return jsonPaging;
	}
	
	
	/**
	 * 得到我的潜在客户列表
	 * @author zxtan
	 * @date 2017-03-29
	 * @param jsonPaging 分页
	 * @param ifaMemberId Ifa member id
	 * @param keyword 搜索关键词
	 * @param groupId 分组Id
	 * @param saleStageId 潜在客户状态
	 * @return
	 */
	public JsonPaging findProspectList(JsonPaging jsonPaging, String ifaMemberId,String keyword,String groupId,String saleStageId){
		List<AppPipelineProspectItemVO>  voList = new ArrayList<AppPipelineProspectItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append("from CrmCustomer c ");
		hql.append(" inner join MemberIfa i on i.id=c.ifa.id ");
		if(!"".equals(groupId)){
			hql.append(" left join CrmCustomerGroupRelationship s on s.customer.id=c.id ");
		}
		hql.append(" where c.isValid='1' and c.clientType='0' and i.member.id=? and c.salesStageId=? ");
		
		List params = new ArrayList();
		params.add(ifaMemberId);
		params.add(saleStageId);
		
		
		if(!"".equals(keyword)){
			hql.append(" and c.nickName like ? ");
			params.add("%"+keyword+"%");
		}

		if(!"".equals(groupId)){
			hql.append(" and s.group.id = ? ");
			params.add(groupId);
		}
		
		hql.append(" order by c.createTime desc ");

		jsonPaging = baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);				
		List list = jsonPaging.getList();
		if(null != list && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[]) list.get(i);
				CrmCustomer customer = (CrmCustomer) objs[0];
				MemberBase member = customer.getMember();
				MemberIfa ifa = (MemberIfa) objs[1];
				
				AppPipelineProspectItemVO vo =new AppPipelineProspectItemVO();								
								
				vo.setCustomerId(customer.getId());
				vo.setMemberId(member.getId());
				vo.setNickName(customer.getNickName());
				vo.setMobileCode(member.getMobileCode());
				vo.setMobileNumber(member.getMobileNumber());	
				vo.setLastUpdate(DateUtil.dateToDateString(customer.getLastUpdate(),CommonConstants.FORMAT_DATE_TIME));
				
				if("sales_contact".equalsIgnoreCase(saleStageId)){
					String sql = "from CrmMemo m where m.ifa.id=? and m.member.id=? order by m.lastModify desc ";
					List<Object> memoParams = new ArrayList<Object>();
					memoParams.add(ifa.getId());
					memoParams.add(member.getId());
					List<CrmMemo> memoList = baseDao.find(sql, memoParams.toArray(), false);
					if(null != memoList && !memoList.isEmpty()){
						vo.setContactTimes(String.valueOf(memoList.size()));
						vo.setLastContact(DateUtil.dateToDateString(memoList.get(0).getLastModify(), CommonConstants.FORMAT_DATE_TIME));
					}
				}
				
				voList.add(vo);
			}
			jsonPaging.setList(voList);
		}
		return jsonPaging;
	}
	
	
	/**
	 * 得到我的客户列表
	 * @author zxtan
	 * @date 2017-04-21
	 * @param jsonPaging 分页
	 * @param ifaMemberId Ifa member id
	 * @param clientType 客户类型
	 * @param keyword 搜索关键词
	 * @param groupId 分组Id
	 * @return
	 */
	public JsonPaging findCustomerList(JsonPaging jsonPaging, String ifaMemberId,String clientType,String keyword,String groupId,String langCode){
		List<AppPipelineClientItemVO>  voList = new ArrayList<AppPipelineClientItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append("from CrmCustomer c ");
		hql.append(" inner join MemberIfa i on i.id=c.ifa.id ");
		if(!"".equals(groupId)){
			hql.append(" left join CrmCustomerGroupRelationship s on s.customer.id=c.id ");
		}
		hql.append(" where c.isValid='1' and i.member.id=? ");
		
		List params = new ArrayList();
		params.add(ifaMemberId);
		if(!"".equals(clientType) && ",0,1,".indexOf(clientType)>-1){
			hql.append(" and c.clientType=? ");
			params.add(clientType);			
		}
				
		if(!"".equals(keyword)){
			hql.append(" and c.nickName like ? ");
			params.add("%"+keyword+"%");
		}

		if(!"".equals(groupId)){
			hql.append(" and s.group.id = ? ");
			params.add(groupId);
		}
		
		hql.append(" order by c.createTime desc ");

		jsonPaging = baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);				
		List list = jsonPaging.getList();
		if(null != list && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[]) list.get(i);
				CrmCustomer customer = (CrmCustomer) objs[0];
				MemberBase member = customer.getMember();
//				MemberIfa ifa = (MemberIfa) objs[1];
				
				AppPipelineClientItemVO vo =new AppPipelineClientItemVO();								
								
				vo.setCustomerId(customer.getId());
				vo.setMemberId(member.getId());
//				vo.setNickName(customer.getNickName());
				vo.setMobileCode(member.getMobileCode());
				vo.setMobileNumber(member.getMobileNumber());	
				vo.setClientType(customer.getClientType());
				
				if("1".equals(customer.getClientType())){
					//existing
					vo.setNickName(getCommonMemberName(member.getId(), langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
				}else {
					//prospect
					String fullName = customer.getNickName();
					if(StringUtils.isBlank(fullName)){
						fullName = getCommonMemberName(member.getId(), langCode, CommonConstants.MEMBER_NAME_NICKNAME);
					}
					vo.setNickName(fullName);
				}
				
				voList.add(vo);
			}
			jsonPaging.setList(voList);
		}
		return jsonPaging;
	}
	
	
	/**
	 * 获取代理商列表
	 * @return
	 */
	public List<MemberDistributor> findAllDistributors(){		
		String hql="from MemberDistributor m order by m.companyName ";
		List<MemberDistributor> list = this.baseDao.find(hql, null, false);

		return list;
	}

	/**
	 * 查找投资者
	 * @author zxtan
	 * @date 2017-04-12
	 * @param jsonPaging 分页信息
	 * @param ifaMemberId 投资顾问 member id
	 * @param languageSpoken 语言
	 * @param invStyle 投资风格
	 * @param intrest 兴趣
	 * @param noIfa 没有Ifa
	 * @param region 地区
	 * @return
	 */
	public JsonPaging findInverstorNotInCrm(JsonPaging jsonPaging, String ifaMemberId,String languageSpoken,String invStyle,String intrest,String noIfa,String region) {
		MemberBase ifaMember = (MemberBase) baseDao.get(MemberBase.class, ifaMemberId);

		StringBuilder hql=new StringBuilder();

		hql.append(" from MemberIndividual m ");
		hql.append(" inner join MemberBase b on m.member.id=b.id");
		hql.append(" where b.id not in (select c.member.id from CrmCustomer c where c.ifa.member.id=? )");
		hql.append(" and (b.inviteCode is null or b.inviteCode='')");
		List params=new ArrayList();
		params.add(ifaMember.getId());
		if(null!=languageSpoken && !"".equals(languageSpoken)){
			String[] arr = languageSpoken.split(",");
			hql.append(" and (1=0");
			for (String str : arr) {
				if("".equals(str))
					continue;
				hql.append(" or b.languageSpoken like ?");
				params.add("%"+str+"%");
			}
			hql.append(")");
			
		}
		if(null!=invStyle && !"".equals(invStyle)){
			String[] arr = invStyle.split(",");
			hql.append(" and (1=0");
			for (String str : arr) {
				if("".equals(str))
					continue;
				hql.append(" or b.investStyle like ?");
				params.add("%"+str+"%");
			}
			hql.append(")");
			
			/*hql.append(" and m.member.investStyle like ?");
			params.add(" %"+invStyle+"%");*/
		}
		if(null!=intrest && !"".equals(intrest)){
			String[] arr = intrest.split(",");
			hql.append(" and (1=0");
			for (String str : arr) {
				if("".equals(str))
					continue;
				hql.append(" or b.hobbyType like ?");
				params.add("%"+str+"%");
			}
			hql.append(")");
			
			
		}
		if(null!=region && !"".equals(region)){
			String[] arr = region.split(",");
			hql.append(" and (1=0");
			for (String str : arr) {
				if("".equals(str))
					continue;
				hql.append(" or m.country.id = ?");
				params.add(str);
			}
			hql.append(")");
		}
		if(null!=noIfa && "1".equals(noIfa)){
			hql.append(" and b.id not in (select e.member.id from CrmCustomer e )");
		}
			
//		jsonPaging.setOrder(null);
//		jsonPaging.setSort(null);
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List list = jsonPaging.getList();		
		if(null!=list && !list.isEmpty()){
			List<AppPipelineInvestorVO> voList=new ArrayList<AppPipelineInvestorVO>();
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				MemberBase member = (MemberBase) objs[1];
				AppPipelineInvestorVO vo = new AppPipelineInvestorVO();
				vo.setMemberId(member.getId());
				vo.setLoginCode(member.getLoginCode());
				vo.setNickName(member.getNickName());
				vo.setEmail(member.getEmail());
				vo.setMobileCode(member.getMobileCode());
				vo.setMobileNumber(member.getMobileNumber());
				
				voList.add(vo);
			}
			jsonPaging.setList(voList);								
		}
		
		return jsonPaging;
	}
	

	/**
	 * 获取投资者的详情
	 * @author zxtan
	 * @date 2017-04-12
	 * @param clientMemberId
	 * @param langCode
	 * @return
	 */
	public AppPipelineInvestorDetailVO findInverstorDetail(String clientMemberId,String langCode) {
		AppPipelineInvestorDetailVO vo = new AppPipelineInvestorDetailVO();
		StringBuilder hql=new StringBuilder();

		hql.append(" from MemberIndividual m ");
		hql.append(" inner join MemberBase b on m.member.id=b.id");
		hql.append(" left join SysParamConfig em on em.configCode=m.employment");
		hql.append(" left join SysParamConfig oc on oc.configCode=m.occupation");
		hql.append(" left join SysParamConfig ed on ed.configCode=m.education");
		hql.append(" left join SysCountry n on n.id=m.nationality");
		hql.append(" where b.id=? ");
		List<Object> params=new ArrayList<Object>();
		params.add(clientMemberId);

		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
				
		if(null!=list && !list.isEmpty()){
			
			Object[] objs = (Object[]) list.get(0);
			MemberIndividual individual = (MemberIndividual) objs[0];
			MemberBase member = (MemberBase) objs[1];
						
			String gender = individual.getGender();
			String iconUrl = member.getIconUrl();
			vo.setMemberId(member.getId());
			vo.setLoginCode(member.getLoginCode());
			vo.setNickName(member.getNickName());
			vo.setGender(gender);
			vo.setIconUrl(PageHelper.getUserHeadUrlForWS(iconUrl, gender));
			vo.setRegistrationDate(DateUtil.dateToDateString(member.getCreateTime(), CommonConstants.FORMAT_DATE_TIME));
			vo.setLastLoginDate(DateUtil.dateToDateString(member.getLoginTime(), CommonConstants.FORMAT_DATE_TIME));
			
			vo.setEmail(member.getEmail());
			vo.setMobileCode(member.getMobileCode());
			vo.setMobileNumber(member.getMobileNumber());
			vo.setWebchatCode(member.getWebchatCode());
			vo.setWeiboCode(member.getWeiboCode());
			vo.setFacebookCode(member.getFacebookCode());
			vo.setLinkedinCode(member.getLinkedInCode());
			vo.setTwitterCode(member.getTwitterCode());
			
			String investStyle = getParamConfigNameList(langCode, member.getInvestStyle());
			String investField = getParamConfigNameList(langCode, member.getInvestField());
			vo.setInvestStyle(investStyle);
			vo.setInvestField(investField);
			
			if(null != objs[2]){
				SysParamConfig info = (SysParamConfig) objs[2];
				if("en".equalsIgnoreCase(langCode)){
					vo.setEmployment(info.getNameEn());
				}else if("tc".equalsIgnoreCase(langCode)){
					vo.setEmployment(info.getNameTc());
				} else {
					vo.setEmployment(info.getNameSc());
				}
			}
			
			if(null != objs[3]){
				SysParamConfig info = (SysParamConfig) objs[3];
				if("en".equalsIgnoreCase(langCode)){
					vo.setOccupation(info.getNameEn());
				}else if("tc".equalsIgnoreCase(langCode)){
					vo.setOccupation(info.getNameTc());
				} else {
					vo.setOccupation(info.getNameSc());
				}
			}
			if(null != objs[4]){
				SysParamConfig info = (SysParamConfig) objs[4];
				if("en".equalsIgnoreCase(langCode)){
					vo.setEducation(info.getNameEn());
				}else if("tc".equalsIgnoreCase(langCode)){
					vo.setEducation(info.getNameTc());
				} else {
					vo.setEducation(info.getNameSc());
				}
			}

			if(null != objs[5]){
				SysCountry info = (SysCountry) objs[5];
				if("en".equalsIgnoreCase(langCode)){
					vo.setNationality(info.getNameEn());
				}else if("tc".equalsIgnoreCase(langCode)){
					vo.setNationality(info.getNameTc());
				} else {
					vo.setNationality(info.getNameSc());
				}
			}
			
			String hobby = getParamConfigNameList(langCode, member.getHobbyType());
			String region = getParamConfigNameList(langCode, member.getLiveRegion());
			String languageSpoken = getParamConfigNameList(langCode, member.getLanguageSpoken());
			String hobbyType = member.getHobbyType();
			if(null != hobbyType){
				String[] arr = hobbyType.split(",");
				for (String item : arr) {
					if(item.indexOf("{")>-1){
						hobby += ","+item.replace("{", "").replace("}", "");
					}
				}
			}
			vo.setHobby(hobby);
			vo.setLiveRegion(region);
			vo.setLanguageSpoken(languageSpoken);
		}
		
		return vo;
	}
	
	
	/**
	 * 添加潜在客户
	 * @author zxtan
	 * @date 2017-04-12
	 * @param ifaMemberId
	 * @param clientMemberId
	 * @param saleStageId
	 * @return
	 */
	public Boolean addCustomer(String ifaMemberId,String clientMemberId,String saleStageId){
		MemberBase member = (MemberBase) baseDao.get(MemberBase.class, clientMemberId);
		MemberIfa ifa = null;
		String hql = "from MemberIfa m where m.member.id=?";
		List<MemberIfa> list = baseDao.find(hql, new Object[]{ifaMemberId}, false);
		if(null != list && !list.isEmpty()){
			ifa = list.get(0);
		}else {
			return false;
		}
		
		if(null == member){ 
			return false;
		}
		
		CrmCustomer customer = new CrmCustomer();
		customer.setId(null);
		customer.setIfa(ifa);
		customer.setMember(member);
		customer.setNickName(member.getNickName());
		customer.setIconUrl(member.getIconUrl());
		customer.setClientType("0");
		customer.setSalesStageId(saleStageId);
		customer.setCreateTime(new Date());
		customer.setLastUpdate(new Date());
		customer.setIsValid("1");
		baseDao.saveOrUpdate(customer);
		
		return true;
	}
}
