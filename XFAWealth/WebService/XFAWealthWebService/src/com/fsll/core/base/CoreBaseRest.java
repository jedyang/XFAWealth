/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.core.base;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.fsll.api.member.service.MemberBaseService;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.action.BaseAct;
import com.fsll.common.util.DESUtils;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.core.service.CoreBaseService;

/**
 * 控制器基类：通用模块
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-2-19
 */
public abstract class CoreBaseRest extends BaseAct{
	
	protected String langCode = "";//当前请求的接口使用的语言
	
	@Autowired
	protected CoreBaseService coreBaseService;
	
	@Autowired
	protected MemberBaseService memberBaseService;
	
	/**
	 * 校验请求权限.如api/fundInfo/findHotFundList,则urlKey=api_fundInfo_findHotFundList
	 * @param request
	 * @param jsonObject
	 * @return 检查情况的编码
	 */
	protected String checkPower(HttpServletRequest request,JSONObject jsonObject,ResultWS result) {
		//从request获得URL KEY
		String reqURI = request.getRequestURI();
		String ctxPath = request.getContextPath();
		String urlKey = reqURI;	//1.获得urlKey
		if(!"/".equals(ctxPath)){
			urlKey = urlKey.replace(ctxPath,"");
		}
		int lastIndex = urlKey.lastIndexOf(".");
		if(lastIndex > -1){
			urlKey = urlKey.substring(0,lastIndex);
		}
		if(urlKey.startsWith("/"))urlKey = urlKey.substring(1,urlKey.length());
		urlKey = urlKey.replace("/","_");
		String appCode = jsonObject.get("_APP_CODE_") == null ? "" : jsonObject.getString("_APP_CODE_");
		result.setApiCode(urlKey);
		if(PropertyUtils.getApiURLPropertyValue(urlKey, appCode)){
			//1.获得参数值
			String ts = jsonObject.get("_ts_") == null ? "" : jsonObject.getString("_ts_");//时间戳
			String tokenId = jsonObject.get("_tokenId_") == null ? "" : jsonObject.getString("_tokenId_");//令牌
			String signature = jsonObject.get("_signature_") == null ? "" : jsonObject.getString("_signature_");//签名
			if("".equals(ts) || "".equals(tokenId) || "".equals(signature)){
				return WSConstants.CODE1002;
			}
			//2.校验签名
			String secureKey = this.md5Encoder(tokenId+CommonConstants.DB_DATA_SECRET_KEY,"utf-8");
			ArrayList<String> list=new ArrayList<String>();
	        list.add(tokenId);
	        list.add(ts);
	        list.add(secureKey);
	        Collections.sort(list);
	        String signatureTmp = this.md5Encoder(list.get(0)+list.get(1)+list.get(2),"utf-8");
			if(!signature.equals(signatureTmp)){
				return WSConstants.CODE1102;
			}
			
			//3.校验token
			String fromType = jsonObject.get("_FROM_TYPE_") == null ? "web" : jsonObject.getString("_FROM_TYPE_");
			String ssoType = memberBaseService.saveCheckSSOToken(tokenId,fromType);
			if("0".equals(ssoType)){
				return WSConstants.CODE1002;
			}else if("2".equals(ssoType)){
				return WSConstants.CODE1100;
			}else if("3".equals(ssoType)){
				return WSConstants.CODE1101;
			}
			return WSConstants.SUCCESS;
		}else{
			return WSConstants.CODE1101;
		}
	}
	
	
	private  String md5Encoder(String s, String charset) {
        try {
            byte[] btInput = s.getBytes(charset);
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < md.length; i++) {
                int val = ((int) md[i]) & 0xff;
                if (val < 16){
                	sb.append("0");
                }
                sb.append(Integer.toHexString(val));
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }
	
	/**
	 * 转换成json字符串-列表
	 * @param list 列表数据
	 * @param excludes 需要排除的字段 ,格式如{"name"}
	 * @param dataFmt 日期字段的格式化
	 * @return
	 */
	protected String toJSONString(List list,String[] excludes,String dataFmt) {
		JsonUtil.init(excludes,dataFmt);
		String jsonData = JsonUtil.objectToJsonStr(list);
		return jsonData;
	}
	
	/**
	 * 转换成json字符串-对象
	 * @param list 列表数据
	 * @param excludes 需要排除的字段 ,格式如{"name"}
	 * @param dataFmt 日期字段的格式化
	 * @return
	 */
	protected String toJSONString(Object obj,String[] excludes,String dataFmt) {
		JsonUtil.init(excludes,dataFmt);
		String jsonData = JsonUtil.objectToJsonStr(obj);
		return jsonData;
	}
	
	/**
	 * 数据加密
	 * @param q 加密所需要的参数
	 * @param oriData 原始加密数据
	 * @return
	 */
	protected String dataEncrypt(JSONObject q,String oriData) {
		String encryptStr = oriData;
		String tokenId = q.get("_tokenId_") == null ? "" : q.getString("_tokenId_");
		if(!"".equals(tokenId)){
			String dataSecrefKey = DESUtils.decrypt(tokenId,CommonConstants.SECRET_KEY);
			encryptStr = DESUtils.encrypt(oriData,dataSecrefKey);
		}
		return encryptStr;
	}
	
	/**
	 * 数据解密
	 * @param q 解密所需要的参数
	 * @param oriData 原始加密数据
	 * @return
	 */
	protected String dataDecrypt(JSONObject q,String oriData) {
		String decryptStr = oriData;
		String tokenId = q.get("_tokenId_") == null ? "" : q.getString("_tokenId_");
		if(!"".equals(tokenId)){
			String dataSecrefKey = DESUtils.decrypt(tokenId,CommonConstants.SECRET_KEY);
			decryptStr = DESUtils.decrypt(oriData,dataSecrefKey);
		}
		return decryptStr;
	}
	
}
