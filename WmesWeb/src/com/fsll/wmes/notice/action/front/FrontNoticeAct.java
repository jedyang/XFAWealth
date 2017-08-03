package com.fsll.wmes.notice.action.front;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.notice.service.NoticeService;
import com.fsll.wmes.notice.vo.NoticeVO;

@Controller
@RequestMapping("/front/notice")
public class FrontNoticeAct extends WmesBaseAct {

	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private MemberBaseService memberBaseService;

	/**
	 * 公告列表页面(查看)
	 * @author wwluo
	 * @data 2017-03-14
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */							
	@RequestMapping(value = "/previewNotices")
	public String previewNotices(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		this.saveLastVisitUrl(request, response);
		return "notice/previewNotices";
	}
	
	/**
	 * 获取公告信息 Announcement
	 * @author wwluo
	 * @data 2017-03-16
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */							
	@RequestMapping(value = "/getAnnouncement")
	public void getAnnouncement(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		String langCode = this.getLoginLangFlag(request);
		String type = request.getParameter("type"); // fund、system
		jsonPaging = this.getJsonPaging(request);
		if("fund".equals(type)){
			jsonPaging = noticeService.getAnnouncementFund(jsonPaging, loginUser, langCode);
		}else{
			jsonPaging = noticeService.getAnnouncementSys(jsonPaging, loginUser, langCode);
		}
		this.toJsonString(response, jsonPaging);
	}

	/**
	 * 公告详情查看（app）
	 * @author wwluo
	 * @data 2017-03-14
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */							
	@RequestMapping(value = "/detail")
	public String preview(String id, String memberId, String langCode, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = memberBaseService.findById(memberId);
		NoticeVO notice = noticeService.getNoticeVOById(id, loginUser, langCode);	
		String content = notice.getContent();
		if (StringUtils.isNotBlank(content) && content.indexOf("loadImgSrcByPath") > -1) {
			content = content.replaceAll("loadImgSrcByPath", "loadImgSrcByPathNoLogin");
			notice.setContent(content);
		}
		model.put("notice", notice);
		return "notice/preview";
	}



}
