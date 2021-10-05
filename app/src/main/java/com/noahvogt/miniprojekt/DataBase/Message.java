package com.noahvogt.miniprojekt.DataBase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

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
    @SerializedName("to")
    private String mTo ;

    @ColumnInfo(name = "cc")
    @SerializedName("cc")
    private String mCc;

    @ColumnInfo(name = "bcc")
    @SerializedName("bcc")
    private String mBcc; //blind carpet copy, not see who sees mails

    @NonNull
    @ColumnInfo(name = "fromEmail")
    @SerializedName("from")
    private String mFrom;

    @NonNull
    @ColumnInfo(name = "date")
    @SerializedName("date")
    private String mDate;

    @ColumnInfo(name = "subject")
    @SerializedName("subject")
    private String mSubject;

    @ColumnInfo(name = "textContent")
    @SerializedName("content")
    private String mTextContent;

    @NonNull
    @ColumnInfo(name = "folder")
    @SerializedName("folder")
    private String mFolder;

    @ColumnInfo(name = "seen")
    @SerializedName("seen")
    private boolean mSeen;

    public String getTo(){return this.mTo;}

    public String getFrom(){return this.mFrom;}

    public String getCc(){return this.mCc;}

    public String getBcc(){return this.mBcc;}

    public String getDate(){return this.mDate;}

    public String getSubject(){return this.mSubject;}

    public String getTextContent(){return this.mTextContent;}

    public String getFolder(){return this.mFolder;}

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
            @NonNull String folder,
            @NonNull boolean seen) {
        this.mTo = to;
        this.mFrom = from;
        this.mCc = cc;
        this.mBcc = bcc;
        this.mDate = date;
        this.mSubject = subject;
        this.mTextContent = textContent;
        this.mFolder = folder;
        this.mSeen = seen;
    }

}
