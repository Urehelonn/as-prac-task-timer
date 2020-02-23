package com.example.tasktimer;

import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class CursorRecyclerViewAdapter extends RecyclerView.Adapter<CursorRecyclerViewAdapter.TaskViewHolder> {
    private static final String TAG = "CursorRecyclerViewAdapt";
    private Cursor mCursor;

    public CursorRecyclerViewAdapter(Cursor cursor) {
        Log.d(TAG, "CursorRecyclerViewAdapter: Constructor called -----------------=================");
        mCursor = cursor;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: starts");

//        when db is empty, give short guide so users can start add their first task
        if (mCursor == null || mCursor.getCount() == 0) {
            Log.d(TAG, "onBindViewHolder: providing instructions");
            holder.name.setText(String.valueOf(R.string.task_quick_tip_title));
            holder.description.setText(String.valueOf(R.string.task_quick_tip));
            holder.editBt.setVisibility(View.GONE);
            holder.deleteBt.setVisibility(View.GONE);
        } else {
            if (!mCursor.moveToPosition(position)) {
                throw new IllegalStateException("Could not move cursor to position " + position + "!");
            }
            holder.name.setText(mCursor.getString(mCursor.getColumnIndex(TasksContract.Columns.TASKS_NAME)));
            holder.description.setText(mCursor.getString(mCursor.getColumnIndex(TasksContract.Columns.TASKS_DESCRIPTION)));
//                TODO: add listeners for the buttons
            holder.editBt.setVisibility(View.VISIBLE);
            holder.deleteBt.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: starts");
        if (mCursor == null || mCursor.getCount() == 0) {
            // still display something even db is empty, since there's basic instruction provided
            return 1;
        } else {
            Log.d(TAG, "getItemCount: get Cursor Count: " + mCursor.getCount());
            return mCursor.getCount();
        }
    }

    /**
     * Swap in new cursor, return the old cursor, which is not closed.
     *
     * @param newCursor The new cursor to be used
     * @return Returns the old Cursor, or null if there wasn't one
     * If new Cursor is same as old Cursor, null returned as well.
     */
    Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) return null;

        final Cursor oldCursor = mCursor;
        mCursor = newCursor;

        if (newCursor != null) {
            // notify for data change
            Log.d(TAG, "swapCursor: data retrieved");
            notifyDataSetChanged();
        } else {
            // notify for data being empty
            Log.d(TAG, "swapCursor: data empty called");
            notifyItemRangeChanged(0, getItemCount());
        }
        return oldCursor;
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "TaskViewHolder";

        TextView name = null;
        TextView description = null;
        ImageButton editBt = null;
        ImageButton deleteBt = null;

        public TaskViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.task_title);
            this.description = itemView.findViewById(R.id.task_description);
            this.editBt = itemView.findViewById(R.id.task_edit_bt);
            this.deleteBt = itemView.findViewById(R.id.task_delete_bt);
            Log.d(TAG, "TaskViewHolder: TaskViewHolder loading completed");
        }
    }
}