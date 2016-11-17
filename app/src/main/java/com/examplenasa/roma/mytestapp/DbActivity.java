package com.examplenasa.roma.mytestapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class DbActivity extends Activity implements OnClickListener {

    final String LOG_TAG = "myLogs";

    Button btnAdd, btnRead, btnClear, btnBack;
    EditText etUniversity, etTeacher, etComment;

    RatingBar ratingBar1, ratingBar2, ratingBar3, ratingBar4, ratingBar5;

    DBHelper dbHelper;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnRead = (Button) findViewById(R.id.btnRead);
        btnRead.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        etUniversity = (EditText) findViewById(R.id.etUniversity);
        etTeacher = (EditText) findViewById(R.id.etEmail);
        etComment = (EditText) findViewById(R.id.editComment);


        ratingBar1 = (RatingBar) findViewById(R.id.ratingBar1);
        ratingBar2 = (RatingBar) findViewById(R.id.ratingBar2);
        ratingBar3 = (RatingBar) findViewById(R.id.ratingBar3);
        ratingBar4 = (RatingBar) findViewById(R.id.ratingBar4);
        ratingBar5 = (RatingBar) findViewById(R.id.ratingBar5);
        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);

    }

    public void setNull(){
        etUniversity.setText("");
        etTeacher.setText("");
        etComment.setText("");


        ratingBar1.setRating(0);
        ratingBar2.setRating(0);
        ratingBar3.setRating(0);
        ratingBar4.setRating(0);
        ratingBar5.setRating(0);
    }

    @Override
    public void onClick(View v) {

        ContentValues cv = new ContentValues();

        String name = etUniversity.getText().toString();
        String email = etTeacher.getText().toString();

        String rating1 = String.valueOf(ratingBar1.getRating());
        String rating2 = String.valueOf(ratingBar2.getRating());
        String rating3 = String.valueOf(ratingBar3.getRating());
        String rating4 = String.valueOf(ratingBar4.getRating());
        String rating5 = String.valueOf(ratingBar5.getRating());

        String comment = etComment.getText().toString();

        SQLiteDatabase db = dbHelper.getWritableDatabase();


        switch (v.getId()) {
            case R.id.btnAdd:
                Log.d(LOG_TAG, "--- Insert in mytable: ---");


                cv.put("university", name);
                cv.put("teacher", email);
                cv.put("rating1",rating1);
                cv.put("rating2",rating2);
                cv.put("rating3",rating3);
                cv.put("rating4",rating4);
                cv.put("rating5",rating5);
                cv.put("comment",comment);

                long rowID = db.insert("mytable", null, cv);
                Log.d(LOG_TAG, "row inserted, ID = " + rowID);

                Toast.makeText(this, "Дані успішно додано", Toast.LENGTH_LONG).show();

                setNull();

                findViewById(R.id.layout_input).setVisibility(View.VISIBLE);
                findViewById(R.id.list_db).setVisibility(View.GONE);

                break;


            case R.id.btnRead:
                ArrayList list = new ArrayList();
                ListView lvMain = (ListView) findViewById(R.id.list_db);



                Log.d(LOG_TAG, "--- Rows in mytable: ---");

                Cursor c = db.query("mytable", null, null, null, null, null, null, null);


                if (c.moveToFirst()) {


                    int idColIndex = c.getColumnIndex("id");
                    int universeColIndex = c.getColumnIndex("university");
                    int teacherColIndex = c.getColumnIndex("teacher");
                    int rating1ColIndex = c.getColumnIndex("rating1");
                    int rating2ColIndex = c.getColumnIndex("rating2");
                    int rating3ColIndex = c.getColumnIndex("rating3");
                    int rating4ColIndex = c.getColumnIndex("rating4");
                    int rating5ColIndex = c.getColumnIndex("rating5");
                    int commentColIndex = c.getColumnIndex("comment");
                    do {

                        Log.d(LOG_TAG,
                                "ID = " + c.getInt(idColIndex) +
                                        ", university = " + c.getString(universeColIndex) +
                                        ", teacher = " + c.getString(teacherColIndex)+
                                        ", rating1 = " + c.getString(rating1ColIndex)+
                                        ", rating2 = " + c.getString(rating2ColIndex)+
                                        ", rating3 = " + c.getString(rating3ColIndex)+
                                        ", rating4 = " + c.getString(rating4ColIndex)+
                                        ", rating5 = " + c.getString(rating5ColIndex)
                        );

                        list.add(    "ID = " + c.getInt(idColIndex) +
                                ", Навчальний заклад: " + c.getString(universeColIndex) +
                                ", Викладач: " + c.getString(teacherColIndex)+
                                ", Оцінки = ( " + c.getString(rating1ColIndex)+
                                ", " + c.getString(rating2ColIndex)+
                                ", " + c.getString(rating3ColIndex)+
                                ", " + c.getString(rating4ColIndex)+
                                ", " + c.getString(rating5ColIndex)+" )"+
                                "\nКоментар: " + c.getString(commentColIndex));


                    } while (c.moveToNext());
                } else{
                    Log.d(LOG_TAG, "0 rows");}
                c.close();


                // адаптер
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, list);

                //адаптер списку
                lvMain.setAdapter(adapter);

                findViewById(R.id.layout_input).setVisibility(View.GONE);
                findViewById(R.id.list_db).setVisibility(View.VISIBLE);
                findViewById(R.id.btnBack).setVisibility(View.VISIBLE);
                setNull();
                break;

            case R.id.btnClear:
                Log.d(LOG_TAG, "--- Clear mytable: ---");
                int clearCount = db.delete("mytable", null, null);
                Log.d(LOG_TAG, "deleted rows count = " + clearCount);
                Toast.makeText(this, "Видалено " + clearCount +" записів", Toast.LENGTH_LONG).show();

                setNull();
                break;
            case  R.id.btnBack:
                findViewById(R.id.layout_input).setVisibility(View.VISIBLE);
                findViewById(R.id.list_db).setVisibility(View.GONE);
                findViewById(R.id.btnBack).setVisibility(View.GONE);
                break;
        }

        dbHelper.close();

    }



    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, "myDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            // ствоюєм таблицю
            db.execSQL("create table mytable ("
                    + "id integer primary key autoincrement,"
                    + "university text,"
                    + "teacher text,"
                    + "rating1 text,"
                    + "rating2 text,"
                    + "rating3 text,"
                    + "rating4 text,"
                    + "rating5 text,"
                    + "comment text"
                    +");");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        }
    }

}