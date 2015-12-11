package ee.menu24.deliverymeal.app.main.menu.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ee.menu24.deliverymeal.app.R;

/**
 * Created by 1 on 27.10.2015.
 */
public class SignatureFragment extends Fragment  {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_signature, container, false);

        Typeface geometric = Typeface.createFromAsset(getActivity().getAssets(), "fonts/geometric/geometric_706_black.ttf");

        ((TextView) view.findViewById(R.id.email_id)).setTypeface(geometric);
        ((TextView) view.findViewById(R.id.number_id)).setTypeface(geometric);

        return view;
    }
}
