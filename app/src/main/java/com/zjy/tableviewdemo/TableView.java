package com.zjy.tableviewdemo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableView extends HorizontalScrollView {

    private static final String TAG = "TableView";

    private static final String DEFAULT_TEXT_COLOR = "#000000";
    private static final int DEFAULT_COLUMN_WIDTH = 200;
    private static final int DEFAULT_HORIZONTAL_PADDING = 5;
    private static final int DEFAULT_VERTICAL_PADDING = 10;
    private static final int DEFAULT_TEXT_SIZE = 15;

    private Context mContext;
    private LinearLayout.LayoutParams mItemLayoutParams;
    private RelativeLayout mTableLayout;
    private FrameLayout mHeaderLayout;
    private ListView mContentListView;
    private List<String[]> mTableData = new ArrayList<>();
    private int mColumnCount;
    private String[] mHeaderNames;
    private TableAdapter mAdapter;
    private int[] mColumnWidth;

    private boolean mUnitSingleLine;
    private int mTopPadding, mLeftPadding, mBottomPadding, mRightPadding;

    private boolean mIsShowHeader;
    private int mHeaderTextSize;
    private int mContentTextSize;
    private int mHeaderTextColor;
    private int mContentTextColor;
    private boolean mIsHeaderTextBold;

    private boolean mIsShowBorder;
    private GradientDrawable mFrameDrawable;
    private LayerDrawable mHeaderBackDrawable;
    private LayerDrawable mUnitBackDrawable;

    public TableView(Context context) {
        this(context, null);
    }

    public TableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVariable();
        initViews();
    }

    private void initVariable() {
        Resources res = getResources();
        mContext = getContext();
        mItemLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mUnitSingleLine = true;
        mTopPadding = DEFAULT_VERTICAL_PADDING;
        mBottomPadding = DEFAULT_VERTICAL_PADDING;
        mLeftPadding = DEFAULT_HORIZONTAL_PADDING;
        mRightPadding = DEFAULT_HORIZONTAL_PADDING;
        mHeaderTextSize = DEFAULT_TEXT_SIZE;
        mContentTextSize = DEFAULT_TEXT_SIZE;
        mHeaderTextColor = Color.parseColor(DEFAULT_TEXT_COLOR);
        mContentTextColor = Color.parseColor(DEFAULT_TEXT_COLOR);
        mIsHeaderTextBold = false;
        mIsShowHeader = true;
        mIsShowBorder = true;
        mFrameDrawable = (GradientDrawable) res.getDrawable(R.drawable.table_view_frame, null);
        mHeaderBackDrawable = (LayerDrawable) res.getDrawable(R.drawable.table_view_header_back, null);
        mUnitBackDrawable = (LayerDrawable) res.getDrawable(R.drawable.table_view_unit_back, null);
    }

    private void initViews() {
        View.inflate(mContext, R.layout.table_view_layout, this);
        mTableLayout = (RelativeLayout) findViewById(R.id.table_layout);
        mHeaderLayout = (FrameLayout) findViewById(R.id.table_header);
        mContentListView = (ListView) findViewById(R.id.table_content_list);

        setBackground(mFrameDrawable);
    }

    private LinearLayout createHeader() {
        LinearLayout header = new LinearLayout(mContext);
        header.setLayoutParams(mItemLayoutParams);

        for (int i = 0; i < mColumnCount; i++) {
            TextView view = new TextView(mContext);
            view.setWidth(mColumnWidth[i]);
            view.setGravity(Gravity.CENTER_HORIZONTAL);
            view.setTextSize(mHeaderTextSize);
            view.setTextColor(mHeaderTextColor);
            if (mIsHeaderTextBold) {
                view.getPaint().setFakeBoldText(true);
            }
            view.setText(mHeaderNames[i]);
            view.setMaxLines(1);
            view.setBackground(mHeaderBackDrawable);
            view.setPadding(mLeftPadding, mTopPadding, mRightPadding, mBottomPadding);
            header.addView(view);
        }
        return header;
    }

    private LinearLayout createItem() {
        LinearLayout item = new LinearLayout(mContext);
        item.setLayoutParams(mItemLayoutParams);
        for (int i = 0; i < mColumnCount; i++) {
            item.addView(createUnitView(i));
        }
        return item;
    }

    private TextView createUnitView(int columnIndex) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                mColumnWidth[columnIndex], LayoutParams.MATCH_PARENT);
        TextView view = new TextView(mContext);
        view.setGravity(Gravity.CENTER);
        view.setLayoutParams(params);
        view.setTextSize(mContentTextSize);
        view.setTextColor(mContentTextColor);
        if (mUnitSingleLine) {
            view.setMaxLines(1);
        }
        setUnitBackground(view);
        return view;
    }

    private void setUnitBackground(View v) {
        if (!mIsShowBorder) {
            v.setBackground(null);
        } else {
            v.setBackground(mUnitBackDrawable);
        }
        v.setPadding(mLeftPadding, mTopPadding, mRightPadding, mBottomPadding);
    }

    private void fillTable() {
        if (mIsShowHeader) {
            mHeaderLayout.addView(createHeader());
        } else {
            mHeaderLayout.setVisibility(GONE);
        }

        mAdapter = new TableAdapter();
        mContentListView.setAdapter(mAdapter);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        fillTable();
    }

    private class TableAdapter extends BaseAdapter {

        private List<String[]> mData;
        private int mColumnNum;

        TableAdapter() {
            mColumnNum = mColumnCount;
            initDataByTableData();
        }

        private void initDataByTableData() {
            mData = mTableData;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public String[] getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = createItem();
            }
            ViewGroup itemLayout = ((ViewGroup) convertView);

            String[] data = mData.get(position);
            for (int i = 0; i < mColumnNum; i++) {
                View childView = itemLayout.getChildAt(i);
                String text = i >= data.length ? "" : data[i];
                if (childView instanceof TextView) {
                    ((TextView) childView).setText(text);
                }
            }
            return convertView;
        }
    }

    private List<String[]> copyData(List<String[]> srcList) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(srcList);
            String serStr = byteArrayOutputStream.toString("ISO-8859-1");
            serStr = java.net.URLEncoder.encode(serStr, "UTF-8");

            objectOutputStream.close();
            byteArrayOutputStream.close();

            String redStr = java.net.URLDecoder.decode(serStr, "UTF-8");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1"));
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

            @SuppressWarnings("unchecked")
            List<String[]> newList = (List<String[]>) objectInputStream.readObject();

            objectInputStream.close();
            byteArrayInputStream.close();

            return newList;
        } catch (Exception e) {
            Log.e(TAG, "copyData: copy list error, Exception=" + e);
        }
        return null;
    }

    private void updateSomethingByColumnCountChange() {
        // update mColumnWidth
        mColumnWidth = new int[mColumnCount];
        for (int i = 0; i < mColumnCount; i++) {
            mColumnWidth[i] = DEFAULT_COLUMN_WIDTH;
        }
    }

    /**
     * 设置表头
     */
    public void setHeaderNames(String... names) {
        if (names == null) {
            Log.e(TAG, "setHeaderNames: Invalid setting, the param should not be null.");
            return;
        }
        mHeaderNames = names;
        mColumnCount = mHeaderNames.length;
        updateSomethingByColumnCountChange();
    }

    /**
     * 设置表格列数
     * 若在本方法之后调用 {@link #setHeaderNames(String...)} 会覆盖此处的设置
     *
     * @param count 若参数值与表头 mHeaderNames 长度不一致会做一些默认的处理
     */
    public void setColumnCount(int count) {
        mColumnCount = count;
        updateSomethingByColumnCountChange();
        if (mHeaderNames == null) {
            mHeaderNames = new String[mColumnCount];
            return;
        }
        if (mColumnCount <= mHeaderNames.length) {
            return;
        }
        String[] temp = new String[mColumnCount];
        System.arraycopy(mHeaderNames, 0, temp, 0, mHeaderNames.length);
        mHeaderNames = temp;
    }

    /**
     * 设置表格数据
     */
    public void setTableData(List<String[]> data) {
        mTableData = copyData(data);
    }

    /**
     * 设置表格数据
     */
    public void setTableData(String[][] data) {
        setTableData(Arrays.asList(data));
    }

    /**
     * 设置第 columnIndex 列的宽度为 width
     */
    public void setColumnWidth(int columnIndex, int width) {
        if (columnIndex >= mColumnWidth.length || columnIndex < 0) {
            Log.e(TAG, "setColumnWidth: columnIndex out of bounds, " +
                    "columnIndex=" + columnIndex +
                    " mColumnWidth.length=" + mColumnWidth.length);
            return;
        }
        mColumnWidth[columnIndex] = width;
    }

    /**
     * 单元格是否单行显示，如果不单行显示，内容将根据宽度自动换行显示
     */
    public void setUnitSingleLine(boolean isSingleLine) {
        mUnitSingleLine = isSingleLine;
    }

    /**
     * 设置单元格内边距
     */
    public void setUnitPadding(int left, int top, int right, int bottom) {
        mTopPadding = top;
        mLeftPadding = left;
        mBottomPadding = bottom;
        mRightPadding = right;
    }

    public void setHeaderTextSize(int size) {
        this.mHeaderTextSize = size;
    }

    public void setContentTextSize(int size) {
        this.mContentTextSize = size;
    }

    public void setHeaderTextColor(@ColorRes int color) {
        this.mHeaderTextColor = ContextCompat.getColor(mContext, color);
    }

    public void setContentTextColor(@ColorRes int color) {
        this.mContentTextColor = ContextCompat.getColor(mContext, color);
    }

    public void setIsHeaderTextBold(boolean isBold) {
        this.mIsHeaderTextBold = isBold;
    }

    public void setIsShowHeader(boolean isShow) {
        mIsShowHeader = isShow;
    }
}
