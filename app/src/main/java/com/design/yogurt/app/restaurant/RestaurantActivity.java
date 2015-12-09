package com.design.yogurt.app.restaurant;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.design.yogurt.app.IActivity;
import com.design.yogurt.app.main.fragments.AddressDataFragment;
import com.design.yogurt.app.main.service.Restaurant;
import com.design.yogurt.app.main.service.RestaurantList;
import com.design.yogurt.app.main.title.Language;
import com.design.yogurt.app.main.title.Languages;
import com.design.yogurt.app.main.title.fragments.LanguagesFragmentOpacityLow;
import com.design.yogurt.app.main.title.fragments.LanguagesTitle;
import com.design.yogurt.app.restaurant.head.StateMenu;
import com.design.yogurt.app.restaurant.service.DelivaryData;
import com.design.yogurt.app.restaurant.head.RestaurantHeadFragment;
import com.design.yogurt.app.restaurant.head.RestaurantMiniHeadFragment;
import com.design.yogurt.app.restaurant.menu.SMCRestaurantActivity;
import com.design.yogurt.app.restaurant.service.Garbage;
import com.design.yogurt.app.restaurant.service.MealList;
import com.design.yogurt.app.restaurant.service.RegistrationData;
import com.design.yogurt.app.restaurant.service.filter.MealFilter;
import com.design.yogurt.app.restaurant.thread.ChangeLanguageAsyncTask;
import com.design.yogurt.app.restaurant.thread.RestaurantAsyncTask;
import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by 1 on 02.11.2015.
 */
public class RestaurantActivity extends AppCompatActivity implements View.OnClickListener, IActivity {
    private SMCRestaurantActivity slidingMenuConfig;
    private Language language;
    private static CustomViewAbove customViewAbove;
    private RestaurantHeadFragment restaurantHeadFragment;
    private RestaurantMiniHeadFragment restaurantMiniHeadFragment;
    private ScrollView scrollView;
    private FrameLayout restaurantHeadContainer;
    private int languageContainerId;
    private static int MIN_SCROLLY = -100;
    private static int MAX_SCROLLY = 300;
    private RestaurantActivity restaurantActivity;
    private Restaurant restaurant;
    private RestaurantAsyncTask restaurantAsyncTask;
    private Languages oldLanguage;
    private RegistrationData registrationData;

    private int positionRestaurant;
    private ChangeLanguageAsyncTask changeLocale;
    private DisplayMetrics metrics;
    private ImageView garbageButton;
    private TextView garbageNum;
    private Garbage garbage;
    private boolean garbageFlag;

    private TextView specializationField;
    private TextView workDayField;
    private TextView workTimeField;
    private TextView workTimeField2;
    private TextView specializationData;
    private TextView workDayData;
    private TextView workTimesData;
    private TextView workTimesData2;

    private TextView titleDescription;
    private TextView description;
    private TextView titleBranchOffices;
    private LinearLayout infoLayout;
    private AddressDataFragment addressDataFragment;
    private LanguagesFragmentOpacityLow languagesFragmentOpacityLow;
    private LanguagesTitle languagesTitle;
    private RestaurantList restaurantList;

    private Typeface geometric;
    private Typeface arimo;

    private static List<IRestaurantActivityDestroy> iDestroies;

    private DelivaryData delivaryData;


    private boolean firstFlag = true;
    private boolean onResumeFlag;


    private FragmentTransaction ft;
    private MealFilter mealFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        setContentView(R.layout.activity_restaurant);
        getSupportActionBar().setCustomView(R.layout.title);

        geometric = Typeface.createFromAsset(getAssets(), "fonts/geometric/geometric_706_black.ttf");
        arimo = Typeface.createFromAsset(getAssets(), "fonts/arimo/Arimo_Regular.ttf");

         /*INIT LANGUAGE*/
        language = Language.getInstance();
        languagesTitle = language.init(R.id.restaurantLanguageContainer);
        init(language.getLanguages());
        /*END INIT LANGUAGE*/

        restaurantList = RestaurantList.getInstance();

        restaurantActivity = this;
        garbage = Garbage.getInstance();
        garbage.setActivity(this);

        restaurantHeadContainer = (FrameLayout) findViewById(R.id.restaurantHeadContainer);

        garbageButton = (ImageView) findViewById(R.id.garbageButton);
        garbageNum = (TextView) findViewById(R.id.garbageNum);

        scrollView = (ScrollView) findViewById(R.id.scrollView3);
        restaurant = restaurantList.getRestaurant();

        if (restaurant == null)
            finish();

