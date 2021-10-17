package com.noahvogt.snailmail.DataBase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MessageDao {

    // TODO: updating messages, search funktion, read in cc and bcc as list
    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Message message);

    @Delete(entity = Message.class)
    void delete(Message message);

    @Query("UPDATE message_table SET folder = :folder WHERE id = :id")
    void updateFolder(int id, String folder);

    @Query("UPDATE message_table SET date = :date WHERE id = :id")
    void updateDate(int id, String date);

    /* get Draft messages*/
    @Query("SELECT * FROM message_table WHERE user = :user AND folder LIKE 'Draft' ORDER BY date DESC")
    LiveData<List<Message>> getLiveDraftMessages(String user);

    /* get Inbox messages*/
    @Query("SELECT * FROM message_table WHERE user = :user AND folder LIKE 'Inbox' ORDER BY date DESC")
    LiveData<List<Message>> getLiveInboxMessages(String user);


    /* get Sent messages*/
    @Query("SELECT * FROM message_table WHERE user = :user AND folder LIKE 'Sent' ORDER BY date DESC")
    LiveData<List<Message>> getLiveSentMessages(String user);

    /* get Archive messages*/
    @Query("SELECT * FROM message_table WHERE user = :user AND folder LIKE 'Archive' ORDER BY date DESC")
    LiveData<List<Message>> getLiveArchiveMessages(String user);

    /* get Spam messages*/
    @Query("SELECT * FROM message_table WHERE user = :user AND folder LIKE 'Spam' ORDER BY date DESC")
    LiveData<List<Message>> getLiveSpamMessages(String user);




}
