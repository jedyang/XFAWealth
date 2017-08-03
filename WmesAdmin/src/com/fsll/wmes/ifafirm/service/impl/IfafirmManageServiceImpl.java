package com.fsll.wmes.ifafirm.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.StrUtils;
import com.fsll.core.entity.SysCountry;
import com.fsll.core.vo.SysCountryVO;
import com.fsll.wmes.entity.IfafirmDistributor;
import com.fsll.wmes.entity.IfafirmIfafirm;
import com.fsll.wmes.entity.IfafirmTeam;
import com.fsll.wmes.entity.IfafirmTeamIfa;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberIfafirmEn;
import com.fsll.wmes.entity.MemberIfafirmSc;
import com.fsll.wmes.entity.MemberIfafirmTc;
import com.fsll.wmes.ifa.vo.AutoCompleteVO;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmVO;
import com.fsll.wmes.member.vo.IfaVO;
import com.fsll.wmes.web.service.CountryService;
import com.fsll.wmes.web.vo.MemberDistributorExtend;


/***
 * 业务接口实现类：IFAFirmTeam 团队接口
 * @author 林文伟
 * @date 2016-06-22
 */
@Service("ifafirmManageService")
//@Transactional
public class IfafirmManageServiceImpl extends BaseService implements IfafirmManageService {
	@Autowired
	private CountryService countryService;
	
