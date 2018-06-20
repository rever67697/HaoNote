package com.example.administrator.roomdemo;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;

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
 * @date 2018/6/14 0014
 * @description
 */
@Dao
public interface  UserDao {

    /**
     * 传统方法
     * @return
     */
    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    /**
     * rxjava结合room最简便写法(可以用Flowable,Single,Maybe,但是只有Flowable,在调用以后,数据库数据更新了,会自动更新前台数据)
     *
     * @return
     */
    @Query("SELECT * FROM user")
    Flowable<List<User>> getAllUsersTwo();

    @Insert
    void insert(User... users);

    @Update
    void update(User... users);

    @Delete
    void delete(User... users);

    /**
     * 查询年龄
     * @param age
     * @return
     */
    @Query("SELECT * FROM user WHERE age=:age")
    List<User> getUsersByAge(int age);
}
