package jp.taiga.cloudmashup;

import java.util.ArrayList;
import java.util.Random;

import jp.taiga.views.ThreeCloudView;
import jp.taiga.views.TwoCloudView;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ThreeCreateView {

	private Random random = new Random();

	TextView wordA;
	TextView wordB;
	TextView wordC;
	Cursor c;
	ArrayList<String> lockWord;
	ImageButton cloudButton;
	ThreeCloudView cloudView;
	ObjectAnimator transAnimator;
	FrameLayout layout;
	Boolean type;
	ArrayList<String> list;
	int[] index;
	Boolean leftRight;
	Context context;
	int top;
	int x;
	int y;
	SharedPreferences pref;

	public void createThreeCloud(final Context context,
			final FrameLayout layout, final int top, final Boolean type,
			final Boolean leftRight, final int x, final int y) {
		cloudView = new ThreeCloudView(context) {
			protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
				super.onMeasure(widthMeasureSpec, heightMeasureSpec);
				// 引数の情報から画面の横方向の描画領域のサイズを取得する
				int width = MeasureSpec.getSize(widthMeasureSpec);
				int height = MeasureSpec.getSize(heightMeasureSpec);
				// Viewの描画サイズを横方向を画面端まで使う指定
				setMeasuredDimension(width, height);
			}

		};
		list = new ArrayList<String>();
		pref = PreferenceManager.getDefaultSharedPreferences(context);

		this.layout = layout;
		this.type = type;
		this.leftRight = leftRight;
		this.top = top;
		this.context = context;
		this.x = x;
		this.y = y;

		wordA = (TextView) cloudView.findViewById(R.id.wordGetA);
		wordB = (TextView) cloudView.findViewById(R.id.wordGetB);
		wordC = (TextView) cloudView.findViewById(R.id.wordGetC);
		cloudButton = (ImageButton) cloudView.findViewById(R.id.cloud);

		FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(600, 450);

		transAnimator = new ObjectAnimator();

		if (type) {
			// アニメーション有り
			transAnimator = ObjectAnimator
					.ofFloat(cloudView, "x", -600f, 1100f);
			p.setMargins(0, top, 0, 0);
			int between = Integer.valueOf(pref.getString("max", "10"))
					- Integer.valueOf(pref.getString("min", "5"));
			transAnimator.setDuration((random.nextInt(between) + Integer
					.valueOf(pref.getString("min", "5"))) * 1000);
			transAnimator.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					// Write processing after animating
					layout.removeView(cloudView);
					createThreeCloud(context, layout, top, type, leftRight, x,
							y);
				}

				@Override
				public void onAnimationCancel(Animator animation) {
					// TODO 自動生成されたメソッド・スタブ
					layout.removeView(cloudView);
				}

			});

		} else {
			// アニメーションなし
			if (leftRight) {
				p.setMargins(-20, top, 0, 0);
			} else {
				p.setMargins(x, top, 0, 0);
			}

		}

		wordSet(context);

		lockWord(context);

		// if (lockWord.size() == 0) {
		// wordA.setText(list.get(index[0]));
		// wordB.setText(list.get(index[1]));
		// wordC.setText(list.get(index[2]));
		// } else if (lockWord.size() == 1) {
		// wordA.setText(lockWord.get(0));
		// if (lockWord.get(0).equals(list.get(index[1]))) {
		// wordB.setText(list.get(index[0]));
		// } else {
		// wordB.setText(list.get(index[1]));
		// }
		//
		// if (lockWord.get(0).equals(list.get(index[2]))) {
		// wordC.setText(list.get(index[0]));
		// } else {
		// wordC.setText(list.get(index[2]));
		// }
		// } else {
		// wordA.setText(lockWord.get(0));
		// wordB.setText(lockWord.get(1));
		//
		// if (lockWord.get(0).equals(list.get(index[2]))) {
		// wordC.setText(list.get(index[0]));
		// } else if (lockWord.get(1).equals(list.get(index[2]))) {
		// wordC.setText(list.get(index[1]));
		// } else {
		// wordC.setText(list.get(index[2]));
		// }
		// }

		if (lockWord.size() == 0) {
			wordA.setText(list.get(index[0]));
			wordB.setText(list.get(index[1]));
			wordC.setText(list.get(index[2]));
		} else if (lockWord.size() == 1) {
			wordA.setText(lockWord.get(0));

			if (lockWord.get(0).equals(list.get(index[1]))) {
				wordB.setText(list.get(index[0]));
			} else {
				wordB.setText(list.get(index[1]));
			}

			if (lockWord.get(0).equals(list.get(index[2]))) {
				wordC.setText(list.get(index[0]));
			} else {
				wordC.setText(list.get(index[2]));
			}
		} else if (lockWord.size() == 2) {
			wordA.setText(lockWord.get(0));

			wordB.setText(lockWord.get(1));

			if (!lockWord.get(0).equals(list.get(index[2]))
					&& !lockWord.get(1).equals(list.get(index[2]))) {
				wordC.setText(list.get(index[2]));
			} else if (lockWord.get(0).equals(list.get(index[2]))) {
				if (!lockWord.get(0).equals(list.get(index[0]))
						&& !lockWord.get(1).equals(list.get(index[0]))) {
					wordC.setText(list.get(index[0]));
				} else {
					wordC.setText(list.get(index[1]));
				}
			} else {
				if (!lockWord.get(1).equals(list.get(index[0]))
						&& !lockWord.get(0).equals(list.get(index[0]))) {
					wordC.setText(list.get(index[0]));
				} else {
					wordC.setText(list.get(index[1]));
				}
			}
		}

		layout.addView(cloudView, p);

		if (type) {
			transAnimator.start();
		}
	}

	public void remove() {
		transAnimator.cancel();
	}

	public void wordSet(final Context context) {

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

		SelectWord SelectWord = new SelectWord();
		index = SelectWord.select(list);

		cloudButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				String wordAString = wordA.getText().toString();
				String wordBString = wordB.getText().toString();
				String wordCString = wordC.getText().toString();

				try {

					DBHelper helper = new DBHelper(context, "cloud_db.db",
							null, 1);
					SQLiteDatabase db;
					db = helper.getWritableDatabase();

					// insertで行に追加
					db.execSQL("insert into ideas(keywordA,keywordB,keywordC) values ('"
							+ wordAString
							+ "','"
							+ wordBString
							+ "','"
							+ wordCString + "');");

					// 終わったら閉じる。これがないとエラーとなる。データベースも。
					db.close();

					// alphaプロパティを0fから1fに変化させます
					// ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(
					// cloudView, "alpha", 1f, 0f);

					// translationXプロパティを0fからtoXに変化させます
					PropertyValuesHolder holderX;

					Log.d("er", String.valueOf(cloudView.getX()));

					if (type) {
						holderX = PropertyValuesHolder.ofFloat("translationX",
								y / 2 - 20);
					} else {
						if (leftRight) {
							holderX = PropertyValuesHolder.ofFloat(
									"translationX", y / 2 - 20);
						} else {
							holderX = PropertyValuesHolder.ofFloat(
									"translationX", 0 - 20);
						}
					}

					// translationYプロパティを0fからtoYに変化させます
					PropertyValuesHolder holderY = PropertyValuesHolder
							.ofFloat("translationY", cloudView.getY() * -1
									- 300);

					// translationXプロパティを0fからtoXに変化させます
					PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(
							"scaleX", 0.2f);
					// translationYプロパティを0fからtoYに変化させます
					PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(
							"scaleY", 0.2f);

					// targetに対してholderX, holderY, holderRotationを同時に実行させます
					ObjectAnimator objectAnimator = ObjectAnimator
							.ofPropertyValuesHolder(cloudView, holderX,
									holderY, scaleX, scaleY);

					objectAnimator.addListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							// Write processing after animating
							layout.removeView(cloudView);
							if (!type) {
								createThreeCloud(context, layout, top, type,
										leftRight, x, y);
							}
						}

						@Override
						public void onAnimationCancel(Animator animation) {
							// TODO 自動生成されたメソッド・スタブ
							super.onAnimationCancel(animation);
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
	}

	public void lockWord(Context context) {

		lockWord = new ArrayList<String>();

		DBHelper helper = new DBHelper(context, "cloud_db.db", null, 1);
		// データベースの設定
		SQLiteDatabase db;
		db = helper.getWritableDatabase();

		try {

			String countSql = "select count(*) from lock2";

			c = db.rawQuery(countSql, null);
			c.moveToLast();

			long count = c.getLong(0);
			c.close();
			if (count != 0) {
				String lockSql = "select keyword from lock2";
				c = db.rawQuery(lockSql, null);

				boolean isEof = c.moveToFirst();
				// カーソルを先頭に移動
				while (isEof) {
					// while文。カーソルが最後に行くまで繰り返す。
					lockWord.add(c.getString(0));
					// getString(0)メソッドで、カーソルの一行目を追加。2,3も同じ。
					isEof = c.moveToNext();
				}
				// 次のリストにカーソルを移す。
				c.close();

			}
		} catch (SQLException ex) {
			Log.e("error", "データベースエラー");
			Log.e("exception", ex.getMessage());
		}

		db.close();
	}

	public ThreeCloudView get() {
		return this.cloudView;

	}

	public void changeView(long symbol) {

		layout.removeView(cloudView);
		createThreeCloud(context, layout, top, type, leftRight, x, y);

	}

	public void changeView() {

		layout.removeView(cloudView);
		createThreeCloud(context, layout, top, type, leftRight, x, y);

	}

	public void removeView() {

		layout.removeView(cloudView);

	}

}
