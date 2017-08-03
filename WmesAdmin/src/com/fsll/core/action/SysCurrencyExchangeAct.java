/**
 * 
 */
package com.fsll.core.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.service.SysCurrencyExchangeService;
import com.fsll.wmes.entity.WebExchangeRate;
import com.fsll.wmes.fund.vo.GeneralDataVO;

/**
 * @author scshi_u330p
 *	后台货币转换管理
 *@date 20161101
 */

@Controller
@RequestMapping("/console/sys/currency")
public class SysCurrencyExchangeAct extends CoreBaseAct{
	
	@Autowired
	private SysCurrencyExchangeService sysCurrencyExchangeService;
	
	/**
	 * 分页列表
	 * @author scshi_u330p
	 * @date 20161101
	 * */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		 return "console/sys/currency/list";
	}
	

	/**
	 * modify by rqwang 20170512
	 */
	@RequestMapping(value = "/listJson", method = RequestMethod.POST)
	public void listJson(HttpServletRequest request,HttpServletResponse response,ModelMap model)  {
		String keyWord = request.getParameter("keyWord");
		String langCode = this.getLoginLangFlag(request);
		jsonPaging = this.getJsonPaging(request);
		
		try {
			jsonPaging = sysCurrencyExchangeService.findAll(jsonPaging, keyWord,langCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 汇率转换添加页面
	 * @author scshi_u330p
	 * @date 20161102
	 * */
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public String inputPage(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String lang = this.getLoginLangFlag(request);
		String id = request.getParameter("id");
		WebExchangeRate exchange = new WebExchangeRate();
		if(null!=id && !id.isEmpty()){
			exchange = sysCurrencyExchangeService.findById(id);
		}else{
			exchange.setCreateTime(new Date());
		}
		List<GeneralDataVO> currencyList = this.findSysParameters("currency_type",lang);
		model.put("exchange", exchange);
		model.put("currencyList", currencyList);
		return "console/sys/currency/input";
	}
	
	/**
	 * 保存货币汇率记录
	 * @author scshi_u330p
	 * @date 20161104
	 * */
	@RequestMapping(value = "/currencySave", method = RequestMethod.POST)
	public void currencySave(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String actionMsg="global.success";
		String id = request.getParameter("id");
		String fromCurrency = request.getParameter("fromCurrency");
		String toCurrency = request.getParameter("toCurrency");
		String rate = request.getParameter("rate");
		String isValid = request.getParameter("isValid");
		Date date = new Date();
		WebExchangeRate exchangeRate;
		if(StringUtils.isBlank(id)){
			exchangeRate = new WebExchangeRate();
			exchangeRate.setId(null);
			exchangeRate.setCreateTime(date);
		}else{
			exchangeRate = sysCurrencyExchangeService.findById(id);
		}
		exchangeRate.setFromCurrency(fromCurrency);
		exchangeRate.setToCurrency(toCurrency);
		exchangeRate.setRate(Double.valueOf(rate));
		exchangeRate.setIsValid(isValid);
		exchangeRate.setLastUpdate(date);
		exchangeRate = sysCurrencyExchangeService.saveOrUpdate(exchangeRate);
		
		String msg = PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),actionMsg,null);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",msg);

		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 根据输入code获取已存在的汇率关系
	 * @author scshi_u330p
	 * @date 20161104
	 * @param code 
	 * @param type 1-- 查找原始货币，2--查找目标货币
	 * */
	@RequestMapping(value = "/currencyNoExists", method = RequestMethod.GET)
	public void currencyNoExists(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String currencyCode = request.getParameter("code");
		String currencyType = request.getParameter("type");
		List<WebExchangeRate> list = sysCurrencyExchangeService.findExistExchange(currencyCode,currencyType);
		JsonUtil.toWriter(list, response);
	}

}
