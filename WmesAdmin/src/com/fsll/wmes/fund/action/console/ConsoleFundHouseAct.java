package com.fsll.wmes.fund.action.console;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.ResponseUtils;
import com.fsll.core.entity.SysAdmin;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.entity.FundHouse;
import com.fsll.wmes.entity.FundHouseEn;
import com.fsll.wmes.entity.FundHouseSc;
import com.fsll.wmes.entity.FundHouseTc;
import com.fsll.wmes.fund.service.FundHouseService;
import com.fsll.wmes.fund.vo.FundHouseDataVO;

/**
 * 控制器:基金公司信息管理（后台）
 * 
 * @author Yan
 * @version 1.0.0 Created On: 2016-11-16
 */

@Controller
@RequestMapping("/console/fund/fundhouse")
public class ConsoleFundHouseAct extends WmesBaseAct {
	
	@Autowired
	private FundHouseService fundHouseService;
	
    /**
     * 分页列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
        SysAdmin admin = this.getLoginUser(request);
        if(null != admin){
        	return "console/fund/fundhouse/list";//页面摆放路径
        }else{
        	return "redirect:"+this.getFullPath(request)+"viewLogin.do";
        }
    }
    
    /**
     * 分页获得记录
     */
    @RequestMapping(value = "/listJson", method = RequestMethod.POST)
    public String listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String langCode = this.getLoginLangFlag(request);
    	String houseName = request.getParameter("houseName");
    	String startDate = request.getParameter("startDate");
    	String endDate = request.getParameter("endDate");
        jsonPaging = this.getJsonPaging(request);
        jsonPaging.setSort("r.createTime");
        
        jsonPaging = fundHouseService.findAll(jsonPaging,houseName,startDate,endDate, langCode);
        this.toJsonString(response, jsonPaging);
        return null;
    }
    
    /**
     * 详细信息页面
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String fundsdetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String id = request.getParameter("id");
        model.put("id", id);
        FundHouseDataVO fundHouseDataVO = fundHouseService.findById(id);
        model.put("fundHouseDataVO", fundHouseDataVO);
        System.out.println(fundHouseDataVO.getLogoUrl());
        
        return "console/fund/fundhouse/input";
    }
    
    /**
	 * 编辑页面
	 */
	@RequestMapping(value = "/edit")
	public String questform(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.isMobileDevice(request,model);
		String id=request.getParameter("id");
		FundHouse fundHouse = fundHouseService.findFundHouseById(id);
		FundHouseSc fundHouseSc = fundHouseService.findFundHouseScById(id);
		FundHouseTc fundHouseTc = fundHouseService.findFundHouseTcById(id);
		FundHouseEn fundHouseEn = fundHouseService.findFundHouseEnById(id);
		String language = this.getLoginLangFlag(request);//获取语言
		
		model.put("fundHouse", fundHouse);
		model.put("fundHouseSc", fundHouseSc);
		model.put("fundHouseTc", fundHouseTc);
		model.put("fundHouseEn", fundHouseEn);
		model.put("language", language);
		
		return "console/fund/fundhouse/edit";
	}
	
	/****
	 * 新增/保存编辑
	 * 
	 * modify by rqwang 20170511
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void save(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		boolean isAdd =false;
		
		String id=request.getParameter("id");
		String houseNameSc=request.getParameter("houseNameSC");
		String pinYinSc=request.getParameter("pinYinSC");
		String houseNameTc=request.getParameter("houseNameTC");
		String pinYinTc=request.getParameter("pinYinTC");
		String houseNameEn=request.getParameter("houseNameEN");
		String pinYinEn=request.getParameter("pinYinEN");
		
		String imgLogoUrl=request.getParameter("imgLogoUrl");
		
		
		//创建实体
		FundHouse info = new FundHouse();
		FundHouseSc infoSc = new FundHouseSc();
		FundHouseTc infoTc = new FundHouseTc();
		FundHouseEn infoEn = new FundHouseEn();
		Date date = new Date();
		
		if(null==id||"".equals(id))
		{
			isAdd = true;
			info.setId(null);
			infoSc.setId(null);
			infoTc.setId(null);
			infoEn.setId(null);
			info.setCreateTime(date);
		} else {
			isAdd = false;
			//info.setId(id);
			infoSc.setId(id);
			infoTc.setId(id);
			infoEn.setId(id);
			info = fundHouseService.findFundHouseById(id);
		}

		info.setLastUpdate(date);
		info.setIsValid("1");
		
		info.setLogoUrl(imgLogoUrl);
		
		infoSc.setHouseName(houseNameSc);
		infoSc.setPinYin(pinYinSc);
		infoTc.setHouseName(houseNameTc);
		infoTc.setPinYin(pinYinTc);
		infoEn.setHouseName(houseNameEn);
		infoEn.setPinYin(pinYinEn);
				
		info = fundHouseService.save(info, infoSc, infoTc, infoEn, isAdd);
			
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	/****
	 * 删除
	 */
	@RequestMapping(value = "/del", method = RequestMethod.POST)
	public void del(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String id=request.getParameter("id");		
		fundHouseService.deleteById(id);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
		ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
	}
	
	
	/**
	 * 自动获取基金发行公司的汉语拼音首字母
	 * @param str
	 * @return 汉语拼音首字母
	 * @throws UnsupportedEncodingException
	 * @author rqwang
	 */
	@RequestMapping(value="/getPinyin",method = RequestMethod.GET )
	@ResponseBody
	public String getPinyin(@RequestParam("str") String str) throws UnsupportedEncodingException{
		String houseNameSc = URLDecoder.decode(str,"utf-8");
		String pinYinSc = URLDecoder.decode(houseNameSc,"utf-8");
		char[] charArray = pinYinSc.toCharArray();
		String result="";
		for (char c : charArray) {
			String[] hanyuPinyinStringArray = PinyinHelper.toHanyuPinyinStringArray(c);
			if(hanyuPinyinStringArray==null){
				result +=c;
			}else {
				result += hanyuPinyinStringArray[0].toCharArray()[0];							
			}
		}
		result=result.toUpperCase();
		return result;
	}
}
