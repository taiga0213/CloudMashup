package jp.taiga.cloudmashup;

import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class HowToUseActivity extends Activity implements OnClickListener {

	ViewFlipper viewFlipper;

	Animation rightInAnimation;
	Animation leftInAnimation;
	Animation rightOutAnimation;
	Animation leftOutAnimation;

	FrameLayout layout;

	private final int WC = 250;
	private final int PREV = 1;
	private final int NEXT = 2;
	private final int PAGE_STERT = 1;
	private final int PAGE_END = 3;
	
	int page = 1;
	
	ImageButton nextButton;
	ImageButton prevButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_how_to_use);

		viewFlipper = (ViewFlipper) findViewById(R.id.flipper);

		rightInAnimation = AnimationUtils.loadAnimation(this, R.anim.right_in);
		rightOutAnimation = AnimationUtils
				.loadAnimation(this, R.anim.right_out);
		leftInAnimation = AnimationUtils.loadAnimation(this, R.anim.left_in);
		leftOutAnimation = AnimationUtils.loadAnimation(this, R.anim.left_out);
		
		prevButton = (ImageButton)findViewById(R.id.prev);
		nextButton = (ImageButton)findViewById(R.id.next);
		
		prevButton.setOnClickListener(this);
		nextButton.setOnClickListener(this);
		
		addButton();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.how_to_use, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO 自動生成されたメソッド・スタブ
		switch (v.getId()) {
		case R.id.next:
			viewFlipper.setInAnimation(rightInAnimation);
			viewFlipper.setOutAnimation(leftOutAnimation);
			viewFlipper.showNext();
			page++;
			addButton();
			break;
		case R.id.prev:
			viewFlipper.setInAnimation(leftInAnimation);
			viewFlipper.setOutAnimation(rightOutAnimation);
			viewFlipper.showPrevious();
			page--;
			addButton();
			break;
		}
	}

	private void addButton() {
		prevButton.setEnabled(false);
		nextButton.setEnabled(false);
		if(page == PAGE_STERT){
			nextButton();
		}else if(page == PAGE_END) {
			prevButton();
		}else{
			prevButton();
			nextButton();
		}
	}

	private void prevButton() {

//		ImageView prev = new ImageView(this);
//		prev.setImageResource(android.R.drawable.ic_media_previous);
//		prev.setId(PREV);
//		prev.setOnClickListener(this);
//		FrameLayout.LayoutParams prevParams = new FrameLayout.LayoutParams(WC,
//				WC, Gravity.CENTER | Gravity.LEFT);
//
//		layout.addView(prev, prevParams);
		prevButton.setEnabled(true);

	}

	private void nextButton() {
//		ImageView next = new ImageView(this);
//		next.setImageResource(android.R.drawable.ic_media_next);
//		next.setId(NEXT);
//		next.setOnClickListener(this);
//
//		FrameLayout.LayoutParams nextParams = new FrameLayout.LayoutParams(WC,
//				WC, Gravity.CENTER | Gravity.RIGHT);
//
//		layout.addView(next, nextParams);
		nextButton.setEnabled(true);
		
	}

}
