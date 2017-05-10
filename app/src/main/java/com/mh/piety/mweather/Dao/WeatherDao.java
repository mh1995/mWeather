package com.mh.piety.mweather.Dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mh.piety.mweather.Bean.LocalInfoBean;

import java.util.ArrayList;

public class WeatherDao {
    private SQLiteDatabase db;
    public WeatherDao(Context context){
        MySqliteOpenHelper openHelper = new MySqliteOpenHelper(context);
        db=openHelper.getReadableDatabase();
    }
    public ArrayList<LocalInfoBean> queryAll(){
        ArrayList<LocalInfoBean> list = new ArrayList<>();
        LocalInfoBean loc;
        Cursor cursor =db.rawQuery("select * from weather",null);
        while (cursor.moveToNext()){
            loc = new LocalInfoBean();
            loc.position=cursor.getString(1);
            loc.weather_info=cursor.getString(2);
            list.add(loc);
        }
        cursor.close();
        db.close();
        return list;
    }
    public LocalInfoBean query(String position){
        Cursor cursor=db.rawQuery("select * from weather where position=?",new String[]{position});
        if(cursor.moveToNext()){
            LocalInfoBean lb = new LocalInfoBean();
            lb.position=cursor.getString(1);
            lb.weather_info=cursor.getString(2);
            return lb;
        }
        cursor.close();
        db.close();
        return null;
    }
    public void add(String position,String weather_info){
        ContentValues values = new ContentValues();
        values.put("position",position);
        values.put("weather_info",weather_info);
        db.insert("weather",null,values);
        db.close();
    }
    public void update(String position,String weather_info){
        ContentValues values = new ContentValues();
        values.put("weather_info",weather_info);
        db.update("weather",values,"position=?",new String[]{position});
        db.close();
    }
    public void delete(String position){
        db.delete("weather","position=?",new String[]{position});
        db.close();
    }
}
