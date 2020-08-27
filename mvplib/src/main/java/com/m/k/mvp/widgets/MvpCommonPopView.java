package com.m.k.mvp.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.m.k.mvp.utils.MvpUtils;


/**
 * popupwindow 底层实际上是通过  windowmanager 添加 一个 叫 DecorView（FrameLayout）的方式实现的。
 * <p>
 * popupwindow 一般设计到三个 view,{1.mDecorView(FrameLayout),2.mBackgroundView(FrameLayout),3.mContentView(我们要显示的layout)}
 * <p>
 * 1 mDecorView： popupWindow 的根view，它的大小就是通过 设置popouWindow 的 宽高来决定的（setHeight，setWidth）
 * <p>
 * 2 mBackgroundView：它不一定会创建，取决于 是否调用了 setBackgroundDrawable 给  popupWindow 设置背景，如果设置了那么会创建一个 mBackgroundView
 * 然后添加到mDecorView 上，如下：
 * <p>
 * if (mBackground != null) { // 是否设置里背景
 * mBackgroundView = createBackgroundView(mContentView);
 * mBackgroundView.setBackground(mBackground);
 * } else {
 * mBackgroundView = mContentView;
 * }
 * <p>
 * //把我们的 mContentView 的传进来 创建 mBackgroundView
 * <p>
 * private PopupBackgroundView createBackgroundView(View contentView) {
 * final ViewGroup.LayoutParams layoutParams = mContentView.getLayoutParams();
 * final int height;
 * if (layoutParams != null && layoutParams.height == WRAP_CONTENT) {
 * height = WRAP_CONTENT;
 * } else {
 * height = MATCH_PARENT;
 * }
 * <p>
 * final PopupBackgroundView backgroundView = new PopupBackgroundView(mContext);
 * final PopupBackgroundView.LayoutParams listParams = new PopupBackgroundView.LayoutParams(
 * MATCH_PARENT, height);
 * <p>
 * // 把我们的 mContentView 添加到 mBackgroundView上面,
 * // mContentView的宽度是 MATCH_PARENT（和mBackgroundView的宽度一致）。
 * // mContentView的高度 取决于  mContentView.getLayoutParams() ，要么是MATCH_PARENT，要么是 WRAP_CONTENT
 * backgroundView.addView(contentView, listParams);
 * <p>
 * return backgroundView;
 * }
 * <p>
 * mDecorView = createDecorView(mBackgroundView);
 * <p>
 * // 通过把mBackgroundView 传进来 创建 mDecorView，
 * private PopupDecorView createDecorView(View contentView) {
 * final ViewGroup.LayoutParams layoutParams = mContentView.getLayoutParams();
 * final int height;
 * <p>
 * // 如果 mContentView 的 layoutParams ！= null && layoutParams.height == WRAP_CONTENT 时，高度为 wrap_content,否则为 match_parent
 * if (layoutParams != null && layoutParams.height == WRAP_CONTENT) {
 * height = WRAP_CONTENT;
 * } else {
 * height = MATCH_PARENT;
 * }
 * <p>
 * final PopupDecorView decorView = new PopupDecorView(mContext);
 * // 把 mBackgroundView添加到了 mDecorView上
 * // mBackgroundView 的宽度是 MATCH_PARENT（和 mDecorView 一样宽，也就是说等于 setWidth的值）
 * <p>
 * //mBackgroundView  的高度 取决于mBackgroundView.getLayoutParams(),要么是MATCH_PARENT，要么是 WRAP_CONTENT
 * <p>
 * decorView.addView(contentView, MATCH_PARENT, height);
 * decorView.setClipChildren(false);
 * decorView.setClipToPadding(false);
 * <p>
 * return decorView;
 * }
 * <p>
 * <p>
 * <p>
 * <p>
 * 3 mContentView，这就是我们调用popupwindow 的 setContentView 方法传进去我们要显示的内容view
 * <p>
 * <p>
 * 如果设置了pop 的backgrounddrawable 那么创建顺序是
 * <p>
 * 根据 contentview 创建 backgroundview,再根据backgroundview 创建 decorview，最后 windowmanager add  decorview
 * <p>
 * <p>
 * 如果没有设置pop 的backgrounddrawable的背景，那么创建顺序是
 * <p>
 * 直接把 contentview 赋值给 backgroundview。 更具 backgroundview 创建 decorview ，最后 windowmanager add  decorview
 * <p>
 * <p>
 * <p>
 * decorview 的宽高是由 setHeight 和 setWidth 决定的。
 * <p>
 * backgroundview 和 contentview 的 宽度都是不能修改的，都为 match_parent,高度要么是 wrap_content ,要么是 match_parent，只有当
 * <p>
 * contentview 的 layoutparams 不为 null， 并且其 layoutparams.height == wrap_content 时 ，高度为wrap_content 否则其他情况都为match_parent
 */


/**
 *
 *
 * 使用方式：
 * 1. 自己写一个子类继承  在子类内部去调用setContentView（view）以及对view 的初始化
 * 2. 直接new 一个 MvpCommonPopView 实例，调用 setContentView(view)；。在调用者里面对 view  进行初始化
 *
 * 然后调用 showCenter（view）方法 进行显示，把pop 显示在屏幕中间，view 参数可以是任意一个 view .
 *
 * 该pop 可以显示 一下几个功能：<p/>
 *
 * 1. 可以设置pop 外部区域点击时是否关闭pop ：{@link #setTouchOutsideDismiss(boolean)}<p/> // 默认不关闭
 * 2. 可以设置按返回键是否可以关闭pop : {@link #setOnBackKeyDismiss(boolean)}<p/> // 默认是按返回键关闭
 * 3. 可是设置点击pop 外部时事件是否传递到 pop 的后面 : {@link #setOutsideCanTouch(boolean)}<p/> // 默认不传递
 *
 *
 *
 */

