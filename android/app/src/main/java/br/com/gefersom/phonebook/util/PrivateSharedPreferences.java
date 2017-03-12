package br.com.gefersom.phonebook.util;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by me on 24/9/2016.
 */

public class PrivateSharedPreferences {

    static public SharedPreferences getPrivatePreferences(AppCompatActivity activity, String preferencesKey){
        return activity.getSharedPreferences(preferencesKey, AppCompatActivity.MODE_PRIVATE);
    }

}
