/**
 * 
 */
package com.fsll.wmes.product.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.StrUtils;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.wmes.entity.BondInfo;
import com.fsll.wmes.entity.BondInfoEn;
import com.fsll.wmes.entity.BondInfoSc;
import com.fsll.wmes.entity.BondInfoTc;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundInfoTc;
import com.fsll.wmes.entity.ProductDistributor;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.StockInfo;
import com.fsll.wmes.entity.StockInfoEn;
import com.fsll.wmes.entity.StockInfoSc;
import com.fsll.wmes.entity.StockInfoTc;
import com.fsll.wmes.fund.vo.FundInfoDataVO;
import com.fsll.wmes.product.service.ProductInfoService;
import com.fsll.wmes.product.vo.ProductVO;

/**
 * 基金信息查询服务接口实现
 * @author Yan
 * @date 2016-11-28
 */
@Service("productInfoService")
//@Transactional
public class ProductInfoServiceImpl extends BaseService implements ProductInfoService {

	/**
	 * 通过ID查找一条产品基本信息
	 * @param id
	 * @return
	 */
	//@Transactional(readOnly = true)
	public ProductInfo findProductInfoById(String id){
		ProductInfo info = (ProductInfo) baseDao.get(ProductInfo.class, id);
		return info;
	}
	
	/**
	 * 获取产品列表（基金）
	 * @return
	 */
	@Override
	public List<ProductVO> getProductFundList(String distributorId, String langCode){
		String hql = " FROM ProductInfo p "
			+ " LEFT JOIN FundInfo f ON p.id=f.product.id "
			+ " LEFT JOIN " + this.getLangString("FundInfo", langCode) + " ff ON f.id=ff.id "
			+ " WHERE p.isValid='1' AND f.isValid='1' ";
		if(!StrUtils.isEmpty(distributorId)){
			hql += " AND p.id in (select d.product.id from ProductDistributor d where d.distributor.id='"+distributorId+"') ";
		}
		List<Object> queryList = (List<Object>)this.baseDao.find(hql, null, false);
		List<ProductVO> list = new ArrayList<ProductVO>();
		if(!queryList.isEmpty()){
			for (int i = 0; i < queryList.size(); i++) {
				ProductVO vo = new ProductVO();
				Object[] objRead = (Object[])queryList.get(i);
				ProductInfo product = (ProductInfo)objRead[0];
				FundInfo fund = (FundInfo)objRead[1];
				
				vo.setId(fund.getId());
				vo.setIsinCode(fund.getIsinCode());
				vo.setProductId(product.getId());
				//vo.setSymbolCode(product.getSymbolCode());
				vo.setType(product.getType());
				vo.setCreateTime(product.getCreateTime());
				vo.setLastUpdate(product.getLastUpdate());
				vo.setIsValid(product.getIsValid());
				
				if("sc".equals(langCode)){
					FundInfoSc sc = (FundInfoSc)objRead[2];
					vo.setName(sc.getFundName());
					vo.setCurrency(sc.getFundCurrency());
				} else if("tc".equals(langCode)){
					FundInfoTc tc = (FundInfoTc)objRead[2];
					vo.setName(tc.getFundName());
					vo.setCurrency(tc.getFundCurrency());
				} else if("en".equals(langCode)){
					FundInfoEn en = (FundInfoEn)objRead[2];
					vo.setName(en.getFundName());
					vo.setCurrency(en.getFundCurrency());
				}
				list.add(vo);
			}
		}
		return list;
	}
	
