package com.noahvogt.miniprojekt;

public class MailServerCredentials {
    private String mImapHost;
    private String mSmtpHost;
    private String mUsername;
    private String mPassword;
    private int mImapPort;
    private String mName;

    public MailServerCredentials(String name, String username, String password, String imapHost, String smtpHost, int imapPort) {
        mName = name;
        mUsername = username;
        mPassword = password;

        mImapHost = imapHost;
        mImapPort = imapPort;
        mSmtpHost = smtpHost;
    }
}
