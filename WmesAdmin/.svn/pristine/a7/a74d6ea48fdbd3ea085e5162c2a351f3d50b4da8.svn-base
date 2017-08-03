package com.fsll.wmes.company.action.console;

import java.util.ArrayList;
import java.util.Date;
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
import com.fsll.core.entity.SysAdmin;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.company.service.CompanyDistributorService;
import com.fsll.wmes.company.service.CompanyInfoService;
import com.fsll.wmes.company.vo.CompanyDistributorVO;
import com.fsll.wmes.distributor.service.MemberDistributorService;
import com.fsll.wmes.entity.CompanyDistributor;
import com.fsll.wmes.entity.CompanyInfo;
import com.fsll.wmes.entity.CompanyInfoEn;
import com.fsll.wmes.entity.CompanyInfoSc;
import com.fsll.wmes.entity.CompanyInfoTc;
import com.fsll.wmes.entity.MemberDistributor;

/**
 * 运营与代理商关系管理
 * @author Yan
 * @date 2017-01-20
 */
@Controller
@RequestMapping("/console/companydistributor")
public class ConsoleCompanyDistributorAct extends WmesBaseAct {

	@Autowired
	private CompanyInfoService companyInfoService;
	@Autowired
	private MemberDistributorService memberDistributorService;
	@Autowired
	private CompanyDistributorService companyDistributorService;
	
	/**
     * 分页列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
        SysAdmin admin = this.getLoginUser(request);
        if(null != admin){
            return "console/company/companydistributor/list";//页面摆放路径
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
    	String companyName = request.getParameter("companyName");
    	String distributorName = request.getParameter("distributorName");
    	
    	
    	//条件筛选
    	CompanyDistributorVO infoVo = new CompanyDistributorVO();
    	if(StringUtils.isNotBlank(companyName)){
    		infoVo.setCompanyName(companyName);
    	}
    	if(StringUtils.isNotBlank(distributorName)){
    		infoVo.setDistributorName(distributorName);
    	}
    	
        jsonPaging = this.getJsonPaging(request);
        jsonPaging = companyDistributorService.findAll(jsonPaging, infoVo, langCode);
        List<CompanyDistributorVO> list = new ArrayList<CompanyDistributorVO>();
        List<Object> objList = jsonPaging.getList();
        for (int i = 0; i < objList.size(); i++) {
        	CompanyDistributorVO vo = new CompanyDistributorVO();
			Object[] objRead = (Object[])objList.get(i);
			CompanyDistributor info = (CompanyDistributor)objRead[0];
			MemberDistributor distributor = (MemberDistributor)objRead[2];
			vo.setDistributorName(distributor.getCompanyName());
			//多语言获取信息
			if("sc".equals(langCode)){
				CompanyInfoSc infoSc = (CompanyInfoSc)objRead[1];
				if(!"".equals(infoSc) && null!=infoSc){
					vo.setCompanyName(infoSc.getName());
    			}
				
			}
			if("tc".equals(langCode)){
				CompanyInfoTc infoTc = (CompanyInfoTc)objRead[1];
				if(!"".equals(infoTc) && null!=infoTc){
					vo.setCompanyName(infoTc.getName());
				}
				
			}
			if("en".equals(langCode)){
				CompanyInfoEn infoEn = (CompanyInfoEn)objRead[1];
				if(!"".equals(infoEn) && null!=infoEn){
					vo.setCompanyName(infoEn.getName());
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
    	//获取运营企业CompanyInfo
    	List<CompanyInfo> companyVoList = companyInfoService.findAllCompany(langCode);    	
    	model.put("companyVoList", companyVoList);
    	//获取代理商MemberDistributor
    	List<MemberDistributor> distributorList = memberDistributorService.findAllDistributors();
    	model.put("distributorList", distributorList);
    	
    	if(!"".equals(id) && id!=null){
	        model.put("id", id);
	        CompanyDistributorVO infoVo = (CompanyDistributorVO)companyDistributorService.findVoById(id, langCode);
	        model.put("infoVo", infoVo);
    	}
        return "console/company/companydistributor/input";
    }
   
	/**
	 * 新增/保存编辑
	*/
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void save(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		boolean isAdd =false;
		String id=request.getParameter("id");		
		String companyId=request.getParameter("companyId");
		String distributorId=request.getParameter("distributorId");
		
		//创建实体
		CompanyDistributor info;
		if(null==id||"".equals(id)){
			isAdd = true;
			info = new CompanyDistributor();
			info.setId(null);
			info.setCreateTime(new Date());
		} else {
			isAdd = false;
			info = companyDistributorService.findById(id);
		}
		//运营公司
		if(StringUtils.isNotBlank(companyId)){
			CompanyInfo company = companyInfoService.findById(companyId);
			info.setCompany(company);
		}else{
			info.setCompany(null);
		}
		//代理公司
		if(StringUtils.isNotBlank(distributorId)){
			MemberDistributor distributor = memberDistributorService.findById(distributorId);
			info.setDistributor(distributor);
		}else{
			info.setDistributor(null);
		}
		
		info = companyDistributorService.save(info, isAdd);
			
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
		Map<String, Object> obj = new HashMap<String, Object>();
		String id=request.getParameter("id");
		if(StringUtils.isNotBlank(id)){
			companyDistributorService.deleteById(id);
			obj.put("result",true);
			obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		} else {
			obj.put("result", false);
		}
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	} 
}
