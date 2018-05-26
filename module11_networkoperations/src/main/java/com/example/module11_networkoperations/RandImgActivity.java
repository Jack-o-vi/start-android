package com.example.module11_networkoperations;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class RandImgActivity extends AppCompatActivity{


    private ConnectivityManager mConnMgr;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_refresh);

        mConnMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        imageView = findViewById(R.id.imgToRefresh);


//        Button btnClick = findViewById(R.id.btnCheck);
//        btnClick.setOnClickListener(this);
    }

    public void onRefreshImage(View v){
        final String TAG = "Refresh";

//        String imagePath = "https://upload.wikimedia.org/wikipedia/en/thumb/0/0c/Wild_video_game_logo.jpg/220px-Wild_video_game_logo.jpg";
        String imagePath = "http://lh3.googleusercontent.com/jN9tX6dCJ6_XL9E4K1KCO2Tuwe9_rYUbwv723eu6XGI0PWGLcPs0259VscOu249PPKKcU5AOXrq6JnleEaoK6K_JvZ2PY9lw3pMApzOpTQ=s660";

        if(mConnMgr != null){
            NetworkInfo networkInfo = mConnMgr.getActiveNetworkInfo();

            if(networkInfo != null && networkInfo.isConnected()){
                new DownloadImageTask().execute(imagePath);
            } else {
                Toast.makeText(this, "Network Not Available", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    @Override
//    public void onClick(View v) {
//        onRefreshImage(v);
//    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {
            return downloadImage(urls[0]);
        }

        private Bitmap downloadImage(String path) {

            final String TAG ="Download TASK";
            Bitmap bitmap = null;
            InputStream inputStream;
            String requestMethod = "GET";
            try {

                URL url = new URL(path);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setConnectTimeout(5000);

                urlConnection.setReadTimeout(2500);

                urlConnection.setRequestMethod("GET");

                urlConnection.setDoInput(true);


                urlConnection.connect();

                inputStream=urlConnection.getInputStream();

                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (ProtocolException e) {
                System.err.println("Wrong request method");
                requestMethod = "GET";

            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap){
        if(imageView != null){
            imageView.setImageBitmap(bitmap);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        }
    }
}
