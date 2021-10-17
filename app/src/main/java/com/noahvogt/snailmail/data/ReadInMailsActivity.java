package com.noahvogt.snailmail.data;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.noahvogt.snailmail.R;

import java.util.List;

public class ReadInMailsActivity extends AppCompatActivity {

    String mEmail;
    String mPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        mEmail = intent.getStringExtra("Email");
        mPassword = intent.getStringExtra("Password");

        Toast.makeText(getBaseContext(),"Probe Connection ...", Toast.LENGTH_SHORT).show();
        if (MailFunctions.canConnect(MailFunctions.getImapHostFromEmail(mEmail), mEmail, mPassword) == Boolean.TRUE) {
            //showToast("was able to connect");

            List folders = MailFunctions.listMailboxes(MailFunctions.getIMAPConnection(MailFunctions.getImapHostFromEmail(mEmail), mEmail, mPassword, 993));
            for (int i = 0; i < folders.size(); i++) {
                //showToast(folders.get(i).toString());
                // TODO: select right folder to store, Synchronization
                /*gives list of Message Objects/dictionaries */
                //List p = MailFunctions.fetchMailsFromBox(
                //        MailFunctions.getIMAPConnection(
                //                MailFunctions.getImapHostFromEmail(mEmail), mEmail, mPassword),
                //        folders.get(i).toString());
                //System.out.println(folders.get(i).toString());
                //System.out.println(p);
            }
        }

    }
}
