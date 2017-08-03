package com.fsll.wmes.ifafirm.action.console;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.ChineseToEnglish;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.ResponseUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.wmes.entity.IfafirmTeam;
import com.fsll.wmes.entity.IfafirmTeamIfa;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberIfafirmEn;
import com.fsll.wmes.entity.MemberIfafirmSc;
import com.fsll.wmes.entity.MemberIfafirmTc;
import com.fsll.wmes.ifa.vo.AutoCompleteVO;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmVO;
import com.fsll.wmes.web.vo.IfafirmTeamVO;

/*****
 * IfaFirm管理
 * @author 林文伟
 * 2016-06-23
 */
@Controller
public class IfafirmManageAct extends CoreBaseAct {
	
	@Autowired
	private IfafirmManageService ifafirmService;
	
	/**
	 * IFAFirm 分页列表
	 * @author 林文伟   (modify qgfeng)
	 * @date 2016-07-01
	 */
	@RequestMapping(value = "/console/ifafirm/list")
	public String list(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		return "console/ifafirm/list";
	}
	
	/**
	 * 获取IFAFirm 列表数据
	 * @author qgfeng	modify by rqwang 20170615
	 * @date 2016-11-10
	 */
	@RequestMapping(value = "/console/ifafirm/ifaFirmJson")
	public void ifaFirmJson(HttpServletRequest request,HttpServletResponse response,MemberIfafirmVO vo) {
		jsonPaging = this.getJsonPaging(request);
		String  langFlag = this.getLoginLangFlag(request);
		jsonPaging=ifafirmService.getIfaFirmJson(jsonPaging ,vo,langFlag);
		this.toJsonString(response, jsonPaging);
	}
	
	
	/**
	 * 获取代理商下的IFAFirm 分页列表
	 * @author rqwang
	 * @date 2017-06-07
	 */
	@RequestMapping(value = "/console/ifafirm/disIfafirmlist")
	public String disIfafirmlist(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		String id = request.getParameter("id");
		model.put("id", id);
		return "console/distributor/dis_ifaFirm_list";
	}	

