package com.fsll.wmes.company.service;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.company.vo.MemberCompanyIfafirmVO;
import com.fsll.wmes.company.vo.ProductCompanyVO;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.ProductCompany;

/**
 * 产品运营企业关系管理
 * @author Yan
 * @date 2016-12-07
 */
public interface ProductCompanyService {

	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	public ProductCompany findById(String id);
	
	/**
	 * 通过ID查找一条详细数据
	 * @param id
	 * @return
	 */
	public ProductCompanyVO findVoById(String id, String langCode);
	
	/**
     * 分页查询记录
     * @param jsonPaging 分页参数
     * @param infoVo 查询参数
	 * @return
     */
	public JsonPaging findAll(JsonPaging jsonPaging, ProductCompanyVO infoVo, String type, String langCode);

	/**
	 * 更新/保存方法
	 * @param info
	 * @param isAdd
	 * @return
	 */
	public ProductCompany save(ProductCompany info, boolean isAdd);
	
	/**
	 * 通过ID删除一条记录
	 * @param id
	 * @return
	 */
	public boolean deleteById(String id);
}
