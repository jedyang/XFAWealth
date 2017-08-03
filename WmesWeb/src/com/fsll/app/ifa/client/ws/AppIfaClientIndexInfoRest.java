package com.fsll.app.ifa.client.ws;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.ifa.client.service.AppIndexInfoService;
import com.fsll.app.ifa.client.vo.AppClientCountVO;
import com.fsll.app.ifa.client.vo.AppClientIndexVO;
import com.fsll.app.ifa.client.vo.AppClientItemVO;
import com.fsll.app.ifa.client.vo.AppCrmCustomerGroupVO;
import com.fsll.app.ifa.client.vo.AppEventVO;
import com.fsll.app.ifa.client.vo.AppProspectEventVO;
import com.fsll.app.ifa.client.vo.ClientSearchVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;
import com.fsll.wmes.entity.MemberIfa;

/**
 * IFA Market 首页接口服务接口
 * @author zxtan
 * @date 2017-03-14
 */
@RestController
@RequestMapping("/service/ifa/client")
public class AppIfaClientIndexInfoRest extends WmesBaseRest {

	@Autowired
	private AppIndexInfoService indexInfoService;
	
	/**
	 *获取客户分组列表
	 * 调用示例:[地址]/service/ifa/client/findClientGroupList.r
	 * {"memberId":"40280ad455e327690155e3291c582000"}
	 */
	@RequestMapping(value = "/findClientGroupList")
	@ResponseBody
	public ResultWS findClientGroupList(HttpServletRequest request,@RequestBody String body){
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

		
		List<AppCrmCustomerGroupVO> list = indexInfoService.findMyClientGroupList(memberId);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	} 
	
	/**
	 *获取首页统计信息
	 * 调用示例:[地址]/service/ifa/client/findClientCount.r
	 * {"memberId":"40280ad455e327690155e3291c582000","toCurrency":"HKD"}
	 */
	@RequestMapping(value = "/findClientCount")
	@ResponseBody
	public ResultWS findClientCount(HttpServletRequest request,@RequestBody String body){
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
		String toCurrency = jsonObject.optString("toCurrency", CommonConstants.DEF_CURRENCY_HKD);
				
		if("".equals(memberId)||"".equals(toCurrency)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}

		
		AppClientCountVO indexVO = indexInfoService.findClientCount(memberId, toCurrency);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(indexVO);
		return result;
	}   
	
	/**
	 *获取首页客户列表
	 * 调用示例:[地址]/service/ifa/client/findClientList.r
	 * {"memberId":"40280ad455e327690155e3291c582000","toCurrency":"HKD","groupId":"","keyword":"",
	 * "minReturnRate":"","maxReturnRate":"","minAuM":"","maxAuM":"","remindType":"","period":"",
	 * "rows":10,"page":1,"sort":null,"order":"desc"}
	 */
	@RequestMapping(value = "/findClientList")
	@ResponseBody
	public ResultWS findClientList(HttpServletRequest request,@RequestBody String body){
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
		String toCurrency = jsonObject.optString("toCurrency", CommonConstants.DEF_CURRENCY_HKD);
				
		if("".equals(memberId)||"".equals(toCurrency)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		
		MemberIfa ifa = indexInfoService.findMemberIfa(memberId);
		String ifaId = ifa==null?"": ifa.getId();
		String groupId = jsonObject.optString("groupId","");
		String keyword = jsonObject.optString("keyword","");
		String minReturnRate = jsonObject.optString("minReturnRate","");
		String maxReturnRate = jsonObject.optString("maxReturnRate","");
		String minAuM = jsonObject.optString("minAuM","");
		String maxAuM = jsonObject.optString("maxAuM","");
		String remindType = jsonObject.optString("remindType","");
		String period = jsonObject.optString("period","");
		int rows = jsonObject.optInt("rows",10);
		int page = jsonObject.optInt("page",1);
		String sort = jsonObject.optString("sort","totalAuM");//create_time,total_return_rate
		String order = jsonObject.optString("order","desc");
		String returnPeriodCode = jsonObject.optString("returnPeriodCode",CommonConstants.RETURN_PERIOD_CODE_1M);
		
		Date startDate=DateUtil.getCurDate();
		Date endDate=DateUtil.getInternalDateByDay(DateUtil.getCurDate(), 1);
		if("today".equalsIgnoreCase(period)){
			endDate= DateUtil.getInternalDateByDay(DateUtil.getCurDate(), 1);
		}else if ("1W".equalsIgnoreCase(period)) {
			endDate=DateUtil.getInternalDateByDay(DateUtil.getCurDate(), 8);
		}else if ("2W".equalsIgnoreCase(period)) {
			endDate=DateUtil.getInternalDateByDay(DateUtil.getCurDate(), 15);
		}else if ("1M".equalsIgnoreCase(period)) {
			endDate=DateUtil.getInternalDateByMon(DateUtil.getCurDate(), 1);
		}
		int days=0;
		try {
			 days=DateUtil.daysBetween(startDate, endDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ClientSearchVO searchVO = new ClientSearchVO();
		searchVO.setIfaId(ifaId);
		searchVO.setClientGroup(groupId);
		searchVO.setStatistic("h.`total_return_rate`");
		searchVO.setMinAuM(minAuM);
		searchVO.setMaxAuM(maxAuM);
		searchVO.setMinReturnRate(minReturnRate);
		searchVO.setMaxReturnRate(maxReturnRate);
		searchVO.setCurrency(toCurrency);
		searchVO.setSort(sort);
		searchVO.setOrder(order);
		searchVO.setPage(page-1);
		searchVO.setRows(rows);
		searchVO.setReCondition(returnPeriodCode);
		
		String clientId = null;
		if("birth".equalsIgnoreCase(remindType)){
			clientId = indexInfoService.findBirthDayCustomer(ifaId, "", days);
		}else if("schedule".equalsIgnoreCase(remindType)){
			clientId = indexInfoService.findSchedultCustomer(ifaId, "", startDate, endDate);
		}
		searchVO.setClientId(clientId);
		if(!"".equals(keyword)){
			searchVO.setClientName(keyword);
		}
		
		List<AppClientItemVO> clientList = indexInfoService.findMyClientList(searchVO,startDate,endDate);
	
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(clientList);
		return result;
	}  
	
	/**
	 *获取自选分类信息
	 * 调用示例:[地址]/service/ifa/client/findEventInfo.r
	 * {"memberId":"40280ad455e327690155e3291c582000","clientType":"1"}
	 */
	@RequestMapping(value = "/findEventInfo")
	@ResponseBody
	public ResultWS findEventInfo(HttpServletRequest request,@RequestBody String body){
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
		String clientType = jsonObject.optString("clientType", "1");
				
		if("".equals(memberId)||"".equals(clientType)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		
		if("1".equals(clientType)){
			AppEventVO eventVO = indexInfoService.findExistingEventList(memberId);
			result.setData(eventVO);
		}else {
			AppProspectEventVO eventVO = indexInfoService.findProspectEventList(memberId);
			result.setData(eventVO);
		}	
			
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		return result;
	}  
}
