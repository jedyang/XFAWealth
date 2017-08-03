package com.fsll.wmes.distributor.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.distributor.service.MyDistributorService;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.member.vo.MemberAdminVO;


@Service("myDistributorService")
public class MyDistributorServiceImpl extends BaseService implements MyDistributorService {
	
	/**
	 * 获取distributor管理员历表
	 * @author qgfeng
	 * @date 2016-12-2
	 * 
	 */
	@Override
	public JsonPaging findDisMember(JsonPaging jsonPaging,MemberAdminVO adminVo) {
		if(adminVo==null){
			return jsonPaging;
		}
		String hql = " from MemberAdmin r where r.type='2' and r.distributor.id=? ";
		List<String> params = new ArrayList<String>();
		params.add(adminVo.getDistributorId());
		if(StringUtils.isNotBlank(adminVo.getKeyword())){
			hql += " and (r.member.loginCode like ? or r.member.nickName like ?)";
			params.add("%"+adminVo.getKeyword()+"%");
			params.add("%"+adminVo.getKeyword()+"%");
		}
		if(StringUtils.isNotBlank(adminVo.getIsValid())){
			hql += " and r.member.isValid = ? ";
			params.add(adminVo.getIsValid());
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		List<MemberAdminVO> list = new ArrayList<MemberAdminVO>();
		Iterator<MemberAdmin> it = (Iterator) jsonPaging.getList().iterator();
		while (it.hasNext()) {
			MemberAdmin entity = (MemberAdmin) it.next();
			if(entity.getMember()!=null){
				MemberAdminVO vo = new MemberAdminVO();
				vo.setId(entity.getId());
				vo.setParentId(null!=entity.getParent()?entity.getParent().getId():"");
				vo.setParentName(null!=entity.getParent()?entity.getParent().getMember().getNickName():"");
				vo.setLoginCode(entity.getMember().getLoginCode());
				vo.setMemberId(entity.getMember().getId());
				vo.setType(entity.getType());
				vo.setIsValid(entity.getMember().getIsValid());
				vo.setNickName(entity.getMember().getNickName());
				vo.setCreateTime(DateUtil.getDateStr(entity.getMember().getCreateTime()));
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	/**
	 * 查找 distributorMember
	 * @author qgfeng
	 * @date 2016-12-28
	 */
	@Override
	public MemberBase getDisMemberById(String id) {
		String hql = "select r.member from MemberAdmin r where r.id=?";
		List<String> params = new ArrayList<String>();
		params.add(id);
		MemberBase base = (MemberBase) baseDao.getUniqueResult(hql, params.toArray(), false);
		return base;
	}
	/**
	 * 根据memberBase ID 及firmId查找
	 * @author qgfeng
	 * @date 2016-12-28
	 * @param disId 代理公司id
	 * @param memberId 人员id
	 */
	@Override
	public MemberAdmin getDisMemberByMid(String disId, String memberId) {
		if(StringUtils.isBlank(disId) || StringUtils.isBlank(memberId)){
			return null;
		}
		String hql = " from MemberAdmin r where r.distributor.id=? and r.member.id=?";
		List<String> params = new ArrayList<String>();
		params.add(disId);
		params.add(memberId);
		List<MemberAdmin> list = baseDao.find(hql, params.toArray(),false);
		if(list != null && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 保存distributor Member
	 * @author qgfeng
	 * @date 2016-12-28
	 */
	@Override
	public MemberAdmin saveOrUpdateDisMember(MemberAdmin disMember) {
		if(disMember==null || disMember.getMember()==null || disMember.getDistributor()==null){
			return null;
		}
		MemberBase member = (MemberBase) baseDao.saveOrUpdate(disMember.getMember());
		if(member != null){
			disMember.setMember(member);
			disMember = (MemberAdmin) baseDao.saveOrUpdate(disMember);
			return disMember;
		}
		return null;
	}
	/**
	 * 删除distributor Member 级联删除MemberBase
	 * @author qgfeng
	 * @date 2016-12-28
	 * @param ids 代理商ids
	 */
	@Override
	public boolean deleteDistByIds(String ids) {
		if(StringUtils.isBlank(ids)){
			return false;
		}
		String[] idArry = ids.split(",");
		if(idArry != null && idArry.length>0){
			for(String id : idArry){
				if(StringUtils.isBlank(id)) continue;
				MemberAdmin admin = (MemberAdmin) baseDao.get(MemberAdmin.class, id);
				if(admin!=null){
					if(admin.getMember() != null){
						baseDao.delete(admin.getMember());
					}
					baseDao.delete(admin);
				}
			}
			return true;
		}
		return false;
	}
}
