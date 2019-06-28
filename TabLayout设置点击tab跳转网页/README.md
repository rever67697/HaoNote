# TabLayout设置点击tab跳转网页

![1](C:\Users\dell\Desktop\HaoNote\TabLayout设置点击tab跳转网页\1.gif)

实现效果：点击到某个tab，跳转到网页，网页返回的时候，tab选中的为上次选中的那个tab。

初步思想：通过监听实现`Tablayout.setOnTabSelectedListener`，通过这个监听，拿到选中的tab的位置，对应的位置跳转网页，效果没错，但是网页返回来之后，还是选中所在的tab,而不是前一个tab。

~~~~java
mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
	@Override
     public void onTabSelected(TabLayout.Tab tab) {
		if (tab.getPosition() == 1) {
			startActivity(new Intent(MainTwoActivity.this, WebActivity.class));
			mViewpager.setCurrentItem(0);
		}
	}
}
~~~~

上述代码滑动到第一个tab的位置是不生效的，所以不能这样写。



**解决方案：**

取消TabLayout和ViewPager的绑定，由我们手动操作，这样我们就能控制TabLayout的跳转操作了。

首先，注释这一行代码：

~~~~java
//        mTablayout.setupWithViewPager(rootViewPager);
~~~~

然后，初始化TabLayout。

~~~~java
 for (int i = 0; i < mTitle.length; i++) {
            //未设置setupWithViewPager，需要自己初始化
     TabLayout.Tab tab = mTablayout.newTab();
     if (tab != null) {
         //自定义tab布局
         if (i == 0) {
             tab.setCustomView(getTabView("首页", R.drawable.tab_select_main));
         }else if (i == 1) {
             tab.setCustomView(getTabView("发现", R.drawable.tab_select_more));
         }else if (i == 2) {
             tab.setCustomView(getTabView("钱包", R.drawable.tab_select_learn));
         }else if (i == 3) {
             tab.setCustomView(getTabView("我的", R.drawable.tab_select_me));
         }

         if (tab.getCustomView() != null) {
            View parent = (View) tab.getCustomView().getParent();
            parent.setTag(i);
         }
      }
      mTablayout.addTab(tab,i);
  }
~~~~

上述代码自定义tab的布局，并添加到TabLayout中。`getView()`方法为自定义布局方法。

~~~~java
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



最后，我们在监听中手动控制tab的切换就好了。

~~~~java
private int mSelectPos;
mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //判断tab.getPosition() != mSelectPos是因为，点击第二个tab跳转网页之后，再点击现在显示的tab,会变成未选中
                if (tab.getPosition() != mSelectPos) {
                    //把直接记录的tab选中的位置设置为未选中（不写，会出现同时有两个tab选中的情况）
                    View preView1 = mTablayout.getTabAt(mSelectPos).getCustomView();
                    ImageView preImageView1 = (ImageView) preView1.findViewById(R.id.imageview);
                    TextView preTextView1 = (TextView) preView1.findViewById(R.id.textview);
                    preImageView1.setSelected(false);
                    preTextView1.setSelected(false);
                }

                if (tab.getPosition() == 0) {
                    mViewPager.setCurrentItem(0,true);
                    mSelectPos = 0;
                }

                if (tab.getPosition() == 1) {
                    startActivity(new Intent(MainTwoActivity.this, WebActivity.class));
                    View customView = tab.getCustomView();
                    //设置当前tab为未选中
                    ImageView imageView = (ImageView) customView.findViewById(R.id.imageview);
                    TextView textView = (TextView) customView.findViewById(R.id.textview);
                    imageView.setSelected(false);
                    textView.setSelected(false);
                    //设置前一个tab为选中
                    View preView = mTablayout.getTabAt(mSelectPos).getCustomView();
                    ImageView preImageView = (ImageView) preView.findViewById(R.id.imageview);
                    TextView preTextView = (TextView) preView.findViewById(R.id.textview);
                    preImageView.setSelected(true);
                    preTextView.setSelected(true);
                }

                if (tab.getPosition() == 2) {
                    mViewPager.setCurrentItem(2,true);
                    mSelectPos = 2;
                }
                if (tab.getPosition() == 3) {
                    mViewPager.setCurrentItem(3,true);
                    mSelectPos = 3;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    //再次点击时的跳转操作
                    startActivity(new Intent(MainTwoActivity.this, WebActivity.class));
                }
            }
        });
~~~~

