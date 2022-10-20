package com.noahvogt.snailmail.mail.imap;

import java.util.Properties;

import jakarta.mail.MessagingException;
import jakarta.mail.NoSuchProviderException;
import jakarta.mail.Session;
import jakarta.mail.Store;

public class IMAPStoreConnector {
    private static Properties properties;
    private static Session session;

    public static Store getConncetion(String imapHostname, int imapPort, String username,
            String password) throws MessagingException, NoSuchProviderException {
        setupProperties(imapHostname, imapPort);
        getSessionInstance();

        return getStoreConnection(imapHostname, username, password);
    }

    private static void setupProperties(String imapHostname, int imapPort) {
        properties = new Properties();
        properties.put("mail.imap.starttls.enable", "true");
        properties.put("mail.imap.ssl.enable", "true");
        properties.put("mail.imap.host", imapHostname);
        properties.put("mail.imap.port", imapPort);
    }

    private static void getSessionInstance() {
        session = Session.getDefaultInstance(properties);
    }

    private static Store getStoreConnection(String imapHostname, String username, String password)
            throws MessagingException, NoSuchProviderException {
        Store store = session.getStore("imaps");
        store.connect(imapHostname, username, password);

        return store;
    }
}
