package com.example.administrator.rxcachedemo.application;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;

/**
 * @author ZhangXuanChen
 * @date 2017/4/27
 * @package com.ftnew.database.application
 * @description Application基类
 */
public class BaseApplication extends Application {
    private static ArrayList<Activity> activityStack;
    private static BaseApplication mAppUtil;

    /**
     * 单例
     *
     * @return mAppUtil
     */
    public static BaseApplication getInstance() {
        if (mAppUtil == null) {
            mAppUtil = new BaseApplication();
        }
        return mAppUtil;
    }

    /**
     * 获取Activity栈
     *
     * @return
     */
    public ArrayList<Activity> getActivityStack() {
        return activityStack;
    }

    /**
     * 添加Activity到堆栈
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new ArrayList<>();
        }
        activityStack.add(activity);
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        try {
            for (int i = 0, size = activityStack.size(); i < size; i++) {
                if (null != activityStack.get(i)) {
                    finishActivity(activityStack.get(i));
                }
            }
            activityStack.clear();
        } catch (Exception e) {
        }
    }

    /**
     * 结束指定Activity
     *
     * @param activity
     */
    private void finishActivity(Activity activity) {
        try {
            if (activity != null) {
                activityStack.remove(activity);
                activity.finish();
                activity = null;
            }
        } catch (Exception e) {
        }
    }

    /**
     * 结束指定Activity
     *
     * @param activites 栈内需结束的activity类
     */
    public void finishActivity(Class<?>[] activites) {
        try {
            if (activites != null && activites.length > 0) {
                for (int i = 0; i < activites.length; i++) {
                    String activityName = activites[i].getSimpleName();
                    for (int j = activityStack.size() - 1; j >= 0; j--) {
                        Activity activity = activityStack.get(j);
                        String name = activity.getClass().getSimpleName();
                        if (name.equals(activityName)) {
                            finishActivity(activity);
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * 退出应用程序(app首页退出时调用)
     */
    public void AppExit() {
        try {
            finishAllActivity();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
        }
    }
}
