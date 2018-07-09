## 使用RxTool进行动态权限申请

[TOC]

### 添加依赖

~~~~JAVA
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
~~~~

~~~~java
dependencies {
       //基础工具库
       implementation "com.github.vondear.RxTool:RxKit:v2.0.4"

       //UI库
       implementation "com.github.vondear.RxTool:RxUI:v2.0.4"

       //功能库（Zxing扫描与生成二维码条形码 支付宝 微信）
       implementation "com.github.vondear.RxTool:RxFeature:v2.0.4"

       //ArcGis For Android工具库（API：100.1以上版本）
       implementation "com.github.vondear.RxTool:RxArcGisKit:v2.0.4"
}

~~~~

若只使用动态申请权限,则只需要使用基础工具库即可.

### 在application中初始化

~~~~java
RxTool.init(this);
~~~~

### 使用

~~~~java
RxPermissionsTool.
                with(MainActivity.this).
                addPermission(Manifest.permission.ACCESS_FINE_LOCATION).
                addPermission(Manifest.permission.ACCESS_COARSE_LOCATION).
                addPermission(Manifest.permission.READ_EXTERNAL_STORAGE).
                addPermission(Manifest.permission.CAMERA).
                addPermission(Manifest.permission.CALL_PHONE).
                addPermission(Manifest.permission.READ_PHONE_STATE).
                initPermission();
~~~~


