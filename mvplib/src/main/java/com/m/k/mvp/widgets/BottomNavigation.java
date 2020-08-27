package com.m.k.mvp.widgets;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.m.k.mvp.R;
import com.m.k.mvp.utils.MvpUtils;

import java.sql.BatchUpdateException;
import java.util.ArrayList;

public class BottomNavigation extends ConstraintLayout {

    private static final int HORIZONTAL_MARGIN_DP = 32;
    private static final int VERTICAL_MARGIN_DP = 12;

    private NavigationAdapter mAdapter;

    private ArrayList<Integer> mDrawableRes = new ArrayList<>();
    private ArrayList<String> mTitles = new ArrayList<>();
    private ColorStateList mTextColorStateList;
    private OnTabSelectedListener mTabSelectedListener;

    private Paint mLinePaint;

    private int mMarginLeft;
    private int mMarginBottom;
    private int mMarginRight;
    private int mMarginTop;
    private int mDrawableMargin;
    private int mTextSize;
    private int mDrawableIconWidth;
    private int mDrawableIconHeight;
    private int mDeliverLineWidth;
    private int mDeliverLineColor;

    private int mCurrentPosition = 0;


    private boolean mHasDeliverLine; // 是否显示分割线


    public BottomNavigation(Context context) {
        super(context);
    }

    public BottomNavigation(Context context, AttributeSet attrs) {
        super(context, attrs);
        initValue(attrs);
    }

