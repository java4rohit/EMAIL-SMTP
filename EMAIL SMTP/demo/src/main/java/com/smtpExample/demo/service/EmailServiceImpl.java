// Java Program to Illustrate Creation Of
// Service implementation class

package com.smtpExample.demo.service;

// Importing required classes
import java.io.File;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;


import com.smtpExample.demo.Entity.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

// Annotation
@Service
// Class
// Implementing EmailService interface
public class EmailServiceImpl implements EmailService {

	@Autowired private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String sender;

	@Value("${spring.mail.password}")
	private String password;
	@Value("${spring.mail.host}")
	private String host;
	@Value("${spring.mail.port}")
	private String port;


	// Method 1
	// To send a simple email
	public String sendSimpleMail(EmailDetails details)
	{
		// Get system properties
		Properties properties = System.getProperties();
		// Setup mail server
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", 587);
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.auth", "true");

		// Get the Session object.// and pass username and password
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(sender, password);
			}
		});
		session.setDebug(true);
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(sender));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("rohit@retaillogis.com"));
			message.setSubject(details.getSubject());
			message.setText(details.getMsgBody());
			String s_NO = "1";
		    String successFail = "Success";

			LocalDateTime now = LocalDateTime.now();
			System.out.println("Before Formatting: " + now);
			DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");
			DateTimeFormatter date = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String formatTime = now.format(time);
			String formatDate = now.format(date);

			String api_call_time = formatTime;
			String comments = "Working Fine";
			String Trigger_date = formatDate;
			message.setContent("<table border=\"1\" cellspacing=\"0\" cellpadding=\"1\">" +
					"<tr class=\"tableHeader\">" +
					"<td>S.NO</td>" +
					"<td>API Call Time</td>" +
					"<td>Success/Fail</td>" +
					"<td>Comments</td>\n" +
					"<td>Trigger Date</td>" +
					" </tr>\n" +
					" Biomertic API CALL Information\n" +
					"<tr class=\"tableBody\">\n" +
					"<td>"+s_NO+"</td>\n" +
					"<td>"+api_call_time+"</td>\n" +
					"<td>"+successFail +"</td>\n" +
					"<td>"+comments+"</td>\n" +
					"<td>"+Trigger_date+"</td>\n" +
					"</tr>\n" +
					"</#foreach> " +
					"</table>","text/html");
			System.out.println("sending...================================================="+message);
			Transport.send(message);
			return "Sent message successfully....";
		} catch (MessagingException mex) {
			System.out.println(mex);
			return "Sent message ERROR....";
		}
	}

	// Method 2 To send an email with attachment
	public String sendMailWithAttachment(EmailDetails details)
	{
		// Creating a mime message
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper;
		try {
          // Setting multipart as true for attachments to be send
			mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setFrom(sender);
			mimeMessageHelper.setTo(details.getRecipient());
			mimeMessageHelper.setText(details.getMsgBody());
			mimeMessageHelper.setSubject(details.getSubject());
			// Adding the attachment
			FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));
			mimeMessageHelper.addAttachment(file.getFilename(), file);
			// Sending the mail
			javaMailSender.send(mimeMessage);
			return "Mail sent Successfully";
		}

		// Catch block to handle MessagingException
		catch (MessagingException e) {
			// Display message when exception occurred
			return "Error while sending mail!!!";
		}
	}
}
