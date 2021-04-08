package labs.bamboo.newsapp;


import android.content.Context;
import android.util.Log;
import androidx.loader.content.AsyncTaskLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetNews extends AsyncTaskLoader<ArrayList<String[]>> {

    private final String urlString;

    public GetNews( Context context , String urlString) {
        super(context);
        this.urlString = urlString;
    }

    @Override
    public ArrayList<String[]> loadInBackground() {
        ArrayList<String[]> currentNewsList = new ArrayList<String[]>();
        String jsonStr = null;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                jsonStr = null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                buffer.append(currentLine + "\n");
            }

            if (buffer.length() == 0) {
                jsonStr = null;
            }

            jsonStr = buffer.toString();

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONArray newsList = jsonObj.getJSONObject("response").getJSONArray("results");
                for(int i = 0; i < newsList.length(); i++ ) {
                    JSONObject currentRow = newsList.getJSONObject(i);
                    String currentAuthor = "";
                    if (!currentRow.getString("webTitle").equals("null") ) {
                        JSONArray arrayTags = currentRow.getJSONArray("tags");
                        if (arrayTags != null) {
                            if ( arrayTags.length() > 0 ) {
                                JSONObject currentTags = arrayTags.getJSONObject(0);
                                currentAuthor = currentTags.getString("webTitle");
                            }
                        }

                        currentNewsList.add( new String[] { currentRow.getString("webTitle"),
                                currentRow.getString("webUrl"),
                                currentRow.getString("sectionName"),
                                currentRow.getString("webPublicationDate"),
                                currentAuthor
                        });
                    }
                }
            } catch(JSONException ex) {
                ex.printStackTrace();
            }

        }catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            jsonStr = null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
        return currentNewsList;
    }
}
