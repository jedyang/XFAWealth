package com.fsll.app.investor.me.ws;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.investor.me.service.AppInsightInfoService;
import com.fsll.app.investor.me.vo.AppInsightInfoVo;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * 观点接口实现
 * @author zpzhou
 * @date 2016-8-11
 */
@RestController
@RequestMapping("/service/insightInfo")
public class AppInsightInfoRest extends WmesBaseRest {
	@Autowired
	private AppInsightInfoService insightInfoService;
	
//	/**
//	 * 观点列表接口， 通过body传递参数 
//	 * 调用示例:[地址]/service/insightInfo/findInsightList.r
//	 * {"page":1, "rows":15,"_user_id_":"40280a25559b852e01559b8615da0002","type":"1"}
//	 */
//	@RequestMapping(value = "/findInsightList")
//	@ResponseBody
//	public ResultWS findInsightList(HttpServletRequest request,@RequestBody String body) {
//		JSONObject jsonObject = JSONObject.fromObject(body);
//		ResultWS result = new ResultWS();
//		result.setData(null);
//		if (!checkPower(jsonObject)) {
//			result.setRet(WSConstants.FAIL);
//			result.setErrorCode(WSConstants.CODE1001);
//			result.setErrorMsg(WSConstants.MSG1001);
//			return result;
//		}
//		String page = jsonObject.optString("page", "");// 获得当前页数
//		String rows = jsonObject.optString("rows", "");// 获得每页记录数
//		String userId = jsonObject.optString("_user_id_", "");// 获得当前登录用户ID
//		String type = jsonObject.optString("type", "");//0或空为全部，1：关注，2：推荐
//		
//		if ("".equals(page) && "".equals(rows)) {
//			result.setRet(WSConstants.FAIL);
//			result.setErrorCode(WSConstants.CODE1002);
//			result.setErrorMsg(WSConstants.MSG1002);
//			return result;
//		}
//		JsonPaging jsonPaging = new JsonPaging();
//		jsonPaging.setPage(Integer.parseInt(page));
//		jsonPaging.setRows(Integer.parseInt(rows));
//
//		jsonPaging = insightInfoService.findInsightList(jsonPaging, userId,type);
//
//		result.setRet(WSConstants.SUCCESS);
//		result.setErrorCode("");
//		result.setErrorMsg("");
//		result.setData(jsonPaging.getList());
//		result.setCurPage(jsonPaging.getPage());
//		result.setPageSize(jsonPaging.getRows());
//		result.setTotal(jsonPaging.getTotal());
//		return result;
//	}
//	
//	/**
//	 * 观点详细接口， 通过body传递参数 
//	 * 调用示例:[地址]/service/insightInfo/findInsightInfo.r
//	 * {"_user_id_":"40280a25559b852e01559b8615da0002","insightId":"40280a25559b852e01559b8615da0004"}
//	 */
//	@RequestMapping(value = "/findInsightInfo")
//	@ResponseBody
//	public ResultWS findInsightInfo(HttpServletRequest request,@RequestBody String body) {
//		JSONObject jsonObject = JSONObject.fromObject(body);
//		ResultWS result = new ResultWS();
//		result.setData(null);
//		if (!checkPower(jsonObject)) {
//			result.setRet(WSConstants.FAIL);
//			result.setErrorCode(WSConstants.CODE1001);
//			result.setErrorMsg(WSConstants.MSG1001);
//			return result;
//		}
//		String insightId = jsonObject.optString("insightId", "");// 观点ID
//		String userId = jsonObject.optString("_user_id_", "");// 获得当前登录用户ID
//		
//		if ("".equals(userId)) {
//			result.setRet(WSConstants.FAIL);
//			result.setErrorCode(WSConstants.CODE1002);
//			result.setErrorMsg(WSConstants.MSG1002);
//			return result;
//		}
//		AppInsightInfoVo vo = insightInfoService.findInsightInfo(insightId, userId);
//		result.setRet(WSConstants.SUCCESS);
//		result.setErrorCode("");
//		result.setErrorMsg("");
//		result.setData(vo);
//		return result;
//	}
}
