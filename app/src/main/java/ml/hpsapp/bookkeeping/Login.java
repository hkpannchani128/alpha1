package ml.hpsapp.bookkeeping;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.preference.PreferenceActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.security.MessageDigest;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class Login extends AppCompatActivity {

    EditText username, password;
    Button btnlogin;
    String JSON_URL = "https://script.google.com/macros/s/AKfycbz-wEuE3k8xwQTVEXxu_-YiwskBpRCiS82j5F7hv7ZwMgMXM_g/exec?id=1V6vxpw3H-FRKHYGJuZVZiNCKLrYntQRyOIZKj9tCHOk&sheet=user";

    String user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnlogin = findViewById(R.id.btnlogin);

        username = findViewById(R.id.etUsername);
        password = findViewById(R.id.password);

        if (loginStatus() == TRUE) {
            Intent i = new Intent(getApplicationContext(), Home.class);
            startActivity(i);
            finish();
        }
        
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user = username.getText().toString();
                pass = password.getText().toString();


                StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject ob = new JSONObject(response);
                                    JSONArray jsonArray = ob.getJSONArray("user");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String jsonuser = jsonObject.getString("username");
                                        String jsonpass = jsonObject.getString("password");
                                        String jsonemail= jsonObject.getString("email");

                                        String jsonfirstname=jsonObject.getString("firstname");
                                        String jsonlastname=jsonObject.getString("lastname");
                                        String jsonid=jsonObject.getString("id");
                                        String jsonmobile=jsonObject.getString("mobile");


                                        if (user.equals(jsonuser) && pass.equals(jsonpass)) {
                                            SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE).edit();
                                            editor.putString("username", user);
                                            editor.putString("email",jsonemail);
                                            editor.putString("firstname",jsonfirstname);
                                            editor.putString("lastname",jsonlastname);
                                            editor.putString("mobile",jsonmobile);
                                            editor.putString("id",jsonid);



                                            editor.apply();

                                            Intent in = new Intent(getApplicationContext(), Home.class);
                                            startActivity(in);
                                            finish();
                                            break;
                                        } else {


                                            final Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Alert();
                                                }
                                            }, 5000);


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
                                Toast.makeText(Login.this, "Getting error Please Check Network Connection", Toast.LENGTH_SHORT).show();
                            }
                        });

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);

            }
        });
    }

    public boolean loginStatus() {
        SharedPreferences prefs = getSharedPreferences("login", Activity.MODE_PRIVATE);
        String username = prefs.getString("username", "");
        if (!username.equals("")) {
            return TRUE;
        }
        return FALSE;
    }
    public void Alert()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(Login.this);
        alert.setCancelable(true);
        alert.setTitle("Error");
        alert.setMessage("Invalid Credentials");
        alert.show();
    }
}

