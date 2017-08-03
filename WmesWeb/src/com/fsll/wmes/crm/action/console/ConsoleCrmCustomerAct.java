package com.fsll.wmes.crm.action.console;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fsll.core.CoreConstants;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.crm.service.CrmCustomerService;
import com.fsll.wmes.crm.vo.PortfolioVO;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberCorporate;
import com.fsll.wmes.entity.MemberFi;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.PortfolioInfo;
import com.fsll.wmes.member.service.MemberManageServiceForConsole;
import com.fsll.wmes.member.vo.CoporateVO;
import com.fsll.wmes.member.vo.FiVO;
import com.fsll.wmes.member.vo.IfaVO;
import com.fsll.wmes.member.vo.IndividualVO;
import com.fsll.wmes.member.vo.MemberSsoVO;

@Controller
public class ConsoleCrmCustomerAct extends WmesBaseAct {

	@Autowired
	MemberManageServiceForConsole memberManageService;
	@Autowired
	CrmCustomerService crmCustomerService;
	
	/**
	 * ifa客户列表页面
	 * @author mqzou modify qgfeng
	 * @date 2016-08-18
	 */
	@RequestMapping(value = "/console/customer/list")
	public String customerlist(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		this.isMobileDevice(request, model);
		//只允许ifaFirm管理员
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if(curAdmin==null || curAdmin.getIfafirm()==null){
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
		return "console/customer/inverstor_list";
	}

	/**
	 * 获取ifa的客户列表
	 * @author mqzou  modify qgfeng
	 * @date 2016-08-18
	 */
	@RequestMapping(value = "/console/customer/listJson")
	public void findInvestorByIfa(IfaVO ifaVO ,HttpServletRequest request, HttpServletResponse response) {
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		String langCode = getLoginLangFlag(request);
		ifaVO.setIfafirmId(curAdmin.getIfafirm().getId());
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = crmCustomerService.findInvestorByIfa(jsonPaging, ifaVO, langCode);
		this.toJsonString(response, jsonPaging);
	}

	/**
	 * ifa客户详细信息页面
	 * 
	 * @author mqzou
	 * @date 2016-08-18
	 */
	@RequestMapping(value = "/console/ifa/customerdetail")
	public String investorDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		this.isMobileDevice(request, model);
		String id = request.getParameter("id");
		String memberType = request.getParameter("memberType");
		if (CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_COPORATE == Integer.parseInt(memberType)) {
			model.put("id", id);
			MemberCorporate obj = memberManageService.findCorporateById(id);
			CoporateVO vo = new CoporateVO();
			vo.setId(obj.getId());
			vo.setMemberId(null == obj.getMember() ? "" : obj.getMember().getId());
			vo.setCountry(null == obj.getCountry() ? "" : obj.getCountry().getId());
			vo.setIncorporationPlace(null == obj.getIncorporationPlace() ? "" : obj.getIncorporationPlace().getId());
			vo.setCompanyName(obj.getCompanyName());
			vo.setEntityOther(obj.getEntityOther());
			vo.setEntityType(obj.getEntityType());
			vo.setGiin(obj.getGiin());
			vo.setIncorporationDate(obj.getIncorporationDate());
			vo.setIsFinancial(obj.getIsFinancial());
			vo.setMailingAddress(obj.getMailingAddress());
			vo.setNaturePurpose(obj.getNaturePurpose());
			vo.setRegisteredAddress(obj.getRegisteredAddress());
			vo.setRegisterNo(obj.getRegisterNo());
			vo.setWebsite(obj.getWebsite());

			vo.setEmail(obj.getMember().getEmail());

			vo.setLoginCode(obj.getMember().getLoginCode());
			vo.setMobileNumber(obj.getMember().getMobileNumber());
			vo.setNickName(obj.getMember().getNickName());
			vo.setPassword(obj.getMember().getPassword());
			MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
			boolean editable = false;
			if (CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_SYSTEM == curMember.getMemberType()) {
				editable = true;
			}
			model.put("editable", editable);
			model.put("coporatevo", vo);
			model.put("title", "Coporate详细信息");
			return "console/customer/coporator_input";
		} else if (CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_INDIVIDUAL == Integer.parseInt(memberType)) {
			model.put("id", id);
			MemberIndividual obj = memberManageService.findIndividualById(id);
			IndividualVO vo = new IndividualVO();
			vo.setId(obj.getId());
			vo.setMemberId(null == obj.getMember() ? "" : obj.getMember().getId());
			vo.setAddress(obj.getAddress());
			vo.setBorn(obj.getBorn());
			vo.setCertNo(obj.getCertNo());
			vo.setCertType(obj.getCertType());
			vo.setCountry(obj.getCountry() == null ? "" : obj.getCountry().getId());
			vo.setEducation(obj.getEducation());
			vo.setEmail(obj.getMember().getEmail());
			vo.setEmployment(obj.getEmployment());
			vo.setFirstName(obj.getFirstName());
			vo.setGender(obj.getGender());
			vo.setLastName(obj.getLastName());
			vo.setLoginCode(obj.getMember().getLoginCode());
			vo.setMobileNumber(obj.getMember().getMobileNumber());
			vo.setNameChn(obj.getNameChn());
			vo.setNationality(obj.getNationality() == null ? "" : obj.getNationality().getId());
			vo.setNickName(obj.getMember().getNickName());
			vo.setOccupation(obj.getOccupation());
			vo.setPassword(obj.getMember().getPassword());
			vo.setTelephone(obj.getTelephone());
			MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
			boolean editable = false;
			if (CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_SYSTEM == curMember.getMemberType()) {
				editable = true;
			}
			model.put("individualvo", vo);
			model.put("title", "Individual详细信息");
			model.put("editable", editable);
			return "console/customer/individual_input";
		} else if (CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_FI == Integer.parseInt(memberType)) {
			model.put("id", id);
			MemberFi obj = memberManageService.findFiById(id);
			FiVO vo = new FiVO();
			vo.setId(obj.getId());
			vo.setMemberId(null == obj.getMember() ? "" : obj.getMember().getId());
			vo.setCountry(null == obj.getCountry() ? "" : obj.getCountry().getId());
			vo.setIncorporationPlace(null == obj.getIncorporationPlace() ? "" : obj.getIncorporationPlace().getId());
			vo.setCompanyName(obj.getCompanyName());
			vo.setEntityOther(obj.getEntityOther());
			vo.setEntityType(obj.getEntityType());
			vo.setGiin(obj.getGiin());
			vo.setIncorporationDate(obj.getIncorporationDate());
			vo.setIsFinancial(obj.getIsFinancial());
			vo.setMailingAddress(obj.getMailingAddress());
			vo.setNaturePurpose(obj.getNaturePurpose());
			vo.setRegisteredAddress(obj.getRegisteredAddress());
			vo.setRegisterNo(obj.getRegisterNo());
			vo.setWebsite(obj.getWebsite());

			vo.setEmail(obj.getMember().getEmail());

			vo.setLoginCode(obj.getMember().getLoginCode());
			vo.setMobileNumber(obj.getMember().getMobileNumber());
			vo.setNickName(obj.getMember().getNickName());
			vo.setPassword(obj.getMember().getPassword());
			MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
			boolean editable = false;
			if (CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_SYSTEM == curMember.getMemberType()) {
				editable = true;
			}
			model.put("editable", editable);
			model.put("fivo", vo);
			model.put("title", "FI详细信息");
			return "console/customer/fi_input";
		} else {
			return "";
		}
	}
	
