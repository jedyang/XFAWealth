package com.fsll.wmes.investor.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.crm.vo.OrderHistoryVO;
import com.fsll.wmes.entity.IfaMigrateHist;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.OrderHistory;
import com.fsll.wmes.entity.PortfolioHoldAccount;
import com.fsll.wmes.ifa.service.IfaMigrateHistService;
import com.fsll.wmes.investor.service.OrderHistoryService;
import com.fsll.wmes.member.service.MemberBaseService;

@Service("orderHistoryService")
//@Transactional
public class OrderHistoryServiceImpl extends BaseService implements
		OrderHistoryService {
	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
	private IfaMigrateHistService ifaMigrateHistService;
	/**
	 * 获取Ifa管理的某个客户的投资记录
	 * @author zxtan
	 * @date 2016-09-07
	 */
	@Override
	public JsonPaging findOrderHistoryListForIfa(JsonPaging jsonPaging,
			String ifaMemberId, String customerMemberId) {
		// TODO Auto-generated method stub
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from OrderHistory l ");
		hql.append(" where l.ifa.member.id=? and l.member.id=? ");
		params.add(ifaMemberId);
		params.add(customerMemberId);
				
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging , false);

		return jsonPaging;
	}
	
	/**
	 * 获取Ifa管理的某个客户的投资记录
	 * @author zxtan
	 * @date 2016-09-12
	 */
	@Override
	public JsonPaging findOrderHistoryListForIfa(JsonPaging jsonPaging,String langCode,String ifaMemberId, String customerMemberId,OrderHistory orderHistory) {
		// TODO Auto-generated method stub
		Date beginDate = orderHistory.getBeginDate();
		Date endDate = orderHistory.getEndDate();
		String status = orderHistory.getStatus();
//		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("SELECT oh.*,fid.fund_name as product_name,ia.account_no FROM order_history oh ");
		hql.append(" LEFT JOIN investor_account ia ON oh.account_id=ia.id ");
		hql.append(" LEFT JOIN fund_info fi ON oh.product_id = fi.product_id ");
		hql.append(" LEFT JOIN fund_info_"+langCode+" fid ON fi.id = fid.id ");
		hql.append(" LEFT JOIN member_ifa mi ON mi.id = oh.ifa_id ");		
		hql.append(" where mi.member_id='"+ifaMemberId+"' and oh.member_id='"+customerMemberId+"' ");
		
		if( null != status && !"".equals(status)){
			hql.append(" and oh.status = '"+status+"' ");
		} 
		
		if( null != beginDate){
			hql.append(" and oh.order_date >= '"+DateUtil.dateToDateString(beginDate, "yyyy-MM-dd HH:mm:ss")+"' ");			
		}
				
		if( null != endDate){
			hql.append(" and oh.order_date <= '"+DateUtil.dateToDateString(endDate, "yyyy-MM-dd HH:mm:ss")+"' ");
		}
		
		hql.append(" order by oh.order_date desc ");
		
		int curPage = 1;
		if (null!=jsonPaging.getPage())
			curPage = jsonPaging.getPage();
		
		int pageSize = 0;
		if (null!=jsonPaging.getRows())
			pageSize = jsonPaging.getRows();
		
		hql.append(" LIMIT "+ (curPage - 1) * pageSize + ","+pageSize );
				
		List list = springJdbcQueryManager.springJdbcQueryForList(hql.toString());
		List<OrderHistoryVO> listVO = new ArrayList<OrderHistoryVO>();
		if(!list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Map map = (HashMap) list.get(i);
				OrderHistoryVO vo = new OrderHistoryVO();
				String id = map.get("id") == null? "" : map.get("id").toString();
				String transactionUnit = map.get("transaction_unit") == null? "" : map.get("transaction_unit").toString();
				String transactionAmount = map.get("transaction_amount") == null? "" : map.get("transaction_amount").toString();
				String fee = map.get("fee") == null? "" : map.get("fee").toString();
				String orderType = map.get("order_type") == null? "" : map.get("order_type").toString();
				String orderDate = map.get("order_date") == null? "" : map.get("order_date").toString();
				String statusName = map.get("status") == null? "" : map.get("status").toString();
				String productName = map.get("product_name") == null? "" : map.get("product_name").toString();
				String accountNo = map.get("account_no") == null? "" : map.get("account_no").toString();
				vo.setId(id);
				vo.setTransactionUnit( "".equals(transactionUnit)? null :  Double.parseDouble( transactionUnit));
				vo.setTransactionAmount(  "".equals(transactionAmount)? null :  Double.parseDouble(transactionAmount));
				vo.setFee( "".equals(fee)? null :  Double.parseDouble(fee));
				vo.setOrderType(orderType);
				vo.setOrderDate(DateUtil.getDate(orderDate, "yyyy-MM-dd HH:mm:ss"));
				vo.setStatus(statusName);
				vo.setProductName(productName);
				vo.setAccountNo(accountNo);
				
				listVO.add(vo);
			}
//			jsonPaging.setTotal(listVO.size());
		}

		jsonPaging.setList(listVO);
		
		
		return jsonPaging;
	}

	/**
	 * 根据ifa的memberId查找客户订单
	 * @author michael
	 * @param memberId
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public List<OrderHistory> findOrderHistoryByIfa(String memberId) {
		String hql=" from OrderHistory r where r.ifa.member.id=? ";
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		List<OrderHistory> list= (List<OrderHistory>)this.baseDao.find(hql, params.toArray(), false);
		return list;
	}
	
	/**
	 * 检测客户订单是否存在
	 * @author michael
	 * @param ifaMemberId
	 * @param memberId
	 * @return
	 */
	public boolean checkIfExistOrderHistory(String ifaMemberId, String memberId) {
		String hql=" from OrderHistory r where r.ifa.member.id=? and r.member.id=? ";
		List<Object> params=new ArrayList<Object>();
		params.add(ifaMemberId);
		params.add(memberId);
		List list=this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			return true;
		}
		return false;
	}
	
	/**
	 * 修改IFA的客户订单到另一IFA
	 * @author michael
	 * @date 2017-3-1
	 * @param fromMemberId 源IFA
	 * @param toMemberId 目标IFA
	 * @return Boolean
	 */
	public Boolean migrateOrderHistory(String fromMemberId,String toMemberId,MemberBase createBy) {
		List<OrderHistory> list = findOrderHistoryByIfa(fromMemberId);
		if (null!=list && !list.isEmpty()){
			MemberIfa ifa = memberBaseService.findIfaMember(toMemberId);
			for (OrderHistory f: list){
				//已存在的不加？
				if (!checkIfExistOrderHistory(toMemberId, f.getIfa().getMember().getId())){
					boolean status = false;
					try{
						//更新方式
						f.setIfa(ifa);
						this.baseDao.update(f);
						
						status = true;
		            } catch (Exception e) {
		                // TODO: handle exception
		            }
		            
					//保存日志
					IfaMigrateHist hist = new IfaMigrateHist();
					hist.setCreateBy(createBy);
					hist.setCreateTime(new Date());
					hist.setFromMember(memberBaseService.findById(fromMemberId));
					hist.setToMember(ifa.getMember());
					hist.setCusMember(f.getMember());
					hist.setDataType("OrderHistory");
					hist.setStatus(status?"1":"0");
					ifaMigrateHistService.saveOrUpdate(hist);
				}
			}
			return true;
		}
		return false;
	}
}
