package labs.bamboo.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsViewAdapter extends ArrayAdapter<NewsView> {

    public NewsViewAdapter( Context context, ArrayList<NewsView> arrayList ) {
        super(context, 0, arrayList);
    }

    public View getView( int position, View convertView, ViewGroup parent ) {
        View currentItemView = convertView;

        if ( currentItemView == null ) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_custom_view, parent, false);
        }

        NewsView currentNewsPosition = getItem(position);

        ImageView newsImageView = currentItemView.findViewById(R.id.newsImageView);
        assert currentNewsPosition != null;
        newsImageView.setImageResource(currentNewsPosition.getNewsImageView());

        TextView newsTitle = currentItemView.findViewById(R.id.newsTitle);
        newsTitle.setText(currentNewsPosition.getNewsTitle());

        TextView newsPaper = currentItemView.findViewById(R.id.newsPaper);
        newsPaper.setText(currentNewsPosition.getNewsPaper());

        return currentItemView;
    }
}
