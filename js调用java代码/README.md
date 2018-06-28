# js调用java代码

[TOC]

设置webview

~~~~java
webView.getSettings().setJavaScriptEnabled(true);
webView.addJavascriptInterface(this, "shopTest");
webView.setWebViewClient(new webViewViewClient());
~~~~

写方法,注意方法需要加上`@JavascriptInterface`注解

~~~~java
 	/**
     * 注意:js调用java方法时会在一个私有的后台进程中,所以需要runOnUiThread
     */
    @JavascriptInterface
    public void webFinish() {
        	finish();
        }
    }
~~~~

然后在js中 `shopTest.webFinish()`就可以了.



注意:如果在js调用的java方法中修改ui,需要要`runOnUiThread`,因为js调用的java方法,都执行在一个私有的后台进程中.