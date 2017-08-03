package com.fsll.app.ifa.market.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.fsll.app.ifa.market.service.AppInvestorAccountService;
import com.fsll.app.ifa.market.vo.AppAccountBaseInfoVO;
import com.fsll.app.ifa.market.vo.AppAccountDocInfoVO;
import com.fsll.app.ifa.market.vo.AppAccountInfoVO;
import com.fsll.app.ifa.market.vo.AppAccountItemVO;
import com.fsll.app.ifa.market.vo.AppAccountRpqInfoVO;
import com.fsll.app.ifa.market.vo.AppAccountVO;
import com.fsll.app.ifa.market.vo.AppOrderHistoryVO;
import com.fsll.app.investor.me.vo.AppMyAccountBaseInfoVO;
import com.fsll.app.investor.me.vo.AppMyAccountDocInfoVO;
import com.fsll.app.investor.me.vo.AppMyAccountInfoVO;
import com.fsll.app.investor.me.vo.AppMyAccountRpqInfoVO;
import com.fsll.app.investor.me.vo.AppPortfolioOrderHistoryVO;
//import com.fsll.appifa.market.vo.AppAccountBaseInfoVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PageHelper;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.entity.SysCountry;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.core.vo.AccessoryFileVO;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.InvestorDoc;
import com.fsll.wmes.entity.InvestorDocSc;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.OrderHistory;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioHoldAccount;
import com.fsll.wmes.entity.RpqExam;
import com.fsll.wmes.entity.RpqExamEn;

/**
 * IFA-Market投资组合服务
 * @author zxtan
 * @date 2017-03-30
 */
