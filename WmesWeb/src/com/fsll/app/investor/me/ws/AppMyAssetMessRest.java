package com.fsll.app.investor.me.ws;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.investor.me.service.AppIfaInfoService;
import com.fsll.app.investor.me.service.AppMyAssetMessService;
import com.fsll.app.investor.me.service.AppPortfolioMessService;
import com.fsll.app.investor.me.vo.AppFundAllocationVO;
import com.fsll.app.investor.me.vo.AppHoldProductAnalysisVO;
import com.fsll.app.investor.me.vo.AppHoldProductReturnDetailVO;
import com.fsll.app.investor.me.vo.AppHoldProductVO;
import com.fsll.app.investor.me.vo.AppIfaItemVO;
import com.fsll.app.investor.me.vo.AppMyAssetsDetailVO;
import com.fsll.app.investor.me.vo.AppMyAssetsHisMessVo;
import com.fsll.app.investor.me.vo.AppMyAssetsMessVo;
import com.fsll.app.investor.me.vo.AppPortfolioAllocationVO;
import com.fsll.app.investor.me.vo.AppPortfolioDetailVO;
import com.fsll.app.investor.me.vo.AppPortfolioHoldCumperfVO;
import com.fsll.app.investor.me.vo.AppPortfolioHoldVO;
import com.fsll.app.investor.me.vo.AppPortfolioMessVo;
import com.fsll.app.investor.me.vo.AppPortfolioProductVo;
import com.fsll.app.investor.me.vo.AppPortfolioReturnVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * 我的资产信息接口服务
 * @author zpzhou
 * @date 2016-9-13
 */
@RestController
@RequestMapping("/service/myAsset")
public class AppMyAssetMessRest extends WmesBaseRest {

	@Autowired
	private AppMyAssetMessService myAssetInfoService;
	@Autowired
	private AppPortfolioMessService portfolioMessService;

	@Autowired
	private AppIfaInfoService ifaInfoService;

	/**
	 * 得到我的资产信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/myAsset/findMyAssetMess.r
	 * {"memberId":"40280a25559b852e01559b8615da0002" }
	 */
	@RequestMapping(value = "/findMyAssetMess")
	@ResponseBody
	public ResultWS findMyAssetMess(HttpServletRequest request,@RequestBody String body) {
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
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 语言编码
		String toCurrency = jsonObject.optString("toCurrency",CommonConstants.DEF_CURRENCY);
		
		if ("".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}

		AppMyAssetsMessVo vo= myAssetInfoService.findMyAssetMess(memberId,toCurrency,langCode);
		List<AppIfaItemVO> myIfaList = ifaInfoService.findMyIfaList(memberId, langCode);
		vo.setMyIfaList(myIfaList);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(vo);
		return result;
	}
	
	/**
	 * 得到我的资产历史信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/myAsset/findMyAssetHisMess.r
	 * {"memberId":"40280a25559b852e01559b8615da0002","periodCode":"return_period_code_1M" }
	 */
	@RequestMapping(value = "/findMyAssetHisMess")
	@ResponseBody
	public ResultWS findMyAssetHisMess(HttpServletRequest request,@RequestBody String body) {
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
		//String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 语言编码
		String toCurrency = jsonObject.optString("toCurrency",CommonConstants.DEF_CURRENCY);
		String periodCode = jsonObject.optString("periodCode",CommonConstants.RETURN_PERIOD_CODE_1M);

		String startDate = getStartDate(periodCode);
		if ("".equals(memberId) || "".equals(startDate)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}	

		List<AppMyAssetsHisMessVo> list= myAssetInfoService.findMyAssetHisMess(memberId,toCurrency,startDate);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	
	/**
	 * 得到我的资产信息接口， 通过body传递参数 
	 * @author zxtan
	 * 调用示例:[地址]/service/myAsset/findMyAssetDetail.r
	 * {"memberId":"40280a25559b852e01559b8615da0002","periodCode":"return_period_code_1M" }
	 */
	@RequestMapping(value = "/findMyAssetDetail")
	@ResponseBody
	public ResultWS findMyAssetDetail(HttpServletRequest request,@RequestBody String body) {
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
		String toCurrency = jsonObject.optString("toCurrency",CommonConstants.DEF_CURRENCY);
		String periodCode = jsonObject.optString("periodCode",CommonConstants.RETURN_PERIOD_CODE_1M);
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);
		
		String startDate = getStartDate(periodCode);
		
		if ("".equals(memberId) || "".equals(startDate)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}

		AppMyAssetsDetailVO detailVO = new AppMyAssetsDetailVO();

		AppMyAssetsMessVo assetsVO= myAssetInfoService.findMyAssetMess(memberId,toCurrency,langCode);
//		List<AppMyAssetsHisMessVo> assetsHisList = myAssetInfoService.findMyAssetHisMess(memberId, toCurrency,startDate);
		List<AppPortfolioHoldVO> portfolioList = portfolioMessService.findPortfolioList(memberId, toCurrency,periodCode,langCode);
		
		detailVO.setAssetsInfo(assetsVO);
		detailVO.setPortfolioList(portfolioList);
//		detailVO.setAssetsHisList(assetsHisList);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(detailVO);
		return result;
	}
	
