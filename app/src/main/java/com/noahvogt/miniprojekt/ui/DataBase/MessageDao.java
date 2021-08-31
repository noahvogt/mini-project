package com.noahvogt.miniprojekt.ui.DataBase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MessageDao {

    /*

    WordDao is an interface; DAOs must either be interfaces or abstract classes.
    The @Dao annotation identifies it as a DAO class for Room.
    void insert(Word word); Declares a method to insert one word:
    The @Insert annotation is a special DAO method annotation where you don't have to provide any SQL! (There are also @Delete and @Update annotations for deleting and updating rows, but you are not using them in this app.)
    onConflict = OnConflictStrategy.IGNORE: The selected on conflict strategy ignores a new word if it's exactly the same as one already in the list. To know more about the available conflict strategies, check out the documentation.
    deleteAll(): declares a method to delete all the words.
    There is no convenience annotation for deleting multiple entities, so it's annotated with the generic @Query.
    @Query("DELETE FROM word_table"): @Query requires that you provide a SQL query as a string parameter to the annotation.
    List<Word> getAlphabetizedWords(): A method to get all the words and have it return a List of Words.
    @Query("SELECT * FROM word_table ORDER BY word ASC"): Returns a list of words sorted in ascending order.

     */

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Message message);

    @Query("DELETE FROM message_table")
    void deleteAll();

    @Query("SELECT * FROM message_table")
    LiveData<List<Message>> getAllMessages();

    @Query("SELECT * FROM message_table ORDER BY date ASC")
    LiveData<List<Message>> getDateMessages();

    /* selects just from, subject and date from word_table */

    /* @Query("SELECT fromEmail, date, subject, textContent FROM message_table")
    LiveData<List<Message>> getRecyclerviewData();
     */

}
