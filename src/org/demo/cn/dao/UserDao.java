package org.demo.cn.dao;

import org.demo.cn.db.DBHelper;
import org.demo.cn.vo.UserVo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDao {
	
	private Context  mContext;
	
	private DBHelper mDB;
	
	public UserDao(Context context){
		this.mContext = context;
		mDB = new DBHelper(context);
	}
	
	public void add(UserVo vo){
		
		SQLiteDatabase db = mDB.getWritableDatabase();
		try{
			if(db.isOpen()){
				db.beginTransaction();
				ContentValues values = new ContentValues();
				values.put("id", vo.getId());
				values.put("username", vo.getUsername());
				values.put("tel", vo.getTel());
				values.put("img", vo.getImg());
				values.put("email", vo.getEmail());
				values.put("loginTime", vo.getLoginTime());
				db.insert("t_user", null, values);
				
			}
			
		}catch(Exception e){
			
		}finally{
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
		}
	}
	
	public UserVo find(String username){
		SQLiteDatabase db = mDB.getReadableDatabase();
		UserVo vo = null ;
		String sql = "select id,username,tel,img,email,loginTime from t_user where username=? ";
		try{
			Cursor cursor = db.rawQuery("t_user", new String[]{username});
			while(cursor.moveToFirst()){
				vo.setId(cursor.getInt(0));
				vo.setUsername(cursor.getString(1));
				vo.setTel(cursor.getString(2));
				vo.setImg(cursor.getString(3));
				vo.setEmail(cursor.getString(4));
				vo.setLoginTime(cursor.getString(5));
			}
		}catch(Exception e){
			
		}finally{
			db.close();
		}
		return vo;
	}

}
