package com.fsll.app.investor.market.ws;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.investor.market.service.AppIndexTopicService;
import com.fsll.app.investor.market.vo.AppIndexBbsTopicVO;
import com.fsll.app.investor.market.vo.AppIndexBbsTypeVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * 首页热门话题接口服务
 * @author zpzhou
 * @date 2016-10-09
 */
@RestController
@RequestMapping("/service/indexTopic")
public class AppIndexTopicRest extends WmesBaseRest{
	@Autowired
	private AppIndexTopicService indexTopicService;
	
	/**
	 * 得到首页热门话题模块列表信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/indexTopic/findIndexBbsType.r
	 *  {"langCode":"sc"}
	 */
	@RequestMapping(value = "/findIndexBbsType")
	@ResponseBody
	public ResultWS findIndexBbsType(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 语言编码
		List<AppIndexBbsTypeVO> list= indexTopicService.findIndexBbsType(langCode);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 得到热门话题列表信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/indexTopic/findIndexBbsTopicList.r
	 * {"page":1, "rows":15,"typeId":"2","moduleId":"1"}
	 */
	@RequestMapping(value = "/findIndexBbsTopicList")
	@ResponseBody
	public ResultWS findIndexBbsTopicList(HttpServletRequest request,@RequestBody String body) {
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
		String rows = jsonObject.optString("rows", "");// 获得每页记录数
		String typeId = jsonObject.optString("typeId", "");//类型ID  0:最热门  1：推荐  2:模块
		String moduleId = jsonObject.optString("moduleId", "");//模块ID  当typeId=2时不能为空
		if ("".equals(page) || "".equals(rows) || ("2".equals(typeId) && "".equals(moduleId))) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(Integer.parseInt(page));
		jsonPaging.setRows(Integer.parseInt(rows));

		jsonPaging = indexTopicService.findIndexBbsTopicList(jsonPaging, typeId,moduleId);

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
	 * 得到某个热门话题详细信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/indexTopic/findIndexBbsTopicMess.r
	 * {"topicId":"2"}
	 */
	@RequestMapping(value = "/findIndexBbsTopicMess")
	@ResponseBody
	public ResultWS findIndexBbsTopicMess(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String topicId = jsonObject.optString("topicId", "");//话题ID
		if ("".equals(topicId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		AppIndexBbsTopicVO vo = indexTopicService.findIndexBbsTopicMess(topicId);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(vo);
		return result;
	}
	
	/**
	 * 得到某个热门话题的回复列表信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/indexTopic/findIndexBbsReplyList.r
	 * {"page":1, "rows":15,"topicId":"2"}
	 */
	@RequestMapping(value = "/findIndexBbsReplyList")
	@ResponseBody
	public ResultWS findIndexBbsReplyList(HttpServletRequest request,@RequestBody String body) {
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
		String rows = jsonObject.optString("rows", "");// 获得每页记录数
		String topicId = jsonObject.optString("topicId", "");//话题ID
		if ("".equals(page) || "".equals(rows) || "".equals(topicId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(Integer.parseInt(page));
		jsonPaging.setRows(Integer.parseInt(rows));

		jsonPaging = indexTopicService.findIndexBbsReplyList(jsonPaging,topicId);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(jsonPaging.getList());
		result.setCurPage(jsonPaging.getPage());
		result.setPageSize(jsonPaging.getRows());
		result.setTotal(jsonPaging.getTotal());
		return result;
	}
}
