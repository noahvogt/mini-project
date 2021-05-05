package com.noahvogt.miniprojekt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.noahvogt.miniprojekt.ui.home.CustomAdapter;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    //imported by simon 2.may from RecyclerView Programm
    protected String[] data;
    protected CustomAdapter adapter;
    protected RecyclerView recyclerView;

    private static final int DATASET_COUNT = 60; //declares how far recyclerview count


    @Override
    public void onCreate(Bundle savedInstanceState) {
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

        //imported by simon 2.mai
        initDataset(); //initialise the data in data
        recyclerView = findViewById(R.id.recyclerView); //maybe defines view of recyclerView from xml file
        adapter = new CustomAdapter(data); //inserts Array data in Asapter
        recyclerView.setAdapter(adapter); //defines CustomAdapter as Adapter for recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //maybe sets LayoutManager for recyclerView
    }

    /*@Override
    public View onCreatView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.recycler_view_frag)
    }*/

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
    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     * implemented from simon 2.may
     */
    private void initDataset() {
        data = new String[DATASET_COUNT];
        for (int i = 0; i < DATASET_COUNT; i++) {
             data[i] = "This is element #" + i;
        }
    }
}