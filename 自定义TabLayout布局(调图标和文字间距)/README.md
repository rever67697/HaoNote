## 自定义TabLayout布局(调图标和文字间距)

~~~~java
 private void initTabLayout() {
        mTablayout = (TabLayout) findViewById(R.id.tab_main);
        //绑定viewpager到TabLayout中
        mTablayout.setupWithViewPager(rootViewPager);
        //tablayout绑定标题和图标
        for (int i = 0; i < mTitle.length; i++) {
            mTablayout.getTabAt(i).setText(mTitle[i]).setIcon(pic[i]);
        }
        
        // 修改样式，主要是调近了图标与文字之间的距离
        mTablayout.getTabAt(0).setCustomView(getTabView("首页", R.drawable.tab_select_main));
        mTablayout.getTabAt(1).setCustomView(getTabView("分期付", R.drawable.tab_select_learn));
        mTablayout.getTabAt(2).setCustomView(getTabView("我的", R.drawable.tab_select_me));

     
    }


/**
     * 自定义tabLayout的item
     *
     * @param title
     * @param image_src
     * @return
     */
    public View getTabView(String title, int image_src) {
        View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_tab, null);
        TextView textView = (TextView) v.findViewById(R.id.textview);
        textView.setText(title);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageview);
        imageView.setImageResource(image_src);
        return v;
    }
~~~~

item_tab布局:

~~~~xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="match_parent"
              android:layout_height="wrap_content"
              android:gravity="center"
              android:orientation="vertical">

    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:gravity="center"
        android:orientation="vertical">

        <ImageView
            android:id="@+id/imageview"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"/>

        <TextView
            android:id="@+id/textview"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/gap_3"
            android:textColor="@drawable/tab_text_select"
            android:textSize="@dimen/text_size_10"
            />
    </LinearLayout>
</LinearLayout>

~~~~

