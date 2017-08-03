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

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.StrUtils;
import com.fsll.core.vo.SysCountryVO;
import com.fsll.wmes.distributor.vo.DistributorSimpleVO;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.IfafirmDistributor;
import com.fsll.wmes.entity.IfafirmIfafirm;
import com.fsll.wmes.entity.IfafirmTeam;
import com.fsll.wmes.entity.IfafirmTeamIfa;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfaIfafirm;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberIfafirmEn;
import com.fsll.wmes.entity.MemberIfafirmSc;
import com.fsll.wmes.entity.MemberIfafirmTc;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.ifafirm.vo.IfafirmAccountVO;
import com.fsll.wmes.ifafirm.vo.IfafirmCustomerVO;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmBaseVO;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmVO;
import com.fsll.wmes.member.vo.IfaVO;
import com.fsll.wmes.member.vo.MemberVO;
import com.fsll.wmes.web.service.CountryService;
import com.fsll.wmes.web.vo.MemberDistributorExtend;


/***
 * 业务接口实现类：公司管理接口类
 * @author 林文伟
 * @date 2016-06-22
 */
@Service("ifafirmManageService")
//@Transactional
public class IfafirmManageServiceImpl extends BaseService implements IfafirmManageService {

	@Autowired
	private CountryService  countryService;
	
