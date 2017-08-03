package com.fsll.wmes.discover.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.discover.service.DiscoverService;
import com.fsll.wmes.discover.vo.WebDiscussReplyVO;
import com.fsll.wmes.discover.vo.WebDiscussVO;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.WebDiscuss;
import com.fsll.wmes.entity.WebDiscussReply;

@Controller
@RequestMapping("/front/discover")
public class FrontDiscoverAct extends WmesBaseAct{

	@Autowired
	private DiscoverService discoverService;
	

	/**
	 * 评论页面
	 * @author wwluo
	 * @date 2016-11-07 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/memberComment")
	public String memberComment(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		jsonPaging = this.getJsonPaging(request);
		jsonPaging.setRows(10);
		jsonPaging = getWebDiscuss(request);
		model.put("webDiscusss", jsonPaging);
		//评论对象 Id
		String relateId = request.getParameter("relateId");
		//对应模块
		String moduleType = request.getParameter("moduleType");
		//moduleType = "ifa"; //TODO test 待删除
		//relateId = "1"; //TODO test 待删除
		model.put("moduleType", moduleType);
		model.put("relateId", relateId);
		return "ifa/discover/memberComment";
	}
	
	/**
     * 读取“评论”数据
     * @author wwluo
     * @date 2016-11-07 
     * @param request
     * @return
     */
	public JsonPaging getWebDiscuss(HttpServletRequest request) {
		jsonPaging = this.getJsonPaging(request);
		//评论对象 Id
		String relateId = request.getParameter("relateId");
		//对应模块
		String moduleType = request.getParameter("moduleType");
		//moduleType = "ifa"; //TODO test 待删除
		//relateId = "1"; //TODO test 待删除
		if (StringUtils.isNotBlank(relateId)) {
			//H：Hot Topic ； L：Latest Topic
			String topicType = request.getParameter("topicType");
			jsonPaging = discoverService.getWebDiscuss(jsonPaging,relateId,moduleType,topicType);
		}
		return jsonPaging;
    }
	
	/**
     * 获取“评论”数据，并返回
     * @author wwluo
     * @date 2016-11-07 
     * @param request
     * @param response
     * @param model
     * @return
     */
	@RequestMapping(value = "/getWebDiscuss")
    public void getWebDiscuss(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Map<String, Object> result = new HashMap<String, Object>();
		jsonPaging = getWebDiscuss(request);
		result.put("webDiscusss", jsonPaging);
		JsonUtil.toWriter(result, response);
    }

	/**
	 * 发表“评论”信息
	 * @author wwluo
	 * @date 2016-11-07 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/saveWebDiscuss")
	public void saveWebDiscuss(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();
		boolean flag = false;
		WebDiscussVO webDiscussVO = new WebDiscussVO();
		if(loginUser != null){
			//评论对象 Id
			String relateId = request.getParameter("relateId");
			//对应模块
			String moduleType = request.getParameter("moduleType");
			String content = request.getParameter("content");
			WebDiscuss webDiscuss = new WebDiscuss();
			webDiscuss.setContent(content);
			webDiscuss.setMember(loginUser);
			webDiscuss.setModuleType(moduleType);
			webDiscuss.setRelateId(relateId);
			webDiscuss.setStatus("0"); //TODO
			webDiscuss.setReplyCount(0);
			webDiscuss.setUps(0);
			webDiscuss.setCreateTime(new Date());
			
			webDiscuss = discoverService.saveWebDiscuss(webDiscuss);
			BeanUtils.copyProperties(webDiscuss, webDiscussVO);
			
			Long day = (long) (24*60*60*1000);
			Long hour = (long) (60*60*1000);
			Date replyTime = webDiscuss.getCreateTime();
			Calendar calendar = Calendar.getInstance();
			//时间差
			Long timeDiff = calendar.getTimeInMillis()-replyTime.getTime();
			if(timeDiff > day){
				Long temp = timeDiff/day;
				Integer timeNum = Integer.parseInt(temp.toString());
				webDiscussVO.setTimeNum(timeNum);
				webDiscussVO.setTimeType("D");
			}else{
				Long temp = timeDiff/hour;
				Integer timeNum = Integer.parseInt(temp.toString());
				webDiscussVO.setTimeNum(timeNum);
				webDiscussVO.setTimeType("H");
			}
			
			if(webDiscuss != null && webDiscuss.getMember() != null){
				webDiscussVO.setIconUrl(webDiscuss.getMember().getIconUrl());
				webDiscussVO.setNickName(webDiscuss.getMember().getNickName());
			}
			flag = true;
		}
		result.put("webDiscuss", webDiscussVO);
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
	/**
     * 读取“回复”信息
     * @author wwluo
     * @date 2016-11-07 
     * @param request
     * @param response
     * @param model
     * @return
     */
	@RequestMapping(value = "/getWebDiscussReply")
    public void getWebDiscussReply(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//评论Id
		String discussId = request.getParameter("discussId");
		Map<String, Object> result = new HashMap<String, Object>();
		boolean flag = false;
		List<WebDiscussReplyVO> webDiscussReplys = new ArrayList<WebDiscussReplyVO>();
		if (StringUtils.isNotBlank(discussId)) {
			webDiscussReplys = discoverService.getWebDiscussReply(discussId);
			flag = true;
		}
		result.put("flag", flag);
		result.put("webDiscussReplys", webDiscussReplys);
		JsonUtil.toWriter(result, response);
    }
	
