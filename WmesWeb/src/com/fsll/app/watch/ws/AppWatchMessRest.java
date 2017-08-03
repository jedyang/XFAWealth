package com.fsll.app.watch.ws;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.investor.market.vo.AppPortfolioArenaMessVo;
import com.fsll.app.investor.market.vo.AppStrategyInfoVO;
import com.fsll.app.investor.market.vo.AppStrategyMessVo;
import com.fsll.app.watch.service.AppWatchMessService;
import com.fsll.app.watch.vo.AppFundInfoVO;
import com.fsll.app.watch.vo.AppPortfolioInfoVO;
import com.fsll.app.watch.vo.AppWatchFundVO;
import com.fsll.app.watch.vo.AppWatchProductVo;
import com.fsll.common.CommonConstants;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;
import com.fsll.wmes.entity.WebFollow;
import com.fsll.wmes.entity.WebWatchlist;
import com.fsll.wmes.entity.WebWatchlistAlert;
import com.fsll.wmes.entity.WebWatchlistType;

/**
 * 自选接口服务接口
 * @author zpzhou
 * @date 2016-9-30
 */
@RestController
@RequestMapping("/service/watchMess")
public class AppWatchMessRest extends WmesBaseRest{
	
	@Autowired
	private AppWatchMessService watchMessService;
	
	/**
	 *关注/取消关注
	 * 调用示例:[地址]/service/watchMess/setWebFollowMess
	 * {"relateID":"1679091C5A880FAF6FB5E6087EB1B2DC","memberID":"ALOQ_JPUwJgGqbZRsQD4qw5xyJKnqwpIOKRh","moduleType":"fund","OpType":"Follow"}
	 */
	@RequestMapping(value = "/setWebFollowMess")
	@ResponseBody
	public ResultWS setWebFollowMess(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		result = parseParam(request,jsonObject,result,"mod");
		if(WSConstants.FAIL.equals(result.getRet())){
			return result;
		}
		String relateId = jsonObject.getString("relateId");
		String memberId = jsonObject.getString("memberId");
		String moduleType = jsonObject.getString("moduleType");
		String opType = jsonObject.getString("opType");
		WebFollow obj = watchMessService.saveWebFollowMess(relateId, memberId, opType, moduleType);
		if(null!=obj){
			result.setData(obj.getId());
		}
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		return result;
	}
	
