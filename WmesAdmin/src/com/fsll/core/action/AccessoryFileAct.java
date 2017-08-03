package com.fsll.core.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.entity.SysAdmin;
import com.fsll.core.service.AccessoryFileService;
import com.fsll.core.service.SysAdminService;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.member.service.MemberBaseService;
import com.mysql.jdbc.Field;

@Controller
@RequestMapping(value = "/console")
public class AccessoryFileAct extends CoreBaseAct {

	@Autowired
	private AccessoryFileService accessoryFileService;
	
	@Autowired
	private SysAdminService sysAdminService;
	@Autowired
	private MemberBaseService memberBaseService;
	
	/**
	 * 附件管理分页列表
	 * 
	 * @author mqzou
	 * @date 2016-08-04
	 */
	@RequestMapping(value = "/sys/accessory/list")
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
	@RequestMapping(value = "/sys/accessory/detail")
	public String roleDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		model.put("id", id);
		AccessoryFile file=accessoryFileService.findAccessoryFileById(id);
		String createBy = file.getCreateBy();
		
		//modify by rqwang 20170627
		MemberBase memberBase = memberBaseService.findById(createBy);
		SysAdmin sysAdmin = sysAdminService.findById(createBy);
		if(null != id && StringUtils.isNotBlank(id)){
			if(null == sysAdmin){
				model.put("creater", memberBase.getNickName());
			}else{
				model.put("creater",sysAdmin.getNickName());
			}
		}
		//end
		
		model.put("file", file);
		return "console/sys/accessoryfile/input";
	}
	/**
	 * 分页列表数据查询
	 * @author mqzou
	 * @date 2016-08-04
	 */
	@RequestMapping(value = "/sys/accessory/jsonList")
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
