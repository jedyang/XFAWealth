package com.fsll.app.investor.market.ws;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.investor.market.service.AppIndexChoiceService;
import com.fsll.app.investor.market.vo.AppIndexFundTopVO;
import com.fsll.app.investor.market.vo.AppIndexTopCategoryVO;
import com.fsll.common.CommonConstants;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * 首页你的选择接口服务
 * @author zpzhou
 * @date 2016-9-26
 */
@RestController
@RequestMapping("/service/indexChoice")
public class AppIndexChoiceRest extends WmesBaseRest{
	@Autowired
	private AppIndexChoiceService indexChoiceService;
	
	/**
	 * 得到首页你的选择信息列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/indexChoice/findIndexTopCategory.r
	 *  {"langCode":"sc"}
	 */
	@RequestMapping(value = "/findIndexTopCategory")
	@ResponseBody
	public ResultWS findIndexTopCategory(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 语言编码
		List<AppIndexTopCategoryVO> list= indexChoiceService.findIndexTopCategory(langCode);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 得到首页最佳基金信息列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/indexChoice/findIndexFundTop.r
	 *  {"langCode":"sc","period":"return_period_code_1M",num:3}
	 */
	@RequestMapping(value = "/findIndexFundTop")
	@ResponseBody
	public ResultWS findIndexFundTop(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 语言编码
		String period = jsonObject.optString("period",CommonConstants.RETURN_PERIOD_CODE_1M);
		int num = jsonObject.optInt("num", 3);//返回条数
		List<AppIndexFundTopVO> list= indexChoiceService.findIndexFundTop(period,langCode,num);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
}
