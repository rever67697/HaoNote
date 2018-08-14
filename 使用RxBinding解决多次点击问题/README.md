## 使用RxBinding解决多次点击问题

[TOC]

### 添加依赖

~~~~JAVA
implementation 'com.jakewharton.rxbinding:rxbinding:0.4.0'
implementation 'io.reactivex.rxjava2:rxandroid:2.0.1'
~~~~

### 使用

~~~~java
 RxView.clicks(view)
                .throttleFirst(2, TimeUnit.SECONDS) //取两秒内的点击反应
                .subscribe(new Observer<Void>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Void aVoid) {
                       //在这里写点击事件的处理
                    }
                });
~~~~

