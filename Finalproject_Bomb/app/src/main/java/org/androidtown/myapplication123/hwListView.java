package org.androidtown.myapplication123;

import android.graphics.drawable.Drawable;

import java.text.Collator;
import java.util.Comparator;

public class hwListView {

    /**
     * 리스트 정보를 담고 있을 객체 생성
     */
    //삭제용 아이디
    public Integer mId;

    // 아이콘
    public Drawable mIcon;

    // 제목
    public String mTitle;

    // 과목
    public String mSub;

    //디데이
    public String mDate;

    // 중요도
    public Integer mPower;

    // 추가제거 가능
    public String mDelay;

    //정렬 알고리즘
    public Integer mSort;

    /**
     * 알고리즘값으로 정렬
     */
    public static final Comparator<hwListView> myComparator = new Comparator<hwListView>() {

        @Override
        public int compare(hwListView mList_1, hwListView mList_2) {
            int ret = 0;

            if (mList_1.mSort < mList_2.mSort)
                ret = 1;
            else if (mList_1.mSort == mList_2.mSort)

                ret = 0;
            else
                ret = -1;

            return ret;
        }
    };
}
