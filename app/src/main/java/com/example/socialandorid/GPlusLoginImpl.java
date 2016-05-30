package com.example.socialandorid;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;

/**
 * Created by Hisham on 30/05/16.
 */
public class GPlusLoginImpl implements GoogleApiClient.ConnectionCallbacks {

    private IGPlus igPlus;

    private static final int SIGN_IN_CODE = 0;
    private static final int PROFILE_PIC_SIZE = 120;
    private final Activity activity;
    GoogleApiClient google_api_client;
    GoogleApiAvailability google_api_availability;
    private ConnectionResult connection_result;
    private boolean is_intent_inprogress;
    private boolean is_signInBtn_clicked;
    private int request_code;

    public GPlusLoginImpl(Activity activity) {
        this.activity = activity;
    }

    public void setOnIGplusListener(IGPlus listener){
        igPlus = listener;
    }

    public void buidNewGoogleApiClient() {

        google_api_client = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        if (!connectionResult.hasResolution()) {
                            google_api_availability.getErrorDialog(activity, connectionResult.getErrorCode(), request_code).show();
                            return;
                        }

                        if (!is_intent_inprogress) {

                            connection_result = connectionResult;

                            if (is_signInBtn_clicked) {

                                resolveSignInError();
                            }
                        }

                    }
                })
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }

    public void gPlusSignIn() {
        if (!google_api_client.isConnecting()) {
            Log.d("user connected", "connected");
            is_signInBtn_clicked = true;
            resolveSignInError();

        }
    }

    public void gPlusSignOut() {
        if (google_api_client.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(google_api_client);
            google_api_client.disconnect();
            google_api_client.connect();
        }
    }

    private void resolveSignInError() {
        if (connection_result.hasResolution()) {
            try {
                is_intent_inprogress = true;
                connection_result.startResolutionForResult(activity, SIGN_IN_CODE);
                Log.d("resolve error", "sign in error resolved");
            } catch (IntentSender.SendIntentException e) {
                is_intent_inprogress = false;
                google_api_client.connect();
            }
        }
    }

    private void getProfileInfo() {

        try {

            if (Plus.PeopleApi.getCurrentPerson(google_api_client) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(google_api_client);
                igPlus.onSuccess(currentPerson);
                Plus.PeopleApi.loadVisible(google_api_client, null)
                        .setResultCallback(new ResultCallback<People.LoadPeopleResult>() {
                            @Override
                            public void onResult(People.LoadPeopleResult loadPeopleResult) {
                                if (loadPeopleResult.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
                                    PersonBuffer personBuffer = loadPeopleResult.getPersonBuffer();
                                    try {
                                        int count = personBuffer.getCount();
                                        for (int i = 0; i < count; i++) {
                                            Log.d("", "Display name: " + personBuffer.get(i).getDisplayName());
                                        }
                                    } finally {
                                        personBuffer.release();
                                    }
                                } else {
                                    Log.e("", "Error requesting visible circles: " + loadPeopleResult.getStatus());
                                }
                            }
                        });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void gPlusRevokeAccess() {
        if (google_api_client.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(google_api_client);
            Plus.AccountApi.revokeAccessAndDisconnect(google_api_client)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.d("MainActivity", "User access revoked!");
                            buidNewGoogleApiClient();
                            google_api_client.connect();
                        }

                    });
        }
    }


    public void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        // Check which request we're responding to
        if (requestCode == SIGN_IN_CODE) {
            request_code = requestCode;
            if (responseCode != Activity.RESULT_OK) {
                is_signInBtn_clicked = false;

            }

            is_intent_inprogress = false;

            if (!google_api_client.isConnecting()) {
                google_api_client.connect();
            }
        }
    }


    public void onStart() {
        google_api_client.connect();
    }


    public void onStop() {
        if (google_api_client.isConnected()) {
            google_api_client.disconnect();
        }
    }


    public void onResume() {
        if (google_api_client.isConnected()) {
            google_api_client.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        is_signInBtn_clicked = false;
        // Get user's information and set it into the layout
        getProfileInfo();
    }

    @Override
    public void onConnectionSuspended(int i) {
        google_api_client.connect();
    }
}
