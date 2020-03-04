package com.example.tasktimer;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;

public class TaskEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // need to get bundle and pass to fragment
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TaskEditActivityFragment taskEditActivityFragment = new TaskEditActivityFragment();

        // use Bundle to store task information to pass to fragment
        // the task already in bundle from mainActivity
        Bundle args = getIntent().getExtras();
        taskEditActivityFragment.setArguments(args);
        // those bundle information will be retrieved by fragment_task_edit onCreate next

        // add fragment into container
        fragmentTransaction.replace(R.id.content_task_edit, taskEditActivityFragment);
        fragmentTransaction.commit();
    }

}
