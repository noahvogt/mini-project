package com.noahvogt.miniprojekt.ui.home;

import java.util.ArrayList;

public class Data {
    private String mName;
    private boolean mOnline;

    public Data(String name, boolean online) {
        mName = name;
        mOnline = online;
    }

    public String getName() {
        return mName;
    }

    public boolean isOnline() {
        return mOnline;
    }

    private static int lastContactId = 0;

    public static ArrayList<Data> createContactsList(int numContacts) {
        ArrayList<Data> contacts = new ArrayList<Data>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new Data("Person " + ++lastContactId, i <= numContacts / 2));
        }

        return contacts;
    }
}