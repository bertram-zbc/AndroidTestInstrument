package com.test.baidumap;

import java.io.IOException;

import com.testmap.logic.ClientThread;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
	
	SharedPreferences firstlogin;
//	SharedPreferences userName;
//	SharedPreferences passWord;
	Editor edit;
	private EditText edt1, edt2;
	private TextView fgt_psd, register, login;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			switch(message.what) {
			case 0x1:
				edit.putInt("IsFirstLogin", 1);
				edit.putString("username", edt1.getText().toString());
				edit.putString("password", edt2.getText().toString());
				edit.commit();
//				edit2.commit();
//				edit3.commit();
				Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(LoginActivity.this, TabActivity.class);
				startActivity(intent);
				finish();
				break;
			case 0x2:
				Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
				break;
			case 0x3:
				new Thread(new ClientThread()).start();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		firstlogin = this.getSharedPreferences("Pref01",MODE_PRIVATE);
		edit = firstlogin.edit();
//		userName = this.getSharedPreferences("Pref02",MODE_PRIVATE);
//		edit = userName.edit();
//		passWord = this.getSharedPreferences("Pref02",MODE_PRIVATE);
//		edit = passWord.edit();
		
		fgt_psd = (TextView) findViewById(R.id.tvggtpsd);
		register = (TextView) findViewById(R.id.tvregister);
		login = (TextView) findViewById(R.id.textlogin);
		edt1 = (EditText) findViewById(R.id.editText1);
		edt2 = (EditText) findViewById(R.id.editText2);
		register.setOnClickListener(this);
		fgt_psd.setOnClickListener(this);
		login.setOnClickListener(this);
		
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.textlogin:
			
			new Thread(new LoginThread()).start();
			break;
			
		case R.id.tvregister:
			Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
			startActivity(intent);
			break;
			
		case R.id.tvggtpsd:
			
			break;
			
		default: break;
		}
	}
	private class LoginThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			try {
				String userName, password;
				userName = edt1.getText().toString();
				password = edt2.getText().toString();
				ClientThread.setUser(userName, password);
				new ClientThread().println("login&&" + password);
				String echo = new ClientThread().readLine();
				if(echo == null) {
					Message message = new Message();
					message.what = 0x3;
					handler.sendMessage(message);
				}
				if(echo.equals("true")) {
					Message message = new Message();
					message.what = 0x1;
					handler.sendMessage(message);
				} else {
					Message message = new Message();
					message.what = 0x2;
					handler.sendMessage(message);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Message message = new Message();
				message.what = 0x3;
				handler.sendMessage(message);
			}
		}
		
	}
}
