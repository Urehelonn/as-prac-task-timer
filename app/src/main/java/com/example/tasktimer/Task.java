package com.example.tasktimer;

import java.io.Serializable;

public class Task implements Serializable {
    public static final long serialVersionUID = 20200223L;
    private long mId;
    private final String mName;
    private final String mDescription;
    private final int mSortOrder;

    public long getmId() {
        return mId;
    }
    public void setmId(long mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public String getmDescription() {
        return mDescription;
    }

    public int getmSortOrder() {
        return mSortOrder;
    }

    public Task(long id, String name, String description, int sortOrder) {
        this.mId = id;
        this.mName = name;
        this.mDescription = description;
        this.mSortOrder = sortOrder;
    }

    @Override
    public String toString() {
        return "Task{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mSortOrder=" + mSortOrder +
                '}';
    }
}
