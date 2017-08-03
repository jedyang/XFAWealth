/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.core.base;

import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.action.BaseAct;
import com.fsll.common.util.CSRFTokenUtil;
import com.fsll.common.util.ChineseToEnglish;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.entity.SysAdmin;
import com.fsll.core.entity.SysLog;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.core.service.SysLogService;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.fund.vo.GeneralDataVO;

/**
 * 控制器基类：核对模块
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-2-19
 */
public abstract class CoreBaseAct extends BaseAct{
	
	protected JsonPaging jsonPaging = new JsonPaging();
	
	@Autowired
	private SysLogService sysLogService;
	
    @Autowired
    private SysParamService sysParamService;
    
	/**
	 * 验证CSRFToken的值
	 * 
	 * @param request
	 * @return true合法,false表示不合法
	 */
    protected boolean virifyCSRFToken(HttpServletRequest request) {
    	HttpSession session = request.getSession();
    	String csrfToken = request.getParameter("CSRFToken") == null ? null:request.getParameter("CSRFToken");
        if(session == null || csrfToken == null || !csrfToken.equals(StrUtils.getString(session.getAttribute(CSRFTokenUtil.CSRF_TOKEN_FOR_SESSION_ATTR_NAME)))){
        	return false;
        }else{
        	return true;
        }
	}
    
	/**
	 * 获得当前的登陆用户语言
	 * 
	 * @param request
	 * @return
	 */
	protected String getLoginLangFlag(HttpServletRequest request) {
		Object obj = request.getSession().getAttribute(CoreConstants.LANG_CODE);
		if (obj != null) {
			return (String) obj;
		}
		return CommonConstants.DEF_LANG_CODE;
	}