	/**
	 * 获取代理商下的IFAFirm 列表数据
	 * @author rqwang
	 * @date 2017-06-07
	 */
	@RequestMapping(value = "/console/ifafirm/disifaFirmJson")
	public void disIfafirmlistJson(HttpServletRequest request,HttpServletResponse response,MemberIfafirmVO vo) {
		String id = request.getParameter("id");
		jsonPaging = this.getJsonPaging(request);
		String  langFlag = this.getLoginLangFlag(request);
		jsonPaging=ifafirmService.getdisIfaFirmJson(jsonPaging ,id,vo,langFlag);
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 代理商的IFAFirm详情页面
	 * @author rqwang
	 * @date 2017-06-08
	 */
	@RequestMapping(value = "/console/ifafirm/disInput")
	public String disifaFirmDetail(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String ifafirmId = request.getParameter("ifafirmId");
    	MemberIfafirmVO memberIfafirmVO = new MemberIfafirmVO();
        if(StringUtils.isNoneEmpty(ifafirmId)){
        	MemberIfafirm ifafirm = ifafirmService.findById(ifafirmId,null);
        	BeanUtils.copyProperties(ifafirm, memberIfafirmVO);
        	
        	MemberIfafirmSc ifafirmSc = ifafirmService.findIfafirmScById(ifafirmId);
        	MemberIfafirmTc ifafirmTc = ifafirmService.findIfafirmTcById(ifafirmId);
        	MemberIfafirmEn ifafirmEn = ifafirmService.findIfafirmEnById(ifafirmId);
        	BeanUtils.copyProperties(ifafirm, memberIfafirmVO);
        	if(null != ifafirmSc){
        		memberIfafirmVO.setCompanyNameSc(ifafirmSc.getCompanyName());
        	}
        	if(null != ifafirmTc){
        		memberIfafirmVO.setCompanyNameTc(ifafirmTc.getCompanyName());
        	}
        	if(null != ifafirmEn){
        		memberIfafirmVO.setCompanyNameEn(ifafirmEn.getCompanyName());
        	}
        }
        model.put("ifafirm", memberIfafirmVO);
		return "console/distributor/dis_firm_info";
	}	
	
	/**
	 * ifa_ifafirm数据查询的方法（下拉填充数据）
	 * @author qgfeng
	 * @date 2016-12-5
	 */
	@RequestMapping(value = "/console/ifafirm/allList", method = RequestMethod.POST)
	public void listJsonIfafirm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode = this.getLoginLangFlag(request);
		List<MemberIfafirmVO> list = ifafirmService.getIfafirmlist(langCode);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result", true);
		obj.put("ifafirmJson", list);
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 新增、详情跳转
	 * @author wwluo （modify qgfeng）
	 * @date 2016-08-22
	 */
/*	@RequestMapping(value = "/console/ifafirm/input")
	public String ifaFirmDetail(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String ifafirmId = request.getParameter("ifafirmId");
    	MemberIfafirmVO memberIfafirmVO = new MemberIfafirmVO();
        if(StringUtils.isNoneEmpty(ifafirmId)){
        	MemberIfafirm ifafirm = ifafirmService.findById(ifafirmId,null);
        	BeanUtils.copyProperties(ifafirm, memberIfafirmVO);
        	
        	MemberIfafirmSc ifafirmSc = ifafirmService.findIfafirmScById(ifafirmId);
        	MemberIfafirmTc ifafirmTc = ifafirmService.findIfafirmTcById(ifafirmId);
        	MemberIfafirmEn ifafirmEn = ifafirmService.findIfafirmEnById(ifafirmId);
        	BeanUtils.copyProperties(ifafirm, memberIfafirmVO);
        	if(null != ifafirmSc){
        		memberIfafirmVO.setCompanyNameSc(ifafirmSc.getCompanyName());
        	}
        	if(null != ifafirmTc){
        		memberIfafirmVO.setCompanyNameTc(ifafirmTc.getCompanyName());
        	}
        	if(null != ifafirmEn){
        		memberIfafirmVO.setCompanyNameEn(ifafirmEn.getCompanyName());
        	}
        }
        model.put("ifafirm", memberIfafirmVO);
		return "console/ifafirm/firm_input";
	}*/
	

	@RequestMapping(value = "/console/ifafirm/input")
	public String ifaFirmDetail(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String ifafirmId = request.getParameter("ifafirmId");
    	MemberIfafirmVO memberIfafirmVO = new MemberIfafirmVO();
        if(StringUtils.isNoneEmpty(ifafirmId)){
        	MemberIfafirm ifafirm = ifafirmService.findById(ifafirmId,null);
        	BeanUtils.copyProperties(ifafirm, memberIfafirmVO);
        	
        	MemberIfafirmSc ifafirmSc = ifafirmService.findIfafirmScById(ifafirmId);
        	MemberIfafirmTc ifafirmTc = ifafirmService.findIfafirmTcById(ifafirmId);
        	MemberIfafirmEn ifafirmEn = ifafirmService.findIfafirmEnById(ifafirmId);
        	BeanUtils.copyProperties(ifafirm, memberIfafirmVO);
        	if(null != ifafirmSc){
        		memberIfafirmVO.setCompanyNameSc(ifafirmSc.getCompanyName());
        	}
        	if(null != ifafirmTc){
        		memberIfafirmVO.setCompanyNameTc(ifafirmTc.getCompanyName());
        	}
        	if(null != ifafirmEn){
        		memberIfafirmVO.setCompanyNameEn(ifafirmEn.getCompanyName());
        	}
        }
        model.put("ifafirm", memberIfafirmVO);
		//return "console/ifafirm/firm_info";
		return "console/ifafirm/firm_input";
	}
	
	/****
	 * 保存IFAFirm （modify qgfeng）
	 * @date 2016-3-18
	 */
	@RequestMapping(value = "/console/ifafirm/savefirminfo", method = RequestMethod.POST)
	public void saveFirmInfo(HttpServletRequest request,HttpServletResponse response,MemberIfafirmVO firmVO) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			boolean isAdd =false;
			//保存MemberIfafirm
			MemberIfafirm firm = ifafirmService.findById(firmVO.getId(), null);
			if(null==firm){
				firm = new MemberIfafirm();
				firm.setIsValid("1");
				isAdd = true;
			}
			firm.setEntityType(firmVO.getEntityType());
			firm.setRegisterNo(firmVO.getRegisterNo());
			firm.setIsFinancial(firmVO.getIsFinancial());
			firm.setGiin(firmVO.getGiin());
			firm.setNaturePurpose(firmVO.getNaturePurpose());
			firm.setIncorporationDate(firmVO.getIncorporationDate());
			firm.setIncorporationPlace(firmVO.getIncorporationPlace());
			firm.setRegisteredAddress(firmVO.getRegisteredAddress());
			firm.setMailingAddress(firmVO.getMailingAddress());
			firm.setCountry(firmVO.getCountry());
			firm.setWebsite(firmVO.getWebsite());
			firm.setFirmLogo(firmVO.getFirmLogo());
			
			firm = ifafirmService.saveOrUpdate(firm, isAdd);
			//保存对应多语言
			if(firm != null && StringUtils.isNotBlank(firm.getId())){
				MemberIfafirmEn firmEn = ifafirmService.findIfafirmEnById(firm.getId());
				MemberIfafirmSc firmSc = ifafirmService.findIfafirmScById(firm.getId());
				MemberIfafirmTc firmTc = ifafirmService.findIfafirmTcById(firm.getId());
				if(firmEn==null){
					firmEn = new MemberIfafirmEn();
				}
				if(firmSc==null){
					firmSc = new MemberIfafirmSc();
				}
				if(firmTc==null){
					firmTc = new MemberIfafirmTc();
				}
				String pinYin = ChineseToEnglish.getPingYin(firmVO.getCompanyNameSc());
				firmEn.setId(firm.getId());
				firmEn.setPinYin(pinYin);
				firmEn.setCompanyName(firmVO.getCompanyNameEn());
				
				firmSc.setId(firm.getId());
				firmSc.setPinYin(pinYin);
				firmSc.setCompanyName(firmVO.getCompanyNameSc());
				
				firmTc.setId(firm.getId());
				firmTc.setPinYin(pinYin);
				firmTc.setCompanyName(firmVO.getCompanyNameTc());
				firmSc = ifafirmService.saveOrUpdateFirmSc(firmSc);
				firmTc = ifafirmService.saveOrUpdateFirmTc(firmTc);
				firmEn = ifafirmService.saveOrUpdateFirmEn(firmEn);
				result= true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result",result);
			JsonUtil.toWriter(obj, response);
		}
	}
	
