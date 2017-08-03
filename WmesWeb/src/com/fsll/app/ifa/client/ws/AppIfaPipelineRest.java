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

import com.fsll.app.ifa.client.service.AppPipelineService;
import com.fsll.app.ifa.client.vo.AppPipelineInvestorDetailVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;
import com.fsll.wmes.entity.MemberDistributor;

/**
 * IFA Market 首页接口服务接口
 * @author zxtan
 * @date 2017-03-14
 */
@RestController
@RequestMapping("/service/ifa/pipeline")
public class AppIfaPipelineRest extends WmesBaseRest {

	@Autowired
	private AppPipelineService pipelineService;
	
	/**
	 *获取IFA销售管道-开户情况列表
	 * 调用示例:[地址]/service/ifa/pipeline/findAccountList.r
	 * {"memberId":"40280ad455e327690155e3291c582000","keyword":"",
	 * "groupId":"","openStatus":"","rows":10,"page":1}
	 */
	@RequestMapping(value = "/findAccountList")
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
		Integer page = jsonObject.optInt("page", 1);// 获得当前页数
		Integer rows = jsonObject.optInt("rows", 10);// 获得每页记录数isRead
		
		String memberId = jsonObject.optString("memberId","");
		String keyword = jsonObject.optString("keyword","");
		String groupId = jsonObject.optString("groupId", "");
		String openStatus = jsonObject.optString("openStatus", "");
				
		if("".equals(memberId)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);
		
