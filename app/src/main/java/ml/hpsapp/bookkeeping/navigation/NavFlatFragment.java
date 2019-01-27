package ml.hpsapp.bookkeeping.navigation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
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

import java.util.Iterator;

import ml.hpsapp.bookkeeping.R;

import static android.content.Context.MODE_PRIVATE;

public class NavFlatFragment extends Fragment {
    Spinner monthlist;
    WebView flatweb;
    TableLayout stk;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.nav_fragment_flat, container, false);
//        monthlist=v.findViewById(R.id.monthlist);
//        stk= v.findViewById(R.id.flat_data);
//        ArrayAdapter<String> monthlistadpt = new ArrayAdapter<>(getActivity(),
//                android.R.layout.simple_expandable_list_item_1, getResources().getStringArray(R.array.monthlist));
//        monthlistadpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        monthlist.setAdapter(monthlistadpt);
//
//        monthlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String month=getResources().getStringArray(R.array.monthlist)[position];
////                Toast.makeText(getContext(), month, Toast.LENGTH_SHORT).show();
//                FetchFlatData(month);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        flatweb=v.findViewById(R.id.flatweb);
        flatweb.getSettings().setJavaScriptEnabled(true);
        flatweb.getSettings().setUseWideViewPort(true);
        flatweb.loadUrl("https://docs.google.com/spreadsheets/d/e/2PACX-1vRvaXvppVFn9xTOBuYtsxjy18jEuuMETVywfwSTbfgPa06SrMLvy0u68HxMATLRj8-czYI-a-4LsT6s/pubhtml?widget=true&amp;headers=false");




        getActivity().setTitle(R.string.flat);
        return v;
    }

//    public void FetchFlatData(final String month) {
//        String JSON_URL = "https://script.google.com/macros/s/AKfycbz-wEuE3k8xwQTVEXxu_-YiwskBpRCiS82j5F7hv7ZwMgMXM_g/exec?id=1oAc4mp7cQnah97HJ7XK00ZDwb83HvOHrJlYzHZMy-Bg&sheet="+month;
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            SetJsonAsTable(response,month);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
////                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getContext(), "Getting error Please Check Network Connection", Toast.LENGTH_SHORT).show();
//                    }
//                });
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        requestQueue.add(stringRequest);
//
//    }

//    public void SetJsonAsTable(String jsndata,String month) throws JSONException {
//        JSONObject resobj  = new JSONObject(jsndata);
//        JSONArray jsonArray = resobj.getJSONArray(month);
//        String key[] = new String[25];int keycount=0;
//        JSONObject jsonObject = jsonArray.getJSONObject(0);
//        Iterator<?> iter = jsonObject.keys();
//        while (iter.hasNext()) {
//            key[keycount] = (String) iter.next();
//            keycount++;
//        }
//
//        TableRow tbrow0 = new TableRow(getActivity());
//        tbrow0.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//        tbrow0.setGravity(Gravity.CENTER_HORIZONTAL);
//
//
//    }
}