        restaurantAsyncTask = null;
        restaurantAsyncTask = new RestaurantAsyncTask(this);
        restaurantAsyncTask.execute(restaurant.getMenuLink());


        initFragment(restaurant);
        scrollInit();

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        garbageNum = (TextView) findViewById(R.id.garbageNum);
        garbageNum.setTypeface(geometric);


        delivaryData = DelivaryData.getInstance();
        restaurant = restaurantList.getRestaurant();

    }

    public void initInfo(){
        /*INFO LAYOUT*/
        specializationField = (TextView) findViewById(R.id.specialization_field);
        specializationField.setTypeface(arimo);
        specializationField.setText(restaurant.getSpecializationField());

        workDayField = (TextView) findViewById(R.id.work_day_field);
        workDayField.setTypeface(arimo);
        workDayField.setText(restaurant.getWorkDayField());

        workTimeField = (TextView) findViewById(R.id.work_time_field);
        workTimeField.setTypeface(arimo);
        if (restaurant.getWorkTimeFields() != null && restaurant.getWorkTimeFields().size() > 0) {
            workTimeField.setText(restaurant.getWorkTimeFields().get(0));
        }

        workTimeField2 = (TextView) findViewById(R.id.work_time_field2);
        workTimeField2.setTypeface(arimo);
        if (restaurant.getWorkTimeFields().size() > 1) {
            workTimeField2.setText(restaurant.getWorkTimeFields().get(1));
        }
        else {
            workTimeField2.setVisibility(TextView.GONE);
        }

        specializationData = (TextView) findViewById(R.id.specialization_data);
        specializationData.setTypeface(geometric);
        specializationData.setText(restaurant.getSpecializationData());

        workDayData = (TextView) findViewById(R.id.work_day_data);
        workDayData.setTypeface(geometric);
        workDayData.setText(restaurant.getWorkDayData());

        workTimesData = (TextView) findViewById(R.id.work_time_data);
        workTimesData.setTypeface(geometric);
        if (restaurant.getWorkTimesData() != null && restaurant.getWorkTimesData().size() > 0) {
            workTimesData.setText(restaurant.getWorkTimesData().get(0));
        }

        workTimesData2 = (TextView) findViewById(R.id.work_time_data2);
        workTimesData2.setTypeface(geometric);

        if (restaurant.getWorkTimesData() != null && restaurant.getWorkTimesData().size() > 1){
            workTimesData2.setText(restaurant.getWorkTimesData().get(1));
        }
        else {
            workTimesData2.setVisibility(TextView.GONE);
        }

        titleDescription = (TextView) findViewById(R.id.title_description);
        titleDescription.setTypeface(geometric);
        titleDescription.setText(restaurant.getTitleDescription());

        description = (TextView) findViewById(R.id.description);
        description.setTypeface(arimo);
        description.setText(restaurant.getDescription());

        titleBranchOffices = (TextView) findViewById(R.id.branches_offices);
        titleBranchOffices.setTypeface(geometric);
        titleBranchOffices.setText(restaurant.getTitleBranchOffices());

        ft = getSupportFragmentManager().beginTransaction();

        if (restaurant.getAddressDataFragmentList() == null){
            restaurant.setAddressDataFragmentList(new ArrayList<AddressDataFragment>());
        }

        for (String branchesText : restaurant.getAddressBranchOffices()) {
            AddressDataFragment addressDataFragment = new AddressDataFragment();
            restaurant.getAddressDataFragmentList().add(addressDataFragment);
            addressDataFragment.setAddressBranchesOfficesText(branchesText);
            ft.add(R.id.address_data_container, addressDataFragment);
        }
        ft.commit();

        infoLayout = (LinearLayout) findViewById(R.id.info_layout);
        infoLayout.setVisibility(LinearLayout.GONE);
    }


    public void updateInfo(){
        /*INFO LAYOUT*/

        specializationField.setText(restaurant.getSpecializationField());


        workDayField.setText(restaurant.getWorkDayField());


        if (restaurant.getWorkTimeFields() != null && restaurant.getWorkTimeFields().size() > 0) {
            workTimeField.setText(restaurant.getWorkTimeFields().get(0));
        }

        if (restaurant.getWorkTimeFields().size() > 1) {
            workTimeField2.setText(restaurant.getWorkTimeFields().get(1));
        }
        else {
            workTimeField2.setVisibility(TextView.GONE);
        }

        specializationData.setText(restaurant.getSpecializationData());


        workDayData.setText(restaurant.getWorkDayData());


        if (restaurant.getWorkTimesData() != null && restaurant.getWorkTimesData().size() > 0) {
            workTimesData.setText(restaurant.getWorkTimesData().get(0));
        }


        if (restaurant.getWorkTimesData() != null && restaurant.getWorkTimesData().size() > 1){
            workTimesData2.setText(restaurant.getWorkTimesData().get(1));
        }
        else {
            workTimesData2.setVisibility(TextView.GONE);
        }

        titleDescription.setText(restaurant.getTitleDescription());

        description.setText(restaurant.getDescription());

        titleBranchOffices.setText(restaurant.getTitleBranchOffices());



        /*remove fragment from place*/

        ft = getSupportFragmentManager().beginTransaction();

        if (restaurant.getAddressDataFragmentList() == null){
            restaurant.setAddressDataFragmentList(new ArrayList<AddressDataFragment>());
        }

        for (AddressDataFragment addressDataFragment : restaurant.getAddressDataFragmentList()) {
            ft.remove(addressDataFragment);
        }
        ft.commit();


        /*add fragment on place*/

        ft = getSupportFragmentManager().beginTransaction();

        for (String branchesText : restaurant.getAddressBranchOffices()) {
            AddressDataFragment addressDataFragment = new AddressDataFragment();
            addressDataFragment.setAddressBranchesOfficesText(branchesText);
            ft.add(R.id.address_data_container, addressDataFragment);
        }
        ft.commit();




        infoLayout = (LinearLayout) findViewById(R.id.info_layout);
        infoLayout.setVisibility(LinearLayout.GONE);
    }

    @Override
    public void onClick(View v) {
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
            case R.id.imageView3:{
                if (changeLocale != null){
                   if (!changeLocale.isCancelled())
                       return;
                }

                if (restaurantAsyncTask != null){
                    restaurantAsyncTask.getLoadPageFragment().getThread().interrupt();
                    restaurantAsyncTask.cancel(true);
                    restaurantAsyncTask = null;
                }

                finish();
                break;
            }
            case R.id.fullRestaurantImg:{
                ft = getSupportFragmentManager().beginTransaction();
                ft.remove(restaurantMiniHeadFragment);
                ft.commit();
                ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
                ft.add(R.id.restaurantHeadContainer, restaurantHeadFragment);
                ft.commit();
                firstFlag = true;
                break;
            }
            case R.id.garbageButton:{

                if (garbage.getTotal() == 0)
                    return;


                garbageFlag = true;
                customViewAbove.setCurrentItem(0);

                slidingMenuConfig.setGarbageFragment();

                break;
            }
        }
    }

    public void initFragment(Restaurant restaurant){
        restaurantHeadFragment = new RestaurantHeadFragment();
        restaurantHeadFragment.init(restaurant);

        restaurantMiniHeadFragment = new RestaurantMiniHeadFragment();
        restaurantMiniHeadFragment.init(restaurant);

        ft = getSupportFragmentManager().beginTransaction();
        if (firstFlag) {
            ft.add(R.id.restaurantHeadContainer, restaurantHeadFragment);
        }
        else {
            ft.add(R.id.restaurantHeadContainer, restaurantMiniHeadFragment);
        }

        ft.commit();
    }

    public void removeFragment(){
        restaurantHeadFragment.setRestaurant(restaurant);
        restaurantMiniHeadFragment.setRestaurant(restaurant);

        ft = getSupportFragmentManager().beginTransaction();

        if (restaurantHeadFragment.isAdded()){
            ft.remove(restaurantHeadFragment);
        }
        else if (restaurantMiniHeadFragment.isAdded()){
            ft.remove(restaurantMiniHeadFragment);
        }

        ft.commit();
    }

    public void scrollInit() {
        scrollView = (ScrollView) findViewById(R.id.scrollView3);
        final Boolean[] threadRunState = {false};
        final int[] scrollY = {scrollView.getScrollY()};

        scrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_SCROLL:
                    case MotionEvent.ACTION_MOVE:
                        if (scrollView.getScrollY() < MIN_SCROLLY && !firstFlag) {
                            firstFlag = true;
                            ft = getSupportFragmentManager().beginTransaction();
                            ft.remove(restaurantMiniHeadFragment);
                            ft.commit();
                            ft = getSupportFragmentManager().beginTransaction();
                            ft.setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
                            ft.add(R.id.restaurantHeadContainer, restaurantHeadFragment);
                            ft.commit();

                            ((MarginLayoutParams) scrollView.getLayoutParams()).setMargins(0, (int) (180 * metrics.density), 0, 0);

                        } else if (scrollView.getScrollY() > MAX_SCROLLY && firstFlag) {
                            firstFlag = false;
                            ft = getSupportFragmentManager().beginTransaction();
                            ft.remove(restaurantHeadFragment);
                            ft.commit();

                            ft = getSupportFragmentManager().beginTransaction();
                            ft.setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
                            ft.add(R.id.restaurantHeadContainer, restaurantMiniHeadFragment);
                            ft.commit();
                            ((MarginLayoutParams) scrollView.getLayoutParams()).setMargins(0, (int) (50 * metrics.density), 0, 0);
                        }
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:

                        break;
                }

                scrollY[0] = scrollView.getScrollY();
                if (!threadRunState[0] && (scrollView.getChildAt(0).getMeasuredHeight() * 2 / 3) < scrollY[0]) {
                    threadRunState[0] = true;

                    MealBody mealBody = MealBody.getInstance(RestaurantActivity.this);

                    mealBody.update(threadRunState);
                }
                return false;
            }
        });
    }


    public static void setCustomViewAbove(CustomViewAbove customViewAbove) {
        RestaurantActivity.customViewAbove = customViewAbove;
    }


    public static CustomViewAbove getCustomViewAbove() {
        return RestaurantActivity.customViewAbove;
    }


    /***************Language***********************/

    public void init(Languages languages){
        languagesFragmentOpacityLow = new LanguagesFragmentOpacityLow();
        if (!(languages == null)){

            switch (languages) {
                case RU: {
                    ((ImageView) findViewById(R.id.language_image)).setImageResource(R.drawable.language_ru);
                    break;
                }
                case EE: {
                    ((ImageView) findViewById(R.id.language_image)).setImageResource(R.drawable.language_ee);
                    break;
                }
                case EN: {
                    ((ImageView) findViewById(R.id.language_image)).setImageResource(R.drawable.language_en);
                }
            }
        }

        (findViewById(R.id.language_image)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languagesTitle.dropDownUpLanguageList(RestaurantActivity.this);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    @Override
    public void changeLanguage(Languages languages) {
        if (language.getLanguages() == languages) {
            return;
        }

        if (changeLocale != null){
            if (!changeLocale.isCancelled())
                return;
        }

        if (restaurantAsyncTask != null){
            if (!restaurantAsyncTask.isCancelled()){
                return;
            }
        }


        changeLocale = new ChangeLanguageAsyncTask(this);
        Locale myLocale = null;

        oldLanguage = language.getLanguages();
        language.setLanguages(languages);

        switch (languages){
            case RU:{
                ((ImageView) findViewById(R.id.language_image)).setImageResource(R.drawable.language_ru);
                myLocale = new Locale("ru");
                changeLocale.execute(language.getURL());
                break;
            }
            case EE:{
                ((ImageView) findViewById(R.id.language_image)).setImageResource(R.drawable.language_ee);
                myLocale = new Locale("et");
                changeLocale.execute(language.getURL());
                break;
            }
            case EN:{
                ((ImageView) findViewById(R.id.language_image)).setImageResource(R.drawable.language_en);
                myLocale = new Locale("en");
                changeLocale.execute(language.getURL());
                break;
            }
            default:
                return;
        }

        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());


        updateUI();

    }

    private void updateUI(){

        EditText search = (EditText) findViewById(R.id.editText);
        search.setHint(R.string.search);
        TextView find = (TextView) findViewById(R.id.search_button);
        find.setText(R.string.search_button);

        TextView textView26 = (TextView) findViewById(R.id.textView26);
        textView26.setText(R.string.back);

        TextView pay = (TextView) findViewById(R.id.textView25);
        pay.setText(R.string.pay);

        TextView personal_cabinet = (TextView) findViewById(R.id.personal_cabinet_text);
        personal_cabinet.setText(R.string.personal_cabinet);

        TextView yourData = (TextView) findViewById(R.id.deliverText);
        yourData.setText(R.string.your_data);

        TextView yourOrder = (TextView) findViewById(R.id.mealText);
        yourOrder.setText(R.string.your_order);

        TextView total = (TextView) findViewById(R.id.total_text1);
        total.setText(R.string.total);

        TextView nameLabel = (TextView) findViewById(R.id.name_reg_form_label);
        nameLabel.setText(R.string.name);

        TextView emailLabel = (TextView) findViewById(R.id.email_reg_form_label);
        emailLabel.setText(R.string.your_email);

        TextView passwordLabel = (TextView) findViewById(R.id.password_reg_form_label);
        passwordLabel.setText(R.string.password);

        TextView confirmPasswordLabel = (TextView) findViewById(R.id.confirm_pass_reg_form_label);
        confirmPasswordLabel.setText(R.string.confirm_password);

        TextView phoneLabel = (TextView) findViewById(R.id.phone_reg_form_label);
        phoneLabel.setText(R.string.your_phone);

        TextView countryLabel = (TextView) findViewById(R.id.country_reg_form_label);
        countryLabel.setText(R.string.country);

        TextView cityLabel = (TextView) findViewById(R.id.city_reg_form_label);
        cityLabel.setText(R.string.city);

        TextView indexLabel = (TextView) findViewById(R.id.index_reg_form_label);
        indexLabel.setText(R.string.index);

        TextView streetLabel = (TextView) findViewById(R.id.street_reg_form_label);
        streetLabel.setText(R.string.address);

        TextView houseNumLabel = (TextView) findViewById(R.id.num_house_reg_form_label);
        houseNumLabel.setText(R.string.num_house);

        TextView officeNumLabel = (TextView) findViewById(R.id.num_flat_reg_form_label);
        officeNumLabel.setText(R.string.num_flat);

        registrationData = RegistrationData.getInstance();
        TextView okRegFormButton = (TextView) findViewById(R.id.ok_reg_form_btn);

        if (registrationData.isPersonalCabinetType()) {
            okRegFormButton.setText(R.string.register);
        }else{
            okRegFormButton.setText(R.string.enter);
        }

        TextView registrationLabel = (TextView) findViewById(R.id.registrationText);
        registrationLabel.setText(R.string.registration);

        TextView loginLabel = (TextView) findViewById(R.id.login);
        loginLabel.setText(R.string.login);

        /**
         * YOUR DATA in MenuFragment
         * */

        TextView withDelivery = (TextView) findViewById(R.id.withDelivery);
        withDelivery.setText(R.string.with_delivery);

        TextView withoutDelivery = (TextView) findViewById(R.id.withoutDelivery);
        withoutDelivery.setText(R.string.without_delivery);

        TextView name = (TextView) findViewById(R.id.name);
        name.setText(R.string.enter_name);

        TextView chooseCity = (TextView) findViewById(R.id.city);
        chooseCity.setText(R.string.choose_city);

        TextView streetText = (TextView) findViewById(R.id.streetText);
        streetText.setText(R.string.address);

        TextView numHouse = (TextView) findViewById(R.id.numHouse);
        numHouse.setText(R.string.num_house);

        TextView numFlat = (TextView) findViewById(R.id.numFlat);
        numFlat.setText(R.string.num_flat);

        TextView email = (TextView) findViewById(R.id.email);
        email.setText(R.string.your_email);

        TextView phone = (TextView) findViewById(R.id.phone);
        phone.setText(R.string.your_phone);
    }

    public SMCRestaurantActivity getSlidingMenuConfig() {
        return slidingMenuConfig;
    }

    public void setSlidingMenuConfig(SMCRestaurantActivity slidingMenuConfig) {
        this.slidingMenuConfig = slidingMenuConfig;
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (!onResumeFlag){
            onResumeFlag = true;
            return;
        }
        garbageNum.setText(String.valueOf(garbage.getTotal()));
    }

    public boolean isGarbageFlag() {
        return garbageFlag;
    }

    public void setGarbageFlag(boolean garbageFlag) {
        this.garbageFlag = garbageFlag;
    }

    public static void setiDestroy(IRestaurantActivityDestroy iDestroy) {
        if (iDestroies == null){
            iDestroies = new ArrayList<>();
        }

        RestaurantActivity.iDestroies.add(iDestroy);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        MealList.clear();
        garbage.removeActivity();
        garbage.onDestroy();
        mealFilter = MealFilter.getInstance();
        mealFilter.destroy();
        StateMenu.onDestroy();
        delivaryData = DelivaryData.getInstance();
        delivaryData.onDestroy();
        registrationData = RegistrationData.getInstance();
        registrationData.onDestroy();
        MealBody mealBody = MealBody.getInstance(this);
        mealBody.deleteAllFragments();
        mealBody.onDestroy();

        SMCRestaurantActivity.getSmcRestaurantActivity().remove();

        customViewAbove = null;

       if (restaurant != null){
           restaurant.clearInfo();
       }

        if (slidingMenuConfig != null)
            slidingMenuConfig = null;

        if (iDestroies == null)
            return;
        for (int i = 0; i < iDestroies.size(); i++) {
            iDestroies.get(i).change();
        }

    }
}
