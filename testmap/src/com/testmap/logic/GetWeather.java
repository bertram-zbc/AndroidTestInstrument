package com.testmap.logic;

import java.util.concurrent.Semaphore;

import json.JSONObject;

import com.show.api.ShowApiRequest;

public class GetWeather {
	static String res = null;
	static Semaphore semaphore = new Semaphore(0); 
	public static Weather[] getWeather(String addrName) {
		final String addrName2 = addrName;
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				res = new ShowApiRequest("http://route.showapi.com/9-2","12177","cb2bed5e3daf4824a5ddf157fd28a492")
		        .addTextPara("areaid","")
		        .addTextPara("area",addrName2)
		        .addTextPara("needMoreDay","1")
		        .addTextPara("needIndex","0")
		        .addTextPara("needHourData","0")
		        .post();
				semaphore.release();
			}
		}).start();
		
		try {
			semaphore.acquire();
			Weather[] weathers = new Weather[7];
			for(int i=0;i<7;i++)
			{
				weathers[i] = new Weather();
			}
			JSONObject dataJson = new JSONObject();
			try {
				 dataJson = new JSONObject(res);
			} catch (Exception e) {
				return null;
			}
			String error= dataJson.getString("showapi_res_error");
			if(error!=""){
				return null;
			}
			JSONObject resBody = dataJson.getJSONObject("showapi_res_body");
			JSONObject [] f = new JSONObject[7];
			f[0] = resBody.getJSONObject("f1");
			f[1] = resBody.getJSONObject("f2");
			f[2] = resBody.getJSONObject("f3");
			f[3] = resBody.getJSONObject("f4");
			f[4] = resBody.getJSONObject("f5");
			f[5] = resBody.getJSONObject("f6");
			f[6] = resBody.getJSONObject("f7");
			for(int i=0;i<7;i++){				
				String temp = f[i].getString("day");
				weathers[i].setDate(temp.substring(0, 4)+"/"+temp.substring(4,6)+"/"+temp.substring(6, 8));
				weathers[i].setTemperature(f[i].getString("day_air_temperature")+"/"+f[i].getString("night_air_temperature"));
				weathers[i].setWeather(f[i].getString("day_weather"));
			}
			return weathers;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return null;
		
	}
}
