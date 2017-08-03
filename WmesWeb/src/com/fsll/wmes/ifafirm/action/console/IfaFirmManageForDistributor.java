/**
 * 
 */
package com.fsll.wmes.ifafirm.action.console;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.core.CoreConstants;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.ifafirm.service.IfaFirmForDistributorService;
import com.fsll.wmes.ifafirm.vo.IfaFrimDistributorVo;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmVO;
import com.fsll.wmes.member.vo.MemberSsoVO;

/**
 * @author scshi_u330p
 */
@Controller
@RequestMapping("/console/ifafirm/dis")
public class IfaFirmManageForDistributor extends WmesBaseAct{
	@Autowired
	private IfaFirmForDistributorService ifaFirmForDistributorService;
	
	/**
	 * ifa公司列表页面
	 * @author scshi_u330p
	 * @date 20170102
	 * */
	@RequestMapping(value = "/firmPage", method = RequestMethod.GET)
	public String firmPage(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		
		return "console/ifafirm/distributor/firmPage";
	}
	
	/**
	 * 获取ifa公司分页数据
	 * @author scshi_u330p
	 * @date 20170102
	 * */
	@RequestMapping(value = "/firmList", method = RequestMethod.POST)
	public void list(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		IfaFrimDistributorVo svo = new IfaFrimDistributorVo();
		String firmName = request.getParameter("firmName");
		String productName = request.getParameter("productName");
		
		//获取当前登录者信息
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.FRONT_USER_SSO);
		String language = this.getLoginLangFlag(request);//获取语言
		
		svo.setDistributorId(curMember.getDistributorId());
		svo.setFirmName(firmName);
		svo.setProductName(productName);
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = ifaFirmForDistributorService.loadIfaFirmList(svo,language,jsonPaging);
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * ifa公司详情
	 * @author scshi_u330p
	 * */
	@RequestMapping(value = "/firmDetail", method = RequestMethod.GET)
	public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String id = request.getParameter("id");
		String langCode = this.getLoginLangFlag(request);
		MemberIfafirmVO vo = ifaFirmForDistributorService.loadIfrFirmInfo(id,langCode);
		model.put("ifafirm", vo);
		return "console/ifafirm/distributor/firmDetail";
	}
	
	/**
	 * ifafrim下产品列表
	 * */
	@RequestMapping(value = "/productPage", method = RequestMethod.GET)
	public String pPage(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String firmId = request.getParameter("firmId");
		model.put("firmId", firmId);
		return "console/ifafirm/distributor/productPage";
	}
	
	/**
	 *产品列表json
	 * */
	@RequestMapping(value = "/pListJson", method = RequestMethod.POST)
	public void productListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String firmId = request.getParameter("firmId");
		String langCode = this.getLoginLangFlag(request);
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = ifaFirmForDistributorService.loadFirmProductJson(firmId,langCode,jsonPaging);
		this.toJsonString(response, jsonPaging);
	}

}
