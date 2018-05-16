package com.example.newsreader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment1 extends Fragment {
	private static final int NODE_CHANNEL = 0;
	private static final int NODE_ITEM = 1;
	private static final int NODE_NONE = -1;

	private View layoutView;
	private ProgressDialog pd;  //进度指示dialog

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 加载fragment1.xml布局
		layoutView = inflater.inflate(R.layout.fragment1, null);
		pd = ProgressDialog.show(getActivity(), "请稍候。。。", "正在加载数据",true,true);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 通过httpGet获取RSS数据
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(
						"http://news.163.com/special/00011K6L/rss_newstop.xml");
				try {
					HttpResponse response = client.execute(get);
					// 检查服务器返回的响应码，200表示成功
					if (response.getStatusLine().getStatusCode() == 200) {
						// 将获取到的数据转换成文本字符
						HttpEntity entity = response.getEntity();
						String content = EntityUtils.toString(entity, "UTF-8");
						// 在Lgocat视图中显示得到的RSS内容
						Log.i("rss", content);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				//数据加载完毕销毁进度条
				pd.dismiss();
			}
		}).start();

		return layoutView;
	}

	/*
	 * P205
	 */
	public List<Map<String, String>>getRssItems(InputStream xml)throws Exception{
		List<Map<String , String>>
		itemList = new ArrayList<Map<String,String>>();
		Map<String, String> item = new HashMap<String, String>();
		String name ,value;
		
		int currNode = NODE_NONE;
		
		XmlPullParser pullParser = Xml.newPullParser();
		pullParser.setInput(xml,"UTF-8");
		int event = pullParser.getEventType();
		while(event!=XmlPullParser.END_DOCUMENT){
			switch (event) {
			case XmlPullParser.START_TAG:
				name = pullParser.getName();
				break;

			default:
				break;
			}
		}
		return null;
	}




}
