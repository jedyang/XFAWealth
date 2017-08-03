/**
 * 
 */
package com.fsll.wmes.web.action.console;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.entity.MemberMenu;
import com.fsll.wmes.web.service.ConsoleMenuService;
import com.fsll.wmes.web.service.FrontMenuService;

/**
 * @author qgfeng
 *	工作台菜单
 */
@Controller
@RequestMapping("/console/consoleMenu")
public class ConsoleMenuAct extends WmesBaseAct  {
	
	@Autowired
	private ConsoleMenuService consoleMenuService;
	
	@Autowired
	private FrontMenuService frontMenuService;
	
	/**
	 * 工作台菜单列表
	 * @author @author qgfeng
	 * @date 20161102
	 * */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		return "console/frontMenu/console_list";
	}
	
	/**
	 * 列表查询
	 * */
	@RequestMapping(value = "/listJson", method = RequestMethod.POST)
	public void listJson(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String keyword=request.getParameter("keyword");
		MemberMenu consoleMenu = new MemberMenu();
		if(null!=keyword&&!"".equals(keyword)){
			try {
				keyword=URLDecoder.decode(keyword, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		consoleMenu.setNameSc(keyword);
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = consoleMenuService.findAll(jsonPaging,consoleMenu);
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 新增菜单
	 * @author rqwang
	 * @date20170513
	 * 
	 * */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String langCode = this.getLoginLangFlag(request);
		MemberMenu menu = new MemberMenu();
		List<MemberMenu> list = frontMenuService.findAllMenu();

	    String parentId = request.getParameter("parentId");
	    MemberMenu parentMenu = frontMenuService.findMenuById(parentId);
	    if(null != parentMenu){
	    	menu.setParent(parentMenu);
	    }
	    model.addAttribute("langCode",langCode);
		model.addAttribute("menu", menu);
		model.addAttribute("menuList", list);
		return "console/frontMenu/console_input";
	}
	
	/**
	 * 菜单编辑
	 * @author rqwang
	 * @date 20170513
	 * */
	@RequestMapping(value = "/input", method = RequestMethod.GET)
	public String frontMenuEdit(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		
		List<MemberMenu> list = frontMenuService.findAllMenu();
	    String id=request.getParameter("id");
	    MemberMenu menu = frontMenuService.findMenuById(id);
	   
	    model.addAttribute("menu", menu);
		model.addAttribute("menuList", list);
		return "console/frontMenu/console_input";
	}

	//consoleList
}
