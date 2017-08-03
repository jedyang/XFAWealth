package com.fsll.wmes.web.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sun.util.logging.resources.logging;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.StrUtils;
import com.fsll.common.util.WebServiceUtil;
import com.fsll.core.service.SysParamService;
import com.fsll.core.vo.SysParamVO;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.MemberAppToken;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberSso;
import com.fsll.wmes.entity.WebReadToDo;
import com.fsll.wmes.entity.WebReadToDoEn;
import com.fsll.wmes.entity.WebReadToDoSc;
import com.fsll.wmes.entity.WebReadToDoTc;
import com.fsll.wmes.member.service.MemberAppTokenService;
import com.fsll.wmes.web.service.EmailLogService;
import com.fsll.wmes.web.service.WebReadToDoService;
import com.fsll.wmes.web.vo.PushAppMsgVO;
/***
 * 业务接口类：待办管理
 * @author michael
 * @date 2016-10-13
 */
@Service("webReadToDoService")
//@Transactional
public class WebReadToDoServiceImpl extends BaseService implements WebReadToDoService {
	@Autowired
	protected MemberAppTokenService memberAppTokenService;
	@Autowired
	private SysParamService sysParamService;
	
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
		hql += " order by createTime desc ";

		
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
		return sendToRead(webReadToDo.getType(), webReadToDo.getModuleType(), webReadToDo.getRelateId(), webReadToDo.getTitle(), webReadToDo.getMsgUrl(), webReadToDo.getMsgParam(), webReadToDo.getIsApp(), webReadToDo.getFromMember(), webReadToDo.getOwner());
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
		return sendToRead(type, module, relateId, title, "", "", "", fromUser, toUser);
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
	 * @param url 	任务url,此字段供web使用
	 * @param parms 相关参数key=value格式,多个之间用&分隔
	 * @param ifApp 是否app使用的待办,1是,0否
	 * @return
	 */
	public WebReadToDo sendToRead(String type, String module, String relateId, String title, String url, String parms, String ifApp, MemberBase fromUser, MemberBase toUser) {
		WebReadToDo webReadToDo = new WebReadToDo();
		webReadToDo.setType(type);
		webReadToDo.setModuleType(module);
		webReadToDo.setRelateId(relateId);
		webReadToDo.setFromMember(fromUser);
		webReadToDo.setMsgUrl(url);
		webReadToDo.setMsgParam(parms);
		webReadToDo.setIsApp(ifApp);
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
		
		//如果app也使用，则发送消息
		if (StringUtils.isNotEmpty(ifApp) && "1".equals(ifApp)){//非null和空串
			try {
				MemberAppToken token = memberAppTokenService.findDeviceToken(toUser.getId());
				PushAppMsgVO msg = new PushAppMsgVO();
				//公共
				msg.setType("unicast");//unicast-单播,listcast-列播,broadcast-广播,customizedcast-自有的alias进行推送
				msg.setDeviceTokens(token.getDeviceToken());
				msg.setAlias(token.getAlias());
				msg.setAliasType(token.getAliasType());
				msg.setProductionMode("false");//可选，true/false, 正式/测试模式。测试模式下，只会将消息发给测试设备
				msg.setDescription("发送待阅消息，同步发送至app。");//可选
				msg.setExtraField(JsonUtil.toJson(webReadToDo));
				
				//ios
				msg.setAlert(title);//内容
//				msg.setBadge(badge);//角标??
//				msg.setIosSound(iosSound);
				
				//android
				msg.setDisplayType("notification");//消息类型，值可以为notification-通知，message-消息
				String typeName = StrUtils.getString(getMsgType(type, StrUtils.getString(toUser.getLangCode(),CommonConstants.DEF_LANG_CODE)),title);
				msg.setTicker(typeName);//通知栏提示文字
				msg.setTitle(typeName);//通知类型
				msg.setText(title);//标题文字描述
//				msg.setAfterOpen("go_url");//点击"通知"的后续行为，默认为打开app,go_app:打开应用 go_url:跳转到URL go_activity:打开特定的activity go_custom"用户自定义内容
//				msg.setUrl(url);
//				msg.setActivity(activity);//可选 ,当after_open为go_activity时,必填
				
				WebServiceUtil.pushSms(token.getAppType(), msg);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return webReadToDo;
	}
    
	private String getMsgType(String type, String langCode){
		String result = sysParamService.findNameByValue("WEB_READ_MESSAGE_TYPE",type,langCode);

		return result;
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
	 * 生成todo记录
	 * 
	 * @param todo
	 * @return
	 */
	public WebReadToDo saveWebReadToDo(WebReadToDo todo) { 
		return (WebReadToDo)baseDao.saveOrUpdate(todo); 
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
		  WebReadToDo newTodo = saveWebReadToDo(todo);
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
	 * 更新待办为已阅
	 * @param en
	 */
	@Override
	public void updateReadToDoReaded(String id){
		String sql = " update web_read_to_do set is_read='1',read_time=? where id=?" ;
		List<Object> params=new ArrayList<Object>();
		params.add(new Date());
		params.add(id);
		springJdbcQueryManager.update(sql, params.toArray());
	}
	
	/***
	 * 列表查询的方法
	 * @author michael
	 * @date 2017-02-16
	 * @param owner
	 * @param relateId
	 * @return
	 */
	public List findByOwner(MemberBase owner, String relateId) {
		String hql=" from WebReadToDo where isValid='1' and relateId=?";
		List<Object> params=new ArrayList<Object>();
		params.add(relateId);
		if(null!=owner && null != owner.getId() && !"".equals( owner.getId())){
			hql += " and owner.id=? ";
			params.add( owner.getId());
		}
		hql += " order by createTime desc ";

		List list=this.baseDao.find(hql, params.toArray(), false);

		return list;
	}
	
	/**
	 * 更新待办为已阅
	 * @author michael
	 * @date 2017-02-16
	 * @param owner
	 * @param relateId
	 */
	public void updateReadToDoReaded(MemberBase owner, String relateId){
		String hql = " update WebReadToDo set isRead='1',readTime=? where isRead<>'1' and owner.id=? and relateId=?" ;
		List<Object> params=new ArrayList<Object>();
		params.add(new Date());
		params.add(owner.getId());
		params.add(relateId);
		this.baseDao.updateHql(hql, params.toArray());
	}


	/**
	 * 更新待办已阅（处理好友 申请消息后将所有相关的待办都设置为已阅）
	 * @author mqzou 2017-05-11
	 * @param type
	 * @param fromMemberId
	 * @param owner
	 */
	@Override
	public void updateReadTodoReaded(String type, String fromMemberId, String owner) {
		StringBuilder hql=new StringBuilder();
		hql.append(" update WebReadToDo set isRead='1' ,readTime=? where isRead<>'1' and  moduleType=? and fromMember.id=? and owner.id=?");
		List<Object> params=new ArrayList<Object>();
		params.add(DateUtil.getCurDate());
		params.add(type);
		params.add(fromMemberId);
		params.add(owner);
		 this.baseDao.updateHql(hql.toString(), params.toArray());
		//System.out.println("aaaaaaaaaaaaaaaaaaaaf");
		
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
			MemberAppToken memberAppToken = memberAppTokenService.findDeviceToken(memberId);
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
}
