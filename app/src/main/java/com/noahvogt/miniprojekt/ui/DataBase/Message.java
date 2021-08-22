package com.noahvogt.miniprojekt.ui.DataBase;

public class Message {

    private int id = 0;
    private int account = 0;
    private String to ;
    private String cc;
    private String bcc; //blind carpet copy, not see who sees mails
    private String from;
    private String date;
    private String betreff;
    private int attachment = 0;
    private boolean seen = true;

    public int getId(){return id;}

    public int getAccount(){return account;}

    public String getTo(){return to;}

    public String getCc(){return cc;}

    public String getBcc(){return bcc;}

    public  String getFrom(){return from;}

    public String getDate(){return date;}

    public String getBetreff(){return betreff;}

    public int getAttachment(){return attachment;}

    public boolean getSeen(){return seen;}

    public void setId(int i){id = i;}

    public void setAccount(int i){account = i;}

    public void setTo(String s){to = s;}

    public void setCc(String s){cc = s;}

    public void setBcc(String s){bcc = s;}

    public void setFrom(String s){from = s;}

    public void setDate(String s){date = s;}

    public void setBetreff(String s){betreff = s;}

    public void setAttachment(int i){ attachment = i;}

    public void setSeen(boolean b){seen = b;}


}
