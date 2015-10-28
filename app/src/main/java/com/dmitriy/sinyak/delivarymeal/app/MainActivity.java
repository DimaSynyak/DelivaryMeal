package com.dmitriy.sinyak.delivarymeal.app;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import com.dmitriy.sinyak.delivarymeal.app.fragments.ButtonFragment;
import com.dmitriy.sinyak.delivarymeal.app.fragments.NullFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private boolean flag;
    private ButtonFragment fragment_button;
    private NullFragment null_fragment;
    private FrameLayout frameFragment;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameFragment =(FrameLayout) findViewById(R.id.frameFragment);
//        fragment_button = getSupportFragmentManager().findFragmentById(R.id.button_fragment);
//        fragment_layout = (LinearLayout) findViewById(R.id.fragment_layout);
        fragment_button = new ButtonFragment();
        null_fragment = new NullFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_in_bottom);
        ft.replace(frameFragment.getId(), fragment_button);
        ft.addToBackStack(null);


        // configure the SlidingMenu

        SlidingMenu menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);

        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        menu.setMenu(R.layout.menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:{

                ft = getSupportFragmentManager().beginTransaction();


                if (!flag){
                    ft.setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_in_bottom);
                    flag = true;
                    ft.replace(R.id.frameFragment, fragment_button);
                }
                else{
                    ft.setCustomAnimations(R.anim.abc_slide_out_top, R.anim.abc_slide_out_bottom);
                    ft.replace(R.id.frameFragment, null_fragment);
                    flag = false;
                }

                ft.commit();
            }
        }
    }
}
