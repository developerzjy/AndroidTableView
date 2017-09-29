package com.zjy.tableviewdemo;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.AnyRes;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnItemClick;
import butterknife.OnItemSelected;
import butterknife.Unbinder;

/**
 * Created by zjy on 2017/9/27.
 */

public class StyleActivity extends Activity {

//    @BindView(R.id.column_index_et)
//    EditText mColumnIndexEt;
//    @BindView(R.id.column_width_et)
//    EditText mColumnWidthEt;
//    @BindView(R.id.set_column_width_bt)
//    Button mSetColumnWidthBt;

    private Unbinder mButterKnifeBinder;
    @BindView(R.id.style_table_view)
    TableView mTableView;
    @BindView(R.id.info_text)
    TextView mInfoTv;
    private List<Integer> mTemp = new ArrayList<>(); // 用来避免Spinner在初始化的时候调用OnItemSelected事件
    private int[] mColorId = {
            R.color.red_color,
            R.color.orange_color,
            R.color.yellow_color,
            R.color.green_color,
            R.color.cyan_color,
            R.color.blue_color,
            R.color.purple_color};
    private SparseIntArray mDefColor = new SparseIntArray() {{
        put(R.id.outer_border_color_sp, R.color.def_border_color);
        put(R.id.header_border_color_sp, R.color.def_border_color);
        put(R.id.unit_border_color_sp, R.color.def_border_color);
        put(R.id.header_text_color_sp, R.color.def_text_color);
        put(R.id.unit_text_color_sp, R.color.def_text_color);
        put(R.id.header_back_color_sp, R.color.def_header_back_color);
        put(R.id.unit_back_color_sp, R.color.def_unit_back_color);
    }};
    private int[] mBorderWidth = {2, 5, 10, 15};
    private int[] mTextSize = {15, 18, 20, 24};
    private int[] mColumnCount = {-1, 3, 5, 15};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_style);
        mButterKnifeBinder = ButterKnife.bind(this);
        initTableView();
    }

    private void initTableView() {
        mTableView.setHeaderNames("Title1", "Title2", "Title3", "Title4", "Title5", "Title6", "Title7", "Title8", "Title9", "Title10");
        mTableView.setTableData(getTestData());
    }


    @OnItemSelected({R.id.outer_border_color_sp, R.id.header_border_color_sp, R.id.unit_border_color_sp,
            R.id.header_text_color_sp, R.id.unit_text_color_sp, R.id.header_back_color_sp, R.id.unit_back_color_sp})
    void onColorSelected(AdapterView<?> parent, View view, int position, long id) {
        int vId = parent.getId();
        if (!mTemp.contains(vId)){
            mTemp.add(vId);
            return;
        }
        @ColorRes int color = position == 0 ? mDefColor.get(vId) : mColorId[position - 1];
        if (vId == R.id.outer_border_color_sp) {
            mTableView.setFrameBorderColor(color);
        } else if (vId == R.id.header_border_color_sp) {
            mTableView.setHeaderBorderColor(color);
        } else if (vId == R.id.unit_border_color_sp) {
            mTableView.setUnitBorderColor(color);
        } else if (vId == R.id.header_text_color_sp) {
            mTableView.setHeaderTextColor(color);
        } else if (vId == R.id.unit_text_color_sp) {
            mTableView.setUnitTextColor(color);
        } else if (vId == R.id.header_back_color_sp) {
            mTableView.setHeaderBackColor(color);
        } else if (vId == R.id.unit_back_color_sp) {
            mTableView.setUnitBackColor(color);
        }
        mTableView.notifyAttributesChanged();
    }

    @OnItemSelected({R.id.header_border_width_sp, R.id.unit_border_width_sp, R.id.header_text_size_sp,
            R.id.unit_text_size_sp, R.id.column_count_sp, R.id.set_padding_sp})
    void onSomethingSelected(AdapterView<?> parent, View view, int position, long id) {
        int vId = parent.getId();
        if (!mTemp.contains(vId)){
            mTemp.add(vId);
            return;
        }
        if (vId == R.id.header_border_width_sp) {
            mTableView.setHeaderBorderWidth(mBorderWidth[position]);
        } else if (vId == R.id.unit_border_width_sp) {
            mTableView.setUnitBorderWidth(mBorderWidth[position]);
        } else if (vId == R.id.header_text_size_sp) {
            mTableView.setHeaderTextSize(mTextSize[position]);
        } else if (vId == R.id.unit_text_size_sp) {
            mTableView.setUnitTextSize(mTextSize[position]);
        } else if (vId == R.id.column_count_sp) {
            mTableView.setColumnCount(mColumnCount[position]);
        } else if (vId == R.id.set_padding_sp) {
            if (position == 2) {
                mTableView.setUnitPadding(10, 20, 10, 20);
            } else if (position == 1) {
                mTableView.setUnitPadding(0, 0, 0, 0);
            } else {
                mTableView.setUnitPadding(5, 10, 5, 10);
            }
        }
        mTableView.notifyAttributesChanged();
    }

    @OnCheckedChanged({R.id.bold_header_cb,R.id.show_header_cb,R.id.show_border_cb,R.id.single_line_cb})
    void onCheckBoxCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if (id == R.id.bold_header_cb) {
            mTableView.setIsHeaderTextBold(isChecked);
        } else if (id == R.id.show_header_cb) {
            mTableView.setIsShowHeader(isChecked);
        } else if (id == R.id.show_border_cb) {
            mTableView.setIsShowBorder(isChecked);
        } else if (id == R.id.single_line_cb) {
            mTableView.setUnitSingleLine(isChecked);
        }
        mTableView.notifyAttributesChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    private void updateInfoText(String text) {
        mInfoTv.setText(text);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mButterKnifeBinder.unbind();
    }

    private String[][] getTestData() {
        String[][] testData = {
                {"1", "自动换行测试start 11111111111111111 end", "11", "1111", "111", "111", "1111", "11111", "11111", "111"},
                {"2", "22", "2222", "222", "222222", "222", "222222", "2222", "2", "2"},
                {"3", "", "", ""},
                {"4", "", "", "", "", "yyy"},
                {"5", "", "", "自动换行测试start 5555555 end", "", "", "", "", "", ""},
                {"6", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                {"7", "", "", "", "", "", "", "", "", ""},
                {"8", "", "", "", "", "自动换行测试start 88888888888888888888888888888888888888888888 end", "", "", "", ""},
                {"9", "", "", "", "", "", "", "", "", ""},
                {"10", "", "", "", "", "", "", "", "", ""},
                {"11", "", "", "", "", "", "", "", "", ""},
                {"12", "", "", "", "", "", "", "", "", ""},
                {"13", "", "", "", ""},
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
