package com.fsll.wmes.crm.service;

import java.util.List;

import com.fsll.wmes.crm.vo.ScheduleFilterVO;
import com.fsll.wmes.crm.vo.ScheduleVO;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.CrmSchedule;
import com.fsll.wmes.entity.CrmScheduleGroup;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;

/***
 * 业务接口类：客户日程记录
 * @author wwluo
 * @date 2016-09-06
 */
public interface CrmScheduleService {

	/***
	 * 通过memberId获取ifa
	 * @author wwluo
	 * @date 2016-09-06
	 */
	public MemberIfa getMemberIfa(String memberId);

	/***
	 * 获取ifa客户日程,客户Id（customerId）为空获取全部日程
	 * @author wwluo
	 * @date 2016-09-06
	 */
	public List<ScheduleVO> getCRMSchedule(String memberId,ScheduleFilterVO filter);

	/***
	 * 更新日程
	 * @author wwluo
	 * @date 2016-09-07
	 */
	public CrmSchedule updateSchedule(CrmSchedule crmSchedule);

	/***
	 * 根据id获取日程实体
	 * @author wwluo
	 * @date 2016-09-07
	 */
	public CrmSchedule getScheduleById(String id);

	/***
	 * 根据id获取客户实体
	 * @author wwluo
	 * @date 2016-09-07
	 */
	public CrmCustomer getCustomerById(String customerId);

	/**
	 * 删除日程（逻辑删除）
	 * @author wwluo
	 * @date 2016-09-07 
	 */
	public boolean deleteSchedule(String id);

	/**
	 * 获取日程分组
	 * @author wwluo
	 * @date 2016-09-12
	 */
	public List<CrmScheduleGroup> getScheduleGroup(String memberId);

	/**
	 * 根据id获取分组实体
	 * @author wwluo
	 * @date 2016-09-12
	 */
	public CrmScheduleGroup getCrmScheduleGroupById(String groupId);

	/**
	 * 根据id获取member实体
	 * @author wwluo
	 * @date 2016-09-12
	 */
	public MemberBase getMemberBaseById(String memberId);

	/**
	 * 创建与修改分组信息
	 * @author wwluo
	 * @date 2016-09-12
	 */
	public CrmScheduleGroup createAndUpdateGroup(CrmScheduleGroup group);

	/**
	 * 删除分组
	 * @author wwluo
	 * @date 2016-09-12 
	 */
	public boolean deleteGroup(String groupId);

	/**
	 * 获取ifa下客户
	 * @author wwluo
	 * @date 2016-09-28 
	 */
	public List<CrmCustomer> getCustomers(MemberIfa memberIfa,String type,String id);

	/**
	 * 日程更换分组
	 * @author wwluo
	 * @date 2016-09-28 
	 */
	public CrmScheduleGroup updateScheduleGroup(String groupId,
			String eventId,String type);

	/**
	 * 更新是否重要事件
	 * @author wwluo
	 * @date 2016-09-28 
	 */
	public boolean updateImportant(String eventId, String type,
			String ifImportant);

	/**
	 * 获取一件日程
	 * @author wwluo
	 * @date 2016-09-28 
	 */
	public ScheduleVO getScheduleInfo(String eventId, ScheduleFilterVO filter);

}
