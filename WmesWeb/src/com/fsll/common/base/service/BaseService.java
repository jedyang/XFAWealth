/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.common.base.service;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.fsll.app.investor.me.vo.AppProductInfoVO;
import com.fsll.buscore.trade.service.TradeCoreService;
import com.fsll.common.CommonConstants;
import com.fsll.common.mongodb.MongoDao;
import com.fsll.common.persistence.BaseDao;
import com.fsll.common.persistence.SpringJdbcQueryManager;
import com.fsll.common.util.DateUtil;
import com.fsll.core.CoreConstants;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.entity.SysCountry;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.core.vo.AccessoryFileVO;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.BondInfo;
import com.fsll.wmes.entity.BondInfoEn;
import com.fsll.wmes.entity.BondMarketDay;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundMarket;
import com.fsll.wmes.entity.InsureInfo;
import com.fsll.wmes.entity.InsureInfoEn;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.StockInfo;
import com.fsll.wmes.entity.StockInfoEn;
import com.fsll.wmes.entity.StockMarketDay;
import com.fsll.wmes.entity.WebExchangeRate;
import com.fsll.wmes.portfolio.vo.FundProfitVO;
import com.fsll.wmes.portfolio.vo.PortfolioProfitVO;
import com.fsll.wmes.web.service.WebInterfaceLogService;

/**
 * 业务实现基类：通用模块
 * 
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0 Created On: 2016-2-19
 */
public class BaseService {

	@Autowired
	public BaseDao baseDao;

	@Autowired
	public SpringJdbcQueryManager springJdbcQueryManager;

	@Resource
	public MongoDao mongoDao;

	@Resource
	public WebInterfaceLogService webInterfaceLogService;
	
	@Resource
	public TradeCoreService tradeCoreService;
	
	/**
	 * 获得远程主机ip地址
	 * 
	 * @param request
	 * @return
	 */
	protected String getRemoteHost(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
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
		return null;
	}

	/**
	 * 获得当前的登陆用户
	 * 
	 * @param request
	 * @return
	 */
	protected MemberBase getLoginUser(HttpServletRequest request) {
		Object memberBase = request.getSession().getAttribute(
				CoreConstants.FRONT_USER_LOGIN);
		MemberBase loginMemberBase = null;
		if (memberBase != null) {
			loginMemberBase = (MemberBase) memberBase;
		}
		return loginMemberBase;
	}

	/**
	 * 处理字符串类型的id,添加单引号
	 * 
	 * @param request
	 * @return
	 */
	protected String getNewStrId(String ids) {
		String newIds = "";
		if (!"".equals(ids)) {
			String tmpStr = ids;
			if (tmpStr.endsWith(","))
				tmpStr = tmpStr.substring(0, tmpStr.length() - 1);
			String[] arr = tmpStr.split(",");
			for (String id : arr) {
				if ("".equals(newIds)) {
					newIds = "'" + id + "'";
				} else {
					newIds += ",'" + id + "'";
				}
			}
		}
		return newIds;
	}

	/**
	 * 将语言标识的第一个字母变成大写
	 * 
	 * @param langCode
	 * @return
	 */
	protected String getLangFirstCharUpper(String langCode) {
		String firstChar = langCode.substring(0, 1).toUpperCase();
		String lastStr = langCode.substring(1, langCode.length());
		return firstChar + lastStr;
	}

	/**
	 * 获取语言字符串(首字母大写，用于hql)
	 * 
	 * @param prefix
	 *            前缀
	 * @param langCode
	 *            语言
	 * @return
	 */
	protected String getLangString(String prefix, String langCode) {
		String result = prefix;
		if (null != langCode && !"".equals(langCode)) {// 按语言参数
			result += getLangFirstCharUpper(langCode);
		} else {// 默认语言
			result += getLangFirstCharUpper(CommonConstants.DEF_LANG_CODE);
		}
		return result;
	}

	/**
	 * 查询附件
	 * 
	 * @param relateId
	 *            accessory_file表relate_id
	 * @param moduleId
	 *            模块Id
	 * */
	public List<AccessoryFile> findFileAttr(String relateId, String moduleId) {
		String hql = "from AccessoryFile t where t.relateId=? and t.moduleType=?";
		List args = new ArrayList();
		args.add(relateId);
		args.add(moduleId);
		List<AccessoryFile> fileList = this.baseDao.find(hql, args.toArray(),
				false);
		return fileList;
	}

	/**
	 * 查询附件
	 * 
	 * @param relateId
	 *            accessory_file表relate_id
	 * @param moduleId
	 *            模块Id
	 * */
	public List<AccessoryFileVO> findFileAttrVO(String relateId, String moduleId) {
		String hql = "from AccessoryFile t where t.relateId=? and t.moduleType=?";
		List args = new ArrayList();
		args.add(relateId);
		args.add(moduleId);
		List<AccessoryFile> fileList = this.baseDao.find(hql, args.toArray(), false);
		List<AccessoryFileVO> rList = new ArrayList<AccessoryFileVO>();
		if (fileList != null && !fileList.isEmpty()) {
			for (AccessoryFile vo : fileList) {
				AccessoryFileVO tmpVO = new AccessoryFileVO();
				BeanUtils.copyProperties(vo, tmpVO);
				rList.add(tmpVO);
			}
		}
		return rList;
	}

