package com.fsll.wmes.investor.service;



import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysCountry;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberCorporate;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.member.vo.IndividualVO;

public interface InvestorManageService {

	
	/**
	 * Investor管理列表查询
	 * @author mqzou
	 * @date 2016-06-28
	 * @param individualVO
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public List<IndividualVO> getIndividualList(MemberIndividual individualVO,int startIndex,int pageSize);
	
	
	/**
	 * Individual列表查询
	 * @author mqzou
	 * @date 2016-06-28
	 * @param jsonpaging
	 * @param memberIndividual
	 * @return
	 */
	public JsonPaging findAllIndividual(JsonPaging jsonpaging, MemberIndividual memberIndividual );
	
	/**
	 * 根据id获取一条数据 Individual
	 * @author mqzou
	 * @date 2016-06-28
	 * @param id
	 * @return
	 */
	public MemberIndividual findIndividualById(String id) ;
	
	/**
	 * Corporate管理列表查询
	 * @author mqzou
	 * @date 2016-06-28
	 * @param jsonpaging
	 * @param memberCorporate
	 * @return
	 */
	public JsonPaging findAllCorporate(JsonPaging jsonpaging,MemberCorporate memberCorporate);
	
	/**
	 * 保存Individual信息
	 * @author mqzou
	 * @date 2016-06-29
	 * @param memberIndividual
	 * @return
	 */
	public MemberIndividual saveIndividual(MemberIndividual memberIndividual,MemberBase memberBase);
	
	/**
	 * 获取国家实体
	 * @param id
	 * @return
	 */
	public SysCountry findCountryById(String id);
	
}