	/***
     * ifafirm列表查询的方法
     * @author 林文伟
     * @date 2016-06-29
     */
	@Override
	//@Transactional(readOnly=true)
	public JsonPaging findAll(JsonPaging jsonpaging, MemberIfafirm info,MemberAdmin admin) {
		String hql = "";
		if(null!=admin)
		{
			if("2".equals(admin.getType()))//代理商
			{
				hql=" from MemberIfafirm r  ";
				hql += " inner join IfafirmDistributor d on d.ifafirm.id = r.id ";
				hql += " where d.distributor.id = '" + admin.getDistributor().getId() + "' ";
			}
			else if("0".equals(admin.getType()))//平台管理员
			{
				hql=" from MemberIfafirm r  ";
				hql += " where 1=1 ";
			}
			else //公司
			{
				String firmid = admin.getIfafirm().getId();
				hql=" from MemberIfafirm r  ";
				hql += " where r.id = '"+firmid+"' ";
			}
		}
		
		
		List<Object> params=new ArrayList<Object>();
		if(null!=info.getCompanyName()&&!"".equals(info.getCompanyName())){
			hql+=" and r.companyName like '%"+info.getCompanyName()+"%'";
		}
		
		jsonpaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);
		return jsonpaging;
	}
	
	/***
     * ifafirm的团队数据列表查询的方法
     * @author 林文伟
     * @date 2016-06-29
     */
	public List<IfafirmTeam> findTeamAll( IfafirmTeam team) {
		
		String  ifafirmId = team.getIfafirm().getId();
		List<IfafirmTeam> teamlist = new ArrayList<IfafirmTeam>();
		//先获取所有层节点
		String hql=" from IfafirmTeam r  where isValid='1' and classLayer=1 ";
		List<Object> params=new ArrayList<Object>();
		hql+=" and r.ifafirm.id = ?";
		params.add(ifafirmId);
		if (!StrUtils.isEmpty(team.getName())){
			hql+=" and r.name like ?";
			params.add("%"+team.getName()+"%");
		}
		hql+="  order by r.orderBy asc"; //and ( r.parentId is null or r.parentId = '' )

		List<IfafirmTeam> alllist=this.baseDao.find(hql, params.toArray(), false);
		if(!alllist.isEmpty())
		{
			for(IfafirmTeam each : alllist)
			{
				int classLayer = each.getClassLayer();
//				String id = each.getId();
				
				if(classLayer==1) //第1层时，可能有子层级
				{
					teamlist.add(each);

					List<IfafirmTeam> twolist=findChildTeam(each);
					
					if(!twolist.isEmpty())
					{
						teamlist.addAll(twolist);
					}
				}
			}
		}
		
		return teamlist;
	}
	
	/**
	 * 
	 * @param team
	 * @return
	 */
	public List<IfafirmTeam> findChildTeam(IfafirmTeam team) {
		List<IfafirmTeam> teamlist = new ArrayList<IfafirmTeam>();
		String hql1=" from IfafirmTeam r  where r.isValid='1' and r.parent.id=? and r.ifafirm.id=? order by r.orderBy asc";
		List<Object> params1=new ArrayList<Object>();
		params1.add(team.getId());
		params1.add(team.getIfafirm().getId());
		List<IfafirmTeam> list=this.baseDao.find(hql1, params1.toArray(), false);
		if(!list.isEmpty())
		{
			for(IfafirmTeam each : list)
			{
				teamlist.add(each);
				List<IfafirmTeam> twolist=findChildTeam(each);
				
				if(!twolist.isEmpty())
				{
					teamlist.addAll(twolist);
				}
			}
		}
		
		return teamlist;
	}
	
	/***
     * 通过流程ID获取动作列表
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public MemberIfafirm saveOrUpdate(MemberIfafirm memberIfafirm, boolean isAdd) {
		memberIfafirm=(MemberIfafirm)this.baseDao.saveOrUpdate(memberIfafirm,isAdd);
		return memberIfafirm;
	}
	
	/***
     * 通过输入一个KEY，同时传递一个公司的ID，获取公司以下所有IFA数据，用于团队成员选择窗口
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public List<MemberIfa> getIFAKeyList(String ifafirmId,String keyword) {
//		String hql = " FROM IfafirmTeamIfa a INNER JOIN MemberIfa b  ON a.ifa.id=b.id WHERE  a.ifafirm.id=? and ( b.firstName like ? or b.nickName like ? or b.lastName like ? )";
		String hql = " FROM MemberIfa  b WHERE  b.id in (select f.ifa.id from MemberIfaIfafirm f where f.ifafirm.id=?) "
			+ "and ( b.firstName like ? or b.lastName like ? or b.nameChn like ? )";
		List<Object> params1=new ArrayList<Object>();
		params1.add(ifafirmId);
		params1.add("%"+keyword+"%");
		params1.add("%"+keyword+"%");
		params1.add("%"+keyword+"%");
		List<MemberIfa> list = this.baseDao.find(hql, params1.toArray(), false);
//		List<MemberIfa> returnList = new ArrayList<MemberIfa>();
//		if(!list.isEmpty())
//		{
//			for(int i=0;i<list.size();i++)
//			{
//				 list.get(i).
//				MemberIfafirm vo = new MemberIfafirm();
//				BeanUtils.copyProperties(each,vo);//拷贝信息;
//				returnList.add(each);
//			}
//		}
		return list;
	}
	
	/***
     * 通过输入一个KEY，同时传递一个公司的ID，获取公司以下所有待选IFA数据，用于团队成员选择窗口
     * @author zxtan
     * @date 2016-12-12
     */
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
     * 通过ID获取团队信息
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
     * 批量保存团队信息
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public IfafirmTeam saveOrUpdateTeam(IfafirmTeam memberIfafirm, boolean isAdd) {
		memberIfafirm=(IfafirmTeam)this.baseDao.saveOrUpdate(memberIfafirm,isAdd);
		return memberIfafirm;
	}
	
	/***
     * 通过公司与团队ID获取其所有成员数据
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public List<MemberIfa> getTeamMemberIfaByTeamId(String ifafirmId,String teamId) {
		String hql = " FROM IfafirmTeamIfa a INNER JOIN MemberIfa b  ON a.ifaId=b.id WHERE a.teamId=? AND a.ifafirmId=?";
		List<Object> params = new ArrayList<Object>();
		params.add(teamId);
		params.add(ifafirmId);
		List<MemberIfa> list = this.baseDao.find(hql, params.toArray(), false);
		return list;
	}
	
	/***
     * 保存团队成员
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public Boolean delTeamMemberIfa(String teamId,String ifafirmId)
	{
		String hql1 = " delete IfafirmTeamIfa where  teamId =? and ifafirmId = ? ";
		List<Object> params = new ArrayList<Object>();
		params.add(teamId);
		params.add(ifafirmId);
		int rs = this.baseDao.updateHql(hql1, params.toArray());
		
		
		return rs>0?true:false;
	}
	
	/***
     * IfafirmTeamIfa 删除团队成员
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public Boolean delIfafirmTeamIfa(String id)
	{
		String hql1 = " delete IfafirmTeamIfa where  id =?";
		List<Object> params = new ArrayList<Object>();
		params.add(id);
		int rs = this.baseDao.updateHql(hql1, params.toArray());
		return rs>0?true:false;
	}
	
	/***
     * 
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public IfafirmTeamIfa saveOrUpdateTeamMemberifa(IfafirmTeamIfa ifafirmTeamIfa, boolean isAdd) {
		ifafirmTeamIfa=(IfafirmTeamIfa)this.baseDao.saveOrUpdate(ifafirmTeamIfa,isAdd);
		return ifafirmTeamIfa;
	}
	
	/***
     * 通过公司ID,团队ID获取旗下所有的团队成员
     * @author 林文伟
     * @date 2016-06-29
     */
	@Override
	//@Transactional
	public JsonPaging findAllTeamIfa(JsonPaging jsonpaging, String ifafirmId ,String teamId,String keyword,String ifaid) {
		//String hql=" from MemberIfafirm r inner join SysCountry c on r.country=c.id where 1=1";
		String hql="";
		List<Object> params=new ArrayList<Object>();
		if(""==ifaid)
		{
			hql=" from MemberIfa b INNER JOIN IfafirmTeamIfa a ON a.ifa.id=b.id  where  a.team.id=? and (b.firstName like ? or b.nickName like ? or b.lastName like ? or b.nameChn like ? ) ORDER BY a.isSupervisor desc";
			params.add(teamId);
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
			//System.out.print(hql);
		}
		else
		{
			params.add(teamId);
			params.add(ifaid);
			hql=" from MemberIfa b INNER JOIN IfafirmTeamIfa a ON a.ifa.id=b.id  where a.team.id=? and a.ifa.id=?";
		}
		
		jsonpaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);
		List list = new ArrayList();
		Iterator it = jsonpaging.getList().iterator();
		//Integer indexNumber = (jsonpaging.getPage()-1)*jsonpaging.getRows();
		//Integer index = 0;
    	while(it.hasNext()){
			//index++;
    		Object obj = (Object)it.next();
    		//MemberIfa vo = new MemberIfa();
			//BeanUtils.copyProperties(obj,vo);//拷贝信息;
			
			list.add(obj);
		}
    	jsonpaging.setList(list);
		return jsonpaging;
	}
	
	
	/***
     * IfafirmTeamIfa 通过公司ID,团队ID获取旗下所有的团队成员
     * @author 林文伟
     * @date 2016-06-29
     */
	@Override
	//@Transactional
	public JsonPaging findAllTeamIfa(JsonPaging jsonpaging, String ifafirmId ,String teamId,String keywords) {
		List<Object> params=new ArrayList<Object>();
		String hql=" from MemberIfa b INNER JOIN IfafirmTeamIfa a ON a.ifa.id=b.id  where  a.team.id=? and b.id in (select ifa.id from MemberIfaIfafirm where ifafirm.id=?) ";
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
		
		return jsonpaging;
	}
	
	/***
     * 给团队添加成员
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public IfafirmTeamIfa saveOrUpdateTeamIfa(IfafirmTeamIfa ifafirmTeamIfa, boolean isAdd) {
		ifafirmTeamIfa=(IfafirmTeamIfa)this.baseDao.saveOrUpdate(ifafirmTeamIfa,isAdd);
		return ifafirmTeamIfa;
	}
	
	/***
     * 判断该成员是否存在在该团队中，如果已存在，则返回该成员实体
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public IfafirmTeamIfa checkIfaIsExistInTeam(String ifafirmId,String teamId,String ifaId) {
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
	
	/***
     * 删除团队数据，注意同时要删除子团队跟所有团队的成员
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public Boolean delIfafirmTeamInfo(String teamId)
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
	
	/***
     * 逻辑删除团队数据，注意同时要删除子团队跟所有团队的成员
     * @author michael
     * @date 2017-02-08
     */
	public Boolean delIfafirmTeamInfoLogical(String teamId)
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
				//同时删除子团队
				String hql2 = " update IfafirmTeam set isValid='0' where id =? ";
				List<Object> params2 = new ArrayList<Object>();
				params2.add(childId);
				this.baseDao.updateHql(hql2, params2.toArray());
			}
		}

		//删除团队本身
		String hql4 = " update IfafirmTeam set isValid='0' where  id =? ";
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
	
	/***
     * 启动与取消团队成员的sv操作
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public void dealSupervisor(String id)
	{
		//先获取其信息
		Object obj =  baseDao.get(IfafirmTeamIfa.class, id);
		if(obj!=null)
		{
			IfafirmTeamIfa model = (IfafirmTeamIfa)obj;
			String isSupervisor = model.getIsSupervisor();
			String hql1 = " UPDATE IfafirmTeamIfa SET isSupervisor = ? where  id =? ";
			List<Object> params1 = new ArrayList<Object>();
			if("1".equals(isSupervisor)) //如果数据是启用，则取消
			{
				//取消SV
				params1.add("0");
				params1.add(id);
			}
			else
			{
				//启用SV
				params1.add("1");
				params1.add(id);
			}
			
			this.baseDao.updateHql(hql1, params1.toArray());
		} 
	}
	
	/***
     * ifafirm的代理商列表查询的方法
     * @author 林文伟
     * @date 2016-07-12
     */
	@Override
	//@Transactional(readOnly=true)
	public JsonPaging findIfafirmDistributorList(JsonPaging jsonpaging, String ifafirmId) {
		//String hql=" from MemberIfafirm r inner join SysCountry c on r.country=c.id where 1=1";
		
		//modify wwluo 160905 增加 isValid 状态条件
		//String hql=" from MemberDistributor a INNER JOIN IfafirmDistributor b ON a.id=b.distributor.id   where b.ifafirm.id=?";
		String hql=" from MemberDistributor a INNER JOIN IfafirmDistributor b ON a.id=b.distributor.id where b.ifafirm.id=? and b.isValid=1";
		//end
		
		
		List<Object> params=new ArrayList<Object>();
		params.add(ifafirmId);
		
		jsonpaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);

		return jsonpaging;
	}
	
	/***
     * 删除公司与代理商的关联关系
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public void delIfafirmDistributorid(String id)
	{
		//modify wwluo 160905      修改为逻辑删除
		//String hql1 = " delete IfafirmDistributor  where  id =? ";
		String hql1 = " update IfafirmDistributor set isValid=0 where  id =? ";
		//end
		
		List<Object> params1 = new ArrayList<Object>();
		params1.add(id);
		
		this.baseDao.updateHql(hql1, params1.toArray());
	}
	
	/***
     * 添加公司与代理商的关联关系
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public void addIfafirmDistributorid(String distributorId,String ifafirmId)
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

	@Override
	public MemberIfafirm findById(String id) {
		MemberIfafirm memberIfafirm = (MemberIfafirm) baseDao.get(MemberIfafirm.class, id);
		return memberIfafirm;
	}

	/**
	 * 获取ifafirm 列表数据
	 * @author wwluo
	 * @date 2016-08-22
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public JsonPaging getIfaFirmJson(JsonPaging jsonPaging,String companyName ,String registerNo ,String entityType,String lang) {
		StringBuffer hql = new StringBuffer(" from MemberIfafirm where isValid='1' ");
		List params = new ArrayList();
		if(StringUtils.isNotBlank(companyName)){
			hql.append(" and companyName like ?");
			params.add("%"+companyName+"%");
		}
		if(StringUtils.isNotBlank(registerNo)){
			hql.append(" and registerNo like ?");
			params.add("%"+registerNo+"%");
		}
		if(StringUtils.isNotBlank(entityType)){
			hql.append(" and entityType=?");
			params.add(entityType);
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		if(!jsonPaging.getList().isEmpty()){
			List<MemberIfafirmVO> ifafirmVOs = new ArrayList<MemberIfafirmVO>();
			for (Object object : jsonPaging.getList()) {
				MemberIfafirm memberIfafirm = (MemberIfafirm) object;
				MemberIfafirmVO memberIfafirmVO = new MemberIfafirmVO();
				BeanUtils.copyProperties(memberIfafirm, memberIfafirmVO);
				MemberIfafirmSc memberIfafirmSc = (MemberIfafirmSc) this.baseDao.get(MemberIfafirmSc.class, memberIfafirm.getId());
				if(memberIfafirmSc != null){
					memberIfafirmVO.setCompanyNameSc(memberIfafirmSc.getCompanyName());
					if(CommonConstants.LANG_CODE_SC.equals(lang)){
						memberIfafirmVO.setCompanyName(memberIfafirmSc.getCompanyName());
					}
				}
				MemberIfafirmTc memberIfafirmTc = (MemberIfafirmTc) this.baseDao.get(MemberIfafirmTc.class, memberIfafirm.getId());
				if(memberIfafirmTc != null){
					memberIfafirmVO.setCompanyNameTc(memberIfafirmTc.getCompanyName());
					if(CommonConstants.LANG_CODE_TC.equals(lang)){
						memberIfafirmVO.setCompanyName(memberIfafirmTc.getCompanyName());
					}
				}
				MemberIfafirmEn memberIfafirmEn = (MemberIfafirmEn) this.baseDao.get(MemberIfafirmEn.class, memberIfafirm.getId());
				if(memberIfafirmEn != null){
					memberIfafirmVO.setCompanyNameEn(memberIfafirmEn.getCompanyName());
					if(CommonConstants.LANG_CODE_EN.equals(lang)){
						memberIfafirmVO.setCompanyName(memberIfafirmEn.getCompanyName());
					}
				}
				ifafirmVOs.add(memberIfafirmVO);
			}
			jsonPaging.getList().clear();
			jsonPaging.getList().addAll(ifafirmVOs);
			jsonPaging.setTotal(jsonPaging.getList().size());
		}
		return jsonPaging;
	}
	
	/**
	 * 删除firm（逻辑删除）
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
	 * 根据id获取firm多语言公司名称
	 * @author wwluo
	 * @date 2016-08-22
	 */
	public Object findCompanyNameById(String langFlag,String id) {
		if(StringUtils.isNotBlank(id)){
			if("en".equals(langFlag)){
				MemberIfafirmEn memberIfafirmEn = (MemberIfafirmEn) this.baseDao.get(MemberIfafirmEn.class, id);
				return memberIfafirmEn;
			}else if ("tc".equals(langFlag)){
				MemberIfafirmTc memberIfafirmTc = (MemberIfafirmTc) this.baseDao.get(MemberIfafirmTc.class, id);
				return memberIfafirmTc;
			}else{
				MemberIfafirmSc memberIfafirmSc = (MemberIfafirmSc) this.baseDao.get(MemberIfafirmSc.class, id);
				return memberIfafirmSc;
			}
		}
		return null;
	}

	/**
	 * 根据id获取ifafirm关联的公司（ifafirm -ifafirm）
	 * @author wwluo
	 * @date 2016-08-23
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public List findIfafirmIfafirmByid(String ifafirmId) {
		List list = new ArrayList();
		if(StringUtils.isNotBlank(ifafirmId)){
			List params =new ArrayList();
			StringBuffer hql =new StringBuffer(" from IfafirmIfafirm where isValid=1 and parent_ifafirm_id=?");	
			params.add(ifafirmId);
			List<IfafirmIfafirm> firmList = 
				this.baseDao.find( hql.toString(), params.toArray(), false );
			if(!firmList.isEmpty()){
				for (IfafirmIfafirm ifafirmIfafirm : firmList) {
					MemberIfafirmVO vo = new MemberIfafirmVO();
					BeanUtils.copyProperties(ifafirmIfafirm.getIfafirm(), vo);
					list.add(vo);
				}
			}
		}
		return list;
	}
	
	
	/**
	 * 保存公司与公司关系（add childFirm）
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
			    //DateFormat mediumDateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM);
			    //mediumDateFormat.format(new Date());
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
	 * 移除公司与公司关系（remove childFirm）
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
	
	@Override
	public MemberIfafirm findById(String id,String langCode) {
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
		MemberIfafirmSc memberIfafirmSc = new MemberIfafirmSc();
		if(StringUtils.isNotBlank(id)){
			memberIfafirmSc = (MemberIfafirmSc) baseDao.get(MemberIfafirmSc.class, id);
		}
		return memberIfafirmSc;
	}
	/**
	 * 获取ifafirm 繁体中文表
	 * @author wwluo
	 * @date 2016-08-26
	 */
	@Override
	public MemberIfafirmTc findIfafirmTcById(String id) {
		MemberIfafirmTc memberIfafirmTc = new MemberIfafirmTc();
		if(StringUtils.isNotBlank(id)){
			memberIfafirmTc = (MemberIfafirmTc) baseDao.get(MemberIfafirmTc.class, id);
		}
		return memberIfafirmTc;
	}
	/**
	 * 获取ifafirm 英文表
	 * @author wwluo
	 * @date 2016-08-26
	 */
	@Override
	public MemberIfafirmEn findIfafirmEnById(String id) {
		MemberIfafirmEn memberIfafirmEn = new MemberIfafirmEn();
		if(StringUtils.isNotBlank(id)){
			memberIfafirmEn = (MemberIfafirmEn) baseDao.get(MemberIfafirmEn.class, id);
		}
		return memberIfafirmEn;
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
	
	/**
	 * 获取ifafirm的简单信息
	 * @author mqzou
	 * @date 2016-11-24
	 * @param ifafirmId
	 * @param langCode
	 * @return
	 */
	@Override
	public MemberIfafirmBaseVO findIfafirmBase(String ifafirmId, String langCode) {
		MemberIfafirmBaseVO vo=new MemberIfafirmBaseVO();
		vo.setId(ifafirmId);
		StringBuilder hql=new StringBuilder();
		hql.append(" select i.companyName,f.filePath from "+getLangString("MemberIfafirm", langCode)+" i left join ");
		hql.append(" MemberIfafirm m on i.id=m.id left join AccessoryFile f on m.firmLogo=f.id where i.id=?");
		List<Object> params=new ArrayList<Object>();
		params.add(ifafirmId);
		List list=this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			Object[] obj=(Object[])list.get(0);
			String companyName=null!=obj[0]?obj[0].toString():"";
			String filePath=null!=obj[1]?obj[1].toString():"";
			vo.setIfafirmName(companyName);
			vo.setIconUrl(filePath);
		}
		return vo;
	}
	
	
	/***
     * IfafirmTeamIfa 通过公司ID,团队ID获取旗下所有的团队成员
     * @author 林文伟
     * @date 2016-06-29
     */
	@Override
	public JsonPaging findFirmCustomerList(JsonPaging jsonpaging, String ifafirmId ,String keyword) {
		List<Object> params=new ArrayList<Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" FROM CrmCustomer c ");
