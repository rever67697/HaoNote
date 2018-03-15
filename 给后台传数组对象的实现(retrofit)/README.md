## 给后台传数组对象的实现(retrofit)

给后台传数据的方式:

1.Urlencode

2.json



当我们不设置传的类型时,其实就是以表单的形式传输(就是Urlencode):

~~~~java
 @Headers("Accept:application/json")
 @GET(Constant.UrlOrigin.get_fangke_info)
 Observable<FangkeBean> getFangkeInfoRx(@Header("Authorization") String tokenAndtype,          @QueryMap Map<String, Object> Map );
~~~~

可以看出@QueryMap就是以表单的形式传输,如下:

~~~~
address_id=32&goods_data%5B%5D=11
~~~~

是以键值对的形式传给后台的.



但是当后台需要的请求参数为:

~~~~json
{
    "order_goods_id" : 1,//订单商品id
    "content" : "内容",
    "imgs" : [
        "评论图片url","url"
    ],
    "is_anonymous" : 0, //是否匿名0否，1是
    "header" : "头像",
    "nickname" : "昵称",
}

~~~~

上边代码imgs是需要传数组对象的,以表单形式无法传输,就需要以第二种json方式传输.

需要做以下修改:

~~~~java
@Headers({
            "Accept:application/json",
            "Content-Type: application/json"
    })
    @POST(Constant.UrlOrigin.get_add_comment_info)
    Observable<EmptyBean> getAddCommentRx(@Body RequestBody route);
~~~~

首先,我们需要添加请求头` "Content-Type: application/json"`,然后使用`@Body RequestBody route`添加请求体.

然后我们需要在presenter中,<u>拿到整个转换为json字符串的请求参数</u>,然后转换为请求体:

~~~~java
RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), canshu);
~~~~

然后在调用

~~~~java
dataManager.getAddCommentInfo(requestBody)
~~~~

就可以了.



现在说说<u>拿到整个转换为json字符串的请求参</u>,就是

~~~~java
private void requestComment() {

        Map<String, Object> map = new HashMap<>();
  		//mUploadUrls是一个list
        map.put("imgs", mUploadUrls);
        map.put("header", header);
        map.put("nickname", nickname);

        Gson gson = new Gson();
        String json=gson.toJson(map);
        mPresenter.getAddCommentInfo(getTokenAndTokenType(),json);
    }
~~~~

直接把整个map转换为json字符串,在presenter中转换为请求体就可以了

