package ml.hpsapp.bookkeeping;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class test extends AppCompatActivity {
    TextView flatbal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        flatbal = findViewById(R.id.flat_bal);


        String JSON_URL = "https://script.google.com/macros/s/AKfycbz-wEuE3k8xwQTVEXxu_-YiwskBpRCiS82j5F7hv7ZwMgMXM_g/exec?id=1oAc4mp7cQnah97HJ7XK00ZDwb83HvOHrJlYzHZMy-Bg&sheet=JAN";
        final int[] flattotal = new int[1];
        SharedPreferences prefs = getSharedPreferences("login", Activity.MODE_PRIVATE);
        final String id = prefs.getString("id", "");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject ob = new JSONObject(response);
                            JSONArray jsonArray = ob.getJSONArray("JAN");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String record = jsonObject.getString("Record");
                                String cost = jsonObject.getString("cost" + id);

                                if (record.equals("SETTLE")) {
//                                    flattotal[0] = Integer.parseInt(cost);
                                    flatbal.setText(cost);
                                    break;
                                } else {

                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
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

        Toast.makeText(getApplicationContext(), Integer.toString(flattotal[0]), Toast.LENGTH_SHORT).show();

    }
}
