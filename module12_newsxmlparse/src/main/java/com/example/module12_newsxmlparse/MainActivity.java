package com.example.module12_newsxmlparse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    // XML feed URL
    public final String mNewsFeed = "http://www.wsj.com/xml/rss/3_7455.xml";
    // Reference to container layout
    public LinearLayout mContainerLayout;
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

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            loadNewsPage();
        } else {
            loadDefaultMessage();
        }
    }

    private void loadDefaultMessage() {
        TextView message = new TextView(this);
        message.setText("Internet Connection is not available!");
        mContainerLayout.removeAllViews();
        mContainerLayout.addView(message);
    }

    private void loadNewsPage() {
        new DownloadNewsTask().execute(mNewsFeed);
    }

    private class DownloadNewsTask extends AsyncTask<String, Void, List<SimpleXMLParser.NewsItem>> {
        @Override
        protected List<SimpleXMLParser.NewsItem> doInBackground(String... params) {

            List<SimpleXMLParser.NewsItem> items;
            InputStream xmlStream = null;
            String url = params[0];

            xmlStream = downloadXML(url);

            items = createNewsItemsFromXml(xmlStream);


            return items;
        }

        @Override
        protected void onPostExecute( List<SimpleXMLParser.NewsItem> items){

            LayoutInflater inflater = LayoutInflater.from(getBaseContext());
            mContainerLayout.removeAllViews();
            for(SimpleXMLParser.NewsItem item : items){

                LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.news_item, null, false);

                TextView heading = findViewById(R.id.heading);
                TextView description = findViewById(R.id.description);

                heading.setText(item.title);
                description.setText(item.description);

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


