package jp.taiga.cloudmashup;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

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

	ProgressDialog dialog;

	String serverIP = "192.168.43.94";// 実家

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		layout = (LinearLayout) findViewById(R.id.LinearLayout1);

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		actionBar.addTab(actionBar
				.newTab()
				.setText("ワード")
				.setTabListener(
						new TabListener<ShereWordFragment>(this, "tag1",
								ShereWordFragment.class)));
		actionBar.addTab(actionBar
				.newTab()
				.setText("トレンド")
				.setTabListener(
						new TabListener<ShereTrendFragment>(this, "tag2",
								ShereTrendFragment.class)));

	}

	@Override
	protected void onDestroy() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		pref.edit().remove("GetResponse").commit();
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
			SharedPreferences pref = PreferenceManager
					.getDefaultSharedPreferences(this);
			pref.edit().remove("GetResponse").commit();
			this.finish();
			startActivity((new Intent(ShareActivity.this, ShareActivity.class)));
//			this.recreate();

		}
		return false;
	}

}
