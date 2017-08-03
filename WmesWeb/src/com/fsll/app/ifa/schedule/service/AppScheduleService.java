package com.fsll.app.ifa.schedule.service;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import com.fsll.app.ifa.schedule.vo.AppScheduleGroupVO;
import com.fsll.app.ifa.schedule.vo.AppScheduleItemVO;
import com.fsll.app.ifa.schedule.vo.AppScheduleTipsVO;
import com.fsll.app.ifa.schedule.vo.AppScheduleVO;
import com.fsll.common.util.JsonPaging;

/**
 * 日程接口Service
 * @author zxtan
 * @date 2017-03-23
 */
public interface AppScheduleService {

	/**
	 * 获取我的分组列表
	 *@author zxtan
	 *@date 2017-03-23
	 * @param memberId Ifa Member Id
	 * @return
	 */
	public List<AppScheduleGroupVO> findScheduleGroupList(String memberId);
	
	/**
	 * 获取我的日程列表（分页）
	 *@author zxtan
	 *@date 2017-03-23
	 * @param jsonPaging
	 * @param ifaMemberId Ifa Member Id
	 * @param clientMemberId Ifa Member Id
	 * @param groupId 分组Id
	 * @param sort 排序字段
	 * @return
	 */
	public JsonPaging findScheduleList(JsonPaging jsonPaging,String ifaMemberId,String clientMemberId,String groupId,String sort);
	
	/**
	 * 获取日程提醒
	 * @author zxtan
	 * @date 2017-04-24
	 */
	public List<AppScheduleTipsVO> findScheduleTipsList( String ifaMemberId,Date startDate,Date endDate);
	/**
	 * 获取我某一天的日程
	 *@author zxtan
	 *@date 2017-03-23
	 * @param memberId  Ifa Member Id
	 * @param date
	 * @return
	 */
	public List<AppScheduleItemVO> findScheduleListByDate(String memberId,Date date);
	
	/**
	 * 获取日程详情
	 *@author zxtan
	 *@date 2017-04-25
	 * @param scheduleId  
	 * @return
	 */
	public AppScheduleVO findScheduleInfo(String scheduleId);
	
	/**
	 * 新增、更新日程
	 * @author zxtan
	 * @date 2017-03-30
	 * @param jsonObject
	 * @return
	 */
	public boolean updateSchedule(JSONObject jsonObject);
	
	/**
	 * 删除日程（逻辑删除）
	 * @author zxtan
	 * @date 2017-03-30
	 * @param id
	 * @return
	 */
	public boolean deleteSchedule(String id);
}
