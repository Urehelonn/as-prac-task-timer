package com.example.tasktimer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements CursorRecyclerViewAdapter.OnTaskClickListener {

    private static final String TAG = "MainActivity";
    // landscape mode provide not only the task list, task detail as well
    private boolean mTwoPane = false;

    private static final String TASK_EDIT_FRAGMENT = "TaskEditFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.task_detail_container) != null) {
            mTwoPane = true;
        }
//        String[] projection = {
//                TasksContract.Columns._ID,
//                TasksContract.Columns.TASKS_NAME,
//                TasksContract.Columns.TASKS_DESCRIPTION,
//                TasksContract.Columns.TASKS_SORTORDER
//        };
//        ContentResolver contentResolver = getContentResolver();

        /*
            **** Example of insertion:
                ContentValues values = new ContentValues();
                values.put(TasksContract.Columns.TASKS_NAME, "Insertion.");
                values.put(TasksContract.Columns.TASKS_DESCRIPTION, "Insertion test.");
                values.put(TasksContract.Columns.TASKS_SORTORDER, 99);
                int count = contentResolver.delete(TasksContract.buildTaskUri(4), null, null);
         */

        /*
            **** Example of deletion:
                ContentValues values = new ContentValues();
                int count = contentResolver.delete(TasksContract.buildTaskUri(4), null, null);
         */

        /*
            **** Example of update multiple:
                String selection = TasksContract.Columns.TASKS_SORTORDER + " = " + 2;
                int count = contentResolver.update(TasksContract.CONTENT_URI, values, selection, null);
        */

        /*
            **** Example of usage for args[] param:
                ContentValues values = new ContentValues();
                values.put(TasksContract.Columns.TASKS_DESCRIPTION, "For deletion.");
                String selection = TasksContract.Columns.TASKS_SORTORDER + " = ?";
                String[] args = {"99"};
                int count = contentResolver.update(TasksContract.CONTENT_URI, values, selection, args);
         */

//        Cursor cursor = contentResolver.query(
//                TasksContract.CONTENT_URI,
//                projection, null, null,
//                TasksContract.Columns.TASKS_SORTORDER
//        );
//
//        if (cursor != null) {
//            Log.d(TAG, "onCreate: number of rows: " + cursor.getCount());
//            while (cursor.moveToNext()) {
//                for (int i = 0; i < cursor.getColumnCount(); i++) {
//                    Log.d(TAG, "onCreate: " + cursor.getColumnName(i) + ":" + cursor.getString(i));
//                }
//                Log.d(TAG, "onCreate: get records ====================================");
//            }
//            cursor.close();
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_addTask:
                taskEditRequest(null);
                break;

            case R.id.menu_info:
                break;

            case R.id.menu_showDurations:
                break;

            case R.id.menu_settings:
                break;

            case R.id.menu_tester:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEditClick(Task task) {
        taskEditRequest(task);
    }

    @Override
    public void onDeleteClick(Task task) {
        getContentResolver().delete(TasksContract.buildTaskUri(task.getmId()), null, null);
    }

    private void taskEditRequest(Task task) {
//        Log.d(TAG, "taskEditRequest: starts ----------------------------");
        if (mTwoPane) {
            Log.d(TAG, "taskEditRequest: in two-pane mode (landscape)");
            TaskEditActivityFragment frag = new TaskEditActivityFragment();
            // extract task information to place into task detail container
            Bundle arguments = new Bundle();
            arguments.putSerializable(Task.class.getSimpleName(), task);
            frag.setArguments(arguments);

            FragmentManager fManager = getSupportFragmentManager();
            FragmentTransaction ft = fManager.beginTransaction();
            ft.replace(R.id.task_detail_container, frag);
            ft.commit();
        } else {
            Log.d(TAG, "taskEditRequest: in single-pane mode(portrait)");
            // if single-pane mode switch activity
            Intent detailIntent = new Intent(this, TaskEditActivity.class);
            if (task != null) {
                // edit task
                detailIntent.putExtra(Task.class.getSimpleName(), task);
            }
            // else add new task
            startActivity(detailIntent);
        }
//        Log.d(TAG, "taskEditRequest: done ----------------------------");
    }
}
