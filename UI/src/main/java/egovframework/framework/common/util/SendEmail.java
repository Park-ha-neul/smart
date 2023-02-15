package egovframework.framework.common.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
/*
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
*/
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
/*
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
*/
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.swing.Box;

import org.apache.log4j.Logger;

import egovframework.framework.common.object.DataMap;

/*******************************************
 *
 	- writer : Jo Jung Hyun
 	- create date : 2011.12.06
 	- comment : 메일보내기 (gmail)
 	- 추가 : <dependency>
	-		<groupId>javax.mail</groupId>
	-		<artifactId>mail</artifactId>
	-		<version>1.4</version>
	-	</dependency>
 	- 받는 사람은 to_list List형태로 box에 담아서 보냄
 *
********************************************/

public class SendEmail {

	public static boolean send(DataMap box, List fileList) throws MessagingException {

		System.out.println("################# SendEmail Start ###########################\n");
		System.out.println("\n################# ttl :" + box.getString("ttl"));
		System.out.println("\n################# cnts :" + box.getString("cnts"));
		System.out.println("\n################# to_list:" + box.getString("to_list"));
		System.out.println("\n################# to_cc_list:" + box.getString("to_cc_list"));
		System.out.println("\n################# to_bcc_list:" + box.getString("to_bcc_list"));

		System.out.println("################# SendEmail Start ###########################\n");

		List afList = new ArrayList(); // 첨부파일 리스트

		//메일 환경변수 설정
		DataMap emailProp = new DataMap();
		emailProp.put("host", StringUtil.nvl(box.getString("host"), EgovPropertiesUtil.getProperty("system.email.host")));
		//EmailProp.put("name", SysUtil.nvl(box.getString("ins_nm")), SysUtil.nvl(box.getString("from")));
		emailProp.put("from", StringUtil.nvl(box.getString("from")));

		Properties props = new Properties();
//		props.put("mail.smtp.starttls.enable", "true");
//		props.put("mail.smtp.host", "smtp.gmail.com");
//		props.put("mail.smtp.auth", "true");

//		EmailAuthenticator authenticator = new EmailAuthenticator(EmailProp.getString("id"), EmailProp.getString("password"));
//		Session session = Session.getInstance(props, authenticator);

		//smtp인증 필요없는경우
		props.put("mail.smtp.auth", "false");
		props.put("mail.smtp.host", emailProp.getString("host"));
		Session session = Session.getInstance(props, null);
//		Session session = Session.getDefaultInstance(props, null);
//		session.setDebug(true);
//		Transport transport = session.getTransport();

		//
		try {
			//=====================================================================
			//★ 1.Message(봉투)
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(emailProp.getString("from")));
			msg.setRecipients(Message.RecipientType.TO, InternetAddr_List(box.getList("to_list")));

			if(!StringUtil.nvl(box.getString("to_cc_list")).equals("")){
					msg.setRecipients(Message.RecipientType.CC, InternetAddr_List(box.getList("to_cc_list")));
			}
			if(!StringUtil.nvl(box.getString("to_bcc_list")).equals("")){
				msg.setRecipients(Message.RecipientType.BCC, InternetAddr_List(box.getList("to_bcc_list")));
			}
			msg.setSubject(box.getString("ttl"));
			msg.setSentDate(new Date());
			//msg.setContent(box.getString("cnts"),"text/html; charset=euc-kr");	// html type

			//=====================================================================
			//★ 2.Multipart 만듦(속봉투)

			//=====================================================================
			//보낼 메시지 셋팅

			MimeBodyPart mbp01 = new MimeBodyPart();
			mbp01.setContent(box.getString("cnts"),"text/html; charset=euc-kr");
			//mbp01.setText(box.getString("cnts"));

			//=====================================================================
			//보낼 파일 셋팅

			//첨부파일이 존재할때
			if(fileList != null){
				afList = attachFile(fileList);
			}

			//=====================================================================
			//★ 4.Multipart(속봉투)에 다가 MimeBodyPart를 넣어준다.

			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp01); //MimeBodyPart(내용물->메시지)

			//첨부파일 셋팅
			for(int i = 0; i < afList.size(); i++){
				mp.addBodyPart((MimeBodyPart)afList.get(i));
			}


			//=====================================================================
			//★ 5.Message(봉투)에다가 Multipart(속봉투) 넣어준다!
			//msg.setContent(mp,"text/html");
			msg.setContent(mp);


