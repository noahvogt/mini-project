package com.noahvogt.miniprojekt.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.chaquo.python.PyObject;
import com.noahvogt.miniprojekt.MainActivity;
import com.noahvogt.miniprojekt.data.MailFunctions;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DownloadWorker extends Worker {

    public DownloadWorker(
            @NonNull Context appContext,
            @NonNull WorkerParameters workerParameters){
        super(appContext, workerParameters);
    }

    private static final String TAG = DownloadWorker.class.getSimpleName();


    @NonNull
    @NotNull
    @Override
    public Result doWork() {
            try {
                String mEmail = getInputData().getString(MainActivity.emailData);
                String mPassword = getInputData().getString(MainActivity.passwordData);

                    List folders = MailFunctions.listMailboxes(MailFunctions.getIMAPConnection(MailFunctions.getImapHostFromEmail(mEmail), mEmail, mPassword));
                    System.out.println(folders);
                    for (int i = 0; i < folders.size(); i++) {
                        //showToast(folders.get(i).toString());
                        // TODO: select right folder to store, Synchronization
                        /*gives list of Message Objects/dictionaries */
                        PyObject messages = MailFunctions.fetchMailsFromBox(
                                MailFunctions.getIMAPConnection(
                                        MailFunctions.getImapHostFromEmail(mEmail), mEmail, mPassword),
                                folders.get(i).toString());
                        System.out.println(folders.get(i).toString());
                        System.out.println(messages);
                    }


                return Result.success();
            } catch (Throwable throwable){
                Log.e(TAG, "Error, downloading Messages", throwable);
                return Result.failure();
            }

    }
}