	/**
	 * 查找产品VO列表
	 * @update 20170224 by michael
	 * @param loginId
	 * @param keyword
	 * @param type
	 * @param langCode
	 * @return
	 */
	public JsonPaging getProductVOList(JsonPaging jsonPaging, String loginId, String keyword, String type, String langCode){
		String hql = "SELECT p FROM ProductInfo p "
			+ " LEFT JOIN FundInfo f ON p.id=f.product.id "
			+ " LEFT JOIN " + this.getLangString("FundInfo", langCode) + " fl ON f.id=fl.id "
			+ " LEFT JOIN ProductDistributor pd ON p.id=pd.product.id "
//			+ " LEFT JOIN BondInfo b ON p.id=b.product.id "
//			+ " LEFT JOIN " + this.getLangString("BondInfo", langCode) + " bl ON b.id=bl.id "
//			+ " LEFT JOIN StockInfo s ON p.id=s.product.id "
//			+ " LEFT JOIN " + this.getLangString("StockInfo", langCode) + " sl ON s.id=sl.id "
//			+ " LEFT JOIN SysParamConfig c ON c.configCode=fl.fundCurrencyCode OR c.configCode=bl.bondCurrencyCode OR c.configCode=sl.stockCurrencyCode "
			+ " WHERE p.isValid = '1' ";
//			+ " AND c.type.id=(SELECT t.id FROM SysParamType t WHERE t.typeCode='currency_type') ";
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isNotBlank(keyword)){
			hql += " AND fl.fundName LIKE ? ";
			params.add("%"+keyword+"%");
		}
		if(StringUtils.isNotBlank(loginId)){
			hql += " AND pd.distributor.id=? ";
			params.add(loginId);
		}
		if(StringUtils.isNotBlank(type)){
			hql += " AND p.type=? ";
			params.add(type);
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		List<Object> result = new ArrayList<Object>();
		List<Object> list = jsonPaging.getList();
		if (null!=list && !list.isEmpty()){
			for (int i=0;i<list.size();i++){
            	ProductInfo info = (ProductInfo)list.get(i);
				ProductVO vo = new ProductVO();
            	vo.setProductId(info.getId());
            	vo.setType(info.getType());
            	//vo.setSymbolCode(info.getSymbolCode());
            	vo.setCreateTime(info.getCreateTime());
            	vo.setLastUpdate(info.getLastUpdate());
            	vo.setIsValid(info.getIsValid());
            	
            	if("fund".equals(info.getType())){	//product为基金类型
            		ProductVO prd = findFundInfoByProductId(info.getId(),langCode);
            		if (null!=prd){
	            		vo.setIsinCode(prd.getIsinCode());
	            		vo.setId(prd.getId());
	            		vo.setName(prd.getName());
	            		vo.setCurrency(prd.getCurrency());
	            	}
            	} else if("bond".equals(info.getType())){	//product为债券类型
            		ProductVO prd = findBondInfoByProductId(info.getId(),langCode);
            		if (null!=prd){
	            		vo.setIsinCode(prd.getIsinCode());
	            		vo.setId(prd.getId());
	            		vo.setName(prd.getName());
	            		vo.setCurrency(prd.getCurrency());
	            	}
            	} else if("stock".equals(info.getType())){	//product为股票类型
            		ProductVO prd = findStockInfoByProductId(info.getId(),langCode);
            		if (null!=prd){
	            		vo.setIsinCode(prd.getIsinCode());
	            		vo.setId(prd.getId());
	            		vo.setName(prd.getName());
	            		vo.setCurrency(prd.getCurrency());
	            	}
            	}
            	result.add(vo);
			}
		}
		jsonPaging.setList(result);
		return jsonPaging;
	}
	
	/**
	 * 通过产品ID查找基金产品
	 * @param productId
	 * @param langCode
	 * @return
	 */
	public ProductVO findFundInfoByProductId(String productId, String langCode){
		String hql = "SELECT b,bl.fundName,bl.fundCurrencyCode,c FROM FundInfo b "
			+ " LEFT JOIN " + this.getLangString("FundInfo", langCode) + " bl ON b.id=bl.id "
			+ " LEFT JOIN SysParamConfig c ON c.configCode=bl.fundCurrencyCode "
			+ " WHERE b.isValid = '1' and b.product.id=? "
			+ " AND (bl.fundCurrencyCode is null OR c.type.id=(SELECT t.id FROM SysParamType t WHERE t.typeCode='currency_type')) ";
		List<Object> params = new ArrayList<Object>();
		params.add(productId);
		List list = this.baseDao.find(hql, params.toArray(), false);
		ProductVO vo = new ProductVO();
		if (null!=list && !list.isEmpty()){
			Object[] objs = (Object[])list.get(0);
			FundInfo f = (FundInfo)objs[0];
			vo.setProductId(f.getProduct().getId());
			vo.setId(f.getId());
			vo.setIsinCode(f.getIsinCode());
			vo.setName(StrUtils.getString(objs[1]));
			vo.setCurrencyCode(StrUtils.getString(objs[2]));
			SysParamConfig config = (SysParamConfig)objs[3];
			String currency = "";
			if (null!=config){
				if("sc".equals(langCode)){
					currency = config.getNameSc();
	    		} else if("tc".equals(langCode)){
	    			currency = config.getNameTc();
	        	} else if("en".equals(langCode)){
	        		currency = config.getNameEn();
	        	}
			}
			vo.setCurrency(currency);
			
			return vo;
		}
		return null;
	}
	
