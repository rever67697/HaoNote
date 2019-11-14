# android问题收集

[TOC]

## Unable to create layer for RoundRectView

~~~~java
JNI DETECTED ERROR IN APPLICATION: JNI CallObjectMethod called with pending exception java.lang.IllegalStateException: Unable to create layer for RoundRectView, size 640x19919 exceeds max size 16384'
~~~~

报错信息在debug级别的日志里边：

![1](C:\Users\dell\Desktop\HaoNote\android问题收集\1.png)

一般都出现在scrollview加载webview，或者加载的textview中有加载html的代码。

解决方案：只需要在`mianfest`中将硬件加速关闭即可（硬件加速默认开启）。

~~~~java
android:hardwareAccelerated="false"
~~~~



## 打包apkInfo must not be null

`android studio`打包的时候出现问题.

由于使用`RePlugin`插件化开发,把studio的配置改为了:

~~~~
classpath 'com.android.tools.build:gradle:3.1.4'
~~~~

gradle改为了4.4.

然后打包的时候报错:

![4](C:\Users\dell\Desktop\HaoNote\android问题收集\4.png)

但是点击左边,查看message详情信息却显示成功,,而且安装包可以安装运行.

![2](C:\Users\dell\Desktop\HaoNote\android问题收集\2.png)

这是因为打包的时候生成的`output.json`冲突导致的,在打包的时候,把apk文件的输出目录换到非该项目路径就好了,如:可以放到C盘,D盘下:

![3](C:\Users\dell\Desktop\HaoNote\android问题收集\3.png)



## 项目有相同的依赖库的不同版本时

项目下载的依赖,或者依赖库里依赖的库,有不同版本时,会出现问题,可以在app的`build.gradle`中固定版本.

~~~~java
dependencies {
	configurations.all {
        resolutionStrategy {
            force 'com.github.bumptech.glide:glide:3.7.0'
        }
    }
}
~~~~

