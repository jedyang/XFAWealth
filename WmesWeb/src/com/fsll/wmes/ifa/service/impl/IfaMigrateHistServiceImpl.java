package com.fsll.wmes.ifa.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.StrUtils;
import com.fsll.wmes.entity.IfaMigrateHist;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.ifa.service.IfaMigrateHistService;

/**
 * IFA客户数据迁移接口实现
 * @author michael
 * @date 2017-3-1
 */
@Service("ifaMigrateHistService")
public class IfaMigrateHistServiceImpl extends BaseService implements IfaMigrateHistService {
	/**
	 * 增加或者修改一条数据
	 * @param hist 帐号信息
	 * @return 
	 */
	public IfaMigrateHist saveOrUpdate(IfaMigrateHist hist){
		return (IfaMigrateHist)baseDao.saveOrUpdate(hist);
	}

	/**
	 * 删除多条数据
	 * @param ids
	 */
	public boolean delete(String ids){
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
	 * 通过ID删除一条记录
	 * @param id
	 * @return
	 */
	private boolean deleteById(String id){
		if (id == null) {
			return false;
		}else{
			IfaMigrateHist hist = findById(id);
			if(hist != null){
				baseDao.delete(hist);
				return true;
			}else{
				return false;
			}
		}
	}
	
	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	public IfaMigrateHist findById(String id){
		return (IfaMigrateHist) baseDao.get(IfaMigrateHist.class, id);
	}
	
	 /***
     * 分页查询记录
     * @param jsonPaging 分页参数
     * @param hist 查询参数
	 * @return
     */
	public JsonPaging findAll(JsonPaging jsonPaging, IfaMigrateHist hist){
		String hql=" from IfaMigrateHist r where 1=1 ";
		List<Object> params=new ArrayList<Object>();
		if(null!=hist.getStatus() && !StrUtils.isEmpty(hist.getStatus())){
			hql+=" and r.status = ? ";
			params.add(hist.getStatus());
		}
		if(null!=hist.getStatus() && !StrUtils.isEmpty(hist.getStatus())){
			hql+=" and r.dataType = ? ";
			params.add(hist.getDataType());
		}
		if(null!=hist.getFromMember() && !StrUtils.isEmpty(hist.getFromMember().getId())){
			hql+=" and r.fromMember.id = ? ";
			params.add(hist.getFromMember().getId());
		}
		if(null!=hist.getToMember() && !StrUtils.isEmpty(hist.getToMember().getId())){
			hql+=" and r.toMember.id = ? ";
			params.add(hist.getToMember().getId());
		}
		if(null!=hist.getCusMember() && !StrUtils.isEmpty(hist.getCusMember().getId())){
			hql+=" and r.cusMember.id = ? ";
			params.add(hist.getCusMember().getId());
		}
		if(null!=hist.getCreateBy() && !StrUtils.isEmpty(hist.getCreateBy().getId())){
			hql+=" and r.createBy.id = ? ";
			params.add(hist.getCreateBy().getId());
		}
		if(null!=hist.getCreateTime()){
			hql+=" and r.createTime >= ? ";
			params.add(hist.getCreateTime());
		}

		jsonPaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		return jsonPaging;
	}

}
