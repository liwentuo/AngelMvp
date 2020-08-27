package com.m.k.mvp.widgets;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.m.k.mvp.R;

public class TogglePasswordButton extends AppCompatImageView {


    private boolean isOpen  = false; // 默认眼睛是闭着的
    private EditText mPassword;

    private OnClickListener mListener;

    public TogglePasswordButton(Context context) {
        super(context);
        initClickListener();
    }

    public TogglePasswordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClickListener();
    }


    public TogglePasswordButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initClickListener();
    }


    private void initClickListener(){
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onClick(v);
                }
                if(mPassword != null){
                    toggle();
                }
            }
        });
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mListener = l;
    }


    private void toggle(){
        if(isOpen){
            setBackgroundResource(R.drawable.ic_eyes_close);
            isOpen = false;
            mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        }else{
           setBackgroundResource(R.drawable.ic_eyes_open);
            mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isOpen = true;
        }
    }


    public void bindEditText(EditText editText){

        mPassword = editText;


        hideOrShow(editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               hideOrShow(editText);
            }
        });
    }


    private void hideOrShow(EditText editText){
        if(editText.getText().toString().length() > 0){
            setVisibility(VISIBLE);
        }else{
            setVisibility(GONE);
        }
    }
}
