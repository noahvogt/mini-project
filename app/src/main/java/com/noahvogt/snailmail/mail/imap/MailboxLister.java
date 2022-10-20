package com.noahvogt.snailmail.mail.imap;

import jakarta.mail.Folder;
import jakarta.mail.MessagingException;
import jakarta.mail.NoSuchProviderException;
import jakarta.mail.Store;

public class MailboxLister {

    public static Folder[] list(Store imapStoreConnection, boolean considerSubfolders)
            throws NoSuchProviderException, MessagingException {
        /*
         * this returns all subfolders. when only wishing to get the top-level folders,
         * use .list() without arguments
         */
        String searchString = considerSubfolders ? "*" : "";

        return imapStoreConnection.getDefaultFolder().list(searchString);
    }
}
