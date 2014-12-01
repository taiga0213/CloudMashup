package jp.taiga.cloudmashup;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WordViewActivity extends Activity implements OnClickListener{
	
	public static final int MENU_BACK = 0;
	LinearLayout slideLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_word_view);
		Intent intent = getIntent();
		String wordA = intent.getStringExtra("wordA");
		String wordB = intent.getStringExtra("wordB");
		
		TextView getWordA = (TextView)findViewById(R.id.getWordA);
		TextView getWordB = (TextView)findViewById(R.id.getWordB);
	
		getWordA.setText(wordA);
		getWordB.setText(wordB);
		
		slideLayout = (LinearLayout)findViewById(R.id.slideLayout);
		
		Button back = (Button)findViewById(R.id.back);
		Button save = (Button)findViewById(R.id.save);
		
		back.setOnClickListener(this);
		save.setOnClickListener(this);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.word_view, menu);

		menu.add(0, MENU_BACK, 0, "BACK")
				.setIcon(android.R.drawable.ic_menu_revert)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_BACK:
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			return true;

		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO 自動生成されたメソッド・スタブ
		Intent intent = new Intent(this, MainActivity.class);
		switch (v.getId()) {
		case R.id.back:
			startActivity(intent);
			break;

		case R.id.save:
			startActivity(intent);
			break;
		}
		
	}

}
