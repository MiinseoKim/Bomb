package org.androidtown.myapplication123;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class hwTT extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    private TimeTableView mTimeTableView;
    private List<TimeTableModel> mList;
    private Button menu;
    SQLiteDatabase sqlitedb;
    DBManager dbmanager;
    public static Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hw_tt);
        mContext= hwTT.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_tt);
        setSupportActionBar(toolbar);

        mList = new ArrayList<TimeTableModel>();
        mTimeTableView = (TimeTableView) findViewById(R.id.main_timetable_ly);
        addTimeTable();
        mTimeTableView.setTimeTable(mList);

        menu=(Button)findViewById(R.id.drawer);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void addTimeTable() {

        try{
            dbmanager = new DBManager(this);
            sqlitedb = dbmanager.getReadableDatabase();
            Cursor cursor = sqlitedb.query("ttPlus", null, "lecture is not null", null, null, null, null, null);

            while(cursor.moveToNext()){
                Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                String subject = cursor.getString(cursor.getColumnIndex("lecture"));
                String room = cursor.getString(cursor.getColumnIndex("room"));
                Integer stime = cursor.getInt(cursor.getColumnIndex("startNum"));
                Integer etime = cursor.getInt(cursor.getColumnIndex("endNum"));
                Integer week = cursor.getInt(cursor.getColumnIndex("week"));

                mList.add(new TimeTableModel(id,stime, etime, week, subject, room));

            }
            cursor.close();
            sqlitedb.close();
            dbmanager.close();

        }catch (SQLiteException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    public void goAdd(View v){
        Intent it = new Intent(getApplicationContext(), hwTtPlus.class);
        startActivityForResult(it,5000);
        hwTT.this.finish();
    }

    public void reStart() {
        finish();
        startActivity(new Intent(hwTT.this, hwTT.class));
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
