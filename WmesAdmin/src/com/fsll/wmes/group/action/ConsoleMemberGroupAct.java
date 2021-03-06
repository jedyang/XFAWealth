package com.fsll.wmes.group.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.JsonUtil;
import com.fsll.core.entity.SysAdmin;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.entity.MemberGroup;
import com.fsll.wmes.group.service.MemberGroupService;

@Controller
@RequestMapping("/console/memberGroup")
public class ConsoleMemberGroupAct extends WmesBaseAct{

	@Autowired
	private MemberGroupService memberGroupService;
	
	/**
	 *  客户分组列表页面
	 * @author wwluo
	 * @date 2017-06-01
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		return "console/group/list";
	}
	
	/**
	 *  客户分组数据获取
	 * @author wwluo
	 * @date 2017-06-01
	 */
	@RequestMapping(value = "/listJson")
	public void listJson(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String langCode = getLoginLangFlag(request);
		jsonPaging = getJsonPaging(request);
		jsonPaging = memberGroupService.getGroups(jsonPaging, langCode, null);
		toJsonString(response, jsonPaging);
	}
	
	/**
	 *  客户分组添加编辑页面
	 * @author wwluo
	 * @date 2017-06-01
	 */
	@RequestMapping(value = "/input", method = RequestMethod.GET)
	public String input(String id, HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberGroup group = memberGroupService.findById(id);
		model.put("group", group);
		return "console/group/input";
	}
	
	/**
	 *  客户分组信息保存
	 * @author wwluo
	 * @date 2017-06-01
	 */
	@RequestMapping(value = "/save")
	public void save(MemberGroup group, HttpServletRequest request,HttpServletResponse response,ModelMap model){
		SysAdmin admin = getLoginUser(request);
		Map<String, Object> result = memberGroupService.saveGroup(group, admin);
		JsonUtil.toWriter(result, response);
	}

	/**
	 *  客户分组状态修改
	 * @author wwluo
	 * @date 2017-06-01
	 */
	@RequestMapping(value = "/update")
	public void update(String id, String isValid, HttpServletRequest request,HttpServletResponse response,ModelMap model){
		Map<String, Object> result = memberGroupService.updateGroup(id, isValid);
		JsonUtil.toWriter(result, response);
	}

}
