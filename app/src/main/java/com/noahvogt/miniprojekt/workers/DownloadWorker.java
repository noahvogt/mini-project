package com.noahvogt.miniprojekt.workers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.chaquo.python.PyObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.noahvogt.miniprojekt.DataBase.Message;
import com.noahvogt.miniprojekt.MailServerCredentials;
import com.noahvogt.miniprojekt.MainActivity;
import com.noahvogt.miniprojekt.data.BooleanTypeAdapter;
import com.noahvogt.miniprojekt.data.MailFunctions;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.noahvogt.miniprojekt.MainActivity.mEmailViewModel;
import static com.noahvogt.miniprojekt.MainActivity.newMailServerCredentials;

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
                String mName = getInputData().getString(MainActivity.nameData);

                    /*List folders = MailFunctions.listMailboxes(MailFunctions.getIMAPConnection(MailFunctions.getImapHostFromEmail(mEmail), mEmail, mPassword, 993));
                    System.out.println(folders);
                    for (int i = 0; i < folders.size(); i++) {
                        //showToast(folders.get(i).toString());
                        // TODO: select right folder to store, Synchronization
                        /*gives list of Message Objects/dictionaries */
                        /*String messages = MailFunctions.fetchMailsFromBox(
                                MailFunctions.getIMAPConnection(
                                        MailFunctions.getImapHostFromEmail(mEmail), mEmail, mPassword, 993),
                                folders.get(i).toString());
                        System.out.println(folders.get(i).toString());
                        System.out.println(messages);

                         */

                /* init custom gson with hook to parse booleans correctly */
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(Boolean.class, new BooleanTypeAdapter());
                Gson gson = gsonBuilder.create();

                /* safe mail server login credentials */
                newMailServerCredentials = new MailServerCredentials(
                        mName, mEmail, mPassword, MailFunctions.getImapHostFromEmail(mEmail), MailFunctions.getSmtpHostFromEmail(mEmail), 993, 587, "");
                String newCredentialsJson = gson.toJson(newMailServerCredentials);
                System.out.println(newCredentialsJson);
                MainActivity.credentialsEditor.putString("data", newCredentialsJson);
                MainActivity.credentialsEditor.apply();

                /* download all messages from mail server */

                /* read login credentials from SharedPreferences */
                SharedPreferences credentialsReader = getApplicationContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
                String readJsonData = credentialsReader.getString("data", "");
                MailServerCredentials readMailServerCredentials = gson.fromJson(readJsonData, MailServerCredentials.class);


                /* fetch and print draft messages */
                String fetchedMails = MailFunctions.fetchMailsFromBox(MailFunctions.getIMAPConnection(newMailServerCredentials.getImapHost(),
                        newMailServerCredentials.getUsername(), newMailServerCredentials.getPassword(), newMailServerCredentials.getImapPort()), "INBOX");
                System.out.println("Yaaay" + fetchedMails + "\n MAANCBJC");

                /* parse messages in arraylist of Message class and loop through it */
                Type messageType = new TypeToken<ArrayList<Message>>(){}.getType();
                ArrayList<Message> messages = gson.fromJson(fetchedMails, messageType);
                for (int i = 0; i < messages.size(); i++) {
                    Message message = messages.get(i);
                    mEmailViewModel.insert(message);
                    System.out.println("Message #" + i);
                    System.out.println("Date: " + message.getDate());
                    System.out.println("Subject: " + message.getSubject());
                    System.out.println(messages.get(i).toString());
                }





                return Result.success();
            } catch (Throwable throwable){
                Log.e(TAG, "Error, downloading Messages", throwable);
                return Result.failure();
            }

    }
}
