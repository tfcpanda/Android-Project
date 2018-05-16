package com.example.newsreader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mytest.newsreader.bean.NewsBean;

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
import android.widget.ListView;

public class Fragment1 extends Fragment {
	private static final int NODE_CHANNEL = 0;
	private static final int NODE_ITEM = 1;
	private static final int NODE_NONE = -1;

	private View layoutView;
	private ProgressDialog pd;  //进度指示dialog
	private List<NewsBean> newsList = new ArrayList<NewsBean>();//新闻条目数组
	private ListView listView1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layoutView = inflater.inflate(R.layout.fragment1, null);
		listView1 = (ListView) layoutView.findViewById(R.id.listView1);
		pd = ProgressDialog.show(getActivity(), "请稍后。。。", "正在加载数据",true,true);
		//启动一个线程
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(
						"http://news.163.com/special/00011K6L/rss_newstop.xml");
				try {
					HttpResponse response = client.execute(get);
					// 检查服务器返回的响应码，200表示成功
					if (response.getStatusLine().getStatusCode() == 200) {
						//获取网络连接的输入流，然后解析收到的RSS数据
						InputStream stream = response.getEntity().getContent();
						List<Map<String, String>> items = getRssItems(stream);
						//先清空数组列表
						newsList.clear();
						//将解析后的RSS数据转换成Bean对象保存
						for(Map<String, String> item:items){
							NewsBean news = new NewsBean();
							news.title = item.get("title");
							news.description = item.get("description");
							news.link = item.get("Link");
							news.pubDate = item.get("pubDate");
							news.guid = item.get("guid");
							newsList.add(news);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				//销毁进度条
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
				//确定当前是channel还是item节点
				if("channel".equalsIgnoreCase(name)){
					currNode = NODE_CHANNEL;
					break;
				}else if("item".equalsIgnoreCase(name)){
					currNode = NODE_CHANNEL;
					break;
				}
				//如果当前是在channel节点中，则提取item节点的子元素
				if(currNode == NODE_ITEM){
					value = pullParser.nextText();
					item.put(name, value);
				}
				break;
				//节点元素结束，比如</title>
			case XmlPullParser.END_TAG:
				name = pullParser.getName();
				//如果到了，</item>节点，说明一条新闻结束，然后
				//把这条新闻加入动态数组，并为吓一跳新闻做准备
				if("item".equals(name)){
					itemList.add(item);
					item = new HashMap<String, String>();
				}
				break;
			}
			//继续处理下一节点
			event = pullParser.next();
		}
		return itemList;
	}




}
