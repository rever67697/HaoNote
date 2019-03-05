# 解决Android 8.0及以上WebView回退失效

##前言

最新开发项目使用WebView的时候发现华为手机判断是否能回退（canGoBack）的时候失效了，无论打开多少层网页，点击返回按钮都会关闭Activity，一开始以为是华为手机的问题，然后用其他版本高一点的手机测试也是一样（8.0版本），查阅了官方文档才知道是sdk的问题。

##问题

Android 8.0开始WebView的shouldOverrideUrlLoading(WebView view, String url)返回值是false才会自动重定向，并且无需调用loadUrl，与8.0之前的效果刚好相反。

##解决办法

~~~~java
@Override

public boolean shouldOverrideUrlLoading(WebView view, String url) {
   //Android 8.0以下版本的需要返回true 并且需要loadUrl()
   if (Build.VERSION.SDK_INT < 26) {
        view.loadUrl(url);
        return true;
   }
   return false;
}

~~~~

