package com.example.socialandorid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.model.people.Person;


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
