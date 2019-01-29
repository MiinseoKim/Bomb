package org.androidtown.myapplication123;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.kakaolink.AppActionBuilder;
import com.kakao.kakaolink.AppActionInfoBuilder;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class hwList extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;
    private Button menu;
    SQLiteDatabase sqlitedb;
    DBManager dbmanager;


    private KakaoLink kakaoLink;
    private KakaoTalkLinkMessageBuilder mKakaoTalkLinkMessageBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hw_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_list);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new ListViewAdapter(this);
        addList();
        mAdapter.sort();
        mListView.setAdapter(mAdapter);
        leftTaskNumber();
        countlog();

        //숏클릭
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Toast.makeText(hwList.this, "길게 클릭 해주세요 :D", Toast.LENGTH_LONG).show();
            }
        });
        //롱클릭
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id){
                final hwListView mData = mAdapter.mListData.get(position);
                //Toast.makeText(hwList.this, mData.mTitle, Toast.LENGTH_SHORT).show();

                final Context mContext;
                mContext =  hwList.this;

                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("과제 정보");

                String message;
                message = "과목:  ";
                message += mData.mSub;
                message += "\n과제:  ";
                message += mData.mTitle;
                message += "\n중요도:  ";
                switch (mData.mPower){
                    case 1 :
                        message += "콩알탄";
                        break;
                    case 2 :
                        message += "수류탄";
                        break;
                    case 3 :
                        message += "핵폭탄";
                        break;
                }
                message += "\n지각제출:  ";
                message += mData.mDelay;
                dialog.setMessage(message);
                dialog.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        remove(mData.mId);
                        dialog.dismiss();
                        Toast.makeText(hwList.this, "성공적으로 폭탄을 제거하다니 ! \n이번 학기 과탑은 따놓은 당상 !!", Toast.LENGTH_LONG).show();
                        ((hwList)(mContext)).reStart();

                    }
                });
                dialog.setNeutralButton("공유", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            kakaoLink = KakaoLink.getKakaoLink(getApplicationContext());
                            mKakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
                        } catch (KakaoParameterException e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }

                        SendKakaoMessage();

                    }
                });
                dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                dialog.show();
                return true;

            }
        });

    }

    private void addList() {

        try{
            dbmanager = new DBManager(this);
            sqlitedb = dbmanager.getWritableDatabase();
            Cursor cursor = sqlitedb.query("listPlus", null, "task is not null", null, null, null, null, null);

            while(cursor.moveToNext()){
                Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                String task = cursor.getString(cursor.getColumnIndex("task"));
                String subject = cursor.getString(cursor.getColumnIndex("subject"));
                Integer date = cursor.getInt(cursor.getColumnIndex("date"));
                Integer power = cursor.getInt(cursor.getColumnIndex("power"));
                Integer delay = cursor.getInt(cursor.getColumnIndex("delay"));

                int dYear =1,dMonth=1,dDay=1;
                long d,t,r;
                int ddays=0;

                String resultDayText="D-0";

                dYear = date / 10000;
                dMonth = (date % 10000)/100;
                dDay = date % 100;

                Calendar calendar =Calendar.getInstance();              //현재 날짜 불러옴


                Calendar dCalendar =Calendar.getInstance();
                dCalendar.set(dYear,dMonth-1, dDay);

                t=calendar.getTimeInMillis();                 //오늘 날짜를 밀리타임으로 바꿈
                d=dCalendar.getTimeInMillis();              //디데이날짜를 밀리타임으로 바꿈
                r=(d-t)/(24*60*60*1000);                 //디데이 날짜에서 오늘 날짜를 뺀 값을 '일'단위로 바꿈

                ddays=(int)r;

                if(ddays < 0){
                    String updateHW = "update listPlus set checkPopup = 0 where date = "+date+";";
                    sqlitedb.execSQL(updateHW);

                }


                // 우선순위 알고리즘을 위한 식 만들기

                int totalScore; // 우선순위 알고리즘 계산 변수

                totalScore = power * 3 + delay * 2 - ddays;

                // 디데이가 플러스가 되면 플러스로 바꿔주는 함수!
                if(ddays>=0){
                    resultDayText = String.format("D-%d", ddays);
                }
                else{
                    int absR=Math.abs(ddays);
                    resultDayText = String.format("D+%d", absR);
                }

                String text_delay = "";
                if(delay == 1)
                    text_delay = "가능";
                else
                    text_delay = "불가능";

                if(resultDayText.contains("+")) {
                    mAdapter.addItem(id, getResources().getDrawable(R.drawable.bomb4), task, subject, resultDayText, power, text_delay, totalScore - 1000);
                }
                else if(totalScore>8 || ddays <= 1)
                    mAdapter.addItem(id,getResources().getDrawable(R.drawable.bomb3),task, subject,resultDayText,power,text_delay,totalScore+1000);
                else if(totalScore>5 && totalScore <=8)
                    mAdapter.addItem(id,getResources().getDrawable(R.drawable.bomb2),task, subject,resultDayText,power,text_delay,totalScore);
                else if(totalScore <= 5)
                    mAdapter.addItem(id,getResources().getDrawable(R.drawable.bomb),task, subject,resultDayText,power,text_delay,totalScore);

            }

            cursor.close();
            sqlitedb.close();
            dbmanager.close();


        }catch (SQLiteException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void remove(int id){

        try{
            dbmanager = new DBManager(hwList.this);
            sqlitedb = dbmanager.getWritableDatabase();

            String DeleteHW = "delete from listPlus where id = " + id + ";";
            sqlitedb.execSQL(DeleteHW);
            String updateCN = "update totalScore set clearNumber = clearNumber + 1 where id = 1;";
            sqlitedb.execSQL(updateCN);

            sqlitedb.close();
            dbmanager.close();

        }
        catch (SQLiteException e){
            e.getMessage();
        }
        mAdapter.sort();
    }

    private class ViewHolder {
        public ImageView mIcon;
        public TextView mText;
        public TextView mSub;
        public TextView mDate;
    }


    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<hwListView> mListData = new ArrayList<hwListView>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public int getdbId(int id){
            return id;
        }



        public void addItem(Integer id,Drawable icon, String mTitle,String mSub, String mDate, Integer mPower, String mDelay,Integer mSort){
            hwListView addInfo = new hwListView();
            addInfo.mId=id;
            addInfo.mIcon = icon;
            addInfo.mTitle = mTitle;
            addInfo.mSub= mSub;
            addInfo.mDate = mDate;
            addInfo.mPower = mPower;
            addInfo.mDelay = mDelay;
            addInfo.mSort = mSort;

            mListData.add(addInfo);

        }

        public void sort(){
            Collections.sort(mListData, hwListView.myComparator);
            dataChange();
        }

        public void dataChange(){
            mAdapter.notifyDataSetChanged();
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item, null);

                holder.mIcon = (ImageView) convertView.findViewById(R.id.img_list);
                holder.mText = (TextView) convertView.findViewById(R.id.name);
                holder.mSub = (TextView) convertView.findViewById(R.id.subject);
                holder.mDate = (TextView) convertView.findViewById(R.id.date);


                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            hwListView mData = mListData.get(position);

            if (mData.mIcon != null) {
                holder.mIcon.setVisibility(View.VISIBLE);
                holder.mIcon.setImageDrawable(mData.mIcon);
            }else{
                holder.mIcon.setVisibility(View.GONE);
            }

            holder.mText.setText(mData.mTitle);
            holder.mSub.setText(mData.mSub);
            holder.mDate.setText(mData.mDate);

            return convertView;
        }
    }



    public void goAdd(View v) {
        Intent myIntent = new Intent(getApplicationContext(), hwPlus.class);
        startActivityForResult(myIntent,5000);
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
            Intent intent = new Intent(getApplicationContext(), hwCal.class);
            intent.addFlags(intent.FLAG_ACTIVITY_SINGLE_TOP | intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);

        } else if (id == R.id.hwTt) {
            Intent intent = new Intent(getApplicationContext(), hwTT.class);
            intent.addFlags(intent.FLAG_ACTIVITY_SINGLE_TOP | intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        } else if (id == R.id.hwList) {
            Intent intent = new Intent(getApplicationContext(), hwList.class);
            intent.addFlags(intent.FLAG_ACTIVITY_SINGLE_TOP | intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        } else if (id == R.id.hwPlus) {
            Intent intent = new Intent(getApplicationContext(), hwPlus.class);
            intent.addFlags(intent.FLAG_ACTIVITY_SINGLE_TOP | intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void alarmManager(){

        try {
            dbmanager = new DBManager(this);
            sqlitedb = dbmanager.getReadableDatabase();
            Cursor cursor = sqlitedb.query("listPlus", null, "task is not null", null, null, null, null, null);

            AlarmManager mgrAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);
            ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();

            Calendar calendar = Calendar.getInstance();
            long now;

            AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent;
            //PendingIntent[] sender = new PendingIntent[cursor.getCount() * 4];
            int count = 0;
            long alarmTime = 0;

            while (cursor.moveToNext()) {

                intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), count, intent, 0);
                // sender[count] = PendingIntent.getBroadcast(getApplicationContext(), count, intent, 0);

                Integer date = cursor.getInt(cursor.getColumnIndex("date"));

                int dYear = 1, dMonth = 1, dDay = 1;
                long d, t, r;

                dYear = date / 10000;
                dMonth = (date % 10000) / 100;
                dDay = date % 100;

                Calendar dCalendar = Calendar.getInstance();

                dCalendar.set(dYear, dMonth-1, dDay - 5, 20, 00);

                alarmTime = dCalendar.getTimeInMillis();

                now = calendar.getTimeInMillis();

                Log.e("리스트알람시간 확인", dCalendar.getTime().toString() + " // " + (now - alarmTime)+" //  "+count);

                if((now-alarmTime) < 0) {

                    mgrAlarm.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime,1000*60*60*24*2, pendingIntent);

                    Log.e("리스트 5일전리피트셋팅알람시간 확인", dCalendar.getTime().toString() + " // " + (now - alarmTime)+" //  "+count);

                    intentArray.add(pendingIntent);
                }else if(now - alarmTime < (1000*60*60*24*3))
                {

                    dCalendar.set(dYear, dMonth-1, dDay - 3, 19, 00);

                    alarmTime = dCalendar.getTimeInMillis();

                    mgrAlarm.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime,1000*60*60*24*2, pendingIntent);

                    Log.e("리스트 3일전 리피트셋팅알람시간 확인", dCalendar.getTime().toString() + " // " + (now - alarmTime)+" //  "+count);

                    intentArray.add(pendingIntent);
                }else if(now - alarmTime < (1000*60*60*24*5))
                {

                    dCalendar.set(dYear, dMonth-1, dDay - 1, 19, 00);

                    alarmTime = dCalendar.getTimeInMillis();

                    mgrAlarm.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime,1000*60*60*24*2, pendingIntent);

                    Log.e("리스트 1일전 리피트셋팅알람시간 확인", dCalendar.getTime().toString() + " // " + (now - alarmTime)+" //  "+count);

                    intentArray.add(pendingIntent);
                }

                count++;

            }
        } catch (SQLiteException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }


    /*과제 총 개수 세는 DB입력*/
    public void countlog(){
        dbmanager = new DBManager(hwList.this);
        sqlitedb = dbmanager.getWritableDatabase();
        Cursor cursor = sqlitedb.query("totalScore", null, "totalNumber is not null", null, null, null,null);

        while(cursor.moveToNext()){
            Integer totalNumber=cursor.getInt(cursor.getColumnIndex("totalNumber"));
            Integer failNumber = cursor.getInt(cursor.getColumnIndex("failNumber"));
            Integer clearNumber = cursor.getInt(cursor.getColumnIndex("clearNumber"));
            Integer leftNumber=cursor.getInt(cursor.getColumnIndex("leftNumber"));

            Log.e("개수 체크", "총개수 : "+ totalNumber + " / 해결 개수 : "+clearNumber+"/과제수 :"+leftNumber+"/실패 :"+failNumber);
        }
        cursor.close();
        sqlitedb.close();
        dbmanager.close();
    }


    /*과제 해결 개수 세는 DB입력*/


    public void leftTaskNumber(){

        try{
            dbmanager = new DBManager(hwList.this);
            sqlitedb = dbmanager.getWritableDatabase();

            String updateHW = "update totalScore set leftNumber = totalNumber - clearNumber where id = 1;";
            sqlitedb.execSQL(updateHW);

            sqlitedb.close();
            dbmanager.close();


        }
        catch (SQLiteException e){
            e.getMessage();
        }
    }


    private void SendKakaoMessage(){
                try{
                    mKakaoTalkLinkMessageBuilder.addText("[과제BomB]\n과제가 투척되었어요!\n지금 제거하러 가볼까요?");
                    mKakaoTalkLinkMessageBuilder.addImage("http://imageshack.com/a/img921/2563/TTpSdW.png", 128, 128);
                    mKakaoTalkLinkMessageBuilder.addAppButton("폭탄 제거하러가기", new AppActionBuilder()
                            .addActionInfo(AppActionInfoBuilder.createAndroidActionInfoBuilder().setExecuteParam("execparamkey1=1111").setMarketParam("referrer=kakaotalklink").build())
                            .setIOSExecuteURLParam("target=main", AppActionBuilder.DEVICE_TYPE.PHONE).build());
                    kakaoLink.sendMessage(mKakaoTalkLinkMessageBuilder, this);
                }catch(KakaoParameterException e){
                    e.printStackTrace();
                }
    }

    public void reStart() {
        finish();
        startActivity(new Intent(hwList.this, hwList.class));
    }

}

