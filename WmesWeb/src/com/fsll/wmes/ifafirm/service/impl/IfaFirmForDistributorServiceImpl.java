package com.fsll.wmes.ifafirm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.StrUtils;
import com.fsll.core.entity.SysCountry;
import com.fsll.core.vo.SysCountryVO;
import com.fsll.wmes.entity.IfafirmDistributor;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberIfafirmEn;
import com.fsll.wmes.entity.MemberIfafirmSc;
import com.fsll.wmes.entity.MemberIfafirmTc;
import com.fsll.wmes.ifafirm.service.IfaFirmForDistributorService;
import com.fsll.wmes.ifafirm.vo.IfaFrimDistributorVo;
import com.fsll.wmes.ifafirm.vo.IfafirmDistributorVO;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmVO;
import com.fsll.wmes.web.service.CountryService;

import freemarker.template.utility.StringUtil;

@Service("ifaFirmForDistributorService")
//@Transactional
public class IfaFirmForDistributorServiceImpl extends BaseService implements IfaFirmForDistributorService {

	@Autowired
	private CountryService countryService;
	
	/**
     * 分页查询记录
     * @param jsonpaging 分页参数
     * @param MemberCompanyIfafirmVO 查询参数
	 * @return
     */
	@Override
	public JsonPaging findAll(JsonPaging jsonpaging, IfafirmDistributorVO infoVo, String langCode) {
		String hql=" FROM IfafirmDistributor r "
			+ " LEFT JOIN " + this.getLangString("MemberIfafirm", langCode)
			+ " ll ON r.ifafirm.id=ll.id "
			+ " WHERE 1=1 ";
		List<Object> params=new ArrayList<Object>();
		if(null!=infoVo.getDistributor() && !StrUtils.isEmpty(infoVo.getDistributor().getId())){
			hql += " AND r.distributor.id=? ";
			params.add(infoVo.getDistributor().getId());
		}
		//名字查询筛选	modify by rqwang 2017-06-14
		if(!StrUtils.isEmpty(infoVo.getIfafirmName())){
			//hql += " AND ll.name LIKE ? ";
			hql += " and ll.companyName like ? ";
			
			params.add("%" + infoVo.getIfafirmName() + "%");
		}

		jsonpaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);
		
