package com.fsll.app.fund.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.fund.service.AppMarketInfoService;
import com.fsll.app.fund.vo.AppMarketIndexDetailVo;
import com.fsll.app.fund.vo.AppMarketIndexVo;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.wmes.entity.MarketIndex;
import com.fsll.wmes.entity.MarketIndexDetail;
import com.fsll.wmes.entity.MarketIndexHis;

/**
 * 大盘指数服务接口现实
 * @author zpzhou
 * @date 2016-9-13
 */
@Service("appMarketInfoService")
//@Transactional
public class AppMarketInfoServiceImpl extends BaseService implements AppMarketInfoService {

	/**
	 * 得到我关注的Market指数信息
	 * @param userId 用户ID
	 * @param langCode 语言
	 * @return
	 */
	public List<AppMarketIndexVo> findMarketFollowList(String userId,String langCode){
		String hql = "select f.id,f.code,f.name,f.curDot,f.increase,f.upsDowns ";
		hql += " from WebFollow w ";
		hql += " left join MarketIndex f on f.id=w.relateId ";
		hql += " where w.moduleType='market' and w.isValid='1' and w.member.id=? ";
		List params = new ArrayList();
		params.add(userId);
		List list = baseDao.find(hql, params.toArray(), false);
		List<AppMarketIndexVo> listTemp=new ArrayList();
		for(int i=0;i<list.size(); i++){
			Object[] objs = (Object[])list.get(i);
			AppMarketIndexVo  vo = new AppMarketIndexVo();
			vo.setId(objs[0]==null?"":objs[0].toString());
			vo.setCode(objs[1]==null?"":objs[1].toString());
			vo.setName(objs[2]==null?"":objs[2].toString());
			vo.setCurDot(objs[3]==null?"":objs[3].toString());
			vo.setIncrease(objs[4]==null?"":objs[4].toString());
			vo.setUpsDowns(objs[5]==null?"":objs[5].toString());
			listTemp.add(vo);
    	}
		return listTemp;
	}

	/**
	 * @param langCode 语言
	 * @param keyWord 搜索关键词
	 * @return
	 */
	public List<AppMarketIndexVo> findMarketList(String langCode,String keyWord){
		String hql = " from MarketIndex m where 1=1 ";
		List params = new ArrayList();
		if(!"".equals(keyWord)){
			hql+=" and m.name like ? or m.code like ?";
			params.add("%"+keyWord+"%");
			params.add("%"+keyWord+"%");
		}
		List<MarketIndex> list = baseDao.find(hql, params.toArray(), false);
		List<AppMarketIndexVo> listTemp=new ArrayList();
		for(int i=0;i<list.size(); i++){
			MarketIndex index= (MarketIndex)list.get(i);
			AppMarketIndexVo  vo = new AppMarketIndexVo();
			vo.setId(index.getId());
			vo.setCode(index.getCode());
			vo.setName(index.getName());
			vo.setCurDot(String.valueOf(index.getCurDot()));
			vo.setIncrease(index.getIncrease());
			vo.setUpsDowns(index.getUpsDowns());
			listTemp.add(vo);
    	}
		return listTemp;
	}
	/**
	 * 得到大盘指数分时信息数据
	 * @param indexId 语言
	 * @return
	 */
	public List<AppMarketIndexDetailVo> getMarketDetailMess(String indexId){
		String hql = " from MarketIndexDetail m where m.index.id=? ";
		List params = new ArrayList();
		params.add(indexId);
		List<MarketIndexDetail> list = baseDao.find(hql, params.toArray(), false);
		List<AppMarketIndexDetailVo> listTemp=new ArrayList();
		for(int i=0;i<list.size(); i++){
			MarketIndexDetail detail= (MarketIndexDetail)list.get(i);
			AppMarketIndexDetailVo  vo = new AppMarketIndexDetailVo();
			vo.setId(detail.getId());
			vo.setMarketTime(DateUtil.dateToDateString(detail.getMarketTime(), "HH:mm:ss"));
			vo.setCurDot(String.valueOf(detail.getCurDot()));
			listTemp.add(vo);
    	}
		return listTemp;
	}
	/**
	 * 得到每天的大盘指数列表
	 * @param langCode 语言
	 * @param keyWord 搜索关键词
	 * @return
	 */
	public List<AppMarketIndexVo> findMarketHisList(String code){
		String hql = " from MarketIndexHis m where m.code = ? ";
		List params = new ArrayList();
		params.add(code);
		List<MarketIndexHis> list = baseDao.find(hql, params.toArray(), false);
		List<AppMarketIndexVo> listTemp=new ArrayList();
		for(int i=0;i<list.size(); i++){
			MarketIndexHis index= (MarketIndexHis)list.get(i);
			AppMarketIndexVo  vo = new AppMarketIndexVo();
			vo.setCurDot(String.valueOf(index.getCurDot()));
			vo.setIncrease(index.getIncrease());
			vo.setUpsDowns(index.getUpsDowns());
			vo.setMarketDate(DateUtil.dateToDateString(index.getMarketDate(), "yyyy-MM-dd"));
			listTemp.add(vo);
    	}
		return listTemp;
	}

}
