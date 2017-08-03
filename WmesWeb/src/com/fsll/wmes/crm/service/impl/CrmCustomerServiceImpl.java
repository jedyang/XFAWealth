package com.fsll.wmes.crm.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.ChineseToEnglish;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.StrUtils;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.crm.service.CrmCustomerService;
import com.fsll.wmes.crm.vo.ClientRemindVO;
import com.fsll.wmes.crm.vo.ClientSearchVO;
import com.fsll.wmes.crm.vo.ClientsBasicVO;
import com.fsll.wmes.crm.vo.CrmClientForIfaVO;
import com.fsll.wmes.crm.vo.CrmClientVO;
import com.fsll.wmes.crm.vo.CrmCustomerVO;
import com.fsll.wmes.crm.vo.CrmExistingCustomerVO;
import com.fsll.wmes.crm.vo.CrmPortfolioHoldVO;
import com.fsll.wmes.crm.vo.CrmProspectCustomerVO;
import com.fsll.wmes.crm.vo.PortfolioVO;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.CrmCustomerGroup;
import com.fsll.wmes.entity.CrmCustomerGroupRelationship;
import com.fsll.wmes.entity.CrmMemo;
import com.fsll.wmes.entity.IfaMigrateHist;
import com.fsll.wmes.entity.InvestorDoc;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberCorporate;
import com.fsll.wmes.entity.MemberFi;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioHoldAccount;
import com.fsll.wmes.entity.PortfolioHoldAlert;
import com.fsll.wmes.entity.PortfolioInfo;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.ifa.service.IfaManageService;
import com.fsll.wmes.ifa.service.IfaMigrateHistService;
import com.fsll.wmes.ifa.service.IfaSpaceService;
import com.fsll.wmes.ifa.vo.IfaInvestorVO;
import com.fsll.wmes.investor.vo.InvestorVO;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.vo.IfaVO;

/***
 * 业务接口实现类：客户信息管理接口实现类
 * @author mqzou
 * @version 1.0
 * @date 2016-08-23
 */
@Service("crmCustomerService")
//@Transactional
public class CrmCustomerServiceImpl extends BaseService implements CrmCustomerService {
	@Autowired
	private IfaSpaceService ifaSpaceService;
	@Autowired
	private IfaManageService ifaManageService;
	@Autowired
	private IfaMigrateHistService ifaMigrateHistService;
	@Autowired
	private MemberBaseService memberBaseService;
	
