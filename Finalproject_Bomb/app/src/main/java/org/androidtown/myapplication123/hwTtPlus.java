package org.androidtown.myapplication123;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class hwTtPlus extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener{

    SQLiteDatabase sqlitedb;
    DBManager dbmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hw_tt_plus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }
    public void addDB(View v){
        EditText et_subject = (EditText)findViewById(R.id.et_subject);   //EditText로 작성한 텍스트를 받아옴
        EditText et_Room = (EditText)findViewById(R.id.et_Room);
        RadioGroup rg_week = (RadioGroup)findViewById(R.id.rg_week);

        Integer weekday=0;

        RadioGroup rg_stime = (RadioGroup)findViewById(R.id.rg_stime);

        Integer stime=0;

        RadioGroup rg_etime = (RadioGroup)findViewById(R.id.rg_etime);

        Integer etime=0;

        //받아온 텍스트를 스트링으로 변환
        String str_subject = et_subject.getText().toString();
        String str_room = et_Room.getText().toString();

        if(rg_week.getCheckedRadioButtonId() == R.id.rdBtn_mon){
            weekday = 1;
        } else if(rg_week.getCheckedRadioButtonId() == R.id.rdBtn_tue){
            weekday = 2;
        } else if(rg_week.getCheckedRadioButtonId() == R.id.rdBtn_wed){
            weekday = 3;
        }else if(rg_week.getCheckedRadioButtonId() == R.id.rdBtn_thu){
            weekday = 4;
        }else if(rg_week.getCheckedRadioButtonId() == R.id.rdBtn_fri){
            weekday = 5;
        }

        if(rg_stime.getCheckedRadioButtonId() == R.id.srdBtn_1){
            stime = 1;
        } else if(rg_stime.getCheckedRadioButtonId() == R.id.srdBtn_2){
            stime = 2;
        }else if(rg_stime.getCheckedRadioButtonId() == R.id.srdBtn_3){
            stime = 3;
        }else if(rg_stime.getCheckedRadioButtonId() == R.id.srdBtn_4){
            stime = 4;
        }else if(rg_stime.getCheckedRadioButtonId() == R.id.srdBtn_5){
            stime = 5;
        }else if(rg_stime.getCheckedRadioButtonId() == R.id.srdBtn_6){
            stime = 6;
        }

        if(rg_etime.getCheckedRadioButtonId() == R.id.erdBtn_1){
            etime = 1;
        } else if(rg_etime.getCheckedRadioButtonId() == R.id.erdBtn_2){
            etime = 2;
        }else if(rg_etime.getCheckedRadioButtonId() == R.id.erdBtn_3){
            etime = 3;
        }else if(rg_etime.getCheckedRadioButtonId() == R.id.erdBtn_4){
            etime = 4;
        }else if(rg_etime.getCheckedRadioButtonId() == R.id.erdBtn_5){
            etime = 5;
        }else if(rg_etime.getCheckedRadioButtonId() == R.id.erdBtn_6){
            etime = 6;
        }

        if(str_subject.equals("")){
            Toast.makeText(hwTtPlus.this, "과목명을 입력해주세요!", Toast.LENGTH_LONG).show();
        }else if(str_room.equals("")){
            Toast.makeText(hwTtPlus.this, "강의실을 입력해주세요!", Toast.LENGTH_LONG).show();
        }else if(rg_week.getCheckedRadioButtonId() != R.id.rdBtn_mon && rg_week.getCheckedRadioButtonId() != R.id.rdBtn_tue
                && rg_week.getCheckedRadioButtonId() != R.id.rdBtn_wed && rg_week.getCheckedRadioButtonId() != R.id.rdBtn_thu
                &&rg_week.getCheckedRadioButtonId() != R.id.rdBtn_fri){
            Toast.makeText(hwTtPlus.this, "요일을 체크해주세요!", Toast.LENGTH_LONG).show();
        }else if(rg_stime.getCheckedRadioButtonId() != R.id.srdBtn_1 && rg_stime.getCheckedRadioButtonId() != R.id.srdBtn_2
                && rg_stime.getCheckedRadioButtonId() != R.id.srdBtn_3 && rg_stime.getCheckedRadioButtonId() != R.id.srdBtn_4
                && rg_stime.getCheckedRadioButtonId() != R.id.srdBtn_5 && rg_stime.getCheckedRadioButtonId() != R.id.srdBtn_6 ){
            Toast.makeText(hwTtPlus.this, "강의 시작 시간을 입력해주세요!", Toast.LENGTH_LONG).show();
        }else if(rg_etime.getCheckedRadioButtonId() != R.id.erdBtn_1 && rg_etime.getCheckedRadioButtonId() != R.id.erdBtn_2
                && rg_etime.getCheckedRadioButtonId() != R.id.erdBtn_3 && rg_etime.getCheckedRadioButtonId() != R.id.erdBtn_4
                && rg_etime.getCheckedRadioButtonId() != R.id.erdBtn_5 && rg_etime.getCheckedRadioButtonId() != R.id.erdBtn_6 ){
            Toast.makeText(hwTtPlus.this, "강의 종료 시간을 입력해주세요!", Toast.LENGTH_LONG).show();
        } else {

            int OK = 0;
            try {
                dbmanager = new DBManager(this);
                sqlitedb = dbmanager.getReadableDatabase();
                Cursor cursor = sqlitedb.query("ttPlus", null, "lecture is not null", null, null, null, null, null);

                //동일 시간표 입력 방지
                while (cursor.moveToNext()) {

                    int weekCheck = cursor.getInt(cursor.getColumnIndex("week"));
                    int startTime = cursor.getInt(cursor.getColumnIndex("startNum"));
                    int endTime = cursor.getInt(cursor.getColumnIndex("endNum"));

                    Log.e("시간표 디비 체크",weekday+" / "+stime+"~"+etime);
                    Log.e("시간표 디비 체크",weekCheck+" / "+startTime+"~"+endTime);

                    if ((weekday == weekCheck && stime == startTime) || (weekday == weekCheck && stime == endTime)
                            || (weekday == weekCheck && etime == startTime) || (weekday == weekCheck && etime == endTime)) {
                        OK = 0;
                        break;
                    } else {
                        OK = 1;
                    }

                    Log.e("OK값 확인",OK+"");

                }
                sqlitedb.close();
                dbmanager.close();
                //디비 입력후에 시간표로 이동

                if(OK == 1)
                    inputDB(stime, etime, weekday, str_subject, str_room);
                else
                    Toast.makeText(hwTtPlus.this, "시간표가 겹칩니다!", Toast.LENGTH_LONG).show();

            } catch (SQLiteException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }

    }

    private void inputDB(int StartTime, int endTime, int week, String subject, String room){
        try {
            dbmanager = new DBManager(this);
            sqlitedb = dbmanager.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("startNum" , StartTime);
            values.put("endNum" , endTime);
            values.put("week" , week);
            values.put("lecture", subject);
            values.put("room", room);

            sqlitedb.insert("ttPlus", null, values);
            sqlitedb.close();
            dbmanager.close();

            //디비 입력후에 시간표로 이동
            Intent myIntent = new Intent(getApplicationContext(), hwTT.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityForResult(myIntent,5000);
            this.finish();

        }catch (SQLiteException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public void close(View v){
        finish();
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.hwCal) {
            Intent intent = new Intent (getApplicationContext(), hwCal.class);
            intent.addFlags(intent.FLAG_ACTIVITY_SINGLE_TOP|intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);

        } else if (id == R.id.hwTt) {
            Intent intent = new Intent (getApplicationContext(), hwTT.class);
            intent.addFlags(intent.FLAG_ACTIVITY_SINGLE_TOP|intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        } else if (id == R.id.hwList) {
            Intent intent = new Intent (getApplicationContext(), hwList.class);
            intent.addFlags(intent.FLAG_ACTIVITY_SINGLE_TOP|intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        } else if (id == R.id.hwPlus) {
            Intent intent = new Intent (getApplicationContext(), hwPlus.class);
            intent.addFlags(intent.FLAG_ACTIVITY_SINGLE_TOP|intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
