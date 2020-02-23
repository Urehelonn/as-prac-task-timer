package com.example.tasktimer;

import androidx.fragment.app.Fragment;

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
        Log.d(TAG, "TaskEditActivityFragment: constructor :::::::::::::::::");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "TaskEditActivityFragment: created: ");
        View view = inflater.inflate(R.layout.fragment_task_edit, container, false);
        mNameInput = view.findViewById(R.id.task_title);
        mDescriptionInput = view.findViewById(R.id.task_description);
        mSortOrderInput = view.findViewById(R.id.task_sortorder);
        mSubmitBt = view.findViewById(R.id.task_edit_submit_bt);

        Bundle arguments = getActivity().getIntent().getExtras();

        final Task task;
        if (arguments != null) {
//            task editing
            Log.d(TAG, "onCreateView: task details found, editing.....");
            task = (Task) arguments.getSerializable(Task.class.getSimpleName());
            mNameInput.setText(task.getmName());
            mDescriptionInput.setText(task.getmDescription());
            mSortOrderInput.setText(task.getmSortOrder());
            mMode = FragmentEditMode.EDIT;
        } else {
//            task creating
            task = null;
            Log.d(TAG, "onCreateView: no task, create task start: ");
            mMode = FragmentEditMode.ADD;
        }
        return view;
    }
}
