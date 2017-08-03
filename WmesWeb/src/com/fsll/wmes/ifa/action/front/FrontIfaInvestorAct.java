package com.fsll.wmes.ifa.action.front;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.ResponseUtils;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberInviteCode;
import com.fsll.wmes.entity.WebEmailLog;
import com.fsll.wmes.investor.service.InvestorService;
import com.fsll.wmes.member.service.MemberInviteCodeService;
import com.fsll.wmes.web.service.EmailLogService;

/**
 * @author Yan
 * @date 2017-01-07 
 */
@Controller
@RequestMapping("/front/ifa/investor")
public class FrontIfaInvestorAct extends WmesBaseAct {
	
	@Autowired
	private MemberInviteCodeService memberInviteCodeService;
	@Autowired
	private EmailLogService emailLogService;
	
	
	/**
	 * 跳转ifa对应investor页面
	 */
	@RequestMapping(value = "/investorList", method = RequestMethod.GET)
	public String investorList(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		if(null!=loginUser){
			return "ifa/investor/investorList";
		}else{
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
	}
	
	/**
	 * 分页获得记录，供前台调用
	 */
	@RequestMapping(value = "/getListJson", method = RequestMethod.POST)
	public void getListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		String keyword = request.getParameter("keyword");
		if(null!=loginUser){
			int memberType = loginUser.getMemberType();
			//IFA
			if(memberType==2){
//				String memberId = loginUser.getId();
//				jsonPaging = investorAccountService.findInvestorListForIfa(jsonPaging, memberId);
				MemberInviteCode code = new MemberInviteCode();
				code.setMember(loginUser);
				if(StringUtils.isNotBlank(keyword)){
					code.setToEmail(keyword);
					code.setInviteCode(keyword);
				}
				jsonPaging = this.getJsonPaging(request);
				jsonPaging = memberInviteCodeService.findAll(jsonPaging, code);
				/*List<Object> list = jsonPaging.getList();
				if(!list.isEmpty()){
					for (int i = 0; i < list.size(); i++) {
						MemberInviteCode info = (MemberInviteCode)list.get(i);
						Date create = info.getCreateTime();
					}
				}
				jsonPaging.setList(list);*/
				model.put("jsonPaging", jsonPaging);
				this.toJsonString(response, jsonPaging);
				//ResponseUtils.renderHtml(response,JsonUtil.toJson(jsonPaging));
			}	
		}
	}
	
	/**
	 * 生成随机数字和字母
	 * @param length
	 * @return
	 */
    public String getStringRandom(int length) {
        String val = "";  
        Random random = new Random();  
        //参数length，表示生成几位随机数  
        for(int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
            //输出字母还是数字  
            if( "char".equalsIgnoreCase(charOrNum) ) {  
                //输出是大写字母还是小写字母  
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
                val += (char)(random.nextInt(26) + temp);  
            } else if( "num".equalsIgnoreCase(charOrNum) ) {  
                val += String.valueOf(random.nextInt(10));  
            }  
        }  
        return val;  
    }
    
	/**
	 * 发送邀请码
	 */
	@RequestMapping(value = "/sendInviteCode")
	public void sendInviteCode(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode = this.getLoginLangFlag(request);
		MemberBase base = this.getLoginUser(request);
		if(null != base && null != base.getMemberType() && 2 == base.getMemberType()){//IFA
			String email = request.getParameter("email");
			String inviteCode = this.getStringRandom(8);
			Date create = new Date();
			Calendar calendar = new GregorianCalendar(); 
		    calendar.setTime(create); 
		    calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
		    Date expire = calendar.getTime();   //这个时间就是日期往后推一天的结果 
			if(StringUtils.isNotBlank(email)){
				MemberInviteCode code = new MemberInviteCode();
				code.setMember(base);
				code.setToEmail(email);
				code.setInviteCode(inviteCode);
				code.setCreateTime(create);
				code.setExpireTime(expire);
				code.setLastUpdate(create);
				code.setIsValid("1");
				try {
					//获取发送信息多语言
					String key = "member.invitecode.msg";
					String[] vals = new String[]{email,inviteCode};
					String message = PropertyUtils.getPropertyValue(langCode, key, vals);
					String title = "Send invite code";
					//发送邮件和写邮件日志
					WebEmailLog emailLog =sendEmail("inviteCode", base, null, email, title, message, code.getId());
					if(null!=emailLog){
						MemberInviteCode invite = memberInviteCodeService.saveOrUpdate(code);
						if(null != invite){
							Map<String, Object> obj = new HashMap<String, Object>();
							obj.put("result",true);
							obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
							ResponseUtils.renderHtml(response,JsonUtil.toJson(obj));
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
	}
	
	//生成随机码
	public static String getRandomString(int length) { //length表示生成字符串的长度  
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < length; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }     
	    return sb.toString();     
	 }  
}