    public BottomNavigation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initValue(attrs);

    }


    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    private void initValue(AttributeSet set) {

        TypedArray array = getContext().obtainStyledAttributes(set, R.styleable.BottomNavigation);

        mMarginLeft = array.getDimensionPixelSize(R.styleable.BottomNavigation_bottomNavigationLeftMargin, MvpUtils.dip2px(getContext(), HORIZONTAL_MARGIN_DP));
        mMarginRight = array.getDimensionPixelSize(R.styleable.BottomNavigation_bottomNavigationRightMargin, MvpUtils.dip2px(getContext(), HORIZONTAL_MARGIN_DP));
        mMarginTop = array.getDimensionPixelSize(R.styleable.BottomNavigation_bottomNavigationTopMargin, MvpUtils.dip2px(getContext(), VERTICAL_MARGIN_DP));
        mMarginBottom = array.getDimensionPixelSize(R.styleable.BottomNavigation_bottomNavigationBottomMargin, MvpUtils.dip2px(getContext(), VERTICAL_MARGIN_DP));
        mDrawableIconWidth = array.getDimensionPixelSize(R.styleable.BottomNavigation_bottomNavigationDrawableWidth, 0);
        mDrawableIconHeight = array.getDimensionPixelSize(R.styleable.BottomNavigation_bottomNavigationDrawableHeight, 0);

        mDrawableMargin = array.getDimensionPixelSize(R.styleable.BottomNavigation_bottomNavigationDrawableMargin, 0);
        mTextSize = array.getDimensionPixelSize(R.styleable.BottomNavigation_bottomNavigationTextSize, 0);

        mTextColorStateList = array.getColorStateList(R.styleable.BottomNavigation_bottomNavigationTextColor);

        mHasDeliverLine = array.getBoolean(R.styleable.BottomNavigation_bottomNavigationDeliverLine, true);
        mDeliverLineWidth = array.getDimensionPixelSize(R.styleable.BottomNavigation_bottomNavigationDeliverLineWidth, MvpUtils.dip2px(getContext(), 1));
        mDeliverLineColor = array.getColor(R.styleable.BottomNavigation_bottomNavigationDeliverLineColor, Color.GRAY);

        array.recycle();


        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(mDeliverLineColor);
        mLinePaint.setStrokeWidth(mDeliverLineWidth);

        setWillNotDraw(!mHasDeliverLine);
    }

    public void setTabSelectedListener(OnTabSelectedListener tabSelectedListener) {
        this.mTabSelectedListener = tabSelectedListener;
        if (mAdapter != null) {
            View tabView = mAdapter.getHolderByPosition(mCurrentPosition).mItemView;
            mTabSelectedListener.onTabSelect(tabView, (Integer) tabView.getTag());

        }
    }

    public BottomNavigation addItem(@DrawableRes int drawableId, String title) {

        mDrawableRes.add(drawableId);
        mTitles.add(title);
        return this;
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();


    }

    public BottomNavigation setCurrentPosition(int position) {

        mCurrentPosition = position;
        return this;
    }

    public void apply() {

        // 把这些drawable 和 title  应用到 我的布局上
        if (mDrawableRes.size() > 0) {
            ArrayList<TabData> list = new ArrayList<>();
            for (int i = 0; i < mDrawableRes.size(); i++) {
                list.add(new TabData(mDrawableRes.get(i), mTitles.get(i)));

            }
            apply(new SimpleNavigationAdapter(list));
        }

    }

    public void apply(NavigationAdapter<? extends TabHolder> adapter) {
        mAdapter = adapter;
        initView();

    }


    private void initView() {

        removeAllViews();


        int minTabWidth = Integer.MAX_VALUE; // 所有tab 中最小的那一个的宽度
        int maxTabHeight = 0; // 所有tab 中最高哪一个的高度
        int maxTabHeightIndex = 0; // 最高tab 的index

        for (int i = 0; i < mAdapter.getCount(); i++) {
            TabHolder holder = mAdapter.createHolder(this, i);
            addView(holder.mItemView);

            mAdapter.bindData(holder, i);


            // 计算每一个tab 的宽和高
            holder.mItemView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            if (holder.mItemView.getMeasuredWidth() < minTabWidth) {
                minTabWidth = holder.mItemView.getMeasuredWidth();
            }


            if (holder.mItemView.getMeasuredHeight() > maxTabHeight) {
                maxTabHeight = holder.mItemView.getMeasuredHeight();
                maxTabHeightIndex = i;
            }


        }


        // 添加约束条件
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);

        int previousId = 0; // 用于记录上一个 id
        View view;
        int ids[] = new int[mAdapter.getCount()];

        // 在使用链的时候，如果是水平的链，那么就只需要把链上的所有控件的垂直方向上的约束添加上即可

        // 先把最高的那一个tab 固定好


        view = mAdapter.getHolderByPosition(maxTabHeightIndex).mItemView;

        constraintSet.connect(view.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(view.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        ids[maxTabHeightIndex] = view.getId();
        previousId = view.getId();


        if (maxTabHeightIndex > 0) {
            for (int i = maxTabHeightIndex - 1; i >= 0; i--) {
                view = mAdapter.getHolderByPosition(i).mItemView;
                constraintSet.connect(view.getId(), ConstraintSet.BOTTOM, previousId, ConstraintSet.BOTTOM);
                previousId = view.getId();
                ids[i] = view.getId();
            }

        }


        if (maxTabHeightIndex < mAdapter.getCount() - 1) {
            for (int i = maxTabHeightIndex + 1; i < mAdapter.getCount(); i++) {
                view = mAdapter.getHolderByPosition(i).mItemView;
                constraintSet.connect(view.getId(), ConstraintSet.BOTTOM, previousId, ConstraintSet.BOTTOM);
                previousId = view.getId();
                ids[i] = view.getId();
            }
        }


        // 第一个参数： 你这个链的左端需要连接到的控件的Id
        // 第二个参数： 你这个链的左端需要连接到第一个参数指定的控件的那一边
        // 第三个参数： 你这个链的右端需要连接大的控件的Id
        // 第四个参数： 你这个链的右端需要连接到第三个参数指定的控件的那一边
        // 第五个参数： 你这个链上多有控件的Id 的一个数组
        // 第六个参数： 权重
        // 第七个参数： 链的模式
        constraintSet.createHorizontalChain(ConstraintSet.PARENT_ID, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, ids, null, ConstraintSet.CHAIN_SPREAD_INSIDE);

        constraintSet.applyTo(this);


        // 通过设置 tab 的 padding  来扩大点击事件区域

        int padding = Math.max(mMarginBottom, mMarginTop);
        int paddingOffset = Math.min(minTabWidth / 2, Math.min(mMarginLeft, mMarginRight));
        int paddingTop = mHasDeliverLine ? padding + mDeliverLineWidth : padding;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            mAdapter.getHolderByPosition(i).mItemView.setPadding(paddingOffset, paddingTop, paddingOffset, padding);
        }

        //
        setPadding(mMarginLeft - paddingOffset, 0, mMarginRight - paddingOffset, 0);


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mHasDeliverLine) {
            canvas.drawLine(0, 0, getWidth(), 0, mLinePaint);
        }

    }

    public static class SimpleNavigationAdapter implements NavigationAdapter<SimpleNavigationAdapter.SimpleTabHolder> {

        private int mId = 1000;

        private ArrayList<TabData> mTabData;
        private ArrayList<SimpleTabHolder> mHolders = new ArrayList<>();
        private boolean isFirst = true;

        private CheckBox mPreCheckedTab;

        SimpleNavigationAdapter(ArrayList<TabData> tabData) {
            this.mTabData = tabData;
        }

        @Override
        public SimpleTabHolder createHolder(ViewGroup parent, int position) {

            BottomNavigation navigation = ((BottomNavigation) parent);
            CheckBox tabView = new CheckBox(parent.getContext());
            tabView.setTag(position);
            tabView.setId(mId + position);
            tabView.setButtonDrawable(null);
            tabView.setGravity(Gravity.CENTER);
            tabView.setTextColor(navigation.mTextColorStateList);
            int textSize = navigation.mTextSize;
            if (textSize > 0) {
                tabView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }


            tabView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (navigation.mTabSelectedListener != null) {

                        if (isChecked) {

                            if (mPreCheckedTab != buttonView) {
                                navigation.mTabSelectedListener.onTabSelect(buttonView, (Integer) buttonView.getTag());

                                if (mPreCheckedTab != null) {
                                    navigation.mTabSelectedListener.onTabUnSelect(mPreCheckedTab, (Integer) mPreCheckedTab.getTag());

                                    CheckBox temp = mPreCheckedTab;
                                    mPreCheckedTab = (CheckBox) buttonView;
                                    temp.setChecked(false);
                                    return;

                                }

                                mPreCheckedTab = (CheckBox) buttonView;
                            }

                        } else {

                            if (mPreCheckedTab == buttonView) {
                                navigation.mTabSelectedListener.onTabReSelected(buttonView, (Integer) buttonView.getTag());
                                mPreCheckedTab.setChecked(true);
                            }

                        }

                    }
                }
            });


            SimpleTabHolder holder = new SimpleTabHolder(tabView);

            mHolders.add(holder);
            return holder;
        }

        private void select(int position) {
            getHolderByPosition(position).mItemView.setChecked(true);
        }

        @Override
        public void bindData(SimpleTabHolder holder, int position) {

            Drawable topDrawable = holder.mItemView.getContext().getResources().getDrawable(mTabData.get(position).getDrawableId());

            BottomNavigation navigation = (BottomNavigation) holder.mItemView.getParent();

            if (navigation.mDrawableIconWidth > 0 && navigation.mDrawableIconHeight > 0) {
                topDrawable.setBounds(0, 0, navigation.mDrawableIconWidth, navigation.mDrawableIconHeight);
                holder.mItemView.setCompoundDrawables(null, topDrawable, null, null);
            } else {
                holder.mItemView.setCompoundDrawablesWithIntrinsicBounds(null, topDrawable, null, null);
            }


            holder.mItemView.setText(mTabData.get(position).getTitle());


            if (navigation.mCurrentPosition == position) {
                holder.mItemView.setChecked(true);
                if(navigation.mTabSelectedListener == null){
                    mPreCheckedTab = holder.mItemView;
                }

            }


        }

        @Override
        public int getCount() {
            return mTabData == null ? 0 : mTabData.size();
        }


        @Override
        public SimpleTabHolder getHolderByPosition(int position) {
            return mHolders.size() == 0 ? null : mHolders.get(position);
        }

        static class SimpleTabHolder extends TabHolder<CheckBox> {

            SimpleTabHolder(CheckBox itemView) {
                super(itemView);
            }

        }

    }


    public static abstract class TabHolder<T extends View> {
       protected T mItemView;

       public TabHolder(T itemView) {
            this.mItemView = itemView;
        }

    }

    public interface NavigationAdapter<TH extends TabHolder> {

        TH createHolder(ViewGroup parent, int position);

        void bindData(TH holder, int position);

        int getCount();

        TH getHolderByPosition(int position);

    }

    private static class TabData {
        private int drawableId;
        private String title;

        TabData(int drawableId, String title) {
            this.drawableId = drawableId;
            this.title = title;
        }

        int getDrawableId() {
            return drawableId;
        }

        String getTitle() {
            return title;
        }
    }

    public interface OnTabSelectedListener {

        void onTabSelect(View tab, int position);

        void onTabUnSelect(View tab, int position);

        void onTabReSelected(View tab, int position);
    }
}
