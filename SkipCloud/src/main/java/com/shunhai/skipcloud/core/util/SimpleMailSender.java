package com.shunhai.skipcloud.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleMailSender {

	public static final Logger logger = LoggerFactory.getLogger(SimpleMailSender.class);
	//注册模板标识
	public static final String REGISTER="_register";
	//改密模板标识
	public static final String CHANGES_PWD="_changesPwd";

    //邮箱的配置文件
    private Properties properties ;

    //邮件服务器登录验证
    private MailAuthenticator authenticator;

    //邮箱session
    private Session session;

    //邮箱的配置文件路径
    private String mailConfigPath="/mail.properties";

    /**
     * 初始化邮件发送器
     * 获取发送邮件的props文件，以及加载邮箱的配置文件
     */
    public SimpleMailSender() {
    	//发送邮件的props文件
        Properties props = System.getProperties();
    	InputStream inputStream = PropertiesTool.class.getResourceAsStream(this.mailConfigPath);
    	this.properties = new Properties();
    	try {
			properties.load(inputStream);
		} catch (IOException e) {
			logger.error("无法加载"+this.mailConfigPath+"目录下的properties文件");
		}
    	init(props);
    }

    /**
     * 初始化
     * @param props 发送邮件的props文件
     */
	private void init(Properties props) {
    	String smtpHostName = this.properties.getProperty("smtpHostName");
    	if(null == smtpHostName || smtpHostName.equals("")){
    		smtpHostName = "smtp." + this.properties.getProperty("username").split("@")[1];
    	}
    	// 初始化props
    	props.put("mail.smtp.auth", "true"); // 使用验证
    	props.put("mail.smtp.host", smtpHostName);
    	props.put("mail.smtp.starttls.enable", "true");// 使用 STARTTLS安全连接
    	props.put("mail.transport.protocol", "smtp");
    	//props.put("mail.smtp.ssl.enable", "true");
    	// props.put("mail.smtp.port", "465"); //google使用465或587端口
    	// props.put("mail.debug", "true");

    	// 验证邮箱地址和密码
		this.authenticator = new MailAuthenticator(this.properties.getProperty("username"), properties.getProperty("password"));
    	// 创建session
    	this.session = Session.getInstance(props, this.authenticator);
    }

    /**
     * 发送邮件
     * @param recipient 收件人邮箱地址
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    public void send(String recipient, String subject, Object content){
    	// 创建mime类型邮件
    	MimeMessage message = new MimeMessage(session);
    	try {
    		// 设置发信人
			message.setFrom(new InternetAddress(authenticator.getUsername()));
			// 设置收件人
	    	message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
	    	// 设置主题
	    	message.setSubject(subject);
	    	// 设置邮件内容
	    	//String str = content.toString();
	    	//String newStr[] = str.split("\"");
	    	message.setContent(content.toString(), "text/html;charset=utf-8");
	    	// 设置发送时间
	    	message.setSentDate(new Date());
	    	// 发送
	    	Transport.send(message);
		} catch (AddressException e) {
			logger.error("发送方或接收方的邮箱地址错误："+e.getMessage());
		} catch (MessagingException e) {
			logger.error("发送的消息异常："+e.getMessage());
		}

    }

    /**
     * 群发邮件
     * @param recipients 收件人们
     * @param subject 主题
     * @param content 内容
     */
    public void send(List<String> recipients, String subject, Object content){
    	// 创建mime类型邮件
    	MimeMessage message = new MimeMessage(session);
    	try {
    		// 设置发信人
			message.setFrom(new InternetAddress(authenticator.getUsername()));
			// 设置收件人们
	    	int num = recipients.size();
	    	InternetAddress[] addresses = new InternetAddress[num];
	    	for (int i = 0; i < num; i++) {
	    		addresses[i] = new InternetAddress(recipients.get(i));
	    	}
	    	message.setRecipients(RecipientType.TO, addresses);
	    	// 设置主题
	    	message.setSubject(subject);
	    	// 设置邮件内容
	    	message.setContent(content.toString(), "text/html;charset=utf-8");
	    	// 设置发送时间
	    	message.setSentDate(new Date());
	    	// 发送
	    	Transport.send(message);
    	} catch (AddressException e) {
			logger.error("发送方或接收方的邮箱地址错误："+e.getMessage());
		} catch (MessagingException e) {
			logger.error("发送的消息异常："+e.getMessage());
		}
    }

    /**
     * 发送邮件
     * @param recipient 收件人邮箱地址
     * @param properties 邮件配置文件
     */
    public void send(String recipient , String template){
    	properties.setProperty("toEmailAddress", recipient);
    	send(recipient, PropertiesTool.get(properties, "mailSubject"+template) , PropertiesTool.get(properties, "mailContent"+template));
    }

    /**
     * 群发邮件
     * @param recipients 收件人们的邮箱地址
     * @param properties 邮件配置文件
     */
    public void send(List<String> recipients , String template){
    	send(recipients, PropertiesTool.get(properties, "mailSubject"+template) , PropertiesTool.get(properties, "mailContent"+template));
    }

    //邮箱密码验证类
    class MailAuthenticator extends Authenticator {

    	//用户名（登录邮箱）
        private String username;

        //密码
        private String password;

        public MailAuthenticator() {

        }
        /**
         * 初始化邮箱和密码
         * @param username 邮箱
         * @param password 密码
         */
        public MailAuthenticator(String username, String password) {
        	this.username = username;
        	this.password = password;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
        	return new PasswordAuthentication(this.username, this.password);
        }

        public String getPassword() {
        	return this.password;
        }

        public String getUsername() {
        	return this.username;
        }

        public void setPassword(String password) {
        	this.password = password;
        }

        public void setUsername(String username) {
        	this.username = username;
        }
    }
}
