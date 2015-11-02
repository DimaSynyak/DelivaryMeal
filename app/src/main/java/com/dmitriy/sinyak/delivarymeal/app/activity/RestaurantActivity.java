package com.dmitriy.sinyak.delivarymeal.app.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.config.menu.SlidingMenuConfig;
import com.dmitriy.sinyak.delivarymeal.app.fragments.title.LanguagesTitle;
import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;

/**
 * Created by 1 on 02.11.2015.
 */
public class RestaurantActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editText;
    private SlidingMenuConfig slidingMenuConfig;
    private LanguagesTitle languagesFragment;
    private Button button;
    private CustomViewAbove customViewAbove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        setContentView(R.layout.activity_restaurant);
        getSupportActionBar().setCustomView(R.layout.title);

        editText = (EditText) findViewById(R.id.editText2);

        editText.setText(getIntent().getStringExtra("object"));

        languagesFragment = new LanguagesTitle(this);

        slidingMenuConfig = new SlidingMenuConfig(this);
        slidingMenuConfig.initSlidingMenu();
        customViewAbove = CustomViewAbove.customViewAbove;
        button = (Button) findViewById(R.id.button);
    }


    @Override
    public void onClick(View v) {
        slidingMenuConfig.onClickDp(v.getId());
        languagesFragment.onClickDp(v.getId());
        switch (v.getId()){
            case R.id.menuClick:{
                if (customViewAbove.getCurrentItem() == 1){
                    customViewAbove.setCurrentItem(0);
                }
                else {
                    customViewAbove.setCurrentItem(1);
                }
                break;
            }
            case R.id.button:{
                this.finish();
                break;
            }
            case R.id.imageView3:{
                finish();
                break;
            }
        }
    }
}
