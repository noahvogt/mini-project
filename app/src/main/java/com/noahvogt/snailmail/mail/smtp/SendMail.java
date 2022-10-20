package com.noahvogt.snailmail.mail.smtp;

import android.content.Context;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class SendMail {
    static Message message;
    static Session session;
    static Properties properties;

    public static void sendMessage(String fromAddress, String toAddresses, String smtpHostname,
            int smtpPort, String username, String password, String subject, String messageBody,
            Context context, String cc, String bcc) {
        setupProperties(smtpHostname, smtpPort);
        getSessionInstance(username, password);
        composeMessage(fromAddress, subject, messageBody, toAddresses, cc, bcc);
        transportMessage();
    }

    public static void composeMessage(String fromAddress, String subject, String messageBody,
            String to, String cc, String bcc) {
        message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(fromAddress));

            addRecipientsOfCertainTypeSuccess(Message.RecipientType.TO, to);
            addRecipientsOfCertainTypeSuccess(Message.RecipientType.CC, cc);
            addRecipientsOfCertainTypeSuccess(Message.RecipientType.BCC, bcc);

            message.setSubject(subject);
            message.setText(messageBody);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error while parsing message");
        }
    }

    public static void addRecipientsOfCertainTypeSuccess(Message.RecipientType recipientType,
            String recipients) throws MessagingException {
        if (!recipients.isEmpty()) {
            String[] recipientsArray = recipients.split(",");
            for (String recipient : recipientsArray)
                message.addRecipient(recipientType, new InternetAddress(recipient));
        }
    }

    public static void setupProperties(String smtpHostname, int smtpPort) {
        properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtpHostname);
        properties.put("mail.smtp.port", smtpPort);
    }

    public static void getSessionInstance(String username, String password) {
        session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public static void transportMessage() {
        try {
            Transport.send(message);
            System.out.println("Message sent successfully");
        } catch (MessagingException e) {
            System.out.println("Error while sending message");
        }
    }
}
