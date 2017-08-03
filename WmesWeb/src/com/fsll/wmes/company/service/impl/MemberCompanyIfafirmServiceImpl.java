package com.fsll.wmes.company.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.company.service.MemberCompanyIfafirmService;
import com.fsll.wmes.company.vo.MemberCompanyIfafirmVO;
import com.fsll.wmes.entity.CompanyInfo;
import com.fsll.wmes.entity.CompanyInfoEn;
import com.fsll.wmes.entity.CompanyInfoSc;
import com.fsll.wmes.entity.CompanyInfoTc;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberCompanyIfafirm;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberIfafirmEn;
import com.fsll.wmes.entity.MemberIfafirmSc;
import com.fsll.wmes.entity.MemberIfafirmTc;

/***
 * 业务接口实现类：公司用户管理
 * @author Yan
 * @date 2016-12-05
 */
@Service("memberCompanyIfafirmService")
//@Transactional
public class MemberCompanyIfafirmServiceImpl extends BaseService implements MemberCompanyIfafirmService {
	
	/**
	 * 增加或者修改一条数据
	 * @param 操作日志 
	 * @return 
	 */
	public  MemberCompanyIfafirm saveOrUpdate(MemberCompanyIfafirm info){
		if(null == info.getId() || "".equals(info.getId())){
			info.setId(null);
			info = (MemberCompanyIfafirm)baseDao.create(info);
		}else{
			info = (MemberCompanyIfafirm)baseDao.update(info);
		}
		return info;
	}
	
	/**
	 * 通过ID删除一条记录
	 * @param id
	 * @return
	 */
	public boolean deleteById(String id){
		if (id == null) {
			return false;
		}else{
			MemberCompanyIfafirm info = findById(id);
			if(info != null){
				baseDao.delete(info);
				return true;
			}else{
				return false;
			}
		}
	}
	
