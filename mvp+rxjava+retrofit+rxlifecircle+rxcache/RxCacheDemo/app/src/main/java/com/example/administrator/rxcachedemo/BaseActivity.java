package com.example.administrator.rxcachedemo;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.TextView;

import com.example.administrator.rxcachedemo.application.BaseApplication;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃  神兽保佑
 * 　　　　┃　　　┃  代码无bug
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 *
 * @author hao
 * @date 2017/10/30
 * @description 基类activity
 */

public class BaseActivity extends RxAppCompatActivity {

    /**
     * 是否显示toolbar
     */
    private boolean mIsShowTitleBar;
    /**
     * 是否显示返回键
     */
    private boolean mIsShowBack;
    /**
     * 标题
     */
    private TextView mTitle;
    /**
     * toolbar
     */
    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        BaseApplication.getInstance().addActivity(this);
        //        sp = SharedPreferencesUtil.getInstance(this);
//        if (mIsShowTitleBar) {
//            initToolBar(mIsShowBack);
//        }
    }

    /**
     * 是否显示toolbar(如果要显示toolbar一定要调用)
     *
     * @param showTitleBar
     */
    public void isShowToolBar(boolean showTitleBar,boolean showBack) {
        mIsShowTitleBar = showTitleBar;
        mIsShowBack = showBack;
    }

//    /**
//     * 设置标题名
//     *
//     * @param title
//     */
//    private void showTitle(String title) {
//        if (mTitle == null) {
//            mTitle = (TextView) findViewById(R.id.tv_title);
//        }
//        if (title != null && !"".equals(title)) {
//            mTitle.setText(title);
//            mTitle.setVisibility(View.VISIBLE);
//        } else {
//            mTitle.setVisibility(View.GONE);
//        }
//    }


//    /**
//     * 初始化toolbar
//     *
//     * @param isShowBack 是否显示返回键
//     */
//    private void initToolBar(boolean isShowBack) {
//        if (mToolBar == null) {
//            mToolBar = (Toolbar) findViewById(R.id.tool_bar);
//        }
//
//        //把ToolBar设置上去
//        setSupportActionBar(mToolBar);
//        // 显示应用的Logo
//        getSupportActionBar().setDisplayShowTitleEnabled(false);//是否显示title(为true会显示项目的label名称,下边的setTitle方法不生效;为false下边的setTitle方法生效)
//        getSupportActionBar().setDisplayUseLogoEnabled(false);//是否显示logo
//
//        if (isShowBack) {
//            // 显示导航按钮(左侧侧滑抽屉按钮,现在为返回键)
//            mToolBar.setNavigationIcon(R.mipmap.ic_btn_back);
//            mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    finish();
//                }
//            });
//        }
//    }


    /**
     * 显示无数据图
     *
     * @param
     * @param isShow
     */
    public void showNoDataView(boolean isShow) {

    }

    /**
     * 重写返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 判断用户是否点击了返回键
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
