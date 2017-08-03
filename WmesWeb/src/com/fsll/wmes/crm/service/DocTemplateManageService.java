/**
 * 
 */
package com.fsll.wmes.crm.service;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.crm.vo.DocInfoVo;
import com.fsll.wmes.crm.vo.DocTemplateVo;
import com.fsll.wmes.entity.DocTemplate;
import com.fsll.wmes.entity.MemberAdmin;

/*****
 * 文档审查模板管理接口
 * @author scshi_u330p
 * @date 20161213
 */
public interface DocTemplateManageService {
	
	/**
	 * 文档审查模板列表数据
	 * @author scshi_u330p
	 * @date 20161215
	 * */
	public JsonPaging findPageAll(JsonPaging jsonPaging, MemberAdmin admin,
			String language, String docTemplateTitle, String docTemplateStatus);
	
	/**
	 * 文档模板新增/查看/编辑表单
	 * @author scshi_u330p
	 * @date 20161216
	 * */
	public DocTemplateVo loadeDocTemplateDetail(String id, String language);
	
	/**
	 * 验证模板编号是否被使用
	 * @author scshi_u330p
	 * @date 20161219
	 * */
	public String checkCodeUnique(String code,String tempId);
	
	/**
	 * 文档模板信息保存
	 * @author scshi_u330p
	 * @date 200161219
	 * */
	public DocTemplate saveDocTempInfo(DocTemplateVo vo, MemberAdmin admin);
	
	/**
	 * 文档列表json
	 * @author scshi_u330p
	 * @date20161216
	 * */
	public JsonPaging loadDocListPageJson(String tempId, String keyword,String langCode,JsonPaging jsonPaging);
	
	/**
	 * 文档新增/新增/查看/编辑
	 * @author scshi_u330p
	 * @date 20161216
	 * */
	public DocInfoVo loadDocInfo(String docId, String language);
	
	/**
	 * 文档列表信息保存
	 * @author scshi_u330p
	 * @date 20161219
	 * */
	public void saveDocListInfo(DocInfoVo vo);
	
	/***
	 * 文档删除
	 * @author scshi_u330p
	 * @date 20161226
	 * */
	public void kycDocInfoDelete(String docId);
	
	/**
	 * 文档模板删除
	 * @author scshi_u330p
	 * @date 20161227
	 * */
	public void kycDocTempDelete(String tempId);

}
