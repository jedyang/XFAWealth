package com.fsll.wmes.web.action.console;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.JsonUtil;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.wmes.entity.WebEmailLog;
import com.fsll.wmes.web.service.EmailLogService;
import com.fsll.wmes.web.vo.EmailLogVO;
import com.fsll.wmes.web.vo.WebToDoVO;

/*****
 * 邮件管理
 * @author mqzou
 * 2016-06-23
 */
@Controller
public class EmailLogAct extends CoreBaseAct {

	@Autowired
	 private EmailLogService emailLogService;
	 
	
	/**
	 * 分页列表
	 * @author mqzou
	 * @date 2016-06-23 
	 */
	@RequestMapping(value = "/console/emaillog/list")
	public String list(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		return "console/emaillog/list";
	}
	/**
	 * 查看明细页面
	 * @author mqzou
	 * @date 2016-06-23 
	 */
	@RequestMapping(value = "/console/emaillog/detail")
	public String view(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String id=request.getParameter("id");
		model.put("id", id);
		WebEmailLog obj = emailLogService.findById(id);        
		EmailLogVO vo = new EmailLogVO();
		BeanUtils.copyProperties(obj,vo);//拷贝信息;
		vo.setToMemberName(obj.getToMember().getNickName());
		model.put("emailVO", vo);
		return "console/emaillog/input";
	}
	/**
	 * 数据查询的方法
	 * @author mqzou
	 * @date 2016-03-17 
	 */
	@RequestMapping(value = "/console/emaillog/listJson", method = RequestMethod.POST)
	public void listJson(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String adr=request.getParameter("address");
		WebEmailLog emailLog=new WebEmailLog();
		if(null!=adr&&!"".equals(adr)){
			try {
				adr=URLDecoder.decode(adr, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		emailLog.setToEmailAddr(adr);
		jsonPaging=this.getJsonPaging(request);
		jsonPaging=emailLogService.findAll(jsonPaging, emailLog);
		this.toJsonString(response, jsonPaging);
	}
	
}
