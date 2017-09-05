package gagan.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    private String base_url_search = "https://content.guardianapis.com/search?q=";
    private String base_url_category = "https://content.guardianapis.com/";
    private String[] category_strings = {"Technology", "Culture", "Sport", "Fashion", "Lifestyle", "Travel"};
    private NewsAdapter newsAdapter;
    private ProgressBar progressBar = null;
    private TextView loadingTV;
    private TextView noResultBool = null;
    private TextView internetBOOL = null;
    private StringBuilder input_value = null;
    private ConnectivityManager connectivityManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText input_text = (EditText) findViewById(R.id.input_text);
        loadingTV = (TextView) findViewById(R.id.loadingTV);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        internetBOOL = (TextView) findViewById(R.id.internetBOOL);
        noResultBool = (TextView) findViewById(R.id.noresults);
        Button clear_button = (Button) findViewById(R.id.clearbutton);
        Button button = (Button) findViewById(R.id.searchbutton);
        connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final Spinner spinner = (Spinner) findViewById(R.id.category_spinner);
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, category_strings);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                input_value = new StringBuilder();
                input_value.append(base_url_category);
                switch (position) {
                    case 0:
                        input_value.append("technology");
                        input_value.append("?api-key=test");
                        break;
                    case 1:
                        input_value.append("culture");
                        input_value.append("?api-key=test");
                        break;
                    case 2:
                        input_value.append("sport");
                        input_value.append("?api-key=test");
                        break;
                    case 3:
                        input_value.append("fashion");
                        input_value.append("?api-key=test");
                        break;
                    case 4:
                        input_value.append("lifeandstyle");
                        input_value.append("?api-key=test");
                        break;
                    case 5:
                        input_value.append("travel");
                        input_value.append("?api-key=test");
                        break;
                }
                execution_helper();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
                newsAdapter.clear();
                execution_helper();
                input_text.setText("");
                progressBar.setVisibility(View.GONE);
                loadingTV.setVisibility(View.GONE);
                noResultBool.setVisibility(View.GONE);
                internetBOOL.setVisibility(View.GONE);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.GONE);
                input_value = new StringBuilder();
                input_value.append(base_url_search);
                String data = input_text.getText().toString();
                input_value.append(data);
                input_value.append("&api-key=test");
                execution_helper();
            }
        });
        ListView main_list = (ListView) findViewById(R.id.news_list);
        TextView emptyListView = (TextView) findViewById(R.id.emptyListTV);
        main_list.setEmptyView(emptyListView);
        newsAdapter = new NewsAdapter(this, new ArrayList<News>());
        main_list.setAdapter(newsAdapter);
        main_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNews = newsAdapter.getItem(position);
                Uri newsUri = Uri.parse(currentNews.getWeburl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);
            }
        });
    }

    private void execution_helper() {
        noResultBool.setVisibility(View.GONE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            progressBar.setVisibility(View.VISIBLE);
            loadingTV.setVisibility(View.VISIBLE);
            getSupportLoaderManager().restartLoader(0, null, MainActivity.this).forceLoad();
        } else {
            newsAdapter.clear();
            progressBar.setVisibility(View.GONE);
            loadingTV.setVisibility(View.GONE);
            noResultBool.setVisibility(View.GONE);
            internetBOOL.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, input_value.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        newsAdapter.clear();
        progressBar.setVisibility(View.GONE);
        loadingTV.setVisibility(View.GONE);
        if (data == null || data.isEmpty()) {
            noResultBool.setVisibility(View.VISIBLE);
        }
        if (data != null && !data.isEmpty()) {
            if (noResultBool.getVisibility() == View.VISIBLE)
                noResultBool.setVisibility(View.GONE);
            newsAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        newsAdapter.clear();
    }
}