	/**
	 * 获取客户列表（条件：本ifafrim、ifa）
	 * @author qgfeng
	 * @date 2016-12-14
	 */
	@Override
	public JsonPaging findInvestorByIfa(JsonPaging jsonPaging,IfaVO ifaVO, String langCode) {
		String hql =" from CrmCustomer c where c.isValid='1' and c.ifa.id in ";
		hql += "( select ifa.id from MemberIfaIfafirm where ifafirm.id = ?) ";
		List<String> params = new ArrayList<String>();
		params.add(ifaVO.getIfafirmId());
		if(StringUtils.isNoneBlank(ifaVO.getId())){
			hql+=" and c.ifa.id=? ";
			params.add(ifaVO.getId());
		}
		if(StringUtils.isNoneBlank(ifaVO.getNickName())){
			hql+=" and c.ifa.member.nickName like ? ";
			params.add("%"+ifaVO.getNickName()+"%");
		}
		if(StringUtils.isNoneBlank(ifaVO.getKeyword())){
			hql +=" and ( c.member.loginCode like ? or c.nickName like ? )";
			params.add("%"+ifaVO.getKeyword()+"%");
			params.add("%"+ifaVO.getKeyword()+"%");
		}
		jsonPaging = baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		List<CrmCustomer> list = jsonPaging.getList();
		List<InvestorVO> listVo = new ArrayList<InvestorVO>();
		if(list!=null && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				CrmCustomer crm =  list.get(i);
				InvestorVO vo=new InvestorVO();
				vo.setEmail(crm.getMember().getEmail());
				vo.setId(crm.getId());
				vo.setLoginCode(crm.getMember().getLoginCode());
				vo.setMemberId(crm.getMember().getId());
				vo.setMemberType(crm.getMember().getSubMemberType()+"");
				vo.setMobileNumber(crm.getMember().getMobileNumber());
				vo.setNickName(crm.getNickName());
				vo.setIfaName(crm.getIfa().getNameChn());
				setInvestor(vo);
				listVo.add(vo);
			}
		}
		jsonPaging.setList(listVo);
		return jsonPaging;
	}
	
	//设置投资人，投资类型
	public void setInvestor(InvestorVO vo){
		if (CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_INDIVIDUAL == Integer.parseInt(vo.getMemberType())){
			String hql = " from MemberIndividual i where i.member.id='"+vo.getMemberId()+"' ";
			List<MemberIndividual> list = baseDao.find(hql, null, false);
			if(list!=null && !list.isEmpty()){
				MemberIndividual individual = list.get(0);
				vo.setCompanyName(individual.getCompanyName());
				vo.setRelateId(individual.getId());
			}
		}else if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_COPORATE == Integer.parseInt(vo.getMemberType())){
			String hql = " from MemberCorporate i where i.member.id='"+vo.getMemberId()+"' ";
			List<MemberCorporate> list = baseDao.find(hql, null, false);
			if(list!=null && !list.isEmpty()){
				MemberCorporate company = list.get(0);
				vo.setCompanyName(company.getCompanyName());
				vo.setRelateId(company.getId());
			}
		}else if(CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_FI == Integer.parseInt(vo.getMemberType())){
			String hql = " from MemberFi i where i.member.id='"+vo.getMemberId()+"' ";
			List<MemberFi> list = baseDao.find(hql, null, false);
			if(list!=null && !list.isEmpty()){
				MemberFi fi = list.get(0);
				vo.setCompanyName(fi.getCompanyName());
				vo.setRelateId(fi.getId());
			}
		}
	}
	
	private String getMapObject(Map map, String key) {
		return map.get(key) == null ? "" : map.get(key).toString();
	}
	@Override
	public JsonPaging findPortfolioByCustomer(JsonPaging jsonPaging, PortfolioInfo portfolioInfo) {
		String hql=" from PortfolioInfo r where r.isValid='1'";
		List params=new ArrayList();
		if(null!=portfolioInfo && null!=portfolioInfo.getMember() && null!=portfolioInfo.getMember().getId()){
			hql+=" and r.member.id='"+portfolioInfo.getMember().getId()+"'";
		}
		jsonPaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		List list=new ArrayList();
		Iterator it=jsonPaging.getList().iterator();
		while (it.hasNext()) {
			PortfolioInfo info = (PortfolioInfo) it.next();
			PortfolioVO vo=new PortfolioVO();

			//vo.setAipExecCycle(info.getAipExecCycle());
			//vo.setAipNextTime(info.getAipNextTime());
			//vo.setAipTimeDistance(info.getAipTimeDistance());

			vo.setAipType(info.getAipFlag());
			vo.setCreateTime(info.getCreateTime());
			vo.setCreator(null!=info.getCreator()?info.getCreator().getNickName():"");
			vo.setCreatorId(null!=info.getCreator()?info.getCreator().getId():"");
		/*	vo.setCurrencyType(info.getCurrencyType());
			vo.setDesc(info.getDesc());*/
			vo.setId(info.getId());
			vo.setLoginCode(null!=info.getMember()?info.getMember().getLoginCode():"");
			vo.setMemberId(null!=info.getMember()?info.getMember().getId():"");
			vo.setNickName(null!=info.getMember()?info.getMember().getNickName():"");
			//vo.setObjectiveDesc(info.getObjectiveDesc());
			vo.setPortfolioName(info.getPortfolioName());
			vo.setProposalId(null!=info.getProposal()?info.getProposal().getId():"");
			vo.setProposalName(null!=info.getProposal()?info.getProposal().getProposalName():"");
//			vo.setReturnRate(info.getReturnRate());
			//vo.setReturnTotal(info.getReturnTotal());
			//vo.setRiskLeve(info.getRiskLeve());
			//vo.setStatus(info.getStatus());
			//vo.setTotalInvestAmount(info.getTotalInvestAmount());
			//vo.setType(info.getType());
			list.add(vo);
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	@Override
	public PortfolioInfo findPortfolioInfo(String id) {
		PortfolioInfo vo=(PortfolioInfo)this.baseDao.get(PortfolioInfo.class, id);
		return vo;
	}
	
	/**
	 * 获取客户信息
	 * @author zxtan
	 * @date 2016-09-13
	 * @param langCode
	 * @param ifaMemberId
	 * @param customerMemberId
	 * @return
	 */
	@Override
	public CrmCustomerVO findCustomerDetailForIfa(String customerId,String langCode){
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("SELECT c.id,c.ifa_id,c.member_id,c.nick_name,mb.icon_url,c.client_type, c.remark,mb.mobile_code,mb.mobile_number,mb.email,");
		sBuilder.append("mi.first_name,mi.last_name,mi.name_chn,mi.gender,mi.nationality,con.name_"+langCode+" as nationality_name,");
		sBuilder.append("mi.occupation,oc.name_"+langCode+" as occupation_name,mi.employment,em.name_"+langCode+" as employment_name,");
		sBuilder.append("mi.education,edu.name_"+langCode+" as education_name,mi.cert_no,mi.telephone,mi.born");
		sBuilder.append(" FROM crm_customer c "); 
		sBuilder.append(" LEFT JOIN member_base mb ON c.member_id=mb.id ");
		sBuilder.append(" LEFT JOIN member_individual mi ON mb.id=mi.member_id ");
		sBuilder.append(" LEFT JOIN sys_country con ON mi.nationality = con.id ");
		sBuilder.append(" LEFT JOIN sys_param_config oc ON mi.occupation = oc.config_code ");
		sBuilder.append(" LEFT JOIN sys_param_config em ON mi.employment = em.config_code ");
		sBuilder.append(" LEFT JOIN sys_param_config edu ON mi.education = edu.config_code ");
		sBuilder.append(" LEFT JOIN member_ifa ifa ON c.ifa_id=ifa.id ");
		sBuilder.append(" WHERE c.id='"+customerId+"' ");
		
		List list = springJdbcQueryManager.springJdbcQueryForList(sBuilder.toString());
		CrmCustomerVO vo = new CrmCustomerVO();
		if(!list.isEmpty()){
			Map map = (HashMap) list.get(0);
			String id = getMapValue(map,"id");
			String ifaId = getMapValue(map,"ifa_id");
			String memberId = getMapValue(map,"member_id");
			String iconUrl = getMapValue(map,"icon_url");
			String remark = getMapValue(map,"remark");
			String mobileCode = getMapValue(map,"mobile_code");
			String mobileNumber = getMapValue(map,"mobile_number");
			String email = getMapValue(map,"email");
			String nickName = getMapValue(map,"nick_name");
			String firstName = getMapValue(map,"first_name");
			String lastName = getMapValue(map,"last_name");
			String nameChn = getMapValue(map,"name_chn");
			String gender = getMapValue(map,"gender");
			String nationality = getMapValue(map,"nationality");
			String nationalityName = getMapValue(map,"nationality_name");
			String occupation = getMapValue(map,"occupation");
			String occupationName = getMapValue(map,"occupation_name");
			String employment = getMapValue(map,"employment");
			String employmentName = getMapValue(map,"employment_name");
			String education = getMapValue(map,"education");
			String educationName = getMapValue(map,"education_name");
			String certNo = getMapValue(map,"cert_no");
			String telephone = getMapValue(map,"telephone");
			String born = getMapValue(map,"born");
			String clientType=getMapObject(map, "client_type");
			
			vo.setId(id);
			vo.setIfaId(ifaId);
			vo.setMemberId(memberId);
			vo.setIconUrl(iconUrl);
			vo.setRemark(remark);
			vo.setMobileCode(mobileCode);
			vo.setMobileNumber(mobileNumber);
			vo.setEmail(email);
			vo.setNickName(nickName);
			vo.setFirstName(firstName);
			vo.setLastName(lastName);
			vo.setNameChn(nameChn);
			vo.setGender(gender);
			vo.setNationality(nationality);
			vo.setNationalityName(nationalityName);
			vo.setOccupation(occupation);
			vo.setOccupationName(occupationName);
			vo.setEmployment(employment);
			vo.setEmploymentName(employmentName);
			vo.setEducation(education);
			vo.setEducationName(educationName);
			vo.setCertNo(certNo);
			vo.setTelephone(telephone);
			vo.setBorn(born);
			vo.setClientType(clientType);
			String clientName=getCommonMemberName(vo.getMemberId(), langCode, "2");
			vo.setClientName(clientName);
		}
		
		return vo;
	}
	
		
	/**
	 * 获取客户列表
	 * @author zxtan
	 * @date 2016-09-26
	 * modify by mqzou 2016-10-25 增加搜索条件
	 */
	@Override
	public JsonPaging findCustomerListForIfa(JsonPaging jsonPaging,String ifaMemberId,String areaId,String period,String saleStageId,String keyword,String character){
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
			sBuilder.append(" ,CASE WHEN CAST(CONCAT(YEAR(NOW()),DATE_FORMAT(mi.born,'-%m-%d'))AS DATE) >= CAST(DATE_FORMAT(NOW(),'%y-%m-%d') AS DATE) AND CAST(CONCAT(YEAR(NOW()),DATE_FORMAT(mi.born,'-%m-%d'))AS DATE) <= DATE_ADD(NOW(),INTERVAL "+intervalUnit+") THEN 1 ELSE 0 END AS birthday_remind,");
			sBuilder.append(" ( SELECT COUNT(0) FROM crm_schedule WHERE customer_id=c.id AND start_date >= CAST(DATE_FORMAT(NOW(),'%y-%m-%d') AS DATE) AND start_date < DATE_ADD(NOW(),INTERVAL "+intervalUnit+")) AS appointment_remind ");
		}
		sBuilder.append(" FROM crm_customer c ");
		sBuilder.append(" LEFT JOIN member_base mb ON c.member_id=mb.id ");
		sBuilder.append(" LEFT JOIN member_individual mi ON mb.id=mi.member_id ");
		sBuilder.append(" LEFT JOIN member_ifa ifa ON c.ifa_id=ifa.id ");
		sBuilder.append(" LEFT JOIN crm_customer_group_relationship s ON c.id = s.customer_id ");
		sBuilder.append(" LEFT JOIN crm_customer_group g ON s.group_id = g.id ");
		
		sBuilder.append(" WHERE 1=1 ");
		

		if(ifaMemberId != null && !"".equals(ifaMemberId)){
			sBuilder.append(" AND ifa.member_id= ? ");	
			params.add(ifaMemberId);
		}

		sBuilder.append(" AND c.client_type= '0' ");
//		if(clientType != null && !"".equals(clientType)){	
//			params.add(clientType);
//		}
		
		if(saleStageId != null && !"".equals(saleStageId)){
			if("sales_close".equals(saleStageId)){
				sBuilder.append(" AND ( c.sales_stage_id= ? or c.sales_stage_id='sales_suspend' ) ");	
				params.add(saleStageId);
			}else {
				sBuilder.append(" AND c.sales_stage_id= ? ");	
				params.add(saleStageId);
			}			
		}else {
			sBuilder.append(" AND 1=2 ");
		}
		
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
	 * 获取客户列表
	 * @author zxtan
	 * @date 2016-09-28
	 */
	@Override
	public JsonPaging findExistingCustomerListForIfa(JsonPaging jsonPaging,String ifaMemberId,String areaId,String period,String clientStatus,String keyword,String character){
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
		
		if(ifaMemberId != null && !"".equals(ifaMemberId)){
			sBuilder.append(" AND ifa.member_id= ? ");	
			params.add(ifaMemberId);
		}
		
		if(clientStatus != null && !"".equals(clientStatus)){
			if("Customer-Care".equalsIgnoreCase(clientStatus)){
				sBuilder.append(" AND EXISTS (SELECT p.id FROM portfolio_info p INNER JOIN portfolio_hold h ON p.id= h.portfolio_id ");
				sBuilder.append(" WHERE p.member_id=c.member_id AND c.ifa_id= p.ifa_id ) ");				
			}else if("Open-Account".equalsIgnoreCase(clientStatus)){
				sBuilder.append(" AND EXISTS (SELECT ia.id FROM investor_account ia WHERE c.ifa_id=ia.ifa_id AND c.member_id=ia.member_id ");
				sBuilder.append(" AND ia.open_status not in ('0','1') ) ");
			}else if("Portfolio".equalsIgnoreCase(clientStatus)){
				sBuilder.append(" AND EXISTS (SELECT cp.id FROM crm_proposal cp INNER JOIN portfolio_info p ON cp.id=p.proposal_id  ");
				sBuilder.append(" INNER JOIN order_plan op ON p.id = op.portfolio_id ");
				sBuilder.append(" WHERE p.member_id=c.member_id AND c.ifa_id= p.ifa_id AND cp.status='1' AND op.finish_status='0' ) ");
			}else if("Proposal".equalsIgnoreCase(clientStatus)){
				sBuilder.append(" AND EXISTS (SELECT p.id FROM crm_proposal p  ");
				sBuilder.append(" WHERE p.member_id=c.member_id AND p.ifa_id= c.ifa_id AND p.status='0' ) ");
			}else if("Not-Yet-Invest".equalsIgnoreCase(clientStatus)){
				sBuilder.append(" AND EXISTS (SELECT ia.id FROM investor_account ia WHERE c.ifa_id=ia.ifa_id AND c.member_id=ia.member_id AND ia.open_status ='1' ) ");
				sBuilder.append(" AND EXISTS (SELECT p.id FROM crm_proposal p WHERE p.member_id=c.member_id AND p.ifa_id= c.ifa_id AND p.status='1' ) ");
			}else {
				sBuilder.append(" AND 1=2 ");
			}			
		}else {
			sBuilder.append(" AND 1=2 ");
		}
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
	 * 获取客户生日提醒
	 * @author zxtan
	 * @date 2016-09-26
	 */
	@Override
	public Long findCustomerBirthdayRemindForIfa(String ifaMemberId,String clientType,String period){
		StringBuilder sBuilder = new StringBuilder();
		List<Object> params=new ArrayList<Object>();
		sBuilder.append("SELECT COUNT(c.id) as birthday_remind FROM crm_customer c ");
		sBuilder.append(" LEFT JOIN member_individual mi ON c.member_id= mi.member_id ");
		sBuilder.append(" LEFT JOIN member_ifa ifa ON ifa.id=c.ifa_id ");
		sBuilder.append(" WHERE 1=1 ");
		if(clientType != null && !"".equals(clientType)){
			sBuilder.append(" AND c.client_type= ? ");	
			params.add(clientType);
		}
		if(ifaMemberId != null && !"".equals(ifaMemberId)){
			sBuilder.append(" AND ifa.member_id= ? ");	
			params.add(ifaMemberId);
		}		
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
			sBuilder.append("  AND CAST(CONCAT(YEAR(NOW()),DATE_FORMAT(mi.born,'-%m-%d'))AS DATE) >=CAST(DATE_FORMAT(NOW(),'%y-%m-%d') AS DATE) AND CAST(CONCAT(YEAR(NOW()),DATE_FORMAT(mi.born,'-%m-%d'))AS DATE) <= DATE_ADD(NOW(),INTERVAL "+intervalUnit+")");			
		}
		
		Long countNum = springJdbcQueryManager.springJdbcQueryForTotal(sBuilder.toString(), params.toArray());
		return countNum;
	}
	
	/**
	 * 获取客户日程提醒
	 * @author zxtan
	 * @date 2016-09-26
	 */
	@Override
	public Long findCustomerAppointmentRemindForIfa(String ifaMemberId,String clientType,String period){
		StringBuilder sBuilder = new StringBuilder();
		List<Object> params=new ArrayList<Object>();
		sBuilder.append("SELECT COUNT(s.id) as appointment_remind FROM crm_schedule s ");
		sBuilder.append(" LEFT JOIN crm_customer c ON s.customer_id=c.id ");
		sBuilder.append(" LEFT JOIN member_ifa ifa ON ifa.id=c.ifa_id ");
		sBuilder.append(" WHERE 1=1 ");
		if(clientType != null && !"".equals(clientType)){
			sBuilder.append(" AND c.client_type= ? ");	
			params.add(clientType);
		}
		if(ifaMemberId != null && !"".equals(ifaMemberId)){
			sBuilder.append(" AND ifa.member_id= ? ");	
			params.add(ifaMemberId);
		}		
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
			sBuilder.append("  AND s.start_date >=CAST(DATE_FORMAT(NOW(),'%y-%m-%d') AS DATE) AND s.start_date  <= DATE_ADD(NOW(),INTERVAL "+intervalUnit+")");			
		}
			
		Long countNum = springJdbcQueryManager.springJdbcQueryForTotal(sBuilder.toString(), params.toArray());
		return countNum;
	}
	
	
	/**
	 * 获取客户投资组合提醒
	 * @author zxtan
	 * @date 2016-09-29
	 */
	@Override
	public Long findCustomerPortfolioRemindForIfa(String ifaMemberId,String clientType){
		StringBuilder sBuilder = new StringBuilder();
		List<Object> params=new ArrayList<Object>();
		sBuilder.append("SELECT COUNT(pi.id) as portfolio_remind FROM portfolio_info pi ");
		sBuilder.append(" INNER JOIN portfolio_hold ph ON pi.id = ph.portfolio_id ");
		sBuilder.append(" INNER JOIN crm_customer c ON pi.member_id = c.member_id AND pi.ifa_id = c.ifa_id ");
		sBuilder.append(" INNER JOIN member_ifa ifa ON c.ifa_id = ifa.id ");
		sBuilder.append(" WHERE IFNULL(ph.total_return_value,0) > IFNULL(ifa.portfolio_critical_value,0) ");
		if(clientType != null && !"".equals(clientType)){
			sBuilder.append(" AND c.client_type= ? ");	
			params.add(clientType);
		}
		if(ifaMemberId != null && !"".equals(ifaMemberId)){
			sBuilder.append(" AND ifa.member_id= ? ");	
			params.add(ifaMemberId);
		}		
				
		Long countNum = springJdbcQueryManager.springJdbcQueryForTotal(sBuilder.toString(), params.toArray());
		return countNum;
	}
	
	/**
	 * 获取客户KYC提醒
	 * @author zxtan
	 * @date 2016-09-29
	 */
	@Override
	public Long findCustomerKYCRemindForIfa(String ifaMemberId,String clientType,String period){
		StringBuilder sBuilder = new StringBuilder();
		List<Object> params=new ArrayList<Object>();
		sBuilder.append("SELECT COUNT(c.id) as kyc_remind FROM crm_customer c ");
		sBuilder.append(" INNER JOIN member_ifa ifa ON c.ifa_id = ifa.id ");
		sBuilder.append(" WHERE 1=1 ");
		if(clientType != null && !"".equals(clientType)){
			sBuilder.append(" AND c.client_type= ? ");	
			params.add(clientType);
		}
		if(ifaMemberId != null && !"".equals(ifaMemberId)){
			sBuilder.append(" AND ifa.member_id= ? ");	
			params.add(ifaMemberId);
		}		
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
			sBuilder.append(" AND ( EXISTS ( SELECT r.id FROM rpq_exam r WHERE r.member_id = c.member_id "); 
			sBuilder.append("		AND CAST(CONCAT(YEAR(NOW()),DATE_FORMAT(r.expire_date,'-%m-%d'))AS DATE) >=CAST(DATE_FORMAT(NOW(),'%y-%m-%d') AS DATE) "); 
			sBuilder.append("		AND CAST(CONCAT(YEAR(NOW()),DATE_FORMAT(r.expire_date,'-%m-%d'))AS DATE) <= DATE_ADD(NOW(),INTERVAL "+intervalUnit+")) ");
			sBuilder.append(" OR EXISTS ( SELECT d.id FROM investor_doc d WHERE d.member_id = c.member_id "); 
			sBuilder.append("		AND CAST(CONCAT(YEAR(NOW()),DATE_FORMAT(d.expire_date,'-%m-%d'))AS DATE) >=CAST(DATE_FORMAT(NOW(),'%y-%m-%d') AS DATE) "); 
			sBuilder.append("		AND CAST(CONCAT(YEAR(NOW()),DATE_FORMAT(d.expire_date,'-%m-%d'))AS DATE) <= DATE_ADD(NOW(),INTERVAL "+intervalUnit+"))) ");			
		}
			
		Long countNum = springJdbcQueryManager.springJdbcQueryForTotal(sBuilder.toString(), params.toArray());
		return countNum;
	}

	
	/**
	 * 获取客户信息
	 * @author zxtan
	 * @date 2016-09-27
	 * @param obj
	 * @return
	 */
	@Override
	public CrmCustomer findCustomerById(String id){
		CrmCustomer obj = (CrmCustomer) this.baseDao.get(CrmCustomer.class, id);
		return obj;
	}
	
	/**
	 * 保存客户信息
	 * @author zxtan
	 * @date 2016-09-27
	 * @param obj
	 * @return
	 */
	@Override
	public CrmCustomer saveCustomer(CrmCustomer obj){
		obj = (CrmCustomer)this.baseDao.saveOrUpdate(obj);
		return obj;
	}
	
	/**
	 * 更新客户信息重要性
	 * @author zxtan
	 * @date 2016-09-27
	 * @param obj
	 * @return
	 */
	@Override
	public Boolean updateImportant(String id,String isImportant){
		String hql = "update CrmCustomer set isImportant=? where id=? ";
		List params=new ArrayList();
		params.add(isImportant);
		params.add(id);
		int resultCount = this.baseDao.updateHql(hql, params.toArray());
		return resultCount>0;
	}
	
	/**
	 * 更新客户信息备注
	 * @author zxtan
	 * @date 2016-10-08
	 * @param id
	 * @param remark
	 * @return
	 */
	@Override
	public Boolean updateRemark(String id,String remark){
		String hql = "update CrmCustomer set remark=? where id=? ";
		List params=new ArrayList();
		params.add(remark);
		params.add(id);
		int resultCount = this.baseDao.updateHql(hql, params.toArray());
		return resultCount>0;
	}
	
	/**
	 * 删除客户
	 * @author zxtan
	 * @date 2016-09-27
	 * @param obj
	 * @return
	 */
	@Override
	public void deleteCustomer(String id,String saleStageId){
		CrmCustomer obj = findCustomerById(id);
		if("sales_proposal,sales_close,".indexOf(saleStageId)>-1){
			String memberId = obj.getMember().getId();
			String ifaId = obj.getIfa().getId();
			String hql = "delete CrmProposal p where p.member.id=? and p.ifaMember.id=? ";
			List params=new ArrayList();
			params.add(memberId);
			params.add(ifaId);
			this.baseDao.updateHql(hql, params.toArray());
		}
		
		this.baseDao.delete(obj);
	}

	
	@Override
	public JsonPaging findCustomerByIfa(JsonPaging jsonPaging,MemberIfa memberIfa) {
		String hql=" from CrmCustomer r where r.ifa.id='"+memberIfa.getId()+"' and r.isValid='1'";
		jsonPaging =this.baseDao.selectJsonPaging(hql, null, jsonPaging, false);
		if(null!=jsonPaging && null!=jsonPaging.getList()){
			Iterator it=jsonPaging.getList().iterator();
			List list=new ArrayList();
			while (it.hasNext()) {
				CrmCustomer object = (CrmCustomer) it.next();
				list.add(object);
			}
			jsonPaging.setList(list);
		}
		return jsonPaging;
	}
	
	/**
	 * 获取非ifa客户的所有投资人
	 * @author mqzou
	 * @date 2016-10-25
	 * @param ifaMember
	 * @return
	 */
	@Override
	public JsonPaging findInverstorNotInCrm(JsonPaging jsonPaging, MemberBase ifaMember,String langCode,String invStyle,String intrest,String noIfa,String region) {
		String language=ifaMember.getLanguageSpoken();
		String style=ifaMember.getInvestStyle();
		if(null==style)
			style="";
		String[] styles=style.split(",");
		
		/*if(null==language || "".equals(language))
			language="null";*/
		StringBuilder hql=new StringBuilder();
		hql.append(" select m,");
		if(null!=language && !"".equals(language)){
			String[] langs= StrUtils.splitAndTrim(language, ",", "");
			hql.append(" case when (1=0");
			for (String string : langs) {
				hql.append(" or b.languageSpoken like '%"+string+"%' ");
			}
			hql.append(" )  then 1 else 0 end as langFlag,");
		}else {
			hql.append(" 0 as langFlag,");
		}
		if(null!=styles && styles.length>0){
			hql.append("case when (1=0");
			for (String str : styles) {
				hql.append(" or b.investStyle like '%"+str+"%' ");
			}
			hql.append(") then 1 else 0 end as invFlag"); 
		}else {
			hql.append(" 0 as invFlag");
		}
		hql.append(" from MemberIndividual m ");
		hql.append(" left join MemberBase b on m.member.id=b.id");
		hql.append(" where b.id not in (select c.member.id from CrmCustomer c where c.ifa.member.id=? )");
		hql.append(" and (b.inviteCode is null or b.inviteCode='')");
		List params=new ArrayList();
		params.add(ifaMember.getId());
		if(null!=langCode && !"".equals(langCode)){
			String[] langs=langCode.split(",");
			hql.append(" and (1=0");
			for (String str : langs) {
				if("".equals(str))
					continue;
				hql.append(" or b.languageSpoken like ?");
				params.add("%"+str+"%");
			}
			hql.append(")");
			
		}
		if(null!=invStyle && !"".equals(invStyle)){
			String[] langs=invStyle.split(",");
			hql.append(" and (1=0");
			for (String str : langs) {
				if("".equals(str))
					continue;
				hql.append(" or b.investStyle like ?");
				params.add("%"+str+"%");
			}
			hql.append(")");
			
			/*hql.append(" and m.member.investStyle like ?");
			params.add(" %"+invStyle+"%");*/
		}
		if(null!=intrest && !"".equals(intrest)){
			String[] langs=intrest.split(",");
			hql.append(" and (1=0");
			for (String str : langs) {
				if("".equals(str))
					continue;
				hql.append(" or b.hobbyType like ?");
				params.add("%"+str+"%");
			}
			hql.append(")");
			
			
		}
		if(null!=region && !"".equals(region)){
			String[] langs=region.split(",");
			hql.append(" and (1=0");
			for (String str : langs) {
				if("".equals(str))
					continue;
				hql.append(" or m.country.id = ?");
				params.add(str);
			}
			hql.append(")");
		}
		if(null!=noIfa && !"".equals(noIfa)){
		//	hql.append(" and b.id not in (select e.member.id from CrmCustomer e where e.isValid='1')");
			hql.append(" and b.id not in (select e.member.id from InvestorAccount e )");
		}
			
		jsonPaging.setOrder(null);
		jsonPaging.setSort(null);
		jsonPaging=this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		//List<CrmSelectVO> list=new ArrayList<CrmSelectVO>();
		List<IfaInvestorVO> list=new ArrayList<IfaInvestorVO>();
		if(null!=jsonPaging.getList()){
			Iterator it=jsonPaging.getList().iterator();
			while (it.hasNext()) {
				Object[] object = (Object[]) it.next();
				IfaInvestorVO vo=new IfaInvestorVO();
				int index=0;
				MemberIndividual individual=(MemberIndividual)object[index++];
				String privacySetting=individual.getMember().getPrivacySetting();
				if(null!=privacySetting && !"".equals(privacySetting)){
					if(privacySetting.contains("mobile_number:0")){
						 vo.setMobileNumber("*****"+individual.getMember().getMobileNumber().substring(individual.getMember().getMobileNumber().length()-1,individual.getMember().getMobileNumber().length()));
					}else {
						 vo.setMobileNumber(individual.getMember().getMobileNumber());
					}
					
					if(privacySetting.contains("email:0")){
						vo.setEmail("*****"+individual.getMember().getEmail().substring(individual.getMember().getEmail().length()-1,individual.getMember().getEmail().length()));
					}else {
						vo.setEmail(individual.getMember().getEmail());
					}
				}else {
					 vo.setEmail(individual.getMember().getEmail());
					 vo.setMobileNumber(individual.getMember().getMobileNumber());
				}
				Object langFlag=object[index++];
				Object invFlag=object[index++];
			    vo.setCountry(null!=individual.getCountry()?individual.getCountry().getNameEn():"");
			   // vo.setEmail(individual.getMember().getEmail());
			    vo.setLoginCode(individual.getMember().getLoginCode());
			    vo.setMemberId(individual.getMember().getId());
			    //vo.setMobileNumber(individual.getMember().getMobileNumber());
			    vo.setMobileCode(individual.getMember().getMobileCode());
			    vo.setSameLang(null!=langFlag?langFlag.toString():"0");
			    vo.setSameStyle(null!=invFlag?invFlag.toString():"0");
			    vo.setNameChn(individual.getNameChn());
			    vo.setNameEn(individual.getLastName()+" "+individual.getFirstName());
			    boolean read=ifaSpaceService.checkInsightRead(ifaMember.getId(), vo.getMemberId());
			    vo.setReadInsight(read?"1":"0");
			    boolean hasIfa=ifaSpaceService.checkInvestorWithIfa(vo.getMemberId());
			    vo.setNoIfa(hasIfa?"0":"1");
			    vo.setNickName(individual.getMember().getNickName());
				list.add(vo);
			}
			
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	
	/**
	 * 保存CRM 用户分组信息
	 * @author zxtan
	 * @date 2016-11-14
	 */
	@Override
	public CrmCustomerGroup saveGroup(CrmCustomerGroup obj) {
		// TODO Auto-generated method stub
		CrmCustomerGroup group = new CrmCustomerGroup();
		if( null != obj && StringUtils.isNotBlank(obj.getId()) ){
			group = (CrmCustomerGroup) this.baseDao.update(obj);
		}else {
			group = (CrmCustomerGroup) this.baseDao.create(obj);
		}		
		return group;
	}
	
	/**
	 * 删除CRM 用户分组信息
	 * @author zxtan
	 * @date 2016-11-14
	 */
	@Override
	public Boolean deleteGroup(String id) {
		// TODO Auto-generated method stub
		boolean flag = false;
		if(StringUtils.isNotBlank(id)){
			CrmCustomerGroup group = (CrmCustomerGroup) this.baseDao.get(CrmCustomerGroup.class, id);
			this.baseDao.delete(group);
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 通过Id 获得CRM 用户分组信息
	 * @author zxtan
	 * @date 2016-11-14
	 */
	@Override
	public CrmCustomerGroup findGroupById(String id){
		if(null == id || "".equals(id)){
			return null;
		}else {
			CrmCustomerGroup obj = (CrmCustomerGroup) this.baseDao.get(CrmCustomerGroup.class, id);
			return obj;
		}
		
	}
	
	
	/**
	 * 获取Ifa的CRM 用户分组信息
	 * @author zxtan
	 * @date 2016-11-14
	 */
	@Override
	public JsonPaging findGroupList(JsonPaging jsonPaging,String ifaId) {
		// TODO Auto-generated method stub
		StringBuilder sBuilder = new StringBuilder();
		List<Object> params=new ArrayList<Object>();
		sBuilder.append( " from CrmCustomerGroup l where l.isValid = 1 ");
		if(ifaId != null && !"".equals(ifaId)){
			sBuilder.append(" and l.ifa.id = ?  ");	
			params.add(ifaId);
		}else {
			sBuilder.append(" and 1 = 2 ");
		}
		
		jsonPaging = this.baseDao.selectJsonPagingNoTotal(sBuilder.toString(), params.toArray(), jsonPaging, false);
		return jsonPaging;
	}
	
	/**
	 * 保存CRM 用户分组关系信息
	 * @author zxtan
	 * @date 2016-11-14
	 */
	@Override
	public CrmCustomerGroupRelationship saveGroupRelationship(
			CrmCustomerGroupRelationship obj) {
		// TODO Auto-generated method stub
		CrmCustomerGroupRelationship groupRelationship = new CrmCustomerGroupRelationship();
		if( null != obj && StringUtils.isNotBlank(obj.getId()) ){
			groupRelationship = (CrmCustomerGroupRelationship) this.baseDao.update(obj);
		}else {
			groupRelationship = (CrmCustomerGroupRelationship) this.baseDao.create(obj);
		}		
		return groupRelationship;
	}
	
	/**
	 * 删除CRM 用户分组关系信息
	 * @author zxtan
	 * @date 2016-11-14
	 */
	@Override
	public Boolean deleteGroupRelationship(String id) {
		// TODO Auto-generated method stub
		boolean flag = false;
		if(StringUtils.isNotBlank(id)){
			CrmCustomerGroupRelationship relationship = (CrmCustomerGroupRelationship) this.baseDao.get(CrmCustomerGroupRelationship.class, id);
			this.baseDao.delete(relationship);
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 通过CustomerId获得 CRM 用户分组关系信息
	 * @author zxtan
	 * @date 2016-11-14
	 */
	@Override
	public CrmCustomerGroupRelationship findGroupRelationshipByCustomerId(String customerId) {
		// TODO Auto-generated method stub
//		CrmCustomerGroupRelationship obj;
//		CrmCustomerGroupRelationship groupRelationship = new CrmCustomerGroupRelationship();
		String hql = " from CrmCustomerGroupRelationship s where s.customer.id=? ";
		List<Object> params=new ArrayList<Object>();
		params.add(customerId);
		List<CrmCustomerGroupRelationship> list = this.baseDao.find(hql, params.toArray(), false);
		if(null != list && !list.isEmpty()){
			return list.get(0);
		}
		return null;		
	}
	
	/**
	 * 获取是否重要客户
	 * @author mqzou
	 * @date 2016-11-21
	 * @param ifaMemberId
	 * @param memberId
	 * @return
	 */
	@Override
	public boolean checkIfImportantCrm(String ifaMemberId, String memberId) {
		String hql=" from CrmCustomer r where r.ifa.member.id=? and r.member.id=? ";
		List<Object> params=new ArrayList<Object>();
		params.add(ifaMemberId);
		params.add(memberId);
		List list=this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			CrmCustomer customer=(CrmCustomer)list.get(0);
			if("1".equals(customer.getIsImportant()))
				return true;
		}
		return false;
	}
	
	@Override
	public List<CrmProspectCustomerVO> findPropectCustomerList(String langCode, String ifaMemberId,String saleStageId,String keyword){
//		String sql = "CALL  pro_getProspectCustomerList('" + ifaMemberId + "','" + saleStageId + "','" + keyword + "')";
//		List resultList = this.springJdbcQueryManager.springJdbcQueryForList(sql);
		MemberBase member = (MemberBase) baseDao.get(MemberBase.class, ifaMemberId);
		if(null == member) return null;
		String dateformat = StringUtils.isNotBlank(member.getDateFormat())?member.getDateFormat(): CommonConstants.FORMAT_DATE;
		String toCurrency = StringUtils.isNotBlank(member.getDefCurrency())?member.getDefCurrency():CommonConstants.DEF_CURRENCY;
		String sql = "CALL  pro_getProspectCustomerList(?,?,?)";
		List<Object> params = new ArrayList<Object>();
		params.add(ifaMemberId);
		params.add(saleStageId);
		params.add(keyword);
		List resultList = this.springJdbcQueryManager.springJdbcQueryForList(sql,params.toArray());
			
		
		List<CrmProspectCustomerVO> voList = new ArrayList<CrmProspectCustomerVO>();
	    Iterator it = (Iterator) resultList.iterator();
	    while (it.hasNext()) {
	    	Map map = (HashMap) it.next();
	    	CrmProspectCustomerVO vo = new CrmProspectCustomerVO();
	    	
	    	vo.setId(getMapObject(map, "id"));
	    	vo.setIfaId(getMapObject(map, "ifa_id"));
	    	vo.setMemberId(getMapObject(map, "member_id"));
	    	vo.setNickName(getMapObject(map, "nick_name"));
	    	if( StringUtils.isBlank(vo.getNickName())){
	    		vo.setNickName(getCommonMemberName(vo.getMemberId(), langCode,CommonConstants.MEMBER_NAME_NICKNAME));
	    	}
	    	
	    	//由于真名的显示原因，改用逻辑查询关键字，弃用sql查询
	    	String nickName = vo.getNickName();
	    	if(StringUtils.isNotBlank(keyword)){
	    		if(nickName.indexOf(keyword)== -1){
	    			continue;
	    		}
	    	}
	    	
	    	
	    	String pinyin = ChineseToEnglish.getPinYinHeadChar(getMapObject(map, "nick_name"));
	    	if(!"".equals(pinyin)){
	    		pinyin = pinyin.substring(0, 1).toUpperCase();
	    		vo.setPinyin(pinyin);
	    	}
	    	
	    	
	    	String createTime = getMapObject(map, "create_time");
	    	if(!"".equals(createTime)){
	    		Date dtCreateTime = DateUtil.getDate(createTime,CommonConstants.FORMAT_DATE_TIME);
	    		createTime = DateUtil.getDateTimeGoneFormatStr(dtCreateTime, langCode, dateformat);
	    		vo.setCreateTime(createTime);
	    	}
	    	
	    	String lastUpdate = getMapObject(map, "last_update");
	    	if(!"".equals(lastUpdate)){
	    		Date dtLastUpdate = DateUtil.getDate(lastUpdate,CommonConstants.FORMAT_DATE_TIME);
	    		lastUpdate = DateUtil.getDateTimeGoneFormatStr(dtLastUpdate, langCode, dateformat);
	    		vo.setLastUpdate(lastUpdate);
	    	}
	    	
	    	String privacySetting = getMapObject(map, "privacy_setting");	    	
	    	
    		if(privacySetting.indexOf("email:1") == -1){
    			vo.setEmail(getMapObject(map, "email"));
			}
    		if(privacySetting.indexOf("mobile_number:1") == -1){
    			vo.setMobileCode(getMapObject(map, "mobile_code"));
		    	vo.setMobileNumber(getMapObject(map, "mobile_number"));
			}		 	
	    	    
			//vo.setBirthdayRemind(getMapObject(map, "birthday_remind"));
	    	//vo.setAppointmentRemind(getMapObject(map, "appointment_remind"));
	    	
	    	vo.setGroupName(getMapObject(map, "group_name"));
	    	vo.setContactTimes(getMapObject(map, "contact_times"));
	    	String lastContact = getMapObject(map, "last_modify");
	    	if(!"".equals(lastContact)){
	    		Date dt = DateUtil.getDate(lastContact,CommonConstants.FORMAT_DATE_TIME);
	    		vo.setLastContact( DateUtil.getDateTimeGoneFormatStr(dt, langCode, dateformat));
	    	}
	    	vo.setProposalId(getMapObject(map, "proposal_id"));
    		String fromCurrency = getMapObject(map, "base_curr_id");
	    	vo.setProposalCurrency(toCurrency);
	    	vo.setProposalStatus(getMapObject(map, "status"));
	    	String proLastUpdate = getMapObject(map, "proposal_last_update");
	    	if(!"".equals(proLastUpdate)){
	    		Date dtProLastUpdate = DateUtil.getDate(proLastUpdate,CommonConstants.FORMAT_DATE_TIME);
	    		proLastUpdate = DateUtil.getDateTimeGoneFormatStr(dtProLastUpdate, langCode, dateformat);
	    		vo.setProposalLastUpdate(proLastUpdate);
	    	}
	    	String invAmount = getMapObject(map, "initial_invest_amount");
	    	if(!"".equals(invAmount)){
	    		double amount = Double.parseDouble(invAmount);	    		
	    		vo.setProposalInvAmount(getFormatNumByCurrency(amount, fromCurrency , toCurrency));
	    	}
	    		    	
	    	voList.add(vo);
	    }
		return voList;
	}

	/**
	 * 获取IFA的客户列表
	 * @author mqzou
	 * @date 2016-12-15
	 * @param jsonPaging
	 * @param clientSearchVO
	 * @return
	 */
	@Override
	public JsonPaging findClentsByIFA(JsonPaging jsonPaging, ClientSearchVO clientSearchVO) {
		String sql=" CALL pro_getclients(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		List<Object> params=new ArrayList<Object>();
		params.add(clientSearchVO.getIfaId());
		params.add(clientSearchVO.getClientId());
		params.add(clientSearchVO.getClientName());
		params.add(clientSearchVO.getMinReturnRate());
		params.add(clientSearchVO.getMaxReturnRate());
		params.add(clientSearchVO.getMinAuM());
		params.add(clientSearchVO.getMaxAuM());
		params.add(clientSearchVO.getClientGroup());
		params.add(clientSearchVO.getCurrency());
		params.add(clientSearchVO.getStatistic());
		params.add(jsonPaging.getOrder());
		params.add(jsonPaging.getSort());
		params.add((jsonPaging.getPage()-1)*jsonPaging.getRows());
		params.add(jsonPaging.getRows());
		params.add(null==clientSearchVO.getReCondition()?"":clientSearchVO.getReCondition());
		List resultList=this.springJdbcQueryManager.springJdbcQueryForList(sql, params.toArray());
		List<CrmClientVO> list=new ArrayList<CrmClientVO>();
		if(null!=resultList && !resultList.isEmpty()){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				HashMap map = (HashMap) it.next();
				CrmClientVO vo=new CrmClientVO();
				String id=getMapValue(map, "id");
				String memberId=getMapValue(map, "member_id");
				//String nickName=getMapValue(map, "nick_name");
				String totalRate=getMapValue(map, "total_return_rate");
				String totalValue=getMapValue(map, "total_return_value");
				String createTime=getMapValue(map, "create_time");
				String totalAuM=getMapValue(map, "totalAuM");
				
				vo.setId(id);
				vo.setMemberId(memberId);
				String nickName=getCommonMemberName(memberId, clientSearchVO.getLangCode(), "2");
				vo.setNickName(nickName);
				vo.setTotalReturnRate(null!=totalRate && !"".equals(totalRate)?Double.parseDouble(totalRate)*100:0);
				vo.setTotalReturn(null!=totalValue && !"".equals(totalValue)?Double.parseDouble(totalValue):0);
				vo.setAum(null!=totalAuM && !"".equals(totalAuM)?Double.parseDouble(totalAuM):0);
				if(null!=createTime && !"".equals(createTime)){
					Date createDate=DateUtil.getDate(createTime);
					try {
						int days=DateUtil.daysBetween(createDate, DateUtil.getCurDate());
						vo.setDays(String.valueOf(days));
					} catch (ParseException e) {
						vo.setDays("0");
						e.printStackTrace();
					}
					
				}else {
					vo.setDays("0");
				}
				list.add(vo);
				
			}
		}
		jsonPaging.setList(list);
		
		 sql=" CALL pro_getclientstotal(?,?,?,?,?,?,?,?,?,?,?)";
		 params=new ArrayList<Object>();
		params.add(clientSearchVO.getIfaId());
		params.add(clientSearchVO.getClientId());
		params.add(clientSearchVO.getClientName());
		params.add(clientSearchVO.getMinReturnRate());
		params.add(clientSearchVO.getMaxReturnRate());
		params.add(clientSearchVO.getMinAuM());
		params.add(clientSearchVO.getMaxAuM());
		params.add(clientSearchVO.getClientGroup());
		params.add(clientSearchVO.getCurrency());
		params.add(clientSearchVO.getStatistic());
		params.add(null==clientSearchVO.getReCondition()?"":clientSearchVO.getReCondition());
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
	 * 获取IFA的所有客户的概况信息
	 * @author mqzou
	 * @date 2016-12-15
	 * @param ifaId
	 * @return
	 *//*
	@Override
	public ClientsBasicVO findClientsBasic(String ifaId) {
		ClientsBasicVO vo=new ClientsBasicVO();
		vo.setTotalNum(0);
		vo.setProfitNum(0);
		vo.setLossNum(0);
		StringBuilder hql=new StringBuilder();
		hql.append("select r.id,sum(h.totalReturnRate) from  CrmCustomer r    left join PortfolioHold h on r.member.id=h.member.id ");
	//	hql.append(" select r.id,sum(h.totalReturnRate)  from PortfolioHold h inner join PortfolioInfo p on h.portfolio.id=p.id inner join CrmCustomer r on p.member.id=r.member.id");
		hql.append("  where r.ifa.id=? and r.clientType='1'");
		hql.append(" group by r.id");
	    List<Object> params=new ArrayList<Object>();
	    params.add(ifaId);
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
	
	*//**
	 * 获取IFA的所有客户的总资产
	 * @author mqzou
	 * @date 2016-12-15
	 * modify by mqzou 2017-01-06 投资人资产信息表更改
	 * @param ifaId
	 * @return
	 *//*
	@Override
	public double findClientAuM(String ifaId,String currency) {
		double total=0;
		StringBuilder hql=new StringBuilder();
		hql.append(" select c from CrmCustomer r left join InvestorAccount a on r.member.id=a.member.id and r.ifa.id=a.ifa.id ");
		hql.append(" left join InvestorAccountCurrency c on a.id=c.account.id");
		
		hql.append(" select c from CrmCustomer r left join PortfolioHold h on r.ifa.id=h.ifa.id and r.member.id=h.member.id");
		hql.append(" left join PortfolioHoldAccount c on h.id=c.portfolioHold.id");
		hql.append(" where r.ifa.id=?  and r.clientType='1'");
		List<Object> params=new ArrayList<Object>();
		params.add(ifaId);
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
					Double rate=getExchangeRate(account.getBaseCurrency(), currency);
					total+=sum*rate;
				}else {
					total+=sum;
				}
			}
		}
		return total;
	}
	*/
	/**
	 * 获取时间段内客户信息提醒
	 * @author mqzou
	 * @date 2016-12-16
	 * @param ifaId
	 * @param memberId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Override
	public ClientRemindVO findClientRemind(String ifaId, String memberId, String startDateStr, String endDateStr) {
		Date startDate=DateUtil.getDate(startDateStr);
		Date endDate=DateUtil.getDate(endDateStr);
		ClientRemindVO vo=new ClientRemindVO();
		String sql="call pro_getclientbirthdayremind(?,?,?,?)";
		List<Object> params=new ArrayList<Object>();
		params.add(ifaId);
		params.add(memberId);
		int days=0;
		try {
			days=DateUtil.daysBetween(startDate,endDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		params.add(days);
		params.add("1");
		List resultList=this.springJdbcQueryManager.springJdbcQueryForList(sql, params.toArray());
		if(null!=resultList && !resultList.isEmpty()){
			vo.setBirthday(String.valueOf(resultList.size()));
		}
		
		StringBuilder hql=new StringBuilder();
		hql.append(" from CrmSchedule r where r.customer.ifa.id=?");
		hql.append(" and (r.startDate between ? and ? or r.endDate between ? and ? or ? between r.startDate and r.endDate  )");
		hql.append(" and r.customer.clientType='1'");
		
		
		params=new ArrayList<Object>();
		params.add(ifaId);
		params.add(startDate);
		params.add(endDate);
		params.add(startDate);
		params.add(endDate);
		params.add(startDate);
		resultList=this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=resultList){
			vo.setAppoint(String.valueOf(resultList.size()));

		}
		
		
		return vo;
	}
	
	@Override
	public List findRpqPre(String ifaId, String memberId, Date startDate, Date endDate) {
		StringBuilder hql=new StringBuilder();
		hql.append(" select distinct r from CrmCustomer r left join RpqExam e on r.member.id=e.member.id");
		hql.append(" left join InvestorDoc d on r.member.id=d.member.id");
		hql.append(" where r.ifa.id=? and r.clientType='1' and  (coalesce(e.expireDate,CURRENT_DATE()) <? or coalesce(d.expireDate,CURRENT_DATE()) <?)");
		hql.append(" and r.member.id in(select a.member.id  from InvestorAccount a where a.openStatus='3') ");
		hql.append(" and e is not null");
		List<Object> params=new ArrayList<Object>();
		params.add(ifaId);
		params.add(endDate);
		params.add(endDate);
		
		if(null!=memberId && !"".equals(memberId)){
			hql.append(" and r.member.id=?");
			params.add(memberId);
		}
		List list=this.baseDao.find(hql.toString(), params.toArray(), false);
		
		return list;
	}

	/**
	 * 检查客户是是否生日
	 * @author mqzou
	 * @date 2016-12-16
	 * @param ifaId
	 * @param memberId
	 * @param days
	 * @return
	 */
	@Override
	public List checkBirthDayCustomer(String ifaId, String memberId, int days) {
		String sql="call pro_getclientbirthdayremind(?,?,?,?)";
		List<Object> params=new ArrayList<Object>();
		params.add(ifaId);
		params.add(memberId);
		params.add(days);
		params.add("1");
		List list=this.springJdbcQueryManager.springJdbcQueryForList(sql, params.toArray());
		return list;
	}
	    	
	
	public List<CrmExistingCustomerVO> findExistingCustomerList(String langCode, String ifaMemberId,String clientStatus,String keyword,String toCurrency){
		MemberBase member = (MemberBase) baseDao.get(MemberBase.class, ifaMemberId);
		if(null == member) return null;
		String dateformat = StringUtils.isNotBlank(member.getDateFormat())?member.getDateFormat(): CommonConstants.FORMAT_DATE;
		toCurrency = StringUtils.isNotBlank(member.getDefCurrency())?member.getDefCurrency():CommonConstants.DEF_CURRENCY;
		String sql = "CALL  pro_getExistingCustomerList(?,?,?)";
		List<Object> params = new ArrayList<Object>();
		params.add(ifaMemberId);
		params.add(clientStatus);
		params.add(keyword);
		List resultList = this.springJdbcQueryManager.springJdbcQueryForList(sql,params.toArray());
				
		List<CrmExistingCustomerVO> voList = new ArrayList<CrmExistingCustomerVO>();
	    Iterator it = (Iterator) resultList.iterator();
	    while (it.hasNext()) {
	    	Map map = (HashMap) it.next();
	    	CrmExistingCustomerVO vo = new CrmExistingCustomerVO();
	    	
	    	vo.setId(getMapObject(map, "id"));
	    	String ifaId = getMapObject(map, "ifa_id");
	    	String memberId = getMapObject(map, "member_id");
	    	vo.setIfaId(ifaId);
	    	vo.setMemberId(memberId);
//	    	vo.setNickName(getMapObject(map, "nick_name"));
	    	//由于真名的显示原因，改用逻辑查询关键字，弃用sql查询
	    	String nickName = getCommonMemberName(vo.getMemberId(), langCode,CommonConstants.MEMBER_NAME_REAL_NAME);
	    	if(StringUtils.isNotBlank(keyword)){
	    		if(nickName.indexOf(keyword)== -1){
	    			continue;
	    		}
	    	}
	    	vo.setNickName(nickName);

	    	String pinyin = ChineseToEnglish.getPinYinHeadChar(getMapObject(map, "nick_name"));
	    	if(!"".equals(pinyin)){
	    		pinyin = pinyin.substring(0, 1).toUpperCase();
	    		vo.setPinyin(pinyin);
	    	}
	    	
	    	String createTime = getMapObject(map, "create_time");
	    	if(!"".equals(createTime)){	    		
	    		Date dt = DateUtil.getDate(createTime,CommonConstants.FORMAT_DATE_TIME);
	    		createTime = DateUtil.getDateTimeGoneFormatStr(dt, langCode, dateformat);
	    		vo.setCreateTime(createTime);
	    	}
	    	
	    	String lastUpdate = getMapObject(map, "last_update");
	    	if(!"".equals(lastUpdate)){
	    		Date dt = DateUtil.getDate(lastUpdate,CommonConstants.FORMAT_DATE_TIME);
	    		lastUpdate = DateUtil.getDateTimeGoneFormatStr(dt, langCode, dateformat);
	    		vo.setLastUpdate(lastUpdate);
	    	}
	    	
	    	String privacySetting = getMapObject(map, "privacy_setting");	    	
	    	
    		if(privacySetting.indexOf("email:1") == -1){
    			vo.setEmail(getMapObject(map, "email"));
			}
    		if(privacySetting.indexOf("mobile_number:1") == -1){
    			vo.setMobileCode(getMapObject(map, "mobile_code"));
		    	vo.setMobileNumber(getMapObject(map, "mobile_number"));
			}		    	
	  
	    	
	    	if("Customer-Care".equalsIgnoreCase(clientStatus)){
	    		List<CrmPortfolioHoldVO> holdVOs = this.getPortfolioHoldList(ifaId,memberId,toCurrency);
	    		vo.setHoldList(holdVOs);
	    	}

	    	vo.setBirthdayRemind(getMapObject(map, "birthday_remind"));
	    	vo.setAppointmentRemind(getMapObject(map, "appointment_remind"));
	    	vo.setGroupName(getMapObject(map, "group_name"));
	    	
	    	

	    	vo.setInvestorId(getMapObject(map, "investor_id"));
	    	
	    	vo.setDistributorId(getMapObject(map, "distributor_id"));
	    	vo.setCompanyName(getMapObject(map, "company_name"));
	    	String logofile = PageHelper.getLogoUrl(getMapObject(map, "logofile"),"D");
	    	vo.setLogofile(logofile);
	    	vo.setOpenStatus(getMapObject(map, "open_status"));
	    	vo.setAccountSum(getMapObject(map, "account_sum"));
	    	String accountLastUpdate = getMapObject(map, "account_last_update");
	    	if(!"".equals(accountLastUpdate)){
	    		Date dtAccountLastUpdate = DateUtil.getDate(accountLastUpdate,CommonConstants.FORMAT_DATE_TIME);
	    		accountLastUpdate = DateUtil.getDateTimeGoneFormatStr(dtAccountLastUpdate, langCode, dateformat);
	    		vo.setAccountLastUpdate(accountLastUpdate);
	    	}

	    	vo.setProposalId(getMapObject(map, "proposal_id"));
	    	String proposalCurrency = getMapObject(map, "base_curr_id");
	    	vo.setProposalCurrency(toCurrency);
	    	
	    	vo.setProposalStatus(getMapObject(map, "proposal_status"));
	    	String proLastUpdate = getMapObject(map, "proposal_last_update");
	    	if(!"".equals(proLastUpdate)){
	    		Date dtProLastUpdate = DateUtil.getDate(proLastUpdate,CommonConstants.FORMAT_DATE_TIME);
	    		proLastUpdate = DateUtil.getDateTimeGoneFormatStr(dtProLastUpdate, langCode, dateformat);
	    		vo.setProposalLastUpdate(proLastUpdate);
	    	}
	    	String invAmount = getMapObject(map, "initial_invest_amount");
	    	if(!"".equals(invAmount)){
	    		vo.setProposalInvAmount(getFormatNumByCurrency( Double.parseDouble(invAmount),proposalCurrency,toCurrency));
	    	}

	    	vo.setPortfolioHoldId(getMapObject(map, "hold_id"));
	    	vo.setPortfolioId(getMapObject(map, "portfolio_id"));
	    	String portfolioCurrency = getMapObject(map, "portfolio_currency");
	    	vo.setPortfolioCurrency(toCurrency);
	    	vo.setPlanId(getMapObject(map, "plan_id"));
	    	vo.setPlanStatus(getMapObject(map, "plan_status"));
	    	String planBuy = getMapObject(map, "total_buy");
	    	if(!"".equals(planBuy)){
	    		vo.setPlanBuy(getFormatNumByCurrency(Double.parseDouble(planBuy), portfolioCurrency, toCurrency));
	    	}else {
	    		vo.setPlanBuy(getFormatNum(null));
			}
	    	String planSell = getMapObject(map, "total_sell");
	    	if(!"".equals(planSell)){
	    		vo.setPlanSell(getFormatNumByCurrency(Double.parseDouble(planSell),portfolioCurrency, toCurrency));
	    	}else {
	    		vo.setPlanSell(getFormatNum(null));
			}
	    	
	    	String planLastUpdate = getMapObject(map, "plan_last_update");
	    	if(!"".equals(planLastUpdate)){
	    		Date dtPlanLastUpdate = DateUtil.getDate(planLastUpdate,CommonConstants.FORMAT_DATE_TIME);
	    		planLastUpdate = DateUtil.getDateTimeGoneFormatStr(dtPlanLastUpdate, langCode, dateformat);
	    		vo.setPlanLastUpdate(planLastUpdate);
	    	}
	    	
	    	vo.setAccountNo(getMapObject(map, "account_no"));
	    	String openTime = getMapObject(map, "open_time");
 	
	    	if(!"".equals(openTime)){
	    		Date dt = DateUtil.getDate(openTime);
	    		try {
					int days = daysBetween(dt,new Date());
					vo.setOpenTime(String.valueOf(days));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    	
	    	String investorId = getMapObject(map, "investor_id");
	    	String rpqExpireDate = getMapObject(map, "rpq_expire_date");
	    	if(!"".equals(rpqExpireDate)){
	    		Date dtRpqExpireDate = DateUtil.getDate(rpqExpireDate);
	    		try {
					int days = daysBetween(new Date(), dtRpqExpireDate);
					vo.setRpqExpireDays(String.valueOf(days));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    	
	    	Date docExpireDate = getDocExpireDate(investorId);
	    	if(null != docExpireDate){
	    		try {
					int days = daysBetween(new Date(), docExpireDate);
					vo.setDocExpireDays(String.valueOf(days));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    	
	    	
	    	voList.add(vo);
	    }
		return voList;
	}

	/**
	 * 获取持仓列表
	 * @author zxtan
	 * @date 2016-12-19
	 * @return
	 */
	private List<CrmPortfolioHoldVO> getPortfolioHoldList(String ifaId,String memberId,String toCurrency){
		List<CrmPortfolioHoldVO> voList = new ArrayList<CrmPortfolioHoldVO>();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" From PortfolioHold h ");
		hql.append(" LEFT JOIN PortfolioHoldAlert a on h.id=a.portfolioHold.id and a.alertType='ifa_asc' ");
		hql.append(" LEFT JOIN PortfolioHoldAlert d on h.id=d.portfolioHold.id and d.alertType='ifa_desc' ");
		hql.append(" WHERE IFNULL(h.totalMarketValue,0)>0 and h.ifa.id=? and h.member.id=? ");
		hql.append(" and exists (select pd.id from PortfolioHoldProduct pd where pd.portfolioHold.id=h.id ) ");
		
		params.add(ifaId);
		params.add(memberId);
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			Double totalMarketValue =  0.0;	
			Double totalReturnValue =  0.0;	
			Boolean ifAscAlert = false;
			Boolean ifDescAlert = false;
			
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				PortfolioHold hold = (PortfolioHold) objs[0];
				CrmPortfolioHoldVO vo = new CrmPortfolioHoldVO();
				
				vo.setPortfolioHoldId(hold.getId());
				if( null != hold.getPortfolio()){
					vo.setPortfolioId(hold.getPortfolio().getId());
				}
				String fromCurrency = hold.getBaseCurrency();				
				vo.setBaseCurrency(toCurrency);
				
				vo.setPortfolioName(hold.getPortfolioName());
				Double tempMarketValue = getNumByCurrency(hold.getTotalMarketValue(), fromCurrency, toCurrency);
				Double tempreturnValue = getNumByCurrency(hold.getTotalReturnValue(), fromCurrency, toCurrency);
				tempMarketValue = tempMarketValue== null?0:tempMarketValue;
				tempreturnValue = tempreturnValue==null?0:tempreturnValue;
				totalMarketValue += tempMarketValue;
				totalReturnValue += tempreturnValue;
				
				vo.setTotalMarketValue(getFormatNum(tempMarketValue,toCurrency));
				vo.setTotalReturnValue(getFormatNum(tempreturnValue,toCurrency));
				
				double tempReturnRate = hold.getTotalReturnRate()==null?0.0:hold.getTotalReturnRate();
				vo.setTotalReturnRate(getFormatNumByPer(tempReturnRate));
				if(null != objs[1]){
					PortfolioHoldAlert ascAlert = (PortfolioHoldAlert) objs[2];
					if(hold.getTotalReturnRate() > ascAlert.getAlertValue()){
						vo.setAscAlert("1");
						ifAscAlert = true;
					}
				}
				if(null != objs[2]){
					PortfolioHoldAlert descAlert = (PortfolioHoldAlert) objs[3];
					if(hold.getTotalReturnRate() < descAlert.getAlertValue()){
						vo.setDescAlert("1");
						ifDescAlert = true;
					}
				}
				
				voList.add(vo);
			}
			
			if(voList.size()>1){
				Double returnRate = 0.0;
				CrmPortfolioHoldVO vo = new CrmPortfolioHoldVO();
				
				for (int i = 0; i < voList.size(); i++) {
//					Object[] objs = (Object[]) list.get(i);
//					PortfolioHold hold = (PortfolioHold) objs[0];
					double value = Double.parseDouble( voList.get(i).getTotalMarketValue().replace(",", ""));
					double rate = Double.parseDouble( voList.get(i).getTotalReturnRate().replace("%", ""))/100.0;
					returnRate += value* rate / totalMarketValue;
				}
				vo.setTotalMarketValue( getFormatNum(totalMarketValue,toCurrency));
				vo.setTotalReturnRate(getFormatNumByPer(returnRate));
				vo.setTotalReturnValue(getFormatNum(totalReturnValue,toCurrency));
				vo.setBaseCurrency(toCurrency);
				vo.setAscAlert(ifAscAlert == true? "1":"0" );
				vo.setDescAlert(ifDescAlert == true? "1":"0");
				vo.setIfSummary("1");
				voList.add(0, vo);
			}
		}
		return voList;
	}
	
	/**
	 * 获取文档过期时间
	 * @author zxtan
	 * @date 2016-12-19
	 * @return
	 */
	private Date getDocExpireDate(String accountId){
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" From InvestorDoc d ");
		hql.append(" WHERE d.account.id=? Order By d.expireDate ");		
		params.add(accountId);
		Date expireDate = null;
		List<InvestorDoc> list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			expireDate = list.get(0).getExpireDate();
		}
		return expireDate;
	}
	
	/**
	 * 检查客户是否有日程
	 * @author mqzou
	 * @date 2016-12-16
	 * @param ifaId
	 * @param memberId
	 * @return
	 */
	@Override
	public List checkSchedultCustomer(String ifaId, String memberId, Date startDate, Date endDate) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from CrmSchedule r where r.isValid='1' ");
		List<Object> params=new ArrayList<Object>();
		if(null!=ifaId && !"".equals(ifaId)){
			hql.append(" and r.customer.ifa.id=?");
			params.add(ifaId);
		}
		if(null!=memberId && !"".equals(memberId)){
			hql.append(" and r.customer.member.id=?");
			params.add(memberId);
		}
		if(null!=startDate){
		    hql.append(" and (r.startDate between ? and ? or r.endDate between ? and ? or ? between r.startDate and r.endDate  )");
			params.add(startDate);
			params.add(endDate);
			params.add(startDate);
			params.add(endDate);
			params.add(startDate);
		}
		List list=this.baseDao.find(hql.toString(), params.toArray(), false);
		return list;
	}
	
	/**
	 * 更新潜在客户状态
	 * @author zxtan
	 * @date 2016-12-19
	 * @param obj
	 * @return
	 */
	//@Override
	public Boolean updateStageId(String id,String stageId){
		String hql = "update CrmCustomer set salesStageId=?,lastUpdate=NOW() where id=? ";
		List params=new ArrayList();
		params.add(stageId);
		params.add(id);
		int resultCount = this.baseDao.updateHql(hql, params.toArray());
		return resultCount>0;
	}

	/**
	 * 获取ifa的客户列表（ifa详情）
	 * @author mqzou 2016-12-26
	 * @param ifaMemberId
	 * @param keyWord
	 * @param type 客户类型
	 * @return
	 */
	@Override
	public JsonPaging findClientForIfa(JsonPaging jsonPaging,String ifaMemberId, String keyWord,String type,String langCode) {
		 List<CrmClientForIfaVO> list=new ArrayList<CrmClientForIfaVO>();
		StringBuilder hql=new StringBuilder();
		List<Object> params=new ArrayList<Object>();
		hql.append("  from CrmCustomer r where r.isValid='1'  and r.clientType=? ");
		params.add(type);
		if(null!=ifaMemberId){
			hql.append(" and r.ifa.member.id=?");
			params.add(ifaMemberId);
		}
		if(null!=keyWord && !"".equals(keyWord)){
			hql.append(" and r.nickName like ?");
			params.add("%"+keyWord+"%");
		}
		jsonPaging=this.baseDao.selectJsonPaging(hql.toString(), params.toArray(),jsonPaging, false);
		if(null!=jsonPaging &&  null!=jsonPaging.getList()){
			Iterator it=jsonPaging.getList().iterator();
			while (it.hasNext()) {
				CrmCustomer customer = (CrmCustomer) it.next();
				CrmClientForIfaVO vo=new CrmClientForIfaVO();
				vo.setId(customer.getId());
				vo.setMemberId(customer.getMember().getId());
				vo.setNickName(customer.getNickName());
				vo.setCreateDate(DateUtil.getDateStr(customer.getCreateTime()));
				vo.setEmail(customer.getMember().getEmail());
				String mobileCode=customer.getMember().getMobileCode();
				if(null!=mobileCode && !"".equals(mobileCode)){
					mobileCode=mobileCode+"-";
				}else {
					mobileCode="";
				}
				vo.setContact(mobileCode+customer.getMember().getMobileNumber());
				vo.setIconUrl(PageHelper.getUserHeadUrl(customer.getMember().getIconUrl(), ""));
				vo.setName(getCommonMemberName(vo.getMemberId(), langCode,null));
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	/**
	 * 根据ifaId和memberId获取实体
	 * @author mqzou 2016-12-29
	 * @param ifaId
	 * @param memberId
	 * @return
	 */
	@Override
	public CrmCustomer findByIfaAndMember(String ifaId, String memberId) {
		String hql=" from CrmCustomer r where r.ifa.id=? and r.member.id=?";
		List<Object> params=new ArrayList<Object>();
		params.add(ifaId);
		params.add(memberId);
		List list=this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			CrmCustomer customer=(CrmCustomer)list.get(0);
			return customer;
		}
		return null;
	}
	
	/**
	 * 根据ifaId获取客户实体列表
	 * @author mqzou 2016-12-29
	 * @param ifaId
	 * @param memberId
	 * @return
	 */
	@Override
	public List<CrmCustomer> findCustomerByIfaId(String ifaId) {
		String hql=" from CrmCustomer r where r.ifa.id=? and r.isValid='1' ";
		List<Object> params=new ArrayList<Object>();
		params.add(ifaId);
		List<CrmCustomer> list=this.baseDao.find(hql, params.toArray(), false);
		return  list;
	}
	
	/**
	 * 根据ifa的memberId查找客户
	 * @author michael
	 * @param memberId IFA的memberId
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public List<CrmCustomer> findCustomerByIfa(String memberId) {
		String hql=" from CrmCustomer r where r.ifa.member.id=? and r.isValid='1' ";
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		List<CrmCustomer> list= (List<CrmCustomer>)this.baseDao.find(hql, params.toArray(), false);
		return list;
	}
	
	/**
	 * 检测是否存在客户
	 * @author michael
	 * @param ifaMemberId
	 * @param memberId
	 * @return
	 */
	public boolean checkIfExistCrm(String ifaMemberId, String memberId) {
		String hql=" from CrmCustomer r where r.isValid='1' and r.ifa.member.id=? and r.member.id=? ";
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
	 * 复制IFA的客户到另一IFA
	 * @author michael
	 * @date 2017-3-1
	 * @param fromMemberId 源IFA
	 * @param toMemberId 目标IFA
	 * @return Boolean
	 */
	public Boolean migrateIfaCustomer(String fromMemberId,String toMemberId,MemberBase createBy) {
		List<CrmCustomer> list = findCustomerByIfa(fromMemberId);
		if (null!=list && !list.isEmpty()){
			for (CrmCustomer f: list){
				//客户类型client_type有没要求，已存在的不加？
				if (!checkIfExistCrm(toMemberId, f.getIfa().getMember().getId())){
					boolean status = false;
					try{
						CrmCustomer nCustomer = new CrmCustomer();
						BeanUtils.copyProperties(f, nCustomer);
						nCustomer.setId(null);
						nCustomer.setRemark(nCustomer.getRemark()+"\n(copy from "+nCustomer.getIfa().getMember().getNickName()+")");
						nCustomer.setCreateTime(new Date());
						nCustomer.setLastUpdate(new Date());
						this.baseDao.saveOrUpdate(nCustomer);
						
						//旧记录设为冻结状态
						f.setIsValid("0");
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
					hist.setToMember(memberBaseService.findById(toMemberId));
					hist.setCusMember(f.getMember());
					hist.setDataType("CrmCustomer");
					hist.setStatus(status?"1":"0");
					ifaMigrateHistService.saveOrUpdate(hist);
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 根据客户的memberId查找客户
	 * @author michael
	 * @param memberId 客户id
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public List<CrmCustomer> findCustomerByMember(String memberId) {
		String hql=" from CrmCustomer r where r.member.id=? and r.isValid='1' ";
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		List<CrmCustomer> list= (List<CrmCustomer>)this.baseDao.find(hql, params.toArray(), false);
		return list;
	}

	/**
	 * 获取备注记录
	 * @author wwluo
	 * @param ifaMemberId 
	 * @param memberId 
	 * @return
	 */
	@Override
	public List<CrmMemo> findCrmMemo(String ifaMemberId, String memberId) {
		List<CrmMemo> memos = null;
		if (StringUtils.isNotBlank(ifaMemberId) && StringUtils.isNotBlank(memberId)) {
			StringBuffer hql = new StringBuffer(""
					+ " FROM"
					+ " CrmMemo m"
					+ " WHERE"
					+ " m.ifa.id=?"
					+ " AND"
					+ " m.member.id=?"
					+ " ORDER BY"
					+ " m.lastModify"
					+ " DESC"
					+ " ");
			List<Object> params = new ArrayList<Object>();
			params.add(ifaMemberId);
			params.add(memberId);
			memos = this.baseDao.find(hql.toString(), params.toArray(), false);
		}
		return memos;
	}
}
