package com.fsll.wmes.company.action.console;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.ResponseUtils;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.company.service.CompanyInfoService;
import com.fsll.wmes.company.service.MemberCompanyIfafirmService;
import com.fsll.wmes.company.vo.MemberCompanyIfafirmVO;
import com.fsll.wmes.entity.CompanyInfo;
import com.fsll.wmes.entity.CompanyInfoEn;
import com.fsll.wmes.entity.CompanyInfoSc;
import com.fsll.wmes.entity.CompanyInfoTc;
import com.fsll.wmes.entity.MemberCompanyIfafirm;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberIfafirmEn;
import com.fsll.wmes.entity.MemberIfafirmSc;
import com.fsll.wmes.entity.MemberIfafirmTc;
import com.fsll.wmes.member.service.MemberIfafirmService;

/**
 * 运营公司信息管理-公司IFA Firm设置（后台）
 * @author Yan
 * @version 1.0.0 Created On: 2016-12-06
 */

@Controller
@RequestMapping("/console/membercompanyifafirm")
public class ConsoleMemberCompanyIfafirmAct extends WmesBaseAct {
	
	@Autowired
	private MemberCompanyIfafirmService memberCompanyIfafirmService;
	@Autowired
	private CompanyInfoService companyInfoService;
	@Autowired
	private MemberIfafirmService memberIfafirmService;
	
    /**
     * 分页列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
        return "console/company/membercompanyifafirm/list";//页面摆放路径
    }
    
    /**
     * 分页获得记录
     */
    @RequestMapping(value = "/listJson", method = RequestMethod.POST)
    public String listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String langCode = this.getLoginLangFlag(request);
    	String companyName = request.getParameter("companyName");
    	
    	//条件筛选
    	MemberCompanyIfafirmVO infoVo = new MemberCompanyIfafirmVO();
    	if(StringUtils.isNotBlank(companyName)){
    		infoVo.setCompanyName(companyName);
    	}
    	
        jsonPaging = this.getJsonPaging(request);
        jsonPaging = memberCompanyIfafirmService.findAll(jsonPaging, infoVo, langCode);
        List<MemberCompanyIfafirmVO> list = new ArrayList<MemberCompanyIfafirmVO>();
        List<Object> objList = jsonPaging.getList();
        if (null!=objList)
        for (int i = 0; i < objList.size(); i++) {
			MemberCompanyIfafirmVO vo = new MemberCompanyIfafirmVO();
			Object[] objRead = (Object[])objList.get(i);
			MemberCompanyIfafirm info = (MemberCompanyIfafirm)objRead[0];
			if("sc".equals(langCode)){
				CompanyInfoSc infoSc = (CompanyInfoSc)objRead[1];
				if(!"".equals(infoSc) && null!=infoSc){
					vo.setCompanyName(infoSc.getName());
    			}
    			MemberIfafirmSc ifafirmSc = (MemberIfafirmSc)objRead[2];
    			if(!"".equals(ifafirmSc) && null!=ifafirmSc){
    				vo.setIfafirmName(ifafirmSc.getCompanyName());
    			}
			}
			if("tc".equals(langCode)){
				CompanyInfoTc infoTc = (CompanyInfoTc)objRead[1];
				if(!"".equals(infoTc) && null!=infoTc){
					vo.setCompanyName(infoTc.getName());
				}
				MemberIfafirmTc ifafirmTc = (MemberIfafirmTc)objRead[2];
				if(!"".equals(ifafirmTc) && null!=ifafirmTc){
					vo.setIfafirmName(ifafirmTc.getCompanyName());
				}
			}
			if("en".equals(langCode)){
				CompanyInfoEn infoEn = (CompanyInfoEn)objRead[1];
				if(!"".equals(infoEn) && null!=infoEn){
					vo.setCompanyName(infoEn.getName());
				}
				MemberIfafirmEn ifafirmEn = (MemberIfafirmEn)objRead[2];
				if(!"".equals(ifafirmEn) && null!=ifafirmEn){
					vo.setIfafirmName(ifafirmEn.getCompanyName());
    			}
			}
			BeanUtils.copyProperties(info,vo);
			list.add(vo);
		}
		jsonPaging.setList(list);
        this.toJsonString(response, jsonPaging);
        return null;
    }
    
    /**
     * 详细信息页面
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String langCode = this.getLoginLangFlag(request);
    	String id = request.getParameter("id");
    	
    	List<CompanyInfo> companyVoList = companyInfoService.findAllCompany(langCode);
    	List<MemberIfafirm> memberIfafirmList = (List<MemberIfafirm>)memberIfafirmService.findAllMemberIfafirm(langCode);
    	model.put("companyVoList", companyVoList);
    	model.put("memberIfafirmList", memberIfafirmList);
    	
    	if(!"".equals(id) && id!=null){
	        model.put("id", id);
	        MemberCompanyIfafirmVO infoVo = (MemberCompanyIfafirmVO)memberCompanyIfafirmService.findVoById(id, langCode);
	        model.put("infoVo", infoVo);
    	}
        return "console/company/membercompanyifafirm/input";
    }
    
   
	/**
	 * 新增/保存编辑
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void save(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		boolean isAdd =false;
		String id=request.getParameter("id");
		
		String companyId=request.getParameter("companyId");
		String ifafirmId=request.getParameter("ifafirmId");
		
		//创建实体
		MemberCompanyIfafirm info;
		if(null==id||"".equals(id)){
			isAdd = true;
			info = new MemberCompanyIfafirm();
			info.setId(null);
		} else {
			isAdd = false;
			info = memberCompanyIfafirmService.findById(id);
		}
		if(StringUtils.isNotBlank(companyId)){
			CompanyInfo company = companyInfoService.findById(companyId);
			info.setCompany(company);
		}else{
			info.setCompany(null);
		}
		if(StringUtils.isNotBlank(ifafirmId)){
			MemberIfafirm ifafirm = memberIfafirmService.findById(ifafirmId);
			info.setIfafirm(ifafirm);
		}else{
			info.setIfafirm(null);
		}
		
		if(info.getCompany() != null && info.getIfafirm() != null) {
			info = memberCompanyIfafirmService.save(info, isAdd);
		}
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value = "/del", method = RequestMethod.POST)
	public void del(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String id=request.getParameter("id");		
		memberCompanyIfafirmService.deleteById(id);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
}
