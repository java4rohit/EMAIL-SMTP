// Java Program to Illustrate Creation Of
// Service Interface

package com.smtpExample.demo.service;

import com.smtpExample.demo.Entity.EmailDetails;

public interface EmailService {

	// Method To send a simple email
	String sendSimpleMail(EmailDetails details);
	// Method To send an email with attachment
	String sendMailWithAttachment(EmailDetails details);
}
