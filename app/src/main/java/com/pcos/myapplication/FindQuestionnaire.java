package com.pcos.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindQuestionnaire extends AppCompatActivity {

    protected WebView webView;
    //String cookies = "";
    //SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_findques);

        init();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(webView.canGoBack())
            {
                webView.goBack();
                return true;
            }
            else
            {
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void init(){
        webView = (WebView) findViewById(R.id.webView);

        //sp = getSharedPreferences("aaa", MODE_PRIVATE);
        //final Context context = this;
        //final CookieManager cm = CookieManager.getInstance();
        final java.net.CookieManager msCookieManager = new java.net.CookieManager();

        webView.getSettings()
                .setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setSupportZoom(false);

        webView.getSettings().setBuiltInZoomControls(false);

        webView.getSettings().setUseWideViewPort(false);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Map<String,String>header = new HashMap<String, String>();
                String cookies = TextUtils.join(";",  msCookieManager.getCookieStore().getCookies());
                header.put("Cookie", cookies);
                view.loadUrl(url, header);

                return true;
            }

            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                try {
                    if (url.equals("http://10.0.0.2:9000/pcos/login")) {
                        URL url1 = new URL(url);
                        HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-type", "application/json");

                        JSONObject jobj = new JSONObject();
                        try {
                            String username = getIntent().getStringExtra("email");
                            String password = getIntent().getStringExtra("password");
                            jobj.put("username", username);
                            jobj.put("password", password);
                            //jobj.put("redirectURL", "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        conn.getOutputStream().write(jobj.toString().getBytes());

                        final String COOKIES_HEADER = "Set-Cookie";


                        Map<String, List<String>> headerFields = conn.getHeaderFields();
                        List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);


                        if (cookiesHeader != null) {
                            for (String cookie : cookiesHeader) {
                                msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                            }
                        }

                        URL url2 = new URL("http://10.0.0.2:9000/pcos/patientquestionnaire/" +
                            getIntent().getStringExtra("patientID"));
                        HttpURLConnection connection = (HttpURLConnection) url2.openConnection();
                        conn.setRequestMethod("GET");
                        connection.setRequestProperty("Cookie",
                                TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()));

                        WebResourceResponse resp = new WebResourceResponse("text/html",
                                connection.getHeaderField("encoding"),
                                connection.getInputStream());

                        return resp;
                    }
                }
                catch (Exception e) {
                }
                return super.shouldInterceptRequest(view, url);
            }

        });
        //synCookies(this, sp.getString("cook", ""));

        webView.loadUrl("http://10.0.0.2:9000/pcos/login");

    }

    /*public void synCookies(Context context, String url) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        //cookieManager.removeSessionCookie();
        cookieManager.setCookie(url, cookies);
        CookieSyncManager.getInstance().sync();
    }*/
}
