package jp.taiga.cloudmashup;

import java.util.concurrent.ExecutionException;

import jp.taiga.views.CustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ShareActivity extends Activity {

	ListView listView;
	ArrayAdapter<String> adapter;
	public static final int MENU_BACK = 0;
	public static final int MENU_RESUME = 1;
	Cursor cursor;
	SQLiteDatabase db;
	int clickPosition;
	String item;
	PopupWindow mPopupWindow;
	LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);

		layout = (LinearLayout) findViewById(R.id.LinearLayout1);

		listView = (ListView) findViewById(R.id.listView);

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自動生成されたメソッド・スタブ

				item = (String) listView.getItemAtPosition(position);
				clickPosition = position;

				new AlertDialog.Builder(ShareActivity.this)
						.setTitle("このワードを追加しますか?")
						.setMessage("【　" + item + "　】")
						.setPositiveButton("Yes", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO 自動生成されたメソッド・スタブ
								try {
									DBHelper helper = new DBHelper(
											ShareActivity.this, "cloud_db.db",
											null, 1);
									db = helper.getWritableDatabase();

									// insertで行に追加
									db.execSQL("insert into keywords(keyword) values ('"
											+ item + "');");
									adapter.remove(item);
									Toast.makeText(ShareActivity.this,
											"追加されました", Toast.LENGTH_SHORT)
											.show();
								} catch (SQLiteConstraintException e) {
									Log.e("error", "制約違反(重複登録)");
									Toast.makeText(ShareActivity.this,
											"すでに登録された単語です", Toast.LENGTH_SHORT)
											.show();
								} catch (SQLException e) {
									Log.e("error", "データベースエラー");
									e.printStackTrace();
								}

							}
						}).setNegativeButton("No", null).show();
			}
		});

		wordGet();

	}

	@Override
	protected void onDestroy() {
		if (mPopupWindow != null && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.share, menu);
		menu.add(0, MENU_RESUME, 0, "RESUME")
				.setIcon(R.drawable.ic_menu_refresh)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(0, MENU_BACK, 0, "BACK")
				.setIcon(android.R.drawable.ic_menu_revert)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO 自動生成されたメソッド・スタブ
		switch (item.getItemId()) {
		case MENU_BACK:
			finish();
			return true;

		case MENU_RESUME:
			wordGet();
		}
		return false;
	}

	private void wordGet() {
		adapter.clear();

		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArary = new JSONArray();

		// jsonデータの作成
		JSONObject jsonOneData;

		try {
			DBHelper helper = new DBHelper(this, "cloud_db.db", null, 1);
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
				// jsonOneData = new JSONObject();
				// jsonOneData.put("word", cursor.getString(0));
				jsonArary.put(cursor.getString(0));
				// getString(0)メソッドで、カーソルの一行目を追加。2,3も同じ。
				// Log.d("JS", cursor.getString(0));
				isEof = cursor.moveToNext();
			}

			jsonObject.put("words", jsonArary);
			// 次のリストにカーソルを移す。
			cursor.close();

		} catch (JSONException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}

		PostMessageTask post = new PostMessageTask(this);
		post.execute(jsonObject);

		try {
			if (post.get().equals("ERORR")) {

				layout.setBackgroundResource(R.drawable.not_connect);

			} else {

				layout.setBackgroundResource(R.drawable.sky);
				try {
					JSONObject json = new JSONObject(post.get());
					JSONArray words = json.getJSONArray("words");
					int count = words.length();
					JSONObject[] bookObject = new JSONObject[count];
					for (int i = 0; i < count; i++) {
						bookObject[i] = words.getJSONObject(i);
						adapter.add(bookObject[i].getString("word"));
					}

				} catch (JSONException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			}
		} catch (InterruptedException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
	}
}
