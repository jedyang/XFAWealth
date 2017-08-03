/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.api.product.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fsll.api.product.service.PruductService;
import com.fsll.api.product.vo.ProductBasicDataVO;
import com.fsll.api.product.vo.ProductDetailVo;
import com.fsll.api.product.vo.ProductListVo;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.dao.entity.BondInfo;
import com.fsll.dao.entity.FundInfo;
import com.fsll.dao.entity.InsureInfo;
import com.fsll.dao.entity.StockInfo;
import com.fsll.dao.product.BondInfoDao;
import com.fsll.dao.product.FundInfoDao;
import com.fsll.dao.product.InsureInfoDao;
import com.fsll.dao.product.ProductReportDao;
import com.fsll.dao.product.StockInfoDao;

/**
 *产品信息业务实现
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-6-19
 */
@Service("pruductService")
public class PruductServiceImpl extends BaseService implements PruductService {
	
	@Autowired
	public FundInfoDao fundInfoDao;
	
	@Autowired
	public BondInfoDao bondInfoDao;
	
	@Autowired
	public StockInfoDao stockInfoDao;
	
	@Autowired
	public InsureInfoDao insureInfoDao;
	
	@Autowired
	public ProductReportDao productReportDao;
	
	/**
	 * 按条件搜索列表
	 * @param jsonPaging 分页信息
	 * @param q 查询条件josn对象
	 * @return	JsonPaging	分页基金数据
	 */
	public JsonPaging findAll(JsonPaging jsonPaging,JSONObject q){
		//获取版本参数.0表示默认版本,版本参数必须为整数
		int v = q.optInt("_VER_",0);
		//建议采用一个方法控制;如果实在变动太多,则另起方法
		/*
		if(v == 201){//201版本
			return findAllForV201(jsonPaging,q,v);
		}else{//默认版本
			return findAllForV100(jsonPaging,q,v);
		}
		*/
		String productType = q.optString("productType","fund");
		if("fund".equals(productType)){//基金
			jsonPaging = fundInfoDao.findList(jsonPaging,q);
		}else if("stock".equals(productType)){//股票
			jsonPaging = stockInfoDao.findList(jsonPaging,q);
		}else if("bond".equals(productType)){//债券
			jsonPaging = bondInfoDao.findList(jsonPaging,q);
		}else if("insure".equals(productType)){//保险
			jsonPaging = insureInfoDao.findList(jsonPaging,q);
		}
		
		List<ProductListVo> pList=new ArrayList<ProductListVo>();
		List list=jsonPaging.getList();
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			ProductListVo  vo = new ProductListVo();
			vo.setId(objs[0]==null?"":objs[0].toString());
			vo.setName(objs[1]==null?"":objs[1].toString());
			vo.setRiskLevel(objs[2]==null?"":objs[2].toString());
			
			if(v == 201){//如果版本为2.01版本.则显示增量字段
				vo.setIncrease(objs[3]==null?"":objs[3].toString());
			}
			
			if(null==vo.getIncrease() || "".equals(vo.getIncrease())){
				vo.setIncrease(getFormatNumByPer(null));
			}else{
				vo.setIncrease(getFormatNumByPer(Double.parseDouble(vo.getIncrease())));
			}
			pList.add(vo);
		}
		jsonPaging.setList(pList);
		return jsonPaging;
	}
	
	/**
	 * 增加或者修改一条数据
	 * @param obj
	 */
	public  Object saveOrUpdate(JSONObject q){
		//1.获得公共字段
		String productType = q.optString("productType","fund");
		String code = q.optString("code","");
		
		//2.获得特有字段
		if("fund".equals(productType)){//基金
			String fundId = q.optString("fundId","");
			FundInfo fundInfo = null;
			if("".equals(fundId)){//新增
				fundInfo = new FundInfo();
				fundInfo.setId(null);
			}else{
				fundInfo = fundInfoDao.findById(fundId);
			}
			fundInfo.setIsinCode(code);
			fundInfo = fundInfoDao.saveOrUpdate(fundInfo);
			return fundInfo;
		}else if("stock".equals(productType)){//股票
			String stockId = q.optString("stockId","");
			StockInfo stockInfo = null;
			if("".equals(stockId)){//新增
				stockInfo = new StockInfo();
				stockInfo.setId(null);
			}else{
				stockInfo = stockInfoDao.findById(stockId);
			}
			stockInfo.setSymbolCode(code);
			stockInfo = stockInfoDao.saveOrUpdate(stockInfo);
			return stockInfo;
		}else if("bond".equals(productType)){//债券
			String bondId = q.optString("bondId","");
			BondInfo bondInfo = null;
			if("".equals(bondId)){//新增
				bondInfo = new BondInfo();
				bondInfo.setId(null);
			}else{
				bondInfo = bondInfoDao.findById(bondId);
			}
			bondInfo.setSymbolCode(code);
			bondInfo = bondInfoDao.saveOrUpdate(bondInfo);
			return bondInfo;
		}else if("insure".equals(productType)){//保险
			String insureId = q.optString("insureId","");
			InsureInfo insureInfo = null;
			if("".equals(insureId)){//新增
				insureInfo = new InsureInfo();
				insureInfo.setId(null);
			}else{
				insureInfo = insureInfoDao.findById(insureId);
			}
			insureInfo.setIsinCode(code);
			insureInfo = insureInfoDao.saveOrUpdate(insureInfo);
			return insureInfo;
		}
		return null;
	}
	
	/**
	 * 修改数据的状态
	 * @param q 参数对象
	 * @return
	 */
	public  boolean saveStatus(JSONObject q){
		String productType = q.optString("productType","fund");
		String ids = q.optString("ids","");
		String status = q.optString("status","");
		boolean result = false;
		if("fund".equals(productType)){//基金
			result = fundInfoDao.saveStatus(ids, status);
		}else if("stock".equals(productType)){//股票
			result = stockInfoDao.saveStatus(ids, status);
		}else if("bond".equals(productType)){//债券
			result = bondInfoDao.saveStatus(ids, status);
		}else if("insure".equals(productType)){//保险
			result = insureInfoDao.saveStatus(ids, status);
		}
		return result;
	}
	
	/**
	 * 通过ID删除记录
	 * @param id
	 * @return
	 */
	public boolean delete(JSONObject q){
		String productType = q.optString("productType","fund");
		String ids = q.optString("ids","");
		
		boolean result = false;
		if("fund".equals(productType)){//基金
			result = fundInfoDao.deleteById(ids);
		}else if("stock".equals(productType)){//股票
			result = stockInfoDao.deleteById(ids);
		}else if("bond".equals(productType)){//债券
			result = bondInfoDao.deleteById(ids);
		}else if("insure".equals(productType)){//保险
			result = insureInfoDao.deleteById(ids);
		}
		return result;
	}
	
	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	public ProductDetailVo findById(JSONObject q){
		String productType = q.optString("productType","fund");
		String productId = q.optString("productId","");
		
		ProductDetailVo result = new ProductDetailVo();
		
		//0.上一条和下一条的功能需求
		result = this.parsePreOrNext(q,result);
		
		//具体业务处理
		if("fund".equals(productType)){//基金
			FundInfo fundInfo = fundInfoDao.findByPruductId(productId);
			
			//1.设置公共信息
			ProductBasicDataVO vo = new ProductBasicDataVO();
			vo.setId(fundInfo.getId());
			vo.setProductId(productId);
			vo.setProductCode(fundInfo.getIsinCode());
			vo.setProductType(productType);
			
			//2.设置特有信息
			//....
			
			result.setVo(vo);
		}else if("stock".equals(productType)){//股票
			StockInfo stockInfo = stockInfoDao.findByPruductId(productId);
			
			//1.设置公共信息
			ProductBasicDataVO vo = new ProductBasicDataVO();
			vo.setId(stockInfo.getId());
			vo.setProductId(productId);
			vo.setProductCode(stockInfo.getSymbolCode());
			vo.setProductType(productType);
			
			//2.设置特有信息
			//....
			
			result.setVo(vo);
		}else if("bond".equals(productType)){//债券
			BondInfo bondInfo =bondInfoDao.findByPruductId(productId);
			
			//1.设置公共信息
			ProductBasicDataVO vo = new ProductBasicDataVO();
			vo.setId(bondInfo.getId());
			vo.setProductId(productId);
			vo.setProductCode(bondInfo.getSymbolCode());
			vo.setProductType(productType);
			
			//2.设置特有信息
			//....
			
			result.setVo(vo);
		}else if("insure".equals(productType)){//保险
			InsureInfo insureInfo = insureInfoDao.findByPruductId(productId);
			
			//1.设置公共信息
			ProductBasicDataVO vo = new ProductBasicDataVO();
			vo.setId(insureInfo.getId());
			vo.setProductId(productId);
			vo.setProductCode(insureInfo.getIsinCode());
			vo.setProductType(productType);
			
			//2.设置特有信息
			//....
			
			result.setVo(vo);
		}
		return result;
	}
	
	/**
	 * 通过ID删除记录
	 * @param id
	 * @return
	 */
	private ProductDetailVo parsePreOrNext(JSONObject q,ProductDetailVo result){
		String searchListType = q.optString("searchListType","");
		if("api_product_list".equals(searchListType)){//从列表中进来
			String searchListCond = q.optString("searchListCond","");
			JSONObject searchListCondForJson = JSONObject.fromObject(searchListCond);
			Integer searchPage = q.optInt("searchPage",1);//当前页page,此处的当前页是来源列表中第几条记录 等于 page*rows + index + 1
			String searchSort = q.optString("searchSort","");//排序字段
			String searchOrder = q.optString("searchOrder","");//asc或者dec
			JsonPaging jsonPaging = new JsonPaging();
			jsonPaging.setSort(searchSort);
			jsonPaging.setOrder(searchOrder);
			
			//查询前一条记录
			String searchPreId = "";//上一个标识
			String searchPreName = "";
			String searchNextId = "";//下一个标识
			String searchNextName = "";
			
			Integer totalPage = 0;
			if(searchPage > 1){
				jsonPaging.setPage(searchPage-1);
				jsonPaging.setRows(1);
				jsonPaging = this.findAll(jsonPaging,searchListCondForJson);
				totalPage = jsonPaging.getTotal();
				List listTmp = jsonPaging.getList();
				if(!listTmp.isEmpty()){
					ProductListVo tmpVO = (ProductListVo)listTmp.get(0);
					searchPreId = tmpVO.getId();
					searchPreName = tmpVO.getName();
				}
			}
			if(searchPage > 1){
				jsonPaging.setPage(searchPage+1);
				jsonPaging.setRows(1);
				jsonPaging = this.findAll(jsonPaging,searchListCondForJson);
				totalPage = jsonPaging.getTotal();
				List listTmp = jsonPaging.getList();
				if(!listTmp.isEmpty()){
					ProductListVo tmpVO = (ProductListVo)listTmp.get(0);
					searchNextId = tmpVO.getId();
					searchNextName = tmpVO.getName();
				}
			}
			if("".equals(searchPreId)){//说明是第一条记录,则前一条记录就是最后一条记录
				jsonPaging.setPage(totalPage);
				jsonPaging.setRows(1);
				jsonPaging = this.findAll(jsonPaging,searchListCondForJson);
				totalPage = jsonPaging.getTotal();
				List listTmp = jsonPaging.getList();
				if(!listTmp.isEmpty()){
					ProductListVo tmpVO = (ProductListVo)listTmp.get(0);
					searchPreId = tmpVO.getId();
					searchPreName = tmpVO.getName();
				}
			}
			if("".equals(searchNextId)){//说明是最后一条记录,则下一条记录就是第一条记录
				jsonPaging.setPage(1);
				jsonPaging.setRows(1);
				jsonPaging = this.findAll(jsonPaging,searchListCondForJson);
				totalPage = jsonPaging.getTotal();
				List listTmp = jsonPaging.getList();
				if(!listTmp.isEmpty()){
					ProductListVo tmpVO = (ProductListVo)listTmp.get(0);
					searchNextId = tmpVO.getId();
					searchNextName = tmpVO.getName();
				}
			}
			
			//保存结果
			result.setSearchListType(searchListType);
			result.setSearchListCond(searchListCond);
			result.setSearchPage(searchPage);
			result.setSearchOrder(searchOrder);
			result.setSearchSort(searchSort);
			result.setSearchPreId(searchPreId);
			result.setSearchPreName(searchPreName);
			result.setSearchNextId(searchNextId);
			result.setSearchNextName(searchNextName);
		}
		return result;
	}
	
}
