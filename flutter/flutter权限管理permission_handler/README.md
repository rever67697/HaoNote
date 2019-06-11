# flutter权限管理permission_handler

[TOC]

## 添加依赖

~~~~xml
  #权限
  permission_handler: ^3.0.0
~~~~

## 使用

在`android`的`mainfest`中添加权限:

~~~~java
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.CAMERA"/>
    <!-- 这个权限用于进行网络定位 -->
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
    <!-- 这个权限用于访问GPS定位 -->
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
~~~~

然后在代码中请求:

~~~~java
import 'package:permission_handler/permission_handler.dart';
requestPermiss() async {
    //请求权限
    Map<PermissionGroup, PermissionStatus> permissions =
    await PermissionHandler()
        .requestPermissions([PermissionGroup.location,PermissionGroup.camera]);
    //校验权限
    if(permissions[PermissionGroup.camera] != PermissionStatus.granted){
      print("无照相权限");
    }
    if(permissions[PermissionGroup.location] != PermissionStatus.granted){
      print("无定位权限");
    }
  }
~~~~

打开设置页面:

~~~~java
bool isOpened = await PermissionHandler().openAppSettings();
~~~~

