package com.noahvogt.miniprojekt.DataBase;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class EmailRepository {

    private MessageDao messageDao;
    private final LiveData<List<Message>> mDraftMessage;
    private LiveData<List<Message>> mInboxMessage;
    private LiveData<List<Message>> mSentMessage;
    private LiveData<List<Message>> mArchiveMessage;
    private LiveData<List<Message>> mSpamMessage;


    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    /*get all messages sorted by date out of Database*/
    public EmailRepository(Application application) {
        EmailRoomDatabase db = EmailRoomDatabase.getDatabase(application);
        messageDao = db.messageDao();
        mInboxMessage = messageDao.getInboxMessages();
        mDraftMessage = messageDao.getDraftMessages();
        mArchiveMessage = messageDao.getArchiveMessages();
        mSentMessage = messageDao.getSentMessages();
        mSpamMessage = messageDao.getSpamMessages();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    /* returns all messages of with tag Draft */
    public LiveData<List<Message>> getDraftMessages() {
        return mDraftMessage;
    }

    public LiveData<List<Message>> getSpamMessage(){return mSpamMessage;}

    public LiveData<List<Message>> getInboxMessages() {
        return mInboxMessage;
    }

    public LiveData<List<Message>> getSentMessages() {
        return mSentMessage;
    }

    public LiveData<List<Message>> getArchiveMessages() {
        return mArchiveMessage;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Message message) {
        EmailRoomDatabase.databaseWriteExecutor.execute(() -> {
            messageDao.insert(message);
        });
    }

    public void deleteMessage(final Message message){
       /* new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                db.MessageDao().deleteMessage(message);
                return null;
            }
        }.execute();

        */
        EmailRoomDatabase.databaseWriteExecutor.execute(() -> {
            messageDao.delete(message);
        });
    }

    public void updateMessage(final Message message){
        EmailRoomDatabase.databaseWriteExecutor.execute(() -> {
            messageDao.updateMessage(message);
        });
    }
}