	/**
	 * 获取ifafirm 列表数据
	 *  @author qgfeng modify by rqwang 20170615
	 * @date 2016-11-10
	 */
	@Override
	public JsonPaging getIfaFirmJson(JsonPaging jsonPaging,MemberIfafirmVO vo,String langCode) {
		StringBuffer hql = new StringBuffer(" select f,lang.companyName from MemberIfafirm f ");
		hql.append(","+this.getLangString("MemberIfafirm", langCode)+" lang where f.id = lang.id ");
		hql.append(" and f.isValid=1 ");
		List<String> params = new ArrayList<String>();
		if(StringUtils.isNotBlank(vo.getCompanyName())){
			hql.append(" and lang.companyName like ?");
			params.add("%"+vo.getCompanyName()+"%");
		}
		if(StringUtils.isNotBlank(vo.getRegisterNo())){
			hql.append(" and f.registerNo like ?");
			params.add("%"+vo.getRegisterNo()+"%");
		}
		if(StringUtils.isNotBlank(vo.getEntityType())){
			hql.append(" and f.entityType=?");
			params.add(vo.getEntityType());
		}

		jsonPaging = baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List jsonList = jsonPaging.getList();
		List<MemberIfafirmVO> list = new ArrayList<MemberIfafirmVO>();
		if(null != jsonList && !jsonList.isEmpty()){
			for(int i=0;i<jsonList.size();i++){
				Object[] objs = (Object[])jsonList.get(i);
				MemberIfafirm firm = (MemberIfafirm) objs[0];
				MemberIfafirmVO firmVo = new MemberIfafirmVO();
				firmVo.setId(firm.getId());
				firmVo.setEntityType(firm.getEntityType());
				//firmVo.setIncorporationPlace(firm.getIncorporationPlace());
				firmVo.setIncorporationDate(firm.getIncorporationDate());
				String logoUrlParse = PageHelper.getLogoUrl(firm.getFirmLogo(), "F");
				firmVo.setFirmLogo(logoUrlParse);
				firmVo.setWebsite(firm.getWebsite());
				
				if(StringUtils.isNotBlank(firm.getIncorporationPlace())){
					SysCountryVO county = countryService.findById(firm.getCountry());
					if(county != null)firmVo.setIncorporationPlace(county.getCountryName(langCode));
				}
				if(objs[1]!=null){
					firmVo.setCompanyName(objs[1].toString());
				}
				firmVo.setIsFinancial(firm.getIsFinancial());
				list.add(firmVo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	
	@Override
	public List<MemberIfafirmVO> getIfafirmlist(String langCode) {
		StringBuffer hql = new StringBuffer(" select f,lang.companyName from MemberIfafirm f ");
		hql.append(" left join "+this.getLangString("MemberIfafirm", langCode)+" lang on f.id = lang.id ");
		hql.append(" where f.isValid=1");
		List list = baseDao.find(hql.toString(), null, false);
		List<MemberIfafirmVO> listVo = new ArrayList<MemberIfafirmVO>();
		if(null != list && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[])list.get(i);
				MemberIfafirm firm = (MemberIfafirm) objs[0];
				MemberIfafirmVO firmVo = new MemberIfafirmVO();
				firmVo.setId(firm.getId());
				firmVo.setEntityType(firm.getEntityType());
				firmVo.setCountry(firm.getCountry());
				firmVo.setIncorporationDate(firm.getIncorporationDate());
				
				if(StringUtils.isNotBlank(firm.getCountry())){
					SysCountryVO county = countryService.findById(firm.getCountry());
					if(county != null)firmVo.setCountry(county.getCountryName(langCode));
				}
				if(objs[1]!=null){
					firmVo.setCompanyName(objs[1].toString());
				}
				listVo.add(firmVo);
			}
		}
		return listVo;
	}
	
	@Override
	public MemberIfafirm findById(String id,String langCode) {
		if(StringUtils.isBlank(id)){
			return null;
		}
		MemberIfafirm memberIfafirm = (MemberIfafirm) baseDao.get(MemberIfafirm.class, id);
		if(memberIfafirm != null && StringUtils.isNoneBlank(langCode)){
			String hhql = "select companyName from "+this.getLangString("MemberIfafirm", langCode);
			hhql+=" where id = '"+id+"'";
			String companyName = (String) baseDao.getUniqueResult(hhql,null, false);
			memberIfafirm.setCompanyName(companyName);
		}
		return memberIfafirm;
	}
	/**
	 * 获取ifafirm 简体中文表
	 * @author wwluo
	 * @date 2016-08-26
	 */
	@Override
	public MemberIfafirmSc findIfafirmScById(String id) {
		if(StringUtils.isBlank(id)){
			return null;
		}
		MemberIfafirmSc firmSc = (MemberIfafirmSc) baseDao.get(MemberIfafirmSc.class, id);
		return firmSc;
	}
	/**
	 * 获取ifafirm 繁体中文表
	 * @author wwluo
	 * @date 2016-08-26
	 */
	@Override
	public MemberIfafirmTc findIfafirmTcById(String id) {
		if(StringUtils.isBlank(id)){
			return null;
		}
		MemberIfafirmTc	firmTc = (MemberIfafirmTc) baseDao.get(MemberIfafirmTc.class, id);
		return firmTc;
	}
	/**
	 * 获取ifafirm 英文表
	 * @author wwluo
	 * @date 2016-08-26
	 */
	@Override
	public MemberIfafirmEn findIfafirmEnById(String id) {
		if(StringUtils.isBlank(id)){
			return null;
		}
		MemberIfafirmEn firmEn = (MemberIfafirmEn) baseDao.get(MemberIfafirmEn.class, id);
		return firmEn;
	}

	/***
     * 保存ifafirm
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public MemberIfafirm saveOrUpdate(MemberIfafirm memberIfafirm, boolean isAdd) {
		memberIfafirm=(MemberIfafirm)this.baseDao.saveOrUpdate(memberIfafirm,isAdd);
		return memberIfafirm;
	}
	
	/**
	 * 删除ifafirm（逻辑删除）
	 * @author wwluo
	 * @date 2016-08-22
	 */
	@Override
	public boolean deleteById(String ifafirmId) {
		boolean flag = false;
		MemberIfafirm ifafirm = (MemberIfafirm) this.baseDao.get(MemberIfafirm.class, ifafirmId);
		if(null != ifafirm){
			ifafirm.setIsValid("0");
			this.baseDao.update(ifafirm);
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 保存ifafirm 简体中文表信息
	 * @author wwluo
	 * @date 2016-08-26
	 */
	@Override
	public MemberIfafirmSc saveOrUpdateFirmSc(MemberIfafirmSc memberIfafirmSc) {
		MemberIfafirmSc firmSc = 
			(MemberIfafirmSc) this.baseDao.get(MemberIfafirmSc.class , memberIfafirmSc.getId());
		if(null != firmSc){
			BeanUtils.copyProperties(memberIfafirmSc, firmSc);
			memberIfafirmSc = (MemberIfafirmSc) this.baseDao.update(firmSc);
		}else{
			memberIfafirmSc = (MemberIfafirmSc) this.baseDao.create(memberIfafirmSc);
		}
		return memberIfafirmSc;
	}
	
	/**
	 * 保存ifafirm 繁体中文表信息
	 * @author wwluo
	 * @date 2016-08-26
	 */
	@Override
	public MemberIfafirmTc saveOrUpdateFirmTc(MemberIfafirmTc memberIfafirmTc) {
		MemberIfafirmTc firmTc = 
			(MemberIfafirmTc) this.baseDao.get(MemberIfafirmTc.class , memberIfafirmTc.getId());
		if(null != firmTc){
			BeanUtils.copyProperties(memberIfafirmTc, firmTc);
			memberIfafirmTc = (MemberIfafirmTc) this.baseDao.update(firmTc);
		}else{
			memberIfafirmTc = (MemberIfafirmTc) this.baseDao.create(memberIfafirmTc);
		}
		return memberIfafirmTc;
	}

	/**
	 * 保存ifafirm 英文表信息
	 * @author wwluo
	 * @date 2016-08-26
	 */
	@Override
	public MemberIfafirmEn saveOrUpdateFirmEn(MemberIfafirmEn memberIfafirmEn) {
		MemberIfafirmEn firmEn = 
			(MemberIfafirmEn) this.baseDao.get(MemberIfafirmEn.class , memberIfafirmEn.getId());
		if(null != firmEn){
			BeanUtils.copyProperties(memberIfafirmEn, firmEn);
			memberIfafirmEn = (MemberIfafirmEn) this.baseDao.update(firmEn);
		}else{
			memberIfafirmEn = (MemberIfafirmEn) this.baseDao.create(memberIfafirmEn);
		}
		return memberIfafirmEn;
	}
	
	//-----end MemberIfafirm action----
	
	/**
	 * IFAFirmTeam数据列表查询
	 * @author qgfeng
	 * @date 2016-11-15
	 */
	@Override
	//@Transactional(readOnly=true)
	public List<IfafirmTeam> findTeamAll( IfafirmTeam team) {
		//获取team中最高层级
		String  ifafirmId = team.getIfafirm().getId();
		List<IfafirmTeam> teamlist = new ArrayList<IfafirmTeam>();
		if (StrUtils.isEmpty(ifafirmId)) return teamlist;
		
		String hql=" from IfafirmTeam r  where r.ifafirm.id ='"+ifafirmId+"' and r.classLayer=1 order by r.orderBy asc";
		List<IfafirmTeam> parentList=this.baseDao.find(hql,null, false);
		if(parentList!=null && !parentList.isEmpty())
			{
				for(IfafirmTeam each : parentList)
				{
					String id = each.getId();
					hql=" from IfafirmTeam where FIND_IN_SET(id,fn_queryIfafirmTeam('"+id+"'))>0 ";
					List<IfafirmTeam> childList=this.baseDao.find(hql,null, false);
					if(childList!=null && !childList.isEmpty()){
						teamlist.addAll(childList);
					}
			}
		}
		return teamlist;
	}
	
	/***
     * 通过ID获取IFAFirmTeam
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public IfafirmTeam getIfafirmTeam(String teamId) {
		Object obj = (IfafirmTeam) baseDao.get(IfafirmTeam.class, teamId);
		if(obj!=null)
		{
			return (IfafirmTeam)obj;
		} else return null;
	}
	
	/***
     * 保存IFAFirmTeam
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public IfafirmTeam saveOrUpdateTeam(IfafirmTeam team, boolean isAdd) {
		team=(IfafirmTeam)this.baseDao.saveOrUpdate(team,isAdd);
		return team;
	}
	
	/***
     * 删除团队数据，注意同时要删除子团队跟所有团队的成员
     * @author qgfeng
	 * @date 2016-11-15
     */
	@Override
	public Boolean delIfafirmTeam(String teamId)
	{
		//add by zxtan 20161207 begin
		Boolean hasMember = isExistsTeamIfa(teamId);//是否有团队成员
		if(hasMember){
			//有团队成员，不能删除团队
			return false;
		}
		//add by zxtan 20161207 end
		//先获取所有子团队
		String hql = " FROM IfafirmTeam t WHERE t.parent.id=? ";
		List<Object> params = new ArrayList<Object>();
		params.add(teamId);
		List<IfafirmTeam> list = this.baseDao.find(hql, params.toArray(), false);
		if(!list.isEmpty())
		{
			for(IfafirmTeam each : list)
			{
				String childId = each.getId();
				//删除子团队成员
				String hql1 = " delete IfafirmTeamIfa i where  i.team.id =? ";
				List<Object> params1 = new ArrayList<Object>();
				params1.add(childId);
				this.baseDao.updateHql(hql1, params1.toArray());
				//同时删除子团队
				String hql2 = " delete IfafirmTeam where id =? ";
				List<Object> params2 = new ArrayList<Object>();
				params2.add(childId);
				this.baseDao.updateHql(hql2, params2.toArray());
			}
		}
		//删除团队本身成员
		String hql3 = " delete IfafirmTeamIfa i where  i.team.id =?  ";
		List<Object> params3 = new ArrayList<Object>();
		params3.add(teamId);
		this.baseDao.updateHql(hql3, params3.toArray());
		//删除团队本身
		String hql4 = " delete IfafirmTeam where  id =? ";
		List<Object> params4 = new ArrayList<Object>();
		params4.add(teamId);
		int rs = this.baseDao.updateHql(hql4, params4.toArray());
		
		return rs>0?true:false;
	}
	
	/**
	 * 检查是否有团队成员
	 * @author zxtan
	 * @date 20161207
	 * @param teamId
	 * @return
	 */
	private Boolean isExistsTeamIfa(String teamId){
		String hql = " from IfafirmTeamIfa i where  i.team.id =? ";
		List<Object> params = new ArrayList<Object>();
		params.add(teamId);
		List<IfafirmTeamIfa> list = this.baseDao.find(hql, params.toArray(), false);
		if(!list.isEmpty())
		{
			return true;
		} else {
			hql = " FROM IfafirmTeam t WHERE t.parent.id=? ";
			params = new ArrayList<Object>();
			params.add(teamId);
			List<IfafirmTeam> subTeamList = this.baseDao.find(hql, params.toArray(), false);
			if(!subTeamList.isEmpty())
			{
				for(IfafirmTeam each : subTeamList)
				{
					String childId = each.getId();
					//删除子团队成员
					hql = " from IfafirmTeamIfa i where  i.team.id =? ";
					params = new ArrayList<Object>();
					params.add(childId);
					List<IfafirmTeamIfa> subTeamIfaList = this.baseDao.find(hql, params.toArray(), false);
					if(!subTeamIfaList.isEmpty())
					{
						return true;
					}					
				}
			}
		}
		return false;
	}
	
	//-----end IfafirmTeam action----
	
	/***
     * IfafirmTeamIfa 启动与取消团队成员的sv操作
     * @author qgfeng
	 * @date 2016-11-15
     */
	@Override
	public void mergeSupervisor(String id)
	{
		//先获取其信息
		Object obj =  baseDao.get(IfafirmTeamIfa.class, id);
		if(obj!=null)
		{
			IfafirmTeamIfa teamIfa = (IfafirmTeamIfa)obj;
			String isSupervisor = teamIfa.getIsSupervisor();
			if("1".equals(isSupervisor)) //如果数据是启用，则取消
			{
				//取消SV
				teamIfa.setIsSupervisor("0");
			}
			else
			{
				//启用SV
				teamIfa.setIsSupervisor("1");
			}
			this.baseDao.update(teamIfa);
		} 
	}
	
	/**
	 * IfafirmTeamIfa 通过公司与团队ID获取其所有成员数据
	 * @author qgfeng
	 * @date 2017-1-12
	 */
	@Override
	//@Transactional
	public JsonPaging findAllTeamIfa(JsonPaging jsonpaging, String ifafirmId ,String teamId,String keywords) {
		List<Object> params=new ArrayList<Object>();
		String hql=" from MemberIfa b INNER JOIN IfafirmTeamIfa a ON a.ifa.id=b.id "
			+ " where  a.team.id=? and b.ifafirm.id=? ";
		params.add(teamId);
		params.add(ifafirmId);
		if(StringUtils.isNoneBlank(keywords)){
			hql += " and (b.firstName like ? or b.lastName like ? or b.nameChn like ?)";
			params.add("%"+keywords+"%");
			params.add("%"+keywords+"%");
			params.add("%"+keywords+"%");
		}
		hql +=" ORDER BY a.isSupervisor desc";
		jsonpaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);
		List list = jsonpaging.getList();
		if(list!=null && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Object[] obj = (Object[]) list.get(i);
				MemberIfa ifa = (MemberIfa) obj[0];
				IfaVO vo = new IfaVO(ifa,null);
				obj[0] = vo;
			}
		}
		return jsonpaging;
	}
	
	/***
     * IfafirmTeamIfa 给团队添加成员
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public IfafirmTeamIfa saveOrUpdateTeamIfa(IfafirmTeamIfa ifafirmTeamIfa, boolean isAdd) {
		ifafirmTeamIfa=(IfafirmTeamIfa)this.baseDao.saveOrUpdate(ifafirmTeamIfa,isAdd);
		return ifafirmTeamIfa;
	}
	
	/***
     * IfafirmTeamIfa 判断该成员是否存在在该团队中，如果已存在，则返回该成员实体
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public IfafirmTeamIfa getIfaIsExistInTeam(String ifafirmId,String teamId,String ifaId) {
		String hql = " FROM IfafirmTeamIfa a  WHERE  a.ifafirm.id=? AND a.team.id=? AND a.ifa.id=? ";
		List<Object> params = new ArrayList<Object>();
		params.add(ifafirmId);
		params.add(teamId);
		params.add(ifaId);
		List<IfafirmTeamIfa> list = this.baseDao.find(hql, params.toArray(), false);
		if(!list.isEmpty())
		{
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * IfafirmTeamIfa 删除团队成员团
	 * @author qgfeng
	 * @date 2017-1-12
	 */
	@Override
	public Boolean delIfafirmTeamIfa(String ifafirmTeamIfaId)
	{
		if(StringUtils.isBlank(ifafirmTeamIfaId)){
			return false;
		}
		IfafirmTeamIfa teamIfa = (IfafirmTeamIfa) baseDao.get(IfafirmTeamIfa.class, ifafirmTeamIfaId);
		if(teamIfa != null){
			baseDao.delete(teamIfa);
			return true;
		}
		return false;
	}
	/***
     * 通过输入一个KEY，同时传递一个公司的ID，获取公司以下所有待选IFA数据，用于团队成员选择窗口
     * @author zxtan
     * @date 2016-12-12
     */
	@Override
//	public JsonPaging getIFAListForSelect(JsonPaging jsonPaging,String ifafirmId,String teamId,String keyword) {
//		List<Object> params = new ArrayList<Object>();
//		StringBuilder hql = new StringBuilder();		
//		hql.append(" SELECT m.ifa FROM MemberIfaIfafirm m WHERE m.ifafirm.id=? ");
//		//筛选出未进团队的ifa
//		hql.append(" AND m.ifa.id not in ( SELECT i.ifa.id FROM IfafirmTeamIfa i where i.team.id=?)");
//		params.add(ifafirmId);
//		params.add(teamId);
//		if(StringUtils.isNoneBlank(keyword)){
//			hql.append(" and ( m.ifa.firstName like ? or m.ifa.lastName like ? or m.ifa.nameChn like ? )");
//			params.add("%"+keyword+"%");
//			params.add("%"+keyword+"%");
//			params.add("%"+keyword+"%");
//		}
//		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
//		return jsonPaging;
//	}
	public JsonPaging getIFAListForSelect(JsonPaging jsonpaging, String ifafirmId,String teamId,String keyword) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT fn_queryIfafirmTeam(id) FROM IfafirmTeam t WHERE t.classLayer=1 AND t.ifafirm.id=? AND FIND_IN_SET(?,fn_queryIfafirmTeam(t.id))>0 ");
		List<Object> params = new ArrayList<Object>();
		params.add(ifafirmId);
		params.add(teamId);
		
		Object objTeamIds = this.baseDao.getUniqueResult(hql.toString(), params.toArray(), false);
		if(null != objTeamIds){
			hql = new StringBuilder();
			params.clear();
			
			hql.append(" FROM MemberIfa m WHERE m.id in (select ifa.id from MemberIfaIfafirm where ifafirm.id=?) ");
			hql.append(" and ( m.firstName like ? or m.lastName like ? or m.nameChn like ? )");
			hql.append(" AND m.id not in ( ");
			hql.append(" SELECT i.ifa.id FROM IfafirmTeamIfa i");
			hql.append(" WHERE FIND_IN_SET(i.team.id,? ) >0 )");

			params.add(ifafirmId);
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
			params.add(objTeamIds);
//			List<MemberIfa> list = this.baseDao.find(hql.toString(), params.toArray(), false);
			jsonpaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonpaging, false);
			return jsonpaging;
		}
		return null;		
	}
	
