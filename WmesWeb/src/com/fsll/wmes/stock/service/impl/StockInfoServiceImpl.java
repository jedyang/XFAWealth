package com.fsll.wmes.stock.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.fsll.common.base.service.BaseService;
import com.fsll.wmes.entity.BondInfo;
import com.fsll.wmes.entity.StockInfo;
import com.fsll.wmes.entity.StockInfoEn;
import com.fsll.wmes.entity.StockInfoSc;
import com.fsll.wmes.entity.StockInfoTc;
import com.fsll.wmes.stock.service.StockInfoService;
import com.fsll.wmes.stock.vo.StockInfoVO;
import com.fsll.wmes.stock.vo.StockProductVO;

/**
 * 股票信息查询服务接口实现
 * @author Yan
 * @date 2016-12-09
 */
@Service("stockInfoService")
//@Transactional
public class StockInfoServiceImpl extends BaseService implements StockInfoService {

	/**
	 * 通过ID查找一条股票基本信息
	 * @param id
	 * @return
	 */
	public StockInfo findStockInfoById(String id){
		StockInfo info = (StockInfo) baseDao.get(StockInfo.class, id);
		return info;
	}
	
	/**
	 * 通过ID查找一条股票附加英文信息
	 * @param id
	 * @return
	 */
	public StockInfoEn findStockInfoEnById(String id){
		StockInfoEn info = (StockInfoEn) baseDao.get(StockInfoEn.class, id);
		return info;
	}
	
	/**
	 * 通过ID查找一条股票附加简体中文信息
	 * @param id
	 * @return
	 */
	public StockInfoSc findStockInfoScById(String id){
		StockInfoSc info = (StockInfoSc) baseDao.get(StockInfoSc.class, id);
		return info;
	}
	
	/**
	 * 通过ID查找一条股票附加繁体中文信息
	 * @param id
	 * @return
	 */
	public StockInfoTc findStockInfoTcById(String id){
		StockInfoTc info = (StockInfoTc) baseDao.get(StockInfoTc.class, id);
		return info;
	}
	
	/**
	 * 获取股票基础数据列表
	 * @return
	 */
	@Override
	public List<StockProductVO> getStockProductVoList(String langCode) {
		List<StockProductVO> voList = new ArrayList<StockProductVO>();
//		List params = new ArrayList();
		String hql = " FROM StockInfo r "
			+ " INNER JOIN " + this.getLangString("StockInfo", langCode)+" i ON r.id=i.id ";
		List<Object> list = this.baseDao.find(hql, null, false);
		if(!list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Object[] objRead = (Object[])list.get(i);
				StockInfo info = (StockInfo)objRead[0];
				StockProductVO vo = new StockProductVO();
				vo.setId(info.getId());
				if(!"".equals(info.getProduct()) && null!=info.getProduct()){
					vo.setProductId(info.getProduct().getId());
				}
				if("sc".equals(langCode)){
					StockInfoSc sc = (StockInfoSc)objRead[1];
					vo.setName(sc.getStockName());
				} else if("tc".equals(langCode)){
					StockInfoTc tc = (StockInfoTc)objRead[1];
					vo.setName(tc.getStockName());
				} else if("en".equals(langCode)){
					StockInfoEn en = (StockInfoEn)objRead[1];
					vo.setName(en.getStockName());
				}
				voList.add(vo);
			}
		}
		return voList;
	}

	/**
	 * 通过ID查找股票全部信息
	 * @param id
	 * @param langCode 语言
	 * @return
	 */
	public StockInfoVO findStockFullInfoById(String id, String langCode){
		StockInfoVO result = findStockFullInfoById(id);
		result.setDefaultInfoByLang(langCode);
		
		return result;
	}
	
	/**
	 * 通过ID查找股票全部信息（含多语言）
	 * @param id
	 * @return
	 */
	public StockInfoVO findStockFullInfoById(String id){
		StockInfoVO result = new StockInfoVO();
		StockInfo info = findStockInfoById(id);
		if(info != null){
			result.setStockInfo(info);
			result.setStockInfoEn(findStockInfoEnById(id));
			result.setStockInfoSc(findStockInfoScById(id));
			result.setStockInfoTc(findStockInfoTcById(id));
			if(info.getProduct() != null){
				result.setProductId(info.getProduct().getId());
			}
		}
		return result;
	}
	
	/**
	 * 根据产品ID获取股票信息（股票的基表信息与股票的多语言信息）
	 * @author 林文伟
	 * @param productId 产品ID
	 * @return
	 */
	@Override
	public StockInfo getStockInfoByProductId(String productId,String langCode){
		StockInfo stock = new StockInfo();
		if (StringUtils.isNotBlank(productId)) {
			String hql = "select a.exchangeCode,b.stockName  from StockInfo a left join "+this.getLangString("StockInfo", langCode)+" b on a.id=b.id  where  a.product.id='"+productId+"'";
			List params = new ArrayList();
			params.add(productId);
			List  list = this.baseDao.find(hql.toString(), null, false);
			if(!list.isEmpty()){
				Object[] objs = (Object[])list.get(0);
				String exchange = objs[0]==null?"0":objs[0].toString();
				String stockName = objs[1]==null?"":objs[1].toString();
				stock.setExchangeCode(exchange);
				stock.setStockName(stockName);
			}
		}
		return stock;
	}
}
