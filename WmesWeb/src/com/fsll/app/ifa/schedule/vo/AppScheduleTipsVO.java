package com.fsll.app.ifa.schedule.vo;

/**
 * 日程日历实体类VO
 * @author zxtan
 * @date 2017-04-23
 */
public class AppScheduleTipsVO {
	private String date; //日期
	private String count;//日程数量

	
	public String getDate(){
		return date;
	}
	public void setDate(String date){
		this.date = date;
	}
	public String getCount(){
		return count;
	}
	public void setCount(String count){
		this.count = count;
	}
	
}
