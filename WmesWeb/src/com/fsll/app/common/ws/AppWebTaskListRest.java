package com.fsll.app.common.ws;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.common.service.AppWebTaskListService;
import com.fsll.app.member.service.AppMemberBaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.core.CoreConstants;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.WebTaskList;

/**
 * 控制器:待办管理
 * 
 * @author michael
 * @date 2016-10-13
 */
@RestController
@RequestMapping("/service/webTaskList")
public class AppWebTaskListRest extends WmesBaseRest{	
	@Autowired
	private AppWebTaskListService webTaskListService;
	
	@Autowired
	private AppMemberBaseService memberBaseService;
	
	/**
	 * 发送一个待办
	 * 调用示例:[地址]/service/webTaskList/send.r
	 * {"type":"task_type_01","fromUserId":"40280ad455e327690155e3291c580000","toUserId":"40280ad455de27960155de42f8990001","module":"K","relateId":"40280a25559b852e01559b8615da0006","title":"测试","targetDate":"2016-10-30"}
	 */
	@RequestMapping(value = "/send")
	@ResponseBody
	public ResultWS send(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		result = parseParam(request,jsonObject,result,"send");
		if(WSConstants.FAIL.equals(result.getRet())){
			return result;
		}
		
		MemberBase fromUser = memberBaseService.findById(jsonObject.getString("fromUserId"));
		MemberBase toUser = memberBaseService.findById(jsonObject.getString("toUserId"));
		Date targetDate = DateUtil.StringToDate(jsonObject.optString("targetDate", ""), CoreConstants.DATE_FORMAT);
		WebTaskList readToDo = webTaskListService.sendToDo(jsonObject.optString("type", ""), jsonObject.optString("module", ""), jsonObject.optString("relateId", ""), jsonObject.optString("title", ""),targetDate, fromUser, toUser);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(readToDo.getId());
		return result;
	}
	
	/**
	 * 分析参数,必需参数是否为空
	 * @param request
	 * @param jsonObject
	 * @param result
	 * @param oper add mod
	 * @return
	 */
	private ResultWS parseParam(HttpServletRequest request,JSONObject jsonObject,ResultWS result,String oper) {
		String title = jsonObject.get("title") == null ? "" : jsonObject.getString("title");
		String type = jsonObject.get("type") == null ? "" : jsonObject.getString("type");
		String fromUserId = jsonObject.get("fromUserId") == null ? "" : jsonObject.getString("fromUserId");
		String toUserId = jsonObject.get("toUserId") == null ? "" : jsonObject.getString("toUserId");
		String module = jsonObject.get("module") == null ? "" : jsonObject.getString("module");
		String relateId = jsonObject.get("relateId") == null ? "" : jsonObject.getString("relateId");
		String targetDate = jsonObject.get("targetDate") == null ? "" : jsonObject.getString("targetDate");
//		if("send".equals(oper)){
//			String id = jsonObject.get("id") == null ? "" : jsonObject.getString("id");
//			if("".equals(id)){
//				result.setRet(WSConstants.FAIL);
//				result.setErrorCode(WSConstants.CODE1002);
//				result.setErrorMsg("“id”"+WSConstants.MSG1002);
//				return result;
//			}
//		}
		if("".equals(title)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg("“title”"+WSConstants.MSG1002);
			return result;
		}
		if("".equals(type)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg("“type”"+WSConstants.MSG1002);
			return result;
		}
		if("".equals(fromUserId)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg("“fromUserId”"+WSConstants.MSG1002);
			return result;
		}
		if("".equals(toUserId)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg("“toUserId”"+WSConstants.MSG1002);
			return result;
		}
		if("".equals(module)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg("“module”"+WSConstants.MSG1002);
			return result;
		}
		if("".equals(relateId)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg("“relateId”"+WSConstants.MSG1002);
			return result;
		}
		if("".equals(targetDate)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg("“targetDate”"+WSConstants.MSG1002);
			return result;
		}
		return result;
	}
	
}
