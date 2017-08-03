
package com.fsll.wmes.ifafirm.action.console;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.core.CoreConstants;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.distributor.service.DistributorService;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.ifafirm.service.IfaFirmForDistributorService;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.ifafirm.vo.IfaFrimDistributorVo;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmVO;
import com.fsll.wmes.member.vo.MemberSsoVO;

/**
 * 投资顾问公司，产品管理
 * @author rqwang
 */
@Controller
@RequestMapping("/console/ifafirm/productManage")
public class IfaFirmProductManageAct extends CoreBaseAct{
	
	@Autowired
	private IfafirmManageService ifafirmManageService;
	
	@Autowired
	private DistributorService distributorService;
	
	/**
	 * IFAFirm的代理商列表
	 * @author rqwang
	 * @date 2017-06-09
	 * */
	@RequestMapping(value = "/disList")
	public String disList(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		//获取当前登录者信息
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.FRONT_USER_SSO);
		String ifafirmId = curMember.getIfafirmId();//获取登录的IFAFirm的id
		
		MemberIfafirm memberIfafirm = ifafirmManageService.findById(ifafirmId,getLoginLangFlag(request));
		if(null != memberIfafirm){
			model.put("companyName",memberIfafirm.getCompanyName());
		}
		model.put("ifafirmId",ifafirmId);
		
		return "console/ifafirm/distributor/firm_dis";
	}
	
	/**
	 * IFAFirm的代理商列表数据
	 * @author rqwang
	 * @date 2017-06-09
	 * */
	@RequestMapping(value = "/disListJson", method = RequestMethod.POST)
	public void findIfafirmDisListJson(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String ifafirmId = request.getParameter("ifafirmId");
		String companyName = request.getParameter("companyName");
		
		MemberDistributor disInfo = new MemberDistributor();
		if(null != companyName &&!"".equals(companyName)){
			try {
				companyName=URLDecoder.decode(companyName, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		disInfo.setCompanyName(companyName);
		
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = ifafirmManageService.findIfafirmDisList(jsonPaging, ifafirmId,disInfo,request);
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * IFAFirm的代理商详情
	 * @author rqwang
	 * @date 2017-06-12
	 */
	@RequestMapping(value = "/disDetail")
	public String disDetail(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		String id = request.getParameter("id");
		if(StringUtils.isNoneBlank(id)){
			MemberDistributor distributor = distributorService.findDistributorById(id);
			model.put("distributor", distributor);
		}
		return "console/ifafirm/distributor/firm_dis_detail";
	}
	
}
