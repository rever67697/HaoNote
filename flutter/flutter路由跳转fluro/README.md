# flutter路由跳转fluro

[TOC]

## 添加依赖

~~~~dart
#路由跳转
  fluro: ^1.4.0
~~~~

## 新建相关文件

1. 新建`application.dart`文件,用于跳转时获取`router`

   ~~~~java
   import 'package:fluro/fluro.dart';
   
   class Application{
     static Router router;
   }
   ~~~~

   

2. 新建`route_handlers.dart`文件,用于初始化跳转到各个页面的handle,并获取到上个页面传递过来的值,然后在初始化要跳转到的页面.

   ~~~~java
   import 'package:fluro/fluro.dart';
   import 'package:flutter/material.dart';
   import 'package:flutter_my_demo/page/home.dart';
   import 'package:flutter_my_demo/page/webview.dart';
   
   var homeHandler = new Handler(
       handlerFunc: (BuildContext context, Map<String, List<String>> params) {
     return new HomePage();
   });
   
   var webViewHandler = new Handler(
       handlerFunc: (BuildContext context, Map<String, List<String>> params) {
     //获取路由跳转传来的参数
     String url = params["url"].first;
     String title = params["title"].first;
     return new WebViewPage(url, title);
   });
   ~~~~

   

3. 新建`routes.dart`文件,用于绑定路由地址和对应的`handler`.

   ~~~~java
   import 'package:flutter/material.dart';
   import 'package:fluro/fluro.dart';
   import 'package:flutter_my_demo/route/route_handlers.dart';
   
   class Routes {
     static String root = "/";
     static String home = "/home";
     static String web = "/web";
   
     static void configureRoutes(Router router){
       router.notFoundHandler = new Handler(
           handlerFunc: (BuildContext context,Map<String,List<String>> params){
         print("route is not find !");
       });
   
       router.define(home, handler: homeHandler);
       router.define(web, handler: webViewHandler);
     }
   }
   ~~~~

   

## 初始化配置

~~~~java
import 'package:fluro/fluro.dart';
import 'package:flutter/material.dart';
import 'package:flutter_my_demo/route/application.dart';
import 'package:flutter_my_demo/route/routes.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {

  MyApp(){
    //初始化路由
    final router = new Router();
    Routes.configureRoutes(router);
    Application.router = router;
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MySplashPage(),
      //初始化路由
      onGenerateRoute: Application.router.generator,
    );
  }
}
~~~~

注意上边代码的两个初始化路由的步骤.

## 使用路由

1. 无参数跳转

   ~~~~java
   Application.router.navigateTo(context, "/home",transition: TransitionType.fadeIn);
   ~~~~

2. 有参数跳转

   ~~~~java
   String route = '/web?url=${Uri.encodeComponent(itemUrl)}&title=${Uri.encodeComponent(itemTitle)}';
   Application.router.navigateTo(context, route,transition: TransitionType.fadeIn);
   ~~~~

   或者

   ~~~~java
   Application.router.navigateTo(context, '${Routes.web}?title=${Uri.encodeComponent(itemTitle)}&url=${Uri.encodeComponent(itemUrl)}',transition: TransitionType.fadeIn);
   ~~~~

   注意:参数的值需要使用`Uri.encodeComponent()`方法调用一下,要不会报错.