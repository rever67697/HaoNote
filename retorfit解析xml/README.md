# retorfit解析xml

有一种简单方法，直接设置retrofit的ConverterFactory为`Simple XML `

但是，我想直接按字符串接收，然后将xml转换为json.

首先，在`RetrofitHelper`中随便添加一个`ConverterFactory`

~~~~java
public class RetrofitHelper {

    private static RetrofitHelper retrofitHelper;
    private RetrofitService retrofitService;

    public static RetrofitHelper getInstance() {
        return retrofitHelper == null ? retrofitHelper = new RetrofitHelper() : retrofitHelper;
    }

    private RetrofitHelper() {
        // 初始化Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create()) // json解析
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 支持RxJava
                .client(RetrofitUtils.getOkHttpClient()) //打印请求参数
                .build();
        retrofitService = retrofit.create(RetrofitService.class);
    }

    public RetrofitService getRetrofitService() {
        return retrofitService;
    }
}
~~~~

然后使用我们的网络请求框架，以`ResponseBody`接收返回的数据。

~~~~java
@Override
    public void updateView(ResponseBody expressInfo) {
        try {
            String string = expressInfo.string();
            Log.v("hao", "xml: " + string);
            Log.v("hao", "json: " + XmlParser.xml2json(string));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
~~~~

`XmlParser`代码：

~~~~java
public class XmlParser {
    public static String xml2json(String response) {
        JSONObject jsonObj = null;
        try {
            jsonObj = XML.toJSONObject(response);
        } catch (JSONException e) {
            Log.e("JSON exception", e.getMessage());
            e.printStackTrace();
        }
        return jsonObj.toString();
    }
}
~~~~

这样就拿到了json数据了。

注意：使用xml转json工具类需要导入jar包：`java-json.jar`

