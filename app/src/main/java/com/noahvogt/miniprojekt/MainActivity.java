package com.noahvogt.miniprojekt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.noahvogt.miniprojekt.ui.home.CustomAdapter;
import com.noahvogt.miniprojekt.ui.home.Data;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    //imported by simon 2.may from RecyclerView Programm, changed to 23.may Simon to ArrayList<Data>...
    protected ArrayList<Data> data;
    //private static final int DATASET_COUNT = 60; //declares how far recyclerview count

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

        //initDataset();
        // Lookup the recyclerview in activity layou
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        // Initialize contacts
        data = Data.createContactsList(20);
        // Create adapter passing in the sample user data
        CustomAdapter adapter = new CustomAdapter(data);
        // Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(adapter);
        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        // Attach the layout manager to the recycler view
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    //@Override
    //public View onCreateView(LayoutInflater inflater, ViewGroup container,
        //                     Bundle savedInstanceState){
      //  View rootView = inflater.inflate(R.layout.recycler_view_frag, container, false);

      //  RecyclerView recyclerView = findViewById(R.id.recyclerView);
      //  CustomAdapter adapter = new CustomAdapter(data);
      //  recyclerView.setAdapter(adapter);
      //  recyclerView.setLayoutManager(new LinearLayoutManager(this));

       // return rootView;
    //}

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

    //private void initDataset() {
      //  data = new String[DATASET_COUNT];
       // for (int i = 0; i < DATASET_COUNT; i++) {
         //   data[i] = "This is element #" + i;
       // }
    //}
}