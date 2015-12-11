package ee.menu24.deliverymeal.app.main.title.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ee.menu24.deliverymeal.app.R;
import ee.menu24.deliverymeal.app.main.thread.Count;

/**
 * Created by 1 on 30.10.2015.
 */
public class LoadPageFragment extends Fragment {

    private Count count;
    private TextView dynamicTextView;
    private int data;
    private Thread thread;
    private boolean stateOnPause;

    public void setCount(Count count) {
        this.count = count;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.percent_fragment, container, false);
        dynamicTextView = (TextView) view.findViewById(R.id.dynamicText);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        data = 0;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {

                        synchronized (count) {

                            LoadPageFragment.this.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (stateOnPause)
                                        return;
                                    dynamicTextView.setText(String.valueOf(data));
                                }
                            });

                            data++;

                            if (data >= count.getData()) {
                                data = count.getData();
                            }

                            if (count.getData() < 100) {
                                count.wait(100);
                            }
                            else {
                                count.wait(10);
                            }

                            if (data >= 100){
                                count.setStateData(true);
                                break;
                            }
                        }

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        synchronized (count) {
            count.setStateLoadFragment(true);
            count.notifyAll();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        count = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        stateOnPause = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        stateOnPause = false;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//        if (thread != null){
//            thread.interrupt();
//            thread = null;
//        }
//
//        count = null;
    }
}
