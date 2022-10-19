package com.noahvogt.snailmail.mail;

import java.util.ArrayList;
import java.util.Properties;

import jakarta.mail.Folder;
import jakarta.mail.MessagingException;
import jakarta.mail.NoSuchProviderException;
import jakarta.mail.Session;
import jakarta.mail.Store;

public class GetMailBoxes {
    static Properties properties;
    static Session session;

    public static ArrayList<String> getMailBoxesAsArrayList(String imapHostname, int imapPort, String username, String password) {
        setupProperties(imapHostname, imapPort);
        getSessionInstance(username, password);
        ArrayList<String> mailBoxes = new ArrayList<String>();

        System.out.println("### noice");

        try {
            Store store = session.getStore("imaps");
            store.connect(imapHostname, username, password);
            /*
             * this returns all subfolders. when only wishing to get the top-level folders,
             * use .list() without arguments
             */
            Folder[] folders = store.getDefaultFolder().list("*");

            for (Folder folder : folders) {
                System.out.println("## " + folder);
                mailBoxes.add(folder.getFullName());
            }

        } catch (NoSuchProviderException e) {
            System.out.println("Provider Not Found");
            e.printStackTrace();
        } catch (MessagingException e) {
            System.out.println("Messaging Problem");
            e.printStackTrace();
        }

        return mailBoxes;
    }

    public static void setupProperties(String imapHostname, int imapPort) {
        properties = new Properties();
        // properties.put("mail.imap.ssl.enable", "true");
        properties.put("mail.imap.ssl.enable", "true");
        properties.put("mail.imap.host", imapHostname);
        properties.put("mail.imap.port", imapPort);
    }

    public static void getSessionInstance(String username, String password) {
        session = Session.getDefaultInstance(properties);
    }
}
