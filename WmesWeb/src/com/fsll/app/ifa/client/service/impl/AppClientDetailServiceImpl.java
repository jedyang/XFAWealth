package com.fsll.app.ifa.client.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.fsll.app.ifa.client.service.AppClientDetailService;
import com.fsll.app.ifa.client.vo.AppAccountBaseInfoVO;
import com.fsll.app.ifa.client.vo.AppAccountItemVO;
import com.fsll.app.ifa.client.vo.AppAccountVO;
import com.fsll.app.ifa.client.vo.AppCrmMemoVO;
import com.fsll.app.ifa.client.vo.AppCrmProposalItemVO;
import com.fsll.app.ifa.client.vo.AppOrderHistoryVO;
import com.fsll.app.ifa.client.vo.AppPortfolioHoldItemVO;
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
import com.fsll.wmes.entity.CrmMemo;
import com.fsll.wmes.entity.CrmProposal;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.InvestorDoc;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.OrderHistory;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioHoldAccount;
import com.fsll.wmes.entity.PortfolioHoldReturn;
import com.fsll.wmes.entity.RpqExam;

/**
 * IFA客户详情接口服务类实现
 * @author zxtan
 * @date 2017-03-24
 */
@Service("appIfaClientDetailService")
public class AppClientDetailServiceImpl extends BaseService implements
		AppClientDetailService {


	/**
	 * 获取IFA客户详情的组合列表
	 * @author zxtan
	 * @date 2017-03-24
	 */
	public JsonPaging findPortfolioHoldList(JsonPaging jsonPaging,
			String ifaMemberId, String clientMemberId,String periodCode,String toCurrency,String langCode) {		
		StringBuilder hql = new StringBuilder();
		hql.append("from PortfolioHold h ");
		hql.append(" inner join MemberIfa i on i.id=h.ifa.id ");
		hql.append(" inner join CrmCustomer c on c.member.id=h.member.id and c.ifa.id=i.id ");
		hql.append(" left join PortfolioHoldReturn r on r.portfolioHold.id=h.id and r.periodCode=? ");
		hql.append(" where i.member.id=? and h.member.id=? ");
		hql.append(" order by h.totalAsset desc ");
		List<Object> params = new ArrayList<Object>();
		params.add(periodCode);
		params.add(ifaMemberId);
		params.add(clientMemberId);
		
		jsonPaging = baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List<AppPortfolioHoldItemVO> voList = new ArrayList<AppPortfolioHoldItemVO>();
		List list = jsonPaging.getList();
		String toCurrencyName = getParamConfigName(langCode, toCurrency, CommonConstants.SYS_PARAM_TYPE_CURRENCY);
		if(null != list &&!list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				PortfolioHold hold = (PortfolioHold) objs[0];
				MemberIfa ifa = (MemberIfa) objs[1];
				
				AppPortfolioHoldItemVO vo = new AppPortfolioHoldItemVO();
				vo.setId(hold.getId());
				vo.setPortfolioName(hold.getPortfolioName());
				if(StringUtils.isBlank(toCurrency)){
					vo.setTotalAsset(getFormatNum(hold.getTotalAsset(),hold.getBaseCurrency()));
					vo.setBaseCurrency(getParamConfigName(langCode, hold.getBaseCurrency(), CommonConstants.SYS_PARAM_TYPE_CURRENCY));
				}else {
					vo.setTotalAsset(getFormatNumByCurrency(hold.getTotalAsset(),hold.getBaseCurrency(),toCurrency));
					vo.setBaseCurrency(toCurrencyName);
				}
				
				vo.setRiskLevel(String.valueOf(hold.getRiskLevel()));
				double totalReturnRate = hold.getTotalReturnRate()==null?0:hold.getTotalReturnRate();
				double ifaMin = ifa.getPortfolioCriticalValue()==null?0:Double.parseDouble(ifa.getPortfolioCriticalValue())/100.0;
				double ifaMax = ifa.getPortfolioReturnValue()==null?0:Double.parseDouble(ifa.getPortfolioReturnValue())/100.0;
				if(totalReturnRate> ifaMax || totalReturnRate <ifaMin){
					vo.setAlertFlag("1");
				}else {
					vo.setAlertFlag("0");
				}
				
				try {
					int investDays = DateUtil.daysBetween(hold.getCreateTime(), DateUtil.getCurDate());
					vo.setInvestDays(String.valueOf(investDays));					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(null != objs[3]){
					PortfolioHoldReturn holdReturn = (PortfolioHoldReturn) objs[3];
					vo.setPeriodCode(holdReturn.getPeriodCode());
					vo.setIncrease(getFormatNumByPer(holdReturn.getIncrease()));
				}
				
				voList.add(vo);
				
			}
		
			jsonPaging.setList(voList);
		}
		
		
		return jsonPaging;
	}
	
	/**
	 * 获取IFA客户详情的销售记录列表
	 * @author zxtan
	 * @date 2017-03-24
	 */
	public JsonPaging findCrmMemoList(JsonPaging jsonPaging,String ifaMemberId, String clientMemberId) {
		StringBuilder hql = new StringBuilder();
		hql.append("from CrmMemo m ");
		hql.append(" inner join MemberIfa i on i.id=m.ifa.id ");
		hql.append(" inner join CrmCustomer c on c.member.id=m.member.id and c.ifa.id=i.id ");
		hql.append(" where i.member.id=? and m.member.id=? ");
		hql.append(" order by m.lastModify desc ");
		List<Object> params = new ArrayList<Object>();
		params.add(ifaMemberId);
		params.add(clientMemberId);
		
		jsonPaging = baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List<AppCrmMemoVO> voList = new ArrayList<AppCrmMemoVO>();
		List list = jsonPaging.getList();
		if(null != list &&!list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				CrmMemo memo = (CrmMemo) objs[0];
//				MemberIfa ifa = (MemberIfa) objs[1];
				
				AppCrmMemoVO vo = new AppCrmMemoVO();
				vo.setId(memo.getId());
				vo.setMemoText(memo.getMemoText());
				vo.setCreateTime(DateUtil.dateToDateString(memo.getCreateTime(), CommonConstants.FORMAT_DATE_TIME));
				vo.setLastModify(DateUtil.dateToDateString(memo.getLastModify(), CommonConstants.FORMAT_DATE_TIME));
				List<AccessoryFile> fileList = findFileAttr(memo.getId(),"crm_memo");
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
		
			jsonPaging.setList(voList);
		}
		
		
		return jsonPaging;
	}
	
	/**
	 * 更新/删除IFA客户详情的销售记录
	 * @author zxtan
	 * @date 2017-03-24
	 */
	public boolean updateCrmMemo(String updateType, String id,	String memoText) {
		CrmMemo info = (CrmMemo) baseDao.get(CrmMemo.class, id);
		if("delete".equalsIgnoreCase(updateType)){
			baseDao.delete(info);
			return true;
		}else if("update".equalsIgnoreCase(updateType)) {
			info.setMemoText(memoText);
			info.setLastModify(new Date());
			baseDao.update(info);
			return true;
		}
		return false;
	}
	
	
	/**
	 * IFA客户详情的账户列表
	 * @author zxtan
	 * @date 2017-03-24
	 */
	public List<AppAccountVO> findAccountList(String ifaMemberId,String clientMemberId,String toCurrency,String langCode) {
		// TODO Auto-generated method stub
		List<AppAccountVO> voList = new ArrayList<AppAccountVO>();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from InvestorAccount a ");
		hql.append(" inner join MemberDistributor d on d.id = a.distributor.id ");
		hql.append(" inner join MemberIfa i on i.id=a.ifa.id ");
		hql.append(" left join RpqExam r on r.relateId = a.id and r.status='1' ");
		hql.append(" where a.subFlag = '0' and i.member.id=? and a.member.id=? ");
		params.add(ifaMemberId);
		params.add(clientMemberId);
		
		hql.append(" order by d.companyName ");
		
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				AppAccountVO vo = new AppAccountVO();
				Object[] objs = (Object[]) list.get(i);
				InvestorAccount account = (InvestorAccount) objs[0];
				MemberDistributor distributor = (MemberDistributor) objs[1];
				vo.setCompanyName(distributor.getCompanyName());
				RpqExam rpq = null;
				if( null != objs[3] ){
					rpq = (RpqExam) objs[3];	
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
				vo = findInvestorAccountList(ifaMemberId,clientMemberId, distributor.getId(),toCurrency,langCode, vo);
				voList.add(vo);
			}
		}
		return voList;
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
	 * 获取同一代理商下的所有账户
	 * @author zxtan
	 * @date 2017-01-12
	 * @return
	 */
	private AppAccountVO findInvestorAccountList(String ifaMemberId,String clientMemberId,String distributorId,String toCurrency,String langCode,AppAccountVO accountVO){
		List<AppAccountItemVO> infoList = new ArrayList<AppAccountItemVO>();
		
		StringBuilder hql = new StringBuilder();
		hql.append(" From InvestorAccount a ");
		hql.append(" inner join MemberIfa i on i.id=a.ifa.id ");
		hql.append(" left join PortfolioHoldAccount ha on ha.account.id= a.id ");
		hql.append(" WHERE i.member.id=? and a.member.id=? and a.distributor.id = ? ");	
		hql.append(" Order By ifnull(a.subFlag,'1'),a.accountNo ");	
		
		List<Object> params = new ArrayList<Object>();
		params.add(ifaMemberId);
		params.add(clientMemberId);	
		params.add(distributorId);		
		
		//Date expireDate = null;
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		String toCurrencyName = getParamConfigName(langCode, toCurrency, CommonConstants.SYS_PARAM_TYPE_CURRENCY);
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
					
					if(StringUtils.isBlank(toCurrency)){
						vo.setBaseCurrency(getParamConfigName(langCode, holdAccount.getBaseCurrency(), CommonConstants.SYS_PARAM_TYPE_CURRENCY));
						vo.setMarketValue(getFormatNum(holdAccount.getMarketValue(),holdAccount.getBaseCurrency()));
						vo.setCashAvailable(getFormatNum(holdAccount.getCashAvailable(),holdAccount.getBaseCurrency()));
						vo.setCashHold(getFormatNum(holdAccount.getCashHold(),holdAccount.getBaseCurrency()));
						vo.setCashWithdrawal(getFormatNum(holdAccount.getCashWithdrawal(),holdAccount.getBaseCurrency()));
						vo.setTotalCash(getFormatNum(holdAccount.getTotalCash(),holdAccount.getBaseCurrency()));
						vo.setTotalAsset(getFormatNum(holdAccount.getTotalAsset(),holdAccount.getBaseCurrency()));
					}else {
						vo.setBaseCurrency(toCurrencyName);
						vo.setMarketValue(getFormatNumByCurrency(holdAccount.getMarketValue(),holdAccount.getBaseCurrency(),toCurrency));
						vo.setCashAvailable(getFormatNumByCurrency(holdAccount.getCashAvailable(),holdAccount.getBaseCurrency(),toCurrency));
						vo.setCashHold(getFormatNumByCurrency(holdAccount.getCashHold(),holdAccount.getBaseCurrency(),toCurrency));
						vo.setCashWithdrawal(getFormatNumByCurrency(holdAccount.getCashWithdrawal(),holdAccount.getBaseCurrency(),toCurrency));
						vo.setTotalCash(getFormatNumByCurrency(holdAccount.getTotalCash(),holdAccount.getBaseCurrency(),toCurrency));
						vo.setTotalAsset(getFormatNumByCurrency(holdAccount.getTotalAsset(),holdAccount.getBaseCurrency(),toCurrency));
					}
				}
				
				infoList.add(vo);				
			}
		}
		
		accountVO.setAccountList(infoList);
		return accountVO;
	}
	
	
	/**
	 * 得到投资客户的投资方案列表
	 * @param ifaMemberId ifa member id
	 * @param clientMemberId 投资者member id
	 * @return
	 */
	public List<AppCrmProposalItemVO> findProposalList(String ifaMemberId,String clientMemberId,String toCurrency,String langCode){
		List<AppCrmProposalItemVO>  voList = new ArrayList<AppCrmProposalItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from CrmProposal m ");
		hql.append(" inner join MemberIfa i on i.id=m.ifaMember.id ");
		hql.append(" where i.member.id=? and m.member.id=? and m.isValid='1' ");
		hql.append(" order by m.lastUpdate desc ");
		List params = new ArrayList();
		params.add(ifaMemberId);
		params.add(clientMemberId);		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		String toCurrencyName = getParamConfigName(langCode, toCurrency, CommonConstants.SYS_PARAM_TYPE_CURRENCY);
		if(null != list && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[]) list.get(i);
				CrmProposal info = (CrmProposal) objs[0];
				AppCrmProposalItemVO vo =new AppCrmProposalItemVO();
				vo.setId(info.getId());
				vo.setProposalName(info.getProposalName());
				if(StringUtils.isBlank(toCurrency)){
					vo.setBaseCurrId(getParamConfigName(langCode, info.getBaseCurrId(), CommonConstants.SYS_PARAM_TYPE_CURRENCY));
					vo.setInitialInvestAmount(getFormatNum(info.getInitialInvestAmount(),info.getBaseCurrId()));
				}else {
					vo.setBaseCurrId(toCurrencyName);
					vo.setInitialInvestAmount(getFormatNumByCurrency(info.getInitialInvestAmount(),info.getBaseCurrId(),toCurrency));
				}
				vo.setStatus(info.getStatus());
				vo.setLastUpdate(DateUtil.dateToDateString(info.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
				vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
				
				voList.add(vo);
			}
		}
		return voList;
	}
	
	
	/**
	 * 得到投资客户的交易记录列表
	 * @author zxtan
	 * @date 2017-03-24
	 * @return
	 */
	public JsonPaging findOrderHistoryList(JsonPaging jsonPaging,String ifaMemberId, String clientMemberId,String toCurrency,String langCode){
		List<AppOrderHistoryVO>  voList = new ArrayList<AppOrderHistoryVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from OrderHistory m ");
		hql.append(" inner join InvestorAccount a on a.id=m.account.id ");
		hql.append(" inner join FundInfo f on f.product.id=m.product.id ");		
		hql.append(" inner join "+this.getLangString("FundInfo", langCode)+" s on s.id=f.id ");
		hql.append(" inner join PortfolioHold p on p.id=m.portfolioHold.id ");
		hql.append(" inner join MemberIfa i on i.id=m.ifa.id");
		hql.append(" where i.member.id=? and m.member.id=?  ");//and m.status in ('2','3')
		hql.append(" order by m.updateTime desc ");	

		List params = new ArrayList();
		params.add(ifaMemberId);
		params.add(clientMemberId);
				
		jsonPaging = baseDao.selectJsonPagingNoTotal(hql.toString(), params.toArray(), jsonPaging, false);
		
		List list = jsonPaging.getList();
		String toCurrencyName = getParamConfigName(langCode, toCurrency, CommonConstants.SYS_PARAM_TYPE_CURRENCY);
		if(null != list && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[])list.get(i);
				OrderHistory history = (OrderHistory) objs[0];
				InvestorAccount account = (InvestorAccount) objs[1];
				AppOrderHistoryVO vo = new AppOrderHistoryVO();
				FundInfoSc fundInfo = new FundInfoSc();
				
				BeanUtils.copyProperties(objs[3], fundInfo);
				vo.setFundName(fundInfo.getFundName());
			
				PortfolioHold portfolioHold = (PortfolioHold) objs[4];
				vo.setPortfolioName(portfolioHold.getPortfolioName());
				
				
				vo.setId(history.getId());
				vo.setAccountNo(account.getAccountNo());
				if(StringUtils.isBlank(toCurrency)){
					vo.setCurrencyCode(getParamConfigName(langCode, history.getCurrencyCode(), CommonConstants.SYS_PARAM_TYPE_CURRENCY));
					vo.setCommissionPrice(getFormatNum(history.getCommissionPrice(),history.getCurrencyCode()));
					vo.setCommissionAmount(getFormatNum(history.getCommissionAmount(),history.getCurrencyCode()));
					vo.setTransactionAmount(getFormatNum(history.getTransactionAmount(),history.getCurrencyCode()));
					vo.setFee(getFormatNum(history.getFee(),history.getCurrencyCode()));					
				}else {
					vo.setCurrencyCode(toCurrencyName);
					vo.setCommissionPrice(getFormatNumByCurrency(history.getCommissionPrice(),history.getCurrencyCode(),toCurrency));
					vo.setCommissionAmount(getFormatNumByCurrency(history.getCommissionAmount(),history.getCurrencyCode(),toCurrency));
					vo.setTransactionAmount(getFormatNumByCurrency(history.getTransactionAmount(),history.getCurrencyCode(),toCurrency));
					vo.setFee(getFormatNumByCurrency(history.getFee(),history.getCurrencyCode(),toCurrency));					
				}
				
				vo.setCommissionUnit(String.valueOf(history.getCommissionUnit()));

				vo.setTransactionUnit( String.valueOf(history.getTransactionUnit()));		
								
				vo.setOrderType(history.getOrderType());
				vo.setOrderDate(DateUtil.dateToDateString(history.getOrderDate(), "yyyy-MM-dd HH:mm:ss"));
				vo.setCloseTime(DateUtil.dateToDateString(history.getCloseTime(), "yyyy-MM-dd HH:mm:ss"));
				vo.setUpdateTime(DateUtil.dateToDateString(history.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
				String status = "-1";
				if(",3,".indexOf(","+history.getStatus()+",")>-1){
					status = "3";
				}else if(",4,5,-1,".indexOf(history.getStatus())>-1){
					status = "-1";
				}else {
					status = "1";
				}
				vo.setStatus(status);
				vo.setIfAip(history.getIfAip());
				vo.setSwitchFlag(history.getSwitchFlag());
				
				voList.add(vo);
			}
		}
		jsonPaging.setList(voList);
		return jsonPaging;	
	}
	
	/**
	 * 获取账户基本信息
	 * @author zxtan
	 * @date 2017-03-24
	 * @return
	 */
	public AppAccountBaseInfoVO findAccountBaseInfo(String ifaMemberId,String clientMemberId,String langCode){

		AppAccountBaseInfoVO vo = new AppAccountBaseInfoVO();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" From MemberBase m ");
		hql.append(" inner join MemberIndividual d on d.member.id= m.id ");
		hql.append(" left join SysCountry c on c.id= d.nationality.id ");
		hql.append(" left join SysParamConfig ed on ed.configCode= d.education ");
		hql.append(" left join SysParamConfig em on em.configCode= d.employment ");
		hql.append(" left join SysParamConfig oc on oc.configCode= d.occupation ");
		hql.append(" WHERE m.isValid='1' and m.id=? ");
		params.add(clientMemberId);	
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){	
			Object[] objs = (Object[]) list.get(0);
			MemberBase member = (MemberBase) objs[0];		
			MemberIndividual individual = (MemberIndividual) objs[1];
			
			
			vo.setFirstName(individual.getFirstName());
			vo.setLastName(individual.getLastName());
			vo.setNameChn(individual.getNameChn());
			vo.setNickName(member.getNickName());			
			vo.setGender(individual.getGender());
			vo.setBorn(DateUtil.dateToDateString(individual.getBorn(), CommonConstants.FORMAT_DATE));
			vo.setCertNo(individual.getCertNo());
			if(null != objs[2]){
				SysCountry country = (SysCountry) objs[2];
				if("sc".equalsIgnoreCase(langCode)){
					vo.setNationality(country.getNameSc());
				}else if ("tc".equalsIgnoreCase(langCode)) {
					vo.setNationality(country.getNameTc());
				}else {
					vo.setNationality(country.getNameEn());
				}
			}
			
			if(null != objs[3]){
				SysParamConfig edu = (SysParamConfig) objs[3];
				if("sc".equalsIgnoreCase(langCode)){
					vo.setEducation(edu.getNameSc());
				}else if ("tc".equalsIgnoreCase(langCode)) {
					vo.setEducation(edu.getNameTc());
				}else {
					vo.setEducation(edu.getNameEn());
				}
			}
			
			if(null != objs[4]){
				SysParamConfig emp = (SysParamConfig) objs[4];
				if("sc".equalsIgnoreCase(langCode)){
					vo.setEmployment(emp.getNameSc());
				}else if ("tc".equalsIgnoreCase(langCode)) {
					vo.setEmployment(emp.getNameTc());
				}else {
					vo.setEmployment(emp.getNameEn());
				}
			}
			
			if(null != objs[5]){
				SysParamConfig ocu = (SysParamConfig) objs[5];
				if("sc".equalsIgnoreCase(langCode)){
					vo.setOccupation(ocu.getNameSc());
				}else if ("tc".equalsIgnoreCase(langCode)) {
					vo.setOccupation(ocu.getNameTc());
				}else {
					vo.setOccupation(ocu.getNameEn());
				}
			}							
		}
		
		return vo;
	}

}