	/**
	 * 得到某个投资组合基本信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/myAsset/findProductAnalysis.r
	 * {"memberId":"40280a25559b852e01559b8615da0002","langCode":"sc","toCurrency":"HKD" }
	 * @author zxtan
	 * @date 2017-02-17
	 */
	@RequestMapping(value = "/findProductAnalysis")
	@ResponseBody
	public ResultWS findProductAnalysis(HttpServletRequest request,@RequestBody String body) {
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
		String memberId = jsonObject.optString("memberId", "");// 获得组合ID
		String toCurrency = jsonObject.optString("toCurrency","");
		if ("".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		AppHoldProductAnalysisVO vo = new AppHoldProductAnalysisVO();
		//产品配置
		List<AppPortfolioAllocationVO> allocationList = myAssetInfoService.findProductAllocationList(memberId, langCode, toCurrency);
				
		//产品列表
		List<AppHoldProductVO> fundList = myAssetInfoService.findHoldProductList(memberId, "fund", langCode, toCurrency);
		List<AppHoldProductVO> bondList = myAssetInfoService.findHoldProductList(memberId, "bond", langCode, toCurrency);
		List<AppHoldProductVO> stockList = myAssetInfoService.findHoldProductList(memberId, "stock", langCode, toCurrency);
		List<AppHoldProductVO> insureList = myAssetInfoService.findHoldProductList(memberId, "insure", langCode, toCurrency);
		
		vo.setAllocationList(allocationList);
		vo.setFundList(fundList);
		vo.setBondList(bondList);
		vo.setStockList(stockList);
		vo.setInsureList(insureList);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(vo);
		return result;
	}
	
	/**
	 * 得到某个投资组合基本信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/myAsset/findHoldProductReturnDetail.r
	 * {"holdProductId":"40280a19596cd56i01s96ce18eb80005","langCode":"sc","toCurrency":"HKD" }
	 * @author zxtan
	 * @date 2017-03-03
	 */
	@RequestMapping(value = "/findHoldProductReturnDetail")
	@ResponseBody
	public ResultWS findHoldProductReturnDetail(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		String holdProductId = jsonObject.optString("holdProductId", "");// 获得持仓产品ID
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);	
		String toCurrency = jsonObject.optString("toCurrency","");
		if ("".equals(holdProductId)||"".equals(langCode)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		AppHoldProductReturnDetailVO vo = myAssetInfoService.findHoldProductReturnDetail(holdProductId, langCode, toCurrency);
				
				
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(vo);
		return result;
	}
	
	/**
	 * 弃用
	 * 得到某个投资组合基本信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/myAsset/findProductAllocationList.r
	 * {"memberId":"40280a25559b852e01559b8615da0002","langCode":"sc","toCurrency":"HKD" }
	 * @author zxtan
	 * @date 2017-02-17
	 */
	@RequestMapping(value = "/findProductAllocationList")
	@ResponseBody
	public ResultWS findProductAllocationList(HttpServletRequest request,@RequestBody String body) {
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
		String memberId = jsonObject.optString("memberId", "");// 获得组合ID
		String toCurrency = jsonObject.optString("toCurrency","");
		if ("".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		
		//产品配置
		List<AppPortfolioAllocationVO> allocationList = myAssetInfoService.findProductAllocationList(memberId, langCode, toCurrency);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(allocationList);
		return result;
	}
	
	
	/**
	 * 得到某个投资组合基本信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/myAsset/findFundAllocationList.r
	 * {"memberId":"40280a25559b852e01559b8615da0002","langCode":"sc","toCurrency":"HKD" }
	 * @author zxtan
	 * @date 2017-02-17
	 *//*
	@RequestMapping(value = "/findFundAllocationList")
	@ResponseBody
	public ResultWS findFundAllocationList(HttpServletRequest request,@RequestBody String body) {
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
		String memberId = jsonObject.optString("memberId", "");// 获得组合ID
		String toCurrency = jsonObject.optString("toCurrency","");
		if ("".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}		
		
		//产品配置
		AppFundAllocationVO allocationVO = new AppFundAllocationVO();
		
		//基金配置（行业，类型，区域）
		List<AppPortfolioAllocationVO> sectorTypeList = myAssetInfoService.findProductFundAllocationList(memberId, langCode, toCurrency,"sectorType");
//		List<AppPortfolioAllocationVO> fundTypeList = portfolioMessService.findPortfolioFundAllocationList(memberId, langCode, toCurrency,"fundType");
		List<AppPortfolioAllocationVO> geoAllocationList = myAssetInfoService.findProductFundAllocationList(memberId, langCode, toCurrency,"geoAllocation");
		
		List<AppPortfolioProductVo> fundList = myAssetInfoService.findProductFundList(memberId, langCode, toCurrency);
		
//		allocationVO.setFundTypeList(fundTypeList);
		allocationVO.setSectorTypeList(sectorTypeList);
		allocationVO.setGeoAllocationList(geoAllocationList);	
		allocationVO.setFundList(fundList);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(allocationVO);
		return result;
	}	*/

}
