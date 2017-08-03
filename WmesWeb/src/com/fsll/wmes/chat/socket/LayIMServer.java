package com.fsll.wmes.chat.socket;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import com.fsll.wmes.chat.pojo.SocketUser;
import com.fsll.wmes.chat.pojo.message.ToServerTextMessage;
import com.fsll.wmes.chat.socket.manager.OnLineUserManager;
import com.fsll.wmes.chat.socket.sender.MessageSender;
import com.fsll.wmes.chat.util.LayIMFactory;


/**
 * mjhuang
 */
@ServerEndpoint(value="/websocket/{uid}",configurator=SpringConfigurator.class)
public class LayIMServer {
	@Autowired
	private MessageSender messageSender;
	
	@OnOpen
	public void open(Session session,@PathParam("uid") String uid) {
		SocketUser user = new SocketUser();
		user.setSession(session);
		user.setUserId(uid);
		LayIMFactory.createManager().addUser(user);//保存在线列表
		//print("当前在线用户：" + LayIMFactory.createManager().getOnlineCount());
		//print("缓存中的用户个数：" + new OnLineUserManager().getOnLineUsers().size());
		
	}
	
	@OnMessage
	public void receiveMessage(String message, Session session) {
		ToServerTextMessage toServerTextMessage = LayIMFactory.createSerializer().toObject(message, ToServerTextMessage.class);
		//得到接收的对象
		messageSender.sendMessage(toServerTextMessage);
	}

	@OnError
	public void error(Throwable t) {
		//print(t.getMessage());
	}

	@OnClose
	public void close(Session session) {
		SocketUser user = new SocketUser();
		user.setSession(session);
		user.setUserId("0");
	//	print("用户掉线");
		LayIMFactory.createManager().removeUser(user);//移除该用户
	}

	/*private void print(String msg) {
		System.out.println(msg);
	}*/
	
}
