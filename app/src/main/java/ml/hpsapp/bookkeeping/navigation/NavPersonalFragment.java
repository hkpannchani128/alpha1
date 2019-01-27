package ml.hpsapp.bookkeeping.navigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import ml.hpsapp.bookkeeping.R;

public class NavPersonalFragment extends Fragment {
    WebView personalweb;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav_fragment_personal, container, false);


        personalweb=view.findViewById(R.id.personalweb);
        personalweb.getSettings().setJavaScriptEnabled(true);
        personalweb.getSettings().setUseWideViewPort(true);

        personalweb.loadUrl("https://docs.google.com/spreadsheets/d/e/2PACX-1vSxjaIwwl732Q4LhtKjg8Hcdx0V7i9YxR18PwEZyZFxZ2_6snbfXuOqVnEXro45WN43NEcv5U6njady/pubhtml?widget=true&amp;headers=false");


        getActivity().setTitle(R.string.personal);

        return view;
    }
}