package com.android.nejm.utils;

import android.app.Activity;
import android.content.Context;

import com.android.nejm.activitys.MainActivity;

import java.util.Stack;


/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 * Created by xiaoyu on 2017/8/9.
 */

public class AppManager {
    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager(){}
    /**
     * 单一实例
     */
    public static AppManager getAppManager(){
        if(instance==null){
            instance=new AppManager();
        }
        return instance;
    }
    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity){
//        if(activityStack==null){
//            activityStack=new Stack<Activity>();
//        }
//        if(!activityStack.contains(activity)){
//            activityStack.add(activity);
//        }
    }
    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity(){
        Activity activity=activityStack.lastElement();
        return activity;
    }
    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity(){
        Activity activity=activityStack.lastElement();
        finishActivity(activity);
    }
    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity){
        if(activity!=null){
            activityStack.remove(activity);
            activity.finish();
            activity=null;
        }
    }
    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls){
        Activity act=null;
        for (Activity activity : activityStack) {
            if(activity.getClass().equals(cls) ){
                act=activity;
            }
        }
        if(act!=null){
            finishActivity(act);
        }
    }
    /**
     * 结束所有Activity
     */
    public void finishAllActivity(){
        for (int i = 0, size = activityStack.size(); i < size; i++){
            if (null != activityStack.get(i)){
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }
    /**
     * 退出应用程序
     */
    public void appExit(Context context) {
        try {
            finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
//			ActivityManager activityMgr= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//			activityMgr.restartPackage(context.getPackageName());
//			System.exit(0);
        } catch (Exception e) {

        }
    }


    /**
     * 结束部分Activity
     */
    public void finishOtherActivity(){
        for (int i = 0, size = activityStack.size(); i < size; i++){
            if (null != activityStack.get(i)){
                Activity activity = activityStack.get(i);
                if (!activity.equals(MainActivity.class)) {
                    activityStack.get(i).finish();
                }
            }
        }
    }
}
