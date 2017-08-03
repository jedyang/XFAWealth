/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.dao.product.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Repository;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.dao.CommonDao;
import com.fsll.common.util.JsonPaging;
import com.fsll.dao.product.ProductReportDao;

/**
 * Dao复合或者报表查询接口实现
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-6-23
 */
@Repository("productReportDao")
public class ProductReportDaoImpl  extends CommonDao implements ProductReportDao {
	/**
	 * 按条件多表查询-有分页且有总记录数
	 * @param jsonPaging 分页信息
	 * @param q 查询
	 * @return	JsonPaging	分页基金数据
	 */
	public JsonPaging findXXX(JsonPaging jsonPaging,JSONObject q){
		//1.获取查询字段
		String langCode = q.optString("langCode", CommonConstants.DEF_LANG_CODE);
		String periodCode = q.optString("periodCode","return_period_code_1M");
		
		//2.组装查询sql
		String hql = "select f.id,s.fundName,f.riskLevel,r.increase,count(*) as num,p.id ";
		hql += " from ProductInfo p";
		hql += " right join PortfolioArenaProduct t on t.product.id=p.id ";
		hql += " inner join FundInfo f on f.product.id=p.id ";
		hql += " inner join "+this.getLangString("FundInfo", langCode)+" s on s.id=f.id ";
		hql += " left join FundReturn r on r.fund.id=f.id and r.periodCode=? ";
		hql += " where p.type=? and p.isValid=? GROUP BY t.product.id order by num desc,r.increase desc ";
		List params = new ArrayList();
		params.add(periodCode);
		params.add("fund");
		params.add("1");
		
		//3.返回查询结果
		jsonPaging= baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		return jsonPaging;
	}
	
	/**
	 * 按条件多表查询-有分页无总记录数
	 * @param jsonPaging 分页信息
	 * @param q 查询
	 * @return	JsonPaging	分页基金数据
	 */
	public JsonPaging queryXXX(JsonPaging jsonPaging,JSONObject q){
		//1.获取查询字段
		String langCode = q.optString("langCode", CommonConstants.DEF_LANG_CODE);
		String periodCode = q.optString("periodCode","return_period_code_1M");
		
		//2.组装查询sql
		String hql = "select f.id,s.fundName,f.riskLevel,r.increase,count(*) as num,p.id ";
		hql += " from ProductInfo p";
		hql += " right join PortfolioArenaProduct t on t.product.id=p.id ";
		hql += " inner join FundInfo f on f.product.id=p.id ";
		hql += " inner join "+this.getLangString("FundInfo", langCode)+" s on s.id=f.id ";
		hql += " left join FundReturn r on r.fund.id=f.id and r.periodCode=? ";
		hql += " where p.type=? and p.isValid=? GROUP BY t.product.id order by num desc,r.increase desc ";
		List params = new ArrayList();
		params.add(periodCode);
		params.add("fund");
		params.add("1");
		
		//3.返回查询结果
		jsonPaging= baseDao.selectJsonPagingNoTotal(hql, params.toArray(), jsonPaging, false);
		return jsonPaging;
	}
	
	/**
	 * 按条件多表查询-返回列表
	 * @param jsonPaging 分页信息
	 * @param q 查询
	 * @return	JsonPaging	分页基金数据
	 */
	public List getXXX(JSONObject q){
		//1.获取查询字段
		String langCode = q.optString("langCode", CommonConstants.DEF_LANG_CODE);
		String periodCode = q.optString("periodCode","return_period_code_1M");
		
		//2.组装查询sql
		String hql = "select f.id,s.fundName,f.riskLevel,r.increase,count(*) as num,p.id ";
		hql += " from ProductInfo p";
		hql += " right join PortfolioArenaProduct t on t.product.id=p.id ";
		hql += " inner join FundInfo f on f.product.id=p.id ";
		hql += " inner join "+this.getLangString("FundInfo", langCode)+" s on s.id=f.id ";
		hql += " left join FundReturn r on r.fund.id=f.id and r.periodCode=? ";
		hql += " where p.type=? and p.isValid=? GROUP BY t.product.id order by num desc,r.increase desc ";
		List params = new ArrayList();
		params.add(periodCode);
		params.add("fund");
		params.add("1");
		
		//3.返回查询结果
		return baseDao.find(hql, params.toArray(),false);
	}
	
}
