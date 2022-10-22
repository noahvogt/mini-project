package com.noahvogt.snailmail.workers;

import com.noahvogt.snailmail.mail.smtp.SendMail;

import android.content.Context;

public class SendMailRunnable implements Runnable {
    private String fromAddress, toAddresses, smtpHostname, username, password, subject, messageBody,
            cc, bcc;
    private int smtpPort;
    private Context context;

    public SendMailRunnable(String fromAddress, String toAddresses, String smtpHostname,
            String username, String password, String subject, String messageBody, String cc,
            String bcc, int smtpPort, Context context) {
        this.fromAddress = fromAddress;
        this.toAddresses = toAddresses;
        this.smtpHostname = smtpHostname;
        this.username = username;
        this.password = password;
        this.subject = subject;
        this.messageBody = messageBody;
        this.cc = cc;
        this.bcc = bcc;
        this.smtpPort = smtpPort;
        this.context = context;
    }

    @Override
    public void run() {
        SendMail.sendMessage(fromAddress, toAddresses, smtpHostname, smtpPort, username, password,
                subject, messageBody, context, cc, bcc);
    }
}
