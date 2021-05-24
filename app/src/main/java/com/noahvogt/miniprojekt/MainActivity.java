package com.noahvogt.miniprojekt;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.webkit.WebHistoryItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

// regex utils for email string validation
import android.util.Patterns;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private EditText newemail_name, newemail_email, newemail_password; // may not be private
    private Button newemail_save_button, newemail_cancel_button; // may not be private


    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void onClick(View view) {
        // TODO: remove future button action ambiguity
        createNewEmailDialog();
    }


    public void createNewEmailDialog(){
        // define View window
        dialogBuilder = new AlertDialog.Builder(this);
        final View emailPopupView = getLayoutInflater().inflate(R.layout.popup, null);

        // init text field variables
        newemail_name = emailPopupView.findViewById(R.id.popup_material_name_asking_text);
        newemail_email = emailPopupView.findViewById(R.id.popup_material_email_asking_text);
        newemail_password = emailPopupView.findViewById(R.id.popup_material_password_asking_text);

        // init button variables
        newemail_save_button = (Button) emailPopupView.findViewById(R.id.saveButton);
        newemail_cancel_button = (Button) emailPopupView.findViewById(R.id.cancelButton);

        // open View window
        dialogBuilder.setView(emailPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        // store user input
        newemail_save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // store user input (only needed for DEBUGGING)
                String name = newemail_name.getText().toString();
                String email = newemail_email.getText().toString();
                String password = newemail_password.getText().toString();

                if (!validateEmail() | !validateName() | !validatePassword()) {
                    return;
                }

                // show all strings the user gave, this will later be stored to a secure database and checked for validation
                showToast(name);
                showToast(email);
                showToast(password);


                showSnackbar(emailPopupView,"save button clicked");
            }
        });

        newemail_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // define save button here
                dialog.dismiss();
            }
        });

    }

    // TODO: resolve password endIcon conflict
    private boolean validateName() {
        String name = newemail_name.getText().toString().trim();

        if (name.isEmpty()) {
            newemail_name.setError("Field can't be empty");
            return false;
        } else if (name.length() > 50) {
            newemail_name.setError("Name too long");
            return false;
        } else {
            newemail_name.setError(null);
            return true;
        }
    }

    // TODO: resolve password endIcon conflict
    private boolean validateEmail() {
        String email = newemail_email.getText().toString().trim();

        if (email.isEmpty()) {
            newemail_email.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            newemail_email.setError("Please enter a valid email address");
            return false;
        } else {
            newemail_email.setError(null);
            return true;
        }
    }

    // TODO: resolve password endIcon conflicts
    private boolean validatePassword() {
        String password = newemail_password.getText().toString().trim();

        if (password.isEmpty()) {
            newemail_password.setError("Field can't be empty");
            return false;
        } else {
            newemail_password.setError(null);
            return true;
        }
    }

    // show debug output in  specific view
    private void showSnackbar(View View, String text) {
        Snackbar.make(View, text, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    // like showSnackbar(), but global and uglier
    private void showToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}