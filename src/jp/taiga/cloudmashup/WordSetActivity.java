package jp.taiga.cloudmashup;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import jp.taiga.beans.LockBean;
import jp.taiga.views.CustomAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class WordSetActivity extends Activity implements OnClickListener {

	// ArrayAdapter<String> adapter;
	CustomAdapter<String> adapter;
	public static final int MENU_BACK = 0;
	public static final int MENU_WORD_SHARE = 1;
	ListView listView;
	String item;
	DBHelper helper;
	// データベースの設定
	SQLiteDatabase db;
	Cursor cc;
	Cursor c;
	String nowLockWord;
	int lockposition;
	int clickPosition;
	EditText addWord;
	LinearLayout layout;
	ProgressDialog dialog;

	String serverIP = "192.168.43.94";// 実家

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_word_set);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		layout = (LinearLayout) findViewById(R.id.LinearLayout1);

		adapter = new CustomAdapter<String>(this, android.R.layout.simple_list_item_1);

		addWord = (EditText) findViewById(R.id.add_word);
		addWord.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& keyCode == KeyEvent.KEYCODE_ENTER) {
					add();

					return true;
				}
				return false;
			}
		});

		listView = (ListView) findViewById(R.id.ListView1);
		
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		String wordNum = pref.getString("wordNum", "2");
		
		if(wordNum.equals("2")){
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO 自動生成されたメソッド・スタブ
					// クリックされたアイテムを取得します
					item = (String) listView.getItemAtPosition(position);
					clickPosition = position;

					new AlertDialog.Builder(WordSetActivity.this)
							.setTitle("このワードを削除しますか?")
							.setMessage("【　" + item + "　】")
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											try {
												DBHelper helper = new DBHelper(
														WordSetActivity.this,
														"cloud_db.db", null, 1);
												db = helper.getWritableDatabase();

												String countSql = "select count(*) from keywords";

												cc = db.rawQuery(countSql, null);
												cc.moveToLast();

												long count = cc.getLong(0);
												cc.close();

												if (count > 3) {
													String deleteSql = "delete from keywords where keyword = '"
															+ item + "';";
													db.execSQL(deleteSql);

													String lockCountSql = "select count(*) from lock";

													cc = db.rawQuery(lockCountSql,
															null);
													cc.moveToLast();

													long lockCount = cc.getLong(0);
													cc.close();
													if (lockCount != 0) {
														String lockSql = "select keyword from lock";
														cc = db.rawQuery(lockSql,
																null);
														cc.moveToLast();
														if (cc.getString(0).equals(
																item)) {
															db.execSQL("delete from lock where keyword = '"+item+"';");
//															adapter.setPosition(-1);
														}
														cc.close();
													} 
													
//													else if (clickPosition < adapter
//															.getPosition()) {
//														adapter.setPosition(adapter
//																.getPosition() - 1);
//													}

													adapter.clear();

													// カーソルの設定
													String[] cols = { "keyword" };
													c = db.query("keywords", cols,
															null, null, null, null,
															"keyword asc", null);
													// カーソルのリストを作る。１：テーブル名、２：取得する列名（カラム等）の配列、
													// ３＆４：取得するレコードの条件、５：GROUP
													// BY条件、６：「HAVING」条件、
													// ７：「ORDER BY」条件、８：「limit」条件
													boolean isEof = c.moveToFirst();
													lockposition = 0;
													// カーソルを先頭に移動
													while (isEof) {
														// while文。カーソルが最後に行くまで繰り返す。
														adapter.add(c.getString(0));
														if (c.getString(0).equals(
																nowLockWord)) {
															adapter.setPosition(lockposition);
														}
														lockposition++;
														// getString(0)メソッドで、カーソルの一行目を追加。2,3も同じ。
														isEof = c.moveToNext();
													}
													// 次のリストにカーソルを移す。
													c.close();

													db.close();
												} else {
													Toast.makeText(
															WordSetActivity.this,
															"単語を3つ以下にできません",
															Toast.LENGTH_SHORT)
															.show();
												}

											} catch (Exception e) {
												e.printStackTrace();
											}

										}
									}).setNegativeButton("No", null).show();

				}

			});

			listView.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO 自動生成されたメソッド・スタブ
					item = (String) listView.getItemAtPosition(position);
					try {
						// 現在の時刻を取得
						Date date = new Date();
						// 表示形式を設定
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy'-'MM'-'dd kk':'mm':'ss");

						DBHelper helper = new DBHelper(WordSetActivity.this,
								"cloud_db.db", null, 1);
						db = helper.getWritableDatabase();

						String countSql = "select count(*) from lock";

						cc = db.rawQuery(countSql, null);
						cc.moveToLast();

						long count = cc.getLong(0);
						cc.close();

						ArrayList<LockBean> lockList = new ArrayList<LockBean>();

						String lockWordSql = "select * from lock";

						cc = db.rawQuery(lockWordSql, null);

						boolean match = false;
						String matchWord = "";

						boolean isEof = cc.moveToFirst();
						// カーソルを先頭に移動
						while (isEof) {
							LockBean bean = new LockBean();
							bean.setKeyword(cc.getString(0));
							bean.setDate(sdf.parse(cc.getString(1)));
							lockList.add(bean);

							if (cc.getString(0).equals(item)) {
								match = true;
								matchWord = cc.getString(0);
							}

							isEof = cc.moveToNext();
						}
						// 次のリストにカーソルを移す。
						cc.close();

						
						if (match) {

							String deleteSql = "delete from lock where keyword = '"
									+ matchWord + "';";
							db.execSQL(deleteSql);
							adapter.clear();
//							adapter.setPosition(-1);
							// カーソルの設定
							String[] cols = { "keyword" };
							c = db.query("keywords", cols, null, null, null, null,
									"keyword asc", null);
							// カーソルのリストを作る。１：テーブル名、２：取得する列名（カラム等）の配列、
							// ３＆４：取得するレコードの条件、５：GROUP BY条件、６：「HAVING」条件、
							// ７：「ORDER BY」条件、８：「limit」条件
							isEof = c.moveToFirst();
							// カーソルを先頭に移動
							while (isEof) {
								// while文。カーソルが最後に行くまで繰り返す。
								adapter.add(c.getString(0));
								// getString(0)メソッドで、カーソルの一行目を追加。2,3も同じ。
								isEof = c.moveToNext();
							}
							// 次のリストにカーソルを移す。
							c.close();

							Toast.makeText(getBaseContext(), "ロックを解除しました",
									Toast.LENGTH_SHORT).show();

						} else if(count == 1) {
							LockBean deleteBean = new LockBean();

							for (LockBean lockBean : lockList) {
								if (deleteBean.getDate() == null
										|| deleteBean.getDate().after(
												lockBean.getDate())) {
									deleteBean = lockBean;
								}
							}
							
							String deleteSql = "delete from lock where keyword = '"+deleteBean.getKeyword()+"';";
							db.execSQL(deleteSql);
							db.execSQL("insert into lock(keyword,date) values ('"
									+ item + "','" + sdf.format(date) + "');");

							Toast.makeText(getBaseContext(), "キーワードをロックしました",
									Toast.LENGTH_SHORT).show();
						} else{
							db.execSQL("insert into lock(keyword,date) values ('"
									+ item + "','" + sdf.format(date) + "');");

							Toast.makeText(getBaseContext(), "キーワードをロックしました",
									Toast.LENGTH_SHORT).show();
						}

						String lockSql = "select keyword from lock";
						cc = db.rawQuery(lockSql, null);
						cc.moveToLast();
						String lockWord = cc.getString(0);
						nowLockWord = lockWord;
						cc.close();

						adapter.clear();

						// カーソルの設定
						String[] cols = { "keyword" };
						c = db.query("keywords", cols, null, null, null, null,
								"keyword asc", null);
						// カーソルのリストを作る。１：テーブル名、２：取得する列名（カラム等）の配列、
						// ３＆４：取得するレコードの条件、５：GROUP BY条件、６：「HAVING」条件、
						// ７：「ORDER BY」条件、８：「limit」条件
						isEof = c.moveToFirst();
						lockposition = 0;
						// カーソルを先頭に移動
						while (isEof) {
							// while文。カーソルが最後に行くまで繰り返す。
							adapter.add(c.getString(0));
							if (c.getString(0).equals(nowLockWord)) {
								adapter.setPosition(lockposition);
							}
							lockposition++;
							// getString(0)メソッドで、カーソルの一行目を追加。2,3も同じ。
							isEof = c.moveToNext();
						}
						// 次のリストにカーソルを移す。
						c.close();
						db.close();

					} catch (Exception e) {
						e.printStackTrace();
					}
					return true;
				}

			});

			listView.setAdapter(adapter);

			ImageButton addButton = (ImageButton) findViewById(R.id.add_button);
			addButton.setOnClickListener(this);

			try {
				DBHelper helper = new DBHelper(WordSetActivity.this, "cloud_db.db",
						null, 1);
				db = helper.getWritableDatabase();
//
//				String countSql = "select count(*) from lock";
//
//				cc = db.rawQuery(countSql, null);
//				cc.moveToLast();
//
//				long count = cc.getLong(0);
//				cc.close();
//				if (count != 0) {
//					String lockSql = "select keyword from lock";
//					cc = db.rawQuery(lockSql, null);
//					cc.moveToLast();
//					String lockWord = cc.getString(0);
//					nowLockWord = lockWord;
//
//					cc.close();
//
//				} else {
//					adapter.setPosition(-1);
//				}
//
				// カーソルの設定
				String[] cols = { "keyword" };
				Cursor c = db.query("keywords", cols, null, null, null, null,
						"keyword asc", null);
				// カーソルのリストを作る。１：テーブル名、２：取得する列名（カラム等）の配列、
				// ３＆４：取得するレコードの条件、５：GROUP BY条件、６：「HAVING」条件、
				// ７：「ORDER BY」条件、８：「limit」条件
				boolean isEof = c.moveToFirst();
//				lockposition = 0;
//				// カーソルを先頭に移動
				while (isEof) {
//					// while文。カーソルが最後に行くまで繰り返す。
					adapter.add(c.getString(0));
//					if (c.getString(0).equals(nowLockWord)) {
//						adapter.setPosition(lockposition);
//					}
//					lockposition++;
//					// getString(0)メソッドで、カーソルの一行目を追加。2,3も同じ。
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
			
		}else{
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO 自動生成されたメソッド・スタブ
					// クリックされたアイテムを取得します
					item = (String) listView.getItemAtPosition(position);
					clickPosition = position;

					new AlertDialog.Builder(WordSetActivity.this)
							.setTitle("このワードを削除しますか?")
							.setMessage("【　" + item + "　】")
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											try {
												DBHelper helper = new DBHelper(
														WordSetActivity.this,
														"cloud_db.db", null, 1);
												db = helper.getWritableDatabase();

												String countSql = "select count(*) from keywords";

												cc = db.rawQuery(countSql, null);
												cc.moveToLast();

												long count = cc.getLong(0);
												cc.close();

												if (count > 3) {
													String deleteSql = "delete from keywords where keyword = '"
															+ item + "';";
													db.execSQL(deleteSql);

													String lockCountSql = "select count(*) from lock2";

													cc = db.rawQuery(lockCountSql,
															null);
													cc.moveToLast();

													long lockCount = cc.getLong(0);
													cc.close();
													if (lockCount != 0) {
														String lockSql = "select keyword from lock2";
														cc = db.rawQuery(lockSql,
																null);
														cc.moveToLast();
														if (cc.getString(0).equals(
																item)) {
															db.execSQL("delete from lock2 where keyword = '"+item+"';");
//															adapter.setPosition(-1);
														}
														cc.close();
													} 
													
//													else if (clickPosition < adapter
//															.getPosition()) {
//														adapter.setPosition(adapter
//																.getPosition() - 1);
//													}

													adapter.clear();

													// カーソルの設定
													String[] cols = { "keyword" };
													c = db.query("keywords", cols,
															null, null, null, null,
															"keyword asc", null);
													// カーソルのリストを作る。１：テーブル名、２：取得する列名（カラム等）の配列、
													// ３＆４：取得するレコードの条件、５：GROUP
													// BY条件、６：「HAVING」条件、
													// ７：「ORDER BY」条件、８：「limit」条件
													boolean isEof = c.moveToFirst();
													lockposition = 0;
													// カーソルを先頭に移動
													while (isEof) {
														// while文。カーソルが最後に行くまで繰り返す。
														adapter.add(c.getString(0));
														if (c.getString(0).equals(
																nowLockWord)) {
															adapter.setPosition(lockposition);
														}
														lockposition++;
														// getString(0)メソッドで、カーソルの一行目を追加。2,3も同じ。
														isEof = c.moveToNext();
													}
													// 次のリストにカーソルを移す。
													c.close();

													db.close();
												} else {
													Toast.makeText(
															WordSetActivity.this,
															"単語を3つ以下にできません",
															Toast.LENGTH_SHORT)
															.show();
												}

											} catch (Exception e) {
												e.printStackTrace();
											}

										}
									}).setNegativeButton("No", null).show();

				}

			});

			listView.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO 自動生成されたメソッド・スタブ
					item = (String) listView.getItemAtPosition(position);
					try {
						// 現在の時刻を取得
						Date date = new Date();
						// 表示形式を設定
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy'-'MM'-'dd kk':'mm':'ss");

						DBHelper helper = new DBHelper(WordSetActivity.this,
								"cloud_db.db", null, 1);
						db = helper.getWritableDatabase();

						String countSql = "select count(*) from lock2";

						cc = db.rawQuery(countSql, null);
						cc.moveToLast();

						long count = cc.getLong(0);
						cc.close();

						ArrayList<LockBean> lockList = new ArrayList<LockBean>();

						String lockWordSql = "select * from lock2";

						cc = db.rawQuery(lockWordSql, null);

						boolean match = false;
						String matchWord = "";

						boolean isEof = cc.moveToFirst();
						// カーソルを先頭に移動
						while (isEof) {
							LockBean bean = new LockBean();
							bean.setKeyword(cc.getString(0));
							bean.setDate(sdf.parse(cc.getString(1)));
							lockList.add(bean);

							if (cc.getString(0).equals(item)) {
								match = true;
								matchWord = cc.getString(0);
							}

							isEof = cc.moveToNext();
						}
						// 次のリストにカーソルを移す。
						cc.close();

						
						if (match) {

							String deleteSql = "delete from lock2 where keyword = '"
									+ matchWord + "';";
							db.execSQL(deleteSql);
							adapter.clear();
//							adapter.setPosition(-1);
							// カーソルの設定
							String[] cols = { "keyword" };
							c = db.query("keywords", cols, null, null, null, null,
									"keyword asc", null);
							// カーソルのリストを作る。１：テーブル名、２：取得する列名（カラム等）の配列、
							// ３＆４：取得するレコードの条件、５：GROUP BY条件、６：「HAVING」条件、
							// ７：「ORDER BY」条件、８：「limit」条件
							isEof = c.moveToFirst();
							// カーソルを先頭に移動
							while (isEof) {
								// while文。カーソルが最後に行くまで繰り返す。
								adapter.add(c.getString(0));
								// getString(0)メソッドで、カーソルの一行目を追加。2,3も同じ。
								isEof = c.moveToNext();
							}
							// 次のリストにカーソルを移す。
							c.close();

							Toast.makeText(getBaseContext(), "ロックを解除しました",
									Toast.LENGTH_SHORT).show();

						} else if(count == 2) {
							LockBean deleteBean = new LockBean();

							for (LockBean lockBean : lockList) {
								if (deleteBean.getDate() == null
										|| deleteBean.getDate().after(
												lockBean.getDate())) {
									deleteBean = lockBean;
								}
							}
							
							String deleteSql = "delete from lock2 where keyword = '"+deleteBean.getKeyword()+"';";
							db.execSQL(deleteSql);
							db.execSQL("insert into lock2(keyword,date) values ('"
									+ item + "','" + sdf.format(date) + "');");

							Toast.makeText(getBaseContext(), "キーワードをロックしました",
									Toast.LENGTH_SHORT).show();
						} else{
							db.execSQL("insert into lock2(keyword,date) values ('"
									+ item + "','" + sdf.format(date) + "');");

							Toast.makeText(getBaseContext(), "キーワードをロックしました",
									Toast.LENGTH_SHORT).show();
						}

						String lockSql = "select keyword from lock2";
						cc = db.rawQuery(lockSql, null);
						cc.moveToLast();
						String lockWord = cc.getString(0);
						nowLockWord = lockWord;
						cc.close();

						adapter.clear();

						// カーソルの設定
						String[] cols = { "keyword" };
						c = db.query("keywords", cols, null, null, null, null,
								"keyword asc", null);
						// カーソルのリストを作る。１：テーブル名、２：取得する列名（カラム等）の配列、
						// ３＆４：取得するレコードの条件、５：GROUP BY条件、６：「HAVING」条件、
						// ７：「ORDER BY」条件、８：「limit」条件
						isEof = c.moveToFirst();
						lockposition = 0;
						// カーソルを先頭に移動
						while (isEof) {
							// while文。カーソルが最後に行くまで繰り返す。
							adapter.add(c.getString(0));
							if (c.getString(0).equals(nowLockWord)) {
								adapter.setPosition(lockposition);
							}
							lockposition++;
							// getString(0)メソッドで、カーソルの一行目を追加。2,3も同じ。
							isEof = c.moveToNext();
						}
						// 次のリストにカーソルを移す。
						c.close();
						db.close();

					} catch (Exception e) {
						e.printStackTrace();
					}
					return true;
				}

			});

			listView.setAdapter(adapter);

			ImageButton addButton = (ImageButton) findViewById(R.id.add_button);
			addButton.setOnClickListener(this);

			try {
				DBHelper helper = new DBHelper(WordSetActivity.this, "cloud_db.db",
						null, 1);
				db = helper.getWritableDatabase();
//
//				String countSql = "select count(*) from lock";
//
//				cc = db.rawQuery(countSql, null);
//				cc.moveToLast();
//
//				long count = cc.getLong(0);
//				cc.close();
//				if (count != 0) {
//					String lockSql = "select keyword from lock";
//					cc = db.rawQuery(lockSql, null);
//					cc.moveToLast();
//					String lockWord = cc.getString(0);
//					nowLockWord = lockWord;
//
//					cc.close();
//
//				} else {
//					adapter.setPosition(-1);
//				}
//
				// カーソルの設定
				String[] cols = { "keyword" };
				Cursor c = db.query("keywords", cols, null, null, null, null,
						"keyword asc", null);
				// カーソルのリストを作る。１：テーブル名、２：取得する列名（カラム等）の配列、
				// ３＆４：取得するレコードの条件、５：GROUP BY条件、６：「HAVING」条件、
				// ７：「ORDER BY」条件、８：「limit」条件
				boolean isEof = c.moveToFirst();
//				lockposition = 0;
//				// カーソルを先頭に移動
				while (isEof) {
//					// while文。カーソルが最後に行くまで繰り返す。
					adapter.add(c.getString(0));
//					if (c.getString(0).equals(nowLockWord)) {
//						adapter.setPosition(lockposition);
//					}
//					lockposition++;
//					// getString(0)メソッドで、カーソルの一行目を追加。2,3も同じ。
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
		}
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.word_set, menu);
		menu.add(0, MENU_WORD_SHARE, 0, "SHARE")
				.setIcon(android.R.drawable.ic_menu_share)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(0, MENU_BACK, 0, "BACK")
				.setIcon(android.R.drawable.ic_menu_revert)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_BACK:
			// Intent intent = new Intent(this, MainActivity.class);
			// startActivity(intent);
			finish();// カーソルを次に進める
			return true;
		case MENU_WORD_SHARE:
			dialog = new ProgressDialog(this);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setMessage("通信中です...");
			dialog.setCancelable(true);
			dialog.show();

			(new Thread(runnable)).start();
			return true;

		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO 自動生成されたメソッド・スタブ
		// ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
		// .hideSoftInputFromWindow(v.getWindowToken(), 0);
		add();

	}

	private void add() {
		String addText = addWord.getText().toString();
		if (addText != null && addText != "" && !addText.equals("")
				&& addText.length() <= 10) {
			try {

				DBHelper helper = new DBHelper(WordSetActivity.this,
						"cloud_db.db", null, 1);
				db = helper.getWritableDatabase();

				// insertで行に追加
				db.execSQL("insert into keywords(keyword) values ('" + addText
						+ "');");
				// EditTextの初期化
				addWord.setText("");

				adapter.clear();

				String countSql = "select count(*) from lock";

				cc = db.rawQuery(countSql, null);
				cc.moveToLast();

				long count = cc.getLong(0);
				cc.close();
				if (count != 0) {
					String lockSql = "select keyword from lock";
					cc = db.rawQuery(lockSql, null);
					cc.moveToLast();
					String lockWord = cc.getString(0);
					nowLockWord = lockWord;

					cc.close();

				} else {
					adapter.setPosition(-1);
				}

				// カーソルの設定
				String[] cols = { "keyword" };
				c = db.query("keywords", cols, null, null, null, null,
						"keyword asc", null);
				// カーソルのリストを作る。１：テーブル名、２：取得する列名（カラム等）の配列、
				// ３＆４：取得するレコードの条件、５：GROUP BY条件、６：「HAVING」条件、
				// ７：「ORDER BY」条件、８：「limit」条件
				boolean isEof = c.moveToFirst();
				lockposition = 0;
				// カーソルを先頭に移動
				while (isEof) {
					// while文。カーソルが最後に行くまで繰り返す。
					adapter.add(c.getString(0));
					if (c.getString(0).equals(nowLockWord)) {
						adapter.setPosition(lockposition);
					}
					lockposition++;
					// getString(0)メソッドで、カーソルの一行目を追加。2,3も同じ。
					isEof = c.moveToNext();
				}
				// 次のリストにカーソルを移す。
				c.close();
				// 終わったら閉じる。これがないとエラーとなる。データベースも。
				db.close();
			} catch (SQLiteConstraintException e) {
				Log.e("error", "制約違反(重複登録)");
				Toast.makeText(this, "すでに登録された単語です", Toast.LENGTH_SHORT).show();
			} catch (SQLException e) {
				Log.e("error", "データベースエラー");
				e.printStackTrace();
			}
		} else {
			if (addText.length() > 10) {
				Log.e("error", "文字オーバー");
				Toast.makeText(this, "10文字以下で入力してください", Toast.LENGTH_SHORT)
						.show();
			} else {
				Log.e("error", "未入力");
				Toast.makeText(this, "キーワードを入力してください", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	@Override
	protected void onRestart() {
		// TODO 自動生成されたメソッド・スタブ
		super.onRestart();
		try {

			DBHelper helper = new DBHelper(WordSetActivity.this, "cloud_db.db",
					null, 1);
			db = helper.getWritableDatabase();

			adapter.clear();

			String countSql = "select count(*) from lock";

			cc = db.rawQuery(countSql, null);
			cc.moveToLast();

			long count = cc.getLong(0);
			cc.close();
			if (count != 0) {
				String lockSql = "select keyword from lock";
				cc = db.rawQuery(lockSql, null);
				cc.moveToLast();
				String lockWord = cc.getString(0);
				nowLockWord = lockWord;

				cc.close();

			} else {
				adapter.setPosition(-1);
			}

			// カーソルの設定
			String[] cols = { "keyword" };
			c = db.query("keywords", cols, null, null, null, null,
					"keyword asc", null);
			// カーソルのリストを作る。１：テーブル名、２：取得する列名（カラム等）の配列、
			// ３＆４：取得するレコードの条件、５：GROUP BY条件、６：「HAVING」条件、
			// ７：「ORDER BY」条件、８：「limit」条件
			boolean isEof = c.moveToFirst();
			lockposition = 0;
			// カーソルを先頭に移動
			while (isEof) {
				// while文。カーソルが最後に行くまで繰り返す。
				adapter.add(c.getString(0));
				if (c.getString(0).equals(nowLockWord)) {
					adapter.setPosition(lockposition);
				}
				lockposition++;
				// getString(0)メソッドで、カーソルの一行目を追加。2,3も同じ。
				isEof = c.moveToNext();
			}
			// 次のリストにカーソルを移す。
			c.close();
			// 終わったら閉じる。これがないとエラーとなる。データベースも。
			db.close();
		} catch (SQLException e) {
			Log.e("error", "データベースエラー");
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		pref.edit().remove("GetResponse").commit();
		super.onDestroy();
	}

	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://" + serverIP
					+ ":8080/CloudMashupServer/CloudMashup");

			HttpParams httpParams = httpClient.getParams();

			HttpConnectionParams.setConnectionTimeout(httpParams, 10 * 1000);
			HttpConnectionParams.setSoTimeout(httpParams, 30 * 1000);

			HttpResponse httpResponse = null;
			try {
				httpResponse = httpClient.execute(httppost);
			} catch (ClientProtocolException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			if (httpResponse == null) {

				Message message = new Message();
				Bundle bundle = new Bundle();
				bundle.putString("Mes", "接続できませんでした。");
				message.setData(bundle);
				handler.sendMessage(message);

			} else {
				Intent intent = new Intent(WordSetActivity.this,
						ShareActivity.class);
				startActivity(intent);
			}

			dialog.dismiss();

		}
	};

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String text = msg.getData().get("Mes").toString();
			Toast.makeText(WordSetActivity.this, text, Toast.LENGTH_SHORT)
					.show();
		}
	};

}
