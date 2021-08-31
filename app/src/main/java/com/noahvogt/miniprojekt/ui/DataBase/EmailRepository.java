package com.noahvogt.miniprojekt.ui.DataBase;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class EmailRepository {

    private MessageDao messageDao;
    private LiveData<List<Message>> mAllMessage;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    /*get all messages sorted by date out of Database*/
    public EmailRepository(Application application) {
        EmailRoomDatabase db = EmailRoomDatabase.getDatabase(application);
        messageDao = db.messageDao();
        mAllMessage = messageDao.getDateMessages();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    /* returns all messages sorted by date */
    public LiveData<List<Message>> getDraftMessages() {
        return mAllMessage;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Message message) {
        EmailRoomDatabase.databaseWriteExecutor.execute(() -> {
            messageDao.insert(message);
        });
    }
}
