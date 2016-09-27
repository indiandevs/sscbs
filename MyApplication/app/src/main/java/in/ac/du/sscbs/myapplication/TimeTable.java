package in.ac.du.sscbs.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.PersistableBundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class TimeTable extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    ConnectionDetector connectionDetector;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ErrorDialogMessage errorDialogMessage;
    boolean loadingFinished = true;
    boolean redirect = false;
    Context context;

    public WebView Wv;
    final String url = "http://184.171.248.18/~collegep/att_sscbs/main/Student/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Time Table");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Wv = (WebView) findViewById(R.id.wv_timetable);
        WebSettings webSettings = Wv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        Wv.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        Wv.setScrollbarFadingEnabled(true);

        connectionDetector = new ConnectionDetector(this);
        errorDialogMessage = new ErrorDialogMessage(this);
        if (!connectionDetector.isConnectingToInternet()) {


            errorDialogMessage.show();
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.rl_time_table);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.progress_color_1, R.color.progress_color_3, R.color.progress_color_4, R.color.progress_color_5);

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

                                        mSwipeRefreshLayout.setRefreshing(false);

                                    } else {
                                        redirect = false;
                                    }

                                }

                                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                    //Intent intent = new Intent(getApplicationContext(),check_connection.class);
                                    //startActivity(intent);
                                    // FragmentManager fm =getFragmentManager();
                                    // fm.beginTransaction().replace(R.id.content,new connectionErrorFragment()).commit();
                                    view.loadUrl("about:blank");

                                    CharSequence text = "Check your internet ,we are not able to load page!";
                                    int duration = Toast.LENGTH_LONG;

                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                    super.onReceivedError(view, errorCode, description, failingUrl);
                                }



                            }


        );
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
    public void onRefresh() {

        String url = Wv.getUrl();
        Wv.loadUrl(url);
        mSwipeRefreshLayout.setRefreshing(false);

    }


    @Override
    public void onBackPressed() {

        String loadingUrl = Wv.getUrl();

        if (loadingUrl != null && !loadingUrl.equals(url)) {


            if(loadingUrl.equals("about:blank"))
            {
                NavUtils.navigateUpFromSameTask(this);

            }
            else Wv.goBack();



        } else {
            NavUtils.navigateUpFromSameTask(this);

        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        // Save the state of the WebView
        Wv.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        // Restore the state of the WebView
        Wv.restoreState(savedInstanceState);
    }



}
