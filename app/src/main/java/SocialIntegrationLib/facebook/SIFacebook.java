package SocialIntegrationLib.facebook;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * Project: <b>CodeForIndia2015</b><br/>
 * Created by: Akhilesh Dhar Dubey on 27/9/15.<br/>
 * Email id: 2akhileshdubey@gmail.com<br/>
 * Skype id: akhileshdubey91
 * <p/>
 */

public class SIFacebook {

    private static SIFacebook siFacebook;
    private static Activity activity;
    private static CallbackManager callbackManager;
    private final String APPLICATION_ID_PROPERTY = "com.facebook.sdk.ApplicationId";
    private final String TAG = "siFacebook";
    private ArrayList<String> facebookPermissions = new ArrayList<>();

    private SIFacebook() {
        try {
            getHashKey("com.ttn.googlehackathon");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static SIFacebook getInstance(Activity activity1) {

        if (activity1 == null)
            throw new NullPointerException("Activity instance cannot be null");
        activity = activity1;
        if (siFacebook == null) {
            FacebookSdk.sdkInitialize(activity.getApplicationContext());
            siFacebook = new SIFacebook();
        }
        if (callbackManager == null) {
            callbackManager = CallbackManager.Factory.create();
        }

        return siFacebook;
    }

    public static String getFacebookCoverURL(String id) {
        return "https://graph.facebook.com/" + id + "?fields=cover";
    }

    public static void logout() {
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.logOut();
    }

    public void setFragment(LoginButton loginButton, android.support.v4.app.Fragment fragment) {
        loginButton.setFragment(fragment);
    }

    public void login(LoginButton loginButton, FacebookCallback<LoginResult> loginResultFacebookCallback) throws NullPointerException {
        if (callbackManager == null)
            callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(getFacebookPermissions());
        loginButton.registerCallback(callbackManager, loginResultFacebookCallback);

    }

    public void login(FacebookCallback<LoginResult> loginResultFacebookCallback) {

        if (loginResultFacebookCallback == null) {
            Log.e(TAG, "Please register SIFacebookListener before calling login().");
            return;
        }

        if (!isActivityDefined()) {
            Log.e(TAG, "Please define com.facebook.LoginActivity in AndroidManifest.xml as <activity android:name=\"com.facebook.FacebookActivity\" />.");
            return;
        }

        if (!isMetadataDefined()) {
            Log.e(TAG, "Please provide Application ID in AndroidManifest.xml <meta-data android:name=\"com.facebook.sdk.ApplicationId\" android:value=\"@string/app_id\" /> inside <application>.");
            return;
        }

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, loginResultFacebookCallback);
        LoginManager.getInstance().logInWithReadPermissions(activity, getFacebookPermissions());
    }

    public void newGraphMeRequest(final AccessToken accessToken, Bundle parameters, GraphRequest.GraphJSONObjectCallback graphJSONObjectCallback) {

        GraphRequest request = GraphRequest.newMeRequest(
                accessToken, graphJSONObjectCallback);

        if (parameters != null)
            request.setParameters(parameters);
        request.executeAsync();

    }

    public void newGraphPathRequest(final AccessToken accessToken, String graphPath, Bundle parameters, GraphRequest.Callback graphCallback) {

        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken, graphPath, graphCallback);

        if (parameters != null)
            request.setParameters(parameters);
        request.executeAsync();

    }

    public void getMutualFriends(String otherPersonId, GraphRequest.Callback graphRequestCallback) {
        String graphPath = "/" + otherPersonId + "/";

        Bundle parameters = new Bundle();
        parameters.putString("fields", "context.fields(mutual_friends)");

        newGraphPathRequest(AccessToken.getCurrentAccessToken(), graphPath, parameters, graphRequestCallback);

    }

    public void appInviteDialog(String appLinkUrl, String previewImageUrl, FacebookCallback<AppInviteDialog.Result> facebookCallback) {

        AppInviteDialog appInviteDialog = new AppInviteDialog(activity);
        if (AppInviteDialog.canShow()) {
            AppInviteContent.Builder content = new AppInviteContent.Builder();
            content.setApplinkUrl(appLinkUrl);
            if (previewImageUrl != null)
                content.setPreviewImageUrl(previewImageUrl);

            AppInviteContent appInviteContent = content.build();

            if (callbackManager == null)
                callbackManager = CallbackManager.Factory.create();

            appInviteDialog.registerCallback(callbackManager, facebookCallback);
            AppInviteDialog.show(activity, appInviteContent);
        }
    }

    public ArrayList<String> getFacebookPermissions() {
        return facebookPermissions;
    }

    public void addFacebookPermissions(String facebookPermission) {
        this.facebookPermissions.add(facebookPermission);
    }

    public void addFacebookPermissions(List<String> facebookPermissions) {
        this.facebookPermissions.addAll(facebookPermissions);
    }

    public String getHashKey(String appPackageName) throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        PackageInfo info = activity.getPackageManager().getPackageInfo(
                appPackageName,
                PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            return Base64.encodeToString(md.digest(), Base64.DEFAULT);
        }

