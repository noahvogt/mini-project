/* Note: this file is derived from a template located in the following repository:
 * https://github.com/googlecodelabs/android-room-with-a-view
 *
 * This means was originally licensed under the Apache License 2.0, which can be found
 * in the text file 'APL' in the root directory of this repository. Any changes made to
 * this file however are licensed under the GNU General Public License Version 3.0,
 * which can be found in the file 'LICENSE' in the root directory of this repository.
 */

package com.noahvogt.snailmail.database;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "message_table")
public class Message {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    @ColumnInfo(name = "user")
    private String mUser;

    /* name of Columm */
    @ColumnInfo(name = "to")
    @SerializedName("to")
    private String mTo;

    @ColumnInfo(name = "cc")
    @SerializedName("cc")
    private String mCc;

    @ColumnInfo(name = "bcc")
    @SerializedName("bcc")
    private String mBcc; // blind carpet copy, not see who sees mails

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

    public String getUser() {
        return this.mUser;
    }

    public String getTo() {
        return this.mTo;
    }

    public String getFrom() {
        return this.mFrom;
    }

    public String getCc() {
        return this.mCc;
    }

    public String getBcc() {
        return this.mBcc;
    }

    public String getDate() {
        return this.mDate;
    }

    public String getSubject() {
        return this.mSubject;
    }

    public String getTextContent() {
        return this.mTextContent;
    }

    public String getFolder() {
        return this.mFolder;
    }

    public int getId() {
        return this.id;
    }

    public boolean isSeen() {
        return this.mSeen;
    }

    public void putUser(String user) {
        this.mUser = user;
    }

    public void putDate(String date) {
        this.mDate = date;
    }

    public Message(@NonNull String user, String to, String cc, String bcc, @NonNull String from,
            @NonNull String date, String subject, String textContent, @NonNull String folder,
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
        this.mUser = user;
    }

}
