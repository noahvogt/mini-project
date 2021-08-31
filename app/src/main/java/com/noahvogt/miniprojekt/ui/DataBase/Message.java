package com.noahvogt.miniprojekt.ui.DataBase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/* @Embedded to express entitys together
 *  https://developer.android.com/training/data-storage/room/relationships
 * to learn more */

@Entity(tableName = "message_table")
public class Message {

    @PrimaryKey (autoGenerate = true)
    public int id;

    /* name of Columm */
    @NonNull
    @ColumnInfo(name = "to")
    private String mTo ;


    @ColumnInfo(name = "cc")
    private String mCc;

    @ColumnInfo(name = "bcc")
    private String mBcc; //blind carpet copy, not see who sees mails

    @NonNull
    @ColumnInfo(name = "fromEmail")
    private String mFrom;

    @NonNull
    @ColumnInfo(name = "date")
    private String mDate;

    @ColumnInfo(name = "subject")
    private String mSubject;

    @ColumnInfo(name = "textContent")
    private String mTextContent;

    @ColumnInfo(name = "seen")
    private boolean mSeen;

    public String getTo(){return this.mTo;}

    public String getFrom(){return this.mFrom;}

    public String getCc(){return this.mCc;}

    public String getBcc(){return this.mBcc;}

    public String getDate(){return this.mDate;}

    public String getSubject(){return this.mSubject;}

    public String getTextContent(){return this.mTextContent;}

    public int getId(){return this.id;}

    public boolean isSeen() {return this.mSeen;}

    public Message(
            @NonNull String to,
            String cc,
            String bcc,
            @NonNull String from,
            @NonNull String date,
            String subject,
            String textContent,
            @NonNull boolean seen) {
        this.mTo = to;
        this.mFrom = from;
        this.mCc = cc;
        this.mBcc = bcc;
        this.mDate = date;
        this.mSubject = subject;
        this.mTextContent = textContent;
        this.mSeen = seen;
    }

}
