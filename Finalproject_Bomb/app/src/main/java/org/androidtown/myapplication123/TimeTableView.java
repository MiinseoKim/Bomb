package org.androidtown.myapplication123;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TimeTableView extends LinearLayout {

    SQLiteDatabase sqlitedb;
    DBManager dbmanager;
    /**
     * 배색배열
     */
    public static int colors[] = {R.drawable.label_aqua,
            R.drawable.label_peach, R.drawable.label_orangepink,
            R.drawable.lable_orange, R.drawable.label_pink,
            R.drawable.label_blue, R.drawable.label_green,
            R.drawable.label_skyblue, R.drawable.lable_piiink,
            R.drawable.label_piink, R.drawable.lable_purple,
            R.drawable.lable_yellow, R.drawable.lable_lemon,
            R.drawable.lable_green, R.drawable.lable_grey};
    private final static int START = 0;
    //교시 수
    public final static int MAXNUM = 6;
    //요일 수
    public final static int WEEKNUM = 6;
    //단일 높이 view
    private final static int TimeTableHeight = 50;
    //선 높이
    private final static int TimeTableLineHeight = 2;
    private final static int TimeTableNumWidth = 20;
    private final static int TimeTableWeekNameHeight = 30;
    private LinearLayout mHorizontalWeekLayout;//행
    private LinearLayout mVerticalWeekLaout;//열
    private String[] weekname = {"월", "화", "수", "목", "금"," "};
    public static String[] colorStr = new String[20];
    int colornum = 0;

    String todo = "";
    String title = "";


    //데이터 소스
    private List<TimeTableModel> mListTimeTable = new ArrayList<TimeTableModel>();

    public TimeTableView(Context context) {
        super(context);
    }

    public TimeTableView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /**
     * 가로 요일 라인
     *
     * @return
     */
    private View getWeekTransverseLine() {
        TextView mWeekline = new TextView(getContext());
        mWeekline.setBackgroundColor(getResources().getColor(R.color.view_line));
        mWeekline.setHeight(TimeTableLineHeight);
        mWeekline.setWidth(LinearLayout.LayoutParams.FILL_PARENT);
        return mWeekline;
    }

    /**
     * 세로
     *
     * @return
     */
    private View getWeekVerticalLine() {
        TextView mWeekline = new TextView(getContext());
        mWeekline.setBackgroundColor(getResources().getColor(R.color.view_line));
        mWeekline.setHeight(dip2px(TimeTableWeekNameHeight));
        mWeekline.setWidth((TimeTableLineHeight));
        return mWeekline;
    }


    private void initView() {

        mHorizontalWeekLayout = new LinearLayout(getContext());
        mHorizontalWeekLayout.setOrientation(HORIZONTAL);

        mVerticalWeekLaout = new LinearLayout(getContext());
        mVerticalWeekLaout.setOrientation(HORIZONTAL);

        //표 그리기 i는 요일
        for (int i = 0; i <= WEEKNUM; i++) {
            switch (i) {
                case 0:
                    //0의시간표,0칸 비어있는것
                    TextView mTime = new TextView(getContext());
                    mTime.setHeight(dip2px(TimeTableWeekNameHeight));
                    mTime.setWidth((dip2px(TimeTableNumWidth)));
                    mHorizontalWeekLayout.addView(mTime);

                    //그리기-월요일~MAXNUM
                    LinearLayout mMonday = new LinearLayout(getContext());
                    ViewGroup.LayoutParams mm = new ViewGroup.LayoutParams(dip2px(TimeTableNumWidth), dip2px(MAXNUM * TimeTableHeight) + MAXNUM * 2);
                    mMonday.setLayoutParams(mm);
                    mMonday.setOrientation(VERTICAL);
                    for (int j = 1; j <= MAXNUM; j++) {  //j는 교시
                        TextView mNum = new TextView(getContext());
                        mNum.setGravity(Gravity.CENTER);
                        mNum.setTextColor(getResources().getColor(R.color.white));
                        mNum.setHeight(dip2px(TimeTableHeight));
                        mNum.setWidth(dip2px(TimeTableNumWidth));
                        mNum.setTextSize(14);
                        mNum.setText(j + "");
                        mMonday.addView(mNum);
                        mMonday.addView(getWeekTransverseLine());
                    }
                    mVerticalWeekLaout.addView(mMonday);
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    // 요일 라인 그리기
                    LinearLayout mHoriView = new LinearLayout(getContext());
                    mHoriView.setOrientation(VERTICAL);
                    TextView mWeekName = new TextView(getContext());
                    mWeekName.setTextColor(getResources().getColor(R.color.white));
                    mWeekName.setWidth(((getViewWidth() - dip2px(TimeTableNumWidth))) / WEEKNUM);
                    mWeekName.setHeight(dip2px(TimeTableWeekNameHeight));
                    mWeekName.setGravity(Gravity.CENTER);
                    mWeekName.setTextSize(16);
                    mWeekName.setText(weekname[i - 1]);
                    mHoriView.addView(mWeekName);
                    mHorizontalWeekLayout.addView(mHoriView);

                    List<TimeTableModel> mListMon = new ArrayList<>();
                    //1.일요일 그리다~7의 시간표
                    for (TimeTableModel timeTableModel : mListTimeTable) {
                        if (timeTableModel.getWeek() == i) {
                            mListMon.add(timeTableModel);
                        }
                    }
                    //추가
                    LinearLayout mLayout = getTimeTableView(mListMon, i);
                    mLayout.setOrientation(VERTICAL);
                    ViewGroup.LayoutParams linearParams = new ViewGroup.LayoutParams((getViewWidth() - dip2px(20)) / WEEKNUM, LinearLayout.LayoutParams.FILL_PARENT);
                    mLayout.setLayoutParams(linearParams);
                    mLayout.setWeightSum(1);
                    mVerticalWeekLaout.addView(mLayout);
                    break;

                default:
                    break;
            }
            TextView l = new TextView(getContext());
            l.setHeight(dip2px(TimeTableHeight * MAXNUM) + MAXNUM * 2);
            l.setWidth(2);
            l.setBackgroundColor(getResources().getColor(R.color.view_line));
            mVerticalWeekLaout.addView(l);
            mHorizontalWeekLayout.addView(getWeekVerticalLine());
        }//for end
        addView(mHorizontalWeekLayout);
        addView(getWeekTransverseLine());
        addView(mVerticalWeekLaout);
        addView(getWeekTransverseLine());
    }//init end

    private int getViewWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    private View addStartView(int startnum, final int week, final int start) {
        LinearLayout mStartView = new LinearLayout(getContext());
        mStartView.setOrientation(VERTICAL);
        for (int i = 1; i < startnum; i++) {
            TextView mTime = new TextView(getContext());
            mTime.setGravity(Gravity.CENTER);
            mTime.setHeight(dip2px(TimeTableHeight));
            mTime.setWidth(dip2px(TimeTableHeight));
            mStartView.addView(mTime);
            mStartView.addView(getWeekTransverseLine());
            final int num = i;
            //강의 없는곳 클릭시 추가 이벤트
            /*mTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(),  week+ "주" + (start + num) + "시간" , Toast.LENGTH_LONG).show();
                }
            });*/

        }
        return mStartView;
    }

    /**
     * 일요일, 월요일 시간표
     *
     * @param model
     * @param week
     * @return
     */
    private LinearLayout getTimeTableView(List<TimeTableModel> model, int week) {
        LinearLayout mTimeTableView = new LinearLayout(getContext());
        mTimeTableView.setOrientation(VERTICAL);
        int modesize = model.size();
        if (modesize <= 0) {
            mTimeTableView.addView(addStartView(MAXNUM + 1, week, 0));
        } else {
            for (int i = 0; i < modesize; i++) {
                if (i == 0) {
                    //0의 추가적인 공백시작 (피치수)
                    mTimeTableView.addView(addStartView(model.get(0).getStartnum(), week, 0));
                    mTimeTableView.addView(getMode(model.get(0)));
                } else if (model.get(i).getStartnum() - model.get(i - 1).getStartnum() > 0) {
                    //그리기
                    mTimeTableView.addView(addStartView(model.get(i).getStartnum() - model.get(i - 1).getEndnum(), week, model.get(i - 1).getEndnum()));
                    mTimeTableView.addView(getMode(model.get(i)));
                }
                if (i + 1 == modesize) {
                    mTimeTableView.addView(addStartView(MAXNUM - model.get(i).getEndnum(), week, model.get(i).getEndnum()));
                }
            }
        }
        return mTimeTableView;
    }

    /**
     * 시간표 가져옴,단일View 이 설정 가능
     *
     * @param model 데이터 형식
     * @return
     */
    @SuppressWarnings("deprecation")
    private View getMode(final TimeTableModel model) {

        final LinearLayout mTimeTableView = new LinearLayout(getContext());
        mTimeTableView.setOrientation(VERTICAL);
        TextView mTimeTableNameView = new TextView(getContext());
        int num = model.getEndnum() - model.getStartnum();  //start num과 endnum 같을시
        mTimeTableNameView.setHeight(dip2px((num + 1) * TimeTableHeight) + num * 2);
        mTimeTableNameView.setTextColor(getContext().getResources().getColor(
                android.R.color.white));
        mTimeTableNameView.setWidth(dip2px(50));
        mTimeTableNameView.setTextSize(16);
        mTimeTableNameView.setGravity(Gravity.CENTER);
        mTimeTableNameView.setText(model.getName() + "\n@" + model.getClassroom());
        mTimeTableView.addView(mTimeTableNameView);
        mTimeTableView.addView(getWeekTransverseLine());
        mTimeTableView.setBackgroundDrawable(getContext().getResources()
                .getDrawable(colors[getColorNum(model.getName())]));
        mTimeTableView.setOnClickListener(new OnClickListener() {

            //과목 클릭시 이벤트
            public void onClick(View v) {
                Context mContext;
                mContext =  getContext();
                getTodoList(model.getName());

                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle(title+" 과제 목록");
                dialog.setMessage(todo);
                dialog.setNegativeButton("과목 삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(model.getId());
                        dialog.dismiss();
                        ((hwTT)(hwTT.mContext)).reStart();
                    }
                });
                dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                dialog.show();
            }
        });
        return mTimeTableView;
    }

    /**
     * 전환dp - 입력값에 따라 크기 변환
     *
     * @param dpValue
     * @return
     */
    public int dip2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setTimeTable(List<TimeTableModel> mlist) {
        this. mListTimeTable = mlist;
        for (TimeTableModel timeTableModel : mlist) {
            addTimeName(timeTableModel.getName());
        }
        initView();
        invalidate();
    }

    /**
     * 시간표 입력여부 판단할 배열의 순환시간표 존재 탈퇴하고 있다면 순환 출력.존재하면colorSt[20]배열 삽입
     *
     * @param name
     */
    private void addTimeName(String name) {
        boolean isRepeat = true;
        for (int i = 0; i < 20; i++) {
            if (name.equals(colorStr[i])) {
                isRepeat = true;
                break;
            } else {
                isRepeat = false;
            }
        }
        if (!isRepeat) {
            colorStr[colornum] = name;
            colornum++;
        }
    }

    /**
     * 배열에 있는 수업 받아옴
     *
     * @param name
     * @return
     */
    public int getColorNum(String name) {
        int num = 0;
        for (int i = 0; i < 20; i++) {
            if (name.equals(colorStr[i])) {
                num = i;
            }
        }
        return num;
    }

    public void delete (int id){
        try{
            dbmanager = new DBManager(getContext());
            sqlitedb = dbmanager.getWritableDatabase();


            String DeleteHW = "delete from ttPlus where id = " + id + ";";
            sqlitedb.execSQL(DeleteHW);

            sqlitedb.close();
            dbmanager.close();

        }
        catch (SQLiteException e){
            e.getMessage();
        }
    }

    public void getTodoList(String clickSubject){

        String list="";
        String littleTitle="";

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

                if(clickSubject.equals(subject)) {
                    littleTitle = subject;
                    list += str_dMonth+"/"+str_dDay + " : " + task + "\n";
                    //Log.e("시간표 과제목록",littleTitle +" - "+ list);
                }

            }
        }catch (SQLiteException e) {
            e.printStackTrace();
            Log.e("","디비받아오기 실패");
        }

        todo = list;
        title = littleTitle;

    }
}
