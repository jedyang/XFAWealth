package com.fsll.wmes.im;

import java.util.ArrayList;
import java.util.List;

import com.fsll.common.util.PropertyUtils;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Userinfos;
import com.taobao.api.request.OpenimUsersAddRequest;
import com.taobao.api.request.OpenimUsersDeleteRequest;
import com.taobao.api.request.OpenimUsersGetRequest;
import com.taobao.api.request.OpenimUsersUpdateRequest;
import com.taobao.api.response.OpenimUsersAddResponse;
import com.taobao.api.response.OpenimUsersDeleteResponse;
import com.taobao.api.response.OpenimUsersGetResponse;
import com.taobao.api.response.OpenimUsersUpdateResponse;

public class ImTbUtil {
	
    static String url=PropertyUtils.getConfPropertyValue("im_app_url");
    static String appkey=PropertyUtils.getConfPropertyValue("im_app_key");
    static String secret=PropertyUtils.getConfPropertyValue("im_app_secret");
    static TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
    
    /**
     * 新增一条记录
     * @param userId  等于im_user_id
     * @param userPwd 等于im_user_pwd
     * @param userSex 等于sex  只能为M(男)或者F(女),可为空         
     * @param nickName 等于im_nick_name
     * @param iconUrl 为全路径,如http://....../
     * @return
     */
    public static boolean addOpenImUser(String userId,String userPwd,String userSex,String nickName,String iconUrl) {
        try{
        	Userinfos uinfos = new Userinfos();
            uinfos.setNick(nickName);
            uinfos.setIconUrl(iconUrl);
            uinfos.setEmail("");
            uinfos.setMobile("");
            uinfos.setTaobaoid("");
            uinfos.setUserid(userId);//只有Userid和Password是必须的
            uinfos.setPassword(userPwd);//必填
            uinfos.setGender(userSex);//注意：gender只能为“M”(男)或者“F”(女            
            OpenimUsersAddRequest req = new OpenimUsersAddRequest();
            List<Userinfos> listUinfos = new ArrayList<Userinfos>();
            listUinfos.add(uinfos);
            req.setUserinfos(listUinfos);
            OpenimUsersAddResponse rsp = client.execute(req);
            if (rsp.getErrorCode() == null) {
                //代表Api正确执行，可以通过UidSucc得到创建成功的Uid
                if (rsp.getUidSucc() != null) {
                	return true;
                }
            } else {
            	return false;
            }
		} catch (ApiException e) {
			e.printStackTrace();
		}
        return false;
    }
    
    /**
     * 修改一条记录
     * @param userId
     * @param userPwd
     * @param userSex
     * @param nickName
     * @param iconUrl
     * @return
     */
    public static boolean updateOpenImUser(String userId,String userPwd,String userSex,String nickName,String iconUrl) {
        try{
        	Userinfos uinfos = new Userinfos();
            uinfos.setNick(nickName);
            uinfos.setIconUrl(iconUrl);
            uinfos.setEmail("");
            uinfos.setMobile("");
            uinfos.setTaobaoid("");
            uinfos.setUserid(userId);//只有Userid和Password是必须的
            uinfos.setPassword(userPwd);//必填
            uinfos.setGender(userSex);//注意：gender只能为“M”(男)或者“F”(女            
            OpenimUsersUpdateRequest req = new OpenimUsersUpdateRequest();
            List<Userinfos> listUinfos = new ArrayList<Userinfos>();
            listUinfos.add(uinfos);
            req.setUserinfos(listUinfos);
            OpenimUsersUpdateResponse rsp = client.execute(req);
            if (rsp.getErrorCode() == null) {
                //代表Api正确执行，可以通过UidSucc得到创建成功的Uid
                if (rsp.getUidSucc() != null) {
                	return true;
                }
            } else {
            	return false;
            }
		} catch (ApiException e) {
			e.printStackTrace();
		}
        return false;
    }
    
    /**
     * 删除一条记录
     * @param userId
     * @param userPwd
     * @param userSex
     * @param nickName
     * @param iconUrl
     * @return
     */
    public static boolean deleteOpenIMUser(String userId){
    	try{
            OpenimUsersDeleteRequest req = new OpenimUsersDeleteRequest();
            req.setUserids(userId);
            OpenimUsersDeleteResponse rsp = client.execute(req);
            if (rsp.getErrorCode() == null) {
            	return true;
            } else {
            	return false;
            }
    	} catch (ApiException e) {
			e.printStackTrace();
		}
    	return false;
    }
    
    /**
     * 通过id获得记录
     * @param userIds
     * @return
     */
    public static List<Userinfos> getOpenIMUser(String userIds){
    	try{
            OpenimUsersGetRequest req = new OpenimUsersGetRequest();
            req.setUserids(userIds);
            OpenimUsersGetResponse rsp = client.execute(req);
            if (rsp.getErrorCode() == null) {
            	return rsp.getUserinfos();
            }
    	} catch (ApiException e) {
			e.printStackTrace();
		}
        return null;
    }
    
}
