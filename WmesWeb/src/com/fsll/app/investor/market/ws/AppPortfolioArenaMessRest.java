package com.fsll.app.investor.market.ws;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.investor.market.service.AppPortfolioArenaMessService;
import com.fsll.app.investor.market.vo.AppPortfolioArenaAllocationDetailVO;
import com.fsll.app.investor.market.vo.AppPortfolioArenaAllocationVO;
import com.fsll.app.investor.market.vo.AppPortfolioArenaCumperfVO;
import com.fsll.app.investor.market.vo.AppPortfolioArenaDetailVO;
import com.fsll.app.investor.market.vo.AppPortfolioArenaMarketMessVo;
import com.fsll.app.investor.market.vo.AppPortfolioArenaMessVo;
import com.fsll.app.investor.market.vo.AppPortfolioArenaProductVo;
import com.fsll.app.investor.market.vo.AppPortfolioArenaReturnVO;
import com.fsll.buscore.fund.service.CorePortfolioService;
import com.fsll.buscore.fund.vo.CorePortfolioVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * 投资组合用于展示的接口服务
 * @author zpzhou
 * @date 2016-9-14
 */
@RestController
@RequestMapping("/service/portfolioArena")
public class AppPortfolioArenaMessRest extends WmesBaseRest {

	@Autowired
	private AppPortfolioArenaMessService portfolioArenaMessService;
	@Autowired
	private CorePortfolioService corePortfolioService;
	
