package com.example.socialandorid.linkedin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialandorid.R;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

/**
 * Created by ayushgarg on 03/06/16.
 */
public class LinkedinLoginImpl {

    private ILinkedinListner iLinkedinListner;

    private final Activity activity;
    private static final String host = "api.linkedin.com";
    private static final String topCardUrl = "https://" + host + "/v1/people/~:" +
            "(email-address,formatted-name,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";

    public LinkedinLoginImpl(Activity activity) {
        this.activity = activity;
    }

    public void setOnILinkedinListner(ILinkedinListner listner){
        iLinkedinListner = listner;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LISessionManager.getInstance(activity).onActivityResult(activity,
                requestCode, resultCode, data);
        getUserData();
    }

    public void login_linkedin(){
        LISessionManager.getInstance(activity).init(activity, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                // Toast.makeText(getApplicationContext(), "success" + LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAuthError(LIAuthError error) {
                Toast.makeText(activity, "failed " + error.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }, true);
    }

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    public void getUserData(){
        APIHelper apiHelper = APIHelper.getInstance(activity);
        apiHelper.getRequest(activity, topCardUrl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {
                    iLinkedinListner.onSuccess(result.getResponseDataAsJson());
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onApiError(LIApiError error) {
                // ((TextView) findViewById(R.id.error)).setText(error.toString());
            }
        });
    }
}
