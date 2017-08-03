package com.fsll.wmes.investor.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.StrUtils;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.DocList;
import com.fsll.wmes.entity.DocListEn;
import com.fsll.wmes.entity.DocListSc;
import com.fsll.wmes.entity.DocListTc;
import com.fsll.wmes.entity.DocTemplate;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.InvestorAccountContact;
import com.fsll.wmes.entity.InvestorAccountCurrency;
import com.fsll.wmes.entity.InvestorBackground;
import com.fsll.wmes.entity.InvestorDoc;
import com.fsll.wmes.entity.InvestorDocEn;
import com.fsll.wmes.entity.InvestorDocSc;
import com.fsll.wmes.entity.InvestorDocTc;
import com.fsll.wmes.entity.InvestorTraining;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.PortfolioHoldAccount;
import com.fsll.wmes.entity.PortfolioHoldProduct;
import com.fsll.wmes.entity.RpqExam;
import com.fsll.wmes.entity.RpqExamEn;
import com.fsll.wmes.entity.RpqExamSc;
import com.fsll.wmes.entity.RpqExamTc;
import com.fsll.wmes.entity.WebExchangeRate;
import com.fsll.wmes.entity.WfProcedureInstanseTodo;
import com.fsll.wmes.investor.service.InvestorServiceForConsole;
import com.fsll.wmes.investor.vo.ApprovalHisVO;
import com.fsll.wmes.investor.vo.InvestorAccountCurrencyVO;
import com.fsll.wmes.investor.vo.InvestorAccountVO;
import com.fsll.wmes.investor.vo.InvestorApprovalVO;
import com.fsll.wmes.member.vo.DocListVO;

/***
 * 业务接口实现类：investor管理接口类
 * 
 * @author mqzou
 * @date 2016-06-22
 */
@Service("investorServiceForConsole")
//@Transactional
public class InvestorServiceImplForConsole extends BaseService implements InvestorServiceForConsole {

	@Autowired
	private SysParamService sysParamService;
	
	@Override
	public JsonPaging findInvestorAccountForApproval(JsonPaging jsonPaging, InvestorAccount investorAccount) {
		String hql = " from InvestorAccount i where ( isValid='1' or isValid is null) ";
		if (null != investorAccount.getDistributor()) {
			hql += " and i.distributor.id='" + investorAccount.getDistributor().getId() + "'";
		}
		List params = new ArrayList();
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		Iterator it = (Iterator) jsonPaging.getList().iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			InvestorAccount account = (InvestorAccount) it.next();
			InvestorAccountVO vo = new InvestorAccountVO();
			vo.setId(account.getId());
			vo.setAccountNo(account.getAccountNo());
			vo.setAccType(account.getAccType());
			vo.setBaseCurrency(account.getBaseCurrency());
			vo.setCies(account.getCies());
			vo.setCreateBy(null != account.getCreateBy() ? account.getCreateBy().getId() : "");
			vo.setCreateByName(null != account.getCreateBy() ? account.getCreateBy().getNickName() : "");
			vo.setCreateTime(account.getCreateTime());
			vo.setCurStep(account.getCurStep());
			vo.setDiscFlag(account.getDiscFlag());
			vo.setDistributorId(null != account.getDistributor() ? account.getDistributor().getId() : "");
			vo.setDividend(account.getDividend());
			vo.setEmail(null != account.getMember() ? account.getMember().getEmail() : "");
			vo.setFinishStatus(account.getFinishStatus());
			vo.setFlowId(account.getFlowId());
			vo.setFlowStatus(account.getFlowStatus());
			vo.setFromType(account.getFromType());
			vo.setIfaId(null != account.getIfa() ? account.getIfa().getId() : "");
			vo.setIfaName(null != account.getIfa() ? account.getIfa().getMember().getNickName() : "");
			vo.setInvestType(account.getInvestType());
			vo.setLoginCode(null != account.getMember() ? account.getMember().getLoginCode() : "");
			vo.setMemberId(null != account.getMember() ? account.getMember().getId() : "");
			vo.setMobileNumber(null != account.getMember() ? account.getMember().getMobileNumber() : "");
			vo.setNickName(null != account.getMember() ? account.getMember().getNickName() : "");
			vo.setOpenStatus(account.getOpenStatus());
			vo.setPurpose(account.getPurpose());
			vo.setPurposeOther(account.getPurposeOther());
			vo.setSentBy(account.getSentBy());
			vo.setSubmitStatus(account.getSubmitStatus());
			vo.setTotalFlag(account.getTotalFlag());
			list.add(vo);
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}


