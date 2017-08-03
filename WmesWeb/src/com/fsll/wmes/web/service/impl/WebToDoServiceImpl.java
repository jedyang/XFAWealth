//package com.fsll.wmes.web.service.impl;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.fsll.common.base.service.BaseService;
//import com.fsll.common.util.JsonPaging;
//import com.fsll.wmes.web.service.WebToDoService;
//import com.fsll.wmes.entity.WebToDo;
///***
// * 业务接口实现类：待办管理接口类
// * @author tan
// * @date 2016-06-23
// */
//@Service("webToDoService")
////@Transactional
//public class WebToDoServiceImpl extends BaseService implements WebToDoService {
// 
//  
//	/***
//     * 查询内容
//     * @author tan
//     * @date 2016-06-23
//     */
//	@Override
//	public WebToDo findById(String id) {
//		String hql="from WebToDo where id=? ";
//		List params=new ArrayList();
//		params.add(id);
//		List<WebToDo> list=this.baseDao.find(hql, params.toArray(), false);
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
//	public JsonPaging findAll(JsonPaging jsonpaging, WebToDo webToDo) {
//		String hql=" from WebToDo where isValid=1 and status=? ";
//		List<Object> params=new ArrayList<Object>();
//		params.add(webToDo.getStatus());
//		
//		if(null != webToDo.getOwner().getId() && !"".equals(webToDo.getOwner().getId())){
//			hql += "and owner.id=? ";
//			params.add(webToDo.getOwner().getId());
//		}
//		if(null!=webToDo.getFromMember() && null!=webToDo.getFromMember().getNickName() && !"".equals(webToDo.getFromMember().getNickName())){
//			hql += "and fromMember.nickName like ? ";
//			params.add("%"+webToDo.getFromMember().getNickName()+"%");
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
//	public WebToDo saveOrUpdate(WebToDo webToDo) {
//		if(null == webToDo.getId() || "".equals(webToDo.getId())){
//			webToDo.setId(null);
//			webToDo.setCreateTime(new Date());
//			webToDo.setReceiveTime(new Date());
//			webToDo.setStatus("1");
//			webToDo.setIsValid("1");
//			webToDo = (WebToDo)baseDao.create(webToDo);
//		}else{
//			webToDo = (WebToDo)baseDao.update(webToDo);
//		}
//		return webToDo;
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