	/**
	 * 获取ifa客户的投资组合列表
	 * 
	 * @author mqzou
	 * @date 2016-08-23
	 */
	@RequestMapping(value = "/console/customer/portfolioListJson")
	public void getPortfolioList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String memberId=request.getParameter("memberId");
		PortfolioInfo portfolioInfo=new PortfolioInfo();
		if(null!=memberId && !"".equals(memberId)){
			MemberBase memberBase=new MemberBase();
			memberBase.setId(memberId);
			portfolioInfo.setMember(memberBase);
		}
		jsonPaging=getJsonPaging(request);
		jsonPaging=crmCustomerService.findPortfolioByCustomer(jsonPaging, portfolioInfo);
		toJsonString(response, jsonPaging);
		
	}
	
	/**
	 * ifa客户的投资组合列表页面
	 * 
	 * @author mqzou
	 * @date 2016-08-23
	 */
	@RequestMapping(value = "/console/customer/portfolioList")
	public String showPortfolioList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String id=request.getParameter("memberId");
		model.put("memberId", id);
		return "console/portfolio/list";
	}
	
	/**
	 * ifa客户的投资组合列表页面
	 * 
	 * @author mqzou
	 * @date 2016-08-24
	 */
	@RequestMapping(value = "/console/customer/portfolioDetail")
	public String showPortfolioDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String id=request.getParameter("id");
		PortfolioInfo info=crmCustomerService.findPortfolioInfo(id);
		PortfolioVO vo=new PortfolioVO();
		if(null!=info){
			//vo.setAipExecCycle(info.getAipExecCycle());
			//vo.setAipNextTime(info.getAipNextTime());
			//vo.setAipTimeDistance(info.getAipTimeDistance());
			vo.setAipType(info.getAipFlag());
			vo.setCreateTime(info.getCreateTime());
			vo.setCreator(null!=info.getCreator()?info.getCreator().getNickName():"");
			vo.setCreatorId(null!=info.getCreator()?info.getCreator().getId():"");
			vo.setCurrencyType(info.getBaseCurrency());
		//	vo.setDesc(info.getDesc());
			vo.setId(info.getId());
			vo.setLoginCode(null!=info.getMember()?info.getMember().getLoginCode():"");
			vo.setMemberId(null!=info.getMember()?info.getMember().getId():"");
			vo.setNickName(null!=info.getMember()?info.getMember().getNickName():"");
			//vo.setObjectiveDesc(info.getObjectiveDesc());
			vo.setPortfolioName(info.getPortfolioName());
			vo.setProposalId(null!=info.getProposal()?info.getProposal().getId():"");
			vo.setProposalName(null!=info.getProposal()?info.getProposal().getProposalName():"");
//			vo.setReturnRate(info.getReturnRate());
			//vo.setReturnTotal(info.getReturnTotal());
			//vo.setRiskLeve(info.getRiskLeve());
			//vo.setStatus(info.getStatus());
			/*vo.setTotalInvestAmount(info.getTotalInvestAmount());
			vo.setType(info.getType());*/
		}
		model.put("portfolio", vo);
		return "console/portfolio/input";
	}

}
