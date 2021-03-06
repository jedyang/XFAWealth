package com.fsll.wmes.web.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.WebEmailLog;
import com.fsll.wmes.entity.WebReadToDo;
import com.fsll.wmes.web.service.EmailLogService;
import com.fsll.wmes.web.vo.EmailLogVO;


/***
 * 业务接口实现类：邮件管理接口类
 * @author mqzou
 * @date 2016-06-22
 */
@Service("emailService")
//@Transactional
public class EmailLogServiceImpl extends BaseService implements EmailLogService {

	/***
     * 查询email管理
     * @author mqzou
     * @date 2016-06-23
     */
	@Override
	//@Transactional(readOnly=true)
	public JsonPaging findAll(JsonPaging jsonpaging, WebEmailLog emailLog) {
		String hql=" from WebEmailLog r where 1=1";
		List<Object> params=new ArrayList<Object>();
		if(null!=emailLog.getToEmailAddr()&&!"".equals(emailLog.getToEmailAddr())){
			hql+=" and r.toEmailAddr like '%"+emailLog.getToEmailAddr()+"%'";
		}
//		if(null!=emailLog.getCreatorId()){
//			hql+=" AND r.creatorId='"+emailLog.getCreatorId()+"'"; 
//		}
//		if(null!=emailLog.getModuleType()){
//			hql+=" AND r.moduleType LIKE '%"+emailLog.getModuleType()+"%' ";
//		}
		jsonpaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);
		List list = new ArrayList();
		Iterator it = jsonpaging.getList().iterator();
		//Integer indexNumber = (jsonpaging.getPage()-1)*jsonpaging.getRows();
		//Integer index = 0;
    	while(it.hasNext()){
			//index++;
			WebEmailLog obj = (WebEmailLog)it.next();
			EmailLogVO vo = new EmailLogVO();
			BeanUtils.copyProperties(obj,vo);//拷贝信息;
			vo.setToMemberName(obj.getToMember().getNickName());
			String flag=obj.getSendFlag();
			if("0".equals(flag))
				vo.setSendFlagName("等待发送");
			else if("1".equals(flag)) {
				vo.setSendFlagName("已发送");
			}
			String content=obj.getToEmailContent();
			if(content.length()>30){
				vo.setToEmailContent(content.substring(0, 30)+"...");
			}
			String title=obj.getToEmailTitle();
			if(title.length()>10){
				vo.setToEmailTitle(title.substring(0, 10)+"...");
			}
			list.add(vo);
		}
    	jsonpaging.setList(list);
		return jsonpaging;
	}

	@Override
	public WebEmailLog findById(String id) {
		WebEmailLog log = (WebEmailLog) baseDao.get(WebEmailLog.class, id);
		return findDetail(log);
	}
	
	/**
	 * 获得一条记录的详细信息
	 * @param 
	 * @return
	 */
	private WebEmailLog findDetail(WebEmailLog log){
		
		return log;
	}

	/***
	 * 保存对象的方法
	 * @author michael
	 * @date 2016-11-14
	 * @param webEmailLog 对象
	 * @return
	 */
	public WebEmailLog saveOrUpdate(WebEmailLog webEmailLog){
		return (WebEmailLog)baseDao.saveOrUpdate(webEmailLog);
	}
}