	/**
	 * 清空附件
	 * 
	 * @param relateId
	 * @param module
	 * */
	public void clearFileAttr(String relateId, String module) {
		String hql = "delete from AccessoryFile t where t.relateId=? and t.moduleType=?";
		List args = new ArrayList();
		args.add(relateId);
		args.add(module);
		this.baseDao.updateHql(hql, args.toArray());
	}
	
	/**
	 * 根据语言获取国家名称
	 * @author mqzou	
	 * @date 2016-07-07
	 * @param sysCountry
	 * @param langCode
	 */
	public String getCountryString(SysCountry sysCountry,String langCode){
		if(null!=sysCountry){
			if("sc".equalsIgnoreCase(langCode)){
				return sysCountry.getNameSc();
			}else if ("tc".equalsIgnoreCase(langCode)) {
				return sysCountry.getNameTc();
			}else if ("en".equalsIgnoreCase(langCode)) {
				return sysCountry.getNameEn();
			}else {
				return "";
			}
		}else {
			return "";
		}
		
	}
    /**
     * 根据货币类型信息，获取两位小数
     * @author zpzhou
     * @param num 字符串长度
     * @return
     */
    public String getFormatNumByCurrency(Double valueStr,String fromCurrency, String toCurrency) {
    	   	
    	Double rate;
    	if(null==valueStr){
    		return "-";
    	}else if(null==fromCurrency || "".equals(fromCurrency) || null==toCurrency || "".equals(toCurrency)){
    		return "-";
    	}else{
    		rate = getExchangeRate(fromCurrency,toCurrency);
    		if (null!=rate){
    			int digits = 2;
    			if("JPY".equalsIgnoreCase(toCurrency)) digits = 0;
    			java.text.DecimalFormat df =  (DecimalFormat) DecimalFormat.getInstance(); // new java.text.DecimalFormat("#.00");
        		df.setMaximumFractionDigits(digits);
        		df.setMinimumFractionDigits(digits);
    	    	df.setGroupingSize(3); 
//    			df.applyPattern("0.0;'@'-#.0");
    			return df.format(valueStr*rate);
    		}else{
    			return "-";
    		}
    	}
    }
    
    /**
     * 根据货币类型信息，获取汇率兑换结果
     * @author zxtan
     * @date 2016-12-22
     * @param valueStr
     * @param fromCurrency 
     * @param toCurrency 
     * @return
     */
    public Double getNumByCurrency(Double valueStr,String fromCurrency, String toCurrency) {
    	Double rate;
    	if(null==valueStr){
    		return null;
    	}else if(null==fromCurrency || "".equals(fromCurrency) || null==toCurrency || "".equals(toCurrency)){
    		return null;
    	}else{
    		rate = getExchangeRate(fromCurrency,toCurrency);
    		if (null!=rate){
    			return valueStr*rate;
    		}else{
    			return null;
    		}
    	}
    }
    
    /**
     * 获取两位小数
     * @author zpzhou
     * @param num 字符串长度
     * @return
     */
    public String getFormatNum(Double valueStr) {
    	if(null==valueStr){
    		return "-";
    	}else{
    		int digits = 2;
    		java.text.DecimalFormat df =  (DecimalFormat) DecimalFormat.getInstance(); // new java.text.DecimalFormat("#.00");
        	df.setMaximumFractionDigits(digits);
    		df.setMinimumFractionDigits(digits);
        	df.setGroupingSize(3); 
    		return df.format(valueStr);
    	}
    }
    
    /**
     * 获取两位小数
     * @author zpzhou
     * @param num 字符串长度
     * @return
     */
    public String getFormatNum(Double valueStr,String currency) {
    	if(null==valueStr){
    		return "-";
    	}else{
    		int digits = 2;
			if("JPY".equalsIgnoreCase(currency)) digits = 0;
    		java.text.DecimalFormat df =  (DecimalFormat) DecimalFormat.getInstance(); // new java.text.DecimalFormat("#.00");
        	df.setMaximumFractionDigits(digits);
    		df.setMinimumFractionDigits(digits);
        	df.setGroupingSize(3); 
    		return df.format(valueStr);
    	}
    }
    
    /**
     * 根据兑换率,获取两位小数
     * @author zpzhou
     * @param num 字符串长度
     * @return
     */
    public String getFormatNumByRate(Double valueStr,Double rate,String currency) {
//    	java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
    	if(null==valueStr || null == rate){
    		return "-";
    	}else{
    		int digits = 2;
			if("JPY".equalsIgnoreCase(currency)) digits = 0;
    		java.text.DecimalFormat df =  (DecimalFormat) DecimalFormat.getInstance(); // new java.text.DecimalFormat("#.00");
        	df.setMaximumFractionDigits(digits);
    		df.setMinimumFractionDigits(digits);
        	df.setGroupingSize(3);
			return df.format(valueStr*rate);    		
    	}
    }
    
