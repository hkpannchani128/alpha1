package ml.hpsapp.bookkeeping.dashboardtab;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ml.hpsapp.bookkeeping.Home;
import ml.hpsapp.bookkeeping.R;
import ml.hpsapp.bookkeeping.SplashActivity;

public class TabPersonalDashboard extends Fragment {
    Calendar cal= Calendar.getInstance();
    SimpleDateFormat month_date = new SimpleDateFormat("MMM");
    String current_month = month_date.format(cal.getTime());
    LayoutInflater lf;
    View view;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        lf = this.getActivity().getLayoutInflater();
        view = lf.inflate(R.layout.tab_fragment_dashboard_personal, container, false);

        mSwipeRefreshLayout = view.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent i = new Intent(getActivity(), SplashActivity.class);
                startActivity(i);
                getActivity().finish();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        FetchPersonalData();
        return view;
    }

    public void FetchPersonalData() {
        SharedPreferences prefs = getActivity().getSharedPreferences("login", Activity.MODE_PRIVATE);
        final String id = prefs.getString("id", "");
        final String firstname = prefs.getString("firstname", "");


        final TableLayout stk = view.findViewById(R.id.personal_data);
        stk.setWeightSum(5f);
        TableRow tbrow0 = new TableRow(getActivity());
        tbrow0.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tbrow0.setGravity(Gravity.CENTER_HORIZONTAL);


        TextView tv0 = new TextView(getActivity());
        tv0.setText(R.string.date);
        tv0.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        tv0.setTextColor(Color.WHITE);
        tv0.setGravity(Gravity.CENTER_HORIZONTAL);
        tbrow0.addView(tv0);

        TextView tv1 = new TextView(getActivity());
        tv1.setText(R.string.description);
        tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        tv1.setTextColor(Color.WHITE);
        tv1.setGravity(Gravity.LEFT);
        tbrow0.addView(tv1);

        TextView tv2 = new TextView(getActivity());
        tv2.setText(R.string.total);
        tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        tv2.setTextColor(Color.WHITE);
        tv2.setGravity(Gravity.LEFT);
        tbrow0.addView(tv2);

        TextView tv3 = new TextView(getActivity());
        tv3.setText(R.string.initials);
        tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        tv3.setTextColor(Color.WHITE);
        tv3.setGravity(Gravity.LEFT);
        tbrow0.addView(tv3);

        TextView tv4 = new TextView(getActivity());
        tv4.setText(firstname + id);
        tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        tv4.setTextColor(Color.WHITE);
        tv4.setGravity(Gravity.LEFT);
        tbrow0.addView(tv4);

        stk.addView(tbrow0, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));


        SharedPreferences prefsjsn = getActivity().getSharedPreferences("jsndata", Activity.MODE_PRIVATE);
        final String personaldata = prefsjsn.getString("personaldata", "");

        try {
            JSONObject ob = new JSONObject(personaldata);
            JSONArray jsonArray = ob.getJSONArray(current_month);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String date = jsonObject.getString("date");
                String desc = jsonObject.getString("desc");
                String total = jsonObject.getString("total");
                String initials = jsonObject.getString("initials");
                String firstnameid = jsonObject.getString(firstname + id);
//                if (date.equals("sum")){
//                    continue;
//                }
                if (!date.equals("") && !String.format("%.2f", Float.parseFloat(firstnameid)).equals("0.00")) {
                    TableRow tbrow = new TableRow(getActivity().getApplicationContext());
                    tbrow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                    TextView t1v = new TextView(getActivity().getApplicationContext());
                    t1v.setText("" + date);
                    t1v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                    t1v.setTextColor(Color.WHITE);
                    t1v.setGravity(Gravity.CENTER_HORIZONTAL);
                    tbrow.addView(t1v);

                    TextView t2v = new TextView(getActivity().getApplicationContext());
                    t2v.setText("" + desc);
                    t2v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                    t2v.setTextColor(Color.WHITE);
                    t2v.setGravity(Gravity.LEFT);
                    tbrow.addView(t2v);

                    TextView t3v = new TextView(getActivity().getApplicationContext());
                    if (total.length() > 5) {
                        total = null;
                    }
                    t3v.setText(total);
                    t3v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                    t3v.setTextColor(Color.WHITE);
                    t3v.setGravity(Gravity.LEFT);
                    tbrow.addView(t3v);

                    TextView t4v = new TextView(getActivity().getApplicationContext());
                    t4v.setText(initials);
                    t4v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                    t4v.setTextColor(Color.WHITE);
                    t4v.setGravity(Gravity.LEFT);
                    tbrow.addView(t4v);

                    TextView t5v = new TextView(getActivity().getApplicationContext());
                    t5v.setText(String.format("%.2f", Float.parseFloat(firstnameid)));
                    t5v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                    if (Float.parseFloat(firstnameid) >= 0) {
                        t5v.setTextColor(Color.GREEN);
                    } else {
                        t5v.setTextColor(Color.RED);
                    }

                    t5v.setGravity(Gravity.LEFT);
                    tbrow.addView(t5v);


                    stk.addView(tbrow, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
