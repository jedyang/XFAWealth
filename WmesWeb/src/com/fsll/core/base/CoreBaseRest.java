/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.core.base;

import java.security.MessageDigest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.fsll.app.member.service.AppMemberBaseService;
import com.fsll.common.base.action.BaseAct;
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
	protected AppMemberBaseService memberBaseService;
	
	/**
	 * 校验请求权限
	 * @param request
	 * @return
	 */
	protected String checkPower(JSONObject jsonObject) {
//		//1.获得参数值
//		String ts = jsonObject.get("_ts_") == null ? "" : jsonObject.getString("_ts_");//时间戳
//		String tokenId = jsonObject.get("_tokenId_") == null ? "" : jsonObject.getString("_tokenId_");//令牌
//		String signature = jsonObject.get("_signature_") == null ? "" : jsonObject.getString("_signature_");//签名
//		if("".equals(ts) || "".equals(tokenId) || "".equals(signature)){
//			return WSConstants.CODE1002;
//		}
//		
//		//2.校验token
//		String fromType = "android";
//		String ssoType = memberBaseService.saveCheckSSOToken(tokenId,fromType);
//		if("0".equals(ssoType)){
//			return WSConstants.CODE1002;
//		}else if("2".equals(ssoType)){
//			return WSConstants.CODE1100;
//		}else if("3".equals(ssoType)){
//			return WSConstants.CODE1101;
//		}
//		//3.校验签名
//		String secureKey = this.md5Encoder(tokenId+CommonConstants.DB_DATA_SECRET_KEY,"utf-8");
//		ArrayList<String> list=new ArrayList<String>();
//        list.add(tokenId);
//        list.add(ts);
//        list.add(secureKey);
//        Collections.sort(list);
//        String signatureTmp = this.md5Encoder(list.get(0)+list.get(1)+list.get(2),"utf-8");
//		if(!signature.equals(signatureTmp)){
//			return WSConstants.CODE1102;
//		}
		return WSConstants.SUCCESS;
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
	
}
