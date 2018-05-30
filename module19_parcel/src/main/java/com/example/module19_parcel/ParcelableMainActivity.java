package com.example.module19_parcel;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class ParcelableMainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "[MainActivity]";
    Parcel p;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onclick(View v) {
        MyObject myObj = new MyObject("text", 1);
        Log.d(LOG_TAG, "myObject: " + myObj.s + ", " + myObj.i);
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra(MyObject.class.getCanonicalName(), myObj);
        Log.d(LOG_TAG, "startActivity");
        startActivity(intent);
    }
}

