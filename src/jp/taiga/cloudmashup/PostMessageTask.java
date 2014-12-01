package jp.taiga.cloudmashup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class PostMessageTask extends AsyncTask<JSONObject, Integer, String> {

	Context context;
	ProgressDialog dialog;
	
	public PostMessageTask(Context context) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.context = context;
	}

	@Override
	protected String doInBackground(JSONObject... contents) {
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				"http://192.168.43.166:8080/CloudMashupServer/CloudMashup");

		
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
		if(httpResponse==null){
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
		this.dialog.dismiss();
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO 自動生成されたメソッド・スタブ
		this.dialog.dismiss();
		return;
	}

	@Override
	protected void onPreExecute() {
		// TODO 自動生成されたメソッド・スタブ
		this.dialog = new ProgressDialog(this.context);
		this.dialog.setMessage("HTTP");
		this.dialog.show();
	}

}