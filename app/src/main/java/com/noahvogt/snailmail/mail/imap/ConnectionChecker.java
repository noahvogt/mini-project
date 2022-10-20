package com.noahvogt.snailmail.mail.imap;

import jakarta.mail.MessagingException;
import jakarta.mail.NoSuchProviderException;

public class ConnectionChecker {
    public static boolean canConnect(String imapHostname, int imapPort, String username,
            String password) {
        try {
            IMAPStoreConnector.getConncetion(imapHostname, imapPort, username, password);
        } catch (NoSuchProviderException e) {
            return false;
        } catch (MessagingException e) {
            return false;
        }

        return true;
    }
}
