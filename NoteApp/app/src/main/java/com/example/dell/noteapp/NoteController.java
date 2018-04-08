package com.example.dell.noteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Dell on 3/30/2018.
 */

public class NoteController extends SQLiteDataController {
    public NoteController(Context con) {
        super(con);
        try {
            this.createDataBase();
            openDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public boolean insertNote(Note note) {
        try {
//            openDataBase();
            ContentValues contentValues=new ContentValues();
            contentValues.put("image",note.getImage());
            contentValues.put("content",note.getContent());
            SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date=note.getTime();
            String ngay=df.format(date);
            contentValues.put("time",ngay);
            database.insert("tblNote",null,contentValues);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            close();
        }
        return false;
    }
    public void editNote(Note note){
//        openDataBase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("image",note.getImage());
        contentValues.put("content",note.getContent());
        contentValues.put("image",note.getImage());
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date=note.getTime();
        String ngay=df.format(date);
        contentValues.put("time",ngay);
        database.update("tblNote",contentValues,"id="+note.getId(),null);
    }
    public void deleteNote(int id){
        try{
//            openDataBase();
            database.delete("tblNote","id="+id,null);

        }catch (Exception e){

        }finally {
//            close();
        }

    }
    public ArrayList<Note> getAll(){
        ArrayList<Note> notes=new ArrayList<>();
        try {
//            openDataBase();

            Cursor cursor = database.rawQuery("Select * From tblNote", null);
            // Duyệt lấy ra các bản ghi
            while (cursor.moveToNext()) {
                int id=cursor.getInt(0);
//                byte[] image=cursor.getBlob(1);
                String image=cursor.getString(1);
                String content=cursor.getString(2);
                String ngayTao = cursor.getString(3);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date ngayTaoDate = dateFormat.parse(ngayTao);


                Note note=new Note(id,image,content,ngayTaoDate);
                notes.add(note);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
//            close();
        }
        Log.i("size",notes.size()+"");
        return notes;
    }

//    public void createDataBase() {
//        try {
//            isCreatedDatabase();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public void createDataBase() throws IOException{

        boolean dbExist = checkExistDataBase();
        SQLiteDatabase db_Read = null;

        if(dbExist){
//do nothing - database already exist
        }else{

//By calling this method and empty database will be created into the default system path
//of your application so we are gonna be able to overwrite that database with our database.
            db_Read = this.getReadableDatabase();
            db_Read.close();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }
}
