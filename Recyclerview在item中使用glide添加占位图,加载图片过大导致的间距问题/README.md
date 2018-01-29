## Recyclerview在item中使用glide添加占位图,加载图片过大导致的间距问题

[TOC]

加载的图片过大或图片大小不定,只是尺寸(宽高比)一定的话,使用glide添加占位图会导致间距过大,这时需要给imageview添加属性:

~~~~java
android:adjustViewBounds="true"
~~~~

图片自适应大小就好了.

但是别设置

~~~~java
android:scaleType = "center"
~~~~

会出现imageview图片和占位图叠加,导致背景色异常的情况.