	/**
	 * 发表“回复”信息
	 * @author wwluo
	 * @date 2016-11-07 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/saveWebDiscussReply")
	public void saveWebDiscussReply(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();
		boolean flag = false;
		WebDiscussReplyVO discussReplyVO = new WebDiscussReplyVO();
		if(loginUser != null){
			//评论Id
			String discussId = request.getParameter("discussId");
			if (StringUtils.isNotBlank(discussId)) {
				WebDiscuss webDiscuss = discoverService.getWebDiscussById(discussId);
				//回复id
				String discussReplyId = request.getParameter("discussReplyId");
				String content = request.getParameter("content");
				WebDiscussReply discussReply = new WebDiscussReply();
				discussReply.setMember(loginUser);
				discussReply.setReplyContent(content);
				discussReply.setReplyCount(0);
				discussReply.setReplyTime(new Date());
				discussReply.setStatus("0");//TODO
				discussReply.setUps(0);
				discussReply.setDiscuss(webDiscuss);
				if (StringUtils.isNotBlank(discussReplyId)) {
					WebDiscussReply webDiscussReply = discoverService.getWebDiscussReplyById(discussReplyId);
					if(webDiscussReply != null){
						Integer replyCount = webDiscussReply.getReplyCount();
						replyCount ++;
						webDiscussReply.setReplyCount(replyCount);
						webDiscussReply = discoverService.saveWebDiscussReply(webDiscussReply);
					}
					discussReply.setParent(webDiscussReply);
				}else{
					Integer replyCount = webDiscuss.getReplyCount();
					replyCount ++;
					webDiscuss.setReplyCount(replyCount);
					webDiscuss = discoverService.saveWebDiscuss(webDiscuss);
				}
				discussReply = discoverService.saveWebDiscussReply(discussReply);
				if(discussReply != null){
					BeanUtils.copyProperties(discussReply, discussReplyVO);
					
					Long day = (long) (24*60*60*1000);
					Long hour = (long) (60*60*1000);
					Date replyTime = discussReply.getReplyTime();
					Calendar calendar = Calendar.getInstance();
					//时间差
					Long timeDiff = calendar.getTimeInMillis()-replyTime.getTime();
					if(timeDiff > day){
						Long temp = timeDiff/day;
						Integer timeNum = Integer.parseInt(temp.toString());
						discussReplyVO.setTimeNum(timeNum);
						discussReplyVO.setTimeType("D");
					}else{
						Long temp = timeDiff/hour;
						Integer timeNum = Integer.parseInt(temp.toString());
						discussReplyVO.setTimeNum(timeNum);
						discussReplyVO.setTimeType("H");
					}
					
					if(discussReply.getMember() != null){
						discussReplyVO.setIconUrl(webDiscuss.getMember().getIconUrl());
						discussReplyVO.setNickName(webDiscuss.getMember().getNickName());
					}
					if(discussReply.getParent() != null && discussReply.getParent().getMember() != null){
						discussReplyVO.setBeRepliedTo(discussReply.getParent().getMember().getNickName());
					}else if(discussReply.getParent() == null && webDiscuss.getMember() != null){
						discussReplyVO.setBeRepliedTo(webDiscuss.getMember().getNickName());
					}
				}
				flag = true;
			}
		}
		result.put("webDiscussReply", discussReplyVO);
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 评论“顶”操作
	 * @author wwluo
	 * @date 2016-11-07 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/webDiscussUps")
	public void webDiscussUps(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();
		boolean flag = false;
		if(loginUser != null){
			//评论Id
			String discussId = request.getParameter("discussId");
			//回复id
			String discussReplyId = request.getParameter("discussReplyId");
			if (StringUtils.isNotBlank(discussId)) {
				WebDiscuss webDiscuss = discoverService.getWebDiscussById(discussId);
				if(webDiscuss != null){
					Integer ups = webDiscuss.getUps();
					ups ++;
					webDiscuss.setUps(ups);
					webDiscuss = discoverService.saveWebDiscuss(webDiscuss);
					flag = true;
				}
			}else if (StringUtils.isNotBlank(discussReplyId)) {
				WebDiscussReply webDiscussReply = discoverService.getWebDiscussReplyById(discussReplyId);
				if(webDiscussReply != null){
					Integer ups = webDiscussReply.getUps();
					ups ++;
					webDiscussReply.setUps(ups);
					webDiscussReply = discoverService.saveWebDiscussReply(webDiscussReply);
					flag = true;
				}
			}
		}
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	

	/**
	 * 新闻首页
	 * @author xczhao
	 *//*
	@RequestMapping(value = "/news/newsIndex")
	public String newsIndex(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "ifa/discover/news/newsIndex";
	}
	
	*//**
	 * 新闻列表
	 * @author xczhao
	 *//*
	@RequestMapping(value = "/news/newsList")
	public String newsList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "ifa/discover/news/newsList";
	}
	*//**
	 * 新闻内容
	 * @author xczhao
	 *//*
	@RequestMapping(value = "/news/newsContent")
	public String newsContent(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "ifa/discover/news/newsContent";
	}
	*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
