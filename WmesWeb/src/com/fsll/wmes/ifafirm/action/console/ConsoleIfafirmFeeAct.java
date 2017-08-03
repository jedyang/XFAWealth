package com.fsll.wmes.ifafirm.action.console;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.ResponseUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.core.service.SysParamTypeService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.entity.IfafirmFee;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberIfafirmEn;
import com.fsll.wmes.entity.MemberIfafirmSc;
import com.fsll.wmes.entity.MemberIfafirmTc;
import com.fsll.wmes.ifafirm.service.IfafirmFeeService;
import com.fsll.wmes.ifafirm.vo.IfafirmFeeVO;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.service.MemberIfafirmService;

/**
 * 控制器:产品代理费用（工作台）
 * @author Yan
 * @version 1.0.0 Created On: 2016-12-19
 */

@Controller
@RequestMapping("/console/ifafirm/fee")
public class ConsoleIfafirmFeeAct extends WmesBaseAct {
	
	@Autowired
	private IfafirmFeeService ifafirmFeeService;
	@Autowired
	private SysParamTypeService sysParamTypeService;
	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
	private MemberIfafirmService memberIfafirmService;

	
    /**
     * 分页列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
        //获取登录公司ID
    	MemberAdmin admin = this.getLoginMemberAdmin(request);
    	if(null!=admin && !"".equals(admin.getIfafirm())){
    		String ifafirmId = admin.getIfafirm().getId();
    		model.put("ifafirmId", ifafirmId);
            return "console/ifafirm/fee/fee_list";//页面摆放路径
    	} else{
    		return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
    	}
    }
    
    /**
     * 分页获得记录
     */
    @RequestMapping(value = "/listJson", method = RequestMethod.POST)
    public String listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	MemberAdmin admin = this.getLoginMemberAdmin(request);
    	if(null!=admin && !"".equals(admin.getIfafirm())){
    		String ifafirmId = admin.getIfafirm().getId();
    		model.put("ifafirmId", ifafirmId);

    		String langCode = this.getLoginLangFlag(request);
         	String name = request.getParameter("name");

            jsonPaging = ifafirmFeeService.getList(jsonPaging, ifafirmId, name,langCode,"");//查看所有状态
            List<Object> jsonList = jsonPaging.getList();
            List<Object> list = new ArrayList<Object>();
            if(!jsonList.isEmpty()){
            	for (int i = 0; i < jsonList.size(); i++) {
            		IfafirmFeeVO vo = new IfafirmFeeVO();
    				Object[] objRead = (Object[])jsonList.get(i);
    				IfafirmFee info = (IfafirmFee)objRead[0];
    				SysParamConfig config = (SysParamConfig)objRead[1];
    				MemberIfafirm ifafirm = (MemberIfafirm)objRead[2];
    				
    				if("sc".equals(langCode)){
    					MemberIfafirmSc sc = (MemberIfafirmSc)objRead[3];
    					vo.setIfafirmName(sc.getCompanyName());
    				} else if("tc".equals(langCode)){
    					MemberIfafirmTc tc = (MemberIfafirmTc)objRead[3];
    					vo.setIfafirmName(tc.getCompanyName());
    				} else if("en".equals(langCode)){
    					MemberIfafirmEn en = (MemberIfafirmEn)objRead[3];
    					vo.setIfafirmName(en.getCompanyName());
    				}
    				
    				vo.setId(info.getId());
    				vo.setIfafirmId(ifafirm.getId());
    				vo.setFeeType(info.getFeeType());				
    				vo.setFeeDefValue(info.getFeeDefValue());
    				vo.setFeeMin(info.getFeeMin());
    				vo.setFeeMax(info.getFeeMax());
    				vo.setCreateTime(info.getCreateTime());
    				vo.setIsValid(info.getIsValid());
    				
    				if("sc".equals(langCode) && null!=config){
    					vo.setFeeTypeName(config.getNameSc());
    				} else if("tc".equals(langCode) && null!=config){
    					vo.setFeeTypeName(config.getNameTc());
    				} else if("en".equals(langCode) && null!=config){
    					vo.setFeeTypeName(config.getNameEn());
    				}
    				list.add(vo);
    			}
            	jsonPaging.setList(list);
            }
            this.toJsonString(response, jsonPaging);
    	} else{
    		this.toJsonString(response, null);
    	}
    	
        return null;
    }
    
    /**
     * 详细信息页面
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	this.isMobileDevice(request,model);
    	String langCode = this.getLoginLangFlag(request);
    	MemberAdmin admin = this.getLoginMemberAdmin(request);
    	if(null!=admin && !"".equals(admin.getIfafirm())){
    		MemberIfafirm ifafirm = memberIfafirmService.findById(admin.getIfafirm().getId());
			String name = memberIfafirmService.getName(ifafirm.getId(), langCode);
    		model.put("name", name);
    	}
    	
//    	List<MemberBase> list = memberBaseService.findListByMemberType(1);
//    	model.put("list", list);
    	
    	String id = request.getParameter("id");
    	IfafirmFee info = ifafirmFeeService.findById(id);
    	if (null==info){
    		info = new IfafirmFee();
    		info.setIsValid("0");
    	}
		List<SysParamConfig> configList = sysParamTypeService.getChildren("ifafirm_fee_type");
    	model.put("info", info);
    	model.put("configList", configList);
        return "console/ifafirm/fee/fee_input";
    }
    
    /**
     * 
     * @param request
     * @param response
     * @param model
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void save(HttpServletRequest request,HttpServletResponse response,ModelMap model, IfafirmFee fee ) {
		boolean isAdd =false;
		/*
		String id = request.getParameter("id");
		String feeType = request.getParameter("feeType");
		String feeDefValue = request.getParameter("feeDefValue");
		String feeMin = request.getParameter("feeMin");
		String feeMax = request.getParameter("feeMax");*/
		
//    	IfafirmFee fee = new IfafirmFee();
    	if(StrUtils.isEmpty(fee.getId())){
			isAdd=true;
			fee.setId(null);
			fee.setIsValid("1");
		}
    	if (null==fee.getCreateTime()) fee.setCreateTime(new Date());
    	MemberAdmin admin = this.getLoginMemberAdmin(request);
		if(null!=admin && !"".equals(admin.getIfafirm())){
			MemberIfafirm ifafirm = admin.getIfafirm();
			fee.setIfafirm(ifafirm);
    	}
		fee.setLastUpdate(new Date());
		/*
		if(StringUtils.isNotBlank(feeType))
			fee.setFeeType(feeType);
		if(StringUtils.isNotBlank(feeDefValue))
			fee.setFeeDefValue(Double.valueOf(feeDefValue));
		if(StringUtils.isNotBlank(feeMax))
			fee.setFeeMax(Double.valueOf(feeMax));
		if(StringUtils.isNotBlank(feeMin))
			fee.setFeeMin(Double.valueOf(feeMin));
		
		fee.setLastUpdate(new Date());
		fee.setIsValid("1");*/
		
		ifafirmFeeService.saveOrUpdate(fee, isAdd);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/****
	 * 删除
	 */
	@RequestMapping(value = "/del", method = RequestMethod.POST)
	public void del(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String id=request.getParameter("id");		
		ifafirmFeeService.deleteById(id);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
}
