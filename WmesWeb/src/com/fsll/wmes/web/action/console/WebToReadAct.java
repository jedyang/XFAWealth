///*
// * Copyright (c) 2016-2019 by fsll
// * All rights reserved.
// */
//package com.fsll.wmes.web.action.console;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import com.fsll.common.util.JsonUtil;
//import com.fsll.core.CoreConstants;
//import com.fsll.wmes.base.WmesBaseAct;
//import com.fsll.wmes.member.vo.MemberSsoVO;
//import com.fsll.wmes.web.service.WebToReadService;
//
///**
// * 控制器:待办信息管理
// * 
// * @author tan
// * @version 1.0.0 Created On: 2016-6-23
// */
//@Controller
//@RequestMapping("/console/toread")
//public class WebToReadAct extends WmesBaseAct {
//
//	@Autowired
//	private WebToReadService webToReadService;
//
//	/**
//	 * 列表页面
//	 */
//	@RequestMapping(value = "/mylist", method = RequestMethod.GET)
//	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
//		this.isMobileDevice(request, model);
//		return "console/toread/mylist";// 页面摆放路径
//	}
//
//	/**
//	 * 分页获得记录
//	 */
//	@RequestMapping(value = "/listJson", method = RequestMethod.POST)
//	public String listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
//		String ownerId = request.getParameter("ownerId");
//		String status = request.getParameter("status");
//
//		jsonPaging = this.getJsonPaging(request);
//		
//		/*WebToRead webToRead=new WebToRead();
//		webToRead.setStatus(status);
//		MemberBase ownerVo=new MemberBase();
//		ownerVo.setId(ownerId);
//		webToRead.setOwner(ownerVo);
//
//		jsonPaging = webToReadService.findAll(jsonPaging,webToRead);
//
//		List<WebToReadVO> list = new ArrayList<WebToReadVO>();
//		Iterator it = jsonPaging.getList().iterator();
//		while (it.hasNext()) {
//			WebToRead obj = (WebToRead) it.next();
//			WebToReadVO vo = new WebToReadVO();
//			BeanUtils.copyProperties(obj, vo);// 拷贝信息
//			vo.setFromMemberId(obj.getFromMember().getId());
//			vo.setFromMemberName(obj.getFromMember().getNickName());
//			vo.setOwnerId(obj.getOwner().getId());
//			vo.setOwnerName(obj.getOwner().getNickName());
//			list.add(vo);
//		}
//
//		jsonPaging.setList(list);*/
//
//		this.toJsonString(response, jsonPaging);
//		return null;
//	}
//
//	/**
//	 * 分页获得我的待阅记录
//	 * mqzou 2016-08-01
//	 */
//	@RequestMapping(value = "/myListJson", method = RequestMethod.POST)
//	public String myListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
//
//		String sender= request.getParameter("sender");
//		String status = request.getParameter("status");
//
//		jsonPaging = this.getJsonPaging(request);
//		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
//
//		/*WebToRead webToRead=new WebToRead();
//		webToRead.setStatus(status);
//		MemberBase ownerVo=new MemberBase();
//		ownerVo.setId(curMember.getId());
//		webToRead.setOwner(ownerVo);
//		
//		MemberBase senderVo=new MemberBase();
//		senderVo.setNickName(sender);
//		webToRead.setFromMember(senderVo);
//		jsonPaging = webToReadService.findAll(jsonPaging,webToRead);
//
//		List<WebToReadVO> list = new ArrayList<WebToReadVO>();
//		Iterator it = jsonPaging.getList().iterator();
//		while (it.hasNext()) {
//			WebToRead obj = (WebToRead) it.next();
//			WebToReadVO vo = new WebToReadVO();
//			BeanUtils.copyProperties(obj, vo);// 拷贝信息
//			vo.setFromMemberId(obj.getFromMember().getId());
//			vo.setFromMemberName(obj.getFromMember().getNickName());
//			vo.setOwnerId(obj.getOwner().getId());
//			vo.setOwnerName(obj.getOwner().getNickName());
//			list.add(vo);
//		}
//
//		jsonPaging.setList(list);*/
//
//		this.toJsonString(response, jsonPaging);
//		return null;
//	}
//
//	/**
//	 * 详细信息页面
//	 */
//	@RequestMapping(value = "/detail", method = RequestMethod.GET)
//	public String fundsdetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
//		String id = request.getParameter("id");
//		String isAll = request.getParameter("isAll");
//		model.put("id", id);
//		model.put("isAll", isAll);//是1，否0
//		/*WebToRead obj = webToReadService.findById(id);
//		WebToReadVO vo = new WebToReadVO();
//		BeanUtils.copyProperties(obj, vo);// 拷贝信息
//		vo.setFromMemberId(obj.getFromMember().getId());
//		vo.setFromMemberName(obj.getFromMember().getNickName());
//		vo.setOwnerId(obj.getOwner().getId());
//		vo.setOwnerName(obj.getOwner().getNickName());
//		model.put("toreadVO", vo);*/
//		return "console/toread/input";
//	}
//	
//	
//	/**
//	 * 修改待阅状态
//	 * @author wwluo
//	 * @date 2016-08-25
//	 */
//	@RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
//	public void updateStatus(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
//		String toreadId = request.getParameter("toreadId");
//		boolean flag = false;
//		flag = webToReadService.updateStatusById(toreadId);
//		Map<String, Object> obj = new HashMap<String, Object>();
//		obj.put("flag",flag);
//		JsonUtil.toWriter(obj, response);
//	}
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//
//}
