/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.web.action.console;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.core.CoreConstants;
import com.fsll.core.entity.SysAdmin;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.WebReadToDo;
import com.fsll.wmes.entity.WebReadToDoEn;
import com.fsll.wmes.entity.WebReadToDoSc;
import com.fsll.wmes.entity.WebReadToDoTc;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.web.service.WebReadService;
import com.fsll.wmes.web.vo.WebToDoVO;

/**
 * 控制器:待办信息管理
 * 
 * @author tan
 * @version 1.0.0 Created On: 2016-6-23
 */
@Controller
@RequestMapping("/console/webread")
public class WebToReadAct extends WmesBaseAct {

	@Autowired
	private WebReadService webReadService;
	@Autowired
	private MemberBaseService memberBaseService;

	/**
	 * 列表页面
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String todoList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		this.isMobileDevice(request, model);
		return "console/todo/list";// 页面摆放路径
	}
	
	/**
	 * 列表页面
	 
	@RequestMapping(value = "/toreadlist", method = RequestMethod.GET)
	public String toreadList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		this.isMobileDevice(request, model);
		return "console/toread/list";// 页面摆放路径
	}*/

	/**
	 * 分页获得记录
	 * Yan 2016-11-24
	 */
	@RequestMapping(value = "/listJson", method = RequestMethod.POST)
	public String listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode = this.getLoginLangFlag(request);
//		String myType = request.getParameter("myType");
		String ownerName = request.getParameter("ownerName");
		String isRead = request.getParameter("isRead");
		String type = request.getParameter("type");
		String title = request.getParameter("title");
		String moduleType = request.getParameter("moduleType");

		//待需求明确后筛选我的列表
//		if("1".equals(myType)){
//			SysAdmin admin = (SysAdmin)request.getSession().getAttribute(CoreConstants.USER_LOGIN);
//			ownerName = admin.getNickName();
//		}
		
		jsonPaging = this.getJsonPaging(request);
		
		WebReadToDo todo =new WebReadToDo();
		todo.setIsRead(isRead);
		todo.setType(type);
		todo.setTitle(title);
		todo.setModuleType(moduleType);
		todo.setIsRead(isRead);
		jsonPaging = webReadService.findAll(jsonPaging,todo,ownerName,langCode);
		List jsonList = jsonPaging.getList();
		List<WebToDoVO> list = new ArrayList<WebToDoVO>();
//		Iterator it = jsonPaging.getList().iterator();
//		while (it.hasNext()) {
//			WebReadToDo obj = (WebReadToDo) it.next();
		for (int i = 0; i < jsonList.size(); i++) {
			Object[] objRead = (Object[])jsonList.get(i);
			WebReadToDo obj = (WebReadToDo)objRead[0];
			String objTitle = "";
			if("sc".equals(langCode)){
				WebReadToDoSc objSc = (WebReadToDoSc)objRead[1];
				objTitle = objSc.getTitle();
			}else if("en".equals(langCode)){
				WebReadToDoEn objEn = (WebReadToDoEn)objRead[1];
				objTitle = objEn.getTitle();
			}else if("tc".equals(langCode)){
				WebReadToDoTc objTc = (WebReadToDoTc)objRead[1];
				objTitle = objTc.getTitle();
			}
			WebToDoVO vo = new WebToDoVO();
			BeanUtils.copyProperties(obj, vo);// 拷贝信息
			vo.setFromMemberId(obj.getFromMember().getId());
			vo.setFromMemberName(obj.getFromMember().getNickName());
			vo.setOwnerId(obj.getOwner().getId());
			vo.setOwnerName(obj.getOwner().getNickName());
			vo.setType(obj.getType());
			vo.setModuleType(obj.getModuleType());
			vo.setRelateId(obj.getRelateId());
			vo.setIsRead(obj.getIsRead());
			vo.setReadTime(obj.getReadTime());
			vo.setCreateTime(obj.getCreateTime());
			vo.setIsValid(obj.getIsValid());
			vo.setTitle(objTitle);
			list.add(vo);
		}

