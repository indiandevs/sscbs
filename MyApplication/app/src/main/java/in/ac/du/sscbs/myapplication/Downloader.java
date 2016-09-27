package in.ac.du.sscbs.myapplication;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.design.widget.Snackbar;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.URI;

/**
 * Created by baymax on 30/12/15.
 */
public class Downloader extends BroadcastReceiver {


    static Downloader downloader = null;
    String state;
    DownloadManager.Request request;
    DownloadManager downloadManager;
    Context context;
    long myDownloadrefrence;
    boolean isAvailable, isWriteable;
    boolean isFileAvailable;

    static String getFileName(String temp) {


        String data = null;
        StringBuffer stringBuffer = new StringBuffer();

        int length = temp.length();
        length--;
        while (temp.charAt(length) != '/') {


            stringBuffer.insert(0, temp.charAt(length));
            --length;
        }

        data = stringBuffer.toString();

        return data;
    }


    private Downloader() {


        state = Environment.getExternalStorageState();


        context = MyApplication.getAppContext();
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

    }

    public static Downloader getInstance() {

        if (downloader == null) {
            downloader = new Downloader();
        }


        return downloader;

    }


    boolean download(String url) {

        isFileAvailable = false;

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            isAvailable = isWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            isAvailable = true;
            isWriteable = false;

            return false;
        } else {

            isAvailable = false;
            isWriteable = false;
            return false;
        }

        if (isWriteable && isAvailable) {

            File direct = new File(Environment.getExternalStorageDirectory()
                    + "/SSCBS");

            if (!direct.exists()) {
                direct.mkdirs();
            }


            String fileName = getFileName(url);
            Uri uri = Uri.parse(url);
            request = new DownloadManager.Request(uri);


            if (fileName != null) {

                request.setTitle(fileName);


            }


            request.setDestinationInExternalPublicDir("/SSCBS", fileName);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);


            myDownloadrefrence = downloadManager.enqueue(request);


        } else {

            Toast.makeText(context, "No Write Permissions", Toast.LENGTH_SHORT).show();
        }


        return true;
    }

    @Override
    public void onReceive(Context context, Intent intent) {


        long refrence = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        if (myDownloadrefrence == refrence) {

            Toast.makeText(context, "File Has Been Downloaded", Toast.LENGTH_SHORT).show();
        }

    }

}