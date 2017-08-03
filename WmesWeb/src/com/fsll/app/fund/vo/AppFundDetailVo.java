
package com.fsll.app.fund.vo;

import java.util.ArrayList;
import java.util.List;

import com.fsll.buscore.fund.vo.CoreFundNavVO;

/**
 * 基金详细信息VO
 * @author zpzhou
 * @date 2016-7-22
 */
public class AppFundDetailVo {
	private AppFundBasicDataVO vo=new AppFundBasicDataVO();
	private List<AppFundCumulativePerformanceDataVO> ljList=new ArrayList<AppFundCumulativePerformanceDataVO>();
	private List<AppFundYearPerformanceDataVO> ndList=new ArrayList<AppFundYearPerformanceDataVO>();
	private List<CoreFundNavVO> navList=new ArrayList<CoreFundNavVO>(); 
	public List<AppFundCumulativePerformanceDataVO> getLjList() {
		return ljList;
	}
	public void setLjList(List<AppFundCumulativePerformanceDataVO> ljList) {
		this.ljList = ljList;
	}
	public List<AppFundYearPerformanceDataVO> getNdList() {
		return ndList;
	}
	public void setNdList(List<AppFundYearPerformanceDataVO> ndList) {
		this.ndList = ndList;
	}
	public AppFundBasicDataVO getVo() {
		return vo;
	}
	public void setVo(AppFundBasicDataVO vo) {
		this.vo = vo;
	}
	public List<CoreFundNavVO> getNavList() {
		return navList;
	}
	public void setNavList(List<CoreFundNavVO> navList) {
		this.navList = navList;
	}	
}
