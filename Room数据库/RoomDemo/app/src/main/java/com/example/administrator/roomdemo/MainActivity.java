package com.example.administrator.roomdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DefaultSubscriber;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv;
    private TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        //开始存储数据

        //传统写法
        Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> emitter) throws Exception {
                //对数据库操作必须在子线程中
                User user = new User();
                user.setName("name1");
                user.setAge(18);
                UserDatabase
                        .getInstance(MainActivity.this)
                        .getUserDao()
                        .insert(user);
                emitter.onNext(user);
                Log.v("hao", "MainActivity subscribe()");
            }
        }).subscribeOn(Schedulers.io()) //上边的被观察者在子线程执行操作
                .observeOn(AndroidSchedulers.mainThread()) //下边的观察者在主线程中执行操作
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(User user) {
                        //这在主线程中操作
                        Toast.makeText(MainActivity.this, "存储完成了", Toast.LENGTH_SHORT).show();
                        Log.v("hao", "MainActivity onNext()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.v("hao", "MainActivity onError(): " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.v("hao", "MainActivity onComplete()");
                    }
                });

    }

    private void initView() {
        tv = (TextView) findViewById(R.id.tv);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv.setOnClickListener(this);
        tv1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv:
                //传统写法
                Observable.create(new ObservableOnSubscribe<List<User>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<User>> emitter) throws Exception {
                        //对数据库操作必须在子线程中
                        List<User> allUsers = UserDatabase.getInstance(MainActivity.this)
                                .getUserDao().getAllUsers();
                        //
                        emitter.onNext(allUsers);
                    }
                }).subscribeOn(Schedulers.io()) //上边的被观察者在子线程执行操作
                        .observeOn(AndroidSchedulers.mainThread()) //下边的观察者在主线程中执行操作
                        .subscribe(new Observer<List<User>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(List<User> users) {
                                tv.setText(users.get(0).getName());
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                break;
            case R.id.tv1:
                //rxjava和room结合的最简便写法
                UserDatabase.getInstance(MainActivity.this).getUserDao().getAllUsersTwo()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultSubscriber<List<User>>() {
                            @Override
                            public void onNext(List<User> users) {
                                tv1.setText(users.get(0).getAge()+"");
                            }

                            @Override
                            public void onError(Throwable t) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });

                break;
        }
    }
}