	/**
	 *获取自选分类信息
	 * 调用示例:[地址]/service/watchMess/getWatchTypeMess.r
	 * {"memberId":"ALOQ_JPUwJgGqbZRsQD4qw5xyJKnqwpIOKRh"}
	 */
	@RequestMapping(value = "/getWatchTypeMess")
	@ResponseBody
	public ResultWS getWatchTypeMess(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String memberId = jsonObject.optString("memberId","");
		if("".equals(memberId)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		List<WebWatchlistType> list = watchMessService.getWatchTypeMess(memberId);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 *新增/修改自选分类信息
	 * 调用示例:[地址]/service/watchMess/saveWatchTypeMess.r
	 * {"typeId":"","memberID":"40280ad655bfe9820155bfef8921000c","name":"test1"}
	 */
	@RequestMapping(value = "/saveWatchTypeMess")
	@ResponseBody
	public ResultWS saveWatchTypeMess(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String typeId = jsonObject.optString("typeId","");//为空时是新增，不为空则为修改
		String memberId = jsonObject.optString("memberId","");
		String name = jsonObject.optString("name","");
		if("".equals(memberId) || "".equals(name) ){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		int resultData = watchMessService.saveWatchTypeMess(typeId,memberId,name);
		if(0==resultData){
			result.setRet(WSConstants.SUCCESS);
			result.setErrorCode("");
			result.setErrorMsg("");
			return result;
		}else{
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE3001);
			result.setErrorMsg(WSConstants.MSG3001);
			return result;
		}
	}
	
	/**
	 *删除自选分类信息
	 * 调用示例:[地址]/service/watchMess/deleteWatchTypeMess.r
	 * {"typeId":"4028b881571d761401571d7a301c0001"}
	 */
	@RequestMapping(value = "/deleteWatchTypeMess")
	@ResponseBody
	public ResultWS deleteWatchTypeMess(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String typeId = jsonObject.optString("typeId","");
		if("".equals(typeId)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		int resultData = watchMessService.deleteWatchTypeMess(typeId);
		if(0==resultData){
			result.setRet(WSConstants.SUCCESS);
			result.setErrorCode("");
			result.setErrorMsg("");
			return result;
		}else{
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE3001);
			result.setErrorMsg(WSConstants.MSG3001);
			return result;
		}
	}
	
	/**
	 *获取自选预警设置信息
	 * 调用示例:[地址]/service/watchMess/getWatchAlertMess.r
	 * {"productId":"40280a0a57025f6401570282eb490003","memberID":"40280ad655bfe9820155bfef8921000c"}
	 */
	@RequestMapping(value = "/getWatchAlertMess")
	@ResponseBody
	public ResultWS getWatchAlertMess(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String productId = jsonObject.optString("productId","");
		String memberId = jsonObject.optString("memberId","");
		if("".equals(productId) && "".equals(memberId)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorCode(WSConstants.MSG1002);
			return result;
		}
		List<WebWatchlistAlert> list = watchMessService.getWatchAlertMess(productId,memberId);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 *保存自选预警设置信息
	 * 调用示例:[地址]/service/watchMess/saveWatchAlertMess.r
	 * {"productId":"40280a0a57025f6401570282eb490003","memberID":"40280ad655bfe9820155bfef8921000c","alertMess":[{"dataValue":"45","ifOpen":"0","type":"0","upDown":"0"},{"dataValue":"58","ifOpen":"1","type":"2","upDown":"1"}]}
	 */
	@RequestMapping(value = "/saveWatchAlertMess")
	@ResponseBody
	public ResultWS saveWatchAlertMess(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		JSONArray array = jsonObject.getJSONArray("alertMess");
		String productId = jsonObject.optString("productId","");
		String memberId = jsonObject.optString("memberId","");
		if("".equals(productId) && "".equals(memberId)|| array.isEmpty()){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		int resultData = watchMessService.saveWatchAlertMess(productId,memberId,array);
		if(0==resultData){
			result.setRet(WSConstants.SUCCESS);
			result.setErrorCode("");
			result.setErrorMsg("");
			return result;
		}else{
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE3001);
			result.setErrorMsg(WSConstants.MSG3001);
			return result;
		}
	}

	/**
	 * 分析参数,必需参数是否为空
	 * @param request
	 * @param jsonObject
	 * @param result
	 * @param oper add mod
	 * @return
	 */
	private ResultWS parseParam(HttpServletRequest request,JSONObject jsonObject,ResultWS result,String oper) {
		String relateId = jsonObject.getString("relateId");
		String memberId = jsonObject.getString("memberId");
		String moduleType = jsonObject.getString("moduleType");
		
		if("mod".equals(oper)){//关注/取消关注
			String opType = jsonObject.getString("opType");
			String errorMsg ="";
			if("".equals(relateId)){
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1002);
				errorMsg += "“relateId”"+WSConstants.MSG1002+"; ";
			}
			if("".equals(memberId)){
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1002);
				errorMsg += "“memberId”"+WSConstants.MSG1002+"; ";
			}
			if("".equals(opType)){
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1002);
				errorMsg += "“opType”"+WSConstants.MSG1002+"; ";
			}
			if("".equals(moduleType)){
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1002);
				errorMsg += "“moduleType”"+WSConstants.MSG1002+"; ";
			}
			if(!"".equals(errorMsg)){
				result.setErrorMsg(errorMsg);
			}
			return result;
		}else if("status".equals(oper)){//获取关注状态
			String errorMsg ="";
			if("".equals(relateId)){
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1002);
				errorMsg += "“relateId”"+WSConstants.MSG1002+"; ";
			}
			if("".equals(memberId)){
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1002);
				errorMsg += "“memberId”"+WSConstants.MSG1002+"; ";
			}
			if("".equals(moduleType)){
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1002);
				errorMsg += "“moduleType”"+WSConstants.MSG1002+"; ";
			}
			if(!"".equals(errorMsg)){
				result.setErrorMsg(errorMsg);
			}
			return result;
		}
		
		return result;
	}
	
	/**
	 * 得到我自选的产品列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/watchMess/findWatchProductList.r
	 * {"langCode":"sc","memberId":"40280ad655bfe9820155bfef8921000c","typeId":"3"}
	 */
	@RequestMapping(value = "/findWatchProductList")
	@ResponseBody
	public ResultWS findWatchProductList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String typeId = jsonObject.optString("typeId","");//产品自选类别
		String memberId = jsonObject.optString("memberId", "");// 获得当前登录用户ID
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 语言编码
		//String toCurrency = jsonObject.optString("toCurrency",CommonConstants.DEF_CURRENCY);
		if ("".equals(typeId)||"".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}

		List<AppWatchProductVo> list= watchMessService.findWatchProductList(typeId,memberId,langCode);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	
	/**
	 * 得到我自选的基金产品列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/watchMess/findWatchFundList.r
	 * {"memberId":"40280ad655bfe9820155bfef8921000c","langCode":"sc","toCurrency":"HKD"}
	 */
	@RequestMapping(value = "/findWatchFundList")
	@ResponseBody
	public ResultWS findWatchFundList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String typeId = jsonObject.optString("typeId","");//产品自选类别
		String memberId = jsonObject.optString("memberId", "");// 获得当前登录用户ID
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 语言编码
		String toCurrency = jsonObject.optString("toCurrency","");
		if ("".equals(memberId)|| "".equals(langCode)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}

		List<AppWatchFundVO> list= watchMessService.findWatchFundList(memberId,typeId,langCode,toCurrency);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 得到我自选的投资策略列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/watchMess/findWatchStrategyList.r
	 * {"langCode":"sc","memberId":"40280ad65601004c0156010188370004"}
	 */
	@RequestMapping(value = "/findWatchStrategyList")
	@ResponseBody
	public ResultWS findWatchStrategyList(HttpServletRequest request,@RequestBody String body) {
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
		//String toCurrency = jsonObject.optString("toCurrency",CommonConstants.DEF_CURRENCY);

		if ("".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}

		List<AppStrategyMessVo> list= watchMessService.findWatchStrategyList(memberId,langCode);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 得到我自选的投资组合列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/watchMess/findWatchPortfolioList.r
	 * {"langCode":"sc","memberId":"40280ad65601004c0156010188370004","periodCode":"return_period_code_1M"}
	 */
	@RequestMapping(value = "/findWatchPortfolioList")
	@ResponseBody
	public ResultWS findWatchPortfolioList(HttpServletRequest request,@RequestBody String body) {
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
		//String toCurrency = jsonObject.optString("toCurrency",CommonConstants.DEF_CURRENCY);
		String periodCode = jsonObject.optString("periodCode",CommonConstants.RETURN_PERIOD_CODE_1M);

		if ("".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		List<AppPortfolioArenaMessVo> list= watchMessService.findWatchPortfolioList(memberId,periodCode,langCode);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	
	/**
	 * 得到我感兴趣的基金列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/watchMess/findVisitedFundList.r
	 * {"langCode":"sc","memberId":"40280ad65601004c0156010188370004","periodCode":"return_period_code_1M","rows":2}
	 */
	@RequestMapping(value = "/findVisitedFundList")
	@ResponseBody
	public ResultWS findVisitedFundList(HttpServletRequest request,@RequestBody String body) {
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
		String periodCode = jsonObject.optString("periodCode",CommonConstants.RETURN_PERIOD_CODE_1M);
		int rows = jsonObject.optInt("rows", 2);
		
		if ("".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		List<AppFundInfoVO> list= watchMessService.findVisitedFundList(memberId, langCode, periodCode, rows);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 得到我感兴趣的投资组合列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/watchMess/findVisitedPortfolioList.r
	 * {"langCode":"sc","memberId":"40280ad65601004c0156010188370004","periodCode":"return_period_code_1M","rows":2}
	 */
	@RequestMapping(value = "/findVisitedPortfolioList")
	@ResponseBody
	public ResultWS findVisitedPortfolioList(HttpServletRequest request,@RequestBody String body) {
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
		String periodCode = jsonObject.optString("periodCode",CommonConstants.RETURN_PERIOD_CODE_1M);
		int rows = jsonObject.optInt("rows", 2);
		
		if ("".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		List<AppPortfolioInfoVO> list= watchMessService.findVisitedPortfolioList(memberId, langCode, periodCode, rows);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 得到我感兴趣的投资策略列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/watchMess/findVisitedStrategyList.r
	 * {"langCode":"sc","memberId":"40280ad65601004c0156010188370004","periodCode":"return_period_code_1M","rows":2}
	 */
	@RequestMapping(value = "/findVisitedStrategyList")
	@ResponseBody
	public ResultWS findVisitedStrategyList(HttpServletRequest request,@RequestBody String body) {
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
		int rows = jsonObject.optInt("rows", 2);
		
		if ("".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		List<AppStrategyInfoVO> list= watchMessService.findVisitedStrategyList(memberId, langCode, rows);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 *关注/取消关注
	 * 调用示例:[地址]/service/watchMess/setWebWatchlistMess.r
	 * {"productId":"FUND_000000000000001","memberId":"40280ad65601004c0156010188390025","watchTypeId":"4","OpType":"Add"}
	 */
	@RequestMapping(value = "/setWebWatchlistMess")
	@ResponseBody
	public ResultWS setWebWatchlistMess(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		String productId = jsonObject.optString("productId","");
		String memberId = jsonObject.optString("memberId","");
		String watchTypeId = jsonObject.optString("watchTypeId","");
		String opType = jsonObject.optString("opType","");
		
		if ("".equals(productId) ||"".equals(memberId) ||("".equals(watchTypeId)&& "add".equalsIgnoreCase(opType)) ||"".equals(opType)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		WebWatchlist obj = watchMessService.saveWebWatchlistMess(productId,watchTypeId, memberId, opType);
		if(null!=obj){
			result.setData(obj.getId());
		}
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		return result;
	}

}