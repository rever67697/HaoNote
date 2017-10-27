package com.m520it.jdmall.db;

public class DbConst {

	public static final String DB_NAME = "jdmall.db";

	public static final int DB_VERSION = 1;

	public static final String USER_TABLE = "user";
	public static final String COLUMN_USERNAME = "name";
	public static final String COLUMN_PASSWORD = "pwd";
	public static final String CREATE_USER_TABLE_SQL = "create table "
			+ USER_TABLE + " (_id integer primary key autoincrement,"
			+ COLUMN_USERNAME + " varchar(20)," + COLUMN_PASSWORD
			+ " varchar(40));";
}
