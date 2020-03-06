package com.example.tasktimer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements CursorRecyclerViewAdapter.OnTaskClickListener,
        AppDialog.DialogEvents, TaskEditActivityFragment.OnSaveClicked {

    private static final String TAG = "MainActivity";
    private boolean mTwoPane = false;

    private static final int DIALOG_ID_DELETE = 1;
    private static final int DIALOG_ID_CANCEL_EDIT = 2;
    private static final String TASKID = "TaskId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.task_detail_container) != null) {
            mTwoPane = true;
            Log.d(TAG, "2pane triggered===========================================================");
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
//                Log.d(TAG, "onCreate:====================================");
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
        // add dialog confirmation before delete
        Log.d(TAG, "onDeleteClick: clicked");

        AppDialog dialog = new AppDialog();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, DIALOG_ID_DELETE);
        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.dialog_delete_message, task.getmId(), task.getmName()));
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.delete);

        args.putLong(TASKID, task.getmId());

        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), null);
    }

    private void taskEditRequest(Task task) {
//        Log.d(TAG, "taskEditRequest: starts ----------------------------");
        if (mTwoPane) {
            Log.d(TAG, "taskEditRequest: in two-pane mode (landscape)");
            TaskEditActivityFragment taskEditActivityFragment = new TaskEditActivityFragment();
            // use Bundle to store task information to pass to fragment
            Bundle args = new Bundle();
            args.putSerializable(Task.class.getSimpleName(), task);
            taskEditActivityFragment.setArguments(args);
            // those bundle information will be retrieved by fragment_task_edit onCreate next

            // insert fragment using fragment manager
            getSupportFragmentManager().beginTransaction().replace(R.id.task_detail_container, taskEditActivityFragment).commit();
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

    @Override
    public void onSaveClicked() {
        // remove task edit fragment after change saved
        Log.d(TAG, "onSaveClicked: starts");
        FragmentManager fm = getSupportFragmentManager();
        Fragment frag = fm.findFragmentById(R.id.task_detail_container);
        if (frag != null) {
            getSupportFragmentManager().beginTransaction().remove(frag).commit();
        }
    }

    @Override
    public void onPositiveDialogResult(int dialogId, Bundle args) {
        Log.d(TAG, "onPositiveDialogResult: called");
        switch (dialogId) {
            case DIALOG_ID_DELETE:
                Long taskId = args.getLong("TaskId");
                // avoid getLong crush if task id==0
                if (BuildConfig.DEBUG && taskId == 0) throw new AssertionError("TaskID is zero");
                getContentResolver().delete(TasksContract.buildTaskUri(taskId), null, null);
                break;
            case DIALOG_ID_CANCEL_EDIT:
                break;
        }
    }

    @Override
    public void onNegativeDialogResult(int dialogId, Bundle args) {
        Log.d(TAG, "onNegativeDialogResult: called");
        switch (dialogId) {
            case DIALOG_ID_DELETE:
                break;
            case DIALOG_ID_CANCEL_EDIT:
                finish();
                break;
        }
    }

    @Override
    public void onDialogCancelled(int dialogId) {
        Log.d(TAG, "onDialogCancelled: called");
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: called");
        FragmentManager fm = getSupportFragmentManager();
        TaskEditActivityFragment taskEditActivityFragment = (TaskEditActivityFragment) fm.findFragmentById(R.id.task_detail_container);
        if (taskEditActivityFragment == null || taskEditActivityFragment.canClose()) {
            super.onBackPressed();
        } else {
            // when still in taskEdit, show dialog for exit confirmation
            AppDialog dialog = new AppDialog();
            Bundle args = new Bundle();
            args.putInt(AppDialog.DIALOG_ID, DIALOG_ID_CANCEL_EDIT);
            args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.cancel_edit_message));
            args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.cancel_positive);
            args.putInt(AppDialog.DIALOG_NEGATIVE_RID, R.string.cancel_negative);

            dialog.setArguments(args);
            dialog.show(fm, null);
        }
    }
}
