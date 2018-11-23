# edittext加上输入法中的搜索键实现搜索功能

首先，布局里在`EditText`中添加属性：

~~~~java
android:imeOptions="actionSearch"
android:singleLine="true"
~~~~

然后代码添加监听：

~~~~java
et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 当按了搜索之后的操做，如关闭软键盘 				             
                    return true;
                }

                return false;
            }
        });
~~~~

