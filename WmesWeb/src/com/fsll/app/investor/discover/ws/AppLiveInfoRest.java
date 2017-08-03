package com.fsll.app.investor.discover.ws;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.investor.discover.service.AppLiveInfoService;
import com.fsll.app.investor.discover.service.AppNewsInfoService;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;
import com.fsll.wmes.entity.LiveCategory;

/**
 * 新闻接口实现
 * @author zxtan
 * @date 2016-11-25
 */
@RestController
@RequestMapping("/service/liveInfo")
public class AppLiveInfoRest extends WmesBaseRest {
	@Autowired
	private AppLiveInfoService liveInfoService;
	
	/**
	 * 新闻列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/liveInfo/findLiveCategoryList.r
	 * {}
	 */
	@RequestMapping(value = "/findLiveCategoryList")
	@ResponseBody
	public ResultWS findLiveCategoryList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		List<LiveCategory> list = liveInfoService.findLiveCategoryList();

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 直播视频列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/liveInfo/findLiveList.r
	 * {"categoryId":"a495cc10b2f311e6945c00155d8a2a75"}
	 */
	@RequestMapping(value = "/findLiveList")
	@ResponseBody
	public ResultWS findLiveList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		int page = jsonObject.optInt("page", 1);// 获得当前页数
		int rows = jsonObject.optInt("rows", 10);// 获得每页记录数
		String categoryId = jsonObject.optString("categoryId", "");//0或空为全部，1：关注，2：推荐
		if ("".equals(categoryId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);

		jsonPaging = liveInfoService.findLiveList(jsonPaging, categoryId);

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
