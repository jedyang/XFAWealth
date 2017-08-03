//package com.fsll.wmes.web.service.impl;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.fsll.common.base.service.BaseService;
//import com.fsll.common.util.JsonPaging;
//import com.fsll.wmes.web.service.WebToReadService;
//import com.fsll.wmes.entity.WebToRead;
///***
// * 业务接口实现类：待阅管理接口类
// * @author tan
// * @date 2016-06-23
// */
//@Service("webToReadService")
////@Transactional
//public class WebToReadServiceImpl extends BaseService implements WebToReadService {
// 
//  
//	/***
//     * 查询内容
//     * @author tan
//     * @date 2016-06-23
//     */
//	@Override
//	public WebToRead findById(String id) {
//		String hql="from WebToRead where id=? ";
//		List params=new ArrayList();
//		params.add(id);
//		List<WebToRead> list=this.baseDao.find(hql, params.toArray(), false);
//		if (null!=list&&!list.isEmpty()) {
//			return list.get(0);
//		}
//		return null;
//	}
//	
//	
//    /***
//     * 查询
//     * @author tan
//     * @date 2016-06-23
//     */
//	@Override
//	//@Transactional(readOnly=true)
//	public JsonPaging findAll(JsonPaging jsonpaging,WebToRead webToRead) {
//		String hql=" from WebToRead where isValid=1 and status=? ";
//		List<Object> params=new ArrayList<Object>();
//		params.add(webToRead.getStatus());
//		
//		if(null != webToRead.getOwner().getId() && !"".equals( webToRead.getOwner().getId())){
//			hql += "and owner.id=? ";
//			params.add( webToRead.getOwner().getId());
//		}
//		if(null!=webToRead.getFromMember() && null!=webToRead.getFromMember().getNickName() && !"".equals(webToRead.getFromMember().getNickName())){
//			hql += "and fromMember.nickName like ? ";
//			params.add("%"+webToRead.getFromMember().getNickName()+"%");
//		}
//		
//		jsonpaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);
//
//		return jsonpaging;
//	}
//	
//	 
//
//	
//	/***
//     * 创建或更新
//     * @author tan
//     * @date 2016-06-23
//     */
//	@Override
//	public WebToRead saveOrUpdate(WebToRead webToRead) {
//		if(null == webToRead.getId() || "".equals(webToRead.getId())){
//			webToRead.setId(null);
//			webToRead.setCreateTime(new Date());
//			webToRead.setReceiveTime(new Date());
//			webToRead.setStatus("1");
//			webToRead.setIsValid("1");
//			webToRead = (WebToRead)baseDao.create(webToRead);
//		}else{
//			webToRead = (WebToRead)baseDao.update(webToRead);
//		}
//		return webToRead;
//	}
//
//
//	/**
//	 * 修改待阅状态
//	 * @author wwluo
//	 * @date 2016-08-25
//	 * @param toreadId
//	 * @return boolean
//	 */
//	@Override
//	public boolean updateStatusById(String toreadId) {
//		if(StringUtils.isNotBlank(toreadId)){
//			WebToRead webToRead = (WebToRead) this.baseDao.get(WebToRead.class, toreadId);
//			if(null != webToRead){
//				webToRead.setStatus("2");//'处理情况 1新建, 2完成,3删除'
//				this.baseDao.update(webToRead);
//				return true;
//			}
//		}
//		return false;
//	}
//	
//
////	/***
////	 * 根据待办编码查询其他待办
////	 * @author tan
////	 * @date 2016-06-23
////	 * @param domain
////	 * @return
////	 */
////	public List<WebToDo> findOtherByToDoCode(String id,String toDoCode){
////		String hql="from WebToDo where id !=? and toDoCode=?";
////		List params=new ArrayList();
////		params.add(id);
////		params.add(toDoCode);
////		List<WebToDo> list=this.baseDao.find(hql, params.toArray(), false);
////		if (null!=list&&!list.isEmpty()) {
////			return list;
////		}
////		return null;
////	}
//    
//}
