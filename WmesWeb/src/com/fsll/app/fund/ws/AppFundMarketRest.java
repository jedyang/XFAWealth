package com.fsll.app.fund.ws;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.fund.service.AppFundMarketService;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.StrUtils;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * 获取基金净值列表
 * @author michale
 * @date 20160615
 */
@RestController
@RequestMapping("/service/fundMarket")
public class AppFundMarketRest extends WmesBaseRest{
	@Autowired
	private AppFundMarketService fundMarketService;
	
	/**
	 * 获取基金净值列表， 通过body传递参数
	 * 调用示例:[地址]/service/fundMarket/getFundMarketList.r
	   {"langCode":"sc", "page":1, "rows":5, "fundId":"8F1E79BAAFA643A6818C364413ED0901","toCurrency":"USD","period":"day", "startDate":"2016-06-01", "endDate":"2017-06-01" }
	 */
	@RequestMapping(value = "/getFundMarketList")
	@ResponseBody
	public ResultWS getFundMarketList(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}

		String langCode=jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		String fundId=jsonObject.optString("fundId","");
		String page = jsonObject.optString("page", "");// 获得当前页数
		String rows = jsonObject.optString("rows", "");// 获得每页记录数
		String period = jsonObject.optString("period", "");// 时期类型: day, week, month
		String startDate = jsonObject.optString("startDate", "");// 获得开始时间，可空 -- 从有记录以来
		String endDate = jsonObject.optString("endDate", "");// 获得结束时间，可空 -- 到最新日期
		String toCurrency = jsonObject.optString("toCurrency", "");// 获得货币，可空 -- 直接显示源价
		
		if ("".equals(page) || "".equals(rows) || "".equals(period)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		//设置分页信息
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(StrUtils.getInt(page));
		jsonPaging.setRows(StrUtils.getInt(rows));
		jsonPaging.setSort("marketDate");
		jsonPaging.setOrder("desc");
		
		//获取数据
		jsonPaging = fundMarketService.findFundMarketList(jsonPaging, fundId,  period, startDate, endDate, langCode, toCurrency);
		
		
		//设置返回对象
		if(jsonPaging.getList().isEmpty()){
			result.setRet(WSConstants.SUCCESS);
			result.setErrorCode(WSConstants.CODE2001);
			result.setErrorMsg(WSConstants.MSG2001);
			return result;
		}else{
			result.setRet(WSConstants.SUCCESS);
			result.setErrorCode("");
			result.setErrorMsg("");
			result.setData(jsonPaging.getList());
			result.setCurPage(jsonPaging.getPage());
			result.setPageSize(jsonPaging.getRows());
			result.setTotal(jsonPaging.getTotal());
		}
		return result;
	}
	
}
