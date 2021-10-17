/* Note: this file is derived from a template located in the following repository:
 * https://github.com/googlecodelabs/android-room-with-a-view
 * 
 * This means was originally licensed under the Apache License 2.0, which can be found
 * in the text file 'APL' in the root directory of this repository. Any changes made to
 * this file however are licensed under the GNU General Public License Version 3.0,
 * which can be found in the file 'LICENSE' in the root directory of this repository.
 */

package com.noahvogt.snailmail.DataBase;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Message.class}, version = 1, exportSchema = true)
public abstract class EmailRoomDatabase extends RoomDatabase{

    public abstract MessageDao messageDao();

    /* the INSTANCE can be used by different threads at the same time */
    private static volatile EmailRoomDatabase INSTANCE;

    /* tells room the schema is changed from the version that is installed in device
     * is not used */
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE word_table "
                    + "ADD COLUMN last_update INTEGER ");
        }
    };

    /* creating n threads */
    private static final int NUMBER_OF_THREADS = 6;
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

            });
        }
    };

}


