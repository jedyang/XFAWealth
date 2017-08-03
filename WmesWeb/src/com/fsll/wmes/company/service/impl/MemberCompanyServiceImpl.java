package com.fsll.wmes.company.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.vo.MenuTreeVO;
import com.fsll.wmes.company.service.MemberCompanyService;
import com.fsll.wmes.company.service.MemberCompanyService;
import com.fsll.wmes.company.vo.MemberCompanyVO;
import com.fsll.wmes.entity.CompanyInfo;
import com.fsll.wmes.entity.CompanyInfoEn;
import com.fsll.wmes.entity.CompanyInfoSc;
import com.fsll.wmes.entity.CompanyInfoTc;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberCompany;
import com.fsll.wmes.entity.MemberCompanyMenu;
import com.fsll.wmes.entity.MemberMenu;

/***
 * 业务接口实现类：公司用户管理
 * @author Yan
 * @date 2016-12-05
 */
@Service("memberCompanyService")
//@Transactional
public class MemberCompanyServiceImpl extends BaseService implements MemberCompanyService {
	
	/**
	 * 增加或者修改一条数据
	 * @param 操作日志 
	 * @return 
	 */
	public  MemberCompany saveOrUpdate(MemberCompany info){
		if(null == info.getId() || "".equals(info.getId())){
			info.setId(null);
			info = (MemberCompany)baseDao.create(info);
		}else{
			info = (MemberCompany)baseDao.update(info);
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
			MemberCompany info = findById(id);
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
	public MemberCompany findById(String id) {
		MemberCompany info = (MemberCompany) baseDao.get(MemberCompany.class, id);
		return info;
	}
	
	/**
	 * 通过MemberID查找一条详细数据
	 * @param id
	 * @return
	 */
	@Override
	public List<MemberCompany> findByMemberCompany(MemberCompany info) {
		String hql = " FROM MemberCompany mc WHERE 1=1 ";
		List<Object> params = new ArrayList<Object>();
		if(null!=(info.getMember())){
			hql += " AND mc.member.id=? ";
			params.add(info.getMember().getId());
		}
		if(null!=(info.getCompany())){
			hql += " AND mc.company.id=? ";
			params.add(info.getCompany().getId());
		}
		List<MemberCompany> obj = (List<MemberCompany>)this.baseDao.find(hql, params.toArray(), false);
		return obj;
	}

	/**
	 * 通过ID查找一条详细数据
	 * @param id
	 * @return
	 */	
	@Override
	public MemberCompanyVO findVoById(String id, String langCode) {
		MemberCompanyVO infoVo = new MemberCompanyVO();
		MemberCompany info = (MemberCompany) baseDao.get(MemberCompany.class, id);
		CompanyInfo cInfo = info.getCompany();
		MemberBase mInfo = info.getMember();
		infoVo.setId(id);
		infoVo.setCompany(cInfo);
		infoVo.setMember(mInfo);
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

    /***
     * 分页查询记录
     * @param jsonpaging 分页参数
     * @param MemberCompany 查询参数
	 * @return
     */
	@Override
	//@Transactional(readOnly=true)
	public JsonPaging findAll(JsonPaging jsonpaging, MemberCompanyVO infoVo, String langCode) {
		String hql=" FROM MemberCompany r "
			+ " INNER JOIN " + this.getLangString("CompanyInfo", langCode)
			+ " l ON r.company.id=l.id "
			+ " WHERE 1=1 ";
		List<Object> params=new ArrayList<Object>();
		//名字查询筛选
		if(!"".equals(infoVo.getCompanyName()) && null!=infoVo.getCompanyName()){
			hql += " AND l.name LIKE ? ";
			params.add("%" + infoVo.getCompanyName() + "%");
		}

		jsonpaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);
		
		return jsonpaging;
	}

	/**
     * 更新/保存方法
	 * @param info
	 * @param isAdd
	 * @return
     */
	@Override
	public MemberCompany save(MemberCompany info, boolean isAdd) {
		MemberCompany MemberCompany = (MemberCompany)this.baseDao.saveOrUpdate(info,isAdd);		
		return MemberCompany;
	}

}
