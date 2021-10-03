package com.noahvogt.miniprojekt.DataBase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Message.class}, version = 1, exportSchema = true)
public abstract class EmailRoomDatabase extends RoomDatabase{

    public abstract MessageDao messageDao();

    /* the INSTANCE can be used by different threads at the same time */
    private static volatile EmailRoomDatabase INSTANCE;

    /* tells room the schema is changed from the version tha is istallend in device
     * is not used */
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE word_table "
                    + "ADD COLUMN last_update INTEGER ");
        }
    };

    /* creating 4 threads */
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static EmailRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            /* synchronize all threads of EmailRoomDatabase */
            synchronized (EmailRoomDatabase.class) {
                if (INSTANCE == null) {
                    /* passes the interface in the Room and deletes old data/schema from device*/
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            EmailRoomDatabase.class, "message_database")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                MessageDao dao = INSTANCE.messageDao();
                dao.deleteAll();


                Message word = new Message("Noah", null , null,
                        "Samuel", "31.09.21", "inboxTest", "I Try my best", "Inbox",
                        true);
                dao.insert(word);

               word = new Message("Noah", null, null,
                       "arldemeier", "bobo", "sentTest", "i could cry", "Sent",
                       true);
               dao.insert(word);

               
               for (Integer n = 1; n < 20; n++) {
                   word = new Message("Simon", null, null,
                           "stefan", "tomorrow", n.toString(), "lets goo", "Sent",
                           true);
                   dao.insert(word);
               }

               /*
               for (int i = 1; i < 20; i++){
                   word = new Message("Simon", null, null,
                           "Hans", "tomorrow", "inbox20", "lets goo","Inbox",
                           true);
                   dao.insert(word);
               }


                word = new Message("Simon", null, null,
                        "Maurice", "yesterday", "ArchiveTest", "lets goo","Archive",
                        true);
                dao.insert(word);

                word = new Message("Simon", null, null,
                        "Lenny", "tomorrow", "SpamTest", "lets goo","Spam",
                        true);
                dao.insert(word);


                */


            });
        }
    };

}


