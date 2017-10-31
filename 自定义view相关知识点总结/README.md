# 自定义view相关知识点总结

[TOC]



## 一、坐标系	

### 屏幕默认坐标系

 ![1](C:\Users\C\Desktop\笔记\自定义view相关知识点总结\1.png)

### View的坐标系

​	view的坐标系统是相对于父控件而言的.

~~~~JAVA
  getTop();       //获取子View左上角距父View顶部的距离
  getLeft();      //获取子View左上角距父View左侧的距离
  getBottom();    //获取子View右下角距父View顶部的距离
  getRight();     //获取子View右下角距父View左侧的距离
~~~~

如下图所示:

 ![2](C:\Users\C\Desktop\笔记\自定义view相关知识点总结\2.png)

### MotionEvent中的get和getRaw的区别

~~~~JAVA
 	event.getX();       //触摸点相对于其所在组件坐标系的坐标
    event.getY();

    event.getRawX();    //触摸点相对于屏幕默认坐标系的坐标
    event.getRawY();
~~~~

如图所示:

 ![3](C:\Users\C\Desktop\笔记\自定义view相关知识点总结\3.png)

## 二、自定义View的流程和分类

### 1.自定义view的分类

* 自定义ViewGroup :大多继承ViewGroup或者各种Layout.
* 自定义View:继承各种view

### 2.构造器参数分析

view的构造函数有四种:

~~~~java
  public void SloopView(Context context) {}
  public void SloopView(Context context, AttributeSet attrs) {}
  public void SloopView(Context context, AttributeSet attrs, int defStyleAttr) {}
  public void SloopView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {}
~~~~

* 第四种有四个参数的构造函数出现在API21以上,暂不考虑.


* 有三个参数的构造函数中第三个参数是默认的Style，这里的默认的Style是指它在当前Application或Activity所用的Theme中的默认Style，且只有在明确调用的时候才会生效( 即使在Veiw的布局文件中使用了style这个属性,三个参数的构造函数也不会调用,必须在代码中明确调用三个参数的构造方法,才会被调用).
* 有两个参数的构造参数在布局文件设置宽高的时候就已经被调用.

### 3.测量View的大小OnMeasure()

~~~~JAVA
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthsize = MeasureSpec.getSize(widthMeasureSpec);      //取出宽度的确切数值
        int widthmode = MeasureSpec.getMode(widthMeasureSpec);      //取出宽度的测量模式

        int heightsize = MeasureSpec.getSize(heightMeasureSpec);    //取出高度的确切数值
        int heightmode = MeasureSpec.getMode(heightMeasureSpec);    //取出高度的测量模式
    }
~~~~

如果对View的宽高进行修改了，不要调用super.onMeasure(widthMeasureSpec,heightMeasureSpec);要调用setMeasuredDimension(widthsize,heightsize); 这个函数。

### 4.确定View的大小OnSizeChanged()

~~~~JAVA
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
~~~~

可以看出，它又四个参数，分别为 宽度，高度，上一次宽度，上一次高度。

这个函数比较简单，我们只需关注 宽度(w), 高度(h) 即可，这两个参数就是View最终的大小。

### 5.确定字View的布局位置onLayout()

**它用于确定子View的位置，在自定义ViewGroup中会用到，他调用的是子View的layout函数。**

在自定义ViewGroup中,onLayout一般是循环取出子View,然后经过计算得出各个子View的坐标值,然后用以下函数设置子View位置:

~~~~JAVA
 child.layout(l, t, r, b);
~~~~

四个参数分别为:

|  名称  |        说明         |    对应的函数     |
| :--: | :---------------: | :----------: |
|  l   | View左侧距父View左侧的距离 |  getLeft();  |
|  t   | View顶部距父View顶部的距离 |  getTop();   |
|  r   | View右侧距父View顶部的距离 | getRight();  |
|  b   | View底部距父View顶部的距离 | getBottom(); |

如下图:

 ![2](C:\Users\C\Desktop\笔记\自定义view相关知识点总结\2.png)

### 6.画图onDraw()

 ![6](C:\Users\C\Desktop\笔记\自定义view相关知识点总结\6.png)