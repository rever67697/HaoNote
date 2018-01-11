# android7.0PopWindow显示位置错误的解决方法

[TOC]

解决方案: 重写PopWindow的showAsDropDown()方法.

## 详细步骤

1.新建类MyPopWindow,继承PopupWindow.

2.重写showAsDropDown()方法.

~~~~java
public class MyPopWindow extends PopupWindow {


    public MyPopWindow(View contentView, int width, int height) {
        super(contentView,width,height);
    }

    @Override
    public void showAsDropDown(View anchor) {
        if(Build.VERSION.SDK_INT == 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if(Build.VERSION.SDK_INT == 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor, xoff, yoff);
    }

}
~~~~

3.把使用PopupWindow的地方改为自己写的MyPopWindow.

### 注意:

此种方法只能在静态布局中,即不能有数据动态的加入(例如: recyclerview),否则显示位置会变.

如果有recyclerview或者其他,可以尝试使用**bottomsheet**,  github网址如下:

https://github.com/Flipboard/bottomsheet



