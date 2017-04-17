package com.test.baidumap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.baidumap.R;
import com.test.sortlistview.CharacterParser;
import com.test.sortlistview.ClearEditText;
import com.test.sortlistview.PinyinComparator;
import com.test.sortlistview.SideBar;
import com.test.sortlistview.SideBar.OnTouchingLetterChangedListener;
import com.test.sortlistview.SortAdapter;
import com.test.sortlistview.SortModel;

public class SortListActivity extends Activity {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	private TextView tvback;
	
	/**
	 * ����ת����ƴ������
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	
	/**
	 * ����ƴ��������ListView�����������
	 */
	private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sortlistview);	
		initViews();
	}

	private void initViews() {
		//ʵ��������תƴ����
		characterParser = CharacterParser.getInstance();
		
		pinyinComparator = new PinyinComparator();
		
		tvback = (TextView) findViewById(R.id.tvback);
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		
		tvback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(SortListActivity.this, ConditionActivity.class);
//				setResult(1, intent);
//				finish();
				finish();
			}
		});
		
		//�����Ҳഥ������
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String s) {
				//����ĸ�״γ��ֵ�λ��
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1){
					sortListView.setSelection(position);
				}
				
			}
		});
		
		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//����Ҫ����adapter.getItem(position)����ȡ��ǰposition����Ӧ�Ķ���
//				Toast.makeText(getApplication(), ((SortModel)adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
				String isRecommand = SortListActivity.this.getIntent().getExtras().getString("IsRecommand");
				if(isRecommand.equals("yes")){
					Intent intent = new Intent();
					intent.setClass(SortListActivity.this, CityActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("CityName", ((SortModel)adapter.getItem(position)).getName().toString());
					intent.putExtras(bundle);
					startActivity(intent);
				}else{
					Intent intent = new Intent();
					intent.setClass(SortListActivity.this, ConditionActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("dest_city", ((SortModel)adapter.getItem(position)).getName().toString());
					intent.putExtras(bundle);
					setResult(1, intent);
				}
				
				finish();
				
			}
		});
		
		SourceDateList = filledData(getResources().getStringArray(R.array.date));
		
		// ����a-z��������Դ����
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(this, SourceDateList);
		sortListView.setAdapter(adapter);
		
		
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		mClearEditText.clearFocus();
		//�������������ֵ�ĸı�����������
		mClearEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//������������ֵΪ�գ�����Ϊԭ�����б�����Ϊ���������б�
				filterData(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}


	/**
	 * ΪListView�������
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(String [] date){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<date.length; i++){
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			//����ת����ƴ��
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
			if(sortString.matches("[A-Z]")){
				sortModel.setSortLetters(sortString.toUpperCase());
			}else{
				sortModel.setSortLetters("#");
			}
			
			mSortList.add(sortModel);
		}
		return mSortList;
		
	}
	
	/**
	 * ����������е�ֵ���������ݲ�����ListView
	 * @param filterStr
	 */
	private void filterData(String filterStr){
		List<SortModel> filterDateList = new ArrayList<SortModel>();
		
		if(TextUtils.isEmpty(filterStr)){
			filterDateList = SourceDateList;
		}else{
			filterDateList.clear();
			for(SortModel sortModel : SourceDateList){
				String name = sortModel.getName();
				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(sortModel);
				}
			}
		}
		
		// ����a-z��������
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}
	
}