	/**
	 * 通过产品ID查找债劵产品
	 * @param productId
	 * @param langCode
	 * @return
	 */
	public ProductVO findBondInfoByProductId(String productId, String langCode){
		String hql = "SELECT b,bl.bondName,bl.bondCurrencyCode,c FROM BondInfo b "
			+ " LEFT JOIN " + this.getLangString("BondInfo", langCode) + " bl ON b.id=bl.id "
			+ " LEFT JOIN SysParamConfig c ON c.configCode=bl.bondCurrencyCode "
			+ " WHERE b.isValid = '1' and b.product.id=? "
			+ " AND (bl.bondCurrencyCode is null OR c.type.id=(SELECT t.id FROM SysParamType t WHERE t.typeCode='currency_type')) ";
		List<Object> params = new ArrayList<Object>();
		params.add(productId);
		List list = this.baseDao.find(hql, params.toArray(), false);
		ProductVO vo = new ProductVO();
		if (null!=list && !list.isEmpty()){
			Object[] objs = (Object[])list.get(0);
			BondInfo f = (BondInfo)objs[0];
			vo.setProductId(f.getProduct().getId());
			vo.setId(f.getId());
			vo.setIsinCode(f.getSymbolCode());
			vo.setName(StrUtils.getString(objs[1]));
			vo.setCurrencyCode(StrUtils.getString(objs[2]));
			SysParamConfig config = (SysParamConfig)objs[3];
			String currency = "";
			if (null!=config){
				if("sc".equals(langCode)){
					currency = config.getNameSc();
	    		} else if("tc".equals(langCode)){
	    			currency = config.getNameTc();
	        	} else if("en".equals(langCode)){
	        		currency = config.getNameEn();
	        	}
			}
			vo.setCurrency(currency);
			
			return vo;
		}
		return null;
	}
	
	/**
	 * 通过产品ID查找股票产品
	 * @param productId
	 * @param langCode
	 * @return
	 */
	public ProductVO findStockInfoByProductId(String productId, String langCode){
		String hql = "SELECT b,bl.stockName,bl.stockCurrencyCode,c FROM StockInfo b "
			+ " LEFT JOIN " + this.getLangString("StockInfo", langCode) + " bl ON b.id=bl.id "
			+ " LEFT JOIN SysParamConfig c ON c.configCode=bl.stockCurrencyCode "
			+ " WHERE b.isValid = '1' and b.product.id=? "
			+ " AND (bl.stockCurrencyCode is null OR c.type.id=(SELECT t.id FROM SysParamType t WHERE t.typeCode='currency_type')) ";
		List<Object> params = new ArrayList<Object>();
		params.add(productId);
		List list = this.baseDao.find(hql, params.toArray(), false);
		ProductVO vo = new ProductVO();
		if (null!=list && !list.isEmpty()){
			Object[] objs = (Object[])list.get(0);
			StockInfo f = (StockInfo)objs[0];
			vo.setProductId(f.getProduct().getId());
			vo.setId(f.getId());
			vo.setIsinCode(f.getSymbolCode());
			vo.setName(StrUtils.getString(objs[1]));
			vo.setCurrencyCode(StrUtils.getString(objs[2]));
			SysParamConfig config = (SysParamConfig)objs[3];
			String currency = "";
			if (null!=config){
				if("sc".equals(langCode)){
					currency = config.getNameSc();
	    		} else if("tc".equals(langCode)){
	    			currency = config.getNameTc();
	        	} else if("en".equals(langCode)){
	        		currency = config.getNameEn();
	        	}
			}
			vo.setCurrency(currency);
			
			return vo;
		}
		return null;
	}
	
	/**
	 * 保存信息
	 * @param info
	 * @return
	 */
	public ProductInfo saveOrUpdate(ProductInfo info, boolean isAdd){
		if(isAdd){
			return (ProductInfo)this.baseDao.create(info);
		}else{
			return (ProductInfo)this.baseDao.saveOrUpdate(info);
		}
	}
	
	/**
	 * 通过ID删除一条记录
	 * @param id
	 * @return
	 */
	@Override
	public boolean deleteById(String id){
		if (id == null) {
			return false;
		}else{
			ProductInfo info = findProductInfoById(id);
			if(info != null){
				baseDao.delete(info);
				return true;
			}else{
				return false;
			}
		}
	}

}
