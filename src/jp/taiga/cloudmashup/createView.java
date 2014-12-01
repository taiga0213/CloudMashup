package jp.taiga.cloudmashup;

import java.util.ArrayList;
import java.util.Random;

import jp.taiga.views.cloudView;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class createView {

	private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
	private Random random = new Random();

	TextView wordA;
	TextView wordB;
	Cursor c;
	String lockWord = "";
	Button cloudButton;

	public void createCloud(final Context context, final FrameLayout layout,
			final int top) {
		final cloudView cloudView = new cloudView(context);

		wordA = (TextView) cloudView.findViewById(R.id.getWordA);
		wordB = (TextView) cloudView.findViewById(R.id.getWordB);
		cloudButton = (Button) cloudView.findViewById(R.id.cloud);

		FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(600, 450);
		p.setMargins(0, top, 0, 0);
		ObjectAnimator transAnimator = ObjectAnimator.ofFloat(cloudView, "x",
				-600f, 1100f);
		
		transAnimator.setDuration((random.nextInt(30) + 30) * 100);
		transAnimator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				// Write processing after animating
				layout.removeView(cloudView);
				createCloud(context, layout, top);
			}
		});

		ArrayList<String> list = new ArrayList<String>();
		// ダミーデータ格納
		// for(int i=0;i<10;i++){
		// list.add("ダミー"+Integer.toString(i));
		// }

		// データ一覧
		try {
			DBHelper helper = new DBHelper(context, "cloud_db.db", null, 1);
			// データベースの設定
			SQLiteDatabase db;
			db = helper.getWritableDatabase();

			// カーソルの設定
			String[] cols = { "keyword" };
			Cursor c = db.query("keywords", cols, null, null, null, null,
					"keyword asc", null);
			// カーソルのリストを作る。１：テーブル名、２：取得する列名（カラム等）の配列、
			// ３＆４：取得するレコードの条件、５：GROUP BY条件、６：「HAVING」条件、
			// ７：「ORDER BY」条件、８：「limit」条件
			boolean isEof = c.moveToFirst();
			// カーソルを先頭に移動
			while (isEof) {
				// while文。カーソルが最後に行くまで繰り返す。
				list.add(c.getString(0));
				// getString(0)メソッドで、カーソルの一行目を追加。2,3も同じ。
				isEof = c.moveToNext();
			}
			// 次のリストにカーソルを移す。
			c.close();
			// 終わったら閉じる。これがないとエラーとなる。データベースも。
			db.close();
		} catch (SQLException ex) {
			Log.e("error", "データベースエラー");
			Log.e("exception", ex.getMessage());
		}

		selectWord SelectWord = new selectWord();
		int[] index = SelectWord.select(list);

		cloudButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				String wordAString = wordA.getText().toString();
				String wordBString = wordB.getText().toString();
				// Intent intent = new Intent(context, WordViewActivity.class);
				// intent.putExtra("wordA", wordAString);
				// intent.putExtra("wordB", wordBString);
				// v.getContext().startActivity(intent);

				try {

					DBHelper helper = new DBHelper(context, "cloud_db.db",
							null, 1);
					SQLiteDatabase db;
					db = helper.getWritableDatabase();

					// insertで行に追加
					db.execSQL("insert into ideas values ('" + wordAString
							+ "','" + wordBString + "');");

					// 終わったら閉じる。これがないとエラーとなる。データベースも。
					db.close();

					// alphaプロパティを0fから1fに変化させます
//					ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(
//							cloudView, "alpha", 1f, 0f);
					

				    // translationXプロパティを0fからtoXに変化させます
				    PropertyValuesHolder holderX = PropertyValuesHolder.ofFloat( "translationX", 700f );
				    // translationYプロパティを0fからtoYに変化させます
				    PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat( "translationY", cloudView.getY()*-1-300 );
				    
				    // translationXプロパティを0fからtoXに変化させます
				    PropertyValuesHolder scaleX  = PropertyValuesHolder.ofFloat( "scaleX", 0.2f );
				    // translationYプロパティを0fからtoYに変化させます
				    PropertyValuesHolder scaleY  = PropertyValuesHolder.ofFloat( "scaleY", 0.2f );
				    
				    // targetに対してholderX, holderY, holderRotationを同時に実行させます
				    ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
				            cloudView, holderX, holderY, scaleX, scaleY);


					objectAnimator.addListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							// Write processing after animating
							layout.removeView(cloudView);
						}
					});

					// 3秒かけて実行させます
					objectAnimator.setDuration(1000);

					// アニメーションを開始します
					objectAnimator.start();
				} catch (SQLiteConstraintException e) {
					Log.e("error", "制約違反(重複登録)");
					Toast.makeText(context, "すでに登録された単語です", Toast.LENGTH_SHORT)
							.show();
				} catch (SQLException e) {
					Log.e("error", "データベースエラー");
					e.printStackTrace();
				}

			}
		});

		try {
			DBHelper helper = new DBHelper(context, "cloud_db.db", null, 1);
			// データベースの設定
			SQLiteDatabase db;
			db = helper.getWritableDatabase();

			String countSql = "select count(*) from lock";

			c = db.rawQuery(countSql, null);
			c.moveToLast();

			long count = c.getLong(0);
			c.close();
			if (count != 0) {
				String lockSql = "select keyword from lock";
				c = db.rawQuery(lockSql, null);
				c.moveToLast();
				lockWord = c.getString(0);

				c.close();
			} else {
				lockWord = "";
			}
		} catch (SQLException ex) {
			Log.e("error", "データベースエラー");
			Log.e("exception", ex.getMessage());
		}

		if (lockWord.equals("")) {
			wordA.setText(list.get(index[0]));
		} else {
			wordA.setText(lockWord);
		}

		if (lockWord.equals(list.get(index[1]))) {
			wordB.setText(list.get(index[0]));
		} else {
			wordB.setText(list.get(index[1]));
		}

		layout.addView(cloudView, p);
		transAnimator.start();
	}

}
