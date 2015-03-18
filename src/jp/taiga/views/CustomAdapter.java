package jp.taiga.views;

import java.util.List;

import jp.taiga.beans.LockBean;
import jp.taiga.cloudmashup.DBHelper;
import jp.taiga.cloudmashup.WordSetActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CustomAdapter<T> extends ArrayAdapter<T> {

	private int backColor;
	private int lockPosition;

	public CustomAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public void setBackColor(int background) {
		backColor = background;
	}

	public void setPosition(int position) {
		lockPosition = position;
	}

	public int getPosition() {
		return lockPosition;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自動生成されたメソッド・スタブ
		View v = super.getView(position, convertView, parent);

		ListView listView = (ListView) parent;

		boolean match = false;

		SQLiteDatabase db;
		Cursor cc;

		DBHelper helper = new DBHelper(getContext(), "cloud_db.db", null, 1);
		db = helper.getWritableDatabase();

		String matchWord = "";

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getContext());
		String wordNum = pref.getString("wordNum", "2");

		String lockWordSql;
		if (wordNum.equals("2")) {

			lockWordSql = "select * from lock";
		} else {
			lockWordSql = "select * from lock2";
		}
		cc = db.rawQuery(lockWordSql, null);

		boolean isEof = cc.moveToFirst();
		// カーソルを先頭に移動
		while (isEof) {

			if (cc.getString(0).equals(listView.getItemAtPosition(position))) {
				match = true;
			}

			isEof = cc.moveToNext();
		}

		if (match) {
			v.setBackgroundColor(Color.argb(200, 250, 219, 218));
		} else {
			v.setBackgroundColor(Color.argb(0, 0, 0, 0));
		}

		return v;
	}

}
