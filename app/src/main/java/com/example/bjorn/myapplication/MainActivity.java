package com.example.bjorn.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


class ButtonListener  implements View.OnClickListener {

    private static final String TAG = "myLogs";
    private TextView tvOut;
    private Activity curActivity;
    public ButtonListener(TextView tvOut, Activity activity){
            this.tvOut = tvOut;
            curActivity = activity;
    }

    public void onClick(View v) {
        // по id определяем кнопку, вызвавшую этот обработчик
        Log.d(TAG, "по id определяем кнопку, вызвавшую этот обработчик");
        // по id определеяем кнопку, вызвавшую этот обработчик
        switch (v.getId()) {
            case R.id.btnOk:
                // кнопка ОК
                Log.d(TAG, "кнопка ОК");
                tvOut.setText("Нажата кнопка ОК");
                Toast.makeText(curActivity, "Нажата кнопка ОК", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnCancel:
                // кнопка Cancel
                Log.d(TAG, "кнопка Cancel");
                // кнопка Cancel
                tvOut.setText("Нажата кнопка Cancel");
                Toast.makeText(curActivity, "Нажата кнопка Cancel", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

public class MainActivity extends AppCompatActivity {

    private TextView tvOut;
    private Button btnOk;
    private Button btnCancel;
    private static final String TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Log.d(TAG, "найдем View-элементы");
        // найдем View-элементы
        tvOut = findViewById(R.id.tvOut);
        btnOk = findViewById(R.id.btnOk);
        btnCancel = findViewById(R.id.btnCancel);

        ButtonListener btnList = new ButtonListener(tvOut, this);
        // присваиваем обработчик кнопкам
        Log.d(TAG, "присваиваем обработчик кнопкам");
        btnOk.setOnClickListener(btnList);
        btnCancel.setOnClickListener(btnList);

    }
}