	/***
     * IfafirmTeamIfa
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public IfafirmTeamIfa saveOrUpdateTeamMemberifa(IfafirmTeamIfa ifafirmTeamIfa, boolean isAdd) {
		ifafirmTeamIfa=(IfafirmTeamIfa)this.baseDao.saveOrUpdate(ifafirmTeamIfa,isAdd);
		return ifafirmTeamIfa;
	}
	
	/***
     * IfafirmTeamIfa 通过输入一个KEY，同时传递一个公司的ID，获取公司以下所有IFA数据，用于团队成员选择窗口
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public List<MemberIfa> getIFAKeyList(String ifafirmId,String keywords) {
		String hql = " FROM MemberIfa  b WHERE  b.ifafirm.id=? and ( b.firstName like ? or b.lastName like ? )";
		List<Object> params1=new ArrayList<Object>();
		params1.add(ifafirmId);
		params1.add("%"+keywords+"%");
		params1.add("%"+keywords+"%");
		List<MemberIfa> list = this.baseDao.find(hql, params1.toArray(), false);
		return list;
	}
	
	//-----end IfafirmTeamIfa action----
	
	/***
     * ifafirm的代理商列表查询的方法
     * @author 林文伟   modify by rqwang 20170603 
     * @date 2016-07-12
     */
	@Override
	//@Transactional(readOnly=true)
	public JsonPaging findIfafirmDistributorList(JsonPaging jsonpaging, String ifafirmId,HttpServletRequest request) {
		String lang = this.getLoginLangFlag(request);
		String langues = this.getLangFirstCharUpper(lang);
		//StringBuffer sbHql = new StringBuffer(" select b.id,a.companyName,a.id,a.logoFile,a.website,a.registerNo,a.incorporationDate, s.name"+langues+" as incorporationPlace,a.isFinancial ");
		StringBuffer sbHql = new StringBuffer();
		sbHql.append(" from MemberDistributor a INNER JOIN IfafirmDistributor b ON a.id=b.distributor.id ");
		sbHql.append(" left join SysCountry s on s.id=a.incorporationPlace ");
		sbHql.append(" where b.ifafirm.id=? ");
		List<Object> params=new ArrayList<Object>();
		params.add(ifafirmId);
		jsonpaging=this.baseDao.selectJsonPaging(sbHql.toString(), params.toArray(), jsonpaging, false);

		//start modify by rqwang 20170626
		List jsonList = jsonpaging.getList();
		List<MemberDistributorExtend> list = new ArrayList<MemberDistributorExtend>();
		if(null != jsonList && !jsonList.isEmpty()){
			for(int i=0;i<jsonList.size();i++){
				Object[] objs = (Object[])jsonList.get(i);
				MemberDistributor distributor = (MemberDistributor) objs[0];
				IfafirmDistributor ifafirmDistributor = (IfafirmDistributor) objs[1];
				MemberDistributorExtend vo = new MemberDistributorExtend();
				
				vo.setIfafirmDistributorid(ifafirmDistributor.getId());//删除代理商与ifafirm关系使用
				vo.setId(distributor.getId());
				vo.setCompanyName(distributor.getCompanyName());
				vo.setWebsite(distributor.getWebsite());
				vo.setRegisterNo(distributor.getRegisterNo());
				vo.setIsFinancial(distributor.getIsFinancial());
				String countryId = distributor.getIncorporationPlace();
	    		if(StringUtils.isNoneBlank(countryId)){
	    			SysCountryVO sysCountyVo = countryService.findById(countryId);
	        		String incorporationPlaceName = sysCountyVo.getCountryName(lang);
	        		vo.setincorporationPlaceName(incorporationPlaceName);
	    		}
	    		String logoUrlParse = PageHelper.getLogoUrl(distributor.getLogoFile(), "D");
	    		vo.setDistributorLogo(logoUrlParse);
	    		vo.setEntityType(distributor.getEntityType());
				list.add(vo);
			}
		}
		jsonpaging.setList(list);
		return jsonpaging;
		//end
	}
	
