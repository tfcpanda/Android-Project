package com.example.newsreader;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.support.v4.app.*;


public class MainActivity extends FragmentActivity {
	private RadioGroup navGroup;
	
	
	private String tabs[] = { "首页", "新闻", "组图", "更多" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 初始化底部导航栏
		navGroup = (RadioGroup) findViewById(R.id.navgroup);
		// 设定导航栏中每个选项的选中时间响应
		navGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radioButton1:
					// 切换到"首页"界面
					switchFragmentSupport(R.id.content, tabs[0]);
					break;
				case R.id.radioButton2:
					// 切换到"首页"界面
					switchFragmentSupport(R.id.content, tabs[1]);
					break;
				case R.id.radioButton3:
					// 切换到"首页"界面
					switchFragmentSupport(R.id.content, tabs[2]);
					break;
				case R.id.radioButton4:
					// 切换到"首页"界面
					switchFragmentSupport(R.id.content, tabs[3]);
					break;
				}

			}

		});
		// 默认选中最左边的RadioButton
		RadioButton btn = (RadioButton) navGroup.getChildAt(0);
		btn.toggle();
	}

	/*
	 * 动态切换组件中显示的界面
	 * @param containerId
	 * 带切换界面的布局控件
	 * @param tag
	 * 目标Fragment的标签名称
	 */

	private void switchFragmentSupport(int containerId, String tag) {
		// 获取FragmentManager管理器
		FragmentManager manager = getSupportFragmentManager();
		//获取tag标签名查找是否存在对应的Fragmemt对象
		Fragment destFragment = manager.findFragmentByTag(tag);
		//如果tag标签对应的Fragment对象不存在，则初始化它
		if(destFragment == null){
			if(tag.equals(tabs[0])) destFragment = new Fragment1();
			if(tag.equals(tabs[1])) destFragment = new Fragment2();
			if(tag.equals(tabs[2])) destFragment = new Fragment3();
			if(tag.equals(tabs[3])) destFragment = new Fragment4();
		}
		//获取FragmentTranstion事务对象
		FragmentTransaction ft = manager.beginTransaction();
		//将组件id为containerId的内容替换为destFragment
		//并把destFragment的标签设为tag变量的值
		ft.replace(containerId,destFragment,tag);
		ft.commit();

	}
}
