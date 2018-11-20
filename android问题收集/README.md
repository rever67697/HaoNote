# android问题收集

[TOC]

1. ~~~~java
   JNI DETECTED ERROR IN APPLICATION: JNI CallObjectMethod called with pending exception java.lang.IllegalStateException: Unable to create layer for RoundRectView, size 640x19919 exceeds max size 16384'
   ~~~~

报错信息在debug级别的日志里边：

![1](C:\Users\Administrator\Desktop\HaoNote\android问题收集\1.png)

一般都出现在scrollview加载webview，或者加载的textview中有加载html的代码。

解决方案：只需要在`mianfest`中将硬件加速关闭即可（硬件加速默认开启）。

~~~~java
android:hardwareAccelerated="false"
~~~~

