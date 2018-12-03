# TabLayout选中时的动画效果

[TOC]

先上效果图：

![1](C:\Users\Administrator\Desktop\HaoNote\TabLayout选中时的动画效果\1.gif)

首先，需要自定义tablayout的布局：

~~~~java
/**
     * 自定义tabLayout的item
     *
     * @param title
     * @param image_src
     * @return
     */
    fun getTabView(title: String, image_src: Int): View {
        val v = LayoutInflater.from(applicationContext).inflate(R.layout.item_tab, null)
        val textView = v.findViewById(R.id.textview) as TextView
        textView.text = title
        val imageView = v.findViewById(R.id.imageview) as ImageView
        imageView.setImageResource(image_src)
        return v
    }
~~~~

`item_tab.xml`文件：

~~~~java
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              xmlns:app="http://schemas.android.com/apk/res-auto"
              android:layout_width="match_parent"
              android:layout_height="wrap_content"
              android:gravity="center"
              android:orientation="vertical"
              >

    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="50dp"
        android:layout_gravity="center"
        android:gravity="center"
        android:orientation="vertical">

        <pl.droidsonroids.gif.GifImageView
            android:id="@+id/imageview"
            android:layout_width="30dp"
            android:layout_height="30dp"
            />
        <TextView
            android:id="@+id/textview"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="3dp"
            android:textColor="@drawable/tab_text_select"
            android:textSize="10sp"
            />
    </LinearLayout>
</LinearLayout>

~~~~

布局中用了一个第三方的gif库：

~~~~
implementation 'pl.droidsonroids.gif:android-gif-drawable:1.2.15'
~~~~

然后设置上去，虽然设置的selector没用。

~~~~java
tab_main.getTabAt(0)!!.customView = getTabView("首页", R.drawable.tab_select_main)
        tab_main.getTabAt(1)!!.customView = getTabView("订单", R.drawable.tab_select_learn)
        tab_main.getTabAt(2)!!.customView = getTabView("我的", R.drawable.tab_select_me)
~~~~



最好，设置监听就好了：

~~~~java
tab_main!!.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val view = tab!!.customView
                val imageView = view!!.findViewById(R.id.imageview) as GifImageView
                //拿到tablayout的item,然后执行动画
                if (tab.position == 0) {
                    val gifDrawable = GifDrawable(resources, R.mipmap.ic_refresh_head)
                    gifDrawable.loopCount = 1
                    imageView.setImageDrawable(gifDrawable)
                    gifDrawable.start()
                } else if (tab.position == 1) {
                    val gifDrawable = GifDrawable(resources, R.mipmap.ic_refresh_head)
                    gifDrawable.loopCount = 1
                    imageView.setImageDrawable(gifDrawable)
                    gifDrawable.start()
                } else if (tab.position == 2) {
                    val gifDrawable = GifDrawable(resources, R.mipmap.ic_main_select)
                    gifDrawable.loopCount = 1
                    imageView.setImageDrawable(gifDrawable)
                    gifDrawable.start()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                //等到未选中状态，再恢复为以前的图片
                val view = tab!!.customView
                val imageView = view!!.findViewById(R.id.imageview) as GifImageView
                if (tab.position == 0) {
                    imageView.imageResource = R.drawable.tab_select_main
                } else if (tab.position == 1) {
                    imageView.imageResource = R.drawable.tab_select_learn
                } else if (tab.position == 2) {
                    imageView.imageResource = R.drawable.tab_select_me
                }
            }
        })
~~~~