public class MvpCommonPopView extends PopupWindow {


    private boolean isOutsideTouchDismiss; // 点击pop 外部是否消失
    private boolean isBackKeyDismiss = true;// 按返回键是否消失

    private  Activity context;


    public MvpCommonPopView(Context context, int width, int height) {
        super(context);

        setHeight(height); //
        setWidth(width);
        this.context = (Activity) context;
        init(context);
    }

    // 这个构造方法，pop 的高度是WRAP_CONTENT,宽度是 MATCH_PARENT
    public MvpCommonPopView(Context context) {
        this(context,WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);


    }

    protected void init(Context context) {
        // 在比较老的版本上，不设置背景就不能响应返回键和点击外部消失的，但是新版本google api 已经修改了，为了保险起见，设个背景最好
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        backgroundAlpha( 0.5f);
        /**
         * 设置widow 是否获取焦点，默认false,如果为false,那么点击pop以外的区域，事件会传递到popu后面的（behind）界面上.
         * 点击pop 以外的区域pop 不会消失，注意如果pop 里面有输入框，那么输入框不能弹出键盘
         * 如果设置为true,pop 会有焦点，点击外部时 pop 会消失。
         */

        setFocusable(true);
        // 这个方法是控制是否当pop 以外的区域点击时是否发送 MotionEvent.ACTION_OUTSIDE 通知 pop,true 通知，false 不通知，但是 这个方法必须要 setFocusable = false ,setTouchable = true 时才有效果
        setOutsideTouchable(true);

        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int x = (int) event.getX();
                final int y = (int) event.getY();


                if ((event.getAction() == MotionEvent.ACTION_DOWN) // 当 setFocusable为 true 点击外部会收到这个事件
                        && ((x < 0) || (x >= v.getWidth()) || (y < 0) || (y >= v.getHeight()))) {
                    if (isOutsideTouchDismiss) {
                        dismiss();

                    }
                    return true;

                }

               /* if (event.getAction() == MotionEvent.ACTION_OUTSIDE) { // ACTION_OUTSIDE 事件是 当 setOutsideTouchable(true) 方法 设置为true 时，才会被接受。否则不会接收这个事件
                    if (isOutsideTouchDismiss) {
                        CommonPopView.super.dismiss();
                        return true;
                    }
                }*/


                /**
                 * 返回 true 不拦截，走默认行为
                 *
                 *  @Override
                 *         public boolean onTouchEvent(MotionEvent event) {
                 *             final int x = (int) event.getX();
                 *             final int y = (int) event.getY();
                 *
                 *             if ((event.getAction() == MotionEvent.ACTION_DOWN)
                 *                     && ((x < 0) || (x >= getWidth()) || (y < 0) || (y >= getHeight()))) {
                 *                 dismiss();
                 *                 return true;
                 *             } else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                 *                 dismiss();
                 *                 return true;
                 *             } else {
                 *                 return super.onTouchEvent(event);
                 *             }
                 *         }
                 */
                return false;
            }
        });


    }


    /**
     * 设置点击pop 外部是否消失,这个方法只有在给pop 设置的
     *  宽高不是Match_parent 才有效，因为设置为了Match_parent这个都是pop,那就没有点击外部只说
     * @param dismiss
     */
    //
    public void setTouchOutsideDismiss(boolean dismiss) {
        isOutsideTouchDismiss = dismiss;


    }

    // view: 任意一个已经显示在界面上的view，注意如果这个view 没有添加的 window 上时，这个方法会报错。
    // 因为 pop 在显示的时候需要通过 view.getWidowToken.
    public void showCenter(View view){
        showAtLocation(view, Gravity.CENTER,0,0);
    }



    public void setOnBackKeyDismiss(boolean dismiss) {
        isBackKeyDismiss = dismiss;
    }

    @Override
    public void dismiss() {

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement traceElement = stackTrace[3];
        String fileName = traceElement.getFileName(); // 拿到调用 dismiss 方法的类名
        fileName = fileName.substring(0, fileName.lastIndexOf("."));
        if (!fileName.equals(PopupWindow.class.getSimpleName())) { // 如果调用者不是从系统来的，那么就是我们认为的
            destroy();
            super.dismiss();
        } else {
            if (!isBackKeyDismiss) {
                return;
            } else {
                destroy();
                super.dismiss();
            }
        }


    }

    private void destroy(){
        backgroundAlpha(1);
        this.context = null;
    }


    /**
     * 设置pop 外部是否可点击，也就是点击pop 的外部 能把事件传递到pop 的 behind
     * <p>
     * 注意：如果 设置为true,那么再设置点击外部关闭 pop 将会无效，因此在为true 想关闭 pop ,必须手动关闭,调用close
     *
     * @param touch
     */
    public void setOutsideCanTouch(boolean touch) {

        // 设置为false， 点击pop 外部，事件会传递到 pop 的behind,设置为true ,不会。虽然设置为false ,但是 pop 里面如果有 输入框任然可以获取焦点，弹出键盘，这和 setFocusable = false 不一样。
        if(MvpUtils.hasQ()){
            setTouchModal(!touch);
        }

    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha( float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }
}
