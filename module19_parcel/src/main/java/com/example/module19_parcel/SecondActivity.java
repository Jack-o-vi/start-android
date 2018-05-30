package com.example.module19_parcel;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

public class SecondActivity extends Activity {
    public static final String LOG_TAG ="[SecondActivity]";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        Log.d(LOG_TAG, "getParcelableExtra");

        MyObject myObject = getIntent().getParcelableExtra(MyObject.class.getCanonicalName());
        Log.d(LOG_TAG, "myObject: " + myObject.s + ", " + myObject.i);
    }
}
