package com.fsll.app.investor.me.ws;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.common.service.AppSysCountryService;
import com.fsll.app.common.service.AppSysParamService;
import com.fsll.app.investor.me.service.AppCrmProposalMessService;
import com.fsll.app.investor.me.service.AppIfaInfoService;
import com.fsll.app.investor.me.service.AppInsightInfoService;
import com.fsll.app.investor.me.service.AppNewsInfoService;
import com.fsll.app.investor.me.service.AppPortfolioMessService;
import com.fsll.app.investor.me.vo.AppCommunityTopicItemVO;
import com.fsll.app.investor.me.vo.AppCrmProposalMessVo;
import com.fsll.app.investor.me.vo.AppIfaItemVO;
import com.fsll.app.investor.me.vo.AppIfaSearchConditionVO;
import com.fsll.app.investor.me.vo.AppIfaSearchItemVO;
import com.fsll.app.investor.me.vo.AppInsightInfoVo;
import com.fsll.app.investor.me.vo.AppInsightItemVo;
import com.fsll.app.investor.me.vo.AppNewsInfoItemVO;
import com.fsll.app.investor.me.vo.AppNewsItemVo;
import com.fsll.app.investor.me.vo.AppPortfolioHoldVO;
import com.fsll.app.investor.me.vo.AppPortfolioMessVo;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * 基金信息查询服务接口实现
 * 
 * @author zpzhou
 * @date 2016-7-25
 */
@RestController
@RequestMapping("/service/ifaInfo")
public class AppIfaInfoRest extends WmesBaseRest {

	@Autowired
	private AppSysParamService paramService;
	@Autowired
	private AppSysCountryService sysCountryService;
	@Autowired
	private AppIfaInfoService ifaInfoService;
	@Autowired
	private AppPortfolioMessService portfolioMessService;
	@Autowired
	private AppCrmProposalMessService proposalMessService;
	@Autowired
	private AppInsightInfoService insightInfoService;
	@Autowired
	private AppNewsInfoService newsInfoService;
	
	/**
	 * 该接口适用于APP
	 * Ifa搜索条件， 通过body传递参数 调用示例:[地址]/service/ifaInfo/getIfaSearchCondition.r
	 * {"langCode":"sc"}
	 */
	@RequestMapping(value = "/getIfaSearchCondition")
	@ResponseBody
	public ResultWS getIfaSearchCondition(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		String langCode=jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		List<AppIfaSearchItemVO> seList = paramService.getParamListByType(langCode,"service_region");//服务区域
		List<AppIfaSearchItemVO> exList = paramService.getParamListByType(langCode,"expertise_type");//擅长领域
		List<AppIfaSearchItemVO> coList = sysCountryService.findAllCountryList(langCode);//居住地
		List<AppIfaSearchItemVO> disList = ifaInfoService.getDistributorMess();//分销商
		AppIfaSearchConditionVO vo=new AppIfaSearchConditionVO();
		vo.setCoList(coList);
		vo.setDisList(disList);
		vo.setExList(exList);
		vo.setSeList(seList);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(vo);
		return result;
	}
	
