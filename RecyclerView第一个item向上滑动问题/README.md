方法一:

`NestScrollView`添加属性:

~~~~java
android:focusableInTouchMode="true"
~~~~

recyclerview添加属性:

~~~~java
 android:descendantFocusability="blocksDescendants"
~~~~



方法二:

直接设置`RecyclerView`:

~~~java
mRecyclerView.setFocusableInTouchMode(false)
~~~

