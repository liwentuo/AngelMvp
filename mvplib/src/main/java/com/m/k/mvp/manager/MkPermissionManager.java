package com.m.k.mvp.manager;

import android.annotation.SuppressLint;
import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


import com.tbruyelle.rxpermissions3.RxPermissions;

import java.util.ArrayList;
import java.util.Arrays;

import io.reactivex.rxjava3.functions.Consumer;


/*
 * ActivityCompat.shouldShowRequestPermissionRationale 它返回一个boolean 类型，但是分两种情况：
 *
 * 1. 如果用户之前决绝过授权，如果都没有点击不在询问的checkbox ,那么 return true,只要有一个点击了不在询问的checkbox return false，
 * 2. 如果是在第一次请求权限之前，调用这个方法。return false; 因为用户都没有拒绝过，所有不用去显示解释dialog
 *
 *
 **/
public class MkPermissionManager {



    private RxPermissions rxPermissions;


    public MkPermissionManager(FragmentActivity fragmentActivity) {
        rxPermissions = new RxPermissions(fragmentActivity);
    }

    public MkPermissionManager(Fragment fragment) {
        rxPermissions = new RxPermissions(fragment);
    }



    @SuppressLint("CheckResult")
    public void checkPermission(final Activity activity, final OnPermissionCallBack callBack, final String [] must,String [] optional){

        String all [] = concat(must, optional);

        final String [] unGrantedArr = getUnGrantedPermissions(all);


        if(unGrantedArr != null && unGrantedArr.length > 0){
            rxPermissions.shouldShowRequestPermissionRationale(activity,unGrantedArr).subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean aBoolean) throws Exception {
                    if(aBoolean){ // 只有用户之前拒绝过。并且所有权限都决绝时都没有点击 不在询问得checkbox，才会需要显示解释对话框
                        callBack.shouldShowRationale(new PermissionCall() {
                            @Override
                            public void requestPermission()  {
                                rxPermissions.request(unGrantedArr).subscribe(new Consumer<Boolean>() {
                                    @Override
                                    public void accept(Boolean aBoolean) throws Exception {
                                        if(aBoolean){
                                            callBack.onAllMustAccept();
                                        }else{
                                            if(verifyPermissions(must)){
                                                callBack.onAllMustAccept();
                                            }else{
                                                callBack.onDenied();
                                            }

                                        }
                                    }
                                });
                            }
                        },unGrantedArr);
                    }else{
                        rxPermissions.request(unGrantedArr).subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if(!aBoolean){
                                    if(verifyPermissions(must)){
                                        callBack.onAllMustAccept();
                                    }else{
                                        rxPermissions.shouldShowRequestPermissionRationale(activity, unGrantedArr).subscribe(new Consumer<Boolean>() {
                                            @Override
                                            public void accept(Boolean aBoolean) throws Exception {
                                                if(!aBoolean){
                                                    callBack.shouldShowPermissionSetting();
                                                }else{
                                                    callBack.onDenied();
                                                }
                                            }
                                        });
                                    }
                                }else{
                                    callBack.onAllMustAccept();

                                }
                            }
                        });
                    }
                }
            });
        }else{
            callBack.onAllMustAccept();
        }
    }

    private String [] concat(String [] must,String optional []){

        ArrayList<String> arrayList = new ArrayList<>();

        if(must != null){
           arrayList.addAll( Arrays.asList(must));
        }

        if(optional != null){
            arrayList.addAll( Arrays.asList(optional));
        }

        return arrayList.toArray(new String[0]);
    }






    private boolean verifyPermissions(String ...permissions){
        if (permissions.length < 1) {
            return true;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (String per : permissions) {
            if(!rxPermissions.isGranted(per)){
                return false;
            }
        }
        return true;
    }

    /**
     * 获取所有权限中，未授权的
     * @param permissions
     * @return
     */
    private String [] getUnGrantedPermissions(String ...permissions){
        ArrayList<String> list = new ArrayList<>();
        for(String permission : permissions){
            if(!rxPermissions.isGranted(permission)){
                list.add(permission);
            }
        }

        if(list.size() > 0){
            return list.toArray(new String[0]);
        }
        return null;
    }



    public interface OnPermissionCallBack{
        //  所有必须授权的权限都授权了，
         void onAllMustAccept();
         // 需要显示一个解释说明的对话框,如果用户点击了同意就调用call.requestPermission 方法，如果用户拒绝，那么你需要判断一下拒绝权限里面是否包含不要权限，如果不包含，即使拒绝了，也可以继续下一步
         void shouldShowRationale(PermissionCall call,String unGrantedPermissions []);
         // 当用户在授权dialog 选中了不在询问得checkbox 时，你需要引导用户去权限设置页面手动开启权限
         void shouldShowPermissionSetting();
         // 所有必须授权的权限只有有一个没有授权都会回掉这个。
         void onDenied();
    }

    public interface PermissionCall{
        //当显示 解释对话框时，用户点击 "确认" 或者 "同意授权时" 按钮时，调用该方法让该工具类去请求授权
        void requestPermission();
    }
}
