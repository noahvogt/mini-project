package com.noahvogt.miniprojekt.DataBase;

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

    @Query("DELETE FROM message_table WHERE folder = :folder")
    void deleteFolder(String folder);

    @Delete(entity = Message.class)
    void delete(Message message);

    @Delete(entity = Message.class)
    void deleteAll(String folder);

    @Query("UPDATE message_table SET folder = :folder WHERE id = :id")
    void updateFolder(int id, String folder);

    @Query("UPDATE message_table SET date = :date WHERE id = :id")
    void updateDate(int id, String date);


    @Query("SELECT * FROM message_table ORDER BY id ASC")
    List<Message> getAllMessages();

    /*gets messages all messages ordered by date */
    @Query("SELECT * FROM message_table ORDER BY date ASC")
    LiveData<List<Message>> getDateMessages();

    /* get Draft messages*/
    @Query("SELECT * FROM message_table WHERE folder LIKE 'Draft' ORDER BY date DESC")
    LiveData<List<Message>> getLiveDraftMessages();

    @Query("SELECT * FROM message_table WHERE folder LIKE 'Draft' ORDER BY date DESC")
    List<Message> getDraftMessages();

    /* get Inbox messages*/
    @Query("SELECT * FROM message_table WHERE folder LIKE 'Inbox' ORDER BY date DESC")
    LiveData<List<Message>> getLiveInboxMessages();

    @Query("SELECT * FROM message_table WHERE folder LIKE 'Inbox' ORDER BY date DESC")
    List<Message> getInboxMessages();

    /* get Sent messages*/
    @Query("SELECT * FROM message_table WHERE folder LIKE 'Sent' ORDER BY date DESC")
    LiveData<List<Message>> getLiveSentMessages();

    @Query("SELECT * FROM message_table WHERE folder LIKE 'Sent' ORDER BY date DESC")
    List<Message> getSentMessages();

    /* get Archive messages*/
    @Query("SELECT * FROM message_table WHERE folder LIKE 'Archive' ORDER BY date DESC")
    LiveData<List<Message>> getLiveArchiveMessages();

    @Query("SELECT * FROM message_table WHERE folder LIKE 'Archive' ORDER BY date DESC")
    List<Message> getArchiveMessages();

    /* get Spam messages*/
    @Query("SELECT * FROM message_table WHERE folder LIKE 'Spam' ORDER BY date DESC")
    LiveData<List<Message>> getLiveSpamMessages();

    @Query("SELECT * FROM message_table WHERE folder LIKE 'Spam' ORDER BY date DESC")
    List<Message> getSpamMessages();


}
