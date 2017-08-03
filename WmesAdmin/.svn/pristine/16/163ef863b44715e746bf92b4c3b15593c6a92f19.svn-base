package com.fsll.wmes.bond.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysAdmin;
import com.fsll.core.service.SysAdminService;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.bond.service.BondInfoService;
import com.fsll.wmes.bond.vo.BondAskListVO;
import com.fsll.wmes.bond.vo.BondProductVO;
import com.fsll.wmes.entity.BondAsk;
import com.fsll.wmes.entity.BondInfo;
import com.fsll.wmes.entity.BondInfoEn;
import com.fsll.wmes.entity.BondInfoSc;
import com.fsll.wmes.entity.BondInfoTc;
import com.fsll.wmes.entity.ProductDistributor;
import com.fsll.wmes.entity.StockInfo;
import com.fsll.wmes.entity.StockInfoEn;
import com.fsll.wmes.entity.StockInfoSc;
import com.fsll.wmes.entity.StockInfoTc;
import com.fsll.wmes.stock.service.StockInfoService;
import com.fsll.wmes.stock.vo.StockProductVO;

/**
 * 债券信息查询服务接口实现
 * @author Yan
 * @date 2016-12-09
 */
@Service("bondInfoService")
//@Transactional
public class BondInfoServiceImpl extends BaseService implements BondInfoService {

	@Autowired
	private SysParamService sysParamService;
	@Autowired
	private SysAdminService sysAdminService;
	
