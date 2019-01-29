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
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class hwPlus extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private EditText et_name;
    private Spinner et_hwsubject;
    private EditText et_date;
    private RadioGroup rg_Power,rg_Possible;
    private Integer n_Power, n_Delay;

    List list;
    DatePicker mDate;
    SQLiteDatabase sqlitedb;
    DBManager dbmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hw_plus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //spinner
        dbLecture();
        // 스피너 객체생성
        Spinner s = (Spinner) findViewById(R.id.et_hwSubject);
        ArrayAdapter dataAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(dataAdapter);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    public void dbLecture(){
        list = new ArrayList();
        // 디비에접속해서 데이터를 가져와 커서에 넣는다.
        dbmanager = new DBManager(this);
        sqlitedb = dbmanager.getReadableDatabase();
        Cursor c = sqlitedb.query("ttPlus", null, "lecture is not null", null, null, null, null, null);
        // 안드로이드 특성상 앱/데이터의 life cycle을 지키기 위해 startmanagingcursor를 호출한다.
        startManagingCursor(c);
        // create the string array.
        while(c.moveToNext()){
            String sub = c.getString((c.getColumnIndex("lecture")));
            list.add(sub);
        }
    }


    //EditText로 작성한 텍스트를 받아옴
    public void addDB(View v){

        et_name = (EditText)findViewById(R.id.et_hwName);
        et_hwsubject = (Spinner)findViewById(R.id.et_hwSubject);
        et_date = (EditText)findViewById(R.id.dp_hwDate);
        rg_Power = (RadioGroup)findViewById(R.id.rg_123);
        n_Power=0;
        rg_Possible = (RadioGroup)findViewById(R.id.rg_Possible);
        n_Delay = 0;


        //받아온 텍스트를 스트링으로 변환
        String str_name = et_name.getText().toString();
        String str_hwsubject = et_hwsubject.getSelectedItem().toString();
        String str_date = et_date.getText().toString();

        int checkDate=0;
        if(!str_date.equals(""))
            checkDate = Integer.parseInt(str_date);
        int checkMonth = (checkDate%10000)/100;
        int checkDay = checkDate % 100;


        if(rg_Power.getCheckedRadioButtonId() == R.id.rdBtn_1){
            n_Power = 1;
        } else if(rg_Power.getCheckedRadioButtonId() == R.id.rdBtn_2){
            n_Power = 2;
        } else if(rg_Power.getCheckedRadioButtonId() == R.id.rdBtn_3){
            n_Power = 3;
        }

        if(rg_Possible.getCheckedRadioButtonId() == R.id.rdOk){
            n_Delay = 1;
        } else if(rg_Possible.getCheckedRadioButtonId() == R.id.rdNo){
            n_Delay = 2;
        }


        if(str_name.equals(""))
            Toast.makeText(hwPlus.this, "과제명을 입력해주세요!", Toast.LENGTH_LONG).show();
        else if(str_hwsubject.equals(""))
            Toast.makeText(hwPlus.this, "과목명을 입력해주세요!", Toast.LENGTH_LONG).show();

        else if(str_date.length() != 8){
            Toast.makeText(hwPlus.this, "연월일을 정확하게 입력해주세요!", Toast.LENGTH_LONG).show();
        }
        else if(checkMonth == 0 || checkMonth>12){
            Toast.makeText(hwPlus.this, "달을 정확하게 입력해주세요!", Toast.LENGTH_LONG).show();
        }
        else if(checkDay == 0 || checkDay > 31){
            Toast.makeText(hwPlus.this, "날짜를 정확하게 입력해주세요!", Toast.LENGTH_LONG).show();
        }
        else if (rg_Power.getCheckedRadioButtonId() != R.id.rdBtn_1 && rg_Power.getCheckedRadioButtonId() != R.id.rdBtn_2 && rg_Power.getCheckedRadioButtonId() != R.id.rdBtn_3){
            Toast.makeText(hwPlus.this, "중요도를 체크해주세요!", Toast.LENGTH_LONG).show();
        }
        else if(rg_Possible.getCheckedRadioButtonId() != R.id.rdOk && rg_Possible.getCheckedRadioButtonId() != R.id.rdNo){
            Toast.makeText(hwPlus.this, "추가제출여부를 선택해주세요!", Toast.LENGTH_LONG).show();
        }
        else{
            inputDB(str_name, str_hwsubject,str_date,n_Power,n_Delay);
        }



    }

    private void inputDB(String task, String subject, String date, int power, int delay){
        try {
            dbmanager = new DBManager(this);
            sqlitedb = dbmanager.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("task" , task);
            values.put("subject" , subject);
            values.put("date" , date);
            values.put("power", power);
            values.put("delay", delay);
            values.put("checkPopup",2);

            sqlitedb.insert("listPlus", null, values);

            sqlitedb.execSQL("update totalScore set totalNumber = totalNumber + 1 where id = 1;");
            sqlitedb.execSQL("update totalScore set leftNumber = leftNumber + 1 where id = 1;");

            sqlitedb.close();
            dbmanager.close();

            //디비 입력후에 리스트로 이동
            Intent myIntent = new Intent(getApplicationContext(), hwList.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityForResult(myIntent,5000);
            this.finish();

        }catch (SQLiteException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void close(View v){
        hwPlus.this.finish();
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