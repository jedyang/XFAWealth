package com.fsll.app.investor.market.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.investor.market.service.AppIndexChoiceService;
import com.fsll.app.investor.market.vo.AppIndexFundTopVO;
import com.fsll.app.investor.market.vo.AppIndexTopCategoryVO;
import com.fsll.common.base.service.BaseService;

/**
 * 首页你的选择接口服务现实
 * @author zpzhou
 * @date 2016-9-26
 */
@Service("appIndexChoiceService")
//@Transactional
public class AppIndexChoiceServiceImpl extends BaseService implements AppIndexChoiceService {
	/**
	 * 你的选择
	 * @param langCode  语言
	 * @return
	 */
	public List<AppIndexTopCategoryVO> findIndexTopCategory(String langCode){
		List<AppIndexTopCategoryVO> topList=new ArrayList<AppIndexTopCategoryVO>();
		String hql = "select t.id,t.name,t.iconUrl from TopCategory t order by t.orderBy desc";
		List list = baseDao.find(hql, null,false);
		for(int i=0;i<list.size(); i++){
			Object[] objs = (Object[])list.get(i);
			AppIndexTopCategoryVO  vo = new AppIndexTopCategoryVO();
			vo.setId(objs[0]==null?"":objs[0].toString());
			vo.setName(objs[1]==null?"":objs[1].toString());
			vo.setIconUrl(objs[2]==null?"":objs[2].toString());
			topList.add(vo);
    	}
		return topList;
	}
	/**
	 * 得到首页最佳基金数据
	 * @param period 时间类型
	 * @param langCode  语言
	 * @param num 返回条数
	 * @return
	 */
	public List<AppIndexFundTopVO> findIndexFundTop(String period,String langCode,int num) {
		List<AppIndexFundTopVO> topList=new ArrayList<AppIndexFundTopVO>();
		String hql = "select f.id,s.fundName,s.fundType,f.riskLevel,r.increase ";
		hql += " from FundReturn r ";
		hql += " left join r.fund f ";
		hql += " left join "+this.getLangString("FundInfo", langCode);
		hql += " s on s.id=f.id ";
		hql += " where r.periodCode=? order by r.increase desc LIMIT 0, ?";
		List params = new ArrayList();
		params.add(period);
		params.add(num);
		
		List list = baseDao.find(hql, params.toArray(),0,num ,false);
		for(int i=0;i<list.size(); i++){
			Object[] objs = (Object[])list.get(i);
			AppIndexFundTopVO  vo = new AppIndexFundTopVO();
			vo.setFundId(objs[0]==null?"":objs[0].toString());
			vo.setFundName(objs[1]==null?"":objs[1].toString());
			vo.setFundType(objs[2]==null?"":objs[2].toString());
			vo.setRiskLevel(objs[3]==null?"":objs[3].toString());
			vo.setIncrease(objs[4]==null?"":objs[4].toString());
			topList.add(vo);
    	}
		return topList;
	}

}
