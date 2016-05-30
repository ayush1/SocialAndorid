package com.example.socialandorid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements IGPlus{

    SignInButton signIn_btn;
    ProgressDialog progress_dialog;
    private static final String TAG = "MainActivity";
    private GPlusLoginImpl gPlusLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress_dialog = new ProgressDialog(this);
        progress_dialog.setMessage("Signing in....");
        gPlusLogin = new GPlusLoginImpl(MainActivity.this);
        gPlusLogin.buidNewGoogleApiClient();
        gPlusLogin.setOnIGplusListener(this);

        signIn_btn = (SignInButton) findViewById(R.id.sign_in_button);
        signIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gPlusLogin.gPlusSignIn();
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        gPlusLogin.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        gPlusLogin.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        gPlusLogin.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gPlusLogin.onResume();
    }

    @Override
    public void onSuccess(Person person) {
        Log.e(TAG, "onSuccess: " + person.toString() );
    }

    @Override
    public void onFail(String reason) {

    }
}
