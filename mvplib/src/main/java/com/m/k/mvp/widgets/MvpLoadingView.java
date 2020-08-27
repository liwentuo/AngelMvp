package com.m.k.mvp.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.appcompat.widget.ContentFrameLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Group;

import com.cunoraz.gifview.library.GifView;
import com.m.k.mvp.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MvpLoadingView extends ConstraintLayout {

    public static final int MODE_POP = 1;
    public static final int MODE_FULL = 2;
    private static final int MODE_ERROR= 3;// 错误

    private ImageView mGifBackgroundView;
    private GifView mGifView;
    private Group mErrorPage;
    private ImageView mErrorIcon;
    private TextView mErrorMessage;
    private Button mRetry;

    private ViewGroup mParent;

    private OnRetryCallBack mRetryCallBack;
    private OnCancelCallBack mCancelCallBack;

    private int mCurrentMode;
    private int mPreMode;

    private boolean isEnableBackCancel; // 按返回键是否支持关闭 loading

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MODE_POP, MODE_FULL})
    public @interface LoadingMode {
    }


    public MvpLoadingView(Context context) {
        super(context);
    }

    public MvpLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MvpLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setId(10000);
        mGifBackgroundView = findViewById(R.id.mvp_loading_iv_gif_bg_view);
        mGifView = findViewById(R.id.mvp_loading_gif_view);
        mErrorPage = findViewById(R.id.mvp_loading_group_error);
        mErrorIcon = findViewById(R.id.mvp_loading_iv_error_icon);
        mErrorMessage = findViewById(R.id.mvp_loading_tv_error_content);
        mRetry = findViewById(R.id.mvp_loading_btn_retry);


        mRetry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRetryCallBack != null){
                    mRetryCallBack.onRetry();
                    showLoading(mPreMode);
                }
            }
        });
        setFocusableInTouchMode(true);
        requestFocus();
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // 如果支持网络取消，那么关闭自己
                // 如果不支持，不拦截
                if(isEnableBackCancel){
                    if(mCancelCallBack != null){
                        mCancelCallBack.onCancel(); // 回到外面调用者去取消网络请求
                    }
                    closeLoading();
                    return true;
                }else{
                    return false;
                }
            }
        });
    }

    public void setEnableBackCancel(boolean enableBackCancel) {
        isEnableBackCancel = enableBackCancel;
    }

    public void setCancelCallBack(OnCancelCallBack cancelCallBack) {
        this.mCancelCallBack = cancelCallBack;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public void setParentContainer(ViewGroup parent) {
        mParent = parent;
    }


    public static MvpLoadingView inject(ViewGroup parent) {

        // 为了避免重复添加,那么判断一下传进来的 parent 上是否已经添加了loading view


        View child;
        for (int i = parent.getChildCount() - 1; i >= 0; i--) {
            child = parent.getChildAt(i);
            if (child instanceof MvpLoadingView) {
                return (MvpLoadingView) child;
            }
        }


        MvpLoadingView loadingView = (MvpLoadingView) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_view, parent, false);

        loadingView.setParentContainer(parent);

        return loadingView;
    }

    public void showLoading(@LoadingMode int mode) {

        if (this.getParent() == null) {

            String parentClassName = mParent.getClass().getName();
            if (parentClassName.equals(RelativeLayout.class.getName()) || parentClassName.equals(FrameLayout.class.getName()) || parentClassName.equals(ContentFrameLayout.class.getName())) {
                mParent.addView(this, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            } else if (mParent instanceof ConstraintLayout) {

                mParent.addView(this);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone((ConstraintLayout) mParent);

                constraintSet.connect(getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
                constraintSet.connect(getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
                constraintSet.connect(getId(), ConstraintSet.END, mParent.getId(), ConstraintSet.END);
                constraintSet.connect(getId(), ConstraintSet.BOTTOM, mParent.getId(), ConstraintSet.BOTTOM);
                constraintSet.applyTo((ConstraintLayout) mParent);


            }
        }



        if(mCurrentMode == mode){
            return;
        }


        mPreMode = mCurrentMode;

        mCurrentMode = mode;

        mErrorPage.setVisibility(GONE);
        if (mode == MODE_POP) {
            setBackgroundColor(Color.TRANSPARENT);
            mGifBackgroundView.setVisibility(VISIBLE);
        } else {
            setBackgroundColor(Color.WHITE);
            mGifBackgroundView.setVisibility(GONE);
        }
        mGifView.setVisibility(VISIBLE);
        mGifView.play();

}

    public void onError(){
        onError(getContext().getString(R.string.text_error_default),null);
    }

    public void onError(OnRetryCallBack onRetryCallBack){
        onError(getContext().getString(R.string.text_error_default),onRetryCallBack);
    }

    public void onError(String errMessage,OnRetryCallBack onRetryCallBack){

        mRetryCallBack = onRetryCallBack;
        mGifView.pause();
        mGifView.setVisibility(GONE);
        mGifBackgroundView.setVisibility(GONE);

        mErrorPage.setVisibility(VISIBLE);

        if(mCurrentMode != MODE_FULL){
            setBackgroundColor(Color.WHITE);
        }
        mErrorMessage.setText(errMessage);
        mRetry.setVisibility(mRetryCallBack == null ? GONE : VISIBLE);

        mPreMode = mCurrentMode;

        mCurrentMode = MODE_ERROR;
    }


    public void closeLoading() {
        if (mParent != null) {
            mParent.removeView(this);
        }
    }


    public interface OnRetryCallBack{
        void onRetry();
    }
    public interface OnCancelCallBack{
        void onCancel();
    }
}
