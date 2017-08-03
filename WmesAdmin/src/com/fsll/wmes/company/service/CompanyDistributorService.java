package com.fsll.wmes.company.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.company.vo.CompanyDistributorVO;
import com.fsll.wmes.company.vo.MemberCompanyIfafirmVO;
import com.fsll.wmes.entity.CompanyDistributor;
import com.fsll.wmes.entity.MemberBase;

/**
 * 运营与代理商关系管理
 * @author Yan
 * @date 2017-01-20
 */
public interface CompanyDistributorService {

	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	public CompanyDistributor findById(String id);
	
	/**
	 * 通过ID查找一条详细数据
	 * @param id
	 * @return
	 */
	public CompanyDistributorVO findVoById(String id, String langCode);
	
	/**
	 * 查询列表
	 * @param companyId 企业ID
	 * @param langCode
	 * @return
	 */
	public List<CompanyDistributorVO> findList(String companyId, String langCode);
	
	/**
     * 分页查询记录
     * @param jsonPaging 分页参数
     * @param infoVo 查询参数
	 * @return
     */
	public JsonPaging findAll(JsonPaging jsonPaging, CompanyDistributorVO infoVo, String langCode);
	
	/**
	 * 查找distributor的member
	 * @param distributorId
	 * @return
	 */
	public List<MemberBase> findMemberListByDistributorId(String distributorId);
	
	/**
	 * 更新/保存方法
	 * @param info
	 * @param isAdd
	 * @return
	 */
	public CompanyDistributor save(CompanyDistributor info, boolean isAdd);
	
	/**
	 * 通过ID删除一条记录
	 * @param id
	 * @return
	 */
	public boolean deleteById(String id);
}
