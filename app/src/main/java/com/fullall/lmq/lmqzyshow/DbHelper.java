package com.fullall.lmq.lmqzyshow;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(Context context, String name, CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }

    //辅助类建立时运行该方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE  TABLE lmqzyshow (" +
                " id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL" +
                ",mc VARCHAR" +
                ",rq VARCHAR" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
