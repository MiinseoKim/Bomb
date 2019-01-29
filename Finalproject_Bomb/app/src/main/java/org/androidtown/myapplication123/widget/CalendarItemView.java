package org.androidtown.myapplication123.widget;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.androidtown.myapplication123.DBManager;
import org.androidtown.myapplication123.R;
import org.androidtown.myapplication123.utils.Common;
import org.json.JSONArray;

import java.util.Calendar;

public class CalendarItemView extends View {

    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaint_1 = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintBackgroundToday = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintBackgroundEvent = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int dayOfWeek = -1;
    private boolean isStaticText = false;
    private long millis;
    private Rect rect;
    private boolean isTouchMode;
    private int dp11;
    private int dp16;
    private boolean hasEvent = false;
    private int[] mColorEvents;

    String myJSON;
    SQLiteDatabase sqlitedb;
    DBManager dbmanager;
    JSONArray hwCal = null;

    int workday = 0;
    String todo = "";
    String title = "";

    public CalendarItemView(Context context) {
        super(context);
        initialize();
    }

    public CalendarItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize() {
        dp11 = (int) Common.dp2px(getContext(),11);
        dp16 = (int) Common.dp2px(getContext(),16);

        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(dp11);
        mPaintBackground.setColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
        mPaintBackgroundToday.setColor(getContext().getResources().getColor(R.color.colorAccent));
        mPaintBackgroundEvent.setColor(getContext().getResources().getColor(R.color.colorPrimary));
        mPaint_1.setColor(Color.WHITE);
        setClickable(true);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                Log.d("hatti.onTouchEvent", event.getAction() + "");
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
//                        setBackgroundResource(R.drawable.round_default_select);
                        rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                        isTouchMode = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isTouchMode) {
                            ((CalendarView) getParent()).setCurrentSelectedView(v);
                            isTouchMode = false;
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        isTouchMode = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                            isTouchMode = false;
                            return true;
                        }
                        break;
                }
                return false;
            }
        });
        setPadding(30, 0, 30, 0);
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((mPaint.descent() + mPaint.ascent()) / 2));
        mPaint.setTextAlign(Paint.Align.CENTER);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        //getData("http://psyhe6.dothome.co.kr/calList.php");
        getTodoList();

        CalendarView calendarView = (CalendarView) getParent();
        if (calendarView.getParent() instanceof ViewPager) {
            ViewGroup parent = (ViewPager) calendarView.getParent();
            CalendarItemView tagView = (CalendarItemView) parent.getTag();

            if (!isStaticText && tagView != null && tagView.getTag() != null && tagView.getTag() instanceof Long) {
                long millis = (long) tagView.getTag();

                if (isSameDay(millis, this.millis)) {
                    canvas.drawRoundRect(xPos - dp16, getHeight() / 2 - dp16, xPos + dp16, getHeight() / 2 + dp16, 50f, 50f, mPaintBackground);

                    AlertDialog.Builder dialog = new AlertDialog.Builder(calendarView.getContext());
                    dialog.setTitle(title+" 과제 목록");

                    dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });

                    dialog.setMessage(todo);

                    if(todo.equals("")) {
                        Toast.makeText(getContext(),"폭탄이 없습니다",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        dialog.show();
                    }
                }
            }
        }

        if (!isStaticText && isToday(millis)) {
            canvas.drawRoundRect(xPos - dp16, getHeight() / 2 - dp16, xPos + dp16, getHeight() / 2 + dp16, 50f, 50f, mPaintBackgroundToday);
        }

        if (isStaticText) {
            // 요일 표시
            canvas.drawText(CalendarView.DAY_OF_WEEK[dayOfWeek], xPos, yPos, mPaint);
        } else {
            // 날짜 표시
            canvas.drawText(calendar.get(Calendar.DATE) + "", xPos, yPos, mPaint);
        }

        if (hasEvent) {
            mPaintBackgroundEvent.setColor(getResources().getColor(mColorEvents[0]));
            canvas.drawRoundRect(xPos - 30, getHeight() / 2 + 25, xPos + 30, getHeight() / 2 + 35, 50f, 50f, mPaintBackground);
            if(isStaticText)
                canvas.drawRoundRect(xPos - 30, getHeight() / 2 + 25, xPos + 30, getHeight() / 2 + 35, 50f, 50f, mPaint_1);
        }

    }

    private boolean isToday(long millis) {
        Calendar cal1 = Calendar.getInstance();
        return isSameDay(cal1.getTimeInMillis(), millis);

    }

    public void setDate(long millis) {
        this.millis = millis;
        setTag(millis);
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        isStaticText = true;
    }

    public void setEvent(int... resid) {
        hasEvent = true;
        mColorEvents = resid;
    }
    public boolean isSameDay(long millis1, long millis2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTimeInMillis(millis1);
        cal2.setTimeInMillis(millis2);

        Log.d("hatti.calendar", "" + cal1.get(Calendar.YEAR) + "" + cal1.get(Calendar.MONTH) + "" + cal1.get(Calendar.DATE) + " VS " +
                cal2.get(Calendar.YEAR) + "" + cal2.get(Calendar.MONTH) + "" + cal2.get(Calendar.DATE));
        return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) && cal1.get(Calendar.DATE) == cal2.get(Calendar.DATE));
    }

    public boolean isStaticText() {
        return isStaticText;
    }


    public void getTodoList(){

        String list="";
        String littleTitle="";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        int thisMonth = calendar.get(Calendar.MONTH)+1;
        int thisYear = calendar.get(Calendar.YEAR);
        int thisday = calendar.get(Calendar.DATE);

        String str_thisMonth = Integer.toString(thisMonth);
        String str_thisYear = Integer.toString(thisYear);
        String str_thisday = Integer.toString(thisday);

        if(thisMonth < 10)
            str_thisMonth = "0"+str_thisMonth;
        if(thisday < 10)
        str_thisday = "0"+str_thisday;

        String str_today = str_thisYear+str_thisMonth+str_thisday;

        try {
            dbmanager = new DBManager(getContext());
            sqlitedb = dbmanager.getReadableDatabase();
            Cursor cursor = sqlitedb.query("listPlus", null, "task is not null", null, null, null, null, null);

            while(cursor.moveToNext()){
                Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                String task = cursor.getString(cursor.getColumnIndex("task"));
                String subject = cursor.getString(cursor.getColumnIndex("subject"));
                Integer date = cursor.getInt(cursor.getColumnIndex("date"));
                Integer power = cursor.getInt(cursor.getColumnIndex("power"));
                Integer delay = cursor.getInt(cursor.getColumnIndex("delay"));

                String str_date = Integer.toString(date);

                int dYear =1,dMonth=1,dDay=1;
                String str_dYear,str_dMonth,str_dDay;

                dYear = date / 10000;
                dMonth = (date % 10000)/100;
                dDay = date % 100;

                str_dYear = Integer.toString(dYear);
                str_dMonth = Integer.toString(dMonth);
                str_dDay = Integer.toString(dDay);

                if(str_today.equals(str_date)) {
                    setEvent(R.color.colorPrimaryDark);
                    littleTitle = str_dMonth+"월 "+str_dDay+"일";
                    list += subject + " : " + task + "\n";
                    Log.e("과제목록",list);
                }

            }
        }catch (SQLiteException e) {
            e.printStackTrace();
            Log.e("","디비받아오기 실패");
        }

        todo = list;
        title = littleTitle;

    }

    public void setWorkday(int day){
        workday = day;
    }

    public int getWorkday(){
        return workday;
    }
}