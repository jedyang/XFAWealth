package com.fsll.wmes.web.action.front;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.StrUtils;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.WebChat;
import com.fsll.wmes.im.ImTbUtil;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.web.service.WebChatService;
import com.fsll.wmes.web.vo.WebChatListVO;
import com.fsll.wmes.web.vo.WebChatMemberVO;
import com.taobao.api.domain.Userinfos;

@Controller
@RequestMapping("/front/web/webchat")
public class FrontWebChatAct extends WmesBaseAct{
	
	@Autowired
	private WebChatService webChatService;

	@Autowired
	private MemberBaseService memberBaseService;
	
	/**
	 *  登录聊天
	 * @author mqzou 2017-02-13 
	 * 
	 */
	@RequestMapping(value="/loginOnChat")
	public void loginOnChat(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberBase loginUser=getLoginUser(request);
		MemberSsoVO ssoVO=getLoginUserSSO(request);
		HashMap<String,Object> result=new HashMap<String, Object>();
		if(null!=loginUser){
			if(null==loginUser.getImUserId() || "".equals(loginUser.getImUserId())){
				loginUser=memberBaseService.saveOrUpdate(loginUser);
			}
			//检测IM服务端是否存在该帐号
			List<Userinfos> userinfos=ImTbUtil.getOpenIMUser(loginUser.getImUserId());
			if(null==userinfos || userinfos.isEmpty()){//如果不存在，则添加
				if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==loginUser.getSubMemberType()){
					MemberIfa ifa=memberBaseService.findIfaMember(loginUser.getId());
					ImTbUtil.addOpenImUser(loginUser.getImUserId()	, loginUser.getImUserPwd(), ifa.getGender(), loginUser.getImNickName(), "");
				}else if (CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_INDIVIDUAL==loginUser.getSubMemberType()) {
					MemberIndividual individual=memberBaseService.findIndividualMember(loginUser.getId());
					ImTbUtil.addOpenImUser(loginUser.getImUserId()	, loginUser.getImUserPwd(), individual.getGender(), loginUser.getImNickName(), "");
					
				}
				
			}else {//如果已存在帐号则更新一次，避免登录后马上被迫下线
				
				String pwd=UUID.randomUUID().toString();
				/*if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==loginUser.getSubMemberType()){
					MemberIfa ifa=memberBaseService.findIfaMember(loginUser.getId());
					ImTbUtil.updateOpenImUser(loginUser.getImUserId(), pwd, ifa.getGender(), loginUser.getImNickName(), loginUser.getImNickUrl());
				}else if (CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_INDIVIDUAL==loginUser.getSubMemberType()) {
					MemberIndividual individual=memberBaseService.findIndividualMember(loginUser.getId());
					ImTbUtil.updateOpenImUser(loginUser.getImUserId(), pwd, individual.getGender(), loginUser.getImNickName(), loginUser.getImNickUrl());
				}*/
				ssoVO.setImUserPwd(pwd);
				loginUser.setImUserPwd(pwd);
				loginUser=memberBaseService.saveOrUpdate(loginUser);
			}
			
			result.put("loginUser", ssoVO);
		}
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 获取联系人列表
	 * @author mqzou 
	 * 
	 */
	@RequestMapping(value = "/getContacts")
	public void getContacts(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberSsoVO loginUser=getLoginUserSSO(request);
		String langCode=getLoginLangFlag(request);
		if(null!=loginUser){
			List<WebChatListVO> list=new ArrayList<WebChatListVO>();
			//如果是ifa
			if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==loginUser.getSubMemberType()){
				List cusList=webChatService.findInvestorForIfa(loginUser.getIfaId());
				WebChatListVO vo=new WebChatListVO();
				vo.setType("1");
				vo.setMemberList(cusList);
				list.add(vo);
				List colList=webChatService.findColleagueForIfa(loginUser.getIfaId(),langCode);
				vo=new WebChatListVO();
				vo.setType("2");
				vo.setMemberList(colList);
				list.add(vo);
			}else if (CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_INDIVIDUAL==loginUser.getSubMemberType()) {//如果是投资人
				List ifaList=webChatService.findIfaForInvestor(loginUser.getId());
				WebChatListVO vo=new WebChatListVO();
				vo.setType("3");
				vo.setMemberList(ifaList);
				list.add(vo);
			}
			
			JsonUtil.toWriter(list, response);
		}
	}
	
	/**
	 * 发送消息
	 * @author mqzou 2017-02-13
	 */
	@RequestMapping(value = "/sendMessages")
	public void sendMsg(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberBase loginUser=getLoginUser(request);
		HashMap<String,Object> result=new HashMap<String, Object>();
		if(null!=loginUser){
			String content=request.getParameter("content");
			String toUserId=request.getParameter("toUserId");
			MemberBase toUser=memberBaseService.findByImUserId(toUserId);
			content=toUTF8String(content);
			WebChat chat=new WebChat();
			chat.setContent(content);
			chat.setCreateTime(DateUtil.getCurDate());
			chat.setIsRead("1");
			chat.setIsValid("1");
			chat.setReceive(toUser);
			chat.setSend(loginUser);
			webChatService.addWebChat(chat);
			result.put("result", true);
			
		}else {
			result.put("result", false);
		}
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 接收消息
	 * @author mqzou 2017-02-13
	 */
	@RequestMapping(value = "/receiveMessages")
	public void receiveMsg(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberBase loginUser=getLoginUser(request);
		HashMap<String,Object> result=new HashMap<String, Object>();
		if(null!=loginUser){
			String content=request.getParameter("content");
			String fromUserId=request.getParameter("fromUserId");
			MemberBase fromUser=memberBaseService.findByImUserId(fromUserId);
			content=toUTF8String(content);
			WebChat chat=new WebChat();
			chat.setContent(content);
			chat.setCreateTime(DateUtil.getCurDate());
			chat.setIsRead("1");
			chat.setIsValid("1");
			chat.setReceive(loginUser);
			chat.setSend(fromUser);
			webChatService.addWebChat(chat);
			result.put("result", true);
			
		}else {
			result.put("result", false);
		}
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 最近联系人
	 * @author mqzou 2017-02-16
	 * 
	 */
	@RequestMapping(value = "/getRecently")
	public void getRecentList(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberSsoVO loginUser=getLoginUserSSO(request);
		String langCode=getLoginLangFlag(request);
		if(null!=loginUser){
			String ids=StrUtils.getString(request.getParameter("ids"));
			List<WebChatMemberVO> resultList=new ArrayList<WebChatMemberVO>();
			List<WebChatMemberVO> list=new ArrayList<WebChatMemberVO>();
			//如果是ifa
			if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==loginUser.getSubMemberType()){
				List<WebChatMemberVO> cusList=webChatService.findInvestorForIfa(loginUser.getIfaId());
				List<WebChatMemberVO> colList=webChatService.findColleagueForIfa(loginUser.getIfaId(),langCode);
				list.addAll(cusList);
				list.addAll(colList);
				
			}else if (CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_INDIVIDUAL==loginUser.getSubMemberType()) {//如果是投资人
				List<WebChatMemberVO> ifaList=webChatService.findIfaForInvestor(loginUser.getId());
				list.addAll(ifaList);
			}
			List<String> idList=new ArrayList<String>();
			for (WebChatMemberVO vo : list) {
				if(null==vo.getUserId())
					continue;
				if(ids.contains(vo.getUserId()) && !idList.contains(vo.getUserId())){
					idList.add(vo.getUserId());
					resultList.add(vo);
				}
			}
			JsonUtil.toWriter(resultList, response);
		}
	}
	
	/**
	 * 初始化用户的聊天信息
	 * @author mqzou 2017-02-27
	 * 
	 */
	@RequestMapping(value = "/initUserChatInfo")
	public void initUserChatInfo(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		HashMap<String,Object> result=new HashMap<String, Object>();
		try {
			List<MemberBase> memberList=webChatService.findAllMember();
			if(null!=memberList && !memberList.isEmpty()){
				Iterator it=memberList.iterator();
				while (it.hasNext()) {
					MemberBase member = (MemberBase) it.next();
					member=memberBaseService.saveOrUpdate(member);
					//检测IM服务端是否存在该帐号
					List<Userinfos> userinfos=ImTbUtil.getOpenIMUser(member.getImUserId());
					if(null==userinfos || userinfos.isEmpty()){//如果不存在，则添加
						if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_IFA==member.getSubMemberType()){
							MemberIfa ifa=memberBaseService.findIfaMember(member.getId());
							ImTbUtil.addOpenImUser(member.getImUserId()	, member.getImUserPwd(), ifa.getGender(), member.getImNickName(), "");
						}else if (CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_INDIVIDUAL==member.getSubMemberType()) {
							MemberIndividual individual=memberBaseService.findIndividualMember(member.getId());
							ImTbUtil.addOpenImUser(member.getImUserId()	, member.getImUserPwd(), individual.getGender(), member.getImNickName(), "");
							
						}
						
					}
				}
			}
			result.put("result", "1");
		} catch (Exception e) {
			result.put("result", "0");
		}
		
		JsonUtil.toWriter(result, response);
		
	}
}
