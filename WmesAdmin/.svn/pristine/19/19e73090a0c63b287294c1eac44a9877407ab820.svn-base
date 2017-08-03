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

import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.JsonUtil;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.community.service.NewsCommentService;
import com.fsll.wmes.community.vo.NewsCommentVO;
import com.fsll.wmes.entity.NewsComment;

@Controller
@RequestMapping("/console/community/newsComment")
public class ConsoleNewsCommentAct extends WmesBaseAct{
	
	@Autowired
	private NewsCommentService newsCommentService;

	/**
	 *  新闻评论管理页面
	 * @author wwluo
	 * @date 2017-06-02
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		return "console/community/newsComment/list";
	}
	
	/**
	 *  新闻评论编辑页面
	 * @author wwluo
	 * @date 2017-06-02
	 */
	@RequestMapping(value = "/input", method = RequestMethod.GET)
	public String input(String id,HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String langCode = this.getLoginLangFlag(request);
		NewsCommentVO comment = newsCommentService.findNewsCommentVOById(id, langCode);
		model.put("comment", comment);
		return "console/community/newsComment/input";
	}
	
	/**
	 *  新闻评论数据获取
	 * @author wwluo
	 * @date 2017-06-02
	 * @param newsCommentVO 过滤条件
	 */
	@RequestMapping(value = "/getNewsComments", method = RequestMethod.POST)
	public void getNewsComments(NewsCommentVO newsCommentVO,HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String langCode = this.getLoginLangFlag(request);
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = newsCommentService.getNewsComments(jsonPaging, newsCommentVO, langCode);
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 *  删除新闻评论数据（逻辑删除 news_comment.status=-1）
	 * @author wwluo
	 * @date 2017-06-02
	 * @param ids 删除id,可批量删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(String ids,HttpServletRequest request,HttpServletResponse response,ModelMap model){
		Boolean flag = false;
		Map<String, Object> result = new HashMap<String, Object>();
		flag = newsCommentService.deleteNewsComments(ids);
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 *  新闻数据恢复
	 * @author wwluo
	 * @date 2017-06-02
	 * @param id
	 */
	@RequestMapping(value = "/restore", method = RequestMethod.POST)
	public void restore(String id,HttpServletRequest request,HttpServletResponse response,ModelMap model){
		Boolean flag = false;
		Map<String, Object> result = new HashMap<String, Object>();
		NewsComment newsComment = newsCommentService.findById(id);
		if(newsComment != null){
			newsComment.setStatus("1");
			newsComment = newsCommentService.save(newsComment);
			flag = true;
		}
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 *  修改新闻评论
	 * @author wwluo
	 * @date 2017-06-02
	 * @param id 新闻评论实体ID
	 */
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public void modify(String id,HttpServletRequest request,HttpServletResponse response,ModelMap model){
		Boolean flag = false;
		String comment = toUTF8String(request.getParameter("comment"));
		String status = request.getParameter("status");
		if (StringUtils.isNotBlank(id)) {
			NewsComment newsComment = newsCommentService.findById(id);
			if(newsComment != null){
				newsComment.setComment(comment);
				newsComment.setStatus(status);
				newsComment = newsCommentService.save(newsComment);
				flag = true;
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
