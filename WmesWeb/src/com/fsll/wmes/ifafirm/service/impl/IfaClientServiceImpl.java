/**
 * 
 */
package com.fsll.wmes.ifafirm.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.crm.vo.ClientSearchVO;
import com.fsll.wmes.crm.vo.ClientsBasicVO;
import com.fsll.wmes.crm.vo.CrmClientForIfaVO;
import com.fsll.wmes.crm.vo.CrmClientVO;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.OrderHistory;
import com.fsll.wmes.entity.OrderPlan;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioHoldAccount;
import com.fsll.wmes.entity.PortfolioInfo;
import com.fsll.wmes.ifa.service.IfaManageService;
import com.fsll.wmes.ifafirm.service.IfaClientService;
import com.fsll.wmes.ifafirm.vo.TransRecordVO;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.trade.vo.TransactionRecordFilterVO;
import com.fsll.wmes.trade.vo.TransactionRecordVO;

/**
 * @author scshi_u330p
 *@date 20161219
 */
@Service("ifaClientService")
//@Transactional
public class IfaClientServiceImpl  extends BaseService implements IfaClientService{
	
	@Autowired
	protected MemberBaseService memberBaseService; 
	@Autowired
	private IfaManageService ifaManageService;
	@Autowired
	private SysParamService sysParamService;
	/**
	 * 获取客户列表
	 * @author scshi
	 * @date 2016-09-28
	 */
	@Override
	public JsonPaging findExistingCustomerListForIfa(JsonPaging jsonPaging,String ifaMemberId,String areaId,String period,String clientStatus,String keyword,String character,Boolean isDis){
		StringBuilder sBuilder = new StringBuilder();
		List<Object> params=new ArrayList<Object>();
		sBuilder.append("SELECT c.id,c.ifa_id,c.member_id,c.nick_name,c.icon_url,c.remark,c.create_time,c.sales_stage_id,c.is_important,mb.email,mb.mobile_code,mb.mobile_number,");
		sBuilder.append("mi.first_name,mi.last_name,mi.name_chn,mi.gender,mi.nationality,mi.born,g.group_name"); 
		if(period != null && !"".equals(period)){
			String intervalUnit = "0 DAY";
			if( "1day".equalsIgnoreCase(period)){
				intervalUnit = "0 DAY";
			} else if( "1week".equalsIgnoreCase(period)){
				intervalUnit = "1 WEEK";
			} else if( "2week".equalsIgnoreCase(period)){
				intervalUnit = "2 WEEK";
			} else if( "1month".equalsIgnoreCase(period)){
				intervalUnit = "1 MONTH";
			}
			//birthday_remind
			sBuilder.append(" ,CASE WHEN CAST(CONCAT(YEAR(NOW()),DATE_FORMAT(mi.born,'-%m-%d'))AS DATE) >= CAST(DATE_FORMAT(NOW(),'%y-%m-%d') AS DATE) AND CAST(CONCAT(YEAR(NOW()),DATE_FORMAT(mi.born,'-%m-%d'))AS DATE) <= DATE_ADD(NOW(),INTERVAL "+intervalUnit+") THEN 1 ELSE 0 END AS birthday_remind,");
			//appointment_remind
			sBuilder.append(" ( SELECT COUNT(0) FROM crm_schedule WHERE customer_id=c.id AND start_date >= CAST(DATE_FORMAT(NOW(),'%y-%m-%d') AS DATE) AND start_date < DATE_ADD(NOW(),INTERVAL "+intervalUnit+")) AS appointment_remind,");
			//rpq_remind
			sBuilder.append(" ( SELECT COUNT(0) FROM rpq_exam r WHERE r.member_id = c.member_id  AND CAST(CONCAT(YEAR(NOW()),DATE_FORMAT(r.expire_date,'-%m-%d'))AS DATE) >=CAST(DATE_FORMAT(NOW(),'%y-%m-%d') AS DATE)  AND CAST(CONCAT(YEAR(NOW()),DATE_FORMAT(r.expire_date,'-%m-%d'))AS DATE) <= DATE_ADD(NOW(),INTERVAL "+intervalUnit+") ) as rpq_remind,");
			//doc_remind
			sBuilder.append(" ( SELECT COUNT(0) FROM investor_doc d WHERE d.member_id = c.member_id AND CAST(CONCAT(YEAR(NOW()),DATE_FORMAT(d.expire_date,'-%m-%d'))AS DATE) >=CAST(DATE_FORMAT(NOW(),'%y-%m-%d') AS DATE) AND CAST(CONCAT(YEAR(NOW()),DATE_FORMAT(d.expire_date,'-%m-%d'))AS DATE) <= DATE_ADD(NOW(),INTERVAL "+intervalUnit+")) as doc_remind");
		}
		sBuilder.append(" FROM crm_customer c ");
		sBuilder.append(" LEFT JOIN member_base mb ON c.member_id=mb.id ");
		sBuilder.append(" LEFT JOIN member_individual mi ON mb.id=mi.member_id ");
		sBuilder.append(" LEFT JOIN member_ifa ifa ON c.ifa_id=ifa.id ");
		sBuilder.append(" LEFT JOIN crm_customer_group_relationship s ON c.id = s.customer_id ");
		sBuilder.append(" LEFT JOIN crm_customer_group g ON s.group_id = g.id");
		sBuilder.append(" WHERE c.client_type= '1' ");
		
		if(StringUtils.isNotBlank(ifaMemberId)){
			if(isDis){//代理商登录，查询代理商下所有ifa公司
				sBuilder.append(" AND ifa.member_id in ( ");
				sBuilder.append(" select ma.member_id from member_admin ma where ma.ifafirm_id in ( ");
				sBuilder.append(" select fd.ifafirm_id from ifafirm_distributor fd where fd.distributor_id=?)) ");
			}else{
				sBuilder.append(" AND ifa.member_id= (select t.member_id from member_admin ma where ma.ifafirm_id=?) ");	
			}
			params.add(ifaMemberId);
		}
		
//		if(ifaMemberId != null && !"".equals(ifaMemberId)){
//			sBuilder.append(" AND ifa.member_id= ? ");	
//			params.add(ifaMemberId);
//		}
//		if(clientStatus != null && !"".equals(clientStatus)){
//			if("Customer-Care".equalsIgnoreCase(clientStatus)){
//				sBuilder.append(" AND EXISTS (SELECT p.id FROM portfolio_info p INNER JOIN portfolio_hold h ON p.id= h.portfolio_id ");
//				sBuilder.append(" WHERE p.member_id=c.member_id AND c.ifa_id= p.ifa_id ) ");				
//			}else if("Open-Account".equalsIgnoreCase(clientStatus)){
//				sBuilder.append(" AND EXISTS (SELECT ia.id FROM investor_account ia WHERE c.ifa_id=ia.ifa_id AND c.member_id=ia.member_id ");
//				sBuilder.append(" AND ia.open_status not in ('0','1') ) ");
//			}else if("Portfolio".equalsIgnoreCase(clientStatus)){
//				sBuilder.append(" AND EXISTS (SELECT cp.id FROM crm_proposal cp INNER JOIN portfolio_info p ON cp.id=p.proposal_id  ");
//				sBuilder.append(" INNER JOIN order_plan op ON p.id = op.portfolio_id ");
//				sBuilder.append(" WHERE p.member_id=c.member_id AND c.ifa_id= p.ifa_id AND cp.status='1' AND op.finish_status='0' ) ");
//			}else if("Proposal".equalsIgnoreCase(clientStatus)){
//				sBuilder.append(" AND EXISTS (SELECT p.id FROM crm_proposal p  ");
//				sBuilder.append(" WHERE p.member_id=c.member_id AND p.ifa_id= c.ifa_id AND p.status='0' ) ");
//			}else if("Not-Yet-Invest".equalsIgnoreCase(clientStatus)){
//				sBuilder.append(" AND EXISTS (SELECT ia.id FROM investor_account ia WHERE c.ifa_id=ia.ifa_id AND c.member_id=ia.member_id AND ia.open_status ='1' ) ");
//				sBuilder.append(" AND EXISTS (SELECT p.id FROM crm_proposal p WHERE p.member_id=c.member_id AND p.ifa_id= c.ifa_id AND p.status='1' ) ");
//			}else {
//				sBuilder.append(" AND 1=2 ");
//			}			
//		}else {
//			sBuilder.append(" AND 1=2 ");
//		}
		
		if(keyword != null && !"".equals(keyword)){
			sBuilder.append(" AND ( mb.email LIKE ? OR mb.mobile_number LIKE ? OR c.nick_name LIKE ? ) ");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
		}
		if(null!=character && !"".equals(character)){
			sBuilder.append(" and c.nick_name LIKE ? ");
			params.add(character+"%");
		}
		
		sBuilder.append(" ORDER BY c.create_time DESC ");
				
		jsonPaging = springJdbcQueryManager.springJdbcQueryForPaging(sBuilder.toString(), params.toArray(), jsonPaging);
		return jsonPaging;
	}
	
