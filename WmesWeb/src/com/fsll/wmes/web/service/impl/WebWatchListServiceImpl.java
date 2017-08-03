/**
 * 
 */
package com.fsll.wmes.web.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.WebWatchlist;
import com.fsll.wmes.entity.WebWatchlistType;
import com.fsll.wmes.web.service.WebWatchListService;

/**
 * @author zxtan
 * 收藏接口实现
 */
@Service("webWatchListService")
//@Transactional
public class WebWatchListServiceImpl extends BaseService implements WebWatchListService {
	
	/**3.1.13	关注/取消
	 * @author scshi
	 * @param relateId 对应类型id
	 * @param memberID 网站会员ID
	 * @parame OpType	Follow-设置关注;Delete-取消关注
	 * @param moduleType 对应模块,ifa关注,article文章关注,strategy策略关注分类,portfolio_info组合关注分类
	 */
	public WebWatchlist saveWebWatchlist(String productId, String memberId,String opType, String typeId) {
		String hql = "from WebWatchlist t where t.product.id=? and t.member.id=?  and t.isValid='1' ";
		List params = new ArrayList();
		params.add(productId);
		params.add(memberId);
//		params.add(typeId);
		List<WebWatchlist> list = this.baseDao.find(hql, params.toArray(), false);
		
		if("Follow".equals(opType) && list.isEmpty()){
			WebWatchlist follow = new WebWatchlist();
			MemberBase member = (MemberBase)this.baseDao.get(MemberBase.class, memberId);
			follow.setCreateTime(new Date());
			follow.setLastUpdate(new Date());
			follow.setIsValid("1");
			follow.setMember(member);
			
			//add by linwenwei :fund=1,stock=2,bond=3
			/*WebWatchlistType wt = new WebWatchlistType();
			wt.setId("1");
			follow.setWebWatchlistType(wt);*/
			//modify by mqzou 2017-04-01
			WebWatchlistType wt =findTypeById(typeId);
			follow.setWebWatchlistType(wt);
			
			ProductInfo product = (ProductInfo) this.baseDao.get(ProductInfo.class, productId);
			follow.setProduct(product);
			follow.setOrderBy(1);
			this.baseDao.create(follow);
			return follow;
		}else if("Delete".equals(opType) && !list.isEmpty()){
			WebWatchlist follow = list.get(0);
            this.baseDao.delete(follow);
            return new WebWatchlist();
		}
		return null;
		
	}

	/**	获取基金收藏状态
	 * @author zxtan
	 * @param productId 对应产品id
	 * @param memberID 网站会员ID
	 * @param typeId 对应模块
	 */
	//@Transactional(readOnly=true)
	public String getWebWatchStatus(String productId, String memberId,String typeId) {
		String hql = "from WebWatchlist t where t.product.id=? and t.member.id=? and t.isValid='1' ";
		List params = new ArrayList();
		params.add(productId);
		params.add(memberId);
		List<WebWatchlist> list = this.baseDao.find(hql, params.toArray(), false);
		if(!list.isEmpty()){
			//WebSelf obj = list.get(0);
			return "1";
		}
		return null;
	}
	
	/**
	 * 获取type
	 * @author mqzou 2017-04-01
	 * @param id
	 * @return
	 */
	@Override
	public WebWatchlistType findTypeById(String id) {
		WebWatchlistType type=(WebWatchlistType)baseDao.get(WebWatchlistType.class, id);
		return type;
	}
	
	/**
	 * 保存type
	 * @author mqzou 2017-04-01
	 * @param id
	 * @return
	 */
	@Override
	public WebWatchlistType saveType(WebWatchlistType type) {
		type=(WebWatchlistType)baseDao.saveOrUpdate(type);
		return type;
	}
	
	/**
	 * 获取用户的所有分类中的最大排序号
	 * @author mqzou 2017-04-01
	 * @param memberId
	 * @return
	 */
	@Override
	public int getMaxTypeOrderBy(String memberId) {
		String hql=" select max(r.orderBy) from WebWatchlistType r where r.member.id=?";
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		List list=baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			Object object=list.get(0);
			if(null!=object && !"".equals(object.toString())){
				return Integer.valueOf(object.toString());
			}
		}
		return 0;
	}
	
	/**
	 * 获取用户的所有分类
	 * @author mqzou 2017-04-01
	 * @param memberId
	 * @return
	 */
	@Override
	public List<WebWatchlistType> findAllType(String memberId) {
		String hql="  from WebWatchlistType r where r.member.id=? and r.isValid='1' order by r.orderBy";
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		List resultList=baseDao.find(hql, params.toArray(), false);
		List<WebWatchlistType> list=new ArrayList<WebWatchlistType>();
		if(null!=resultList && !resultList.isEmpty()){
			 Iterator it=resultList.iterator();
			 while (it.hasNext()) {
				WebWatchlistType object = (WebWatchlistType) it.next();
				list.add(object);
			}
		}
		return list;
	}
	
	/**
	 * 新增type
	 *  @author mqzou 2017-04-01
	 * @param name
	 * @param member
	 * @return
	 */
	@Override
	public WebWatchlistType saveWatchlistType(String name, MemberBase member) {
		WebWatchlistType vo=new WebWatchlistType();
		vo.setCreateTime(DateUtil.getCurDate());
		vo.setIsValid("1");
		vo.setLastUpdate(DateUtil.getCurDate());
		vo.setMember(member);
		vo.setName(name);
		vo.setOrderBy(getMaxTypeOrderBy(member.getId()+1));
		vo=saveType(vo);
		return vo;
	}

	/**
	 * 删除
	 * @author mqzou 2017-04-05
	 * @param type
	 */
	@Override
	public void deleteType(WebWatchlistType type) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from WebWatchlist r where r.webWatchlistType.id=?");
		List<Object> params=new ArrayList<Object>();
		params.add(type.getId());
		List list=this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			Iterator it=list.iterator();
			while (it.hasNext()) {
				WebWatchlist vo = (WebWatchlist) it.next();
				vo.setIsValid("0");
				baseDao.saveOrUpdate(vo);
			}
		}
		
		type.setIsValid("0");
		baseDao.saveOrUpdate(type);
		
	}
}
