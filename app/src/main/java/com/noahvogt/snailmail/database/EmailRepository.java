/* Note: this file is derived from a template located in the following repository:
 * https://github.com/googlecodelabs/android-room-with-a-view
 * 
 * This means was originally licensed under the Apache License 2.0, which can be found
 * in the text file 'APL' in the root directory of this repository. Any changes made to
 * this file however are licensed under the GNU General Public License Version 3.0,
 * which can be found in the file 'LICENSE' in the root directory of this repository.
 */

package com.noahvogt.snailmail.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import static com.noahvogt.snailmail.MainActivity.userGlobal;

public class EmailRepository {

    private MessageDao messageDao;
    private final LiveData<List<Message>> mDraftLiveMessage;
    private LiveData<List<Message>> mInboxLiveMessage;
    private LiveData<List<Message>> mSentLiveMessage;
    private LiveData<List<Message>> mArchiveLiveMessage;
    private LiveData<List<Message>> mSpamLiveMessage;

    private List<Message> mAllMessages;


    /*get all messages sorted by date out of Database*/
    public EmailRepository(Application application) {
        EmailRoomDatabase db = EmailRoomDatabase.getDatabase(application);
        messageDao = db.messageDao();
        mInboxLiveMessage = messageDao.getLiveInboxMessages(userGlobal);
        mDraftLiveMessage = messageDao.getLiveDraftMessages(userGlobal);
        mArchiveLiveMessage = messageDao.getLiveArchiveMessages(userGlobal);
        mSentLiveMessage = messageDao.getLiveSentMessages(userGlobal);
        mSpamLiveMessage = messageDao.getLiveSpamMessages(userGlobal);
    }


    /* returns all messages of with tag Draft */
    public LiveData<List<Message>> getDraftMessages() {
        return mDraftLiveMessage;
    }

    public LiveData<List<Message>> getSpamMessage(){return mSpamLiveMessage;}

    public LiveData<List<Message>> getInboxMessages() {
        return mInboxLiveMessage;
    }

    public LiveData<List<Message>> getSentMessages() {
        return mSentLiveMessage;
    }

    public LiveData<List<Message>> getArchiveMessages() {
        return mArchiveLiveMessage;
    }

    /*problems with main Thread */
    public List<Message> getAllMessages(){
        return mAllMessages;}



    public void insert(Message message) {
        EmailRoomDatabase.databaseWriteExecutor.execute(() -> {
            messageDao.insert(message);
        });
    }

    public void deleteMessage(final Message message){
        EmailRoomDatabase.databaseWriteExecutor.execute(() -> {
            messageDao.delete(message);
        });
    }

    public void updateFolder(final int id, String folder){
        EmailRoomDatabase.databaseWriteExecutor.execute(() -> {
            messageDao.updateFolder(id ,folder);
        });
    }

    public void updateDate(final int id, final String date){
        EmailRoomDatabase.databaseWriteExecutor.execute(() -> {
            messageDao.updateDate(id, date);
        });
    }
}
