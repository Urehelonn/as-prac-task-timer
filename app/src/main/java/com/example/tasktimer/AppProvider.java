package com.example.tasktimer;

// Provider, for database { @link AppDatabase }

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AppProvider extends ContentProvider {

    private static final String TAG = "AppProvider";

    private AppDatabase dbHelper;

    public static final UriMatcher sUriMatcher = buildUriMatcher();

    static final String CONTENT_AUTHORITY = "com.example.tasktimer.provider";
    public static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final int TASKS = 100;
    public static final int TASKS_ID = 101;

    public static final int TIMINGS = 200;
    public static final int TIMINGS_ID = 201;

//    private static final int TASK_TIMES = 300;
//    private static final int TASK_TIMES_ID = 301;

    public static final int TASK_DURATIONS = 400;
    public static final int TASK_DURATIONS_ID = 401;


    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        //eg. content://com.example.tasktimer.provider.Tasks
        matcher.addURI(CONTENT_AUTHORITY, TasksContract.TABLE_NAME, TASKS);
        //eg. content://com.example.tasktimer.provider.Tasks/8
        matcher.addURI(CONTENT_AUTHORITY, TasksContract.TABLE_NAME + "/#", TASKS_ID);

//        //eg. content://com.example.tasktimer.provider.Timing
//        matcher.addURI(CONTENT_AUTHORITY, TimingContract.TABLE_NAME, TIMINGS);
//        //eg. content://com.example.tasktimer.provider.Timing/8
//        matcher.addURI(CONTENT_AUTHORITY, TimingContract.TABLE_NAME + "/#", TIMINGS_ID);
//
//        //eg. content://com.example.tasktimer.provider.Timing
//        matcher.addURI(CONTENT_AUTHORITY, DurationsContract.TABLE_NAME, TASK_DURATIONS);
//        //eg. content://com.example.tasktimer.provider.Timing/8
//        matcher.addURI(CONTENT_AUTHORITY, DurationsContract.TABLE_NAME + "/#", TASK_DURATIONS_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = AppDatabase.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG, "query: called with URI " + uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "query: match is " + match);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (match) {
            case TASKS:
                queryBuilder.setTables(TasksContract.TABLE_NAME);
                break;
            case TASKS_ID:
                queryBuilder.setTables(TasksContract.TABLE_NAME);
                long taskId = TasksContract.getTaskId(uri);
                queryBuilder.appendWhere(TasksContract.Columns._ID + " = " + taskId);
                break;

//            case TIMINGS:
//                queryBuilder.setTables(TimingsContract.TABLE_NAME);
//                break;
//            case TIMINGS_ID:
//                queryBuilder.setTables(TimingsContract.TABLE_NAME);
//                long timingId = TimingsContract.getTimingId(uri);
//                queryBuilder.appendWhere(TimingsContract.Columns._ID + " = " + timingId);
//                break;
//
//            case TASK_DURATIONS:
//                queryBuilder.setTables(DuratioinsContract.TABLE_NAME);
//                break;
//            case TASK_DURATIONS_ID:
//                queryBuilder.setTables(DuratioinsContract.TABLE_NAME);
//                long durationId = DuratioinsContract.getTimingId(uri);
//                queryBuilder.appendWhere(DuratioinsContract.Columns._ID + " = " + durationId);
//                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    //    https://developer.android.com/reference/android/content/ContentProvider#getType(android.net.Uri)
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TASKS:
                return TasksContract.CONTENT_TYPE;
            case TASKS_ID:
                return TasksContract.CONTENT_ITEM_TYPE;

//            case TIMINGS:
//                return TimingsContract.Timing.CONTENT_TYPE;
//            case TIMINGS_ID:
//                return TimingsContract.Timing.CONTENT_ITEM_TYPE;
//
//            case TASK_DURATIONS:
//                return DurationsContract.TaskDuration.CONTENT_TYPE;
//            case TASK_DURATIONS_ID:
//                return DurationsContract.TaskDuration.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d(TAG, "Insertion with uri: " + uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "match is " + match);

        final SQLiteDatabase db;
        Uri returnUri;
        long recordId;

        switch (match) {
            case TASKS:
                // call writable db can be really slow, so only call it after URI is granted to be valid
                db = dbHelper.getWritableDatabase();
                recordId = db.insert(TasksContract.TABLE_NAME, null, values);
                if (recordId >= 0) {
                    returnUri = TasksContract.buildTaskUri(recordId);
                } else {
                    throw new android.database.SQLException("Failed to insert into " + uri.toString());
                }
                break;

            case TIMINGS:
//                db = dbHelper.getWritableDatabase();
//                recordId = db.insert(TimingsContract.Timing.buildTimingUri(recordId));
//                if (recordId >= 0) {
//                    returnUri = TimingsContract.Timing.buildTimingUri(recordId);
//                } else {
//                    throw new android.database.SQLException("Failed to insert into " + uri.toString());
//                }
//                break;
            default:
                throw new IllegalArgumentException("unknown uri: uri");
        }
        Log.d(TAG, "Exiting insert, returning " + returnUri);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
