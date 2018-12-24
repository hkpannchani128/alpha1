package ml.hpsapp.bookkeeping.dashboardtab;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ml.hpsapp.bookkeeping.Home;
import ml.hpsapp.bookkeeping.R;
import ml.hpsapp.bookkeeping.navigation.NavDashboardFragment;

public class TabSummaryDashboard extends Fragment {
    TextView flat_bal, personal_bal, total_bal;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater lf = this.getActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.tab_fragment_dashboard_summary, container, false);

        mSwipeRefreshLayout = view.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent i = new Intent(getActivity(), Home.class);
                startActivity(i);
                getActivity().finish();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        flat_bal = view.findViewById(R.id.total_flat_bal);
        personal_bal = view.findViewById(R.id.total_personal_bal);
        total_bal = view.findViewById(R.id.total_bal_ans);

        TabSummaryDashboardBW ob_tab1 = new TabSummaryDashboardBW();
        ob_tab1.execute();


        return view;
    }


    public class TabSummaryDashboardBW extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            Float flat, personal;
            while (flat_bal.getText().toString().equals("N/A") && personal_bal.getText().toString().equals("N/A")) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            flat = Float.parseFloat(flat_bal.getText().toString());
            personal = Float.parseFloat(personal_bal.getText().toString());
            return String.format("%.2f", flat + personal);
//            return Float.toString(1000);
        }

        @Override
        protected void onPreExecute() {
            FetchFlatTotal();
            FetchPersonalTotal();
        }

        @Override
        protected void onPostExecute(String result) {
            total_bal.setText(result);
            if (Float.parseFloat(result) >= 0) {
                total_bal.setTextColor(Color.GREEN);
            } else {
                total_bal.setTextColor(Color.RED);
            }
//            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        public void FetchFlatTotal() {
            SharedPreferences prefs = getActivity().getSharedPreferences("login", Activity.MODE_PRIVATE);
            SharedPreferences prefsjsn = getActivity().getSharedPreferences("jsndata", Activity.MODE_PRIVATE);


            final String flatdata = prefsjsn.getString("flatdata", "");
            final String id = prefs.getString("id", "");
            try {
                JSONObject ob = new JSONObject(flatdata);
                JSONArray jsonArray = ob.getJSONArray("JAN");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String record = jsonObject.getString("Record");
                    String cost = jsonObject.getString("cost" + id);

                    if (record.equals("SETTLE")) {
                        flat_bal.setText(String.format("%.2f", Float.parseFloat(cost)));
                        if (Float.parseFloat(cost) >= 0) {
                            flat_bal.setTextColor(Color.GREEN);
                        } else {
                            flat_bal.setTextColor(Color.RED);
                        }
                        break;
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public void FetchPersonalTotal() {
            SharedPreferences prefs = getActivity().getSharedPreferences("login", Activity.MODE_PRIVATE);
            SharedPreferences prefsjsn = getActivity().getSharedPreferences("jsndata", Activity.MODE_PRIVATE);
            final String firstname = prefs.getString("firstname", "");
            final String id = prefs.getString("id", "");
            final String personaldata = prefsjsn.getString("personaldata", "");

            try {
                JSONObject ob = new JSONObject(personaldata);
                JSONArray jsonArray = ob.getJSONArray("JAN");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String record = jsonObject.getString("date");
                    String cost = jsonObject.getString(firstname + id);

                    if (record.equals("sum")) {
                        personal_bal.setText(String.format("%.2f", Float.parseFloat(cost)));
                        if (Float.parseFloat(cost) >= 0) {
                            personal_bal.setTextColor(Color.GREEN);
                        } else {
                            personal_bal.setTextColor(Color.RED);
                        }
                        break;
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}