package jp.taiga.cloudmashup;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class IdeaViewActivity extends Activity {

	DBHelper helper;
	// データベースの設定
	SQLiteDatabase db;
	ArrayAdapter<String> adapter;
	String item;
	ListView listView;
	Cursor cc;
	Cursor c;
	public static final int MENU_BACK = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_idea_view);

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);

		listView = (ListView) findViewById(R.id.listView);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自動生成されたメソッド・スタブ
				// クリックされたアイテムを取得します
				item = (String) listView.getItemAtPosition(position);

				// 　選択したアイテムの削除
				new AlertDialog.Builder(IdeaViewActivity.this)
						.setTitle("このワードを削除しますか?")
						.setMessage("【　" + item + "　】")
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										try {
											// DBヘルパーの起動
											DBHelper helper = new DBHelper(
													IdeaViewActivity.this,
													"cloud_db.db", null, 1);
											db = helper.getWritableDatabase();

											// 保存時に付与した文字でワードを分離
											String[] word = item.split(" \\+ ");

											// 分離したワードでDelete文を実行
											String deleteSql = "delete from ideas where keywordA = '"
													+ word[0]
													+ "' and keywordB = '"
													+ word[1] + "';";
											db.execSQL(deleteSql);

											// listviewの初期化
											adapter.clear();

											// カーソルの設定
											String[] cols = { "keywordA",
													"keywordB" };
											// カーソルのリストを作る。１：テーブル名、２：取得する列名（カラム等）の配列、
											// ３＆４：取得するレコードの条件、５：GROUP
											// BY条件、６：「HAVING」条件、
											// ７：「ORDER BY」条件、８：「limit」条件
											Cursor c = db.query("ideas", cols,
													null, null, null, null,
													null, null);
											// カーソルを先頭に移動
											boolean isEof = c.moveToFirst();

											// while文。カーソルが最後に行くまで繰り返す。
											while (isEof) {
												String text = null;
												text = c.getString(0) + " + "
														+ c.getString(1);
												// listviewのアダプタに追加
												adapter.add(text);
												//カーソルを次に進める
												isEof = c.moveToNext();
											}

											// カーソルのクローズ
											c.close();

										} catch (Exception e) {
											e.printStackTrace();
										}
										// アダプタの中身がなくなったときに代わりにメッセージを入れる
										if (adapter.isEmpty()) {
											adapter.add("組み合わせが存在しません");
										}
									}
								}).setNegativeButton("No", null).show();

			}

		});

		// アダプターのセット
		listView.setAdapter(adapter);

		try {
			// DBヘルパーの起動
			DBHelper helper = new DBHelper(IdeaViewActivity.this,
					"cloud_db.db", null, 1);
			db = helper.getWritableDatabase();

			// カーソルの設定
			String[] cols = { "keywordA", "keywordB" };
			// カーソルのリストを作る。１：テーブル名、２：取得する列名（カラム等）の配列、
			// ３＆４：取得するレコードの条件、５：GROUP BY条件、６：「HAVING」条件、
			// ７：「ORDER BY」条件、８：「limit」条件
			Cursor c = db.query("ideas", cols, null, null, null, null, null,
					null);
			// カーソルを先頭に移動
			boolean isEof = c.moveToFirst();
			// while文。カーソルが最後に行くまで繰り返す。
			while (isEof) {
				String text = null;
				text = c.getString(0) + " + " + c.getString(1);
				//アダプターに内容をセット
				adapter.add(text);
				//カーソルを次に進める
				isEof = c.moveToNext();
			}
			if (adapter.isEmpty()) {
				adapter.add("組み合わせが存在しません");
			}
			//カーソルを閉じる
			c.close();

		} catch (SQLException ex) {
			Log.e("error", "データベースエラー");
			Log.e("exception", ex.getMessage());
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.word_set, menu);

		menu.add(0, MENU_BACK, 0, "BACK")
				.setIcon(android.R.drawable.ic_menu_revert)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_BACK:
			finish();//Activityを終了し、前の画面に戻る
			return true;

		}
		return false;
	}

}
