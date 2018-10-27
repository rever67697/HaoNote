## 监听软键盘开启

[TOC]

我们要做软键盘启动的时候把页面顶上去,需要以下步骤:

1.在MainFest.xml文件中的这个activity中加入属性

~~~~java
android:windowSoftInputMode="stateHidden|adjustResize"
~~~~

2.在这个页面最外层用ScrollView包裹,然后监听软键盘启动.

~~~~java
//屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    
     //监听到软键盘弹起
        mScrollView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right,
                                       int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                    //监听到软键盘弹起
                    mScrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            mScrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });

                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    //监听到软件盘关闭.

                }
            }
        });
~~~~



注意,如果使用沉浸式,监听可能会不起作用,因为沉浸式很可能让oldBottom - bottom == 0.

下面,推荐一种沉浸式实现监听软键盘的方案.

使用**ImmersionBar**实现沉浸式.

地址:https://github.com/gyf-dev/ImmersionBar

然后这样设置

~~~~java
ImmersionBar.with(this).statusBarColor(R.color.status_bar).statusBarDarkFont(true)
                .keyboardEnable(true) //解决软键盘与底部输入框冲突问题 ，默认是false
                .setOnKeyboardListener(new OnKeyboardListener() {  //软键盘弹出关闭的回调监听
                    @Override
                    public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
                        if (isPopup) {
                            //弹出软键盘
                            mScrollView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mScrollView.fullScroll(View.FOCUS_DOWN);
                                }
                            });
                        }else {
                            //关闭软键盘
                        }
                    }
                })
                .init();
~~~~

另外一种使用沉浸式的方案就是:

最外层布局设置属性:

~~~~java
android:fitsSystemWindows="true"
~~~~

然后mainfest.xml在此activity添加属性:

~~~~java
android:windowSoftInputMode="stateHidden|adjustResize"
~~~~

https://blog.csdn.net/smileiam/article/details/69055963