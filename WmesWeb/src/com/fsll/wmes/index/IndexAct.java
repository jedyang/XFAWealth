package com.fsll.wmes.index;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fsll.core.CoreConstants;
import com.fsll.wmes.base.WmesBaseAct;

/****
 * WEB首页
 * @author mjhuang
 * @version 1.0
 * @date 2016-06-21
 */
@Controller
public class IndexAct extends WmesBaseAct{
	/**
	 * 首页列表
	 */
	@RequestMapping(value = "index")
	public String index(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		//如果已登录，则按角色跳转到对应角色的首页
		String login = (String) request.getSession().getAttribute(CoreConstants.FRONT_USER_NAME);
		String role = (String) request.getSession().getAttribute(CoreConstants.FRONT_USER_ROLE);
		if (null!=login && !"".equals(login) && null!=role && !"".equals(role)){
			request.setAttribute("role", role);
			return getRoleUrl(request,role);
		}
		return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
	}
	
	/**
	 * ifa/investor角色切换测试
	 * @author scshi
	 * */
	@RequestMapping(value = "roleSelect")
	public String roleChage(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		//如果已登录，则按角色跳转到对应角色的首页
		String login = (String) request.getSession().getAttribute(CoreConstants.FRONT_USER_NAME);
		String role = (String) request.getSession().getAttribute(CoreConstants.FRONT_USER_ROLE);
		if (null!=login && !"".equals(login) && null!=role && !"".equals(role)){
			request.setAttribute("role", role);
			return getRoleUrl(request, role);
		}
		
//		//未登录，从url获取role，一般用于demo
//		role = request.getParameter("role");
//		model.put("role", role);
//		request.getSession().setAttribute("role", role);
//		if("IFA".equalsIgnoreCase(role)){
//			return "ifa/ifaHome";
//		}
		
		//未登录，作为游客，即investor
		request.setAttribute("role", CoreConstants.FRONT_USER_ROLE_GUEST);

		return "redirect:"+this.getFullPath(request)+"front/investor/home.do";
	}
	
	private String getRoleUrl(HttpServletRequest request,String role){
		if (null!=role && !"".equals(role)){
			if(CoreConstants.FRONT_USER_ROLE_IFA.equalsIgnoreCase(role)){
				return "redirect:"+this.getFullPath(request)+"front/ifa/info/ifahome.do";
			}
			else if(CoreConstants.FRONT_USER_ROLE_INVESTOR.equalsIgnoreCase(role)){
				return "redirect:"+this.getFullPath(request)+"front/investor/home.do";
			}
			else if(CoreConstants.FRONT_USER_ROLE_IFA_FIRM.equalsIgnoreCase(role)){
				return "redirect:"+this.getFullPath(request)+"console/ifafirm/info/home.do";
			}
			else if(CoreConstants.FRONT_USER_ROLE_DISTRIBUTOR.equalsIgnoreCase(role)){
				return "redirect:"+this.getFullPath(request)+"console/distributor/info/home.do";
			}
			else if(CoreConstants.FRONT_USER_SYS_ADMIN.equalsIgnoreCase(role)){
				return "redirect:"+this.getFullPath(request)+"console/sys/mutilLang/langPage.do";
			}
			else if(CoreConstants.FRONT_USER_ROLE_GUEST.equalsIgnoreCase(role)){
				return "redirect:"+this.getFullPath(request)+"front/investor/home.do";
			}
		}
		return "redirect:"+this.getFullPath(request)+"front/investor/home.do";
	}
}
