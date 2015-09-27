package NetworkingLib.listeners;

import android.content.Loader;

import NetworkingLib.models.HTTPResponse;

/**
 * <p/>
 * Project: <b>CodeForIndia2015</b><br/>
 * Created by: Akhilesh Dhar Dubey on 27/9/15.<br/>
 * Email id: 2akhileshdubey@gmail.com<br/>
 * Skype id: akhileshdubey91
 * <p/>
 */
public interface OnLoaderFinishedListener {

    void onLoaderFinished(Loader loader, HTTPResponse HTTPResponse);

}
