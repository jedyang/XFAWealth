package com.fsll.core.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fsll.core.CoreConstants;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.entity.SysRole;
import com.fsll.core.service.AccessoryFileService;
import com.fsll.wmes.member.vo.MemberSsoVO;

@Controller
public class AccessoryFileAct extends CoreBaseAct {

	@Autowired
	private AccessoryFileService accessoryFileService;
	
	/**
	 * 附件管理分页列表
	 * 
	 * @author mqzou
	 * @date 2016-08-04
	 */
	@RequestMapping(value = "/console/sys/accessory/list")
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		this.isMobileDevice(request, model);
		
		return "console/sys/accessoryfile/list";
	}
	/**
	 * 附件明细
	 * 
	 * @author mqzou
	 * @date 2016-08-04
	 */
	@RequestMapping(value = "/console/sys/accessory/detail")
	public String roleDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		model.put("id", id);
		AccessoryFile file=accessoryFileService.findAccessoryFileById(id);
		
		model.put("file", file);
		return "console/sys/accessoryfile/input";

	}
	/**
	 * 分页列表数据查询
	 * @author mqzou
	 * @date 2016-08-04
	 */
	@RequestMapping(value = "/console/sys/accessory/jsonList")
	public void getList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String keyword=request.getParameter("keyWord");
		String module=request.getParameter("module");
		if (null != keyword && !"".equals(keyword)) {
			try {
				keyword = URLDecoder.decode(keyword, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (null != module && !"".equals(module)) {
			try {
				module = URLDecoder.decode(module, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		AccessoryFile file=new AccessoryFile();
		file.setFileName(keyword);
		file.setModuleType(module);
		jsonPaging=getJsonPaging(request);
		jsonPaging=accessoryFileService.findAllAccessoryFile(jsonPaging, file);
		this.toJsonString(response, jsonPaging);
		
	}
}