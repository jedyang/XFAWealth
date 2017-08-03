package com.fsll.app.fund.ws;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.fund.service.AppFundInfoService;
import com.fsll.app.fund.service.AppFundMarketService;
import com.fsll.app.fund.service.AppFundPortfolioService;
import com.fsll.app.fund.service.AppFundReturnService;
import com.fsll.app.fund.vo.AppChartDataVO;
import com.fsll.app.fund.vo.AppFundBasicDataVO;
import com.fsll.app.fund.vo.AppFundCompositionDataVO;
import com.fsll.app.fund.vo.AppFundCumulativePerformanceDataVO;
import com.fsll.app.fund.vo.AppFundDetailVo;
import com.fsll.app.fund.vo.AppFundGeneralDataVO;
import com.fsll.app.fund.vo.AppFundSearchDetailVO;
import com.fsll.app.fund.vo.AppFundYearPerformanceDataVO;
import com.fsll.buscore.fund.service.CoreFundService;
import com.fsll.buscore.fund.vo.CoreFundNavVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.StrUtils;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;
import com.fsll.wmes.entity.FundPortfolio;

/**
 * 基金信息查询服务接口实现
 * 
 * @author zpzhou
 * @date 2016-6-23
 */
@RestController
@RequestMapping("/service/fundInfo")
public class AppFundInfoRest extends WmesBaseRest {
	@Autowired
	private AppFundInfoService fundInfoService;
	@Autowired
	private AppFundReturnService fundReturnService;
	@Autowired
	private AppFundMarketService fundMarketService;
	@Autowired
	private AppFundPortfolioService fundPortfolioService;
	
	@Autowired
	private CoreFundService coreFundService;
	
