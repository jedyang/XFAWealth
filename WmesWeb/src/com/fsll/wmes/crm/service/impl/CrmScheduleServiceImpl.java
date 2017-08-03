package com.fsll.wmes.crm.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.crm.service.CrmScheduleService;
import com.fsll.wmes.crm.vo.ScheduleFilterVO;
import com.fsll.wmes.crm.vo.ScheduleVO;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.CrmSchedule;
import com.fsll.wmes.entity.CrmScheduleGroup;
import com.fsll.wmes.entity.IfafirmTeam;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.WebTaskList;
import com.fsll.wmes.ifa.vo.MemberIfaVO;


/***
 * 业务接口实现类：客户日程记录
 * @author wwluo
 * @date 2016-09-06
 */
@Service("crmScheduleService")
//@Transactional
public class CrmScheduleServiceImpl extends BaseService implements CrmScheduleService{

	/***
	 * 通过memberId获取ifa
	 * @author wwluo
	 * @date 2016-09-06
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	@Override
	public MemberIfa getMemberIfa(String memberId) {
		MemberIfa memberIfa = new MemberIfa();
		if(StringUtils.isNotBlank(memberId)){
			StringBuffer hql = new StringBuffer("from MemberIfa m where m.member.isValid=1 and member_id=?");
			List params = new ArrayList();
			params.add(memberId);
			List list = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(null != list && !list.isEmpty()){
				memberIfa = (MemberIfa) list.get(0);
			}
		}
		return memberIfa;
	}

    /**
	 * 获取ifa客户日程,客户Id（customerId）为空获取全部日程
	 * @author wwluo
	 * @date 2016-09-06
	 * @param memberId 当前 memberId
	 * @param filter 条件查询VO
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<ScheduleVO> getCRMSchedule(String memberId,ScheduleFilterVO filter) {
		List<ScheduleVO> volist = new ArrayList<ScheduleVO>();
		if (StringUtils.isNotBlank(memberId)) {
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" CrmSchedule c" +
					" WHERE" +
					" c.isValid=1" +
					"");
			List params = new ArrayList();
			//分组
			if(filter != null && "1".equals(filter.getUnclassified())){
				hql.append(" AND c.group IS NULL");
			}else if(filter != null && StringUtils.isNotBlank(filter.getGroupId())){
				hql.append(" AND c.group.id=?");
				params.add(filter.getGroupId());
			}
			//事件类型
			if(filter != null && StringUtils.isNotBlank(filter.getEventType())){
				hql.append(" AND c.eventType=?");
				params.add(filter.getEventType());
			}
			// 重要事件
			if(filter != null && StringUtils.isNotBlank(filter.getIfImportant())){
				hql.append(" AND c.ifImportant=?");
				params.add(filter.getIfImportant());
			}
			//单个客户日程
			if(filter != null && StringUtils.isNotBlank(filter.getCustomerId())){
				hql.append(" AND c.customer.id=?");
				params.add(filter.getCustomerId());
			//下属客户的日程
			}else if("1".equals(filter.getTeamIfa())){
				hql.append(
					" AND c.creator.id" +
					" IN" +
					" (SELECT" +
					" i.member.id" +
					" FROM" +
					" IfafirmTeamIfa t" +
					" LEFT JOIN" +
					" MemberIfa i" +
					" ON" +
					" i.id=t.ifa.id" +
					" LEFT JOIN" +
					" IfafirmTeam f" +
					" ON" +
					" f.id=t.team.id" +
					" WHERE" +
					" t.team IN(" +
					" SELECT" +
					" ft" +
					" FROM" +
					" IfafirmTeamIfa it" +
					" LEFT JOIN" +
					" MemberIfa mi" +
					" ON" +
					" mi.id=it.ifa.id" +
					" LEFT JOIN" +
					" IfafirmTeam ft" +
					" ON" +
					" ft.id=it.team.id" +
					" WHERE" +
					" ft.isValid=1" +
					" AND" +
					" it.isSupervisor=1" +
					" AND" +
					" mi.member.id=?" +
					" ))" +
					"");
				params.add(memberId);
			}else{
				//全部客户日程
				hql.append(" AND c.creator.id=?");
				params.add(memberId);
			}
			//客户类型
			if(StringUtils.isNotBlank(filter.getClientType())){
				hql.append(" AND c.customer.clientType=?");
				params.add(filter.getClientType());
			}
			//排序
			if(StringUtils.isNotBlank(filter.getSort())){
				if(StringUtils.isNotBlank(filter.getOrder())){
					hql.append(" ORDER BY ? " + filter.getOrder());
				}else{
					hql.append(" ORDER BY ? DESC");
				}
				params.add("c."+filter.getSort());
			}
			List<CrmSchedule> list = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(list != null && !list.isEmpty() && "1".equals(filter.getTeamIfa())){
				List<CrmSchedule> temp = new ArrayList<CrmSchedule>();
				for (CrmSchedule crmSchedule : list) {
					if(!memberId.equals(crmSchedule.getCreator().getId())){
						temp.add(crmSchedule);
					}
				}
				list.clear();
				list.addAll(temp);
			}
			//封装日程VO信息
			volist = scheList(volist, list, filter.getLangCode());
		}
		return volist;
	}

	/**
	 * 获取ifa团队
	 * @author wwluo
	 * @date 2016-12-21
	 * @param memberId 当前 memberId
	 * @param isSupervisor 
	 */
	@SuppressWarnings("unchecked")
	private List<IfafirmTeam> getIfaTeamByMember(String memberId,String isSupervisor) {
		List<IfafirmTeam> ifafirmTeams = null;
		if (StringUtils.isNotBlank(memberId)) {
			StringBuffer teamHql = new StringBuffer("" +
					" SELECT" +
					" f" +
					" FROM" +
					" IfafirmTeamIfa t" +
					" LEFT JOIN" +
					" MemberIfa i" +
					" ON" +
					" i.id=t.ifa.id" +
					" LEFT JOIN" +
					" IfafirmTeam f" +
					" ON" +
					" f.id=t.team.id" +
					" WHERE" +
					" f.isValid=1" +
					" AND" +
					" i.member.id=?");
			List<Object> teamParams = new ArrayList<Object>();
			teamParams.add(memberId);
			if (StringUtils.isNotBlank(isSupervisor)) {
				teamHql.append(" AND t.isSupervisor=?");
				teamParams.add(isSupervisor);
			}
			ifafirmTeams = this.baseDao.find(teamHql.toString(), teamParams.toArray(), false);
		}
		return ifafirmTeams;
	}
	
