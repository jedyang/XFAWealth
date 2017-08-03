package com.fsll.app.investor.me.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.investor.me.service.AppMyAccountService;
import com.fsll.app.investor.me.vo.AppMyAccountBaseInfoVO;
import com.fsll.app.investor.me.vo.AppMyAccountDocInfoVO;
import com.fsll.app.investor.me.vo.AppMyAccountInfoVO;
import com.fsll.app.investor.me.vo.AppMyAccountRpqInfoVO;
import com.fsll.app.investor.me.vo.AppMyAccountVO;
import com.fsll.app.investor.me.vo.AppPieChartItemVO;
import com.fsll.app.investor.me.vo.AppPortfolioAllocationVO;
import com.fsll.app.investor.me.vo.AppPortfolioHoldProductVO;
import com.fsll.app.investor.me.vo.AppPortfolioOrderHistoryVO;
import com.fsll.app.investor.me.vo.AppProductInfoVO;
import com.fsll.app.investor.me.vo.AppRpqExamQuestItemVO;
import com.fsll.app.investor.me.vo.AppRpqExamQuestVO;
import com.fsll.app.investor.me.vo.AppRpqExamResultVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.entity.SysCountry;
import com.fsll.core.vo.AccessoryFileVO;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundPortfolio;
import com.fsll.wmes.entity.FundPortfolioSc;
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
import com.fsll.wmes.entity.PortfolioHoldProduct;
import com.fsll.wmes.entity.PortfolioInfo;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.RpqExam;
import com.fsll.wmes.entity.RpqExamAnswer;
import com.fsll.wmes.entity.RpqExamEn;
import com.fsll.wmes.entity.RpqExamQuest;
import com.fsll.wmes.entity.RpqExamQuestEn;
import com.fsll.wmes.entity.RpqExamQuestItem;
import com.fsll.wmes.entity.RpqExamQuestItemEn;
import com.fsll.wmes.entity.RpqExamSc;
import com.fsll.wmes.entity.RpqExamTc;
import com.fsll.wmes.entity.RpqPageLevel;
import com.fsll.wmes.entity.RpqPageLevelEn;
import com.fsll.wmes.entity.RpqPageLevelSc;
import com.fsll.wmes.entity.RpqPageLevelTc;
/**
 * 基金信息查询服务接口
 * @author zxtan
 * @date 2017-01-12
 */
@Service("appMyAccountService")
//@Transactional
public class AppMyAccountServiceImpl extends BaseService implements AppMyAccountService {


