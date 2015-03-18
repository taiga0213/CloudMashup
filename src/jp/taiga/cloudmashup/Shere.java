package jp.taiga.cloudmashup;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

public class Shere {

	ListView listView;
	Cursor cursor;
	SQLiteDatabase db;
	int clickPosition;
	String item;
	PopupWindow mPopupWindow;
	// LinearLayout layout;
	PostMessageTask post;

	public void wordGet(Context context, LinearLayout layout,
			ArrayAdapter<String> adapter) {
		adapter.clear();

		// 　送信JSON形式
		// {
		// "keywordsBean":[
		// {"keyword":"ワード"},
		// {"keyword":"ワード"},
		// {"keyword":"ワード"}
		// ]
		// }
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArary = new JSONArray();

		try {
			DBHelper helper = new DBHelper(context, "cloud_db.db", null, 1);
			db = helper.getWritableDatabase();

			// カーソルの設定
			String[] cols = { "keyword" };
			cursor = db.query("keywords", cols, null, null, null, null,
					"keyword asc", null);
			// カーソルのリストを作る。１：テーブル名、２：取得する列名（カラム等）の配列、
			// ３＆４：取得するレコードの条件、５：GROUP
			// BY条件、６：「HAVING」条件、
			// ７：「ORDER BY」条件、８：「limit」条件
			boolean isEof = cursor.moveToFirst();
			// カーソルを先頭に移動
			while (isEof) {
				// while文。カーソルが最後に行くまで繰り返す。
				JSONObject oneJsonObject = new JSONObject();
				oneJsonObject.put("keyword", cursor.getString(0));
				jsonArary.put(oneJsonObject);
				// getString(0)メソッドで、カーソルの一行目を追加。2,3も同じ。
				isEof = cursor.moveToNext();
			}

			jsonObject.put("keywordsBean", jsonArary);
			// 次のリストにカーソルを移す。
			cursor.close();

		} catch (JSONException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}

		post = new PostMessageTask(context);
		post.execute(jsonObject);

		try {
			if (post.get().equals("ERORR")) {

				layout.setBackgroundResource(R.drawable.not_connect);
				SharedPreferences pref = PreferenceManager
						.getDefaultSharedPreferences(context);

				SharedPreferences.Editor editor = pref.edit();
				editor.putString("GetResponse", null);
				editor.apply();

			} else {

				layout.setBackgroundResource(R.drawable.sky);
				SharedPreferences pref = PreferenceManager
						.getDefaultSharedPreferences(context);

				SharedPreferences.Editor editor = pref.edit();
				editor.putString("GetResponse", post.get());
				editor.apply();

			}
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	public void wordSet(Context context, LinearLayout layout,
			ArrayAdapter<String> adapter) {

		try {
			// 受信JSON形式
			// {
			// "keywordsBean":[
			// {"keyword":"ワード"},
			// {"keyword":"ワード"},
			// {"keyword":"ワード"}
			// ],
			// "trendbean":[
			// {"place":"場所","trend":"ワード"},
			// {"place":"場所","trend":"ワード"},
			// {"place":"場所","trend":"ワード"}
			// 　 ]
			// }

			SharedPreferences pref = PreferenceManager
					.getDefaultSharedPreferences(context);

			JSONObject json = new JSONObject(
					pref.getString("GetResponse", null));
			JSONArray words = json.getJSONArray("keywordsBean");
			int count = words.length();
			for (int i = 0; i < count; i++) {
				// adapter.add(words.getString(i));
				adapter.add(words.getJSONObject(i).getString("keyword"));
			}

		} catch (JSONException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	public void trendSet(Context context, LinearLayout layout,
			ArrayAdapter<String> adapter) {

		try {
			// 受信JSON形式
			// {
			// "keywordsBean":[
			// {"keyword":"ワード"},
			// {"keyword":"ワード"},
			// {"keyword":"ワード"}
			// ],
			// "trendbean":[
			// {"place":"場所","trend":"ワード"},
			// {"place":"場所","trend":"ワード"},
			// {"place":"場所","trend":"ワード"}
			// 　 ]
			// }

			SharedPreferences pref = PreferenceManager
					.getDefaultSharedPreferences(context);

			JSONObject json = new JSONObject(
					pref.getString("GetResponse", null));

			// テスト用(trend)
			JSONArray trends = json.getJSONArray("trendBean");
			int count = trends.length();
			for (int i = 0; i < count; i++) {
				// adapter.add(words.getString(i));
				adapter.add("[" + trends.getJSONObject(i).getString("place")
						+ "]:" + trends.getJSONObject(i).getString("trend"));
			}

		} catch (JSONException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

}
