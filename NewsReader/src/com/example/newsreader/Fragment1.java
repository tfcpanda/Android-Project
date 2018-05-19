package com.example.newsreader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mytest.newsreader.bean.NewsActivity;
import mytest.newsreader.bean.NewsBean;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Fragment1 extends Fragment {
	private static final int NODE_CHANNEL = 0;
	private static final int NODE_ITEM = 1;
	private static final int NODE_NONE = -1;
	//指示RSS新闻数据以获取
	private static final int MSG_NEWS_LOADED = 100;

	private View layoutView;
	private ProgressDialog pd;  //进度指示dialog
	private List<NewsBean> newsList = new ArrayList<NewsBean>();//新闻条目数组
	private ListView listView1;
	//使用Adapter才能使ListView显示数据
	private NewsAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layoutView = inflater.inflate(R.layout.fragment1, null);
		listView1 = (ListView) layoutView.findViewById(R.id.listView1);
		pd = ProgressDialog.show(getActivity(), "请稍后。。。", "正在加载数据",true,true);
		// 初始化ListView显示的数据源
		adapter = new NewsAdapter(newsList);
		listView1.setAdapter(adapter);
		
		listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int location, long id) {
				Intent intent = new Intent(Fragment1.this.getActivity(), NewsActivity.class);
				NewsBean news = newsList.get(location);
				intent.putExtra("news", news);
				startActivity(intent);
			}
		});
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
						// 数据加载完毕，通知ListView显示
						//adapter.notifyDataSetChanged();
						Message msg = mUIHandler.obtainMessage(MSG_NEWS_LOADED);
		                //msg.obj = newsList;
		                msg.sendToTarget();
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
	
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_NEWS_LOADED:
				// 更新ListView显示
				adapter.notifyDataSetChanged();
				// 销毁进度条
				pd.dismiss();
				break;
			}
		}
	};

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

	class NewsAdapter extends BaseAdapter{
		//带显示的新闻列表
		private List<NewsBean> newsItems;
		public NewsAdapter(List<NewsBean> newsitems){
			this.newsItems = newsitems;
		}
		//ListView组件根据getcont()的返回值来显示总的数据条数
		@Override
		public int getCount() {
			return newsItems.size();
		}
		//返回第position行条目，这里住直接设为NewsBean数据对象
		@Override
		public Object getItem(int position) {
			return newsItems.get(position);
		}
		//返回第position行条目的id，这里直接设置为position
		@Override
		public long getItemId(int position) {
			return position;
		}
		//ListView显示每条数据时，都要调用getView()方法
		//创建视图组件，然后传递给ListView显示
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			if(view == null){
				view = getActivity().getLayoutInflater().inflate(R.layout.news_item, null);
			}
			//初始化航布局视图中的各个子控件
			TextView newsTitle = (TextView) view.findViewById(R.id.news_title);
			TextView newsDescr = (TextView) view.findViewById(R.id.news_description);
			TextView newsPubdate = (TextView) view.findViewById(R.id.news_pubdate);
			ImageView newsIcon = (ImageView) view.findViewById(R.id.news_inco);
			//获取position行的数据
			NewsBean item = newsItems.get(position);
			//将第position行的数据显示到布局界面中
			newsTitle.setText(item.title);
			newsDescr.setText(item.description);
			newsPubdate.setText(item.pubDate);
			//将航布局视图返回给ListView组件显示
			return view;
		}

	}


}
