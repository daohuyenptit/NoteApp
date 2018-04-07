package com.example.dell.noteapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Dell on 4/3/2018.
 */

public class SharedPreference {

    public static void savePinCode(Context context,String name,String code){
        SharedPreferences sharedPreference= context.getSharedPreferences("MyShare",context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreference.edit();
        editor.putString(name,code);
        editor.commit();
    }
    public static String getPinCode(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("MyShare",context.MODE_PRIVATE);
        return  sharedPreferences.getString("savePin","");
    }
}
