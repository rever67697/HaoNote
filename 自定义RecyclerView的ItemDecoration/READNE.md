## 自定义RecyclerView的ItemDecoration

需求:想要设置两个item间距一样,一行两个item的情况.

1.设置item的宽度为`match_parent`,当设置girdlayoutmanger的时候,会把item宽度根据屏幕宽度减去各自的间距之后,平均分的.

2.写ItemDecoration类.

~~~~java
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
  	//设置左边,中间,右边的间距
    private int spacing;

    public GridSpacingItemDecoration(int spanCount, int spacing) {
        this.spanCount = spanCount;
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
      	//拿到每个item的位置
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (parent.getChildAdapterPosition(view) == 0) {
          	//当位置为0,也就是是为头布局的时候不设置间距都为定好的传入的间距.
            return;
        }

        //spanCount定死是两个,使左边,中间,右边的间距
        if (column == 1) {
          	//因为添加了头布局,所以column == 1时为左边
            outRect.left = spacing;
            outRect.right = spacing / 2;
            outRect.top = spacing;
        } else {
           //因为添加了头布局,所以column == 0时为右边
            outRect.left = spacing / 2;
            outRect.right = spacing;
            outRect.top = spacing;
        }
    }
}

~~~~

以上代码是专门为一行两个item的时候写的,有具体需求可以具体修改.