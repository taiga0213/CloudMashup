package jp.taiga.cloudmashup;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class SettingActivity extends Activity {
	
	public static final int MENU_BACK = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new MyPrefsFragment()).commit();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		menu.add(0, MENU_BACK, 0, "back")
				.setIcon(android.R.drawable.ic_menu_revert)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_BACK:
			finish();
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			return true;

		}
		return false;
	}
	public static class MyPrefsFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref);
		}

		@Override
		public void onStop() {
			// TODO 自動生成されたメソッド・スタブ
			super.onStop();
			getFragmentManager().beginTransaction()
			.remove(this).commit();
		}
	}

}
