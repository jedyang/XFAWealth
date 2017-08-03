/**
 * 
 */
package com.fsll.wmes.crm.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.StrUtils;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.crm.service.InvestorClientManageService;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmBaseVO;
import com.fsll.wmes.investor.service.InvestorService;
import com.fsll.wmes.investor.vo.AccountVO;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.vo.PersonalInfoVO;
import com.fsll.wmes.strategy.vo.CharDataVO;

/**
 * @author scshi_u330p
 *
 */
@Service("investorClientManageService")
//@Transactional
public class InvestorClientManageServiceImpl extends BaseService implements InvestorClientManageService {
	@Autowired
	private MemberBaseService memberBaseService;
	
	@Autowired
	private SysParamService sysParamService;
	
	@Autowired 
	private InvestorService investorService;
	
	@Autowired
	private IfafirmManageService ifafirmManageService;
	
	/**
	 * 代理商客户列表json
	 * @date 20161223
	 * @author scshi_u330p
	 * */
	public JsonPaging findAccountList(JsonPaging jsonPaging, AccountVO accountVO,String langCode){
		List params = new ArrayList();

		StringBuilder sql=new StringBuilder();
		//ifaId varchar(36),distributorId varchar(36),conditionStr varchar(100),
	    //ifafirmId varchar(36),startDate varchar(20),endDate varchar(20),
		//currency varchar(10),orderby varchar(50),pageIndex int ,pageSize int
		sql.append(" call pro_getaccountlist(?,?,?,?,?,?,?,?,?,?,?);");
		params.add(accountVO.getDocStauts());
		params.add(accountVO.getIfaId());
		params.add(accountVO.getDistributorId());
		params.add(accountVO.getOpenStatus());
		params.add(accountVO.getIfafirmId());
		
		params.add(DateUtil.getCurDateStr());
		params.add(accountVO.getNextRPQDate());
		params.add(accountVO.getBaseCurrency());
		if (!StrUtils.isEmpty(jsonPaging.getOrderStr()) && "undefine".equalsIgnoreCase(StrUtils.getString(jsonPaging.getOrderStr())))
			params.add(jsonPaging.getOrderStr());
		else {
			params.add("");
		}
		params.add((jsonPaging.getPage()-1)*jsonPaging.getRows());
		params.add(jsonPaging.getRows());
		//params.add(total);
		List resultList=springJdbcQueryManager.springJdbcQueryForList(sql.toString(), params.toArray());
		
		
		params=new ArrayList();
		sql=new StringBuilder();
		//ifaId VARCHAR(36),distributorId VARCHAR(36),conditionStr VARCHAR(100),
	    //ifafirmId VARCHAR(36),startDate VARCHAR(20),endDate VARCHAR(20)
		sql.append(" call pro_getaccountlisttotal(?,?,?,?,?,?);");
		params.add(accountVO.getIfaId());
		params.add(accountVO.getDistributorId());
		params.add(accountVO.getOpenStatus());
		//params.add(accountVO.getIsValid());
		params.add(accountVO.getIfafirmId());
		params.add(accountVO.getNextRPQDate());
		params.add(DateUtil.getCurDateStr());
		List totalList=springJdbcQueryManager.springJdbcQueryForList(sql.toString(), params.toArray());
		if(null!=totalList && !totalList.isEmpty()){
			HashMap map=(HashMap)totalList.get(0);
			if(map.containsKey("count(*)")){
				Object total=map.get("count(*)");
				if(null!=total){
					jsonPaging.setTotal(Integer.valueOf(total.toString()));
				}
			}
		}
		List<AccountVO> list=new ArrayList<AccountVO>();
		if(null!=resultList){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				HashMap map = (HashMap) it.next();
				Object id=getMapObject(map, "id");
				Object nextDoc=getMapObject(map, "docDate");
			    Object nextRpq=getMapObject(map, "rpqDate");
			    Object riskLevel=getMapObject(map, "risk_level");
			    Object marketValueObj=getMapObject(map, "marketValue");
			    Object cashObj=getMapObject(map, "cash");
			    Object totalAssetsObj=getMapObject(map, "totalAssets");
			    Object nickName=getMapObject(map, "nick_name");
			    double marketValue=null!=marketValueObj&&!"".equals(marketValueObj.toString()) ?Double.valueOf(marketValueObj.toString()):0;
			    double cash=null!=cashObj&& !"".equals(cashObj.toString())?Double.valueOf(cashObj.toString()):0;
			    double totalAssets=null!=totalAssetsObj&& !"".equals(totalAssetsObj.toString())?Double.valueOf(totalAssetsObj.toString()):0;
			    
			    
			    AccountVO vo=new AccountVO();
			    if(null==id )
			    	continue;
			    InvestorAccount  investorAccount=investorService.findInvestorAccountById(id.toString());
			    vo.setCustomerName(null!=nickName?nickName.toString():"");
				vo.setId(investorAccount.getId());
				vo.setAccountNo(investorAccount.getAccountNo());
				vo.setAccType("I".equals(investorAccount.getAccType())?"Indiviual":"Joint");
				vo.setBaseCurrency(null!=investorAccount.getBaseCurrency()?investorAccount.getBaseCurrency():"");
				vo.setBaseCurName(sysParamService.findNameByCode(vo.getBaseCurrency(), langCode));
				vo.setCies((null!=investorAccount.getCies() && "1".equals(investorAccount.getCies()))?"CIES":"");
				vo.setDistributorId(null!=investorAccount.getDistributor()?investorAccount.getDistributor().getId():"");
				vo.setFromType(investorAccount.getFromType());
				vo.setMemberId(null!=investorAccount.getMember()? investorAccount.getMember().getId():"");
				vo.setSubFlag(StrUtils.getString(getMapObject(map, "sub_flag")));
				vo.setSubRelateId(StrUtils.getString(getMapObject(map, "sub_relate_id")));
				
				vo.setOpenStatus(investorAccount.getOpenStatus());
				if("-1".equals(vo.getOpenStatus())){
					//approvalCount++;
				}
				if("3".equals(investorAccount.getOpenStatus())){
					
					vo.setTotalAssest(StrUtils.getNumberString(totalAssets,"#,##0.00"));
					vo.setProductValue(StrUtils.getNumberString(marketValue,"#,##0.00"));
					vo.setCash(StrUtils.getNumberString(cash,"#,##0.00"));

					try {
						if (null != nextDoc && !"".equals(nextDoc.toString())) {
							vo.setNextDCDate(String.valueOf(DateUtil.daysBetween(DateUtil.getCurDate(), DateUtil.StringToDate(nextDoc.toString(), DateUtil.DEFAULT_DATE_FORMAT))));
						} else {
							vo.setNextDCDate("0");
						}
						if (null != nextRpq && !"".equals(nextRpq.toString())) {
							vo.setNextRPQDate(String.valueOf(daysBetween(DateUtil.getCurDate(),DateUtil.StringToDate(nextRpq.toString(), DateUtil.DEFAULT_DATE_FORMAT))));
						} else {
							vo.setNextRPQDate("0");
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
				}
				
				
				vo.setFlowStatus(investorAccount.getFlowStatus());
				vo.setFaca((null!=investorAccount.getFaca() && "1".equals(investorAccount.getFaca()))?"FACA":"");
				vo.setSubFlag(null!=investorAccount.getSubFlag()?investorAccount.getSubFlag():"");
				vo.setLoginCode(null!=investorAccount.getMember()? investorAccount.getMember().getLoginCode():"");
				vo.setRiskLevel(null!=riskLevel?riskLevel.toString():"0");
				vo.setDistributor(null!=investorAccount.getDistributor()?investorAccount.getDistributor().getCompanyName():"");
				vo.setDistributorIcon(null!=investorAccount.getDistributor()?investorAccount.getDistributor().getLogofile():"");
				vo.setIsValid(investorAccount.getIsValid());
				vo.setIfafirmId(null!=investorAccount.getIfa()&& null!=investorAccount.getIfa().getIfafirm()? investorAccount.getIfa().getIfafirm().getId():"");
				MemberIfafirmBaseVO ifafirmBaseVO=ifafirmManageService.findIfafirmBase(vo.getIfafirmId(), langCode);
				vo.setIfafirmName(null!=ifafirmBaseVO?ifafirmBaseVO.getIfafirmName():"");
				vo.setIfafirmIcon(null!=ifafirmBaseVO?ifafirmBaseVO.getIconUrl():"");
				vo.setIfaId(null!=investorAccount.getIfa()?investorAccount.getIfa().getId():"");
				PersonalInfoVO personalInfo = memberBaseService.findPersonalInfoById(investorAccount.getIfa().getMember().getId(), langCode);
				if (null!=personalInfo)
				vo.setIfaName(personalInfo.getIfaPerson().getFirstName()+" "+personalInfo.getIfaPerson().getLastName());
				
				//主账户多少个文档需要审批
				if("0".equals(vo.getSubFlag())){
					StringBuffer checkCountHql = new StringBuffer(" from InvestorDoc t where t.account.id=? and t.checkStatus='0' ");
					checkCountHql.append(" and t.isValid='1' order by t.lastUpdate desc ");
					List checkCountList = this.baseDao.find(checkCountHql.toString(), new String[]{id.toString()}, false);
					if(null!=checkCountList)
						vo.setDocCheckCount(checkCountList.size());
				}
				
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	
	
//	/**
//	 * ifa客户列表（停用）
//	 * @author scshi_u330p
//	 * @date 20161223
//	 * */
//	public JsonPaging findIfaAccountList(JsonPaging jsonPaging,AccountVO accountVO, String langCode){
//		
//		List params = new ArrayList();
//		
//		StringBuilder sql=new StringBuilder();
//		sql.append(" call pro_getaccountlist(?,?,?,?,?,?,?,?,?,?);");
//		
//		params.add(accountVO.getIfaId());
//		params.add(accountVO.getDistributorId());
//		params.add(accountVO.getOpenStatus());
//		params.add(accountVO.getIfafirmId());
//		
//		params.add(DateUtil.getCurDateStr());
//		params.add(accountVO.getNextRPQDate());
//		params.add(accountVO.getBaseCurrency());
//		if (!StrUtils.isEmpty(jsonPaging.getOrderStr()) && "undefine".equalsIgnoreCase(StrUtils.getString(jsonPaging.getOrderStr())))
//			params.add(jsonPaging.getOrderStr());
//		else {
//			params.add("");
//		}
//		params.add((jsonPaging.getPage()-1)*jsonPaging.getRows());
//		params.add(jsonPaging.getRows());
//		List resultList=springJdbcQueryManager.springJdbcQueryForList(sql.toString(), params.toArray());
//		
//		//params.add(total);
//		params=new ArrayList();
//		sql=new StringBuilder();
//		sql.append(" call pro_getaccountlisttotal(?,?,?,?,?,?);");
//		params.add(accountVO.getIfaId());
//		params.add(accountVO.getDistributorId());
//		params.add(accountVO.getOpenStatus());
////		params.add(accountVO.getIsValid());
//		params.add(accountVO.getIfafirmId());
//		params.add(accountVO.getNextRPQDate());
//		params.add(DateUtil.getCurDateStr());
//		List totalList=springJdbcQueryManager.springJdbcQueryForList(sql.toString(), params.toArray());
//		if(null!=totalList && !totalList.isEmpty()){
//			HashMap map=(HashMap)totalList.get(0);
//			if(map.containsKey("count(*)")){
//				Object total=map.get("count(*)");
//				if(null!=total){
//					jsonPaging.setTotal(Integer.valueOf(total.toString()));
//				}
//			}
//		}
//		
//		//int size=resultList.size();
//		List<AccountVO> list=new ArrayList<AccountVO>();
//		if(null!=resultList){
//			Iterator it=resultList.iterator();
//			while (it.hasNext()) {
//				HashMap map = (HashMap) it.next();
//				Object id=getMapObject(map, "id");
//				Object nextDoc=getMapObject(map, "docDate");
//			    Object nextRpq=getMapObject(map, "rpqDate");
//			    Object riskLevel=getMapObject(map, "risk_level");
//			    AccountVO vo=new AccountVO();
//			    if(null==id )
//			    	continue;
//			    InvestorAccount  investorAccount=(InvestorAccount)this.baseDao.get(InvestorAccount.class,id.toString());
//				vo.setId(investorAccount.getId());
//				vo.setAccountNo(investorAccount.getAccountNo());
//				vo.setAccType("I".equals(investorAccount.getAccType())?"Indiviual":"Joint");
//				vo.setBaseCurrency(null!=investorAccount.getBaseCurrency()?investorAccount.getBaseCurrency():"");
//				vo.setBaseCurName(sysParamService.findNameByCode(vo.getBaseCurrency(), langCode));
//				vo.setCies(null!=investorAccount.getCies() && "1".equals(investorAccount.getCies())?"CIES":"");
//				vo.setDistributorId(null!=investorAccount.getDistributor()?investorAccount.getDistributor().getId():"");
//				vo.setFromType(investorAccount.getFromType());
//				vo.setMemberId(investorAccount.getMember().getId());
//				vo.setOpenStatus(investorAccount.getOpenStatus());
//				if("-1".equals(vo.getOpenStatus())){
//					//approvalCount++;
//				}
//				if("1".equals(investorAccount.getOpenStatus())){
//					String accountNo="";
//					String subAccountNo="";
//					if("1".equals(investorAccount.getSubFlag())){
//						subAccountNo=investorAccount.getAccountNo();
//					}else {
//						accountNo=investorAccount.getAccountNo();
//					}
//					List<CharDataVO> chartList=investorService.findAccountCurrency(accountNo, subAccountNo,accountVO.getBaseCurrency());
//					double totalAssets=0;
//					double cash=0;
//					double market=0;
//					if(null!=chartList){
//						Iterator iterator=chartList.iterator();
//						while (iterator.hasNext()) {
//							CharDataVO object = (CharDataVO) iterator.next();
//							if("Market Value".equals(object.getName())){
//								vo.setProductValue(object.getValue()>0?StrUtils.getNumberString(object.getValue()):"0");
//								market=object.getValue();
//							}else if ("Cash".equals(object.getName())) {
//								vo.setCash(object.getValue()>0?StrUtils.getNumberString(object.getValue()):"0");
//								cash=object.getValue();
//							}
//							totalAssets+=object.getValue();
//						}
//					}
//					vo.setTotalAssest(totalAssets>0?StrUtils.getNumberString(totalAssets):"0");
//					String chartStr=JsonUtil.toJson(chartList).replace("\"", "'");
//					vo.setChartData(chartStr);
//					if(totalAssets>0){
//						vo.setCashPercent(StrUtils.getNumberString(cash/totalAssets*100)+"%");
//						vo.setProductValuePercent(StrUtils.getNumberString(market/totalAssets*100)+"%");
//					}
//					try {
//						if (null != nextDoc && !"".equals(nextDoc.toString())) {
//
//							vo.setNextDCDate(String.valueOf(this.daysBetween(DateUtil.getCurDate(), DateUtil.getDate(nextDoc))));
//
//						} else {
//							vo.setNextDCDate("0");
//						}
//						if (null != nextRpq && !"".equals(nextRpq.toString())) {
//							vo.setNextRPQDate(String.valueOf(daysBetween(DateUtil.getCurDate(), DateUtil.getDate(nextRpq.toString()))));
//						} else {
//							vo.setNextRPQDate("0");
//						}
//					} catch (ParseException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				
//				vo.setFlowStatus(investorAccount.getFlowStatus());
//				vo.setFaca(null!=investorAccount.getFaca() && "1".equals(investorAccount.getFaca())?"FACA":"");
//				vo.setSubFlag(null!=investorAccount.getSubFlag()?investorAccount.getSubFlag():"");
//				vo.setLoginCode(investorAccount.getMember().getLoginCode());
//				vo.setRiskLevel(null!=riskLevel?riskLevel.toString():"0");
//				vo.setDistributor(null!=investorAccount.getDistributor()?investorAccount.getDistributor().getCompanyName():"");
//				vo.setDistributorIcon(null!=investorAccount.getDistributor()?investorAccount.getDistributor().getLogofile():"");
//				vo.setIsValid(investorAccount.getIsValid());
//				vo.setIfafirmId(investorAccount.getIfa().getIfafirm().getId());
//				MemberIfafirmBaseVO ifafirmBaseVO=ifafirmManageService.findIfafirmBase(vo.getIfafirmId(), langCode);
//				vo.setIfafirmName(null!=ifafirmBaseVO?ifafirmBaseVO.getIfafirmName():"");
//				vo.setIfafirmIcon(null!=ifafirmBaseVO?ifafirmBaseVO.getIconUrl():"");
//				vo.setIfaId(null!=investorAccount.getIfa()?investorAccount.getIfa().getId():"");
//				
//				list.add(vo);
//			}
//		}
//		jsonPaging.setList(list);
//		return jsonPaging;
//	}
	
	
	private String getMapObject(Map map, String key) {
		return map.get(key) == null ? "" : map.get(key).toString();
	}

}
