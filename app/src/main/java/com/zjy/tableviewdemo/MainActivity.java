package com.zjy.tableviewdemo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        Button bt = (Button) findViewById(R.id.test_bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,StyleActivity.class));
            }
        });

        TableView tv = (TableView)findViewById(R.id.test_table_view);
//        tv.setHeaderNames("t1","t2","t3","t4","t5","t6","t7","t8","t9","t10","t11","t12","t13","t14","t15");
//        //tv.setHeaderNames("t1","t2","t3");
//        tv.setTableData(getTestData2());
//        tv.setColumnWidth(1,100);
//        tv.setColumnWidth(2,300);
//        tv.setUnitSingleLine(false);
//        tv.setUnitPadding(15,30,15,30);

//        tv.setHeaderTextColor(R.color.test1);
//        tv.setContentTextColor(R.color.test2);
//        tv.setHeaderTextSize(25);
//        tv.setContentTextSize(15);
//        tv.setIsHeaderTextBold(true);
//        tv.setIsShowHeader(false);

//        tv.setIsShowBorder(false);

//        tv.setFrameBorderColor(R.color.test2);
//        tv.setHeaderBorderColor(R.color.green);
//        tv.setHeaderBorderWidth(8);
//        tv.setHeaderBackColor(R.color.test1);
//        tv.setUnitBorderColor(R.color.colorAccent);
//        tv.setUnitBorderWidth(8);
//        tv.setUnitBackColor(R.color.test3);
    }

    private List<String[]> getTestData1() {
        List<String[]> data = new ArrayList<>();
        data.add(a1);
        data.add(a2);
        data.add(a3);
        data.add(a4);
        data.add(a5);
        data.add(a6);
        data.add(a7);
        data.add(a8);
        data.add(a9);
        data.add(a10);
        data.add(a11);
        return data;
    }

    private String[][] getTestData2() {
        String[][] data = {a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11};
        return data;
    }

    String[] a1 = {"111","1","111","11","111111","111","111","11","aaa","aaa","aaa","aaa","aaa","aaa","aaa"};
    String[] a2 = {"2","2222","22","222","22","222","222222","22222","aaa","aaa","aaa","aaa","aaa","aaa","aaa"};
    String[] a3 = {"33333","33","3","33","333","333","3","3333333","aaa","aaa","aaa","aaa","aaa","aaa","aaa"};
    String[] a4 = {"44","444","444","444","4444","44","444","4444","aaa","aaa","aaa","aaa","aaa","aaa","aaa"};
    String[] a5 = {"5","5","5","5","5","5","5","5555555555555555","aaa","aaa","aaa","aaa","aaa","aaa","aaa"};
    String[] a6 = {"666","66","6","6","6","6","6","6666666666666","aaa","aaa","aaa","aaa","aaa","aaa","aaa"};
    String[] a7 = {"777","777","77","","7","","7","777","aaa","aaa","aaa","aaa","aaa","aaa","aaa"};
    String[] a8 = {"8","8","8","8","8","8","8","8","aaa","aaa","aaa","aaa","aaa","aaa","aaa"};
    String[] a9 = {"9","9","999","9999","99","999","9999","q9999999999999999999999999999q","aaa","aaa","aaa","aaa","aaa","aaa","aaa"};
    String[] a10 = {"8","8","888","888888","88","888","8888","888888888","aaa","aaa","aaa","aaa","aaa","aaa","aaa"};
    String[] a11 = {"9","9","999","9999","99","999","9999","999999999999999","aaa","aaa","aaa","aaa","aaa","aaa","aaa"};

    @Override
    protected void onResume() {
        super.onResume();
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }
}
