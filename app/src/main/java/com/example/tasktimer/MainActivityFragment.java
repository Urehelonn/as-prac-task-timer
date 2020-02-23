package com.example.tasktimer;


import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.security.InvalidParameterException;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "MainActivityFragment";

    // used to identify loader
    public static final int LOADER_ID = 0;
    private CursorRecyclerViewAdapter mAdapter; // adapter reference

    public MainActivityFragment() {
        Log.d(TAG, "MainActivityFragment: starts -----------------------------");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: starts ------------------>>>>>>>>>>>>>");
        super.onActivityCreated(savedInstanceState);
        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreatedView: starts ------------------");
        // create view with list to fill db data into container
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        RecyclerView rv = view.findViewById(R.id.task_list);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CursorRecyclerViewAdapter(null);
        rv.setAdapter(mAdapter);

        Log.d(TAG, "onCreatedView: returned view ------------------");
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: cursor load starts with id: " + id);
        String[] projection = {
                TasksContract.Columns._ID, TasksContract.Columns.TASKS_NAME,
                TasksContract.Columns.TASKS_DESCRIPTION, TasksContract.Columns.TASKS_SORTORDER};
        String inOrder = TasksContract.Columns.TASKS_SORTORDER + ", " + TasksContract.Columns.TASKS_NAME;

        switch (id) {
            case LOADER_ID:
                return new CursorLoader(getActivity(), TasksContract.CONTENT_URI, projection,
                        null, null, inOrder);
            default:
                throw new InvalidParameterException(TAG + ".onCreateLoader called with invalid loader id");
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "Entering onLoadFinished");

        // retrieve data from older cursor
        mAdapter.swapCursor(data);
        int count = mAdapter.getItemCount();
        Log.d(TAG, "onLoadFinished with count: " + count);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset starts: ---------------->>>>>>>><<<<<<<<-----------------");
        // close cursor
        mAdapter.swapCursor(null);
    }

}