    /**
     * 获取两位小数的百分比
     * @author zpzhou
     * @param num 字符串长度
     * @return
     */
    public String getFormatNumByPer(Double valueStr) {
    	java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00%");
    	if(null==valueStr){
    		return "-";
    	}else{
    		return df.format(valueStr);
    	}
    }
    
	/**
	 * 获取兑换率
	 * @author zpzhou
	 * @param fromCurrency
	 * @param toCurrency
	 * @return
	 */
	public Double getExchangeRate(String fromCurrency, String toCurrency) {
		if( StringUtils.isBlank(fromCurrency) || StringUtils.isBlank(toCurrency) ){
    		return null;
    	}else if (fromCurrency.equals(toCurrency)){
			return 1d;
		}else{
			String hql = "from WebExchangeRate t where t.isValid='1' and fromCurrency=? and toCurrency=? order by t.fromCurrency,t.toCurrency  ";
			List params = new ArrayList();
			params.add(fromCurrency);
			params.add(toCurrency);
			List<WebExchangeRate> list = this.baseDao.find(hql, params.toArray(), false);
			if (null!=list && !list.isEmpty()) return list.get(0).getRate();
			return 0d;
		}
	}
	
	/**  
     * 计算两个日期之间相差的天数  
     * @param smdate 较小的时间 
     * @param bdate  较大的时间 
     * @return 相差天数 
     * @throws ParseException  
     */    
    public  int daysBetween(Date smdate,Date bdate) throws ParseException   
    {    
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        smdate=sdf.parse(sdf.format(smdate));  
        bdate=sdf.parse(sdf.format(bdate));  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();         
        long betweenDays=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(betweenDays));           
    }
    
	/**
	 * 获取基础数据名称
	 * @author wwluo
	 * @param langCode 
	 * @param configCode 
	 * @param typeCode 
	 * @return
	 */
	public String getParamConfigName(String langCode, String configCode,String typeCode) {
		String name = null;
		if(StringUtils.isNotBlank(configCode) 
				&& StringUtils.isNotBlank(langCode)
					&& StringUtils.isNotBlank(typeCode)){
			String lang = langCode.substring(0, 1).toUpperCase()+langCode.substring(1, 2);
			StringBuffer hql = new StringBuffer("" +
					" SELECT" +
					" c.name" + lang +
					" FROM" +
					" SysParamConfig c" +
					" WHERE" +
					" c.type.typeCode=?" +
					" AND" +
					" c.configCode=?");
			List<Object> params = new ArrayList<Object>();
			params.add(typeCode);
			params.add(configCode);
			List<String> sysParamConfigs = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(sysParamConfigs != null && !sysParamConfigs.isEmpty()){
				name = sysParamConfigs.get(0);
			}
		}
		return name;
	}
	
	
	/**
	 * 获取基础数据名称
	 * @author zxtan
	 * @param langCode 语言
	 * @param configCodeList 以逗号隔开的code串
	 * @return
	 */
	public String getParamConfigNameList(String langCode, String configCodeList) {
		String name = null;
		if(StringUtils.isNotBlank(configCodeList) 
				&& StringUtils.isNotBlank(langCode)){
			//String lang = langCode.substring(0, 1).toUpperCase()+langCode.substring(1, 2);
			String hql = " SELECT fn_getconfigname(?,?) from SysParamConfig ";
			List<Object> params = new ArrayList<Object>();
			params.add(configCodeList);
			params.add(langCode);
			List list = this.baseDao.find(hql.toString(), params.toArray(),1,2, false);
			if(list != null && !list.isEmpty()){
				name = (String) list.get(0);
			}
		}
		return name;
	}
	
	/**
	 * 获取基础数据
	 * @param configCode 
	 * @return
	 */
	public SysParamConfig getParamConfigByCode(String configCode) {
		if(StringUtils.isNotBlank(configCode)){
			StringBuffer hql = new StringBuffer("FROM SysParamConfig c WHERE c.configCode=?");
			List<Object> params = new ArrayList<Object>();
			params.add(configCode);
			List<SysParamConfig> sysParamConfigs = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(sysParamConfigs != null && !sysParamConfigs.isEmpty()){
				return sysParamConfigs.get(0);
			}
		}
		return null;
	}
	
	/**
	 * 获取基础数据
	 * @param configCode 
	 * @return
	 */
	public List<SysParamConfig> getParamConfigListByTypeCode(String typeCode) {
		if(StringUtils.isNotBlank(typeCode)){
			StringBuilder hql = new StringBuilder();
			hql.append(" FROM SysParamConfig c ");
			hql.append(" WHERE c.isValid='1' ");
			hql.append(" and exists ( select s.id from SysParamType s where s.id=c.type.id and s.typeCode=? ) ");
			hql.append(" order by c.orderBy ");
			List<Object> params = new ArrayList<Object>();
			params.add(typeCode);
			List<SysParamConfig> list = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(list != null && !list.isEmpty()){
				return list;
			}
		}
		return null;
	}
	
	/**
	 * 计算组合收益情况
	 * @author mjhuang 2016-12-13
	 * @param fundAllot 基金分配字符串,格式为       基金1:比例1;基金2:比例2;.....
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	public List<PortfolioProfitVO> findPortfolioProfit(String fundAllot,String startDate,String endDate) {
		List<PortfolioProfitVO> voList = new ArrayList<PortfolioProfitVO>();
		DecimalFormat    df   = new DecimalFormat("######0.00");
		if (!"".equals(fundAllot)) {
			if(fundAllot.endsWith(";"))fundAllot = fundAllot.substring(0,fundAllot.length()-1);
			String[] arr = fundAllot.split(";");
			HashMap<String,List<FundProfitVO>> mapFund = new HashMap<String,List<FundProfitVO>>(); 
			HashMap<String,String> mapAllot = new HashMap<String,String>(); 
			int maxLength = 0;//保存最长的数据长度
			List<FundProfitVO> tmpList = new ArrayList<FundProfitVO>();
			List<FundProfitVO> maxLengthList = new ArrayList<FundProfitVO>();//保存最长的基金数组
			for (String tmp : arr) {
				if(!"".equals(tmp)){
					String[] arrSec = tmp.split(":");
					tmpList = findFundProfit(arrSec[0],startDate,endDate);
					if(tmpList.size()>maxLength){
						maxLength = tmpList.size();
						maxLengthList = tmpList;
					}
					mapFund.put(arrSec[0],tmpList);
					mapAllot.put(arrSec[0],arrSec[1]);
				}
			}
			for(FundProfitVO obj:maxLengthList){
				//每一天计算组合的收益情况
				double dayProfitRate = 0.00;//收益比率
				//每一天计算每一只基金的收益情况
				Iterator iter = mapFund.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String fundId = (String)entry.getKey();
					String fundAllotRate = mapAllot.get(fundId);//分配比例
					String fundProfitRate = "0.00";//收益比率
					List<FundProfitVO> lsSec =  (List<FundProfitVO>)entry.getValue();
					for(FundProfitVO objSec:lsSec){
						if(objSec.getMarketDate().equals(obj.getMarketDate())){
							fundProfitRate = objSec.getTotalRate();
						}
					}
					//收益率的加权平均
					dayProfitRate += Double.parseDouble(fundProfitRate) * (Double.parseDouble(fundAllotRate)/100);
				}
				PortfolioProfitVO vo = new PortfolioProfitVO();
				vo.setMarketDate(obj.getMarketDate());
				vo.setTotalRate(df.format(dayProfitRate));
				voList.add(vo);
			}
		}
		return voList;
	}
	
	/**
	 * 计算基金收益情况,按从startDate购买一份基金计算
	 * @author mjhuang 2016-12-13
	 * @param fundId 基金
	 * @param startDate 开始时间 格式2016-10-13
	 * @param endDate 结束时间 格式2016-12-13
	 * @return
	 */
	public List<FundProfitVO> findFundProfit(String fundId,String startDate,String endDate) {
		String hql=" from FundMarket r where r.isValid='1' and r.fund.id = ? ";
		List<Object> params=new ArrayList<Object>();
		params.add(fundId);
		String startTimeTmp = startDate + " 00:00:00";;
		String endTimeTmp = endDate + " 23:59:59";
		Date startTime = DateUtil.getDate(startTimeTmp, "yyyy-MM-dd HH:mm:ss");
		Date endTime = DateUtil.getDate(endTimeTmp, "yyyy-MM-dd HH:mm:ss");;
		if (startTime!=null && (endTime==null)){
			hql += " and marketDate >= ? ";
			params.add(startTime);
		}
		if (endTime!=null && (startTime==null)){
			hql += " and marketDate <= ? ";
			params.add(endTime);
		}
		if(startTime!=null && endTime!=null){				
			hql += " and (r.marketDate between ? and ?) ";
			params.add(startTime);
			params.add(endTime);
		}
		hql += " order by r.marketDate asc ";
		List<FundMarket> list = this.baseDao.find(hql, params.toArray(), false);
		List<FundProfitVO> voList = new ArrayList<FundProfitVO>();
		if(!list.isEmpty()){
			double startAccNav = list.get(0).getAccNav();//第一天的累计净值
			double lastDayAccNav = 0.00;//上一日累计净值
			DecimalFormat    df   = new DecimalFormat("######0.00");  
			boolean isFirst = true;
			//计算基金的申请份额
			for(FundMarket obj:list){
				FundProfitVO vo = new FundProfitVO();
				vo.setFundId(fundId);
				vo.setMarketDate(DateUtil.dateToDateString(obj.getMarketDate(),"yyyy-MM-dd"));
				if(isFirst){
					startAccNav = obj.getAccNav();
					lastDayAccNav = obj.getAccNav();
					vo.setTotalRate("0.00");
					vo.setTotalPl(0.00);
					vo.setDayPl(0.00);
					isFirst = false;
				}else{
					double tmpTotalRate = ((obj.getAccNav() - lastDayAccNav)/lastDayAccNav)*100;
					vo.setTotalRate(df.format(tmpTotalRate));
					vo.setTotalPl(obj.getAccNav() - startAccNav);
					vo.setDayPl(obj.getAccNav() - lastDayAccNav);
					lastDayAccNav = obj.getAccNav();
				}
				voList.add(vo);
			}
		}
		return voList;
	}

	/**
	 * 根据获取图表URL
	 * @author wwluo	
	 * @date 2017-01-05
	 * @param relateId 关联的id
	 * @param chartSize 图表尺寸类型，   'small、middle'
	 * @param displayColor 涨跌颜色  ，默认为 1（绿涨红跌）， '1:绿涨红跌;2：红涨绿跌'
	 */
	public String getPerformanceChartImage(String relateId,String chartSize,String displayColor){
		String url = null;
		if (StringUtils.isNotBlank(relateId) 
				&& StringUtils.isNotBlank(chartSize)) {
			String color = "gr";
			if (CommonConstants.DISPLAY_COLOR_RG.equals(displayColor)) {
				color = "rg";
			}
			url = "/u/chart/"+relateId + "_" + chartSize + "_" + color + ".png";
			File imageFile = new File(url);
			if(!imageFile.exists()){
				url = null;
			}
		}
		return url;
	}
	
	/**
	 * 根据周期获取开始日期
	 * @author zxtan	
	 * @date 2017-02-08
	 * @param periodCode
	 * @return
	 */
	public String getStartDate(String periodCode) {
		String startDate = "";
		if(periodCode.indexOf("return_period_code_")>-1){
			String period = periodCode.replace("return_period_code_", "");
			if("YTD".equalsIgnoreCase(period)){
				startDate = DateUtil.formatDate(DateUtil.getCurrYearFirst());
			}else if("LAUNCH".equalsIgnoreCase(period)){
				startDate = "";
			}else {
				String num = period.replace("M", "").replace("Y", "");
				
				int unit;
				try {
					unit = Integer.parseInt(num);
				} catch (Exception e) {
					// TODO: handle exception
					unit = 1;
				}
				
				if(period.indexOf("W")>-1){
					startDate = DateUtil.getCurDateStr(Calendar.DATE, -(unit*7));
				}
				
				if(period.indexOf("M")>-1){
					startDate = DateUtil.getCurDateStr(Calendar.MONTH, -unit);
				}
				
				if(period.indexOf("Y")>-1){
					startDate = DateUtil.getCurDateStr(Calendar.YEAR, -unit);
				}
			}		
		}
		return startDate;
	}
	
	public String getMapValue(Map map,String key){
		String value="";
		if(map.containsKey(key)){
			Object object=map.get(key);
			if(null!=object)
				value=object.toString();
		}
		return value;
	}
	
	
	/**
	 * 获取产品最新一条回报数据
	 * @author zxtan
	 * @date 2017-02-24
	 * @param productId
	 * @param unit
	 * @param toCurrency
	 * @return
	 */
	public AppProductInfoVO findProductInfo(String productId,String langCode){
		ProductInfo productInfo = (ProductInfo) baseDao.get(ProductInfo.class, productId);
		AppProductInfoVO infoVO = new AppProductInfoVO();
		infoVO.setProductId(productId);
		List<Object> params = new ArrayList<Object>();
		//double lastPrice = 0.0;
		
		StringBuilder hql = new StringBuilder();
		if("fund".equalsIgnoreCase(productInfo.getType())){
			hql.append(" from FundInfo m inner join "+getLangString("FundInfo", langCode)+" f on f.id=m.id where m.product.id=? ");
			params.add(productId);
			List list = baseDao.find(hql.toString(), params.toArray(), false);
			if(null != list && !list.isEmpty()){
				Object[] objs = (Object[]) list.get(0);
				FundInfo info = (FundInfo) objs[0];
				FundInfoEn infoEn = new FundInfoEn();
				BeanUtils.copyProperties(objs[1], infoEn);
				infoVO.setProductName(infoEn.getFundName());
				infoVO.setProductCode(info.getIsinCode());
				infoVO.setRiskLevel(info.getRiskLevel());
				infoVO.setLastNav(info.getLastNav());
				infoVO.setBaseCurrency(infoEn.getFundCurrencyCode());
			}
			
		}else if("insure".equalsIgnoreCase(productInfo.getType())){
			hql.append(" from InsureInfo m inner join "+getLangString("InsureInfo", langCode)+" f on f.id=m.id where m.product.id=? ");
			params.add(productId);
			List list = baseDao.find(hql.toString(), params.toArray(), false);
			if(null != list && !list.isEmpty()){
				Object[] objs = (Object[]) list.get(0);
				InsureInfo info = (InsureInfo) objs[0];				
				InsureInfoEn infoEn = new InsureInfoEn();
				BeanUtils.copyProperties(objs[1], infoEn);
				infoVO.setProductName(infoEn.getInsureName());
				infoVO.setProductCode(info.getIsinCode());
				infoVO.setRiskLevel(info.getRiskLevel());
				infoVO.setLastNav(info.getLastNav());
				infoVO.setBaseCurrency(infoEn.getInsureCurrencyCode());
			}
			
		}else if("bond".equalsIgnoreCase(productInfo.getType())){
			hql.append(" from BondInfo m ");
			hql.append(" inner join "+getLangString("BondInfo", langCode)+" f on f.id=m.id ");
			hql.append(" inner join BondMarketDay c on c.bond.id=m.id ");
			hql.append(" where m.product.id=? ");
			hql.append(" order by c.priceDate desc ");
			params.add(productId);
			List list = baseDao.find(hql.toString(), params.toArray(), false);
			if(null != list && !list.isEmpty()){
				Object[] objs = (Object[]) list.get(0);
				BondInfo info = (BondInfo) objs[0];
				BondInfoEn infoEn = new BondInfoEn();
				BondMarketDay market = (BondMarketDay) objs[2];
								
				BeanUtils.copyProperties(objs[1], infoEn);
				infoVO.setProductName(infoEn.getBondName());
				infoVO.setProductCode(info.getSymbolCode());
				infoVO.setRiskLevel(null);
				infoVO.setLastNav(market.getTradePrice());
				infoVO.setBaseCurrency(infoEn.getBondCurrencyCode());
			}
		}else if("stock".equalsIgnoreCase(productInfo.getType())){
			hql.append(" from StockInfo m ");
			hql.append(" inner join "+getLangString("StockInfo", langCode)+" f on f.id=m.id ");
			hql.append(" inner join StockMarketDay c on c.stock.id=m.id ");
			hql.append(" where m.product.id=? order by c.priceDate desc ");
			params.add(productId);
			List list = baseDao.find(hql.toString(), params.toArray(), false);
			if(null != list && !list.isEmpty()){
				Object[] objs = (Object[]) list.get(0);
				StockInfo info = (StockInfo) objs[0];
				StockInfoEn infoEn = new StockInfoEn();
				StockMarketDay market = (StockMarketDay) objs[2];

				BeanUtils.copyProperties(objs[1], infoEn);
				infoVO.setProductName(infoEn.getStockName());
				infoVO.setProductCode(info.getSymbolCode());
				infoVO.setRiskLevel(null);
				infoVO.setLastNav(market.getTradePrice());
				infoVO.setBaseCurrency(infoEn.getStockCurrencyCode());
			}
		}
				
		return infoVO;
	}
	
	public String getUserName(String firstName,String lastName,String nameChn,String langCode){
		String userName = "";
		if("en".equalsIgnoreCase(langCode)){
			firstName = firstName== null ? "":firstName;
			lastName = lastName== null? "":lastName;
			userName = firstName + " "+lastName;
			if("".equals(userName.trim())){
				userName = nameChn;
			}
		}else {
			userName = nameChn==null?"":nameChn;
			if("".equals(userName)){
				firstName = firstName== null ? "":firstName;
				lastName = lastName== null? "":lastName;
				userName = firstName + " "+lastName;
			}
		}
		return userName;
	}
	
	/**
	 * 获取基础数据
	 * @param memberId 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getUserName(String memberId, String langCode) {
		String name = null;
		if(StringUtils.isNotBlank(memberId)){
			MemberBase member = (MemberBase) this.baseDao.get(MemberBase.class, memberId);
			if(member != null){
				if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == member.getMemberType()){
					StringBuffer hql = new StringBuffer(" FROM MemberIndividual c WHERE c.member.id=?");
					List<Object> params = new ArrayList<Object>();
					params.add(memberId);
					List<MemberIndividual> individuals = this.baseDao.find(hql.toString(), params.toArray(), false);
					if(individuals != null && !individuals.isEmpty()){
						MemberIndividual individual = individuals.get(0);
						if(StringUtils.isBlank(langCode) || CommonConstants.LANG_CODE_EN.equals(langCode)){
							name = individual.getLastName() + " " + individual.getFirstName();
						}else{
							name = individual.getNameChn();
						}
					}else{
						name = member.getNickName();
					}
				}else if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == member.getMemberType()){
					StringBuffer hql = new StringBuffer(" FROM MemberIfa c WHERE c.member=?");
					List<Object> params = new ArrayList<Object>();
					params.add(memberId);
					List<MemberIfa> ifas = this.baseDao.find(hql.toString(), params.toArray(), false);
					if(ifas != null && !ifas.isEmpty()){
						MemberIfa ifa = ifas.get(0);
						if(StringUtils.isBlank(langCode) || CommonConstants.LANG_CODE_EN.equals(langCode)){
							name = ifa.getLastName() + " " + ifa.getFirstName();
						}else{
							name = ifa.getNameChn();
						}
					}else{
						name = member.getNickName();
					}
				}else{
					name = member.getNickName();
				}
				if (StringUtils.isBlank(name)) {
					name = member.getLoginCode();
				}
				name = name.trim();
			}
		}
		return name;
	}
	
    /**
     * 提取individual的名字
     * @author mqzou 2017-02-24
     * @param individual
     * @return
     */
    public String getIndividualName(MemberIndividual individual,String langCode){
    	String name="";
    	if(CommonConstants.LANG_CODE_EN.equals(langCode)
    			&& (StringUtils.isNotBlank(individual.getLastName())
    					||StringUtils.isNotBlank(individual.getFirstName()))){
    		name=individual.getFirstName() +" "+individual.getLastName();
    	}else {
			name=individual.getNameChn();
		}
    	if("".equals(name)){
    		name=individual.getMember().getNickName();
    	}
    	if("".equals(name)){
    		name=individual.getMember().getLoginCode();
    	}
    	return name;
    }
    
    /**
     * 提取IFA的名字
     * @author mqzou 2017-02-24
     * @param individual
     * @return
     */
    public String getIfaName(MemberIfa ifa,String langCode){
    	String name="";
    	if(CommonConstants.LANG_CODE_EN.equals(langCode)
    			&& (StringUtils.isNotBlank(ifa.getLastName())
				||StringUtils.isNotBlank(ifa.getFirstName()))){
    		name=ifa.getFirstName() +" "+ifa.getLastName();
    	}else {
			name=ifa.getNameChn();
		}
    	if("".equals(name)){
    		name=ifa.getMember().getNickName();
    	}
    	if("".equals(name)){
    		name=ifa.getMember().getLoginCode();
    	}
    	return name;
    }
    
    
    /**
     * 获得用户的名称----简单方法
     * @author mjhuang 2017-05-09
     * @param memberId=member_base.id
     * @param 语言编码
     * @param 类别 0或者null = 默认,1=呢称 2=真名 
     * @return 用户名称
     */
    public String getCommonMemberName(String memberId,String langCode,String type){
    	String returnName = "";
    	MemberBase memberBase = (MemberBase)this.baseDao.get(MemberBase.class, memberId);
    	if (memberBase == null) return returnName;
    	if(type != null && "1".equals(type)){//返回呢称
    		returnName = memberBase.getNickName();
    	}else if(type != null && "2".equals(type)){//返回真名
        	if(memberBase.getSubMemberType() != null && 11==memberBase.getSubMemberType()){//当为investor 个人时
    			String hqlTmp = "from MemberIndividual r where r.member.id = ? ";
    			List paramsTmp = new ArrayList();
    			paramsTmp.add(memberBase.getId());
    			List<MemberIndividual> listTmp = this.baseDao.find(hqlTmp, paramsTmp.toArray(), false);
    			if(null!=listTmp && !listTmp.isEmpty()){
    				MemberIndividual objTmp = listTmp.get(0);
    				String enName = objTmp.getFirstName() == null ? "":objTmp.getFirstName();
    				if(objTmp.getLastName() != null && !"".equals(objTmp.getLastName())){
    					enName +=" "+objTmp.getLastName();
    				}
    				String chiName = objTmp.getNameChn();
    				if(chiName == null || "".equals(chiName)){
    					chiName = enName;
    				}
    				if(langCode.equals(CommonConstants.LANG_CODE_EN)){
    					returnName = enName;
    				}else{// 中文版：显示中文名，如果没，则显示英文名
    					returnName = chiName;
    				}
    				if("".equals(returnName)){
    					if(memberBase.getNickName() != null && !"".equals(memberBase.getNickName())){
    						returnName = memberBase.getNickName();
    					}else{
    						returnName = memberBase.getLoginCode();
    					}
    				}
    			}	
        	}else if(memberBase.getSubMemberType() != null && 21==memberBase.getSubMemberType()){//当为ifa时
    			String hqlTmp = "from MemberIfa r where r.member.id = ? ";
    			List paramsTmp = new ArrayList();
    			paramsTmp.add(memberBase.getId());
    			List<MemberIfa> listTmp = this.baseDao.find(hqlTmp, paramsTmp.toArray(), false);
    			if(null!=listTmp && !listTmp.isEmpty()){
    				MemberIfa objTmp = listTmp.get(0);
    				String enName = objTmp.getFirstName() == null ? "":objTmp.getFirstName();
    				if(objTmp.getLastName() != null && !"".equals(objTmp.getLastName())){
    					enName +=" "+objTmp.getLastName();
    				}
    				String chiName = objTmp.getNameChn();
    				if(chiName == null || "".equals(chiName)){
    					chiName = enName;
    				}
    				if(langCode.equals(CommonConstants.LANG_CODE_EN)){
    					returnName = enName;
    				}else{// 中文版：显示中文名，如果没，则显示英文名
    					returnName = chiName;
    				}
    				if("".equals(returnName)){
    					if(memberBase.getNickName() != null && !"".equals(memberBase.getNickName())){
    						returnName = memberBase.getNickName();
    					}else{
    						returnName = memberBase.getLoginCode();
    					}
    				}
    			}
        	}
    	}else{
        	if(memberBase.getSubMemberType() != null && 11==memberBase.getSubMemberType()){//当为investor 个人时
    			String hqlTmp = "from MemberIndividual r where r.member.id = ? ";
    			List paramsTmp = new ArrayList();
    			paramsTmp.add(memberBase.getId());
    			List<MemberIndividual> listTmp = this.baseDao.find(hqlTmp, paramsTmp.toArray(), false);
    			if(null!=listTmp && !listTmp.isEmpty()){
    				MemberIndividual objTmp = listTmp.get(0);
    				String enName = objTmp.getFirstName() == null ? "":objTmp.getFirstName();
    				if(objTmp.getLastName() != null && !"".equals(objTmp.getLastName())){
    					enName +=" "+objTmp.getLastName();
    				}
    				String chiName = objTmp.getNameChn();
    				if(chiName == null || "".equals(chiName)){
    					chiName = enName;
    				}
    				if(langCode.equals(CommonConstants.LANG_CODE_EN)){
    					if(!"".equals(enName) && !"".equals(chiName)){//同时有中文和英文
    						returnName = enName+"("+chiName+")";
    					}else if(!"".equals(enName) && "".equals(chiName)){//只有英文
    						returnName = enName;
    					}else if("".equals(enName) && !"".equals(chiName)){//只有中文
    						returnName = chiName;
    					}
    				}else{// 中文版：显示中文名，如果没，则显示英文名
    					if(!"".equals(chiName)){
    						returnName = chiName;
    					}else{
    						returnName = enName;
    					}
    				}
    				if("".equals(returnName)){
    					if(memberBase.getNickName() != null && !"".equals(memberBase.getNickName())){
    						returnName = memberBase.getNickName();
    					}else{
    						returnName = memberBase.getLoginCode();
    					}
    				}
    			}	
        	}else if(memberBase.getSubMemberType() != null && 21==memberBase.getSubMemberType()){//当为ifa时
    			String hqlTmp = "from MemberIfa r where r.member.id = ? ";
    			List paramsTmp = new ArrayList();
    			paramsTmp.add(memberBase.getId());
    			List<MemberIfa> listTmp = this.baseDao.find(hqlTmp, paramsTmp.toArray(), false);
    			if(null!=listTmp && !listTmp.isEmpty()){
    				MemberIfa objTmp = listTmp.get(0);
    				String enName = objTmp.getFirstName() == null ? "":objTmp.getFirstName();
    				if(objTmp.getLastName() != null && !"".equals(objTmp.getLastName())){
    					enName +=" "+objTmp.getLastName();
    				}
    				String chiName = objTmp.getNameChn();
    				if(chiName == null || "".equals(chiName)){
    					chiName = enName;
    				}
    				if(langCode.equals(CommonConstants.LANG_CODE_EN)){
    					if(!"".equals(enName) && !"".equals(chiName)){//同时有中文和英文
    						returnName = enName+"("+chiName+")";
    					}else if(!"".equals(enName) && "".equals(chiName)){//只有英文
    						returnName = enName;
    					}else if("".equals(enName) && !"".equals(chiName)){//只有中文
    						returnName = chiName;
    					}
    				}else{// 中文版：显示中文名，如果没，则显示英文名
    					if(!"".equals(chiName)){
    						returnName = chiName;
    					}else{
    						returnName = enName;
    					}
    				}
    				if("".equals(returnName)){
    					if(memberBase.getNickName() != null && !"".equals(memberBase.getNickName())){
    						returnName = memberBase.getNickName();
    					}else{
    						returnName = memberBase.getLoginCode();
    					}
    				}
    			}
        	}
    	}
    	return returnName;
    }
    
    /**
     * 更新ifa的人气值
     * @author mqzou  2017-06-27
     * @param memberId
     * @param value
     */
    public void updateIfaPopularity(String memberId,int value){
    	String hqlTmp = "from MemberIfa r where r.member.id = ? ";
		List paramsTmp = new ArrayList();
		paramsTmp.add(memberId);
		List<MemberIfa> listTmp = this.baseDao.find(hqlTmp, paramsTmp.toArray(), false);
		if(null!=listTmp && !listTmp.isEmpty()){
			MemberIfa ifa = listTmp.get(0);
			int total=null!=ifa.getPopularityTotal()?ifa.getPopularityTotal():0;
			ifa.setPopularityTotal(total+value);
			this.baseDao.update(ifa);
		}
    }
}
