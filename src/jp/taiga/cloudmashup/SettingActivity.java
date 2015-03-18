package jp.taiga.cloudmashup;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

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

		SharedPreferences pref;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref);
			pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

			
			EditTextPreference min = (EditTextPreference) findPreference("min");
			EditTextPreference max = (EditTextPreference) findPreference("max");

			min.setSummary(min.getText() + "秒");			
			max.setSummary(max.getText() + "秒");
			// リスナーを設定する
			min.setOnPreferenceChangeListener(editTextPreference_OnPreferenceChangeListener);
			max.setOnPreferenceChangeListener(editTextPreference_OnPreferenceChangeListener);
			
			ListPreference wordNum = (ListPreference)findPreference("wordNum");
			wordNum.setSummary(wordNum.getEntry());
			wordNum.setOnPreferenceChangeListener(listPreference_OnPreferenceChangeListener);
		}

		@Override
		public void onStop() {
			// TODO 自動生成されたメソッド・スタブ
			super.onStop();
			getFragmentManager().beginTransaction().remove(this).commit();
		}
		
		private OnPreferenceChangeListener listPreference_OnPreferenceChangeListener = new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// TODO 自動生成されたメソッド・スタブ
				preference.setSummary("表示数："+(String)newValue);
				return true;
			}
		};

		// テキストボックスPreferenceの PreferenceChangeリスナー
		private OnPreferenceChangeListener editTextPreference_OnPreferenceChangeListener = new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				return NumCheck(preference, newValue);
			}
		};

		private boolean NumCheck(Preference preference, Object newValue) {
			String input = newValue.toString();
			if (input != null && !input.equals("")) {
				// nullでなく100以下であれば変更
				if (preference.getKey().equals("max")) {
					if (Integer.parseInt(input) <= Integer.parseInt(pref.getString(
							"min", "0"))) {
						Toast.makeText(getActivity(), "最短時間より大きくしてください",
								Toast.LENGTH_SHORT).show();
						return false;
					}
				}else{
					if (Integer.parseInt(input) >= Integer.parseInt(pref.getString(
							"max", "999"))) {
						Toast.makeText(getActivity(), "最長時間より小さくしてください",
								Toast.LENGTH_SHORT).show();
						return false;
					}
				}
				String summary = input + "秒";
				preference.setSummary(summary);
				return true;

			} else {
				Toast.makeText(getActivity(), "値を入力してください", Toast.LENGTH_SHORT)
						.show();
				return false;
			}
		}

	}

}
