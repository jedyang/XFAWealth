package com.fsll.app.common.ws;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.common.service.AppWebReadToDoService;
import com.fsll.app.common.vo.AppLatestTodoVO;
import com.fsll.app.member.service.AppMemberBaseService;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.WebReadToDo;

/**
 * 控制器:消息管理
 * 
 * @author michael
 * @date 2016-10-13
 */
@RestController
@RequestMapping("/service/webReadToDo")
public class AppWebReadToDoRest extends WmesBaseRest{	
	@Autowired
	private AppWebReadToDoService webReadToDoService;
	
	@Autowired
	private AppMemberBaseService memberBaseService;
	
	/**
	 * 发送一条信息
	 * 调用示例:[地址]/service/webReadToDo/send.r
	 * {"type":"2","fromUserId":"40280ad455e327690155e3291c580000","toUserId":"40280ad455de27960155de42f8990001","module":"insight","relateId":"40280a25559b852e01559b8615da0006","title":"测试"}
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
		WebReadToDo readToDo = webReadToDoService.sendToRead(jsonObject.optString("type", ""), jsonObject.optString("module", ""), jsonObject.optString("relateId", ""), jsonObject.optString("title", ""), fromUser, toUser);

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
		return result;
	}
	
	
	/**
	 * 根据参数得到相应的待办待阅列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/ifa/discover/todo/findTodoList.r
	 * {"page":1, "rows":15,"memberId":"40280ad455de27960155de42f8990001","type":"2","isRead":"0","langCode":"sc","keyword":""}
	 */
	@RequestMapping(value = "/findTodoList")
	@ResponseBody
	public ResultWS getTodoList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String page = jsonObject.optString("page", "");// 获得当前页数
		String rows = jsonObject.optString("rows", "");// 获得每页记录数isRead
		String memberId = jsonObject.optString("memberId", "");// 获得当前登录用户ID
		String isRead = jsonObject.optString("isRead", "");// 是否已阅,1已阅,0待阅
		String type = jsonObject.optString("type", "");//消息类型，1：交易相关消息，2：一般性通知，3：告警、提醒消息
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 语言编码
		String keyword = jsonObject.optString("keyword", "");// 获得查询关键词
		if ("".equals(page) || "".equals(rows) || "".equals(type) || "".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(Integer.parseInt(page));
		jsonPaging.setRows(Integer.parseInt(rows));

		jsonPaging = webReadToDoService.getTodoList(jsonPaging, memberId,type,isRead,langCode,keyword);

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
	 * 根据参数得到相应的最新待办待阅信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/ifa/discover/todo/findLatestTodoInfo.r
	 * {"memberId":"40280ad455de27960155de42f8990001","langCode":"sc"}
	 */
	@RequestMapping(value = "/findLatestTodoInfo")
	@ResponseBody
	public ResultWS getLatestTodoMess(HttpServletRequest request,@RequestBody String body) {
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
//		String type = jsonObject.optString("type", "");//消息类型，1：交易相关消息，2：一般性通知，3：告警、提醒消息
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 语言编码
		if ("".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}

		AppLatestTodoVO info = webReadToDoService.getLatestTodoMess(memberId,langCode);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(info);
		return result;
	}
	
}