//		hql.append(" INNER JOIN MemberBase b ON c.member.id=b.id ");
		hql.append(" INNER JOIN MemberIndividual d ON c.member.id=d.member.id ");
		hql.append(" INNER JOIN MemberIfa i ON c.ifa.id=i.id ");
		hql.append(" WHERE i.ifafirm.id=? AND ( d.firstName like ? OR d.lastName like ? ) ");
		hql.append(" ORDER BY c.createTime ");
		params.add(ifafirmId);
		params.add("%"+keyword+"%");
		params.add("%"+keyword+"%");
		
		jsonpaging=this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonpaging, false);
		List list = jsonpaging.getList();
		List<IfafirmCustomerVO> voList = new ArrayList<IfafirmCustomerVO>();
		if(!list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[])list.get(i);
				CrmCustomer customer = (CrmCustomer) objs[0];
				MemberIndividual individual = (MemberIndividual) objs[1];
				MemberIfa ifa = (MemberIfa) objs[2];
				
				IfafirmCustomerVO vo = new IfafirmCustomerVO();
				vo.setId(customer.getId());
				vo.setMemberId(customer.getMember().getId());
				vo.setNickname(customer.getNickName());
				vo.setIsImportant(customer.getIsImportant());
				vo.setClientType(customer.getClientType());
				if(null != individual){
					String fullName = individual.getFirstName() + " " + individual.getLastName();
					vo.setFullName( fullName );
				}
				if(null != ifa){
					String ifaName = ifa.getFirstName() + " " + ifa.getLastName();
					vo.setIfaName( ifaName );
				}
				voList.add(vo);
			}			
		}
		
		jsonpaging.setList(voList);
		
		return jsonpaging;
	}
	
	/***
     * IfafirmTeamIfa 通过公司ID,团队ID获取旗下所有的团队成员
     * @author 林文伟
     * @date 2016-06-29
     */
	@Override
	public JsonPaging findFirmAccountList(JsonPaging jsonpaging, String ifafirmId ,String keyword,String status) {
		List<Object> params=new ArrayList<Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" FROM InvestorAccount c ");
