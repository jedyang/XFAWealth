package com.fsll.wmes.notice.action.console;

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
import org.yaml.snakeyaml.util.UriEncoder;

import com.fsll.common.CommonConstants;
import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.entity.SysAdmin;
import com.fsll.core.service.AccessoryFileService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfafirmEn;
import com.fsll.wmes.entity.MemberIfafirmSc;
import com.fsll.wmes.entity.MemberIfafirmTc;
import com.fsll.wmes.entity.Notice;
import com.fsll.wmes.notice.service.NoticeService;
import com.fsll.wmes.notice.vo.NoticeFilterVO;
import com.fsll.wmes.notice.vo.NoticeVO;

@Controller
@RequestMapping("/console/notice")
public class ConsoleNoticeAct extends WmesBaseAct{

	@Autowired
	private NoticeService noticeService;
	@Autowired
	private AccessoryFileService accessoryFileService;
	
	/**
	 * 公告列表页面
	 * @author wwluo
	 * @data 2017-03-14
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */							
	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberAdmin memberAdmin = this.getLoginMemberAdmin(request);
		String role = (String) request.getSession().getAttribute(CoreConstants.FRONT_USER_ROLE);
		if (memberAdmin != null || CoreConstants.FRONT_USER_SYS_ADMIN.equals(role)){
			String sourceType = null;
			if ("1".equals(memberAdmin.getType())) {
				sourceType = CommonConstantsWeb.NOTICE_SOURCE_TYPE_IFAFIRM;
			}else if("2".equals(memberAdmin.getType()) && memberAdmin.getDistributor() != null){
				sourceType = CommonConstantsWeb.NOTICE_SOURCE_TYPE_DISTRIBUTOR;
			}
			model.put("sourceType", sourceType);
			return "notice/list";
		}else{
			return "redirect:" + this.getFullPath(request) + "front/index.do";
		}
	}
	
	/**
	 * 公告列表数据获取
	 * @author wwluo
	 * @data 2017-03-14
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */							
	@RequestMapping(value = "/noticeJson")
	public void noticeJson(NoticeFilterVO noticeFilter,HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		JsonPaging jsonPaging = this.getJsonPaging(request);
		MemberAdmin memberAdmin = this.getLoginMemberAdmin(request);
		if (memberAdmin != null && "1".equals(memberAdmin.getType()) && memberAdmin.getIfafirm() != null) {
			noticeFilter.setSourceType(CommonConstantsWeb.NOTICE_SOURCE_TYPE_IFAFIRM);
			noticeFilter.setSource(memberAdmin.getIfafirm().getId());
		}else if(memberAdmin != null && "2".equals(memberAdmin.getType()) && memberAdmin.getDistributor() != null){
			noticeFilter.setSourceType(CommonConstantsWeb.NOTICE_SOURCE_TYPE_DISTRIBUTOR);
			noticeFilter.setSource(memberAdmin.getDistributor().getId());
		}
		if (StringUtils.isNotBlank(noticeFilter.getSourceType()) && StringUtils.isNotBlank(noticeFilter.getSource())) {
			String langCode = this.getLoginLangFlag(request);
			String dateFormat = memberAdmin.getMember().getDateFormat();
			if (StringUtils.isBlank(dateFormat)) {
				dateFormat = CommonConstants.FORMAT_DATE_TIME;
			}
			jsonPaging = noticeService.getNoticeJson(jsonPaging, noticeFilter, langCode, dateFormat);
		}
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 删除公告 (逻辑删除is_valid=0)
	 * @author wwluo
	 * @data 2017-03-16
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */						
	@RequestMapping(value = "/dateleNotice")
	public void dateleNotice(String id,HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Boolean flag = false;
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(id)) {
			Notice notice = noticeService.getNoticeById(id);
			if(notice != null){
				notice.setIsValid("0");
				noticeService.saveNotice(notice);
			}
			flag = true;
		}
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 公告创建、编辑页面
	 * @author wwluo
	 * @data 2017-03-14
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */							
	@RequestMapping(value = "/editNotice")
	public String editNotice(String id, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		String langCode = this.getLoginLangFlag(request);
		MemberAdmin memberAdmin = this.getLoginMemberAdmin(request);
		String role = (String) request.getSession().getAttribute(CoreConstants.FRONT_USER_ROLE);
		if (memberAdmin != null || CoreConstants.FRONT_USER_SYS_ADMIN.equals(role)){
			String sourceType = null;
			if ("1".equals(memberAdmin.getType())) {
				sourceType = CommonConstantsWeb.NOTICE_SOURCE_TYPE_IFAFIRM;
			}else if("2".equals(memberAdmin.getType()) && memberAdmin.getDistributor() != null){
				sourceType = CommonConstantsWeb.NOTICE_SOURCE_TYPE_DISTRIBUTOR;
			}
			NoticeVO vo = null;
			if (StringUtils.isNotBlank(id)) {
				vo = noticeService.getNoticeVOById(id, loginUser, langCode);
			}
			List<AccessoryFile> accessoryFiles = accessoryFileService.getAccessoryFile(id, CommonConstantsWeb.ACCESSORY_FILE_MODULE_TYPE_NOTICE, null, null);
			model.put("notice", vo);
			model.put("sourceType", sourceType);
			model.put("accessoryFiles", accessoryFiles);
			return "notice/editNotice";
		}else{
			return "redirect:" + this.getFullPath(request) + "front/index.do";
		}
	}
	
	/**
	 * 创建、修改公告
	 * @author wwluo
	 * @data 2017-03-14
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */							
	@RequestMapping(value = "/saveNotice")
	public void saveNotice(Notice notice,HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		MemberAdmin memberAdmin = this.getLoginMemberAdmin(request);
		Boolean flag = false;
		if(memberAdmin != null){
			String id = notice.getId();
			String subject = this.toUTF8String(notice.getSubject());
			String content = this.toUTF8String(notice.getContent());
			String accessoryFileId = this.toUTF8String(request.getParameter("accessoryFileId"));
			if (StringUtils.isNotBlank(notice.getId())) {
			// update
				Notice targetNotice = noticeService.getNoticeById(id);
				if(targetNotice != null){
					targetNotice.setSubject(subject);
					targetNotice.setContent(content);
					targetNotice.setLevel(notice.getLevel());;
					targetNotice.setTarget(notice.getTarget());
					targetNotice.setLastUpdate(new Date());
					notice = noticeService.saveNotice(targetNotice);
					flag = true;
				}
			}else{
			// create
				notice.setId(null);
				notice.setSubject(subject);
				notice.setContent(content);
				notice.setType("1");
				if(CommonConstantsWeb.MEMBERADMIN_TYPE_IFA.equals(memberAdmin.getType())){
					notice.setSource(memberAdmin.getIfafirm().getId());
					notice.setSourceType(CommonConstantsWeb.NOTICE_SOURCE_TYPE_IFAFIRM);
				}else if(CommonConstantsWeb.MEMBERADMIN_TYPE_DISTRIBUTOR.equals(memberAdmin.getType())){
					notice.setSource(memberAdmin.getDistributor().getId());
					notice.setSourceType(CommonConstantsWeb.NOTICE_SOURCE_TYPE_DISTRIBUTOR);
				}
				notice.setCreateTime(new Date());
				notice.setLastUpdate(new Date());
				notice.setReleaseDate(new Date());
				notice.setReleaseBy(loginUser.getId());
				notice.setIsValid("1");
				notice = noticeService.saveNotice(notice);
				flag = true;
			}
			if (StringUtils.isNotBlank(accessoryFileId)) {
				List fileIds = JsonUtil.toListMap(accessoryFileId);
				if(fileIds != null && !fileIds.isEmpty()){
					for (Object fileId : fileIds) {
						AccessoryFile accessoryFile = accessoryFileService.findAccessoryFileById((String) fileId);
						if(accessoryFile != null){
							accessoryFile.setRelateId(notice.getId());
							accessoryFileService.saveAccessoryFile(accessoryFile);
						}
					}
				}
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		result.put("notice", notice);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 *  公告附件保存
	 * @author wwluo
	 * @date 2017-06-05
	 */
	@RequestMapping(value = "/uploadNoticeDoc", method = RequestMethod.POST)
	public void uploadNoticeDoc(String id,HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberBase loginUser = this.getLoginUser(request);
		String filePath = toUTF8String(request.getParameter("filePath"));
		String orifilename = toUTF8String(request.getParameter("orifilename"));
		Map<String, Object> result = new HashMap<String, Object>();
		Boolean flag = false;
		AccessoryFile accessoryFile = null;
		accessoryFile = noticeService.saveNoticeDoc(loginUser, id, filePath, orifilename);
		if(accessoryFile != null){
			flag = true;
		}
		result.put("flag", flag);
		result.put("accessoryFile", accessoryFile);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 *  公告附件删除
	 * @author wwluo
	 * @date 2017-06-05
	 */
	@RequestMapping(value = "/removeNoticeDoc", method = RequestMethod.POST)
	public void removeNoticeDoc(String accessoryfileId,HttpServletRequest request,HttpServletResponse response,ModelMap model){
		Map<String, Object> result = new HashMap<String, Object>();
		Boolean flag = false;
		flag = noticeService.delNoticeDoc(accessoryfileId);
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
}
