package com.example.tasktimer;

import android.content.ContentResolver;
import android.content.ContentValues;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String[] projection = {
                TasksContract.Columns._ID,
                TasksContract.Columns.TASKS_NAME,
                TasksContract.Columns.TASKS_DESCRIPTION,
                TasksContract.Columns.TASKS_SORTORDER
        };
        ContentResolver contentResolver = getContentResolver();

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

        Cursor cursor = contentResolver.query(
                TasksContract.CONTENT_URI,
                projection, null, null,
                TasksContract.Columns.TASKS_SORTORDER
        );

        if (cursor != null) {
            Log.d(TAG, "onCreate: number of rows: " + cursor.getCount());
            while (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    Log.d(TAG, "onCreate: " + cursor.getColumnName(i) + ":" + cursor.getString(i));
                }
                Log.d(TAG, "onCreate:====================================");
            }
            cursor.close();
        }
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

        if (id == R.id.menu_settings) {
            return true;
        }

        if (id == R.id.menu_addTask) {
            return true;
        }

        if (id == R.id.menu_info) {
            return true;
        }

        if (id == R.id.menu_showDurations) {
            return true;
        }

        if (id == R.id.menu_tester) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
