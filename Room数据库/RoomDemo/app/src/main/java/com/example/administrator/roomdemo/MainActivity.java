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

public class MainActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        //        new Thread() {
        //            public void run() {
        //                User user = new User();
        //                user.setName("name1");
        //                user.setAge(18);
        //                UserDatabase
        //                        .getInstance(MainActivity.this)
        //                        .getUserDao()
        //                        .insert(user);
        //            }
        //        }.start();

        Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> emitter) throws Exception {
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
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(User user) {
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
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        List<User> allUsers = UserDatabase.getInstance(MainActivity.this)
                                .getUserDao().getAllUsers();
                        Log.v("hao", "MainActivity run(): " + allUsers.get(0).toString());
                    }
                }.start();
            }
        });
    }
}
