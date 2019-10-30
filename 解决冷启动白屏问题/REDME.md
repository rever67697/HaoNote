# 解决冷启动白屏问题

添加主题:

~~~~java
 <style name="SplashStyle" parent="Theme.AppCompat.Light.NoActionBar">
        <item name="android:windowNoTitle">true</item>
        <item name="android:textAllCaps">false</item>
        <item name="windowActionBar">false</item>
        <item name="android:windowFullscreen">true</item>
        <item name="android:windowBackground">@drawable/launch_background</item>
    </style>
~~~~

`launch_background.xml`文件:

~~~~java
<?xml version="1.0" encoding="utf-8"?>
<!-- Modify this file to customize your launch splash screen -->
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:drawable="@android:color/white" />

   <item>
        <bitmap
            android:gravity="center"
            android:src="@mipmap/ic_splash" />
    </item>
</layer-list>
~~~~

然后在`Mainfest`中设置这个闪屏页的activity的`theme`为这个主题.