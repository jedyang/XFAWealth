package com.fsll.wmes.ifa.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.core.service.AccessoryFileService;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.crm.service.CrmCustomerService;
import com.fsll.wmes.crm.vo.ProposalVO;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.CrmProposal;
import com.fsll.wmes.entity.IfaDistributor;
import com.fsll.wmes.entity.IfafirmTeam;
import com.fsll.wmes.entity.IfafirmTeamIfa;
import com.fsll.wmes.entity.InsightInfo;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfaIfafirm;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.PortfolioArena;
import com.fsll.wmes.entity.PortfolioArenaCount;
import com.fsll.wmes.entity.PortfolioArenaReturn;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.StrategyInfo;
import com.fsll.wmes.ifa.service.IfaManageService;
import com.fsll.wmes.ifa.vo.AutoCompleteVO;
import com.fsll.wmes.ifa.vo.IfaDistributorVO;
import com.fsll.wmes.ifa.vo.IfaProposalVO;
import com.fsll.wmes.ifa.vo.InsightInfoVo;
import com.fsll.wmes.ifa.vo.StrategyInfoVO;
import com.fsll.wmes.ifafirm.vo.IfaMyTeamListVO;
import com.fsll.wmes.ifafirm.vo.IfafirmTeamVO;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.vo.IfaVO;
import com.fsll.wmes.portfolio.service.PortfolioArenaService;
import com.fsll.wmes.portfolio.vo.CriteriaVO;
import com.fsll.wmes.portfolio.vo.PortfolioArenaVO;
import com.fsll.wmes.strategy.service.StrategyInfoService;
import com.fsll.wmes.strategy.vo.CharDataVO;
import com.fsll.wmes.strategy.vo.StrategyAllocationTypeVO;
import com.fsll.wmes.strategy.vo.StrategyInfoWebVO;
import com.fsll.wmes.web.service.WebFollowStatusService;

/***
 * 业务接口实现类：ifa信息管理接口实现类
 * 
 * @author wwluo
 * @version 1.0
 * @date 2016-08-15
 */
@Service("ifaManageService")
//@Transactional
public class IfaManageServiceImpl extends BaseService implements IfaManageService {

	@Autowired
	MemberBaseService memberBaseService;
	@Autowired
	private SysParamService sysParamService;
	@Autowired
	private StrategyInfoService strategyInfoService;
	@Autowired
	private PortfolioArenaService portfolioArenaService;
	@Autowired
	private WebFollowStatusService followStatusService; 
	@Autowired
	private CrmCustomerService crmCustomerService;
	@Autowired
	private AccessoryFileService accessoryFileService;
	/**
    * 根据id查询ifa
    * @author qgfeng
    * @date 2016-12-14
    */
	public MemberIfa findIfaById(String ifaId) {
		if(StringUtils.isBlank(ifaId)){
			return null;
		}
		MemberIfa memberIfa = (MemberIfa) this.baseDao.get(MemberIfa.class, ifaId);
		return memberIfa;
	}
	/**
	 * 更新或保存ifa
	 * @author qgfeng
	 * @date 2016-12-28
	 * modify by mqzou 2017-02-07  修改IM端的信息
	 */
	@Override
	public MemberIfa saveOrUpdateIFA(MemberIfa memberIfa) {
		MemberBase memberBase = memberIfa.getMember();
		
		memberBase.setImNickName(memberBase.getNickName());
		memberBase.setImNickUrl("");
		if(null==memberBase.getImUserId() || "".equals(memberBase.getImUserId())){
			memberBase.setImUserId(UUID.randomUUID().toString());
			memberBase.setImUserPwd(memberBase.getImUserId());
		}
		
		if(StringUtils.isBlank(memberIfa.getId())){
			memberBase.setId(null);
			memberBase = (MemberBase) baseDao.create(memberBase);
			memberIfa.setMember(memberBase);
			memberIfa.setId(null);
			memberIfa = (MemberIfa) baseDao.create(memberIfa);
			memberBaseService.saveMemberToIm(memberBase, true);
		}else {
			memberBase = (MemberBase) baseDao.update(memberBase);
			memberIfa.setMember(memberBase);
			memberIfa = (MemberIfa) baseDao.update(memberIfa);
			memberBaseService.saveMemberToIm(memberBase, false);
		}
		return memberIfa;
	}

	 /**
     * 查询条件自动填充IFA
     * @author qgfeng
     * @param keyWord
     * @return
     */
	@Override
	public List<AutoCompleteVO> findAutoIfa(String keyWord,String ifaFirmId) {
		String hql = " from MemberIfa r where r.id in ";
		hql += "( select irm.ifa.id from MemberIfaIfafirm irm where irm.ifafirm.id = ?)";
		List<String> params = new ArrayList<String>();
		params.add(ifaFirmId);
		if (StringUtils.isNotBlank(keyWord)) {
			hql += " and r.member.nickName like ?";
			params.add("%"+keyWord+"%");
		}
		List<MemberIfa> list = baseDao.find(hql, params.toArray(), false);
		List<AutoCompleteVO> finalList = new ArrayList<AutoCompleteVO>();
		Iterator<MemberIfa> iterator = list.iterator();
		while (iterator.hasNext()) {
			MemberIfa ifa = iterator.next();
			if(null != ifa.getMember()){
				AutoCompleteVO vo = new AutoCompleteVO();
				vo.setId(ifa.getId());
				vo.setName(ifa.getMember().getNickName());
				finalList.add(vo);
			}
		}
		return finalList;
	}
	