@Service("appIfaMarketInvestorAccountService")
public class AppInvestorAccountServiceImpl extends BaseService implements
AppInvestorAccountService {

	
	
	/**
	 * IFA客户详情的账户列表
	 * @author zxtan
	 * @date 2017-03-30
	 */
	public JsonPaging findAccountList(JsonPaging jsonPaging, String ifaMemberId,String openStatus,String langCode) {
		// TODO Auto-generated method stub
		List<AppAccountVO> voList = new ArrayList<AppAccountVO>();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from InvestorAccount a ");
		hql.append(" inner join MemberDistributor d on d.id = a.distributor.id ");
		hql.append(" inner join MemberIfa i on i.id=a.ifa.id ");
		hql.append(" inner join CrmCustomer c on c.member.id=a.member.id and c.ifa.id=i.id ");
		hql.append(" left join RpqExam r on r.relateId = a.id and r.status='1' ");
		hql.append(" where a.subFlag = '0' and i.member.id=? ");
		params.add(ifaMemberId);		
//		if(!"".equals(clientMemberId)){
//			hql.append(" and a.member.id=? ");
//			params.add(clientMemberId);
//		}
		
		hql.append(" and exists ( select s.id from InvestorAccount s where s.ifa.id=a.ifa.id and s.member.id=a.member.id ");
		
		if(null != openStatus && !"".equals(openStatus)){
			if("3,4".indexOf(openStatus)>-1){
				hql.append(" and s.openStatus = ? ");
				params.add(openStatus);
			}else if("2".equals(openStatus)) {
				hql.append(" and s.openStatus not in ('3','4') ");
			}else {
				hql.append(" and 1=2 ");
			}
		}
		hql.append(" ) ");
				
		hql.append(" order by d.companyName ");
		
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List list = jsonPaging.getList();
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				AppAccountVO vo = new AppAccountVO();
				Object[] objs = (Object[]) list.get(i);
				InvestorAccount account = (InvestorAccount) objs[0];
				MemberDistributor distributor = (MemberDistributor) objs[1];
				CrmCustomer customer = (CrmCustomer) objs[3];
				MemberBase member = customer.getMember();
				vo.setMemberId(member.getId());
				vo.setNickName(customer.getNickName());
				vo.setCompanyName(distributor.getCompanyName());
				
				boolean result = checkFacaOrCies(member.getId(), "faca");
				if(result){
					vo.setFaca("1");
				}else {
					vo.setFaca("0");
				}
				result = checkFacaOrCies(member.getId(), "cies");
				if(result){
					vo.setCies("1");
				}else {
					vo.setCies("0");
				}
				
				RpqExam rpq = null;
				if( null != objs[4] ){
					rpq = (RpqExam) objs[4];	
					vo.setRpqRiskLevel(rpq.getRiskLevel()==null?null : rpq.getRiskLevel().toString());
					try {
						Date rpqExpireDate = rpq.getExpireDate();
						if(null != rpqExpireDate){
							int days = DateUtil.daysBetween(new Date(), rpqExpireDate);
							vo.setRpqCheckDays(String.valueOf(days));
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
		    	Date docExpireDate = getDocExpireDate(account.getId());
		    	if(null != docExpireDate){
		    		try {
						int days = daysBetween(new Date(), docExpireDate);
						vo.setDocCheckDays(String.valueOf(days));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	}
				vo = findInvestorAccountList(ifaMemberId,member.getId(), distributor.getId(),openStatus, vo,langCode);
				voList.add(vo);
			}
			jsonPaging.setList(voList);
		}
		return jsonPaging;
	}
	
	/**
	 * 获取文档过期时间
	 * @author zxtan
	 * @date 2017-01-12
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
	 * 获取同一代理商下的所有账户
	 * @author zxtan
	 * @date 2017-01-12
	 * @return
	 */
	private AppAccountVO findInvestorAccountList(String ifaMemberId,String clientMemberId,String distributorId,String openStatus,AppAccountVO accountVO,String langCode){
		List<AppAccountItemVO> infoList = new ArrayList<AppAccountItemVO>();
		
		StringBuilder hql = new StringBuilder();
		hql.append(" From InvestorAccount a ");
		hql.append(" inner join MemberIfa i on i.id=a.ifa.id ");
		hql.append(" left join PortfolioHoldAccount ha on ha.account.id= a.id ");
		hql.append(" WHERE i.member.id=? and a.member.id=? and a.distributor.id = ? ");	
				
		List<Object> params = new ArrayList<Object>();
		params.add(ifaMemberId);
		params.add(clientMemberId);	
		params.add(distributorId);	
		
		if(null != openStatus && !"".equals(openStatus)){
			if("3,4".indexOf(openStatus)>-1){
				hql.append(" and a.openStatus = ? ");
				params.add(openStatus);
			}else if("2".equals(openStatus)) {
				hql.append(" and a.openStatus not in ('3','4') ");
			}else {
				hql.append(" and 1=2 ");
			}
		}
		
		hql.append(" Order By ifnull(a.subFlag,'1'),a.accountNo ");	
		
		//Date expireDate = null;
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				AppAccountItemVO vo = new AppAccountItemVO();
				Object[] objs = (Object[]) list.get(i);
				InvestorAccount account = (InvestorAccount) objs[0];
				vo.setAccountId(account.getId());
				vo.setAccountNo(account.getAccountNo());
				String accountCurrency = getParamConfigName(langCode, account.getBaseCurrency(), "currency_type");
				vo.setAccountCurrency(accountCurrency);
				vo.setSubFlag(account.getSubFlag());
				vo.setOpenStatus(account.getOpenStatus());								
				
				if(null != objs[2]){
					PortfolioHoldAccount holdAccount = (PortfolioHoldAccount) objs[2];
					PortfolioHold hold = holdAccount.getPortfolioHold();
					if(null != hold){
						vo.setPortfolioName(hold.getPortfolioName());
					}
					
					String baseCurrency = getParamConfigName(langCode, holdAccount.getBaseCurrency(), "currency_type");
					
					vo.setBaseCurrency(baseCurrency);
					vo.setMarketValue(getFormatNum(holdAccount.getMarketValue(),holdAccount.getBaseCurrency()));
					vo.setCashAvailable(getFormatNum(holdAccount.getCashAvailable(),holdAccount.getBaseCurrency()));
					vo.setCashHold(getFormatNum(holdAccount.getCashHold(),holdAccount.getBaseCurrency()));
					vo.setCashWithdrawal(getFormatNum(holdAccount.getCashWithdrawal(),holdAccount.getBaseCurrency()));
					vo.setTotalCash(getFormatNum(holdAccount.getTotalCash(),holdAccount.getBaseCurrency()));
					vo.setTotalAsset(getFormatNum(holdAccount.getTotalAsset(),holdAccount.getBaseCurrency()));										
				}
				
				infoList.add(vo);				
			}
		}
		
		accountVO.setAccountList(infoList);
		return accountVO;
	}
	
	
	/**
	 * 获取账户信息
	 * @author zxtan
	 * @date 2017-03-31
	 * @return
	 */
	public AppAccountInfoVO findAccountInfo(String accountId,String langCode){
		AppAccountInfoVO vo = new AppAccountInfoVO();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" From InvestorAccount a ");
		hql.append(" left join PortfolioHoldAccount ha on ha.account.id= a.id ");
		hql.append(" WHERE a.id=? ");	
		params.add(accountId);	
//		Date expireDate = null;
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){				
			Object[] objs = (Object[]) list.get(0);
			InvestorAccount account = (InvestorAccount) objs[0];
			vo.setAccountId(account.getId());
			vo.setAccountNo(account.getAccountNo());
			String accountCurrency = getParamConfigName(langCode, account.getBaseCurrency(), "currency_type");
			vo.setAccountCurrency(accountCurrency);
			vo.setSubFlag(account.getSubFlag());
			vo.setCies(account.getCies());
			
			MemberDistributor distributor = account.getDistributor();
			if(null != distributor){
				vo.setCompanyName(distributor.getCompanyName());
			}
					
							
			
			if(null != objs[1]){
				PortfolioHoldAccount holdAccount = (PortfolioHoldAccount) objs[1];
				PortfolioHold hold = holdAccount.getPortfolioHold();
				if(null != hold){
					vo.setPortfolioName(hold.getPortfolioName());
				}
	
				String baseCurrency = holdAccount.getBaseCurrency();
				double marketValue = holdAccount.getMarketValue()== null?0.0:holdAccount.getMarketValue();
				double cashAvailable = holdAccount.getCashAvailable()== null?0.0:holdAccount.getCashAvailable();
				double cashHold = holdAccount.getCashHold()== null?0.0:holdAccount.getCashHold();
				double cashWithdrawal = holdAccount.getCashWithdrawal()== null?0.0:holdAccount.getCashWithdrawal();
				double totalCash = holdAccount.getTotalCash()==null?0.0:holdAccount.getTotalCash();
				double totalAsset = holdAccount.getTotalAsset()==null?0.0:holdAccount.getTotalAsset();
				double marketValueWeight = 0;
				double totalCashWeight = 0;
				if(totalAsset >0){
					totalCashWeight = new BigDecimal(totalCash/totalAsset).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
					marketValueWeight = new BigDecimal(marketValue/totalAsset).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();									
				}
				vo.setTotalCashWeight(String.valueOf(totalCashWeight));
				vo.setMarketValueWeight(String.valueOf(marketValueWeight));

				vo.setBaseCurrency(baseCurrency);
				vo.setMarketValue(getFormatNum(marketValue,baseCurrency));
				vo.setCashAvailable(getFormatNum(cashAvailable,baseCurrency));
				vo.setCashHold(getFormatNum(cashHold,baseCurrency));
				vo.setCashWithdrawal(getFormatNum(cashWithdrawal,baseCurrency));
				vo.setTotalAsset(getFormatNum(totalAsset,baseCurrency));
				vo.setTotalCash(getFormatNum(totalCash,baseCurrency));
								
			}			
		}
		
		return vo;
	}
	
	/**
	 * 得到投资账户的交易记录列表
	 * @author zxtan
	 * @date 2017-03-31
	 * @param accountId 账户ID
	 * @return
	 */
	public List<AppOrderHistoryVO> findAccountOrderHistoryList(String accountId, String toCurrency,String langCode){
		List<AppOrderHistoryVO>  messList = new ArrayList<AppOrderHistoryVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from OrderHistory m ");
		hql.append(" left join InvestorAccount a on a.id=m.account.id ");
		hql.append(" left join FundInfo f on f.product.id=m.product.id ");		
		hql.append(" left join "+this.getLangString("FundInfo", langCode));
		hql.append(" s on s.id=f.id ");
		hql.append(" left join PortfolioHold p on p.id=m.portfolioHold.id ");
		hql.append(" where m.account.id=? ");
		hql.append(" order by m.updateTime desc ");	

		List params = new ArrayList();
		params.add(accountId);
						
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		String toCurrencyName = getParamConfigName(langCode, toCurrency, CommonConstants.SYS_PARAM_TYPE_CURRENCY);
		if(null != list && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[])list.get(i);
				OrderHistory history = (OrderHistory) objs[0];
				InvestorAccount account = (InvestorAccount) objs[1];
				AppOrderHistoryVO vo = new AppOrderHistoryVO();
				FundInfoSc fundInfo = new FundInfoSc();
				
				if(null != objs[3]){
					BeanUtils.copyProperties(objs[3], fundInfo);
					vo.setFundName(fundInfo.getFundName());
				}
				if(null != objs[4]){
					PortfolioHold portfolioHold = (PortfolioHold) objs[4];
					vo.setPortfolioName(portfolioHold.getPortfolioName());
				}
				
				vo.setId(history.getId());
				//vo.setOrderNo(history.getOrderNo());
				vo.setAccountNo(account.getAccountNo());
				String currencyCode = history.getCurrencyCode();
				
				if(null != history.getCommissionUnit()){
					vo.setCommissionUnit(history.getCommissionUnit().toString());
				}
				
				//汇率转换
				if(null != toCurrency && !"".equals(toCurrency)){
					vo.setCurrencyCode(toCurrencyName);
					if(null != history.getCommissionPrice()){
						vo.setCommissionPrice(getFormatNumByCurrency(history.getCommissionPrice(), currencyCode, toCurrency));
					}
					if(null != history.getCommissionAmount()){
						vo.setCommissionAmount(getFormatNumByCurrency(history.getCommissionAmount(),currencyCode,toCurrency));
					}		
					if(null != history.getTransactionAmount()){
						vo.setTransactionAmount(getFormatNumByCurrency(history.getTransactionAmount(),currencyCode,toCurrency));
					}
					if(null != history.getFee()){
						vo.setFee(getFormatNumByCurrency(history.getFee(),currencyCode,toCurrency));
					}
				}else {//原始数据
					vo.setCurrencyCode(getParamConfigName(langCode, currencyCode, CommonConstants.SYS_PARAM_TYPE_CURRENCY));
					if(null != history.getCommissionPrice()){
						vo.setCommissionPrice(getFormatNum(history.getCommissionPrice(),currencyCode));
					}
					if(null != history.getCommissionAmount()){
						vo.setCommissionAmount(getFormatNum(history.getCommissionAmount(),currencyCode));
					}		
					if(null != history.getTransactionAmount()){
						vo.setTransactionAmount(getFormatNum(history.getTransactionAmount(),currencyCode));
					}
					if(null != history.getFee()){
						vo.setFee(getFormatNum(history.getFee(),currencyCode));
					}
				}
				
				
				if(null != history.getTransactionUnit()){
					vo.setTransactionUnit(history.getTransactionUnit().toString());
				}	
				
				vo.setOrderType(history.getOrderType());
				vo.setOrderDate(DateUtil.dateToDateString(history.getOrderDate(), "yyyy-MM-dd HH:mm:ss"));
				vo.setCloseTime(DateUtil.dateToDateString(history.getCloseTime(), "yyyy-MM-dd HH:mm:ss"));
				vo.setUpdateTime(DateUtil.dateToDateString(history.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
				vo.setStatus(history.getStatus());
				vo.setHeadId((long) (0));
				messList.add(vo);
			}
			
		}
				
		return messList;
	}
	
	
	/**
	 * 获取账户RPQ信息
	 * @author zxtan
	 * @date 2017-03-31
	 * @return
	 */
	public AppAccountRpqInfoVO findAccountRPQInfo(String accountId,String langCode){
		AppAccountRpqInfoVO vo = new AppAccountRpqInfoVO();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" From InvestorAccount a ");
		hql.append(" inner join RpqExam r on r.relateId= a.id ");
		hql.append(" inner join "+getLangString("RpqExam", langCode)+" f on f.id= r.id ");
		hql.append(" WHERE a.subFlag='0' and a.isValid='1' and r.status='1' and exists ( ");	
		hql.append(" select t.id from InvestorAccount t where t.id=? and t.distributor.id=a.distributor.id ");
		hql.append(" ) ");
		hql.append(" ORDER BY r.expireDate DESC ");
		params.add(accountId);	
		
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){				
			Object[] objs = (Object[]) list.get(0);
			InvestorAccount account = (InvestorAccount) objs[0];
			RpqExam rpq = (RpqExam) objs[1];			
			vo.setRiskLevel(String.valueOf(account.getRpqLevel()));
			RpqExamEn rpqEn = new RpqExamEn();
			BeanUtils.copyProperties(objs[2], rpqEn);
			vo.setRiskLevel(String.valueOf(account.getRpqLevel()));
			if(StringUtils.isNotBlank(rpqEn.getUserRiskResult())){
				vo.setRiskResult(rpqEn.getUserRiskResult());
				vo.setRiskRemark(rpqEn.getUserRiskRemark());
			}else {
				vo.setRiskResult(rpqEn.getRiskResult());
				vo.setRiskRemark(rpqEn.getRiskRemark());
			}
			try {
				Date rpqExpireDate = rpq.getExpireDate();	
				if(null != rpqExpireDate){
					int days = DateUtil.daysBetween(new Date(), rpqExpireDate);
					vo.setCheckDays(String.valueOf(days));
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return vo;
	}
	
	
	/**
	 * 获取账户DOC信息
	 * @author zxtan
	 * @date 2017-03-31
	 * @return
	 */
	public List<AppAccountDocInfoVO> findAccountDocCheckInfo(String accountId,String langCode){
		List<AppAccountDocInfoVO> voList = new ArrayList<AppAccountDocInfoVO>();
		
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" From InvestorAccount a ");
		hql.append(" inner join InvestorDoc d on d.account.id= a.id ");
		hql.append(" inner join "+getLangString("InvestorDoc", langCode)+" l on l.id= d.id ");
		hql.append(" WHERE a.subFlag='0' and a.isValid='1' and d.isValid='1' and exists ( ");	
		hql.append(" select t.id from InvestorAccount t where t.id=? and t.distributor.id=a.distributor.id ");
		hql.append(" ) ");
		hql.append(" order by d.expireDate ");
		params.add(accountId);	
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){	
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				//InvestorAccount account = (InvestorAccount) objs[0];
				InvestorDoc doc = (InvestorDoc) objs[1];		
				InvestorDocSc docLang = new InvestorDocSc();
				BeanUtils.copyProperties(objs[2], docLang);
				AppAccountDocInfoVO vo = new AppAccountDocInfoVO();
				vo.setDocName(docLang.getDocName());
				try {
					Date rpqExpireDate = doc.getExpireDate();
					if(null != rpqExpireDate){
						int days = DateUtil.daysBetween(new Date(), rpqExpireDate);
						vo.setCheckDays(String.valueOf(days));
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				vo.setIsNecessary(doc.getIsNecessary());
				vo.setCheckStatus(doc.getCheckStatus());
				List<AccessoryFile> fileList = findFileAttr(doc.getId(), "investor_doc");
				List<AccessoryFileVO> voFileList = new ArrayList<AccessoryFileVO>();
				if(null != fileList && !fileList.isEmpty()){
					for (AccessoryFile item : fileList) {
						AccessoryFileVO tmpVO = new AccessoryFileVO();
						tmpVO.setId(item.getId());
						tmpVO.setFileName(item.getFileName());
						tmpVO.setFilePath(PageHelper.getImgUrlForWS(item.getFilePath()));
						tmpVO.setFileType(item.getFileType());
						tmpVO.setModuleType(item.getModuleType());
						tmpVO.setLangCode(item.getLangCode());
						tmpVO.setCreateTime(DateUtil.dateToDateString(item.getCreateTime(), CommonConstants.FORMAT_DATE_TIME));
						voFileList.add(tmpVO);
					}
				}
				vo.setFileList(voFileList);
				
				voList.add(vo);
			}
		}
		
		return voList;
	}
	
//	/**
//	 * 获取附件信息
//	 * @param relateId
//	 * @return
//	 */
//	private List<AccessoryFile> findAccessoryFileList(String relateId) {
//		List<AccessoryFile> list = new ArrayList<AccessoryFile>();
//		String hql = "from AccessoryFile where relateId=? order by createTime ";
//		List<String> params = new ArrayList<String>();
//		params.add(relateId);
//		list = this.baseDao.find(hql, params.toArray(), false);
//		return list;
//	}
	
	
	/**
	 * 获取账户基本信息
	 * @author zxtan
	 * @date 2017-01-16
	 * @return
	 */
	public AppAccountBaseInfoVO findAccountBaseInfo(String accountId,String langCode){

		AppAccountBaseInfoVO vo = new AppAccountBaseInfoVO();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" From InvestorAccount a ");
		hql.append(" inner join MemberBase m on m.id= a.member.id ");
		hql.append(" inner join MemberIndividual d on d.member.id= m.id ");
		hql.append(" left join SysCountry c on c.id= d.country.id ");
		hql.append(" WHERE a.isValid='1' and a.id=? ");
		params.add(accountId);	
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){	
			Object[] objs = (Object[]) list.get(0);
			InvestorAccount account = (InvestorAccount) objs[0];
			MemberBase member = (MemberBase) objs[1];		
			MemberIndividual individual = (MemberIndividual) objs[2];
			SysCountry country = new SysCountry();
			if(null != objs[3]){
				country =(SysCountry) objs[3];
			}
				
			vo.setAccType(account.getAccType());
			vo.setCies(account.getCies());
			vo.setDividend(account.getDividend());
			vo.setBaseCurrency(account.getBaseCurrency());
			vo.setPurpose(account.getPurpose());
			
			vo.setFirstName(individual.getFirstName());
			vo.setLastName(individual.getLastName());
			vo.setNameChn(individual.getNameChn());
			vo.setNickName(member.getNickName());
			if(null != country){
				if("sc".equalsIgnoreCase(langCode)){
					vo.setCountry(country.getNameSc());
				}else if ("tc".equalsIgnoreCase(langCode)) {
					vo.setCountry(country.getNameTc());
				}else {
					vo.setCountry(country.getNameEn());
				}
			}				
		}
		
		return vo;
	}
		
	
	
