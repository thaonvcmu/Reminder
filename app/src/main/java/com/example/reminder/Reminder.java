package com.example.reminder;

public class Reminder {
    private int mId;
    private String mContent;
    private int mInportant;

    public Reminder(int mId, String mContent, int mInportant) {
        this.mId = mId;
        this.mContent = mContent;
        this.mInportant = mInportant;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public int getmInportant() {
        return mInportant;
    }

    public void setmInportant(int mInportant) {
        this.mInportant = mInportant;
    }
}
