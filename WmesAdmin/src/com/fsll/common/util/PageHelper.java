package com.fsll.common.util;

import com.fsll.common.CommonConstants;
import com.fsll.core.CoreConstants;

/**
 * 页面数据显示格式化工具，提供静态方法，不可以实例化。
 * @author michael
 * @date 2016-11-03
 */
public class PageHelper {
	/**
	 * 禁止实例化
	 */
	private PageHelper() {
	}
	
    /**
     * 格式化的数字字符串
     * @param val
     * @return
     */
    public static String getNumberString(Object val){
    	return getNumberString(val, null, 0);
    }
    
    /**
     * 格式化的数字字符串
     * @param val 数字对象
     * @param format 数字格式，如果空则使用默认格式 PRICE=#,##0.##
     * @return
     */
    public static String getNumberString(Object val, String format){
    	return getNumberString(val, format, 0);
    }
    
    /**
     * 格式化的数字字符串
     * @param val 数字对象
     * @param format 数字格式，如果空则使用默认格式 PRICE=#,##0.##
     * @param rate 兑换率，> 0 时使用
     * @return
     */
    public static String getNumberString(Object val, String format, double rate){
    	return getNumberString(val, format, rate, "");
    }
    
    /**
     * 格式化的数字字符串
     * @param val 数字对象
     * @param format 数字格式，如果空则使用默认格式 PRICE=#,##0.##
     * @param rate 兑换率，> 0 时使用
     * @param replace 替换字符，如果对象为null时使用
     * @return
     */
    public static String getNumberString(Object val, String format, double rate, String replace){
    	if (null==format) format = CoreConstants.NUMBER_PRICE;
    	if (val != null && !"".equals(val.toString())) {
			return StrUtils.getNumberString(val, format, rate);//转换出错会返回“”
		}
    	if (null!=replace) return replace;
    	return "-";//空则返回 -
    }
    
    /**
     * 转换为百分比字符串（会自动往后移两位）
     * @param val
     * @return
     */
    public static String getPercentString(Object val){
    	return getPercentString(val, null);
    }
    
    /**
     * 转换为百分比字符串（会自动往后移两位）
     * @param val 数字对象
     * @param format 数字格式，如果空则使用默认格式 PERCENT = #0.##%
     * @return
     */
    public static String getPercentString(Object val, String format){
    	return getPercentString(val, format, "");
    }
    
    /**
     * 转换为百分比字符串（会自动往后移两位）
     * @param val 数字对象
     * @param format 数字格式，如果空则使用默认格式 PERCENT = #0.##%
     * @param replace 替换字符，如果对象为null时使用
     * @return
     */
    public static String getPercentString(Object val, String format, String replace){
    	if (null==format) format = CoreConstants.NUMBER_PERCENT;
    	if (val != null && !"".equals(val.toString())) {
			return StrUtils.getPercentString(val, format);//转换出错会返回“”
		}
    	if (null!=replace) return replace;
    	return "-";//空则返回 -
    }

    /**
     * 百分比数字格式化，包含颜色格式参数
     * @param s 数字对象
     * @param f 数字格式，如果空则使用默认格式 PRICE=#,###.##
     * @param c 数字颜色
     * @return
     */
    public static String getDisplayString(Object s, String f, String c){
    	return getDisplayString(s, f, c, "");
    }
    
    /**
     * 百分比数字格式化，包含颜色格式参数
     * @param s 数字对象
     * @param f 数字格式，如果空则使用默认格式 PRICE=#,###.##
     * @param c 数字颜色
     * @param replace 替换字符，如果对象为null时使用
     * @return
     */
    public static String getDisplayString(Object s, String f, String c, String replace){
    	if (null==f) f = CoreConstants.NUMBER_PERCENT;
    	if (s != null && !"".equals(s.toString())) {
			double d = StrUtils.getDoubleVal(s);
			//暂时两种颜色格式，1 : 涨“绿”跌“红”，2 : 涨“红”跌“绿”，0 显示灰色。
			if (c==null || "".equals(c) || (!"1".equals(c) && !"2".equals(c))) c = CommonConstants.DEF_DISPLAY_COLOR;
			String style = "price_positive";
			if ("1".equals(c)){//涨“绿”跌“红”
				if (d>0) 
					style = "price_positive";//“绿”
				else if (d<0) 
					style = "price_negative";//“红”
				else //d==0
					style = "price_zero";//“灰色”
			}else if ("2".equals(c)){//涨“红”跌“绿”
				if (d>0) 
					style = "price_negative";//“红”
				else if (d<0) 
					style = "price_positive";//“绿”
				else //d==0
					style = "price_zero";//“灰色”
			}else{
				if (d>0) 
					style = "price_positive";//“绿”
				else if (d<0) 
					style = "price_negative";//“红”
				else //d==0
					style = "price_zero";//“灰色”
			}
			return ("<font class='"+style+"'>"+StrUtils.getPercentString(s, f)+"</font>");//转换出错会返回“”
		}
    	if (null!=replace) return replace;
    	return "-";//空则返回 -
    }
    
	/**
	 * 获取用户头像
	 * @author mqzou
	 * @date 2017-01-07
	 * @param url
	 * @param gender
	 * @return
	 */
	public static String getUserHeadUrl(String url,String gender){
		if(null!=url && !"".equals(url)){
			if(url.startsWith("/res")){
				return url;
			}else if (url.startsWith("/u")) {
				return "/loadImgSrcByPath.do?filePath="+url;
			}else {
				if("F".startsWith(StrUtils.getString(gender).toUpperCase())){
					return "/res/images/common/portrait_f.png";
				}else if("M".startsWith(StrUtils.getString(gender).toUpperCase())){
					return "/res/images/common/portrait_m.png";
				}else {
					return "/res/images/common/portrait.png";
				}
			}
		}else {
			if("F".startsWith(StrUtils.getString(gender).toUpperCase())){
				return "/res/images/common/portrait_f.png";
			}else if("M".startsWith(StrUtils.getString(gender).toUpperCase())){
				return "/res/images/common/portrait_m.png";
			}else {
				return "/res/images/common/portrait.png";
			}
		}
	}
	
	/**
	 * 获取公司Logo Url
	 * @author zxtan
	 * @date 2017-01-09
	 * @param url
	 * @param type  F:Ifafirm  D:Distributor
	 * @return
	 */
	public static String getLogoUrl(String url,String type){
		if(null!=url && !"".equals(url)){
			if(url.startsWith("/res")){
				return url;
			}else if (url.startsWith("/u")) {
				return "/loadImgSrcByPath.do?filePath="+url;
			}else {
				if("F".startsWith(StrUtils.getString(type).toUpperCase())){//ifafirm
					return "/res/images/common/default_ifafirm.png";
				}else if("D".startsWith(StrUtils.getString(type).toUpperCase())){//distributor
					return "/res/images/common/default_distributor.png";
				}else if("C".startsWith(StrUtils.getString(type).toUpperCase())){//company
					return "/res/images/common/default_company.png";
				}else {
					return "/res/images/common/default_no_image.png";
				}
			}
		}else {
			if("F".startsWith(StrUtils.getString(type).toUpperCase())){//ifafirm
				return "/res/images/common/default_ifafirm.png";
			}else if("D".startsWith(StrUtils.getString(type).toUpperCase())){//distributor
				return "/res/images/common/default_distributor.png";
			}else if("C".startsWith(StrUtils.getString(type).toUpperCase())){//company
				return "/res/images/common/default_company.png";
			}else {
				return "/res/images/common/default_no_image.png";
			}
		}
	}
}
