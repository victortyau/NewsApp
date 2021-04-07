package labs.bamboo.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String[]> arrayList;
        ArrayList<NewsView> arrayListNews = new ArrayList<>();
        ListView newsListView = findViewById(R.id.newsListView);
        Intent intent = new Intent(Intent.ACTION_VIEW);

        try {
            arrayList = new GetNews().execute("https://content.guardianapis.com/search?q=debate&tag=politics/politics&from-date=2014-01-01&api-key=test").get();

            if ( arrayList != null ) {

                if ( arrayList.size() > 0 ) {
                    for( int i = 0; i < arrayList.size(); i++ ) {
                        String[] currentNews = arrayList.get(i);
                        arrayListNews.add(new NewsView(R.drawable.jest, currentNews[0], getResources().getString(R.string.guardian)));
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
        }catch (ExecutionException | InterruptedException ei){
            ei.printStackTrace();
        }

    }
}