	/**
	 * Ifa firm,Distributor 查看已开户投资账号列表
	 * @author zxtan
	 * @date 2016-08-24
	 */
	@Override
	public JsonPaging findInvestorAccount(JsonPaging jsonPaging,InvestorAccountVO info,String langCode) {
		// TODO Auto-generated method stub
		StringBuilder hql = new StringBuilder("select a from InvestorAccount a ");
		hql.append(" left join MemberIndividual i on a.member.id=i.member.id");
		hql.append(" left join MemberDistributor d on a.distributor.id=d.id");
		hql.append(" left join MemberBase b on a.member.id=b.id and i.member.id=b.id");
		hql.append("  where 1=1 and a.isValid='1' ");
		List<Object> params=new ArrayList<Object>();
		if(StringUtils.isNotBlank(info.getIfaName())){
			hql.append(" and a.ifa.member.nickName like ?");
			params.add("%"+info.getIfaName()+"%");
		}
		if(StringUtils.isNotBlank(info.getKeyword())){
			hql.append(" and ( a.member.nickName like ? or a.accountNo like ?) ");
			params.add("%"+info.getKeyword()+"%");
			params.add("%"+info.getKeyword()+"%");
		}
		if(StringUtils.isNotBlank(info.getAccountNo())){
			hql.append(" and a.accountNo like ?");
			params.add("%"+info.getAccountNo()+"%");
		}
		if(StringUtils.isNotBlank(info.getDistributorName())){
			hql.append(" and d.companyName like ?");
			params.add("%"+info.getDistributorName()+"%");
		}
		if(StringUtils.isNotBlank(info.getFlowStatus())){
			hql.append(" and a.flowStatus=?");
			params.add(info.getFlowStatus());
		}
		if(StringUtils.isNotBlank(info.getNickName())){
			hql.append(" and (concat(i.firstName,i.lastName) like ? or i.nameChn like ? or b.nickName like ? )");
			params.add("%"+info.getNickName()+"%");
			params.add("%"+info.getNickName()+"%");
			params.add("%"+info.getNickName()+"%");
		}
		jsonPaging=this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		Iterator it=(Iterator)jsonPaging.getList().iterator();
		List<InvestorAccountVO> list=new ArrayList<InvestorAccountVO>();
		while (it.hasNext()) {
			InvestorAccount account = (InvestorAccount) it.next();
			InvestorAccountVO vo = new InvestorAccountVO();
			BeanUtils.copyProperties(account,vo);//拷贝信息
			vo.setCreateBy(null!=account.getCreateBy()?account.getCreateBy().getId():"");
			vo.setCreateByName(null!=account.getCreateBy()?account.getCreateBy().getNickName():"");
			vo.setDistributorId(null!=account.getDistributor()?account.getDistributor().getId():"");
			vo.setDistributorName(null!=account.getDistributor()?account.getDistributor().getCompanyName():"");
			vo.setEmail(null!=account.getMember()?account.getMember().getEmail():"");
			vo.setIfaId(null!=account.getIfa()?account.getIfa().getId():"");
			if(null!=vo.getIfaId() && !"".equals(vo.getIfaId())){
				String ifaName=getCommonMemberName(account.getIfa().getMember().getId(), langCode, "2");
				vo.setIfaName(ifaName);
			}else {
				vo.setIfaName("");
			}
			
			vo.setLoginCode(null!=account.getMember()?account.getMember().getLoginCode():"");
			vo.setMemberId(null!=account.getMember()?account.getMember().getId():"");
			vo.setMobileNumber(null!=account.getMember()?account.getMember().getMobileNumber():"");
			String nickName=getCommonMemberName(account.getMember().getId(), langCode, "2");
			vo.setNickName(nickName);
			vo.setLastUpdate(DateUtil.getDateStr(account.getLastUpdate()));
			list.add(vo);
		}
		
		jsonPaging.setList(list);
		return jsonPaging;
	}
	