	/**
	 * 该适用于APP
	 * IFA列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/ifaInfo/findIfaList.r
	 * {"langCode":"sc", "page":1, "rows":15, "serviceRegion":"", "expertiseType":"", "country":"1,2", 
	   "distributor":"","_user_id_":"40280a25559b852e01559b8615da0002" }
	 */
	@RequestMapping(value = "/findIfaList")
	@ResponseBody
	public ResultWS findIfaList(HttpServletRequest request,@RequestBody String body) {
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
		String serviceRegion = jsonObject.optString("serviceRegion", "");//服务区域
		String expertiseType = jsonObject.optString("expertiseType", "");//擅长领域
		String country = jsonObject.optString("country", "");//居住地
		String distributor = jsonObject.optString("distributor", "");//分销商
		String userId = jsonObject.optString("_user_id_", "");// 获得当前登录用户ID
		String langCode=jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		
		if ("".equals(page) && "".equals(rows)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(Integer.parseInt(page));
		jsonPaging.setRows(Integer.parseInt(rows));

		jsonPaging = ifaInfoService.findIfaList(jsonPaging, langCode, userId, serviceRegion, expertiseType, country, distributor);

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
	 * 该接口适用于APP
	 * Ifa添加/取消好友， 通过body传递参数 调用示例:[地址]/service/ifaInfo/saveWebFriendMess.r
	 * {"langCode":"sc","fromMemberId":"40280a25559b852e01559b8615da0002",
	   "toMemberId":"40280a25559b852e01559b89c80f0004","applyMsg":"测试测试","opType":"Add"}
	 */
	@RequestMapping(value = "/saveWebFriendMess")
	@ResponseBody
	public ResultWS saveWebFriendMess(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		//String langCode=jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);//获得语言
		String fromMemberId = jsonObject.optString("fromMemberId", "");//发起的会员ID
		String toMemberId = jsonObject.optString("toMemberId", "");//关联的会员ID
		String applyMsg = jsonObject.optString("applyMsg", "");//申请信息
		String opType = jsonObject.optString("opType", "");//Add-添加;Delete-删除
		if ("".equals(fromMemberId) && "".equals(toMemberId) && "".equals(opType)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		int status = ifaInfoService.saveWebFriendMess(fromMemberId, toMemberId,  applyMsg, opType);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(status);
		return result;
	}
	
	
	/**
	 * 【我的投资顾问】该适用于APP
	 * @author zxtan
	 * IFA列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/ifaInfo/findMyIfaList.r
	 * {"memberId":"40280ad65601004c0156010188390029","langCode":"sc" }
	 */
	@RequestMapping(value = "/findMyIfaList")
	@ResponseBody
	public ResultWS findMyIfaList(HttpServletRequest request,@RequestBody String body) {
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
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		
		if ("".equals(memberId) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		

		List<AppIfaItemVO> list = ifaInfoService.findMyIfaList(memberId, langCode);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 该适用于APP
	 * IFA列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/ifaInfo/findMyIfaPortfolioList.r
	 * {"memberId":"40280ad65601004c0156010188390029","ifaId":"40280ad455e327690155e3291ifa2000","toCurrency":"HKD","periodCode":"return_period_code_1M" }
	 */
	@RequestMapping(value = "/findMyIfaPortfolioList")
	@ResponseBody
	public ResultWS findMyIfaPortfolioList(HttpServletRequest request,@RequestBody String body) {
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
		String ifaId = jsonObject.optString("ifaId", "");// 获得当前Ifa ID
		String periodCode = jsonObject.optString("periodCode", CommonConstants.RETURN_PERIOD_CODE_1M);// 获得回报周期
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		String toCurrency = jsonObject.optString("toCurrency", "");// 获得转换货币
				
		if ("".equals(memberId) || "".equals(ifaId) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}

		if("".equals(periodCode)){
			periodCode = CommonConstants.RETURN_PERIOD_CODE_1M;
		}
		
		List<AppPortfolioHoldVO> list = portfolioMessService.findMyIfaPortfolioList(memberId, ifaId, periodCode, toCurrency,langCode);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 该适用于APP
	 * IFA列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/ifaInfo/findMyIfaProposalList.r
	 * {"memberId":"40280ad65601004c0156010188390024","ifaId":"40280ad455e327690155e3291ifa2000","periodCode":"return_period_code_1M","toCurrency":"" }
	 */
	@RequestMapping(value = "/findMyIfaProposalList")
	@ResponseBody
	public ResultWS findMyIfaProposalList(HttpServletRequest request,@RequestBody String body) {
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
		String ifaId = jsonObject.optString("ifaId", "");// 获得当前Ifa ID		
		String toCurrency = jsonObject.optString("toCurrency", "");// 获得转换货币
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);
		
		if ("".equals(memberId) || "".equals(ifaId) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
				
		List<AppCrmProposalMessVo> list = proposalMessService.findMyIfaProposalList(memberId, ifaId, toCurrency,langCode);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	
	/**
	 * 该适用于APP
	 * IFA列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/ifaInfo/findMyIfaInsightList.r
	 * {"memberId":"40280ad65601004c0156010188390024","ifaMemberId":"40280ad455e327690155e3291c582000","langCode":"sc" }
	 */
	@RequestMapping(value = "/findMyIfaInsightList")
	@ResponseBody
	public ResultWS findMyIfaInsightList(HttpServletRequest request,@RequestBody String body) {
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
		String ifaMemberId = jsonObject.optString("ifaMemberId", "");// 获得当前Ifa用户ID
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE); 		
		if ("".equals(memberId) || "".equals(ifaMemberId)|| "".equals(langCode) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
				
		List<AppCommunityTopicItemVO> list = insightInfoService.findMyIfaInsightList(memberId, ifaMemberId,langCode);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 观点详细接口， 通过body传递参数 
	 * 调用示例:[地址]/service/ifaInfo/findInsightInfo.r
	 * {"topicId":"40280a295ad6a4f9015ad6a7428ca001","langCode":"sc"}
	 */
	@RequestMapping(value = "/findInsightInfo")
	@ResponseBody
	public ResultWS findInsightInfo(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String topicId = jsonObject.optString("topicId", "");// 观点ID
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 获得当前Ifa用户ID
		
		if ("".equals(topicId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		AppCommunityTopicItemVO vo = insightInfoService.findInsightInfo(topicId,langCode);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(vo);
		return result;
	}
	
	/**
	 * 该适用于APP
	 * IFA列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/ifaInfo/findMyIfaNewsList.r
	 * {"memberId":"40280ad655bfe9820155bfef8921000c","ifaMemberId":"40280ad455e327690155e3291c582000","langCode":"sc"}
	 */
	@RequestMapping(value = "/findMyIfaNewsList")
	@ResponseBody
	public ResultWS findMyIfaNewsList(HttpServletRequest request,@RequestBody String body) {
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
		String ifaMemberId = jsonObject.optString("ifaMemberId", "");// 获得当前Ifa用户ID
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 获得当前Ifa用户ID
				
		if ("".equals(memberId) || "".equals(ifaMemberId)|| "".equals(langCode) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
				
		List<AppNewsInfoItemVO> list = newsInfoService.findMyIfaNewsList(memberId, ifaMemberId,langCode);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 新闻内容接口， 通过body传递参数 
	 * 调用示例:[地址]/service/ifaInfo/findNewsInfo.r
	 * {"id":"9b466f3d-ebe2-4346-9724-8948b263e9f4"}
	 */
	@RequestMapping(value = "/findNewsInfo")
	@ResponseBody
	public ResultWS findNewsInfo(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String id = jsonObject.optString("id","");
		if ("".equals(id) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}


		AppNewsInfoItemVO info = newsInfoService.findNewsInfo(id);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(info);
		return result;
	}
	
}
