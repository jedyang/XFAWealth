package com.fsll.wmes.web.service;

import com.fsll.wmes.entity.WebVisitLog;

/***
 * 业务接口类：访问
 * @author mqzou
 * @date 2016-12-22
 */
public interface WebVisitLogService {

	/**
	 * 保存访问记录
	 * @author mqzou
	 * @date 2016-12-22
	 * @param log
	 * @return
	 */
	public WebVisitLog addLog(WebVisitLog log);
	
	/**
	 * 获取最新的一条访问记录（根据memberId或者浏览器标识）
	 * @author mqzou
	 * @date 2016-12-22
	 * @param log
	 * @return
	 */
	public WebVisitLog findLastLog(WebVisitLog log);
	
	
}
