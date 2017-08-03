package com.fsll.wmes.community.action.console;

import java.util.HashMap;
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
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.community.service.TopicCommentService;
import com.fsll.wmes.community.vo.NewsCommentVO;
import com.fsll.wmes.community.vo.TopicCommentVO;
import com.fsll.wmes.entity.CommunityComment;
import com.fsll.wmes.entity.NewsComment;

@Controller
@RequestMapping("/console/community/topicComment")
public class ConsoleTopicCommentAct extends WmesBaseAct{

	@Autowired
	private TopicCommentService topicCommentService;
	
	/**
	 *  帖子评论管理页面
	 * @author wwluo
	 * @date 2017-06-02
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		return "console/community/topicComment/list";
	}
	
	/**
	 *  帖子评论编辑页面
	 * @author wwluo
	 * @date 2017-06-02
	 */
	@RequestMapping(value = "/input", method = RequestMethod.GET)
	public String input(String id,HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String langCode = this.getLoginLangFlag(request);
		TopicCommentVO comment = topicCommentService.findCommentVOById(id, langCode);
		model.put("comment", comment);
		return "console/community/topicComment/input";
	}
	
	/**
	 *  帖子评论(包含问题答案)数据获取
	 * @author wwluo
	 * @date 2017-06-02
	 * @param newsCommentVO 过滤条件
	 */
	@RequestMapping(value = "/getTopicComments", method = RequestMethod.POST)
	public void getTopicComments(TopicCommentVO topicCommentVO,HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String langCode = this.getLoginLangFlag(request);
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = topicCommentService.getTopicComments(jsonPaging, topicCommentVO, langCode);
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 *  删除帖子评论数据（逻辑删除）
	 * @author wwluo
	 * @date 2017-06-02
	 * @param ids 删除id,可批量删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(String ids,HttpServletRequest request,HttpServletResponse response,ModelMap model){
		Boolean flag = false;
		Map<String, Object> result = new HashMap<String, Object>();
		flag = topicCommentService.deleteCommunityComments(ids);
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}

	/**
	 *  社区评论数据恢复
	 * @author wwluo
	 * @date 2017-06-02
	 * @param id
	 */
	@RequestMapping(value = "/restore", method = RequestMethod.POST)
	public void restore(String id,HttpServletRequest request,HttpServletResponse response,ModelMap model){
		Boolean flag = false;
		Map<String, Object> result = new HashMap<String, Object>();
		CommunityComment comment = topicCommentService.findById(id);
		if(comment != null){
			comment.setStatus(1);
			comment = topicCommentService.save(comment);
			flag = true;
		}
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 *  修改社区评论
	 * @author wwluo
	 * @date 2017-06-02
	 * @param id 评论实体ID
	 */
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public void modify(String id,HttpServletRequest request,HttpServletResponse response,ModelMap model){
		Boolean flag = false;
		String content = toUTF8String(request.getParameter("comment"));
		String statusStr = request.getParameter("status");
		Integer status = null;
		if (StringUtils.isNotBlank(statusStr)) {
			status = Integer.valueOf(statusStr);
		}
		if (StringUtils.isNotBlank(id)) {
			CommunityComment comment = topicCommentService.findById(id);
			if(comment != null){
				comment.setContent(content);
				comment.setStatus(status);
				comment = topicCommentService.save(comment);
				flag = true;
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
}
