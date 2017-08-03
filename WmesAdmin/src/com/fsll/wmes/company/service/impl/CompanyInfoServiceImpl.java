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
import com.fsll.common.util.StrUtils;
import com.fsll.core.vo.MenuTreeVO;
import com.fsll.core.vo.SysCountryVO;
import com.fsll.wmes.company.service.CompanyInfoService;
import com.fsll.wmes.company.vo.CompanyInfoVO;
import com.fsll.wmes.company.vo.MemberCompanyVO;
import com.fsll.wmes.entity.CompanyInfo;
import com.fsll.wmes.entity.CompanyInfoEn;
import com.fsll.wmes.entity.CompanyInfoSc;
import com.fsll.wmes.entity.CompanyInfoTc;
import com.fsll.wmes.entity.MemberCompanyMenu;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberMenu;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmVO;

/***
 * 业务接口实现类：基金公司信息管理
 * @author Yan
 * @date 2016-11-16
 */
@Service("companyInfoService")
//@Transactional
public class CompanyInfoServiceImpl extends BaseService implements CompanyInfoService {
	
	/**
	 * 增加或者修改一条数据
	 * @param 操作日志 
	 * @return 
	 */
	public  CompanyInfo saveOrUpdate(CompanyInfo info){
		if(null == info.getId() || "".equals(info.getId())){
			info.setId(null);
			info = (CompanyInfo)baseDao.create(info);
		}else{
			info = (CompanyInfo)baseDao.update(info);
		}
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
	@Override
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
		infoVO.setBackgroundUrl(info.getBackgroundUrl());
		infoVO.setCssUrl(info.getCssUrl());
		infoVO.setLoginUrl(info.getLoginUrl());
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

	/**
	 * 通过code查找一条数据
	 * @param code
	 * @return
	 */
	@Override
	public CompanyInfo findByCode(String code) {
		String hql = " FROM CompanyInfo r WHERE r.isValid=1 AND r.code=? ";
		List<Object> params=new ArrayList<Object>();
		params.add(code);
		List list = this.baseDao.find(hql, params.toArray(), false);
		if(!list.isEmpty()){
			return (CompanyInfo)list.get(0);
		}
		return null;
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
			+ " LEFT JOIN " + this.getLangString("CompanyInfo", langCode)
			+ " f ON r.id=f.id "
			+ " WHERE 1=1 ";
		List<Object> params=new ArrayList<Object>();
		if(!"".equals(infoVo) && null!=infoVo){
			//公司名称
			if(!StrUtils.isEmpty(infoVo.getName())){
				hql += " AND f.name LIKE ? ";
				params.add("%" + infoVo.getName() + "%");
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
	 * 查找所有运营公司信息VO
	 * @return
	 */
	public List<MemberCompanyVO> getCompanyList(String langCode) {
		return findCompanyList("", "", langCode);
	}
	
	/**
	 * 查找运营公司信息VO
	 * @return
	 */
	public List<MemberCompanyVO> findCompanyList(String productId, String distributorId, String langCode) {
		String hql = "SELECT c,l.name FROM CompanyInfo c "
			+ " LEFT JOIN "+this.getLangString("CompanyInfo", langCode)+" l on c.id = l.id "
			+ " WHERE c.isValid='1' ";

		List<String> params = new ArrayList<String>();
		if (!StrUtils.isEmpty(productId)){
			hql += " AND c.id in (select p.company.id from ProductCompany p where p.product.id=?) ";
			params.add(productId);
		}
		if (!StrUtils.isEmpty(distributorId)){
			hql += " AND c.id in (select d.company.id from CompanyDistributor d where d.distributor.id=?) ";
			params.add(distributorId);
		}
		List list = baseDao.find(hql, params.toArray(), false);
		List<MemberCompanyVO> listVo = new ArrayList<MemberCompanyVO>();
		if(null != list && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[])list.get(i);
				CompanyInfo company = (CompanyInfo) objs[0];
				MemberCompanyVO companyVo = new MemberCompanyVO();
				companyVo.setId(company.getId());
				companyVo.setCompany(company);
				companyVo.setCompanyName(StrUtils.getString(objs[1]));

				listVo.add(companyVo);
			}
		}
		return listVo;
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
		String hql = //" SELECT r.id,r." + this.getLangString("name", langCode) + ",r.parent.id ";
			" FROM MemberMenu r WHERE 1=1 AND r.isValid='1'";
		List allMenu = this.baseDao.find(hql,null, false);
		//角色菜单
		hql = " FROM MemberCompanyMenu l WHERE 1=1 AND l.company.id=? ";
		List<String> params = new ArrayList<String>();
		params.add(id);
		List<MemberCompanyMenu> companyMenu = this.baseDao.find(hql,params.toArray(), false);
		//获取已配置项
		Map<String,String> map =new HashMap<String, String>();
		if(companyMenu!=null && !companyMenu.isEmpty()){
			for (MemberCompanyMenu m : companyMenu) {
				map.put(m.getMenu().getId(), "0");
			}
		}
		//封装数据
		List<MenuTreeVO> listVo=new ArrayList<MenuTreeVO>();
		MenuTreeVO topMenu = new MenuTreeVO();
		MenuTreeVO qtaMenu = new MenuTreeVO();
		MenuTreeVO qtbMenu = new MenuTreeVO();
		MenuTreeVO csaMenu = new MenuTreeVO();
		MenuTreeVO csbMenu = new MenuTreeVO();
		topMenu.setName("所有菜单项");
		topMenu.setId("addTop");
		qtaMenu.setName("投资人菜单项");
		qtaMenu.setId("addInvestor");
		qtaMenu.setpId("addTop");
		qtbMenu.setName("ifa菜单项");
		qtbMenu.setId("addIfa");
		qtbMenu.setpId("addTop");
		csaMenu.setName("ifaFirm菜单项");
		csaMenu.setId("addIfaFirm");
		csaMenu.setpId("addTop");
		csbMenu.setName("代理商菜单项");
		csbMenu.setId("addDistributor");
		csbMenu.setpId("addTop");
		listVo.add(topMenu);
		listVo.add(qtaMenu);
		listVo.add(qtbMenu);
		listVo.add(csaMenu);
		listVo.add(csbMenu);
		if(null!=allMenu && !allMenu.isEmpty()){
			Iterator<MemberMenu> it = allMenu.iterator();
			while (it.hasNext()) {
				MemberMenu menu = it.next();
				MenuTreeVO vo=new MenuTreeVO();
				vo.setId(menu.getId());
				vo.setName(menu.getMenuName(langCode));
				if(menu.getParent()!=null){
					vo.setpId(menu.getParent().getId());
				}else{
					if(menu.getCode().contains("qta_")){
						vo.setpId("addInvestor");
					}else if(menu.getCode().contains("qtb_")){
						vo.setpId("addIfa");
					}else if(menu.getCode().contains("csa_")){
						vo.setpId("addIfaFirm");
					}else if(menu.getCode().contains("csb_")){
						vo.setpId("addDistributor");
					}
				}
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
			//过滤查询是添加的自定义ID项
			if(!"addTop".equals(menuId)&&!"addInvestor".equals(menuId)&&!"addIfa".equals(menuId)
					&&!"addIfaFirm".equals(menuId)&&!"addDistributor".equals(menuId)){
				MemberCompanyMenu companyMenu = new MemberCompanyMenu();
				MemberMenu menu = new MemberMenu();
				menu.setId(menuId);
				companyMenu.setCompany(info);
				companyMenu.setMenu(menu);
				baseDao.saveOrUpdate(companyMenu, true);
			}
		}
		return true;
	}
	
	/***
	 * 查询产品的运营公司
	 * @Auth Michael
	 * @date 2017-3-1
	 * @param productId
	 * @return list
	 */
	public List<CompanyInfo> findCompanyByProduct(String productId, String langCode){
		if (StrUtils.isEmpty(productId)) return null;
		String hql=" from CompanyInfo c ";
		hql += " INNER JOIN " + this.getLangString("CompanyInfo",langCode);
		hql += " l ON c.id=l.id";
		hql += " left join ProductCompany pc on c.id=pc.company.id where pc.company.id=?";
		List params=new ArrayList();
		params.add(productId);
		List<CompanyInfo> list=this.baseDao.find(hql, params.toArray(), false);

		return list;
	}
	
	/***
	 * 查询基金的运营公司
	 * @Auth Michael
	 * @date 2017-3-1
	 * @param fundId
	 * @return list
	 */
	public List<CompanyInfo> findCompanyByFund(String fundId, String langCode){
		if (StrUtils.isEmpty(fundId)) return null;
		String hql=" from CompanyInfo c ";
		hql += " INNER JOIN " + this.getLangString("CompanyInfo",langCode);
		hql += " l ON c.id=l.id";
		hql += " left join ProductCompany pc on c.id=pc.company.id ";
		hql += " where pc.product.id is not null and pc.product.id in (select f.product.id from FundInfo f where f.id=?)";
		List params=new ArrayList();
		params.add(fundId);
		List<CompanyInfo> list=this.baseDao.find(hql, params.toArray(), false);

		return list;
	}
}
