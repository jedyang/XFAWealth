package com.fsll.wmes.web.action.console;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.JsonUtil;
import com.fsll.core.CoreConstants;
import com.fsll.core.entity.SysAdmin;
import com.fsll.core.entity.SysMenu;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.web.service.MenuService;

/**
 * 控制器:后台菜单管理
 * 
 * @author 
 * @version 1.0
 * @date 2016-07-22
 */
@Controller
public class MenuAct extends WmesBaseAct {

	@Autowired
	private MenuService menuService;

	/**
	 * 后台菜单列表
	 * @date 2016-07-22
	 */
	@RequestMapping(value = "/menu/list", method = RequestMethod.GET)
	public void list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String code = request.getParameter("code");
		request.getSession().setAttribute("_MENU_CODE_", code);
		// modify by mqzou 2016-08-24 获取权限菜单
		//MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.USER_LOGIN);
		// List<SysMenu> menuList = menuService.findMenu();
		SysAdmin admin = (SysAdmin)request.getSession().getAttribute(CoreConstants.USER_LOGIN);
		List<SysMenu> menuList = menuService.findMenuForPower(admin.getId());
		
		//菜单排序
		try{
			Collections.sort(menuList, new Comparator<SysMenu>() {
				public int compare(SysMenu arg0, SysMenu arg1) {
					int hits0 = arg0.getOrderBy();
					int hits1 = arg1.getOrderBy();
					if (hits1 < hits0) {
						return 1;
					} else if (hits1 == hits0) {
						return 0;
					} else {
						return -1;
					}
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
		menuList = CompleteMenu(menuList);
		
		//生成菜单的json数据
		StringBuffer menuBuffer = new StringBuffer();
		//String menuJson = "";
		for (SysMenu sysMenu : menuList) {
//			menuJson = JsonUtil.toJson(sysMenu);
//			menuBuffer.append(menuJson);
//			menuBuffer.insert(menuBuffer.length() - 1, ",\"childMenu\":");
//			if (sysMenu.getChildSet().size() != 0) {
//				Set<SysMenu> sysMenuSet = sysMenu.getChildSet();
//				menuJson = JsonUtil.toJson(sysMenuSet);
//				menuBuffer.insert(menuBuffer.length() - 1, menuJson);
//			} else {
//				menuBuffer.insert(menuBuffer.length() - 1, "null");
//			}
			menuBuffer.append(getChildMenuJsonString(sysMenu));
			menuBuffer.append(",");
		}
		menuBuffer.insert(0, "[");
		menuBuffer.replace(menuBuffer.length() - 1, menuBuffer.length(), "]");
		
		//返回结果
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result", true);
		obj.put("menuJson", menuBuffer.toString());
		JsonUtil.toWriter(obj, response);
	}

	/**
	 * 递归获取子菜单的Json
	 * @param sysMenu
	 * @return
	 */
	private String getChildMenuJsonString(SysMenu sysMenu){
		StringBuffer menuBuffer = new StringBuffer();
		String menuJson = JsonUtil.toJson(sysMenu);
		menuBuffer.append(menuJson);
		menuBuffer.insert(menuBuffer.length() - 1, ",\"childMenu\":");
		if (null!=sysMenu.getChildSet() && sysMenu.getChildSet().size() != 0) {
			Iterator<SysMenu> it = sysMenu.getChildSet().iterator();
			menuBuffer.insert(menuBuffer.length() - 1, "[");
			while (it.hasNext()) {
				SysMenu child = (SysMenu) it.next();
				menuJson = getChildMenuJsonString(child);
				menuBuffer.insert(menuBuffer.length() - 1, menuJson);
				if (it.hasNext()) menuBuffer.insert(menuBuffer.length()- 1 , ",");
			}
			menuBuffer.insert(menuBuffer.length()- 1 , "]");
		} else {
			menuBuffer.insert(menuBuffer.length() - 1, "null");
		}
		
		return menuBuffer.toString();
	}
	
	/**
	 * 整理菜单（父子关系）
	 * 
	 * @author mqzou
	 * @date 2016-08-23
	 */
	private List<SysMenu> CompleteMenu(List<SysMenu> list) {
		List<SysMenu> resultList = new ArrayList<SysMenu>();
		Iterator<SysMenu> it = list.iterator();
		while (it.hasNext()) {
			SysMenu sysMenu = (SysMenu) it.next();
			if (null == sysMenu.getParent()) {
				resultList.add(sysMenu);
				it.remove();
			}
		}
		for (SysMenu sysMenu : resultList) {
			sysMenu.setChildSet(getChildMenu(sysMenu, list));
		}
		return resultList;
	}

	/**
	 * 递归获取菜单子节点
	 * 
	 * @author mqzou
	 * @date 2016-08-23
	 */
	private Set<SysMenu> getChildMenu(SysMenu menu, List<SysMenu> list) {
		Set<SysMenu> set = menu.getChildSet();
		if (null == set || set.isEmpty())
			return set;
		Iterator<SysMenu> it = set.iterator();
		while (it.hasNext()) {
			SysMenu sysMenu = (SysMenu) it.next();
			if (!list.isEmpty())
				sysMenu.setChildSet(getChildMenu(sysMenu, list));
			if (list.contains(sysMenu)) {
				list.remove(sysMenu);
			} else {
				it.remove();
			}
		}

		/*
		 * Iterator<SysMenu> it=list.iterator(); while (it.hasNext()) { SysMenu
		 * sysMenu = (SysMenu) it.next(); if(sysMenu.getParent()==menu){
		 * it.remove(); if(list.size()>0)
		 * sysMenu.setChildSet(getChildMenu(sysMenu, list));
		 * 
		 * set.add(sysMenu); } }
		 */

		return set;

	}
	
	/**
	 * 更新菜單編碼，用於刷新后定位
	 * @author wwluo
	 * @date 2016-08-29
	 */
	@RequestMapping(value = "/menu/updateMenuCode", method = RequestMethod.POST)
	public void updateMenuCode(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String menuCode = request.getParameter("menuCode");
		request.getSession().setAttribute("_MENU_CODE_", menuCode);
	}
}