	/***
     * 删除公司与代理商的关联关系(IfafirmDistributor)
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public void delIfafirmDistributorid(String id)
	{
		String hql1 = " delete IfafirmDistributor where  id =? ";
		List<Object> params = new ArrayList<Object>();
		params.add(id);
		this.baseDao.updateHql(hql1, params.toArray());
	}
	
	/***
     * 添加公司与代理商的关联关系(IfafirmDistributor)
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public void addIfafirmDistributor(String distributorId,String ifafirmId)
	{
		String hql1 = " delete IfafirmDistributor a  where  a.distributor.id =? and a.ifafirm.id=?  ";
		List<Object> params1 = new ArrayList<Object>();
		params1.add(distributorId);
		params1.add(ifafirmId);
		this.baseDao.updateHql(hql1, params1.toArray());
		IfafirmDistributor model = new IfafirmDistributor();
		
		MemberDistributor distributor = new MemberDistributor();
		distributor.setId(distributorId);
		
		MemberIfafirm ifafirm = new MemberIfafirm();
		ifafirm.setId(ifafirmId);
		
		model.setDistributor(distributor);
		model.setIfafirm(ifafirm);
		this.baseDao.saveOrUpdate(model,true);
	}

	//-----end IfafirmDistributor action----
	
	/**
	 * IfafirmDistributor 根据id获取ifafirm关联的公司（子公司）
	 * @author qgfeng
	 * @date 2016-11-15
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public List findIfafirmIfafirmByid(String ifafirmId,String langCode) {
		List list = new ArrayList();
		if(StringUtils.isNotBlank(ifafirmId)){
			List params =new ArrayList();
			StringBuffer hql =new StringBuffer(" from IfafirmIfafirm where isValid=1 and parent_ifafirm_id=?");	
			params.add(ifafirmId);
			List<IfafirmIfafirm> firmList = this.baseDao.find( hql.toString(), params.toArray(), false );
			if(!firmList.isEmpty()){
				for (IfafirmIfafirm ifafirmIfafirm : firmList) {
					String hhql = "select companyName from "+this.getLangString("MemberIfafirm", langCode);
					hhql+=" where id = '"+ifafirmIfafirm.getIfafirm().getId()+"'";
					String companyName = (String) baseDao.getUniqueResult(hhql,null, false);
					MemberIfafirmVO vo = new MemberIfafirmVO();
					BeanUtils.copyProperties(ifafirmIfafirm.getIfafirm(), vo);
					vo.setCompanyName(companyName);
					list.add(vo);
				}
			}
		}
		return list;
	}
	
	
	/**
	 * IfafirmDistributor保存公司与公司关系（子公司）
	 * @author wwluo
	 * @date 2016-08-23
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean saveChildFirm(String parentId, String childId) {
		boolean flag = false;
		if(StringUtils.isNotBlank(parentId) && StringUtils.isNoneBlank(childId)){
			MemberIfafirm parentFirm = (MemberIfafirm) this.baseDao.get(MemberIfafirm.class, parentId);
			MemberIfafirm childFirm = (MemberIfafirm) this.baseDao.get(MemberIfafirm.class, childId);
			if(null != parentFirm && null != childFirm){
				IfafirmIfafirm ifafirm = new IfafirmIfafirm();
			    StringBuffer hql = new StringBuffer(" from IfafirmIfafirm where parentIfafirm=? and ifafirm=?");
			    List params = new ArrayList();
			    params.add(parentFirm);
			    params.add(childFirm);
			    List<IfafirmIfafirm> list = this.baseDao.find(hql.toString(), params.toArray(), false);
			    if(!list.isEmpty()){
			    	ifafirm = list.get(0);
			    }else{
			    	ifafirm.setParentIfafirm(parentFirm);
					ifafirm.setIfafirm(childFirm);
			    }
			    ifafirm.setIsValid("1");
		    	ifafirm.setCreateTime(new Date());
			    IfafirmIfafirm obj = (IfafirmIfafirm) this.baseDao.saveOrUpdate(ifafirm);
			    if(null != obj){
			    	flag = true;
			    }
			}
		}
		return flag;
	}
	
	/**
	 * IfafirmDistributor移除公司与公司关系（子公司）
	 * @author wwluo
	 * @date 2016-08-23
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean removeChildFirm(String parentId, String childId) {
		boolean flag = false;
		if(StringUtils.isNotBlank(parentId) && StringUtils.isNoneBlank(childId)){
			MemberIfafirm parentFirm = (MemberIfafirm) this.baseDao.get(MemberIfafirm.class, parentId);
			MemberIfafirm childFirm = (MemberIfafirm) this.baseDao.get(MemberIfafirm.class, childId);
			if(null != parentFirm && null != childFirm){
				IfafirmIfafirm ifafirm = new IfafirmIfafirm();
			    StringBuffer hql = new StringBuffer(" from IfafirmIfafirm where parentIfafirm=? and ifafirm=?");
			    List params = new ArrayList();
			    params.add(parentFirm);
			    params.add(childFirm);
			    List<IfafirmIfafirm> list = this.baseDao.find(hql.toString(), params.toArray(), false);
			    if(!list.isEmpty()){
			    	ifafirm = list.get(0);
			    }else{
			    	return flag;
			    }
			    ifafirm.setIsValid("0");
			    IfafirmIfafirm obj = (IfafirmIfafirm) this.baseDao.update(ifafirm);
			    if(null != obj){
			    	flag = true;
			    }
			}
		}
		return flag;
	}
	//-----end IfafirmIfafirm action----
	/**
	 * 查询条件自动填充IFAFirm
	 * @author qgfeng
	 * @date 2016-11-10
	 */
	@Override
	public List<AutoCompleteVO> findAutoIfafirm(String keyWord,String langCode) {
		String hql = "select r.id,r.companyName from "+this.getLangString("MemberIfafirm", langCode)+" r where 1=1 ";
		List<String> params = new ArrayList<String>();
		if (StringUtils.isNotBlank(keyWord)) {
			hql += " and r.companyName like ?";
			params.add("%"+keyWord+"%");
		}
		List<Object[]> list = baseDao.find(hql, params.toArray(), false);
		List<AutoCompleteVO> finalList = new ArrayList<AutoCompleteVO>();
		Iterator<Object[]> iterator = list.iterator();
		while (iterator.hasNext()) {
			Object[] obj = iterator.next();
			if(obj[1]!=null){
				AutoCompleteVO vo = new AutoCompleteVO();
				vo.setId(obj[0].toString());
				vo.setName(obj[1].toString());
				finalList.add(vo);
			}
		}
		return finalList;
	}

