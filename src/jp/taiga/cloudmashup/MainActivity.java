package jp.taiga.cloudmashup;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	FrameLayout frameLayout;
	int topA = 50;
	int topB = 600;
	int topC = 1150;
	Animation anim;
	createView cloudA;
	createView cloudB;
	createView cloudC;
	int number = 1;
	public static final int MENU_WORD_ADD = 0;
	public static final int MENU_WORD_LIST = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		frameLayout = (FrameLayout) findViewById(R.id.FrameLayout1);
		cloudA = new createView();
		cloudB = new createView();
		cloudC = new createView();

		cloudA.createCloud(this, frameLayout, topA);
		cloudB.createCloud(this, frameLayout, topB);
		cloudC.createCloud(this, frameLayout, topC);

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
			db.execSQL("insert into ideas values ('" + wordA + "','" + wordB + "');");
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
