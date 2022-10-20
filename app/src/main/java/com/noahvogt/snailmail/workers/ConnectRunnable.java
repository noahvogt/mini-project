package com.noahvogt.snailmail.workers;

import com.noahvogt.snailmail.mail.imap.ConnectionChecker;

public class ConnectRunnable implements Runnable {
    private boolean canConnect;
    private String imapHost, username, password;
    private int imapPort;

    public ConnectRunnable(String imapHost, int imapPort, String username, String password) {
        this.imapHost = imapHost;
        this.imapPort = imapPort;
        this.username = username;
        this.password = password;
    }

    @Override
    public void run() {
        canConnect = ConnectionChecker.canConnect(imapHost, imapPort, username, password);
        return;
    }

    public boolean getConnect() {
        return canConnect;
    }
}
