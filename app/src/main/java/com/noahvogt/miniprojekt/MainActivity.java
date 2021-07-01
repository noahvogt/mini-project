package com.noahvogt.miniprojekt;

import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import static com.noahvogt.miniprojekt.R.id.drawer_layout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarConfiguration mAppBarConfiguration;

    private AlertDialog dialog;

    private EditText newemail_name, newemail_email, newemail_password; // may not be private

    SharedPreferences preferences;

    // empty descriptor
    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // define button listeners

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

        // init preferences + simple editor
         preferences = (SharedPreferences) getSharedPreferences("UserPrefrences", Context.MODE_PRIVATE);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(drawer_layout);
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
        getMenuInflater().inflate(R.menu.create_message_options_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // better leave empty to avoid any listener disambiguity
    public void onClick(View view) {}


    public void createNewEmailDialog(){
        // define View window
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View emailPopupView = getLayoutInflater().inflate(R.layout.popup, null);

        // init text field variables
        newemail_name = emailPopupView.findViewById(R.id.popup_material_name_asking_text);
        newemail_email = emailPopupView.findViewById(R.id.popup_material_email_asking_text);
        newemail_password = emailPopupView.findViewById(R.id.popup_material_password_asking_text);

        // init button variables
        Button newemail_save_button = (Button) emailPopupView.findViewById(R.id.saveButton);
        // may not be private
        Button newemail_cancel_button = (Button) emailPopupView.findViewById(R.id.cancelButton);

        // open View window
        dialogBuilder.setView(emailPopupView);
        dialog = dialogBuilder.create();
        dialog.show();


        SharedPreferences.Editor preferencesEditor = preferences.edit();

        // store user input
        newemail_save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // store user input, MAYBE DELETE LATER
                String name = newemail_name.getText().toString();
                String email = newemail_email.getText().toString();
                String password = newemail_password.getText().toString();

                if (!mailFunctions.validateEmail(newemail_email) | !mailFunctions.validateName(newemail_name) | !mailFunctions.validatePassword(newemail_password)) {
                    return;
                }

                // show all strings the user gave, this will later be stored to a secure database and checked for validation
                showToast(name);
                showToast(email);
                showToast(password);
                preferencesEditor.putString("name", name);
                preferencesEditor.putString("email", email);
                preferencesEditor.putString("password", password);
                preferencesEditor.commit();

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