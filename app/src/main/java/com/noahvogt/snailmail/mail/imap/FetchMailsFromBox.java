package com.noahvogt.snailmail.mail.imap;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.noahvogt.snailmail.database.Message;

import jakarta.mail.Address;
import jakarta.mail.Flags;
import jakarta.mail.Folder;
import jakarta.mail.MessagingException;
import jakarta.mail.Store;
import jakarta.mail.internet.MimeMessage.RecipientType;

public class FetchMailsFromBox {
    public static ArrayList<Message> get(Store imapSessionStore, Folder folder, String username)
            throws MessagingException {
        ArrayList<Message> messageArraylist = new ArrayList<Message>();
        folder.open(Folder.READ_ONLY);
        jakarta.mail.Message messages[] = folder.getMessages();
        int messageNumber = 0;
        for (jakarta.mail.Message message : messages) {
            messageNumber++;
            System.out.println(messageNumber);
            Message newMessage = new Message(username, convertAddressArraytoJSON(message.getRecipients(RecipientType.TO)), convertAddressArraytoJSON(message.getRecipients(RecipientType.CC)), convertAddressArraytoJSON(message.getRecipients(RecipientType.BCC)), convertAddressArraytoJSON(message.getFrom()), message.getSentDate().toString(), message.getSubject(), message.getContentType(), folder.getFullName(), message.isSet(Flags.Flag.SEEN));
            messageArraylist.add(newMessage);
        }

        return messageArraylist;
    }

    public static String convertAddressArraytoJSON(Address[] addressArray) {
        Gson gson = new Gson();
        int length = (addressArray == null) ? 0 : addressArray.length;
        String[] stringArray = new String[length];

        for (int i = 0; i < length; i++) {
            stringArray[i] = addressArray[i].toString();
        }

        return gson.toJson(stringArray);
    }
}
