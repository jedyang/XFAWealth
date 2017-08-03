package com.fsll.wmes.web.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PageHelper;
import com.fsll.core.vo.SysCountryVO;
import com.fsll.wmes.entity.DistributorOrg;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.web.service.CountryService;
import com.fsll.wmes.web.service.DistributorService;
import com.fsll.wmes.web.vo.MemberDistributorExtend;


/***
 * 业务接口实现类：工作流管理接口类
 * @author 林文伟
 * @date 2016-06-22
 */
@Service("distributorService")
//@Transactional
public class DistributorServiceImpl extends BaseService implements DistributorService {
	
	@Autowired
	private CountryService countryService;

	/***
     * 代理商列表查询的方法
     * @author 林文伟
     * @date 2016-07-11
     */
	@Override
	//@Transactional(readOnly=true)
	public JsonPaging findAll(JsonPaging jsonpaging, MemberDistributor info,String langCode) {
		String hql="  from MemberDistributor r  where 1=1";
		List<Object> params=new ArrayList<Object>();
		if(null!=info.getCompanyName()&&!"".equals(info.getCompanyName())){
			hql+=" and r.companyName like '%"+info.getCompanyName()+"%'";
		}
		if(null!=info.getEntityType()&&!"".equals(info.getEntityType())){
			hql+=" and r.entityType = '"+info.getEntityType()+"'";
		}
		if(null!=info.getIncorporationPlace()&&!"".equals(info.getIncorporationPlace())){
			hql+=" and r.incorporationPlace = '"+info.getIncorporationPlace()+"'";
		}
		if(null!=info.getRegisterNo()&&!"".equals(info.getRegisterNo())){
			hql+=" and r.registerNo like '%"+info.getRegisterNo()+"%'";
		}

		jsonpaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);
		List<MemberDistributorExtend> list = new ArrayList<MemberDistributorExtend>();
		Iterator it = jsonpaging.getList().iterator();
    	while(it.hasNext()){
    		MemberDistributor obj = (MemberDistributor)it.next();
    		MemberDistributorExtend vo = new MemberDistributorExtend();
    		String countryId = obj.getIncorporationPlace();
    		if(StringUtils.isNoneBlank(countryId)){
    			SysCountryVO sysCountyVo = countryService.findById(countryId);
        		String incorporationPlaceName = sysCountyVo.getCountryName(langCode);
        		vo.setincorporationPlaceName(incorporationPlaceName);
    		}
    		String logoUrlParse = PageHelper.getLogoUrl(obj.getLogoFile(), "D");
			//vo.setDistributorLogo(logoUrlParse);
			BeanUtils.copyProperties(obj,vo);//拷贝信息;
			vo.setDistributorLogo(logoUrlParse);
			list.add(vo);
		}
    	jsonpaging.setList(list);
		return jsonpaging;
	}
	
	/***
     * 获取代理商的组织架构数据
     * @author 林文伟
     * @date 2016-06-29
     */
	@Override
	//@Transactional(readOnly=true)
	public List<DistributorOrg> findDistributorOrgAll(String distributorId) {
		
		List<DistributorOrg> teamlist = new ArrayList<DistributorOrg>();
		//先获取所有层节点
		//String hql=" from DistributorOrg r  where r.distributor.id=? order by r.orderBy asc";
		//List<Object> params=new ArrayList<Object>();
		//params.add(distributorId);
		//List<DistributorOrg> alllist=this.baseDao.find(hql, params.toArray(), false);

		
		return teamlist;
	}
	
	/**
	 * 通过id获取代理商主数据
	 * @param id
	 * @return
	 */
	//@Transactional(readOnly = true)
	public MemberDistributor findDistributorById(String id){
		if(StringUtils.isBlank(id)){
			return null;
		}
		MemberDistributor memberDistributor = (MemberDistributor) baseDao.get(MemberDistributor.class, id);
		return memberDistributor;
	}

	/**
	 * 获取所有的代理商
     * @author mqzou
     * @date 2016-07-19
	 * @return
	 */
	@Override
	public List<MemberDistributor> findAllDistributor() {
		String hql="from MemberDistributor r ";
		List<MemberDistributor> list=this.baseDao.find(hql, null, false);
		return list;
	}
	
	/***
     * 保存代理商信息
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public MemberDistributor saveOrUpdate(MemberDistributor memberDistributor) {
		memberDistributor=(MemberDistributor)this.baseDao.saveOrUpdate(memberDistributor);
		return memberDistributor;
	}
	
	/***
     * 通过ID获取单个代理商信息
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public MemberDistributor getMemberDistributor(String id) {
		Object obj = (MemberDistributor) baseDao.get(MemberDistributor.class, id);
		if(obj!=null)
		{
			return (MemberDistributor)obj;
		} else return null;
	}
	
	/***
     * 删除代理商
     * @author 林文伟
     * @date 2016-0-20
     */
	@Override
	public int delDistributor(String id) {
		
		String hql = " delete MemberDistributor where  id=? ";
		List<Object> params = new ArrayList<Object>();
		params.add(id);
		return this.baseDao.updateHql(hql, params.toArray());
	}
	
	/***
     * 获取管理员
     * @author 林文伟
     * @date 2016-06-29
     */
	@Override
	//@Transactional(readOnly=true)
	public MemberAdmin findDistributorMemberAdmin(String memberId) {
		
		//先获取所有层节点
		String hql=" from MemberAdmin r  where r.member.id=? ";
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		List<MemberAdmin> list=this.baseDao.find(hql, params.toArray(), false);
		if(list.isEmpty())return null;
		else
		{
			MemberAdmin model = list.get(0);
			return model;
		}
	}
}