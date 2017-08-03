/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.base;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fsll.common.CommonConstants;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;

/**
 * 控制器基类：核对模块
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-2-19
 */
public abstract class WmesBaseAct extends CoreBaseAct{

    
    public String toUTF8String(String val){
    	try {
        	if (null!=val && !"".equals(val)){
        		val = URLDecoder.decode(val,"UTF-8");
        		//val = new String(val.getBytes("ISO-8859-1"),"UTF-8");
        		return val;
        	}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
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
    		name=individual.getLastName() +" "+individual.getFirstName();
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
    		name=ifa.getLastName() +" "+ifa.getFirstName();
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
    	MemberBase memberBase = (MemberBase)this.coreBaseService.get(MemberBase.class, memberId);
    	if(type != null && "1".equals(type)){//返回呢称
    		returnName = memberBase.getNickName();
    	}else if(type != null && "2".equals(type)){//返回真名
        	if(memberBase.getSubMemberType() != null && 11==memberBase.getSubMemberType()){//当为investor 个人时
    			String hqlTmp = "from MemberIndividual r where r.member.id = ? ";
    			List paramsTmp = new ArrayList();
    			paramsTmp.add(memberBase.getId());
    			List<MemberIndividual> listTmp = this.coreBaseService.findHql(hqlTmp, paramsTmp.toArray());
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
    			List<MemberIfa> listTmp = this.coreBaseService.findHql(hqlTmp, paramsTmp.toArray());
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
    			List<MemberIndividual> listTmp = this.coreBaseService.findHql(hqlTmp, paramsTmp.toArray());
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
    			List<MemberIfa> listTmp = this.coreBaseService.findHql(hqlTmp, paramsTmp.toArray());
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
		List<MemberIfa> listTmp = this.coreBaseService.findHql(hqlTmp, paramsTmp.toArray());
		if(null!=listTmp && !listTmp.isEmpty()){
			MemberIfa ifa = listTmp.get(0);
			int total=null!=ifa.getPopularityTotal()?ifa.getPopularityTotal():0;
			ifa.setPopularityTotal(total+value);
			this.coreBaseService.update(ifa);
		}
			
    }
}
