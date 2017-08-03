package com.fsll.wmes.crm.action.front;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.CommonConstants;
import com.fsll.common.exception.commonException;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.crm.service.CrmScheduleService;
import com.fsll.wmes.crm.vo.ScheduleFilterVO;
import com.fsll.wmes.crm.vo.ScheduleVO;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.CrmSchedule;
import com.fsll.wmes.entity.CrmScheduleGroup;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.WebPush;
import com.fsll.wmes.entity.WebPushDetail;
import com.fsll.wmes.entity.WebReadToDo;
import com.fsll.wmes.entity.WebView;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.strategy.service.StrategyInfoService;
import com.fsll.wmes.web.service.WebReadToDoService;
import com.mchange.v2.util.PropertiesUtils;



/**
 * 控制器:客户日程记录
 * @author wwluo
 * @version 1.0
 * @date 2016-09-06
 */
@Controller
@RequestMapping("/front/crm/schedule")
public class FrontCrmScheduleAct extends WmesBaseAct{
	
	@Autowired
	private CrmScheduleService crmScheduleService;

	/**
	 * 跳转日程页面
	 * @author wwluo
	 * @date 2016-09-06 
	 */
	@RequestMapping(value = "/calendar_view")
	public String scheduleList(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String customerId = request.getParameter("customerId");//客户ID
		model.put("customerId", customerId);
		return "ifa/crm/schedule/calendar_view";
	}
	
