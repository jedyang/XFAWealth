/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.buscore.fund.vo;

import java.util.Date;
import java.util.List;

/**
 * 基金行情数据
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-5-9
 */
public class CoreFundNavAlignVO {
	private int dataIndex;
	private Date dataDate;
	private String dataDateStr;
	private List<CoreFundNavVO> dataList;
	public int getDataIndex() {
		return dataIndex;
	}
	public void setDataIndex(int dataIndex) {
		this.dataIndex = dataIndex;
	}
	
	public Date getDataDate() {
		return dataDate;
	}
	public void setDataDate(Date dataDate) {
		this.dataDate = dataDate;
	}
	public List<CoreFundNavVO> getDataList() {
		return dataList;
	}
	public void setDataList(List<CoreFundNavVO> dataList) {
		this.dataList = dataList;
	}
	public String getDataDateStr() {
		return dataDateStr;
	}
	public void setDataDateStr(String dataDateStr) {
		this.dataDateStr = dataDateStr;
	}
	
}
