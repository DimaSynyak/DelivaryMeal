package ee.menu24.deliverymeal.app.main.menu;

import android.graphics.Typeface;
import android.media.Image;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ee.menu24.deliverymeal.app.R;
import ee.menu24.deliverymeal.app.main.MainActivity;
import ee.menu24.deliverymeal.app.restaurant.RestaurantActivity;
import ee.menu24.deliverymeal.app.restaurant.service.filter.FilterFragment;
import ee.menu24.deliverymeal.app.restaurant.service.filter.FilterItemFragment;
import ee.menu24.deliverymeal.app.main.thread.ChangeLocale;
import ee.menu24.deliverymeal.app.main.title.Language;
import ee.menu24.deliverymeal.app.restaurant.service.filter.FilterData;
import ee.menu24.deliverymeal.app.restaurant.service.filter.RestaurantFilter;

import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.List;

/**
 * Created by 1 on 30.10.2015.
 */
public class SlidingMenuConfig {

    private FragmentActivity activity;
    private FragmentTransaction ft;


    private LinearLayout categoryLayout;
    private LinearLayout criteriaLayout;
    private LinearLayout categoryContainer;
    private LinearLayout criteriaContainer;
    private EditText editText;

    private Thread changeLocalThread;
    private ChangeLocale changeLocale;

    private static SlidingMenuConfig slidingMenuConfig;

    private Language language;

    private RestaurantFilter filter;

    public SlidingMenuConfig(FragmentActivity activity) {
        this.activity = activity;
        language = Language.getInstance();
        slidingMenuConfig = this;
    }

    public void initSlidingMenu(){
        Typeface geometric = Typeface.createFromAsset(activity.getAssets(), "fonts/geometric/geometric_706_black.ttf");
        Typeface arimo = Typeface.createFromAsset(activity.getAssets(), "fonts/arimo/Arimo_Regular.ttf");

        final SlidingMenu menu = new SlidingMenu(activity);
        MainActivity.setCustomViewAbove(menu.getmViewAbove());

        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.menu_main);



        categoryLayout = (LinearLayout) activity.findViewById(R.id.category_layout);
        criteriaLayout = (LinearLayout) activity.findViewById(R.id.criteria_layout);
        categoryContainer = (LinearLayout) activity.findViewById(R.id.category_container);
        categoryContainer.setVisibility(View.GONE);
        criteriaContainer = (LinearLayout) activity.findViewById(R.id.criteria_container);
        criteriaContainer.setVisibility(View.GONE);
        editText = (EditText) activity.findViewById(R.id.editText);

        TextView categoryText = (TextView) activity.findViewById(R.id.categoryText);
        categoryText.setTypeface(geometric);

        TextView criteriaText = (TextView) activity.findViewById(R.id.criteriaText);
        criteriaText.setTypeface(geometric);

        TextView searchButton = (TextView) activity.findViewById(R.id.search_button);
        searchButton.setTypeface(geometric);

        searchButton.setOnClickListener(new View.OnClickListener() {
            private CustomViewAbove customViewAbove;
            @Override
            public void onClick(View v) {

                customViewAbove = MainActivity.getCustomViewAbove();
                if(customViewAbove != null)
                    customViewAbove.setCurrentItem(1);

                RestaurantFilter.getSearchData().setText(String.valueOf(editText.getText()));
                RestaurantFilter.getSearchData().setStateUse(true);

                if (changeLocalThread != null){
                    changeLocalThread.interrupt();
                    changeLocalThread = null;
                }

                changeLocale = new ChangeLocale(((AppCompatActivity)activity));
                changeLocale.setUrl(language.getURL());

                changeLocalThread = new Thread(changeLocale);
                changeLocalThread.start();
            }
        });

        initListeners();
    }

    private void initListeners(){

        filter = RestaurantFilter.getInstance();

        addFilterData();

        categoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryContainer.getVisibility() == View.VISIBLE) {
                    categoryContainer.setVisibility(View.GONE);
                } else {
                    categoryContainer.setVisibility(View.VISIBLE);
                    criteriaContainer.setVisibility(View.GONE);
                }

            }
        });

        criteriaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (criteriaContainer.getVisibility() == View.VISIBLE) {
                    criteriaContainer.setVisibility(View.GONE);
                } else {
                    criteriaContainer.setVisibility(View.VISIBLE);
                    categoryContainer.setVisibility(View.GONE);
                }
            }
        });


    }

    public void addFilterData(){
        List<FilterData> dataList = filter.getCategoryList();
        ft = activity.getSupportFragmentManager().beginTransaction();
        for (FilterData filterData : dataList) {
            FilterItemFragment filterItemFragment = new FilterItemFragment();
            filterItemFragment.setFilterData(filterData);
            filterItemFragment.setResIdContainer(R.id.filterLayout);

            ft.add(R.id.category_container, filterItemFragment);
        }
        ft.commit();

        dataList = filter.getCriteriaList();
        ft = activity.getSupportFragmentManager().beginTransaction();
        for (FilterData filterData : dataList) {
            FilterItemFragment filterItemFragment = new FilterItemFragment();
            filterItemFragment.setFilterData(filterData);
            filterItemFragment.setResIdContainer(R.id.filterLayout);

            ft.add(R.id.criteria_container, filterItemFragment);
        }
        ft.commit();
    }

    public void updateFilterData(){
        removeFilterData();
        addFilterData();
    }

    public void removeFilterData(){
        List<FilterData> dataList = filter.getCategoryList();

        for (FilterData filterData : dataList) {
            FilterItemFragment filterItemFragment = filterData.getFilterItemFragment();

            if (filterItemFragment != null && filterItemFragment.isAdded()){
                ft = activity.getSupportFragmentManager().beginTransaction();
                ft.remove(filterItemFragment);
                ft.commit();
            }

            FilterFragment filterFragment = filterData.getFilterFragment();
            if (filterFragment !=null && filterFragment.isAdded()){
                ft = activity.getSupportFragmentManager().beginTransaction();
                ft.remove(filterFragment);
                ft.commit();
            }

        }



        dataList = filter.getCriteriaList();

        for (FilterData filterData : dataList) {
            FilterItemFragment filterItemFragment = filterData.getFilterItemFragment();

            if (filterItemFragment != null && filterItemFragment.isAdded()){
                ft = activity.getSupportFragmentManager().beginTransaction();
                ft.remove(filterItemFragment);
                ft.commit();
            }


            FilterFragment filterFragment = filterData.getFilterFragment();
            if (filterFragment !=null && filterFragment.isAdded()){
                ft = activity.getSupportFragmentManager().beginTransaction();
                ft.remove(filterFragment);
                ft.commit();
            }

        }

        filter.clear();
    }

    public static SlidingMenuConfig getSlidingMenuConfig() {
        return slidingMenuConfig;
    }

    public static void setSlidingMenuConfig(SlidingMenuConfig slidingMenuConfig) {
        SlidingMenuConfig.slidingMenuConfig = slidingMenuConfig;
    }
}
