package jp.taiga.cloudmashup;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * トップ画面
 */
public class TopActivity extends Activity implements OnClickListener {

	public static final int MENU_INFO = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_top);
		try {
			DBHelper helper = new DBHelper(this, "cloud_db.db", null, 1);
			// データベースの設定
			SQLiteDatabase db;
			db = helper.getWritableDatabase();

			String countSql = "select count(*) from keywords";

			Cursor c = db.rawQuery(countSql, null);
			c.moveToLast();
			long count = c.getLong(0);
			c.close();

			if (count < 2) {
				db.execSQL("insert into keywords(keyword) values ('ダミー1');");
				db.execSQL("insert into keywords(keyword) values ('ダミー2');");
			}

			db.close();
		} catch (SQLException ex) {
			Log.e("error", "データベースエラー");
			Log.e("exception", ex.getMessage());
		}
		// メイン処理(マッシュアップ)画面遷移ボタン
		Button button = (Button) findViewById(R.id.btnCustom);
		button.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.top, menu);
		menu.add(0, MENU_INFO, 0, "Info")
		.setIcon(android.R.drawable.ic_menu_info_details)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_INFO:
			Intent intent = new Intent(this, HowToUseActivity.class);
			startActivity(intent);
			return true;

		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO 自動生成されたメソッド・スタブ
		// インテント作成
		Intent intent = new Intent(this, MainActivity.class);
		// 画面遷移
		startActivity(intent);

	}

}
