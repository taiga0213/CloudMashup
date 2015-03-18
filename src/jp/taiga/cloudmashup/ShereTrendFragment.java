package jp.taiga.cloudmashup;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ShereTrendFragment extends Fragment {

	ListView listView;
	ArrayAdapter<String> adapter;
	public static final int MENU_BACK = 0;
	public static final int MENU_RESUME = 1;
	Cursor cursor;
	String[] trend;
	SQLiteDatabase db;
	int clickPosition;
	String item;
	PopupWindow mPopupWindow;
	LinearLayout layout;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_trend, container,
				false);

		layout = (LinearLayout) rootView.findViewById(R.id.LinearLayout1);

		listView = (ListView) rootView.findViewById(R.id.listView);

		// adapter = new ArrayAdapter<String>(getActivity(),
		// android.R.layout.simple_list_item_1);

		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1);

		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自動生成されたメソッド・スタブ

				item = (String) listView.getItemAtPosition(position);
				trend = item.split(":");

				clickPosition = position;

				new AlertDialog.Builder(getActivity())
						.setTitle("このワードを追加しますか?")
						.setMessage("【　" + trend[1] + "　】")
						.setPositiveButton("Yes", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO 自動生成されたメソッド・スタブ
								try {
									DBHelper helper = new DBHelper(
											getActivity(), "cloud_db.db", null,
											1);
									db = helper.getWritableDatabase();

									// insertで行に追加
									db.execSQL("insert into keywords(keyword) values ('"
											+ trend[1] + "');");
									adapter.remove(item);
									Toast.makeText(getActivity(), "追加されました",
											Toast.LENGTH_SHORT).show();
								} catch (SQLiteConstraintException e) {
									Log.e("error", "制約違反(重複登録)");
									Toast.makeText(getActivity(),
											"すでに登録された単語です", Toast.LENGTH_SHORT)
											.show();
								} catch (SQLException e) {
									Log.e("error", "データベースエラー");
									e.printStackTrace();
								}

							}
						}).setNegativeButton("No", null).show();
			}
		});

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		Shere shere = new Shere();

		if (pref.getString("GetResponse", null) == null) {
			shere.wordGet(getActivity(), layout, adapter);
		}

		if (pref.getString("GetResponse", null) != null) {
			shere.trendSet(getActivity(), layout, adapter);
		}

		return rootView;
	}

}