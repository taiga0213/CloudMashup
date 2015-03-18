package jp.taiga.cloudmashup;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 自動生成されたメソッド・スタブ
		String createSqlKey = "";
        createSqlKey += "create table keywords (";
        //テーブルの名前
        createSqlKey += "keyword text primary key";
        createSqlKey += ")";
        db.execSQL(createSqlKey);
        
        
		String createSqlIdia = "";
        createSqlIdia += "create table ideas (";
        //テーブルの名前
        createSqlIdia += "keywordA text,";
        createSqlIdia += "keywordB text,";
        createSqlIdia += "keywordC text,";
        createSqlIdia += "comment text,";
        createSqlIdia += "primary key(keywordA, keywordB,keywordC)";
        createSqlIdia += ")";
        db.execSQL(createSqlIdia);
        
		String createSqlLock = "";
        createSqlLock += "create table lock (";
        //テーブルの名前
        createSqlLock += "keyword text,";
        createSqlLock += "date text,";
        createSqlLock += "primary key(keyword)";
        createSqlLock += ")";
        db.execSQL(createSqlLock);
        
        String createSqlLock2 = "";
        createSqlLock2 += "create table lock2 (";
        //テーブルの名前
        createSqlLock2 += "keyword text,";
        createSqlLock2 += "date text,";
        createSqlLock2 += "primary key(keyword)";
        createSqlLock2 += ")";
        db.execSQL(createSqlLock2);
        
		db.execSQL("insert into keywords(keyword) values ('Android');");
		db.execSQL("insert into keywords(keyword) values ('アプリ');");
		db.execSQL("insert into keywords(keyword) values ('アイディア');");
                
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
}
