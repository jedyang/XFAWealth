package com.fsll.wmes.distributor.action.console;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.distributor.service.MyDistributorService;
import com.fsll.wmes.entity.CompanyInfo;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberCompany;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.vo.MemberAdminVO;
/**
 * 工作台我的代理商控制类（代理商管理员）
 * @author qgfeng
 * @date 2016-12-9
 * @return
 */
@Controller
@RequestMapping("/console/distributor")
public class ConsoleMyDistributorAct  extends WmesBaseAct{
	
	@Autowired
	private MyDistributorService myDistributorService;
	@Autowired
	private MemberBaseService baseService;
	/**
	 * Distributor member分页列表
	 * @author qgfeng
	 * @date 2016-11-9
	 * @return
	 */
	@RequestMapping(value = "/member/list")
	public String memberList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//只允许distributor超级管理员
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if(curAdmin==null || curAdmin.getDistributor()==null){
			return "redirect:"+this.getFullPath(request)+"front/logout.do";
		}
		return "console/distributor/mydisbutor/member_list";
	}

	/**
	 * Distributor member数据查询的方法
	 * @author qgfeng
	 * @date 2016-12-2
	 */
	@RequestMapping(value = "/member/listJson", method = RequestMethod.POST)
	public void memberListJson(HttpServletRequest request, HttpServletResponse response,MemberAdminVO adminVo) {
		jsonPaging=this.getJsonPaging(request);
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		adminVo.setDistributorId(curAdmin.getDistributor().getId());
		jsonPaging = myDistributorService.findDisMember(jsonPaging, adminVo);
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * distributor member 启用/禁用操作
	 * @author qgfeng
	 * @date 2016-11-22
	 */
	@RequestMapping(value = "/member/validOperate", method = RequestMethod.POST)
	public void memberValidOperate(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			String ids = request.getParameter("ids");
			String isValid = request.getParameter("isValid");
			String[] idArry = ids.split(",");
			if(idArry!=null && idArry.length>0){
				for (String id : idArry) {
					MemberBase base = myDistributorService.getDisMemberById(id);
					if(base!=null){
						base.setIsValid(isValid);
						baseService.saveOrUpdate(base);
					}
				}
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
	 * 新增修改distributor member
	 * @author qgfeng
	 * @date 2016-12-28
	 */
	@RequestMapping(value = "/member/input/{id}")
	public String memberInput(@PathVariable("id")String id, ModelMap model) {
		if(StringUtils.isNotBlank(id)){
			MemberBase memberBase = myDistributorService.getDisMemberById(id);
			model.put("memberBase",memberBase);
		}
		return "console/distributor/mydisbutor/member_input";
	}
	
	/**
	 * 保存distributor Member
	 * @author qgfeng
	 * @date 2016-12-28
	 */
	@RequestMapping(value = "/member/save", method = RequestMethod.POST)
	public String saveMember(HttpServletRequest request, HttpServletResponse response,MemberAdminVO memberVo) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			MemberBase base = null;
			//检查是否管理员
			MemberAdmin curAdmin = getLoginMemberAdmin(request);
			if (curAdmin==null || curAdmin.getParent()!=null){
				obj.put("msg", "非管理员账号");
				throw new Exception("非管理员账号");
			}
			if(StringUtils.isBlank(memberVo.getMemberId())){
				base = new MemberBase();
				base.setIsValid("1");
				base.setStatus("1");
				base.setCreateTime(new Date());
				base.setPassword(pwdEncoder.encodePassword(memberVo.getPassword()));
				base.setMemberType(3);
				base.setSubMemberType(31);
			}else{
				base = baseService.findById(memberVo.getMemberId());
				//修改了密码
				if(StringUtils.isNotBlank(memberVo.getRepassword())){
					base.setPassword(pwdEncoder.encodePassword(memberVo.getRepassword()));
				}
			}
			base.setLoginCode(memberVo.getLoginCode());
			base.setEmail(memberVo.getEmail());
			base.setMobileNumber(memberVo.getMobileNumber());
			base.setNickName(memberVo.getNickName());
			base.setLastUpdate(new Date());

			MemberAdmin disMember = myDistributorService.getDisMemberByMid(curAdmin.getDistributor().getId(),memberVo.getMemberId());
			if(disMember==null){
				disMember = new MemberAdmin();
				disMember.setDistributor(curAdmin.getDistributor());
				disMember.setType("2");
				disMember.setParent(curAdmin);
			}
			disMember.setMember(base);
			disMember = myDistributorService.saveOrUpdateDisMember(disMember);
			if(disMember != null){
				result = true;
			}
			//保存用户和运营公司的关系
			if (disMember != null && disMember.getMember() != null && !disMember.getMember().getId().isEmpty()){
				String companyId = StrUtils.getString(request.getSession().getAttribute(CoreConstants.FRONT_COMPANY_ID));
				CompanyInfo company = new CompanyInfo();
				company.setId(companyId);

				MemberCompany memberCompany = new MemberCompany();
				memberCompany.setCompany(company);
				memberCompany.setMember(disMember.getMember());
				baseService.saveMemberCompany(memberCompany);
			}
		} catch (Exception e) {
			e.printStackTrace();
			obj.put("msg", e.getMessage());
		}finally{
			obj.put("result", result);
			JsonUtil.toWriter(obj, response);
		}
		return null;
	}
	
	/**
	 * 删除distributor Member
	 * @author qgfeng
	 * @date 2016-12-28
	 */
	@RequestMapping(value = "/member/delete", method = RequestMethod.POST)
	public String deleteMember(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = false;
		try {
			String ids = request.getParameter("ids");
			result = myDistributorService.deleteDistByIds(ids);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			obj.put("result", result);
			JsonUtil.toWriter(obj, response);
		}
		return null;
	}

}
