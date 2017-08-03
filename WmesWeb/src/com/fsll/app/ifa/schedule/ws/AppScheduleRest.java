package com.fsll.app.ifa.schedule.ws;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.ifa.schedule.service.AppScheduleService;
import com.fsll.app.ifa.schedule.vo.AppScheduleGroupVO;
import com.fsll.app.ifa.schedule.vo.AppScheduleItemVO;
import com.fsll.app.ifa.schedule.vo.AppScheduleTipsVO;
import com.fsll.app.ifa.schedule.vo.AppScheduleVO;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * 日程接口
 * @author zxtan
 * @date 2017-03-23
 */
@RestController
@RequestMapping("/service/ifa/schedule")
public class AppScheduleRest extends WmesBaseRest {

	@Autowired
	private AppScheduleService scheduleService;
	
	/**
	 *获取日程分组
	 *@author zxtan
	 *@date 2017-03-23
	 * 调用示例:[地址]/service/ifa/schedule/findScheduleGroupList.r
	 * {"memberId":"40280ad455e327690155e3291c582000"}
	 */
	@RequestMapping("/findScheduleGroupList")
	@ResponseBody
	public ResultWS findScheduleGroupList(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String memberId = jsonObject.optString("memberId","");
		
		if("".equals(memberId)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		List<AppScheduleGroupVO> list = scheduleService.findScheduleGroupList(memberId);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	
	/**
	 * 获取日程安排
	 *@author zxtan
	 *@date 2017-03-23
	 * 调用示例:[地址]/service/ifa/schedule/findScheduleList.r
	 * {"page":1, "rows":15,"memberId":"40280ad455e327690155e3291c582000","groupId":"40280a705a849f2b015a84c814050002","sort":"endTime"}
	 */
	@RequestMapping(value = "/findScheduleList")
	@ResponseBody
	public ResultWS findScheduleList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		Integer page = jsonObject.optInt("page", 1);// 获得当前页数
		Integer rows = jsonObject.optInt("rows", 10);// 获得每页记录数
		String memberId = jsonObject.optString("memberId", "");// 获得当前登录用户ID
		String groupId = jsonObject.optString("groupId", "");// 分组Id
		String sort = jsonObject.optString("sort", "endTime");//排序字段
		sort = "".equals(sort)?"endTime":sort;
		if ("".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);

		jsonPaging = scheduleService.findScheduleList(jsonPaging, memberId,"",groupId,sort);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(jsonPaging.getList());
		result.setCurPage(jsonPaging.getPage());
		result.setPageSize(jsonPaging.getRows());
		result.setTotal(jsonPaging.getTotal());
		return result;
	}
	
	/**
	 * 获取日程提醒
	 * @author zxtan
	 * @date 2017-04-24
	 * 调用示例:[地址]/service/ifa/schedule/findScheduleTipsList.r
	 * {"memberId":"40280ad455e327690155e3291c582000","startDate":"2017-03-01","endDate":"2017-04-30"}
	 */
	@RequestMapping(value = "/findScheduleTipsList")
	@ResponseBody
	public ResultWS findScheduleTipsList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}

		String memberId = jsonObject.optString("memberId", "");// 获得当前登录用户ID
		String startDate = jsonObject.optString("startDate", "");// 分组Id
		String endDate = jsonObject.optString("endDate", "");//排序字段
		
		if ("".equals(memberId) || "".equals(startDate) || "".equals(endDate)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}

		List<AppScheduleTipsVO> list = scheduleService.findScheduleTipsList( memberId,DateUtil.getDate(startDate),DateUtil.getDate(endDate));

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	
	/**
	 *获取某天的日程安排
	 *@author zxtan
	 *@date 2017-03-23
	 * 调用示例:[地址]/service/ifa/schedule/findScheduleListByDate.r
	 * {"memberId":"40280ad455e327690155e3291c582000","date":"2017-03-25"}
	 */
	@RequestMapping("/findScheduleListByDate")
	@ResponseBody
	public ResultWS findScheduleListByDate(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String memberId = jsonObject.optString("memberId","");
		String date = jsonObject.optString("date","");
		
		if("".equals(memberId)|| "".equals(date)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		Date dt = DateUtil.getDate(date);
		
		List<AppScheduleItemVO> list = scheduleService.findScheduleListByDate(memberId,dt);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 *获取日程详情
	 *@author zxtan
	 *@date 2017-04-25
	 * 调用示例:[地址]/service/ifa/schedule/findScheduleInfo.r
	 * {"scheduleId":"40280ad55b9f72ec015b9f8a27c40005"}
	 */
	@RequestMapping("/findScheduleInfo")
	@ResponseBody
	public ResultWS findScheduleInfo(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String scheduleId = jsonObject.optString("scheduleId","");
				
		if("".equals(scheduleId)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
				
		AppScheduleVO info = scheduleService.findScheduleInfo(scheduleId);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(info);
		return result;
	}
	
	/**
	 *更新/删除策略
	 *@author zxtan
	 *@date 2017-03-23
	 * 调用示例:[地址]/service/ifa/market/schedule/updateSchedule.r
	 * {"id":"40280a705aa6e766015aa6efc605a001","isValid":"0"}
	 */
	@RequestMapping("/updateSchedule")
	@ResponseBody
	public ResultWS updateSchedule(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String title = jsonObject.optString("title","");
		String customerId = jsonObject.optString("customerId","");
		String startTime = jsonObject.optString("startTime","");
		String endTime = jsonObject.optString("endTime","");
		
		if("".equals(title)||"".equals(startTime)||"".equals(endTime)||"".equals(customerId)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		boolean flag = false;
		flag = scheduleService.updateSchedule(jsonObject);
		map.put("flag", flag);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(map);
		return result;
	}
	
	/**
	 *更新/删除日程
	 *@author zxtan
	 *@date 2017-03-23
	 * 调用示例:[地址]/service/ifa/market/schedule/deleteSchedule.r
	 * {"id":"40280a705aa6e766015aa6efc605a001"}
	 */
	@RequestMapping("/deleteSchedule")
	@ResponseBody
	public ResultWS deleteSchedule(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String id = jsonObject.optString("id","");
		
		if( "".equals(id)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		boolean flag = false;
		flag = scheduleService.deleteSchedule(id);
		map.put("flag", flag);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(map);
		return result;
	}
}
