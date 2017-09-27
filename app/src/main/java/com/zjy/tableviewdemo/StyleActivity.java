package com.zjy.tableviewdemo;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnItemSelected;

/**
 * Created by zjy on 2017/9/27.
 */

public class StyleActivity extends Activity {

    @BindView(R.id.style_table_view)
    TableView mTableView;
    @BindView(R.id.info_text)
    TextView mInfoTv;
    @BindView(R.id.column_index_et)
    EditText mColumnIndexEt;
    @BindView(R.id.column_width_et)
    EditText mColumnWidthEt;
    @BindView(R.id.set_column_width_bt)
    Button mSetColumnWidthBt;
    @BindView(R.id.outer_border_color_sp)
    Spinner mFBorderColorSp;
    @BindView(R.id.header_border_color_sp)
    Spinner mHBorderColorSp;
    @BindView(R.id.unit_border_color_sp)
    Spinner mUBorderColorSp;
    @BindView(R.id.header_border_width_sp)
    Spinner mHBorderWidth;
    @BindView(R.id.unit_border_width_sp)
    Spinner mUBorderWidthSp;
    @BindView(R.id.header_text_color_sp)
    Spinner mHTextColorSp;
    @BindView(R.id.unit_text_color_sp)
    Spinner mUTextColorSp;
    @BindView(R.id.header_text_size_sp)
    Spinner mHTextSizeSp;
    @BindView(R.id.unit_text_size_sp)
    Spinner mUTextSizeSp;
    @BindView(R.id.bold_header_cb)
    CheckBox mHBoldCb;
    @BindView(R.id.show_header_cb)
    CheckBox mShowHeaderCb;
    @BindView(R.id.show_border_cb)
    CheckBox mShowBorderCb;
    @BindView(R.id.single_line_cb)
    CheckBox mUSingleLineCb;
    @BindView(R.id.header_back_color_sp)
    Spinner mHBackColorSp;
    @BindView(R.id.unit_back_color_sp)
    Spinner mUBackColorSp;
    @BindView(R.id.column_count_sp)
    Spinner mColumnCountSp;
    @BindView(R.id.set_padding_sp)
    Spinner mUPaddingSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_style);

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
}
