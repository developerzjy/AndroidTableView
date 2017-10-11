package com.zjy.tableviewdemo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableView extends HorizontalScrollView {

    private static final String TAG = "TableView";

    public static final int MODE_NONE_EVENT = 0; //不处理任何事件
    public static final int MODE_ITEM_EVENT = 1; //item处理事件
    public static final int MODE_ALL_UNIT_EVENT = 2; //所有单元格处理事件
    public static final int MODE_EITHER_UNIT_EVENT = 3; //某列单元格处理事件

    private static final String DEFAULT_TEXT_COLOR = "#000000";
    private static final String DEFAULT_BORDER_COLOR = "#000000";
    private static final String DEFAULT_HEADER_BACK_COLOR = "#BDBDBD";
    private static final String DEFAULT_UNIT_BACK_COLOR = "#FCFCFC";
    private static final String DEFAULT_UNIT_DOWN_COLOR = "#F0F0F0";
    private static final String DEFAULT_UNIT_SELECTED_COLOR = "#BEBEBE";
    private static final int DEFAULT_COLUMN_WIDTH = 200;
    private static final int DEFAULT_HORIZONTAL_PADDING = 5;
    private static final int DEFAULT_VERTICAL_PADDING = 10;
    private static final int DEFAULT_TEXT_SIZE = 15;
    private static final int DEFAULT_BORDER_WIDTH = 2;

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
    private int mUnitTextSize;
    private int mHeaderTextColor;
    private int mUnitTextColor;
    private boolean mIsHeaderTextBold;

    private boolean mIsShowBorder;
    private GradientDrawable mFrameDrawable;

    private int mFrameBorderWidth;
    private int mFrameBorderColor;
    private int mHeaderBorderWidth;
    private int mHeaderBorderColor;
    private int mHeaderBackColor;
    private int mUnitBorderWidth;
    private int mUnitBorderColor;
    private int mUnitBackColor;

    @EventMode
    private int mEventMode;
    private OnTableItemClickListener mItemClickListener;
    private OnTableItemLongClickListener mItemLongClickListener;
    private OnUnitClickListener mUnitClickListener;

    private List<Integer> mColumnEventIndex = new ArrayList<>();
    private int mUnitDownColor;
    private boolean mIsUnitSelectable;
    private int mUnitSelectedColor;
    private Map<Point, String> mUnitSelectedMap = new HashMap<>();

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
        mUnitTextSize = DEFAULT_TEXT_SIZE;
        mHeaderTextColor = Color.parseColor(DEFAULT_TEXT_COLOR);
        mUnitTextColor = Color.parseColor(DEFAULT_TEXT_COLOR);
        mIsHeaderTextBold = false;
        mIsShowHeader = true;
        mIsShowBorder = true;
        mFrameDrawable = (GradientDrawable) res.getDrawable(R.drawable.table_view_frame, null);

        mFrameBorderWidth = DEFAULT_BORDER_WIDTH;
        mFrameBorderColor = Color.parseColor(DEFAULT_BORDER_COLOR);
        mHeaderBorderWidth = DEFAULT_BORDER_WIDTH;
        mHeaderBorderColor = Color.parseColor(DEFAULT_BORDER_COLOR);
        mHeaderBackColor = Color.parseColor(DEFAULT_HEADER_BACK_COLOR);
        mUnitBorderWidth = DEFAULT_BORDER_WIDTH;
        mUnitBorderColor = Color.parseColor(DEFAULT_BORDER_COLOR);
        mUnitBackColor = Color.parseColor(DEFAULT_UNIT_BACK_COLOR);
        mUnitDownColor = Color.parseColor(DEFAULT_UNIT_DOWN_COLOR);
        mUnitSelectedColor = Color.parseColor(DEFAULT_UNIT_SELECTED_COLOR);
        updateDrawable();
        mEventMode = MODE_NONE_EVENT;
        mIsUnitSelectable = false;
    }

    private void updateDrawable() {
        mFrameDrawable.setStroke(mFrameBorderWidth, mFrameBorderColor);
    }

    private void initViews() {
        View.inflate(mContext, R.layout.table_view_layout, this);
        mTableLayout = (RelativeLayout) findViewById(R.id.table_layout);
        mHeaderLayout = (FrameLayout) findViewById(R.id.table_header);
        mContentListView = (ListView) findViewById(R.id.table_content_list);
    }

    private LinearLayout createHeader() {
        LinearLayout header = new LinearLayout(mContext);
        header.setLayoutParams(mItemLayoutParams);

        for (int i = 0; i < mColumnCount; i++) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    mColumnWidth[i], LayoutParams.MATCH_PARENT);
            BorderTextView view = new BorderTextView(mContext);
            view.setLayoutParams(params);
            view.setGravity(Gravity.CENTER_HORIZONTAL);
            view.setTextSize(mHeaderTextSize);
            view.setTextColor(mHeaderTextColor);
            if (mIsHeaderTextBold) {
                view.getPaint().setFakeBoldText(true);
            }
            view.setText(mHeaderNames[i]);
            view.setMaxLines(1);
            view.setBorderColor(mHeaderBorderColor);
            view.setBorderWidth(mHeaderBorderWidth);
            view.setBackgroundColor(mHeaderBackColor);
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
        BorderTextView view = new BorderTextView(mContext);
        view.setGravity(Gravity.CENTER);
        view.setLayoutParams(params);
        view.setTextSize(mUnitTextSize);
        view.setTextColor(mUnitTextColor);
        if (mUnitSingleLine) {
            view.setMaxLines(1);
        }
        view.setBorderColor(mUnitBorderColor);
        view.setBorderWidth(mUnitBorderWidth);
        view.setBackgroundColor(mUnitBackColor);
        view.setPadding(mLeftPadding, mTopPadding, mRightPadding, mBottomPadding);
        return view;
    }

    private void fillTable() {
        updateDrawable();
        if (mIsShowHeader) {
            mHeaderLayout.setVisibility(VISIBLE);
            mHeaderLayout.addView(createHeader());
        } else {
            mHeaderLayout.setVisibility(GONE);
        }

        mAdapter = new TableAdapter();
        mContentListView.setAdapter(mAdapter);
        if (mEventMode == MODE_ITEM_EVENT) {
            mContentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(position, getRowData(position));
                    }
                }
            });
            mContentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mItemLongClickListener != null) {
                        mItemLongClickListener.onItemLongClick(position, getRowData(position));
                    }
                    return true;
                }
            });
        }

        setBackground(mFrameDrawable);
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
                if (mEventMode == MODE_ALL_UNIT_EVENT || mEventMode == MODE_EITHER_UNIT_EVENT) {
                    if (mColumnEventIndex.contains(i)) {
                        Point coordinate = new Point(i, position); //column=x, row=y
                        childView.setTag(coordinate);
                        childView.setOnTouchListener(touchListener);
                        if (mUnitSelectedMap.containsKey(coordinate)) {
                            childView.setBackgroundColor(mUnitSelectedColor);
                        } else {
                            childView.setBackgroundColor(mUnitBackColor);
                        }
                    }
                }
            }
            return convertView;
        }

        private View.OnTouchListener touchListener = new OnTouchListener() {

            private boolean isContainsUnit = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (mIsUnitSelectable) {
                        Point coordinate = (Point) v.getTag();
                        isContainsUnit = mUnitSelectedMap.containsKey(coordinate);
                        //颜色设置为选中或者取消选中
                        int color = isContainsUnit ? mUnitBackColor : mUnitSelectedColor;
                        v.setBackgroundColor(color);
                    } else {
                        v.setBackgroundColor(mUnitDownColor);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    Point coordinate = (Point) v.getTag();
                    if (mUnitClickListener != null) {
                        mUnitClickListener.onUnitClick(coordinate.y, coordinate.x, getRowData(coordinate.y)[coordinate.x]);
                    }
                    if (mIsUnitSelectable) {
                        //将选中的unit保存起来或者把保存的取消掉
                        if (isContainsUnit) {
                            mUnitSelectedMap.remove(coordinate);
                        } else {
                            mUnitSelectedMap.put(coordinate, getRowData(coordinate.y)[coordinate.x]);
                        }
                    } else {
                        v.setBackgroundColor(mUnitBackColor);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    if (mIsUnitSelectable) {
                        //颜色设置为取消选中或者选中（与down事件相反）
                        int color = isContainsUnit ? mUnitSelectedColor : mUnitBackColor;
                        v.setBackgroundColor(color);
                    } else {
                        v.setBackgroundColor(mUnitBackColor);
                    }
                }
                return true;
            }
        };
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
     * 获取表格某一行的数据,返回字符串数组
     */
    public String[] getRowData(int position) {
        String[] src = mTableData.get(position);
        String[] dest = new String[mColumnCount];
        int length = Math.min(src.length, mColumnCount);
        System.arraycopy(src, 0, dest, 0, length);
        return dest;
    }

    /**
     * 获取所有选中的单元格信息
     * 返回一个map，key是单元格的坐标信息（column=x, row=y），值是单元格显示的字符串
     */
    public Map<Point, String> getSelectedUnits() {
        return mUnitSelectedMap;
    }

    /**
     * 清除所有选择的单元格
     */
    public void clearSelectedUnits() {
        mUnitSelectedMap.clear();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 设置 第row行 第column列 为选中状态
     */
    public void setUnitSelected(int row, int column) {
        if ((mEventMode == MODE_ALL_UNIT_EVENT || mEventMode == MODE_EITHER_UNIT_EVENT) && mIsUnitSelectable) {
            Point p = new Point(column, row);
            if (!mUnitSelectedMap.containsKey(p)) {
                mUnitSelectedMap.put(p, getRowData(row)[column]);
            }
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
        if (count <= 0) {
            Log.e(TAG, "setColumnCount: Invalid setting, the count should be larger than 0.");
            return;
        }
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
     * 添加一行数据到表格末尾
     */
    public void addData(String[] data) {
        String[] dest = new String[mColumnCount];
        int length = Math.min(data.length, mColumnCount);
        System.arraycopy(data, 0, dest, 0, length);
        mTableData.add(dest);
    }

    /**
     * 添加一行数据到第row行
     */
    public void addRowData(int row, String[] data) {
        String[] dest = new String[mColumnCount];
        int length = Math.min(data.length, mColumnCount);
        System.arraycopy(data, 0, dest, 0, length);
        mTableData.add(row, dest);
    }

    /**
     * 删除一行数据
     */
    public void deleteRowData(int row) {
        mTableData.remove(row);
    }

    /**
     * 修改一行数据
     */
    public void modifyRowData(int row, String[] newData) {
        deleteRowData(row);
        addRowData(row, newData);
    }

    /**
     * 获取所有数据
     */
    public List<String[]> getAllData() {
        return copyData(mTableData);
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
        if (width <= 0) {
            Log.e(TAG, "setColumnWidth: width can not be " + width);
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

    public void setUnitTextSize(int size) {
        this.mUnitTextSize = size;
    }

    public void setHeaderTextColor(@ColorRes int color) {
        this.mHeaderTextColor = ContextCompat.getColor(mContext, color);
    }

    public void setUnitTextColor(@ColorRes int color) {
        this.mUnitTextColor = ContextCompat.getColor(mContext, color);
    }

    public void setIsHeaderTextBold(boolean isBold) {
        this.mIsHeaderTextBold = isBold;
    }

    public void setIsShowHeader(boolean isShow) {
        mIsShowHeader = isShow;
    }

    public void setIsShowBorder(boolean isShow) {
        mIsShowBorder = isShow;
        if (!mIsShowBorder) {
            mFrameBorderWidth = 0;
            mHeaderBorderWidth = 0;
            mUnitBorderWidth = 0;
        } else {
            mFrameBorderWidth = DEFAULT_BORDER_WIDTH;
            mHeaderBorderWidth = DEFAULT_BORDER_WIDTH;
            mUnitBorderWidth = DEFAULT_BORDER_WIDTH;
        }
    }

    public void setFrameBorderColor(@ColorRes int color) {
        mFrameBorderColor = ContextCompat.getColor(mContext, color);
    }

    public void setHeaderBorderColor(@ColorRes int color) {
        mHeaderBorderColor = ContextCompat.getColor(mContext, color);
    }

    public void setHeaderBorderWidth(int width) {
        mHeaderBorderWidth = mIsShowBorder ? width : 0;
    }

    public void setHeaderBackColor(@ColorRes int color) {
        mHeaderBackColor = ContextCompat.getColor(mContext, color);
    }

    public void setUnitBorderColor(@ColorRes int color) {
        mUnitBorderColor = ContextCompat.getColor(mContext, color);
    }

    public void setUnitBorderWidth(int width) {
        mUnitBorderWidth = mIsShowBorder ? width : 0;
    }

    public void setUnitBackColor(@ColorRes int color) {
        mUnitBackColor = ContextCompat.getColor(mContext, color);
    }

    public void setUnitSelectable(boolean selectable) {
        mIsUnitSelectable = selectable;
    }

    /**
     * 在单元格处理事件的时候，设置单元格按下状态的颜色
     */
    public void setUnitDownColor(@ColorRes int color) {
        mUnitDownColor = ContextCompat.getColor(mContext, color);
    }

    /**
     * 当单元格可以被选中的时候，设置被选中状态的颜色
     */
    public void setUnitSelectedColor(@ColorRes int color) {
        mUnitSelectedColor = ContextCompat.getColor(mContext, color);
    }

    public void setEventMode(@EventMode int mode) {
        mEventMode = mode;
        mColumnEventIndex.clear();
        mUnitSelectedMap.clear();
        if (mode == MODE_ALL_UNIT_EVENT) {
            for (int i = 0; i < mColumnCount; i++) {
                mColumnEventIndex.add(i);
            }
        }
    }

    public void setOnItemClickListener(OnTableItemClickListener listener) {
        mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnTableItemLongClickListener listener) {
        mItemLongClickListener = listener;
    }

    public void setOnUnitClickListener(OnUnitClickListener listener) {
        mUnitClickListener = listener;
    }

    public void setColumnEventIndex(int... index) {
        if (mEventMode != MODE_EITHER_UNIT_EVENT) {
            return;
        }
        for (int x : index) {
            if (!mColumnEventIndex.contains(x) && x < mColumnCount && x >= 0) {
                mColumnEventIndex.add(x);
            }
        }
    }

    public void notifyAttributesChanged() {
        mHeaderLayout.removeAllViews();
        mContentListView.setAdapter(null);
        fillTable();
    }


    private class BorderTextView extends TextView {

        private int mBorderColor = Color.parseColor(DEFAULT_BORDER_COLOR);
        private int mBorderWidth = DEFAULT_BORDER_WIDTH;
        private Paint mPaint = new Paint();

        public BorderTextView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            mPaint.setStrokeWidth(mBorderWidth);
            mPaint.setColor(mBorderColor);
            canvas.drawLine(getWidth() - mBorderWidth / 2, 0, getWidth() - mBorderWidth / 2, getHeight(), mPaint);
            canvas.drawLine(0, getHeight() - mBorderWidth / 2, getWidth(), getHeight() - mBorderWidth / 2, mPaint);
        }

        public void setBorderColor(int color) {
            mBorderColor = color;
        }

        public void setBorderWidth(int width) {
            mBorderWidth = width;
        }
    }

    @IntDef({MODE_NONE_EVENT, MODE_ITEM_EVENT, MODE_ALL_UNIT_EVENT, MODE_EITHER_UNIT_EVENT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface EventMode {
    }

    public interface OnTableItemClickListener {
        void onItemClick(int position, String[] rowData);
    }

    public interface OnTableItemLongClickListener {
        void onItemLongClick(int position, String[] rowData);
    }

    public interface OnUnitClickListener {
        void onUnitClick(int row, int column, String unitText);
    }
}
