package jp.taiga.cloudmashup;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * トップ画面
 */
public class TopActivity extends Activity implements OnClickListener {

	public static final int MENU_INFO = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_top);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// メイン処理(マッシュアップ)画面遷移ボタン
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.RelativeLayout1);
		layout.setOnClickListener(this);
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
			// Intent intent = new Intent(this, HowToUseActivity.class);
			// startActivity(intent);
			try {
			DBHelper helper = new DBHelper(this, "cloud_db.db",
					null, 1);
			SQLiteDatabase db = helper.getWritableDatabase();
			
			db.execSQL("delete from ideas");
			db.execSQL("delete from lock");
			db.execSQL("delete from lock2");
			db.execSQL("delete from keywords");
			
			db.execSQL("insert into keywords(keyword) values ('Android');");
			db.execSQL("insert into keywords(keyword) values ('アプリ');");
			db.execSQL("insert into keywords(keyword) values ('アイディア');");
			
			Toast.makeText(this, "DB初期化", Toast.LENGTH_SHORT).show();
			

			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
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
