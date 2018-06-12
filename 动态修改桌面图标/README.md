# 动态修改桌面图标

[TOC]

效果如下:

![1](C:\Users\Administrator\Desktop\HaoNote\动态修改桌面图标\1.gif)



首先,在Manifest中添加权限

~~~~xml
<uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES" />
~~~~

并添加activity-alias标签:

~~~~xml
<application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN"/>

                <category android:name="android.intent.category.LAUNCHER"/>
            </intent-filter>
        </activity>
		<!--添加的代码-->
        <activity-alias
            android:name=".newsLuncherActivity"
            android:enabled="false"
            android:icon="@mipmap/tab_home_selected"
            android:label="@string/app_name"
            android:targetActivity=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN"/>
                <category android:name="android.intent.category.LAUNCHER"/>
            </intent-filter>
        </activity-alias>
    </application>
~~~~

然后,在activity中调如下方法就可以了:

~~~~java
private void changeLuncher() {
		//注意:com.example.administrator.updateicondemo是你的包名 		         
		//newsLuncherActivity是activity-alias的name
        String name = "com.example.administrator.updateicondemo.newsLuncherActivity";
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(getComponentName(),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(MainActivity.this, name),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        //Intent 重启 Launcher 应用
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        List<ResolveInfo> resolves = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo res : resolves) {
            if (res.activityInfo != null) {
                ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                am.killBackgroundProcesses(res.activityInfo.packageName);
            }
        }
        Toast.makeText(this,"桌面图标已更换",Toast.LENGTH_LONG).show();
    }
~~~~