	@Override
	public List<MemberIfafirmVO> getIfaFirms(String langCode) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 代理商下的ifaFirm列表
	 * @author rqwang
	 * @date 2016-06-08
	 */
	@Override
	public JsonPaging getdisIfaFirmJson(JsonPaging jsonPaging, String id,
			MemberIfafirmVO vo, String langFlag) {
		StringBuffer hql = new StringBuffer(" select f,lang.companyName from MemberIfafirm f ");
		hql.append(" left join "+this.getLangString("MemberIfafirm", langFlag)+" lang on f.id = lang.id ");
		hql.append(" left join  IfafirmDistributor dis on f.id=dis.ifafirm.id");
		hql.append(" where dis.distributor.id=? and f.isValid=1 ");
		List<String> params = new ArrayList<String>();
		params.add(id);
		if(StringUtils.isNotBlank(vo.getCompanyName())){
			hql.append(" and lang.companyName like ?");
			params.add("%"+vo.getCompanyName()+"%");
		}
		if(StringUtils.isNotBlank(vo.getRegisterNo())){
			hql.append(" and f.registerNo like ?");
			params.add("%"+vo.getRegisterNo()+"%");
		}
		if(StringUtils.isNotBlank(vo.getEntityType())){
			hql.append(" and f.entityType=?");
			params.add(vo.getEntityType());
		}
		jsonPaging = baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List jsonList = jsonPaging.getList();
		List<MemberIfafirmVO> list = new ArrayList<MemberIfafirmVO>();
		if(null != jsonList && !jsonList.isEmpty()){
			for(int i=0;i<jsonList.size();i++){
				Object[] objs = (Object[])jsonList.get(i);
				MemberIfafirm firm = (MemberIfafirm) objs[0];
				MemberIfafirmVO firmVo = new MemberIfafirmVO();
				firmVo.setId(firm.getId());
				firmVo.setEntityType(firm.getEntityType());
				firmVo.setCountry(firm.getCountry());
				firmVo.setIncorporationDate(firm.getIncorporationDate());
				String logoUrlParse = PageHelper.getLogoUrl(firm.getFirmLogo(), "F");
				firmVo.setFirmLogo(logoUrlParse);
				firmVo.setWebsite(firm.getWebsite());
				
				if(StringUtils.isNotBlank(firm.getCountry())){
					SysCountryVO county = countryService.findById(firm.getCountry());
					if(county != null)firmVo.setCountry(county.getCountryName(langFlag));
				}
				if(objs[1]!=null){
					firmVo.setCompanyName(objs[1].toString());
				}
				firmVo.setIsFinancial(firm.getIsFinancial());
				list.add(firmVo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}


}


