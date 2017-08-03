package com.fsll.wmes.notice.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.service.AccessoryFileService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.notice.service.NoticeService;
import com.fsll.wmes.notice.vo.NoticeVO;

@Controller
@RequestMapping("/console/notice")
public class ConsoleNoticeAct extends WmesBaseAct{

	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private AccessoryFileService accessoryFileService;
	
	/**
	 *  业务公告管理页面
	 * @author wwluo
	 * @date 2017-06-05
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		return "console/notice/console/list";
	}
	
	/**
	 *  业务公告详情页面
	 * @author wwluo
	 * @date 2017-06-05
	 */
	@RequestMapping(value = "/input", method = RequestMethod.GET)
	public String input(String id,HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String langCode = getLoginLangFlag(request);
		NoticeVO vo = noticeService.findNoticeVOById(id, langCode);
		List<AccessoryFile> accessoryFiles = accessoryFileService.getAccessoryFile(id, CommonConstantsWeb.ACCESSORY_FILE_MODULE_TYPE_NOTICE, null, null);
		model.put("notice", vo);
		model.put("accessoryFiles", accessoryFiles);
		return "console/notice/console/input";
	}
	
	/**
	 *  业务公告数据获取
	 * @author wwluo
	 * @date 2017-06-05
	 */
	@RequestMapping(value = "/getNotices", method = RequestMethod.POST)
	public void getNotices(NoticeVO noticeVO,HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String langCode = getLoginLangFlag(request);
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = noticeService.getConsoleNotices(jsonPaging, noticeVO, langCode);
		this.toJsonString(response, jsonPaging);
	}
}
