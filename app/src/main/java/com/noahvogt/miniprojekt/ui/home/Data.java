package com.noahvogt.miniprojekt.ui.home;

import java.util.ArrayList;

public class Data {
    private String mName;
    private String mdate;
    private String mBetreff;
    private String mBegin;


    public Data(String name, String date, String begin, String betreff) {
        mName = name;
        mdate = date;
        mBegin = begin;
        mBetreff = betreff;


    }

    public String getName() {
        return mName;
    }
    public String getDate(){return mdate;}
    public String getBetreff(){return mBetreff;}
    public String getBegin(){return mBegin;}



    private static int lastContactId = 0;

    public static ArrayList<Data> createContactsList(int numContacts) {
        ArrayList<Data> contacts = new ArrayList<Data>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new Data("Person " + ++lastContactId, "14.04.2021", "This email begins with..",
                    "My Betreff is no that god"));
        }

        return contacts;
    }
}