	/**
	 * 获取ifa客户日程,客户Id（customerId）为空获取全部日程
	 * @author wwluo
	 * @date 2016-09-06
	 */
	@RequestMapping(value = "/getCRMSchedule")
	public void getCRMSchedule(ScheduleFilterVO filter,HttpServletRequest request,HttpServletResponse response,ModelMap model) {		
		List<ScheduleVO> list = new ArrayList<ScheduleVO>();
		String langCode = this.getLoginLangFlag(request);
		filter.setLangCode(langCode);
		MemberBase  curMember = this.getLoginUser(request);
		if(null != curMember){
			list = crmScheduleService.getCRMSchedule(curMember.getId(),filter);
		}
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("crmScheduleJson", JsonUtil.toJson(list));
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 日程详情弹窗
	 * @author wwluo
	 * @date 2016-09-07 
	 */
	@RequestMapping(value = "/detail")
	public String scheduleDetail(ScheduleVO vo,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String customerId = request.getParameter("customerId");//客户ID
		if(vo.getStart().indexOf("T") > -1){
			String start = vo.getStart().substring(0, vo.getStart().indexOf("."));
			vo.setStart(start.replace("T", " "));
		}
		if(vo.getEnd().indexOf("T") > -1){
			String end = vo.getEnd().substring(0, vo.getEnd().indexOf("."));
			vo.setEnd(end.replace("T", " "));
		}
		CrmCustomer customer = new CrmCustomer();
		if(StringUtils.isNotBlank(customerId)){
			customer = crmScheduleService.getCustomerById(customerId);
		}else if(vo != null && StringUtils.isNotBlank(vo.getId())){
			CrmSchedule schedule = crmScheduleService.getScheduleById(vo.getId());
			customer = schedule.getCustomer();
		}
		model.put("customer", customer);
		model.put("scheduleVO", vo);
		return "ifa/crm/schedule/schedule_dialog";
	}
	
	/**
	 * 日程添加与更新
	 * @author wwluo
	 * @date 2016-09-07 
	 */
	@RequestMapping(value = "/updateSchedule")
	public void updateSchedule(CrmSchedule crmSchedule,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String id = request.getParameter("id");
		String startDate = request.getParameter("strStartDate");
		String title = this.toUTF8String(request.getParameter("title"));
		String eventType = request.getParameter("eventType");
		String repeatDay = request.getParameter("repeatDay");
		String endDate = request.getParameter("strEndDate");
		String startTime = request.getParameter("strStartTime");
		String endTime = request.getParameter("strEndTime");
		String customerId = request.getParameter("customerId");
		String content = this.toUTF8String(request.getParameter("content"));
		
		String groupId = request.getParameter("groupId");
		String repeatFrom = request.getParameter("strRepeatFrom");
		String repeatTo = request.getParameter("strRepeatTo");
		
		String remindSetting = request.getParameter("remindSetting");
		String ifImportant = request.getParameter("ifImportant");
		CrmCustomer crmCustomer = new CrmCustomer();
		boolean flag = true;
		if(StringUtils.isNotBlank(id)){
			crmSchedule = crmScheduleService.getScheduleById(id);
			//判断实体是否存在
			if(null == crmSchedule){
				flag = false;
			}
		}else{
			crmSchedule.setIsValid("1");
			crmSchedule.setItemName(null);
			crmSchedule.setCreateTime(new Date());
			crmSchedule.setLastUpdate(new Date());
			crmSchedule.setRemark(null);
			crmSchedule.setRemind(null);
			MemberBase curMember = this.getLoginUser(request);    
			crmSchedule.setCreator(curMember);
			crmSchedule.setId(null);
		}
		SimpleDateFormat formatterDate = new SimpleDateFormat(CommonConstants.FORMAT_DATE);
		SimpleDateFormat formatterTime = new SimpleDateFormat(CommonConstants.FORMAT_TIME);
		try {
			if(StringUtils.isNotBlank(startDate)){
				crmSchedule.setStartDate(formatterDate.parse(startDate));	
			}
			if(StringUtils.isNotBlank(endDate)){
				crmSchedule.setEndDate(formatterDate.parse(endDate));
			}
			if(StringUtils.isNotBlank(startTime)){
				crmSchedule.setStartTime(new Time(formatterTime.parse(startTime).getTime()));
			}
			if(StringUtils.isNotBlank(endTime)){
				crmSchedule.setEndTime(new Time(formatterTime.parse(endTime).getTime()));
			}
			if(StringUtils.isNotBlank(repeatFrom)){
				crmSchedule.setRepeatFrom(formatterDate.parse(repeatFrom));
			}
			if(StringUtils.isNotBlank(repeatTo)){
				crmSchedule.setRepeatTo(formatterDate.parse(repeatTo));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//crmSchedule.setId(id);
		if (StringUtils.isNotBlank(title)) {
			crmSchedule.setTitle(title);
		}
		if (StringUtils.isNotBlank(eventType)) {
			crmSchedule.setEventType(eventType);
		}
		if (StringUtils.isNotBlank(repeatDay)) {
			crmSchedule.setRepeatDay(repeatDay);
		}
		if(StringUtils.isNotBlank(customerId)){
			crmCustomer = crmScheduleService.getCustomerById(customerId);
			if(null != crmCustomer){
				crmSchedule.setCustomer(crmCustomer);
			}
		}
		crmSchedule.setIsValid("1");
		CrmScheduleGroup group = null;
		if (StringUtils.isNotBlank(groupId)) {
			group = new CrmScheduleGroup();
			group.setId(groupId);
		}
		crmSchedule.setGroup(group);
		crmSchedule.setContent(content);
		crmSchedule.setRemindSetting(remindSetting);
		crmSchedule.setIfImportant(ifImportant);
		crmSchedule = crmScheduleService.updateSchedule(crmSchedule);
		crmCustomer = crmSchedule.getCustomer();
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("flag", flag);
		obj.put("crmSchedule", JsonUtil.toJson(crmSchedule));
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 删除日程（逻辑删除）
	 * @author wwluo
	 * @date 2016-09-07 
	 */
	@RequestMapping(value = "/deleteSchedule", method = RequestMethod.POST)
	public void deleteSchedule(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String id = request.getParameter("id");
		boolean flag = false;
		flag = crmScheduleService.deleteSchedule(id);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("flag", flag);
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 获取日程分组
	 * @author wwluo
	 * @date 2016-09-12
	 */
	@RequestMapping(value = "/getScheduleGroup", method = RequestMethod.POST)
	public void getScheduleGroup(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase  curMember = this.getLoginUser(request);
		List<CrmScheduleGroup> list = new ArrayList<CrmScheduleGroup>();
		if(null != curMember && StringUtils.isNotBlank(curMember.getId())){
				list = crmScheduleService.getScheduleGroup(curMember.getId());
		}
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("crmScheduleGroupJson", JsonUtil.toJson(list));
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 跳转分组弹窗
	 * @author wwluo
	 * @date 2016-09-12 
	 */
	@RequestMapping(value = "/groupDetail")
	public String groupDetail(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String groupId = request.getParameter("groupId");//客户ID
		CrmScheduleGroup  group = new CrmScheduleGroup();
		if(StringUtils.isNotBlank(groupId)){
			group = crmScheduleService.getCrmScheduleGroupById(groupId);
		}
		model.put("group", group);
		return "ifa/crm/schedule/group_dialog";
	}
	
	/**
	 * 创建与修改分组
	 * @author wwluo
	 * @date 2016-09-12 
	 */
	@RequestMapping(value = "/createGroup", method = RequestMethod.POST)
	public void createGroup(CrmScheduleGroup group,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String groupId = request.getParameter("groupId");
		String memberId = request.getParameter("memberId");
		MemberBase  curMember = this.getLoginUser(request); 
		String groupName = this.toUTF8String(request.getParameter("groupName"));
		String color = request.getParameter("color");
    	memberId = curMember.getId();          
		//判断是否存在分组
		if(StringUtils.isNotBlank(groupId)){
			group = crmScheduleService.getCrmScheduleGroupById(groupId);
		}
		group.setColor(color);
		group.setGroupName(groupName);
		MemberBase menber = new MemberBase();
		//获取member实体
		if(StringUtils.isNotBlank(memberId)){
			menber = crmScheduleService.getMemberBaseById(memberId);
		}
		group.setMember(menber);
		boolean flag = false;
		group = crmScheduleService.createAndUpdateGroup(group);
		Map<String, Object> obj = new HashMap<String, Object>();
		if(null != group){
			flag = true;
		}
		obj.put("flag", flag);
		obj.put("group", group);
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 删除分组
	 * @author wwluo
	 * @date 2016-09-12 
	 */
	@RequestMapping(value = "/deleteGroup", method = RequestMethod.POST)
	public void deleteGroup(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String groupId = request.getParameter("groupId");
		boolean flag = false;
		flag = crmScheduleService.deleteGroup(groupId);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("flag", flag);
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 获取ifa下客户
	 * @author wwluo
	 * @date 2016-09-28 
	 */
	@RequestMapping(value = "/getCustomers", method = RequestMethod.POST)
	public void getCustomers(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		List<CrmCustomer> list = new ArrayList<CrmCustomer>();
		MemberBase curMember = this.getLoginUser(request);    
		if(null != curMember){
			String type = request.getParameter("type");
			String id = request.getParameter("id");
			MemberIfa memberIfa = crmScheduleService.getMemberIfa(curMember.getId());
			if(null != memberIfa && StringUtils.isNotBlank(memberIfa.getId())) {
				list = crmScheduleService.getCustomers(memberIfa,type,id);
			}
		}
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("customersJson", JsonUtil.toJson(list));
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 日程更换分组
	 * @author wwluo
	 * @date 2016-09-28 
	 */
	@RequestMapping(value = "/updateScheduleGroup", method = RequestMethod.POST)
	public void updateScheduleGroup(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase curMember = this.getLoginUser(request);    
		CrmScheduleGroup crmScheduleGroup = null;
		boolean flag = false;
		if(null != curMember){
			String groupId = request.getParameter("groupId");
			String eventId = request.getParameter("id");
			String type = request.getParameter("type");
			crmScheduleGroup = crmScheduleService.updateScheduleGroup(groupId,eventId,type);
			if(crmScheduleGroup != null){
				flag = true;
			}
		}
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("flag", flag);
		obj.put("group", crmScheduleGroup);
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 获取提醒数据
	 * @author wwluo
	 * @date 2016-09-28 
	 */
	@RequestMapping(value = "/getRemind", method = RequestMethod.POST)
	public void getRemind(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String lang = this.getLoginLangFlag(request);
		boolean flag = false;
		List<GeneralDataVO> dataVOs = this.findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_REMIND_TIME, lang);
		if(dataVOs !=null && !dataVOs.isEmpty()){
			flag = true;
		}
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("flag", flag);
		obj.put("remindData", dataVOs);
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 更新是否重要事件
	 * @author wwluo
	 * @date 2016-09-28 
	 */
	@RequestMapping(value = "/updateImportant", method = RequestMethod.POST)
	public void updateImportant(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		boolean flag = false;
		String eventId = request.getParameter("id");
		String type = request.getParameter("type");
		String ifImportant = request.getParameter("ifImportant");
		flag = crmScheduleService.updateImportant(eventId,type,ifImportant);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("flag", flag);
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 获取一件日程
	 * @author wwluo
	 * @date 2016-09-06
	 */
	@RequestMapping(value = "/getScheduleInfo")
	public void getScheduleInfo(ScheduleFilterVO filter,HttpServletRequest request,HttpServletResponse response,ModelMap model) {		
		ScheduleVO schedule = null;
		boolean flag = false;
		String langCode = this.getLoginLangFlag(request);
		filter.setLangCode(langCode);
		String eventId = request.getParameter("id");
		if (StringUtils.isNotBlank(eventId)) {
			schedule = crmScheduleService.getScheduleInfo(eventId,filter);
			flag = true;
		}
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("flag", flag);
		obj.put("schedule", schedule);
		JsonUtil.toWriter(obj, response);
	}
	
	
	
	
	
}






















