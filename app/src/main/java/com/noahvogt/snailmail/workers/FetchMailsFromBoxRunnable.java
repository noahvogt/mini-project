package com.noahvogt.snailmail.workers;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.noahvogt.snailmail.database.Message;
import com.noahvogt.snailmail.mail.imap.FetchMailsFromBox;
import com.noahvogt.snailmail.mail.imap.IMAPStoreConnector;

import jakarta.mail.Folder;
import jakarta.mail.MessagingException;
import jakarta.mail.Store;

public class FetchMailsFromBoxRunnable implements Runnable {
    private Store connectedStore;
    private String imapHost, username, password, mailJSONString;
    private int imapPort;
    private Folder folder;

    public FetchMailsFromBoxRunnable(String imapHost, int imapPort, String username, String password, Folder folder) {
        this.imapHost = imapHost;
        this.imapPort = imapPort;
        this.username = username;
        this.password = password;
        this.folder = folder;
    }

    @Override
    public void run() {
        try {
            Gson gson = new Gson();
			connectedStore = IMAPStoreConnector.getConncetion(imapHost, imapPort, username, password);
            ArrayList<Message> messageArraylist = FetchMailsFromBox.get(connectedStore, folder, username);
            mailJSONString = gson.toJson(messageArraylist);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
        return;
    }

    public String getMailsJSONString() {
        return mailJSONString;
    }
}
