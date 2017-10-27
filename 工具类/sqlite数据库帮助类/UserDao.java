package com.m520it.jdmall.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.m520it.jdmall.bean.UserBean;

public class UserDao {

	private DbHelper mHelper;

	public UserDao(Context context) {
		mHelper = new DbHelper(context);
	}

	/**
	 * 	保存用户的账号密码
	 */
	public void saveUser(String name, String pwd) {
		deleteUsers();
		SQLiteDatabase db = mHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DbConst.COLUMN_USERNAME, name);
		values.put(DbConst.COLUMN_PASSWORD, pwd);
		db.insert(DbConst.USER_TABLE, null, values);
	}

	/**
	 * 	获取当前登录的用户信息
	 */
	public UserBean getRecentLoginUser() {
		SQLiteDatabase db = mHelper.getReadableDatabase();
		Cursor cursor = db.query(DbConst.USER_TABLE, new String[] {
				DbConst.COLUMN_USERNAME, DbConst.COLUMN_PASSWORD }, null, null,
				null, null, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			String name = cursor.getString(cursor
					.getColumnIndex(DbConst.COLUMN_USERNAME));
			String pwd = cursor.getString(cursor
					.getColumnIndex(DbConst.COLUMN_PASSWORD));
			return new UserBean(name, pwd);
		}
		return null;
	}

	/**
	 * 删除所有用户信息
	 */
	public boolean deleteUsers() {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			Cursor cursor = db.query(DbConst.USER_TABLE,
					new String[] { DbConst.COLUMN_USERNAME }, null, null, null,
					null, null);
			while (cursor.moveToNext()) {
				String username = cursor.getString(cursor
						.getColumnIndex(DbConst.COLUMN_USERNAME));
				int rowId = db.delete(DbConst.USER_TABLE,
						DbConst.COLUMN_USERNAME + "=?",
						new String[] { username });
				if (rowId <= 0) {
					return false;
				}
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		return true;
	}

}
