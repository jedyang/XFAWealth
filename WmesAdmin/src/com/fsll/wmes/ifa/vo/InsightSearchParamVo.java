package com.fsll.wmes.ifa.vo;

/**
 * @author scshi
 * @date 20160821
 * */
public class InsightSearchParamVo {
	private String issuedDate;
	
	private String allocation;
	
	private String sector;
	
	private String viewSort;
	
	private String issuedDateSort;
	
	private String keyWord;

	public String getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(String issuedDate) {
		this.issuedDate = issuedDate;
	}

	public String getAllocation() {
		return allocation;
	}

	public void setAllocation(String allocation) {
		this.allocation = allocation;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getViewSort() {
		return viewSort;
	}

	public void setViewSort(String viewSort) {
		this.viewSort = viewSort;
	}

	public String getIssuedDateSort() {
		return issuedDateSort;
	}

	public void setIssuedDateSort(String issuedDateSort) {
		this.issuedDateSort = issuedDateSort;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

}