        return null;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public boolean isFacebookRequest(int requestCode) {
        return (FacebookSdk.isFacebookRequestCode(requestCode));
    }

    public void openFacebookUserProfile(Activity activity, String userId) {
        Intent intent;
        try {
            activity.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + userId));
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + userId));
        }
        activity.startActivity(intent);
    }

    public Profile getUserProfileDetail() {
        return Profile.getCurrentProfile();
    }

    public URL getProfilePictureUrl(String userId) {
        try {
            return new URL("https://graph.facebook.com/" + userId
                    + "/picture?type=small");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isMetadataDefined() {
        ApplicationInfo ai = null;
        try {
            ai = activity.getPackageManager().getApplicationInfo(
                    activity.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        String applicationId = null;
        try {
            if (ai != null) {
                applicationId = ai.metaData.getString(APPLICATION_ID_PROPERTY);
            }
        } catch (Exception ignored) {
        }

        return applicationId != null;
    }

    private boolean isActivityDefined() {
        Intent intent = new Intent(activity, FacebookActivity.class);
        List<ResolveInfo> list = activity.getPackageManager()
                .queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public void shareLink(String link, String name, String caption,
                          String description, String pictureUrl) {
    }

    public void sharePhoto(Bitmap image, String description) {
    }

    public void sharePhoto(File image, String description)
            throws FileNotFoundException {
    }

    public void shareVideo(File video, String description)
            throws FileNotFoundException {

    }

    public void shareMessage(String message, String link, String linkName,
                             String linkDescription, String linkPicture) {
    }

    public void shareMessage(String message) {
    }

    private interface Permissions {
        String PUBLISH_ACTIONS = "publish_actions";
        // Open Graph Permissions
        String USER_ACTIONS_MUSIC = "user_actions.music";
        String USER_ACTIONS_NEWS = "user_actions.news";
        String USER_ACTIONS_VIDEO = "user_actions.video";
        String USER_ACTIONS_APP_NAMESPACE = "user_actions:" + "APP_NAMESPACE";

        // Email
        String EMAIL = "email";
        // Extented Profile Properties
        String USER_ABOUT_ME = "user_about_me";
        String USER_ACTIVITIES = "user_activities";
        String USER_BIRTHDAY = "user_birthday";
        String USER_EDUCATION_HISTORY = "user_education_history";
        String USER_EVENTS = "user_events";
        String USER_GROUPS = "user_groups";
        String USER_HOMETOWN = "user_hometown";
        String USER_INTERESTS = "user_interests";
        String USER_LIKES = "user_likes";
        String USER_LOCATION = "user_location";
        String USER_PHOTOS = "user_photos";
        String USER_RELATIONSHIPS = "user_relationships";
        String USER_RELATIONSHIP_DETAILS = "user_relationship_details";
        String USER_RELIGION_POLITICS = "user_religion_politics";
        String USER_STATUS = "user_status";
        String USER_VIDEOS = "user_videos";
        String USER_WEBSITE = "user_website";
        String USER_WORK_HISTORY = "user_work_history";

        // Extended permissions - Read
        String READ_FRIENDLISTS = "read_friendlists";
        String READ_INSIGHTS = "read_insights";
        String READ_MAILBOX = "read_mailbox";
        String READ_STREAM = "read_stream";
        // Extended permissions - Publish
        String MANAGE_NOTIFICATIONS = "manage_notfications";
        String RSVP_EVENTS = "rsvp_events";

        // Pages Permissions
        String MANAGE_PAGES = "manage_pages";
        String READ_PAGE_MAILBOXES = "read_page_mailboxes";
        // Open Graph Permissions
        String USER_ACTIONS_BOOKS = "user_actions.books";
        String USER_ACTIONS_FITNESS = "user_actions.fitness";

        // User Permissions
        String PUBLIC_PROFILE = "public_profile";
        // Friends
        String USER_FRIENDS = "user_friends";
        // Extented Profile Properties
        String USER_TAGGED_PLACES = "user_tagged_places";
    }

    public interface GraphPath {

        String FRIENDS = "friends";
        String TAGGABLE_FRIENDS = "taggable_friends";
        String INVITABLE_FRIENDS = "invitable_friends";
        String ALBUMS = "albums";
        String SCORES = "scores";
        String APPREQUESTS = "apprequests";
        String FEED = "feed";
        String PHOTOS = "photos";
        String VIDEOS = "videos";
        String CHECKINS = "checkins";
        String EVENTS = "events";
        String GROUPS = "groups";
        String POSTS = "posts";
        String COMMENTS = "comments";
        String LIKES = "likes";
        String LINKS = "links";
        String STATUSES = "statuses";
        String TAGGED = "tagged";
        String ACCOUNTS = "accounts";
        String BOOKS = "books";
        String MUSIC = "music";
        String FAMILY = "family";
        String MOVIES = "movies";
        String GAMES = "games";
        Object NOTIFICATIONS = "notifications";
        String TELEVISION = "television";
        String OBJECTS = "objects";

    }
}
