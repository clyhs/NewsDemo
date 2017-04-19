package org.demo.cn.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	
	private static final String DB_NAME = "newsdemo.db";
	
	public DBHelper(Context context){
		super(context,DB_NAME,null,1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String userTableSql = 
				"create table if not exists " +
				"t_user(id integer ," +
				"username varchar(30)," +
				"tel varchar(30)," +
				"img varchar(200)," +
				"email varchar(50)," +
				"loginTime varchar(50))";
		db.execSQL(userTableSql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
