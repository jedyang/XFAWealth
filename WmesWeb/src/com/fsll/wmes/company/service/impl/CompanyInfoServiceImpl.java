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
import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.vo.MenuTreeVO;
import com.fsll.wmes.company.service.CompanyInfoService;
import com.fsll.wmes.company.vo.CompanyInfoVO;
import com.fsll.wmes.entity.CompanyInfo;
import com.fsll.wmes.entity.CompanyInfoEn;
import com.fsll.wmes.entity.CompanyInfoSc;
import com.fsll.wmes.entity.CompanyInfoTc;
import com.fsll.wmes.entity.MemberCompany;
import com.fsll.wmes.entity.MemberCompanyMenu;
import com.fsll.wmes.entity.MemberMenu;

/***
 * 业务接口实现类：基金公司信息管理
 * @author Yan
 * @date 2016-11-16
 */
@Service("companyInfoService")
////@Transactional
public class CompanyInfoServiceImpl extends BaseService implements CompanyInfoService {
	
	/**
	 * 增加或者修改一条数据
	 * @param 操作日志 
	 * @return 
	 */
	public  CompanyInfo saveOrUpdate(CompanyInfo info){
		//boolean isAdd = false;
		if(null == info.getId() || "".equals(info.getId())){
			info.setId(null);
			info = (CompanyInfo)baseDao.create(info);
			//isAdd = true;
		}else{
			info = (CompanyInfo)baseDao.update(info);
			//isAdd = false;
		}
		//if(isAdd == false){
		//	throw new RuntimeException("主动抛出异常");
		//}
		return info;
	}
	
