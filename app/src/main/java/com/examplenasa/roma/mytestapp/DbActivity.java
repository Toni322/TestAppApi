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

    Button btnAdd, btnRead, btnClear;
    EditText etName, etTeacher;

    RatingBar ratingBar;

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

        etName = (EditText) findViewById(R.id.etName);
        etTeacher = (EditText) findViewById(R.id.etEmail);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar1);
        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);

    }


    @Override
    public void onClick(View v) {

        // создаем объект для данных
        ContentValues cv = new ContentValues();

        // получаем данные из полей ввода
        String name = etName.getText().toString();
        String email = etTeacher.getText().toString();
        float a = ratingBar.getRating();
        String rating = String.valueOf(a);
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        switch (v.getId()) {
            case R.id.btnAdd:
                Log.d(LOG_TAG, "--- Insert in mytable: ---");
                // подготовим данные для вставки в виде пар: наименование столбца - значение


                cv.put("name", name);
                cv.put("email", email);
                cv.put("rating",rating);

                // вставляем запись и получаем ее ID
                long rowID = db.insert("mytable", null, cv);
                Log.d(LOG_TAG, "row inserted, ID = " + rowID);

                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();

                findViewById(R.id.layout_input).setVisibility(View.VISIBLE);
                findViewById(R.id.list_db).setVisibility(View.GONE);

                break;


            case R.id.btnRead:
                String[] names = { "Иван", "Марья", "Петр", "Антон", "Даша", "Борис",
                        "Костя", "Игорь", "Анна", "Денис", "Андрей" };

                ArrayList list = new ArrayList();



                ListView lvMain = (ListView) findViewById(R.id.list_db);






                Log.d(LOG_TAG, "--- Rows in mytable: ---");
                // делаем запрос всех данных из таблицы mytable, получаем Cursor
                Cursor c = db.query("mytable", null, null, null, null, null, null, null);

                // ставим позицию курсора на первую строку выборки
                // если в выборке нет строк, вернется false
                if (c.moveToFirst()) {

                    // определяем номера столбцов по имени в выборке
                    int idColIndex = c.getColumnIndex("id");
                    int nameColIndex = c.getColumnIndex("name");
                    int emailColIndex = c.getColumnIndex("email");
                    int ratingColIndex = c.getColumnIndex("rating");
                    do {
                        // получаем значения по номерам столбцов и пишем все в лог
                        Log.d(LOG_TAG,
                                "ID = " + c.getInt(idColIndex) +
                                        ", name = " + c.getString(nameColIndex) +
                                        ", email = " + c.getString(emailColIndex)+
                                       ", rating = " + c.getString(ratingColIndex)
                        );

                        list.add(  "ID = " + c.getInt(idColIndex) +
                                ", name = " + c.getString(nameColIndex) +
                                ", email = " + c.getString(emailColIndex)+
                                ", rating = " + c.getString(ratingColIndex));

                        // переход на следующую строку
                        // а если следующей нет (текущая - последняя), то false - выходим из цикла
                    } while (c.moveToNext());
                } else
                    Log.d(LOG_TAG, "0 rows");
                c.close();


                // создаем адаптер
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, list);

                // присваиваем адаптер списку
                lvMain.setAdapter(adapter);
               findViewById(R.id.layout_input).setVisibility(View.GONE);
                findViewById(R.id.list_db).setVisibility(View.VISIBLE);

                break;
            case R.id.btnClear:
                Log.d(LOG_TAG, "--- Clear mytable: ---");
                // удаляем все записи
                int clearCount = db.delete("mytable", null, null);
                Log.d(LOG_TAG, "deleted rows count = " + clearCount);
                break;
        }
        // закрываем подключение к БД
        dbHelper.close();

    }



    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "myDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("create table mytable ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "email text,"
                    + "rating text"
                    +");");

            ContentValues cvv = new ContentValues();
            cvv.put("name", "NAMEEE");
            cvv.put("email", "MAIL");
            cvv.put("rating", "100");

            db.insert("mytable",null,cvv);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        }
    }

}