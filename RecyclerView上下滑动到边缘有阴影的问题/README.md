# RecyclerView上下滑动到边缘有阴影的问题

`Recycler`添加属性：

~~~~java
android:overScrollMode="never"
~~~~

或者代码设置：

~~~~java
recyclerview.setOverScrollMode(View.OVER_SCROLL_NEVER);
~~~~