//		hql.append(" INNER JOIN MemberBase b ON c.member.id=b.id ");
		hql.append(" INNER JOIN MemberIndividual d ON c.member.id=d.member.id ");
		hql.append(" INNER JOIN MemberIfa i ON c.ifa.id=i.id ");
		hql.append(" WHERE c.finishStatus='1' AND i.ifafirm.id=? AND ( d.firstName like ? OR d.lastName like ? ) ");

		params.add(ifafirmId);
		params.add("%"+keyword+"%");
		params.add("%"+keyword+"%");
		if(!"".equals(status)){
			if("1,2,3".indexOf(status) >-1){
				hql.append(" and c.flowStatus = ? ");
				params.add(status);
			}else if("4".equals(status)) {
				hql.append(" and ( c.flowStatus = '0' or c.flowStatus = '4' ) ");
			}
		}
		hql.append(" ORDER BY c.createTime ");
		
		jsonpaging=this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonpaging, false);
		List list = jsonpaging.getList();
		List<IfafirmAccountVO> voList = new ArrayList<IfafirmAccountVO>();
		if(!list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[])list.get(i);
				InvestorAccount customer = (InvestorAccount) objs[0];
//				MemberBase member = (MemberBase) objs[1];
				MemberIndividual individual = (MemberIndividual) objs[1];
				MemberIfa ifa = (MemberIfa) objs[2];
				
				IfafirmAccountVO vo = new IfafirmAccountVO();
				vo.setId(customer.getId());
				vo.setMemberId(customer.getMember().getId());
				vo.setNickname(customer.getMember().getNickName());
				vo.setAccountNo(customer.getAccountNo());
				vo.setFlowStatus(customer.getFlowStatus());
				vo.setCreateTime(DateUtil.dateToDateString(customer.getCreateTime(), "yyyy-MM-dd"));
				if(null != individual){
					String fullName = individual.getFirstName() + " " + individual.getLastName();
					vo.setFullName( fullName );					
				}
				if(null != ifa){
					String ifaName = ifa.getFirstName() + " " + ifa.getLastName();
					vo.setIfaName( ifaName );
				}
				voList.add(vo);
			}			
		}
		
		jsonpaging.setList(voList);
		
		return jsonpaging;
	}
	
	/**
	 * 获取ifa的ifafirm
	 * @author mqzou
	 * @date 2016-11-26
	 * @param ifa
	 * @return
	 */
	@Override
	public MemberIfafirm findIfafirmByIfa(MemberIfa ifa, String langCode) {
		MemberIfafirm ifafirm=null;
		List<Object> params=new ArrayList<Object>();
		StringBuilder hql=new StringBuilder();
		hql.append(" select r,m.companyName from  MemberIfaIfafirm r left join  "+getLangString("MemberIfafirm", langCode)+"  m on r.ifafirm.id=m.id");
		hql.append(" left join MemberIfa i on r.ifa.id=i.id");
		hql.append(" where r.isValid='1' and r.checkStatus='1'");
		if(null!=ifa){
			hql.append(" and i.member.id=?");
			params.add(ifa.getMember().getId());
		}
		List list=this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=list && !list.isEmpty()){
				Object[] object = (Object[]) list.get(0);
				MemberIfaIfafirm  ifaIfafirm=(MemberIfaIfafirm)object[0];
				String companyName=null!=object[1]?object[1].toString():"";
				 ifafirm=ifaIfafirm.getIfafirm();
				ifafirm.setCompanyName(companyName);
			
		}
		return ifafirm;
	}
	
	/**
	 * 通过团队编码获取记录
	 * @param code
	 * @return
	 */
	public IfafirmTeam findTeamByCode(String code) {
		String hql = " FROM IfafirmTeam a  WHERE  a.isValid='1' and a.code=? ";
		List<Object> params = new ArrayList<Object>();
		params.add(code);
		
		List list = this.baseDao.find(hql, params.toArray(), false);
		
		if(null!=list && !list.isEmpty())
		{
			return (IfafirmTeam)list.get(0);
		}
		return null;
	}
	
	/**
	 * 检查团队编码是否已被使用
	 * @param code
	 * @return
	 */
	public boolean checkTeamIfExist(String code) {
		String hql = " FROM IfafirmTeam a  WHERE  a.isValid='1' and a.code=? ";
		List<Object> params = new ArrayList<Object>();
		params.add(code);
		
		List list = this.baseDao.find(hql, params.toArray(), false);
		
		if(!list.isEmpty())
		{
			return true;
		}
		return false;
	}

	/**
	 * 获取该member所有IFA
	 * @date 2016-11-26
	 * @author wwluo
	 * @param memberId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MemberIfa> getAllIfabyMember(String memberId) {
		List<MemberIfa> memberIfas = null;
		if(StringUtils.isNotBlank(memberId)){
			StringBuffer hql = new StringBuffer("" +
					" SELECT i FROM" +
					" MemberIfa i" +
					" LEFT JOIN" +
					" CrmCustomer c" +
					" ON" +
					" i.id=c.ifa.id" +
					" WHERE" +
					" c.member.id=?" +
					"");
			List<Object> params = new ArrayList<Object>();
			params.add(memberId);
			memberIfas = this.baseDao.find(hql.toString(), params.toArray(), false);
		}
		return memberIfas;
	}

	/**
	 * 获取该公司所有客户
	 * @date 2017-03-17
	 * @author wwluo
	 * @param ifafirmId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MemberVO> getClientsByIfafirm(String ifafirmId, String langCode) {
		List<MemberVO> vos = null;
		if (StringUtils.isNotBlank(ifafirmId)) {
			StringBuffer hql = new StringBuffer(""
					+ " SELECT"
					+ " c.member"
					+ " FROM"
					+ " CrmCustomer c"
					+ " LEFT JOIN"
					+ " MemberIfaIfafirm mi"
					+ " ON"
					+ " mi.ifa.id=c.ifa.id"
					+ " WHERE"
					+ " mi.isValid=1"
					+ " AND"
					+ " c.isValid=1"
					+ " AND"
					+ " CURDATE() >= mi.beginDate"
					+ " AND"
					+ " CURDATE() <= mi.endDate"
					+ " AND"
					+ " mi.ifafirm.id=?");
			List<Object> params = new ArrayList<Object>();
			params.add(ifafirmId);
			List<MemberBase> memberBases = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(memberBases != null && !memberBases.isEmpty()){
				vos = new ArrayList<MemberVO>();
				for (MemberBase memberBase : memberBases) {
					MemberVO vo = new MemberVO();
					vo.setId(memberBase.getId());
					vo.setIconUrl(PageHelper.getUserHeadUrl(memberBase.getIconUrl(), null));
					List<MemberIndividual> individuals = this.baseDao.find("FROM MemberIndividual t WHERE t.member.id=?", new String[]{memberBase.getId()}, false);
					if(individuals != null && !individuals.isEmpty()){
						MemberIndividual individual = individuals.get(0);
						String name = null;
						if(CommonConstants.LANG_CODE_EN.equals(langCode)){
				    		name = individual.getLastName() + " " + individual.getFirstName();
				    	}else {
							name = individual.getNameChn();
						}
				    	if(StringUtils.isBlank(name)){
				    		name = individual.getMember().getNickName();
				    	}
				    	if(StringUtils.isBlank(name)){
				    		name = individual.getMember().getLoginCode();
				    	}
				    	vo.setName(name);
					}
					vos.add(vo);
				}
			}
		}
		return vos;
	}

	/**
	 * 获取该公司所有投资顾问
	 * @date 2017-03-17
	 * @author wwluo
	 * @param memberId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MemberVO> getIfasByIfafirm(String ifafirmId,String langCode) {
		List<MemberVO> vos = null;
		if (StringUtils.isNotBlank(ifafirmId)) {
			StringBuffer hql = new StringBuffer(""
					+ " SELECT"
					+ " i"
					+ " FROM"
					+ " MemberIfa i"
					+ " LEFT JOIN"
					+ " MemberIfaIfafirm mi"
					+ " ON"
					+ " mi.ifa.id=i.id"
					+ " WHERE"
					+ " mi.isValid=1"
					+ " AND"
					+ " CURDATE() >= mi.beginDate"
					+ " AND"
					+ " CURDATE() <= mi.endDate"
					+ " AND"
					+ " mi.ifafirm.id=?");
			List<Object> params = new ArrayList<Object>();
			params.add(ifafirmId);
			List<MemberIfa> memberIfas = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(memberIfas != null && !memberIfas.isEmpty()){
				vos = new ArrayList<MemberVO>();
				for (MemberIfa ifa : memberIfas) {
					MemberVO vo = new MemberVO();
					if(ifa.getMember() != null){
						vo.setId(ifa.getMember().getId());
						vo.setIconUrl(PageHelper.getUserHeadUrl(ifa.getMember().getIconUrl(), null));
					}
					String name = null;
			    	if(CommonConstants.LANG_CODE_EN.equals(langCode)){
			    		name = ifa.getLastName() + " " + ifa.getFirstName();
			    	}else {
						name = ifa.getNameChn();
					}
			    	if (StringUtils.isBlank(name) && ifa.getMember() != null) {
			    		name = ifa.getMember().getNickName();
					}
			    	if (StringUtils.isBlank(name) && ifa.getMember() != null) {
			    		name = ifa.getMember().getLoginCode();
			    	}
			    	vo.setName(name);
					vos.add(vo);
				}
			}
		}
		return vos;
	}
	
	/**
	 * 获取ifafirm下所有有效的ifa
	 * @author michael
	 * @date 2016-12-13
	 */
	public List findIfaByIfaFirm(IfaVO ifaVo, String langCode) {
		String hql = "select distinct i from MemberIfa i ";
		hql += " LEFT JOIN MemberIfaIfafirm ifm on ifm.ifa.id=i.id ";
		hql += " LEFT JOIN MemberBase b on i.member.id=b.id ";
		hql += " where ifm.ifafirm.id =? and (current_date()<=ifm.endDate or ifm.endDate is null) ";
		List<String> parms = new ArrayList<String>();
		parms.add(ifaVo.getIfafirmId());
		if (null != ifaVo && StringUtils.isNoneBlank(ifaVo.getKeyword())) {
			hql += " and (b.nickName like ? or b.loginCode like ? or i.nameChn like ?)";
			parms.add("%"+ifaVo.getKeyword()+"%");
			parms.add("%"+ifaVo.getKeyword()+"%");
			parms.add("%"+ifaVo.getKeyword()+"%");
		}
		if(StringUtils.isNoneBlank(ifaVo.getCheckStatus())){
			hql+=" and ifm.checkStatus = ? ";
			parms.add(ifaVo.getCheckStatus());
		}
		List list = this.baseDao.find(hql, parms.toArray(), false);
		return list;
	}

	//modyfy by rqwang 20170626
	@Override
	public JsonPaging findIfafirmDisList(JsonPaging jsonPaging,
			String ifafirmId, MemberDistributor disInfo,HttpServletRequest request) {
		String lang = this.getLoginLangFlag(request);
		String langues = this.getLangFirstCharUpper(lang);
		StringBuffer sbHql = new StringBuffer();
		sbHql.append(" from MemberDistributor a INNER JOIN IfafirmDistributor b ON a.id=b.distributor.id ");
		sbHql.append(" left join SysCountry s on s.id=a.incorporationPlace ");
		sbHql.append(" where b.ifafirm.id=? ");
		List<Object> params=new ArrayList<Object>();
		params.add(ifafirmId);
		
		if(null!=disInfo.getCompanyName() &&! "".equals(disInfo.getCompanyName())){
			sbHql.append(" and a.companyName like '%" + disInfo.getCompanyName()+"%' ");
		}
		
		jsonPaging=this.baseDao.selectJsonPaging(sbHql.toString(), params.toArray(), jsonPaging, false);

		List jsonList = jsonPaging.getList();
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
	    		String logoUrlParse = PageHelper.getLogoUrl(distributor.getLogofile(), "D");
	    		vo.setDistributorLogo(logoUrlParse);
	    		vo.setEntityType(distributor.getEntityType());
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
		
	}
	
	/**
	 *  获取与ifafirm关联的所有代理商
	 * @author mqzou 2017-06-15
	 * @param ifafirmId
	 * @param langCode
	 * @return
	 */
	@Override
	public List<DistributorSimpleVO> findAllDistributorList(String ifafirmId) {
		StringBuilder hql=new StringBuilder();
		hql.append("select a from MemberDistributor a INNER JOIN IfafirmDistributor b ON a.id=b.distributor.id ");
		hql.append(" where b.ifafirm.id=? ");
		List<Object> params=new ArrayList<Object>();
		params.add(ifafirmId);
		List resultList=baseDao.find(hql.toString(), params.toArray(), false);
		List<DistributorSimpleVO> list=new ArrayList<DistributorSimpleVO>();
		if(null!=resultList && !resultList.isEmpty()){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				MemberDistributor distributor = (MemberDistributor) it.next();
				DistributorSimpleVO vo=new DistributorSimpleVO();
				vo.setId(distributor.getId());
				vo.setIconUrl(distributor.getLogofile());
				vo.setDistributorName(distributor.getCompanyName());
				list.add(vo);
			}
		}
		return list;
	}
}
