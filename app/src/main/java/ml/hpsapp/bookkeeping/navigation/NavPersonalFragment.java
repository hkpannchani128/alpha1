package ml.hpsapp.bookkeeping.navigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ml.hpsapp.bookkeeping.R;

public class NavPersonalFragment extends Fragment {

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.nav_fragment_personal, container, false);



        getActivity().setTitle(R.string.personal);

        return inflater.inflate(R.layout.nav_fragment_personal, container, false);
    }
}