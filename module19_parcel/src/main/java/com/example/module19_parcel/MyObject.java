package com.example.module19_parcel;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class MyObject implements Parcelable {

    public static final String LOG_TAG = "[MyObject]";

    public String s;
    public int i;

    public static final Creator<MyObject> CREATOR = new Creator<MyObject>() {
        @Override
        public MyObject createFromParcel(Parcel in) {
            Log.d(LOG_TAG, "createFromParcel");
            return new MyObject(in);
        }

        @Override
        public MyObject[] newArray(int size) {
            return new MyObject[size];
        }
    };

    public MyObject(String s, int i) {
        Log.d(LOG_TAG, "MyObject(String _s, int _i)");
        this.i = i;
        this.s = s;
    }

    protected MyObject(Parcel in) {
        Log.d(LOG_TAG, "MyObject(Parcel parcel)");
        s = in.readString();
        i = in.readInt();
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable instance's marshaled representation.
     * For example, if the object will include a file descriptor in the output of writeToParcel(Parcel, int),
     * the return value of this method must include the CONTENTS_FILE_DESCRIPTOR bit.
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.d(LOG_TAG, "writeToParcel");
        dest.writeString(s);
        dest.writeInt(i);
    }
}
