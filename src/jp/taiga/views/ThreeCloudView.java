package jp.taiga.views;

import jp.taiga.cloudmashup.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ThreeCloudView extends FrameLayout{
	
	public ThreeCloudView(Context context) {
		super(context);
		View layout = LayoutInflater.from(context).inflate(R.layout.cloud_three, this);
		TextView wordA = (TextView)findViewById(R.id.wordGetA);
		TextView wordB = (TextView)findViewById(R.id.wordGetB);
		TextView wordC = (TextView)findViewById(R.id.wordGetC);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public ThreeCloudView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO 自動生成されたコンストラクター・スタブ
		View layout = LayoutInflater.from(context).inflate(R.layout.cloud_three, this);
		TextView wordA = (TextView)findViewById(R.id.wordGetA);
		TextView wordB = (TextView)findViewById(R.id.wordGetB);
		TextView wordC = (TextView)findViewById(R.id.wordGetC);
	}

	public ThreeCloudView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自動生成されたコンストラクター・スタブ
		View layout = LayoutInflater.from(context).inflate(R.layout.cloud_three, this);
		TextView wordA = (TextView)findViewById(R.id.wordGetA);
		TextView wordB = (TextView)findViewById(R.id.wordGetB);
		TextView wordC = (TextView)findViewById(R.id.wordGetC);
	}
	
	

}
