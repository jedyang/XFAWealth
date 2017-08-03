package com.fsll.common.util;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;


/**
 * 邮件发送工具实现类
 * 
 * @author mjhuang
 * @create 2015/07/12
 */
public class MailUtil {
	protected final Logger logger = Logger.getLogger(getClass());
	public static final String ENCODEING = "UTF-8";
	private static final String HOST = PropertyUtils.getConfPropertyValue("mail.smtp.host"); // 服务器地址
	private static final String SENDER = PropertyUtils.getConfPropertyValue("mail.smtp.sender"); // 发件人的邮箱
	private static final String NAME = PropertyUtils.getConfPropertyValue("mail.smtp.name"); // 发件人昵称
	private static final String USERNAME = PropertyUtils.getConfPropertyValue("mail.smtp.username"); // 账号
	private static final String PASSWORD = PropertyUtils.getConfPropertyValue("mail.smtp.password"); // 密码
	/**
	 * 
	 * @param receiver // 收件人的邮箱
	 * @param subject // 主题
	 * @param message // 信息(支持HTML)
	 * @param attPaths 附件,格式如fileName1:filePath1;fileName2:filePath2;.......
	 *                filePath1使用的是绝对路径示例如下:
	 *                myFile1.png:/u/corner/201701/1.png;附件2.png:/u/corner/201701/2.png
	 * @return
	 */
	public boolean send(String receiver,String subject,String message,String attPaths) {
		// 发送email
		HtmlEmail email = new HtmlEmail();
		try {
			// 这里是SMTP发送服务器的名字：163的如下："smtp.163.com"
			email.setHostName(HOST);
			// 字符编码集的设置
			email.setCharset(ENCODEING);
			// 收件人的邮箱
			email.addTo(receiver);
			// 发送人的邮箱
			email.setFrom(SENDER,NAME);
			// 如果需要认证信息的话，设置认证：用户名-密码。分别为发件人在邮件服务器上的注册名称和密码
			email.setAuthentication(USERNAME,PASSWORD);
			// 要发送的邮件主题
			email.setSubject(subject);
			// 要发送的信息，由于使用了HtmlEmail，可以在邮件内容中使用HTML标签
			email.setMsg(message);
			//email.setHtmlMsg(message);
			
			if(attPaths != null && !"".equals(attPaths)){
				if(attPaths.endsWith(";"))attPaths = attPaths.substring(0,attPaths.length()-1);
				if(attPaths != null && !"".equals(attPaths)){
					String[] arr = attPaths.split(";");
					for (String attPathTmp : arr) {
						if(attPathTmp != null && !"".equals(attPathTmp)){
							String[] arrSec = attPathTmp.split(":");
							if(arrSec.length == 2){
								String fileName = arrSec[0];
								String filePath = arrSec[1];
								//String fileNameTmp = filePath.substring(filePath.lastIndexOf("/")+1);  
								EmailAttachment attachment = new EmailAttachment();//创建附件对象
								attachment.setPath(filePath);//附件的地址,绝对路径
								attachment.setDisposition(EmailAttachment.ATTACHMENT);//设定为附件
								//attachment.setDescription(fileName);//附件的描述 
								attachment.setName(fileName);//附件的名称,必须和文件名一致
								//添加附件对象
								email.attach(attachment);
							}
						}
					}
				}
			}
			//发送
			email.send();
			if (logger.isDebugEnabled()) {
				logger.debug(SENDER + " 发送邮件到 " + receiver);
			}
			return true;
		} catch (EmailException e) {
			e.printStackTrace();
			logger.info(SENDER + " 发送邮件到 " + receiver + " 失败");
			return false;
		}
	}

}
