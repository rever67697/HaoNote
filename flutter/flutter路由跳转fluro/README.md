# flutter路由跳转fluro

[TOC]

## 添加依赖

~~~~dart
#路由跳转
  fluro: ^1.5.0
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
   
   注意：路由跳转都是进栈操作，原有页面没有销毁。如果想跳转时同时销毁页面，可用原生的路由跳转方法：
   
   ~~~~java
   Navigator.of(context).pushAndRemoveUntil(
               MaterialPageRoute(builder: (context) => HomePage()),
                   (route) => route == null);
   ~~~~
   
   ## 路由执行startActivityForResult相似操作
   
   在跳转后边加`then`操作，`then`未跳转的页面销毁之后，回调执行的方法。
   
   ~~~~java
   Application.router.navigateTo(context,
                 '${Routes.web}?title=${Uri.encodeComponent(itemTitle)}&url=${Uri
                     .encodeComponent(itemUrl)}',
                 transition: TransitionType.fadeIn).then((result){
                   if(result == "key"){
                     //执行func路由,func路由为弹出弹窗
                     Application.router.navigateTo(context, "/demo/func?message=$result");
                   }
             });
   ~~~~
   
   
   
   在跳转到的web页面要销毁时，执行`Navigator.pop(context, 'key');`方法.然后就会调用上边代码中的`then`方法了。
   
   `func`路由的`handler`如下：
   
   ~~~~java
   var demoFunctionHandler = new Handler(
       type: HandlerType.function,
       handlerFunc: (BuildContext context, Map<String, List<String>> params) {
         String message = params["message"]?.first;
         showDialog(
           context: context,
           builder: (context) {
             return new AlertDialog(
               title: new Text(
                 "Hey Hey!",
                 style: new TextStyle(
                   color: const Color(0xFF00D6F7),
                   fontFamily: "Lazer84",
                   fontSize: 22.0,
                 ),
               ),
               content: new Text("$message"),
               actions: <Widget>[
                 new Padding(
                   padding: new EdgeInsets.only(bottom: 8.0, right: 8.0),
                   child: new FlatButton(
                     onPressed: () {
                       Navigator.of(context).pop(true);
                     },
                     child: new Text("OK"),
                   ),
                 ),
               ],
             );
           },
         );
       });
   ~~~~
   
   

## 封装Navigator工具类使用

封装工具类`NavigatorUtil`

~~~~dart
import 'package:fluro/fluro.dart';
import 'package:flutter/material.dart';
import 'application.dart';
import 'routers.dart';

/// fluro的路由跳转工具类
class NavigatorUtils {
  
  ///replace为false表示跳转页面之后不销毁该页面
  static push(BuildContext context, String path,
      {bool replace = false, bool clearStack = false}) {
    Application.router.navigateTo(context, path, replace: replace, clearStack: clearStack, transition: TransitionType.native);
  }

  static pushResult(BuildContext context, String path, Function(Object) function,
      {bool replace = false, bool clearStack = false}) {
    Application.router.navigateTo(context, path, replace: replace, clearStack: clearStack, transition: TransitionType.native).then((result){
      // 页面返回result为null
      if (result == null){
        return;
      }
      function(result);
    }).catchError((error) {
      print("$error");
    });
  }

  /// 返回
  static void goBack(BuildContext context) {
    Navigator.pop(context);
  }

  /// 带参数返回
  static void goBackWithParams(BuildContext context, result) {
    Navigator.pop(context, result);
  }
  
  /// 跳到WebView页
  static goWebViewPage(BuildContext context, String title, String url){
    //fluro 不支持传中文,需转换
    push(context, '${Routes.web}?title=${Uri.encodeComponent(title)}&url=${Uri.encodeComponent(url)}');
  }
}
~~~~

使用则为:

~~~~dart
NavigatorUtils.push(context, Routes.home, replace: true);
~~~~