		jsonPaging = pipelineService.findAccountList(jsonPaging, memberId, keyword, groupId, openStatus);
		
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
	 *获取IFA销售管道-KYC情况列表
	 * 调用示例:[地址]/service/ifa/pipeline/findKYCList.r
	 * {"memberId":"40280ad455e327690155e3291c582000","keyword":"",
	 * "groupId":"","distributorId":"","rows":10,"page":1}
	 */
	@RequestMapping(value = "/findKYCList")
	@ResponseBody
	public ResultWS findKYCList(HttpServletRequest request,@RequestBody String body){
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
		
		String memberId = jsonObject.optString("memberId","");
		String keyword = jsonObject.optString("keyword","");
		String groupId = jsonObject.optString("groupId", "");
		String distributorId = jsonObject.optString("distributorId", "");
				
		if("".equals(memberId)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);
		
		jsonPaging = pipelineService.findKYCList(jsonPaging, memberId, keyword, distributorId, groupId);
		
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
	 *获取IFA销售管道-NotYetInvest情况列表
	 * 调用示例:[地址]/service/ifa/pipeline/findKYCList.r
	 * {"memberId":"40280ad455e327690155e3291c582000","keyword":"",
	 * "groupId":"","rows":10,"page":1}
	 */
	@RequestMapping(value = "/findNotYetInvestList")
	@ResponseBody
	public ResultWS findNotYetInvestList(HttpServletRequest request,@RequestBody String body){
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
		
		String memberId = jsonObject.optString("memberId","");
		String keyword = jsonObject.optString("keyword","");
		String groupId = jsonObject.optString("groupId", "");
				
		if("".equals(memberId)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);
		
		jsonPaging = pipelineService.findNotYetInvestList(jsonPaging, memberId, keyword, groupId);
		
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
	 *获取IFA销售管道-组合持仓情况列表
	 * 调用示例:[地址]/service/ifa/pipeline/findPortfolioHoldList.r
	 * {"memberId":"40280ad455e327690155e3291c582000","keyword":"",
	 * "groupId":"","toCurrency":"HKD","rows":10,"page":1}
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
		Integer page = jsonObject.optInt("page", 1);// 获得当前页数
		Integer rows = jsonObject.optInt("rows", 10);// 获得每页记录数isRead
		
		String memberId = jsonObject.optString("memberId","");
		String keyword = jsonObject.optString("keyword","");
		String groupId = jsonObject.optString("groupId", "");
		String toCurrency = jsonObject.optString("toCurrency", "");
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		
		if("".equals(memberId)||"".equals(toCurrency)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);
		
		jsonPaging = pipelineService.findPortfolioHoldList(jsonPaging, memberId, keyword, groupId, toCurrency,langCode);
		
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
	 *获取IFA销售管道-组合计划情况列表
	 * 调用示例:[地址]/service/ifa/pipeline/findPortfolioOrderList.r
	 * {"memberId":"40280ad455e327690155e3291c582000","keyword":"","status":"",
	 * "groupId":"","toCurrency":"HKD","minAmount":"","maxAmount":"","rows":10,"page":1}
	 */
	@RequestMapping(value = "/findPortfolioOrderList")
	@ResponseBody
	public ResultWS findPortfolioOrderList(HttpServletRequest request,@RequestBody String body){
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
		
		String memberId = jsonObject.optString("memberId","");
		String keyword = jsonObject.optString("keyword","");
		String groupId = jsonObject.optString("groupId", "");
		String toCurrency = jsonObject.optString("toCurrency", "");
		String status = jsonObject.optString("status", "");
		String minAmount = jsonObject.optString("minAmount", "");
		String maxAmount = jsonObject.optString("maxAmount", "");
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
				
		if("".equals(memberId)||"".equals(toCurrency)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);
		
		jsonPaging = pipelineService.findPortfolioOrderList(jsonPaging, memberId, keyword, status, minAmount, maxAmount, groupId, toCurrency,langCode);
		
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
	 *获取IFA销售管道-投资方案情况列表
	 * 调用示例:[地址]/service/ifa/pipeline/findProposalList.r
	 * {"memberId":"40280ad455e327690155e3291c582000","clientType":"1","keyword":"","status":"",
	 * "groupId":"","minAmount":"","maxAmount":"","rows":10,"page":1}
	 */
	@RequestMapping(value = "/findProposalList")
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
		Integer page = jsonObject.optInt("page", 1);// 获得当前页数
		Integer rows = jsonObject.optInt("rows", 10);// 获得每页记录数isRead
		
		String memberId = jsonObject.optString("memberId","");
		String clientType = jsonObject.optString("clientType", "");
		String keyword = jsonObject.optString("keyword","");
		String groupId = jsonObject.optString("groupId", "");
		String status = jsonObject.optString("status", "");
		String minAmount = jsonObject.optString("minAmount", "");
		String maxAmount = jsonObject.optString("maxAmount", "");
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		
		if("".equals(memberId)||"".equals(clientType)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);
		
		jsonPaging = pipelineService.findProposalList(jsonPaging, memberId,clientType, keyword, status, minAmount, maxAmount, groupId,langCode);
		
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
	 *获取IFA销售管道-潜在客户列表
	 * 调用示例:[地址]/service/ifa/pipeline/findProspectList.r
	 * {"memberId":"40280ad455e327690155e3291c582000","keyword":"",
	 * "groupId":"","saleStageId":"sales_new","rows":10,"page":1}
	 */
	@RequestMapping(value = "/findProspectList")
	@ResponseBody
	public ResultWS findProspectList(HttpServletRequest request,@RequestBody String body){
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
		
		String memberId = jsonObject.optString("memberId","");
		String keyword = jsonObject.optString("keyword","");
		String groupId = jsonObject.optString("groupId", "");
		String saleStageId = jsonObject.optString("saleStageId", "");
				
		if("".equals(memberId)||"".equals(saleStageId)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);
		
		jsonPaging = pipelineService.findProspectList(jsonPaging, memberId, keyword, groupId, saleStageId);
		
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
	 *获取IFA销售管道-代理商列表
	 * 调用示例:[地址]/service/ifa/pipeline/findAllDistributors.r
	 * {"memberId":"40280ad455e327690155e3291c582000"}
	 */
	@RequestMapping(value = "/findAllDistributors")
	@ResponseBody
	public ResultWS findAllDistributors(HttpServletRequest request,@RequestBody String body){
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
				
		List<MemberDistributor> list = pipelineService.findAllDistributors();
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	
	/**
	 *获取IFA销售管道-查找潜在客户列表
	 * 调用示例:[地址]/service/ifa/pipeline/findInverstorList.r
	 * {"memberId":"40280ad455e327690155e3291c582000","languageSpoken":"",
	 * "invStyle":"","intrest":"","region":"","noIfa":"1","rows":10,"page":1}
	 */
	@RequestMapping(value = "/findInverstorList")
	@ResponseBody
	public ResultWS findInverstor(HttpServletRequest request,@RequestBody String body){
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
		
		String memberId = jsonObject.optString("memberId","");
		String languageSpoken = jsonObject.optString("languageSpoken","");
		String invStyle = jsonObject.optString("invStyle", "");
		String intrest = jsonObject.optString("intrest", "");
		String noIfa = jsonObject.optString("noIfa","");
		String region = jsonObject.optString("region", "");
				
		if("".equals(memberId)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);
		
		jsonPaging = pipelineService.findInverstorNotInCrm(jsonPaging, memberId, languageSpoken, invStyle, intrest, noIfa, region);
		
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
	 *获取IFA销售管道-查找潜在客户详情
	 * 调用示例:[地址]/service/ifa/pipeline/findInverstorDetail.r
	 * {"clientMemberId":"40280ad655bfe9820155bfef8921000c","langCode":"sc"}
	 */
	@RequestMapping(value = "/findInverstorDetail")
	@ResponseBody
	public ResultWS findInverstorDetail(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		String clientMemberId = jsonObject.optString("clientMemberId","");
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);
		
				
		if("".equals(clientMemberId)||"".equals(langCode)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
				
		AppPipelineInvestorDetailVO info = pipelineService.findInverstorDetail(clientMemberId, langCode);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(info);
		return result;
	}
	
	/**
	 *获取IFA销售管道-查找潜在客户列表
	 * 调用示例:[地址]/service/ifa/pipeline/addCustomer.r
	 * {"ifaMemberId":"40280ad455e327690155e3291c582000","clientMemberId":"40280ad65601004c0156010188390030","saleStageId":"sales_new"}
	 */
	@RequestMapping(value = "/addCustomer")
	@ResponseBody
	public ResultWS addCustomer(HttpServletRequest request,@RequestBody String body){
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
		String saleStageId = jsonObject.optString("saleStageId", "sales_new");
				
		if("".equals(ifaMemberId)|| "".equals(clientMemberId)|| "".equals(saleStageId)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}

		
		Map<String,Object> map = new HashMap<String, Object>();
		boolean flag = pipelineService.addCustomer(ifaMemberId, clientMemberId, saleStageId);
		map.put("flag", flag);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(map);
		return result;
	}
	
	/**
	 *获取IFA销售管道-客户列表
	 * 调用示例:[地址]/service/ifa/pipeline/findCustomerList.r
	 * {"memberId":"40280ad455e327690155e3291c582000","keyword":"",
	 * "groupId":"","clientType":"1","rows":10,"page":1}
	 */
	@RequestMapping(value = "/findCustomerList")
	@ResponseBody
	public ResultWS findCustomerList(HttpServletRequest request,@RequestBody String body){
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
		
		String memberId = jsonObject.optString("memberId","");
		String keyword = jsonObject.optString("keyword","");
		String groupId = jsonObject.optString("groupId", "");
		String clientType = jsonObject.optString("clientType", "");
		String langCode = jsonObject.optString("langCode", ""); 
				
		if("".equals(memberId)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);
		
		jsonPaging = pipelineService.findCustomerList(jsonPaging, memberId, clientType, keyword, groupId,langCode);
		
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
