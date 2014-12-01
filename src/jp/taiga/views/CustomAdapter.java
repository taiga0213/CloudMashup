package jp.taiga.views;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
		
		if(position==lockPosition){
			v.setBackgroundColor(Color.argb(200, 250, 219, 218));
		}else{
			v.setBackgroundColor(Color.argb(0, 0, 0, 0));
		}
		
		return v;
	}

}