	/**
	 * 我的账户列表
	 * @author zxtan
	 * @date 2017-01-12
	 */
	public List<AppMyAccountVO> findMyAccountList(String memberId,String langCode,String toCurrency,String openStatus) {
		// TODO Auto-generated method stub
		List<AppMyAccountVO> voList = new ArrayList<AppMyAccountVO>();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from InvestorAccount a ");
		hql.append(" inner join MemberDistributor d on d.id = a.distributor.id ");
		hql.append(" left join RpqExam r on r.relateId = a.id and r.status='1' ");
		hql.append(" where a.subFlag = '0' and a.member.id=? ");
		params.add(memberId);
				
		hql.append(" and exists ( select s.id from InvestorAccount s where s.distributor.id=d.id ");
		
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
		
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				AppMyAccountVO vo = new AppMyAccountVO();
				Object[] objs = (Object[]) list.get(i);
				InvestorAccount account = (InvestorAccount) objs[0];
				MemberDistributor distributor = (MemberDistributor) objs[1];
				vo.setCompanyName(distributor.getCompanyName());
				RpqExam rpq = null;
				if( null != objs[2] ){
					rpq = (RpqExam) objs[2];	
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
				vo = findInvestorAccountList(langCode,memberId, distributor.getId(), toCurrency,openStatus, vo);
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
	private AppMyAccountVO findInvestorAccountList(String langCode,String memberId,String distributorId,String toCurrency,String openStatus,AppMyAccountVO accountVO){
		List<AppMyAccountInfoVO> infoList = new ArrayList<AppMyAccountInfoVO>();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" From InvestorAccount a ");
		hql.append(" left join PortfolioHoldAccount ha on ha.account.id= a.id ");
		hql.append(" WHERE a.member.id=? and a.distributor.id = ? ");	
		params.add(memberId);	
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
		hql.append(" Order By a.subFlag,a.accountNo ");	
		//Date expireDate = null;
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			String toCurrencyName = getParamConfigName(langCode, toCurrency, "currency_type");
			for (int i = 0; i < list.size(); i++) {
				AppMyAccountInfoVO vo = new AppMyAccountInfoVO();
				Object[] objs = (Object[]) list.get(i);
				InvestorAccount account = (InvestorAccount) objs[0];
				vo.setAccountId(account.getId());
				vo.setAccountNo(account.getAccountNo());
				String accountCurrency = getParamConfigName(langCode, account.getBaseCurrency(), "currency_type");
				vo.setAccountCurrency(accountCurrency);
				vo.setSubFlag(account.getSubFlag());
				vo.setCies(account.getCies());
				vo.setOpenStatus(account.getOpenStatus());
				
				MemberIfa ifa = account.getIfa();
				if(null != ifa) {
					String iconUrl = ifa.getMember().getIconUrl();
					String gender = ifa.getGender();
					
					vo.setIfaIcon(PageHelper.getUserHeadUrlForWS(iconUrl, gender));
					
					String ifaName = getCommonMemberName(ifa.getMember().getId(), langCode,CommonConstants.MEMBER_NAME_REAL_NAME);
					vo.setIfaName(ifaName);
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
					
					if(null != toCurrency && !"".equals(toCurrency)){
						vo.setBaseCurrency(toCurrencyName);
						Double rate = getExchangeRate(baseCurrency, toCurrency);
						if(null == rate){
							vo.setMarketValue(getFormatNum(null));
							vo.setCashAvailable(getFormatNum(null));
							vo.setCashHold(getFormatNum(null));
							vo.setCashWithdrawal(getFormatNum(null));	
							vo.setTotalCash(getFormatNum(null));
							vo.setTotalAsset(getFormatNum(null));	
						}else {
							vo.setMarketValue(getFormatNum(marketValue*rate,toCurrency));
							vo.setCashAvailable(getFormatNum(cashAvailable*rate,toCurrency));
							vo.setCashHold(getFormatNum(cashHold*rate,toCurrency));
							vo.setCashWithdrawal(getFormatNum(cashWithdrawal*rate,toCurrency));
							vo.setTotalCash(getFormatNum(totalCash*rate,toCurrency));
							vo.setTotalAsset(getFormatNum(totalAsset*rate,toCurrency));	
						}					
					}else {
						vo.setBaseCurrency(getParamConfigName(langCode, baseCurrency, "currency_type"));
						vo.setMarketValue(getFormatNum(marketValue,baseCurrency));
						vo.setCashAvailable(getFormatNum(cashAvailable,baseCurrency));
						vo.setCashHold(getFormatNum(cashHold,baseCurrency));
						vo.setCashWithdrawal(getFormatNum(cashWithdrawal,baseCurrency));
						vo.setTotalCash(getFormatNum(totalCash,baseCurrency));
						vo.setTotalAsset(getFormatNum(totalAsset,baseCurrency));
					}					
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
	 * @date 2017-01-12
	 * @return
	 */
	public AppMyAccountInfoVO findAccountInfo(String accountId,String langCode,String toCurrency){
		AppMyAccountInfoVO vo = new AppMyAccountInfoVO();
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
			
			MemberIfa ifa = account.getIfa();
			if(null != ifa) {
				String iconUrl = ifa.getMember().getIconUrl();
				String gender = ifa.getGender();
				
				vo.setIfaIcon(PageHelper.getUserHeadUrlForWS(iconUrl, gender));
				String ifaName = getCommonMemberName(ifa.getMember().getId(), langCode,CommonConstants.MEMBER_NAME_REAL_NAME);
				vo.setIfaName(ifaName);				
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
				
				
				if(null != toCurrency && !"".equals(toCurrency)){
					vo.setBaseCurrency(getParamConfigName(langCode, toCurrency, "currency_type"));
					Double rate = getExchangeRate(baseCurrency, toCurrency);
					if(null == rate){
						vo.setMarketValue(getFormatNum(null));
						vo.setCashAvailable(getFormatNum(null));
						vo.setCashHold(getFormatNum(null));
						vo.setCashWithdrawal(getFormatNum(null));	
						vo.setTotalAsset(null);
						vo.setTotalCash(null);
					}else {
						vo.setMarketValue(getFormatNum(marketValue*rate,toCurrency));
						vo.setCashAvailable(getFormatNum(cashAvailable*rate,toCurrency));
						vo.setCashHold(getFormatNum(cashHold*rate,toCurrency));
						vo.setCashWithdrawal(getFormatNum(cashWithdrawal*rate,toCurrency));	
						vo.setTotalAsset(getFormatNum(totalAsset*rate,toCurrency));
						vo.setTotalCash(getFormatNum(totalCash*rate,toCurrency));
					}					
				}else {
					vo.setBaseCurrency(getParamConfigName(langCode, baseCurrency, "currency_type"));
					vo.setMarketValue(getFormatNum(marketValue,baseCurrency));
					vo.setCashAvailable(getFormatNum(cashAvailable,baseCurrency));
					vo.setCashHold(getFormatNum(cashHold,baseCurrency));
					vo.setCashWithdrawal(getFormatNum(cashWithdrawal,baseCurrency));
					vo.setTotalAsset(getFormatNum(totalAsset,baseCurrency));
					vo.setTotalCash(getFormatNum(totalCash,baseCurrency));
				}					
			}			
		}
		
		return vo;
	}
	


	
	/**
	 * 获取账户RPQ信息
	 * @author zxtan
	 * @date 2017-01-16
	 * @return
	 */
	public AppMyAccountRpqInfoVO findAccountRPQInfo(String accountId,String langCode){
		AppMyAccountRpqInfoVO vo = new AppMyAccountRpqInfoVO();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" From InvestorAccount a ");
		hql.append(" inner join RpqExam r on r.relateId= a.id ");
		hql.append(" inner join "+getLangString("RpqExam", langCode)+" f on f.id= r.id ");
		hql.append(" WHERE a.subFlag='0' and a.isValid='1' and exists ( ");	
		hql.append(" select t.id from InvestorAccount t where t.id=? and t.distributor.id=a.distributor.id ");
		hql.append(" ) ");
		hql.append(" ORDER BY r.expireDate DESC ");
		params.add(accountId);	
//		Date expireDate = null;
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){				
			Object[] objs = (Object[]) list.get(0);
			InvestorAccount account = (InvestorAccount) objs[0];
			RpqExam rpq = (RpqExam) objs[1];
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
	 * @date 2017-01-12
	 * @return
	 */
	public List<AppMyAccountDocInfoVO> findAccountDocCheckInfo(String accountId,String langCode){
		List<AppMyAccountDocInfoVO> voList = new ArrayList<AppMyAccountDocInfoVO>();
		
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
				AppMyAccountDocInfoVO vo = new AppMyAccountDocInfoVO();
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
				List<AccessoryFile> fileList = findFileAttr(doc.getId(),"investor_doc");
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
	public AppMyAccountBaseInfoVO findAccountBaseInfo(String accountId,String langCode){

		AppMyAccountBaseInfoVO vo = new AppMyAccountBaseInfoVO();
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
	
	
	/**
	 * 得到投资账户的交易记录列表
	 * @author zxtan
	 * @date 2017-01-13
	 * @param accountId 账户ID
	 * @return
	 */
	public List<AppPortfolioOrderHistoryVO> findAccountOrderHistoryList(String accountId, String toCurrency,String langCode){
		List<AppPortfolioOrderHistoryVO>  messList = new ArrayList<AppPortfolioOrderHistoryVO>();
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
		String toCurrencyName = getParamConfigName(langCode, toCurrency, "currency_type");
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			OrderHistory history = (OrderHistory) objs[0];
			InvestorAccount account = (InvestorAccount) objs[1];
			AppPortfolioOrderHistoryVO vo = new AppPortfolioOrderHistoryVO();
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
				vo.setCurrencyCode(getParamConfigName(langCode, currencyCode, "currency_type"));
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
			vo.setIfAip(history.getIfAip());
			vo.setSwitchFlag(history.getSwitchFlag());
			
			messList.add(vo);
		}
				
		return messList;
	}
	
	/**
	 * 生成RPQ问卷
	 * @author zxtan
	 * @date 2017-03-08
	 * @param accountId
	 * @param langCode
	 * @return
	 */
	public List<AppRpqExamQuestVO> createRpqExamQuest(String accountId,String langCode){
		List<AppRpqExamQuestVO> voList = new ArrayList<AppRpqExamQuestVO>();
		String examId = "";
		String sql = "call pro_createRpqExam(?)";
		List<Object> params = new ArrayList<Object>();
		params.add(accountId);
		List list = this.springJdbcQueryManager.springJdbcQueryForList(sql, params.toArray());
		Iterator it = (Iterator) list.iterator();
	    while (it.hasNext()) {
	    	Map map = (HashMap) it.next();
	    	examId = map.get("examId") == null ? "" : map.get("examId").toString();	    	  	
	    }
	    
	    if("".equals(examId)) return voList;
	    
	    StringBuilder hql = new StringBuilder();
	    hql.append("from RpqExamQuest m ");
	    hql.append(" inner join "+this.getLangString("RpqExamQuest", langCode)+" f on f.id=m.id ");
	    hql.append(" inner join RpqExam e on e.id=m.exam.id ");
	    hql.append(" where e.id=? order by m.orderBy ");
	    params = new ArrayList<Object>();
	    params.add(examId);
	    List listQuest = baseDao.find(hql.toString(), params.toArray(), false);
	    if(null != listQuest && !listQuest.isEmpty()){
	    	for (int i = 0; i < listQuest.size(); i++) {
	    		AppRpqExamQuestVO vo = new AppRpqExamQuestVO();
				Object[] objs = (Object[]) listQuest.get(i);
				RpqExamQuest quest = (RpqExamQuest) objs[0];
				RpqExamQuestEn questEn = new RpqExamQuestEn();
				BeanUtils.copyProperties(objs[1], questEn);
				
				String questId = quest.getId();
				vo.setQuestId(questId);
				vo.setOrderBy(quest.getOrderBy());
				vo.setTitle(questEn.getTitle());
				vo.setRemark(questEn.getRemark());
				vo.setQuestType(quest.getQuestType());
				
				List<AppRpqExamQuestItemVO> itemVOList = new ArrayList<AppRpqExamQuestItemVO>();
			    StringBuilder hqlItem = new StringBuilder();
			    hqlItem.append("from RpqExamQuestItem m ");
			    hqlItem.append(" inner join "+this.getLangString("RpqExamQuestItem", langCode)+" f on f.id=m.id ");
			    hqlItem.append(" where m.quest.id=? order by m.orderBy ");
			    params = new ArrayList<Object>();
			    params.add(questId);
			    List listItem = baseDao.find(hqlItem.toString(), params.toArray(), false);
			    if(null != listItem && !listItem.isEmpty()){
			    	for (int j = 0; j < listItem.size(); j++) {
			    		AppRpqExamQuestItemVO itemVO = new AppRpqExamQuestItemVO();
						Object[] objsItem = (Object[]) listItem.get(j);
						RpqExamQuestItem item = (RpqExamQuestItem) objsItem[0];
						RpqExamQuestItemEn itemEn = new RpqExamQuestItemEn();
						BeanUtils.copyProperties(objsItem[1], itemEn);
						
						itemVO.setItemId(item.getId());
						itemVO.setQuestId(questId);
						itemVO.setOrderBy(item.getOrderBy());
						itemVO.setType(item.getType());
						itemVO.setScoreValue(item.getScoreValue());
						itemVO.setTitle(itemEn.getTitle());
						itemVO.setRemark(itemEn.getRemark());
						itemVOList.add(itemVO);
					}
			    }
			    vo.setQuestItemList(itemVOList);
			    
			    voList.add(vo);
			}
	    }
	    return voList;
	}
	
	/**
	 * RPQ问卷评测结果
	 * @author zxtan
	 * @date 2017-03-08
	 * @param answerArray
	 * @param langCode
	 * @return
	 */
	public AppRpqExamResultVO saveRpqExamAnswer(JSONArray answerArray,String langCode){
		AppRpqExamResultVO vo = new AppRpqExamResultVO();
		int score = 0;
		String pageId = "";
		String examId = "";
		for (int i = 0; i < answerArray.size(); i++) {
			JSONObject answer = answerArray.getJSONObject(i);
			String questId = answer.optString("questId", "");
			String itemId = answer.optString("itemId", "");
			if(!"".equals(questId) && !"".equals(itemId)){
				RpqExamQuest quest = (RpqExamQuest) baseDao.get(RpqExamQuest.class, questId);
				RpqExam exam = quest.getExam();
				if("".equals(examId)) {
					examId = exam.getId();
					pageId = exam.getPageId();
				}
				RpqExamQuestItem item = (RpqExamQuestItem) baseDao.get(RpqExamQuestItem.class, itemId);

				int itemScore = (item.getScoreValue()==null)?0:item.getScoreValue();
				score = score + itemScore;
				
				RpqExamAnswer examAnswer = new RpqExamAnswer();
				examAnswer.setId(null);
				examAnswer.setExam(exam);
				examAnswer.setQuest(quest);
				examAnswer.setItem(item);
				examAnswer.setItemValue(String.valueOf(item.getScoreValue()));
				
				baseDao.saveOrUpdate(examAnswer);
			}
		}
		
		StringBuilder hql = new StringBuilder();
		hql.append("from RpqPageLevel m ");
		hql.append(" inner join RpqPageLevelEn en on en.id=m.id ");
		hql.append(" inner join RpqPageLevelSc sc on sc.id=m.id ");
		hql.append(" inner join RpqPageLevelTc tc on tc.id=m.id ");
		hql.append(" where m.status='using' and m.isValid='1' and m.page.id=? ");
		hql.append(" and ? between m.beginScore and m.endScore ");
		List<Object> params = new ArrayList<Object>();
		params.add(pageId);
		params.add(score);
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			Object[] objs = (Object[]) list.get(0);
			RpqPageLevel pageLevel = (RpqPageLevel) objs[0];
			RpqPageLevelEn pageLevelEn = (RpqPageLevelEn) objs[1];
			RpqPageLevelSc pageLevelSc = (RpqPageLevelSc) objs[2];
			RpqPageLevelTc pageLevelTc = (RpqPageLevelTc) objs[3];
			
			RpqExam exam = (RpqExam) baseDao.get(RpqExam.class, examId);
			RpqExamEn examEn = (RpqExamEn) baseDao.get(RpqExamEn.class, examId);
			RpqExamSc examSc = (RpqExamSc) baseDao.get(RpqExamSc.class, examId);
			RpqExamTc examTc = (RpqExamTc) baseDao.get(RpqExamTc.class, examId);
			
			//返回结果
			vo.setExamId(examId);
			vo.setExpireDate(DateUtil.addDateToString(DateUtil.getCurDateStr(), Calendar.YEAR, 1));
			vo.setRiskLevel(String.valueOf(pageLevel.getRiskLevel()));
			if("en".equalsIgnoreCase(langCode)){
				vo.setRiskResult(pageLevelEn.getResult());
				vo.setRiskRemark(pageLevelEn.getRemark());
			}else if("sc".equalsIgnoreCase(langCode)){
				vo.setRiskResult(pageLevelSc.getResult());
				vo.setRiskRemark(pageLevelSc.getRemark());
			}else if("tc".equalsIgnoreCase(langCode)){
				vo.setRiskResult(pageLevelTc.getResult());
				vo.setRiskRemark(pageLevelTc.getRemark());
			}
			
			
			String sql = "update RpqExam t set t.status='0' where t.relateId=? ";
			baseDao.updateHql(sql, new Object[]{exam.getRelateId()});
			
			
			exam.setTotalScore(score);
			exam.setRiskLevel(pageLevel.getRiskLevel());
			exam.setExpireDate(DateUtil.addDate(DateUtil.getDate(DateUtil.getCurDateStr()), Calendar.YEAR, 1));
			exam.setLastUpdate(new Date());
			exam.setStatus("1");
			exam.setIsValid("1");
			baseDao.saveOrUpdate(exam);
			
			examEn.setRiskResult(pageLevelEn.getResult());
			examEn.setRiskRemark(pageLevelEn.getRemark());
			baseDao.saveOrUpdate(examEn);

			examSc.setRiskResult(pageLevelSc.getResult());
			examSc.setRiskRemark(pageLevelSc.getRemark());
			baseDao.saveOrUpdate(examSc);

			examTc.setRiskResult(pageLevelTc.getResult());
			examTc.setRiskRemark(pageLevelTc.getRemark());
			baseDao.saveOrUpdate(examTc);
					
			InvestorAccount account = (InvestorAccount) baseDao.get(InvestorAccount.class, exam.getRelateId());
			account.setRpqLevel(pageLevel.getRiskLevel());
			baseDao.saveOrUpdate(account);
			
			String updateSql = "update InvestorAccount t set t.rpqLevel=? where t.masterAccount.id=? ";
			baseDao.updateHql(updateSql, new Object[]{pageLevel.getRiskLevel(),exam.getRelateId()});
		}
		return vo;
	}

}
