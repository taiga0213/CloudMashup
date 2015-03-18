package jp.taiga.cloudmashup;

import jp.taiga.beans.IdeaBean;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class IdeaAdapter extends ArrayAdapter<IdeaBean> {

	private LayoutInflater layoutInflater;

	public IdeaAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 特定の行(position)のデータを得る
		IdeaBean item = (IdeaBean) getItem(position);

		// convertViewは使い回しされている可能性があるのでnullの時だけ新しく作る
		if (null == convertView) {
			convertView = layoutInflater.inflate(R.layout.layout_idea, null);
		}

		if(item.getKeywordC().equals("null")){
			TextView idea = (TextView) convertView.findViewById(R.id.idea);
			idea.setText(item.getKeywordA() + " + " + item.getKeywordB());
		}else{
			TextView idea = (TextView) convertView.findViewById(R.id.idea);
			idea.setText(item.getKeywordA() + " + " + item.getKeywordB() + " + " +item.getKeywordC());	
		}

		
		TextView comment = (TextView) convertView.findViewById(R.id.comment);
		comment.setText(item.getComment());

		return convertView;
	}

}
