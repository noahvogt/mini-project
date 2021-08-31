package com.noahvogt.miniprojekt;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.noahvogt.miniprojekt.ui.home.CustomAdapter;
import com.noahvogt.miniprojekt.ui.home.Data;
import com.noahvogt.miniprojekt.ui.home.SettingsActivity;
import com.noahvogt.miniprojekt.ui.home.SettingsFragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;


import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.noahvogt.miniprojekt.ui.home.CustomAdapter;
import com.noahvogt.miniprojekt.ui.home.Data;

import java.util.ArrayList;
import java.util.List;

import com.chaquo.python.android.PyApplication;

import com.noahvogt.miniprojekt.mailFunctions;

import static com.noahvogt.miniprojekt.R.id.drawer_layout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarConfiguration mAppBarConfiguration;

    /* declare vars that should always be used / shown by default */
    protected ArrayList<Data> data;
    private AlertDialog dialog;
    private EditText newemail_name, newemail_email, newemail_password; /* may not be private */

    private EditText newemail_name, newemail_email, newemail_password; // may not be private

    /* empty descriptor */
    public MainActivity() {
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        if (! Python.isStarted()) {
           Python.start(new AndroidPlatform(this));
        }
         define button listeners
        */

        Button add_email_button = (Button) findViewById(R.id.addEmailButton);
        add_email_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewEmailDialog();
            }
        });

        ImageButton message_create_button = (ImageButton) findViewById(R.id.messageButton);
        message_create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = messageCreateFragment.newInstance();
                dialog.show(getSupportFragmentManager(), "tag");
            }
        });

        /* invoke toolbar */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* invoke drawer */
        DrawerLayout drawer = findViewById(drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        /*
         Passing each menu ID as a set of Ids because each
         menu should be considered as top level destinations.
        */
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        /* invoke recycleViewer */

        //initDataset();
        /* Lookup the recyclerview in activity layout */
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        /* Initialize contacts */
        data = Data.createContactsList(20);
        /* Create adapter passing in the sample user data */
        CustomAdapter adapter = new CustomAdapter(data);
        /* Attach the adapter to the recyclerview to populate items */
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //
        Button settingButton = findViewById(R.id.settingsButton);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Inflate the menu; this adds items to the action bar if it is present. */
        getMenuInflater().inflate(R.menu.create_message_options_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    /* better leave empty to avoid any listener disambiguity */
    public void onClick(View view) {}


    public void createNewEmailDialog(){
        /* define View window */
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View emailPopupView = getLayoutInflater().inflate(R.layout.popup, null);

        /* init text field variables */
        newemail_name = emailPopupView.findViewById(R.id.popup_material_name_asking_text);
        newemail_email = emailPopupView.findViewById(R.id.popup_material_email_asking_text);
        newemail_password = emailPopupView.findViewById(R.id.popup_material_password_asking_text);

        /* init button variables */
        Button newemail_save_button = (Button) emailPopupView.findViewById(R.id.saveButton);
        /* may not be private */
        Button newemail_cancel_button = (Button) emailPopupView.findViewById(R.id.cancelButton);


        /* open View window */
            dialogBuilder.setView(emailPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        /* store user input */
        newemail_save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* store user input (only needed for DEBUGGING) */
                String name = newemail_name.getText().toString();
                String email = newemail_email.getText().toString();
                String password = newemail_password.getText().toString();

                if (!mailFunctions.validateEmail(newemail_email) | !mailFunctions.validateName(newemail_name) | !mailFunctions.validatePassword(newemail_password)) {
                    return;
                }

                /* connect to mail server and print various debugging output */
                if (mailFunctions.canConnect(name, email, password) == Boolean.TRUE) {
                    showToast("was able to connect");
                    List l =  mailFunctions.listMailboxes(mailFunctions.getIMAPConnection(name, email, password));
                    for (int i = 0; i < l.size(); i++) {
                        showToast(l.get(i).toString());
                    }
                } else {
                    showToast("failed to connect");

                    /* show all strings the user gave, this will later be stored to a secure database and checked for validation */
                    showToast(name);
                    showToast(email);
                    showToast(password);
                }


            }
        });

        newemail_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    /* show debug output in  specific view */
    private void showSnackbar(View View, String text) {
        Snackbar.make(View, text, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    /* like showSnackbar(), but global and uglier */
    private void showToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }


}
