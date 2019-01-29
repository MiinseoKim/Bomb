package org.androidtown.myapplication123;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by 민서 on 2016-10-04.
 */
public class manual extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual);
    }


    public void finish(View v){
        finish();
    }

}
