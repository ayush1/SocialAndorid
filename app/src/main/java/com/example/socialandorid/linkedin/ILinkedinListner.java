package com.example.socialandorid.linkedin;

import org.json.JSONObject;

/**
 * Created by ayushgarg on 03/06/16.
 */
public interface ILinkedinListner {

    void onSuccess(JSONObject jsonObject);
    void onFail(String reason);
}
