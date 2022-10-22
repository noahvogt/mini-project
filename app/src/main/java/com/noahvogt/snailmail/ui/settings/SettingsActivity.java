package com.noahvogt.snailmail.ui.settings;

import java.util.Objects;

import com.noahvogt.snailmail.R;
import com.noahvogt.snailmail.ui.reader.ReaderFragment;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        /* change the title of our action bar */
        Objects.requireNonNull(getSupportActionBar()).setTitle("Settings");

        /* if frame layout is empty or not. */
        if (findViewById(R.id.idFrameLayout) != null) {
            if (savedInstanceState != null) {
                return;
            }
            /* inflate our fragment */
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.idFrameLayout, new SettingsFragment()).commit();
        }

        if (findViewById(R.id.show_Layout) != null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.show_Layout, new ReaderFragment()).commit();
        }
    }
}
