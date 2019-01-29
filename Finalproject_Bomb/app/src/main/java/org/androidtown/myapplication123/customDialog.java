package org.androidtown.myapplication123;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 민서 on 2016-11-15.
 */
public class customDialog extends Dialog {

    private TextView mTitleView;
    private ImageView mContentView;
    private Button closeButton;
    private Button realCloseButton;
    private String mTitle;

    private View.OnClickListener CloseClickListener;
    private View.OnClickListener NeverShowAgainListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.4f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.customdialog);

       // mTitleView = (TextView) findViewById(R.id.txt_title);
        mContentView = (ImageView) findViewById(R.id.txt_content);
        closeButton = (Button) findViewById(R.id.btn_close_popup);
        realCloseButton = (Button) findViewById(R.id.btn_real_close);

        // 제목과 내용을 생성자에서 셋팅한다.
        //mTitleView.setText(mTitle);

        // 클릭 이벤트 셋팅
        if (CloseClickListener != null) {
            closeButton.setOnClickListener(CloseClickListener);
        }
        if (NeverShowAgainListener != null) {
            realCloseButton.setOnClickListener(NeverShowAgainListener);
        }
    }

    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public customDialog(Context context, String title,
                        View.OnClickListener leftListener,
                        View.OnClickListener rightListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
     //   this.mTitle = title;
        this.NeverShowAgainListener = leftListener;
        this.CloseClickListener = rightListener;
    }
}