		return jsonpaging;
	}
	
	/**
     * 通过代理商分页查询顾问公司记录
     * @param distributorId 代理商id
	 * @return
     */
	public List findIfafirmByDistributor(String distributorId, String langCode) {
		String hql="SELECT r,l.companyName FROM MemberIfafirm r "
			+ " LEFT JOIN " + this.getLangString("MemberIfafirm", langCode)
			+ " l ON r.id=l.id "
			+ " WHERE 1=1 ";
		List<Object> params=new ArrayList<Object>();
		if(!StrUtils.isEmpty(distributorId)){
			hql += " AND r.id in (select d.ifafirm.id from IfafirmDistributor d where d.distributor.id=?) ";
			params.add(distributorId);
		}

		List list=this.baseDao.find(hql, params.toArray(), false);
		
		return list;
	}
	
	/**
     * 通过代理商分页查询IFA
     * @param distributorId 代理商id
	 * @return
     */
	public List findIfaByDistributor(String distributorId) {
		String hql="SELECT i FROM MemberIfa i "
			+ " LEFT JOIN MemberIfaIfafirm r"
			+ " ON i.id=r.ifa.id "
			+ " WHERE 1=1 ";
		List<Object> params=new ArrayList<Object>();
		if(!StrUtils.isEmpty(distributorId)){
			hql += " AND r.ifafirm.id in (select d.ifafirm.id from IfafirmDistributor d where d.distributor.id=?) ";
			params.add(distributorId);
		}

		List list=this.baseDao.find(hql, params.toArray(), false);
		
		return list;
	}
	
	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */	
	@Override
	public IfafirmDistributor findById(String id) {
		IfafirmDistributor info = (IfafirmDistributor) baseDao.get(IfafirmDistributor.class, id);
		return info;
	}
	
	/**
	 * 通过ID查找一条详细数据
	 * @param id
	 * @return
	 */	
	@Override
	public IfafirmDistributorVO findVoById(String id, String langCode) {
		IfafirmDistributorVO infoVo = new IfafirmDistributorVO();
		IfafirmDistributor info = (IfafirmDistributor) baseDao.get(IfafirmDistributor.class, id);
		MemberDistributor dInfo = info.getDistributor();
		MemberIfafirm mInfo = info.getIfafirm();
		infoVo.setId(id);
		infoVo.setDistributor(dInfo);
		infoVo.setIfafirm(mInfo);
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
	 * 增加或者修改一条数据
	 * @param 操作日志 
	 * @return 
	 */
	public  IfafirmDistributor saveOrUpdate(IfafirmDistributor info){
		if(null == info.getId() || "".equals(info.getId())){
			info.setId(null);
			info = (IfafirmDistributor)baseDao.create(info);
		}else{
			info = (IfafirmDistributor)baseDao.update(info);
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
			IfafirmDistributor info = findById(id);
			if(info != null){
				baseDao.delete(info);
				return true;
			}else{
				return false;
			}
		}
	}
	
	/**
	 * 获取ifa公司分页数据
	 * @author scshi_u330p
	 * @date 20170102
	 * */
	@Override
	public JsonPaging loadIfaFirmList(IfaFrimDistributorVo svo, String language, JsonPaging jsonPaging) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer(" select t.id,lang.company_name from member_ifafirm t ");
		sb.append(" LEFT JOIN member_ifafirm_"+language+" lang on t.id=lang.id ");
		//sb.append(" LEFT JOIN product_ifafirm_distributor pro on t.id=pro.ifafirm_id");
		sb.append(" where t.id in (select pro.ifafirm_id from product_ifafirm_distributor pro ");
		sb.append(" where pro.distributor_id=? and pro.ifafirm_id is not null ");
		List params = new ArrayList();
		params.add(svo.getDistributorId());
		
		String productName = svo.getProductName();
		sb.append(" and pro.product_id in (");
			sb.append(" select b.product_id as pcode from bond_info b ");
			sb.append(" LEFT JOIN bond_info_"+language+" blang on b.id=blang.id ");
			if(StringUtils.isNotBlank(productName)){
				sb.append(" where blang.bond_name like ? ");
				params.add("%"+productName+"%");
			}
			sb.append(" union select f.product_id as pcode from fund_info f ");
			sb.append(" LEFT JOIN fund_info_"+language+" flang on f.id=flang.id ");
			if(StringUtils.isNotBlank(productName)){
				sb.append(" where flang.fund_name like ? ");
				params.add("%"+productName+"%");
			}
			sb.append(" union select s.product_id as pcode from stock_info s ");
			sb.append(" LEFT JOIN stock_info_"+language+" slang on s.id=slang.id ");
			if(StringUtils.isNotBlank(productName)){
				sb.append(" where slang.stock_name like ? ");
				params.add("%"+productName+"%");
			}
		sb.append(")");
		sb.append(")");
		
		if(StringUtils.isNoneBlank(svo.getFirmName())){
			sb.append(" and lang.company_name like ? ");
			params.add("%"+svo.getFirmName()+"%");
		}
		jsonPaging = this.springJdbcQueryManager.springJdbcQueryForPaging(sb.toString(), params.toArray(), jsonPaging);
		//jsonPaging = this.baseDao.selectJsonPaging(sb.toString(), params.toArray(), jsonPaging, false);
		List list = jsonPaging.getList();
		List voList = new ArrayList();
		if(!list.isEmpty()){
			for(int y=0;y<list.size();y++){
				IfaFrimDistributorVo vo = new IfaFrimDistributorVo();
				HashMap objs = (HashMap)list.get(y);
				String firmId = StrUtils.getString(objs.get("ID"));
				String firmName = StrUtils.getString(objs.get("company_name"));
				vo.setFirmName(firmName);
				vo.setFrimId(firmId);
				vo.setDistributorId(svo.getDistributorId());
				productName = "";
				if(StringUtils.isNotBlank(firmId)){
					StringBuffer proNameList = new StringBuffer (" select x.pname from product_ifafirm_distributor pid LEFT JOIN ");
					proNameList.append(" (select blang.bond_name as pname,b.product_id as pcode from bond_info b ");
					proNameList.append(" LEFT JOIN bond_info_"+language+" blang on b.id=blang.id ");
					proNameList.append(" union select flang.fund_name as pname,f.product_id as pcode from fund_info f  ");
					proNameList.append(" LEFT JOIN fund_info_"+language+" flang on f.id=flang.id");
					proNameList.append(" union select slang.stock_name as pname,s.product_id as pcode from stock_info s ");
					proNameList.append(" LEFT JOIN stock_info_"+language+" slang on s.id=slang.id) x ");
					proNameList.append(" on pid.product_id=x.pcode ");
					proNameList.append(" where pid.ifafirm_id=? ");
					List nameList = this.springJdbcQueryManager.springJdbcQueryForList(proNameList.toString(), new String[]{firmId});
					if(!nameList.isEmpty()){
						for(int x=0;x<nameList.size();x++){
							HashMap nameObjs = (HashMap)nameList.get(x);
							if(0!=x)productName +=" - ";
							productName += StrUtils.getString(nameObjs.get("pname"));;
						}
					}
				}
				vo.setProductName(productName);
				voList.add(vo);
			}
		}
		jsonPaging.setList(voList);
		return jsonPaging;
	}
	
	
	/***
	 * 加载ifaFirm公司信息
	 * @author scshi_u330p
	 * @date 20170109
	 * */
	public MemberIfafirmVO loadIfrFirmInfo(String id,String langCode){
		MemberIfafirmVO memberIfafirmVO = new MemberIfafirmVO();
		StringBuffer sb = new StringBuffer("select t.id,t.entityType,t.registerNo,t.isFinancial,t.giin,t.incorporationDate, ");
		sb.append(" t.naturePurpose,t.incorporationPlace,t.mailingAddress,t.website,t.superCheckType,t.firmLogo,lang.companyName,t.country ");
		sb.append(" from MemberIfafirm t");
		sb.append(" LEFT JOIN "+this.getLangString("MemberIfafirm", langCode)+" lang on t.id=lang.id ");
		sb.append(" where t.id=? ");
		List list = this.baseDao.find(sb.toString(), new String[]{id}, false);
		if(!list.isEmpty()){
			
			for(int x=0;x<list.size();x++){
				Object[] objs = (Object[])list.get(x);
				memberIfafirmVO.setId(null==objs[0]?"":objs[0].toString());
				memberIfafirmVO.setEntityType(null==objs[1]?"":objs[1].toString());
				memberIfafirmVO.setRegisterNo(null==objs[2]?"":objs[2].toString());
				memberIfafirmVO.setIsFinancial(null==objs[3]?"":objs[3].toString());
				memberIfafirmVO.setGiin(null==objs[4]?"":objs[4].toString());
				memberIfafirmVO.setIncorporationDate(null==objs[5]?null:DateUtil.StringToDate(objs[5].toString(), "yyyy-MM-dd"));
				memberIfafirmVO.setNaturePurpose(null==objs[6]?"":objs[6].toString());
				if(null!=objs[7] && StringUtils.isNotBlank(objs[7].toString()) ){
					String countryId = objs[7].toString();
					SysCountry country = (SysCountry)this.baseDao.get(SysCountry.class, countryId);
					String countryName = country.getCountryName(langCode);
					memberIfafirmVO.setIncorporationPlace(countryName);
				}
				memberIfafirmVO.setMailingAddress(null==objs[8]?"":objs[8].toString());
				memberIfafirmVO.setWebsite(null==objs[9]?"":objs[9].toString());
				memberIfafirmVO.setSuperCheckType(null==objs[10]?"":objs[10].toString());
//				memberIfafirmVO.setFirmLogo(null==objs[11]?"":objs[11].toString());
				if(null!=objs[11]){
					String frimLog = PageHelper.getLogoUrl(objs[11].toString(),"F");
					memberIfafirmVO.setFirmLogo(frimLog);
				}
				memberIfafirmVO.setCompanyName(null==objs[12]?"":objs[12].toString());
				if(null!=objs[13] && StringUtils.isNotBlank(objs[13].toString()) ){
					String countryId = objs[13].toString();
					SysCountry country = (SysCountry)this.baseDao.get(SysCountry.class, countryId);
					String countryName = country.getCountryName(langCode);
					memberIfafirmVO.setCountry(countryName);
				}
			}
		}
		return memberIfafirmVO;
	}
	
	/**
	 * 获取ifafirm公司产品分页信息
	 * @author scshi_u330p
	 * @date 20170109
	 * */
	public JsonPaging loadFirmProductJson(String firmId,String langCode,JsonPaging jsonPaging){
		
		StringBuffer proNameList = new StringBuffer (" select x.pname,pid.create_time from product_ifafirm_distributor pid LEFT JOIN ");
		proNameList.append(" (select blang.bond_name as pname,b.product_id as pcode from bond_info b ");
		proNameList.append(" LEFT JOIN bond_info_"+langCode+" blang on b.id=blang.id ");
		proNameList.append(" union select flang.fund_name as pname,f.product_id as pcode from fund_info f  ");
		proNameList.append(" LEFT JOIN fund_info_"+langCode+" flang on f.id=flang.id");
		proNameList.append(" union select slang.stock_name as pname,s.product_id as pcode from stock_info s ");
		proNameList.append(" LEFT JOIN stock_info_"+langCode+" slang on s.id=slang.id) x ");
		proNameList.append(" on pid.product_id=x.pcode ");
		proNameList.append(" where pid.ifafirm_id=? ");
		jsonPaging = this.springJdbcQueryManager.springJdbcQueryForPaging(proNameList.toString(), new String[]{firmId}, jsonPaging);
		List list = jsonPaging.getList();
		List<IfaFrimDistributorVo> voList = new ArrayList();
		if(!list.isEmpty()){
			for(int x=0;x<list.size();x++){
				IfaFrimDistributorVo vo = new IfaFrimDistributorVo();
				HashMap nameObjs = (HashMap)list.get(x);
				String pName = StrUtils.getString(nameObjs.get("pname"));
				String pDate = StrUtils.getString(nameObjs.get("create_time"));
				vo.setProductName(pName);
				vo.setProductTime(pDate);
				voList.add(vo);
			}
			jsonPaging.setList(voList);
		}
		return jsonPaging;
	}

}
