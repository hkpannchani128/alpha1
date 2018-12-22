package ml.hpsapp.bookkeeping.dashboardtab;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import ml.hpsapp.bookkeeping.R;

public class TabFlatDashboard extends Fragment {
    LayoutInflater lf;
    View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        lf = this.getActivity().getLayoutInflater();
        view = lf.inflate(R.layout.tab_fragment_dashboard_flat, container, false);


        FetchFlatData();
        return view;
    }

    public void FetchFlatData() {
        final TableLayout stk = view.findViewById(R.id.flat_data);
        stk.setWeightSum(4f);
        TableRow tbrow0 = new TableRow(getActivity());
        tbrow0.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tbrow0.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView tv0 = new TextView(getActivity());
        tv0.setText(R.string.record);
        tv0.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        tv0.setTextColor(Color.WHITE);
        tv0.setGravity(Gravity.CENTER);
        tbrow0.addView(tv0);

        TextView tv1 = new TextView(getActivity());
        tv1.setText(R.string.subject);
        tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 2f));
        tv1.setTextColor(Color.WHITE);
        tv1.setGravity(Gravity.LEFT);
        tbrow0.addView(tv1);

        TextView tv2 = new TextView(getActivity().getApplicationContext());
        tv2.setText(R.string.cost);
        tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        tv2.setTextColor(Color.WHITE);
        tv2.setGravity(Gravity.LEFT);
        tbrow0.addView(tv2);

        stk.addView(tbrow0, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));


        SharedPreferences prefs = getActivity().getSharedPreferences("login", Activity.MODE_PRIVATE);
        final String id = prefs.getString("id", "");
        SharedPreferences prefsjsn = getActivity().getSharedPreferences("jsndata", Activity.MODE_PRIVATE);
        final String flatdata = prefsjsn.getString("flatdata", "");
        final String firstname = prefs.getString("firstname", "");

        try {
            JSONObject ob = new JSONObject(flatdata);
            JSONArray jsonArray = ob.getJSONArray("JAN");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String record = jsonObject.getString("Record");
                String name = jsonObject.getString(firstname);
                String cost = jsonObject.getString("cost" + id);
                if (record.equals("monthtotal")) {
                    break;
                }
                if (!record.equals("monthtotal")) {
                    TableRow tbrow = new TableRow(getActivity().getApplicationContext());
                    tbrow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                    TextView t1v = new TextView(getActivity().getApplicationContext());
                    t1v.setText("" + record);
                    t1v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                    t1v.setTextColor(Color.WHITE);
                    t1v.setGravity(Gravity.CENTER);
                    tbrow.addView(t1v);

                    TextView t2v = new TextView(getActivity().getApplicationContext());
                    t2v.setText("" + name);
                    t2v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 2f));
                    t2v.setTextColor(Color.WHITE);
                    t2v.setGravity(Gravity.LEFT);
                    tbrow.addView(t2v);

                    TextView t3v = new TextView(getActivity().getApplicationContext());
                    t3v.setText(cost);
                    t3v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                    t3v.setTextColor(Color.WHITE);
                    t3v.setGravity(Gravity.LEFT);
                    tbrow.addView(t3v);
                    stk.addView(tbrow, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
