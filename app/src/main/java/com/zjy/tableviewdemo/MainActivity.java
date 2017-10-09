package com.zjy.tableviewdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity {

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

        TableView tv = (TableView)findViewById(R.id.main_table);
        tv.setHeaderNames("t0","t1","t2","t3","t4","t5","t6","t7","t8","t9");
        tv.setTableData(getTestData());

        //tv.setEventMode(TableView.MODE_NONE_EVENT);
        //tv.setEventMode(TableView.MODE_ITEM_EVENT);
        //tv.setEventMode(TableView.MODE_ALL_UNIT_EVENT);
        //tv.setEventMode(TableView.MODE_EITHER_UNIT_EVENT);
        //tv.setColumnEventIndex(1,7,9,18);

        tv.setOnItemClickListener(new TableView.OnTableItemClickListener() {
            @Override
            public void onItemClick(int position, String[] rowData) {
                Log.d(TAG, "click item,pos="+position+"  data="+rowData[1]);
            }
        });
        tv.setOnItemLongClickListener(new TableView.OnTableItemLongClickListener() {
            @Override
            public void onItemLongClick(int position, String[] rowData) {
                Log.d(TAG, "long click item,pos="+position+"  data="+rowData[1]);
            }
        });
        tv.setOnUnitClickListener(new TableView.OnUnitClickListener() {
            @Override
            public void onUnitClick(int row, int column, String unitText) {
                Log.d(TAG, "onUnitClick: row="+row+"  column="+column+"  text="+unitText);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    private String[][] getTestData() {
        String[][] testData = {
                {"1","","","","","","","","",""},
                {"2","","","","","","","","",""},
                {"3","","","","","","","","",""},
                {"4","","","","","","","","",""},
                {"5","","","","","","","","",""},
                {"6","","","","","","","","",""},
                {"7","","","","","","","","",""},
                {"8","","","","","","","","",""},
                {"9","","","","","","","","",""},
                {"10","","","","","","","","",""},
                {"11","","","","","","","","",""},
                {"12","","","","","","","","",""},
                {"13","","","","","","","","",""},
                {"14","","","","","","","","",""},
                {"15","","","","","","","","",""},
                {"16","","","","","","","","",""},
                {"17","","","","","","","","",""},
                {"18","","","","","","","","",""},
                {"19","","","","","","","","",""},
                {"20","","","","","","","","",""},
        };

        for (int i = 0; i < testData.length; i++) {
            String[] arr = testData[i];
            for (int j = 0; j < arr.length; j++) {
                if (TextUtils.isEmpty(testData[i][j])) {
                    testData[i][j] = "( " + (i + 1) + " , " + (j + 1) + " )";
                }
            }
        }

        return testData;
    }
}
