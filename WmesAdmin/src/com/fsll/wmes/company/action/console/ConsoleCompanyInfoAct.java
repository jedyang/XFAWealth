package com.fsll.wmes.company.action.console;

import java.util.ArrayList;
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
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.ResponseUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.entity.SysAdmin;
import com.fsll.core.vo.MenuTreeVO;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.company.service.CompanyInfoService;
import com.fsll.wmes.company.vo.CompanyInfoVO;
import com.fsll.wmes.company.vo.MemberCompanyVO;
import com.fsll.wmes.entity.CompanyInfo;
import com.fsll.wmes.entity.CompanyInfoEn;
import com.fsll.wmes.entity.CompanyInfoSc;
import com.fsll.wmes.entity.CompanyInfoTc;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmVO;

/**
 * 运营公司信息管理（后台）
 * @author Yan
 * @version 1.0.0 Created On: 2016-11-29
 */

@Controller
@RequestMapping("/console/company")
public class ConsoleCompanyInfoAct extends WmesBaseAct {
	
	@Autowired
	private CompanyInfoService companyInfoService;
	
    /**
     * 分页列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
        SysAdmin admin = this.getLoginUser(request);
        if(null != admin){
            return "console/company/list";//页面摆放路径
        }else{
        	return "redirect:"+this.getFullPath(request)+"viewLogin.do";
        }
    }
    
    /**
     * 分页获得记录
     */
    @RequestMapping(value = "/listJson", method = RequestMethod.POST)
    public String listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String langCode = this.getLoginLangFlag(request);
    	String name = request.getParameter("name");
    	String code = request.getParameter("code");
    	CompanyInfoVO infoVo = new CompanyInfoVO();
    	infoVo.setCode(code);
    	if(!StrUtils.isEmpty(name)){
    		infoVo.setName(name);
    	}
        jsonPaging = this.getJsonPaging(request);
        jsonPaging = companyInfoService.findAll(jsonPaging,infoVo,langCode);
        //路径转换
        List list = jsonPaging.getList();
        if(!list.isEmpty()){
        	for (int i = 0; i < list.size(); i++) {
        		Object[] obj = (Object[])list.get(i);
        		CompanyInfo info = (CompanyInfo)obj[0];
        		String logoUrl = info.getLogoUrl();
        		if (!StrUtils.isEmpty(logoUrl)){
	        		logoUrl = PageHelper.getLogoUrl(logoUrl, "C");
        		}else{
        			//logoUrl = "/res/images/common/default_no_image.png";
        			logoUrl = "/res/images/common/default_company.png";
        		}
        		info.setLogoUrl(logoUrl);
			}
        }
        jsonPaging.setList(list);
        this.toJsonString(response, jsonPaging);
        return null;
    }
    
    /**
	 * company数据查询的方法（下拉填充数据）
	 * @author Yan
	 * @date 2017-02-13
	 */
	@RequestMapping(value = "/allList", method = RequestMethod.POST)
	public void listJsonCompany(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode = this.getLoginLangFlag(request);
		List<MemberCompanyVO> list = companyInfoService.getCompanyList(langCode);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result", true);
		obj.put("companyJson", list);
		JsonUtil.toWriter(obj, response);
	}
	
    /**
     * 详细信息页面
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String input(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String id = request.getParameter("id");
    	if(!StrUtils.isEmpty(id)){
	        model.put("id", id);
	        CompanyInfoVO infoVO = companyInfoService.findVoById(id);
	        model.put("infoVO", infoVO);
    	}else {
    		model.put("infoVO", new CompanyInfoVO());
		}
        return "console/company/input";
    }
    
    /**
	 * 编辑页面
	 */
	@RequestMapping(value = "/edit")
	public String edit(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		String id=request.getParameter("id");
		CompanyInfo info = companyInfoService.findById(id);
		CompanyInfoSc infoSc = companyInfoService.findScById(id);
		CompanyInfoTc infoTc = companyInfoService.findTcById(id);
		CompanyInfoEn infoEn = companyInfoService.findEnById(id);
		String language = this.getLoginLangFlag(request);//获取语言
		
		model.put("info", info);
		model.put("infoSc", infoSc);
		model.put("infoTc", infoTc);
		model.put("infoEn", infoEn);
		model.put("language", language);
		
		return "console/company/edit";
	}
	
	/**
	 * 查询code唯一性
	 */
	@RequestMapping(value = "/checkCodeUnique", method = RequestMethod.POST)
	public void checkCodeUnique(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		
		String code = request.getParameter("code");
		//传入code参数不为空
		if(StringUtils.isNotBlank(code)){
			CompanyInfo info = companyInfoService.findByCode(code);
			//code暂未存在
			if(null==info){
				obj.put("result",true);
				obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
			}else{
				obj.put("result",false);
				obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.failed",null));
			}
		}else{
			obj.put("result",false);
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.failed",null));
		}
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/**
	 * 新增/保存编辑
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void save(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		boolean isAdd =false;
		List<String> isSubAdd = new ArrayList<String>();
		
		String id=request.getParameter("id");
		String code = request.getParameter("code");
		String webUrl = request.getParameter("webUrl");
		String logoUrl = request.getParameter("logoUrl");
		String backgroundUrl = request.getParameter("backgroundUrl");
		String cssUrl = request.getParameter("cssUrl");
		String loginUrl = request.getParameter("loginUrl");
		String nameSc=request.getParameter("nameSc");
		String sysNameSc=request.getParameter("sysNameSc");
		String copyrightSc=request.getParameter("copyrightSc");
		String nameTc=request.getParameter("nameTc");
		String sysNameTc=request.getParameter("sysNameTc");
		String copyrightTc=request.getParameter("copyrightTc");
		String nameEn=request.getParameter("nameEn");
		String sysNameEn=request.getParameter("sysNameEn");
		String copyrightEn=request.getParameter("copyrightEn");
		
		//创建实体
		CompanyInfo info;
		CompanyInfoSc infoSc;
		CompanyInfoTc infoTc;
		CompanyInfoEn infoEn;
		
		if(null==id||"".equals(id))
		{
			isAdd = true;
			info = new CompanyInfo();
			infoSc = new CompanyInfoSc();
			infoTc = new CompanyInfoTc();
			infoEn = new CompanyInfoEn();
			info.setId(null);
			infoSc.setId(null);
			infoTc.setId(null);
			infoEn.setId(null);
		} else {
			isAdd = false;
			info = companyInfoService.findById(id);
			infoSc = companyInfoService.findScById(id);
			if("".equals(infoSc)||infoSc==null){
				isSubAdd.add("sc");
				infoSc = new CompanyInfoSc();
				infoSc.setId(id);
			}
			infoTc = companyInfoService.findTcById(id);
			if("".equals(infoTc)||infoTc==null){
				isSubAdd.add("tc");
				infoTc = new CompanyInfoTc();
				infoTc.setId(id);
			}
			infoEn = companyInfoService.findEnById(id);
			if("".equals(infoEn)||infoEn==null){
				isSubAdd.add("en");
				infoEn = new CompanyInfoEn();
				infoEn.setId(info.getId());
			}
		}

		info.setIsValid("1");
		info.setCode(code);
		info.setWebUrl(webUrl);
		info.setLogoUrl(logoUrl);
		info.setBackgroundUrl(backgroundUrl);
		info.setCssUrl(cssUrl);
		info.setLoginUrl(loginUrl);
		infoSc.setName(nameSc);
		infoSc.setSysName(sysNameSc);
		infoSc.setCopyright(copyrightSc);
		infoTc.setName(nameTc);
		infoTc.setSysName(sysNameTc);
		infoTc.setCopyright(copyrightTc);
		infoEn.setName(nameEn);
		infoEn.setSysName(sysNameEn);
		infoEn.setCopyright(copyrightEn);
				
		info = companyInfoService.save(info, infoSc, infoTc, infoEn, isAdd,isSubAdd);
			
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value = "/del", method = RequestMethod.POST)
	public void delete(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String id=request.getParameter("id");		
		companyInfoService.deleteById(id);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/**
	 * 跳转菜单页
	 */
	@RequestMapping(value = "/menu")
	public String menu(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		model.put("id", id);
		return "console/company/company_menu";
	}
	
	/**
	 * 获取权限
	 */
	@RequestMapping(value = "/getMenuTree", method = RequestMethod.POST)
	public void getMenuTree(HttpServletRequest request, HttpServletResponse response){
		String id=request.getParameter("id");
		String langCode=getLoginLangFlag(request);
		Map<String, Object> obj = new HashMap<String, Object>();
		List<MenuTreeVO> list=companyInfoService.getMenuTree(id, langCode);
		obj.put("treeData", list);
		obj.put("id", id);
		JsonUtil.toWriter(obj,response);
	}
	
	/**
	 * 保存菜单权限
	 */
	@RequestMapping(value = "/saveMenu", method = RequestMethod.POST)
	public void saveMenu(HttpServletRequest request, HttpServletResponse response){
		String id=request.getParameter("id");
		String menuIds=request.getParameter("menuIds");
		String[] menuId=menuIds.split(",");
		
		boolean result = companyInfoService.saveMenu(menuId, id);
		Map<String, Object> obj = new HashMap<String, Object>();
		if(result){
			obj.put("result", true);
			obj.put("message", "success");
		}else {
			obj.put("result", false);
			obj.put("message", "failure");
		}
		JsonUtil.toWriter(obj,response);
	}
}
