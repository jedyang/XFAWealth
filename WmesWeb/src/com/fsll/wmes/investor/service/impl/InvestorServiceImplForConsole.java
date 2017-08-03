package com.fsll.wmes.investor.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.MemberIfaIfafirm;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.WfProcedureInstanseTodo;
import com.fsll.wmes.investor.service.InvestorServiceForConsole;
import com.fsll.wmes.investor.vo.AccountVO;
import com.fsll.wmes.investor.vo.ApprovalHisVO;
import com.fsll.wmes.investor.vo.InvestorAccountVO;
import com.fsll.wmes.investor.vo.InvestorApprovalVO;

/***
 * 业务接口实现类：investor管理接口类
 * 
 * @author mqzou
 * @date 2016-06-22
 */
@Service("investorServiceForConsole")
//@Transactional
public class InvestorServiceImplForConsole extends BaseService implements InvestorServiceForConsole {

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
			vo.setDiscFlag(account.getAuthorized());
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
	public JsonPaging findInvestorAccount(JsonPaging jsonPaging,String adminType, String adminMemberId) {
		// TODO Auto-generated method stub
		StringBuilder hql = new StringBuilder(" from InvestorAccount l ");
		
		List<Object> params=new ArrayList<Object>();
		if("0".equals(adminType)){
			hql.append(" where l.isValid='1' ");
		}else if("1".equals(adminType)){
			hql.append(" where l.isValid='1' ");
			hql.append(" and ( l.ifa != null and l.ifa.ifafirm != null and l.ifa.ifafirm.id = ? ) ");
			params.add(adminMemberId);
		}else if("2".equals(adminType)){
			hql.append(" where l.isValid='1' ");
			hql.append(" and ( l.distributor != null and l.distributor.id = ? ) ");
			params.add(adminMemberId);
		}else {
			hql.append(" where l.isValid='1' ");
			hql.append(" and 1=2 ");
		}
		
		jsonPaging=this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		Iterator it=(Iterator)jsonPaging.getList().iterator();
		List list=new ArrayList();
		while (it.hasNext()) {
			InvestorAccount account = (InvestorAccount) it.next();
			InvestorAccountVO vo = new InvestorAccountVO();
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
			vo.setNickName(null!=account.getMember()?account.getMember().getNickName():"");
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
			hql.append(" and i.isValid='1' and (i.flowStatus='3' or i.flowStatus='4')"); //待Ifa firm 审批
			
			hql.append(" and ( i.ifa !=null and i.ifa.ifafirm !=null and i.ifa.ifafirm.id=? ) ");
			params.add(adminMemberId);
		}else if(CommonConstantsWeb.MEMBERADMIN_TYPE_DISTRIBUTOR.equals(adminType)){
			hql.append(" and i.isValid='1' and (i.flowStatus='5'or i.flowStatus='6')");//待Distributor 审批
			//hql.append(" and (i.distributor != null and i.distributor.id = ? ) ");
			params.add(adminMemberId);
		}else {
			hql.append(" where i.isValid='1'   ");
		}
		hql.append(" and w.isValid='1' and w.flowState='0'");
		
		
		jsonPaging=this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		Iterator it=(Iterator)jsonPaging.getList().iterator();
		List list=new ArrayList();
		while (it.hasNext()) {
			// modify by mqzou 2016-08-30
			Object[] obj=(Object[])it.next();
			InvestorAccount account = (InvestorAccount)obj[1];
			WfProcedureInstanseTodo todo=(WfProcedureInstanseTodo)obj[0];
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
			if(curUserId.equals(approveUserId)){//是否可以审批
				vo.setCanApprove("1");
			}else {
				vo.setCanApprove("0");
			}
			
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
			vo.setDiscFlag(account.getAuthorized());
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
			vo.setApprover(null!=todo.getFlowUser()?todo.getFlowUser().getLoginCode():"");
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
	 * 获取主账户select option
	 * @author scshi_u330p
	 * @date 20170405
	 * */
	public List findMasterAccountByDisId(String distributorId){
		StringBuffer masterHql = new StringBuffer("from InvestorAccount t where t.distributor.id=? and t.subFlag='0' ");
		masterHql.append(" and t.openStatus='3' and t.finishStatus='1' and t.isValid='1' ");
		List<InvestorAccount> masterList = this.baseDao.find(masterHql.toString(), new String[]{distributorId}, false);
		return masterList;
	}
	
	/**
	 * 加载主账户关联的ifa，ifa公司，投资人，交易账号
	 * @author scshi_u330p
	 * @date 20170405
	 * */
	public AccountVO loadMasterAccountInfoForSub(String masterId,String langCode){
		InvestorAccount msAccount = (InvestorAccount)this.baseDao.get(InvestorAccount.class, masterId);
		AccountVO vo = new AccountVO();
		vo.setId(masterId);
		vo.setAccountNo(msAccount.getAccountNo());
		if(null!=msAccount.getMember()){
			vo.setMemberId(msAccount.getMember().getId());
			StringBuffer customerSql = new StringBuffer("from MemberIndividual t where t.member.id=? ");
			List<MemberIndividual> indvList = this.baseDao.find(customerSql.toString(),new String[]{msAccount.getMember().getId()},false);
			if(null!=indvList && !indvList.isEmpty()){
				MemberIndividual idv = indvList.get(0);
				vo.setCustomerName(idv.getFirstName()+" "+idv.getLastName());
			}
		}	
		vo.setCustomerName(msAccount.getMember()==null?"":msAccount.getMember().getImNickName());
		
		if(null!=msAccount.getIfa()){
			vo.setIfaId(msAccount.getIfa().getId());
			vo.setIfaName(msAccount.getIfa().getFirstName()+" "+msAccount.getIfa().getLastName());
			
			StringBuffer firmHql = new StringBuffer("select t.ifafirm.id,lang.companyName from MemberIfaIfafirm t ");
			firmHql.append("left join "+this.getLangString("MemberIfafirm", langCode)+" lang on t.ifafirm.id=lang.id ");
			firmHql.append("where t.ifa.id=? and t.checkStatus='1' order by t.lastUpdate desc ");
			List firmList = this.baseDao.find(firmHql.toString(), new String[]{msAccount.getIfa().getId()}, false);
			if(null!=firmList && !firmList.isEmpty()){
				Object[] objs = (Object[])firmList.get(0);
				vo.setIfafirmId(null==objs[0]?"":objs[0].toString());
				vo.setIfafirmName(null==objs[1]?"":objs[1].toString());
			}
		}
		return vo;
	}
	

}
