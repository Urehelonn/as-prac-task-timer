package com.example.tasktimer;

import androidx.fragment.app.Fragment;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A placeholder fragment containing a simple view.
 */
public class TaskEditActivityFragment extends Fragment {

    private static final String TAG = "TaskEditActivityFragment";

    public enum FragmentEditMode {
        EDIT,
        ADD
    }

    private FragmentEditMode mMode;

    private EditText mNameInput;
    private EditText mDescriptionInput;
    private EditText mSortOrderInput;
    private Button mSubmitBt;

    public TaskEditActivityFragment() {
        Log.d(TAG, "TaskEditActivityFragment: constructor");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        Log.d(TAG, "TaskEditActivityFragment: created: ");
        View view = inflater.inflate(R.layout.fragment_task_edit, container, false);
        mNameInput = view.findViewById(R.id.task_title);
        mDescriptionInput = view.findViewById(R.id.task_description);
        mSortOrderInput = view.findViewById(R.id.task_sortorder);
        mSubmitBt = view.findViewById(R.id.task_edit_submit_bt);

//        Bundle arguments = getActivity().getIntent().getExtras();
        Bundle arguments = getArguments();

        final Task task;
        if (arguments != null) {
//            task editing
            Log.d(TAG, "onCreateView: task details found, editing.....");
            task = (Task) arguments.getSerializable(Task.class.getSimpleName());
            Log.d(TAG, "task name:" + task.getmName());
            Log.d(TAG, "task description:" + task.getmDescription());
            Log.d(TAG, "task so:" + task.getmSortOrder());
            mNameInput.setText(task.getmName());
            mDescriptionInput.setText(task.getmDescription());
            mSortOrderInput.setText(Integer.toString(task.getmSortOrder()));
            mMode = FragmentEditMode.EDIT;
        } else {
//            task creating
            task = null;
            Log.d(TAG, "onCreateView: no task, create task start: ");
            mMode = FragmentEditMode.ADD;
        }

        mSubmitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if any change, update database
                int sortOrder;
                if (mSortOrderInput.length() > 0) {
                    sortOrder = Integer.parseInt(mSortOrderInput.getText().toString());
                } else {
                    sortOrder = 0;
                }
                ContentResolver cr = getActivity().getContentResolver();
                ContentValues cv = new ContentValues();

                switch (mMode) {
                    case EDIT:
                        if (!mNameInput.getText().toString().equals(task.getmName())) {
                            cv.put(TasksContract.Columns.TASKS_NAME, mNameInput.getText().toString());
                        }
                        if (!mDescriptionInput.getText().toString().equals(task.getmDescription())) {
                            cv.put(TasksContract.Columns.TASKS_DESCRIPTION, mDescriptionInput.getText().toString());
                        }
                        if (sortOrder != task.getmSortOrder()) {
                            cv.put(TasksContract.Columns.TASKS_SORTORDER, sortOrder);
                        }
                        if (cv.size() != 0) {
                            Log.d(TAG, "onClick: update task.");
                            int count = cr.update(TasksContract.buildTaskUri(task.getmId()), cv, null, null);
                            Log.d(TAG, "onClick: update task done, " + count + " task(s) has been updated.");
                        }
                        break;

                    case ADD:
                        if (mNameInput.length() > 0) {
                            cv.put(TasksContract.Columns.TASKS_NAME, mNameInput.getText().toString());
                            cv.put(TasksContract.Columns.TASKS_DESCRIPTION, mDescriptionInput.getText().toString());
                            cv.put(TasksContract.Columns.TASKS_SORTORDER, mSortOrderInput.getText().toString());
                            Uri insertionRes = cr.insert(TasksContract.CONTENT_URI, cv);
                            Log.d(TAG, "onClick: create task done, with uri: " + insertionRes);
                        }
                        break;
                }

                Log.d(TAG, "onClick: Editing done");
            }
        });
        Log.d(TAG, "onCreateView: Exit..............................");
        return view;
    }
}
