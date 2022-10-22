/* Note: this file is derived from a template located in the following repository:
 * https://github.com/googlecodelabs/android-room-with-a-view
 * 
 * This means was originally licensed under the Apache License 2.0, which can be found
 * in the text file 'APL' in the root directory of this repository. Any changes made to
 * this file however are licensed under the GNU General Public License Version 3.0,
 * which can be found in the file 'LICENSE' in the root directory of this repository.
 */

package com.noahvogt.snailmail.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MessageDao {

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

    /* get Draft messages */
    @Query("SELECT * FROM message_table WHERE user = :user AND folder LIKE 'Draft' ORDER BY date DESC")
    LiveData<List<Message>> getLiveDraftMessages(String user);

    /* get Inbox messages */
    @Query("SELECT * FROM message_table WHERE user = :user AND folder LIKE 'Inbox' ORDER BY date DESC")
    LiveData<List<Message>> getLiveInboxMessages(String user);

    /* get Sent messages */
    @Query("SELECT * FROM message_table WHERE user = :user AND folder LIKE 'Sent' ORDER BY date DESC")
    LiveData<List<Message>> getLiveSentMessages(String user);

    /* get Archive messages */
    @Query("SELECT * FROM message_table WHERE user = :user AND folder LIKE 'Archive' ORDER BY date DESC")
    LiveData<List<Message>> getLiveArchiveMessages(String user);

    /* get Spam messages */
    @Query("SELECT * FROM message_table WHERE user = :user AND folder LIKE 'Spam' ORDER BY date DESC")
    LiveData<List<Message>> getLiveSpamMessages(String user);

}
