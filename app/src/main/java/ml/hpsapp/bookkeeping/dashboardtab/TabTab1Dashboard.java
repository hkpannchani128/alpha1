package ml.hpsapp.bookkeeping.dashboardtab;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import ml.hpsapp.bookkeeping.R;

public class TabTab1Dashboard extends Fragment {
    TextView flat_bal, personal_bal, total_bal;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater lf = this.getActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.tab_fragment_dashboard_tab1, container, false);

        flat_bal = view.findViewById(R.id.total_flat_bal);
        personal_bal = view.findViewById(R.id.total_personal_bal);
        total_bal = view.findViewById(R.id.total_bal_ans);

        TabTab1DashboardBW ob_tab1 = new TabTab1DashboardBW();
        ob_tab1.execute();


        return view;
    }


    public class TabTab1DashboardBW extends AsyncTask<String, String, String> {

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
//            return Float.toString(flat+personal);
        }

        @Override
        protected void onPreExecute() {
            FetchFlatTotal();
            FetchPersonalTotal();
        }

        @Override
        protected void onPostExecute(String result) {
            total_bal.setText(result);
            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        public void FetchFlatTotal() {
            String JSON_URL = "https://script.google.com/macros/s/AKfycbz-wEuE3k8xwQTVEXxu_-YiwskBpRCiS82j5F7hv7ZwMgMXM_g/exec?id=1oAc4mp7cQnah97HJ7XK00ZDwb83HvOHrJlYzHZMy-Bg&sheet=JAN";
            SharedPreferences prefs = getActivity().getSharedPreferences("login", Activity.MODE_PRIVATE);
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
                                        flat_bal.setText(String.format("%.2f", Float.parseFloat(cost)));
                                        break;
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
                            Toast.makeText(getActivity(), "Getting error Please Check Network Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);

        }

        public void FetchPersonalTotal() {
            String JSON_URL = "https://script.google.com/macros/s/AKfycbz-wEuE3k8xwQTVEXxu_-YiwskBpRCiS82j5F7hv7ZwMgMXM_g/exec?id=1DaW0kQA2p4leMtmIIOrZqyXUl8Voh2O84yYX4E9YuY4&sheet=JAN";
            SharedPreferences prefs = getActivity().getSharedPreferences("login", Activity.MODE_PRIVATE);
            final String firstname = prefs.getString("firstname", "");
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
                                    String record = jsonObject.getString("date");
                                    String cost = jsonObject.getString(firstname + id);

                                    if (record.equals("sum")) {
                                        personal_bal.setText(String.format("%.2f", Float.parseFloat(cost)));
                                        break;
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
                            Toast.makeText(getActivity(), "Getting error Please Check Network Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);

        }
    }

}