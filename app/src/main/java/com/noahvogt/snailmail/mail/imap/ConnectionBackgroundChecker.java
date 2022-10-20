package com.noahvogt.snailmail.mail.imap;

import com.noahvogt.snailmail.workers.ConnectRunnable;

public class ConnectionBackgroundChecker {
    public static boolean canConnect(String imapHost, int imapPort, String username,
            String password) {
        ConnectRunnable connectRunnable = new ConnectRunnable(imapHost, imapPort, username,
                password);
        Thread checkConnectionThread = new Thread(connectRunnable);

        checkConnectionThread.start();
        try {
            checkConnectionThread.join();
            return connectRunnable.getConnect();
        } catch (InterruptedException e) {
            return false;
        }
    }
}
