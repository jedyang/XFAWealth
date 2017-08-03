package com.fsll.app.ifa.schedule.service.impl;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.fsll.app.ifa.schedule.service.AppScheduleService;
import com.fsll.app.ifa.schedule.vo.AppScheduleGroupVO;
import com.fsll.app.ifa.schedule.vo.AppScheduleItemVO;
import com.fsll.app.ifa.schedule.vo.AppScheduleTipsVO;
import com.fsll.app.ifa.schedule.vo.AppScheduleVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.CrmSchedule;
import com.fsll.wmes.entity.CrmScheduleGroup;
import com.fsll.wmes.entity.MemberBase;
/**
 * 日程接口Service实现
 * @author zxtan
 * @date 2017-03-23
 */
@Service("appIfaScheduleService")
public class AppScheduleServiceImpl extends BaseService implements
		AppScheduleService {


	/**
	 * 获取我的分组列表
	 *@author zxtan
	 *@date 2017-03-23
	 * @param memberId Ifa Member Id
	 * @return
	 */
	public List<AppScheduleGroupVO> findScheduleGroupList(String memberId) {
		// TODO Auto-generated method stub
		List<AppScheduleGroupVO> voList = new ArrayList<AppScheduleGroupVO>();
		String hql = "from CrmScheduleGroup m where m.member.id=? order by m.groupName ";
		List<Object> params = new ArrayList<Object>();
		params.add(memberId);
		List<CrmScheduleGroup> list = baseDao.find(hql, params.toArray(), false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				CrmScheduleGroup group = list.get(i);
				AppScheduleGroupVO vo = new AppScheduleGroupVO();
				vo.setGroupId(group.getId());
				vo.setGroupName(group.getGroupName());
				vo.setColor(group.getColor());
				vo.setIcon(group.getIcon());
				voList.add(vo);
			}
		}
		return voList;
	}


	/**
	 * 获取我的日程列表（分页）
	 *@author zxtan
	 *@date 2017-03-23
	 * @param jsonPaging
	 * @param ifaMemberId Ifa Member Id
	 * @param clientMemberId Client Member Id
	 * @param groupId 分组Id
	 * @param sort 排序字段
	 * @return
	 */
	public JsonPaging findScheduleList(JsonPaging jsonPaging, String ifaMemberId,String clientMemberId,String groupId,String sort) {
		// TODO Auto-generated method stub
		List<AppScheduleItemVO> voList = new ArrayList<AppScheduleItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append("from CrmSchedule m where m.isValid='1' and m.creator.id=? ");
		List<Object> params = new ArrayList<Object>();
		params.add(ifaMemberId);
		
		if(!"".equals(clientMemberId)){
			hql.append(" and m.customer.member.id=? ");
			params.add(clientMemberId);
		}
		
		if(!"".equals(groupId)){
			hql.append(" and m.group.id=? ");
			params.add(groupId);
		}
		
		if("createTime".equalsIgnoreCase(sort)){
			hql.append(" order by m.createTime desc ");
		}else {
			hql.append(" order by m.endDate desc,m.endTime desc ");
		}
		
		jsonPaging = baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List<CrmSchedule> list = jsonPaging.getList();
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				CrmSchedule schedule = list.get(i);
				CrmScheduleGroup group = schedule.getGroup();
				AppScheduleItemVO vo = new AppScheduleItemVO();
				vo.setId(schedule.getId());
				vo.setTitle(schedule.getTitle());
				if(null != group){
					vo.setGroupId(group.getId());
					vo.setGroupName(group.getGroupName());
				}
				vo.setCreateTime(DateUtil.dateToDateString(schedule.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
				String startTime = DateUtil.dateToDateString(schedule.getStartDate(), "yyyy-MM-dd")+" "+schedule.getStartTime().toString();
				String endTime = DateUtil.dateToDateString(schedule.getEndDate(), "yyyy-MM-dd")+" "+schedule.getEndTime().toString();
				vo.setStartTime(startTime);
				vo.setEndTime(endTime);
				voList.add(vo);
			}
		}
		jsonPaging.setList(voList);
		
		return jsonPaging;
	}
	
	/**
	 * 获取日程提醒
	 * @author zxtan
	 * @date 2017-04-24
	 */
	@SuppressWarnings("deprecation")
	public List<AppScheduleTipsVO> findScheduleTipsList( String ifaMemberId,Date startDate,Date endDate) {
		// TODO Auto-generated method stub
		List<AppScheduleTipsVO> voList = new ArrayList<AppScheduleTipsVO>();
		StringBuilder hql = new StringBuilder();
		hql.append("from CrmSchedule m where m.isValid='1' and m.creator.id=? ");
		hql.append(" and (m.startDate>=? or (m.startDate<? and m.endDate>?)) ");
		hql.append(" and (m.endDate <=? or (m.startDate<? and m.endDate>?)) ");
		hql.append(" order by m.startDate,m.startTime ");
		List<Object> params = new ArrayList<Object>();
		params.add(ifaMemberId);
		params.add(startDate);
		params.add(startDate);
		params.add(startDate);
		params.add(endDate);
		params.add(endDate);
		params.add(endDate);

		Map<Date, Integer> map = new TreeMap<Date, Integer>();
		
		List<CrmSchedule> list = baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				CrmSchedule schedule = list.get(i);
				Date dtStart = schedule.getStartDate();
				Date dtEnd = schedule.getEndDate();
				
				if(startDate.compareTo(dtStart)==1){
					dtStart = startDate;
				}
				
				if(dtEnd.compareTo(endDate)==1){
					dtEnd = endDate;
				}
				dtEnd = DateUtil.addDate(dtEnd,Calendar.DATE,1);
				
				if("0".equals(schedule.getEventType())){
					//普通日程					
					while (dtEnd.compareTo(dtStart)==1) {//dtEnd > dtStart
						if(map.containsKey(dtStart)){
							map.put(dtStart, map.get(dtStart) + 1);	
						}else {
							map.put(dtStart, 1);
						}
						dtStart = DateUtil.addDate(dtStart,Calendar.DATE,1);
					}					
				}else {
					//重复日程	
					while (dtEnd.compareTo(dtStart)==1) {//dtEnd > dtStart
						Calendar calBegin = Calendar.getInstance();  
				        calBegin.setTime(dtStart);
				        int dayOfWeek = calBegin.get(Calendar.DAY_OF_WEEK) - 1;
				        char repeaNum = schedule.getRepeatDay().charAt(dayOfWeek);
				        if('1' == repeaNum){				        	
							if(map.containsKey(dtStart)){
								map.put(dtStart, map.get(dtStart) + 1);	
							}else {
								map.put(dtStart, 1);
							}
				        }
						dtStart = DateUtil.addDate(dtStart,Calendar.DATE,1);
					}				
				}
				
			}
		}
		
		for (Iterator<Date> it = map.keySet().iterator(); it.hasNext();) {
		    AppScheduleTipsVO vo = new AppScheduleTipsVO();
		    Date date = it.next();
		    Object count = map.get(date);
		    vo.setDate(DateUtil.dateToDateString(date, CommonConstants.FORMAT_DATE));
		    vo.setCount(String.valueOf(count));
		    voList.add(vo);
		}
		
		return voList;
	}


	/**
	 * 获取我某一天的日程
	 *@author zxtan
	 *@date 2017-03-23
	 * @param memberId  Ifa Member Id
	 * @param date
	 * @return
	 */
	public List<AppScheduleItemVO> findScheduleListByDate(String memberId,
			Date date) {
		// TODO Auto-generated method stub
		List<AppScheduleItemVO> voList = new ArrayList<AppScheduleItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append("from CrmSchedule m where m.isValid='1' and m.creator.id=? ");
		hql.append(" and m.startDate<=? and m.endDate>=? ");		
		hql.append(" order by m.startDate,m.startTime ");
		List<Object> params = new ArrayList<Object>();
		params.add(memberId);
		params.add(date);
		params.add(date);
		
		List<CrmSchedule> list = baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				CrmSchedule schedule = list.get(i);
				CrmScheduleGroup group = schedule.getGroup();
				AppScheduleItemVO vo = new AppScheduleItemVO();
				if("1".equals(schedule.getEventType())){
					Calendar calBegin = Calendar.getInstance();  
			        calBegin.setTime(date);
			        int dayOfWeek = calBegin.get(Calendar.DAY_OF_WEEK) - 1;
			        char repeaNum = schedule.getRepeatDay().charAt(dayOfWeek);
			        if('0' == repeaNum){
			        	continue;
			        }
				}
				
				vo.setId(schedule.getId());
				vo.setTitle(schedule.getTitle());
				if(null != group){
					vo.setGroupId(group.getId());
					vo.setGroupName(group.getGroupName());
				}
				vo.setCreateTime(DateUtil.dateToDateString(schedule.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
				String startTime = DateUtil.dateToDateString(schedule.getStartDate(), "yyyy-MM-dd")+" "+schedule.getStartTime().toString();
				String endTime = DateUtil.dateToDateString(schedule.getEndDate(), "yyyy-MM-dd")+" "+schedule.getEndTime().toString();
				vo.setStartTime(startTime);
				vo.setEndTime(endTime);
				
				voList.add(vo);
			}
		}
		return voList;
	}
	
	
	/**
	 * 获取日程详情
	 *@author zxtan
	 *@date 2017-04-25
	 * @param scheduleId  
	 * @return
	 */
	public AppScheduleVO findScheduleInfo(String scheduleId) {
		// TODO Auto-generated method stub
		AppScheduleVO vo = new AppScheduleVO();
		StringBuilder hql = new StringBuilder();
		hql.append("from CrmSchedule m where m.id=? ");
		List<Object> params = new ArrayList<Object>();
		params.add(scheduleId);
		
		List<CrmSchedule> list = baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			
			CrmSchedule schedule = list.get(0);
			CrmScheduleGroup group = schedule.getGroup();
			CrmCustomer customer = schedule.getCustomer();
							
			vo.setId(schedule.getId());
			vo.setTitle(schedule.getTitle());
			vo.setContent(schedule.getContent());
			if(null != customer){
				vo.setCustomerId(customer.getId());
				vo.setNickName(customer.getNickName());
			}
			if(null != group){
				vo.setGroupId(group.getId());
				vo.setGroupName(group.getGroupName());
			}
			
			String startTime = DateUtil.dateToDateString(schedule.getStartDate(), "yyyy-MM-dd")+" "+schedule.getStartTime().toString();
			String endTime = DateUtil.dateToDateString(schedule.getEndDate(), "yyyy-MM-dd")+" "+schedule.getEndTime().toString();
			vo.setStartTime(startTime);
			vo.setEndTime(endTime);	
			vo.setCreateTime(DateUtil.dateToDateString(schedule.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			vo.setLastUpdate(DateUtil.dateToDateString(schedule.getLastUpdate(), "yyyy-MM-dd HH:mm:ss"));
			vo.setRepeatDay(schedule.getRepeatDay());
			vo.setEventType(schedule.getEventType());
			vo.setRepeatFrom(DateUtil.dateToDateString(schedule.getRepeatFrom(), "yyyy-MM-dd"));
			vo.setRepeatTo(DateUtil.dateToDateString(schedule.getRepeatTo(), "yyyy-MM-dd"));
			vo.setRemindSetting(schedule.getRemindSetting());
			vo.setIsValid(schedule.getIsValid());
			vo.setIfImportant(schedule.getIfImportant());
		}
		return vo;
	}
	
	/**
	 * 新增、更新日程
	 * @author zxtan
	 * @date 2017-03-30
	 * @param jsonObject
	 * @return
	 */
	public boolean updateSchedule(JSONObject jsonObject){
		try {
			String id = jsonObject.optString("id", "");
			String title = jsonObject.optString("title", "");
			String customerId = jsonObject.optString("customerId", "");
			String content = jsonObject.optString("content", "");
			String startTime = jsonObject.optString("startTime", "");
			String endTime = jsonObject.optString("endTime", "");
			String color = jsonObject.optString("color", null);
			String remind = jsonObject.optString("remind", null);
			String repeatDay = jsonObject.optString("repeatDay", null);
			String itemName = jsonObject.optString("itemName", null);
			String remark = jsonObject.optString("remark", null);
			String eventType = jsonObject.optString("eventType", "0");
//			String repeatFrom = jsonObject.optString("repeatFrom", "");
//			String repeatTo = jsonObject.optString("repeatTo", "");
			String remindSetting = jsonObject.optString("remindSetting", "");
			String groupId = jsonObject.optString("groupId", "");
			String creatorId = jsonObject.optString("creatorId", "");
			String ifImportant = jsonObject.optString("ifImportant", "0");
			
			MemberBase creator = (MemberBase) baseDao.get(MemberBase.class, creatorId);
			CrmCustomer customer = (CrmCustomer) baseDao.get(CrmCustomer.class, customerId);
			CrmScheduleGroup group = null;
			if(!"".equals(groupId)){ 
				group = (CrmScheduleGroup) baseDao.get(CrmScheduleGroup.class, groupId);
			}
			
			
			CrmSchedule schedule = new CrmSchedule();
			if("".equals(id)){
				schedule.setId(null);
				schedule.setCreateTime(new Date());
			}else {
				schedule = (CrmSchedule) baseDao.get(CrmSchedule.class, id);
			}
			
			schedule.setCustomer(customer);
			schedule.setTitle(title);
			schedule.setContent(content);
			Date startDate = DateUtil.getDate(startTime, CommonConstants.FORMAT_DATE);
			Date endDate = DateUtil.getDate(endTime, CommonConstants.FORMAT_DATE);
			schedule.setStartDate(startDate);
			schedule.setStartTime(new Time(DateUtil.getDate(startTime, CommonConstants.FORMAT_DATE_TIME).getTime()));
			schedule.setEndDate(DateUtil.getDate(endTime, CommonConstants.FORMAT_DATE));
			schedule.setEndTime(new Time(DateUtil.getDate(endTime, CommonConstants.FORMAT_DATE_TIME).getTime()));
			schedule.setColor(color);
			schedule.setRemind(remind);
			schedule.setRepeatDay(repeatDay);
			schedule.setItemName(itemName);
			schedule.setRemark(remark);
			schedule.setEventType(eventType);
			schedule.setRepeatFrom(startDate);
			schedule.setRepeatTo(endDate);
			schedule.setRemindSetting(remindSetting);
			schedule.setGroup(group);
			schedule.setCreator(creator);
			schedule.setLastUpdate(new Date());
			schedule.setIsValid("1");
			schedule.setIfImportant(ifImportant);
			
			baseDao.create(schedule);
			
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
	
	/**
	 * 删除日程（逻辑删除）
	 * @author zxtan
	 * @date 2017-03-30
	 * @param id
	 * @return
	 */
	public boolean deleteSchedule(String id){
		CrmSchedule schedule = (CrmSchedule) baseDao.get(CrmSchedule.class, id);
		if(null == schedule){
			return false;
		}else {
			schedule.setIsValid("0");
			baseDao.update(schedule);
			return true;
		}
	}

}
