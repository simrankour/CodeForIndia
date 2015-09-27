package NetworkingLib;

import android.util.Log;

import NetworkingLib.loader.LoaderHandler;

/**
 * <p/>
 * Project: <b>CodeForIndia2015</b><br/>
 * Created by: Akhilesh Dhar Dubey on 27/9/15.<br/>
 * Email id: 2akhileshdubey@gmail.com<br/>
 * Skype id: akhileshdubey91
 * <p/>
 */

public class Logger {

    /*
     Print error log in console
     */
    public static void e(String tag, String msg) {
        if (LoaderHandler.isLoggingEnabled()) {
            if (msg != null)
                Log.e(tag, msg);
        }
    }

    /*
    Print Debug log in console
     */
    public static void d(String tag, String msg) {
        if (LoaderHandler.isLoggingEnabled()) {
            if (msg != null)
                Log.d(tag, msg);
        }
    }

    /*
Print Information log in console
 */
    public static void i(String tag, String msg) {
        if (LoaderHandler.isLoggingEnabled()) {
            if (msg != null)
                Log.i(tag, msg);
        }
    }
}
