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
import com.fsll.core.vo.MenuTreeVO;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.company.service.CompanyDistributorService;
import com.fsll.wmes.company.service.CompanyInfoService;
import com.fsll.wmes.company.service.MemberCompanyIfafirmService;
import com.fsll.wmes.company.service.MemberCompanyService;
import com.fsll.wmes.company.vo.CompanyDistributorVO;
import com.fsll.wmes.company.vo.CompanyInfoVO;
import com.fsll.wmes.company.vo.MemberCompanyIfafirmVO;
import com.fsll.wmes.company.vo.MemberCompanyVO;
import com.fsll.wmes.entity.CompanyInfo;
import com.fsll.wmes.entity.CompanyInfoEn;
import com.fsll.wmes.entity.CompanyInfoSc;
import com.fsll.wmes.entity.CompanyInfoTc;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberCompany;
import com.fsll.wmes.entity.ProductDistributor;
import com.fsll.wmes.fund.vo.FundDistributorVO;
import com.fsll.wmes.member.service.MemberBaseService;

/**
 * 运营公司信息管理-公司用户管理（后台）
 * @author Yan
 * @version 1.0.0 Created On: 2016-12-05
 */

@Controller
@RequestMapping("/console/membercompany")
public class ConsoleMemberCompanyAct extends WmesBaseAct {
	
	@Autowired
	private MemberCompanyService memberCompanyService;
	@Autowired
	private CompanyInfoService companyInfoService;
	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
	private MemberCompanyIfafirmService memberCompanyIfafirmService;
	@Autowired
	private CompanyDistributorService companyDistributorService;
	
    /**
     * 分页列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
        return "console/company/membercompany/list";//页面摆放路径
    }
    
    /**
     * 分页获得记录
     */
    @RequestMapping(value = "/listJson", method = RequestMethod.POST)
    public String listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String langCode = this.getLoginLangFlag(request);
    	String companyName = request.getParameter("companyName");
    	
    	MemberCompanyVO infoVo = new MemberCompanyVO();
    	if(StringUtils.isNotBlank(companyName)){
    		infoVo.setCompanyName(companyName);
    	}
    	
        jsonPaging = this.getJsonPaging(request);
        jsonPaging = memberCompanyService.findAll(jsonPaging, infoVo, langCode);
        List<MemberCompanyVO> list = new ArrayList<MemberCompanyVO>();
        List<Object> objList = jsonPaging.getList();
        if (null!=objList && !objList.isEmpty())
        for (int i = 0; i < objList.size(); i++) {
			MemberCompanyVO vo = new MemberCompanyVO();
			Object[] objRead = (Object[])objList.get(i);
			MemberCompany info = (MemberCompany)objRead[0];
			if("sc".equals(langCode)){
				CompanyInfoSc infoSc = (CompanyInfoSc)objRead[1];
    			vo.setCompanyName(infoSc.getName());
			}
			if("tc".equals(langCode)){
				CompanyInfoTc infoTc = (CompanyInfoTc)objRead[1];
				vo.setCompanyName(infoTc.getName());
			}
			if("en".equals(langCode)){
				CompanyInfoEn infoEn = (CompanyInfoEn)objRead[1];
				vo.setCompanyName(infoEn.getName());
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
    	List<MemberBase> memberList = (List<MemberBase>)memberBaseService.findAllMember(0);
    	model.put("companyVoList", companyVoList);
    	model.put("memberList", memberList);
    	
    	if(!"".equals(id) && id!=null){
	        model.put("id", id);
	        MemberCompanyVO infoVo = (MemberCompanyVO)memberCompanyService.findVoById(id, langCode);
	        model.put("infoVo", infoVo);
    	}
        return "console/company/membercompany/input";
    }
    
    /**
     * 分页获得ifafirm记录
     */
    @RequestMapping(value = "/getIfaFirmList", method = RequestMethod.POST)
    public String getIfaFirmList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String langCode = this.getLoginLangFlag(request);
    	String companyId = request.getParameter("companyId");
    	List<MemberCompanyIfafirmVO> ifafirmList = memberCompanyIfafirmService.findList(companyId, langCode);
    	jsonPaging.setList(ifafirmList);
        this.toJsonString(response, jsonPaging);
        return null;
    }
    
    /**
     * 分页获得distributor记录
     */
    @RequestMapping(value = "/getDistributorList", method = RequestMethod.POST)
    public String getDistributorList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String langCode = this.getLoginLangFlag(request);
    	String companyId = request.getParameter("companyId");
    	List<CompanyDistributorVO> distributorList = companyDistributorService.findList(companyId, langCode);
    	jsonPaging.setList(distributorList);
        this.toJsonString(response, jsonPaging);
        return null;
    }
    
    /**
     * 分页获得member记录
     */
    @RequestMapping(value = "/getMemberList", method = RequestMethod.POST)
    public String getMemberList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String id = request.getParameter("id");
    	String type = request.getParameter("type");
    	if("ifafirm".equals(type)){
	    	List<MemberBase> memberList = memberCompanyIfafirmService.findMemberListByIfafirmId(id);
	    	jsonPaging.setList(memberList);
    	}else if("distributor".equals(type)){
    		List<MemberBase> distributorList = companyDistributorService.findMemberListByDistributorId(id);
    		jsonPaging.setList(distributorList);
    	}else if("investor".equals(type)){
    		List<MemberBase> investorList = memberBaseService.findAllMember(1);
    		jsonPaging.setList(investorList);
    	}
        this.toJsonString(response, jsonPaging);
        return null;
    }
    
	/**
	 * 新增/保存编辑
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void save(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		boolean isAdd =false;
		String id=request.getParameter("id");
		
		String companyId=request.getParameter("companyId");
		String memberList=request.getParameter("memberList");
		
		if(StringUtils.isNotBlank(memberList)){
			String[] memberIdList = memberList.split(",");
			for (int i = 0; i < memberIdList.length; i++) {
				String memberId = memberIdList[i];

				//创建实体
				MemberCompany info;
				if(null==id||"".equals(id)){
					isAdd = true;
					info = new MemberCompany();
					info.setId(null);
				} else {
					isAdd = false;
					info = memberCompanyService.findById(id);
				}
				if(StringUtils.isNotBlank(companyId)){
					CompanyInfo company = companyInfoService.findById(companyId);
					info.setCompany(company);
				}
				if(StringUtils.isNotBlank(memberId)){
					MemberBase member = memberBaseService.findById(memberId);
					info.setMember(member);
				}	
				info = memberCompanyService.save(info, isAdd);
			}
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
		memberCompanyService.deleteById(id);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
}
