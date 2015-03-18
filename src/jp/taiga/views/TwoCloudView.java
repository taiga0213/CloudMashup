package jp.taiga.views;

import jp.taiga.cloudmashup.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TwoCloudView extends FrameLayout{
	
	public TwoCloudView(Context context) {
		super(context);
		View layout = LayoutInflater.from(context).inflate(R.layout.cloud_two, this);
		TextView wordA = (TextView)findViewById(R.id.getWordA);
		TextView wordB = (TextView)findViewById(R.id.getWordB);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public TwoCloudView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO 自動生成されたコンストラクター・スタブ
		View layout = LayoutInflater.from(context).inflate(R.layout.cloud_two, this);
		TextView wordA = (TextView)findViewById(R.id.getWordA);
		TextView wordB = (TextView)findViewById(R.id.getWordB);
	}

	public TwoCloudView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自動生成されたコンストラクター・スタブ
		View layout = LayoutInflater.from(context).inflate(R.layout.cloud_two, this);
		TextView wordA = (TextView)findViewById(R.id.getWordA);
		TextView wordB = (TextView)findViewById(R.id.getWordB);
	}
	
	

}
