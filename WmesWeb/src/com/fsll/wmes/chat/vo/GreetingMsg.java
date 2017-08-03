/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.chat.vo;

/**
 * 
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-9-19
 */
public class GreetingMsg{
    private String name;
    private String content;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
    public String toString() {
        return "UserChatMsg{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
