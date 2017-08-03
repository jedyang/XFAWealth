/**
 * 
 */
package com.fsll.wmes.fund.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.StrUtils;
import com.fsll.wmes.fund.service.FundFeesItemService;
import com.fsll.wmes.fund.vo.FundChargesDataVO;

/**
 * @author tan
 *基金管理费用信息接口实现
 * @date 20160629
 */
@Service("fundFeesItemService")
//@Transactional
public class FundFeesItemServiceImpl extends BaseService implements FundFeesItemService {

	
	   /***
     * 分页查询记录
     * @param jsonpaging 分页参数
     * @param fundInfo 查询参数
	 * @return
     */
	//@Transactional(readOnly = true)
	public JsonPaging findAll(JsonPaging jsonPaging,String fundId,String feesItem,String langCode){
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder(" From FundFeesItem l ");
		hql.append(" inner join "+this.getLangString("FundFeesItem", langCode));
		hql.append(" r on l.id=r.id ");
		hql.append(" where l.isValid='1' and l.fund.id= ? ");		
			
		params.add(fundId);
		
		if(feesItem != null && !"".equals(feesItem)){
			hql.append(" and r.feesItem like ?  ");
			params.add("%"+feesItem+"%");
		}
		
		
		this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging , false);

		return jsonPaging;
	}

	/**
	 * 修改状态
	 * @param ids 主键
	 * @param status 状态  0/1
	 */
	@Override
	public Boolean saveStatus(String ids, String status) {
		if (!"".equals(ids)) {
			String tmpStr = "";
			String[] arr =null;
			if(ids.endsWith(",")){
				tmpStr = ids.substring(0,ids.length()-1);
				arr =tmpStr.split(",");
			}else{
				arr =new String[]{ids};
			}			
			for (String id : arr) {				 
				String hql = "update FundFeesItem v set v.isValid = ? where v.id =  ? ";
				List<Object> params = new ArrayList<Object>();
				params.add(status);
				if(!"".equals(id)){
					params.add(id); 
					this.baseDao.updateHql(hql, params.toArray());
				}
			}
		}
		return true;
	}

//	/**
//	 * 根据id查找英文实体内容
//	 * @param id
//	 * @return
//	 */
//	@Override
//	public FundFeesItemEn findById4En(String id) {
//		// TODO Auto-generated method stub
//		String hql = "from FundFeesItemEn where id=? ";
//		List params=new ArrayList();
//		params.add(id);
//		List<FundFeesItemEn> list = this.baseDao.find(hql, params.toArray(), false);
//		if (null!=list&&!list.isEmpty()) {
//			return list.get(0);
//		}
//		
//		return null;
//	}
//
//	/**
//	 * 根据id查找简体实体内容
//	 * @param id
//	 * @return
//	 */
//	@Override
//	public FundFeesItemSc findById4Sc(String id) {
//		// TODO Auto-generated method stub
//		String hql = "from FundFeesItemSc where id=? ";
//		List params=new ArrayList();
//		params.add(id);
//		List<FundFeesItemSc> list = this.baseDao.find(hql, params.toArray(), false);
//		if (null!=list&&!list.isEmpty()) {
//			return list.get(0);
//		}
//		
//		return null;
//	}
//
//	/**
//	 * 根据id查找繁体实体内容
//	 * @param id
//	 * @return
//	 */
//	@Override
//	public FundFeesItemTc findById4Tc(String id) {
//		// TODO Auto-generated method stub
//		String hql = "from FundFeesItemTc where id=? ";
//		List params=new ArrayList();
//		params.add(id);
//		List<FundFeesItemTc> list = this.baseDao.find(hql, params.toArray(), false);
//		if (null!=list&&!list.isEmpty()) {
//			return list.get(0);
//		}
//		
//		return null;
//	}