//	/**
//	 * 获取账户基本信息
//	 * @author zxtan
//	 * @date 2017-03-24
//	 * @return
//	 */
//	public AppAccountBaseInfoVO findAccountBaseInfo(String ifaMemberId,String clientMemberId,String langCode){
//
//		AppAccountBaseInfoVO vo = new AppAccountBaseInfoVO();
//		List<Object> params = new ArrayList<Object>();
//		StringBuilder hql = new StringBuilder();
//		hql.append(" From MemberBase m ");
//		hql.append(" inner join MemberIndividual d on d.member.id= m.id ");
//		hql.append(" left join SysCountry c on c.id= d.nationality.id ");
//		hql.append(" left join SysParamConfig ed on ed.configCode= d.education ");
//		hql.append(" left join SysParamConfig em on em.configCode= d.employment ");
//		hql.append(" left join SysParamConfig oc on oc.configCode= d.occupation ");
//		hql.append(" WHERE m.isValid='1' and m.id=? ");
//		params.add(clientMemberId);	
//		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
//		if(null != list && !list.isEmpty()){	
//			Object[] objs = (Object[]) list.get(0);
//			MemberBase member = (MemberBase) objs[0];		
//			MemberIndividual individual = (MemberIndividual) objs[1];
//			
//			
//			vo.setFirstName(individual.getFirstName());
//			vo.setLastName(individual.getLastName());
//			vo.setNameChn(individual.getNameChn());
//			vo.setNickName(member.getNickName());			
//			vo.setGender(individual.getGender());
//			vo.setBorn(DateUtil.dateToDateString(individual.getBorn(), CommonConstants.FORMAT_DATE));
//			vo.setCertNo(individual.getCertNo());
//			if(null != objs[2]){
//				SysCountry country = (SysCountry) objs[2];
//				if("sc".equalsIgnoreCase(langCode)){
//					vo.setNationality(country.getNameSc());
//				}else if ("tc".equalsIgnoreCase(langCode)) {
//					vo.setNationality(country.getNameTc());
//				}else {
//					vo.setNationality(country.getNameEn());
//				}
//			}
//			
//			if(null != objs[3]){
//				SysParamConfig edu = (SysParamConfig) objs[3];
//				if("sc".equalsIgnoreCase(langCode)){
//					vo.setEducation(edu.getNameSc());
//				}else if ("tc".equalsIgnoreCase(langCode)) {
//					vo.setEducation(edu.getNameTc());
//				}else {
//					vo.setEducation(edu.getNameEn());
//				}
//			}
//			
//			if(null != objs[4]){
//				SysParamConfig emp = (SysParamConfig) objs[4];
//				if("sc".equalsIgnoreCase(langCode)){
//					vo.setEmployment(emp.getNameSc());
//				}else if ("tc".equalsIgnoreCase(langCode)) {
//					vo.setEmployment(emp.getNameTc());
//				}else {
//					vo.setEmployment(emp.getNameEn());
//				}
//			}
//			
//			if(null != objs[5]){
//				SysParamConfig ocu = (SysParamConfig) objs[5];
//				if("sc".equalsIgnoreCase(langCode)){
//					vo.setOccupation(ocu.getNameSc());
//				}else if ("tc".equalsIgnoreCase(langCode)) {
//					vo.setOccupation(ocu.getNameTc());
//				}else {
//					vo.setOccupation(ocu.getNameEn());
//				}
//			}							
//		}
//		
//		return vo;
//	}

}
