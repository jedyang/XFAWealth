package com.fsll.wmes.stock.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fsll.common.base.service.BaseService;
import com.fsll.wmes.entity.StockInfo;
import com.fsll.wmes.entity.StockInfoEn;
import com.fsll.wmes.entity.StockInfoSc;
import com.fsll.wmes.entity.StockInfoTc;
import com.fsll.wmes.stock.service.StockInfoService;
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

}
