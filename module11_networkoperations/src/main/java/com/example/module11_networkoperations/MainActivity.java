package com.example.module11_networkoperations;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ConnectivityManager mConnMgr;

    public NetworkReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mConnMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        mReceiver = new NetworkReceiver();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        registerReceiver(mReceiver, filter);

        Button btnClick = findViewById(R.id.btnCheck);
        btnClick.setOnClickListener(this);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();

        if(mReceiver != null){
            unregisterReceiver(mReceiver);
        }
    }

    public boolean onShowNetworkStatus(View v){

        if(mConnMgr != null){

            NetworkInfo networkInfo = mConnMgr.getActiveNetworkInfo();

            if(networkInfo != null && networkInfo.isConnected()){
                Toast.makeText(this, "Network Available", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(this, "Network Not Available", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        TextView tv = findViewById(R.id.tvMsg);
        if(onShowNetworkStatus(v)){
         Intent intent = new Intent("refreshImg");
         startActivity(intent);
        }
    }

    public class NetworkReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            NetworkInfo networkInfo = mConnMgr.getActiveNetworkInfo();

            if(networkInfo != null){
                boolean isWifiAvailable = mConnMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();

                boolean isGSMAvailable = mConnMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();

                if(isWifiAvailable){
                    Toast.makeText(context, "Wifi Reconnected", Toast.LENGTH_SHORT).show();
                } else if (isGSMAvailable){
                    Toast.makeText(context, "GSM Not Available", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Network Not Available", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
