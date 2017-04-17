package com.test.baidumap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class RecommandActivity extends Activity {
	
	private ImageView image1;
	private ImageView image2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recommand);
		
		image1 = (ImageView) findViewById(R.id.pic1);
		image1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RecommandActivity.this,SortListActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("IsRecommand", "yes");
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		
		image2 = (ImageView) findViewById(R.id.pic4);
		image2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RecommandActivity.this,HotelActivity.class);
				startActivity(intent);
			}
		});
		
	}

}
