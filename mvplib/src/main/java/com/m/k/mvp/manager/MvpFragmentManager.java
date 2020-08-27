package com.m.k.mvp.manager;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.m.k.mvp.base.BaseFragment;

public class MvpFragmentManager {


    /**
     * add  -- remove
     *
     * replace -> （add remove）
     *
     *
     * show --> hide
     * attach --> detach
     *
     *
     *
     * 某一次操作，add B hide A
     *
     * 返回时： remove B  show A
     */




    public static BaseFragment addOrShowFragment(FragmentManager manager, Class<? extends BaseFragment> willShowFragment,BaseFragment preFragment,@IdRes int containerId){

        FragmentTransaction transaction = manager.beginTransaction();
        try {

            String tag = willShowFragment.getName();

            BaseFragment  willShowFragmentInstance = (BaseFragment) manager.findFragmentByTag(tag);

            if(willShowFragmentInstance == null){ // 当前activity 已经添加了 该fragment

                willShowFragmentInstance = willShowFragment.newInstance();

                /**
                 * `17: 18  从 A  进入B   此时事务里面记录的 将要显示的fragment 是B ，上一个是A 。 提交过后，这个事务这个记录是不不是已经被记录了。17： 20 的时候
                 * 按返回键。拿出 17：18 分记录的那个事务，进行逆向操作。
                 *
                 * 比如：从A 里面 要显示B 。 B 就是将要显示的fragment .A 就是上一个 fragment
                 * 第一个 enter : 表示将要显示的 fragment 进入的动画
                 * 第二个 exit : 新fragment 进入，上一个fragment 退出的动画
                 * 第三个 popEnter : 表示按返回键时上一个fragment 弹进的动画
                 * 第四个 popExit: 表示按返回键时 将要显示的fragment 被弹出的动画
                 */
                transaction.setCustomAnimations(

                        willShowFragmentInstance.isNeedAnimation() ?   willShowFragmentInstance.getEnterAnimation() : 0,
                        (preFragment == null ? 0 : (preFragment.isNeedAnimation() ? preFragment.getExitAnimation() :0)),
                        (preFragment == null ? 0 : (preFragment .isNeedAnimation() ? preFragment.getPopEnterAnimation() : 0))
                        , willShowFragmentInstance .isNeedAnimation() ? willShowFragmentInstance.getPopExitAnimation() : 0);

                transaction.add(containerId,willShowFragmentInstance, tag);

                if(willShowFragmentInstance.isNeedAddToBackStack()){
                    transaction.addToBackStack(tag); // 把这一次事务添加到回退栈里面
                }

            }else{ // 当前 activity 已经添加过这个fragment

               int count = manager.getBackStackEntryCount(); // 获取回退栈里面 事务的个数

                FragmentManager.BackStackEntry entry = null;
                for(int i = 0; i < count; i++){
                    entry = manager.getBackStackEntryAt(i);
                    if(entry.getName().equals(tag)){
                        manager.popBackStackImmediate(tag,0);
                        return willShowFragmentInstance;
                    }
                }
                if(count > 0){
                    manager.popBackStackImmediate(manager.getBackStackEntryAt(0).getName(),FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    return willShowFragmentInstance;
                }else{
                    if(!willShowFragmentInstance.isAdded()){ // 如果没有添加
                        transaction.add(containerId,willShowFragmentInstance, tag);
                    }
                    if(willShowFragmentInstance.isDetached()){ // 如果从activity  detach 了
                        transaction.attach(willShowFragmentInstance);
                    }
                    if(willShowFragmentInstance.isHidden()){// 如果被隐藏了
                        transaction.show(willShowFragmentInstance);
                    }

                }
            }

            handPreFragment(transaction,preFragment,willShowFragmentInstance);
            transaction.commit();
            return willShowFragmentInstance; // 把自己（即将要显示的） 返回出去。
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    private static void handPreFragment(FragmentTransaction transaction,BaseFragment preFragment,BaseFragment willShowFragment){
        if(preFragment != null){

            switch (willShowFragment.getActionFroPreFragment()){
                case REMOVE:{
                    transaction.remove(preFragment);
                    break;
                }
                case DETACH:{
                    transaction.detach(preFragment);
                    break;
                }
                case HIDE:{
                    transaction.hide(preFragment);
                    break;
                }
            }
        }

    }
}