//	@Override
//	public FundFeesItem saveOrUpdate(FundFeesItem obj) {
//		// TODO Auto-generated method stub
//		if(null == obj.getId() || "".equals(obj.getId())){
//			obj.setId(null);
//			obj = (FundFeesItem)baseDao.create(obj);
//		}else{
//			obj = (FundFeesItem)baseDao.update(obj);
//		}
//		return obj;
//	}
//
//	@Override
//	public FundFeesItemEn saveOrUpdate4En(FundFeesItemEn obj,Boolean isAdd) {
//		// TODO Auto-generated method stub
//		if(true == isAdd){
//			obj = (FundFeesItemEn)baseDao.create(obj);
//		}else{
//			obj = (FundFeesItemEn)baseDao.update(obj);
//		}
//		return obj;
//	}
//
//	@Override
//	public FundFeesItemSc saveOrUpdate4Sc(FundFeesItemSc obj,Boolean isAdd) {
//		// TODO Auto-generated method stub
//		if(true == isAdd){
//			obj = (FundFeesItemSc)baseDao.create(obj);
//		}else{
//			obj = (FundFeesItemSc)baseDao.update(obj);
//		}
//		return obj;
//	}
//
//	@Override
//	public FundFeesItemTc saveOrUpdate4Tc(FundFeesItemTc obj,Boolean isAdd) {
//		// TODO Auto-generated method stub
//		if(true == isAdd){
//			obj = (FundFeesItemTc)baseDao.create(obj);
//		}else{
//			obj = (FundFeesItemTc)baseDao.update(obj);
//		}
//		return obj;
//	}


	@Override
	public Object findItemById(String id) {
		String hql = " FROM FundFeesItem r WHERE r.id=?";
		List params=new ArrayList();
		params.add(id);
		List<Object> list = this.baseDao.find(hql, params.toArray(), false);
		if (null!=list&&!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public Object findById(String id, String langCode) {
		// TODO Auto-generated method stub
		String hql = "from "+this.getLangString("FundFeesItem", langCode)+" where id=? ";
		List params=new ArrayList();
		params.add(id);
		List<Object> list = this.baseDao.find(hql, params.toArray(), false);
		
		if (null!=list&&!list.isEmpty()) {
			return list.get(0);
		}
		
		return null;
	}
	
	@Override
	public Object saveOrUpdate(Object obj,Boolean isAdd) {
		// TODO Auto-generated method stub
		if(true == isAdd){
			obj = (Object)baseDao.create(obj);
		}else{
			obj = (Object)baseDao.update(obj);
		}
		return obj;
	}
	

	/**3.1.8	获取基金管理费用信息
	 * @author scshi
	 * @param fundId 资金id
	 * @param LANG_CODE 语言
	 * @return	<List>FundChargesDataVO	基金管理费用信息
	 */
	@Override
	//@Transactional(readOnly=true)
	public List<FundChargesDataVO> fundChargesInfo(String fundId,String langCode) {
		List params = new ArrayList();
		String hql = "select t.fund.id, out.feesItem,out.fees From FundFeesItem t ";
		hql += " INNER JOIN "+this.getLangString("FundFeesItem", langCode);
		hql += " out ON t.id=out.id where t.isValid='1' ";
		
		if(null!=fundId && !"".equals(fundId)){
			hql +=" and t.fund.id=? ";
			params.add(fundId);
		}
		
		List preList = this.baseDao.find(hql, params.toArray(), false);
		List<FundChargesDataVO> voList = new ArrayList();
		if(!preList.isEmpty()){
			for(int x=0;x<preList.size();x++){
				FundChargesDataVO vo = new FundChargesDataVO();
				Object[] objs = (Object[])preList.get(x);
				vo.setFundId(StrUtils.getString(objs[0]));
				vo.setItemName(StrUtils.getString(objs[1]));
				vo.setDescription(StrUtils.getString(objs[2]));
				voList.add(vo);
			}
		}
		return voList;
	}

	@Override
	public Boolean deleteItem(Object obj) {
		if(obj != null){
			this.baseDao.delete(obj);
			return true;
		}else{
			return false;
		}
	}

}
