package com.example.tasktimer;

import android.util.Log;

import java.io.Serializable;
import java.util.Date;

class Timing implements Serializable {
    private static final long serialVersionUID = 20200307L;
    private static final String TAG = Timing.class.getSimpleName();

    private long mId;
    private Task mTask;
    private long mStartTime;
    private long mDuration;

    public Timing(Task mTask) {
        this.mTask = mTask;
        Date currentTime = new Date();
        mStartTime = currentTime.getTime() / 1000;
        mDuration = 0;
    }

    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public Task getmTask() {
        return mTask;
    }

    public void setmTask(Task mTask) {
        this.mTask = mTask;
    }

    public long getmStartTime() {
        return mStartTime;
    }

    public void setmStartTime(long mStartTime) {
        this.mStartTime = mStartTime;
    }

    public long getmDuration() {
        return mDuration;
    }

    public void setmDuration() {
        // calculate duration from start time to now
        Date currTime = new Date();
        this.mDuration = (currTime.getTime() / 1000) - mStartTime;
        Log.d(TAG, "setmDuration:" + mDuration);
    }

    @Override
    public String toString() {
        return "Timing{" +
                "mId=" + mId +
                ", mTask=" + mTask +
                ", mStartTime=" + mStartTime +
                ", mDuration=" + mDuration +
                '}';
    }
}
