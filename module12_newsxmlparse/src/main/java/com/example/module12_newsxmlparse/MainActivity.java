package com.example.module12_newsxmlparse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

/**
 * @author Bjorn
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MyLOGS";
    // XML feed URL
    public final String mNewsFeed = "http://www.wsj.com/xml/rss/3_7455.xml";
    // Reference to container layout
    public LinearLayout mContainerLayout;
    public String sPreferredNetwork;
    // Connectivity Manager instance
    private ConnectivityManager connectivityManager;

    // Activity lifeCycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Store the connectivity manager in member variable.
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        mContainerLayout = findViewById(R.id.containerView);
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        sPreferredNetwork = preferences.getString("chosenNetworkType", "Any");
        loadNewsPage();
//        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//
//        if (networkInfo != null && networkInfo.isConnected()) {
//            loadNewsPage();
//        } else {
//            loadDefaultMessage();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_settings) {
            Intent settingIntent = new Intent(getBaseContext(), SettingsActivity.class);
            startActivity(settingIntent);
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void loadDefaultMessage() {
        TextView message = new TextView(this);
        message.setText("Internet Connection is not available!");
        mContainerLayout.removeAllViews();
        mContainerLayout.addView(message);
    }

    private void loadNewsPage() {

        boolean isWifiAvailable = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();

        if (sPreferredNetwork.equals("Any")) {
            if (connectivityManager != null) {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    new DownloadNewsTask().execute(mNewsFeed);
                } else {
                    Toast.makeText(this, "Network Not available", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (sPreferredNetwork.equals("Wifi")) {
            if (isWifiAvailable) {
                new DownloadNewsTask().execute(mNewsFeed);
            } else {
                Toast.makeText(this, "Data allowed only on WiFi network", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Data disabled by user!", Toast.LENGTH_SHORT).show();
            loadDefaultMessage();
        }

       // new DownloadNewsTask().execute(mNewsFeed);
    }

    private class DownloadNewsTask extends AsyncTask<String, Void, List<SimpleXMLParser.NewsItem>> {
        @Override
        protected List<SimpleXMLParser.NewsItem> doInBackground(String... params) {

            List<SimpleXMLParser.NewsItem> items;
            InputStream xmlStream;
            String url = params[0];

            xmlStream = downloadXML(url);

            items = createNewsItemsFromXml(xmlStream);
            return items;
        }

        @Override
        protected void onPostExecute(List<SimpleXMLParser.NewsItem> items) {

            LayoutInflater inflater = LayoutInflater.from(getBaseContext());
            mContainerLayout.removeAllViews();
            int[] colors = {Color.parseColor("#00a9ff"), Color.parseColor("#e3e9ed")};
            for (SimpleXMLParser.NewsItem item : items) {

                LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.news_item, mContainerLayout, false);

                TextView tvHeading = (TextView) linearLayout.findViewById(R.id.heading);
                TextView tvDescription = (TextView) linearLayout.findViewById(R.id.description);

                tvHeading.setBackgroundColor(colors[0]);
                tvDescription.setBackgroundColor(colors[1]);
                if (tvHeading != null && tvDescription != null) {
                    tvHeading.setText(item.title);
                    tvDescription.setText(item.description);
                } else {
                    Log.e(TAG, "tvHeading " + (tvHeading == null) + " title: " + item.title);
                    Log.e(TAG, "tvDescription " + (tvDescription == null) + " description: " + item.description);
                }

                mContainerLayout.addView(linearLayout);
            }
        }

        private List<SimpleXMLParser.NewsItem> createNewsItemsFromXml(InputStream xml) {
            SimpleXMLParser parser = new SimpleXMLParser();

            // Parse XML. Return the list of news_items
            return parser.parse(xml);
        }

        public InputStream downloadXML(String path) {
            final String TAG = "Download Task";
            Bitmap bitmap = null;
            InputStream inputStream = null;

            try {

                URL url = new URL(path);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(2500);
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);

                urlConnection.connect();

                inputStream = urlConnection.getInputStream();


            } catch (MalformedURLException e) {
                Log.e(TAG, "URL Error: " + e.getMessage());
            } catch (ProtocolException e) {
                Log.e(TAG, "Download failed: " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return inputStream;
        }
    }
}


