package labs.bamboo.newsapp;

import android.os.AsyncTask;
import android.util.Log;
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

public class GetNews extends AsyncTask<String, Void, ArrayList<String[]>> {

    @Override
    protected ArrayList<String[]> doInBackground(String... params) {
        String jsonStr = null;
        ArrayList currentNewsList = new ArrayList<String[]>();

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
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
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(currentLine + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                jsonStr = null;
            }

            jsonStr = buffer.toString();

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONArray newsList = jsonObj.getJSONObject("response").getJSONArray("results");
                for(int i = 0; i < newsList.length(); i++ ) {
                    JSONObject currentRow = newsList.getJSONObject(i);
                    if (!currentRow.getString("webTitle").equals("null") ) {
                        currentNewsList.add( new String[] { currentRow.getString("webTitle"),  currentRow.getString("webUrl") });
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
