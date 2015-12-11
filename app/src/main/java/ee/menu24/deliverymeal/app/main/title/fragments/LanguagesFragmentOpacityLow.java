package ee.menu24.deliverymeal.app.main.title.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ee.menu24.deliverymeal.app.R;


/**
 * Created by 1 on 30.10.2015.
 */
public class LanguagesFragmentOpacityLow extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.languages_fragment1, container, false);
    }
}
