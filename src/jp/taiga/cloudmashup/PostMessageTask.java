package jp.taiga.cloudmashup;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class PostMessageTask extends AsyncTask<JSONObject, Integer, String> {

	Context context;
	ProgressDialog dialog;
	// String serverIP = "192.168.43.166";//学校無線
	// String serverIP = "192.168.100.8";//シアタールーム
	 String serverIP = "192.168.43.94";//テザリング
	// String serverIP = "192.168.13.68";//TKPカンファレンス

	public PostMessageTask(Context context) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.context = context;
	}

	@Override
	protected String doInBackground(JSONObject... contents) {

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://" + serverIP
				+ ":8080/CloudMashupServer/CloudMashup");

		HttpParams httpParams = httpClient.getParams();

		HttpConnectionParams.setConnectionTimeout(httpParams, 10 * 1000);
		HttpConnectionParams.setSoTimeout(httpParams, 30 * 1000);

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("json", contents[0].toString()));

		try {
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}

		String output = null;
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
			return "ERORR";
		}
		HttpEntity httpEntity = httpResponse.getEntity();
		try {
			output = EntityUtils.toString(httpEntity);
			output = URLDecoder.decode(output, "UTF-8");
			System.out.println(output);
		} catch (ParseException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		return output;

	}

	@Override
	protected void onCancelled() {
		// TODO 自動生成されたメソッド・スタブ
		dialog.dismiss();
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO 自動生成されたメソッド・スタブ
		dialog.dismiss();
		return;
	}

	@Override
	protected void onPreExecute() {
		// TODO 自動生成されたメソッド・スタブ
		dialog = new ProgressDialog(context);
		dialog.setMessage("Loading Contacts ");
		dialog.setIndeterminate(true);
		dialog.show();

	}

}