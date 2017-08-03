package com.fsll.wmes.company.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.company.vo.MemberCompanyIfafirmVO;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberCompanyIfafirm;

/**
 * 企业与ifafirm管理查询服务接口
 * @author Yan
 * @date 2016-12-06
 */
public interface MemberCompanyIfafirmService {
	/**
	 * 增加或者修改一条数据
	 * @param 操作日志 
	 * @return 
	 */
	public MemberCompanyIfafirm saveOrUpdate(MemberCompanyIfafirm info);

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
	public MemberCompanyIfafirm findById(String id);
	
	/**
	 * 通过ID查找一条详细数据
	 * @param id
	 * @return
	 */
	public MemberCompanyIfafirmVO findVoById(String id, String langCode);
	
	/**
	 * 查询列表
	 * @param companyId 企业ID
	 * @param langCode
	 * @return
	 */
	public List<MemberCompanyIfafirmVO> findList(String companyId, String langCode);
	
    /**
     * 分页查询记录
     * @param jsonPaging 分页参数
     * @param CompanyInfo 查询参数
	 * @return
     */
	public JsonPaging findAll(JsonPaging jsonPaging, MemberCompanyIfafirmVO infoVo, String langCode);
	
	/**
	 * 查找Ifafirm的member
	 * @param ifafirmId
	 * @return
	 */
	public List<MemberBase> findMemberListByIfafirmId(String ifafirmId);
	
	/**
	 * 通过IfafirmId查找实体
	 * @param ifafirmId
	 * @return
	 */
	public List<MemberCompanyIfafirm> findCompanyIfafirmByIfafirmId(String ifafirmId);
	
	/**
	 * 更新/保存方法
	 * @param info
	 * @param infooSc
	 * @param infoTc
	 * @param infoEn
	 * @param isAdd
	 * @return
	 */
	public MemberCompanyIfafirm save(MemberCompanyIfafirm info, boolean isAdd);
}