	/**
	 * 获取ifafirm下所有的ifa
	 * @author qgfeng
	 * 		modified by michael @20170328，排除 多条审批记录 出现ifa重复显示问题
	 * @date 2016-12-13
	 */
	@Override
	public JsonPaging findIfaByIfaFirm(JsonPaging jsonPaging,IfaVO ifaVo, String langCode) {
		List<String> parms = new ArrayList<String>();
		/*String hql = " select distinct i,ifm.ifafirm.id AS ifafirmId, ifm.checkStatus from MemberIfa i ";
		hql += " LEFT JOIN MemberIfaIfafirm ifm on ifm.ifa.id=i.id ";
		hql += " LEFT JOIN MemberBase b on i.member.id=b.id ";
		hql += " where ifm.ifafirm.id =? and (current_date()<=ifm.endDate or ifm.endDate is null) ";*/
		
		String hql = " SELECT i from MemberIfa i ";
		hql += " LEFT JOIN MemberBase b on i.member.id=b.id ";
		
		hql += " WHERE i.id in (select ifm.ifa.id from MemberIfaIfafirm ifm where ifm.ifafirm.id =? " +
				"and (current_date()<=ifm.endDate or ifm.endDate is null) ";
		parms.add(ifaVo.getIfafirmId());
		if(StringUtils.isNoneBlank(ifaVo.getCheckStatus())){
			hql+=" and ifm.checkStatus = ? ";
			parms.add(ifaVo.getCheckStatus());
		}
		hql += " ) ";
		
		if (null != ifaVo && StringUtils.isNoneBlank(ifaVo.getKeyword())) {
			hql += " and (b.nickName like ? or b.loginCode like ? or i.nameChn like ?)";
			parms.add("%"+ifaVo.getKeyword()+"%");
			parms.add("%"+ifaVo.getKeyword()+"%");
			parms.add("%"+ifaVo.getKeyword()+"%");
		}

		jsonPaging = this.baseDao.selectJsonPaging(hql, parms.toArray(), jsonPaging, false);
		List<IfaVO> list = new ArrayList<IfaVO>();
		if (null!=jsonPaging.getList()){
			Iterator it = jsonPaging.getList().iterator();
			while (it.hasNext()) {
				MemberIfa ifa = (MemberIfa) it.next();
				if (null!=ifa && !StrUtils.isEmpty(ifa.getId())){
					IfaVO vo = new IfaVO(ifa,langCode);
					
					//查找ifafirm
					MemberIfaIfafirm ifafirm = findIfaIfaFirmByIfaId(ifa.getId(), null);
					if (null!=ifafirm){
						vo.setIfafirmId(StrUtils.getString(ifafirm.getId()));
						vo.setCheckStatus(StrUtils.getString(ifafirm.getCheckStatus()));
					}
					
					//查找ifa团队
					List teamList = findAllTeamByIfaId(ifa.getId(), null);
					if (null!=teamList && !teamList.isEmpty()){
						vo.setTeamList(teamList);
						vo.setTeams("");
						for (int i=0;i<teamList.size();i++){
							IfafirmTeam t = (IfafirmTeam)teamList.get(i);
							if (null!=t && !StrUtils.isEmpty(t.getName()))
								vo.setTeams(vo.getTeams()+t.getName()+";");
						}
						if (vo.getTeams().endsWith(";")) vo.getTeams().substring(0,vo.getTeams().length()-1);
					}
					
					list.add(vo);
				}
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	
	/***
	 * 查询ifa关联的"团队"分页显示
	 * @author wwluo 
	 * 			update by qgfeng
	 * 			update by michael
	 * @date 2016-08-15
	 * @param ifaId
	 * @param keyword
	 * @return
	 */
	public JsonPaging findTeamByIfaId(JsonPaging jsonPaging, String ifaId,String keyword) {
		StringBuffer hql = new StringBuffer("select t from IfafirmTeam t left join IfafirmTeamIfa r on t.id=r.team.id where r.ifa.id=? ");
		List<String> params = new ArrayList<String>();
		params.add(ifaId);
		if(StringUtils.isNotBlank(keyword)){
			hql.append(" and (r.team.code like ? or r.team.name like ? )");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List result = new ArrayList();
		if (null!=jsonPaging.getList() && !jsonPaging.getList().isEmpty()){
			List list = jsonPaging.getList();
			for (int i=0;i<list.size();i++){
				IfafirmTeam team = (IfafirmTeam)list.get(i);
				IfafirmTeamVO vo = new IfafirmTeamVO();
				BeanUtils.copyProperties(team, vo);
				List s = findTeamSV(team.getId());
				vo.setSupervisorList(s);
				if (null!=s && !s.isEmpty()){
					vo.setSupervisor(((MemberBase)s.get(0)).getNickName());
				}
				result.add(vo);
			}
		}
		jsonPaging.setList(result);
		return jsonPaging;
	}

	/***
	 * 查询ifa关联的"团队"分页显示（不获取组长）
	 * @author michael
	 * @date 2016-08-15
	 * @param ifaId
	 * @param keyword
	 * @return
	 */
	public List findAllTeamByIfaId(String ifaId,String keyword) {
		StringBuffer hql = new StringBuffer("select t from IfafirmTeam t left join IfafirmTeamIfa r on t.id=r.team.id where r.ifa.id=? ");
		List<String> params = new ArrayList<String>();
		params.add(ifaId);
		if(StringUtils.isNotBlank(keyword)){
			hql.append(" and (r.team.code like ? or r.team.name like ? )");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
		}
		List result = this.baseDao.find(hql.toString(), params.toArray(), false);
//		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
//		for (int i=0;i<list.size();i++){
//			IfafirmTeam team = (IfafirmTeam)list.get(i);
//			IfafirmTeamVO vo = new IfafirmTeamVO();
//			BeanUtils.copyProperties(team, vo);
//			List s = findTeamSV(team.getId());
//			vo.setSupervisorList(s);
//			if (null!=s && !s.isEmpty()){
//				vo.setSupervisor(((MemberBase)s.get(0)).getNickName());
//			}
//			result.add(vo);
//		}
		return result;
	}
	
	/***
	 * 查询"团队"的组长
	 * @author michael
	 * @date 2017-03-01
	 * @param teamId
	 * @param ifaId
	 * @return
	 */
	public List findTeamSV(String teamId) {
		StringBuffer hql = new StringBuffer("select t from MemberBase t left join MemberIfa i on t.id=i.member.id left join IfafirmTeamIfa r on i.id=r.ifa.id where r.isSupervisor='1' and r.team.id=? ");
		List<String> params = new ArrayList<String>();
		params.add(teamId);
		List result = this.baseDao.find(hql.toString(), params.toArray(), false);
		return result;
	}
	
	/**
	 * 查询ifa所属ifafirm下的"组织架构"
	 * @author wwluo
	 * @date 2016-08-16
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JsonPaging findTeamByIfaFirm(JsonPaging jsonPaging, String ifaFirmId) {
		StringBuffer hql = new StringBuffer(" from IfafirmTeam where parent_id is null and isValid='1' and ifafirm_id=? order by order_by");
		List paramsList = new ArrayList();
		paramsList.add(ifaFirmId);
		jsonPaging= baseDao.selectJsonPaging(hql.toString(), paramsList.toArray(), jsonPaging, false);
		List list = new ArrayList();
		for(Object obj : jsonPaging.getList()){
			list.add(obj);
		}
		jsonPaging.getList().clear();
		compositor(jsonPaging,list, ifaFirmId);
		return jsonPaging;
	}

	/***
	 * 对ifafirmTeam集合进行排序
	 * @author wwluo
	 * @date 2016-08-16
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void compositor(JsonPaging jsonPaging, List list, String ifaFirmId) {
		if(list != null && !list.isEmpty()){
			for (Object obj : list) {
				IfafirmTeam ifafirmTeam = (IfafirmTeam) obj;
				jsonPaging.getList().add(ifafirmTeam);
				String hql = " from IfafirmTeam where parent=? and isValid=1 and ifafirm_id=? order by order_by";
				List paramsList = new ArrayList();
				paramsList.add(ifafirmTeam);
				paramsList.add(ifaFirmId);
				List parantList = this.baseDao.find(hql.toString(), paramsList.toArray(), false);
				compositor(jsonPaging, parantList, ifaFirmId);
			}
		}
	}

	/**
	 * ifa关联team
	 * @author wwluo
	 * @date 2016-08-16
	 */
	@Override
	public boolean saveIfafirmTeamIfa(String ifaFirmId, String teamId, String ifaId) {
		if(StringUtils.isBlank(ifaFirmId) || StringUtils.isBlank(teamId) || StringUtils.isBlank(ifaId)){
			return false;
		}
		String hql = " from IfafirmTeamIfa r where r.ifafirm.id=? and r.team.id=? and r.ifa.id=?";
		List<String> paramsList = new ArrayList<String>();
		paramsList.add(ifaFirmId);
		paramsList.add(teamId);
		paramsList.add(ifaId);
		List<IfafirmTeamIfa> list = baseDao.find(hql, paramsList.toArray(), false);
		if(list == null || list.isEmpty()){
			MemberIfafirm memberIfafirm = (MemberIfafirm) this.baseDao.get(MemberIfafirm.class, ifaFirmId);
			IfafirmTeam ifafirmTeam = (IfafirmTeam) this.baseDao.get(IfafirmTeam.class, teamId);
			MemberIfa memberIfa = (MemberIfa) this.baseDao.get(MemberIfa.class, ifaId);
			if(memberIfafirm != null && ifafirmTeam != null && memberIfa != null){
				IfafirmTeamIfa ifafirmTeamIfa = new IfafirmTeamIfa();
				ifafirmTeamIfa.setIfa(memberIfa);
				ifafirmTeamIfa.setIfafirm(memberIfafirm);
				ifafirmTeamIfa.setTeam(ifafirmTeam);
				ifafirmTeamIfa.setIfa(memberIfa);
				ifafirmTeamIfa.setIsSupervisor("0");
				IfafirmTeamIfa obj = (IfafirmTeamIfa) this.baseDao.saveOrUpdate(ifafirmTeamIfa);
				if(obj != null){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * ifa取消关联team
	 * @author wwluo
	 * @date 2016-08-16
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean removeIfafirmTeamIfa(String teamId, String ifaId) {
		StringBuffer hql = new StringBuffer(" from IfafirmTeamIfa where team_id=? and ifa_id=?");
		List params = new ArrayList<String>();
		params.add(teamId);
		params.add(ifaId);
		List ifafirmTeamIfaList = this.baseDao.find(hql.toString(), params.toArray(), false);
		if (null != ifafirmTeamIfaList && !ifafirmTeamIfaList.isEmpty()) {
			this.baseDao.delete(ifafirmTeamIfaList.get(0));
			return true;
		} else
			return false;
	}
	//------ end Team --------
	 /**
	   * 查询策略列表
	   * @author qgfeng
	   * @date 2016-12-20
	   */
	@Override
	public JsonPaging findStrategyList(JsonPaging jsonPaging,StrategyInfoVO infoVO, String langCode) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from StrategyInfo r where r.isValid='1' and r.creator.id is not null");
		List<Object> params = new ArrayList<Object>();
		if (null != infoVO) {
			if(StringUtils.isNotBlank(infoVO.getCreatorId())){
				hql.append(" and r.creator.id = ? ");
				params.add(infoVO.getCreatorId());
			}
			if(StringUtils.isNotBlank(infoVO.getRiskLevel())){
				hql.append(" and r.riskLevel = ? ");
				params.add(infoVO.getRiskLevel());
			}
			if(StringUtils.isNotBlank(infoVO.getStrategyName())){
				hql.append(" and r.strategyName like ? ");
				params.add("%"+infoVO.getStrategyName()+"%");
			}			
			if(StringUtils.isNoneBlank(infoVO.getSector())){
				String[] sectors=infoVO.getSector().split(",");
				if(sectors!=null && sectors.length>0){
					hql.append(" and ( 1=2 ");
					for (String string : sectors) {
						if("".equals(string))
							continue;
					     hql.append(" or r.sector like ?");
					     params.add("%"+string+"%");
					}
					hql.append("  )");
				}
			}
			if(StringUtils.isNoneBlank(infoVO.getGeoAllocation())){
				String[] regions=infoVO.getGeoAllocation().split(",");
				if(regions!=null && regions.length>0){
					hql.append(" and ( 1=2 ");
					for (String string : regions) {
						if("".equals(string))
							continue;
					     hql.append(" or r.geoAllocation like ?");
					     params.add("%"+string+"%");
					}
					hql.append("  )");
				}
			}
		}
		hql.append(" order by overhead desc,lastUpdate desc ");
		jsonPaging = baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		
		//获取风险评级
		List<SysParamConfig> levels = sysParamService.findParamConfigByType("strategy_risk_rating");
		
		List<StrategyInfoWebVO> list = new ArrayList<StrategyInfoWebVO>();
		Iterator it = jsonPaging.getList().iterator();
		while (it.hasNext()) {
			StrategyInfo info = (StrategyInfo) it.next();
			StrategyInfoWebVO vo = new StrategyInfoWebVO(info);
			vo.setId(info.getId());
			vo.setGeoAllocation(sysParamService.findNameByCode(info.getGeoAllocation(), langCode));
			vo.setSector(sysParamService.findNameByCode(info.getSector(), langCode));
//			String riskName=sysParamService.findNameByValue("strategy_risk_rating", info.getRiskLevel(), langCode);
			vo.setRiskLevel(getLevelNameByValue(levels,info.getRiskLevel(),langCode));
			String isFllow = followStatusService.getWebFollowStatus(info.getId(),infoVO.getCreator(), "strategy");
			vo.setIsFollow(isFllow);
			//设置ifa信息
			try {
				vo.setIfa(memberBaseService.findIfaMember(info.getCreator().getId()));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			/*
			String goe=info.getGeoAllocation();
			vo.setGeoAllocation(sysParamService.findNameByCode(goe, langCode));
			String sector=info.getSector();
			vo.setSector(sysParamService.findNameByCode(sector, langCode));
			vo.setRiskLevel(info.getRiskLevel());*/
			
			//获取图表数据
			List<CharDataVO> allocation=new ArrayList<CharDataVO>();
			List<StrategyAllocationTypeVO> typeList=strategyInfoService.findStrategyAllocationType(info.getId());
			if(null!=typeList){
				Iterator iterator=typeList.iterator();
				while (iterator.hasNext()) {
					StrategyAllocationTypeVO object = (StrategyAllocationTypeVO) iterator.next();
					CharDataVO c=new CharDataVO();
					c.setName(object.getTypeName());
					c.setValue(Double.valueOf(object.getWeight()) );
					allocation.add(c);
				}
			}
			//String str="[{name:'Stock 65%',value:65},{name:'Fund 35%',value:35}]";
			String allocationJson=JsonUtil.toJson(allocation);
			allocationJson.replace("\"", "'");
			vo.setAllocationData(allocationJson);
			
			list.add(vo);

		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	
	/**
	 * 根据风险值获取风险名
	 * @param levels
	 * @param key
	 * @param langCode
	 * @return
	 */
	private String getLevelNameByValue(List<SysParamConfig> levels, String key, String langCode){
		String result = "";
		if (null!=levels && !levels.isEmpty()){
			try {
				for (SysParamConfig rec : levels){
					if ("sc".equalsIgnoreCase(langCode))
						if (rec.getConfValueSc().equals(key)){
							result = rec.getNameSc();
						}
					else if ("tc".equalsIgnoreCase(langCode))
						if (rec.getConfValueTc().equals(key)){
							result = rec.getNameTc();
						}
					else 
						if (rec.getConfValueEn().equals(key)){
							result = rec.getNameEn();
						}
				}
            } catch (Exception e) {
	            // TODO: handle exception
            }
		}
		return result;
	}
	 /**
     * 查询策略的详细信息
     * @author mqzou
     */
	@Override
	public StrategyInfo findStragyById(String id) {
		StrategyInfo info = (StrategyInfo) this.baseDao.get(StrategyInfo.class, id);
		// TODO Auto-generated method stub
		return info;
	}
	/**
	 * 查询IFA创建的投资方案列表
	 * @author qgfeng
	 * @date 2016-12-20
	 */
	@Override
	public JsonPaging findProposalList(JsonPaging jsonPaging,ProposalVO searchVO) {
		String hql = " from CrmProposal r where r.ifaMember.id in ( select ifa.id from MemberIfaIfafirm f where ifafirm.id = ?) ";
		List<Object> params = new ArrayList<Object>();
		params.add(searchVO.getFirmId());
		if (null != searchVO) {
			if(StringUtils.isNotBlank(searchVO.getRiskLevel())){
				hql += " and r.strategy.riskLevel=?";
				params.add(searchVO.getRiskLevel());
			}
			if (StringUtils.isNotBlank(searchVO.getCreatorId())) {
				hql += " and r.ifaMember.id = ?";
				params.add(searchVO.getCreatorId());
			}
			if (StringUtils.isNotBlank(searchVO.getProposalName())) {
				hql += " and r.proposalName like ?";
				params.add("%"+searchVO.getProposalName()+"%" );
			}
			if(StringUtils.isNotBlank(searchVO.getStartDate())){
				hql += (" and r.lastUpdate >= ?");
				params.add(DateUtil.StringToDate(searchVO.getStartDate(),CoreConstants.DATE_FORMAT));
			}
			if(StringUtils.isNotBlank(searchVO.getEndDate())){
				hql += (" and r.lastUpdate <= ?");
				params.add(DateUtil.StringToDate(searchVO.getEndDate(),CoreConstants.DATE_FORMAT));
			}
			if(StringUtils.isNotBlank(searchVO.getStatus())){
				hql += (" and r.status = ?");
				params.add(searchVO.getStatus());
			}
			
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		List<ProposalVO> list = new ArrayList<ProposalVO>();
		Iterator<CrmProposal> it = jsonPaging.getList().iterator();
		while (it.hasNext()) {
			CrmProposal obj = it.next();
			ProposalVO vo = new ProposalVO();
			vo.setId(obj.getId());
			vo.setCreatorId(null != obj.getCreator() ? obj.getCreator().getId() : "");
			vo.setCreatorName(null != obj.getCreator() ? obj.getCreator().getNickName() : "");
			vo.setCurStep(obj.getCurStep());
			vo.setCurrencyType(obj.getBaseCurrId());
			//String customerBase = null != obj.getMember() ? obj.getMember().getId() : "";
			
			CrmCustomer crmCustomer = crmCustomerService.findByIfaAndMember(obj.getIfaMember().getId(),obj.getMember().getId());
			if(crmCustomer != null){
				vo.setCustomerId(crmCustomer.getId());
			}
			
			vo.setCustomerName(null != obj.getMember() ? obj.getMember().getNickName() : "");
			vo.setEmailLogId(null != obj.getEmailLog() ? obj.getEmailLog().getId() : "");
			vo.setInitialInvestAmount(obj.getInitialInvestAmount()==null?0:obj.getInitialInvestAmount());
			vo.setLiquidityNeed(obj.getLiquidityNeed());
			vo.setLrf(obj.getLrf());
			vo.setProposalName(obj.getProposalName());
			vo.setRemark(obj.getRemark());
			vo.setStatus(obj.getStatus());
			vo.setStrategyId(null != obj.getStrategy() ? obj.getStrategy().getId() : "");
			vo.setStrategyName(null != obj.getStrategy() ? obj.getStrategy().getStrategyName() : "");
			vo.setTaxConcerns(obj.getTaxConcerns());
			vo.setTimeFrame(obj.getTimeFrame());
			vo.setUnp(obj.getUnp());
			vo.setCreateTime(obj.getCreateTime());
			vo.setLastUpdate(obj.getLastUpdate()!=null?DateUtil.getDateStr(obj.getLastUpdate(),CoreConstants.DATE_TIME_FORMAT):"");
			list.add(vo);
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}

	@Override
	public JsonPaging findPofolioList(JsonPaging jsonPaging, CriteriaVO criteria,String langCode) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("  ");
		hql.append("select distinct a from PortfolioArena a left join PortfolioArenaReturn r on a.id=r.portfolio.id");
		//hql.append(" where a.creator.id=? ");
		hql.append(" where a.creator.id in ( ");
		hql.append(" select c.member.id from MemberIfaIfafirm b left join MemberIfa c on b.ifa.id=c.id where b.ifafirm.id=? ");
		hql.append(" ) and a.isPublic='1' ");
		params.add(criteria.getFirm().getId());
		//params.add(StrUtils.getString(criteria.getUserId(),"-NONE-"));
//		hql.append(" where 1=1 ");//@@测试，暂时屏蔽条件
		if(null != criteria.getKeyword() && !"".equals(criteria.getKeyword())){
			hql.append(" and ( a.portfolioName like ? )");
			params.add("%"+criteria.getKeyword()+"%");
		}
		if(null != criteria.getSector() && !"".equals(criteria.getSector())){
			hql.append(" and ( 1=2 ");
			String[] strings=criteria.getSector().split(",");
			for (String string : strings) {
				if("".equals(string))
					continue;
				hql.append(" or a.sector like ? ");
				params.add("%"+string+"%");
			}
			hql.append(")");
		}
		if(null != criteria.getGeoAllocation() && !"".equals(criteria.getGeoAllocation())){//可多选，用逗号分隔
			String[] idArrStrings = criteria.getGeoAllocation().split(",");
			hql.append(" and ( " );
			if (idArrStrings!=null && idArrStrings.length>0){
            	for(int k=0;k<idArrStrings.length;k++){
            		String cd = idArrStrings[k].toString();
            		if (!StrUtils.isEmpty(cd)){
	            		hql.append("a.geoAllocation like '%"+ cd +"%'");
	            		if (k<idArrStrings.length-1)  hql.append(" or ");
            		}
            	}
			}
			hql.append(" )");
		}
		
		if(null != criteria.getRiskLevel() && !"".equals(criteria.getRiskLevel())){
			hql.append(" and a.riskLevel=? ");
			params.add(StrUtils.getInteger(criteria.getRiskLevel()));
		}
		
		if(null != criteria.getTotalReturn() && !"".equals(criteria.getTotalReturn())){
			if ("0".equals(criteria.getTotalReturn())){
				hql.append(" and ( a.totalReturn < 0 )");
			}else if ("1".equals(criteria.getTotalReturn())){
				hql.append(" and ( a.totalReturn >= 0 and a.totalReturn < 20 )");
			}else if ("2".equals(criteria.getTotalReturn())){
				hql.append(" and ( a.totalReturn >= 20 and a.totalReturn < 40 )");
			}else if ("3".equals(criteria.getTotalReturn())){
				hql.append(" and ( a.totalReturn >= 40 and a.totalReturn < 60 )");
			}else if ("4".equals(criteria.getTotalReturn())){
				hql.append(" and ( a.totalReturn >= 40 and a.totalReturn < 80 )");
			}else if ("5".equals(criteria.getTotalReturn())){
				hql.append(" and ( a.totalReturn >= 40 and a.totalReturn < 100 )");
			}else if ("6".equals(criteria.getTotalReturn())){
				hql.append(" and ( a.totalReturn >=100 )");
			}
		}
		
		if(null != criteria.getDataFrom() && !"".equals(criteria.getDataFrom())){
			hql.append(" and a.totalReturn >=? ");
			params.add(StrUtils.getDouble(criteria.getDataFrom()));
		}
		if(null != criteria.getDataTo() && !"".equals(criteria.getDataTo())){
			hql.append(" and a.totalReturn <? ");
			params.add(StrUtils.getDouble(criteria.getDataTo()));
		}
		
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List<PortfolioArenaVO> result = new ArrayList<PortfolioArenaVO>();
		List<PortfolioArena> list = (List<PortfolioArena>)jsonPaging.getList();
		if (null!=list && !list.isEmpty()){
			for (PortfolioArena info: list){
				PortfolioArenaVO infoVo = new PortfolioArenaVO(info);
				infoVo.setCreator(info.getCreator());
				//设置ifa信息
				try {
					infoVo.setIfaMember(info.getCreator());
					infoVo.setIfa(memberBaseService.findIfaMember(info.getCreator().getId()));
				} catch (Exception e) {
					// TODO: handle exception
//					e.printStackTrace();
				}
				infoVo.setTotalReturn(null!=info.getTotalReturn()?info.getTotalReturn()*100:0);
				//获取统计数据
				if (criteria.isCheckCount()){
					PortfolioArenaCount count = findPortfolioArenaCountById(info.getId());
					if (null!=count && null!=count.getViews()) infoVo.setClick(count.getViews());
				}
				if (criteria.isCheckProduct()){
					List<ProductInfo> prods = portfolioArenaService.findProductInfoList(info.getId(), CommonConstantsWeb.WEB_PRODUCT_TYPE_FUND);
					if (null==prods || prods.isEmpty()) infoVo.setProductCount(0);
					else infoVo.setProductCount(prods.size());
				}
				if(criteria.isCheckIsMyFollow()){
					String isFollow = followStatusService.getWebFollowStatus(info.getId(), criteria.getUserId(), "portfolio_arena");
					infoVo.setIsFollow(isFollow);
				}
				String goe=info.getGeoAllocation();
				infoVo.setGeoAllocation(sysParamService.findNameByCode(goe, langCode));
				String sector=info.getSector();
				infoVo.setSector(sysParamService.findNameByCode(sector, langCode));
				infoVo.setMonReturn3(findPortfolioReturn(infoVo.getId(), "fund_return_period_code_3M")*100);
				infoVo.setRiskLevel(info.getRiskLevel());
				
//				MemberBase member=memberBaseService.findById(criteria.getUserId());
				String displayColor = CommonConstants.DEF_DISPLAY_COLOR;
				if(null!=criteria.getCurrUser() || !StrUtils.isEmpty(criteria.getCurrUser().getDefDisplayColor()))
					displayColor = criteria.getCurrUser().getDefDisplayColor();
				infoVo.setChartUrl(getPerformanceChartImage(info.getId(), CommonConstantsWeb.CHART_IMAGE_SIZE_MIDDL, displayColor));

				result.add(infoVo);
			}
		}
		jsonPaging.setList(result);		
		return jsonPaging;
	}
	
	@Override
	public JsonPaging findInsightList(JsonPaging jsonPaging,CriteriaVO criter, String langCode) {
		List<Object> params = new ArrayList<Object>();
//		StringBuilder hql = new StringBuilder("from InsightInfo r where r.creator.id=? ");
//		params.add(criter.getUserId());
//ifa firm 可查看旗下所有ifa发布的观点
		StringBuffer hql = new StringBuffer("from InsightInfo r where r.creator.id in ( ");
		hql.append("select a.member.id from MemberIfa a left join MemberIfaIfafirm b on a.id=b.ifa.id where  b.ifafirm.id=? ");
		hql.append(") ");
		params.add(criter.getFirm().getId());
		if(StringUtils.isNotBlank(criter.getKeyword())){
			hql.append(" and ( r.title like ? or r.content like ?)");
			params.add("%"+criter.getKeyword()+"%");
			params.add("%"+criter.getKeyword()+"%");
		}
		if(null != criter.getSector() && !"".equals(criter.getSector())){
			hql.append(" and ( 1=2 ");
			String[] sectors=criter.getSector().split(",");
			if(sectors != null && sectors.length>0){
				for (String sector : sectors) {
					if(StringUtils.isBlank(sector))continue;
					hql.append(" or r.sector like ? ");
					params.add("%"+sector+"%");
				}
			}
			
			hql.append(")");
		}
		if(null != criter.getGeoAllocation() && !"".equals(criter.getGeoAllocation())){//可多选，用逗号分隔
			String[] geoAllocations = criter.getGeoAllocation().split(",");
			hql.append(" and ( 1=2 ");
			if (geoAllocations!=null && geoAllocations.length>0){
            	for(String geoAllocation : geoAllocations){
            		if(StringUtils.isBlank(geoAllocation))continue;
            		hql.append(" or r.geoAllocation like ?");
            		params.add("%"+geoAllocation+"%");
            	}
			}
			hql.append(" )");
		}
		if(StringUtils.isNotBlank(criter.getStartDate())){
			hql.append(" and r.lastUpdate >= ?");
			params.add(DateUtil.StringToDate(criter.getStartDate(),CoreConstants.DATE_FORMAT));
		}
		if(StringUtils.isNotBlank(criter.getEndDate())){
			hql.append(" and r.lastUpdate <= ?");
			params.add(DateUtil.StringToDate(criter.getEndDate(),CoreConstants.DATE_FORMAT));
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		Iterator<InsightInfo> iterator = jsonPaging.getList().iterator();
		List<InsightInfoVo> list = new ArrayList<InsightInfoVo>();
		while(iterator.hasNext()){
			InsightInfo tempInfo = iterator.next();
			InsightInfoVo newVo = new InsightInfoVo();
			newVo.setId(tempInfo.getId());
			newVo.setTitle(tempInfo.getTitle());
			newVo.setContent(tempInfo.getContent());
			newVo.setCreateTime(DateUtil.getDateStr(tempInfo.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
			newVo.setLastUpdate(DateUtil.getDateStr(tempInfo.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
			newVo.setViews(tempInfo.getClick()==null?0:tempInfo.getClick());
			newVo.setUpCounter(tempInfo.getUpCounter()==null?0:tempInfo.getUpCounter());
			newVo.setDownCounter(tempInfo.getDownCounter()==null?0:tempInfo.getDownCounter());
			newVo.setThumbnail(tempInfo.getThumbnail());
			String subHql = " select count(r.id) from WebRecommended r where r.moduleType='insight' and r.relateId='"+tempInfo.getId()+"'";
			Number commenCount = (Number) baseDao.getUniqueResult(subHql, null, false);
			newVo.setComments(commenCount==null?0:commenCount.intValue());
			list.add(newVo);
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	
	

	@Override
	public List getIfaInIfafirm(String ifafirmId) {
		String hql = " select r.member.id from MemberIfa r where r.ifafirm='" + ifafirmId + "'  and r.member.isValid='1'";
		List list = this.baseDao.find(hql, null, false);
		return list;
	}
	
	
	//-------------组件-----
	
	/**
	 * 获取投资组合的收益
	 * @author mqzou 2016-12-13
	 * @param portfolioId
	 * @param periodCode
	 * @return
	 */
	private double findPortfolioReturn(String portfolioId,String periodCode){
		String hql=" from PortfolioArenaReturn r where r.id=? and r.periodCode=? ";
		List<Object> params=new ArrayList<Object>();
		params.add(portfolioId);
		params.add(periodCode);
		List<PortfolioArenaReturn> resultList=this.baseDao.find(hql, params.toArray(), false);
		if(null!=resultList && !resultList.isEmpty()){
			PortfolioArenaReturn ret=(PortfolioArenaReturn)resultList.get(0);
			return ret.getIncrease();
		}else {
			return 0;
		}
	}
	
	/**
	 * 通过ID获取内容信息
	 * @author michael
	 */
	public PortfolioArenaCount findPortfolioArenaCountById(String id) {
		PortfolioArenaCount obj = (PortfolioArenaCount) this.baseDao.get(PortfolioArenaCount.class, id);
		return obj;
	}
	
	/**
	 * IFA审批 （修改状态）
	 * @author qgfeng
	 * @date 2016-12-15
	 */
	@Override
	public boolean saveApprovalStatus(IfaVO ifaVO) {
		if(ifaVO == null){
			return false;
		}
		MemberIfaIfafirm ifaFrim = getIfaIfaFirmByIFrimId(ifaVO.getId(), ifaVO.getIfafirmId());
		if(ifaFrim != null){
			ifaFrim.setCheckStatus(ifaVO.getCheckStatus());
			ifaFrim.setLastUpdate(new Date());
			if(ifaFrim.getCheckDate() == null){
				ifaFrim.setCheckDate(new Date());
			}
			if ("1".equals(ifaVO.getCheckStatus())){
				ifaFrim.setBeginDate(new Date());
				ifaFrim.setEndDate(null);
			}else if ("2".equals(ifaVO.getCheckStatus())){
				ifaFrim.setEndDate(new Date());
			}
			
			this.baseDao.update(ifaFrim);
			return true;
		}
		return false;
	}
	
	/**
     * 查询ae账户（IfaDistributor）
     * @author qgfeng
     * @date 2017-1-18
     */
	@Override
	public JsonPaging findIfaDisList(JsonPaging jsonPaging,IfaDistributorVO searchVo) {
		String hql =" from IfaDistributor r where r.ifa.id = ?";
		List<String> parms = new ArrayList<String>();
		parms.add(searchVo.getIfaId());
		if (StringUtils.isNotBlank(searchVo.getKeyword())) {
			hql += " and (r.ifa.member.nickName like ? or r.ifa.member.loginCode like ? or r.aeCode like ?)";
			parms.add("%"+searchVo.getKeyword()+"%");
			parms.add("%"+searchVo.getKeyword()+"%");
			parms.add("%"+searchVo.getKeyword()+"%");
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql, parms.toArray(), jsonPaging, false);
		List<IfaDistributorVO> list = new ArrayList<IfaDistributorVO>();
		Iterator<IfaDistributor> it = jsonPaging.getList().iterator();
		while (it.hasNext()) {
			IfaDistributor info = it.next();
			IfaDistributorVO vo = new IfaDistributorVO();
			if(info.getIfa() != null && info.getIfa().getMember() !=null){
				vo.setIfaId(info.getIfa().getId());
				vo.setLoginCode(info.getIfa().getMember().getLoginCode());
				vo.setNickName(info.getIfa().getMember().getNickName());
			}
			if(info.getDistributor() != null){
				vo.setDistributorId(info.getDistributor().getId());
				vo.setCompanyName(info.getDistributor().getCompanyName());
			}
			vo.setId(info.getId());
			vo.setIsValid(info.getIsValid());
			vo.setAeCode(info.getAeCode());
			vo.setCreateTime(DateUtil.getDateStr(info.getCreateTime()));
			vo.setLastUpdate(DateUtil.getDateStr(info.getLastUpdate()));
			list.add(vo);
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	 /**
     * 根据ifaID、frimID查找MemberIfaIfafirm
     * @author qgfeng
     * @date 2016-12-15
     */
	@Override
	public MemberIfaIfafirm getIfaIfaFirmByIFrimId(String ifaId, String firmId) {
		String hql = " from MemberIfaIfafirm r where r.isValid='1' and r.ifa.id=? and r.ifafirm.id=?";
		List<String> parms = new ArrayList<String>();
		parms.add(ifaId);
		parms.add(firmId);
		List<MemberIfaIfafirm> list = this.baseDao.find(hql, parms.toArray(), false);
		if(list != null && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	
	 /**
     * 根据ifaID、frimID查找MemberIfaIfafirm
     * @author michael
     * @date 2017-3-28
     * @param ifaId
     * @param checkStatus  
     */
	public MemberIfaIfafirm findIfaIfaFirmByIfaId(String ifaId, String checkStatus) {
		if (!StringUtils.isNoneBlank(ifaId)) return null;
		
		String hql = " from MemberIfaIfafirm r where r.isValid='1' and r.ifa.id=? ";
		List<String> parms = new ArrayList<String>();
		parms.add(ifaId);
		
		hql += "and (current_date()<=r.endDate or r.endDate is null) ";
		if(StringUtils.isNoneBlank(checkStatus)){
			hql+=" and r.checkStatus = ? ";
			parms.add(checkStatus);
		}
		hql+=" order by r.lastUpdate desc ";
		
		List<MemberIfaIfafirm> list = this.baseDao.find(hql, parms.toArray(), false);
		if(list != null && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	
	 /**
     * 保存IFA-IFAFirm关联关系
     * @author qgfeng
     * @date 2016-12-16
     */
	@Override
	public MemberIfaIfafirm saveOrUpdateIfaIfafirm(MemberIfaIfafirm releInfo) {
		if(releInfo == null){
			return null;
		}
		releInfo = (MemberIfaIfafirm) baseDao.saveOrUpdate(releInfo);
		return null;
	}
	/**
     * 获取ifa和ifafirm的关系
     * @author mqzou 2016-12-30
     * @param ifaId
     * @return
     */
	@Override
	public MemberIfaIfafirm getIfaAndIfafirm(String ifaId) {
	    StringBuilder hql=new StringBuilder();
	    hql.append(" from MemberIfaIfafirm r where r.checkStatus='1' and r.isValid='1' and r.ifa.id=?");
	    List<Object> params=new ArrayList<Object>();
	    params.add(ifaId);
	    List list=this.baseDao.find(hql.toString(), params.toArray(), false);
	    if(null!=list && !list.isEmpty()){
	    	MemberIfaIfafirm ifaIfafirm=(MemberIfaIfafirm)list.get(0);
	    	return ifaIfafirm;
	    }
		return null;
	}

	/*************************************************************************************/
	/**
	 * 获取IFA Team列表
	 * @author 林文伟
	  * @param jsonPaging 分页参数
	 * @param filters 过滤条件
	 * @return
	 */
	@Override
	public JsonPaging loadIFAMyTeamList(JsonPaging jsonPaging,String ifaId,String keyWords) {
		//get the d.isSupervisor=1 teamid
		Boolean isHaveSuperVisorData = false;
		List<IfafirmTeam> teamList = new ArrayList<IfafirmTeam>();
		List<IfafirmTeamIfa> adminTeamList =  this.getTeamByIfaId(ifaId,"1");
		if(null!=adminTeamList&&!adminTeamList.isEmpty()){
			isHaveSuperVisorData = true;
			for(IfafirmTeamIfa each :adminTeamList){
				teamList.add(each.getTeam());
				teamList.addAll(this.getTeamByTeamId(each.getTeam().getId()));
			}
		}
		String teamIdList = "";
		for(IfafirmTeam each : teamList){
			if(null!=each){
				teamIdList += "'" + each.getId() + "',";
			}
		}
		// if not have data 
		if("".equals(teamIdList)){
			isHaveSuperVisorData = false;
			List<IfafirmTeamIfa> myTeamList =  this.getTeamByIfaId(ifaId,"0");
			if(null!=myTeamList&&!myTeamList.isEmpty()){
				for(IfafirmTeamIfa each : myTeamList){
					teamIdList += "'" + each.getTeam().getId() + "',";
				}
			}
		}
		//trim ,
		
			if(teamIdList.length()>0 && teamIdList.endsWith(",")){
				teamIdList = teamIdList.substring(0, teamIdList.length()-1);
			}
		
		
 		String hql = "SELECT b.nameChn,b.telephone,c.nickName,b.firstName,b.lastName,c.email,c.loginCode,f.name,b.id,c.id,f.id " +
 				"FROM IfafirmTeamIfa a, MemberIfa b,MemberBase c,IfafirmTeam f " +
 				"WHERE f.id=a.team.id and c.id=b.member.id and a.ifa.id=b.id " ;
 		if(isHaveSuperVisorData){
 			hql += "AND a.team.id IN( "+teamIdList+") ";
 		} else{
 			hql += "AND a.team.id IN( "+teamIdList+") AND b.id='"+ifaId+"'";
 		}
 		
 		//当它不属于任何一个团队时只显示自己的信息
 		boolean isBelongTeam = true;
 		if("".equals(teamIdList)){
 			isBelongTeam = false;
 			hql = "SELECT b.nameChn,b.telephone,c.nickName,b.firstName,b.lastName,c.email,c.loginCode,b.id,c.id " +
				"FROM  MemberIfa b,MemberBase c  " +
				"WHERE c.id=b.member.id and b.id = '"+ifaId+"' " ;
 		}
		
		//关键字
		if(!"".equals(keyWords))
		{
			String conditionSql = " and ( b.firstName like '%"+keyWords+"%' or b.lastName like '%"+keyWords+"%' or c.nickName like '%"+keyWords+"%'  ) ";
			hql+= conditionSql;
		}
		
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), null, jsonPaging , true);
		List voList = jsonPaging.getList();////每个列表都是一个2维数组，每个数组有2个实体
		List<IfaMyTeamListVO> list = new ArrayList<IfaMyTeamListVO>();
		if(voList!=null && !voList.isEmpty()){
			for(int x=0;x<voList.size();x++){
				IfaMyTeamListVO vo = getIfaTeamListResults(voList, x,isBelongTeam);
				list.add(vo);
			}
		}

		jsonPaging.setList(list);
		return jsonPaging;
	}

	//获取ifa展示信息
	public IfaMyTeamListVO getIfaTeamListResults( List voList, int x,boolean isBelongTeam) {
		IfaMyTeamListVO vo = new IfaMyTeamListVO();
		 Object[] objs = (Object[])voList.get(x);
		 if(isBelongTeam==true){
			 String nameChn = objs[0]==null?"":objs[0].toString();
			 String telephone = objs[1]==null?"":objs[1].toString();
			 String nickName = objs[2]==null?"":objs[2].toString();
			 String firstName = objs[3]==null?"":objs[3].toString();
			 String lastName = objs[4]==null?"":objs[4].toString();
			 String email = objs[5]==null?"":objs[5].toString();
			 String loginCode = objs[6]==null?"":objs[6].toString();
			 String teamName = objs[7]==null?"":objs[7].toString();
			 String ifaId = objs[8]==null?"":objs[8].toString();
			 String memberId = objs[9]==null?"":objs[9].toString();
			 String teamId = objs[10]==null?"":objs[10].toString();
			 
			 vo.setEmail(email);
			 vo.setEnglistName(firstName+" "+lastName);
			 vo.setId(ifaId);
			 vo.setLoginId(loginCode);
			 vo.setMemberId(memberId);
			 vo.setName(nameChn);
			 vo.setNickname(nickName);
			 vo.setPhoneNo(telephone);
			 vo.setTeamId(teamId);
			 vo.setTeamName(teamName);
		 }
		 else{
			 String nameChn = objs[0]==null?"":objs[0].toString();
			 String telephone = objs[1]==null?"":objs[1].toString();
			 String nickName = objs[2]==null?"":objs[2].toString();
			 String firstName = objs[3]==null?"":objs[3].toString();
			 String lastName = objs[4]==null?"":objs[4].toString();
			 String email = objs[5]==null?"":objs[5].toString();
			 String loginCode = objs[6]==null?"":objs[6].toString();
			 String teamName = "";
			 String ifaId = objs[7]==null?"":objs[7].toString();
			 String memberId = objs[8]==null?"":objs[8].toString();
			 String teamId = "";
			 
			 vo.setEmail(email);
			 vo.setEnglistName(firstName+" "+lastName);
			 vo.setId(ifaId);
			 vo.setLoginId(loginCode);
			 vo.setMemberId(memberId);
			 vo.setName(nameChn);
			 vo.setNickname(nickName);
			 vo.setPhoneNo(telephone);
			 vo.setTeamId(teamId);
			 vo.setTeamName(teamName);
		 }

		 
		
		return vo;
	}
	
	/**
     * get ifa admin teamid
     * @author wwlin
     * @date 2016-12-15
     */
	public List<IfafirmTeamIfa> getTeamByIfaId(String ifaId,String isSupervisor) {
		String hql = " from IfafirmTeamIfa r where r.ifa.id=? and r.isSupervisor=?";
		List<String> parms = new ArrayList<String>();
		parms.add(ifaId);
		parms.add(isSupervisor);
		List<IfafirmTeamIfa> list = this.baseDao.find(hql, parms.toArray(), false);
		
		return list;
	}
	
	public List<IfafirmTeam> getTeamByTeamId(String temaId) {
		List<IfafirmTeam> teamList = new ArrayList<IfafirmTeam>();
		String hql = " from IfafirmTeam r where r.parent.id=?";
		List<String> parms = new ArrayList<String>();
		parms.add(temaId);
		List<IfafirmTeam> list = this.baseDao.find(hql, parms.toArray(), false);
		if(null!=list&&!list.isEmpty()){
			teamList.addAll(list);
			for(int i=0;i<list.size();i++){
				List<IfafirmTeam> temp =  getTeamByTeamId(list.get(i).getId());
				if(null!=temp&&!temp.isEmpty())teamList.addAll(temp);
			}
		}
		return teamList;
	}

	/**
     * 查找IfaDistributor（AE账户）
     * @param id
     * @return
     */
	public IfaDistributor getIfaDisById(String id) {
		if(StringUtils.isBlank(id)){
			return null;
		}
		IfaDistributor info = (IfaDistributor) baseDao.get(IfaDistributor.class, id);
		return info;
	}

	/**
	 * 保存IfaDistributor（AE账户）
	 * @param ifaDistributor
	 * @return
	 */
	public IfaDistributor saveIfaDis(IfaDistributor ifaDistributor) {
		ifaDistributor = (IfaDistributor) baseDao.saveOrUpdate(ifaDistributor);
		return ifaDistributor;
	}

	/**
	 * 删除IfaDistributor（AE账户）
	 * @param id
	 * @return
	 */
	public boolean deleteIfaDis(String id) {
		IfaDistributor dis = getIfaDisById(id);
		if(dis != null){
			baseDao.delete(dis);
			return true;
		}
		return false;
	}
	
	/***
	 * 校验targetMemberId是否memberId其supervisor
	 * @author wwluo
	 * @date 2017-03-14
	 * @param targetMemberId
	 * @param memberId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean varifySupervisor(String targetMemberId, String memberId) {
		boolean flag = false;
		if (StringUtils.isNotBlank(targetMemberId) && StringUtils.isNotBlank(memberId)) {
			StringBuffer hql = new StringBuffer("" +
					" SELECT" +
					" b.id" +
					" FROM" +
					" IfafirmTeamIfa t,IfafirmTeam d" +
					" LEFT JOIN" +
					" MemberIfa m" +
					" ON" +
					" m.id=t.ifa.id" +
					" LEFT JOIN" +
					" MemberBase b" +
					" ON" +
					" b.id=m.member.id" +
					" WHERE" +
					" t.isSupervisor=1" +
					" AND" +
					" d.id=t.team.id" +
					" AND" +
					" d.id" +
					" IN" +
					" (SELECT" +
					" f.team.id" +
					" FROM" +
					" IfafirmTeamIfa f" +
					" LEFT JOIN" +
					" MemberIfa i" +
					" ON" +
					" i.id=f.ifa.id" +
					" WHERE" +
					" i.member.id=?" +
					" )" +
					"");
			List<Object> params = new ArrayList<Object>();
			params.add(memberId);
			List<String> ids = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(ids != null && !ids.isEmpty()){
				for (String id : ids) {
					if(targetMemberId.equals(id)){
						flag = true;
						break;
					}
				}
			}
		}
		return flag;
	}
	
	 /**
     * 根据ifaID 更新MemberIfaIfafirm记录为无效状态
     * @author michael
     * @date 2016-12-15
     */
	public boolean updateIfaIfaFirmToInvalid(String ifaId) {
		if(StringUtils.isBlank(ifaId) ){
			return false;
		}
		String hql = " update MemberIfaIfafirm r set isValid='0',lastUpdate=? where r.ifa.id=?";
		List<Object> parms = new ArrayList<Object>();
		parms.add(new Date());
		parms.add(ifaId);
		int c = this.baseDao.updateHql(hql, parms.toArray());
		if (c<=0) return false;
		return true;
	}
	
	/**
	 * 获取ifa推荐的策略列表
	 * @author mqzou 
	 * @param jsonPaging
	 * @param keyword
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	@Override
	public JsonPaging findIfaFirmRecommendStrategy(JsonPaging jsonPaging, String ifafirmId, String keyword, String beginDate, String endDate,String langCode) {
		StringBuilder hql=new StringBuilder();
		hql.append(" select r from StrategyInfo r ");
		hql.append(" left join MemberBase m on r.creator.id=m.id");
		hql.append(" left join MemberIfa i on m.id=i.member.id where 1=1");
		hql.append(" and i.id in (select f.ifa.id from MemberIfaIfafirm f where f.ifafirm.id=?)");
		hql.append(" and r.isValid='1' and r.status='1'");
		List<Object> params=new ArrayList<Object>();
		params.add(ifafirmId);
		if(null!=keyword && !"".equals(keyword)){
			hql.append(" and (r.strategyName like ? or concat(i.firstName,i.lastName) like ?  or i.nameChn like ?)");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
			
		}
		if(null!=beginDate && !"".equals(beginDate) && null!=endDate && !"".equals(endDate) ){
			hql.append(" and r.createTime between ? and ?");
			params.add(DateUtil.StringToDate(beginDate, DateUtil.DEFAULT_DATE_FORMAT));
			params.add(DateUtil.StringToDate(endDate, DateUtil.DEFAULT_DATE_FORMAT));
		}
		else if(null!=beginDate && !"".equals(beginDate)){
			hql.append(" and r.createTime >= ?");
			params.add(DateUtil.StringToDate(beginDate, DateUtil.DEFAULT_DATE_FORMAT));
		}
		else if(null!=endDate && !"".equals(endDate)){
			hql.append(" and r.createTime <=?");
			params.add(DateUtil.StringToDate(endDate, DateUtil.DEFAULT_DATE_FORMAT));
		}
		jsonPaging=baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List<StrategyInfoWebVO> list=new ArrayList<StrategyInfoWebVO>();
		if(null!=jsonPaging && null!=jsonPaging.getList()){
			Iterator it=jsonPaging.getList().iterator();
			while (it.hasNext()) {
				StrategyInfo info = (StrategyInfo) it.next();
				StrategyInfoWebVO vo=new StrategyInfoWebVO(info);
				String ifaName=getCommonMemberName(info.getCreator().getId(), langCode, "2");
				vo.setCreatorName(ifaName);
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	@Override
	public JsonPaging findIfaFirmRecommendPortfolio(JsonPaging jsonPaging, String ifafirmId, String keyword, String riskLevel, String totalReturn, String beginDate, String endDate, String langCode) {
		StringBuilder hql=new StringBuilder();
		hql.append(" select r from PortfolioArena r ");
		hql.append(" left join MemberBase m on r.creator.id=m.id");
		hql.append(" left join MemberIfa i on m.id=i.member.id where 1=1");
		hql.append(" and i.id in (select f.ifa.id from MemberIfaIfafirm f where f.ifafirm.id=?)");
		hql.append(" and r.isValid='1' and r.status='1'");
		List<Object> params=new ArrayList<Object>();
		params.add(ifafirmId);
		if(null!=keyword && !"".equals(keyword)){
			hql.append(" and (r.portfolioName like ? or concat(i.firstName,i.lastName) like ?  or i.nameChn like ?)");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
			
		}
		if(null!=beginDate && !"".equals(beginDate) && null!=endDate && !"".equals(endDate) ){
			hql.append(" and r.createTime between ? and ?");
			params.add(DateUtil.StringToDate(beginDate, DateUtil.DEFAULT_DATE_FORMAT));
			params.add(DateUtil.StringToDate(endDate, DateUtil.DEFAULT_DATE_FORMAT));
		}
		else if(null!=beginDate && !"".equals(beginDate)){
			hql.append(" and r.createTime >= ?");
			params.add(DateUtil.StringToDate(beginDate, DateUtil.DEFAULT_DATE_FORMAT));
		}
		else if(null!=endDate && !"".equals(endDate)){
			hql.append(" and r.createTime <=?");
			params.add(DateUtil.StringToDate(endDate, DateUtil.DEFAULT_DATE_FORMAT));
		}
		
		if(null!=riskLevel && !"".equals(riskLevel)){
			hql.append(" and r.riskLevel=?");
			params.add(Integer.parseInt(riskLevel));
		}
		if(null!=totalReturn && !"".equals(totalReturn)){
			//hql.append(" and totalReturn");
		}
		jsonPaging=baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List<PortfolioArenaVO> list=new ArrayList<PortfolioArenaVO>();
		if(null!=jsonPaging && null!=jsonPaging.getList()){
			Iterator it=jsonPaging.getList().iterator();
			while (it.hasNext()) {
				PortfolioArena info = (PortfolioArena) it.next();
				PortfolioArenaVO vo=new PortfolioArenaVO(info);
				String ifaName=getCommonMemberName(info.getCreator().getId(), langCode, "2");
				vo.setCreatorName(ifaName);
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	
	 /**
     * IFA创建的投资方案列表
     * @author wwluo
     * @date 2016-08-25
     * @param memberAdmin 当前管理员
     * @param jsonPaging
     * @param proposalVO 
     * @param langCode
     * @return JsonPaging
     */
	@SuppressWarnings("unchecked")
	@Override
	public JsonPaging getProposals(MemberAdmin memberAdmin,
			JsonPaging jsonPaging, IfaProposalVO ifaProposalVO, String langCode) {
		String dateFormat = memberAdmin.getMember().getDateFormat();
		if (StringUtils.isBlank(dateFormat)) {
			dateFormat = CommonConstants.FORMAT_DATE_TIME;
		}
		if ("1".equals(memberAdmin.getType()) && memberAdmin.getIfafirm() != null) {
			StringBuffer hql = new StringBuffer(""
					+ " SELECT"
					+ " c"
					+ " FROM"
					+ " CrmProposal c"
					+ " LEFT JOIN"
					+ " MemberIfaIfafirm mii"
					+ " ON"
					+ " c.ifaMember.id=mii.ifa.id"
					+ " AND"
					+ " mii.isValid=1"
					+ " AND"
					+ " mii.checkStatus=1"
					+ " LEFT JOIN"
					+ " MemberIfa i"
					+ " ON"
					+ " c.ifaMember.id=i.id"
					+ " LEFT JOIN"
					+ " MemberBase m"
					+ " ON"
					+ " m.id=c.member.id"
					+ " OR"
					+ " m.id=i.member.id"
					+ " AND"
					+ " m.isValid=1"
					+ " LEFT JOIN"
					+ " MemberIndividual mi"
					+ " ON"
					+ " mi.member.id=c.member.id"
					+ " WHERE"
					+ " c.isValid=1"
					+ " AND"
					+ " c.status<>0"
					+ "");
			List<Object> params = new ArrayList<Object>();
			if (StringUtils.isNotBlank(ifaProposalVO.getFilterPeriod()) 
					&& StringUtils.isNotBlank(ifaProposalVO.getFilterPeriodType())) {
				Calendar calendar = Calendar.getInstance();
				String periodType = ifaProposalVO.getFilterPeriodType();
				if("D".equals(periodType)){
					calendar.add(Calendar.DATE, Integer.valueOf(ifaProposalVO.getFilterPeriod()));
				}else if("M".equals(periodType)){
					calendar.add(Calendar.MONTH, Integer.valueOf(ifaProposalVO.getFilterPeriod()));
				}else if("Y".equals(periodType)){
					calendar.add(Calendar.YEAR, Integer.valueOf(ifaProposalVO.getFilterPeriod()));
				}
				hql.append(" AND c.createTime BETWEEN ? AND CURDATE()");
				params.add(calendar.getTime());
			}else if(StringUtils.isNotBlank(ifaProposalVO.getFilterStartTime()) 
					&& StringUtils.isNotBlank(ifaProposalVO.getFilterEndTime())){
				hql.append(" AND c.createTime BETWEEN ? AND ?");
				params.add(DateUtil.StringToDate(ifaProposalVO.getFilterStartTime(), CommonConstants.FORMAT_DATE));
				params.add(DateUtil.StringToDate(ifaProposalVO.getFilterEndTime(), CommonConstants.FORMAT_DATE));
			}
			if (StringUtils.isNotBlank(ifaProposalVO.getFilterStatus())) {
				hql.append(" AND c.status=?");
				params.add(ifaProposalVO.getFilterStatus());
			}
			if (StringUtils.isNotBlank(ifaProposalVO.getFilterKeyWord())) {
			// 客户名称，方案名称，IFA名称，模糊搜索
				hql.append(" AND("
						+ "	TRIM(i.firstName)||' '||TRIM(i.lastName) LIKE ? OR"
						+ "	i.firstName LIKE ? OR"
						+ "	i.lastName LIKE ? OR"
						+ "	i.nameChn LIKE ? OR"
						+ "	TRIM(mi.firstName)||' '||TRIM(mi.lastName) LIKE ? OR"
						+ "	mi.firstName LIKE ? OR"
						+ "	mi.lastName LIKE ? OR"
						+ "	mi.nameChn LIKE ? OR"
						+ "	m.nickName LIKE ? OR"
						+ "	m.loginCode LIKE ? OR"
						+ "	c.proposalName LIKE ?"
						+ ")");
				params.add("%" + ifaProposalVO.getFilterKeyWord() + "%");
				params.add("%" + ifaProposalVO.getFilterKeyWord() + "%");
				params.add("%" + ifaProposalVO.getFilterKeyWord() + "%");
				params.add("%" + ifaProposalVO.getFilterKeyWord() + "%");
				params.add("%" + ifaProposalVO.getFilterKeyWord() + "%");
				params.add("%" + ifaProposalVO.getFilterKeyWord() + "%");
				params.add("%" + ifaProposalVO.getFilterKeyWord() + "%");
				params.add("%" + ifaProposalVO.getFilterKeyWord() + "%");
				params.add("%" + ifaProposalVO.getFilterKeyWord() + "%");
				params.add("%" + ifaProposalVO.getFilterKeyWord() + "%");
				params.add("%" + ifaProposalVO.getFilterKeyWord() + "%");
			}
			hql.append(" AND mii.ifafirm.id=?");
			params.add(memberAdmin.getIfafirm().getId());
			hql.append(" GROUP BY c.id ORDER BY c.createTime DESC");
			List<Object> objects = this.baseDao.find(hql.toString(), params.toArray(), false);
			jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
			if(objects != null && !objects.isEmpty()){
				jsonPaging.setTotal(objects.size());
			}
			if(jsonPaging.getList() != null && !jsonPaging.getList().isEmpty()){
				List<CrmProposal> proposals = jsonPaging.getList();
				List<IfaProposalVO> vos = new ArrayList<IfaProposalVO>();
				for (CrmProposal proposal : proposals) {
					IfaProposalVO vo = new IfaProposalVO();
					BeanUtils.copyProperties(proposal, vo);
					vo.setClientName(getCommonMemberName(proposal.getMember().getId(), langCode, "2"));
					if (proposal.getCreateTime() != null) {
						vo.setCreateTimeStr(DateUtil.dateToDateString(proposal.getCreateTime(), dateFormat));
					}
					vo.setIfaName(getCommonMemberName(proposal.getIfaMember().getMember().getId(), langCode, "2"));
					if (StringUtils.isNotBlank(proposal.getBaseCurrId())) {
						vo.setBaseCurrencyName(getParamConfigName(langCode, proposal.getBaseCurrId(), CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY_TYPE));
					}
					if (StringUtils.isNotBlank(proposal.getStatus())) {
						vo.setStatusDisplay(PropertyUtils.getPropertyValue(langCode, "console.crm.proposal.list.table.title.status." + proposal.getStatus(), null));
					}
					List<AccessoryFile> accessoryFile = accessoryFileService.getAccessoryFile(vo.getId(), CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL, "create_proposal_PDF_" + langCode, langCode);
					if(accessoryFile != null && !accessoryFile.isEmpty()){
						vo.setProposalDocPath(accessoryFile.get(0).getFilePath());
					}
					vos.add(vo);
				}
				jsonPaging.getList().clear();
				jsonPaging.getList().addAll(vos);
			}
		}
		return jsonPaging;
	}
}
