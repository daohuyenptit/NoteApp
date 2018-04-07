package com.example.dell.noteapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AddActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener{
    NoteController controller;
    ImageView imgSelect;
    EditText edtDate, edtTime;
    EditText edtContent;
    int REQUEST_CODE_CAMERA = 200;
    int REQUEST_CODE_GALLERY = 250;
    DatePickerDialog datePickerDialog;
    android.support.v7.widget.Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView imgcamera,imggallery;
    int i=0;
    ProgressDialog progressDialog;
    String image2;
    Uri uri;
    String path;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        checkPermission();
        controller = new NoteController(this);
        addControl();
        ActionBar();
        getImage();
        datePicker();
        timePicker();
        getTimeCurrent();
        progressDialog=new ProgressDialog(this);
        //add cmt


    }
    public void getTimeCurrent(){
        Date date=new Date();
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time=df.format(date);
        DateFormat df1=new SimpleDateFormat("E, MMMM d, yyyy");
        String t1=df1.format(date);
        edtDate.setText(t1);
        edtTime.setText(time.substring(11));
    }

    private void ActionBar() {
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void addControl() {
        imgSelect = findViewById(R.id.imgSelect);
        edtTime = findViewById(R.id.edtTime);
        edtDate = findViewById(R.id.edtDate);
        edtContent = findViewById(R.id.edtContent);
        toolbar = findViewById(R.id.toobar);
        drawerLayout = findViewById(R.id.drawer);
        navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        imgcamera=findViewById(R.id.imgcamera);
        imgcamera.setOnClickListener(this);
        imggallery=findViewById(R.id.imggallery);
        imggallery.setOnClickListener(this);


    }

    public void getImage() {//day đau phải, lúc dấy c có click j đâu, c chọn ảnh từu thư viện xong hiên lên mà,ak
        imgSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
                builder.setMessage("What kind of style do you want to?");
                builder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getImgGallery();


                    }
                });
                builder.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getImgCamera();


                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }
    public void getImgCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);

    }
    public void getImgGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_GALLERY);

    }

    public void datePicker() {
        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR); // current year
                int mMonth = calendar.get(Calendar.MONTH); // current month
                int mDay = calendar.get(Calendar.DAY_OF_MONTH); // current day
                datePickerDialog = new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        edtDate.setText("" + i + "-" + (i1 + 1) + "-" + i2);

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

    }

    public void timePicker() {
        edtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int mi = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        edtTime.setText(i + ":" + i1);

                    }
                }, hour, mi, true);
                timePickerDialog.show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgSelect.setImageBitmap(bitmap);
            uri=data.getData();
            path=uri.toString();
        }
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
             uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgSelect.setImageBitmap(bitmap);
                 path=uri.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1001);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1001) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                checkPermission();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.mnuAdd:
                addNote();

                return true;


        }
        return super.onOptionsItemSelected(item);
    }
    public void addNote(){


        if(imgSelect.getDrawable() ==null){
            imgSelect.setImageResource(R.drawable.image_default);
        }
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imgSelect.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        //
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        String date = edtDate.getText().toString().trim() + " " + edtTime.getText().toString().trim();
//        Date time = null;
//        try {
//            time = df.parse(date);
//            Note note = new Note(path, edtContent.getText().toString(), time);
//            controller.insertNote(note);
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//
//        Intent intent = new Intent();
//        setResult(100, intent);
//        finish();


        new MyAsyntask().execute(bitmap);


    }
     class MyAsyntask extends AsyncTask<Bitmap,Void,String>{
//        String image;
//        public MyAsyntask(String image) {
//            this.image = image;
//        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog.show();

        }

        @Override
        protected String  doInBackground(Bitmap... bitmap) {
            String a=null;
            try{
                a=saveToInternalStorage(bitmap[0],System.currentTimeMillis()+"");
               Log.i("ido", saveToInternalStorage(bitmap[0],System.currentTimeMillis()+""));
                return saveToInternalStorage(bitmap[0],System.currentTimeMillis()+"");

            }catch (Exception e){
                e.printStackTrace();

            }
            return null;

        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String date = edtDate.getText().toString().trim() + " " + edtTime.getText().toString().trim();
            Date time = null;
            try {
                time = df.parse(date);
                Note note = new Note(aVoid, edtContent.getText().toString(), time);
//                Log.i("img",image1+"");
                controller.insertNote(note);
//                progressDialog.dismiss();

            } catch (ParseException e) {
                e.printStackTrace();
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }


            Intent intent = new Intent();
            setResult(100, intent);
            finish();

        }


    }

    private String saveToInternalStorage(Bitmap bitmapImage,String profile){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
//        File directory = Environment.getDataDirectory();
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        // Create imageDir
        File mypath=new File(directory,profile+".jpeg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_lock:
                String pincode=SharedPreference.getPinCode(AddActivity.this);
                PinCode.setPinCode(AddActivity.this,pincode);
                break;
            case R.id.nav_setting:
                startActivity(new Intent(AddActivity.this,SettingActivity.class));
                break;
            case R.id.nav_home:
                startActivity(new Intent(AddActivity.this,NoteListActivity.class));
                break;
        }
        drawerLayout.closeDrawers();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgcamera:
                getImgCamera();
                break;
            case R.id.imggallery:
                getImgGallery();
                break;

        }

    }
}