	/**
	 * 得到首页最佳组合更多列表信息
	 * 通过body传递参数 调用示例:[地址]/service/portfolioArena/findBestPortfolioList.r
	 * {"langCode":"sc", "page":1,"periodCode":"return_period_code_1M","rows":2,"memberId":"ALOQ_JPUwJgGqbZRsQD4qw5xyJKnqwpIOKRh"}
	 */
	@RequestMapping(value = "/findBestPortfolioList")
	@ResponseBody
	public ResultWS findBestPortfolioList(HttpServletRequest request,@RequestBody String body) {
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
		String rows = "5";// jsonObject.optString("rows", "");// 获得每页记录数
		String periodCode = jsonObject.optString("periodCode",CommonConstants.RETURN_PERIOD_CODE_3M);
		String memberId = jsonObject.optString("memberId", "");// 获得当前登录用户ID
		String langCode=jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		if ("".equals(page)||"".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(Integer.parseInt(page));
		jsonPaging.setRows(Integer.parseInt(rows));

		jsonPaging = portfolioArenaMessService.findBestPortfolioList(jsonPaging,memberId,langCode, periodCode);

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
	 * 得到我有权限查看的投资组合列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/portfolioArena/findPortfolioList.r
	 * { "page":1,"periodCode":"return_period_code_1M","rows":2,"memberId":"40280ad8888327690155e3291c580000","keyWord":"组合A"}
	 */
	@RequestMapping(value = "/findPortfolioList")
	@ResponseBody
	public ResultWS findPortfolioList(HttpServletRequest request,@RequestBody String body) {
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
		String memberId = jsonObject.optString("memberId", "");// 获得当前登录用户ID
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 语言编码
		//String toCurrency = jsonObject.optString("toCurrency",CommonConstants.DEF_CURRENCY);
		String periodCode = jsonObject.optString("periodCode",CommonConstants.RETURN_PERIOD_CODE_1M);
		String keyWord = jsonObject.optString("keyWord", "");// 获得查询关键词
		int orderBy = jsonObject.optInt("orderBy", 0);//排序字段,0创建时间 倒序 1回报倒序 2回报顺序3风险倒序4风险顺序

		if ("".equals(page)||"".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(Integer.parseInt(page));
		jsonPaging.setRows(Integer.parseInt(rows));
		switch (orderBy) {
		case 0:
			jsonPaging.setSort("s.createTime");
			jsonPaging.setOrder("DESC");
			break;
		case 1:
			jsonPaging.setSort("r.increase");
			jsonPaging.setOrder("DESC");
			break;
		case 2:
			jsonPaging.setSort("r.increase");
			jsonPaging.setOrder("ASC");
			break;
		case 3:
			jsonPaging.setSort("s.riskLevel");
			jsonPaging.setOrder("DESC");
			break;
		case 4:
			jsonPaging.setSort("s.riskLevel");
			jsonPaging.setOrder("ASC");
			break;

		default:
			jsonPaging.setSort("s.createTime");
			jsonPaging.setOrder("DESC");
			break;
		}
		
		jsonPaging= portfolioArenaMessService.findPortfolioList(jsonPaging,memberId,periodCode,keyWord,langCode);
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
	 * 调用示例:[地址]/service/portfolioArena/findPortfolioInfo.r
	 * {"memberId":"40280ad8888327690155e3291c580000","portfolioId":"40280a25559b852e01559b8615da0002" }
	 * @author zxtan
	 * @date 2016-11-16
	 */
	@RequestMapping(value = "/findPortfolioInfo")
	@ResponseBody
	public ResultWS findPortfolioInfo(HttpServletRequest request,@RequestBody String body) {
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
		String portfolioId = jsonObject.optString("portfolioId", "");// 获得组合ID
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 语言编码
		if ("".equals(memberId) ||"".equals(portfolioId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		AppPortfolioArenaMessVo vo= portfolioArenaMessService.findPortfolioInfoMess(memberId,portfolioId,langCode);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(vo);
		return result;
	}
	
	/**
	 * 得到某个投资组合行情数据接口， 通过body传递参数 
	 * 调用示例:[地址]/service/portfolioArena/findPortfolioReturn.r
	 * {"portfolioId":"40280a25559b852e01559b8615da0002" }
	 * @author zxtan
	 * @date 2016-11-16
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
//		if ("1W".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, -7);
//		String startDate = getStartDate(periodCode);		
			
		if ("".equals(portfolioId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		AppPortfolioArenaReturnVO returnVO = portfolioArenaMessService.findPortfolioReturn(portfolioId, periodCode);
//		List<AppPortfolioArenaCumperfVO> cumperfList = portfolioArenaMessService.findPortfolioCumperfList(portfolioId,startDate);
		String period = periodCode.replace("return_period_code_", "");
		List<CorePortfolioVO> cumperfList = corePortfolioService.getPortfolioReturnRate(portfolioId, period, "HKD");
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
		
		List<AppPortfolioArenaProductVo> list= portfolioArenaMessService.findPortfolioProductList(portfolioId,langCode);
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
				
		AppPortfolioArenaAllocationVO allocationVO = new AppPortfolioArenaAllocationVO();
		List<AppPortfolioArenaAllocationDetailVO> sectorTypeList = portfolioArenaMessService.findPortfolioAllocationList(portfolioId, langCode, "sectorType");
		List<AppPortfolioArenaAllocationDetailVO> fundTypeList = portfolioArenaMessService.findPortfolioAllocationList(portfolioId, langCode, "fundType");
		List<AppPortfolioArenaAllocationDetailVO> geoAllocationList = portfolioArenaMessService.findPortfolioAllocationList(portfolioId, langCode, "geoAllocation");
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
	 * 得到某个投资组合基本信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/portfolioArena/findPortfolioDetail.r
	 * {"memberId":"40280ad8888327690155e3291c580000","portfolioId":"40280a295a5ea067015a5ea3375a0003","langCode":"sc","periodCode":"fund_return_period_code_3M" }
	 * @author zxtan
	 * @date 2016-11-16
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
		
		AppPortfolioArenaDetailVO detailVO = new AppPortfolioArenaDetailVO();
		//基本信息
		AppPortfolioArenaMessVo infoVO = portfolioArenaMessService.findPortfolioInfoMess(memberId,portfolioId,langCode);
		if(null != infoVO){
			//回报
			AppPortfolioArenaReturnVO returnVO = portfolioArenaMessService.findPortfolioReturn(portfolioId, periodCode);
//			List<AppPortfolioArenaCumperfVO> cumperfList = portfolioArenaMessService.findPortfolioCumperfList(portfolioId, startDate);
			
			String period = periodCode.replace("return_period_code_", "");
			List<CorePortfolioVO> cumperfList = corePortfolioService.getPortfolioReturnRate(portfolioId, period, "HKD");

			returnVO.setCumperfList(cumperfList);
			//产品信息
			List<AppPortfolioArenaProductVo> productList = portfolioArenaMessService.findPortfolioProductList(portfolioId, langCode);
			//配置
			AppPortfolioArenaAllocationVO allocationVO = new AppPortfolioArenaAllocationVO();
			List<AppPortfolioArenaAllocationDetailVO> sectorTypeList = portfolioArenaMessService.findPortfolioAllocationList(portfolioId, langCode, "sectorType");
			List<AppPortfolioArenaAllocationDetailVO> fundTypeList = portfolioArenaMessService.findPortfolioAllocationList(portfolioId, langCode, "fundType");
			List<AppPortfolioArenaAllocationDetailVO> geoAllocationList = portfolioArenaMessService.findPortfolioAllocationList(portfolioId, langCode, "geoAllocation");
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
		}else {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1001);
			result.setErrorMsg(WSConstants.MSG1001);
		}
		result.setData(detailVO);
		return result;
	}
}
