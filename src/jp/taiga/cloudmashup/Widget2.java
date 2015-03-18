package jp.taiga.cloudmashup;

import java.util.ArrayList;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class Widget2 extends AppWidgetProvider {

	RemoteViews remoteViews;
	Cursor c;
	String putWordA = "";
	String putWordB = "";
	String lockWord = "";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget2);
		
		Intent intentNext = new Intent();
		intentNext.setAction("UPDATE_WIDGET2");
		PendingIntent next = PendingIntent.getBroadcast(context, 1, intentNext,
				PendingIntent.FLAG_UPDATE_CURRENT);
		
		Intent intentSave = new Intent();
		intentSave.setAction("SAVE_WIDGET2");
		PendingIntent save = PendingIntent.getBroadcast(context, 2, intentSave,
				PendingIntent.FLAG_UPDATE_CURRENT);
		
	
		
		remoteViews.setOnClickPendingIntent(R.id.NextButton,
				next);
		remoteViews.setOnClickPendingIntent(R.id.SaveButton,
				save);
		// /
		ArrayList<String> list = new ArrayList<String>();
		// ダミーデータ格納
		// for(int i=0;i<10;i++){
		// list.add("ダミー"+Integer.toString(i));
		// }

		// データ一覧
		try {
			DBHelper helper = new DBHelper(context, "cloud_db.db", null, 1);
			// データベースの設定
			SQLiteDatabase db;
			db = helper.getWritableDatabase();

			// カーソルの設定
			String[] cols = { "keyword" };
			Cursor c = db.query("keywords", cols, null, null, null, null,
					"keyword asc", null);
			// カーソルのリストを作る。１：テーブル名、２：取得する列名（カラム等）の配列、
			// ３＆４：取得するレコードの条件、５：GROUP BY条件、６：「HAVING」条件、
			// ７：「ORDER BY」条件、８：「limit」条件
			boolean isEof = c.moveToFirst();
			// カーソルを先頭に移動
			while (isEof) {
				// while文。カーソルが最後に行くまで繰り返す。
				list.add(c.getString(0));
				// getString(0)メソッドで、カーソルの一行目を追加。2,3も同じ。
				isEof = c.moveToNext();
			}
			// 次のリストにカーソルを移す。
			c.close();
			// 終わったら閉じる。これがないとエラーとなる。データベースも。
			db.close();
		} catch (SQLException ex) {
			Log.e("error", "データベースエラー");
			Log.e("exception", ex.getMessage());
		}

		SelectWord SelectWord = new SelectWord();
		int[] index = SelectWord.select(list);

		
		try {
			DBHelper helper = new DBHelper(context, "cloud_db.db", null, 1);
			// データベースの設定
			SQLiteDatabase db;
			db = helper.getWritableDatabase();

			String countSql = "select count(*) from lock";

			c = db.rawQuery(countSql, null);
			c.moveToLast();

			long count = c.getLong(0);
			c.close();
			if (count != 0) {
				String lockSql = "select keyword from lock";
				c = db.rawQuery(lockSql, null);
				c.moveToLast();
				lockWord = c.getString(0);

				c.close();
			}else{
				lockWord="";
			}
		} catch (SQLException ex) {
			Log.e("error", "データベースエラー");
			Log.e("exception", ex.getMessage());
		}

		if (lockWord.equals("")) {
			remoteViews.setTextViewText(R.id.WordA, list.get(index[0]));
			putWordA = list.get(index[0]);
		}else{
			remoteViews.setTextViewText(R.id.WordA, lockWord);
			putWordA = lockWord;
		}
		
		if(lockWord.equals(list.get(index[1]))){
			remoteViews.setTextViewText(R.id.WordB, list.get(index[0]));
			putWordB = list.get(index[0]);
		}else{
			remoteViews.setTextViewText(R.id.WordB, list.get(index[1]));
			putWordB = list.get(index[1]);
		}




//		remoteViews.setTextViewText(R.id.WordA, list.get(index[0]));
//		remoteViews.setTextViewText(R.id.WordB, list.get(index[1]));
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		Editor edit = pref.edit();
		edit.putString("word2A", putWordA);
		edit.putString("word2B", putWordB);
		edit.commit();

		// アップデートメソッド呼び出し
		pushWidgetUpdate(context, remoteViews);

	}


	// アップデート
	public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
		ComponentName myWidget = new ComponentName(context, Widget2.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(myWidget, remoteViews);
	}

}
