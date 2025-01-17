package com.espaceadherent.service;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.espaceadherent.config.AppProperties;
import com.espaceadherent.model.User;

import freemarker.template.Configuration;


@Service
public class MailServiceImpl implements MailService {

	private final Logger logger = LogManager.getLogger(getClass());
	private static final String SUPPORT_EMAIL = "support.email";
	public static final String LINE_BREAK = "<br>";
	public final static String BASE_URL = "baseUrl";
	
	@Autowired
	private MessageService messageService;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private Environment env;

	@Autowired
	Configuration freemarkerConfiguration;

	@Autowired
	AppProperties appProperties;

	@Async
	@Override
	public void sendVerificationToken(String token, User user) {
		final String confirmationUrl = appProperties.getClient().getBaseUrl() + "/verify?token=" + token;
		// Assuming "mail/verificationConfirm.ftl" is your email template for the first verification token
		sendHtmlEmail("Registration Confirmation", LINE_BREAK + confirmationUrl, user, "mail/verification.ftl");
	}

	@Async
	@Override
	public void sendVerificationToken2(String token, User user) {
		final String confirmationUrl = appProperties.getClient().getBaseUrl() + "/verify2?token=" + token;
		logger.info("Sending password reset email with URL: " + confirmationUrl);  // Debug log
		sendHtmlEmail("Password Reset",  confirmationUrl, user, "mail/passwordReset.ftl");
	}

	private String geFreeMarkerTemplateContent(Map<String, Object> model, String templateName) {
		StringBuffer content = new StringBuffer();
		try {
			content.append(FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate(templateName), model));
			return content.toString();
		} catch (Exception e) {
			System.out.println("Exception occured while processing fmtemplate:" + e.getMessage());
		}
		return "";
	}

	private void sendHtmlEmail(String subject, String msg, User user, String templateName) {
		Map<String, Object> model = new HashMap<>();
		//model.put("name", user.getDisplayName());
		model.put("msg", msg);
		model.put("title", subject);
		model.put(BASE_URL, appProperties.getClient().getBaseUrl());
		try {
			String emailBody = geFreeMarkerTemplateContent(model, templateName);
			sendHtmlMail(env.getProperty(SUPPORT_EMAIL), user.getEmail(), subject, emailBody);
		} catch (MessagingException e) {
			logger.error("Failed to send mail", e);
		}
	}

	public void sendHtmlMail(String from, String to, String subject, String body) throws MessagingException {
		MimeMessage mail = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true, "UTF-8");
		helper.setFrom(from);
		if (to.contains(",")) {
			helper.setTo(to.split(","));
		} else {
			helper.setTo(to);
		}
		helper.setSubject(subject);
		helper.setText(body, true);
		mailSender.send(mail);
		logger.info("Sent mail: {0}", subject);
	}

}