	/**
	 * Ifa firm,Distributor 待审批投资账号列表
	 * @author zxtan
	 * @date 2016-08-24
	 */
	@Override
	public JsonPaging findInvestorAccountForApproval(JsonPaging jsonPaging,String adminType, String adminMemberId,String curUserId) {
		// modify by mqzou 2016-08-30 列表需关联流程表
		/*StringBuilder hql = new StringBuilder(" from InvestorAccount l ");
		
		List params=new ArrayList();
		if("1".equals(adminType)){
			hql.append(" where l.isValid='1' and l.flowStatus='3' "); //待Ifa firm 审批
			hql.append(" and ( l.ifa != null and l.ifa.ifafirm != null and l.ifa.ifafirm.id = ? ) ");
			params.add(adminMemberId);
		}else if("2".equals(adminType)){
			hql.append(" where l.isValid='1' and l.flowStatus='4' ");//待Distributor 审批
			hql.append(" and ( l.distributor != null and l.distributor.id = ? ) ");
			params.add(adminMemberId);
		}else {
			hql.append(" where l.isValid='1' and 1=2 ");
		}*/
		
		StringBuilder hql = new StringBuilder(" from   WfProcedureInstanseTodo w  , InvestorAccount i where  w.businessId=i.id ");
		List params=new ArrayList();
		if(CommonConstantsWeb.MEMBERADMIN_TYPE_IFA.equals(adminType)){
			hql.append(" and i.isValid='1' and (i.flowStatus='3')"); //待Ifa firm 审批
			hql.append(" and ( i.ifa !=null and i.ifa.ifafirm !=null ) ");
			//params.add(adminMemberId);
		}else if(CommonConstantsWeb.MEMBERADMIN_TYPE_DISTRIBUTOR.equals(adminType)){
			hql.append(" and i.isValid='1' and (i.flowStatus='4')");//待Distributor 审批
			hql.append(" and (i.distributor !=null and i.distributor.id=null ) ");
			//params.add(adminMemberId);
		}else {
			hql.append(" where i.isValid='1' ");
		}
		hql.append(" and w.isValid='1' and w.flowState='0'");
		
		
		jsonPaging=this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		Iterator it=(Iterator)jsonPaging.getList().iterator();
		List list=new ArrayList();
		while (it.hasNext()) {
			// modify by mqzou 2016-08-30
			Object[] obj=(Object[])it.next();
			InvestorAccount account = (InvestorAccount)obj[1];
			WfProcedureInstanseTodo todo = (WfProcedureInstanseTodo)obj[0];
			//InvestorAccount account = (InvestorAccount) it.next();
			/*InvestorAccountVO vo = new InvestorAccountVO();
			BeanUtils.copyProperties(account,vo);//拷贝信息
			vo.setCreateBy(null!=account.getCreateBy()?account.getCreateBy().getId():"");
			vo.setCreateByName(null!=account.getCreateBy()?account.getCreateBy().getNickName():"");
			vo.setDistributorId(null!=account.getDistributor()?account.getDistributor().getId():"");
			vo.setEmail(null!=account.getMember()?account.getMember().getEmail():"");
			vo.setIfaId(null!=account.getIfa()?account.getIfa().getId():"");
			vo.setIfaName(null!=account.getIfa()?account.getIfa().getMember().getNickName():"");
			vo.setLoginCode(null!=account.getMember()?account.getMember().getLoginCode():"");
			vo.setMemberId(null!=account.getMember()?account.getMember().getId():"");
			vo.setMobileNumber(null!=account.getMember()?account.getMember().getMobileNumber():"");
			vo.setNickName(null!=account.getMember()?account.getMember().getNickName():"");*/
			InvestorApprovalVO vo=new InvestorApprovalVO();
			vo.setAccountCreateTime(DateUtil.dateToDateString(account.getCreateTime(), "yyyy-MM-dd"));
			vo.setAccountId(account.getId());
			vo.setAccountNo(account.getAccountNo());
			vo.setAccType(account.getAccType());
			vo.setApprover(null!=todo.getFlowUser()? todo.getFlowUser().getNickName():"");
			String approveUserId=null!=todo.getFlowUser()?todo.getFlowUser().getId():"";
			vo.setApproverId(approveUserId);
			vo.setFlowName(todo.getFlowName());
			vo.setFlowState(account.getFlowStatus());
			vo.setFromType(account.getFromType());
			vo.setMemberId(account.getMember().getId());
			vo.setNickName(account.getMember().getNickName());
			//if(curUserId.equals(approveUserId)){//是否可以审批
			vo.setCanApprove("1");
			//}else {
			//	vo.setCanApprove("0");
			//}
			
			list.add(vo);
		}
		
		jsonPaging.setList(list);
		return jsonPaging;
	}


	/**
	 * 投资账号信息
	 * @author zxtan
	 * @date 2016-08-24
	 */
	@Override
	public InvestorAccount findInvestorAccountById(String id)
	{
		if(StringUtils.isBlank(id)){
			return null;
		}
		InvestorAccount obj = (InvestorAccount) this.baseDao.get(InvestorAccount.class, id);
		return obj;
	}
	
	/**
	 * Ifa firm,Distributor 审批投资账号
	 * @author zxtan
	 * @date 2016-08-24
	 */
	@Override
	public Boolean updateFlowStatus(String id,String status)
	{
		String hql = "update InvestorAccount set flowStatus=? where id=? ";
		List<Object> params = new ArrayList<Object>();
		params.add(status);
		params.add(id);
		int count = this.baseDao.updateHql(hql, params.toArray());
		return count>0;
	}


