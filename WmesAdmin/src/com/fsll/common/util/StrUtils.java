package com.fsll.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.ParserException;


/**
 * 字符串的帮助类，提供静态方法，不可以实例化。
 * 
 */
public class StrUtils {
	public static final String PERCENT = "##.00%";
	public static final String PRICE = "#,###.00";
	/**
	 * 禁止实例化
	 */
	private StrUtils() {
	}
	
	private static final char[] N62_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
                                             'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	private static final char[] N36_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
                                             'v', 'w', 'x', 'y', 'z'     };
	
    private static StringBuilder longToNBuf(long l, char[] chars) {
        int upgrade = chars.length;
        StringBuilder result = new StringBuilder();
        int last;
        while (l > 0) {
            last = (int) (l % upgrade);
            result.append(chars[last]);
            l /= upgrade;
        }
        return result;
    }
    
    /**
     * 长整数转换成N62
     * 
     * @param l
     * @return
     */
    public static String longToN62(long l) {
        return longToNBuf(l, N62_CHARS).reverse().toString();
    }
    
    public static String longToN36(long l) {
        return longToNBuf(l, N36_CHARS).reverse().toString();
    }
    
    /**
     * 长整数转换成N62
     * 
     * @param l
     * @param length 如N62不足length长度，则补足0。
     * @return
     */
    public static String longToN62(long l, int length) {
        StringBuilder sb = longToNBuf(l, N62_CHARS);
        for (int i = sb.length(); i < length; i++) {
            sb.append('0');
        }
        return sb.reverse().toString();
    }
    
    public static String longToN36(long l, int length) {
        StringBuilder sb = longToNBuf(l, N36_CHARS);
        for (int i = sb.length(); i < length; i++) {
            sb.append('0');
        }
        return sb.reverse().toString();
    }
    
    /**
     * N62转换成整数
     * 
     * @param n62
     * @return
     */
    public static long n62ToLong(String n62) {
        return nToLong(n62, N62_CHARS);
    }
    
    public static long n36ToLong(String n36) {
        return nToLong(n36, N36_CHARS);
    }
    
    private static long nToLong(String s, char[] chars) {
        char[] nc = s.toCharArray();
        long result = 0;
        long pow = 1;
        for (int i = nc.length - 1; i >= 0; i--, pow *= chars.length) {
            int n = findNIndex(nc[i], chars);
            result += n * pow;
        }
        return result;
    }
    
    private static int findNIndex(char c, char[] chars) {
        for (int i = 0; i < chars.length; i++) {
            if (c == chars[i]) {
                return i;
            }
        }
        throw new RuntimeException("N62(N36)非法字符：" + c);
    }

	/**
	 * 处理url
	 * 
	 * url为null返回null，url为空串或以http://或https://开头，则加上http://，其他情况返回原参数。
	 * 
	 * @param url
	 * @return
	 */
	public static String handelUrl(String url) {
		String urlTmp = url;
		if (urlTmp == null) {
			return null;
		}
		urlTmp = urlTmp.trim();
		if ("".equals(urlTmp) || urlTmp.startsWith("http://") || urlTmp.startsWith("https://")) {
			return urlTmp;
		} else {
			return "http://" + urlTmp.trim();
		}
	}

	/**
	 * 分割并且去除空格
	 * 
	 * @param str
	 *            待分割字符串
	 * @param sep
	 *            分割符
	 * @param sep2
	 *            第二个分隔符
	 * @return 如果str为空，则返回null。
	 */
	public static String[] splitAndTrim(String str, String sep, String sep2) {
		String strTmp = str;
		if (StringUtils.isBlank(strTmp)) {
			return null;
		}
		if (!StringUtils.isBlank(sep2)) {
			strTmp = StringUtils.replace(strTmp, sep2, sep);
		}
		String[] arr = StringUtils.split(strTmp, sep);
		// trim
		for (int i = 0, len = arr.length; i < len; i++) {
			arr[i] = arr[i].trim();
		}
		return arr;
	}

	/**
	 * 文本转html
	 * 
	 * @param txt
	 * @return
	 */
	public static String txt2htm(String txt) {
		if (StringUtils.isBlank(txt)) {
			return txt;
		}
		StringBuilder sb = new StringBuilder((int) (txt.length() * 1.2));
		char c;
		boolean doub = false;
		for (int i = 0; i < txt.length(); i++) {
			c = txt.charAt(i);
			if (c == ' ') {
				if (doub) {
					sb.append(' ');
					doub = false;
				} else {
					sb.append("&nbsp;");
					doub = true;
				}
			} else {
				doub = false;
				switch (c) {
				case '&':
					sb.append("&amp;");
					break;
				case '<':
					sb.append("&lt;");
					break;
				case '>':
					sb.append("&gt;");
					break;
				case '"':
					sb.append("&quot;");
					break;
				case '\n':
					sb.append("<br/>");
					break;
				default:
					sb.append(c);
					break;
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 剪切文本。如果进行了剪切，则在文本后加上"..."
	 * 
	 * @param s
	 *            剪切对象。
	 * @param len
	 *            编码小于256的作为一个字符，大于256的作为两个字符。
	 * @return
	 */
	public static String textCut(String s, int len, String append) {
		if (s == null) {
			return null;
		}
		int slen = s.length();
		if (slen <= len) {
			return s;
		}
		// 最大计数（如果全是英文）
		int maxCount = len * 2;
		int count = 0;
		int i = 0;
		for (; count < maxCount && i < slen; i++) {
			if (s.codePointAt(i) < 256) {
				count++;
			} else {
				count += 2;
			}
		}
		if (i < slen) {
			if (count > maxCount) {
				i--;
			}
			if (!StringUtils.isBlank(append)) {
				if (s.codePointAt(i - 1) < 256) {
					i -= 2;
				} else {
					i--;
				}
				return s.substring(0, i) + append;
			} else {
				return s.substring(0, i);
			}
		} else {
			return s;
		}
	}

	public static String htmlCut(String s, int len, String append) {
		String text = html2Text(s, len * 2);
		return textCut(text, len, append);
	}

	public static String html2Text(String html, int len) {
		try {
			Lexer lexer = new Lexer(html);
			Node node;
			StringBuilder sb = new StringBuilder(html.length());
			while ((node = lexer.nextNode()) != null) {
				if (node instanceof TextNode) {
					sb.append(node.toHtml());
				}
				if (sb.length() > len) {
					break;
				}
			}
			return sb.toString();
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * p换行
	 * @param inputString
	 * @return
	 */
	public static String removeHtmlTagP(String inputString) {  
	    if (inputString == null)  
	        return null;  
	    String htmlStr = inputString; // 含html标签的字符串  
	    String textStr = "";  
	    java.util.regex.Pattern pScript;  
	    java.util.regex.Matcher mScript;  
	    java.util.regex.Pattern pStyle;  
	    java.util.regex.Matcher mStyle;  
	    java.util.regex.Pattern pHtml;  
	    java.util.regex.Matcher mHtml;  
	    try {  
	        //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>  
	        String regExScript = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";   
	        //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>  
	        String regExStyle = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";   
	        String regExHtml = "<[^>]+>"; // 定义HTML标签的正则表达式  
	        pScript = Pattern.compile(regExScript, Pattern.CASE_INSENSITIVE);  
	        mScript = pScript.matcher(htmlStr);  
	        htmlStr = mScript.replaceAll(""); // 过滤script标签  
	        pStyle = Pattern.compile(regExStyle, Pattern.CASE_INSENSITIVE);  
	        mStyle = pStyle.matcher(htmlStr);  
	        htmlStr = mStyle.replaceAll(""); // 过滤style标签  
	        htmlStr.replace("</p>", "\n");
	        pHtml = Pattern.compile(regExHtml, Pattern.CASE_INSENSITIVE);  
	        mHtml = pHtml.matcher(htmlStr);  
	        htmlStr = mHtml.replaceAll(""); // 过滤html标签  
	        textStr = htmlStr;  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	    return textStr;// 返回文本字符串  
	}  
	
	public static String removeHtmlTag(String inputString) {  
	    if (inputString == null)  
	        return null;  
	    String htmlStr = inputString; // 含html标签的字符串  
	    String textStr = "";  
	    java.util.regex.Pattern pScript;  
	    java.util.regex.Matcher mScript;  
	    java.util.regex.Pattern pStyle;  
	    java.util.regex.Matcher mStyle;  
	    java.util.regex.Pattern pHtml;  
	    java.util.regex.Matcher mHtml;  
	    try {  
	        //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>  
	        String regExScript = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";   
	        //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>  
	        String regExStyle = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";   
	        String regExHtml = "<[^>]+>"; // 定义HTML标签的正则表达式  
	        pScript = Pattern.compile(regExScript, Pattern.CASE_INSENSITIVE);  
	        mScript = pScript.matcher(htmlStr);  
	        htmlStr = mScript.replaceAll(""); // 过滤script标签  
	        pStyle = Pattern.compile(regExStyle, Pattern.CASE_INSENSITIVE);  
	        mStyle = pStyle.matcher(htmlStr);  
	        htmlStr = mStyle.replaceAll(""); // 过滤style标签  
	        pHtml = Pattern.compile(regExHtml, Pattern.CASE_INSENSITIVE);  
	        mHtml = pHtml.matcher(htmlStr);  
	        htmlStr = mHtml.replaceAll(""); // 过滤html标签  
	        textStr = htmlStr;  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	    return textStr;// 返回文本字符串  
	}  

	/**
	 * 检查字符串中是否包含被搜索的字符串。被搜索的字符串可以使用通配符'*'。
	 * 
	 * @param str
	 * @param search
	 * @return
	 */
	public static boolean contains(String str, String search) {
		if (StringUtils.isBlank(str) || StringUtils.isBlank(search)) {
			return false;
		}
		String reg = StringUtils.replace(search, "*", ".*");
		Pattern p = Pattern.compile(reg);
		return p.matcher(str).matches();
	}

	public static boolean containsKeyString(String str) {
		if (StringUtils.isBlank(str)) {
			return false;
		}
		if (str.contains("'") || str.contains("\"") || str.contains("\r")
				|| str.contains("\n") || str.contains("\t")
				|| str.contains("\b") || str.contains("\f")) {
			return true;
		}
		return false;
	}
	
	
	public static String addCharForString(String str, int strLength,char c,int position) {
		  String strTmp = str;
		  int strLen = strTmp.length();
		  if (strLen < strLength) {
			  while (strLen < strLength) {
			  StringBuffer sb = new StringBuffer();
			  if(position==1){
				  //右補充字符c
				  sb.append(c).append(strTmp);
			  }else{
				//左補充字符c
				  sb.append(strTmp).append(c);
			  }
			  strTmp = sb.toString();
			  strLen = strTmp.length();
			  }
			}
		  return strTmp;
	 }

	// 将""和'转义
	public static String replaceKeyString(String str) {
		if (containsKeyString(str)) {
			return str.replace("'", "\\'").replace("\"", "\\\"").replace("\r",
					"\\r").replace("\n", "\\n").replace("\t", "\\t").replace(
					"\b", "\\b").replace("\f", "\\f");
		} else {
			return str;
		}
	}
	
	//单引号转化成双引号
	public static String replaceString(String str) {
		if (containsKeyString(str)) {
			return str.replace("'", "\"").replace("\"", "\\\"").replace("\r",
					"\\r").replace("\n", "\\n").replace("\t", "\\t").replace(
					"\b", "\\b").replace("\f", "\\f");
		} else {
			return str;
		}
	}
	
	public static String getSuffix(String str) {
		int splitIndex = str.lastIndexOf(".");
		return str.substring(splitIndex + 1);
	}

    /**
     * 过滤null值，返回字符串 by michael
     * 
     * @param element
     * @return
     */
    public static String getString(Object element) {
        String result = "";
        if (element != null) {
            result = element.toString();
        }
        
        return result.trim();
    }
    
    /**
     * 过滤null值，返回字符串 by michael
     * 
     * @param element
     * @return
     */
    public static String getString(double val) {
        return String.valueOf(val);
    }
    
    /**
     * 过滤null值，返回字符串 by michael
     * 
     * @param element
     * @return
     */
    public static String getString(Object element, String replace) {
        String result = getString(element);
        if (result.isEmpty()) {
            result = getString(replace);
        }
        
        return result.trim();
    }
    
	/**
	 * 获取小数，过滤null值，返回double by michael
	 * 
	 * @param object 对象
	 * @return
	 */
	public static double getDoubleVal(Object val) {
		Double result = getDouble(val);
        if (result != null) {
            return result.doubleValue();
        }
        
        return -1;
	}  
	
    /**
     * 过滤null值，返回double by michael
     * 
     * @param val
     * @return
     */
    public static Double getDouble(Object val) {
        try {
        	if (null==val) return null;
            return Double.valueOf(getString(val));
		} catch (Exception e) {
			// TODO: handle exception
//			e.printStackTrace();
		}
        
        return null;
    }
    
    /**
     * 过滤null值，返回int by michael
     * 
     * @param val
     * @return
     */
    public static int getInt(String val) {
        Integer result = getInteger(val);
        if (result != null) {
            return result.intValue();
        }
        
        return -1;
    }
    
   
    /**
     * 过滤null值，返回int by michael
     * 
     * @param val
     * @return
     */
    public static int getInt(Integer val) {
    	if (val != null) {
            return val.intValue();
        }
        
        return -1;
    }
    
    /**
     * 过滤null值，返回Integer by michael
     * 
     * @param val
     * @return
     */
    public static Integer getInteger(Object val) {
    	try {
    		if (null==val) return null;
    		return Integer.valueOf(getString(val));
		} catch (Exception e) {
			// TODO: handle exception
		}
 
        return null;
    }
    
    /**
     * 格式化的数字字符串
     * @param val
     * @return
     */
    public static String getNumberString(Object val){
    	try {
        	DecimalFormat df = (DecimalFormat)NumberFormat.getInstance(); 
        	df.applyPattern(PRICE); 
        	return df.format(Double.parseDouble(getString(val)));
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "";
    }
    
    /**
     * 格式化的数字字符串
     * @param val 数字对象
     * @param format 数字格式，如果空则使用默认格式 PRICE=#,###.##
     * @return
     */
    public static String getNumberString(Object val, String format){
		return getNumberString(val, format, 0);
    }
    
    /**
     * 格式化的数字字符串
     * @param val 数字对象
     * @param format 数字格式，如果空则使用默认格式 PRICE=#,###.##
     * @param rate 兑换率，> 0 时使用
     * @return
     */
    public static String getNumberString(Object val, String format, double rate){
    	try {
    		if (null==format || "".equals(format)) format = PRICE;
        	DecimalFormat df = (DecimalFormat)NumberFormat.getInstance(); 
        	df.applyPattern(format); 
        	
        	if(rate>0)
        		return df.format(Double.parseDouble(getString(val))*rate);
        	else
        		return df.format(Double.parseDouble(getString(val)));
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "";
    }
    
    /**
     * 转换为百分比字符串（会自动往后移两位）
     * @param val
     * @return
     */
    public static String getPercentString(Object val){
    	try {
        	DecimalFormat df = (DecimalFormat)NumberFormat.getInstance(); 
        	df.applyPattern(PERCENT); 
        	return df.format(Double.parseDouble(getString(val)));
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "";
    }
    
    /**
     * 转换为百分比字符串（会自动往后移两位）
     * @param val 数字对象
     * @param format 数字格式，如果空则使用默认格式 PERCENT = ##.##%
     * @return
     */
    public static String getPercentString(Object val, String format){
    	try {
    		if (null==format || "".equals(format)) format = PERCENT;
        	DecimalFormat df = (DecimalFormat)NumberFormat.getInstance(); 
        	df.applyPattern(format); 
        	return df.format(Double.parseDouble(getString(val)));
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "";
    }
    
    /**
     * 把逗号分隔的字符串转换为含引号的分隔字符串（ 1,2,3 -> '1','2','3'），用户sql条件
     * @param val 源字符串
     * @param quote 引号，默认单引号
     * @return
     */
    public static String seperateWithQuote(String val, String quote){
    	if (quote==null || "".equals(quote)) quote="'";
    	val = getString(val);
    	StringBuffer r = new StringBuffer();
    	if (val.length()>0 && val.indexOf(quote)<0 )
    	try {
    		String[] idArrStrings = val.split(",");//空串的过滤，如果是“.”和“|”都是转义字符，必须得加"\\";
            if (idArrStrings!=null && idArrStrings.length>0){
            	for(String x: idArrStrings){
            		if (x.trim().length()>0){
            			if (r.length()>0) r.append(",");
            			r.append(quote+x.trim()+quote);
            		}
            	}
            }

        	return r.toString();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return val;
    }
    
    public static String arrayListToString(List<String> arr, String sep){
    	try {
    		String result = "";
    		if (!arr.isEmpty()){
    			for (String s: arr){
    				if (result.length()>0) result += sep;
    				result += s;
    			}
    		}
			return result;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
    }
    
    //随机产生数字与字母组合的字符串  
    private final static String RANDOMSTRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZasdfghjklqwertyuiopzxcvbnm";  
    private static Random random = new Random();
    /**
     * 获取随机的字符 串
     * @author michael
     * @param num 字符串长度
     * @return
     */
    public static String getRandomString(int num) {
    	String resultString = "";
    	for (int i=0;i<num;i++)
    		resultString += Character.toString(RANDOMSTRING.charAt(random.nextInt(RANDOMSTRING.length())));
        return resultString;  
    }
    
    /**
     * 截取字符串方法 能够支持中英混排，英文不能截断单词
     * @author 林文伟
     * @param text 字符串
     * @param subLength 截断长度
     * @param isAppendDot 是否追加点号
     * @return
     */
    public String substringStr(String text,int subLength,Boolean isAppendDot){
		 int length = text.length();
		 if(subLength>=length)return text;
		 
		 String returnStr = "";
		 char [] chArray=text.toCharArray();
		 if(chArray[subLength+1]==' '||chArray[subLength+1]=='\0'){//如果第14个是空格，则表示单词完结
			 returnStr = text+(isAppendDot==true?"...":"");
		 }
		 else if(isLetter(chArray[subLength+1])){//判断是否是英文字母
			 String temp = text.substring(0, subLength);
			 int lastindex = temp.lastIndexOf(" ");
			 String temp1 = temp.substring(0, lastindex);
			 returnStr = temp1+(isAppendDot==true?"...":"");
		 }
		 else{ //不是空格，也不是英文，则照截取
			 String temp = text.substring(0, subLength);
			 returnStr = temp+(isAppendDot==true?"...":"");
		 }
		 return returnStr;
	}
    
    public static boolean isLetter(char c) {  
        int k = 0x80;  
        return (c / k) == 0 ? true : false;  
    }  

    /**
     * 过来null字符串对象，如果是空串则用replace替换
     * @param val
     * @param replace
     * @return
     */
    public static String filterNullString(String val, String replace){
    	if (getString(val).length()>0) return getString(val);
    	else return getString(replace);
    }
    
    /**
     * 将字段串按指定分隔符转换成hashMap格式,用于its指令转换
     * @author mjhuang 2016-12-29
     * @param oriStr
     * @param sep
     * @return
     */
    public static Map stringToMap(String oriStr){
    	Map result = new HashMap();
		String tmpMsg = oriStr;
		if(tmpMsg.startsWith("|"))tmpMsg = tmpMsg.substring(1,tmpMsg.length());
		if(tmpMsg.endsWith("|"))tmpMsg = tmpMsg.substring(0,tmpMsg.length()-1);
		tmpMsg = tmpMsg.replace("||", "|null|");
    	if(tmpMsg != null && tmpMsg.length() > 0){
    		StringTokenizer token=new StringTokenizer(tmpMsg,"|");
    		int index = 0;
    		String key = "";
            while(token.hasMoreElements()){
            	String tmpVal = token.nextToken();
            	tmpVal = tmpVal == null ? "" : tmpVal;
				if(index % 2 == 0){//偶数时
					key = tmpVal;
				}else{
					if("null".equals(tmpVal)){
						result.put(key,"");
					}else{
						result.put(key,tmpVal);
					}
				}
				index ++;
            }
    	}
		return result;
    }
    
    /**
     * 判断字符串是否有效，null 返回true
     * @param s
     * @return
     */
    public static boolean isNull(String s){
    	if (null==s) return true;
    	return false;
    }

    /**
     * 判断字符串是否有效，null 或 空串 都返回true
     * @param s
     * @return
     */
    public static boolean isEmpty(String s){
    	s = getString(s);
    	if (s.trim().length()==0) return true;
    	return false;
    }
    /**
     * double类型相加
     * @author mqzou 
     * @param value1
     * @param value2
     * @param length 保留的小数长度
     */
    public static double doubleAdd(double value1,double value2,int length){
    	value1=getDoubleVal(value1);
    	value2=getDoubleVal(value2);
    	BigDecimal decimal1=new BigDecimal(value1);
		BigDecimal decimal2=new BigDecimal(value2);
		return decimal1.setScale(length, BigDecimal.ROUND_HALF_UP).add(decimal2.setScale(length, BigDecimal.ROUND_HALF_UP)).doubleValue();
    }
    
    /**
     * 字符串去重、去空
     * "aaa,aaa,bbb,ccc,,eee," --> "aaa,bbb,ccc,eee"
     * @author wwluo
     * @return
     */
    public static String reHeavy(String arg){
    	String result = "";
    	if (StringUtils.isNotBlank(arg)) {
    		if(arg.indexOf(",") > -1){
    			String[] orgs = arg.split(",");
    			Set<String> set = new TreeSet<String>();
    			for (int i = 0; i < orgs.length; i++) {
    				if (StringUtils.isNotBlank(orgs[i])) {
    					set.add(orgs[i]);
					}
				}
    			for (String str : set) {
    					result += str + ",";
				}
    			result = result.substring(0,result.length()-1);
    		}else{
    			result = arg;
    		}
		}
    	return result;
    }
    
    /** 
     * 获取指定HTML标签的指定属性的值 
     * @param source 要匹配的源文本 
     * @param element 标签名称 
     * @param attr 标签的属性名称 
     * @return 属性值列表 
     */  
    public static List<String> match(String source, String element, String attr) {  
        List<String> result = new ArrayList<String>();  
        String reg = "<" + element + "[^<>]*?\\s" + attr + "=['\"]?(.*?)['\"]?(\\s.*?)?>";  
        Matcher m = Pattern.compile(reg).matcher(source);  
        while (m.find()) {  
            String r = m.group(1);  
            result.add(r);  
        }  
        return result;  
    } 
    
    /** 
     * 获取指定HTML标签的指定属性的值 (只获取一条)
     * @param source 要匹配的源文本 
     * @param element 标签名称 
     * @param attr 标签的属性名称 
     * @return 属性值列表 
     */  
    public static String matchOne(String source, String element, String attr) {  
       // List<String> result = new ArrayList<String>();  
        String reg = "<" + element + "[^<>]*?\\s" + attr + "=['\"]?(.*?)['\"]?(\\s.*?)?>";  
        Matcher m = Pattern.compile(reg).matcher(source);  
        while (m.find()) {  
            String r = m.group(1);
            return r;
           // result.add(r);  
        }  
        return "";  
    } 

}
