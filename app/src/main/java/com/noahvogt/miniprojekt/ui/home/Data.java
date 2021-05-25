package com.noahvogt.miniprojekt.ui.home;

import java.util.ArrayList;

public class Data {
    private String mName;
    private String mdate;


    public Data(String name, String date) {
        mName = name;
        mdate = date;

    }

    public String getName() {
        return mName;
    }
    public String getDate(){return mdate;}



    private static int lastContactId = 0;

    public static ArrayList<Data> createContactsList(int numContacts) {
        ArrayList<Data> contacts = new ArrayList<Data>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new Data("Person " + ++lastContactId, "14.04.2021"));
        }

        return contacts;
    }
}