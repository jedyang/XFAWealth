/**
 * 
 */
package com.fsll.app.investor.me.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.common.service.AppWebReadToDoService;
import com.fsll.app.common.service.AppWebTaskListService;
import com.fsll.app.investor.me.service.AppCrmProposalMessService;
import com.fsll.app.investor.me.vo.AppCrmProposalMessVo;
import com.fsll.app.investor.me.vo.AppPortfolioMessVo;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.PropertyUtils;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.CrmProposal;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioInfo;
import com.fsll.wmes.entity.PortfolioInfoProduct;
import com.fsll.wmes.entity.WebReadToDo;
import com.fsll.wmes.entity.WebTaskList;

/**
 * 我的客户的投资方案接口服务接口实现类
 * @author zpzhou
 * @date 2016-9-22
 */
@Service("appCrmProposalMessService")
//@Transactional
public class AppCrmProposalMessServiceImpl extends BaseService implements AppCrmProposalMessService {
	
	@Autowired
	private AppWebReadToDoService webReadToDoService;
	@Autowired
	private AppWebTaskListService webTaskListService;
	
	/**
	 * 得到我的投资方案列表
	 * @param memberId 用户ID
	 * @param ifaId 
	 * @param keyWord 搜索关键词
	 * @param toCurrency 货币转换参数
	 * @return
	 */
	public List<AppCrmProposalMessVo> findMyProposalList(String memberId,String ifaId,String keyWord,String toCurrency,String langCode){
		String toCurrencyName = getParamConfigName(langCode, toCurrency, "currency_type");
		List<AppCrmProposalMessVo>  messList = new ArrayList<AppCrmProposalMessVo>();
		String hql = " from CrmProposal s where s.status not in ('-2','0') and s.member.id=? and s.isValid=? ";
		List params = new ArrayList();
		params.add(memberId);
		params.add("1");
		if(null!=keyWord && !"".equals(keyWord)){
			hql += " and s.proposalName like ? ";
			params.add("%"+keyWord+"%");
		}
		if(null!=ifaId && !"".equals(ifaId)){
			hql += " and s.ifaMember.id= ? ";
			params.add(ifaId);
		}
		hql += " order by s.lastUpdate desc ";
		
		List<CrmProposal> list = baseDao.find(hql, params.toArray(), false);
		for(int i=0;i<list.size();i++){
			CrmProposal info=list.get(i);
			AppCrmProposalMessVo vo =new AppCrmProposalMessVo();
			vo.setId(info.getId());
			vo.setProposalName(info.getProposalName());
			
			if("".equals(toCurrency)){
				vo.setBaseCurrId(getParamConfigName(langCode, info.getBaseCurrId(), "currency_type"));
				vo.setInitialInvestAmount(getFormatNum(info.getInitialInvestAmount(),info.getBaseCurrId()));
			}else {
				vo.setBaseCurrId(toCurrencyName);
				vo.setInitialInvestAmount(getFormatNumByCurrency(info.getInitialInvestAmount(),info.getBaseCurrId(),toCurrency));
			}
			
			vo.setRemark(info.getRemark());
			vo.setStatus(info.getStatus());
			vo.setLastUpdate(DateUtil.dateToDateString(info.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
			vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
			MemberBase memberBase=info.getIfaMember().getMember();//得到IFA的基础信息
			vo.setCreator(getCommonMemberName(memberBase.getId(), langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
			vo.setIconUrl(PageHelper.getUserHeadUrlForWS(memberBase.getIconUrl(), null) );
			messList.add(vo);
		}
		return messList;
	}
	/**
	 * 得到投资方案基本信息
	 * @param proposalId 方案ID
	 * @return
	 */
	public AppCrmProposalMessVo findMyProposalMess(String proposalId,String toCurrency,String langCode){
		String toCurrencyName = getParamConfigName(langCode, toCurrency, "currency_type");
		AppCrmProposalMessVo vo =new AppCrmProposalMessVo();
		String hql = " from CrmProposal p  where p.id=? ";
		List params = new ArrayList();
		params.add(proposalId);
		List list = baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			CrmProposal info=(CrmProposal)list.get(0);
			vo.setId(info.getId());
			vo.setProposalName(info.getProposalName());
			vo.setBaseCurrId(info.getBaseCurrId());
			if("".equals(toCurrency)){
				vo.setBaseCurrId(getParamConfigName(langCode, info.getBaseCurrId(), "currency_type"));
				vo.setInitialInvestAmount(getFormatNum(info.getInitialInvestAmount(),info.getBaseCurrId()));
			}else {
				vo.setBaseCurrId(toCurrencyName);
				vo.setInitialInvestAmount(getFormatNumByCurrency(info.getInitialInvestAmount(),info.getBaseCurrId(),toCurrency));	
			}
			
			vo.setLiquidityNeed(info.getLiquidityNeed());
			vo.setTimeFrame(info.getTimeFrame());
			vo.setTaxConcerns(info.getTaxConcerns());
			vo.setLrf(info.getLrf());
			vo.setUnp(info.getUnp());
			vo.setRemark(info.getRemark());
			vo.setStatus(info.getStatus());
			vo.setLastUpdate(DateUtil.dateToDateString(info.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
			vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
			MemberBase memberBase=info.getIfaMember().getMember();//得到IFA的基础信息
			vo.setCreator(getCommonMemberName(memberBase.getId(), langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
			String pdfUrl = findProposalPdfFile(info.getId(),langCode);
			vo.setPdfUrl(pdfUrl);
		}
		return vo;
	}
	
	
	private String findProposalPdfFile(String proposalId,String langCode){
		String hql = "from AccessoryFile f where f.moduleType='crm_proposal' and f.relateId=? and f.langCode=?";
		List<Object> params = new ArrayList<Object>();
		params.add(proposalId);
		params.add(langCode);
		List<AccessoryFile> files = baseDao.find(hql, params.toArray(), false);
		if(null != files && !files.isEmpty()){
			return files.get(0).getFilePath();
		}
		return null;
	}
	
	/**
	 * 得到某个方案下的投资组合列表
	 * @param proposalId 方案ID
	 * @return
	 */
	public List<AppPortfolioMessVo> findProposalPortfolioList(String proposalId,String toCurrency,String langCode){
		List<AppPortfolioMessVo>  messList = new ArrayList<AppPortfolioMessVo>();
		String hql = " from PortfolioInfo p left join PortfolioHold h where p.proposal.id=? and p.isValid=?";
		List params = new ArrayList();
		params.add(proposalId);
		params.add("1");
		List list = baseDao.find(hql, params.toArray(), false);
		String toCurrencyName = getParamConfigName(langCode, toCurrency, CommonConstants.SYS_PARAM_TYPE_CURRENCY);
		if(null != list && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[])list.get(i);
				PortfolioInfo info=(PortfolioInfo)objs[0];
				PortfolioHold hold=(PortfolioHold)objs[1];
				AppPortfolioMessVo vo =new AppPortfolioMessVo();
				vo.setId(info.getId());
				if(StringUtils.isBlank(toCurrency)){
					vo.setBaseCurrency(getParamConfigName(langCode, info.getBaseCurrency(), CommonConstants.SYS_PARAM_TYPE_CURRENCY));
				}else {
					vo.setBaseCurrency(toCurrencyName);
				}
				vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
				vo.setPortfolioName(info.getPortfolioName());
				vo.setRiskLevel(String.valueOf(info.getRiskLevel()));
				
				if(null!=hold){
					vo.setLastUpdate(DateUtil.dateToDateString(hold.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
					vo.setReturnRate(getFormatNumByPer(hold.getTotalReturnRate()));
					//货币转换--兑换率
					String fromCurrency = vo.getBaseCurrency();
					Double rate = getExchangeRate(fromCurrency, toCurrency);
					vo.setTotalAmount(getFormatNumByRate(hold.getTotalMarketValue(),rate,toCurrency));
					vo.setTotalReturn(getFormatNumByRate(hold.getTotalReturnValue(),rate,toCurrency));
				}
				messList.add(vo);
			}
		}
		return messList;
	}
	
	/**
	 * 【我的投资顾问】得到投资方案列表
	 * @author zxtan
	 * @date 2017-01-17
	 * @param memberId 用户ID
	 * @param ifaId 
	 * @param toCurrency 货币转换参数
	 * @return
	 */
	public List<AppCrmProposalMessVo> findMyIfaProposalList(String memberId,String ifaId,String toCurrency,String langCode){
		String toCurrencyName = getParamConfigName(langCode, toCurrency, "currency_type");
		List<AppCrmProposalMessVo>  messList = new ArrayList<AppCrmProposalMessVo>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from CrmProposal s where s.isValid='1' and s.status !='0' ");
		hql.append(" and s.member.id=? and s.ifaMember.id= ? ");
		hql.append(" order by s.lastUpdate desc ");
		List<String> params = new ArrayList<String>();
		params.add(memberId);
		params.add(ifaId);
				
		List<CrmProposal> list = baseDao.find(hql.toString(), params.toArray(), false);
		for(int i=0;i<list.size();i++){
			CrmProposal info=list.get(i);
			AppCrmProposalMessVo vo =new AppCrmProposalMessVo();
			vo.setId(info.getId());
			vo.setProposalName(info.getProposalName());
			if("".equals(toCurrency)){
				vo.setBaseCurrId(getParamConfigName(langCode, info.getBaseCurrId(), "currency_type"));
				vo.setInitialInvestAmount(getFormatNum(info.getInitialInvestAmount(),info.getBaseCurrId()));
			}else {
				vo.setBaseCurrId(toCurrencyName);
				vo.setInitialInvestAmount(getFormatNumByCurrency(info.getInitialInvestAmount(),info.getBaseCurrId(),toCurrency));
			}
			vo.setRemark(info.getRemark());
			vo.setStatus(info.getStatus());
			vo.setLastUpdate(DateUtil.dateToDateString(info.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
			vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
			MemberBase memberBase=info.getIfaMember().getMember();//得到IFA的基础信息
			vo.setCreator(getCommonMemberName(memberBase.getId(), langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
			vo.setIconUrl(PageHelper.getUserHeadUrlForWS(memberBase.getIconUrl(), null) );
			messList.add(vo);
		}
		return messList;
	}
	
	/**
	 * 更新方案状态
	 * @author zxtan
	 * @date 2017-03-06
	 * @param proposalId
	 * @return
	 */
	public boolean updateProposalConfirm(String proposalId,String status,String content,String ip){		
		String sql = "call pro_proposalConfirm (?,?,?,?) ";
		List<Object> params = new ArrayList<Object>();
		params.add(proposalId);
		params.add(status);
		params.add(content);
		params.add(ip);
		List list = this.springJdbcQueryManager.springJdbcQueryForList(sql,params.toArray());
		Iterator it = (Iterator) list.iterator();
	    while (it.hasNext()) {
	    	Map map = (HashMap) it.next();
	    	String id = map.get("id") == null ? "" : map.get("id").toString();
	    	return proposalId.equals(id);   	
	    }
	    
	    CrmProposal crmProposal = (CrmProposal) baseDao.get(CrmProposal.class, proposalId);
		if(crmProposal != null){
		    WebReadToDo webReadToDo = new WebReadToDo();
		 	webReadToDo.setType(CommonConstantsWeb.WEB_READ_MESSAGE_TYPE_NORMAL);
		 	webReadToDo.setRelateId(proposalId);
		 	webReadToDo.setFromMember(crmProposal.getMember());
		 	String key = null;
		 	if("-1".equals(status)){
		 		webReadToDo.setMsgUrl("/front/crm/proposal/createProposalSetOne.do");
		 		webReadToDo.setMsgParam("id="+proposalId+"&memberId="+crmProposal.getMember().getId());
		 		webReadToDo.setAppUrl("com.xinfinance.xfawealthInvestor.business.me.activity.MeProposalDetailActivity");
		 		webReadToDo.setAppParam("proposalId="+proposalId+"&status=-1");
			 	webReadToDo.setModuleType(CommonConstantsWeb.WEB_READ_MODULE_PROPOSAL_REJECT);
			 	key = "proposal.reject";
			 	// 插入待办
			 	WebTaskList taskList = new WebTaskList();
			 	taskList.setCreateDate(new Date());
			 	taskList.setFromMember(crmProposal.getMember());
			 	taskList.setHandleStatus("0");
			 	taskList.setIsValid("1");
			 	taskList.setModuleType("P");
			 	taskList.setOwner(crmProposal.getIfaMember().getMember());
			 	taskList.setRelateId(crmProposal.getId());
			 	taskList.setTaskUrl("/front/crm/proposal/createProposalSetOne.do");
			 	taskList.setTaskParam("id="+proposalId+"&memberId="+crmProposal.getMember().getId());
			 	taskList.setTitleSc(PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_SC, new String[]{getCommonMemberName(crmProposal.getMember().getId(), CommonConstants.LANG_CODE_SC, "2")}));
			 	taskList.setTitleTc(PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_TC, new String[]{getCommonMemberName(crmProposal.getMember().getId(), CommonConstants.LANG_CODE_TC, "2")}));
			 	taskList.setTitleEn(PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_EN, new String[]{getCommonMemberName(crmProposal.getMember().getId(), CommonConstants.LANG_CODE_EN, "2")}));
			 	webTaskListService.saveTaskList(taskList);
		 	}else if("2".equals(status)){
		 		webReadToDo.setMsgUrl("/front/crm/proposal/previewProposal.do");
		 		webReadToDo.setMsgParam("proposalId="+proposalId);
		 		webReadToDo.setAppUrl("com.xinfinance.xfawealthInvestor.business.me.activity.MeProposalDetailActivity");
		 		webReadToDo.setAppParam("proposalId="+proposalId+"&status=2");
			 	webReadToDo.setModuleType(CommonConstantsWeb.WEB_READ_MODULE_PROPOSAL_CONFIRMED);
			 	key = "proposal.confirmed";
			 	// 插入待办
			 	WebTaskList taskList = new WebTaskList();
			 	taskList.setCreateDate(new Date());
			 	taskList.setFromMember(crmProposal.getMember());
			 	taskList.setHandleStatus("0");
			 	taskList.setIsValid("1");
			 	taskList.setModuleType("P");
			 	taskList.setOwner(crmProposal.getIfaMember().getMember());
			 	taskList.setRelateId(crmProposal.getId());
			 	taskList.setTaskUrl("/front/crm/proposal/previewProposal.do");
			 	taskList.setTaskParam("proposalId="+proposalId);
			 	taskList.setTitleSc(PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_SC, new String[]{getCommonMemberName(crmProposal.getMember().getId(), CommonConstants.LANG_CODE_SC, "2")}));
			 	taskList.setTitleTc(PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_TC, new String[]{getCommonMemberName(crmProposal.getMember().getId(), CommonConstants.LANG_CODE_TC, "2")}));
			 	taskList.setTitleEn(PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_EN, new String[]{getCommonMemberName(crmProposal.getMember().getId(), CommonConstants.LANG_CODE_EN, "2")}));
			 	webTaskListService.saveTaskList(taskList);
		 	}
		 	webReadToDo.setIsApp("1");
		 	webReadToDo.setIsRead("0");
		 	webReadToDo.setOwner(crmProposal.getIfaMember().getMember());
		 	webReadToDo.setCreateTime(new Date());
		 	webReadToDo.setIsValid("1");
		 	String titleSc = PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_SC, new String[]{getCommonMemberName(crmProposal.getMember().getId(), CommonConstants.LANG_CODE_SC, "2")});
		 	String titleTc = PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_TC, new String[]{getCommonMemberName(crmProposal.getMember().getId(), CommonConstants.LANG_CODE_TC, "2")});
		 	String titleEn = PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_EN, new String[]{getCommonMemberName(crmProposal.getMember().getId(), CommonConstants.LANG_CODE_EN, "2")});
		 	webReadToDo.setTitle(titleEn);
		 	webReadToDo = webReadToDoService.saveWebReadToDo(webReadToDo,titleEn,titleSc,titleTc);	    
		}
		
		return false;
	}
	


	
}
