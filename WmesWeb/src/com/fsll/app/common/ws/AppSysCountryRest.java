package com.fsll.app.common.ws;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.common.service.AppSysCountryService;
import com.fsll.app.common.vo.AppSysCountryVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.StrUtils;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.core.entity.SysCountry;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * 国家信息接口
 * 
 * @author zpzhou
 * @date 2016年7月11日
 */
@RestController
@RequestMapping("/service/sysCountry")
public class AppSysCountryRest extends WmesBaseRest{	
	@Autowired
	private AppSysCountryService sysCountryService;
	
	/**
	 * 获得基础数据列表 通过body传递参数
	 * 调用示例:[地址]/service/sysCountry/getListByType.r
		POST_BODY: {"page":1, "rows":10, "code":"A", "name":"安"}
	 */
	@RequestMapping(value = "/getCountryList")
	@ResponseBody
	public ResultWS getCountryList(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
//		result.setData(null);
//		String checkPowerResult = checkPower(jsonObject);
//		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
//			result.setRet(WSConstants.FAIL);
//			result.setErrorCode(checkPowerResult);
//			return result;
//		}

		String code = jsonObject.optString("code","");//获得查询编码
		String name = jsonObject.optString("name","");//获得查询名词
		String page = jsonObject.optString("page", "1");// 获得当前页数
		String rows = jsonObject.optString("rows", "");// 获得每页记录数
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);//获得语言编码
		
		if ("".equals(page) || "".equals(rows)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(Integer.parseInt(page));
		jsonPaging.setRows(Integer.parseInt(rows));
		jsonPaging = sysCountryService.findAll(jsonPaging,code,name);
		List<AppSysCountryVO> list = new ArrayList<AppSysCountryVO>();
		Iterator it = jsonPaging.getList().iterator();
		while (it.hasNext()) {
			SysCountry obj = (SysCountry) it.next();
			AppSysCountryVO vo = new AppSysCountryVO();
			BeanUtils.copyProperties(obj,vo);//拷贝信息
			if(null!=langCode && !"".equals(langCode)){
				vo.setName(vo.getCountryName(langCode));
			}
			vo.setOrderBy(StrUtils.getString(obj.getOrderBy()));
			list.add(vo);
		}				
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		result.setCurPage(jsonPaging.getPage());
		result.setPageSize(jsonPaging.getRows());
		result.setTotal(jsonPaging.getTotal());
		return result;
	}
	
}