	@Override
	public JsonPaging findInvestorAccountList(JsonPaging jsonPaging, InvestorAccount investorAccount, String langCode) {
		String hql = " from InvestorAccount i where ( isValid='1' or isValid is null) ";

		if (null != investorAccount && null != investorAccount.getMember() && null != investorAccount.getMember().getId()) {
			hql += " and i.member.id='" + investorAccount.getMember().getId() + "'";
		}
		if (null != investorAccount.getIfa() && null != investorAccount.getIfa().getId()) {
			hql += " and i.ifa.id='" + investorAccount.getIfa().getId() + "'";
		}
		if (null != investorAccount.getIfa() && null != investorAccount.getIfa().getIfafirm() && null != investorAccount.getIfa().getIfafirm().getId()) {
			hql += " and i.ifa.ifafirm.id='" + investorAccount.getIfa().getIfafirm().getId() + "'";
		}

		List params = new ArrayList();
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		Iterator it = (Iterator) jsonPaging.getList().iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			InvestorAccount account = (InvestorAccount) it.next();
			InvestorAccountVO vo = new InvestorAccountVO();
			vo.setId(account.getId());
			vo.setAccountNo(account.getAccountNo());
			vo.setAccType(account.getAccType());
			vo.setBaseCurrency(account.getBaseCurrency());
			vo.setCies(account.getCies());
			vo.setCreateBy(null != account.getCreateBy() ? account.getCreateBy().getId() : "");
			vo.setCreateByName(null != account.getCreateBy() ? account.getCreateBy().getNickName() : "");
			vo.setCreateTime(account.getCreateTime());
			vo.setCurStep(account.getCurStep());
			vo.setDiscFlag(account.getDiscFlag());
			vo.setDistributorId(null != account.getDistributor() ? account.getDistributor().getId() : "");
			vo.setDividend(account.getDividend());
			vo.setEmail(null != account.getMember() ? account.getMember().getEmail() : "");
			vo.setFinishStatus(account.getFinishStatus());
			vo.setFlowId(account.getFlowId());
			vo.setFlowStatus(account.getFlowStatus());
			vo.setFromType(account.getFromType());
			vo.setIfaId(null != account.getIfa() ? account.getIfa().getId() : "");
			vo.setIfaName(null != account.getIfa() ? account.getIfa().getMember().getNickName() : "");
			vo.setInvestType(account.getInvestType());
			vo.setLoginCode(null != account.getMember() ? account.getMember().getLoginCode() : "");
			vo.setMemberId(null != account.getMember() ? account.getMember().getId() : "");
			vo.setMobileNumber(null != account.getMember() ? account.getMember().getMobileNumber() : "");
			vo.setNickName(null != account.getMember() ? account.getMember().getNickName() : "");
			vo.setOpenStatus(account.getOpenStatus());
			vo.setPurpose(account.getPurpose());
			vo.setPurposeOther(account.getPurposeOther());
			vo.setSentBy(account.getSentBy());
			vo.setSubmitStatus(account.getSubmitStatus());
			vo.setTotalFlag(account.getTotalFlag());
			list.add(vo);
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}

	@Override
	public InvestorAccount findById(String id ) {
		InvestorAccount vo=(InvestorAccount)this.baseDao.get(InvestorAccount.class, id);
		return vo;
	}


	@Override
	public List findApproveHis(String accountId) {
		 String hql=" from WfProcedureInstanseTodo w where w.businessId='"+accountId+"'  and w.flowState='1' order by w.processDate";
		 List resultList=this.baseDao.find(hql, null, false);
		 List list=new ArrayList();
		 Iterator it=resultList.iterator();
		 while (it.hasNext()) {
			WfProcedureInstanseTodo todo = (WfProcedureInstanseTodo) it.next();
			ApprovalHisVO vo=new ApprovalHisVO();
			vo.setAction(String.valueOf(todo.getCheckStatus()));
			vo.setApproveData(DateUtil.dateToDateString(todo.getProcessDate(), "yyyy-MM-dd HH:mm:ss"));
			vo.setApprover(todo.getFlowUser().getNickName());
			vo.setComment(todo.getComment());
			vo.setFlowStep(todo.getFlowName());
			vo.setId(todo.getId());
			list.add(vo);
		}
			 
		return list;
	}


	@Override
	public List findAccountFileList(String accountId, String langCode,String moduleType) {
		String hql=" from AccessoryFile r where r.moduleType='"+moduleType+"' and r.relateId='"+accountId+"' and r.langCode='"+langCode+"' ";
		hql+=" order by createTime";
		List resultList=this.baseDao.find(hql, null, false);
		List list=new ArrayList();
		Iterator it=resultList.iterator();
		while (it.hasNext()) {
			AccessoryFile file = (AccessoryFile) it.next();
			list.add(file);
		}
		return list;
	}


	/**
	 * 获取账户详情
	 * @author mqzou 2017-05-16
	 * @param accountId
	 * @param langCode
	 * @return
	 */
	@Override
	public InvestorAccountVO findAccountDetail(String accountId, String langCode,String currency) {
        InvestorAccount account = findInvestorAccountById(accountId);
		if (null != account) {
			if (null == currency || "".equals(currency))
				currency = account.getBaseCurrency();
			InvestorAccountVO vo = new InvestorAccountVO();
			vo.setIfa(account.getIfa());
			vo.setAccountId(account.getId());
			vo.setAccountNo(account.getAccountNo());
			vo.setAccType(account.getAccType());
			vo.setAuthorized(account.getAuthorized());
			vo.setBaseCurrency(account.getBaseCurrency());
			vo.setSourceFrom(account.getSourceFrom());
			String currencyName=sysParamService.findNameByCode(account.getBaseCurrency(), langCode);
			vo.setCurrency(currencyName);
			if (null!=account.getMember())
				vo.setIconUrl(account.getMember().getIconUrl());
			vo.setCies(account.getCies());
			
			if (null!=account.getDistributor()){
				vo.setDistributorId(account.getDistributor().getId());
				vo.setDistributorName(account.getDistributor().getCompanyName());
				vo.setDistributorIconUrl(PageHelper.getLogoUrl(account.getDistributor().getLogoFile(), "D"));
			}
			vo.setFaca(account.getFaca());
			
			if (null!=account.getMember()){
				vo.setLoginCode(account.getMember().getLoginCode());
				String nickName=getCommonMemberName(account.getMember().getId(), langCode, "2");
				vo.setNickName(nickName);
			}
			
			vo.setId(account.getId());

			InvestorAccountCurrencyVO currencyVO = findAccountCurrency(vo.getAccountNo(), currency);
			if (null != currency) {
				vo.setTotalValue(String.valueOf(currencyVO.getTotalAssets()));
				vo.setCashAvailable(String.valueOf(currencyVO.getCashAvailable()));
				vo.setCashForPendingTran(String.valueOf(currencyVO.getCashHold()));
				vo.setCashValue(String.valueOf(currencyVO.getCashValue()));
				vo.setMarketValue(currencyVO.getMarketValue());
				vo.setCashWithdrawal(String.valueOf(currencyVO.getCashWithdrawal()));
				vo.setCashRatio(StrUtils.getNumberString(currencyVO.getCashValue() / currencyVO.getTotalAssets() * 100));

				vo.setMarketRatio(StrUtils.getNumberString(currencyVO.getMarketValue() / currencyVO.getTotalAssets() * 100));
			} else {
				vo.setCashRatio("0");
				vo.setMarketRatio("0");
			}
			vo.setSubFlag(account.getSubFlag());
			vo.setMemberId(account.getMember().getId());
			return vo;
		}
		return null;
	}

