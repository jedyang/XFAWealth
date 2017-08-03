/**
 * 
 */
package com.fsll.wmes.rpq.service;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.RpqPage;
import com.fsll.wmes.entity.RpqQuest;

/**
 * @author scshi
 *代理商RPQ问卷管理
 *@date 20170612
 */
public interface RpqForDistributorService {

	/**
	 *查询RPQ问卷
	 *@author scshi
	 *@date 20170612
	 * */
	public JsonPaging queryPageList(JsonPaging jsonPaging, String language,RpqPage info, String typeSql);

	/**
	 * 代理商题库管理
	 * @author scshi
	 * @date 20170612
	 * */
	public JsonPaging queryQuestionsList(JsonPaging jsonPaging,String language, RpqQuest info);
	
	/**
	 * 保存来自模板的问卷
	 * @author scshi
	 * @date 20170613
	 * */
	public void saveTemplateUnionDistributor(MemberDistributor memberDistributor, String tempId);

}
