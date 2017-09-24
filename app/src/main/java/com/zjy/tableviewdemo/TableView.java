package com.zjy.tableviewdemo;

import android.content.Context;
import android.graphics.Color;
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

    private Context mContext;
    private RelativeLayout mTableLayout;
    private FrameLayout mHeaderLayout;
    private View mDividerView;
    private ListView mContentListView;
    private List<String[]> mTableData = new ArrayList<>();
    private int mColumnCount;
    private String[] mHeaderNames;
    private TableAdapter mAdapter;

    private LinearLayout.LayoutParams mItemLayoutParams;

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
        mContext = getContext();
        mItemLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void initViews() {
        View.inflate(mContext, R.layout.table_view_layout, this);
        mTableLayout = (RelativeLayout)findViewById(R.id.table_layout);
        mHeaderLayout = (FrameLayout) findViewById(R.id.table_header);
        mContentListView = (ListView) findViewById(R.id.table_content_list);
        mDividerView = findViewById(R.id.table_header_divider);

        setBackgroundResource(R.drawable.frame);
    }

    private LinearLayout createHeader() {
        LinearLayout header = new LinearLayout(mContext);
        header.setLayoutParams(mItemLayoutParams);

        for (int i = 0; i < mColumnCount; i++) {
            TextView view = new TextView(mContext);
            view.setWidth(100);
            view.setGravity(Gravity.CENTER_HORIZONTAL);
            view.setText(mHeaderNames[i]);
            view.setMaxLines(1);
            view.setBackgroundResource(R.drawable.right_border);
            view.setPadding(5, 10, 5, 10);
            header.addView(view);
        }
        return header;
    }

    private LinearLayout createItem() {
        LinearLayout item = new LinearLayout(mContext);
        item.setLayoutParams(mItemLayoutParams);
        for (int i = 0; i < mColumnCount; i++) {
            item.addView(createUnitView(100));
        }
        return item;
    }

    private TextView createUnitView(int width) {
        TextView view = new TextView(mContext);
        view.setGravity(Gravity.CENTER);
        view.setWidth(width);
        view.setMaxLines(1);
        view.setBackgroundResource(R.drawable.right_border);
        view.setPadding(5, 10, 5, 10);
        return view;
    }

    private void fillTable() {
        mHeaderLayout.addView(createHeader());
        mHeaderLayout.setBackgroundColor(Color.parseColor("#EEB422"));

        mDividerView.setBackgroundColor(Color.parseColor("#2c2c2c"));
        mDividerView.setMinimumWidth(100 * mColumnCount);

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
                if (childView instanceof TextView) {
                    ((TextView) childView).setText(data[i]);
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

    public void setHeaderNames(String... names) {
        if (names == null) {
            Log.d(TAG, "setHeaderNames: Invalid setting, the param should not be null.");
            return;
        }
        mHeaderNames = names;
        mColumnCount = mHeaderNames.length;
    }

    public void setTableData(List<String[]> data) {
        mTableData = copyData(data);
    }

    public void setTableData(String[][] data) {
        setTableData(Arrays.asList(data));
    }

}
