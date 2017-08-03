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

import com.fsll.app.ifa.market.service.AppPortfolioArenaService;
import com.fsll.app.ifa.market.vo.AppArenaAllocationDetailVO;
import com.fsll.app.ifa.market.vo.AppArenaAllocationVO;
import com.fsll.app.ifa.market.vo.AppArenaDetailVO;
import com.fsll.app.ifa.market.vo.AppArenaProductVO;
import com.fsll.app.ifa.market.vo.AppArenaReturnVO;
import com.fsll.app.ifa.market.vo.AppArenaVO;
import com.fsll.buscore.fund.service.CorePortfolioService;
import com.fsll.buscore.fund.vo.CorePortfolioVO;
import com.fsll.common.CommonConstants;
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
@RequestMapping("/service/ifa/market/arena")
public class AppPortfolioArenaRest extends WmesBaseRest {

	@Autowired
	private AppPortfolioArenaService portfolioArenaService;
	
	@Autowired
	private CorePortfolioService corePortfolioService;

	
	/**
	 * 获取我的组合库列表
	 *@author zxtan
	 *@date 2017-03-28
	 * 调用示例:[地址]/service/ifa/market/arena/findMyPortfolioList.r
	 * {"page":1, "rows":15,"memberId":"40280ad455e327690155e3291c582000","keyword":"","status":"","isValid":"","langCode":"sc","periodCode":"return_period_code_1M"}
	 */
	@RequestMapping(value = "/findMyPortfolioList")
	@ResponseBody
	public ResultWS findMyPortfolioArenaList(HttpServletRequest request,@RequestBody String body) {
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
		String memberId = jsonObject.optString("memberId", "");// 获得当前登录用户ID
		String keyword = jsonObject.optString("keyword", "");// 搜索关键字
		String status = jsonObject.optString("status", "");//策略状态，0：草稿；1：已经发布
		String isValid = jsonObject.optString("isValid", "");//是否有效
		String periodCode = jsonObject.optString("periodCode", CommonConstants.RETURN_PERIOD_CODE_1M);//回报周期
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 获得当前语言
		
		if ( "".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);

		jsonPaging = portfolioArenaService.findMyPortfolioList(jsonPaging, memberId, keyword, status, isValid, periodCode, langCode);

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
	 * 获取组合列表
	 *@author zxtan
	 *@date 2017-04-25
	 * 调用示例:[地址]/service/ifa/market/arena/findAllPortfolioList.r
	 * {"page":1, "rows":15,"memberId":"40280ad455e327690155e3291c582000","keyword":"","langCode":"sc","periodCode":"return_period_code_1M"}
	 */
	@RequestMapping(value = "/findAllPortfolioList")
	@ResponseBody
	public ResultWS findAllPortfolioList(HttpServletRequest request,@RequestBody String body) {
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
		String memberId = jsonObject.optString("memberId", "");// 获得当前登录用户ID
//		String keyword = jsonObject.optString("keyword", "");// 搜索关键字
//		String periodCode = jsonObject.optString("periodCode", CommonConstants.RETURN_PERIOD_CODE_1M);//回报周期
//		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 获得当前语言
//		String sort = jsonObject.optString("sort","0");//排序字段,0创建时间 1回报 2风险		
//		String order = jsonObject.optString("order","DESC");//方向	
		
		if ( "".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);

		jsonPaging = portfolioArenaService.findAllPortfolioList(jsonPaging, jsonObject);

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
	 * 得到某个投资组合基本信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/ifa/market/arena/findPortfolioDetail.r
	 * {"memberId":"40280ad8888327690155e3291c580000","portfolioId":"40280a295a5ea067015a5ea3375a0003","langCode":"sc","periodCode":"fund_return_period_code_3M" }
	 * @author zxtan
	 * @date 2017-04-13
	 */
	@RequestMapping(value = "/findPortfolioDetail")
	@ResponseBody
	public ResultWS findPortfolioDetail(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);

		String memberId = jsonObject.optString("memberId", "");// 获得当前登录用户ID
		String portfolioId = jsonObject.optString("portfolioId", "");// 获得组合ID
		String periodCode = jsonObject.optString("periodCode", CommonConstants.RETURN_PERIOD_CODE_1M);// 行情时间
		if ("".equals(memberId) ||"".equals(portfolioId) || "".equals(periodCode) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
//		String startDate = getStartDate(periodCode);
		
		AppArenaDetailVO detailVO = new AppArenaDetailVO();
		//基本信息
		AppArenaVO infoVO = portfolioArenaService.findPortfolioInfoMess(memberId,portfolioId,langCode);
		//回报
		AppArenaReturnVO returnVO = portfolioArenaService.findPortfolioReturn(portfolioId, periodCode);
//		List<AppArenaCumperfVO> cumperfList = portfolioArenaService.findPortfolioCumperfList(portfolioId, startDate);
		String period = periodCode.replace("return_period_code_", "");
		List<CorePortfolioVO> cumperfList = corePortfolioService.getPortfolioReturnRate(portfolioId, period, "HKD");

		returnVO.setCumperfList(cumperfList);
		//产品信息
		List<AppArenaProductVO> productList = portfolioArenaService.findPortfolioProductList(portfolioId, langCode);
		//配置
		AppArenaAllocationVO allocationVO = new AppArenaAllocationVO();
		List<AppArenaAllocationDetailVO> sectorTypeList = portfolioArenaService.findPortfolioAllocationList(portfolioId, langCode, "sectorType");
		List<AppArenaAllocationDetailVO> fundTypeList = portfolioArenaService.findPortfolioAllocationList(portfolioId, langCode, "fundType");
		List<AppArenaAllocationDetailVO> geoAllocationList = portfolioArenaService.findPortfolioAllocationList(portfolioId, langCode, "geoAllocation");
		allocationVO.setSectorTypeList(sectorTypeList);
		allocationVO.setFundTypeList(fundTypeList);
		allocationVO.setGeoAllocationList(geoAllocationList);
		
		detailVO.setPortfolioInfo(infoVO);
		detailVO.setReturnInfo(returnVO);
		detailVO.setProductList(productList);		
		detailVO.setAllocationList(allocationVO);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(detailVO);
		return result;
	}
	
	
	/**
	 * 得到某个投资组合行情数据接口， 通过body传递参数 
	 * 调用示例:[地址]/service/ifa/market/arena/findPortfolioReturn.r
	 * {"portfolioId":"40280a25559b852e01559b8615da0002" }
	 * @author zxtan
	 * @date 2017-04-13
	 */
	@RequestMapping(value = "/findPortfolioReturn")
	@ResponseBody
	public ResultWS findPortfolioReturn(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String portfolioId = jsonObject.optString("portfolioId", "");// 获得组合ID
		String periodCode = jsonObject.optString("periodCode", CommonConstants.RETURN_PERIOD_CODE_1M);// 获得组合ID
//		String startDate = getStartDate(periodCode);		
		
		
		if ("".equals(portfolioId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		//回报
		AppArenaReturnVO returnVO = portfolioArenaService.findPortfolioReturn(portfolioId, periodCode);

		String period = periodCode.replace("return_period_code_", "");
		List<CorePortfolioVO> cumperfList = corePortfolioService.getPortfolioReturnRate(portfolioId, period, "HKD");

//		List<AppArenaCumperfVO> cumperfList = portfolioArenaService.findPortfolioCumperfList(portfolioId, startDate);
		returnVO.setCumperfList(cumperfList);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(returnVO);
		return result;
	}
	
	
	
	
	/**
	 * 得到投资组合的产品信息列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/portfolioArena/findPortfolioProductList.r
	 * {"portfolioId":"40280a25559b852e01559b8615da0002","langCode":"sc"}
	 * @author zxtan
	 * @date 2016-11-16
	 */
	@RequestMapping(value = "/findPortfolioProductList")
	@ResponseBody
	public ResultWS findPortfolioProductList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String portfolioId = jsonObject.optString("portfolioId", "");// 获得组合ID
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);
		
		if ("".equals(portfolioId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		List<AppArenaProductVO> list= portfolioArenaService.findPortfolioProductList(portfolioId,langCode);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	

	
	/**
	 * 得到某个投资组合配置情况接口， 通过body传递参数 
	 * 调用示例:[地址]/service/portfolioArena/findPortfolioAllocation.r
	 * {"portfolioId":"40280a25559b852e01559b8615da0002","langCode":"sc" }
	 * @author zxtan
	 * @date 2016-11-16
	 */
	@RequestMapping(value = "/findPortfolioAllocation")
	@ResponseBody
	public ResultWS findPortfolioAllocation(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);
		
		String portfolioId = jsonObject.optString("portfolioId", "");// 获得组合ID
		if ("".equals(portfolioId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
				
		AppArenaAllocationVO allocationVO = new AppArenaAllocationVO();
		List<AppArenaAllocationDetailVO> sectorTypeList = portfolioArenaService.findPortfolioAllocationList(portfolioId, langCode, "sectorType");
		List<AppArenaAllocationDetailVO> fundTypeList = portfolioArenaService.findPortfolioAllocationList(portfolioId, langCode, "fundType");
		List<AppArenaAllocationDetailVO> geoAllocationList = portfolioArenaService.findPortfolioAllocationList(portfolioId, langCode, "geoAllocation");
		allocationVO.setSectorTypeList(sectorTypeList);
		allocationVO.setFundTypeList(fundTypeList);
		allocationVO.setGeoAllocationList(geoAllocationList);
		
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(allocationVO);
		return result;
	}
	
	/**
	 *更新/删除组合
	 *@author zxtan
	 *@date 2017-03-23
	 * 调用示例:[地址]/service/ifa/market/arena/updatePortfolioArena.r
	 * {"updateType":"update","id":"40280jk457fb2b140157fb3a406b0001","isValid":"0"}
	 */
	@RequestMapping("/updatePortfolioArena")
	@ResponseBody
	public ResultWS updatePortfolioArena(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String updateType = jsonObject.optString("updateType","");
		String id = jsonObject.optString("id","");
		String isValid = jsonObject.optString("isValid","");
		
		if("".equals(updateType)|| "".equals(id)
				||( "update".equalsIgnoreCase(updateType) && "".equals(isValid))){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		boolean flag = false;
		flag = portfolioArenaService.updatePortfolioArena(updateType, id, isValid);
		map.put("flag", flag);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(map);
		return result;
	}
}