	/**
	 * 删除其他关联记录
	 * @param id
	 */
	private void deleteRelate(String id){
		CompanyInfoEn fundHouseEn = findEnById(id);
		if(fundHouseEn != null){
			baseDao.delete(fundHouseEn);
		}
		CompanyInfoSc fundHouseSc = findScById(id);
		if(fundHouseSc != null){
			baseDao.delete(fundHouseSc);
		}
		CompanyInfoTc fundHouseTc = findTcById(id);
		if(fundHouseTc != null){
			baseDao.delete(fundHouseTc);
		}
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
			CompanyInfo info = findById(id);
			if(info != null){
				deleteRelate(id);
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
	public CompanyInfo findById(String id) {
		CompanyInfo info = (CompanyInfo) baseDao.get(CompanyInfo.class, id);
		return info;
	}
	
	/**
	 * 通过ID查找一条包含多语言的数据
	 * @param id
	 * @return
	 */	
	@Override
	public CompanyInfoVO findVoById(String id) {
		CompanyInfoVO infoVO = new CompanyInfoVO();
		CompanyInfo info = (CompanyInfo) baseDao.get(CompanyInfo.class, id);
		CompanyInfoEn infoEn = findEnById(id);
		CompanyInfoSc infoSc = findScById(id);
		CompanyInfoTc infoTc = findTcById(id);
		infoVO.setId(id);
		infoVO.setCode(info.getCode());
		infoVO.setWebUrl(info.getWebUrl());
		infoVO.setLogoUrl(info.getLogoUrl());
		infoVO.setOrderBy(info.getOrderBy());
		infoVO.setIsValid(info.getIsValid());
		infoVO.setCompanyInfoEn(infoEn);
		infoVO.setCompanyInfoSc(infoSc);
		infoVO.setCompanyInfoTc(infoTc);
		return infoVO;
	}

	/**
	 * 通过ID查找英文信息
	 * @param id
	 * @return
	 */
	@Override
	public CompanyInfoEn findEnById(String id) {
		CompanyInfoEn companyInfoEn = (CompanyInfoEn) baseDao.get(CompanyInfoEn.class, id);
		return companyInfoEn;
	}

	/**
	 * 通过ID查找简体中文信息
	 * @param id
	 * @return
	 */
	@Override
	public CompanyInfoSc findScById(String id) {
		CompanyInfoSc companyInfoSc = (CompanyInfoSc) baseDao.get(CompanyInfoSc.class, id);
		return companyInfoSc;
	}

	/**
	 * 通过ID查找繁体中文信息
	 * @param id
	 * @return
	 */
	@Override
	public CompanyInfoTc findTcById(String id) {
		CompanyInfoTc companyInfoTc = (CompanyInfoTc) baseDao.get(CompanyInfoTc.class, id);
		return companyInfoTc;
	}
	
    /***
     * 分页查询记录
     * @param jsonpaging 分页参数
     * @param companyInfo 查询参数
	 * @return
     */
	@Override
	//@Transactional(readOnly=true)
	public JsonPaging findAll(JsonPaging jsonpaging, CompanyInfoVO infoVo, String langCode) {
		String hql=" FROM CompanyInfo r "
			+ " INNER JOIN " + this.getLangString("CompanyInfo", langCode)
			+ " f ON r.id=f.id "
			+ " WHERE 1=1 ";
		List<Object> params=new ArrayList<Object>();
		if(!"".equals(infoVo) && null!=infoVo){
			CompanyInfoSc infoSc = infoVo.getCompanyInfoSc();
			CompanyInfoTc infoTc = infoVo.getCompanyInfoTc();
			CompanyInfoEn infoEn = infoVo.getCompanyInfoEn();
			//名字查询筛选
			if("sc".equals(langCode)&&!"".equals(infoSc) && null!=infoSc){
				hql += " AND f.name LIKE ? ";
				if(!("").equals(infoSc.getName()) && infoSc.getName()!=null){
					params.add("%" + infoSc.getName() + "%");
				}
			}
			if("tc".equals(langCode)&&!"".equals(infoTc)&&null!=infoTc){
				hql += " AND f.name LIKE ? ";
				if(!("").equals(infoTc.getName()) && infoTc.getName()!=null){				
					params.add("%" + infoTc.getName() + "%");
				}
			}
			if("en".equals(langCode)&&!"".equals(infoEn)&&null!=infoEn){
				hql += " AND f.name LIKE ? ";
				if(!("").equals(infoEn.getName()) && infoEn.getName()!=null){				
					params.add("%" + infoEn.getName() + "%");
				}
			}
			//编号查询筛选
			if(!"".equals(infoVo.getCode())){
				hql += " AND r.code LIKE ? ";
				params.add("%" + infoVo.getCode() + "%");
			}
		}

		jsonpaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);
		
		return jsonpaging;
	}
	
	/***
	 * 查询所有
	 * @return list
	 */
	//@Transactional(readOnly=true)
	@Override
	public List<CompanyInfo> findAllCompany(String langCode){
		String hql="FROM CompanyInfo r ";
			hql += " INNER JOIN " + this.getLangString("CompanyInfo",langCode);
			hql += " l ON r.id=l.id";
		List<CompanyInfo> list=this.baseDao.find(hql, null, false);
		return list;
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
	public CompanyInfo save(CompanyInfo info, CompanyInfoSc infoSc, CompanyInfoTc infoTc
			, CompanyInfoEn infoEn, boolean isAdd, List<String> isSubAdd) {
		
		CompanyInfo companyInfo = (CompanyInfo)this.baseDao.saveOrUpdate(info,isAdd);		
		if(isAdd){
			infoSc.setId(info.getId());
			this.baseDao.create(infoSc);
			infoTc.setId(info.getId());
			this.baseDao.create(infoTc);
			infoEn.setId(info.getId());
			this.baseDao.create(infoEn);
		} else {
			if(!isSubAdd.contains("sc")){
				infoSc.setId(info.getId());
				this.baseDao.saveOrUpdate(infoSc,isAdd);
			}else{
				infoSc.setId(info.getId());
				this.baseDao.create(infoSc);
			}
			if(!isSubAdd.contains("tc")){
				infoTc.setId(info.getId());
				this.baseDao.saveOrUpdate(infoTc,isAdd);
			}else{
				infoTc.setId(info.getId());
				this.baseDao.create(infoTc);
			}
			if(!isSubAdd.contains("en")){
				infoEn.setId(info.getId());
				this.baseDao.saveOrUpdate(infoEn,isAdd);
			}else{
				infoEn.setId(info.getId());
				this.baseDao.create(infoEn);
			}
		}
		return companyInfo;
	}
	
	/**
	 * 获取权限
	 * @param id
	 * @param langCode
	 * @return
	 */
	@Override
	public List<MenuTreeVO> getMenuTree(String id,String langCode) {
		//所有菜单
		String hql = " SELECT r.id,r." + this.getLangString("name", langCode) + ",r.parent.id ";
			hql += " FROM MemberMenu r ";
			hql += " WHERE 1=1 AND r.isValid='1'";
		List allMenu = this.baseDao.find(hql,null, false);
		//角色菜单
		hql = " FROM MemberCompanyMenu l ";
			hql += " WHERE 1=1 AND l.company.id=? ";
		List<String> params = new ArrayList<String>();
		params.add(id);
		List<MemberCompanyMenu> menu = this.baseDao.find(hql,params.toArray(), false);
		Map<String,String> map =new HashMap<String, String>();
		if(menu!=null && !menu.isEmpty()){
			for (MemberCompanyMenu m : menu) {
				map.put(m.getMenu().getId(), "0");
			}
		}
		//封装数据
		List<MenuTreeVO> listVo=new ArrayList<MenuTreeVO>();
		if(null!=allMenu && !allMenu.isEmpty()){
			//尝试用另一种转换方式，但查询时报错未解决
//			for (int i = 0; i < allMenu.size(); i++) {
//				Object[] objs = (Object[]) allMenu.get(i);
//				Object obj = objs[0];
//				if(obj instanceof MemberMenu){
//					MenuTreeVO vo=new MenuTreeVO();
//					vo.setId(((MemberMenu) obj).getId());
//					if("sc".equals(langCode))
//						vo.setName(((MemberMenu) obj).getNameSc());
//					if("tc".equals(langCode))
//						vo.setName(((MemberMenu) obj).getNameTc());
//					if("en".equals(langCode))
//						vo.setName(((MemberMenu) obj).getNameEn());
//					if(!"".equals(((MemberMenu) obj).getParent())&&((MemberMenu) obj).getParent()!=null)
//						vo.setpId(((MemberMenu) obj).getParent().getId());
//					if(map.containsKey(vo.getId())){
//						vo.setChecked(true);
//					}
//					listVo.add(vo);
//				}
//			}
			Iterator it = allMenu.iterator();
			while (it.hasNext()) {
				Object[] objs = (Object[]) it.next();
				MenuTreeVO vo=new MenuTreeVO();
				vo.setId(objs[0]+"");
				vo.setName(objs[1]+"");
				vo.setpId(objs[2]+"");
				if(map.containsKey(vo.getId())){
					vo.setChecked(true);
				}
				listVo.add(vo);
			}
		}
		return listVo;
	}
	
	/**
	 * 菜单保存
	 * @param menuIds
	 * @param id
	 * @return
	 */
	@Override
	public boolean saveMenu(String[] menuIds,String id) {
		//查找要设置的运营公司信息
		CompanyInfo info = findById(id);
		if(info == null){
			return false;
		}
		//删除关联的菜单
		String hql =" DELETE FROM MemberCompanyMenu r where r.company.id=? ";
		List<String> params = new ArrayList<String>();
		params.add(id);
		this.baseDao.updateHql(hql, params.toArray());
		//保存新配置的菜单
		for (String menuId : menuIds) {
			MemberCompanyMenu companyMenu = new MemberCompanyMenu();
			MemberMenu menu = new MemberMenu();
			menu.setId(menuId);
			companyMenu.setCompany(info);
			companyMenu.setMenu(menu);
			baseDao.saveOrUpdate(companyMenu, true);
		}
		return true;
	}
	
	/**
	 * 通过code查找一条包含多语言的数据
	 * @param code
	 * @return
	 */
	public CompanyInfoVO findVoByCode(String code){
		String hql=" FROM CompanyInfo r "
			+ " WHERE code='"+code+"' ";
		
		List list = this.baseDao.find(hql, null, false);
		
		CompanyInfoVO infoVO = new CompanyInfoVO();
		
		if (null!=list && !list.isEmpty()){
			CompanyInfo info = (CompanyInfo) list.get(0);
			String id = info.getId();
			
			CompanyInfoEn infoEn = findEnById(id);
			CompanyInfoSc infoSc = findScById(id);
			CompanyInfoTc infoTc = findTcById(id);
			
			BeanUtils.copyProperties(info, infoVO);
			infoVO.setCompanyInfoEn(infoEn);
			infoVO.setCompanyInfoSc(infoSc);
			infoVO.setCompanyInfoTc(infoTc);
		
		}
		
		return infoVO;
	}
	
	/**
	 * 检查指定用户ID是否该运营公司的用户范围
	 * @param companyId
	 * @param memberId
	 * @return
	 */
	public boolean checkIfValidUser(String companyId, String memberId){
		String hql=" FROM MemberCompany r "
			+ " WHERE r.company.id='"+companyId+"' and r.member.id='"+memberId+"' ";
		
		List list = this.baseDao.find(hql, null, false);
		if (null!=list && !list.isEmpty()) return true;
		return false;
	}
	
	/**
	 * 检查指定用户loginCode是否该运营公司的用户范围
	 * @param companyId
	 * @param loginCode
	 * @return
	 */
	public boolean checkIfValidUserByLoginCode(String companyId, String loginCode){
		String hql=" FROM MemberCompany r "
			+ " WHERE r.company.id='"+companyId+"' and r.member.loginCode='"+loginCode+"' ";
		
		List list = this.baseDao.find(hql, null, false);
		if (null!=list && !list.isEmpty()) return true;
		return false;
	}
	
	/**
	 * 通过用户id查找一条包含多语言的数据
	 * @param code
	 * @return
	 */
	public CompanyInfoVO findByMember(String memberId){
		CompanyInfoVO infoVO = new CompanyInfoVO();
		MemberCompany memberCompany = new MemberCompany();
		
		String hql=" FROM MemberCompany r "
			+ " WHERE r.member.id='"+memberId+"' ";
		
		List list = this.baseDao.find(hql, null, false);
		if (null!=list && !list.isEmpty()) memberCompany = (MemberCompany) list.get(0);
		
		infoVO = findVoById(memberCompany.getCompany().getId());
		return infoVO;
	}
}
