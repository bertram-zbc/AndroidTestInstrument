package com.test.baidumap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.GeolocationPermissions;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity {
	private WebView webView;
	private static String url = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);             
        init();
    }
    
    public static void setUrl(String url) {
    	WebViewActivity.url = url;
    }
    

    @SuppressLint("SetJavaScriptEnabled")
	private void init(){
        webView = (WebView) findViewById(R.id.webView);
        //WebView閸旂姾娴噖eb鐠у嫭绨�
       webView.loadUrl(url);
        //鐟曞棛娲奧ebView姒涙顓绘担璺ㄦ暏缁楊兛绗侀弬瑙勫灗缁崵绮烘妯款吇濞村繗顫嶉崳銊﹀ⅵ瀵拷缍夋い鐢垫畱鐞涘奔璐熼敍灞煎▏缂冩垿銆夐悽鈺揺bView閹垫挸绱�
       WebSettings settings = webView.getSettings();
       settings.setJavaScriptCanOpenWindowsAutomatically(false);
       settings.setJavaScriptEnabled(true);
       settings.setUserAgentString("HFWSH_USER Android");
       
       webView.setWebViewClient(new WebViewClient(){
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
               //鏉╂柨娲栭崐鍏兼Цtrue閻ㄥ嫭妞傞崐娆愬付閸掕泛骞揥ebView閹垫挸绱戦敍灞艰礋false鐠嬪啰鏁ょ化鑽ょ埠濞村繗顫嶉崳銊﹀灗缁楊兛绗侀弬瑙勭セ鐟欏牆娅�
            view.loadUrl(url);
            return true;
        }
       });
       
     //閸氼垳鏁ら弫鐗堝祦鎼达拷 
       settings.setDatabaseEnabled(true);    
       String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath(); 

       //閸氼垳鏁ら崷鎵倞鐎规矮缍� 
       settings.setGeolocationEnabled(true);  
       //鐠佸墽鐤嗙�姘秴閻ㄥ嫭鏆熼幑顔肩氨鐠侯垰绶� 
       settings.setGeolocationDatabasePath(dir);   

       //閺堬拷鍣哥憰浣烘畱閺傝纭堕敍灞肩鐎规俺顩︾拋鍓х枂閿涘矁绻栫亸杈ㄦЦ閸戣桨绗夐弶銉ф畱娑撴槒顩﹂崢鐔锋礈

       settings.setDomStorageEnabled(true); 
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(webView.canGoBack())
            {
                webView.goBack();//鏉╂柨娲栨稉濠佺妞ょ敻娼�
                return true;
            }
            else
            {
               finish();//闁拷鍤粙瀣碍
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    
}