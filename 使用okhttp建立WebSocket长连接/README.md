# 使用okhttp建立WebSocket长连接

[TOC]

## 先引入依赖

除了okhttp的依赖,还需引入:

~~~~java
implementation 'com.squareup.okhttp3:mockwebserver:3.8.0'
~~~~

## 建立长连接

~~~~java
private WebSocket mSocket;

    private void connetctWebSocket() {

        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(3, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(3, TimeUnit.SECONDS)//设置连接超时时间
                .build();
		//链接地址,一般以ws://开头或者wss://
        Request request = new Request.Builder().url("ws://192.168.1.9:8886/websocket/weather").build();
        EchoWebSocketListener socketListener = new EchoWebSocketListener();
        mOkHttpClient.newWebSocket(request, socketListener);
        mOkHttpClient.dispatcher().executorService().shutdown();

    }



    private final class EchoWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            mSocket = webSocket;
            //连接成功后，发送登录信息
            String message = "";
            mSocket.send(message);
            output("连接成功！");

        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
            output("receive bytes:" + bytes.hex());
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            output("服务器端发送来的信息：" + text);

            // 这里自己用于测试断开连接：就直接在接收到服务器发送的消息后，然后断开连接，然后清除 handler，
            //具体可以根据自己实际情况断开连接，比如点击返回键页面关闭时，执行下边逻辑
            //            if (!TextUtils.isEmpty(text)){
            //                if (mSocket  != null) {
            //                    mSocket .close(1000, null);
            //                }
            //                if (mHandler != null){
            //                    mHandler.removeCallbacksAndMessages(null);
            //                    mHandler = null ;
            //                }
            //            }
            /*//收到服务器端发送来的信息后，每隔2秒发送一次心跳包
            final String message = sendHeart();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mSocket.send(message);
                }
            },2000);*/

        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            output("closed:" + reason);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            output("closing:" + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            output("failure:" + t.getMessage());
        }
    }


    private void output(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("hao", "text: " + text);
            }
        });
    }

~~~~