	/**
	 * 封装日程VO信息
	 * @author wwluo
	 * @date 2016-09-06
	 * @param volist 日程VO实体
	 * @param list 日程实体
	 */
	private List<ScheduleVO> scheList(List<ScheduleVO> volist, List<CrmSchedule> list, String langCode) {
		if(null != list && !list.isEmpty()){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			for (CrmSchedule crmSchedule : list) {
				//定期日程
				if("1".equals(crmSchedule.getEventType())){
					if(null!=crmSchedule.getRepeatTo() && null!=crmSchedule.getRepeatFrom()){
						Calendar calBegin = Calendar.getInstance();  
				        calBegin.setTime(crmSchedule.getRepeatFrom());  
				        Calendar calEnd = Calendar.getInstance();  
				        calEnd.setTime(crmSchedule.getRepeatTo());
				        calEnd.add(Calendar.DAY_OF_MONTH, 1);
				        // 测试此日期是否在指定日期之后    
				        while (calEnd.getTime().after(calBegin.getTime())) {
				        	//是否指定每周几
				        	if(StringUtils.isNotBlank(crmSchedule.getRepeatDay())){
				        		int dayOfWeek = calBegin.get(Calendar.DAY_OF_WEEK) - 1;
					        	if (dayOfWeek < 0) dayOfWeek = 0;
				        		char repeaNum = crmSchedule.getRepeatDay().charAt(dayOfWeek);
				        		if('1' == repeaNum){
				        			ScheduleVO repeatVo = new ScheduleVO();
						        	BeanUtils.copyProperties(crmSchedule, repeatVo);
						        	repeatVo.setStart(formatter.format(calBegin.getTime()));
						        	repeatVo.setEnd(formatter.format(calBegin.getTime()));
						        	if(null != crmSchedule.getGroup()){  //TODO
						        		repeatVo.setColor(crmSchedule.getGroup().getColor());
						        		repeatVo.setGroupId(crmSchedule.getGroup().getId());
						        		repeatVo.setGroupName(crmSchedule.getGroup().getGroupName());
									}
						        	//用“T”或者“ ”空格分隔Date与Time。国际标准组织（ISO）格式，输出：2013-11-04T14:03:05.420Z
									if(null != crmSchedule.getStartDate() && null != crmSchedule.getStartTime()){
										repeatVo.setRepeatFrom(formatter.format(crmSchedule.getStartDate())+"T"+crmSchedule.getStartTime().toString());
									}else if(null!=crmSchedule.getStartDate()){
										repeatVo.setRepeatFrom(formatter.format(crmSchedule.getStartDate()));
									}
									if(null != crmSchedule.getStartDate() && null != crmSchedule.getEndTime()){
										repeatVo.setRepeatTo(formatter.format(crmSchedule.getEndDate())+"T"+crmSchedule.getEndTime().toString());
									}else if(null!=crmSchedule.getEndDate()){
										repeatVo.setRepeatTo(formatter.format(crmSchedule.getEndDate()));
									}
						        	//repeatVo.setRepeatFrom(formatter.format(crmSchedule.getRepeatFrom()));
						        	//repeatVo.setRepeatTo(formatter.format(crmSchedule.getRepeatTo()));
						        	if (StringUtils.isNotBlank(crmSchedule.getRemindSetting())) {
						        		if (StringUtils.isBlank(langCode)) {
						        			langCode = CommonConstants.DEF_LANG_CODE;
										}
						        		String remindSettingName = this.getParamConfigName(langCode, CommonConstantsWeb.SYS_PARM_TYPE_REMIND_TIME+"_"+crmSchedule.getRemindSetting(),CommonConstantsWeb.SYS_PARM_TYPE_REMIND_TIME);
						        		repeatVo.setRemindSettingName(remindSettingName);
						        		repeatVo.setRemindSetting(crmSchedule.getRemindSetting());
						        	}
						        	repeatVo.setCreateTime(crmSchedule.getCreateTime());
						        	String customerId = null;
						        	String customerName = null;
						        	if(crmSchedule.getCustomer() != null){
						        		customerId = crmSchedule.getCustomer().getId();
						        		customerName = crmSchedule.getCustomer().getNickName();
						        	}
						        	repeatVo.setCustomerId(customerId);
						        	repeatVo.setCustomerName(customerName);
						        	repeatVo.setIfImportant(crmSchedule.getIfImportant());
						        	if(crmSchedule.getCreator() != null){
						        		repeatVo.setCreatorId(crmSchedule.getCreator().getId());
							        	repeatVo.setCreatorName(crmSchedule.getCreator().getNickName());
						        	}
						        	volist.add(repeatVo);
				        		}
				        	}else{
				        		ScheduleVO repeatVo = new ScheduleVO();
					        	BeanUtils.copyProperties(crmSchedule, repeatVo);
					        	repeatVo.setStart(formatter.format(calBegin.getTime()));
					        	repeatVo.setEnd(formatter.format(calBegin.getTime()));
					        	if(null != crmSchedule.getGroup()){
					        		repeatVo.setColor(crmSchedule.getGroup().getColor());
					        		repeatVo.setGroupId(crmSchedule.getGroup().getId());
					        		repeatVo.setGroupName(crmSchedule.getGroup().getGroupName());
								}
					        	repeatVo.setRepeatFrom(formatter.format(crmSchedule.getRepeatFrom()));
					        	repeatVo.setRepeatTo(formatter.format(crmSchedule.getRepeatTo()));
					        	if (StringUtils.isNotBlank(crmSchedule.getRemindSetting())) {
					        		if (StringUtils.isBlank(langCode)) {
					        			langCode = CommonConstants.DEF_LANG_CODE;
									}
					        		String remindSettingName = this.getParamConfigName(langCode, CommonConstantsWeb.SYS_PARM_TYPE_REMIND_TIME+"_"+crmSchedule.getRemindSetting(),CommonConstantsWeb.SYS_PARM_TYPE_REMIND_TIME);
					        		repeatVo.setRemindSettingName(remindSettingName);
					        		repeatVo.setRemindSetting(crmSchedule.getRemindSetting());
					        	}
					        	repeatVo.setCreateTime(crmSchedule.getCreateTime());
					        	String customerId = null;
					        	String customerName = null;
					        	if(crmSchedule.getCustomer() != null){
					        		customerId = crmSchedule.getCustomer().getId();
					        		customerName = crmSchedule.getCustomer().getNickName();
					        	}
					        	repeatVo.setCustomerId(customerId);
					        	repeatVo.setCustomerName(customerName);
					        	repeatVo.setIfImportant(crmSchedule.getIfImportant());
					        	volist.add(repeatVo);
				        	}
				        	//时间+1
				        	calBegin.add(Calendar.DAY_OF_MONTH, 1);
				        }
					}
				}else{
					//非定期日程处理
					ScheduleVO vo = new ScheduleVO();
					BeanUtils.copyProperties(crmSchedule, vo);
					vo.setCreateTime(crmSchedule.getCreateTime());
					//用“T”或者“ ”空格分隔Date与Time。国际标准组织（ISO）格式，输出：2013-11-04T14:03:05.420Z
					if(null != crmSchedule.getStartDate() && null != crmSchedule.getStartTime()){
						vo.setStart(formatter.format(crmSchedule.getStartDate())+"T"+crmSchedule.getStartTime().toString());
					}else if(null!=crmSchedule.getStartDate()){
						vo.setStart(formatter.format(crmSchedule.getStartDate()));
					}
					if(null != crmSchedule.getStartDate() && null != crmSchedule.getEndTime()){
						vo.setEnd(formatter.format(crmSchedule.getEndDate())+"T"+crmSchedule.getEndTime().toString());
					}else if(null!=crmSchedule.getEndDate()){
						vo.setEnd(formatter.format(crmSchedule.getEndDate()));
					}
					if(null != crmSchedule.getGroup()){
						vo.setColor(crmSchedule.getGroup().getColor());
						vo.setGroupId(crmSchedule.getGroup().getId());
						vo.setGroupName(crmSchedule.getGroup().getGroupName());
					}
					String customerId = null;
		        	String customerName = null;
		        	if(crmSchedule.getCustomer() != null){
		        		customerId = crmSchedule.getCustomer().getId();
		        		customerName = crmSchedule.getCustomer().getNickName();
		        	}
		        	vo.setCustomerId(customerId);
		        	vo.setCustomerName(customerName);
		        	vo.setIfImportant(crmSchedule.getIfImportant());
		        	if (StringUtils.isNotBlank(crmSchedule.getRemindSetting())) {
		        		if (StringUtils.isBlank(langCode)) {
		        			langCode = CommonConstants.DEF_LANG_CODE;
						}
		        		String remindSettingName = this.getParamConfigName(langCode, CommonConstantsWeb.SYS_PARM_TYPE_REMIND_TIME+"_"+crmSchedule.getRemindSetting(),CommonConstantsWeb.SYS_PARM_TYPE_REMIND_TIME);
		        		vo.setRemindSettingName(remindSettingName);
		        		vo.setRemindSetting(crmSchedule.getRemindSetting());
		        	}
		        	vo.setCreatorId(crmSchedule.getCreator().getId());
		        	vo.setCreatorName(crmSchedule.getCreator().getNickName());
					volist.add(vo);
				}
			}
		}
		return volist;
	}	
	
	
	/***
	 * 更新日程
	 * @author wwluo
	 * @date 2016-09-07
	 */
	@Override
	public CrmSchedule updateSchedule(CrmSchedule crmSchedule) {
		crmSchedule = (CrmSchedule) this.baseDao.saveOrUpdate(crmSchedule);
		return crmSchedule;
	}

