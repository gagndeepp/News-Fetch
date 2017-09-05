package gagan.newsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class Utils {
    public static List<News> convertJSonData(String string) throws JSONException {
        if (string == null) {
            return null;
        }
        JSONObject main = new JSONObject(string);
        JSONObject response = main.getJSONObject("response");
        JSONArray results = response.getJSONArray("results");
        List<News> outputList = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            JSONObject obj = results.getJSONObject(i);
            String title = obj.getString("webTitle");
            String web = obj.getString("webUrl");
            String section = obj.getString("sectionName");
            String date = obj.getString("webPublicationDate");
            News object = new News(title, section, web, date);
            outputList.add(object);
        }
        return outputList;
    }

    public static URL formUrl(String web) throws MalformedURLException {
        if (web == null)
            return null;
        URL output = new URL(web);
        return output;
    }

    public static String getHTTPData(URL url) throws IOException {
        if (url == null)
            return null;
        String output = null;
        HttpURLConnection connect = null;
        InputStream stream = null;
        try {
            connect = (HttpURLConnection) url.openConnection();
            connect.setReadTimeout(8000);
            connect.setConnectTimeout(13000);
            connect.connect();
            if (connect.getResponseCode() == 200) {
                stream = connect.getInputStream();
                output = read(stream);
            } else {
                Log.e("Response Code Error", "Bad Response Error Code 200");
            }
        } finally {
            if (connect != null)
                connect.disconnect();
            if (stream != null)
                stream.close();
        }
        return output;
    }

    private static String read(InputStream stream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (stream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(stream, Charset.defaultCharset());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String temp = bufferedReader.readLine();
            while (temp != null) {
                output.append(temp);
                temp = bufferedReader.readLine();
            }
        }
        return output.toString();
    }
}
