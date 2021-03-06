package com.ttn.googlehackathon.utility;

import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Display;
import android.view.WindowManager;

/**
 * Project: <b>GoogleHackathon</b><br/>
 * Created by: Akhilesh Dhar Dubey on 27/9/15.<br/>
 * Email id: akhilesh.dubey@tothenew.com<br/>
 * Skype id: akhileshdubey91
 */
public class AppUtility {

    public static Point getDeviceDimension(Context context) {
        if (context == null) {
            return new Point();
        }
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        return size;
    }

    public boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) || ((connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTING)))
            return true;
        else
            return (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) || ((connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTING));
    }

}
