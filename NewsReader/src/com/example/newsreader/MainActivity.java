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
	
	
	private String tabs[] = { "��ҳ", "����", "��ͼ", "����" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// ��ʼ���ײ�������
		navGroup = (RadioGroup) findViewById(R.id.navgroup);
		// �趨��������ÿ��ѡ���ѡ��ʱ����Ӧ
		navGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radioButton1:
					// �л���"��ҳ"����
					switchFragmentSupport(R.id.content, tabs[0]);
					break;
				case R.id.radioButton2:
					// �л���"��ҳ"����
					switchFragmentSupport(R.id.content, tabs[1]);
					break;
				case R.id.radioButton3:
					// �л���"��ҳ"����
					switchFragmentSupport(R.id.content, tabs[2]);
					break;
				case R.id.radioButton4:
					// �л���"��ҳ"����
					switchFragmentSupport(R.id.content, tabs[3]);
					break;
				}

			}

		});
		// Ĭ��ѡ������ߵ�RadioButton
		RadioButton btn = (RadioButton) navGroup.getChildAt(0);
		btn.toggle();
	}

	/*
	 * ��̬�л��������ʾ�Ľ���
	 * @param containerId
	 * ���л�����Ĳ��ֿؼ�
	 * @param tag
	 * Ŀ��Fragment�ı�ǩ����
	 */

	private void switchFragmentSupport(int containerId, String tag) {
		// ��ȡFragmentManager������
		FragmentManager manager = getSupportFragmentManager();
		//��ȡtag��ǩ�������Ƿ���ڶ�Ӧ��Fragmemt����
		Fragment destFragment = manager.findFragmentByTag(tag);
		//���tag��ǩ��Ӧ��Fragment���󲻴��ڣ����ʼ����
		if(destFragment == null){
			if(tag.equals(tabs[0])) destFragment = new Fragment1();
			if(tag.equals(tabs[1])) destFragment = new Fragment2();
			if(tag.equals(tabs[2])) destFragment = new Fragment3();
			if(tag.equals(tabs[3])) destFragment = new Fragment4();
		}
		//��ȡFragmentTranstion�������
		FragmentTransaction ft = manager.beginTransaction();
		//�����idΪcontainerId�������滻ΪdestFragment
		//����destFragment�ı�ǩ��Ϊtag������ֵ
		ft.replace(containerId,destFragment,tag);
		ft.commit();

	}
}
