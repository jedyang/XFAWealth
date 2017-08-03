package com.fsll.wmes.company.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.company.service.CompanyDistributorService;
import com.fsll.wmes.company.vo.CompanyDistributorVO;
import com.fsll.wmes.company.vo.MemberCompanyIfafirmVO;
import com.fsll.wmes.entity.CompanyInfo;
import com.fsll.wmes.entity.CompanyInfoEn;
import com.fsll.wmes.entity.CompanyInfoSc;
import com.fsll.wmes.entity.CompanyInfoTc;
import com.fsll.wmes.entity.CompanyDistributor;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberCompanyIfafirm;
import com.fsll.wmes.entity.MemberDistributor;

/**
 * 产品运营企业关系管理
 * @author Yan
 * @date 2017-01-18
 */
@Service("companyDistributorService")
//@Transactional
public class CompanyDistributorServiceImpl extends BaseService implements CompanyDistributorService {

	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	@Override
	public CompanyDistributor findById(String id) {
		CompanyDistributor info = (CompanyDistributor)this.baseDao.get(CompanyDistributor.class, id);
		return info;
	}
	
	/**
	 * 通过ID查找一条详细数据
	 * @param id
	 * @return
	 */	
	@Override
	public CompanyDistributorVO findVoById(String id, String langCode) {
		CompanyDistributorVO infoVo = new CompanyDistributorVO();
		CompanyDistributor info = (CompanyDistributor) baseDao.get(CompanyDistributor.class, id);
		CompanyInfo cInfo = info.getCompany();
		
		infoVo.setId(id);
		infoVo.setCompany(cInfo);
		infoVo.setCreateTime(info.getCreateTime());
		infoVo.setDistributor(info.getDistributor());
		
		if("sc".equals(langCode) && !"".equals(cInfo) && null!=cInfo){
			CompanyInfoSc infoSc = (CompanyInfoSc)this.baseDao.get(CompanyInfoSc.class, cInfo.getId());
			infoVo.setCompanyName(infoSc.getName());
		}
		if("tc".equals(langCode) && !"".equals(cInfo) && null!=cInfo){
			CompanyInfoTc infoTc = (CompanyInfoTc)this.baseDao.get(CompanyInfoTc.class, cInfo.getId());
			infoVo.setCompanyName(infoTc.getName());
		}
		if("en".equals(langCode) && !"".equals(cInfo) && null!=cInfo){
			CompanyInfoEn infoEn = (CompanyInfoEn)this.baseDao.get(CompanyInfoEn.class, cInfo.getId());
			infoVo.setCompanyName(infoEn.getName());
		}
		return infoVo;
	}
	
	/**
	 * 查询列表
	 * @param companyId 企业ID
	 * @param langCode
	 * @return
	 */
	@Override
	public List<CompanyDistributorVO> findList(String companyId, String langCode) {
		List<CompanyDistributorVO> listVo = new ArrayList<CompanyDistributorVO>();
		String hql = " FROM CompanyDistributor r "
			+ " LEFT JOIN " + this.getLangString("CompanyInfo", langCode) + " l ON r.company.id=l.id "
			+ " INNER JOIN MemberDistributor d ON r.distributor.id=d.id "
			+ " WHERE 1=1 ";
		List<Object> params=new ArrayList<Object>();
		if(StringUtils.isNotBlank(companyId)){
			hql += " AND r.company.id=? ";
			params.add(companyId);
		}
		List<Object> list = this.baseDao.find(hql, params.toArray(), false);
		if(!list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				CompanyDistributorVO infoVo = new CompanyDistributorVO();
				Object[] objRead = (Object[])list.get(i);
				//CompanyDistributor
				CompanyDistributor info = (CompanyDistributor)objRead[0];
				infoVo.setId(info.getId());
				infoVo.setCompany(info.getCompany());
				infoVo.setDistributor(info.getDistributor());
				//CompanyInfo
				if("sc".equals(langCode) && null!=objRead[1]){
					CompanyInfoSc infoSc = (CompanyInfoSc)objRead[1];
					infoVo.setCompanyName(infoSc.getName());
				}
				if("tc".equals(langCode) && null!=objRead[1]){
					CompanyInfoTc infoTc = (CompanyInfoTc)objRead[1];
					infoVo.setCompanyName(infoTc.getName());
				}
				if("en".equals(langCode) && null!=objRead[1]){
					CompanyInfoEn infoEn = (CompanyInfoEn)objRead[1];
					infoVo.setCompanyName(infoEn.getName());
				}
				if(null!=objRead[2]){
					MemberDistributor distributor = (MemberDistributor)objRead[2];
					infoVo.setDistributorName(distributor.getCompanyName());
				}
				listVo.add(infoVo);
			}
		}
		return listVo;
	}
	
	/**
     * 分页查询记录
     * @param jsonPaging 分页参数
     * @param infoVo 查询参数
	 * @return
     */
	@Override
	public JsonPaging findAll(JsonPaging jsonPaging,CompanyDistributorVO infoVo, String langCode) {
		String hql=" FROM CompanyDistributor r "
			+ " LEFT JOIN " + this.getLangString("CompanyInfo", langCode) + " l ON r.company.id=l.id "
			+ " INNER JOIN MemberDistributor d ON r.distributor.id=d.id "
			+ " WHERE 1=1 ";
		List<Object> params=new ArrayList<Object>();
		//名字查询筛选
		if(!"".equals(infoVo.getCompanyName()) && null!=infoVo.getCompanyName()){
			hql += " AND l.name LIKE ? ";
			params.add("%" + infoVo.getCompanyName() + "%");
		}
		if(!"".equals(infoVo.getDistributorName()) && null!=infoVo.getDistributorName()){
			hql += " AND d.companyName LIKE ? ";
			params.add("%" + infoVo.getDistributorName() + "%");
		}

		jsonPaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		
		return jsonPaging;
	}

	/**
	 * 查找distributor的member
	 * @param distributorId
	 * @return
	 */
	@Override
	public List<MemberBase> findMemberListByDistributorId(String distributorId) {
		List<MemberBase> memberList = new ArrayList<MemberBase>();
		String hql = " FROM MemberAdmin a "
			+ " LEFT JOIN MemberBase b ON a.member.id=b.id "
			+ " WHERE b.isValid=1 ";
		List<Object> params=new ArrayList<Object>();
		if(StringUtils.isNotBlank(distributorId)){
			hql += " AND a.distributor.id=? ";
			params.add(distributorId);
		}
		//筛选未添加的member
		hql += " AND b.id not in ( SELECT mc.member.id FROM MemberCompany mc ) ";
		List<Object> list = this.baseDao.find(hql, params.toArray(), false);
		if(!list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Object[] objRead = (Object[])list.get(i);
				MemberBase member = (MemberBase)objRead[1];
				memberList.add(member);
			}
		}
		return memberList;
	}
	
	/**
	 * 更新/保存方法
	 * @param info
	 * @param isAdd
	 * @return
	 */
	@Override
	public CompanyDistributor save(CompanyDistributor info, boolean isAdd){
		CompanyDistributor infoTemp = (CompanyDistributor)this.baseDao.saveOrUpdate(info,isAdd);		
		return infoTemp;
	}
	
	/**
	 * 通过ID删除一条记录
	 * @param id
	 * @return
	 */
	@Override
	public boolean deleteById(String id){
		if (id == null) {
			return false;
		}else{
			CompanyDistributor info = findById(id);
			baseDao.delete(info);
			return true;
		}
	}
}
