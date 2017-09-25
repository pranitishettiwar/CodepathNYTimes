package com.codepath.nytimessearch.activities;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.codepath.nytimessearch.EndlessScrollListener;
import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.adapter.ArticleArrayAdapter;
import com.codepath.nytimessearch.model.Article;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    private GridView gvResults;

    private ArrayList<Article> articles;
    private ArticleArrayAdapter adapter;
    private final int REQUEST_CODE = 20;
    private String mBeginDate;
    private String mSortOrder;
    private String news_desk;
    private String mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupViews();

    }

    private void setupViews() {
        gvResults = (GridView) findViewById(R.id.gvResults);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);

        gvResults.setOnItemClickListener((parent, view, position, id) -> {
            //create an intent to display the article
            Article article = articles.get(position);
            Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
            i.putExtra("article", Parcels.wrap(article));
            startActivity(i);

        });

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                loadNextDataFromApi(page);
                return true;
            }
        });
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {

        Toast.makeText(SearchActivity.this, "Loading more", Toast.LENGTH_SHORT).show();

        searchNextArticle(mQuery, offset);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (isNetworkAvailable()) {

                    mQuery = query;

                    //Clear the adapter with every search
                    adapter.clear();

                    //fetch the list of articles
                    onArticleSearch(query);
                    searchView.clearFocus();
                    return true;
                } else {
                    Toast.makeText(SearchActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_filter) {
            Intent i = new Intent(SearchActivity.this, FilterActivity.class);
            startActivityForResult(i, REQUEST_CODE);

        }

        return super.onOptionsItemSelected(item);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private void onArticleSearch(String query) {
        adapter.clear();
        //Sample request: https://api.nytimes.com/svc/search/v2/articlesearch.json?begin_date=20160112&sort=oldest&fq=news_desk:
        // (%22Education%22%20%22Health%22)&api-key=227c750bb7714fc39ef1559ef1bd8329
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = new RequestParams();
        params.put("api-key", "a1fd0ee32487496a9fccb272e2e53483");
        params.put("page", 0);
        params.put("q", query);
        params.put("begin_date", mBeginDate);
        params.put("sort", mSortOrder);
        params.put("fq", news_desk);

        Log.d("DEBUG: NEW URL", url);

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());

                JSONArray articleJsonResults = null;

                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    adapter.addAll(Article.fromJSONArray(articleJsonResults));
                    adapter.notifyDataSetChanged();
                    Log.d("DEBUG", articles.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void searchNextArticle(String query, int page) {

        //Sample request: https://api.nytimes.com/svc/search/v2/articlesearch.json?begin_date=20160112&sort=oldest&fq=news_desk:
        // (%22Education%22%20%22Health%22)&api-key=227c750bb7714fc39ef1559ef1bd8329
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = new RequestParams();
        params.put("api-key", "a1fd0ee32487496a9fccb272e2e53483");
        params.put("page", page);
        params.put("q", query);
        params.put("begin_date", mBeginDate);
        params.put("sort", mSortOrder);
        params.put("fq", news_desk);

        Log.d("DEBUG: NEW URL", url);

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());

                JSONArray articleJsonResults = null;

                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    adapter.addAll(Article.fromJSONArray(articleJsonResults));
                    //adapter.notifyDataSetChanged();
                    Log.d("DEBUG", articles.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            mBeginDate = data.getExtras().getString("beginDate");
            mSortOrder = data.getExtras().getString("sortOrder");
            Boolean arts = data.getExtras().getBoolean("arts", false);
            Boolean fashion = data.getExtras().getBoolean("fashion", false);
            Boolean sports = data.getExtras().getBoolean("sports", false);

            if (arts || fashion || sports) {
                news_desk = "news_desk:(";
                if (arts)
                    news_desk += "Arts,";
                if (fashion)
                    news_desk += "Fashion,";
                if (sports)
                    news_desk += "Sports";
                news_desk += ")";
            }
            Toast.makeText(SearchActivity.this, "Filter is applied", Toast.LENGTH_SHORT).show();

            onArticleSearch(mQuery);
        }
    }
}

