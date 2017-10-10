package com.zjy.tableviewdemo;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.util.Map;

/**
 * Created by zjy on 2017/10/10.
 */

public class EventActivity extends Activity {

    private static final String TAG = "EventActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_event);

        initTableView();
    }

    private void initTableView() {
        TableView tv = (TableView) findViewById(R.id.event_table);
        tv.setHeaderNames("t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7", "t8", "t9");
        tv.setTableData(getTestData());

        //注册点击item事件
        //需要 tv.setEventMode(TableView.MODE_ITEM_EVENT);
        tv.setOnItemClickListener(new TableView.OnTableItemClickListener() {
            @Override
            public void onItemClick(int position, String[] rowData) {
                Log.d(TAG, "click item,pos=" + position + "  data=" + rowData[1]);
            }
        });
        //注册长按item事件
        //需要 tv.setEventMode(TableView.MODE_ITEM_EVENT);
        tv.setOnItemLongClickListener(new TableView.OnTableItemLongClickListener() {
            @Override
            public void onItemLongClick(int position, String[] rowData) {
                Log.d(TAG, "long click item,pos=" + position + "  data[1]=" + rowData[1]);
            }
        });
        //注册点击单元格事件
        //需要tv.setEventMode(TableView.MODE_ALL_UNIT_EVENT);
        //或者tv.setEventMode(TableView.MODE_EITHER_UNIT_EVENT); 并且 tv.setColumnEventIndex(1,7,9,18);
        tv.setOnUnitClickListener(new TableView.OnUnitClickListener() {
            @Override
            public void onUnitClick(int row, int column, String unitText) {
                Log.d(TAG, "onUnitClick: row=" + row + "  column=" + column + "  text=" + unitText);
            }
        });

        //***********************************************************************
        //上面注册的事件需要设置相应的事件模式，以下选其一，默认不处理任何事件
        //tv.setEventMode(TableView.MODE_NONE_EVENT);//不处理任何事件

        //tv.setEventMode(TableView.MODE_ITEM_EVENT);//item处理点击和长按事件

        tv.setEventMode(TableView.MODE_ALL_UNIT_EVENT);//所有单元格处理事件

        //tv.setEventMode(TableView.MODE_EITHER_UNIT_EVENT);//自定义某些列的单元格处理事件
        //tv.setColumnEventIndex(1,7,9,18);//设置哪些列的单元格处理事件
        //***********************************************************************

        //其他可选设置项
        tv.setUnitSelectable(true);//单元格处理事件的时候是否可以选中
        //tv.setUnitDownColor(R.color.blue_color);//单元格处理事件的时候，按下态的颜色
        //tv.setUnitSelectedColor(R.color.cyan_color);//单元格被选中的颜色

        //其他相关的方法
        //Map<Point,String> selectedData = tv.getSelectedUnits();//获取所有选中的单元格数据
        //tv.clearSelectedUnits();//清除所有选中的单元格
        //tv.setUnitSelected(1,2);//在单元格可以被选中的时候，设置第1行第2列的单元格被选中
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
                {"1", "", "", "", "", "", "", "", "", ""},
                {"2", "", "", "", "", "", "", "", "", ""},
                {"3", "", "", "", "", "", "", "", "", ""},
                {"4", "", "", "", "", "", "", "", "", ""},
                {"5", "", "", "", "", "", "", "", "", ""},
                {"6", "", "", "", "", "", "", "", "", ""},
                {"7", "", "", "", "", "", "", "", "", ""},
                {"8", "", "", "", "", "", "", "", "", ""},
                {"9", "", "", "", "", "", "", "", "", ""},
                {"10", "", "", "", "", "", "", "", "", ""},
                {"11", "", "", "", "", "", "", "", "", ""},
                {"12", "", "", "", "", "", "", "", "", ""},
                {"13", "", "", "", "", "", "", "", "", ""},
                {"14", "", "", "", "", "", "", "", "", ""},
                {"15", "", "", "", "", "", "", "", "", ""},
                {"16", "", "", "", "", "", "", "", "", ""},
                {"17", "", "", "", "", "", "", "", "", ""},
                {"18", "", "", "", "", "", "", "", "", ""},
                {"19", "", "", "", "", "", "", "", "", ""},
                {"20", "", "", "", "", "", "", "", "", ""},
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
