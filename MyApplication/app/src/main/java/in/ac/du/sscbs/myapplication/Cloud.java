package in.ac.du.sscbs.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class Cloud extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    ConnectionDetector connectionDetector;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ErrorDialogMessage errorDialogMessage;
    boolean loadingFinished = true;
    boolean redirect = false;
    Context context;
    public WebView Wv;
    final String url = "http://10.67.1.9/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("SSCBS CLOUD");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        connectionDetector = new ConnectionDetector(this);
        errorDialogMessage = new ErrorDialogMessage(this);
        if (!connectionDetector.isConnectingToInternet()) {


            errorDialogMessage.show();
        } else {

            Wv = (WebView) findViewById(R.id.wv_cloud);
            WebSettings WebSettings = Wv.getSettings();
            WebSettings.setJavaScriptEnabled(true);

            Wv.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                    if (!loadingFinished) {
                        redirect = true;
                    }

                    loadingFinished = false;
                    view.loadUrl(urlNewString);
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                    loadingFinished = false;
                    mSwipeRefreshLayout.setRefreshing(true);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (!redirect) {
                        loadingFinished = true;
                    }

                    if (loadingFinished && !redirect) {

                        mSwipeRefreshLayout.setRefreshing(true);
                    } else {
                        redirect = false;
                    }

                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    // Intent intent = new Intent(getApplicationContext(),check_connection.class);
                    // startActivity(intent);
                    view.loadUrl("about:blank");

                    CharSequence text = "You Must Be Connected to the Wifi(local network) of the S.S.C.B.S !";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    super.onReceivedError(view, errorCode, description, failingUrl);
                }
            });


            Wv.loadUrl(url);
            mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.rl_login);
            mSwipeRefreshLayout.setOnRefreshListener(this);
            mSwipeRefreshLayout.setColorSchemeResources(R.color.progress_color_1, R.color.progress_color_3, R.color.progress_color_4, R.color.progress_color_5);
        }
    }

    @Override
    public void onRefresh() {

        String url = Wv.getUrl();
        mSwipeRefreshLayout.setRefreshing(false);
        Wv.loadUrl(url);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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


        }

        if (id == android.R.id.home) {

            NavUtils.navigateUpFromSameTask(this);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        NavUtils.navigateUpFromSameTask(this);

    }
}