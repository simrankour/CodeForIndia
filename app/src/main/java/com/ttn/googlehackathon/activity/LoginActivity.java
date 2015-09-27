package com.ttn.googlehackathon.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.ttn.googlehackathon.MyApplication;
import com.ttn.googlehackathon.R;
import com.ttn.googlehackathon.utility.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import SocialIntegrationLib.facebook.SIFacebook;

/**
 * Project: <b>GoogleHackathon</b><br/>
 * Created by: Akhilesh Dhar Dubey on 27/9/15.<br/>
 * Email id: akhilesh.dubey@tothenew.com<br/>
 * Skype id: akhileshdubey91
 */
public class LoginActivity extends Activity {


    private Button buttonLogin;
    private SIFacebook facebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_screen);

        buttonLogin = (Button) findViewById(R.id.button_login);

        facebookLoginNEW();
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (facebook != null && facebook.isFacebookRequest(requestCode))
            facebook.onActivityResult(requestCode, resultCode, data);
    }

    private void facebookLoginNEW() {
        ArrayList<String> permissions = new ArrayList<String>();
        permissions.add("public_profile");
        permissions.add("email");

        facebook = SIFacebook.getInstance(this);
        facebook.addFacebookPermissions(permissions);

        buttonLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                facebook.login(new FacebookCallback<LoginResult>() {

                                   @Override
                                   public void onSuccess(final LoginResult loginResult) {
                                       if (facebook.getUserProfileDetail() != null)
                                           Log.e("Facebook: ", "Login success " + facebook.getUserProfileDetail().getFirstName());
                                       final AccessToken accessToken = loginResult.getAccessToken();

                                       facebook.newGraphMeRequest(accessToken, null, new GraphRequest.GraphJSONObjectCallback() {
                                           @Override
                                           public void onCompleted(
                                                   JSONObject object,
                                                   GraphResponse response) {

                                               Log.v("facebook ", response.toString());
                                               try {
                                                   String id = object.getString("id");
                                                   SharedPreferences sharedPreferences = getSharedPreferences(AppConstant.PREF_LOGIN_FILE_NAME, Activity.MODE_PRIVATE);
                                                   SharedPreferences.Editor editor = sharedPreferences.edit();
                                                   editor.putString("facebook_id", id);
                                                   editor.commit();
                                               } catch (JSONException e) {
                                                   e.printStackTrace();
                                               }

                                               requestFacebookLogin();

                                           }
                                       });
                                   }

                                   @Override
                                   public void onCancel() {
                                       Log.e("Facebook: ", "Login cancel");
                                       Toast toast = MyApplication.getCustomToast(LoginActivity.this, "Facebook login cancelled.");
                                       toast.show();
                                   }

                                   @Override
                                   public void onError(FacebookException e) {
                                       Log.e("Facebook: ", "Login onError" + e);
                                       Toast toast = MyApplication.getCustomToast(LoginActivity.this, "Facebook login, Some error occurred.");
                                       toast.show();
                                   }
                               }
                );
            }
        });
    }

    private void requestFacebookLogin() {
        startActivity(new Intent(LoginActivity.this, HomeNavigationActiviy.class));
        finish();
    }
}
