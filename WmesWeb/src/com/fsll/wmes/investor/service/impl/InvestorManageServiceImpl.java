package com.fsll.wmes.investor.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysCountry;

import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberCorporate;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.investor.service.InvestorManageService;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.vo.IndividualVO;
/***
 * 业务接口实现类：investor管理接口类
 * @author mqzou
 * @date 2016-06-22
 */
@Service("investorManageService")
//@Transactional
public class InvestorManageServiceImpl extends BaseService implements InvestorManageService {

	@Autowired
	private	 MemberBaseService memberBaseService;
	
	@Override
	public List<IndividualVO> getIndividualList(MemberIndividual individualVO,
			int startIndex, int pageSize) {
		String sql=" select a.id,a.nickName, a.firstName,a.lastName,a.member.loginCode,a.gender from MemberIndividual a ";
		List params = new ArrayList();
		if(!"".equals(individualVO.getMember().getLoginCode())){
			sql+=" and a.member.loginCode like '%"+individualVO.getMember().getLangCode()+"%'";
		}
		/*if(!individualVO.getLastName().equals("")){
			sql+=" and b.last_name like '%"+individualVO.getLastName()+"%'";
		}*/
		sql+=" order by a.member.loginCode ";
		
		List list=this.baseDao.find(sql, params.toArray(), false);
		List<IndividualVO> voliList=new ArrayList<IndividualVO>();
		if(!list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				IndividualVO vo=new IndividualVO();
				Object[] objs = (Object[])list.get(i);
				vo.setId(objs[0]==null?"":objs[0].toString());
				vo.setNickName(objs[1]==null?"":objs[1].toString());
				vo.setFirstName(objs[2]==null?"":objs[2].toString());
				vo.setLastName(objs[3]==null?"":objs[3].toString());
				vo.setLoginCode(objs[4]==null?"":objs[4].toString());
				String genderString=objs[5]==null?"":objs[5].toString();
				vo.setGenderValue("F".equals(genderString)?"女":"男");
				voliList.add(vo);				
			}
		}
		return voliList;
	}

	@Override
	public JsonPaging findAllIndividual(JsonPaging jsonpaging,
			MemberIndividual memberIndividual) {
		String hql=" from MemberIndividual r where 1=1";
		List params = new ArrayList();
		/*String sql=" select r.id,r.nickName, r.firstName,r.lastName,r.member.loginCode,r.gender from MemberIndividual r ";
		
		if(!memberIndividual.getMember().getLoginCode().equals("")){
			sql+=" and r.member.loginCode like '%"+memberIndividual.getMember().getLangCode()+"%'";
		}*/
		/*if(!individualVO.getLastName().equals("")){
			sql+=" and b.last_name like '%"+individualVO.getLastName()+"%'";
		}*/
		

		jsonpaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);
		List list = new ArrayList();
		Iterator it = jsonpaging.getList().iterator();
		//Integer indexNumber = (jsonpaging.getPage()-1)*jsonpaging.getRows();
		//Integer index = 0;
    	while(it.hasNext()){
			//index++;
    		
    		MemberIndividual obj = (MemberIndividual)it.next();
			
			list.add(obj);
		}
    	jsonpaging.setList(list);
		return jsonpaging;

	}

	@Override
	public JsonPaging findAllCorporate(JsonPaging jsonpaging,
			MemberCorporate memberCorporate) {
		String hql=" from MemberCorporate r where 1=1";
		List params = new ArrayList();

		jsonpaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);
		List list = new ArrayList();
		Iterator it = jsonpaging.getList().iterator();
		//Integer indexNumber = (jsonpaging.getPage()-1)*jsonpaging.getRows();
		//Integer index = 0;
    	while(it.hasNext()){
			//index++;
    		
    		MemberCorporate obj = (MemberCorporate)it.next();
			
			list.add(obj);
		}
    	jsonpaging.setList(list);
		return jsonpaging;
		//return null;
	}

	@Override
	public MemberIndividual findIndividualById(String id) {
		MemberIndividual vo = (MemberIndividual) baseDao.get(MemberIndividual.class, id);
		return vo;
	}

	/**
	 * modify by mqzou 2017-02-07 修改IM端的信息
	 */
	@Override
	public MemberIndividual saveIndividual(MemberIndividual memberIndividual,MemberBase memberBase) {
		// TODO Auto-generated method stub
		memberBase.setImNickName(memberBase.getNickName());
		memberBase.setImNickUrl("");
		if(null==memberBase.getImUserId() || "".equals(memberBase.getImUserId())){
			memberBase.setImUserId(UUID.randomUUID().toString());
			memberBase.setImUserPwd(memberBase.getImUserId());
		}
		
		if(null == memberIndividual.getId() || "".equals(memberIndividual.getId())){
			if(null == memberBase.getId() || "".equals(memberBase.getId())){
			     memberBase.setId(null);
			     memberBase.setCreateTime(new Date());
			     memberBase=(MemberBase)baseDao.create(memberBase);
			}
			memberIndividual.setMember(memberBase);
			memberIndividual.setId(null);
			memberIndividual.setCreateTime(new Date());
			memberIndividual = (MemberIndividual)baseDao.create(memberIndividual);
			memberBaseService.saveMemberToIm(memberBase, true);
		}else{
			memberIndividual = (MemberIndividual)baseDao.update(memberIndividual);
			memberBase=(MemberBase)baseDao.update(memberBase);
			memberBaseService.saveMemberToIm(memberBase, false);
		}
		return memberIndividual;
	}

	@Override
	public SysCountry findCountryById(String id) {
		 SysCountry vo=(SysCountry)baseDao.get(SysCountry.class, id);
		 return vo;
	}


	

}
