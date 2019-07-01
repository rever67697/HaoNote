# flutter和主线程不同线程的隔离器的定时器

[TOC]

## android_alarm_manager

一个Flutter插件，用于访问Android AlarmManager服务（定时服务，并且和主线程不在一个隔离区中，程序退出后还会继续执行定时器），并在警报触发时在后台运行Dart代码。

地址：<https://github.com/flutter/plugins/tree/master/packages/android_alarm_manager>

## 使用

1. 添加依赖：

   ~~~java
   android_alarm_manager: ^0.4.1+6
   ~~~

​		如果程序运行报错，那就把依赖包下载下来放到项目中。

2. 在`AndroidManifest.xml`中添加权限

   ~~~~java
   <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
   <uses-permission android:name="android.permission.WAKE_LOCK"/>
   ~~~~

   并且在`<application></application>`标签中添加：

   ~~~~java
   <service
       android:name="io.flutter.plugins.androidalarmmanager.AlarmService"
       android:permission="android.permission.BIND_JOB_SERVICE"
       android:exported="false"/>
   <receiver
       android:name="io.flutter.plugins.androidalarmmanager.AlarmBroadcastReceiver"
       android:exported="false"/>
   <receiver
       android:name="io.flutter.plugins.androidalarmmanager.RebootBroadcastReceiver"
       android:enabled="false">
       <intent-filter>
           <action android:name="android.intent.action.BOOT_COMPLETED"></action>
       </intent-filter>
   </receiver>
   ~~~~

3. 在`Application`中注册。

   ~~~~java
   public class Application extends FlutterApplication implements PluginRegistrantCallback {
     @Override
     public void onCreate() {
       super.onCreate();
       AlarmService.setPluginRegistrant(this);
     }
   
     @Override
     public void registerWith(PluginRegistry registry) {
       GeneratedPluginRegistrant.registerWith(registry);
     }
   }
   ~~~~

   如果不在`application`中调用`setPluginRegistrant()`方法,将会抛出异常。

4. 在dart文件中初始化并使用。

   ~~~~dart
   import 'dart:async';
   import 'package:android_alarm_manager/android_alarm_manager.dart';
   import 'package:flutter/widgets.dart';
   
   void printMessage(String msg) => print('[${DateTime.now()}] $msg');
   
   void printPeriodic() => printMessage("Periodic!");
   void printOneShot() => printMessage("One shot!");
   
   Future<void> main() async {
     final int periodicID = 0;
     final int oneShotID = 1;
   
     // 初始化AlarmManager service.
     await AndroidAlarmManager.initialize();
   
     printMessage("main run");
       
     runApp(const Center(
         child:
             Text('See device log for output', textDirection: TextDirection.ltr)));
     //每隔1分钟调用一次,一直调用
     await AndroidAlarmManager.periodic(
         const Duration(seconds: 1), periodicID, printPeriodic,
         wakeup: true);
     //只调用一次
     await AndroidAlarmManager.oneShot(
         const Duration(seconds: 1), oneShotID, printOneShot);
   }
   ~~~~

   

   ## 效果

   控制台打印日志：

   

   ![1](C:\Users\dell\Desktop\HaoNote\flutter\flutter和主线程不通隔离器的定时器\1.png)

