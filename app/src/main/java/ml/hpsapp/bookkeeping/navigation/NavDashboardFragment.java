package ml.hpsapp.bookkeeping.navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import ml.hpsapp.bookkeeping.Home;
import ml.hpsapp.bookkeeping.R;
import ml.hpsapp.bookkeeping.dashboardtab.PagerAdapter;

import static android.content.Context.MODE_PRIVATE;

public class NavDashboardFragment  extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FetchFlatData();
        FetchPersonalData();
        View view = inflater.inflate(R.layout.nav_fragment_dashboard, container, false);


        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.summary));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.flat));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.personal));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = view.findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        getActivity().setTitle(R.string.dashboard);
        return view;
    }


    private void StoreFlatData(String response) {
        SharedPreferences.Editor editor = getActivity().getApplicationContext().getSharedPreferences("jsndata", MODE_PRIVATE).edit();
        editor.putString("flatdata", response);
        editor.apply();
        editor.commit();
    }

    private void StorePersonalData(String response) {
        SharedPreferences.Editor editor = getActivity().getApplicationContext().getSharedPreferences("jsndata", MODE_PRIVATE).edit();
        editor.putString("personaldata", response);
        editor.apply();
        editor.commit();
    }

    public void FetchFlatData() {
        String JSON_URL = "https://script.google.com/macros/s/AKfycbz-wEuE3k8xwQTVEXxu_-YiwskBpRCiS82j5F7hv7ZwMgMXM_g/exec?id=1oAc4mp7cQnah97HJ7XK00ZDwb83HvOHrJlYzHZMy-Bg&sheet=JAN";
        SharedPreferences prefs = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        final String id = prefs.getString("id", "");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        StoreFlatData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Getting error Please Check Network Connection", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    public void FetchPersonalData() {
        String JSON_URL = "https://script.google.com/macros/s/AKfycbz-wEuE3k8xwQTVEXxu_-YiwskBpRCiS82j5F7hv7ZwMgMXM_g/exec?id=1DaW0kQA2p4leMtmIIOrZqyXUl8Voh2O84yYX4E9YuY4&sheet=JAN";
        SharedPreferences prefs = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        final String firstname = prefs.getString("firstname", "");
        final String id = prefs.getString("id", "");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        StorePersonalData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Getting error Please Check Network Connection", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }


}