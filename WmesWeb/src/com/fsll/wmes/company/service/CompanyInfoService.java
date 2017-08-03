package com.fsll.wmes.company.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.vo.MenuTreeVO;
import com.fsll.wmes.company.vo.CompanyInfoVO;
import com.fsll.wmes.entity.CompanyInfo;
import com.fsll.wmes.entity.CompanyInfoEn;
import com.fsll.wmes.entity.CompanyInfoSc;
import com.fsll.wmes.entity.CompanyInfoTc;

/**
 * 运营公司信息查询服务接口
 * @author Yan
 * @date 2016-11-29
 */
public interface CompanyInfoService {
	/**
	 * 增加或者修改一条数据
	 * @param 操作日志 
	 * @return 
	 */
	public CompanyInfo saveOrUpdate(CompanyInfo info);

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
	public CompanyInfo findById(String id);
	
	/**
	 * 通过ID查找一条包含多语言的数据
	 * @param id
	 * @return
	 */
	public CompanyInfoVO findVoById(String id);
	
	/**
	 * 通过ID查找英文信息
	 * @param id
	 * @return
	 */
	public CompanyInfoEn findEnById(String id);
	
	/**
	 * 通过ID查找简体中文信息
	 * @param id
	 * @return
	 */
	public CompanyInfoSc findScById(String id);
	
	/**
	 * 通过ID查找繁体中文信息
	 * @param id
	 * @return
	 */
	public CompanyInfoTc findTcById(String id);
	
    /**
     * 分页查询记录
     * @param jsonPaging 分页参数
     * @param CompanyInfo 查询参数
	 * @return
     */
	public JsonPaging findAll(JsonPaging jsonPaging, CompanyInfoVO infoVo, String langCode);
	
	/**
	 * 查找所有
	 * @return
	 */
	public List<CompanyInfo> findAllCompany(String langCode);
	
	/**
	 * 更新/保存方法
	 * @param info
	 * @param infooSc
	 * @param infoTc
	 * @param infoEn
	 * @param isAdd
	 * @return
	 */
	public CompanyInfo save(CompanyInfo info, CompanyInfoSc infoSc, CompanyInfoTc infoTc
			, CompanyInfoEn infoEn, boolean isAdd, List<String> isSubAdd);
	
	/**
	 * 获取权限
	 * @param id
	 * @param langCode
	 * @return
	 */
	public List<MenuTreeVO> getMenuTree(String id,String langCode);
	
	/**
	 * 菜单保存
	 * @param menuIds
	 * @param id
	 * @return
	 */
	public boolean saveMenu(String[] menuIds,String id);
	
	/**
	 * 通过code查找一条包含多语言的数据
	 * @param code
	 * @return
	 */
	public CompanyInfoVO findVoByCode(String code);
	
	/**
	 * 检查指定用户ID是否该运营公司的用户范围
	 * @param companyId
	 * @param memberId
	 * @return
	 */
	public boolean checkIfValidUser(String companyId, String memberId);
	
	/**
	 * 检查指定用户loginCode是否该运营公司的用户范围
	 * @param companyId
	 * @param loginCode
	 * @return
	 */
	public boolean checkIfValidUserByLoginCode(String companyId, String loginCode);
	
	/**
	 * 通过用户id查找一条包含多语言的数据
	 * @param code
	 * @return
	 */
	public CompanyInfoVO findByMember(String memberId);
}
