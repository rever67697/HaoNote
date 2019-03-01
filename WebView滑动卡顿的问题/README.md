# WebView滑动卡顿的问题

解决方案：启用硬件加速。

在`Mainfest.xml`中

1.可以再`application`标签中全局开启。

2.可以再`activity`中开启，即只在本页面开启硬件加速。

~~~~java
android:hardwareAccelerated="true"
~~~~

