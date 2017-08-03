package com.fsll.wmes.crm.action.console;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.ResponseUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.wmes.crm.service.DocTemplateManageService;
import com.fsll.wmes.crm.vo.DocInfoVo;
import com.fsll.wmes.crm.vo.DocTemplateVo;
import com.fsll.wmes.distributor.service.DistributorService;
import com.fsll.wmes.entity.DocList;
import com.fsll.wmes.entity.DocListEn;
import com.fsll.wmes.entity.DocListSc;
import com.fsll.wmes.entity.DocListTc;
import com.fsll.wmes.entity.DocTemplate;
import com.fsll.wmes.entity.DocTemplateEn;
import com.fsll.wmes.entity.DocTemplateSc;
import com.fsll.wmes.entity.DocTemplateTc;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.member.vo.MemberSsoVO;


/*****
 * 文档审查模板管理
 * @author scshi_u330p
 * @date 20161213
 */
@Controller
@RequestMapping("/console/docTemplate")
public class ConsoleDocTemplateManageAct extends CoreBaseAct{
	@Autowired
	private DistributorService distributorConsoleService; 
	@Autowired
	private DocTemplateManageService docTemplateManageService;
	
	/**
	 * 文档审查模板列表页面
	 * @author scshi_u330p
	 * @date 20161215
	 * */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		return "console/docTemplate/list";
	}
	
	/**
	 * 文档审查模板列表数据
	 * @author scshi_u330p
	 * @date 20161215
	 * */
	@RequestMapping(value = "/tempPageJson", method = RequestMethod.POST)
	public void tempPageJson(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		//获取当前登录者信息
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.FRONT_USER_SSO);
		MemberAdmin admin = distributorConsoleService.findDistributorMemberAdmin(curMember.getId());
		String language = this.getLoginLangFlag(request);//获取语言
		
		String docTemplateTitle = request.getParameter("title");
		String docTemplateStatus = request.getParameter("status");
		if(null!=docTemplateTitle&&!"".equals(docTemplateTitle)){
			try {
				docTemplateTitle=URLDecoder.decode(docTemplateTitle, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		jsonPaging=this.getJsonPaging(request);
		jsonPaging=docTemplateManageService.findPageAll(jsonPaging, admin, language,docTemplateTitle,docTemplateStatus);
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 文档模板新增/查看/编辑表单
	 * @author scshi_u330p
	 * @date 20161216
	 * */
	@RequestMapping(value = "/detail")
	public String detail(HttpServletRequest request,HttpServletResponse response,ModelMap model) {	
		this.isMobileDevice(request,model);
		String id=request.getParameter("id");
		String tab=request.getParameter("tab");
		
		DocTemplateVo vo = new DocTemplateVo();
		vo.setTemp(new DocTemplate());
		vo.getTemp().setIsDefault(false);
		vo.getTemp().setStatus("using");
		vo.getTemp().setLangCode(CoreConstants.LANG_CODE);//sc
		vo.getTemp().setClientType("Individual");
		vo.setTempEn(new DocTemplateEn());
		vo.setTempSc(new DocTemplateSc());
		vo.setTempTc(new DocTemplateTc());
		
		String language = this.getLoginLangFlag(request);//获取语言
		
		if (StringUtils.isNoneBlank(id))
			vo = docTemplateManageService.loadeDocTemplateDetail(id,language);
		
		//model.put("detail", vo);
		model.put("temp", vo.getTemp());
		model.put("tempEn", vo.getTempEn());
		model.put("tempSc",vo.getTempSc());
		model.put("tempTc", vo.getTempTc());
		model.put("tab", tab);
		return "console/docTemplate/detail";
	}	
	
	/**
	 * 文档模板信息保存
	 * @author scshi_u330p
	 * @date 200161219
	 * */
	@RequestMapping(value = "/tempSave", method = RequestMethod.POST)
	public void tempSave(HttpServletRequest request,HttpServletResponse response,ModelMap models,DocTemplateVo vo){
		MemberSsoVO curMember = this.getLoginUserSSO(request);
		MemberAdmin admin = distributorConsoleService.findDistributorMemberAdmin(curMember.getId());
		DocTemplate docTemp = docTemplateManageService.saveDocTempInfo(vo,admin);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result", true);
		obj.put("id", docTemp.getId());
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/**
	 * 文档模板删除
	 * @author scshi_u330p
	 * @date 20161227
	 * */
	@RequestMapping(value = "/tempDel", method = RequestMethod.POST)
	public void tempDelete(HttpServletRequest request,HttpServletResponse response,ModelMap models,String tempId){
		docTemplateManageService.kycDocTempDelete(tempId);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result", true);
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 文档列表json
	 * @author scshi_u330p
	 * @date20161216
	 * */
	@RequestMapping(value = "/listPageJson", method = RequestMethod.POST)
	public void listPageJson(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String tempId = request.getParameter("id");
		String keyword = request.getParameter("keyword");
		String language = this.getLoginLangFlag(request);
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = docTemplateManageService.loadDocListPageJson(tempId,keyword,language,jsonPaging);
		toJsonString(response, jsonPaging);
	}
	
	/**
	 * 文档新增/查看/编辑
	 * @author scshi_u330p
	 * @date 20161216
	 * */
	@RequestMapping("/form")
	public String form(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String docId = request.getParameter("id");
		String tempId = request.getParameter("tempId");
		DocInfoVo vo = new DocInfoVo();
		vo.setDoc(new DocList());
		vo.getDoc().setIsNecessary("1");
		vo.getDoc().setIsValid("1");
		vo.setDocEn(new DocListEn());
		vo.setDocSc(new DocListSc());
		vo.setDocTc(new DocListTc());
		
		String language = this.getLoginLangFlag(request);//获取语言
		
		if (StringUtils.isNoneBlank(docId))
			vo = docTemplateManageService.loadDocInfo(docId,language);
		
		model.put("doc", vo.getDoc());
		model.put("docEn",vo.getDocEn());
		model.put("docTc", vo.getDocTc());
		model.put("docSc", vo.getDocSc());
		
		//获取文档定义列表
		if(StringUtils.isNotBlank(tempId)){
			jsonPaging = this.getJsonPaging(request);
			jsonPaging = docTemplateManageService.loadDocListPageJson(null,null,language,jsonPaging);
			List docModelList = jsonPaging.getList();
			model.put("docModelList", docModelList);
		}
		
		if (vo.getDoc()!=null && vo.getDoc().getTemplate()!=null && StringUtils.isNoneBlank(vo.getDoc().getTemplate().getId()))
			tempId = vo.getDoc().getTemplate().getId();
			
		model.put("tempId", tempId);
		
		return "console/docTemplate/docInfo";
	}
	
	/**
	 * 文档列表信息保存
	 * @author scshi_u330p
	 * @date 20161219
	 * */
	@RequestMapping(value = "/docInfoSave", method = RequestMethod.POST)
	public void infoSave(HttpServletRequest request,HttpServletResponse response,ModelMap model,DocInfoVo vo){
		docTemplateManageService.saveDocListInfo(vo);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result", true);
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/***
	 * 文档删除
	 * @author scshi_u330p
	 * @date 20161226
	 * */
	@RequestMapping(value = "/docDel", method = RequestMethod.POST)
	public void docDelete(HttpServletRequest request,HttpServletResponse response,ModelMap model,String docId){
		docTemplateManageService.kycDocInfoDelete(docId);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result", true);
		JsonUtil.toWriter(obj, response);
	}
	
	
	/**
	 * 验证模板编号是否被使用
	 * @author scshi_u330p
	 * @date 20161219
	 * */
	@RequestMapping("/codeUnique")
	public void codeUnique(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String code = request.getParameter("code");
		String tempId = request.getParameter("tempId");
		String flag = docTemplateManageService.checkCodeUnique(code,tempId);
		Map<String,Object> map = new HashMap();
		map.put("result", "1".equals(flag)?false:true);
		JsonUtil.toWriter(map, response);
	}
	
	
	/***
	 * 文档模板定义
	 * @author scshi
	 * @date 20170615
	 * **/
	@RequestMapping("/docModelPage")
	public String docModelPage(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		this.isMobileDevice(request,model);
		return "console/docTemplate/docModel/modelList";
	}
	
	/***
	 *文档模板列表
	 *@author scshi
	 *@date 20170612
	 * */
	@RequestMapping(value="/docModelListJson",method=RequestMethod.POST)
	public void docModelListJson(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String keyword = request.getParameter("keyword");
		String language = this.getLoginLangFlag(request);
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = docTemplateManageService.loadDocListPageJson(null,keyword,language,jsonPaging);
		toJsonString(response, jsonPaging);
	}
	
	/**
	 * 获取已定义文档数据
	 * @author scshi
	 * @date 20170616
	 * **/
	@RequestMapping(value="/loadDocModelData",method=RequestMethod.POST)
	public void loadDocModelData(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String docModelId = request.getParameter("docModelId");
		String language = this.getLoginLangFlag(request);//获取语言
		
		DocInfoVo vo = new DocInfoVo();
		vo = docTemplateManageService.loadDocInfo(docModelId,language);
		Map<String,Object> map = new HashMap();
		map.put("result", true);
		map.put("docDate", vo);
		JsonUtil.toWriter(map, response);
		
	}
	
}
