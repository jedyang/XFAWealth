/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.oms;

/**
 * itrader消息处理方法
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-12-26
 */
public interface ITraderReceive {
	/**
	 * 设置要发送的命令
	 * @param sendMsg
	 */
	public void setSendMsg(String sendMsg);
	/**
	 * 获得要发送的命令
	 */
	public String getSendMsg();
	
	/**
	 * 
	 * 处理消息
	 * @param waitHandleMsg 待处理消息
	 */
	public void saveHandle(String waitHandleMsg);
}
