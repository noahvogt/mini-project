package com.noahvogt.miniprojekt.workers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.noahvogt.miniprojekt.MainActivity.mEmailViewModel;

public class DownloadWorker extends Worker {

    //TODO: upload every data to server

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

                /* init custom gson with hook to parse booleans correctly */
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(Boolean.class, new BooleanTypeAdapter());
                Gson gson = gsonBuilder.create();

                /* safe mail server login credentials */
                MailServerCredentials newMailServerCredentials = new MailServerCredentials(
                        mName, mEmail, mPassword, MailFunctions.getImapHostFromEmail(mEmail), MailFunctions.getSmtpHostFromEmail(mEmail), 993, 587, "");
                String newCredentialsJson = gson.toJson(newMailServerCredentials);
                System.out.println(newCredentialsJson);
                SharedPreferences creds = getApplicationContext().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
                SharedPreferences.Editor credentialsEditor =  creds.edit();
                credentialsEditor.putString("data", newCredentialsJson);
                credentialsEditor.apply();

                /* download all messages from mail server */

                /* read login credentials from SharedPreferences */
                SharedPreferences credentialsReader = getApplicationContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
                String readJsonData = credentialsReader.getString("data", "");
                MailServerCredentials readMailServerCredentials = gson.fromJson(readJsonData, MailServerCredentials.class);


                List folders =  MailFunctions.listMailboxes(MailFunctions.getIMAPConnection(newMailServerCredentials.getImapHost(),
                        newMailServerCredentials.getUsername(), newMailServerCredentials.getPassword(), newMailServerCredentials.getImapPort()));

                if (mEmailViewModel.getAll(false).size() > 0) {
                    for (int delete = 0; delete < mEmailViewModel.getAll(false).size(); delete++) {
                        mEmailViewModel.deleteMessage(mEmailViewModel.getAll(false).get(delete));
                    }
                }
                mEmailViewModel.getAll(true);

                for (int i = 0; i < folders.size(); i++) {
                    String folderName;
                    String folderNow = folders.get(i).toString();
                    System.out.println("ALL Folders: " + folders);

                    if (folderNow.equals("Inbox") || folderNow.equals("IBOX") || folderNow.equals("inbox")){
                        folderName = "Inbox";
                    }else if (folderNow.equals("Trash") || folderNow.equals("TRASH") ||
                            folderNow.equals("Delete") || folderNow.equals("DELETE") || folderNow.equals("Papierkorb")){
                        folderName = "Delete";
                    } else if (folderNow.equals("Sent") ||folderNow.equals("SENT") || folderNow.equals("Gesendete Objekte")){
                        folderName = "Sent";
                    } else if (folderNow.equals("Archive") || folderNow.equals("ARCHIVE") || folderNow.equals("Archiv/2021") ||
                            folderNow.equals("Archiv/2022") || folderNow.equals("Archiv/2020") ){
                        folderName = "Archive";
                    } else if (folderNow.equals("Draft") || folderNow.equals("Drafts") || folderNow.equals("DRAFT") ||
                            folderNow.equals("DRAFTS") || folderNow.equals("Entw&APw-rfe")  )  {
                        folderName = "Draft";
                    } else if (folderNow.equals("Spam") || folderNow.equals("SPAM") || folderNow.equals("Bulk Mail")  ||
                            folderNow.equals("DRAFTS")){
                        folderName = "Spam";
                    }
                    else {
                        folderName = folders.get(i).toString();
                    }

                    //mEmailViewModel.deleteFolder(folders.get(i).toString());
                    /* fetch and print draft messages */
                    String fetchedMails = MailFunctions.fetchMailsFromBox(MailFunctions.getIMAPConnection(newMailServerCredentials.getImapHost(),
                            newMailServerCredentials.getUsername(), newMailServerCredentials.getPassword(), newMailServerCredentials.getImapPort()),
                            folders.get(i).toString(), folderName);
                    //System.out.println("Folder: " + folders.get(i).toString()+ "\n Foldersize: " + folders.size());
                    //System.out.println("Fetched Mails from Folder " + folders.get(i).toString() + ": \n"+ fetchedMails + "\n MAANCBJC");

                    /* parse messages in arraylist of Message class and loop through it */
                    Type messageType = new TypeToken<ArrayList<Message>>() {
                    }.getType();
                    ArrayList<Message> messages = gson.fromJson(fetchedMails, messageType);
                    for (int k = 0; k < messages.size(); k++) {
                        Message message = messages.get(k);
                        //System.out.println("oldDate: " + message.getDate());
                        SimpleDateFormat rawDate = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
                        SimpleDateFormat date = new SimpleDateFormat("dd.MM.yy");
                        Date middleDate = rawDate.parse(message.getDate());
                        String newDate = date.format(middleDate);
                        //System.out.println("middle Date: " + middleDate);
                        //System.out.println("New Date: " + newDate);

                        mEmailViewModel.insert(message);
                        //TODO: make it work
                        /*dosent wrok idk why*/
                        mEmailViewModel.updateDate(message.getId(), newDate);
                        //System.out.println("Folder: " + folders.get(i).toString() + "\n IMPORTANT FOLDERS");
                        //System.out.println("Size of this Folder: " + messages.size());
                    }

                }





                return Result.success();
            } catch (Throwable throwable){
                Log.e(TAG, "Error, downloading Messages", throwable);
                return Result.failure();
            }

    }
}
