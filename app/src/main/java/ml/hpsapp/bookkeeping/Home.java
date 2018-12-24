package ml.hpsapp.bookkeeping;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
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

import ml.hpsapp.bookkeeping.navigation.NavDashboardFragment;
import ml.hpsapp.bookkeeping.navigation.NavFlatFragment;
import ml.hpsapp.bookkeeping.navigation.NavPersonalFragment;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    TextView dispname, dispemail;
    String URL = "", user = "", pass = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_home);

        SharedPreferences prefs = getSharedPreferences("login", Activity.MODE_PRIVATE);
        String email = prefs.getString("email", "");
        String first = prefs.getString("firstname", "");
        String last = prefs.getString("lastname", "");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.s_drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);

        dispemail = headerView.findViewById(R.id.dispemail);
        dispname = headerView.findViewById(R.id.dispname);
        dispemail.setText(email);
        dispname.setText(first + " " + last);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new NavDashboardFragment()).commit();
            navigationView.setCheckedItem(R.id.dshrd);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dshrd:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NavDashboardFragment()).commit();
                break;
            case R.id.flt:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NavFlatFragment()).commit();
                break;
            case R.id.prsnl:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NavPersonalFragment()).commit();
                break;
            case R.id.shr:
                break;
            case R.id.updt:
                FetchUpdateData();
                break;
            case R.id.s_lgt:
                SharedPreferences.Editor editor = getSharedPreferences("login", MODE_PRIVATE).edit();
//                editor.remove("username");
//                editor.remove("email");
//                editor.remove("user");
                editor.clear().commit();
                editor.apply();

                finish();
                Intent i = new Intent(getApplicationContext(), Login.class);

                startActivity(i);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 1000);
    }


    private void update(String response) {
        int ver = 1;
        try {
            JSONObject ob = new JSONObject(response);
            JSONArray jsonArray = ob.getJSONArray("chk_update");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String version = jsonObject.getString("version");
                final String link = jsonObject.getString("link");

                if (ver < Integer.parseInt(version)) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Update Found");
                    builder.setMessage("Do you want to download update ?");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(link));
                            startActivity(intent);
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    builder.show();


                } else {
                    Toast.makeText(this, "You are using the latest version", Toast.LENGTH_SHORT).show();
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void FetchUpdateData() {
        String check_url = "https://script.google.com/macros/s/AKfycbz-wEuE3k8xwQTVEXxu_-YiwskBpRCiS82j5F7hv7ZwMgMXM_g/exec?id=1V6vxpw3H-FRKHYGJuZVZiNCKLrYntQRyOIZKj9tCHOk&sheet=chk_update";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, check_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        update(response);
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
