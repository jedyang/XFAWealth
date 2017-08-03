package com.fsll.app.investor.discover.ws;

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

import com.fsll.app.investor.discover.service.AppTodoService;
import com.fsll.app.investor.discover.vo.AppTodoVo;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * 待办待阅接口
 * @author zpzhou
 * @date 2016-11-02
 */
@RestController
@RequestMapping("/service/todo")
public class AppTodoRest extends WmesBaseRest {
	@Autowired
	private AppTodoService appTodoService;
	
	/**
	 * 根据参数得到相应的待办待阅列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/todo/getTodoList.r
	 * {"page":1, "rows":15,"memberId":"40280ad455de27960155de42f8990001","type":"2","isRead":"0","langCode":"sc","keyWord":""}
	 */
	@RequestMapping(value = "/getTodoList")
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
		String keyWord = jsonObject.optString("keyWord", "");// 获得查询关键词
		if ("".equals(page) || "".equals(rows) || "".equals(type) || "".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(Integer.parseInt(page));
		jsonPaging.setRows(Integer.parseInt(rows));

		jsonPaging = appTodoService.getTodoList(jsonPaging, memberId,type,isRead,langCode,keyWord);

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
	 * 调用示例:[地址]/service/todo/getLatestTodoMess.r
	 * {"memberId":"40280ad455de27960155de42f8990001","type":"2","langCode":"sc"}
	 */
	@RequestMapping(value = "/getLatestTodoMess")
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
		String type = jsonObject.optString("type", "");//消息类型，1：交易相关消息，2：一般性通知，3：告警、提醒消息
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 语言编码
		if ("".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}

		List<AppTodoVo> list = appTodoService.getLatestTodoMess(memberId,type,langCode);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 根据参数得到相应的最新待办待阅信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/todo/updateReadTodo.r
	 * {"memberId":"40280ad455de27960155de42f8990001","id":"40280a555c87021e015c870c34e80010"}
	 */
	@RequestMapping(value = "/updateReadTodo")
	@ResponseBody
	public ResultWS updateReadTodo(HttpServletRequest request,@RequestBody String body) {
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
		String id = jsonObject.optString("id", "");//消息id
		if ("".equals(memberId)|| "".equals(id)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}

		boolean flag = appTodoService.updateReadTodo(memberId, id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", flag);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(map);
		return result;
	}
}
