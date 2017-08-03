/**
 * 
 */
package com.fsll.app.fund.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.fund.service.AppFundFeesItemService;
import com.fsll.app.fund.vo.AppFundChargesDataVO;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.StrUtils;

/**
 * @author tan
 *基金管理费用信息接口实现
 * @date 20160629
 */
@Service("appFundFeesItemService")
//@Transactional
public class AppFundFeesItemServiceImpl extends BaseService implements AppFundFeesItemService {

	/**3.1.8	获取基金管理费用信息
	 * @author scshi
	 * @param fundId 资金id
	 * @param LANG_CODE 语言
	 * @return	<List>FundChargesDataVO	基金管理费用信息
	 */
	@Override
	//@Transactional(readOnly=true)
	public List<AppFundChargesDataVO> fundChargesInfo(String fundId,String langCode) {
		List params = new ArrayList();
		String hql = "select t.fund.id, out.feesItem,out.fees From FundFeesItem t ";
		hql += " INNER JOIN "+this.getLangString("FundFeesItem", langCode);
		hql += " out ON t.id=out.id where t.isValid='1' ";
		
		if(null!=fundId && !"".equals(fundId)){
			hql +=" and t.fund.id=? ";
			params.add(fundId);
		}
		
		List preList = this.baseDao.find(hql, params.toArray(), false);
		List<AppFundChargesDataVO> voList = new ArrayList();
		if(!preList.isEmpty()){
			for(int x=0;x<preList.size();x++){
				AppFundChargesDataVO vo = new AppFundChargesDataVO();
				Object[] objs = (Object[])preList.get(x);
				vo.setFundId(StrUtils.getString(objs[0]));
				vo.setItemName(StrUtils.getString(objs[1]));
				vo.setDescription(StrUtils.getString(objs[2]));
				voList.add(vo);
			}
		}
		return voList;
	}
}
