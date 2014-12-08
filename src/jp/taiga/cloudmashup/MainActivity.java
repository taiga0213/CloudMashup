package jp.taiga.cloudmashup;

import java.text.NumberFormat;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.inputmethodservice.Keyboard.Key;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	FrameLayout frameLayout;
	int topA = 50;
	int topB = 450;
	int topC = 850;
	int topD = 1250;
	Boolean type;
	Animation anim;
	createView cloudA;
	createView cloudB;
	createView cloudC;
	createView cloudD;
	createView cloudE;
	createView cloudF;
	createView cloudG;
	createView cloudH;
	Boolean left = true;
	Boolean right = false;
	int number = 1;
	public static final int MENU_WORD_ADD = 0;
	public static final int MENU_WORD_LIST = 1;
	public static final int MENU_SETTING = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		frameLayout = (FrameLayout) findViewById(R.id.FrameLayout1);

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		type = pref.getBoolean("animType", true);

		cloudA = new createView();
		cloudB = new createView();
		cloudC = new createView();
		cloudD = new createView();

		cloudA.createCloud(this, frameLayout, topA, type, left);
		cloudB.createCloud(this, frameLayout, topB, type, left);
		cloudC.createCloud(this, frameLayout, topC, type, left);
		cloudD.createCloud(this, frameLayout, topD, type, left);

		if (!type) {

			cloudE = new createView();
			cloudF = new createView();
			cloudG = new createView();
			cloudH = new createView();

			cloudE.createCloud(this, frameLayout, topA, type, right);
			cloudF.createCloud(this, frameLayout, topB, type, right);
			cloudG.createCloud(this, frameLayout, topC, type, right);
			cloudH.createCloud(this, frameLayout, topD, type, right);

			SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

			Sensor accelSensor = sensorManager.getSensorList(
					Sensor.TYPE_ACCELEROMETER).get(0);
			Boolean getaccelSensor = sensorManager.registerListener(
					new SensorEventListener() {

						@Override
						public void onSensorChanged(SensorEvent event) {
							NumberFormat numberFormat = NumberFormat
									.getInstance();
							numberFormat.setMaximumFractionDigits(2);

							if (event.values[0] > 14 || event.values[1] > 14
									|| event.values[2] > 14) {
								
								allChange();
							}

						}

						@Override
						public void onAccuracyChanged(Sensor sensor,
								int accuracy) {

						}
					}, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
		}

	}

	public void allChange() {
		frameLayout.removeAllViews();
		cloudA.createCloud(MainActivity.this, frameLayout, topA, type, left);
		cloudB.createCloud(MainActivity.this, frameLayout, topB, type, left);
		cloudC.createCloud(MainActivity.this, frameLayout, topC, type, left);
		cloudD.createCloud(MainActivity.this, frameLayout, topD, type, left);
		cloudE.createCloud(MainActivity.this, frameLayout, topA, type, right);
		cloudF.createCloud(MainActivity.this, frameLayout, topB, type, right);
		cloudG.createCloud(MainActivity.this, frameLayout, topC, type, right);
		cloudH.createCloud(MainActivity.this, frameLayout, topD, type, right);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO 自動生成されたメソッド・スタブ
		// DOWNとUPが取得できるのでログの2重表示防止のためif
		if (!type) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN
					|| event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					allChange();
					return true;
				}

			}
		}

		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		menu.add(0, MENU_WORD_ADD, 0, "Word ADD")
				.setIcon(android.R.drawable.ic_menu_add)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(0, MENU_WORD_LIST, 0, "Word LIST")
				.setIcon(android.R.drawable.ic_menu_save)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(0, MENU_SETTING, 0, "Setting")
				.setIcon(android.R.drawable.ic_menu_preferences)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_WORD_ADD:
			Intent intent = new Intent(this, WordSetActivity.class);
			startActivity(intent);
			return true;
		case MENU_WORD_LIST:
			intent = new Intent(this, IdeaViewActivity.class);
			startActivity(intent);
			return true;
		case MENU_SETTING:
			intent = new Intent(this, SettingActivity.class);
			startActivity(intent);
			finish();
			return true;

		}
		return false;
	}

	public void cloudClick(View v) {
		// TODO 自動生成されたメソッド・スタブ
		TextView wordAView = (TextView) findViewById(R.id.getWordA);
		TextView wordBView = (TextView) findViewById(R.id.getWordB);
		String wordA = wordAView.getText().toString();
		String wordB = wordBView.getText().toString();

		try {

			DBHelper helper = new DBHelper(MainActivity.this, "cloud_db.db",
					null, 1);
			SQLiteDatabase db;
			db = helper.getWritableDatabase();
			db.execSQL("insert into ideas values ('" + wordA + "','" + wordB
					+ "');");
			db.close();

			Toast.makeText(this, "登録されました", Toast.LENGTH_SHORT).show();

		} catch (SQLiteConstraintException e) {
			Log.e("error", "制約違反(重複登録)");
			Toast.makeText(this, "すでに登録された単語です", Toast.LENGTH_SHORT).show();
		} catch (SQLException e) {
			Log.e("error", "データベースエラー");
			e.printStackTrace();
		}

	}

}
