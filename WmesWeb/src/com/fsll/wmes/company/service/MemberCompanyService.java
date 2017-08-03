package com.fsll.wmes.company.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.company.vo.MemberCompanyVO;
import com.fsll.wmes.entity.MemberCompany;

/**
 * 公司用户管理查询服务接口
 * @author Yan
 * @date 2016-12-05
 */
public interface MemberCompanyService {
	/**
	 * 增加或者修改一条数据
	 * @param 操作日志 
	 * @return 
	 */
	public MemberCompany saveOrUpdate(MemberCompany info);

	/**
	 * 通过ID删除一条记录
	 * @param id
	 * @return
	 */
	public boolean deleteById(String id);
	
	/**
	 * 删除多条数据
	 * @param ids
	 */
	public  boolean delete(String ids);
	
	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	public MemberCompany findById(String id);

	/**
	 * 通过MemberID查找一条详细数据
	 * @param id
	 * @return
	 */
	public List<MemberCompany> findByMemberCompany(MemberCompany info);
	
	/**
	 * 通过ID查找一条详细数据
	 * @param id
	 * @return
	 */
	public MemberCompanyVO findVoById(String id, String langCode);
	
    /**
     * 分页查询记录
     * @param jsonPaging 分页参数
	 * @return
     */
	public JsonPaging findAll(JsonPaging jsonPaging, MemberCompanyVO infoVo, String langCode);
	
	/**
	 * 更新/保存方法
	 * @param info
	 * @param isAdd
	 * @return
	 */
	public MemberCompany save(MemberCompany info, boolean isAdd);
}
