##json解析
本文用的请求框架以后再写,先说用此框架请求后的数据的解析.  

![avatar](/json.png)  
如图,要从最外层的json中拿到对象BackMessage,然后就可以拿到hotImg等数据.先说如何拿到BackMessage的数据.  
 从一个bean对象里拿到集合里的bean对象的数据,只需要用框架中的这种方法就可以了  
`List<HallWatchDetailsBean> BackMessage = XCJsonUtil.parseListBean(response, "BackMessage", HallWatchDetailsBean.class);`  
我们就可以拿到hotImg,moviewName等一系列的数据,但是拿不到里边的对象commentList的数据,所以,接下来重点分析如何拿到commentList中的数据.  

----------
代码如下:  
    `//拿到数据,转换为JSONObject类型
                    JSONObject jsonObject = new JSONObject(response);
                    //解析最外层的对象(对象里边是list集合,要用optJSONArray);jsonArray的值就是BackMessage里边所有的数值
                    JSONArray jsonArray = jsonObject.optJSONArray("BackMessage");
                    //for循环
                    for(int i = 0; i < jsonArray.length(); i++) {
                        //拿到BackMessage对象的集合中第i个的对象的数据
                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                        //拿第i个对象的集合中的数据
                        List<HallWatchDetailsBean> commentList = XCJsonUtil.parseListBean(jsonObject1, "commentList", HallWatchDetailsBean.class);
                    }`