	/***
	 * 根据id获取日程实体
	 * @author wwluo
	 * @date 2016-09-07
	 */
	@Override
	public CrmSchedule getScheduleById(String id) {
		CrmSchedule crmSchedule = new CrmSchedule();
		if(StringUtils.isNotBlank(id)){
			crmSchedule = (CrmSchedule) this.baseDao.get(CrmSchedule.class, id);
		}
		return crmSchedule;
	}

	/***
	 * 根据id获取客户实体
	 * @author wwluo
	 * @date 2016-09-07
	 */
	@Override
	public CrmCustomer getCustomerById(String customerId) {
		CrmCustomer crmCustomer = new CrmCustomer();
		if(StringUtils.isNotBlank(customerId)){
			crmCustomer = (CrmCustomer) this.baseDao.get(CrmCustomer.class, customerId);
		}
		return crmCustomer;
	}

	/**
	 * 删除日程（逻辑删除）
	 * @author wwluo
	 * @date 2016-09-07 
	 */
	@Override
	public boolean deleteSchedule(String id) {
		boolean flag = false;
		if(StringUtils.isNotBlank(id)){
			CrmSchedule crmSchedule = 
				(CrmSchedule) this.baseDao.get(CrmSchedule.class, id);
			if(null != crmSchedule){
				crmSchedule.setIsValid("0");
				this.baseDao.update(crmSchedule);
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 获取日程分组
	 * @author wwluo
	 * @date 2016-09-12
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CrmScheduleGroup> getScheduleGroup(String memberId) {
		List<CrmScheduleGroup> groupList = new ArrayList<CrmScheduleGroup>();
		if(StringUtils.isNotBlank(memberId)){
			StringBuffer hql =new StringBuffer(" from CrmScheduleGroup where member_id=?");
			List<Object> params = new ArrayList<Object>();
			params.add(memberId);
			groupList = this.baseDao.find(hql.toString(), params.toArray(), false);	
		}
		return groupList;
	}

	/**
	 * 根据id获取分组实体
	 * @author wwluo
	 * @date 2016-09-12
	 */
	@Override
	public CrmScheduleGroup getCrmScheduleGroupById(String groupId) {
		CrmScheduleGroup crmScheduleGroup = new CrmScheduleGroup();
		if(StringUtils.isNotBlank(groupId)){
			crmScheduleGroup = (CrmScheduleGroup) this.baseDao.get(CrmScheduleGroup.class, groupId);
		}
		return crmScheduleGroup;
	}

	/**
	 * 根据id获取member实体
	 * @author wwluo
	 * @date 2016-09-12
	 */
	@Override
	public MemberBase getMemberBaseById(String memberId) {
		MemberBase memberBase = new MemberBase();
		if(StringUtils.isNotBlank(memberId)){
			memberBase = (MemberBase) this.baseDao.get(MemberBase.class, memberId);
		}
		return memberBase;
	}

	/**
	 * 创建与修改分组信息
	 * @author wwluo
	 * @date 2016-09-12
	 */
	@Override
	public CrmScheduleGroup createAndUpdateGroup(CrmScheduleGroup group) {
		CrmScheduleGroup crmScheduleGroup = new CrmScheduleGroup();
		if(null != group && StringUtils.isNotBlank(group.getId())){
			crmScheduleGroup = (CrmScheduleGroup) this.baseDao.update(group);
		}else{
			crmScheduleGroup = (CrmScheduleGroup) this.baseDao.create(group);
		}
		return crmScheduleGroup;
	}

	/**
	 * 删除分组
	 * @author wwluo
	 * @date 2016-09-12 
	 */
	@Override
	public boolean deleteGroup(String groupId) {
		boolean flag = false;
		if(StringUtils.isNotBlank(groupId)){
			CrmScheduleGroup crmScheduleGroup = 
				(CrmScheduleGroup) this.baseDao.get(CrmScheduleGroup.class, groupId);
			this.baseDao.delete(crmScheduleGroup);
			StringBuffer hql = new StringBuffer("" +
					" UPDATE" +
					" CrmSchedule c" +
					" SET" +
					" c.group.id=null" +
					" WHERE" +
					" c.group.id=?");
			List<Object> params = new ArrayList<Object>();
			params.add(groupId);
			this.baseDao.updateHql(hql.toString(), params.toArray());
			flag = true;
		}
		return flag;
	}

	/**
	 * 获取ifa下客户
	 * @author wwluo
	 * @date 2016-09-28 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<CrmCustomer> getCustomers(MemberIfa memberIfa,String type,String id) {
		List<CrmCustomer> customers = new ArrayList<CrmCustomer>();
		if(null != memberIfa){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" CrmCustomer" +
					" WHERE" +
					" isValid=1" +
					" AND" +
					" ifa=?" +
					"");
			List params = new ArrayList();
			params.add(memberIfa);
			if (StringUtils.isNotBlank(type)) {
				hql.append(" AND clientType=?");
				params.add(type);
			}
			if(StringUtils.isNotBlank(id)){
				hql.append(" AND id=?");
				params.add(id);
			}
			customers = this.baseDao.find(hql.toString(), params.toArray(), false);
		}
		return customers;
	}

	/**
	 * 日程更换分组
	 * @author wwluo
	 * @date 2016-09-28 
	 */
	@Override
	public CrmScheduleGroup updateScheduleGroup(String groupId,
			String eventId,String type) {
		CrmScheduleGroup group = null;
		if (StringUtils.isNotBlank(groupId)
				&& StringUtils.isNotBlank(eventId)
					&& StringUtils.isNotBlank(type)) {
			group = (CrmScheduleGroup) this.baseDao.get(CrmScheduleGroup.class, groupId);
			if(group != null && CommonConstantsWeb.GROUP_S.equals(type)){
				CrmSchedule crmSchedule = (CrmSchedule) this.baseDao.get(CrmSchedule.class, eventId);
				if(crmSchedule != null){
					crmSchedule.setGroup(group);
					crmSchedule.setLastUpdate(new Date());
					this.baseDao.update(crmSchedule);
				}
			}else if(group != null && CommonConstantsWeb.GROUP_T.equals(type)){
				WebTaskList webTaskList = (WebTaskList) this.baseDao.get(WebTaskList.class, eventId);
				if(webTaskList != null){
					webTaskList.setGroup(group);
					this.baseDao.update(webTaskList);
				}
			}
		}
		return group;
	}

	/**
	 * 更新是否重要事件
	 * @author wwluo
	 * @date 2016-09-28 
	 */
	@Override
	public boolean updateImportant(String eventId, String type,
			String ifImportant) {
		boolean flag = false;
		if (StringUtils.isNotBlank(eventId) 
				&& StringUtils.isNotBlank(type) 
				&& StringUtils.isNotBlank(ifImportant)) {
			if(CommonConstantsWeb.GROUP_S.equals(type)){
				CrmSchedule crmSchedule = (CrmSchedule) this.baseDao.get(CrmSchedule.class, eventId);
				if(crmSchedule != null){
					crmSchedule.setIfImportant(ifImportant);
					crmSchedule.setLastUpdate(new Date());
					this.baseDao.update(crmSchedule);
					flag = true;
				}
			}else if(CommonConstantsWeb.GROUP_T.equals(type)){
				WebTaskList webTaskList = (WebTaskList) this.baseDao.get(WebTaskList.class, eventId);
				if(webTaskList != null){
					webTaskList.setIfImportant(ifImportant);
					this.baseDao.update(webTaskList);
					flag = true;
				}
			}
		}
		return flag;
	}

	/**
	 * 获取一件日程
	 * @author wwluo
	 * @date 2016-09-28 
	 */
	@Override
	public ScheduleVO getScheduleInfo(String eventId, ScheduleFilterVO filter) {
		ScheduleVO target = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		if (StringUtils.isNotBlank(eventId)) {
			CrmSchedule crmSchedule = (CrmSchedule) this.baseDao.get(CrmSchedule.class, eventId);
			if(crmSchedule != null){
				target = new ScheduleVO();
				BeanUtils.copyProperties(crmSchedule, target);
				target.setCreateTime(crmSchedule.getCreateTime());
				//用“T”或者“ ”空格分隔Date与Time。国际标准组织（ISO）格式，输出：2013-11-04T14:03:05.420Z
				if(null != crmSchedule.getStartDate() && null != crmSchedule.getStartTime()){
					target.setStart(formatter.format(crmSchedule.getStartDate())+"T"+crmSchedule.getStartTime().toString());
				}else if(null!=crmSchedule.getStartDate()){
					target.setStart(formatter.format(crmSchedule.getStartDate()));
				}
				if(null != crmSchedule.getStartDate() && null != crmSchedule.getEndTime()){
					target.setEnd(formatter.format(crmSchedule.getEndDate())+"T"+crmSchedule.getEndTime().toString());
				}else if(null!=crmSchedule.getEndDate()){
					target.setEnd(formatter.format(crmSchedule.getEndDate()));
				}
				if(null != crmSchedule.getGroup()){
					target.setColor(crmSchedule.getGroup().getColor());
					target.setGroupId(crmSchedule.getGroup().getId());
					target.setGroupName(crmSchedule.getGroup().getGroupName());
				}
				String customerId = null;
	        	String customerName = null;
	        	if(crmSchedule.getCustomer() != null){
	        		customerId = crmSchedule.getCustomer().getId();
	        		customerName = crmSchedule.getCustomer().getNickName();
	        	}
	        	target.setCustomerId(customerId);
	        	target.setCustomerName(customerName);
	        	target.setCreatorId(crmSchedule.getCreator().getId());
	        	target.setCreatorName(crmSchedule.getCreator().getNickName());
	        	target.setIfImportant(crmSchedule.getIfImportant());
	        	if(crmSchedule.getRepeatFrom() != null){
	        		target.setRepeatFrom(formatter.format(crmSchedule.getRepeatFrom()));
	        	}
	        	if(crmSchedule.getRepeatTo() != null){
	        		target.setRepeatTo(formatter.format(crmSchedule.getRepeatTo()));
	        	}
	        	if (StringUtils.isNotBlank(crmSchedule.getRemindSetting())) {
	        		String langCode = filter.getLangCode();
	        		if (StringUtils.isBlank(langCode)) {
	        			langCode = CommonConstants.DEF_LANG_CODE;
					}
	        		String remindSettingName = this.getParamConfigName(langCode, CommonConstantsWeb.SYS_PARM_TYPE_REMIND_TIME+"_"+crmSchedule.getRemindSetting(),CommonConstantsWeb.SYS_PARM_TYPE_REMIND_TIME);
	        		target.setRemindSettingName(remindSettingName);
	        		target.setRemindSetting(crmSchedule.getRemindSetting());
	        	}
			}
		}
		return target;
	}
}


















