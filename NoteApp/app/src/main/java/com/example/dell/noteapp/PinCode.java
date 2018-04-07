package com.example.dell.noteapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by Dell on 4/5/2018.
 */

public class PinCode
{
    public static void setPinCode(final Context context1, final String pincode){
        final android.support.v7.app.AlertDialog.Builder builder=new android.support.v7.app.AlertDialog.Builder(context1);
        builder.setTitle("Enter PIN code");
        final EditText input = new EditText(context1);
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(input.getText().toString().equals(pincode)){
                    if(context1 instanceof MainActivity){
                        InputMethodManager imm = (InputMethodManager)context1.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                        context1.startActivity(new Intent(context1,NoteListActivity.class));

                        dialogInterface.dismiss();
                    }else{
                        dialogInterface.dismiss();
                    }

                }else{
                    setPinCode(context1,pincode);
                }

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setPinCode(context1,pincode);
            }
        });
        android.support.v7.app.AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    public static void dismissKeyboard(Context context,EditText myEditText) {
        // Check if no view has focus:

    }
}
