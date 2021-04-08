package labs.bamboo.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<String[]>> {

    private Uri.Builder builder;
    private final ArrayList<NewsView> arrayListNews = new ArrayList<>();
    private final Intent intent = new Intent(Intent.ACTION_VIEW);
    private static final String DEBUG_TAG = "NetworkStatusExample";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        builder = new Uri.Builder();
        builder.scheme(getResources().getString(R.string.url_scheme))
                .authority(getResources().getString(R.string.url_authority))
                .appendPath(getResources().getString(R.string.url_path))
                .appendQueryParameter(getResources().getString(R.string.url_q), getResources().getString(R.string.url_debate))
                .appendQueryParameter(getResources().getString(R.string.url_tag),getResources().getString(R.string.url_politics))
                .appendQueryParameter(getResources().getString(R.string.url_date), getResources().getString(R.string.url_date_value))
                .appendQueryParameter(getResources().getString(R.string.url_show_tags), getResources().getString(R.string.url_contributor))
                .appendQueryParameter(getResources().getString(R.string.url_api_key),getResources().getString(R.string.url_api_key_value));



        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiConn = false;
        boolean isMobileConn = false;

        for (Network network : connMgr.getAllNetworks()) {
            NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                isWifiConn |= networkInfo.isConnected();
            }
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                isMobileConn |= networkInfo.isConnected();
            }
        }

        if (isWifiConn || isMobileConn ) {
            getSupportLoaderManager().initLoader(1, null, this).forceLoad();
        }else{
            RelativeLayout mainActivity = findViewById(R.id.mainActivity);
            TextView textView = new TextView(this);
            textView.setText(getResources().getString(R.string.no_internet_connect));
            textView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setGravity(Gravity.CENTER);
            mainActivity.addView(textView);
        }
    }

    @NonNull
    @Override
    public Loader<ArrayList<String[]>> onCreateLoader(int id, @Nullable Bundle args) {
        return new GetNews(MainActivity.this, builder.build().toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<String[]>> loader, ArrayList<String[]> arrayList) {
        Log.v("length", String.valueOf(arrayList.size()));

        ListView newsListView = findViewById(R.id.newsListView);

        if ( arrayList != null ) {
            if ( arrayList.size() > 0 ) {
                for( int i = 0; i < arrayList.size(); i++ ) {
                    String[] currentNews = arrayList.get(i);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = null;
                        try {
                            date = format.parse(currentNews[3]);
                        }catch ( ParseException e ){
                            e.printStackTrace();
                        }

                        arrayListNews.add(new NewsView(R.drawable.jest, currentNews[0], currentNews[4]+" - "+currentNews[2]+" - "+format.format(date)));
                        NewsViewAdapter newsViewAdapter= new NewsViewAdapter(this, arrayListNews);
                        newsListView.setAdapter(newsViewAdapter);
                    }

                newsListView.setOnItemClickListener((parent, view, position, id) -> {
                        intent.setData(Uri.parse(arrayList.get(position)[1]));
                        startActivity(intent);
                });

            }else {
                RelativeLayout mainActivity = findViewById(R.id.mainActivity);
                TextView textView = new TextView(this);
                textView.setText(getResources().getString(R.string.no_news));
                textView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setGravity(Gravity.CENTER);
                mainActivity.addView(textView);
            }
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<String[]>> loader) {
    }
}
