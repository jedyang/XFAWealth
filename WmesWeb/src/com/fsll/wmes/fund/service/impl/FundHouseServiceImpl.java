/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.fund.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.StrUtils;
import com.fsll.wmes.entity.FundHouse;
import com.fsll.wmes.entity.FundHouseEn;
import com.fsll.wmes.entity.FundHouseSc;
import com.fsll.wmes.entity.FundHouseTc;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.fund.service.FundHouseService;
import com.fsll.wmes.fund.vo.FundHouseDataVO;
import com.fsll.wmes.member.service.MemberDistributorService;

/***
 * 业务接口实现类：基金公司
 * @author zxtan
 * @date 2016-10-21
 */
@Service("fundHouseService")
//@Transactional
public class FundHouseServiceImpl extends BaseService implements FundHouseService {

	@Override
	public String getNameList(String idList,String langCode) {
		// TODO Auto-generated method stub
		String nameList = "";
		String idString = StrUtils.seperateWithQuote(idList,"'");
		String hql = "select r.id,r.houseName,r.pinYin from FundHouse l inner join "+getLangString("FundHouse", langCode)+" r on l.id=r.id where l.id in ("+idString+") ";
		List list = this.baseDao.find(hql, null, false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[])list.get(i);
				if("".equals(nameList) ){
					nameList = objs[1].toString();
				}else {
					nameList += "," + objs[1].toString();
				}	
			}
		}
				
		return nameList;
	}
	
	/**
	 * 删除其他关联记录
	 * @param id
	 */
	private void deleteRelate(String id){
		FundHouseEn fundHouseEn = findFundHouseEnById(id);
		if(fundHouseEn != null){
			baseDao.delete(fundHouseEn);
		}
		FundHouseSc fundHouseSc = findFundHouseScById(id);
		if(fundHouseSc != null){
			baseDao.delete(fundHouseSc);
		}
		FundHouseTc fundHouseTc = findFundHouseTcById(id);
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
			FundHouse fundHouse = findFundHouseById(id);
			if(fundHouse != null){
				deleteRelate(id);
				baseDao.delete(fundHouse);
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
	 * 获得一条记录的详细信息
	 * @param CompanyInfo
	 * @return
	 
	private FundHouse findDetail(FundHouse fundHouse){	
		return fundHouse;
	}*/
	
	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	//@Transactional(readOnly=true)
	public FundHouseDataVO findById(String id){
		FundHouseDataVO result = new FundHouseDataVO();
		FundHouse fundHouse = findFundHouseById(id);
		result.setId(fundHouse.getId());
		result.setCreateTime(fundHouse.getCreateTime());
		result.setLastUpdate(fundHouse.getLastUpdate());
		result.setIsValid(fundHouse.getIsValid());
		result.setFundHouseEn(findFundHouseEnById(id));
		result.setFundHouseSc(findFundHouseScById(id));
		result.setFundHouseTc(findFundHouseTcById(id));
		return result;
	}
	
	@Override
	public FundHouse findFundHouseById(String id) {
		FundHouse fundHouse = (FundHouse) baseDao.get(FundHouse.class, id);
		return fundHouse;
	}

	@Override
	public FundHouseEn findFundHouseEnById(String id) {
		FundHouseEn fundHouseEn = (FundHouseEn) baseDao.get(FundHouseEn.class, id);
		return fundHouseEn;
	}

	@Override
	public FundHouseSc findFundHouseScById(String id) {
		FundHouseSc fundHouseSc = (FundHouseSc) baseDao.get(FundHouseSc.class, id);
		return fundHouseSc;
	}

	@Override
	public FundHouseTc findFundHouseTcById(String id) {
		FundHouseTc fundHouseTc = (FundHouseTc) baseDao.get(FundHouseTc.class, id);
		return fundHouseTc;
	}
	
    /***
     * 分页查询记录
     * @param jsonpaging 分页参数
     * @param fundHouse 查询参数
	 * @return
     */
	@Override
	//@Transactional(readOnly=true)
	public JsonPaging findAll(JsonPaging jsonpaging, String houseName,String startDate,String endDate,String langCode) {
		String hql=" FROM FundHouse r ";
			hql += " INNER JOIN " + this.getLangString("FundHouse", langCode);
			hql += " f ON r.id=f.id ";
			hql += " WHERE 1=1 ";
		List<Object> params=new ArrayList<Object>();
		if(!("").equals(houseName)&&houseName!=null){
			hql += " AND f.houseName LIKE ? ";
			params.add("%" + houseName + "%");
		}
		
		//时间段的查询 开始

		Date startTime = DateUtil.getDate(startDate, "yyyy-MM-dd HH:mm:ss");
		Date endTime = DateUtil.getDate(endDate, "yyyy-MM-dd HH:mm:ss");;
		if (startTime!=null && (endTime==null)){
			hql += " AND r.createTime >= ? ";
			params.add(startTime);
		}
		if (endTime!=null && (startTime==null)){
			hql += " AND r.createTime <= ? ";
			params.add(endTime);
		}
		if(startTime!=null && endTime!=null){				
			hql += " AND (r.createTime BETWEEN ? AND ?) ";
			params.add(startTime);
			params.add(endTime);
		}
		//时间段的查询 结束
		jsonpaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);
		
		return jsonpaging;
	}

	/***
     * 更新/保存方法
     */
	@Override
	public FundHouse save(FundHouse fundHouse, FundHouseSc fundHouseSc, FundHouseTc fundHouseTc
			, FundHouseEn fundHouseEn, boolean isAdd) {
		fundHouse = (FundHouse)this.baseDao.saveOrUpdate(fundHouse,isAdd);
		
		if(isAdd)
		{
			fundHouseSc.setId(fundHouse.getId());
			this.baseDao.create(fundHouseSc);
			
			fundHouseTc.setId(fundHouse.getId());
			this.baseDao.create(fundHouseTc);
			
			fundHouseEn.setId(fundHouse.getId());
			this.baseDao.create(fundHouseEn);

		} else {
			fundHouseSc.setId(fundHouse.getId());
			this.baseDao.saveOrUpdate(fundHouseSc,isAdd);
			
			fundHouseTc.setId(fundHouse.getId());
			this.baseDao.saveOrUpdate(fundHouseTc,isAdd);
			
			fundHouseEn.setId(fundHouse.getId());
			this.baseDao.saveOrUpdate(fundHouseEn,isAdd);
		}
		return fundHouse;
	}

	/**
	 * 获取fundHouse列表
	 * @return
	 */
	@Override
	public List<FundHouseDataVO> getFundHouseVoList() {
		String hql = " FROM FundHouse h "
			+ " LEFT JOIN FundHouseSc s ON h.id=s.id "
			+ " LEFT JOIN FundHouseTc t ON h.id=t.id "
			+ " LEFT JOIN FundHouseEn e ON h.id=e.id ";
		List<FundHouseDataVO> list = this.baseDao.find(hql, null, false);
		
		return list;
	}
}
