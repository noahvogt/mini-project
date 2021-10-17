package com.noahvogt.snailmail;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.noahvogt.snailmail.R;
import com.noahvogt.snailmail.SettingsFragment;
import com.noahvogt.snailmail.ui.show.MessageShowFragment;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // below line is to change
        // the title of our action bar.
        Objects.requireNonNull(getSupportActionBar()).setTitle("Settings");

        // below line is used to check if
        // frame layout is empty or not.
        if (findViewById(R.id.idFrameLayout) != null) {
            if (savedInstanceState != null) {
                return;
            }
            // below line is to inflate our fragment.
            getSupportFragmentManager().beginTransaction().add(R.id.idFrameLayout, new SettingsFragment()).commit();
        }

        if (findViewById(R.id.show_Layout) != null){
            getSupportFragmentManager().beginTransaction().add(R.id.show_Layout, new MessageShowFragment()).commit();
        }
    }
}
