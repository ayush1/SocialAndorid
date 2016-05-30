package com.example.socialandorid;

import com.google.android.gms.plus.model.people.Person;

/**
 * Created by Hisham on 30/05/16.
 */
public interface IGPlus {
    void onSuccess(Person person);

    void onFail(String reason);
}
