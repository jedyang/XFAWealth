package com.fsll.app.fund.ws;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.fund.service.AppMarketInfoService;
import com.fsll.app.fund.vo.AppMarketIndexDetailVo;
import com.fsll.app.fund.vo.AppMarketIndexVo;
import com.fsll.common.CommonConstants;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * 大盘指数服务接口实现
 * @author zpzhou
 * @date 2016-9-13
 */
@RestController
@RequestMapping("/service/marketInfo")
public class AppMarketInfoRest extends WmesBaseRest {

	@Autowired
	private AppMarketInfoService marketInfoService;

	/**
	 * 大盘指数关注列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/marketInfo/findMarketFollowList.r
	 * {"langCode":"sc","_user_id_":"40280a25559b852e01559b8615da0002" }
	 */
	@RequestMapping(value = "/findMarketFollowList")
	@ResponseBody
	public ResultWS findMarketFollowList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		String userId = jsonObject.optString("_user_id_", "");// 获得当前登录用户ID
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		
		if ("".equals(userId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}

		List<AppMarketIndexVo> list= marketInfoService.findMarketFollowList(userId,langCode);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 大盘指数列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/marketInfo/findMarketList.r
	 * {"langCode":"sc","keyWord":"001" }
	 */
	@RequestMapping(value = "/findMarketList")
	@ResponseBody
	public ResultWS findMarketList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		String keyWord = jsonObject.optString("keyWord", "");// 搜索关键词
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);

		List<AppMarketIndexVo> list= marketInfoService.findMarketList(langCode,keyWord);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 大盘指数分时信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/marketInfo/getMarketDetailMess.r
	 * {"indexId":"1"}
	 */
	@RequestMapping(value = "/getMarketDetailMess")
	@ResponseBody
	public ResultWS getMarketDetailMess(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		String indexId = jsonObject.optString("indexId", "");//大盘指数ID
		if ("".equals(indexId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		List<AppMarketIndexDetailVo> list= marketInfoService.getMarketDetailMess(indexId);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 得到每天的大盘指数列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/marketInfo/findMarketHisList.r
	 * {"code":"001"}
	 */
	@RequestMapping(value = "/findMarketHisList")
	@ResponseBody
	public ResultWS findMarketHisList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		String code = jsonObject.optString("code", "");//大盘指数编码
		if ("".equals(code)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		List<AppMarketIndexVo> list= marketInfoService.findMarketHisList(code);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
}
