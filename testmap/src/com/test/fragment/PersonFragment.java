package com.test.fragment;

import java.io.IOException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.test.baidumap.PersonMoodsActivity;
import com.test.baidumap.R;
import com.testmap.logic.ClientThread;
import com.testmap.view.CircleImageView;

public class PersonFragment extends DialogFragment {
	
	public static String headPicUrl = ClientThread.getPicIp() + "u1/mycourse.png";
	private CircleImageView imghead;
	private TextView tvname;
	private TextView tvageandsex;
	private TextView tvsend;
	private TextView tvadd;
	private TextView tvmood;
	
	private String currentUname;
	
	private boolean isFriend;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0x1) {
				String [] userInfo = (String[]) msg.obj;
				isFriend = Boolean.parseBoolean(userInfo[9]);
				if(isFriend) {
					tvadd.setText("ɾ������");
				} else {
					tvadd.setText("��Ϊ����");
				}
				if(userInfo[3].equals("0")){
					tvageandsex.setText("��  "+userInfo[2]+"��");
				}else{
					tvageandsex.setText("Ů  "+userInfo[2]+"��");
				}			
				if(userInfo[1].equals(ClientThread.getUserName())) {
					tvname.setText("��");
				} else {
					tvname.setText(userInfo[1]);
				}
				ImageLoader.getInstance().displayImage(ClientThread.getPicIp() + userInfo[1] + "/head/" + userInfo[8], imghead);
			} else if(msg.what == 0x2) {//����վ���ź�
				Toast.makeText(getActivity(), (String) msg.obj, Toast.LENGTH_SHORT).show();
			} else if(msg.what == 0x3) {//�Ӻ��ѳɹ�
				Toast.makeText(getActivity(), "�ѷ��ͺ�������", Toast.LENGTH_SHORT).show();
				isFriend = true;
				tvadd.setText("ɾ������");
			} else if(msg.what == 0x4) {//�Ӻ���ʧ��
				Toast.makeText(getActivity(), "�Ѿ���TA��Ϊ����", Toast.LENGTH_SHORT).show();
			} else if(msg.what == 0x5) {//ɾ������
				isFriend = false;
				tvadd.setText("��Ϊ����");
				Toast.makeText(getActivity(), "�Ѿ���TA����ĺ����б�ɾ��", Toast.LENGTH_SHORT).show();
			} else if(msg.what == 0x6) {
				Toast.makeText(getActivity(), "�������ʧ�������Ժ�����", Toast.LENGTH_SHORT).show();
			}
		};
	};

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		currentUname = getArguments().getString("username");
		
		System.out.println(currentUname);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());  
        LayoutInflater inflater = getActivity().getLayoutInflater();  
        View view = inflater.inflate(R.layout.person, null);
        builder.setView(view);
        
        imghead = (CircleImageView) view.findViewById(R.id.cirleimageview);
        tvname = (TextView) view.findViewById(R.id.textname);
        tvageandsex = (TextView) view.findViewById(R.id.textmessage);
        tvsend = (TextView) view.findViewById(R.id.textsend);
        tvadd  =(TextView) view.findViewById(R.id.textaddfriend);
        tvmood = (TextView) view.findViewById(R.id.textallmoods);
        
        tvSet();
        
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				new ClientThread().println("getAllInfoOfUser&&" + currentUname);
				try {
					String recvString = new ClientThread().readLine();
					if(recvString == null) {
						handler.sendEmptyMessage(0x6);
						return;
					}
					if(recvString.equals("nothing")) {
						return;
					}
					String [] userInfo = recvString.split("&&");
					Message msg = new Message();
					msg.obj = userInfo;
					msg.what = 0x1;
					handler.sendMessage(msg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}).start();
        
		
		
		return builder.create();
	}

	private void tvSet() {
		// TODO Auto-generated method stub
		tvageandsex.setText("");
		/*imghead.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),WeixinActivity.class);
				intent.putExtra(WeixinActivity.WHATACTIVITY, WeixinActivity.PERSONACTIVIY);
				startActivity(intent);
			}
		});
*/		
		if(isFriend) {
			tvadd.setText("ɾ������");
		} else {
			tvadd.setText("��Ϊ����");
		}	
			
		/**
         * �Ӻ��Ѱ�ť����¼�
         */
        tvadd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isFriend) {
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							new ClientThread().println("deleFriend&&" + currentUname);
							try {
								new ClientThread().readLine();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							handler.sendEmptyMessage(0x5);
							
						}
					}).start();
				} else {
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							new ClientThread().println("makeFriends&&" + currentUname + "&&0");
							try {
								String recvString = new ClientThread().readLine();
								if(recvString.equals("success")) {
									handler.sendEmptyMessage(0x3);
								} else {
									handler.sendEmptyMessage(0x4);
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					}).start();
				}
				
			}
		});
        
        /**
         * ������Ϣ��ť����¼�
         */
        tvsend.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
			}
		});
        
		/**
		 * ����鿴�������ж�̬
		 */
        tvmood.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(), PersonMoodsActivity.class);
			intent.putExtra(PersonMoodsActivity.WHATTYPE, PersonMoodsActivity.PEOPLE);
			intent.putExtra(PersonMoodsActivity.UNAME, currentUname);
			intent.putExtra(PersonMoodsActivity.HEADPIC, headPicUrl);
			startActivity(intent);
			
		}
	});
		
	}
}