		jsonPaging.setList(list);

		this.toJsonString(response, jsonPaging);
		return null;
	}

	/**
	 * 分页获得我的待阅记录
	 * mqzou 2016-08-01
	 */
	@RequestMapping(value = "/myListJson", method = RequestMethod.POST)
	public String myListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

//		String sender= request.getParameter("sender");
//		String status = request.getParameter("status");
//
//		jsonPaging = this.getJsonPaging(request);
//		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);

		/*WebToRead webToRead=new WebToRead();
		webToRead.setStatus(status);
		MemberBase ownerVo=new MemberBase();
		ownerVo.setId(curMember.getId());
		webToRead.setOwner(ownerVo);
		
		MemberBase senderVo=new MemberBase();
		senderVo.setNickName(sender);
		webToRead.setFromMember(senderVo);
		jsonPaging = webToReadService.findAll(jsonPaging,webToRead);

		List<WebToReadVO> list = new ArrayList<WebToReadVO>();
		Iterator it = jsonPaging.getList().iterator();
		while (it.hasNext()) {
			WebToRead obj = (WebToRead) it.next();
			WebToReadVO vo = new WebToReadVO();
			BeanUtils.copyProperties(obj, vo);// 拷贝信息
			vo.setFromMemberId(obj.getFromMember().getId());
			vo.setFromMemberName(obj.getFromMember().getNickName());
			vo.setOwnerId(obj.getOwner().getId());
			vo.setOwnerName(obj.getOwner().getNickName());
			list.add(vo);
		}

		jsonPaging.setList(list);*/

		this.toJsonString(response, jsonPaging);
		return null;
	}

	/**
	 * 详细信息页面
	 * Yan 2016-11-24
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public String fundsdetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode = this.getLoginLangFlag(request);
		String id = request.getParameter("id");
		String isAll = request.getParameter("isAll");
		model.put("id", id);
		model.put("isAll", isAll);//是1，否0
		WebReadToDo obj = webReadService.findById(id);
		if(!"".equals(obj) && obj!=null){
			obj.setIsRead("1");
			webReadService.saveOrUpdate(obj);
		}
		
		String objTitle = "";
		List<Object> info = webReadService.findTitleById(id, langCode);
		if("sc".equals(langCode)){
			WebReadToDoSc objSc = (WebReadToDoSc)info.get(0);
			objTitle = objSc.getTitle();
		}else if("en".equals(langCode)){
			WebReadToDoEn objEn = (WebReadToDoEn)info.get(0);
			objTitle = objEn.getTitle();
		}else if("tc".equals(langCode)){
			WebReadToDoTc objTc = (WebReadToDoTc)info.get(0);
			objTitle = objTc.getTitle();
		}
		WebToDoVO vo = new WebToDoVO();
		BeanUtils.copyProperties(obj, vo);// 拷贝信息
		vo.setFromMemberId(obj.getFromMember().getId());
		vo.setFromMemberName(obj.getFromMember().getNickName());
		vo.setOwnerId(obj.getOwner().getId());
		vo.setOwnerName(obj.getOwner().getNickName());
		vo.setType(obj.getType());
		vo.setModuleType(obj.getModuleType());
		vo.setRelateId(obj.getRelateId());
		vo.setIsRead(obj.getIsRead());
		vo.setReadTime(obj.getReadTime());
		vo.setCreateTime(obj.getCreateTime());
		vo.setIsValid(obj.getIsValid());
		vo.setTitle(objTitle);
		model.put("todoVO", vo);
		return "console/todo/input";
	}
	
	
	/**
	 * 修改待阅状态
	 * @author wwluo
	 * @date 2016-08-25
	 */
	@RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
	public void updateStatus(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
//		String toreadId = request.getParameter("toreadId");
		boolean flag = false;
		//flag = webReadService.updateStatusById(toreadId);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("flag",flag);
		JsonUtil.toWriter(obj, response);
	}
}
