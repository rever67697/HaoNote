# Monkey测试

需要环境变量先配置安卓的环境和adb的环境

adb的环境需要:

~~~~
%ANDROID_HOME%\platform-tools
%ANDROID_HOME%\tools
~~~~

然后cmd输入:

~~~~
adb shell monkey -v -p com.allhigh 1000
~~~~

就可以看到手机在测试了.最后两个参数是包名和次数.



如果不想从控制台看日志,可以选择输出日志路径:

````
adb shell monkey -v -p com.allhigh 100000 >C:\Users\dell\Desktop\1.txt
````

