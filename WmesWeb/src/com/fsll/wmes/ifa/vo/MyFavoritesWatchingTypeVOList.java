package com.fsll.wmes.ifa.vo;

import java.util.Date;
import java.util.List;

import com.fsll.wmes.fund.vo.FundInfoDataVO;

/**
 * 显示我自选的基金数据列表
 * @author 林文伟
 * @date 2016-9-28
 */
public class MyFavoritesWatchingTypeVOList {
	private String typeId;
	private String  typeName;
	List<MyFavoritesWatchingListVO> myFavoritesWatchingListVO;
	private Date createTime;
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public List<MyFavoritesWatchingListVO> getMyFavoritesWatchingListVO() {
		return myFavoritesWatchingListVO;
	}
	public void setMyFavoritesWatchingListVO(
			List<MyFavoritesWatchingListVO> myFavoritesWatchingListVO) {
		this.myFavoritesWatchingListVO = myFavoritesWatchingListVO;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	
}
