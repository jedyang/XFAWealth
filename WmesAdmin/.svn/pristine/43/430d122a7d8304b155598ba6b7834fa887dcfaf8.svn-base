package com.fsll.wmes.crm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.crm.service.CrmCustomerService;
import com.fsll.wmes.crm.vo.CrmCustomerVO;
import com.fsll.wmes.crm.vo.PortfolioVO;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberCorporate;
import com.fsll.wmes.entity.MemberFi;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.PortfolioInfo;
import com.fsll.wmes.investor.vo.InvestorVO;
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
	/**
	 * 获取ifa的客户列表
	 * @author qgfeng
	 * @date 2016-11-21
	 */
	@Override
	public JsonPaging findInvestorByIfa(JsonPaging jsonPaging, IfaVO ifaVO, String langCode) {
		String hql =" from CrmCustomer c where c.isValid='1'";
		
		List<String> params = new ArrayList<String>();
		if(StringUtils.isNoneBlank(ifaVO.getId())){
			hql+=" and c.ifa.id=? ";
			params.add(ifaVO.getId());
		}
		if(StringUtils.isNoneBlank(ifaVO.getNickName())){
			hql+=" and c.ifa.member.nickName like ? ";
			params.add("%"+ifaVO.getNickName()+"%");
		}
		
		if(StringUtils.isNoneBlank(ifaVO.getCompanyIfafirm())){
			hql +=" and c.ifa.id in ( select f.ifa.id from MemberIfaIfafirm f left join "+getLangString("MemberIfafirm", langCode)+" lang ";
			hql +=" on f.ifafirm.id=lang.id where lang.companyName like ?)";
			params.add("%"+ifaVO.getCompanyIfafirm()+"%");
		}
		if(StringUtils.isNoneBlank(ifaVO.getKeyword())){
			hql +=" and ( c.member.loginCode like ? or c.nickName like ? )";
			params.add("%"+ifaVO.getKeyword()+"%");
			params.add("%"+ifaVO.getKeyword()+"%");
		}
		
		//modify rqwang 20170515 start
		hql += " and c.ifa.id in (select ifa.id from MemberIfa ifa ) and c.member.id in (select mem.id from MemberBase mem) ";
		jsonPaging = baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		List<CrmCustomer> list = jsonPaging.getList();
		List<InvestorVO> listVo = new ArrayList<InvestorVO>();
		if(list!=null && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				CrmCustomer crm =  list.get(i);
				InvestorVO vo=new InvestorVO();
				vo.setId(crm.getId());
				
				vo.setMemberId(crm.getMember()== null? "":crm.getMember().getId());
				vo.setLoginCode(crm.getMember() == null? "":crm.getMember().getLoginCode());
				vo.setMemberType(crm.getMember() == null? "":crm.getMember().getSubMemberType()+"");
				vo.setMobileNumber(crm.getMember()== null? "":crm.getMember().getMobileNumber());
				vo.setEmail(crm.getMember()== null ? "":crm.getMember().getEmail());
				vo.setNickName(crm.getNickName() );
				vo.setIfaName(crm.getIfa() == null? "":crm.getIfa().getNameChn());
				if(vo.getMemberType()!=null && !"".equals(vo.getMemberType()) && !"null".equals(vo.getMemberType())){
					setInvestor(vo);
				}
		//end
				listVo.add(vo);
				
			}
		}
		jsonPaging.setList(listVo);
		return jsonPaging;
	}
	
	/**
	 * ifaFirm获取ifa的客户列表  rqwang 20170606
	 */
	@Override
	public JsonPaging firmFindInvestorByIfa(JsonPaging jsonPaging,
			String ifafirmId, IfaVO ifaVO, String langCode) {
		String hql = "select c,md from CrmCustomer c " +
				" left join MemberBase b" +
				" on" +
				" b.id=c.member.id" +
				" left join MemberIndividual md on md.member.id = c.member.id ";
		hql += " where c.isValid='1' ";
		List<String> params = new ArrayList<String>();
		
		hql +=" and c.ifa.id in ( select f.ifa.id from MemberIfaIfafirm f  where f.ifafirm.id=?)";
		params.add(ifafirmId);
		
		if(StringUtils.isNoneBlank(ifaVO.getKeyword())){
			hql +=" and ( b.loginCode like ? or md.firstName like ?  or md.lastName like ? or md.nameChn like ? )";
			params.add("%"+ifaVO.getKeyword()+"%");
			params.add("%"+ifaVO.getKeyword()+"%");
			params.add("%"+ifaVO.getKeyword()+"%");
			params.add("%"+ifaVO.getKeyword()+"%");
		}
		

		
		hql += " and c.ifa.id in (select ifa.id from MemberIfa ifa ) and c.member.id in (select mem.id from MemberBase mem) ";

		jsonPaging = baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		List list = jsonPaging.getList();
		List<InvestorVO> listVo = new ArrayList<InvestorVO>();
		if(list!=null && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				CrmCustomer crm =  (CrmCustomer)obj[0];;
				MemberIndividual memIndividual = (MemberIndividual)obj[1];
				InvestorVO vo=new InvestorVO();
				
				if(langCode.equalsIgnoreCase("en")){
					String firstName = memIndividual.getFirstName();
					String lastName = memIndividual.getLastName();
					String name = firstName + " " + lastName;
					vo.setNameChn(name);
				}else{
					vo.setNameChn(memIndividual.getNameChn());
				}
				
				if(null != memIndividual.getNationality() && !"".equals(memIndividual.getNationality())){
					vo.setNationality(memIndividual.getNationality() == null ? "" : memIndividual.getNationality().getCountryName(langCode));
				}
				vo.setId(crm.getId());
				vo.setMemberId(crm.getMember()== null? "":crm.getMember().getId());
				vo.setLoginCode(crm.getMember() == null? "":crm.getMember().getLoginCode());
				vo.setMemberType(crm.getMember() == null? "":crm.getMember().getSubMemberType()+"");
				vo.setMobileNumber(crm.getMember()== null? "":crm.getMember().getMobileNumber());
				vo.setMobileCode(crm.getMember() == null? "":crm.getMember().getMobileCode());
				vo.setEmail(crm.getMember()== null ? "":crm.getMember().getEmail());
				vo.setNickName(crm.getNickName() );
				if(vo.getMemberType()!=null && !"".equals(vo.getMemberType()) && !"null".equals(vo.getMemberType())){
					setInvestor(vo);
				}
				
				listVo.add(vo);
			}
		}
		jsonPaging.setList(listVo);
		return jsonPaging;
	}
	
	/**
	 * dis获取的客户列表  rqwang 20170607
	 */
	@Override
	public JsonPaging disFindInvestor(JsonPaging jsonPaging, String id,
			IfaVO ifaVO, String langCode) {
		String hql =" from MemberIndividual i left join InvestorAccount a on i.member.id = a.member.id ";
		hql += " where a.openStatus='3'";//开户成功
		List<String> params = new ArrayList<String>();
		hql += " and a.distributor.id=?";
		params.add(id);
		
		if(StringUtils.isNoneBlank(ifaVO.getKeyword())){
			hql +=" and ( i.nameChn like ? or i.firstName like ?  or i.lastName like ? or a.accountNo like ?)";
			params.add("%"+ifaVO.getKeyword()+"%");
			params.add("%"+ifaVO.getKeyword()+"%");
			params.add("%"+ifaVO.getKeyword()+"%");
			params.add("%"+ifaVO.getKeyword()+"%");
		}
		
		jsonPaging = baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		List list = jsonPaging.getList();
		List<InvestorVO> listVo = new ArrayList<InvestorVO>();
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				MemberIndividual in = (MemberIndividual)obj[0];
				InvestorAccount ia = (InvestorAccount)obj[1];
				InvestorVO vo = new InvestorVO();
				
				vo.setAccountNo(ia.getAccountNo());
				
				
				if(langCode.equalsIgnoreCase("en")){
					String firstName = in.getFirstName();
					String lastName = in.getLastName();
					String name = firstName + " " + lastName;
					vo.setNameChn(name);
				}else{
					vo.setNameChn(in.getNameChn());
				}
				
				if(null != in.getMember() && !"".equals(in.getMember())){
					vo.setEmail(in.getMember().getEmail());
					vo.setMobileNumber(in.getMember().getMobileNumber());
					vo.setMobileCode(in.getMember().getMobileCode());
				}
				
				if(null != in.getNationality() && !"".equals(in.getNationality())){
					
					vo.setNationality(in.getNationality() == null ? "" : in.getNationality().getCountryName(langCode));
				}
				vo.setMemberType(in.getMember() == null? "":in.getMember().getSubMemberType()+"");
				vo.setRelateId(in.getId() == null ? "" : in.getId());
				listVo.add(vo);
			}
		}
		jsonPaging.setList(listVo);
		return jsonPaging;
	}	
	
	
	//设置投资人，投资类型
	public void setInvestor(InvestorVO vo){
		if (CommonConstantsWeb.MEMBERBASE_SUB_MEMBERTYPE_INDIVIDUAL == Integer.parseInt(vo.getMemberType().trim())){
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

			vo.setAipType(info.getAipFlag());
			vo.setCreateTime(info.getCreateTime());
			vo.setCreator(null!=info.getCreator()?info.getCreator().getNickName():"");
			vo.setCreatorId(null!=info.getCreator()?info.getCreator().getId():"");
			vo.setId(info.getId());
			vo.setLoginCode(null!=info.getMember()?info.getMember().getLoginCode():"");
			vo.setMemberId(null!=info.getMember()?info.getMember().getId():"");
			vo.setNickName(null!=info.getMember()?info.getMember().getNickName():"");
			vo.setPortfolioName(info.getPortfolioName());
			vo.setProposalId(null!=info.getProposal()?info.getProposal().getId():"");
			vo.setProposalName(null!=info.getProposal()?info.getProposal().getProposalName():"");
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
		sBuilder.append("SELECT c.id,c.ifa_id,c.member_id,c.nick_name,c.icon_url,c.remark,mb.mobile_code,mb.mobile_number,mb.email,");
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
		}
		
		return vo;
	}
	
		
	/**
	 * 获取客户列表
	 * @author zxtan
	 * @date 2016-09-26
	 */
	@Override
	public JsonPaging findCustomerListForIfa(JsonPaging jsonPaging,String ifaMemberId,String areaId,String period,String saleStageId,String keyword){
		StringBuilder sBuilder = new StringBuilder();
		List<Object> params=new ArrayList<Object>();
		sBuilder.append("SELECT c.id,c.ifa_id,c.member_id,c.nick_name,c.icon_url,c.remark,c.create_time,c.sales_stage_id,c.is_important,mb.email,mb.mobile_code,mb.mobile_number,");
		sBuilder.append("mi.first_name,mi.last_name,mi.name_chn,mi.gender,mi.nationality,mi.born,"); 
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
			sBuilder.append(" CASE WHEN CAST(CONCAT(YEAR(NOW()),DATE_FORMAT(mi.born,'-%m-%d'))AS DATE) >= CAST(DATE_FORMAT(NOW(),'%y-%m-%d') AS DATE) AND CAST(CONCAT(YEAR(NOW()),DATE_FORMAT(mi.born,'-%m-%d'))AS DATE) <= DATE_ADD(NOW(),INTERVAL "+intervalUnit+") THEN 1 ELSE 0 END AS birthday_remind,");
			sBuilder.append(" ( SELECT COUNT(0) FROM crm_schedule WHERE customer_id=c.id AND start_date >= CAST(DATE_FORMAT(NOW(),'%y-%m-%d') AS DATE) AND start_date < DATE_ADD(NOW(),INTERVAL "+intervalUnit+")) AS appointment_remind");
		}
		sBuilder.append(" FROM crm_customer c ");
		sBuilder.append(" LEFT JOIN member_base mb ON c.member_id=mb.id ");
		sBuilder.append(" LEFT JOIN member_individual mi ON mb.id=mi.member_id ");
		sBuilder.append(" LEFT JOIN member_ifa ifa ON c.ifa_id=ifa.id ");
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
			
		}
		
		if(keyword != null && !"".equals(keyword)){
			sBuilder.append(" AND ( mb.email LIKE ? OR mb.mobile_number LIKE ? OR c.nick_name LIKE ? ) ");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
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
	public JsonPaging findExistingCustomerListForIfa(JsonPaging jsonPaging,String ifaMemberId,String areaId,String period,String clientStatus,String keyword){
		StringBuilder sBuilder = new StringBuilder();
		List<Object> params=new ArrayList<Object>();
		sBuilder.append("SELECT c.id,c.ifa_id,c.member_id,c.nick_name,c.icon_url,c.remark,c.create_time,c.sales_stage_id,c.is_important,mb.email,mb.mobile_code,mb.mobile_number,");
		sBuilder.append("mi.first_name,mi.last_name,mi.name_chn,mi.gender,mi.nationality,mi.born,"); 
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
			sBuilder.append(" CASE WHEN CAST(CONCAT(YEAR(NOW()),DATE_FORMAT(mi.born,'-%m-%d'))AS DATE) >= CAST(DATE_FORMAT(NOW(),'%y-%m-%d') AS DATE) AND CAST(CONCAT(YEAR(NOW()),DATE_FORMAT(mi.born,'-%m-%d'))AS DATE) <= DATE_ADD(NOW(),INTERVAL "+intervalUnit+") THEN 1 ELSE 0 END AS birthday_remind,");
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
		sBuilder.append(" WHERE c.client_type= '1' ");
		

		if(ifaMemberId != null && !"".equals(ifaMemberId)){
			sBuilder.append(" AND ifa.member_id= ? ");	
			params.add(ifaMemberId);
		}
		
		if(clientStatus != null && !"".equals(clientStatus)){
			if("Customer-Care".equalsIgnoreCase(clientStatus)){
				sBuilder.append(" AND EXISTS (SELECT p.id FROM portfolio_info p INNER JOIN portfolio_hold h ON p.id= h.portfolio_id ");
				sBuilder.append(" WHERE p.member_id=c.member_id AND c.ifa_id= p.ifa_id ) ");				
			}else if("Opening-Account".equalsIgnoreCase(clientStatus)){
				sBuilder.append(" AND EXISTS (SELECT ia.id FROM investor_account ia WHERE c.ifa_id=ia.ifa_id AND c.member_id=ia.member_id ");
				sBuilder.append(" AND ia.open_status not in ('0','1') ) ");
			}else if("Portfolio".equalsIgnoreCase(clientStatus)){
				sBuilder.append(" AND EXISTS (SELECT cp.id FROM crm_proposal cp INNER JOIN portfolio_info p ON cp.id=p.proposal_id  ");
				sBuilder.append(" INNER JOIN order_plan op ON p.id = op.portfolio_id ");
				sBuilder.append(" WHERE p.member_id=c.member_id AND c.ifa_id= p.ifa_id AND cp.status='1' AND op.flow_status='0' ) ");
			}else if("Proposal".equalsIgnoreCase(clientStatus)){
				sBuilder.append(" AND EXISTS (SELECT p.id FROM crm_proposal p  ");
				sBuilder.append(" WHERE p.member_id=c.member_id AND p.ifa_id= c.ifa_id AND p.status='0' ) ");
			}else if("Not-Yet-Invest".equalsIgnoreCase(clientStatus)){
				sBuilder.append(" AND EXISTS (SELECT ia.id FROM investor_account ia WHERE c.ifa_id=ia.ifa_id AND c.member_id=ia.member_id AND ia.open_status ='1' ) ");
				sBuilder.append(" AND EXISTS (SELECT p.id FROM crm_proposal p WHERE p.member_id=c.member_id AND p.ifa_id= c.ifa_id AND p.status='1' ) ");
			}else {
				sBuilder.append(" AND 1=2 ");
			}			
		}
		
		if(keyword != null && !"".equals(keyword)){
			sBuilder.append(" AND ( mb.email LIKE ? OR mb.mobile_number LIKE ? OR c.nick_name LIKE ? ) ");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
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
	
	
	
	private String getMapValue(Map map,String keyString){
		String keyValue = map.get(keyString) == null? "" : map.get(keyString).toString();
		return keyValue;
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
	public void deleteCustomer(String id){
		CrmCustomer obj = findCustomerById(id);
		this.baseDao.delete(obj);
	}

	
	@Override
	public JsonPaging findCustomerByIfa(JsonPaging jsonPaging,MemberIfa memberIfa) {
		String hql=" from CrmCustomer r where r.ifa.id='"+memberIfa.getId()+"' and r.isValid='1'";
		jsonPaging =this.baseDao.selectJsonPaging(hql, null, jsonPaging, false);
		Iterator it=jsonPaging.getList().iterator();
		List list=new ArrayList();
		while (it.hasNext()) {
			CrmCustomer object = (CrmCustomer) it.next();
			list.add(object);
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}


}
