package com.fsll.app.ifa.market.ws;

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

import com.fsll.app.ifa.market.service.AppStrategyInfoService;
import com.fsll.app.ifa.market.vo.AppStrategyAllocationVO;
import com.fsll.app.ifa.market.vo.AppStrategyDetailVO;
import com.fsll.app.ifa.market.vo.AppStrategyInfoVO;
import com.fsll.app.ifa.market.vo.AppStrategyProductVo;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * 策略接口
 * @author zxtan
 * @date 2017-03-23
 */
@RestController
@RequestMapping("/service/ifa/market/strategy")
public class AppStrategyInfoRest extends WmesBaseRest {

	@Autowired
	private AppStrategyInfoService strategyInfoService;
	

	
	/**
	 * 获取我的策略管理列表
	 *@author zxtan
	 *@date 2017-03-23
	 * 调用示例:[地址]/service/ifa/market/strategy/findMyStrategyList.r
	 * {"page":1, "rows":15,"memberId":"40280ad455e327690155e3291c580000","keyword":"","status":"","isValid":"1","langCode":"sc"}
	 */
	@RequestMapping(value = "/findMyStrategyList")
	@ResponseBody
	public ResultWS findStrategyList(HttpServletRequest request,@RequestBody String body) {
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
		Integer rows = jsonObject.optInt("rows", 10);// 获得每页记录数isRead
		String memberId = jsonObject.optString("memberId", "");// 获得当前登录IFA用户ID
		String keyword = jsonObject.optString("keyword", "");// 搜索关键字
		String status = jsonObject.optString("status", "");//策略状态，0：草稿；1：已经发布
		String isValid ="1"; //jsonObject.optString("isValid", "1");//是否有效
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 获得当前语言
		
		if ("".equals(memberId) || "".equals(langCode)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);

		jsonPaging = strategyInfoService.findMyStrategyInfoList(jsonPaging, memberId, keyword, status, isValid,langCode);

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
	 * 获取我的策略列表
	 *@author zxtan
	 *@date 2017-03-23
	 * 调用示例:[地址]/service/ifa/market/strategy/findAllStrategyInfoList.r
	 * {"page":1, "rows":15,"memberId":"40280ad455e327690155e3291c580000","keyword":"","langCode":"sc"}
	 */
	@RequestMapping(value = "/findAllStrategyList")
	@ResponseBody
	public ResultWS findAllStrategyList(HttpServletRequest request,@RequestBody String body) {
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
		Integer rows = jsonObject.optInt("rows", 10);// 获得每页记录数isRead
		String memberId = jsonObject.optString("memberId", "");// 获得当前登录IFA用户ID
		String keyword = jsonObject.optString("keyword", "");// 搜索关键字
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 获得当前语言
		
		if ("".equals(memberId) || "".equals(langCode)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);

		jsonPaging = strategyInfoService.findAllStrategyInfoList(jsonPaging, memberId, keyword, langCode);

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
	 * 得到某个策略配置详情接口， 通过body传递参数 
	 * 调用示例:[地址]/service/ifa/market/strategy/findStrategyDetail.r
	 * {"strategyId":"40280ad45707c1fa015707d8acfe0007","langCode":"sc","periodCode":"return_period_code_1M","allocationType":"fund","productType":"fund","productNum":5,"toCurrency":"USD"}
	 */
	@RequestMapping(value = "/findStrategyDetail")
	@ResponseBody
	public ResultWS findStrategyDetail(HttpServletRequest request,@RequestBody String body) {
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
		String strategyId = jsonObject.optString("strategyId", "");// 获得策略ID
		String allocationType = jsonObject.optString("allocationType", "");//分类类型：fund基金,stock股票,bond债券
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);
		String periodCode = jsonObject.optString("periodCode", "");
		String toCurrency = jsonObject.optString("toCurrency",CommonConstants.DEF_CURRENCY);
		String productType = jsonObject.optString("productType", "");//产品类型,fund基金,stock股票,bond债券,futures期货  ALL全部
		int productNum = jsonObject.optInt("productNum", 5);//产品数量
		
		if ( "".equals(strategyId) || "".equals(allocationType) || "".equals(periodCode) || "".equals(productType) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		AppStrategyInfoVO strategyInfo = strategyInfoService.findStrategyInfo(memberId, strategyId, langCode);
		List<AppStrategyAllocationVO> allocationList = strategyInfoService.findStrategyAllocationList(strategyId, allocationType, langCode);
		List<AppStrategyProductVo> productList = strategyInfoService.findStrategyProductList(strategyId, langCode, periodCode, productType,productNum, toCurrency);
				
		AppStrategyDetailVO detailVO = new AppStrategyDetailVO();
		detailVO.setAppStrategyInfo(strategyInfo);
		detailVO.setAllocationList(allocationList);
		detailVO.setProductList(productList);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(detailVO);
		return result;
	}
	
	
	/**
	 *更新策略
	 *@author zxtan
	 *@date 2017-03-23
	 * 调用示例:[地址]/service/ifa/market/strategy/updateStrategyInfo.r
	 * {"id":"40280jk457fb2b140157fb3a406b0001","isValid":"0"}
	 */
	@RequestMapping("/updateStrategyInfo")
	@ResponseBody
	public ResultWS updateStrategyInfo(HttpServletRequest request,@RequestBody String body){
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
		String isValid = jsonObject.optString("isValid","");
		
		if("".equals(id) || "".equals(isValid)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
				
		Map<String, Object> map = new HashMap<String, Object>();
		
		boolean flag = false;
		flag = strategyInfoService.updateStrategyInfo(id, isValid);
		map.put("flag", flag);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(map);
		return result;
	}
	
	/**
	 *删除策略
	 *@author zxtan
	 *@date 2017-03-23
	 * 调用示例:[地址]/service/ifa/market/strategy/deleteStrategyInfo.r
	 * {"id":"40280jk457fb2b140157fb3a406b0001"}
	 */
	@RequestMapping("/deleteStrategyInfo")
	@ResponseBody
	public ResultWS deleteStrategyInfo(HttpServletRequest request,@RequestBody String body){
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
		
		if("".equals(id)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
				
		Map<String, Object> map = new HashMap<String, Object>();
		
		boolean flag = false;
		flag = strategyInfoService.deleteStrategyInfo(id);
		map.put("flag", flag);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(map);
		return result;
	}
}
