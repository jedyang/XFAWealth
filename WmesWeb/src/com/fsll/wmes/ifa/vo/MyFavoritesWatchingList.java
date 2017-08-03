package com.fsll.wmes.ifa.vo;

import java.util.List;

/**
 * 显示我自选的基金数据列表
 * @author 林文伟
 * @date 2016-9-28
 */
public class MyFavoritesWatchingList {
	
	private List<MyFavoritesWatchingTypeVOList> watchingTypeVOList;

	public List<MyFavoritesWatchingTypeVOList> getWatchingTypeVOList() {
		return watchingTypeVOList;
	}

	public void setWatchingTypeVOList(
			List<MyFavoritesWatchingTypeVOList> watchingTypeVOList) {
		this.watchingTypeVOList = watchingTypeVOList;
	}
	
	
}

