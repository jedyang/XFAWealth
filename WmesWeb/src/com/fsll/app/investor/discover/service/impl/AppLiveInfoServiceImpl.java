package com.fsll.app.investor.discover.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.investor.discover.service.AppLiveInfoService;
import com.fsll.app.investor.discover.vo.AppLiveInfoVO;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.LiveCategory;
import com.fsll.wmes.entity.LiveInfo;

/**
 * 新闻接口
 * @author zxtan
 * @date 2016-11-25
 */
@Service("appLiveInfoService")
//@Transactional
public class AppLiveInfoServiceImpl extends BaseService implements AppLiveInfoService {
	
	/**
	 * 获取直播类型列表
	 */
	public List<LiveCategory> findLiveCategoryList() {
		String hql = " from LiveCategory i order by i.orderBy ";
		List<LiveCategory> list = this.baseDao.find(hql, null, false);
		return list;
	}

	/**
	 * 获取直播内容列表
	 */
	public JsonPaging findLiveList(JsonPaging jsonPaging, String categoryId) {
		List<AppLiveInfoVO> liveList = new ArrayList<AppLiveInfoVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from LiveInfo i ");
		hql.append(" left join LiveInfoCategory c on i.id=c.live.id ");
		hql.append(" where i.isValid='1' and i.status='published' and c.category.id = ? ");
		hql.append(" order by i.issuedTime desc ");
		List<String> params = new ArrayList<String>();
		params.add(categoryId);
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List list = jsonPaging.getList();
		for (int i = 0; i < list.size(); i++) {
			Object[] objs = (Object[])list.get(i);
			LiveInfo info = (LiveInfo) objs[0];
			AppLiveInfoVO vo = new AppLiveInfoVO();
			vo.setId(info.getId());
			vo.setTitle(info.getTitle());
			vo.setContent(info.getContent());
			vo.setTimeLength(info.getTimeLength().toString());
			if(null != info.getOverheadBy()){
				vo.setOverheadBy(info.getOverheadBy().toString());
			}
			if(null != info.getAuthor()){
				vo.setAuthor(info.getAuthor().getNickName());
			}
			vo.setIssuedTime(DateUtil.dateToDateString(info.getIssuedTime(), "yyyy-MM-dd HH:mm:ss"));
			vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			vo.setLastUpdate(DateUtil.dateToDateString(info.getLastUpdate(), "yyyy-MM-dd HH:mm:ss"));
			
			liveList.add(vo);
		}
		jsonPaging.setList(liveList);		
		return jsonPaging;
	}

	/**
	 * 获取直播内容实体
	 */
	public LiveInfo findLiveInfoById(String id) {
		LiveInfo vo=(LiveInfo)this.baseDao.get(LiveInfo.class, id);
		return vo;
	}
}
