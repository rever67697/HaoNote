package com.example.administrator.rxcachedemo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.rxcachedemo.mvp.bean.ExpressInfo;
import com.example.administrator.rxcachedemo.mvp.presenter.ExpressPresenter;
import com.example.administrator.rxcachedemo.mvp.view.ExpressView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity implements ExpressView, View.OnClickListener {

    private TextView tv;
    private ProgressDialog progressDialog;
    private ExpressPresenter expressPresenter;
    private TextView tv_json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //必须要初始化presenter
        expressPresenter = new ExpressPresenter(MainActivity.this, this, this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在获取快递信息...");
        initView();
    }

    private void initView() {
        tv = (TextView) findViewById(R.id.tv);
        tv.setOnClickListener(this);
        tv_json = (TextView) findViewById(R.id.tv_json);
        tv_json.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv:
                //传递请求参数,开始网络请求
                tv_json.setText("");
                Map<String, String> map = new HashMap<>();
                map.put("type", "yuantong");
                map.put("postid", "11111111111");
                expressPresenter.getExpressInfo(map, true);
                break;
            default:
                break;
        }
    }

    /**********************实现ExpressView需要重写的方法**********************/

    /**
     * 拿到数据,做数据绑定操作
     *
     * @param expressInfo 快递信息
     */
    @Override
    public void updateView(ExpressInfo expressInfo) {
        tv_json.setText(expressInfo.toString());
    }

    /**
     * 数据请求时的
     * 显示加载框
     */
    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    /**
     * 数据加载完成的隐藏加载框
     */
    @Override
    public void hideProgressDialog() {
        progressDialog.hide();
    }

    /**
     * 显示错误信息
     *
     * @param msg 错误信息
     */
    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
