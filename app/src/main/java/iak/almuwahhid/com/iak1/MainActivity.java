
package iak.almuwahhid.com.iak1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import iak.almuwahhid.com.iak1.Adapter.GithubAdapter;
import iak.almuwahhid.com.iak1.Controller.EventHandler;
import iak.almuwahhid.com.iak1.Utilities.NetworkUtils;
import iak.almuwahhid.com.iak1.kelas.Github;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{
    private EditText mSearchBoxEditText;
    private TextView mUrlDisplayTextView;

    private TextView mErrorMessageDisplay;

    EventHandler evt;

    RecyclerView rv;
    ArrayList<Github> all_data;
    GithubAdapter adapter;
    SharedPreferences sp;

    private ProgressBar mLoadingIndicator;

    String queryUrl="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        evt = new EventHandler();
        sp = getApplicationContext().getSharedPreferences("user", 0);

        // Verifikasi
        if(!evt.auth(sp)){
            startActivity(new Intent(this, Login.class));
        }



        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);
        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        // Cek savedInstance
        if (savedInstanceState != null) {
            queryUrl = savedInstanceState.getString("query");
            URL githubSearchUrl = NetworkUtils.buildUrl(queryUrl);
            mUrlDisplayTextView.setText(githubSearchUrl.toString());
            makeGithubSearchQuery(true);
        }

        rv = (RecyclerView) findViewById(R.id.rv);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String queryUrl = mSearchBoxEditText.getText().toString();
        outState.putString("query", queryUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuku, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.search_toolbar){
            makeGithubSearchQuery(false);
        }else if(item.getItemId()==R.id.logout_toolbar){
            evt.setSESSIONNull(sp);
            startActivity(new Intent(this, Login.class));
        }
        return super.onOptionsItemSelected(item);
    }


    /* TODO : Pembuatan Loader*/
    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            String mGithubJson;
            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }
                mLoadingIndicator.setVisibility(View.VISIBLE);
                /*forceLoad();*/ // fungsi buat apa ?

                // tambahan fungsinya
                if (mGithubJson != null) {
                    deliverResult(mGithubJson);
                } else {
                    forceLoad();
                }
            }

            @Override
            public String loadInBackground() {
                String searchQueryUrlString = args.getString("query");
                if (searchQueryUrlString == null || TextUtils.isEmpty(searchQueryUrlString)) {
                    return null;
                }
                try {
                    URL githubUrl = new URL(searchQueryUrlString);
                    String githubSearchResults = NetworkUtils.getResponseFromHttpUrl(githubUrl);
                    return githubSearchResults;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(String data) {
                try {
                    all_data = evt.setToArray(all_data, data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new GithubAdapter(all_data, getBaseContext());
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
    /* TODO : End of Pembuatan Loader*/


    public class GithubQueryTask extends AsyncTask<URL, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String githubSearchResults = null;
            try {
                githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return githubSearchResults;
        }

        @Override
        protected void onPostExecute(String s) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (s != null && !s.equals("")) {
                showJsonDataView();
                try {
                    all_data = evt.setToArray(all_data, s);
                    adapter = new GithubAdapter(all_data, getBaseContext());
                    rv.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                showErrorMessage();
            }
        }
    }

    private void showErrorMessage() {
        // First, hide the currently visible data
//        mSearchResultsTextView.setVisibility(View.INVISIBLE);
        // Then, show the error
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }
    private void showJsonDataView() {
        // First, make sure the error is invisible
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        // Then, make sure the JSON data is visible
//        mSearchResultsTextView.setVisibility(View.VISIBLE);
    }
    private void makeGithubSearchQuery(Boolean b) {
        String githubQuery ="";
        if(!b){
            githubQuery= mSearchBoxEditText.getText().toString();
        }else if(b){
            githubQuery= queryUrl;
        }
        URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery);
        mUrlDisplayTextView.setText(githubSearchUrl.toString());
        new GithubQueryTask().execute(githubSearchUrl);

/*        Bundle queryBundle = new Bundle();
        queryBundle.putString("query", githubSearchUrl.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> githubSearchLoader = loaderManager.getLoader(22);
        if (githubSearchLoader == null) {
            loaderManager.initLoader(22, queryBundle, this);
        } else {
            loaderManager.restartLoader(22, queryBundle, this);
        }*/
    }
}