	/**
	 * 得到首页最热基金更多列表信息
	 * 通过body传递参数 调用示例:[地址]/service/fundInfo/findHotFundList.r
	 * {"langCode":"sc", "page":1,"periodCode":"return_period_code_1M","rows":2"}
	 */
	@RequestMapping(value = "/findHotFundList")
	@ResponseBody
	public ResultWS findHotFundList(HttpServletRequest request,@RequestBody String body) {
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
		String periodCode = jsonObject.optString("periodCode","return_period_code_1M");
		//String memberId = jsonObject.optString("memberId", "");// 获得当前登录用户ID
		String langCode=jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		int num = jsonObject.optInt("num",5);// 返回的组合列表条数
		if ("".equals(page)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(Integer.parseInt(page));
		jsonPaging.setRows(Integer.parseInt(rows));

		jsonPaging = fundInfoService.findHotFundList(jsonPaging,langCode, periodCode,num);

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
	 * 获取基金列表信息
	 * 通过body传递参数 
	 * 调用示例:[地址]/service/fundInfo/findAllFundList.r
       {"langCode":"sc", "page":1, "rows":2, "sort":"1", "order":"desc", "keyWord":"摩根", "fundType":"0", "dateType":1, "_user_id_":"ALOQ_JPUwJgGqbZRsQD4qw5xyJKnqwpIOKRh", "toCurrency":"USD" }
	 */
	@RequestMapping(value = "/findAllFundList")
	@ResponseBody
	public ResultWS findAllFundList(HttpServletRequest request,
			@RequestBody String body) {
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
		int sort = jsonObject.optInt("sort", 0);// 获得排序字段
		String order = jsonObject.optString("order", "desc");// 获得排序类型asc或者desc
		String keyWord = jsonObject.optString("keyWord", "");// 获得查询关键词
		String userId = jsonObject.optString("_user_id_", "");// 获得当前登录用户ID
		int fundType = jsonObject.optInt("fundType", 0);// 获得股票类型
//		int dateType = jsonObject.optInt("dateType", 0);// 获得股票时间类型
		String langCode=jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		String toCurrency = jsonObject.optString("toCurrency","");
		String periodCode=jsonObject.optString("periodCode", CommonConstants.RETURN_PERIOD_CODE_1W);
		
		if ("".equals(page) || "".equals(order)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(Integer.parseInt(page));
		jsonPaging.setRows(Integer.parseInt(rows));
		jsonPaging.setOrder(order);
		if (sort == 1) {// 按最新净值排序
			jsonPaging.setSort("last_nav");
		} else {// 按时间类型排序
			jsonPaging.setSort("increase");
		}
		jsonPaging = fundInfoService.findAllFundList(jsonPaging, keyWord,
				langCode, userId, getFundTypeMess(fundType), periodCode, toCurrency);
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
	 * 获取我自选的基金列表信息
	 * 通过body传递参数 调用示例:[地址]/service/fundInfo/findFundFollowList.r
	   {"langCode":"sc", "page":1, "rows":2, "sort":"1", "order":"desc",  "dateType":1, "_user_id_":"ALOQ_JPUwJgGqbZRsQD4qw5xyJKnqwpIOKRh", "toCurrency":"USD" }
	 */
	@RequestMapping(value = "/findFundFollowList")
	@ResponseBody
	public ResultWS findFundFollowList(HttpServletRequest request,@RequestBody String body) {
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
		int sort = jsonObject.optInt("sort", 0);// 获得排序字段
		String order = jsonObject.optString("order", "desc");// 获得排序类型asc或者desc
		String userId = jsonObject.optString("_user_id_", "");// 获得当前登录用户ID
//		int dateType = jsonObject.optInt("dateType", 0);// 获得股票时间类型
		String langCode=jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		String toCurrency = jsonObject.optString("toCurrency",CommonConstants.DEF_CURRENCY);
		String periodCode=jsonObject.optString("periodCode", CommonConstants.RETURN_PERIOD_CODE_1W);
		if ("".equals(page) || "".equals(userId) || "".equals(order)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(Integer.parseInt(page));
		jsonPaging.setRows(Integer.parseInt(rows));
		jsonPaging.setOrder(order);
		if (sort == 1) {// 按最新净值排序
			jsonPaging.setSort("last_nav");
		} else {// 按时间类型排序
			jsonPaging.setSort("increase");
		}
		jsonPaging = fundInfoService.getFundFollowList(jsonPaging,langCode, userId, periodCode, toCurrency);
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
	 * 根据条件搜索获取基金列表信息
	 * 通过body传递参数 调用示例:[地址]/service/fundInfo/searchFundList.r
	 * {"langCode":"sc", "page":1, "rows":2, "keyWord":"摩根","_user_id_":"ALOQ_JPUwJgGqbZRsQD4qw5xyJKnqwpIOKRh"}
	 */
	@RequestMapping(value = "/searchFundList")
	@ResponseBody
	public ResultWS searchFundList(HttpServletRequest request,@RequestBody String body) {
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
		String keyWord = jsonObject.optString("keyWord", "");// 获得查询关键词
		String userId = jsonObject.optString("_user_id_", "");// 获得当前登录用户ID
		String langCode=jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		if ("".equals(page) || "".equals(keyWord)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(Integer.parseInt(page));
		jsonPaging.setRows(Integer.parseInt(rows));

		jsonPaging = fundInfoService.queryFundList(jsonPaging, keyWord,langCode, userId);

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
	 *	获取指定基金的基础信息
	 *	POST /service/fundInfo/getFundBasicInfo.r
	 *	{"fundId":"92EB5FFEE6AE2FEC3AD71C777531578F", "langCode":"sc", "_user_id_":"ALOQ_JPUwJgGqbZRsQD4qw5xyJKnqwpIOKRh", "toCurrency":"USD" }
	 */
	@RequestMapping(value = "/getFundBasicInfo")
	@ResponseBody
	public ResultWS getFundBasicInfo(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		//检测权限
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String fundId = jsonObject.optString("fundId", "");// 获得基金id
		String userId = jsonObject.optString("_user_id_", "");// 获得当前登录用户ID
		String langCode=jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		String toCurrency = jsonObject.optString("toCurrency","");
		if (null==fundId || "".equals(fundId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		AppFundBasicDataVO dataVO = fundInfoService.getFundBasicDataMess(fundId, langCode, userId, toCurrency);
		//设置返回对象
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(dataVO);
		return result;
	}
	
	/**
	 * 得到基金详细页面所有信息的接口
	 * 调用示例:[地址]/service/fundInfo/getFundDetailMess.r
	 * {"fundId":"92EB5FFEE6AE2FEC3AD71C777531578F","langCode":"sc", "toCurrency":"USD","_user_id_":"","year":"","lastYears":"6","dataType":"NAV","cycle":"D","period":"1WK","addiDate":"0"}
	 */
	@RequestMapping(value = "/getFundDetailMess")
	@ResponseBody
	public ResultWS getFundDetailMess(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		result = parseParam(request,jsonObject,result);//参数验证
		if(WSConstants.FAIL.equals(result.getRet())){
			return result;
		}
		String langCode=jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 语言编码
		String toCurrency = jsonObject.optString("toCurrency","");
		String fundId = jsonObject.getString("fundId");
		String year = jsonObject.getString("year");//指定获取年度
		String years = jsonObject.getString("lastYears");//上n年
		int lastYears = StrUtils.getInt(years);
		String userId = jsonObject.optString("_user_id_", "");// 获得当前登录用户ID
		//以下参数用于基金详细页图表，默认加载近一周
//		String cycle = jsonObject.optString("cycle","D");
		String periodCode = jsonObject.optString("periodCode",CommonConstants.RETURN_PERIOD_CODE_1W);
//		String currency = jsonObject.optString("currency","");
//		String addiDate = jsonObject.optString("addiDate", "0");// 额外返回的数据周期，可空 -- 默认250
//		String dataType = jsonObject.optString("dataType", "NAV");// 数据类型，目前固定为NAV
		String frequencyType = periodCode.replace("return_period_code_", "");
		
		List<AppFundCumulativePerformanceDataVO> ljList = fundReturnService.fundCumulativePerformanceInfo(fundId, "", langCode);
		List<AppFundYearPerformanceDataVO> ndList = fundReturnService.fundYearPerformanceInfo(fundId,year,lastYears,langCode);
		AppFundBasicDataVO dataVO = fundInfoService.getFundBasicDataMess(fundId, langCode, userId, toCurrency);
		
		//基金详细页图表
//		AppChartDataVO list = fundMarketService.fundChartData(fundId,dataType,cycle,periodCode,addiDate,currency,langCode);
		List<CoreFundNavVO> navList = coreFundService.getFundNav(fundId, frequencyType, toCurrency);
		
		AppFundDetailVo vo=new AppFundDetailVo();
		vo.setLjList(ljList);
		vo.setNdList(ndList);
		vo.setVo(dataVO);
		vo.setNavList(navList);//近一周的图表数据
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(vo);
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
	private ResultWS parseParam(HttpServletRequest request,JSONObject jsonObject,ResultWS result){
		result.setRet(WSConstants.SUCCESS);//假定通过验证
		String fundId = jsonObject.getString("fundId")==null?"":jsonObject.getString("fundId");
//		String cycle = jsonObject.getString("cycle")==null?"":jsonObject.getString("cycle");
		String period = jsonObject.getString("periodCode")==null?"":jsonObject.getString("periodCode");
		if("".equals(fundId)){//参数不能为空
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg("“fundId”"+WSConstants.MSG1002);
		}
//		if("".equals(cycle)){//参数不能为空
//			result.setRet(WSConstants.FAIL);
//			result.setErrorCode(WSConstants.CODE1002);
//			result.setErrorMsg("“cycle”"+WSConstants.MSG1002);
//		}
		if("".equals(period)){//参数不能为空
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg("“period”"+WSConstants.MSG1002);
		}
		return result;
	}
	
//	/**
//	 * 得到基金回报时期编码
//	 * @param dateType
//	 * @return
//	 */
//	private String getDateTypeMess(int dateType) {
//		String dateTypeStr = "";
//		switch (dateType) {
//		case 0:
//			dateTypeStr = "return_period_code_1W";
//			break;
//		case 1:
//			dateTypeStr = "return_period_code_1M";
//			break;
//		case 2:
//			dateTypeStr = "return_period_code_3M";
//			break;
//		case 3:
//			dateTypeStr = "return_period_code_6M";
//			break;
//		case 4:
//			dateTypeStr = "return_period_code_YTD";
//			break;
//		case 5:
//			dateTypeStr = "return_period_code_1Y";
//			break;
//		case 6:
//			dateTypeStr = "return_period_code_3Y";
//			break;
//		case 7:
//			dateTypeStr = "return_period_code_5Y";
//			break;
//		case 8:
//			dateTypeStr = "return_period_code_LAUNCH";
//			break;
//		default:
//			break;
//		}
//		return dateTypeStr;
//	}
	
	/**
	 * 得到基金类型编码
	 * @param fundType
	 * @return
	 */
	private String getFundTypeMess(int fundType) {
		String fundTypeStr = "";
		switch (fundType) {
		case 1:
			fundTypeStr = "fund_type_01";//股票基金
			break;
		case 2:
			fundTypeStr = "fund_type_02";//均衡基金
			break;
//		case 3:
//			fundTypeStr = "fund_type_03";//商品基金
//			break;
		case 3:
			fundTypeStr = "fund_type_04";//定息基金
			break;
		case 4:
			fundTypeStr = "fund_type_05";//期货基金
			break;
		case 5:
			fundTypeStr = "fund_type_06";//货币市场基金
			break;
		case 6:
			fundTypeStr = "fund_type_07";//混合资产基金
			break;
//		case 8:
//			fundTypeStr = "fund_type_08";//另类基金
//			break;
		default:
			break;
		}
		return fundTypeStr;
	}
	
	
	/**
	 *	获取指定基金的基本资料，包含基本信息，行业分布，地区分布
	 *	POST /service/fundInfo/getFundGeneralData.r
	 *	{"fundId":"67FA2229B92C055B259DFB35507402F0", "langCode":"sc", "_user_id_":"ALOQ_JPUwJgGqbZRsQD4qw5xyJKnqwpIOKRh", "toCurrency":"USD" }
	 */
	@RequestMapping(value = "/getFundGeneralData")
	@ResponseBody
	public ResultWS getFundGeneralData(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		//检测权限
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String fundId = jsonObject.optString("fundId", "");// 获得基金id
		String memberId = jsonObject.optString("memberId", "");// 获得当前登录用户ID
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		String toCurrency = jsonObject.optString("toCurrency",CommonConstants.DEF_CURRENCY);
	
		
		if ( "".equals(fundId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		AppFundGeneralDataVO generalDataVO = new AppFundGeneralDataVO();
		AppFundBasicDataVO basicData = fundInfoService.getFundBasicDataMess(fundId, langCode, memberId, toCurrency);
		
		FundPortfolio queryPortfolio = new FundPortfolio();
		queryPortfolio.setFundId(fundId);
		queryPortfolio.setType("sector");			
		List<AppFundCompositionDataVO> sectorList = fundPortfolioService.findPortfolioByType(queryPortfolio, langCode);
		
		queryPortfolio.setType("market");
		List<AppFundCompositionDataVO> marketList = fundPortfolioService.findPortfolioByType(queryPortfolio, langCode);
				
		generalDataVO.setBasicData(basicData);
		generalDataVO.setSectorList(sectorList);
		generalDataVO.setMarketList(marketList);
		
		//设置返回对象
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(generalDataVO);
		return result;
	}
	
	/**
	 *	获取指定基金的基本资料，包含基本信息，行业分布，地区分布
	 *	POST /service/fundInfo/findFundSearchDetail.r
	 *	{"langCode":"sc"}
	 */
	@RequestMapping(value = "/findFundSearchDetail")
	@ResponseBody
	public ResultWS findFundSearchDetail(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		//检测权限
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		
//		String memberId = jsonObject.optString("memberId", "");// 获得当前登录用户ID
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
				
		if ( "".equals(langCode)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
				
		AppFundSearchDetailVO info = fundInfoService.findFundSearchDetail(langCode);
				
		//设置返回对象
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(info);
		return result;
	}
	
	/**
	 * 根据条件搜索获取基金列表信息（高级搜索）
	 * 通过body传递参数 调用示例:[地址]/service/fundInfo/advanceSearchFundList.r
	 * { "page":1, "rows":20, "langCode":"sc","memberId":"743b2d771af84a43b1203321e7666886","fundType":"","riskLevel":"",
	 * "fundGeo":"","fundSector":"","fundSize":"","mgtFee":"","minInitialInv":"","return1MonFrom":"","return1MonTo":"",
	 * "return3MonFrom":"","return3MonTo":"","return6MonFrom":"","return6MonTo":"","fundHouse":"","distributor":"",
	 * "return1YearFrom":"","return1YearTo":"","return3YearFrom":"","return3YearTo":"","return5YearFrom":"","return5YearTo":"",
	 * "returnYTDFrom":"","returnYTDTo":"","returnLaunchFrom":"","returnLaunchTo":""}
	 */
	@RequestMapping(value = "/advanceSearchFundList")
	@ResponseBody
	public ResultWS advanceSearchFundList(HttpServletRequest request,@RequestBody String body) {
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
		String memberId = jsonObject.optString("memberId", "");// 获得当前登录用户ID
		String langCode=jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		
		if ("".equals(memberId) || "".equals(langCode)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);

		jsonPaging = fundInfoService.queryFundListByAdvance(jsonPaging, jsonObject);

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
