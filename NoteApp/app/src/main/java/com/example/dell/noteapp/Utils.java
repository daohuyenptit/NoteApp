package com.example.dell.noteapp;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Dell on 4/8/2018.
 */

public class Utils {


    public static String saveToInternalStorage(Bitmap bitmapImage, String profile,Context context){
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
//        File directory = Environment.getDataDirectory();
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        // Create imageDir
        File mypath=new File(directory,profile+".jpeg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }
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