	/**
	 * 获取债券基础数据列表
	 * @return
	 */
	@Override
	public List<BondProductVO> getBondProductVoList(String langCode) {
		List<BondProductVO> voList = new ArrayList<BondProductVO>();
//		List params = new ArrayList();
		String hql = " FROM BondInfo r "
			+ " INNER JOIN " + this.getLangString("BondInfo", langCode)+" i ON r.id=i.id ";
		List<Object> list = this.baseDao.find(hql, null, false);
		if(!list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Object[] objRead = (Object[])list.get(i);
				BondInfo info = (BondInfo)objRead[0];
				BondProductVO vo = new BondProductVO();
				vo.setId(info.getId());
				if(!"".equals(info.getProduct()) && null!=info.getProduct()){
					vo.setProductId(info.getProduct().getId());
				}
				if("sc".equals(langCode)){
					BondInfoSc sc = (BondInfoSc)objRead[1];
					vo.setName(sc.getBondName());
				} else if("tc".equals(langCode)){
					BondInfoTc tc = (BondInfoTc)objRead[1];
					vo.setName(tc.getBondName());
				} else if("en".equals(langCode)){
					BondInfoEn en = (BondInfoEn)objRead[1];
					vo.setName(en.getBondName());
				}
				voList.add(vo);
			}
		}
		return voList;
	}

	/**
	 * 获取债券报价列表（未报价）
	 * @author mqzou 2017-06-28
	 * @param jsonPaging
	 * @param currency
	 * @param keyword
	 * @return
	 */
	@Override
	public JsonPaging findBondAskList(JsonPaging jsonPaging,  String keyword,String langCode) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from BondAsk r left join "+getLangString("BondInfo", langCode)+" b on r.bond.id=b.id");
		hql.append(" left join MemberIndividual i on r.member.id=i.member.id");
	//	hql.append(" left join MemberDistributor d on r.account.distributor.id=d.id");
		hql.append(" left join ProductDistributor p on p.distributor.id=r.account.distributor.id and p.product.id=r.bond.product.id");
		hql.append(" where r.isValid='1' and r.orderStatus='1'");
		List<Object> params=new ArrayList<Object>();
		if(null!=keyword && !"".equals(keyword)){
			hql.append(" and (b.bondName like ?  or CONCAT(i.firstName,i.lastName) like ? or i.nameChn like ? or r.accountNo like ? or p.symbolCode like ?)");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
		}
		jsonPaging=baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List< BondAskListVO> list=new ArrayList<BondAskListVO>();
		if(null!=jsonPaging && null!=jsonPaging.getList()){
			Iterator it=jsonPaging.getList().iterator();
			while (it.hasNext()) {
				Object[] objects=(Object[])it.next();
				BondAsk ask = (BondAsk) objects[0];
				Object bondObject=objects[1];
				BondAskListVO vo=new BondAskListVO(ask);
				vo.setCurrency(sysParamService.findNameByCode(vo.getCurrency(), langCode));
				String memberName=getCommonMemberName(ask.getMember().getId(), langCode, "2");
				vo.setClientName(memberName);
				if(CommonConstants.LANG_CODE_EN.equals(langCode)){
					BondInfoEn en=(BondInfoEn)bondObject;
					vo.setBondName(en.getBondName());
				}else if (CommonConstants.LANG_CODE_SC.equals(langCode)) {
					BondInfoSc sc=(BondInfoSc)bondObject;
					vo.setBondName(sc.getBondName());
				}else if (CommonConstants.LANG_CODE_TC.equals(langCode)) {
					BondInfoTc tc=(BondInfoTc)bondObject;
					vo.setBondName(tc.getBondName());
				}
				list.add(vo);
				
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}

	/**
	 * 获取债券报价历史列表
	 * @author mqzou 2017-06-28
	 * @param jsonPaging
	 * @param currency
	 * @param keyword
	 * @param status
	 * @param orderType
	 * @return
	 */
	@Override
	public JsonPaging findBondAskHistoryList(JsonPaging jsonPaging,  String keyword, String status, String orderType,String langCode,String startDate,String endDate) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from BondAsk r left join "+getLangString("BondInfo", langCode)+" b on r.bond.id=b.id");
		hql.append(" left join MemberIndividual i on r.member.id=i.member.id");
		hql.append(" left join ProductDistributor p on p.distributor.id=r.account.distributor.id and p.product.id=r.bond.product.id");
		hql.append(" where r.isValid='1' and r.orderStatus!='1'");
		List<Object> params=new ArrayList<Object>();
		if(null!=keyword && !"".equals(keyword)){
			hql.append(" and (b.bondName like ?  or CONCAT(i.firstName,i.lastName) like ? or i.nameChn like ? or r.accountNo like ? or p.symbolCode like ?)");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
		}
		if(null!=status && !"".equals(status)){
			hql.append(" and r.orderStatus=?");
			params.add(status);
		}
		if(null!=orderType && !"".equals(orderType)){
			hql.append(" and r.orderType=?");
			params.add(orderType);
		}
		if(null!=startDate && !"".equals(startDate)){
			hql.append(" and  r.orderDate>?");
			params.add(DateUtil.StringToDate(startDate, DateUtil.DEFAULT_DATE_FORMAT));
		}
		if(null!=endDate && !"".equals(endDate)){
			hql.append(" and  r.orderDate<?");
			params.add(DateUtil.StringToDate(endDate, DateUtil.DEFAULT_DATE_FORMAT));
		}
		jsonPaging=baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List< BondAskListVO> list=new ArrayList<BondAskListVO>();
		if(null!=jsonPaging && null!=jsonPaging.getList()){
			Iterator it=jsonPaging.getList().iterator();
			while (it.hasNext()) {
				Object[] objects=(Object[])it.next();
				BondAsk ask = (BondAsk) objects[0];
				Object bondObject=objects[1];
				BondAskListVO vo=new BondAskListVO(ask);
				vo.setCurrency(sysParamService.findNameByCode(vo.getCurrency(), langCode));
				String memberName=getCommonMemberName(ask.getMember().getId(), langCode, "2");
				vo.setClientName(memberName);
				if(CommonConstants.LANG_CODE_EN.equals(langCode)){
					BondInfoEn en=(BondInfoEn)bondObject;
					vo.setBondName(en.getBondName());
				}else if (CommonConstants.LANG_CODE_SC.equals(langCode)) {
					BondInfoSc sc=(BondInfoSc)bondObject;
					vo.setBondName(sc.getBondName());
				}else if (CommonConstants.LANG_CODE_TC.equals(langCode)) {
					BondInfoTc tc=(BondInfoTc)bondObject;
					vo.setBondName(tc.getBondName());
				}
				vo.setOrderStatus(ask.getOrderStatus());
				
				String answerId=ask.getAnswerId();
				if(null!=answerId && !"".equals(answerId)){
					SysAdmin admin=sysAdminService.findById(answerId);
					if(null!=admin){
						vo.setAnswerName(admin.getNickName());
					}else {
					   vo.setAnswerName(getCommonMemberName(answerId, langCode, "2"));
					}
				}
				if(null!=ask.getAnswerDate()){
					vo.setAnswerDate(DateUtil.dateToDateString(ask.getAnswerDate(), DateUtil.DEFAULT_DATE_TIME_FORMAT));
				}
				if(null!=ask.getAnswerPrice()){
					vo.setAnswerPrice(ask.getAnswerPrice());
				}
				list.add(vo);
				
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	
	/**
	 * 获取债券报价详情
	 * @author mqzou 2017-06-28
	 * @param id
	 * @param langCode
	 * @return
	 */
	@Override
	public BondAskListVO findBondAskDetail(String id, String langCode) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from BondAsk r left join "+getLangString("BondInfo", langCode)+" b on r.bond.id=b.id");
		hql.append(" where r.id=?");
		List<Object> params=new ArrayList<Object>();
	    params.add(id);
	    List list=baseDao.find(hql.toString(), params.toArray(), false);
	    if(null!=list && !list.isEmpty()){
	    	Object[] objects=(Object[])list.get(0);
			BondAsk ask = (BondAsk) objects[0];
			Object bondObject=objects[1];
			BondAskListVO vo=new BondAskListVO(ask);
			vo.setCurrency(sysParamService.findNameByCode(vo.getCurrency(), langCode));
			String memberName=getCommonMemberName(ask.getMember().getId(), langCode, "2");
			vo.setClientName(memberName);
			if(CommonConstants.LANG_CODE_EN.equals(langCode)){
				BondInfoEn en=(BondInfoEn)bondObject;
				vo.setBondName(en.getBondName());
			}else if (CommonConstants.LANG_CODE_SC.equals(langCode)) {
				BondInfoSc sc=(BondInfoSc)bondObject;
				vo.setBondName(sc.getBondName());
			}else if (CommonConstants.LANG_CODE_TC.equals(langCode)) {
				BondInfoTc tc=(BondInfoTc)bondObject;
				vo.setBondName(tc.getBondName());
			}
			return vo;
	    }
		return null;
	}
	
	/**
	 * 获取债券报价内容
	 * @author mqzou 2017-06-28
	 * @param id
	 * @return
	 */
	@Override
	public BondAsk findById(String id) {
		BondAsk vo=(BondAsk)baseDao.get(BondAsk.class, id);
		return vo;
	}
	
	/**
	 * 保存债券报价信息
	 * @author mqzou 2017-06-28
	 * @param ask
	 * @return
	 */
	@Override
	public BondAsk saveBondAsk(BondAsk ask) {
		ask=(BondAsk)baseDao.saveOrUpdate(ask);
		return ask;
	}

}