	/**
	 * 删除IFAFirm
	 * @author wwluo （modify qgfeng）
	 * @date 2016-08-22
	 */
	@RequestMapping(value = "/console/ifafirm/delifafirm")
	public void deleteIfafirm(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String ifafirmId = request.getParameter("ifafirmId");
		Map<String, Object> map = new HashMap<String, Object>();
		boolean result = false;
		try {
			result = ifafirmService.deleteById(ifafirmId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			map.put("result", result);
			JsonUtil.toWriter(map, response);
		}
	}
	//-----end MemberIfafirm action----
	
	/**
	 * 跳转组织架构
	 * @author wwluo
	 * @date 2016-08-25
	 */
	@RequestMapping(value = "/console/ifafirm/orgList")
	public String orgList(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		String ifafirmId = request.getParameter("ifafirmId");	
		MemberIfafirm memberIfafirm = ifafirmService.findById(ifafirmId,getLoginLangFlag(request));
		if(null != memberIfafirm){
			model.put("companyName",memberIfafirm.getCompanyName());
		}
		model.put("ifafirmId",ifafirmId);
		return "console/ifafirm/firm_org";
	}
	
	/**
	 * IFAFirmTeam获取公司的团队数据
	 * @author 林文伟
	 * @date 2016-03-17 
	 */
	@RequestMapping(value = "/console/ifafirm/teamJson", method = RequestMethod.POST)
	public void teamJson(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String ifafirmid=request.getParameter("ifafirmid");
		IfafirmTeam info=new IfafirmTeam();
		MemberIfafirm firm = new MemberIfafirm();
		firm.setId(ifafirmid);
		info.setIfafirm(firm);
		
		List<IfafirmTeam> teamList = ifafirmService.findTeamAll(info);
		String teamJson = JsonUtil.toJson(teamList);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("teamJson",teamJson);
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 弹出添加,编辑团队窗口
	 * @author rqwang
	 * @date 2017-05-23
	 */
	@RequestMapping(value = "/console/ifafirm/inputTeam")
	public String inputTeam(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		String id=request.getParameter("id");
		if(StrUtils.isEmpty(id)){
			String ifafirmid = request.getParameter("ifafirmid");
			String parentteamid = request.getParameter("parentteamid");
			String classlayer = request.getParameter("classlayer");
			String childclasslayer = request.getParameter("childclasslayer");
			model.put("ifafirmid", ifafirmid);
			model.put("parentteamid", parentteamid);
			model.put("classlayer", classlayer);
			model.put("childclasslayer", childclasslayer);
		}else {
			String name=request.getParameter("name");
			String code=request.getParameter("code");
			String reamrk=request.getParameter("reamrk");
			String orderBy=request.getParameter("orderBy");
			 try {
				name = URLDecoder.decode(name,"utf-8");
				code = URLDecoder.decode(code,"utf-8");
				reamrk = URLDecoder.decode(reamrk,"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			model.put("id", id);
			model.put("name", name);
			model.put("code", code);
			model.put("reamrk", reamrk);
			model.put("orderBy", orderBy);
		}
		return "console/ifafirm/firm_org_input";
	}
	
	/**
	 * IFAFirmTeam 新增、编辑团队节点
	 * @author qgfeng
	 * @date 2016-11-15
	 */
	@RequestMapping(value="/console/ifafirm/addTeam")
	public void addTeam(IfafirmTeam info,HttpServletRequest request,HttpServletResponse response,ModelMap model){
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			if (null!=info.getParent() && StrUtils.isEmpty(info.getParent().getId())){
				info.setParent(null);
			}
			if(StringUtils.isNoneBlank(info.getId())){
				//edit
				IfafirmTeam team = ifafirmService.getIfafirmTeam(info.getId());
				if(team!=null){
					team.setName(info.getName());
					team.setCode(info.getCode());
					team.setReamrk(info.getReamrk());
					team.setOrderBy(info.getOrderBy());
					ifafirmService.saveOrUpdateTeam(team, false);
				}
			}else{
				//add
				info.setId(null);
				info.setIsValid("1");
				info = ifafirmService.saveOrUpdateTeam(info, true);
			}
			obj.put("teamId", info.getId());
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result",result);
			JsonUtil.toWriter(obj, response);
		}
	}
	
	/**
	 * IFAFirmTeam 删除节点(同时要删除子团队跟所有团队的成员)
	 * @author qgfeng
	 * @date 2016-11-15
	 */
	@RequestMapping(value = "/console/ifafirm/delTeam", method = RequestMethod.POST)
	public void delTeam(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			String id=request.getParameter("id");
			result = ifafirmService.delIfafirmTeam(id);
			if(!result){
				obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"ifafirm.team.delete.tip1",null));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result",result);
			JsonUtil.toWriter(obj, response);
		}
	}
	
	/**
	 * IfafirmTeamIfa 弹出团队信息窗口
	 * @author 林文伟
	 * @date 2016-07-01
	 */
	@RequestMapping(value = "/console/ifafirm/dialogTeamShow")
	public String dialogTeamInfoShow(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		return "console/ifafirm/dialog_team_form";
	}
	
	//------end fafirmTeam action ------
	
	/**
	 * IfafirmTeamIfa 弹出团队成员管理窗口
	 * @author 林文伟
	 * @date 2016-07-01
	 */
	@RequestMapping(value = "/console/ifafirm/teamUser")
	public String teamMemberIfa(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		String teamid=request.getParameter("teamid");
		String ifafirmid=request.getParameter("ifafirmid");
		model.put("teamid", teamid);
		model.put("ifafirmid", ifafirmid);
		return "console/ifafirm/dialog_team_user";
	}
	
	/**
	 * IfafirmTeamIfa 通过公司与团队ID获取其所有成员数据
	 * @author qgfeng
	 * */
	@RequestMapping(value="/console/ifafirm/teamUserJson")
	public void teamUserJson(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String keywords=request.getParameter("keywords");
		String teamid=request.getParameter("teamid");
		String ifafirmid=request.getParameter("ifafirmid");
		jsonPaging=this.getJsonPaging(request);
		jsonPaging=ifafirmService.findAllTeamIfa(jsonPaging, ifafirmid,teamid,keywords);
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * IfafirmTeamIfa 删除团队成员
	 * delIfafirmTeamIfa
	 * @author qgfeng
	 * @date 2016-11-17
	 */
	@RequestMapping(value = "/console/ifafirm/delIfafirmTeamIfa", method = RequestMethod.POST)
	public void delIfafirmTeamIfa(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String id = request.getParameter("id");
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			result = ifafirmService.delIfafirmTeamIfa(id);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result",result);
			JsonUtil.toWriter(obj, response);
		}
	}
	
	/****
	 * IfafirmTeamIfa 启动与取消团队成员的sv操作
	 * @author qgfeng
	 * @date 2016-3-18
	 */
	@RequestMapping(value = "/console/ifafirm/dealSupervisor", method = RequestMethod.POST)
	public void dealSupervisor(MemberIfafirm info,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String id=request.getParameter("id");
		ifafirmService.mergeSupervisor(id);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/**
	 * 弹出团队成员管理窗口
	 * @author zxtan
	 * @date 2016-12-07
	 */
	@RequestMapping(value = "/console/ifafirm/teamUserSelect")
	public String teamUserSelect(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		String teamid=request.getParameter("teamid");
		String ifafirmid=request.getParameter("ifafirmid");
		model.put("teamid", teamid);
		model.put("ifafirmid", ifafirmid);
		return "console/ifafirm/firm_team_user_select";
	}
	
	/**
	 * 通过输入一个KEY获取IFA数据，用于团队成员选择窗口
	 * */
	@RequestMapping(value="/console/ifafirm/teamUserSelectJson")
	public void getIFAListForSelect(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String ifafirmId=request.getParameter("ifafirmid");
		String teamId=request.getParameter("teamid");
		String keyword=request.getParameter("keyword");
		jsonPaging=this.getJsonPaging(request);
		jsonPaging = ifafirmService.getIFAListForSelect(jsonPaging,ifafirmId, teamId, keyword);
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * IfafirmTeamIfa 给团队添加成员
	 * @author qgfeng
	 * @date 2017-1-12
	 */
	@RequestMapping(value="/console/ifafirm/addTeamMemberIfa")
	public void addTeamMemberIfa(HttpServletRequest request,HttpServletResponse response){
		String ifaIds = request.getParameter("ifaIds");
		String teamid = request.getParameter("teamid");
		String ifafirmid = request.getParameter("ifafirmid");
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			String[] ifaIdArry = ifaIds.split(",");
			if(ifaIdArry != null && ifaIdArry.length > 0){
				for(int i=0;i<ifaIdArry.length;i++){
					String ifaid = ifaIdArry[i];
					//判断该成员是否存在在该团队中
					IfafirmTeamIfa ifafirmTeamIfa = ifafirmService.getIfaIsExistInTeam(ifafirmid, teamid, ifaid);
					if(null==ifafirmTeamIfa)
					{
						IfafirmTeamIfa info = new IfafirmTeamIfa();
						info.setId(null);
						MemberIfafirm memberIfafirm = new MemberIfafirm();
						memberIfafirm.setId(ifafirmid);
						info.setIfafirm(memberIfafirm);

						IfafirmTeam ifafirmTeam1 = new IfafirmTeam();
						ifafirmTeam1.setId(teamid);
						info.setTeam(ifafirmTeam1);
						
						MemberIfa memberIfa = new MemberIfa();
						memberIfa.setId(ifaid);
						info.setIfa(memberIfa);
						info.setIsSupervisor("0");
						info=ifafirmService.saveOrUpdateTeamIfa(info, true);
						obj.put("result",true);
						obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
					} 
				}
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result",result);
			ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
		}
	}
	
	//-----end IfafirmTeamIfa action----
	
	/**
	 * IfafirmDistributor 跳转代理商
	 * @author wwluo modify by rqwang 20170607
	 * @date 2016-08-25
	 */
	@RequestMapping(value = "/console/ifafirm/distributorList")
	public String distributorList(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		String ifafirmId = request.getParameter("ifafirmId");
		MemberIfafirm memberIfafirm = ifafirmService.findById(ifafirmId,getLoginLangFlag(request));
		if(null != memberIfafirm){
			model.put("companyName",memberIfafirm.getCompanyName());
		}
		model.put("ifafirmId",ifafirmId);
		return "console/ifafirm/firm_distributor";
	}
	
	/**
	 * IfafirmDistributor 代理商列表查询的方法
	 * @author 林文伟	modify by rqwang 20170607
	 * @date 2016-03-17 
	 */
	@RequestMapping(value = "/console/ifafirm/distributorListJson", method = RequestMethod.POST)
	public void findIfafirmDistributorListJson(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String ifafirmId=request.getParameter("ifafirmid");
		jsonPaging=this.getJsonPaging(request);
		jsonPaging=ifafirmService.findIfafirmDistributorList(jsonPaging, ifafirmId,request);
		this.toJsonString(response, jsonPaging);
	}
	
	/****
	 * IfafirmDistributor 删除公司与代理商的关联关系
	 * @author 
	 * @date 2016-3-18
	 */
	@RequestMapping(value = "/console/ifafirm/delIfafirmDistributorid", method = RequestMethod.POST)
	public void delIfafirmDistributorid(MemberIfafirm info,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String id=request.getParameter("id");
		ifafirmService.delIfafirmDistributorid(id);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/**
	 * IfafirmDistributor 弹出选择代理商
	 * @author 林文伟
	 * @date 2016-07-11
	 */
	@RequestMapping(value = "/console/ifafirm/selectDistributor")
	public String dialogList(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		return "console/ifafirm/dialog_distributor_list";
	}
	
	/****
	 * IfafirmDistributor 添加公司与代理商的关联关系
	 * @author 
	 * @date 2016-3-18
	 */
	@RequestMapping(value = "/console/ifafirm/addIfafirmDistributor", method = RequestMethod.POST)
	public void addIfafirmDistributor(MemberIfafirm info,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String distributorid=request.getParameter("distributorid");
		String ifafirmid=request.getParameter("ifafirmid");
		ifafirmService.addIfafirmDistributor(distributorid, ifafirmid);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	//-----end IfafirmDistributor action----
	
	/**
	 * IfafirmIfafirm跳转关联的firm页面(子公司)
	 * @author wwluo
	 * @date 2016-08-22
	 */
	@RequestMapping(value = "/console/ifafirm/ifafirmIfafirm")
	public String firmFirmList(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String ifafirmId = request.getParameter("ifafirmId");
		MemberIfafirm memberIfafirm = ifafirmService.findById(ifafirmId,getLoginLangFlag(request));
		if(null != memberIfafirm){
			model.put("companyName",memberIfafirm.getCompanyName());
		}
		model.put("ifafirmId", ifafirmId);
		return "console/ifafirm/firm_firm";
	}
	
	/**
	 * IfafirmIfafirm 获取关联的firm数据(子公司)
	 * @author wwluo
	 * @date 2016-08-22
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/console/ifafirm/ifafirmIfafirmJson")
	public void relevanceFirmJson(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String ifafirmId = request.getParameter("ifafirmId");
		List<MemberIfafirmVO> list = ifafirmService.findIfafirmIfafirmByid(ifafirmId,getLoginLangFlag(request));
		Map map = new HashMap();
		map.put("ifafirmId", ifafirmId);
		map.put("ifafirmJson", list);
		JsonUtil.toWriter(map, response);
	}
	
	/**
	 * IfafirmIfafirm 增加公司关系（子公司）
	 * @author wwluo
	 * @date 2016-08-23
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/console/ifafirm/addChildFirm")
	public void addChildFirm(HttpServletRequest request,
			HttpServletResponse response,ModelMap model) {
		String parentId = request.getParameter("parentId");	
		String childId = request.getParameter("childId");	
		boolean flag = false;
		flag = ifafirmService.saveChildFirm(parentId,childId);
		Map map = new HashMap();
		map.put("flag", flag);
		map.put("parentId", parentId);
		map.put("childId", childId);
		JsonUtil.toWriter(map, response);
	}
	
	/**
	 * IfafirmIfafirm移除公司关系（子公司）
	 * @author wwluo
	 * @date 2016-08-23
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/console/ifafirm/removeChildFirm")
	public void removeChildFirm(HttpServletRequest request,
			HttpServletResponse response,ModelMap model) {
		String parentId = request.getParameter("parentId");	
		String childId = request.getParameter("childId");	
		boolean flag = false;
		flag = ifafirmService.removeChildFirm(parentId,childId);
		Map map = new HashMap();
		map.put("flag", flag);
		map.put("parentId", parentId);
		map.put("childId", childId);
		JsonUtil.toWriter(map, response);
	}

	/**
	 * IfafirmIfafirm 弹出选择子公司窗体
	 * @author 林文伟
	 * @date 2016-07-01
	 */
	@RequestMapping(value = "/console/ifafirm/dialogFirmShow")
	public String dialogFirmShow(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		return "console/ifafirm/dialog_firm";
	}
	
	/**
	 * 在弹出的公司查询窗体中数据查询的方法
	 * @author 林文伟
	 * @date 2016-03-17 
	 */
	@RequestMapping(value = "/console/ifafirm/listfirmJson", method = RequestMethod.POST)
	public void listFirmJson(HttpServletRequest request,HttpServletResponse response,MemberIfafirmVO vo) {
		String companyName=request.getParameter("companyname");
		String entityType=request.getParameter("entitytype");
		String incorporationPlace=request.getParameter("incorporationplace");
		String registerNo=request.getParameter("registerno");
		String isFinancial = request.getParameter("isFinancial");
		vo.setCompanyName(companyName);
		vo.setEntityType(entityType);
		vo.setIncorporationPlace(incorporationPlace);
		vo.setRegisterNo(registerNo);
		vo.setIsFinancial(isFinancial);
		jsonPaging = this.getJsonPaging(request);
		jsonPaging=ifafirmService.getIfaFirmJson(jsonPaging ,vo,getLoginLangFlag(request));
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 弹出公司信息窗体
	 * @author 林文伟
	 * @date 2016-07-01
	 */
	@RequestMapping(value = "/console/ifafirm/dialogFirmInfo")
	public String dialogFirmInfo(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		return "console/ifafirm/dialog_firm_form";
	}
	
	/**
	 * 通过输入一个KEY获取IFA数据，用于团队成员选择窗口
	 * */
	@RequestMapping(value="/console/ifafirm/listKeyToIFAJson")
	public void getIFAKeyList(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String ifafirmid=request.getParameter("ifafirmid");
		String keywords=request.getParameter("keywords");
		List<MemberIfa> list = ifafirmService.getIFAKeyList(ifafirmid,keywords);
		String ifaJson = JsonUtil.toJson(list);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("ifaJson",ifaJson);
		JsonUtil.toWriter(obj, response);
	}
	
	/****
	 * 保存用户表单的方法
	 * @author 
	 * @date 2016-3-18
	 * @param request 用户请求对象
	 * @param response 用户请求对象
	 * @param model 存储对象
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/console/ifafirm/saveallteaminfo", method = RequestMethod.POST)
	public void saveAllTeamInfo(IfafirmTeam info,HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			boolean isAdd =false;
			String json = StringEscapeUtils.unescapeHtml(request.getParameter("json"));
			if(StringUtils.isBlank(json)){
				return ;
			}
	        JSONArray jsonArray = JSONArray.fromObject(json);
	        List<IfafirmTeamVO>	 list = new ArrayList<IfafirmTeamVO>();
	        for (int i = 0; i < jsonArray.size(); i++) {
	            JSONObject jsonObject = jsonArray.getJSONObject(i);
	            IfafirmTeamVO vo = (IfafirmTeamVO) JSONObject.toBean(jsonObject, IfafirmTeamVO.class);
	            list.add(vo);
	        }
			if(!list.isEmpty())
			{
				for(IfafirmTeamVO vo : list)
				{
					String name = vo.getName();
					String code = vo.getcode();
					int orderBy = vo.getOrderby();
					int classlayer = vo.getClasslayer();
					String ifafirmid = vo.getIfafirmid();
					String id = vo.getId();
					String parentid = vo.getParentid();
					//判断该团队是否存在，存在则更新，否则新增
					IfafirmTeam team = ifafirmService.getIfafirmTeam(id);
					if(null!=team)
					{
						if(null!=team.getId()&&!"".endsWith(team.getId()))
						{
							team.setId(id);
							team.setCode(code);
							team.setName(name);
							team.setOrderBy(orderBy);
							team.setClassLayer(classlayer);
							
							MemberIfafirm memberIfafirm = new MemberIfafirm();
							memberIfafirm.setId(ifafirmid);
							info.setIfafirm(memberIfafirm);
							
							IfafirmTeam ifafirmTeam1 = new IfafirmTeam();
							ifafirmTeam1.setId(parentid);
							info.setParent(ifafirmTeam1);
							isAdd = false;
							team=ifafirmService.saveOrUpdateTeam(team,isAdd);
						}
					}
					else 
					{
						IfafirmTeam team1 = new IfafirmTeam();
						isAdd = true;
						team1.setId(null);
						team1.setCode(code);
						team1.setName(name);
						team1.setOrderBy(orderBy);
						team1.setClassLayer(classlayer);
						
						MemberIfafirm memberIfafirm = new MemberIfafirm();
						memberIfafirm.setId(ifafirmid);
						info.setIfafirm(memberIfafirm);

						IfafirmTeam ifafirmTeam1 = new IfafirmTeam();
						ifafirmTeam1.setId(parentid);
						info.setParent(ifafirmTeam1);
						team1=ifafirmService.saveOrUpdateTeam(team1,isAdd);
					}
				}
			}
			result = true;
			obj.put("ifaJson",list);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result",result);
			JsonUtil.toWriter(obj, response);
		}
	}
	
	/**
	 * ifafirm 分页列表
	 * @author wwluo
	 * @date 2016-08-22
	 */
	@RequestMapping(value = "/console/ifafirm/ifaFirmList")
	public String ifaFirmList(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		return "console/ifafirm/firm_List";
	}
	
	/**
	 * 获取自动填充查询条件列表(IFA)
	 * @author mqzou
	 * @date 2016-08-19
	 */
	@RequestMapping(value = "/console/ifafirm/autoIfafirmList", method = RequestMethod.POST)
	public void autoCompleteIfafirm(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String keyWord=request.getParameter("keyWord");
		String langCode = getLoginLangFlag(request);
		List<AutoCompleteVO> list = null;
		if(StringUtils.isNotBlank(keyWord)){
			list=ifafirmService.findAutoIfafirm(keyWord,langCode);
		}
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("list", list);
		JsonUtil.toWriter(obj,response);
	}
	
	/**
	 * 获取IFAFirm 列表数据
	 * @author wwluo
	 * @date 2017-06-06
	 */
	@RequestMapping(value = "/console/ifafirm/getIfaFirms")
	public void getIfaFirms(HttpServletRequest request,HttpServletResponse response,MemberIfafirmVO vo) {
		String langCode = this.getLoginLangFlag(request);
		List<MemberIfafirmVO> ifafirms = ifafirmService.getIfaFirms(langCode);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("ifafirms", ifafirms);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * @author rqwang
	 * @date 2017-06-19
	 */
	@RequestMapping(value = "/console/ifafirm/firmInfo")
	public String ifaFirmInfo(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String ifafirmId = request.getParameter("ifafirmId");
    	MemberIfafirmVO memberIfafirmVO = new MemberIfafirmVO();
        if(StringUtils.isNoneEmpty(ifafirmId)){
        	MemberIfafirm ifafirm = ifafirmService.findById(ifafirmId,null);
        	BeanUtils.copyProperties(ifafirm, memberIfafirmVO);
        	
        	MemberIfafirmSc ifafirmSc = ifafirmService.findIfafirmScById(ifafirmId);
        	MemberIfafirmTc ifafirmTc = ifafirmService.findIfafirmTcById(ifafirmId);
        	MemberIfafirmEn ifafirmEn = ifafirmService.findIfafirmEnById(ifafirmId);
        	BeanUtils.copyProperties(ifafirm, memberIfafirmVO);
        	if(null != ifafirmSc){
        		memberIfafirmVO.setCompanyNameSc(ifafirmSc.getCompanyName());
        	}
        	if(null != ifafirmTc){
        		memberIfafirmVO.setCompanyNameTc(ifafirmTc.getCompanyName());
        	}
        	if(null != ifafirmEn){
        		memberIfafirmVO.setCompanyNameEn(ifafirmEn.getCompanyName());
        	}
        }
        model.put("ifafirm", memberIfafirmVO);
		return "console/ifafirm/firm_info";
	}
}


