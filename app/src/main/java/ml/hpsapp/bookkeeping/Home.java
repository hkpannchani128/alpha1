package ml.hpsapp.bookkeeping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import ml.hpsapp.bookkeeping.navigation.NavDashboardFragment;
import ml.hpsapp.bookkeeping.navigation.NavFlatFragment;
import ml.hpsapp.bookkeeping.navigation.NavPersonalFragment;

public class Home extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    TextView dispname,dispemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        dispemail=findViewById(R.id.dispemail);
        dispname=findViewById(R.id.dispname);

//        Intent in=getIntent();
//       String email= in.getStringExtra("email").toString();

//        dispemail.setText(email);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.s_drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
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
                Toast.makeText(this, R.string.share, Toast.LENGTH_SHORT).show();
                break;
            case R.id.s_lgt:
                SharedPreferences.Editor editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                editor.remove("username");

                editor.apply();

                finish();
                Intent i = new Intent(getApplicationContext(),Login.class);

                startActivity(i);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
