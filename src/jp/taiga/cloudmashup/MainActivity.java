package jp.taiga.cloudmashup;

import java.text.NumberFormat;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.inputmethodservice.Keyboard.Key;
import android.util.Log;
import android.view.Display;
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
	// int topA = 50;
	// int topB = 450;
	// int topC = 850;
	// int topD = 1250;
	int topA;
	int topB;
	int topC;
	int topD;
	Boolean type;
	Animation anim;

	SharedPreferences pref;

	TwoCreateView cloud2A;
	TwoCreateView cloud2B;
	TwoCreateView cloud2C;
	TwoCreateView cloud2D;
	TwoCreateView cloud2E;
	TwoCreateView cloud2F;
	TwoCreateView cloud2G;
	TwoCreateView cloud2H;

	ThreeCreateView cloud3A;
	ThreeCreateView cloud3B;
	ThreeCreateView cloud3C;
	ThreeCreateView cloud3D;
	ThreeCreateView cloud3E;
	ThreeCreateView cloud3F;
	ThreeCreateView cloud3G;
	ThreeCreateView cloud3H;

	Boolean left = true;
	Boolean right = false;
	int number = 1;
	public static final int MENU_WORD_ADD = 0;
	public static final int MENU_WORD_LIST = 1;
	public static final int MENU_SETTING = 2;
	public static final long PULS = 1100;
	public static final long MINUS = -1100;

	int x;
	int y;
	int winY;
	Point p;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		frameLayout = (FrameLayout) findViewById(R.id.FrameLayout1);

		Display display = getWindowManager().getDefaultDisplay();
		p = new Point();
		display.getSize(p);

		x = 0;
		y = p.y / 4 - 20;
		winY = p.x;

		topA = 0;
		topB = topA + y;
		topC = topB + y;
		topD = topC + y;

		pref = PreferenceManager.getDefaultSharedPreferences(this);
		type = pref.getBoolean("animType", true);

		if (pref.getString("wordNum", "2").equals("2")) {
			cloud2A = new TwoCreateView();
			cloud2B = new TwoCreateView();
			cloud2C = new TwoCreateView();
			cloud2D = new TwoCreateView();

			cloud2A.createTwoCloud(this, frameLayout, topA, type, left, x, winY);
			cloud2B.createTwoCloud(this, frameLayout, topB, type, left, x, winY);
			cloud2C.createTwoCloud(this, frameLayout, topC, type, left, x, winY);
			cloud2D.createTwoCloud(this, frameLayout, topD, type, left, x, winY);

			if (!type) {

				x = p.x / 2 - 10;
				cloud2E = new TwoCreateView();
				cloud2F = new TwoCreateView();
				cloud2G = new TwoCreateView();
				cloud2H = new TwoCreateView();

				cloud2E.createTwoCloud(this, frameLayout, topA, type, right, x,
						winY);
				cloud2F.createTwoCloud(this, frameLayout, topB, type, right, x,
						winY);
				cloud2G.createTwoCloud(this, frameLayout, topC, type, right, x,
						winY);
				cloud2H.createTwoCloud(this, frameLayout, topD, type, right, x,
						winY);

				SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

				Sensor accelSensor = sensorManager.getSensorList(
						Sensor.TYPE_ACCELEROMETER).get(0);
				Boolean getaccelSensor = sensorManager.registerListener(
						new SensorEventListener() {

							@Override
							public void onSensorChanged(SensorEvent event) {
								if (!type) {
									NumberFormat numberFormat = NumberFormat
											.getInstance();
									numberFormat.setMaximumFractionDigits(2);

									if (event.values[0] > 14
											|| event.values[1] > 14
											|| event.values[2] > 14) {
										if (event.values[0] > 8) {
											allChange(MINUS);
										} else {
											allChange(PULS);
										}

										Log.d("PM",
												Float.toString(event.values[0]));

									}
								}

							}

							@Override
							public void onAccuracyChanged(Sensor sensor,
									int accuracy) {

							}
						}, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
			}

		} else {
			cloud3A = new ThreeCreateView();
			cloud3B = new ThreeCreateView();
			cloud3C = new ThreeCreateView();
			cloud3D = new ThreeCreateView();

			cloud3A.createThreeCloud(this, frameLayout, topA, type, left, x,
					winY);
			cloud3B.createThreeCloud(this, frameLayout, topB, type, left, x,
					winY);
			cloud3C.createThreeCloud(this, frameLayout, topC, type, left, x,
					winY);
			cloud3D.createThreeCloud(this, frameLayout, topD, type, left, x,
					winY);

			if (!type) {

				x = p.x / 2 - 10;
				cloud3E = new ThreeCreateView();
				cloud3F = new ThreeCreateView();
				cloud3G = new ThreeCreateView();
				cloud3H = new ThreeCreateView();

				cloud3E.createThreeCloud(this, frameLayout, topA, type, right,
						x, winY);
				cloud3F.createThreeCloud(this, frameLayout, topB, type, right,
						x, winY);
				cloud3G.createThreeCloud(this, frameLayout, topC, type, right,
						x, winY);
				cloud3H.createThreeCloud(this, frameLayout, topD, type, right,
						x, winY);

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

								if (!type) {
									if (event.values[0] > 14
											|| event.values[1] > 14
											|| event.values[2] > 14) {
										if (event.values[0] > 8) {
											allChange(MINUS);
											// allChange(0);
										} else {
											allChange(PULS);
											// allChange(0);
										}

										Log.d("PM",
												Float.toString(event.values[0]));

									}
								}

							}

							@Override
							public void onAccuracyChanged(Sensor sensor,
									int accuracy) {

							}
						}, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
			}

		}

	}

	@Override
	protected void onResume() {
		// TODO 自動生成されたメソッド・スタブ
		super.onResume();
	}

	public void allChange(long symbol) {

		frameLayout.removeAllViews();

		if (pref.getString("wordNum", "2").equals("2")) {
			cloud2A = new TwoCreateView();
			cloud2B = new TwoCreateView();
			cloud2C = new TwoCreateView();
			cloud2D = new TwoCreateView();

			cloud2A.createTwoCloud(this, frameLayout, topA, type, left, x, winY);
			cloud2B.createTwoCloud(this, frameLayout, topB, type, left, x, winY);
			cloud2C.createTwoCloud(this, frameLayout, topC, type, left, x, winY);
			cloud2D.createTwoCloud(this, frameLayout, topD, type, left, x, winY);

			if (!type) {

				x = p.x / 2 - 10;
				cloud2E = new TwoCreateView();
				cloud2F = new TwoCreateView();
				cloud2G = new TwoCreateView();
				cloud2H = new TwoCreateView();

				cloud2E.createTwoCloud(this, frameLayout, topA, type, right, x,
						winY);
				cloud2F.createTwoCloud(this, frameLayout, topB, type, right, x,
						winY);
				cloud2G.createTwoCloud(this, frameLayout, topC, type, right, x,
						winY);
				cloud2H.createTwoCloud(this, frameLayout, topD, type, right, x,
						winY);

			}

		} else {
			cloud3A = new ThreeCreateView();
			cloud3B = new ThreeCreateView();
			cloud3C = new ThreeCreateView();
			cloud3D = new ThreeCreateView();

			cloud3A.createThreeCloud(this, frameLayout, topA, type, left, x,
					winY);
			cloud3B.createThreeCloud(this, frameLayout, topB, type, left, x,
					winY);
			cloud3C.createThreeCloud(this, frameLayout, topC, type, left, x,
					winY);
			cloud3D.createThreeCloud(this, frameLayout, topD, type, left, x,
					winY);

			if (!type) {

				x = p.x / 2 - 10;
				cloud3E = new ThreeCreateView();
				cloud3F = new ThreeCreateView();
				cloud3G = new ThreeCreateView();
				cloud3H = new ThreeCreateView();

				cloud3E.createThreeCloud(this, frameLayout, topA, type, right,
						x, winY);
				cloud3F.createThreeCloud(this, frameLayout, topB, type, right,
						x, winY);
				cloud3G.createThreeCloud(this, frameLayout, topC, type, right,
						x, winY);
				cloud3H.createThreeCloud(this, frameLayout, topD, type, right,
						x, winY);

			}

		}

		if (pref.getString("wordNum", "2").equals("2")) {

			// cloud2A.changeView();
			// cloud2B.changeView();
			// cloud2C.changeView();
			// cloud2D.changeView();
			// cloud2E.changeView();
			// cloud2F.changeView();
			// cloud2G.changeView();
			// cloud2H.changeView();
		} else {
			// cloud3A.changeView();
			// cloud3B.changeView();
			// cloud3C.changeView();
			// cloud3D.changeView();
			// cloud3E.changeView();
			// cloud3F.changeView();
			// cloud3G.changeView();
			// cloud3H.changeView();
		}
	}

	public void allRemove() {

		if (pref.getString("wordNum", "2").equals("2")) {

			cloud2A.remove();
			cloud2B.remove();
			cloud2C.remove();
			cloud2D.remove();
			if (!type) {
				cloud2E.remove();
				cloud2F.remove();
				cloud2G.remove();
				cloud2H.remove();
			}
		} else {
			cloud3A.remove();
			cloud3B.remove();
			cloud3C.remove();
			cloud3D.remove();
			if (!type) {
				cloud3E.remove();
				cloud3F.remove();
				cloud3G.remove();
				cloud3H.remove();
			}
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO 自動生成されたメソッド・スタブ
		// DOWNとUPが取得できるのでログの2重表示防止のためif
		if (!type) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN
					|| event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					allChange(PULS);
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

	@Override
	protected void onPause() {
		// TODO 自動生成されたメソッド・スタブ
		super.onPause();
	}

}