	/**
	 * ifafirm-distributor 交易记录分页
	 * */
	public JsonPaging getTransactionRecord(MemberSsoVO loginUser,
			JsonPaging jsonPaging, TransactionRecordFilterVO filter){
		Integer subMemberType = loginUser.getSubMemberType();//31--distributor  22--ifafirm
		
		try {
			if(loginUser != null){
//				StringBuffer hql = new StringBuffer("" +
//						" SELECT" +
//						" p,o,m" +
//						" FROM" +
//						" OrderPlan o" +
//						" LEFT JOIN" +
//						" PortfolioInfo p" +
//						" ON" +
//						" p.id=o.portfolio.id" +
//						" LEFT JOIN" +
//						" MemberBase m" +
//						" ON" +
//						" m.id=p.member.id" +
//						" LEFT JOIN" +
//						" MemberIfa i" +
//						" ON" +
//						" i.id=p.memberIfa.id" +
//						" WHERE" +
//						" i.member=?" +
//						"");
				//StringBuffer hql = new StringBuffer("select p,o,m from OrderPlan o LEFT JOIN PortfolioInfo p on p.id=o.portfolio.id ");
				StringBuffer hql = new StringBuffer("select p,o,m from OrderPlan o LEFT JOIN PortfolioHold p on p.id=o.portfolioHold.id ");
				hql.append(" LEFT JOIN MemberBase m ON m.id=p.member.id ");
				List<Object> params = new ArrayList<Object>();
				MemberBase baseUser = memberBaseService.findByCode(loginUser.getLoginCode());
				hql.append(" LEFT JOIN MemberAdmin ad on ad.member.id=m.id ");
				
				if(31==subMemberType){//代理商查看交易记录
					hql.append(" LEFT JOIN MemberDistributor dis on dis.id=ad.distributor.id ");
					hql.append(" LEFT JOIN IfafirmDistributor fdis on fdis.distributor.id=dis.id ");
					hql.append(" LEFT JOIN MemberIfafirm firm on firm.id=fdis.ifafirm.id ");
				}else{//ifa公司查看交易记录
					hql.append(" LEFT JOIN MemberIfafirm firm on firm.id=ad.ifafirm.id ");
				}
				
				hql.append(" LEFT JOIN MemberIfaIfafirm f on f.ifafirm.id=firm.id ");
				hql.append(" LEFT JOIN MemberIfa i on i.id=f.ifa.id ");
				hql.append(" where ad.member=? ");
				params.add(baseUser);
//				hql.append(" where 1=1 ");//@@测试
				if(filter != null){
					String period = filter.getPeriod();
					String periodType = filter.getPeriodType();
					String fromDate = filter.getFromDate();
					String toDate = filter.getToDate();
					String finishStatus = filter.getFinishStatus();
					String keyWord = filter.getKeyWord();
					// 过滤期间
					if (StringUtils.isNotBlank(period) && StringUtils.isNotBlank(periodType)){
						Calendar calendar = Calendar.getInstance();
						if(CommonConstants.DATE_TYPE_DAY.equals(periodType)){
							calendar.add(Calendar.DATE, Integer.parseInt(period));
						}else if(CommonConstants.DATE_TYPE_MONTH.equals(periodType)){
							calendar.add(Calendar.MONTH, Integer.parseInt(period));
						}else if(CommonConstants.DATE_TYPE_YEAR.equals(periodType)){
							calendar.add(Calendar.YEAR, Integer.parseInt(period));
						}
						hql.append(" AND o.createTime BETWEEN ? AND CURDATE()");
						params.add(calendar.getTime());
					}
					// 过滤指定时间
					if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						hql.append(" AND o.createTime BETWEEN ? AND ?");
						params.add(dateFormat.parse(fromDate));
						params.add(dateFormat.parse(toDate));
					}
					// 过滤指定时间
					if (StringUtils.isNotBlank(finishStatus)) {
						//判定是否筛选多个状态
						if(finishStatus.indexOf(",") > -1){
							String[] status = finishStatus.split(",");
							hql.append(" AND (o.finishStatus=? OR o.finishStatus=?)");
							params.add(status[0]);
							params.add(status[1]);
						}else{
							hql.append(" AND o.finishStatus=?");
							params.add(finishStatus);
						}
					}
					// 过滤关键字（客户名称、组合名称）
					if (StringUtils.isNotBlank(keyWord)) {
						hql.append(" AND (p.portfolioName LIKE ? OR m.nickName LIKE ?)");
						params.add("%"+keyWord+"%");
						params.add("%"+keyWord+"%");
					}
				}
				jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
				if(null!=jsonPaging.getList() && !jsonPaging.getList().isEmpty()){
					List<Object[]> result = jsonPaging.getList();
					List<TransactionRecordVO> recordVOs = new ArrayList<TransactionRecordVO>();
					for (Object[] objects : result) {
						TransactionRecordVO recordVO = new TransactionRecordVO();
						PortfolioHold plan = (PortfolioHold)objects[0];
						PortfolioInfo portfolioInfo = plan.getPortfolio();//(PortfolioInfo) objects[0];
						Map<String,Object> totalBuyMap = null;
						Map<String,Object> totalSellMap = null;
						if (StringUtils.isNotBlank(filter.getToCurrency())) {
							totalBuyMap = this.transationAmountTotal(portfolioInfo, filter.getToCurrency(), CommonConstants.TRAN_TYPE_SELL);
							totalSellMap = this.transationAmountTotal(portfolioInfo, filter.getToCurrency(), CommonConstants.TRAN_TYPE_BUY);
						}else{
							totalBuyMap = this.transationAmountTotal(portfolioInfo, CommonConstants.DEF_CURRENCY, CommonConstants.TRAN_TYPE_SELL);
							totalSellMap = this.transationAmountTotal(portfolioInfo, CommonConstants.DEF_CURRENCY, CommonConstants.TRAN_TYPE_BUY);
						}
						recordVO.setTotalBuy((Double) totalBuyMap.get("totalAmount"));
						recordVO.setTotalSell((Double) totalSellMap.get("totalAmount"));
						recordVO.setToCurrency((String) totalBuyMap.get("toCurrency"));
						recordVO.setOrderPlan((OrderPlan) objects[1]);
						recordVO.setPortfolio(portfolioInfo);
						recordVO.setClient((MemberBase) objects[2]);
						recordVOs.add(recordVO);
					}
					jsonPaging.getList().clear();
					jsonPaging.getList().addAll(recordVOs);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return jsonPaging;
	}
	
	
	/**
	 * 获取交易总金额
	 * @author wwluo
	 * @data 2016-11-25
	 * @param portfolioInfo 组合
	 * @param toCurrency 货币
	 * @param tranType 交易方向，‘B’：买入、‘S’：卖出
	 * @return
	 */
	public Map<String,Object> transationAmountTotal(PortfolioInfo portfolioInfo,String toCurrency,String tranType){
		Map<String,Object> map = new HashMap<String, Object>();
		if(portfolioInfo != null 
				&& StringUtils.isNotBlank(toCurrency) 
				&& StringUtils.isNotBlank(tranType)){
			StringBuffer hql = new StringBuffer( "" +
					" SELECT" +
					" get_exchange_rate(e.fundCurrencyCode,'"+toCurrency+"',f.lastNav) AS lastNav" +
					//" f,p" +
					" ,p.unit" +
					" FROM" +
					" OrderPlanProduct p" +
					
					" LEFT JOIN" +
					" OrderPlan o" +
					" ON o.id=p.plan.id" +
					
					" LEFT JOIN" +
					" FundInfo f" +
					" ON f.product.id=p.product.id" +
					
					" LEFT JOIN" +
					" FundInfoEn e" +
					" ON e.id=f.id" +
					
					" WHERE" +
					" p.original=0" +
					" AND" +
					" o.portfolio=?" +
					" AND" +
					" p.tranType=?" +
					"");
			List<Object> params = new ArrayList<Object>();
			params.add(portfolioInfo);
			params.add(tranType);
			List<Object[]> result = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(result != null && !result.isEmpty()){
				Double totalAmount = 0.0;
				for (Object[] obj : result) {
					Double lastNav = (Double) obj[0];
					Integer unit = (Integer) obj[1];
					if(lastNav != null && unit != null){
						totalAmount = totalAmount+lastNav*unit;
					}
				}
				map.put("toCurrency", toCurrency);
				map.put("totalAmount", totalAmount);
			}
		}
		return map;
	}

	/**
	 * 获取IFA Firm的所有客户的概况信息
	 * @author michael
	 * @date 2016-12-15
	 * @param ifaFirmId
	 * @return
	 */
	public ClientsBasicVO findClientsBasicByFirm(String ifaFirmId) {
		ClientsBasicVO vo=new ClientsBasicVO();
		vo.setTotalNum(0);
		vo.setProfitNum(0);
		vo.setLossNum(0);
		StringBuilder hql=new StringBuilder();
		hql.append("select r.id,sum(h.totalReturnRate) from  CrmCustomer r left join PortfolioHold h on r.member.id=h.member.id ");
		hql.append(" where r.ifa.id in (select ifa.id from MemberIfaIfafirm where ifafirm.id=?) and r.clientType='1'");
		hql.append(" group by r.id");
	    List<Object> params=new ArrayList<Object>();
	    params.add(ifaFirmId);
		List list=this.baseDao.find(hql.toString(), params.toArray(), false);
		if(list!=null && !list.isEmpty()){
			Iterator it=list.iterator();
			while (it.hasNext()) {
				Object[] objects = (Object[]) it.next();
				if(null!=objects[1]){
					double returnRate=Double.parseDouble(objects[1].toString());
					if(returnRate<0)
						vo.setLossNum(vo.getLossNum()+1);
					else {
						vo.setProfitNum(vo.getProfitNum()+1);
					}
				}
				vo.setTotalNum(vo.getTotalNum()+1);
			}
		}
		return vo;
	}
	
	/**
	 * 获取IFA Firm的所有客户的总资产
	 * @author michael
	 * @date 2016-12-15
	 * @param ifaFirmId
	 * @return
	 */
	public double findClientAuMByFirm(String ifaFirmId,String currency) {
		double total=0;
		StringBuilder hql=new StringBuilder();
		
		hql.append(" select c from CrmCustomer r left join PortfolioHold h on r.ifa.id=h.ifa.id and r.member.id=h.member.id");
		hql.append(" left join PortfolioHoldAccount c on h.id=c.portfolioHold.id");
		hql.append(" where r.ifa.id in (select ifa.id from MemberIfaIfafirm where ifafirm.id=?)  and r.clientType='1'");
		List<Object> params=new ArrayList<Object>();
		params.add(ifaFirmId);
		List list=this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			Iterator it=list.iterator();
			while (it.hasNext()) {
				//InvestorAccountCurrency accountCurrency = (InvestorAccountCurrency) it.next();
				PortfolioHoldAccount account=(PortfolioHoldAccount) it.next();
				if(null==account)
					continue;
				double cashAvailable=account.getCashAvailable()==null?0:account.getCashAvailable();
				double cashHold=account.getCashHold()==null?0:account.getCashHold();
				double marketValue=account.getMarketValue()==null?0:account.getMarketValue();
				double sum=cashAvailable+cashHold+marketValue;
				if(!currency.equals(account.getBaseCurrency())){
					double rate=getExchangeRate(account.getBaseCurrency(), currency);
					total+=sum*rate;
				}else {
					total+=sum;
				}
			}
		}
		return total;
	}
	
	/**
	 * 获取ifa firm的客户列表
	 * @author michael
	 * @param ifaFirmId
	 * @param keyWord
	 * @param type 客户类型
	 * @return
	 */
	public List<CrmClientForIfaVO> findClientForIfaFirm(String ifaFirmId, String keyWord,String type) {
		 List<CrmClientForIfaVO> list=new ArrayList<CrmClientForIfaVO>();
		StringBuilder hql=new StringBuilder();
		List<Object> params=new ArrayList<Object>();
		hql.append("from CrmCustomer r where r.isValid='1'  and r.clientType=?");
		params.add(type);
		hql.append(" and r.ifa.id in (select ifa.id from MemberIfaIfafirm where ifafirm.id=?)");
		params.add(ifaFirmId);

		if(null!=keyWord && !"".equals(keyWord)){
			hql.append(" and r.nickName like ?");
			params.add("%"+keyWord+"%");
		}
		List resultList=this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=resultList){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				CrmCustomer customer = (CrmCustomer) it.next();
				CrmClientForIfaVO vo=new CrmClientForIfaVO();
				vo.setId(customer.getId());
				vo.setMember(customer.getMember());
				vo.setMemberId(customer.getMember().getId());
				vo.setNickName(customer.getNickName());
				vo.setCreateDate(DateUtil.getDateStr(customer.getCreateTime()));
				vo.setEmail(customer.getMember().getEmail());
				vo.setContact(customer.getMember().getMobileNumber());
				vo.setIconUrl(customer.getMember().getIconUrl());
				list.add(vo);
			}
		}
		return list;
	}
	
	/**
	 * 获取IFA Firm的客户列表
	 * @author michael
	 * @date 2016-12-15
	 * @param jsonPaging
	 * @param clientSearchVO
	 * @return
	 */
	public JsonPaging findClentsByIfaFirm(JsonPaging jsonPaging, ClientSearchVO clientSearchVO) {
		//ifaFirmId varchar(36),ifaFirmName varchar(100),ifaName varchar(100),distId varchar(36),disName varchar(100),
		//memberId varchar(2000),clientName varchar(50),minReturn varchar(3),maxReturn varchar(3) ,
	    //minAuM varchar(20),maxAuM varchar(20),currency varchar(10),statistic varchar(100),orderStr VARCHAR(50),
	    //sortStr VARCHAR(50), startIndex INT, pageSize  INT
		String sql=" CALL pro_getIfafirmClients(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		List<Object> params=new ArrayList<Object>();
		params.add(clientSearchVO.getIfaFirmId());
		params.add(clientSearchVO.getIfaFirmName());
		params.add(clientSearchVO.getIfaName());
		params.add(clientSearchVO.getDistributorId());
		params.add(clientSearchVO.getDistName());
		params.add(clientSearchVO.getClientId());
		params.add(clientSearchVO.getClientName());
		params.add(clientSearchVO.getMinReturnRate());
		params.add(clientSearchVO.getMaxReturnRate());
		params.add(clientSearchVO.getMinAuM());
		params.add(clientSearchVO.getMaxAuM());
		params.add(clientSearchVO.getMinMarket());
		params.add(clientSearchVO.getMaxMarket());
		params.add(clientSearchVO.getCurrency());
		params.add(clientSearchVO.getStatistic());
		
		params.add(jsonPaging.getOrder());
		params.add(jsonPaging.getSort());
		params.add((jsonPaging.getPage()-1)*jsonPaging.getRows());
		params.add(jsonPaging.getRows());
		String currencyName=sysParamService.findNameByCode(clientSearchVO.getCurrency(), clientSearchVO.getLangCode());
		List resultList=this.springJdbcQueryManager.springJdbcQueryForList(sql, params.toArray());
		List<CrmClientVO> list=new ArrayList<CrmClientVO>();
		if(null!=resultList && !resultList.isEmpty()){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				HashMap map = (HashMap) it.next();
				CrmClientVO vo=new CrmClientVO();
				vo.setCurrencyName(currencyName);
				String id=getMapValue(map, "id");
				String memberId=getMapValue(map, "member_id");
				String nickName=getMapValue(map, "nick_name");
				String totalRate=getMapValue(map, "total_return_rate");
				String totalValue=getMapValue(map, "total_return_value");
				String createTime=getMapValue(map, "create_time");
				String totalAuM=getMapValue(map, "totalAuM");
				String ifaId=getMapValue(map, "ifa_id");
				String totalMarket=getMapValue(map, "totalMarket");
				//String ifaName=StrUtils.getString(getMapValue(map, "first_name"))+" "+StrUtils.getString(getMapValue(map, "last_name"));
				
				vo.setId(id);
				vo.setMemberId(memberId);
				String clientName=getCommonMemberName(memberId, clientSearchVO.getLangCode(), "2");
				vo.setClientName(clientName);
				vo.setNickName(nickName);
				vo.setIfaId(ifaId);
				
				MemberIfa ifa=ifaManageService.findIfaById(ifaId);
				if(null!=ifa){
					String ifaName=getCommonMemberName(ifa.getMember().getId(), clientSearchVO.getLangCode(), "2");
					vo.setIfaName(ifaName);
				}
				vo.setTotalReturnRate(null!=totalRate && !"".equals(totalRate)?Double.parseDouble(totalRate):0);
				vo.setTotalReturn(null!=totalValue && !"".equals(totalValue)?Double.parseDouble(totalValue):0);
				vo.setAum(null!=totalAuM && !"".equals(totalAuM)?Double.parseDouble(totalAuM):0);
				vo.setMarket(null!=totalMarket && !"".equals(totalMarket)?Double.parseDouble(totalMarket):0);
				if(null!=createTime && !"".equals(createTime)){
					Date createDate=DateUtil.getDate(createTime);
					vo.setInvDate(DateUtil.dateToDateString(createDate, clientSearchVO.getDateFormat()));
					try {
						int days=DateUtil.daysBetween(createDate, DateUtil.getCurDate());
						vo.setDays(String.valueOf(days));
					} catch (ParseException e) {
						vo.setDays("0");
						e.printStackTrace();
					}
				}else {
					vo.setDays("0");
					vo.setInvDate("");
				}
				list.add(vo);
				
			}
		}
		jsonPaging.setList(list);
		
		//ifaFirmId varchar(36),ifaFirmName varchar(100),ifaName varchar(100),distId varchar(36),disName varchar(100),
		//memberId VARCHAR(2000),clientName VARCHAR(50),minReturn VARCHAR(3),maxReturn VARCHAR(3) ,
	    //minAuM VARCHAR(20),maxAuM VARCHAR(20),currency VARCHAR(10),statistic VARCHAR(1000)
		sql=" CALL pro_getIfafirmClientsTotal(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		params=new ArrayList<Object>();
		params.add(clientSearchVO.getIfaFirmId());
		params.add(clientSearchVO.getIfaFirmName());
		params.add(clientSearchVO.getIfaName());
		params.add(clientSearchVO.getDistributorId());
		params.add(clientSearchVO.getDistName());
		params.add(clientSearchVO.getClientId());
		params.add(clientSearchVO.getClientName());
		params.add(clientSearchVO.getMinReturnRate());
		params.add(clientSearchVO.getMaxReturnRate());
		params.add(clientSearchVO.getMinAuM());
		params.add(clientSearchVO.getMaxAuM());
		params.add(clientSearchVO.getMinMarket());
		params.add(clientSearchVO.getMaxMarket());
		params.add(clientSearchVO.getCurrency());
		params.add(clientSearchVO.getStatistic());
		resultList=this.springJdbcQueryManager.springJdbcQueryForList(sql, params.toArray());
		if(null!=resultList && !resultList.isEmpty()){
			HashMap map=(HashMap)resultList.get(0);
			String total=getMapValue(map, "totalCount");
			if(null!=total && !"".equals(total)){
				jsonPaging.setTotal(Integer.parseInt(total));
			}else {
				jsonPaging.setTotal(0);
			}
		}
		return jsonPaging;
	}
	
	/**
	 * 获取Distributor的客户列表
	 * @author michael
	 * @param distributorId
	 * @param keyWord
	 * @param type 客户类型
	 * @return
	 */
	public List<CrmClientForIfaVO> findClientForDistributor(String distributorId, String keyWord,String type) {
		 List<CrmClientForIfaVO> list=new ArrayList<CrmClientForIfaVO>();
		StringBuilder hql=new StringBuilder();
		List<Object> params=new ArrayList<Object>();
		hql.append("from CrmCustomer r where r.isValid='1' and r.clientType=?");
		params.add(type);
		hql.append(" and r.ifa.id in (select i.ifa.id from MemberIfaIfafirm i left join IfafirmDistributor d on i.ifafirm.id=d.ifafirm.id where d.distributor.id=?)");
		params.add(distributorId);

		if(null!=keyWord && !"".equals(keyWord)){
			hql.append(" and r.nickName like ?");
			params.add("%"+keyWord+"%");
		}
		List resultList=this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=resultList){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				CrmCustomer customer = (CrmCustomer) it.next();
				CrmClientForIfaVO vo=new CrmClientForIfaVO();
				vo.setId(customer.getId());
				vo.setMemberId(customer.getMember().getId());
				vo.setNickName(customer.getNickName());
				vo.setCreateDate(DateUtil.getDateStr(customer.getCreateTime()));
				vo.setEmail(customer.getMember().getEmail());
				vo.setContact(customer.getMember().getMobileNumber());
				vo.setIconUrl(customer.getMember().getIconUrl());
				list.add(vo);
			}
		}
		return list;
	}

	/**
	 * 交易记录查询
	 * @author wwluo
	 * @param memberAdmin 当前管理员
	 * @param jsonPaging
	 * @param transRecordVO 查询条件
	 * @param langCode
	 * @param currencyCode
	 * @return jsonPaging
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JsonPaging getTransRecords(MemberAdmin memberAdmin,
			JsonPaging jsonPaging, TransRecordVO transRecordVO, String langCode, String currencyCode) {
		String dateFormat = memberAdmin.getMember().getDateFormat();
		if (StringUtils.isBlank(dateFormat)) {
			dateFormat = CommonConstants.FORMAT_DATE_TIME;
		}
		Object[] result = getQueryConditions(memberAdmin, transRecordVO, langCode, currencyCode);
		List<Object> temp = this.baseDao.find((String) result[0], (Object[]) result[1], false);
		jsonPaging = this.baseDao.selectJsonPaging((String) result[0], (Object[]) result[1], jsonPaging, false);
		if(temp != null && !temp.isEmpty()){
			jsonPaging.setTotal(temp.size());
		}
		if(jsonPaging.getList() != null && !jsonPaging.getList().isEmpty()){
			List<TransRecordVO> vos = new ArrayList<TransRecordVO>();
			List<Object[]> objects = jsonPaging.getList();
			for (Object[] objs : objects) {
				OrderHistory history = (OrderHistory) objs[0];
				String fundName = (String) objs[1];
				String symbolCode = (String) objs[2];
				String companyName = (String) objs[3];
				String firmLogo = (String) objs[4];
				TransRecordVO vo = new TransRecordVO();
				BeanUtils.copyProperties(history, vo);
				vo.setFirmLogo(PageHelper.getLogoUrl(firmLogo, "F"));
				vo.setFundName(fundName);
				vo.setSymbolCode(symbolCode);
				vo.setIfaFirmName(companyName);
				if(history.getOrderDate() != null){
					vo.setOrderDateStr(DateUtil.dateToDateString(history.getOrderDate(), dateFormat));
				}
				if(history.getMember() != null){
					vo.setClientName(getCommonMemberName(history.getMember().getId(), langCode, "2"));
				}
				if (StringUtils.isNotBlank(history.getOrderType())) {
					vo.setOrderTypeDisplay(PropertyUtils.getPropertyValue(langCode, "order.plan.history.order.type." + history.getOrderType(), null));
				}
				if (StringUtils.isNotBlank(history.getTranFeeCur())) {
					vo.setTranFeeCurName(getParamConfigName(langCode, history.getTranFeeCur(), CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY_TYPE));
				}
				if (history.getCloseTime() != null) {
					vo.setCloseTimeStr(DateUtil.dateToDateString(history.getCloseTime(), dateFormat));
				}
				if (StringUtils.isNotBlank(history.getStatus())) {
					vo.setStatusDisplay(PropertyUtils.getPropertyValue(langCode, "order.plan.history.status.display." + history.getStatus(), null));
				}
				if(history.getCreatorId() != null){
					vo.setTrader(getCommonMemberName(history.getCreatorId().getId(), langCode, "2"));
				}
				vo.setCurrencyCode(currencyCode);
				vo.setCurrencyName(getParamConfigName(langCode, currencyCode, CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY_TYPE));
				Double rate = getExchangeRate(history.getTranFeeCur(), currencyCode);
				if(rate == null || rate < 0){
					rate = 1d;
				}
				if(vo.getTransactionPrice() != null){
					vo.setTransactionPrice(vo.getTransactionPrice() * rate);	
				}
				if(vo.getTransactionAmount() != null){
					vo.setTransactionAmount(vo.getTransactionAmount() * rate);	
				}
				if(vo.getFee() != null){
					vo.setFee(vo.getFee() * rate);	
				}
				String remark = null;
				if ("1".equals(vo.getSwitchFlag()) 
						&& CommonConstants.ORDER_TYPE_BUY.equals(vo.getOrderType())) {
					remark = PropertyUtils.getPropertyValue(langCode, "order.plan.history.switch.in", null);
				}else if ("1".equals(vo.getSwitchFlag()) 
						&& CommonConstants.ORDER_TYPE_SELL.equals(vo.getOrderType())){
					remark = PropertyUtils.getPropertyValue(langCode, "order.plan.history.switch.out", null);
				}
				vo.setRemark(remark);
				vos.add(vo);
			}
			jsonPaging.getList().clear();
			jsonPaging.getList().addAll(vos);
		}
		return jsonPaging;
	}
	
	/**
	 * 交易记录查询语句
	 * @author wwluo
	 * @param memberAdmin 当前管理员
	 * @param transRecordVO 查询条件
	 * @param langCode
	 * @param currencyCode
	 * @return Object[]
	 */
	private Object[] getQueryConditions(MemberAdmin memberAdmin, TransRecordVO transRecordVO, String langCode, String currencyCode){
		Object[] result = new Object[2];
		if (StringUtils.isBlank(currencyCode)) {
			currencyCode = memberAdmin.getMember().getDefCurrency();
			if (StringUtils.isBlank(currencyCode)) {
				currencyCode = CommonConstants.DEF_CURRENCY;
			}
		}
		StringBuffer hql = new StringBuffer(""
				+ " SELECT"
			    + " h,fl.fundName,pd.symbolCode,ml.companyName,mif.firmLogo"
				+ " FROM"
				+ " OrderHistory h"
				// fundName
				+ " LEFT JOIN"
				+ " FundInfo f"
				+ " ON"
				+ " f.product.id=h.product.id"
				+ " AND"
				+ " f.isValid=1"
				+ " LEFT JOIN"
				+ " FundInfo" + getLangFirstCharUpper(langCode) + " fl"
				+ " ON"
				+ " f.id=fl.id"
				// keyWord
				+ " LEFT JOIN"
				+ " MemberBase m"
				+ " ON"
				+ " m.id=h.member.id"
				+ " AND"
				+ " m.isValid=1"
				+ " LEFT JOIN"
				+ " MemberIndividual mi"
				+ " ON"
				+ " mi.member.id=h.member.id"
				// ifaFirm
				+ " LEFT JOIN"
				+ " CrmCustomer cc"
				+ " ON"
				+ " cc.member.id=h.member.id"
				+ " AND"
				+ " cc.isValid=1"
				+ " AND"
				+ " cc.clientType=1"
				+ " LEFT JOIN"
				+ " MemberIfaIfafirm mii"
				+ " ON"
				+ " mii.ifa.id=cc.ifa.id"
				+ " AND"
				+ " mii.isValid=1"
				+ " AND"
				+ " mii.checkStatus=1"
				// distributor
				+ " LEFT JOIN"
				+ " InvestorAccount ia"
				+ " ON"
				+ " ia.id=h.account.id"
				+ " AND"
				+ " ia.isValid=1"
				+ " AND"
				+ " ia.openStatus=3"
				// symbolCode、companyName
				+ " LEFT JOIN"
				+ " ProductDistributor pd"
				+ " ON"
				+ " pd.product.id=h.product.id"
				+ " LEFT JOIN"
				+ " MemberIfafirm mif"
				+ " ON"
				+ " mii.ifafirm.id=mif.id"
				+ " LEFT JOIN"
				+ " MemberIfafirm" + getLangFirstCharUpper(langCode) + " ml"
				+ " ON"
				+ " mii.ifafirm.id=ml.id"
				+ " WHERE 1=1"
				+ "");
		List<Object> params = new ArrayList<Object>();
		if (StringUtils.isNotBlank(transRecordVO.getFilterPeriod()) 
				&& StringUtils.isNotBlank(transRecordVO.getFilterPeriodType())) {
			Calendar calendar = Calendar.getInstance();
			String periodType = transRecordVO.getFilterPeriodType();
			if("D".equals(periodType)){
				calendar.add(Calendar.DATE, Integer.valueOf(transRecordVO.getFilterPeriod()));
			}else if("M".equals(periodType)){
				calendar.add(Calendar.MONTH, Integer.valueOf(transRecordVO.getFilterPeriod()));
			}else if("Y".equals(periodType)){
				calendar.add(Calendar.YEAR, Integer.valueOf(transRecordVO.getFilterPeriod()));
			}
			hql.append(" AND h.orderDate BETWEEN ? AND CURDATE()");
			params.add(calendar.getTime());
		}else if(StringUtils.isNotBlank(transRecordVO.getFilterStartTime()) 
				&& StringUtils.isNotBlank(transRecordVO.getFilterEndTime())){
			hql.append(" AND h.orderDate BETWEEN ? AND ?");
			params.add(DateUtil.StringToDate(transRecordVO.getFilterStartTime(), CommonConstants.FORMAT_DATE));
			params.add(DateUtil.StringToDate(transRecordVO.getFilterEndTime(), CommonConstants.FORMAT_DATE));
		}
		if (StringUtils.isNotBlank(transRecordVO.getFilterStatus())) {
			String status = StrUtils.reHeavy(transRecordVO.getFilterStatus());
			if(status.indexOf(",") > -1){
				String[] objs = status.split(",");
				hql.append(" AND (");
				for (int i = 0; i < objs.length; i++) {
					if(i == objs.length - 1){
						hql.append("  h.status=?");
					}else{
						hql.append("  h.status=? OR");
					}
					params.add(objs[i]);
				}
				hql.append(" )");
			}else{
				hql.append(" AND h.status=?");
				params.add(transRecordVO.getFilterStatus());
			}
		}
		if (StringUtils.isNotBlank(transRecordVO.getFilterKeyWord())) {
		// 客户名称，投资账户，基金名称的模糊搜索
			hql.append(" AND("
					+ "	TRIM(mi.firstName)||' '||TRIM(mi.lastName) LIKE ? OR"
					+ "	mi.firstName LIKE ? OR"
					+ "	mi.lastName LIKE ? OR"
					+ "	mi.nameChn LIKE ? OR"
					+ "	m.nickName LIKE ? OR"
					+ "	m.loginCode LIKE ? OR"
					+ "	h.accountNo LIKE ? OR"
					+ "	fl.fundName LIKE ?"
					+ ")");
			params.add("%" + transRecordVO.getFilterKeyWord() + "%");
			params.add("%" + transRecordVO.getFilterKeyWord() + "%");
			params.add("%" + transRecordVO.getFilterKeyWord() + "%");
			params.add("%" + transRecordVO.getFilterKeyWord() + "%");
			params.add("%" + transRecordVO.getFilterKeyWord() + "%");
			params.add("%" + transRecordVO.getFilterKeyWord() + "%");
			params.add("%" + transRecordVO.getFilterKeyWord() + "%");
			params.add("%" + transRecordVO.getFilterKeyWord() + "%");
		}
		if (StringUtils.isNotBlank(transRecordVO.getFilterIfaFirmId())) {
			hql.append(" AND mii.ifafirm.id=?");
			params.add(transRecordVO.getFilterIfaFirmId());
		}
		if(CommonConstantsWeb.MEMBERADMIN_TYPE_IFA.equals(memberAdmin.getType()) && memberAdmin.getIfafirm() != null){
		// IFA Firm	
			hql.append(" AND mii.ifafirm.id=?");
			params.add(memberAdmin.getIfafirm().getId());
		}else if(CommonConstantsWeb.MEMBERADMIN_TYPE_DISTRIBUTOR.equals(memberAdmin.getType()) && memberAdmin.getDistributor() != null){
		// Distributor
			hql.append(" AND ia.distributor.id=?");
			params.add(memberAdmin.getDistributor().getId());
		}
		hql.append(" GROUP BY h.id ORDER BY h.orderDate DESC");
		result[0] = hql.toString();
		result[1] = params.toArray();
		return result;
	}
}
