package com.fsll.app.ifa.market.ws;

import java.util.Date;
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
import com.fsll.app.ifa.market.service.AppWebViewService;
import com.fsll.app.ifa.market.vo.AppSelectMemberVO;
import com.fsll.app.ifa.market.vo.AppWebViewVO;
import com.fsll.app.ifa.schedule.service.AppScheduleService;
import com.fsll.app.ifa.schedule.vo.AppScheduleGroupVO;
import com.fsll.app.ifa.schedule.vo.AppScheduleItemVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * 权限接口
 * @author zxtan
 * @date 2017-03-28
 */
@RestController
@RequestMapping("/service/ifa/market/webview")
public class AppWebViewRest extends WmesBaseRest {

	@Autowired
	private AppWebViewService webViewService;
	

	
	/**
	 * 获取我的策略权限
	 *@author zxtan
	 *@date 2017-03-28
	 * 调用示例:[地址]/service/ifa/market/webview/findWebView.r
	 * {"memberId":"40280ad455e327690155e3291c582000","relateId":"40280jk457fb2b140157fb3a406b0001","langCode":"sc"}
	 */
	@RequestMapping(value = "/findWebView")
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
		
		String memberId = jsonObject.optString("memberId", "");// 获得当前登录用户ID
		String relateId = jsonObject.optString("relateId", "");// 关联Id
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 获得当前语言
		
		if ("".equals(relateId) || "".equals(langCode) || "".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}

		AppWebViewVO info = webViewService.findWebView(memberId, relateId, langCode);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(info);
		return result;
	}
	
	
	/**
	 * 获取待选用户
	 *@author zxtan
	 *@date 2017-03-28
	 * 调用示例:[地址]/service/ifa/market/webview/findToBeSelectedMemberList.r
	 * {"memberId":"40280ad455e327690155e3291c582000","userType":"client","langCode":"sc"}
	 */
	@RequestMapping(value = "/findToBeSelectedMemberList")
	@ResponseBody
	public ResultWS findToBeSelectedList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		String memberId = jsonObject.optString("memberId", "");// 获得当前登录IFA用户ID
		String userType = jsonObject.optString("userType", "");// 关联Id
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 获得当前语言
		
		if ("".equals(userType) || "".equals(langCode) || "".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}

		List<AppSelectMemberVO> list = webViewService.findToBeSelectedList(memberId, userType, langCode);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	
	/**
	 *更新/删除策略
	 *@author zxtan
	 *@date 2017-03-23
	 * 调用示例:[地址]/service/ifa/market/webview/updateWebView.r
	 * {"moduleType":"strategy","relateId":"40280jk457fb2b140157fb3a406b0001","memberId":"40280ad455e327690155e3291c582000","scopeFlag":"3","clientFlag":"1","prospectFlag":"-1","buddyFlag":"0","colleagueFlag":"0","prospectList":[{"toMemberId":"40280ad65601004c0156010188390028"}]}
	 */
	@RequestMapping("/updateWebView")
	@ResponseBody
	public ResultWS updateWebView(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String moduleType = jsonObject.optString("moduleType","");
		String memberId = jsonObject.optString("memberId","");
		String relateId = jsonObject.optString("relateId","");
		
		if("".equals(moduleType)|| "".equals(memberId) || "".equals(relateId)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		
		Map<String, Object> map = new HashMap<String, Object>();		
		boolean flag = webViewService.updateWebView(jsonObject);
		map.put("flag", flag);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(map);
		return result;
	}
}
