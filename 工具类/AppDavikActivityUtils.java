package com.jingewenku.abrahamcaijin.commonutil;


import android.app.Activity;

import java.util.Stack;

/**
 * 主要功能: 管理和回收Activity
 * @author: hao
 * 	method:
			getScreenManager      : 单例堆栈集合对象
			removeActivity        : 堆栈中销毁并移除
			removeAllActivity     : 栈中销毁并移除所有Act对象
			currentActivity       : 取当前Act对象
			getCurrentActivityName: 获得当前Act的类名
			addActivity           : 将Act纳入推栈集合中
			exitApp               : 退出栈中所有Activity
 */
public class AppDavikActivityUtils {

    
    //存储ActivityStack
    private static Stack<Activity> activityStack = new Stack<Activity>();

    //单例模式
    private static AppDavikActivityUtils instance;


    /**
     * 单列堆栈集合对象
     * @return AppDavikActivityUtils 单利堆栈集合对象
     */
    public static AppDavikActivityUtils getScreenManager() {
        if (instance == null) {
            instance = new AppDavikActivityUtils();
        }
        return instance;
    }
    

    /**
     * 堆栈中销毁并移除
     * @param activity 指定Act对象
     */
    public void removeActivity(Activity activity) {
        AppLogMessageMgr.i("AppDavikActivityUtils-->>removeActivity", activity != null ? activity.toString() : "");
        if (null != activity) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }



    /**
     * 栈中销毁并移除所有Act对象
     */
    public void removeAllActivity() {
        if (null != activityStack && activityStack.size() > 0) {
                //创建临时集合对象
                Stack<Activity> activityTemp = new Stack<Activity>();
                for (Activity activity : activityStack) {
                    if (null != activity) {
                        //添加到临时集合中
                        activityTemp.add(activity);
                        //结束Activity
                        activity.finish();
                    }
                }
                activityStack.removeAll(activityTemp);
        }
        AppLogMessageMgr.i("AppDavikActivityUtils-->>removeAllActivity", "removeAllActivity");
        System.gc();
        System.exit(0);
    }


    /**
     * 获取当前Act对象
     * @return Activity 当前act
     */
    public Activity currentActivity() {
        Activity activity = null;
        if (!activityStack.empty()){
            activity = activityStack.lastElement();
        }
        AppLogMessageMgr.i("AppDavikActivityUtils-->>currentActivity", activity + "");
        return activity;
    }


    /**
     * 获得当前Act的类名
     * @return String
     */
    public String getCurrentActivityName() {
        String actSimpleName = "";
        if (!activityStack.empty()) {
            actSimpleName = activityStack.lastElement().getClass().getSimpleName();
        }
        AppLogMessageMgr.i("AppDavikActivityUtils-->>getCurrentActivityName", actSimpleName);
        return actSimpleName;
    }


    /**
     * 将Act纳入推栈集合中
     * @param activity Act对象
     */
    public void addActivity(Activity activity) {
        AppLogMessageMgr.i("AppDavikActivityUtils-->>addActivity",  activity != null ? activity.toString() : "");
        if (null == activityStack) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }
    
    

    /**
     * 退出栈中所有Activity
     * @param cls
     * @return void
     */
    public void exitApp(Class<?> cls) {
        AppLogMessageMgr.i("AppDavikActivityUtils-->>exitApp", "exitApp-->>占用内存：" + Runtime.getRuntime().totalMemory());
        while (true) {
            Activity activity = currentActivity();
            if (null == activity) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                break;
            }
            removeActivity(activity);
        }
        System.gc();
        System.exit(0);
    }

    
}
