package ml.hpsapp.bookkeeping;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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

public class Home extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    TextView dispname,dispemail;
    String URL="",user="",pass="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_home);

        SharedPreferences prefs = getSharedPreferences("login", Activity.MODE_PRIVATE);
        String email = prefs.getString("email", "");
        String first = prefs.getString("firstname","");
        String last =  prefs.getString("lastname","");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.s_drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);

        dispemail=headerView.findViewById(R.id.dispemail);
        dispname=headerView.findViewById(R.id.dispname);
        dispemail.setText(email);
        dispname.setText(first + " " + last);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar,
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
                Intent intent = new Intent(getApplicationContext(), test.class);
                startActivity(intent);
                break;
            case R.id.s_lgt:
                SharedPreferences.Editor editor = getSharedPreferences("login", MODE_PRIVATE).edit();
//                editor.remove("username");
//                editor.remove("email");
//                editor.remove("user");
                editor.clear().commit();
                editor.apply();

                finish();
                Intent i = new Intent(getApplicationContext(),Login.class);

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
}
