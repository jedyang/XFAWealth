package com.fsll.app.ifa.client.service;

import java.util.Date;
import java.util.List;

import com.fsll.app.ifa.client.vo.AppClientCountVO;
import com.fsll.app.ifa.client.vo.AppClientIndexVO;
import com.fsll.app.ifa.client.vo.AppClientItemVO;
import com.fsll.app.ifa.client.vo.AppCrmCustomerGroupVO;
import com.fsll.app.ifa.client.vo.AppEventVO;
import com.fsll.app.ifa.client.vo.AppProspectEventVO;
import com.fsll.app.ifa.client.vo.ClientSearchVO;
import com.fsll.wmes.entity.MemberIfa;

public interface AppIndexInfoService {
	
	/**
	 * 获取IFA的分组列表
	 * @author zxtan
	 * @date 2017-03-16
	 */
	public List<AppCrmCustomerGroupVO> findMyClientGroupList(String memberId);
	
	/**
	 * 获取客户首页的统计数据
	 * @param memberId
	 * @param toCurrency
	 * @return
	 */
	public AppClientCountVO findClientCount(String memberId,String toCurrency);
	
	/**
	 * 获取IFA的客户列表
	 * @author zxtan
	 * @date 2017-03-16
	 * @param clientSearchVO
	 * @return
	 */
	public List<AppClientItemVO> findMyClientList(ClientSearchVO clientSearchVO,Date startDate,Date endDate);
	
	/**
	 * 获取IFA个人信息
	 * @author zxtan
	 * @date 2017-03-16
	 * @param memberId	
	 * */
	public MemberIfa findMemberIfa(String memberId);
	
	/**
	 * 检查客户是是否生日
	 * @author zxtan
	 * @date 2017-03-16
	 * @param ifaId
	 * @param memberId
	 * @param days
	 * @return
	 */
	public String findBirthDayCustomer(String ifaId, String memberId, int days);
	
	/**
	 * 检查客户是否有日程
	 * @author zxtan
	 * @date 2017-03-16
	 * @param ifaId
	 * @param memberId
	 * @return
	 */
	public String findSchedultCustomer(String ifaId, String memberId, Date startDate, Date endDate);
	
	/**
	 * 获取IFA正式客户的事件统计
	 * @author zxtan
	 * @date 2017-03-17
	 * @param memberId
	 * @return
	 */
	public AppEventVO findExistingEventList(String memberId);
	/**
	 * 获取IFA潜在客户的事件统计
	 * @author zxtan
	 * @date 2017-03-22
	 * @param memberId
	 * @return
	 */
	public AppProspectEventVO findProspectEventList(String memberId);
}
