package com.fsll.wmes.web.action.console;

import java.net.URLDecoder;
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
import com.fsll.core.entity.SysCountry;
import com.fsll.core.vo.SysCountryVO;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.web.service.CountryService;


/**
 * 控制器:国家管理
 * @author wwluo
 * @version 1.0
 * @date 2016-08-03
 */
@Controller
public class CountryAct extends WmesBaseAct{
	
	@Autowired
	private CountryService countryService;
	
	/**
	 * 跳转分页列表页面
	 * @author wwluo
	 * @date 2016-08-03
	 */
    @RequestMapping(value = "/console/country/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
        return "console/sys/country/list";
    }
    
	/**
	 * 数据查询的方法
	 * @author wwluo
	 * @date 2016-08-03 
	 */
	@RequestMapping(value = "/console/country/listJson", method = RequestMethod.POST)
	public void listJson(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String keyWord = request.getParameter("keyWord");
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = countryService.findAll(jsonPaging, keyWord);
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 异步获取国家列表
	 * @author qgfeng
	 * @date 2016-11-4
	 * 
	 */
	@RequestMapping(value = "/console/country/langListJson", method = RequestMethod.POST)
	public void langListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode = this.getLoginLangFlag(request);
		String keyWord = request.getParameter("keyWord");
		List<GeneralDataVO> countryList = countryService.findCountryList(keyWord, langCode);
		String countryJson = JsonUtil.toJson(countryList);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result", true);
		obj.put("countryJson", countryJson);
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 跳转国家信息页面
	 * @author wwluo
	 * @date 2016-08-03 
	 */
	@RequestMapping(value = "/console/country/detail", method = RequestMethod.GET)
	public String individualAdd(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		SysCountryVO vo = new SysCountryVO();
		if(StringUtils.isNotBlank(id))
		   vo = countryService.findById(id);
		model.put("syscountryvo", vo);
		return "console/sys/country/input";
	}
	
	/**
	 * 保存国家信息
	 * @author wwluo
	 * @date 2016-08-03
	 */
	@RequestMapping(value = "/console/country/save", method = RequestMethod.POST)
	public void update(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws Exception {
        String params = request.getParameter("params");
        String[] inputDataList = params.split("&amp;");
		Map<String,Object> paramsMap = new HashMap<String,Object>();
		for(String memberStr:inputDataList){
			String[] memberStrArray = memberStr.split("=");
			
			paramsMap.put(memberStrArray[0], memberStrArray.length < 2 ? "" : URLDecoder.decode(memberStrArray[1],"utf-8"));
		}
		SysCountry sysCountry = new SysCountry();
		if(StringUtils.isNotEmpty((String) paramsMap.get("id")))
		    sysCountry.setId((String) paramsMap.get("id"));
		else sysCountry.setId(null);
		
		sysCountry.setNameSc((String) paramsMap.get("nameSc"));
		sysCountry.setNameTc((String) paramsMap.get("nameTc"));
		
		sysCountry.setNameEn((String) paramsMap.get("nameEn"));
		sysCountry.setAreaCode((String) paramsMap.get("areaCode"));
		sysCountry.setIsoCode2((String) paramsMap.get("isoCode2"));
		sysCountry.setIsoCode3((String) paramsMap.get("isoCode3"));
		if(StringUtils.isNotEmpty((CharSequence) paramsMap.get("orderBy"))){
			Integer orderBy = Integer.parseInt((String) paramsMap.get("orderBy"));
			sysCountry.setOrderBy(orderBy);
		}else 
			sysCountry.setOrderBy(null);
		boolean flag = countryService.saveOrUpdate(sysCountry);
        Map<String, Object> obj = new HashMap<String, Object>();
        obj.put("flag",flag);
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 删除国家信息
	 * @author wwluo
	 * @date 2016-08-03
	 */
	@RequestMapping(value = "/console/country/delete", method = RequestMethod.GET)
	public void deleteById(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		Map<String, Object> obj = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(id)){
			countryService.deleteById(id);
		    obj.put("flag",true);
		}else obj.put("flag",false);
		JsonUtil.toWriter(obj, response);
	}

}
