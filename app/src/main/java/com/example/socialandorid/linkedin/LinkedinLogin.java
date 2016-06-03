package com.example.socialandorid.linkedin;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialandorid.R;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ayushgarg on 01/06/16.
 */
public class LinkedinLogin extends AppCompatActivity implements ILinkedinListner {

    private LinkedinLoginImpl linkedinLoginimpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linkedin_login);
        linkedinLoginimpl = new LinkedinLoginImpl(LinkedinLogin.this);
        linkedinLoginimpl.login_linkedin();
        linkedinLoginimpl.setOnILinkedinListner(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        linkedinLoginimpl.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        Log.e("Response: ", "onSuccess: " + jsonObject);
    }

    @Override
    public void onFail(String reason) {

    }
}
