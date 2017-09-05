package gagan.newsapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Gagan on 7/12/2017.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private String input_url;

    public NewsLoader(Context context, String url_) {
        super(context);
        input_url = url_;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        URL url = null;
        try {
            url = Utils.formUrl(input_url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String jsondata = null;
        try {
            jsondata = Utils.getHTTPData(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<News> output_list = null;
        try {
            output_list = Utils.convertJSonData(jsondata);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return output_list;
    }
}
