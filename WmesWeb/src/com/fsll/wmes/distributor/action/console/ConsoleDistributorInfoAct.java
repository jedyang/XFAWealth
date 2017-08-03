/**
 * 
 */
package com.fsll.wmes.distributor.action.console;

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

import com.fsll.common.CommonConstants;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.ResponseUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.distributor.service.DistributorService;
import com.fsll.wmes.entity.DistributorOrg;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.vo.FundHouseDataVO;
import com.fsll.wmes.fund.vo.FundScreenerDataVO;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmVO;
import com.fsll.wmes.member.vo.MemberSsoVO;

/**
 * @author michael
 * 代理商信息管理
 */
@Controller
public class ConsoleDistributorInfoAct extends WmesBaseAct {

	@Autowired
    private FundInfoService fundInfoService;
	@Autowired
	private DistributorService distributorService;

	/**
	 * Distributor首页
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/console/distributor/info/home", method = RequestMethod.GET)
	public String ifaFirmHome(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "distributor/distributorHome";
	}

	 
	/**
	 * 代理商详情
	 * @author 林文伟 (modify qgfeng 2016-12-16)
	 * @date 2016-07-19
	 */
	@RequestMapping(value = "/console/distributor/info/distributorInfo")
	public String distributorInfo(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		//只允许ifaFirm管理员
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if(curAdmin==null || curAdmin.getDistributor()==null || curAdmin.getDistributor().getId()==null){
			return "redirect:"+this.getFullPath(request)+"front/logout.do";
		}
		
		model.put("distributor",distributorService.findDistributorById(curAdmin.getDistributor().getId()));
		return "console/distributor/distributorInfo";
	}
	
	/****
	 * 更新、保存代理商表单的方法(只做更新操作)
	 * @author qgfeng 
	 */
	@RequestMapping(value = "/console/distributor/saveInfo", method = RequestMethod.POST)
	public void saveDistributorInfo(MemberDistributor info,HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			MemberDistributor distributor = distributorService.findDistributorById(info.getId());
			if(distributor != null && distributor.getId()!=null){
//				distributor.setCompanyName(info.getCompanyName());
				distributor.setEntityType(info.getEntityType());
				distributor.setRegisterNo(info.getRegisterNo());
				distributor.setIsFinancial(info.getIsFinancial());
				distributor.setGiin(info.getGiin());
				distributor.setIncorporationPlace(info.getIncorporationPlace());
				distributor.setIncorporationDate(info.getIncorporationDate());
				distributor.setRegisteredAddress(info.getRegisteredAddress());
				distributor.setMailingAddress(info.getMailingAddress());
				distributor.setCountry(info.getCountry());
				distributor.setNaturePurpose(info.getNaturePurpose());
				distributor.setWebsite(info.getWebsite());
				info = distributorService.saveOrUpdateDistributor(distributor);
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result",result);
			JsonUtil.toWriter(obj, response);
		}
	}
	/**
	 * 跳转-当前代理商下关联的ifaFrim
	 * @author qgfeng 2016-12-16
	 * @date 2016-12-16
	 */
	@RequestMapping(value = "/console/distributor/relefirm")
	public String relefirm(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		return "console/distributor/rele_ifafirm";
	}
	
	/**
	 * 数据-当前代理商下关联的ifaFrim(分页)
	 * @author qgfeng
	 * @date 2016-12-13
	 */
	@RequestMapping(value = "/console/distributor/relefirmJson", method = RequestMethod.POST)
	public void relefirmJson(HttpServletRequest request, HttpServletResponse response,MemberIfafirmVO firmVo) {
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		firmVo.setDistributorId(curAdmin.getDistributor().getId());
		String langCode=this.getLoginLangFlag(request);
	    jsonPaging=this.getJsonPaging(request);
	    jsonPaging=distributorService.findReleIfafirm(jsonPaging,firmVo, langCode);
	    this.toJsonString(response, jsonPaging);
	}
	
	/****
	 * 移除ifa公司与代理商的关联关系
	 * @author qgfeng
	 * @date 2016-12-16
	 */
	@RequestMapping(value = "/console/distributor/delReleIfafirm", method = RequestMethod.POST)
	public void delReleIfafirm(MemberIfafirm info,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			String ifaFrimId=request.getParameter("ifaFrimId");
			MemberAdmin curAdmin = getLoginMemberAdmin(request);
			String distributorId = curAdmin.getDistributor().getId();
			result = distributorService.delReleIfafirm(distributorId,ifaFrimId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result",result);
			JsonUtil.toWriter(obj, response);
		}
	}
	
	/**
	 * 弹出窗口，显示代理商列表页面
	 * @author 林文伟
	 * @date 2016-07-11
	 */
	@RequestMapping(value = "/console/distributor/dialogList")
	public String dialogList(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		return "console/ifafirm/dialog_distributor_list";
	}
	
	/**
	 * 显示代理商部门树形列表页面
	 * @author 林文伟
	 * @date 2016-07-11
	 */
	@RequestMapping(value = "/console/distributor/org")
	public String orgList(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		String distributorId=request.getParameter("id");
		MemberDistributor memberDistributor = distributorService.findDistributorById(distributorId);
		if(null!=memberDistributor)
		{
			model.put("name", memberDistributor.getCompanyName());
			model.put("id", memberDistributor.getId());
		}
		return "console/distributor/org";
	}
	
	/**
	 * 获取代理商的组织架构数据，自己组装数据
	 * @author 林文伟
	 * @date 2016-07-12 
	 */
	@RequestMapping(value = "/console/distributor/listDistributorOrgJson", method = RequestMethod.POST)
	public void listDistributorOrgJson(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String distributorId=request.getParameter("distributorid");
		List<DistributorOrg> distributorOrgList = distributorService.findDistributorOrgAll(distributorId);
		String countryJson = JsonUtil.toJson(distributorOrgList);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("distributorOrgJson",countryJson);
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 代理商数据查询的方法（下拉填充数据）
	 * @author qgfeng
	 * @date 2016-12-5
	 */
	@RequestMapping(value = "/console/distributor/allList", method = RequestMethod.POST)
	public void listJsonIfafirm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		List<MemberDistributor> list = distributorService.findAllDistributor();
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result", true);
		obj.put("ifafirmJson", list);
		JsonUtil.toWriter(obj, response);
	}
	
	
}
