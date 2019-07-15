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

