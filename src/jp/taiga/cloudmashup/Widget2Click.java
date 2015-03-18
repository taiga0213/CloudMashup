package jp.taiga.cloudmashup;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
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
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class Widget2Click extends BroadcastReceiver {

	Cursor c;
	String lockWord = "";
	String putWordA = "";
	String putWordB = "";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("UPDATE_WIDGET2")) {
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget2);
			ArrayList<String> list = new ArrayList<String>();

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
				} else {
					lockWord = "";
				}
			} catch (SQLException ex) {
				Log.e("error", "データベースエラー");
				Log.e("exception", ex.getMessage());
			}

			if (lockWord.equals("")) {
				remoteViews.setTextViewText(R.id.WordA, list.get(index[0]));
				putWordA = list.get(index[0]);
			} else {
				remoteViews.setTextViewText(R.id.WordA, lockWord);
				putWordA = lockWord;
			}

			if (lockWord.equals(list.get(index[1]))) {
				remoteViews.setTextViewText(R.id.WordB, list.get(index[0]));
				putWordB = list.get(index[0]);
			} else {
				remoteViews.setTextViewText(R.id.WordB, list.get(index[1]));
				putWordB = list.get(index[1]);
			}

			// remoteViews.setTextViewText(R.id.WordA, list.get(index[0]));
			// remoteViews.setTextViewText(R.id.WordB, list.get(index[1]));

			SharedPreferences pref = PreferenceManager
					.getDefaultSharedPreferences(context);
			Editor edit = pref.edit();
			edit.putString("word2A", putWordA);
			edit.putString("word2B", putWordB);
			edit.commit();

			// もう一回クリックイベントを登録(毎回登録しないと上手く動かず)
			// remoteViews.setOnClickPendingIntent(R.id.NextButton,
			// Widget.clickNextButton(context));
			// remoteViews.setOnClickPendingIntent(R.id.SaveButton,
			// Widget.clickSaveButton(context));

			Widget2.pushWidgetUpdate(context.getApplicationContext(),
					remoteViews);
		} else if (intent.getAction().equals("SAVE_WIDGET2")) {
			// RemoteViews remoteViews = new
			// RemoteViews(context.getPackageName(),
			// R.layout.widget);

			
			
			SharedPreferences pref = PreferenceManager
					.getDefaultSharedPreferences(context);

			String wordA = pref.getString("word2A", "");
			String wordB = pref.getString("word2B", "");

			try {

				DBHelper helper = new DBHelper(context, "cloud_db.db", null, 1);
				SQLiteDatabase db;
				db = helper.getWritableDatabase();

				// insertで行に追加
				db.execSQL("insert into ideas(keywordA,keywordB,keywordC) values ('" + wordA + "','"
						+ wordB + "','null');");

				// 終わったら閉じる。これがないとエラーとなる。データベースも。
				db.close();
				
				Toast.makeText(context, "登録されました", Toast.LENGTH_SHORT).show();
			} catch (SQLiteConstraintException e) {
				Log.e("error", "制約違反(重複登録)");
				Toast.makeText(context, "すでに登録されたアイディアです", Toast.LENGTH_SHORT)
						.show();
			} catch (SQLException e) {
				Log.e("error", "データベースエラー");
				e.printStackTrace();
			}

			// remoteViews.setOnClickPendingIntent(R.id.NextButton,
			// Widget.clickNextButton(context));
			// remoteViews.setOnClickPendingIntent(R.id.SaveButton,
			// Widget.clickSaveButton(context));

			// Widget.pushWidgetUpdate(context.getApplicationContext(),
			// remoteViews);
		}
	}
}