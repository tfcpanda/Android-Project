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

	private String tabs[] = { "��ҳ", "����", "��ͼ", "���" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// ��ʼ���ײ�������
		navGroup = (RadioGroup) findViewById(R.id.navgroup);
		// 初始化底部导航栏
		navGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				// 切换到"首页"界面
				case R.id.radioButton1:
					switchFragmentSupport(R.id.content, tabs[0]);
					break;
				case R.id.radioButton2:
					// 切换到"新闻"界面
					switchFragmentSupport(R.id.content, tabs[1]);
					break;
				case R.id.radioButton3:
					// 切换到"组图"界面
					switchFragmentSupport(R.id.content, tabs[2]);
					break;
				case R.id.radioButton4:
					// 切换到"更多"界面
					switchFragmentSupport(R.id.content, tabs[3]);
					break;
				}

			}

		});
		// 默认选中第一个页面
		RadioButton btn = (RadioButton) navGroup.getChildAt(0);
		btn.toggle();
	}

	/*
	 * 动态切换组建中显示的界面
	 * 
	 * @param containerId 
	 * 带切换界面的布局控件
	 * @param tag 
	 * 目标Fragment的标签名称
	 */

	private void switchFragmentSupport(int containerId, String tag) {
		// 获取FragmentMangager管理器
		FragmentManager manager = getSupportFragmentManager();
		// 根据tag标签名查找是否已存在对应的Fragment对象
		Fragment destFragment = manager.findFragmentByTag(tag);
		// 如果tag标签对应的Fragment对象
		if (destFragment == null) {
			if (tag.equals(tabs[0]))
				destFragment = new Fragment1();
			if (tag.equals(tabs[1]))
				destFragment = new Fragment2();
			if (tag.equals(tabs[2]))
				destFragment = new Fragment3();
			if (tag.equals(tabs[3]))
				destFragment = new Fragment4();
		}
		// 获取FragmentTransaction
		FragmentTransaction ft = manager.beginTransaction();
		ft.replace(containerId, destFragment, tag);
		ft.commit();

	}
}
