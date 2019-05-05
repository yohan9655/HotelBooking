package com.example.hotelbooking;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    EditText name,iden,add,num,room,email,age;
    TextInputLayout t;
    EditText na;
    RadioButton rd1,rd2;
    CheckBox ch1,ch2,ch3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        if (Build.VERSION.SDK_INT >= 21)
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black_overlay));

        getWindow().setEnterTransition(new Explode());
        getWindow().setExitTransition(new Explode());

        //LayoutInflater inflater = LayoutInflater.from(this);
        //View v = inflater.inflate(R.layout.activity_main2, null);


        //t = v.findViewById(R.id.input_layout_name);
        name = findViewById(R.id.name);
        iden = findViewById(R.id.iden);
        add = findViewById(R.id.address);
        num = findViewById(R.id.number);
        room = findViewById(R.id.room_number);
        email = findViewById(R.id.Email_ID);
        age = findViewById(R.id.age);
        rd1 = findViewById(R.id.radioButton1);
        rd2 = findViewById(R.id.radioButton2);
        ch1 = findViewById(R.id.checkBox);
        ch2 = findViewById(R.id.checkBox2);
        ch3 = findViewById(R.id.checkBox3);

        try {
            SQLiteDatabase myDataBase = this.openOrCreateDatabase("Users", MODE_PRIVATE, null);
            //myDataBase.execSQL("DROP TABLE CheckIn");
            myDataBase.execSQL("CREATE TABLE IF NOT EXISTS room (room_type VARCHAR NOT NULL, number INTEGER PRIMARY KEY, price INTEGER NOT NULL)");
            myDataBase.execSQL("CREATE TABLE IF NOT EXISTS CheckIn (Name VARCHAR, gender CHAR CHECK( gender IN ('M', 'F', 'T') ), address VARCHAR, age INT(3),id INTEGER PRIMARY KEY, contact_no INTEGER, room_no INTEGER, email VARCHAR, FOREIGN KEY(room_no) REFERENCES room(room_no))");
            myDataBase.execSQL("CREATE TABLE IF NOT EXISTS facility (WiFi CHAR CHECK( WiFi IN('T','F')), free_breakfast CHAR CHECK( free_breakfast IN('T','F')), laundry CHAR CHECK( laundry IN('T','F')), activity_center CHECK( activity_center IN('T','F')), id INTEGER , FOREIGN KEY(id) REFERENCES CheckIn(id))");

            myDataBase.execSQL("INSERT INTO room VALUES('Single',1,1000)");
            myDataBase.execSQL("INSERT INTO room VALUES('Double',2,2000)");
            myDataBase.execSQL("INSERT INTO room VALUES('Double',3,2000)");
            myDataBase.execSQL("INSERT INTO room VALUES('Triple',4,4000)");
            myDataBase.execSQL("INSERT INTO room VALUES('Single',5,1000)");
            myDataBase.execSQL("INSERT INTO room VALUES('Double',6,2000)");
            myDataBase.execSQL("INSERT INTO room VALUES('Triple',7,4000)");
            myDataBase.execSQL("INSERT INTO room VALUES('Triple',8,4000)");
            myDataBase.execSQL("INSERT INTO room VALUES('Double',9,2000)");
            myDataBase.execSQL("INSERT INTO room VALUES('Single',10,1000)");

            Log.i("name", "success");
        }catch(Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG);
        }

    }

   public void submit(View vies) {
        try {
            SQLiteDatabase myEntry = this.openOrCreateDatabase("Users", MODE_PRIVATE, null);
           ContentValues contentValues = new ContentValues();
           ContentValues contentValues1 = new ContentValues();
            String s = name.getText().toString();
            contentValues.put("Name",name.getText().toString());
            if(rd1.isChecked())
                contentValues.put("gender","M");
            else if(rd2.isChecked())
                contentValues.put("gender","M");
            contentValues.put("address",add.getText().toString());
            contentValues.put("age",Integer.parseInt(age.getText().toString()));
            contentValues.put("contact_no",Integer.parseInt(num.getText().toString()));
            contentValues.put("room_no",Integer.parseInt(room.getText().toString()));
            contentValues.put("email",email.getText().toString());
            contentValues.put("id",Integer.parseInt(iden.getText().toString()));

            if(ch1.isActivated())
                contentValues1.put("activity_center", "T");
            else
                contentValues1.put("activity_center", "F");
            if(ch2.isActivated())
                contentValues1.put("laundry", "T");
            else
                contentValues1.put("laundry", "F");
            if(ch3.isActivated())
                contentValues1.put("free_breakfast", "T");
            else
                contentValues1.put("free_breakfast", "F");
            contentValues1.put("id",Integer.parseInt(iden.getText().toString()));

            long q =  myEntry.insert("CheckIn",null,contentValues);
            long a = myEntry.insert("facility", null, contentValues1 );

            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            myEntry.close();
        } catch (Exception e)
        {
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
            Log.i("name", e.toString());
        }
        MainActivity.adapter.notifyDataSetChanged();
        //MainActivity.
        finish();

    }

}
