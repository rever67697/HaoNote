1. 当`package get`命令一直卡着没有执行完毕时,应该就是被墙了,需要设置代理:

   打开PowerShell输入:

   ~~~~java
   set PUB_HOSTED_URL=https://pub.flutter-io.cn
   set FLUTTER_STORAGE_BASE_URL=https://storage.flutter-io.cn
   ~~~~

   

2.如果要在点击事件中改变控件的属性,需要添加`setState`监听.

~~~~dart
bool _selectTwo = false;
new IconButton(
                  color: Colors.blue,
                  icon: _selectTwo ? new Icon(Icons.search, size: 30) : new 		                             Icon(Icons.ac_unit, size: 30),
                  onPressed: () {
                    _selectIndex = 1;
                    setState(() {
                      _selectOne = false;
                      _selectTwo = true;
                    });
                  }),
~~~~



3.资源文件,需要在`pubspec.yaml`中定义文件夹路径，再使用。

~~~~dart
flutter:
	assets:
    #注意:资源文件夹里边如果有文件夹,都需要加上,不加会加载不到资源
    - assets/images/
    - assets/images/banner/
~~~~

上述代码，由于image文件夹中还有一个banner文件夹,banner文件夹中也有资源文件,所以都需要指明.