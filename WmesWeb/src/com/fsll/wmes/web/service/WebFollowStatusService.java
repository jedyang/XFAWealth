/**
 * 
 */
package com.fsll.wmes.web.service;


import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.WebFollow;

/**
 * @author scshi
 * 接口关注状态接口
 *
 */

public interface WebFollowStatusService {
	
	/**3.1.13	关注/取消
	 * @author scshi
	 * @param relateID 对应类型id
	 * @param memberID 网站会员ID
	 * @param OpType	Follow-设置关注;Delete-取消关注
	 * @param moduleType 对应模块,fund基金关注,ifa关注,article文章关注
	 */
	public WebFollow saveWebFollowStatus(String relateId,String memberId,String opType,String moduleType);
	
	/**3.1.14	获取基金关注状态
	 * @author scshi
	 * @param relateID 对应类型id
	 * @param memberID 网站会员ID
	 * @param moduleType 对应模块,fund基金关注,ifa关注,article文章关注
	 */
	public String getWebFollowStatus(String relateId,String memberId,String moduleType);
	
	/**3.1.15	我关注的基金名单列表
	 * @author scshi
	 * @param memberID 网站会员ID
	 * @lang 当前登录用户语言
	 * @param moduleType 对应模块,fund基金关注,ifa关注,article文章关注
	 */
	public JsonPaging getWebFollowList( String memberId,String moduleType,String lang ,JsonPaging jsonPaging);
	
	

}
