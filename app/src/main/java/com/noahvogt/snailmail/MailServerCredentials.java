package com.noahvogt.snailmail;

public class MailServerCredentials {
    private String mImapHost;
    private String mSmtpHost;
    private String mUsername;
    private String mPassword;
    private int mImapPort;
    private int mSmtpPort;
    private String mName;
    private String mSignature;

    public String getImapHost () {return this.mImapHost;}
    public String getSmtpHost () {return this.mSmtpHost;}
    public String getUsername () {return this.mUsername;}
    public String getPassword () {return this.mPassword;}
    public int getImapPort () {return this.mImapPort;}
    public int getSmtpPort () {return  this.mSmtpPort;}
    public String getName () {return this.mName;}
    public String getSignature () {return this.mSignature;}

    public MailServerCredentials(String name, String username, String password, String imapHost, String smtpHost, int imapPort,
                                 int smtpPort, String signature) {
        this.mName = name;
        this.mUsername = username;
        this.mPassword = password;

        this.mImapHost = imapHost;
        this.mImapPort = imapPort;
        this.mSmtpPort = smtpPort;
        this.mSmtpHost = smtpHost;

        this.mSignature = signature;
    }
}