			Transport.send(msg);

//			transport.connect();
//			transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
//			transport.close();

		} catch (Exception e) {
			Logger logger = Logger.getLogger(SendEmail.class);
			//logger.debug(e.toString());
			System.out.println("######### 예외 발생34 ##########");
			return false;
		}

		System.out.println("################# SendEmail End ###########################\n");
		return true;
	}


	public static boolean sendSSL(DataMap box, List fileList) throws MessagingException {

		/*
		 * 필수 파라메터
		 * (String)host : smtp 주소
		 * (String)port : smtp port
		 * (String)id : smtp 계정 id
		 * (String)pw : smtp 계정 pw
		 *
		 * (String)from : 발신자 메일 계정
		 *
		 * (String)ttl : 글제목
		 * (String)cnts : 글내용
		 * (List<String>))to_list : 수신자 메일 리스트
		 *
		 * 선택 파라메터
		 * (List<String>))to_cc_list : 참조자 메일 리스트
		 * (List<String>))to_bcc_list : 비밀참조메일 리스트
		 */

		System.out.println("################# SendEmail Start ###########################\n");
		System.out.println("\n################# ttl :" + box.getString("ttl"));
		System.out.println("\n################# cnts :" + box.getString("cnts"));
		System.out.println("\n################# to_list:" + box.getString("to_list"));
		System.out.println("\n################# to_cc_list:" + box.getString("to_cc_list"));
		System.out.println("\n################# to_bcc_list:" + box.getString("to_bcc_list"));

		System.out.println("################# SendEmail Start ###########################\n");

		List afList = new ArrayList(); // 첨부파일 리스트

		//메일 환경변수 설정
		Properties props = new Properties();
        props.put("mail.smtp.host", EgovPropertiesUtil.getProperty("mail.host"));
        props.put("mail.smtp.port", EgovPropertiesUtil.getProperty("mail.port"));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.trust", EgovPropertiesUtil.getProperty("mail.host"));

        EmailAuthenticator authenticator = new EmailAuthenticator(EgovPropertiesUtil.getProperty("mail.id"), EgovPropertiesUtil.getProperty("mail.pw"));
        Session session = Session.getInstance(props, authenticator);


		//
		try {
			//=====================================================================
			//★ 1.Message(봉투)
			Message msg = new MimeMessage(session);

			msg.setFrom(new InternetAddress(box.getString("from", EgovPropertiesUtil.getProperty("mail.default.from"))));
			msg.setRecipients(Message.RecipientType.TO, InternetAddr_List(box.getList("to_list")));

			if(box.getList("to_cc_list") != null){
					msg.setRecipients(Message.RecipientType.CC, InternetAddr_List(box.getList("to_cc_list")));
			}
			if(box.getList("to_bcc_list") != null){
				msg.setRecipients(Message.RecipientType.BCC, InternetAddr_List(box.getList("to_bcc_list")));
			}
			msg.setSubject(box.getString("ttl"));
			//msg.setContent(box.getString("cnts"), "text/html; charset=EUC-KR");
			msg.setSentDate(new Date());


			//=====================================================================
			//★ 2.Multipart 만듦(속봉투)
			//=====================================================================
			//보낼 메시지 셋팅

			MimeBodyPart mbp01 = new MimeBodyPart();
			mbp01.setContent(box.getString("cnts"),"text/html; charset=euc-kr");
			//mbp01.setText(box.getString("cnts"));

			//=====================================================================
			//보낼 파일 셋팅

			//첨부파일이 존재할때
			if(fileList != null){
				afList = attachFile(fileList);
			}

			//=====================================================================
			//★ 4.Multipart(속봉투)에 다가 MimeBodyPart를 넣어준다.

			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp01); //MimeBodyPart(내용물->메시지)

			//첨부파일 셋팅
			for(int i = 0; i < afList.size(); i++){
				mp.addBodyPart((MimeBodyPart)afList.get(i));
			}


			//=====================================================================
			//★ 5.Message(봉투)에다가 Multipart(속봉투) 넣어준다!
			//msg.setContent(mp,"text/html");
			msg.setContent(mp);

			Transport.send(msg);

		} catch (Exception e) {
			Logger logger = Logger.getLogger(SendEmail.class);
			//logger.debug(e.toString());
			System.out.println("######### 예외 발생35 ##########");
			return false;
		}

		System.out.println("################# SendEmail End ###########################\n");
		return true;
	}

	private static InternetAddress[] InternetAddr_List(List list){
		InternetAddress[] address = new InternetAddress[list.size()];

		for(int i = 0; i < list.size(); i++){

			try {
				System.out.println(list.get(i).toString() + "\n");
				address[i] = new InternetAddress(list.get(i).toString());
			} catch (AddressException e) {
				System.out.println("######### 예외 발생36 ##########");
			}
		}

		return address;
	}

	private static List attachFile(List fileList){
		DataMap fBox = new DataMap();
		List mbpList = new ArrayList();
		MimeBodyPart mbp = new MimeBodyPart(); //★ 3.MimeBodyPart 만듦(내용물)

		for(int i = 0; i < fileList.size(); i++){
			fBox = (DataMap)fileList.get(i);
			File file = new File(fBox.getString("file_path") + fBox.getString("save_file_name")); //이 파일을 첨부해서 보내겟다는 의미!
			FileDataSource fds = new FileDataSource(file);
			try {
				mbp.setDataHandler(new DataHandler(fds));

				try {
					//파일에 한글명 있을시 표시 할수 있게끔 하기 위함.
					mbp.setFileName(MimeUtility.encodeWord(fBox.getString("file_nm")));
				} catch (UnsupportedEncodingException e) {
					System.out.println("######### 예외 발생37 ##########");
				}

			} catch (MessagingException e) {System.out.println("######### 예외 발생38 ##########");}
			mbpList.add(i, mbp);
		}
		return mbpList;
	}

	private static class EmailAuthenticator extends Authenticator {
		private String id;
		private String pw;

		public EmailAuthenticator(String id, String pw) {
			this.id = id;
			this.pw = pw;
		}

		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(id, pw);
		}

	}
}

