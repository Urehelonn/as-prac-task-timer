package com.example.tasktimer;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

public class TaskEditActivity extends AppCompatActivity implements TaskEditActivityFragment.OnSaveClicked, AppDialog.DialogEvents {

    private static final String TAG = "TaskEditActivity";
    private static final int DIALOG_ID_CANCEL_EDIT = 1;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG, "onOptionsItemSelected: on home bt pressed");
                TaskEditActivityFragment frag = (TaskEditActivityFragment) getSupportFragmentManager().findFragmentById(R.id.content_task_edit);
                if (frag.canClose()) {
                    return super.onOptionsItemSelected(item);
                } else {
                    showConfirmationDialog();
                    return true;
                }

            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

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

    @Override
    public void onSaveClicked() {
        // close current task edit activity
        finish();
    }

    @Override
    public void onPositiveDialogResult(int dialogId, Bundle args) {
        Log.d(TAG, "onPositiveDialogResult: called");
    }

    @Override
    public void onNegativeDialogResult(int dialogId, Bundle args) {
        Log.d(TAG, "onNegativeDialogResult: called");
        finish();
    }

    @Override
    public void onDialogCancelled(int dialogId) {
        Log.d(TAG, "onDialogCancelled: called");
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: called");
        TaskEditActivityFragment frag = (TaskEditActivityFragment) getSupportFragmentManager().findFragmentById(R.id.content_task_edit);
        if (frag.canClose()) {
            super.onBackPressed();
        } else {
            showConfirmationDialog();
        }
    }

    private void showConfirmationDialog() {
        AppDialog dialog = new AppDialog();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, DIALOG_ID_CANCEL_EDIT);
        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.cancel_edit_message));
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.cancel_positive);
        args.putInt(AppDialog.DIALOG_NEGATIVE_RID, R.string.cancel_negative);

        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), null);
    }
}