	/**
	 * 获取账户的资产信息
	 * @author mqzou
	 * @date 2016-11-24
	 * @param accountId
	 * @param currency
	 * @return
	 */
	@Override
	public InvestorAccountCurrencyVO findAccountCurrency(String accountNo, String currency) {
		InvestorAccountCurrencyVO vo=new InvestorAccountCurrencyVO();
		String hql=" from PortfolioHoldAccount r where r.accountNo=? or r.subAccountNo=? ";
		List params=new ArrayList();
		params.add(accountNo);
		params.add(accountNo);
		List list=this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			Iterator it=list.iterator();
			while (it.hasNext()) {
				PortfolioHoldAccount accountCurrency = (PortfolioHoldAccount) it.next();
				String cur = accountCurrency.getBaseCurrency();
				double rate = 1;
				if (!currency.equals(cur)) {
					rate=getExchangeRate(cur,currency);
				}
				double cashAvailable=null!=accountCurrency.getCashAvailable()?accountCurrency.getCashAvailable():0;
				double cashHold=null!=accountCurrency.getCashHold()?accountCurrency.getCashHold():0;
				double cashWithdrawal=null!=accountCurrency.getCashWithdrawal()?accountCurrency.getCashWithdrawal():0;
				double marketValue=null!=accountCurrency.getMarketValue()?accountCurrency.getMarketValue():0;
				
				vo.setCashAvailable(cashAvailable*rate+vo.getCashAvailable());
				vo.setCashHold(cashHold*rate+vo.getCashHold());
				vo.setCashValue((cashHold + cashAvailable)*rate+vo.getCashValue());
				vo.setCashWithdrawal(cashWithdrawal*rate+vo.getCashWithdrawal());
				vo.setMarketValue(marketValue*rate+vo.getMarketValue());
				vo.setTotalAssets(vo.getCashValue()+vo.getMarketValue()+vo.getTotalAssets());
			}
			
		}
		return vo;
	}
	/**
	 * 获取账户的资产列表
	 * @author mqzou  2017-04-25
	 * @param accountId
	 * @return
	 */
	@Override
	public List<InvestorAccountCurrency> findAccountCurrency(String accountId) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from InvestorAccountCurrency r where r.account.id=?");
		List<Object> params=new ArrayList<Object>();
		params.add(accountId);
		List list=baseDao.find(hql.toString(), params.toArray(), false);
		
		return list;
	}
	/**
	 * 通过账号ID获取该账号所持有的PortfolioHoldProduct列表
	 * author:林文伟
	 * @param investorId
	 * @return
	 */
	@Override
	public List<PortfolioHoldProduct> findPortfolioHoldProductByAccountId(String accountId) {
		String hql = "FROM PortfolioHoldProduct a WHERE a.account.id=?";
		List<PortfolioHoldProduct> list = this.baseDao.find(hql, new String[] { accountId }, false);
		return list;
	}
	
	/**
	 * 获取投资人背景调查
	 * 
	 * @author wwlin
	 * @date 2016-09-18
	 */
	@Override
	//@Transactional(readOnly = true)
	public List<InvestorBackground> findInvestorbackground(String memberId) {
		String hql = "from InvestorBackground t where t.member.id = ?  ";
		List<String> params = new ArrayList<String>();
		params.add(memberId);

		List<InvestorBackground> list = this.baseDao.find(hql, params.toArray(), false);
		return list;
	}
	/**
	 * 获取投资人培训信息
	 * 
	 * @author wwlin
	 * @date 2016-09-18
	 */
	@Override
	//@Transactional(readOnly = true)
	public List<InvestorTraining> findInvestorTraining(String memberId) {
		String hql = "from InvestorTraining t where t.member.id = ?  ";
		List<String> params = new ArrayList<String>();
		params.add(memberId);

		List<InvestorTraining> list = this.baseDao.find(hql, params.toArray(), false);
		return list;
	}
	/***
	 * IFA问卷列表查询的方法
	 * 
	 * @author 林文伟
	 * @date 2016-09-25
	 */
	@Override
	public List<RpqExam> findRpqExamByMemberId(String langCode, String memberId) {
		String hql = " from RpqExam r  ";
		hql += " inner join " + this.getLangString("RpqExam", langCode);
		hql += " l on l.id=r.id ";
		hql += " where r.status='1' and r.member.id = '" + memberId + "'";
		// List<Object> params=new ArrayList<Object>();
		hql += " order by r.createTime desc";
		// System.out.println("3333333333333"+hql);
		List<RpqExam> rpqExamList = new ArrayList<RpqExam>();
		List list1 = this.baseDao.find(hql, null, false);
		if (!list1.isEmpty()) {
			for (int x = 0; x < list1.size(); x++) {
				// System.out.println("3333333333333");
				RpqExam vo = new RpqExam();
				Object[] each2 = (Object[]) list1.get(x);
				Object questObj2 = (Object) each2[0];// RpqExam实体
				Object questObj3 = (Object) each2[1];// 多语言实体
				if (questObj2 instanceof RpqExam) {
					vo = ((RpqExam) questObj2);
				}
				if (questObj3 instanceof RpqExamEn) {
					// System.out.println("5555555555555");
					String title = ((RpqExamEn) questObj3).getTitle();
					vo.setTitle(title);
				}
				if (questObj3 instanceof RpqExamSc) {
					// System.out.println("5555555555555");
					String title = ((RpqExamSc) questObj3).getTitle();
					vo.setTitle(title);
				}
				if (questObj3 instanceof RpqExamTc) {
					// System.out.println("5555555555555");
					String title = ((RpqExamTc) questObj3).getTitle();
					vo.setTitle(title);
				}
				rpqExamList.add(vo);
			}
		}

		return rpqExamList;
	}
	/***
	 * 获取账户关联联系人id
	 * 
	 * @param accountId
	 *            账户
	 * @param contactType
	 *            联系人类型M--主联系人，J--关联联系人
	 * @return contactId
	 * */
	public String getAccountContactId(String accountId, String contactType) {

		// String hql =
		// "from InvestorAccountContactAgeas t where t.account.id=? and t.contactType=? order by t.id ";
		String hql = "from InvestorAccountContact t where t.account.id=? and t.contactType=? order by t.id ";

		List params = new ArrayList();
		params.add(accountId);
		params.add(contactType);
		List<InvestorAccountContact> voList = this.baseDao.find(hql, params.toArray(), false);
		if (voList.isEmpty())
			return null;
		return voList.get(0).getId();
	}
	
	/***
	 * 获取需要检查的文档 modify by mqzou 2016-10-24
	 * 
	 * @param memberId
	 *            账号
	 * @param distrubuteId
	 *            代理商
	 * @param contactId
	 *            联系人
	 * @param langCode
	 *            当前语言
	 * */
	public List<DocListVO> findContactDocList(String memberId, String distrubuteId, String contactId, String langCode,String accountId) {
		String hql = " select t.id,out.docName,t.docTemplate.id,t.isNecessary,t.updateCycle,t.expireDate,t.submitDate,";
		hql += " t.checkStatus ,t.isValid,t.docListId ";
		hql += " from InvestorDoc t inner join " + this.getLangString("InvestorDoc", langCode);
		hql += " out on t.id=out.id ";
		hql += " where t.member.id=? and t.distributor.id=? ";

		
		List params = new ArrayList();
		params.add(memberId);
		params.add(distrubuteId);
		if(StringUtils.isNotBlank(accountId)){
			hql += " and t.account.id=?";
			params.add(accountId);
		}else{
			hql += " and t.contact.id=? ";
			params.add(contactId);
		}
		hql += " order by t.isValid desc,t.createTime desc ";
		List voList = this.baseDao.find(hql, params.toArray(), false);
		if (voList.isEmpty())
			return null;

		List<DocListVO> list = new ArrayList<DocListVO>();
		for (int z = 0; z < voList.size(); z++) {
			Object[] objs = (Object[]) voList.get(z);
			DocListVO vo = new DocListVO();
			vo.setId(objs[0] == null ? "" : objs[0].toString());
			vo.setDocName(objs[1] == null ? "" : objs[1].toString());
			vo.setTemplateId(objs[2] == null ? "" : objs[2].toString());
			vo.setIsNecessary(objs[3] == null ? "" : objs[3].toString());
			vo.setUpdateCycle(objs[4] == null ? 0 : Integer.parseInt(objs[4].toString()));
			vo.setEffectDate(objs[5] == null ? null : DateUtil.StringToDate(objs[5].toString(), "yyyy-MM-dd hh:mm:ss"));
			vo.setCreateTime(objs[6] == null ? new Date() : DateUtil.StringToDate(objs[6].toString(), "yyyy-MM-dd hh:mm:ss"));
			vo.setCheckStatus(objs[7] == null ? "" : objs[7].toString());
			String isValid = objs[8]==null?"1":objs[8].toString();
			if("0".equals(isValid)){
				vo.setCheckStatus("3");//文档失效
			}
			vo.setDocListId(objs[9]==null?"":objs[9].toString());
			if(null!=vo.getEffectDate()){
				String expireStatus = vo.getEffectDate().after(new Date())?"0":"1";
				vo.setExpireStatus(expireStatus);
				Calendar betweenDay = Calendar.getInstance();
				betweenDay.setTime(vo.getEffectDate());
				int dayEff = betweenDay.get(Calendar.DAY_OF_YEAR);
				betweenDay.setTime(new Date());
				int dayNow = betweenDay.get(Calendar.DAY_OF_YEAR);
				vo.setEffectDateBetween(dayNow-dayEff);
			}
			
			
			// 查询已上传附件
			String fileHql = " from AccessoryFile t where t.relateId=? and t.moduleType=? order by t.createTime desc ";
			List fileParams = new ArrayList();
			fileParams.add(objs[0].toString());
			fileParams.add("investor_doc");
			List<AccessoryFile> fileList = this.baseDao.find(fileHql, fileParams.toArray(), false);
			vo.setFileList(fileList);
			list.add(vo);
		}
		return list;
	}
	
	/**
	 * 从模板复制doclist到invest
	 * @author scshi_u330p
	 * @params distrubuteId 代理商
	 * @params clientType 开户类型
	 * @params loginLangFlag 当前语言
	 * @params memberId 当前登录用户
	 * @params mainContactId 联系人id
	 * */
	public void copyDocListfromTemp(String distrubuteId,String clientType, 
			String loginLangFlag, String memberId,String contactId,InvestorAccount account){
		
		String docTemplateHql = "select dt.id,out.title ";
		docTemplateHql += " from DocTemplate dt inner join " + this.getLangString("DocTemplate", loginLangFlag);
		docTemplateHql += " out on dt.id=out.id ";
		docTemplateHql += " WHERE dt.isValid = 1 and dt.distributor.id = ? and dt.clientType = ? ";
		docTemplateHql += " and dt.isDefault=1 and dt.status='using' ";
		List<String> docTemplateParams = new ArrayList<String>();
		docTemplateParams.add(distrubuteId);
		docTemplateParams.add(clientType);
		// docTemplateParams.add(langCode);
		List voList = this.baseDao.find(docTemplateHql, docTemplateParams.toArray(), false);
		List<DocTemplate> docTemplatesList = new ArrayList<DocTemplate>();
		if (!voList.isEmpty()) {
			for (int x = 0; x < voList.size(); x++) {
				Object[] objs = (Object[]) voList.get(x);
				DocTemplate temp = new DocTemplate();
				String tempId = objs[0] == null ? "" : objs[0].toString();
				if (!"".equals(tempId)) {
					temp = (DocTemplate) this.baseDao.get(DocTemplate.class, tempId);
					if(null!=temp){
						temp.setTitle(objs[1] == null ? "" : objs[1].toString());	
					}
				}
				if(null!=temp)
				  docTemplatesList.add(temp);
			}
		}

		if (null != docTemplatesList && !docTemplatesList.isEmpty()) {
			String docListHql = " from DocList t where t.template.id = ? ORDER BY t.id ";
			List<String> docListParams = new ArrayList<String>();
			docListParams.add(docTemplatesList.get(0).getId());
			List docList = this.baseDao.find(docListHql, docListParams.toArray(), false);
			if (!docList.isEmpty()) {

				MemberBase member = new MemberBase();
				member.setId(memberId);

				MemberDistributor distributor = new MemberDistributor();
				distributor.setId(distrubuteId);

	
				InvestorAccountContact contact = new InvestorAccountContact();
				contact.setId(contactId);
				
				DocTemplate docTemplate = docTemplatesList.get(0);
				for (int y = 0; y < docList.size(); y++) {
					DocList docModel = (DocList) docList.get(y);
					String docModelId = docModel.getId();
					InvestorDoc investDoc = new InvestorDoc();

					investDoc.setId(null);
					investDoc.setCreateTime(new Date());
					investDoc.setDistributor(distributor);
					investDoc.setMember(member);
					investDoc.setDocTemplate(docTemplate);
					if(StringUtils.isNotBlank(contactId)){
						investDoc.setContact(contact);
					}
					investDoc.setIsNecessary(docModel.getIsNecessary());
					investDoc.setUpdateCycle(docModel.getUpdateCycle());
					investDoc.setCreateTime(new Date());
					investDoc.setLastUpdate(new Date());
					investDoc.setIsValid("1");
					investDoc.setAccount(account);
					investDoc.setDocListId(docModel.getId());
					this.baseDao.saveOrUpdate(investDoc);

					// 简体中文版checklist保存
					InvestorDocSc investDocSc = new InvestorDocSc();
					DocListSc docModelSc = (DocListSc) this.baseDao.get(DocListSc.class, docModelId);
					investDocSc.setId(investDoc.getId());
					investDocSc.setDocName(docModelSc.getDocName());
					this.baseDao.create(investDocSc);

					// 繁体
					InvestorDocTc investDocTc = new InvestorDocTc();
					DocListTc docModelTc = (DocListTc) this.baseDao.get(DocListTc.class, docModelId);
					investDocTc.setId(investDoc.getId());
					investDocTc.setDocName(docModelTc.getDocName());
					this.baseDao.create(investDocTc);

					// 英文
					InvestorDocEn investDocEn = new InvestorDocEn();
					DocListEn docModelEn = (DocListEn) this.baseDao.get(DocListEn.class, docModelId);
					investDocEn.setId(investDoc.getId());
					investDocEn.setDocName(docModelEn.getDocName());
					this.baseDao.create(investDocEn);
				}
			}
		}
	}
	/**
	 * 判断文档是否需要更新
	 * @author scshi_u330p
	 * @date 20170112
	 * **/
	public void checkTemplateUpdate(InvestorAccountVO vo,String contactId,String clientType,String accountId){
		
		String memberId = vo.getMemberId();
		String distributorId = vo.getDistributorId();
		
		StringBuffer docSql = new StringBuffer("select t,dt from DocList t left join DocTemplate dt on t.template.id=dt.id ");
		docSql.append("where dt.distributor.id=? and dt.clientType=? ");
		docSql.append("and dt.isDefault=1 and dt.isValid=1 and t.isValid='1' and dt.status='using' ");
		docSql.append(" and t.id not in (select d.docListId from InvestorDoc d where d.isValid='1' ");
		List params = new ArrayList();
		params.add(distributorId);
		params.add(clientType);
		if(StringUtils.isNotBlank(accountId)){
			docSql.append(" and d.account.id=? ) ");
			params.add(accountId);
		}else{//只作为备用，doclist与联系人脱离关系，使用账户id关联
			docSql.append(" and d.contact.id=? )");
			params.add(contactId);
		}
		
		MemberBase member = new MemberBase();
		member.setId(memberId);

		MemberDistributor distributor = new MemberDistributor();
		distributor.setId(distributorId);

		InvestorAccountContact contact = new InvestorAccountContact();
		contact.setId(contactId);
		
		InvestorAccount account = new InvestorAccount();
		account.setId(accountId);
		
		//获取需要新增的模板
		List docList = this.baseDao.find(docSql.toString(), params.toArray(), false);
		if(!docList.isEmpty()){
			for(int x=0;x<docList.size();x++){
				Object[] objs = (Object[]) docList.get(x);
				DocList docModel = (DocList)objs[0];
				
				String docModelId = docModel.getId();
				InvestorDoc investDoc = new InvestorDoc();

				investDoc.setId(null);
				investDoc.setCreateTime(new Date());
				investDoc.setDistributor(distributor);
				investDoc.setMember(member);
				investDoc.setDocTemplate(docModel.getTemplate());
//				if(StringUtils.isNotBlank(contactId)){
//					investDoc.setContact(contact);
//				}
				investDoc.setIsNecessary(docModel.getIsNecessary());
				investDoc.setUpdateCycle(docModel.getUpdateCycle());
				investDoc.setCreateTime(new Date());
				investDoc.setLastUpdate(new Date());
				investDoc.setIsValid("1");
				investDoc.setAccount(account);
				investDoc.setDocListId(docModel.getId());
				this.baseDao.saveOrUpdate(investDoc);

				// 简体中文版checklist保存
				InvestorDocSc investDocSc = new InvestorDocSc();
				DocListSc docModelSc = (DocListSc) this.baseDao.get(DocListSc.class, docModelId);
				investDocSc.setId(investDoc.getId());
				investDocSc.setDocName(docModelSc.getDocName());
				this.baseDao.create(investDocSc);

				// 繁体
				InvestorDocTc investDocTc = new InvestorDocTc();
				DocListTc docModelTc = (DocListTc) this.baseDao.get(DocListTc.class, docModelId);
				investDocTc.setId(investDoc.getId());
				investDocTc.setDocName(docModelTc.getDocName());
				this.baseDao.create(investDocTc);

				// 英文
				InvestorDocEn investDocEn = new InvestorDocEn();
				DocListEn docModelEn = (DocListEn) this.baseDao.get(DocListEn.class, docModelId);
				investDocEn.setId(investDoc.getId());
				investDocEn.setDocName(docModelEn.getDocName());
				this.baseDao.create(investDocEn);
			}
		}
		
		//逻辑删除文档，文档设置为不可用状态
		List delParams = new ArrayList();
		StringBuffer delSql = new StringBuffer("from InvestorDoc t where t.docListId not in( ");
		delSql.append(" select l.id from DocList l left join DocTemplate dt on l.template.id=dt.id ");
		delSql.append(" where dt.distributor.id=? and dt.clientType=? ");
		delSql.append(" and dt.isDefault=1 and dt.isValid=1 and l.isValid='1' ) and t.isValid='1' ");
		delParams.add(distributorId);
		delParams.add(clientType);
		
		if(StringUtils.isNotBlank(accountId)){
			delSql.append(" and t.account.id=?  ");
			delParams.add(accountId);
		}else{//只作为备用，doclist与联系人脱离关系，使用账户id关联
			delSql.append(" and t.contact.id=? ");
			delParams.add(contactId);
		}
		
		List<InvestorDoc> delList = this.baseDao.find(delSql.toString(), delParams.toArray(), false);
		if(!delList.isEmpty()){
			for(int y=0;y<delList.size();y++){
				InvestorDoc delDoc = delList.get(y);
				delDoc.setIsValid("0");
				this.baseDao.saveOrUpdate(delDoc);
			}
		}
		
		//end
	}
}
