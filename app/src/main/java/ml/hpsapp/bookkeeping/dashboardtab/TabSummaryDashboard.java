package ml.hpsapp.bookkeeping.dashboardtab;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ml.hpsapp.bookkeeping.Home;
import ml.hpsapp.bookkeeping.R;
import ml.hpsapp.bookkeeping.SplashActivity;

import static android.content.Context.MODE_PRIVATE;

public class TabSummaryDashboard extends Fragment {
    Calendar cal= Calendar.getInstance();
    SimpleDateFormat month_date = new SimpleDateFormat("MMM");
    String current_month = month_date.format(cal.getTime());
    TextView flat_bal, personal_bal, total_bal, insdate, inssub, insamt;
    Button inssubmit;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater lf = this.getActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.tab_fragment_dashboard_summary, container, false);

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

        flat_bal = view.findViewById(R.id.total_flat_bal);
        personal_bal = view.findViewById(R.id.total_personal_bal);
        total_bal = view.findViewById(R.id.total_bal_ans);

        insdate = view.findViewById(R.id.insdate);
        inssub = view.findViewById(R.id.inssub);
        insamt = view.findViewById(R.id.insamt);
        inssubmit = view.findViewById(R.id.inssubmit);

        inssubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inssubmit.setEnabled(false);
                SharedPreferences prefs = getActivity().getSharedPreferences("login", Activity.MODE_PRIVATE);
                final String firstname = prefs.getString("firstname", "");
                String date = insdate.getText().toString();
                String sub = inssub.getText().toString();
                String amt = insamt.getText().toString();

                Boolean validate = ValidateData(date, sub, amt);
                if (validate) {
                    InsertData ob_insdata = new InsertData();
                    ob_insdata.execute("insertdata", firstname, date, sub, amt);
                } else {
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("INVALID DATA!!!")
                            .setMessage("Please verify data before submit")
                            .setCancelable(true)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                }

            }
        });

        TabSummaryDashboardBW ob_tab1 = new TabSummaryDashboardBW();
        ob_tab1.execute();


        return view;
    }


    public boolean ValidateData(String... data) {
        if (!data[0].equals("") && !data[1].equals("") && !data[2].equals("")) {
            if (Integer.parseInt(data[0]) > 0 && Integer.parseInt(data[0]) < 32 && Integer.parseInt(data[2]) > 0) {
                return true;
            }
        }
        return false;
    }

    public class TabSummaryDashboardBW extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            Float flat, personal;
            while (flat_bal.getText().toString().equals("N/A") && personal_bal.getText().toString().equals("N/A")) {
                try {
                    Thread.sleep(0);
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
            SharedPreferences prefs = getActivity().getSharedPreferences("login", MODE_PRIVATE);
            SharedPreferences prefsjsn = getActivity().getSharedPreferences("jsndata", MODE_PRIVATE);


            final String flatdata = prefsjsn.getString("flatdata", "");
            final String id = prefs.getString("id", "");
            try {
                JSONObject ob = new JSONObject(flatdata);
                JSONArray jsonArray = ob.getJSONArray(current_month);
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
            SharedPreferences prefs = getActivity().getSharedPreferences("login", MODE_PRIVATE);
            SharedPreferences prefsjsn = getActivity().getSharedPreferences("jsndata", MODE_PRIVATE);
            final String firstname = prefs.getString("firstname", "");
            final String id = prefs.getString("id", "");
            final String personaldata = prefsjsn.getString("personaldata", "");

            try {
                JSONObject ob = new JSONObject(personaldata);
                JSONArray jsonArray = ob.getJSONArray(current_month);
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

    public class InsertData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... data) {
            String type = data[0];
            String username = data[1];
            String date = data[2];
            String subject = data[3];
            String amount = data[4];
            String login_url = "https://script.google.com/macros/s/AKfycbwJ46_IDxMuKSxto51-gfUTVQEU2SxBT2ig84BzDg_c2r8cGuc/exec";
            if (type.equals("insertdata")) try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8") + "&" + URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(subject, "UTF-8") + "&" + URLEncoder.encode("amount", "UTF-8") + "=" + URLEncoder.encode(amount, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
//            Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            try {
                JSONObject ob = new JSONObject(result);
                JSONArray array = ob.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    String res = jsonObject.getString("result");
                    Toast.makeText(getContext(), res, Toast.LENGTH_LONG).show();
                    if (res.equals("success")) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Success!!!")
                                .setMessage("Data recorded successfully")
                                .setCancelable(true)
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).show();
                        insdate.setText("");
                        inssub.setText("");
                        insamt.setText("");
                        inssubmit.setEnabled(true);
                    } else {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Failed!!!")
                                .setMessage("Error in recording Data.Please contact the developer")
                                .setCancelable(true)
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}