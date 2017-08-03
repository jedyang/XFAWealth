/**
 * 
 */
package com.fsll.app.fund.service;


import com.fsll.app.fund.vo.AppFundAnncInfoDataVO;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.FundAnno;

/**
 * @author scshi
 * 基金公告列表数据接口
 *
 */
public interface AppFundAnnoService {
	
	/**3.1.11	获取基金公告
	 * @author scshi
	 * @param queryAnno 封装查询条件
	 * @param jsonPaging 分页信息
	 * @param language 语言编码
	 * @return	JsonPaging	基金公告列表数据
	 */
	
	public JsonPaging fundAnncList(JsonPaging jsonPaging,FundAnno queryAnno, String language);
	
	/**3.1.12	获取基金公告详细信息
	 * @author scshi
	 * @param anncId 公共ID
	 * @return	AppFundAnncInfoDataVO	公共实体
	 */
	public AppFundAnncInfoDataVO fundAnncInfo(String anncId);
	
	

}
