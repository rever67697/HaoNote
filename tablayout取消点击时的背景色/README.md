# tablayout取消点击时的背景色

~~~~java
tabLayout.setTabRippleColor(ColorStateList.valueOf(getContext().getResources().getColor(R.color.white)));
~~~~

注意:这是`targetSdkVersion =28`时的,28以下调不到这个方法. `targetSdkVersion`在28以下用这个方法:

~~~~xml
app:tabBackground="@android:color/transparent"
~~~~

