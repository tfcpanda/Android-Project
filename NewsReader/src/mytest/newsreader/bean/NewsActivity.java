package mytest.newsreader.bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.example.newsreader.R;

import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class NewsActivity extends Activity {
	protected void OnCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_detail);
		setTitle(R.string.app_name);
		//接受intent传递过来的数据
		Intent intent = this.getIntent();
		final NewsBean news = (NewsBean) intent.getSerializableExtra("news");
		//初始化组件
		TextView titleView = (TextView) findViewById(R.id.newsTitle);
		TextView pubDTextView = (TextView) findViewById(R.id.newsPubDate);
		final WebView webview = (WebView) findViewById(R.id.newsDetail);
		//显示新闻标题
		titleView.setText(news.title);
		//显示新闻发布时间
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM YYY HH:mm:ss 'GMT'",Locale.US);
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date d = sdf.parse(news.pubDate);
			//按照年月日时分秒的格式显示时间
			SimpleDateFormat sdf2 = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss",Locale.US);
			String s = sdf2.format(d);
			pubDTextView.setText("(发布日期:"+s+")");
		}catch (Exception e) {
			e.printStackTrace();
		}
		//webview 参数设置（是否支持多窗口，是否支持缩放）
		WebSettings settings = webview.getSettings();
		settings.setSupportMultipleWindows(false);
		settings.setSupportZoom(false);
		// 加载新闻描述内容
		webview.loadDataWithBaseURL(news.guid, news.description, null, "utf-8", null);
		
		// 返回动作
		ImageView back = (ImageView) findViewById(R.id.imageViewBack);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		// 浏览详细页面
		ImageView browser = (ImageView) findViewById(R.id.imageViewBrowser);
		browser.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				webview.loadUrl(news.guid);
			}
		});
	}
}
