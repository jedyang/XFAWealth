package com.fsll.app.investor.discover.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.LiveCategory;
import com.fsll.wmes.entity.LiveInfo;


/**
 * 新闻接口
 * @author zxtan
 * @date 2016-11-25
 */
public interface AppLiveInfoService {
	
	/**
	 * 得到直播视频列表信息
	 * @return
	 */
	public List<LiveCategory> findLiveCategoryList();
	
	/**
	 * 得到直播视频列表信息
	 * @param jsonPaging 分页参数
	 * @param categoryId 分类ID
	 * @return
	 */
	public JsonPaging findLiveList(JsonPaging jsonPaging,String categoryId);
	
	/**
	 * 获取直播视频的信息实体
	 * @param id
	 * @return
	 */
	public LiveInfo findLiveInfoById(String id);
}
