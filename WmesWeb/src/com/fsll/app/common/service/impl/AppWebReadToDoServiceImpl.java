package com.fsll.app.common.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.common.service.AppWebReadToDoService;
import com.fsll.app.common.vo.AppLatestTodoVO;
import com.fsll.app.common.vo.AppTodoVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.WebServiceUtil;
import com.fsll.wmes.entity.MemberAppToken;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.WebReadToDo;
import com.fsll.wmes.entity.WebReadToDoEn;
import com.fsll.wmes.entity.WebReadToDoSc;
import com.fsll.wmes.entity.WebReadToDoTc;
import com.fsll.wmes.web.vo.PushAppMsgVO;
/***
 * 业务接口类：待办管理
 * @author michael
 * @date 2016-10-13
 */
@Service("appWebReadToDoService")
//@Transactional
public class AppWebReadToDoServiceImpl extends BaseService implements AppWebReadToDoService {
 
  
	/***
	 * 根据id查询对象
	 * @author michael
	 * @date 2016-10-13
	 * @param id
	 * @return
	 */
	@Override
	public WebReadToDo findById(String id) {
		String hql="from WebReadToDo where id=? ";
		List params=new ArrayList();
		params.add(id);
		List<WebReadToDo> list=this.baseDao.find(hql, params.toArray(), false);
		if (null!=list&&!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
	
	
	/***
	 * 列表查询的方法
	 * @author michael
	 * @date 2016-10-13
	 * @param memberBase
	 * @param jsonPaging
	 * @return
	 */
	public JsonPaging findAll(JsonPaging jsonPaging, MemberBase memberBase) {
		String hql=" from WebReadToDo where isValid='1' ";
		List<Object> params=new ArrayList<Object>();
		
		if(null!=memberBase && null != memberBase.getId() && !"".equals( memberBase.getId())){
			hql += "and owner.id=? ";
			params.add( memberBase.getId());
		}
		hql += " order by  ";

		
		jsonPaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);

		return jsonPaging;
	}
	
	/***
	 * 保存对象的方法
	 * @author michael
	 * @date 2016-10-13
	 * @param webReadToDo 对象
	 * @return
	 */
	@Override
	public WebReadToDo saveOrUpdate(WebReadToDo webReadToDo) {
		return (WebReadToDo)baseDao.saveOrUpdate(webReadToDo);
	}

	/**
	 * 发送消息
	 * @author michael
	 * @date 2016-10-13
	 * @param webReadToDo
	 * @return
	 */
	public WebReadToDo sendToRead(WebReadToDo webReadToDo){
		return sendToRead(webReadToDo.getType(), webReadToDo.getModuleType(), webReadToDo.getRelateId(), webReadToDo.getTitle(), webReadToDo.getFromMember(), webReadToDo.getOwner());
	}
	
	/***
	 * 发送消息
	 * @author michael
	 * @date 2016-10-13
	 * @param type 消息类型  1：交易相关消息，2：一般性通知，3：告警、提醒消息（CommonConstantsWeb已定义）
	 * @param module 模块：insight关点,news新闻 （CommonConstantsWeb已定义）
	 * @param relateId 模块对应的对象id
	 * @param title 标题
	 * @param fromUser 发送人
	 * @param toUser 接收人
	 * @return
	 */
	public WebReadToDo sendToRead(String type, String module, String relateId, String title, MemberBase fromUser, MemberBase toUser) {
		WebReadToDo webReadToDo = new WebReadToDo();
		webReadToDo.setType(type);
		webReadToDo.setModuleType(module);
		webReadToDo.setRelateId(relateId);
		webReadToDo.setFromMember(fromUser);
		webReadToDo.setIsRead("0");//待阅
		webReadToDo.setIsValid("1");
		webReadToDo.setOwner(toUser);
		webReadToDo.setCreateTime(new Date());
		
		webReadToDo =  (WebReadToDo)baseDao.saveOrUpdate(webReadToDo);
		if (null!=webReadToDo && null!=webReadToDo.getId()){
			WebReadToDoEn en = new WebReadToDoEn();
			en.setId(webReadToDo.getId());
			en.setTitle(title);
			createReadToDoEn(en);
			
			WebReadToDoSc sc = new WebReadToDoSc();
			sc.setId(webReadToDo.getId());
			sc.setTitle(title);
			createReadToDoSc(sc);

			WebReadToDoTc tc = new WebReadToDoTc();
			tc.setId(webReadToDo.getId());
			tc.setTitle(title);
			createReadToDoTc(tc);
		}
		return webReadToDo;
	}
    
	/**
	 * 创建英文附加信息
	 * @param en
	 */
	private void createReadToDoEn(WebReadToDoEn en){
		String sql = " insert into web_read_to_do_en (id, title) value(?,?) " ;
		List<Object> params=new ArrayList<Object>();
		params.add( en.getId());
		params.add( en.getTitle());
		springJdbcQueryManager.update(sql, params.toArray());
	}
	
	/**
	 * 创建简体附加信息
	 * @param en
	 */
	private void createReadToDoSc(WebReadToDoSc sc){
		String sql = " insert into web_read_to_do_sc (id, title) value(?,?) " ;
		List<Object> params=new ArrayList<Object>();
		params.add( sc.getId());
		params.add( sc.getTitle());
		springJdbcQueryManager.update(sql, params.toArray());
	}
	
	/**
	 * 创建繁体附加信息
	 * @param en
	 */
	private void createReadToDoTc(WebReadToDoTc tc){
		String sql = " insert into web_read_to_do_tc (id, title) value(?,?) " ;
		List<Object> params=new ArrayList<Object>();
		params.add( tc.getId());
		params.add( tc.getTitle());
		springJdbcQueryManager.update(sql, params.toArray());
	}
	
	/**
	 * 根据消息类型得到待办待阅列表信息
	 * @param jsonPaging 分页参数
	 * @param memberId 用户ID
	 * @param langCode 语言
	 * @param isRead 是否已阅,1已阅,0待阅
	 * @param keyWord 关键词
	 * @param type 消息类型，1：交易相关消息，2：一般性通知，3：告警、提醒消息
	 * @return
	 */
	public JsonPaging getTodoList(JsonPaging jsonPaging,String memberId,String type,String isRead,String langCode,String keyWord){
		List<AppTodoVO> todoList=new ArrayList<AppTodoVO>();
//		String hql="select t.id,s.title,t.type,t.moduleType,t.relateId,t.isRead,t.readTime,t.createTime " +
		String hql=" from WebReadToDo t " +
				" inner join "+this.getLangString("WebReadToDo", langCode)+" s on s.id=t.id "+
				" inner join MemberBase b on b.id=t.fromMember.id "+
//				" left join MemberIfa i on i.member.id=b.id "+
//				" left join MemberIndividual d on d.member.id=b.id "+
				" where t.type=? and t.owner.id=? and t.isValid=? ";
		List params = new ArrayList();
		params.add(type);
		params.add(memberId);
		params.add("1");
		if(null!=isRead && !"".equals(isRead)){
			hql += " and t.isRead=? ";
			params.add(isRead);
		}
		if(null!=keyWord && !"".equals(keyWord)){
			hql += " and s.title like ? ";
			params.add("%"+keyWord+"%");
		}
		hql+=" order by t.createTime desc ";
		jsonPaging=baseDao.selectJsonPagingNoTotal(hql, params.toArray(),jsonPaging, false);
		List list=jsonPaging.getList();
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			WebReadToDo todo = (WebReadToDo) objs[0];
			WebReadToDoSc todoSc = new WebReadToDoSc();
			BeanUtils.copyProperties(objs[1], todoSc);
			MemberBase member = (MemberBase) objs[2];			
			
			AppTodoVO  vo = new AppTodoVO();
			
			vo.setId(todo.getId());
			vo.setTitle(todoSc.getTitle());
			vo.setType(todo.getType());
			vo.setModuleType(todo.getModuleType());
			vo.setRelateId(todo.getRelateId());
			vo.setFromMemberId(member.getId());
			
			String userName = getCommonMemberName(member.getId(),langCode,CommonConstants.MEMBER_NAME_REAL_NAME);

			vo.setFromMemberName(userName);
			
			vo.setMsgUrl(todo.getMsgUrl());
			vo.setMsgParam(todo.getMsgParam());
			vo.setAppUrl(todo.getAppUrl());
			vo.setAppParam(todo.getAppParam());
			vo.setIsApp(todo.getIsApp());
			vo.setIsRead(todo.getIsRead());			
			vo.setReadTime(DateUtil.dateToDateString(todo.getReadTime(), "yyyy-MM-dd HH:mm:ss"));
			vo.setCreateTime(DateUtil.dateToDateString(todo.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
						
			todoList.add(vo);
		}
		jsonPaging.setList(todoList);
		return jsonPaging;
	}
	
	/**
	 * 根据消息类型得到最新待办待阅信息
	 * @param memberId 用户ID
	 * @param langCode 语言
	 * @param type 消息类型，1：交易相关消息，2：一般性通知，3：告警、提醒消息
	 * @return
	 */
	public AppLatestTodoVO getLatestTodoMess(String memberId,String langCode){
//		List<AppTodoVO> todoList=new ArrayList<AppTodoVO>();
		AppLatestTodoVO vo = new AppLatestTodoVO();
		vo = getTodoList(vo,memberId, "1", langCode);
		vo = getTodoList(vo,memberId, "2", langCode);
		vo = getTodoList(vo,memberId, "3", langCode);
		
		return vo;
	}
	
	/**
	 * 根据消息类型得到待办待阅列表信息
	 * @param memberId 用户ID
	 * @param langCode 语言
	 * @param type 消息类型，1：交易相关消息，2：一般性通知，3：告警、提醒消息
	 * @return 
	 */
	private AppLatestTodoVO getTodoList(AppLatestTodoVO info,String memberId,String type,String langCode){
		List<AppTodoVO> todoList =  info.getTodoList();
		if(null == todoList){
			todoList = new ArrayList<AppTodoVO>();
		}
		
		StringBuilder hql = new StringBuilder();
		hql.append(" from WebReadToDo t ");
		hql.append(" inner join "+this.getLangString("WebReadToDo", langCode)+" s on s.id=t.id ");
		hql.append(" inner join MemberBase b on b.id=t.fromMember.id ");
		hql.append(" where t.type=? and t.owner.id=? and t.isValid=? ");
		hql.append(" and (t.isRead = '0' or t.isRead is null) ");
		List params = new ArrayList();
		params.add(type);
		params.add(memberId);
		params.add("1");
		hql.append(" order by t.createTime desc ");
		List list=baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			Object[] objs = (Object[])list.get(0);
			WebReadToDo todo = (WebReadToDo) objs[0];
			WebReadToDoSc todoSc = new WebReadToDoSc();
			BeanUtils.copyProperties(objs[1], todoSc);
			MemberBase member = (MemberBase) objs[2];			
			
			AppTodoVO  vo = new AppTodoVO();
			
			vo.setId(todo.getId());
			vo.setTitle(todoSc.getTitle());
			vo.setType(todo.getType());
			vo.setModuleType(todo.getModuleType());
			vo.setRelateId(todo.getRelateId());
			vo.setFromMemberId(todo.getFromMember().getId());
			String userName = getCommonMemberName(member.getId(), langCode,CommonConstants.MEMBER_NAME_REAL_NAME);
			vo.setFromMemberName(userName);
			
			vo.setMsgUrl(todo.getMsgUrl());
			vo.setMsgParam(todo.getMsgParam());
			vo.setAppUrl(todo.getAppUrl());
			vo.setAppParam(todo.getAppParam());
			vo.setIsApp(todo.getIsApp());
			vo.setIsRead(todo.getIsRead());			
			vo.setReadTime(DateUtil.dateToDateString(todo.getReadTime(), "yyyy-MM-dd HH:mm:ss"));
			vo.setCreateTime(DateUtil.dateToDateString(todo.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			
			todoList.add(vo);
			if("1".equals(type)){
				info.setDealCount(String.valueOf(list.size()));
			}else if("2".equals(type)){
				info.setNoteCount(String.valueOf(list.size()));
			}else if("3".equals(type)){
				info.setAlertCount(String.valueOf(list.size()));
			}
			info.setTodoList(todoList);
		}
		return info;
	}
	
	/**
	 * 生成WebReadToDo记录
	 * @param todo
	 * @param titleEn
	 * @param titleSc
	 * @param titleTc
	 * @return
	 */
	 public WebReadToDo saveWebReadToDo(WebReadToDo todo, String titleEn, String titleSc, String titleTc){ 
		  WebReadToDo newTodo = (WebReadToDo) baseDao.saveOrUpdate(todo);
		  WebReadToDoEn en = new WebReadToDoEn(); 
		  en.setId(newTodo.getId());
		  en.setTitle(null==titleEn?todo.getTitle():titleEn); 
		  createReadToDoEn(en);
		  WebReadToDoSc sc = new WebReadToDoSc(); 
		  sc.setId(newTodo.getId());
		  sc.setTitle(null==titleSc?todo.getTitle():titleSc);
		  createReadToDoSc(sc);
		  WebReadToDoTc tc = new WebReadToDoTc(); 
		  tc.setId(newTodo.getId());
		  tc.setTitle(null==titleTc?todo.getTitle():titleTc); 
		  createReadToDoTc(tc);
		  // TODO app消息推送
		  if("1".equals(newTodo.getIsApp())){
			  String langCode = newTodo.getOwner().getLangCode();
			  if(CommonConstants.LANG_CODE_SC.equals(langCode)){
				  pushAppMsg(newTodo.getOwner().getId(), sc.getTitle(), sc.getTitle(), newTodo.getAppUrl(), newTodo.getAppParam());
			  }else if(CommonConstants.LANG_CODE_TC.equals(langCode)){
				  pushAppMsg(newTodo.getOwner().getId(), tc.getTitle(), tc.getTitle(), newTodo.getAppUrl(), newTodo.getAppParam());
			  }else{
				  pushAppMsg(newTodo.getOwner().getId(), en.getTitle(), en.getTitle(), newTodo.getAppUrl(), newTodo.getAppParam());
			  }
		  }
		  return newTodo; 
	}
	 
		/**
		 * app消息推送
		 * @author wwluo
		 * @param memberId
		 * @param title
		 * @param text
		 * @param activity
		 * @param extraField
		 */
		@SuppressWarnings({ "unused" })
		private void pushAppMsg(String memberId, String title, String text, String activity, String extraField){
			if (StringUtils.isNotBlank(memberId)) {
				//memberId = "0d6a8a08c49b4b5cba58b86d02b7e068";
				MemberAppToken memberAppToken = findDeviceToken(memberId);
				if(memberAppToken != null && StringUtils.isNotBlank(memberAppToken.getAppType())){
					String deviceType = null;
					if("2".equals(memberAppToken.getAppType())){
						deviceType = CommonConstants.DEVICE_TYPE_IOS;
					}else if("1".equals(memberAppToken.getAppType())){
						deviceType = CommonConstants.DEVICE_TYPE_ANDROID;
					}
					PushAppMsgVO pushAppMsg = new PushAppMsgVO();
					pushAppMsg.setType("unicast"); // unicast单播、listcast列播、broadcast广播、customizedcast（自有alias）
					pushAppMsg.setAlias(memberAppToken.getAlias());
					pushAppMsg.setAliasType(memberAppToken.getAliasType());
					pushAppMsg.setDeviceTokens(memberAppToken.getDeviceToken()); // 设备token
					pushAppMsg.setProductionMode("true"); // 模式（true正式，false测试）
					pushAppMsg.setTitle(title); // title
					pushAppMsg.setText(text); // text
					pushAppMsg.setActivity(activity); // 包名地址
					pushAppMsg.setExtraField(extraField); // 参数
					WebServiceUtil.pushSms(deviceType, pushAppMsg);
				}
			}
		}
		
		/**
		 * 查找用户的app设备信息
		 * @author michael
		 * @param memberId
		 * @return
		 */
		public MemberAppToken findDeviceToken(String memberId){
			String hql = "from MemberAppToken t where t.memberId=? order by lastUpdate desc ";
			List params = new ArrayList();
			params.add(memberId);
			List<MemberAppToken> list = this.baseDao.find(hql, params.toArray(), false);
			if(null!=list && !list.isEmpty()){
				return list.get(0);
			}
			return null;
		}
}
