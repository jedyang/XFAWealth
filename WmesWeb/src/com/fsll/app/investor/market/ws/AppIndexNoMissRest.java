package com.fsll.app.investor.market.ws;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.investor.market.service.AppIndexNoMissService;
import com.fsll.app.investor.market.vo.AppIndexNoMissVo;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * 首页不容错过接口服务
 * @author zpzhou
 * @date 2016-9-26
 */
@RestController
@RequestMapping("/service/indexNoMiss")
public class AppIndexNoMissRest extends WmesBaseRest {

	@Autowired
	private AppIndexNoMissService indexNoMissService;

	/**
	 * 得到首页不容错过信息
	 * 调用示例:[地址]/service/indexNoMiss/findIndexNoMiss.r
	 * {"langCode":"sc","period":"return_period_code_1M" }
	 */
	@RequestMapping(value = "/findIndexNoMiss")
	@ResponseBody
	public ResultWS findIndexNoMiss(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String memberId = jsonObject.optString("memberId", "");// 用户Id
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);
		String period = jsonObject.optString("period",CommonConstants.RETURN_PERIOD_CODE_1M);
		
		if ("".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		List<AppIndexNoMissVo> list= indexNoMissService.findIndexNoMiss(memberId,langCode,period);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 得到首页更多的策略信息列表接口
	 * 通过body传递参数 调用示例:[地址]/service/indexNoMiss/findNoMissStrategyMore.r
	 * {"page":1, "rows":2}
	 */
	@RequestMapping(value = "/findNoMissStrategyMore")
	@ResponseBody
	public ResultWS findNoMissStrategyMore(HttpServletRequest request,@RequestBody String body) {
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
		if ("".equals(page) || "".equals(rows)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(Integer.parseInt(page));
		jsonPaging.setRows(Integer.parseInt(rows));

		jsonPaging = indexNoMissService.findNoMissStrategyMore(jsonPaging);

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
	 * 得到首页更多的组合信息列表接口
	 * 通过body传递参数 调用示例:[地址]/service/indexNoMiss/findNoMissPortfolioMore.r
	 * {"page":1, "rows":2}
	 */
	@RequestMapping(value = "/findNoMissPortfolioMore")
	@ResponseBody
	public ResultWS findNoMissPortfolioMore(HttpServletRequest request,@RequestBody String body) {
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
		if ("".equals(page) || "".equals(rows)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(Integer.parseInt(page));
		jsonPaging.setRows(Integer.parseInt(rows));

		jsonPaging = indexNoMissService.findNoMissPortfolioMore(jsonPaging);

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
	 * 得到首页更多的基金信息列表接口
	 * 通过body传递参数 调用示例:[地址]/service/indexNoMiss/findNoMissFundMore.r
	 * {"page":1, "rows":2}
	 */
	@RequestMapping(value = "/findNoMissFundMore")
	@ResponseBody
	public ResultWS findNoMissFundMore(HttpServletRequest request,@RequestBody String body) {
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
		if ("".equals(page) || "".equals(rows)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(Integer.parseInt(page));
		jsonPaging.setRows(Integer.parseInt(rows));
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);
		String period = jsonObject.optString("period",CommonConstants.RETURN_PERIOD_CODE_1M);

		jsonPaging = indexNoMissService.findNoMissFundMore(jsonPaging,langCode,period);

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
