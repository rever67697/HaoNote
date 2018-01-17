# NestScrollView或者Recyclerview滑动到某位置,显示控件的效果

[TOC]

## NestScrollview滑动到某位置时的监听

~~~~java
 mNestScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        int width, height;
                        Point p = new Point();
                        getActivity().getWindowManager().getDefaultDisplay().getSize(p);
                        width = p.x;
                        height = p.y;

                        Rect rect = new Rect(0, 0, width, height);
						//设置到某控件的位置时
                        if (mTextView.getLocalVisibleRect(rect)) {
                            //滑动的位置,当mTextView可见时
                        } else {
                            //当滑动到mTextView不可见时
                        }
                    }
                });
~~~~

## recyclerview滑动到某位置的监听

~~~~java
mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                GridLayoutManager manger = (GridLayoutManager) recyclerView.getLayoutManager();
                int p = manger.findFirstVisibleItemPosition();
                if (p == 0) {
                    
                } else {
                    
                }
            }
        });
~~~~

可根据p的位置判断,当recyclerview有头布局时,整个头布局的位置p都为0,等到第一个item时为1.

注意:Recyclerview嵌套NestScrollview时会导致封装到Recyclerview内部的上拉加载失效.

解决方法:1.不用NestScrollview,而是在Recyclerview加上头布局和脚部局.

2.可以试试在整个布局,也就是NestScrollview外边包裹下拉刷新和上拉加载的布局.

