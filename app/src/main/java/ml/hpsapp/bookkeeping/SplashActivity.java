package ml.hpsapp.bookkeeping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SplashActivity extends AppCompatActivity {
    WebView loading;
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat month_date = new SimpleDateFormat("MMM");
    String current_month = month_date.format(cal.getTime());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FetchFlatData();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loading = findViewById(R.id.loading);
        loading.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        loading.loadUrl("file:///android_asset/loading.html");


    }

    private void StoreFlatData(String response) {
        SharedPreferences.Editor editor = getApplicationContext().getApplicationContext().getSharedPreferences("jsndata", MODE_PRIVATE).edit();
        editor.putString("flatdata", response);
        editor.apply();
        editor.commit();
    }

    private void StorePersonalData(String response) {
        SharedPreferences.Editor editor = getApplicationContext().getApplicationContext().getSharedPreferences("jsndata", MODE_PRIVATE).edit();
        editor.putString("personaldata", response);
        editor.apply();
        editor.commit();
    }

    public void FetchFlatData() {
        String JSON_URL = "https://script.google.com/macros/s/AKfycbz-wEuE3k8xwQTVEXxu_-YiwskBpRCiS82j5F7hv7ZwMgMXM_g/exec?id=1oAc4mp7cQnah97HJ7XK00ZDwb83HvOHrJlYzHZMy-Bg&sheet=" + current_month;
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
        final String id = prefs.getString("id", "");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        StoreFlatData(response);
                        FetchPersonalData();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Getting error Please Check Network Connection", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    public void FetchPersonalData() {
        String JSON_URL = "https://script.google.com/macros/s/AKfycbz-wEuE3k8xwQTVEXxu_-YiwskBpRCiS82j5F7hv7ZwMgMXM_g/exec?id=1DaW0kQA2p4leMtmIIOrZqyXUl8Voh2O84yYX4E9YuY4&sheet=" + current_month;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        StorePersonalData(response);
                        if (!response.equals("")) {
                            Intent i = new Intent(getApplicationContext(), Login.class);
                            startActivity(i);
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(), "NO Internet..Loading Old Data", Toast.LENGTH_LONG).show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent i = new Intent(getApplicationContext(), Login.class);
                                    startActivity(i);
                                    finish();
                                }
                            }, 500);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Getting error Please Check Network Connection", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
}
