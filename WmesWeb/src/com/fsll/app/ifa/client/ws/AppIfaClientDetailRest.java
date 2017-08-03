package com.fsll.app.ifa.client.ws;

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

import com.fsll.app.ifa.client.service.AppClientDetailService;
import com.fsll.app.ifa.client.vo.AppAccountBaseInfoVO;
import com.fsll.app.ifa.client.vo.AppAccountVO;
import com.fsll.app.ifa.client.vo.AppCrmProposalItemVO;
import com.fsll.app.ifa.schedule.service.AppScheduleService;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * IFA Market 首页接口服务接口
 * @author zxtan
 * @date 2017-03-14
 */
@RestController
@RequestMapping("/service/ifa/clientDetail")
public class AppIfaClientDetailRest extends WmesBaseRest {

	@Autowired
	private AppClientDetailService clientDetailService;
	@Autowired
	private AppScheduleService scheduleService;
	
	/**
	 *获取IFA客户详情-组合列表
	 * 调用示例:[地址]/service/ifa/clientDetail/findPortfolioHoldList.r
	 * {"ifaMemberId":"40280ad455e327690155e3291c582000","clientMemberId":"40280ad655bfe9820155bfef8921000c",
	 * "periodCode":"return_period_code_1M","rows":10,"page":1}
	 */
	@RequestMapping(value = "/findPortfolioHoldList")
	@ResponseBody
	public ResultWS findPortfolioHoldList(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String ifaMemberId = jsonObject.optString("ifaMemberId","");
		String clientMemberId = jsonObject.optString("clientMemberId","");
		String periodCode = jsonObject.optString("periodCode", CommonConstants.RETURN_PERIOD_CODE_1M);
		String toCurrency = jsonObject.optString("toCurrency","");
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
				
		if("".equals(ifaMemberId)||"".equals(clientMemberId)||"".equals(periodCode)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		int rows = jsonObject.optInt("rows",10);
		int page = jsonObject.optInt("page",1);
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);
		
		jsonPaging = clientDetailService.findPortfolioHoldList(jsonPaging, ifaMemberId, clientMemberId, periodCode,toCurrency,langCode);
		
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
	 * 获取日程安排
	 *@author zxtan
	 *@date 2017-03-23
	 * 调用示例:[地址]/service/ifa/clientDetail/findScheduleList.r
	 * {"page":1, "rows":15,"ifaMemberId":"40280ad455e327690155e3291c582000","clientMemberId":"40280ad655bfe9820155bfef8921000c","groupId":"40280a705a849f2b015a84c814050002","sort":"endTime"}
	 */
	@RequestMapping(value = "/findScheduleList")
	@ResponseBody
	public ResultWS findScheduleList(HttpServletRequest request,@RequestBody String body) {
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
		String ifaMemberId = jsonObject.optString("ifaMemberId", "");// 获得当前登录用户ID
		String clientMemberId = jsonObject.optString("clientMemberId", "");// 获得当前登录用户ID
		String groupId = jsonObject.optString("groupId", "");// 分组Id
		String sort = jsonObject.optString("sort", "endTime");//排序字段
		
		if ( "".equals(sort)|| "".equals(ifaMemberId) || "".equals(clientMemberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);

		jsonPaging = scheduleService.findScheduleList(jsonPaging, ifaMemberId,clientMemberId,groupId,sort);

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
	 * 获取日程安排
	 *@author zxtan
	 *@date 2017-03-23
	 * 调用示例:[地址]/service/ifa/clientDetail/findCrmMemoList.r
	 * {"page":1, "rows":15,"ifaMemberId":"40280ad455e327690155e3291c582000","clientMemberId":"40280ad655bfe9820155bfef8921000c"}
	 */
	@RequestMapping(value = "/findCrmMemoList")
	@ResponseBody
	public ResultWS findCrmMemoList(HttpServletRequest request,@RequestBody String body) {
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
		String ifaMemberId = jsonObject.optString("ifaMemberId", "");// 获得当前登录用户ID
		String clientMemberId = jsonObject.optString("clientMemberId", "");// 获得当前登录用户ID
		
		if ( "".equals(ifaMemberId) || "".equals(clientMemberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);

		jsonPaging = clientDetailService.findCrmMemoList(jsonPaging, ifaMemberId, clientMemberId);

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
	 *更新/删除策略
	 *@author zxtan
	 *@date 2017-03-23
	 * 调用示例:[地址]/service/ifa/clientDetail/updateCrmMemo.r
	 * {"updateType":"update","id":"40280a295a45cef6015a4622ef250003","memoText":"abc"}
	 */
	@RequestMapping("/updateCrmMemo")
	@ResponseBody
	public ResultWS updateCrmMemo(HttpServletRequest request,@RequestBody String body){
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
		String memoText = jsonObject.optString("memoText","");
		
		if("".equals(updateType)|| "".equals(id)
				||( "update".equalsIgnoreCase(updateType) && "".equals(memoText))){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		boolean flag = false;
		flag = clientDetailService.updateCrmMemo(updateType, id, memoText);
		map.put("flag", flag);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(map);
		return result;
	} 
	
	/**
	 *获取账户列表
	 *@author zxtan
	 *@date 2017-03-24
	 * 调用示例:[地址]/service/ifa/clientDetail/findAccountList.r
	 * {"ifaMemberId":"40280ad455e327690155e3291c582000","clientMemberId":"40280ad655bfe9820155bfef8921000c"}
	 */
	@RequestMapping("/findAccountList")
	@ResponseBody
	public ResultWS findAccountList(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String ifaMemberId = jsonObject.optString("ifaMemberId","");
		String clientMemberId = jsonObject.optString("clientMemberId","");
		String toCurrency = jsonObject.optString("toCurrency","");
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		
		if("".equals(ifaMemberId)|| "".equals(clientMemberId)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		List<AppAccountVO> list = clientDetailService.findAccountList(ifaMemberId, clientMemberId,toCurrency,langCode);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	} 
	
	/**
	 *获取方案列表
	 *@author zxtan
	 *@date 2017-03-24
	 * 调用示例:[地址]/service/ifa/clientDetail/findProposalList.r
	 * {"ifaMemberId":"40280ad455e327690155e3291c582000","clientMemberId":"40280ad655bfe9820155bfef8921000c"}
	 */
	@RequestMapping("/findProposalList")
	@ResponseBody
	public ResultWS findProposalList(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String ifaMemberId = jsonObject.optString("ifaMemberId","");
		String clientMemberId = jsonObject.optString("clientMemberId","");
		String toCurrency = jsonObject.optString("toCurrency","");
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		
		if("".equals(ifaMemberId)|| "".equals(clientMemberId)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		List<AppCrmProposalItemVO> list = clientDetailService.findProposalList(ifaMemberId, clientMemberId,toCurrency,langCode);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	} 
	
	/**
	 * 获取交易记录
	 *@author zxtan
	 *@date 2017-03-24
	 * 调用示例:[地址]/service/ifa/clientDetail/findOrderHistoryList.r
	 * {"page":1, "rows":15,"ifaMemberId":"40280ad455e327690155e3291c582000","clientMemberId":"40280ad655bfe9820155bfef8921000c","langCode":"sc"}
	 */
	@RequestMapping(value = "/findOrderHistoryList")
	@ResponseBody
	public ResultWS findOrderHistoryList(HttpServletRequest request,@RequestBody String body) {
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
		String ifaMemberId = jsonObject.optString("ifaMemberId", "");// 获得当前登录用户ID
		String clientMemberId = jsonObject.optString("clientMemberId", "");// 获得当前登录用户ID
		String toCurrency = jsonObject.optString("toCurrency","");
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 语言
		
		
		if ( "".equals(langCode)|| "".equals(ifaMemberId) || "".equals(clientMemberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);

		jsonPaging = clientDetailService.findOrderHistoryList(jsonPaging, ifaMemberId, clientMemberId,toCurrency, langCode);

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
	 *获取账户列表
	 *@author zxtan
	 *@date 2017-03-24
	 * 调用示例:[地址]/service/ifa/clientDetail/findAccountBaseInfo.r
	 * {"ifaMemberId":"40280ad455e327690155e3291c582000","clientMemberId":"40280ad655bfe9820155bfef8921000c","langCode":"sc"}
	 */
	@RequestMapping("/findAccountBaseInfo")
	@ResponseBody
	public ResultWS findAccountBaseInfo(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String ifaMemberId = jsonObject.optString("ifaMemberId","");
		String clientMemberId = jsonObject.optString("clientMemberId","");
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);
		
		if("".equals(ifaMemberId)|| "".equals(clientMemberId)|| "".equals(langCode)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		AppAccountBaseInfoVO info = clientDetailService.findAccountBaseInfo(ifaMemberId, clientMemberId, langCode);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(info);
		return result;
	} 
}
