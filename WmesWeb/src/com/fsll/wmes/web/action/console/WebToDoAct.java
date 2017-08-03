///*
// * Copyright (c) 2016-2019 by fsll
// * All rights reserved.
// */
//package com.fsll.wmes.web.action.console;
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
//import com.fsll.wmes.base.WmesBaseAct;
//import com.fsll.wmes.web.service.WebToDoService;
//
//
///**
// * 控制器:待办信息管理
// * 
// * @author tan
// * @version 1.0.0 Created On: 2016-6-23
// */
//@Controller
//@RequestMapping("/console/todo")
//public class WebToDoAct extends WmesBaseAct {
//    
//    @Autowired
//    private WebToDoService webToDoService;
//
////    @Autowired
////    private SysParamService sysParamService;
////    
////    public FundInfoDataVO fundInfoVO;
////    public List<GeneralDataVO> fundFeesList;
////    public List<GeneralDataVO> fundDocsList;
////    public List<GeneralDataVO> fundBonusList;
////    
////    @Autowired
////	private WebFollowStatusService webFollowStatusService;
//    
//    /**
//     * 列表页面
//     */
//    @RequestMapping(value = "/list", method = RequestMethod.GET)
//    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
//        this.isMobileDevice(request, model);
//        return "console/todo/list";//页面摆放路径
//    }
//    /**
//     * 列表页面
//     */
//    @RequestMapping(value = "/mylist", method = RequestMethod.GET)
//    public String myList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
//        this.isMobileDevice(request, model);
//        return "console/todo/mylist";//页面摆放路径
//    }
//    
//    /**
//     * 分页获得记录
//     */
//    @RequestMapping(value = "/listJson", method = RequestMethod.POST)
//    public String listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
//        String ownerId = request.getParameter("ownerId");
//        String status = request.getParameter("status");
//
//        jsonPaging = this.getJsonPaging(request);
////        // jsonPaging.setSort("convert_mine(" + jsonPaging.getSort() + ",gbk)");
////        // 解决中英文混排问题，modified by michael @20160613
////        jsonPaging.setSort("ifnull(fristPinyin(" + jsonPaging.getSort() + "), pinyin(" + jsonPaging.getSort() + ")) ");
////        
////        // 解决orderby字段排序问题，需转换成数字类型，modified by michael @20160613
////        if (jsonPaging.getSort() != null && jsonPaging.getSort().contains("orderBy")) {
////            jsonPaging.setSort("(orderBy+0)");
////        }
//        /*WebToDo webToDo=new WebToDo();
//        webToDo.setStatus(status);
//        MemberBase memberBase=new MemberBase();
//        memberBase.setId(ownerId);
//        webToDo.setOwner(memberBase);
//        jsonPaging = webToDoService.findAll(jsonPaging,webToDo);
//        
//        List<WebToDoVO> list = new ArrayList<WebToDoVO>();
//		Iterator it = jsonPaging.getList().iterator();
//		while (it.hasNext()) {
//			WebToDo obj = (WebToDo) it.next();
//			WebToDoVO vo = new WebToDoVO();
//			BeanUtils.copyProperties(obj,vo);//拷贝信息
//			vo.setFromMemberId(obj.getFromMember().getId());
//			vo.setFromMemberName(obj.getFromMember().getNickName());
//			vo.setOwnerId(obj.getOwner().getId());
//			vo.setOwnerName(obj.getOwner().getNickName());
//			list.add(vo);
//		}
//		
//		jsonPaging.setList(list);*/
//        
//        this.toJsonString(response, jsonPaging);
//        return null;
//    }
//    
//    /**
//     * 详细信息页面
//     */
//    @RequestMapping(value = "/detail", method = RequestMethod.GET)
//    public String fundsdetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
//        String id = request.getParameter("id");
//        String isAll = request.getParameter("isAll");
//        model.put("isAll", isAll);
//        model.put("id", id);
//       /* WebToDo obj = webToDoService.findById(id);        
//        WebToDoVO vo = new WebToDoVO();
//		BeanUtils.copyProperties(obj,vo);//拷贝信息
//		vo.setFromMemberId(obj.getFromMember().getId());
//		vo.setFromMemberName(obj.getFromMember().getNickName());
//		vo.setOwnerId(obj.getOwner().getId());
//		vo.setOwnerName(obj.getOwner().getNickName());
//		model.put("todoVO", vo);
//        */
//        return "console/todo/input";
//    }
//    
////    /***
////	 * 我关注的基金列表
////	 * */
////	@RequestMapping(value = "/collectionList", method = RequestMethod.GET)
////	public String collectionList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
////		
////		jsonPaging = this.getJsonPaging(request);
////		MemberBase loginMemberBase = this.getLoginUser(request);//获取当前登录用户
////		String memberId = null;;
////		if(null==loginMemberBase){
////			memberId = "ALOQ_JPUwJgGqbZRsQD4qw5xyJKnqwpIOKRh";
////		}else{
////			memberId = loginMemberBase.getId();
////		}
////		String lang = this.getLoginLangFlag(request);
////		String moduleType = "fund";//基金类型
////		jsonPaging = webFollowStatusService.getWebFollowList(memberId, moduleType,lang, jsonPaging);
////		model.put("jsonPaging", jsonPaging);
////		return "fund/base/fundcollection"; 
////	}
//
//    /**
//     * 分页获得我的待办记录
//     * mqou 2016-08-01
//     */
//    @RequestMapping(value = "/myListJson", method = RequestMethod.POST)
//    public String myListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
//    	String sender= request.getParameter("sender");
//        String status = request.getParameter("status");
//        
//       /* WebToDo webToDo=new WebToDo();
//        MemberSsoVO curMember=(MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
//
//        webToDo.setStatus(status);
//        MemberBase memberBase=new MemberBase();
//        memberBase.setId(curMember.getId());
//        webToDo.setOwner(memberBase);
//        webToDo.setStatus(status);
//        MemberBase senderVo=new MemberBase();
//        senderVo.setNickName(sender);
//        webToDo.setFromMember(senderVo);
//        jsonPaging = this.getJsonPaging(request);
//
//        jsonPaging = webToDoService.findAll(jsonPaging, webToDo);
//        
//        List<WebToDoVO> list = new ArrayList<WebToDoVO>();
//		Iterator it = jsonPaging.getList().iterator();
//		while (it.hasNext()) {
//			WebToDo obj = (WebToDo) it.next();
//			WebToDoVO vo = new WebToDoVO();
//			BeanUtils.copyProperties(obj,vo);//拷贝信息
//			vo.setFromMemberId(obj.getFromMember().getId());
//			vo.setFromMemberName(obj.getFromMember().getNickName());
//			vo.setOwnerId(obj.getOwner().getId());
//			vo.setOwnerName(obj.getOwner().getNickName());
//			list.add(vo);
//		}
//		
//		jsonPaging.setList(list);
//        */
//        this.toJsonString(response, jsonPaging);
//        return null;
//    }
// 
//
//}
