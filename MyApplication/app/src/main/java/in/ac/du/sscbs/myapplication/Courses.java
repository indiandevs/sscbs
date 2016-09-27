package in.ac.du.sscbs.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Courses extends AppCompatActivity implements AdapterView.OnItemClickListener {


    final String cour[] = {"B.Tech(computer science)",
            "BSC(Computer Science)", "BBS", "BMS", "BFIA"};
    ListView course;
    Downloader downloader;
    ConnectionDetector connectionDetector;
    Context context = this;
    ErrorDialogMessage errorDialogMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);


        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Courses");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        course = (ListView) findViewById(R.id.lv_courses);

        ArrayAdapter<String> cours = new ArrayAdapter<String>(this,
                R.layout.simple_list_item_1, cour);
        course.setAdapter(cours);
        downloader = Downloader.getInstance();
        connectionDetector = new ConnectionDetector(this);
        course.setOnItemClickListener(this);
        errorDialogMessage = new ErrorDialogMessage(this);

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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String URL = null;
        Boolean isPDF = false;

        if (connectionDetector.isConnectingToInternet()) {


            switch (position) {

                case 0:
                    URL = "http://www.sscbsdu.ac.in/files/courses/btech.pdf";
                    isPDF = true;

                    break;
                case 1:
                    URL = "http://www.sscbsdu.ac.in/files/courses/bscsyllabus.pdf";
                    isPDF = true;
                    break;

                case 2:URL = "http://www.sscbsdu.ac.in/files/courses/bbssyllabus.pdf";
                    isPDF = true;
                    break;

                case 3:URL = "http://www.sscbsdu.ac.in/index.php/2014-01-16-07-34-49/2014-01-16-07-36-02/2014-01-28-06-56-35/bachelor-of-management-studies";
                    isPDF = false;
                    break;

                case 4:

                   URL =  "http://www.sscbsdu.ac.in/files/courses/bfiasyllabus.pdf";
                    isPDF  = true;
                    break;
            }

            if(isPDF){

                if(URL!=null && downloader.download(URL)) {

                    Toast.makeText(context,"Downloading",Toast.LENGTH_SHORT).show();
                }
            }  else{


                Intent intent = new Intent(
                        android.content.Intent.ACTION_VIEW,
                        Uri.parse(URL));

               Intent chooser = Intent.createChooser(intent, "Browse");
                startActivity(chooser);
            }


        } else{

            errorDialogMessage.show();
        }

    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);

    }
}
