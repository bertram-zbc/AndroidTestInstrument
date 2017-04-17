package com.test.baidumap;

import java.io.IOException;
import com.testmap.logic.ClientThread;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener {
	ImageButton imageBt1;
	EditText userName,password1,password2;
	TextView textLogin;
	Handler handler = new Handler()
	{
		public void handleMessage(Message msg){
			switch(msg.what){
			case 0x1:
				Toast.makeText(RegisterActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
				break;
			case 0x2:
				Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
				break;
			case 0x3:
				Toast.makeText(RegisterActivity.this, "网络错误，重新尝试中····", Toast.LENGTH_SHORT);
				new Thread(new ClientThread()).start();
				break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		System.out.println("111111111111111111111111111111111111111111");
		textLogin = (TextView) findViewById(R.id.textregister);
		imageBt1 = (ImageButton) findViewById(R.id.imageButton1);
		userName = (EditText) findViewById(R.id.editText1);
		password1 = (EditText) findViewById(R.id.editText2);
		password2 = (EditText) findViewById(R.id.editText3);
		System.out.println("22222222222222222222222222222222222222222");
		imageBt1.setOnClickListener(this);
		textLogin.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.textregister:
				System.out.println("33333333333333333333333333333333333333333333");
			String str0,str1,str2;
			str0 = userName.getText().toString();
			str1 = password1.getText().toString();
			str2 = password2.getText().toString();
			if(str0.equals("")||str1.equals("")||str2.equals("")){
				Toast.makeText(RegisterActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			System.out.println("444444444444444444444444444444444444444444444444444");
			if(!str1.equals(str2)){
				Toast.makeText(RegisterActivity.this, "两次输入密码不相同", Toast.LENGTH_SHORT).show();
				return;
			}
			System.out.println("5555555555555555555555555555555555555555555555555555");
			System.out.println("6666666666666666666666666666666666666666666666666666666666");
			new Thread(new RegisterThread()).start();
			break;
			
		case R.id.imageButton1:
			finish();
			break;
		
		default:break;
		}
		
	}
		
	private class RegisterThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{
				String userID,Password;
				userID = userName.getText().toString();
				Password = password1.getText().toString();
				ClientThread.setUser(userID, Password);
				new ClientThread().println("register&&"+Password);//2015-12-15
				Message msg = new Message();
				msg.obj = new ClientThread().readLine();
				if(msg.obj.equals("failed")){
					msg.what=0x1;
				}
				else if(msg.obj.equals("success")){
					msg.what=0x2;
				}
				handler.sendMessage(msg);
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
