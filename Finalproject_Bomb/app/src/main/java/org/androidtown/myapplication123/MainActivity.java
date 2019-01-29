package org.androidtown.myapplication123;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class
MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button btn_tt;
    private Button btn_cal;
    private Button btn_plus;
    private Button btn_list;
    private Button manual;
    private Button menu;
    private TextView todo;
    private TextView clear;
    private TextView total;
    private ImageView grade;
    public static Activity mainContext;
    SQLiteDatabase sqlitedb;
    DBManager dbmanager;


    private customDialog mCustomDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainContext=MainActivity.this;
        startActivity(new Intent(this, SplashActivity.class));



        mCustomDialog = new customDialog(this,"[폭탄제거 실패]",leftListener,rightListener); // 버튼 이벤트

        for(int i = 0; i < 14; i++) {

            int[] check = new int[15];
            check = alarmDday();
            if (check[i] < 0) {
                mCustomDialog.show();
            }
        }

        alarmManager();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btn_cal = (Button)findViewById(R.id.btn_cal);
        btn_tt = (Button)findViewById(R.id.btn_tt);
        btn_plus = (Button)findViewById(R.id.btn_plus);
        btn_list = (Button)findViewById(R.id.btn_list);
        manual= (Button)findViewById(R.id.manual);
        menu=(Button)findViewById(R.id.drawer);
        todo=(TextView)findViewById(R.id.todo);
        clear=(TextView)findViewById(R.id.done);
        total=(TextView)findViewById(R.id.miss);
        grade=(ImageView)findViewById(R.id.grade);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        btn_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cal = new Intent(MainActivity.this, hwCal.class);
                startActivity(cal);
            }
        });
        btn_tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tt = new Intent(MainActivity.this, hwTT.class);
                startActivity(tt);

            }
        });
        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent plus = new Intent(MainActivity.this, hwPlus.class);
                startActivity(plus);
            }
        });
        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent list = new Intent(MainActivity.this, hwList.class);
                startActivity(list);
            }
        });

        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manual = new Intent(MainActivity.this, manual.class);
                startActivity(manual);
            }
        });
        grade.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("새로운 학기 시작");
                dialog.setMessage("모든 데이터를 초기화 시키겠습니까?");
                dialog.setNegativeButton("초기화", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reset();
                        onRestart();
                        dialog.dismiss();
                    }
                });
                dialog.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        DrawGrade();
    }

    public void DrawGrade(){
        try{
            dbmanager = new DBManager(MainActivity.this);
            sqlitedb = dbmanager.getWritableDatabase();

            Cursor cursor = sqlitedb.query("totalScore", null, "totalNumber is not null", null, null, null, null, null);

            double p = 1;

            double dT, dC;

            while(cursor.moveToNext()) {
                Integer c=cursor.getInt(cursor.getColumnIndex("clearNumber"));
                Integer l =cursor.getInt(cursor.getColumnIndex("leftNumber"));
                Integer t=cursor.getInt(cursor.getColumnIndex("totalNumber"));
                String totalNumber=Integer.toString(t);
                String clearNumber=Integer.toString(c);
                String leftNumber=Integer.toString(l);
                todo.setText(leftNumber);
                clear.setText(clearNumber);
                total.setText(totalNumber);

                BitmapDrawable aplus=(BitmapDrawable) getResources().getDrawable(R.drawable.aplus);
                BitmapDrawable a=(BitmapDrawable) getResources().getDrawable(R.drawable.a);
                BitmapDrawable bplus=(BitmapDrawable) getResources().getDrawable(R.drawable.bplus);
                BitmapDrawable b=(BitmapDrawable) getResources().getDrawable(R.drawable.b);
                BitmapDrawable cplus=(BitmapDrawable) getResources().getDrawable(R.drawable.cplus);
                BitmapDrawable ff=(BitmapDrawable) getResources().getDrawable(R.drawable.f);

                dT = (double)t;
                dC = (double)c;

                if(t != 0)
                    p = (dC/dT)*100.0;

                if(t == 0){
                    grade.setImageDrawable(aplus);
                }
                else if(t <= 6){
                    grade.setImageDrawable(a);
                }
                else {
                    if (p >= 95){
                        grade.setImageDrawable(aplus);
                        Log.e("수행률","퍼센트" +p);
                    }
                    if (p < 95 && p >= 85){
                        grade.setImageDrawable(a);
                        Log.e("수행률","퍼센트" +p);
                    }
                    if (p < 85 && p >= 75){
                        grade.setImageDrawable(bplus);
                        Log.e("수행률","퍼센트" +p);
                    }
                    if (p < 75 && p >= 65){
                        grade.setImageDrawable(b);
                        Log.e("수행률","퍼센트" +p);
                    }
                    if (p < 65 && p >= 50){
                        grade.setImageDrawable(cplus);
                        Log.e("수행률","퍼센트" +p);
                    }
                    else if (p < 50){
                        grade.setImageDrawable(ff);
                        Log.e("수행률","퍼센트" +p);
                    }
                }
            }
            cursor.close();
            sqlitedb.close();
            dbmanager.close();
            grade.invalidate();
        }
        catch (SQLiteException e){
            e.getMessage();
        }
    }

    public void reStart() {
        finish();
        startActivity(new Intent(MainActivity.this, MainActivity.class));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void reset(){
        try{
            dbmanager = new DBManager(MainActivity.this);
            sqlitedb = dbmanager.getWritableDatabase();

            String DeleteHW = "delete from listPlus ;";
            String DeleteTT= "delete from ttPlus;";
            String DeleteScore= "delete from totalScore;";
            String updateScore = "INSERT INTO totalScore (id, totalNumber,leftNumber, clearNumber,failNumber) VALUES(1, 0,0, 0,0);";
            String updateTT = "INSERT INTO ttPlus (id, lecture, startNum, endNum, week, room) VALUES(1, \"\",0 ,0, 0,\"\");";
            sqlitedb.execSQL(DeleteHW);
            sqlitedb.execSQL(DeleteTT);
            sqlitedb.execSQL(DeleteScore);
            sqlitedb.execSQL(updateScore);
            sqlitedb.execSQL(updateTT);

            sqlitedb.close();
            dbmanager.close();
            Toast.makeText(MainActivity.this, "초기화 완료! \n새로운 학기도 화이팅:)", Toast.LENGTH_LONG).show();
        }
        catch (SQLiteException e){
            e.getMessage();
        }
    }

    public int[] alarmDday(){

        int alarmCheck = -1;
        int[] alarmday = new int[15];


        int i = 0;

        try{
            dbmanager = new DBManager(this);
            sqlitedb = dbmanager.getReadableDatabase();
            Cursor cursor = sqlitedb.query("listPlus", null, "task is not null", null, null, null, null, null);

            while(cursor.moveToNext()){

                Integer date = cursor.getInt(cursor.getColumnIndex("date"));
                Integer checkPopup = cursor.getInt(cursor.getColumnIndex("checkPopup"));
                Log.e("알람디데이 확인",date+" // "+checkPopup);

                int dYear =1,dMonth=1,dDay=1;
                long d,t,r;

                int resultNumber=0;

                dYear = date / 10000;
                dMonth = (date % 10000)/100;
                dDay = date % 100;

                Calendar calendar =Calendar.getInstance();              //현재 날짜 불러옴

                Calendar dCalendar =Calendar.getInstance();
                dCalendar.set(dYear,dMonth-1, dDay);

                t=calendar.getTimeInMillis();                 //오늘 날짜를 밀리타임으로 바꿈
                d=dCalendar.getTimeInMillis();              //디데이날짜를 밀리타임으로 바꿈
                r=(d-t)/(24*60*60*1000);                 //디데이 날짜에서 오늘 날짜를 뺀 값을 '일'단위로 바꿈

                resultNumber=(int)r;

                alarmCheck = resultNumber;

                if(checkPopup == 0)
                    alarmday[i] = alarmCheck;
                else
                    alarmday[i] = 0;

                i++;

            }
            cursor.close();
            sqlitedb.close();
            dbmanager.close();
        }catch (SQLiteException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();

            alarmCheck = -1;
        }

        return alarmday;

    }
    // 다시보지않기 버튼 리스너

    private void alarmManager() {

        {

            try {
                dbmanager = new DBManager(this);
                sqlitedb = dbmanager.getReadableDatabase();
                Cursor cursor = sqlitedb.query("listPlus", null, "task is not null", null, null, null, null, null);

                AlarmManager mgrAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);
                ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();

                Calendar calendar = Calendar.getInstance();

                long now;
                long days;

                int result;

                Intent intent;
                int count = 0;
                long alarmTime = 0;

                while (cursor.moveToNext()) {

                    intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), count, intent, 0);

                    Integer date = cursor.getInt(cursor.getColumnIndex("date"));
                    String task = cursor.getString(cursor.getColumnIndex("task"));

                    int dYear = 1, dMonth = 1, dDay = 1;
                    long d, t;

                    int r;

                    dYear = date / 10000;
                    dMonth = (date % 10000) / 100;
                    dDay = date % 100;

                    Calendar dCalendar = Calendar.getInstance();

                    dCalendar.set(dYear, dMonth - 1, dDay);

                    t = calendar.getTimeInMillis();                 //오늘 날짜를 밀리타임으로 바꿈
                    d = dCalendar.getTimeInMillis();              //디데이날짜를 밀리타임으로 바꿈
                    r = (int) ((d - t) / (24 * 60 * 60 * 1000));                 //디데이 날짜에서 오늘 날짜를 뺀 값을 '일'단위로 바꿈

                    Log.e("리스트 알람 실제 디데이 확인", r + "일");

                    dCalendar.add(Calendar.DATE, -7);

                    alarmTime = dCalendar.getTimeInMillis();

                    now = calendar.getTimeInMillis();

                    // 디데이보다 7일전 날짜와 오늘 날짜를 비교해 디데이 계산
                    // 계산날짜가 플러스이면 남은 날짜 있음 (오늘 이후에 디데이 7일전 알람 셋팅됨)
                    // 계산날짜가 -2 ~ 0 사이이면 디데이 1일전 짜리 알람 설정함
                    // 계산날짜가 -4 ~ -2 사이이면 디데이 3일전짜리 알람 설정함
                    // 계산날짜가 -6 ~ -4 사이이면 디데이 5일전짜리 알람 설정함

                    days = (alarmTime - now) / (1000 * 60 * 60 * 24);

                    result = (int) days;

                    if (r <= 6 && r >= 0)
                        result = result - 1;

                    Log.e("남은날짜 확인", "7일전 디데이 날짜 : " + dCalendar.getTime().toString() + " // " + task + " // 디데이 : " + result);

                    //오늘 - 알람날짜 < 3일 일 때 3일전부터 알람 셋팅이다. => 오늘 - 3일 - 알람날짜 < 0일 일때 3일전 부터 알람 셋팅

                    //Log.e("리스트알람시간 확인", dCalendar.getTime().toString() + " // " + (now - alarmTime)+" //  "+count);

                    if ((result) >= 0) {

                        dCalendar.set(dYear, dMonth - 1, dDay, 19, 00);
                        dCalendar.add(Calendar.DATE, -7);

                        alarmTime = dCalendar.getTimeInMillis();

                        mgrAlarm.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, 1000 * 60 * 60 * 24 * 2, pendingIntent);

                        Log.e("리스트 7일전리피트셋팅알람시간 확인", task + " // " + dCalendar.getTime().toString() + " // " + (result) + " //  " + count);

                        intentArray.add(pendingIntent);

                        count++;
                    } else if (result >= -6 && result <= -4) {

                        dCalendar.set(dYear, dMonth - 1, dDay, 19, 00);
                        dCalendar.add(Calendar.DATE, -1);

                        alarmTime = dCalendar.getTimeInMillis();

                        mgrAlarm.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, 1000 * 60 * 60 * 24 * 2, pendingIntent);

                        Log.e("리스트 1일전 리피트셋팅알람시간 확인", task + " // " + dCalendar.getTime().toString() + " // " + (result) + " //  " + count);

                        intentArray.add(pendingIntent);
                        count++;
                    } else if (result > -4 && result <= -2) {

                        dCalendar.set(dYear, dMonth - 1, dDay, 19, 00);
                        dCalendar.add(Calendar.DATE, -3);

                        alarmTime = dCalendar.getTimeInMillis();

                        mgrAlarm.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, 1000 * 60 * 60 * 24 * 2, pendingIntent);

                        Log.e("리스트 3일전 리피트셋팅알람시간 확인", task + " // " + dCalendar.getTime().toString() + " // " + (result) + " //  " + count);

                        intentArray.add(pendingIntent);
                        count++;
                    } else if (result > -2 && result < 0) {
                        dCalendar.set(dYear, dMonth - 1, dDay, 19, 00);
                        dCalendar.add(Calendar.DATE, -5);

                        alarmTime = dCalendar.getTimeInMillis();

                        mgrAlarm.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, 1000 * 60 * 60 * 24 * 2, pendingIntent);

                        Log.e("리스트 5일전 리피트셋팅알람시간 확인", task + " // " + dCalendar.getTime().toString() + " // " + (result) + " //  " + count);

                        intentArray.add(pendingIntent);
                        count++;
                    }
                }
            } catch (SQLiteException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private View.OnClickListener leftListener = new View.OnClickListener() {
        public void onClick(View v) {

            try{
                dbmanager = new DBManager(MainActivity.this);
                sqlitedb = dbmanager.getWritableDatabase();

                String updateHW = "update listPlus set checkPopup = 1 where checkPopup = 0;";
                sqlitedb.execSQL(updateHW);

                sqlitedb.close();
                dbmanager.close();
                //  Toast.makeText(MainActivity.this, "check에 값 1 넣음", Toast.LENGTH_LONG).show();
                mCustomDialog.dismiss();
            }
            catch (SQLiteException e){
                e.getMessage();
                mCustomDialog.dismiss();
            }
        }
    };

    // 단순 닫기 버튼 리스너
    private View.OnClickListener rightListener = new View.OnClickListener() {
        public void onClick(View v) {
            mCustomDialog.dismiss();
        }
    };

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
