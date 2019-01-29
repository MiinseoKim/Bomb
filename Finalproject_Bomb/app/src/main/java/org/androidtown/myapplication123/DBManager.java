package org.androidtown.myapplication123;

/**
 * Created by 민서 on 2016-06-09.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {

    public DBManager(Context context) {
        super(context, "hwDB", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table ttPlus (id integer PRIMARY KEY autoincrement, lecture text, startNum integer, endNum integer,week integer, room text);");
        db.execSQL("create table listPlus (id integer PRIMARY KEY autoincrement, task text, subject text null, date integer, power integer, delay integer, checkPopup integer);");
        db.execSQL("create table totalScore (id integer PRIMARY KEY autoincrement, totalNumber integer,leftNumber integer, clearNumber integer, failNumber integer);");
        db.execSQL("INSERT INTO totalScore (id, totalNumber,leftNumber, clearNumber,failNumber) VALUES(1, 0,0, 0,0);");
        db.execSQL("INSERT INTO ttPlus (id, lecture, startNum, endNum, week, room) VALUES(1, \"과목을 선택하세요\",0 ,0, 0,\"\");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
