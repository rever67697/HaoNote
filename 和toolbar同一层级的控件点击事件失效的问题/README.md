# 和toolbar同一层级的控件点击事件失效的问题

~~~~xml
<FrameLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <android.support.v7.widget.Toolbar
        android:id="@+id/tool_bar"
        android:layout_width="match_parent"
        android:layout_height="44dp"
        android:elevation="4dp"
        >

        <TextView
            android:id="@+id/tv_title"
            android:layout_width="230dp"
            android:layout_height="wrap_content"
            android:layout_gravity="center"
            android:singleLine="true"
            android:text=""
            android:textColor="#FF081E3C"
            android:textSize="17sp"/>
    	</android.support.v7.widget.Toolbar>

        <TextView
            android:id="@+id/tv_des"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="dianw"/>
    </FrameLayout>
~~~~

如上述布局代码，`toolbar`和id为`tv_des`的`textview`的控件在同一层级下时，`tv_des`的点击事件会失效，解决方案有两种：

1.把`toolbar`放到一个新建的xml文件中，然后用`include`标签引入，就可以了。

2.设置`tv_des`的事件穿透：

~~~~java
tool_bar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //R.id.tv_des是个ToolBar平级的view的id
                return findViewById(R.id.tv_des).dispatchTouchEvent(event);
            }
        });
~~~~