	/**
	 * 删除多条数据
	 * @param ids
	 */
	public  boolean delete(String ids){
		if (!"".equals(ids)) {
			String tmpStr = ids;
			if(ids.endsWith(","))tmpStr = ids.substring(0,ids.length()-1);
			String[] arr = tmpStr.split(",");
			for (String id : arr) {
				boolean result = deleteById(id);
				if(!result){
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */	
	@Override
	public MemberCompanyIfafirm findById(String id) {
		MemberCompanyIfafirm info = (MemberCompanyIfafirm) baseDao.get(MemberCompanyIfafirm.class, id);
		return info;
	}
	
	/**
	 * 通过ID查找一条详细数据
	 * @param id
	 * @return
	 */	
	@Override
	public MemberCompanyIfafirmVO findVoById(String id, String langCode) {
		MemberCompanyIfafirmVO infoVo = new MemberCompanyIfafirmVO();
		MemberCompanyIfafirm info = (MemberCompanyIfafirm) baseDao.get(MemberCompanyIfafirm.class, id);
		CompanyInfo cInfo = info.getCompany();
		MemberIfafirm mInfo = info.getIfafirm();
		infoVo.setId(id);
		infoVo.setCompany(cInfo);
		infoVo.setIfafirm(mInfo);
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
		if("sc".equals(langCode) && !"".equals(mInfo) && null!=mInfo){
			MemberIfafirmSc infoSc = (MemberIfafirmSc)this.baseDao.get(MemberIfafirmSc.class, mInfo.getId());
			infoVo.setIfafirmName(infoSc.getCompanyName());
		}
		if("tc".equals(langCode) && !"".equals(mInfo) && null!=mInfo){
			MemberIfafirmTc infoTc = (MemberIfafirmTc)this.baseDao.get(MemberIfafirmTc.class, mInfo.getId());
			infoVo.setIfafirmName(infoTc.getCompanyName());
		}
		if("en".equals(langCode) && !"".equals(mInfo) && null!=mInfo){
			MemberIfafirmEn infoEn = (MemberIfafirmEn)this.baseDao.get(MemberIfafirmEn.class, mInfo.getId());
			infoVo.setIfafirmName(infoEn.getCompanyName());
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
	public List<MemberCompanyIfafirmVO> findList(String companyId, String langCode) {
		List<MemberCompanyIfafirmVO> listVo = new ArrayList<MemberCompanyIfafirmVO>();
		String hql=" FROM MemberCompanyIfafirm r "
			+ " LEFT JOIN " + this.getLangString("CompanyInfo", langCode)
			+ " l ON r.company.id=l.id "
			+ " LEFT JOIN " + this.getLangString("MemberIfafirm", langCode)
			+ " ll ON r.ifafirm.id=ll.id "
			+ " WHERE 1=1 ";
		List<Object> params=new ArrayList<Object>();
		//名字查询筛选
		if(StringUtils.isNotBlank(companyId)){
			hql += " AND r.company.id=? ";
			params.add(companyId);
		}
		List<Object> list = this.baseDao.find(hql, params.toArray(), false);
		if(!list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				MemberCompanyIfafirmVO infoVo = new MemberCompanyIfafirmVO();
				Object[] objRead = (Object[])list.get(i);
				//MemberCompanyIfafirm
				MemberCompanyIfafirm info = (MemberCompanyIfafirm)objRead[0];
				infoVo.setId(info.getId());
				infoVo.setCompany(info.getCompany());
				infoVo.setIfafirm(info.getIfafirm());
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
				//MemberIfafirm
				if("sc".equals(langCode) && null!=objRead[2]){
					MemberIfafirmSc infoSc = (MemberIfafirmSc)objRead[2];
					infoVo.setIfafirmName(infoSc.getCompanyName());
				}
				if("tc".equals(langCode) && null!=objRead[2]){
					MemberIfafirmTc infoTc = (MemberIfafirmTc)objRead[2];
					infoVo.setIfafirmName(infoTc.getCompanyName());
				}
				if("en".equals(langCode) && null!=objRead[2]){
					MemberIfafirmEn infoEn = (MemberIfafirmEn)objRead[2];
					infoVo.setIfafirmName(infoEn.getCompanyName());
				}
				listVo.add(infoVo);
			}
		}
		return listVo;
	}
	
    /**
     * 分页查询记录
     * @param jsonpaging 分页参数
     * @param MemberCompanyIfafirmVO 查询参数
	 * @return
     */
	@Override
	//@Transactional(readOnly=true)
	public JsonPaging findAll(JsonPaging jsonpaging, MemberCompanyIfafirmVO infoVo, String langCode) {
		String hql=" FROM MemberCompanyIfafirm r "
			+ " LEFT JOIN " + this.getLangString("CompanyInfo", langCode)
			+ " l ON r.company.id=l.id "
			+ " LEFT JOIN " + this.getLangString("MemberIfafirm", langCode)
			+ " ll ON r.ifafirm.id=ll.id "
			+ " WHERE 1=1 ";
		List<Object> params=new ArrayList<Object>();
		if(null!=infoVo.getIfafirm()&&!"".equals(infoVo.getIfafirm().getId())){
			hql += " AND r.ifafirm.id=? ";
			params.add(infoVo.getIfafirm().getId());
		}
		//名字查询筛选
		if(!"".equals(infoVo.getCompanyName()) && null!=infoVo.getCompanyName()){
			hql += " AND l.name LIKE ? ";
			params.add("%" + infoVo.getCompanyName() + "%");
		}

		jsonpaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);
		
		return jsonpaging;
	}

	/**
	 * 查找Ifafirm的member
	 * @param ifafirmId
	 * @return
	 */
	@Override
	public List<MemberBase> findMemberListByIfafirmId(String ifafirmId) {
		List<MemberBase> memberList = new ArrayList<MemberBase>();
		//IFA角色
		String hql1 = " FROM MemberIfaIfafirm i "
			+ " LEFT JOIN MemberIfa a ON i.ifa.id=a.id "
			+ " LEFT JOIN MemberBase m ON a.member.id=m.id "
			+ " WHERE m.isValid=1 ";
		List<Object> params1=new ArrayList<Object>();
		if(StringUtils.isNotBlank(ifafirmId)){
			hql1 += " AND i.ifafirm.id=? ";
			params1.add(ifafirmId);
		}
		//筛选未添加的member
		hql1 += " AND m.id not in ( SELECT mc.member.id FROM MemberCompany mc ) ";
		List<Object> list1 = this.baseDao.find(hql1, params1.toArray(), false);
		if(!list1.isEmpty()){
			for (int i = 0; i < list1.size(); i++) {
				Object[] objRead = (Object[])list1.get(i);
				MemberBase member = (MemberBase)objRead[2];
				memberList.add(member);
			}
		}
		//admin角色
		String hql2 = " FROM MemberAdmin a "
			+ " LEFT JOIN MemberBase b ON a.member.id=b.id "
			+ " WHERE b.isValid=1 ";
		List<Object> params2=new ArrayList<Object>();
		if(StringUtils.isNotBlank(ifafirmId)){
			hql2 += " AND a.ifafirm.id=? ";
			params2.add(ifafirmId);
		}
		//筛选未添加的member
		hql2 += " AND b.id not in ( SELECT mc.member.id FROM MemberCompany mc ) ";
		List<Object> list2 = this.baseDao.find(hql2, params2.toArray(), false);
		if(!list2.isEmpty()){
			for (int i = 0; i < list2.size(); i++) {
				Object[] objRead = (Object[])list2.get(i);
				MemberBase member = (MemberBase)objRead[1];
				memberList.add(member);
			}
		}
		return memberList;
	}

	/**
	 * 通过IfafirmId查找实体
	 * @param ifafirmId
	 * @return
	 */
	@Override
	public List<MemberCompanyIfafirm> findCompanyIfafirmByIfafirmId(String ifafirmId) {
		if(StringUtils.isNotBlank(ifafirmId)){
			String hql = " FROM MemberCompanyIfafirm m WHERE m.ifafirm.id=? ";
			List<MemberCompanyIfafirm> list = (List<MemberCompanyIfafirm>)this.baseDao.find(hql, new String[]{ifafirmId}, false);
			return list;
		} else {
			return null;
		}
	}
	
	/**
     * 更新/保存方法
	 * @param info
	 * @param infooSc
	 * @param infoTc
	 * @param infoEn
	 * @param isAdd
	 * @return
     */
	@Override
	public MemberCompanyIfafirm save(MemberCompanyIfafirm info, boolean isAdd) {
		info = (MemberCompanyIfafirm)this.baseDao.saveOrUpdate(info,isAdd);		
		return info;
	}

}