	/**
	 * 获得当前的登陆用户
	 * 
	 * @param request
	 * @return
	 */
	protected SysAdmin getLoginUser(HttpServletRequest request) {
		Object memberBase = request.getSession().getAttribute(CoreConstants.USER_LOGIN);
		SysAdmin sysAdmin = null;
		if (memberBase != null) {
			sysAdmin = (SysAdmin) memberBase;
		}
		return sysAdmin;
	}
    
	
	/**
	 * 返回分页json给页面
	 * @param response
	 * @param jsonPaging
	 */
	public void toJsonString(HttpServletResponse response,JsonPaging jsonPaging) {
		Integer record = jsonPaging.getTotal();
		//int _totalPages = (int) (record % jsonPaging.getRows());
		//Integer total = (int) (record / jsonPaging.getRows());
		//if (_totalPages > 0)total++;
		//if (jsonPaging.getPage() > total)jsonPaging.setPage(total);
		Map<String, Object> statuts = new HashMap<String, Object>();
		//statuts.put("page",jsonPaging.getPage());
		statuts.put("total",record);
		//statuts.put("records",record);
		statuts.put("rows",jsonPaging.getList());
		JsonUtil.toWriter(statuts, response);
	}
	
	
	/**
	 * 初始化分页参数
	 * @param response
	 * @param jsonPaging
	 */
	public JsonPaging getJsonPaging(HttpServletRequest request) {
		Integer page = request.getParameter("page") == null ? 1:Integer.parseInt(request.getParameter("page"));
		Integer rows = request.getParameter("rows") == null ? 10:Integer.parseInt(request.getParameter("rows"));
		//String sord = request.getParameter("sord") == null ? "":request.getParameter("sord");
		//String sidx = request.getParameter("sidx") == null ? "":request.getParameter("sidx");
		String sort = request.getParameter("sort") == null ? "":request.getParameter("sort");
		String order = request.getParameter("order") == null ? "":request.getParameter("order");
	    //Integer rows = 0;  //指示每页显示的记录条数。
	    //Integer page = 0;  //当前页         返回中要包含：当前是第几页数据
	    //Integer total = 0;  //总页数        返回中要包含：总共有多少页数据
//	    Integer record = 0;  ////总记录数   返回中要包含：总的记录条数
	    //String sord;  //指示查询排序的方式，可能的值是ASC和DESC
	    //String sidx; //指示查询排序的条件,这是一个字符串，可能是数据库表字段或者是POJO对象的属性名.这需要程序来处理
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);
		jsonPaging.setOrder(order);
		jsonPaging.setSort(sort);
		return jsonPaging;
	}

	public void setJsonPaging(JsonPaging jsonPaging) {
		this.jsonPaging = jsonPaging;
	}
	
	/**
	 * 系统操作日志添加
	 * @param request 请求 
     * @param loginCode 登陆ID  可以为空
     * @param nickName 名称   可以为空
	 * @param moduleType  登陆 退出 故障 其他为模块的具体名称
	 * @param remark 备注
	 */
	public void saveSysLog(HttpServletRequest request,String loginCode,String nickName,String moduleType, String remark){
		SysLog sysLog = new SysLog();
		if(this.getLoginUser(request) != null){
			sysLog.setAdmin(this.getLoginUser(request));
		}
		sysLog.setLoginCode(loginCode);
		sysLog.setNickName(nickName);
		sysLog.setIp(this.getRemoteHost(request));
		sysLog.setModuleType(moduleType);
		sysLog.setRemark(remark);
		sysLog.setCreateTime(new Date());
		sysLogService.saveOrUpdate(sysLog);
	}
    
	public String toUTF8String(String val){
    	try {
        	if (null!=val && !"".equals(val)){
        		val = URLDecoder.decode(val,"UTF-8");
        		return val;
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
    }
    
	/**
	 * 调用ws接口，获取返回结果(适应与RestFul的url参数方式的调用)
	 * @param urlString  http://www.xx.com/service/servicename?a=a&b=b...
	 * @return
	 */
	protected String sendWebServiceByUrl(String urlString){
		String res = ""; 
		try { 
			URL url = new URL(urlString);
			java.net.HttpURLConnection conn = (java.net.HttpURLConnection)url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(),"UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				res += line;
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * 调用ws接口，获取返回结果(适应与RestFul的body参数方式的调用)
	 * @param urlString http://www.xx.com/service/servicename
	 * @param bodyString 参数json字符串
	 */
	public String sendWebServiceByBody(String urlString, String bodyString) {
		RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        JSONObject jsonObj = JSONObject.fromObject(bodyString);
        HttpEntity<String> formEntity = new HttpEntity<String>(jsonObj.toString(), headers);
        String result = restTemplate.postForObject(urlString, formEntity, String.class);
        return result;
	}
	
	/**
	 * 获取环境变量（通过请求获取语言编码）
	 * @param request 请求
	 * @param key 环境变量编码
	 * @return
	 */
	public String getProperty(HttpServletRequest request, String key){
		return PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),key,null);
	}
	
	public String getProperty(String langCode, String key){
		return PropertyUtils.getPropertyValue(langCode,key,null);
	}
	
	 /**
     * 获取基础参数表
     * （基金类型fund_type、教育程度education、就业情况employment、货币类型currency_type、主题分类fund_sector、
     * 		职业分类occupation、年龄段age_scope、费用类型fees_item）
     * 
     * @return List<GeneralDataVO>
     */
	public List<GeneralDataVO>  findSysParameters(String type, String lang){
		List<GeneralDataVO> list = new ArrayList<GeneralDataVO>();
		List<SysParamConfig> configs = sysParamService.findParamConfigByType(type);
		lang = (lang==null)?"":lang;
		if (configs!=null && !configs.isEmpty()){
			for(SysParamConfig c: configs){
				GeneralDataVO vo = new GeneralDataVO();
				vo.setId(c.getId());
				vo.setItemCode(c.getConfigCode());
				if ("en".equals(lang)){
					vo.setName(c.getNameEn());
					vo.setValue(c.getConfValueEn());
					vo.setKey((null!=c.getNameEn() && c.getNameEn().length()>0)?c.getNameEn().substring(0,1).toUpperCase():"");
				}else if ("tc".equals(lang)){
					vo.setName(c.getNameTc());
					vo.setValue(c.getConfValueTc());
					//设定拼音为关键词
					try {
						vo.setKey(ChineseToEnglish.getPinYinHeadChar(c.getNameTc()));
					} catch (Exception e) {
						// TODO: handle exception
						vo.setKey(c.getNameEn());
					}
				}else {
					vo.setName(c.getNameSc());
					vo.setValue(c.getConfValueSc());
					//设定拼音为关键词
					try {
						vo.setKey(ChineseToEnglish.getPinYinHeadChar(c.getNameSc()).substring(0,1).toUpperCase());
					} catch (Exception e) {
						// TODO: handle exception
						vo.setKey(c.getNameEn());
					}
				}
				
				list.add(vo);
			}
		}
		return list;
	}
	
    	
}
