package com.fsll.wmes.member.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysCountry;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberCorporate;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberFi;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.member.service.MemberManageServiceForConsole;
import com.fsll.wmes.member.vo.FiVO;
import com.fsll.wmes.member.vo.IfaVO;
import com.fsll.wmes.member.vo.IndividualVO;
import com.fsll.wmes.member.vo.MemberAdminVO;

/***
 * 业务接口实现类：用户管理接口类
 * 
 * @author mqzou
 * @date 2016-06-30
 */
@Service("memberManageServiceImpl")
//@Transactional
public class MemberManageServiceImplForConsole extends BaseService implements MemberManageServiceForConsole {


	@Override
	public JsonPaging findAllIndividual(JsonPaging jsonpaging, MemberIndividual memberIndividual, String langCode) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from MemberIndividual r where r.member.isValid='1' ");
		List<String> params = new ArrayList<String>();
		if (!"".equals(memberIndividual.getMember().getLoginCode())) {
			String loginCode = memberIndividual.getMember().getLoginCode();
			hql.append(" and (r.member.loginCode like ? or r.member.nickName like ? or r.member.email like ? or r.firstName like ? or r.lastName like ? or r.nameChn like ? ) ");
			params.add("%"+loginCode+"%");
			params.add("%"+loginCode+"%");
			params.add("%"+loginCode+"%");
			params.add("%"+loginCode+"%");
			params.add("%"+loginCode+"%");
			params.add("%"+loginCode+"%");
		}
		//如果当前用户是ifa
		/*if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA==memberIndividual.getMember().getMemberType()){
			hql.append(" and r.member.id in (select i.member.id from InvestorAccount i left join MemberAdmin m on i.ifa.ifafirm.id=m.ifafirm.id  where m.member.id=? ) ");
			params.add(memberIndividual.getMember().getId());
		}
*/
		jsonpaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonpaging, false);
		List<IndividualVO> list = new ArrayList<IndividualVO>();
		Iterator<MemberIndividual> it = jsonpaging.getList().iterator();
		while (it.hasNext()) {
			MemberIndividual obj = it.next();
			IndividualVO vo = new IndividualVO();
			vo.setId(obj.getId());
			vo.setMemberId(null == obj.getMember() ? "" : obj.getMember().getId());
			vo.setEmail(null == obj.getMember().getEmail() ? "" : obj.getMember().getEmail());
			vo.setFirstName(null == obj.getFirstName() ? "" : obj.getFirstName());
			vo.setLastName(null == obj.getLastName() ? "" : obj.getLastName());
			vo.setLoginCode(null == obj.getMember().getLoginCode() ? "" : obj.getMember().getLoginCode());
			vo.setNickName(null == obj.getMember() ? "" : obj.getMember().getNickName());
			vo.setCountry(this.getCountryString(obj.getCountry(), langCode));
			vo.setNameChn(null == obj.getNameChn() ? "" : obj.getNameChn());
			list.add(vo);
		}
		jsonpaging.setList(list);
		return jsonpaging;
	}

	@Override
	public JsonPaging findAllCorporate(JsonPaging jsonpaging, MemberCorporate memberCorporate, String langCode) {
		String hql = " from MemberCorporate r where  r.member.isValid='1' ";
		List params = new ArrayList();

		jsonpaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);
		List list = new ArrayList();
		Iterator it = jsonpaging.getList().iterator();
		// Integer indexNumber = (jsonpaging.getPage()-1)*jsonpaging.getRows();
		// Integer index = 0;
		while (it.hasNext()) {
			// index++;

			MemberCorporate obj = (MemberCorporate) it.next();

			list.add(obj);
		}
		jsonpaging.setList(list);
		return jsonpaging;
		// return null;
	}

	@Override
	public MemberIndividual findIndividualById(String id) {
		MemberIndividual vo = (MemberIndividual) baseDao.get(MemberIndividual.class, id);
		return vo;
	}

	//保存Individual信息
	@Override
	public MemberIndividual saveIndividual(MemberIndividual memberIndividual) {
		if(memberIndividual == null || memberIndividual.getMember() == null){
			return null;
		}
		MemberBase memberBase = memberIndividual.getMember();
		memberBase = (MemberBase) baseDao.saveOrUpdate(memberBase);
		if(memberBase != null){
			memberIndividual.setMember(memberBase);
			memberIndividual = (MemberIndividual) baseDao.saveOrUpdate(memberIndividual);
			return memberIndividual;
		}
		return null;
	}
	
	@Override
	public MemberIndividual saveIndividual(MemberIndividual memberIndividual, MemberBase memberBase) {
		// TODO Auto-generated method stub
		if (null == memberIndividual.getId() || "".equals(memberIndividual.getId())) {
			if (null == memberBase.getId() || "".equals(memberBase.getId())) {
				memberBase.setId(null);
				memberBase.setCreateTime(new Date());
				memberBase = (MemberBase) baseDao.create(memberBase);
				memberBase.setIsValid("1");
			}
			memberIndividual.setMember(memberBase);
			memberIndividual.setId(null);
			memberIndividual.setCreateTime(new Date());
			memberIndividual = (MemberIndividual) baseDao.create(memberIndividual);
		} else {
			memberIndividual = (MemberIndividual) baseDao.update(memberIndividual);
			memberBase = (MemberBase) baseDao.update(memberBase);

		}
		return memberIndividual;
	}

	@Override
	public SysCountry findCountryById(String id) {
		SysCountry vo = (SysCountry) baseDao.get(SysCountry.class, id);
		return vo;
	}

	@Override
	public JsonPaging findAllIfa(JsonPaging jsonPaging, MemberIfa memberIfa, String langCode) {
		String hql = " from MemberIfa r where   r.member.isValid='1' ";
		List params = new ArrayList();

		if (null != memberIfa && null != memberIfa.getMember() && null != memberIfa.getMember().getLoginCode()) {
			hql += " and (r.member.loginCode like '%" + memberIfa.getMember().getLoginCode() + "%' or r.member.nickName like '%" + memberIfa.getMember().getLoginCode() + "%' )";
		}
		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA==memberIfa.getMember().getMemberType()){
			hql+=" and r.ifafirm.id='"+memberIfa.getIfafirm().getId()+"'";
		}

		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		List list = new ArrayList();
		Iterator it = jsonPaging.getList().iterator();
		while (it.hasNext()) {
			IfaVO vo = new IfaVO();
			MemberIfa obj = (MemberIfa) it.next();
			vo.setId(obj.getId());
			vo.setMemberId(null == obj.getMember() ? "" : obj.getMember().getId());
			vo.setEmail(null == obj.getMember().getEmail() ? "" : obj.getMember().getEmail());
			vo.setFirstName(null == obj.getFirstName() ? "" : obj.getFirstName());
			vo.setLastName(null == obj.getLastName() ? "" : obj.getLastName());
			vo.setLoginCode(null == obj.getMember().getLoginCode() ? "" : obj.getMember().getLoginCode());
			vo.setNickName(null == obj.getMember() ? "" : obj.getMember().getNickName());
			vo.setCountry(this.getCountryString(obj.getCountry(), langCode));

			String companyType = obj.getCompanyType();
			vo.setCompanyTypeValue(companyType == "2" ? "Ifafirm" : "Individual");
			vo.setCompanyIfafirmId(obj.getIfafirm() == null ? "" : obj.getIfafirm().getId());
			vo.setCompanyIfafirm(obj.getIfafirm() == null ? "" : obj.getIfafirm().getCompanyName());
			vo.setAppellation(obj.getAppellation());
			vo.setCeNumber(obj.getCeNumber());
			vo.setCfaNumber(obj.getCfaNumber());
			vo.setCfpNumber(obj.getCfpNumber());
			list.add(vo);
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}

	@Override
	public MemberIfa finIfaById(String id) {
		if(StringUtils.isBlank(id)){
			return null;
		}
		MemberIfa vo = (MemberIfa) baseDao.get(MemberIfa.class, id);
		return vo;
	}

	@Override
	public MemberIfa saveIfa(MemberIfa memberIfa) {
		// TODO Auto-generated method stub
		MemberBase memberBase = memberIfa.getMember();
		if (null == memberIfa.getId() || "".equals(memberIfa.getId())) {
			memberBase.setId(null);
			memberBase.setCreateTime(new Date());
			memberBase.setIsValid("1");
			memberBase = (MemberBase) baseDao.create(memberBase);
			memberIfa.setMember(memberBase);
			memberIfa.setId(null);
			memberIfa.setCreateTime(new Date());
			memberIfa = (MemberIfa) baseDao.create(memberIfa);
		} else {

			memberIfa = (MemberIfa) baseDao.update(memberIfa);
			memberBase = (MemberBase) baseDao.update(memberBase);

		}
		return memberIfa;
	}

	@Override
	public JsonPaging findAllIfainIfaFirm(JsonPaging jsonPaging, MemberIfa memberIfa, String langCode) {
		String hql = " from MemberIfa r where companyType='2' and  r.member.isValid='1'";
		List params = new ArrayList();

		/*
		 * if(!individualVO.getLastName().equals("")){
		 * sql+=" and b.last_name like '%"+individualVO.getLastName()+"%'"; }
		 */

		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		List list = new ArrayList();
		Iterator it = jsonPaging.getList().iterator();
		while (it.hasNext()) {
			IfaVO vo = new IfaVO();
			MemberIfa obj = (MemberIfa) it.next();
			vo.setId(obj.getId());
			vo.setMemberId(null == obj.getMember() ? "" : obj.getMember().getId());
			vo.setEmail(obj.getMember().getEmail());
			vo.setFirstName(obj.getFirstName());
			vo.setLastName(obj.getLastName());
			vo.setLoginCode(obj.getMember().getLoginCode());
			vo.setNickName(null==obj.getMember()?"":obj.getMember().getNickName());
			vo.setCountry(this.getCountryString(obj.getCountry(), langCode));

			String companyType = obj.getCompanyType();
			vo.setCompanyTypeValue(companyType == "2" ? "Ifafirm" : "Individual");
			vo.setCompanyIfafirmId(obj.getIfafirm() == null ? "" : obj.getIfafirm().getId());
			vo.setCompanyIfafirm(obj.getIfafirm() == null ? "" : obj.getIfafirm().getCompanyName());
			vo.setAppellation(obj.getAppellation());
			vo.setCeNumber(obj.getCeNumber());
			vo.setCfaNumber(obj.getCfaNumber());
			vo.setCfpNumber(obj.getCfpNumber());
			list.add(vo);
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}

	@Override
	public List<MemberIfafirm> getIfafirmlist() {
		// TODO Auto-generated method stub
		String hql = " from MemberIfafirm r where 1=1 ";
		List list = this.baseDao.find(hql, null, false);
		List returnList = new ArrayList();
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			MemberIfafirm vo = (MemberIfafirm) iterator.next();
			returnList.add(vo);
		}
		return returnList;
	}

	@Override
	public MemberIfafirm findIfafirmById(String id) {
		MemberIfafirm vo = (MemberIfafirm) this.baseDao.get(MemberIfafirm.class, id);
		return vo;
	}

	@Override
	public JsonPaging findallCoporate(JsonPaging jsonPaging, MemberCorporate memberCorporate, String langCode) {
		String hql = " from MemberCorporate r where r.member.isValid='1' ";
		List params = new ArrayList();

		if (null != memberCorporate && null != memberCorporate.getMember() && null != memberCorporate.getMember().getLoginCode()) {
			hql += " and (r.member.loginCode like '%" + memberCorporate.getMember().getLoginCode() + "%' or r.member.nickName like '%" + memberCorporate.getMember().getLoginCode() + "%' )";
		}
		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA==memberCorporate.getMember().getMemberType()){
			hql+=" and r.member.id in (select i.member.id from InvestorAccount i left join MemberAdmin m on i.ifa.ifafirm.id=m.ifafirm.id  where m.member.id='"+memberCorporate.getMember().getId()+"' )";
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		List list = new ArrayList();
		Iterator it = jsonPaging.getList().iterator();
		while (it.hasNext()) {
			MemberCorporate obj = (MemberCorporate) it.next();
			FiVO vo = new FiVO();
			vo.setId(obj.getId());
			vo.setMemberId(null == obj.getMember() ? "" : obj.getMember().getId());
			vo.setEmail(obj.getMember().getEmail());
			vo.setLoginCode(obj.getMember().getLoginCode());
			vo.setNickName(obj.getMember().getNickName());
			vo.setCompanyName(obj.getCompanyName());
			vo.setCountry(this.getCountryString(obj.getCountry(), langCode));

			list.add(vo);
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}

	@Override
	public MemberCorporate findCorporateById(String id) {
		MemberCorporate vo = (MemberCorporate) this.baseDao.get(MemberCorporate.class, id);
		return vo;
	}

	@Override
	public MemberCorporate savecCorporate(MemberCorporate memberCorporate) {
		// TODO Auto-generated method stub
		MemberBase memberBase = memberCorporate.getMember();
		if (null == memberCorporate.getId() || "".equals(memberCorporate.getId())) {
			memberBase.setId(null);
			memberBase.setCreateTime(new Date());
			memberBase.setIsValid("1");
			memberBase = (MemberBase) baseDao.create(memberBase);
			memberCorporate.setMember(memberBase);
			memberCorporate.setId(null);
			memberCorporate = (MemberCorporate) baseDao.create(memberCorporate);
		} else {

			memberCorporate = (MemberCorporate) baseDao.update(memberCorporate);
			memberBase = (MemberBase) baseDao.update(memberBase);

		}
		return memberCorporate;
	}

	@Override
	public JsonPaging findallFi(JsonPaging jsonPaging, MemberFi memberFi, String langCode) {
		String hql = " from MemberFi r where  r.member.isValid='1' ";
		List params = new ArrayList();

		if (null != memberFi && null != memberFi.getMember() && null != memberFi.getMember().getLoginCode()) {
			hql += " and (r.member.loginCode like '%" + memberFi.getMember().getLoginCode() + "%' or r.member.nickName like '%" + memberFi.getMember().getLoginCode() + "%' )";
		}
		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA==memberFi.getMember().getMemberType()){
			hql+=" and r.member.id in (select i.member.id from InvestorAccount i left join MemberAdmin m on i.ifa.ifafirm.id=m.ifafirm.id  where m.member.id='"+memberFi.getMember().getId()+"' )";
		}

		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		List list = new ArrayList();
		Iterator it = jsonPaging.getList().iterator();
		while (it.hasNext()) {
			MemberFi obj = (MemberFi) it.next();
			FiVO vo = new FiVO();
			vo.setId(obj.getId());
			vo.setMemberId(null == obj.getMember() ? "" : obj.getMember().getId());
			vo.setEmail(obj.getMember().getEmail());

			vo.setLoginCode(obj.getMember().getLoginCode());
			vo.setNickName(obj.getMember().getNickName());
			vo.setCompanyName(obj.getCompanyName());
			vo.setCountry(this.getCountryString(obj.getCountry(), langCode));
			list.add(vo);
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}

	@Override
	public MemberFi findFiById(String id) {
		MemberFi vo = (MemberFi) this.baseDao.get(MemberFi.class, id);
		return vo;
	}

	@Override
	public MemberFi saveFi(MemberFi memberFi) {
		// TODO Auto-generated method stub
		MemberBase memberBase = memberFi.getMember();
		if (null == memberFi.getId() || "".equals(memberFi.getId())) {
			memberBase.setId(null);
			memberBase.setCreateTime(new Date());
			memberBase.setIsValid("1");
			memberBase = (MemberBase) baseDao.create(memberBase);
			memberFi.setMember(memberBase);
			memberFi.setId(null);
			memberFi = (MemberFi) baseDao.create(memberFi);
		} else {

			memberFi = (MemberFi) baseDao.update(memberFi);
			memberBase = (MemberBase) baseDao.update(memberBase);

		}
		return memberFi;
	}

	@Override
	public MemberDistributor findDistributorById(String id) {
		// TODO Auto-generated method stub
		String hql = "from MemberDistributor where id=? ";
		List params = new ArrayList();
		params.add(id);
		List<MemberDistributor> list = this.baseDao.find(hql, params.toArray(), false);

		if (null != list && !list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/***
	 * 查询所有的代理公司方法
	 * 
	 * @date 2016-7-06
	 * @return list
	 */
	//@Transactional(readOnly = true)
	@Override
	public List<MemberDistributor> findAllDistributors() {

		String hql = "from MemberDistributor ";
		List<MemberDistributor> list = this.baseDao.find(hql, null, false);

		return list;
	}

	@Override
	public MemberIndividual delteIndividual(String id) {
		MemberIndividual vo = findIndividualById(id);
		if (null != vo) {
			MemberBase base = vo.getMember();
			base.setIsValid("0");
			vo.setMember(base);
			base = (MemberBase) this.baseDao.update(base);
			return vo;
		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MemberFi deleteMemberFi(String id) {
		MemberFi vo = findFiById(id);
		if (null != vo) {
			MemberBase base = vo.getMember();
			base.setIsValid("0");
			vo.setMember(base);
			base = (MemberBase) this.baseDao.update(base);
			return vo;
		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MemberCorporate delelteCorporate(String id) {
		MemberCorporate vo = findCorporateById(id);
		if (null != vo) {
			MemberBase base = vo.getMember();
			base.setIsValid("0");
			vo.setMember(base);
			base = (MemberBase) this.baseDao.update(base);
			return vo;
		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MemberIfa deleteIfa(String id) {
		MemberIfa vo = finIfaById(id);
		if (null != vo) {
			MemberBase base = vo.getMember();
			base.setIsValid("0");
			vo.setMember(base);
			base = (MemberBase) this.baseDao.update(base);
			return vo;
		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MemberIfa findMemberIfaByMemberId(String memeberId) {
		MemberIfa memberIfa = null;
		String hql = " from MemberIfa r  where r.member.id='" + memeberId + "'";
		List list = this.baseDao.find(hql, null, false);
		if (null != list && !list.isEmpty()) {
			Iterator it = (Iterator) list.iterator();
			while (it.hasNext()) {
				memberIfa = (MemberIfa) it.next();
			}
		}
		// TODO Auto-generated method stub
		return memberIfa;
	}

	@Override
	public JsonPaging findAllAdminMember(JsonPaging jsonPaging, MemberAdmin memberAdmin) {
		String hql = " from MemberAdmin r where r.member.isValid='1'";

		if (null != memberAdmin ) {
			if(null != memberAdmin.getMember() && null != memberAdmin.getMember().getLoginCode() && !"".equals(memberAdmin.getMember().getLoginCode())){
				hql += " and (r.member.loginCode like '%" + memberAdmin.getMember().getLoginCode() + "%' or r.member.nickName like '%" + memberAdmin.getMember().getLoginCode() + "%')";
			}
			if(null!=memberAdmin.getType() && !"".equals(memberAdmin.getType())){
				hql+=" and r.type='"+memberAdmin.getType()+"'";
			}
			
		}
		
		jsonPaging.setOrder("asc, asc,asc,asc");
		jsonPaging.setSort("r.type, r.ifafirm,r.distributor,r.member.loginCode");

		List params = new ArrayList();
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);

	
		List list = new ArrayList();
		Iterator it = (Iterator) jsonPaging.getList().iterator();

		while (it.hasNext()) {
			MemberAdmin entity = (MemberAdmin) it.next();
			MemberAdminVO vo = new MemberAdminVO();
			vo.setId(entity.getId());
			vo.setParentId(null!=entity.getParent()?entity.getParent().getId():"");
			vo.setParentName(null!=entity.getParent()?entity.getParent().getMember().getNickName():"");
			String company = "";
			if (null != entity.getIfafirm() )
				company = entity.getIfafirm().getCompanyName();
			else if (null != entity.getDistributor()) {
				company = entity.getDistributor().getCompanyName();
			}
			vo.setCompanyName(company);
			vo.setLoginCode(entity.getMember().getLoginCode());
			vo.setMemberId(entity.getMember().getId());
			vo.setType(entity.getType());
			vo.setNickName(entity.getMember().getNickName());
			list.add(vo);
		}
		jsonPaging.setList(list);

		return jsonPaging;
	}
	
	private String getMapObject(Map map, String key) {
		return map.get(key) == null ? "" : map.get(key).toString();
	}
	@Override
	public JsonPaging findAllAdminMemberPro(JsonPaging jsonPaging, MemberAdmin memberAdmin,String langCode) {
		int pageIndex=(jsonPaging.getPage()-1)*jsonPaging.getRows();
		int pageSize=jsonPaging.getRows();
		String keyWord="";
		String id="";
		if (null != memberAdmin && null != memberAdmin.getMember() && null != memberAdmin.getMember().getLoginCode()) {
			keyWord=memberAdmin.getMember().getLoginCode();
		}
		if(null!=memberAdmin && null!= memberAdmin.getId()){
			id=memberAdmin.getId();
		}
		
		String sql="CALL  pro_getchildadmin('"+id+"',"+pageIndex+","+pageSize+",'"+keyWord+"','"+langCode+"')";

		//jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		//List resultList=this.baseDao.find(sql, params.toArray(), false);
		List resultList=springJdbcQueryManager.springJdbcQueryForList(sql);
		List list = new ArrayList();
		Iterator it = (Iterator) resultList.iterator();

		while (it.hasNext()) {
			HashMap entity = (HashMap) it.next();
			MemberAdminVO vo = new MemberAdminVO();
			vo.setId(getMapObject(entity,"id"));
			vo.setParentId(getMapObject(entity,"parent_id"));
			vo.setParentName(getMapObject(entity,"parent_name"));
			String company = "";
			if (null != getMapObject(entity,"ifafirm") && !"".equals(getMapObject(entity,"ifafirm")))
				company = getMapObject(entity,"ifafirm");
			else if (null != getMapObject(entity,"distributor" ) && !"".equals(getMapObject(entity,"distributor"))) {
				company = getMapObject(entity,"distributor");
			}
			vo.setCompanyName(company);
			vo.setLoginCode(getMapObject(entity,"login_code"));
			vo.setMemberId(getMapObject(entity,"member_id"));
			vo.setType(getMapObject(entity,"type"));
			vo.setNickName(getMapObject(entity,"nick_name"));
			list.add(vo);
		}
		jsonPaging.setList(list);

		return jsonPaging;
	}

	@Override
	public MemberAdmin findAdminById(String id) {
		MemberAdmin vo = (MemberAdmin) this.baseDao.get(MemberAdmin.class, id);
		return vo;
	}

	@Override
	public MemberAdmin saveAdmin(MemberAdmin memberAdmin) {
		MemberBase memberBase = memberAdmin.getMember();
		if (null == memberAdmin.getId() || "".equals(memberAdmin.getId())) {
			memberBase.setId(null);
			memberBase.setCreateTime(new Date());
			memberBase.setIsValid("1");
			memberBase = (MemberBase) baseDao.create(memberBase);
			memberAdmin.setMember(memberBase);
			memberAdmin.setId(null);
			memberAdmin = (MemberAdmin) baseDao.create(memberAdmin);
		} else {

			memberAdmin = (MemberAdmin) baseDao.update(memberAdmin);
			memberBase = (MemberBase) baseDao.update(memberBase);

		}
		return memberAdmin;
	}

	@Override
	public MemberAdmin findAdminByMemberId(String memberId) {
		String hql = " from MemberAdmin r where r.member.id='" + memberId + "'";
		List<MemberAdmin> list = this.baseDao.find(hql, null, false);
		if (null != list && !list.isEmpty()) {
			MemberAdmin memberAdmin = (MemberAdmin) list.get(0);
			return memberAdmin;
		}
		return null;
	}

	@Override
	public boolean deleteMember(String memberId) {
		MemberBase memberBase = findMemberbaseById(memberId);
		if (null != memberBase) {
			memberBase.setIsValid("0");
			memberBase.setLastUpdate(new Date());
			this.baseDao.update(memberBase);
			return true;
		}
		return false;
	}

	/**
	 * 获取memberBase实体
	 * 
	 * @author mqzou
	 * @date 2016-07-20
	 * @param id
	 * @return
	 */
	private MemberBase findMemberbaseById(String id) {
		MemberBase vo = (MemberBase) this.baseDao.get(MemberBase.class, id);
		return vo;
	}

	@Override
	public boolean checkLoginCode(String loginCode, String userId) {
		String hql = " from MemberBase r where loginCode='" + loginCode + "'";
		if (null != userId && !"".equals(userId)) {
			hql += " and id<>'" + userId + "'";
		}
		List list = this.baseDao.find(hql, null, false);
		if (null == list || list.isEmpty()) {
			return true;
		}
		// TODO Auto-generated method stub
		return false;
	}



}
