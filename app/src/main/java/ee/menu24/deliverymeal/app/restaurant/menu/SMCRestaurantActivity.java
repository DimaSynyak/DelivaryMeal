package ee.menu24.deliverymeal.app.restaurant.menu;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import ee.menu24.deliverymeal.app.R;
import ee.menu24.deliverymeal.app.restaurant.service.filter.FilterFragment;
import ee.menu24.deliverymeal.app.main.service.Restaurant;
import ee.menu24.deliverymeal.app.main.service.RestaurantList;
import ee.menu24.deliverymeal.app.main.title.Language;
import ee.menu24.deliverymeal.app.restaurant.RestaurantActivity;
import ee.menu24.deliverymeal.app.restaurant.menu.fragments.OrderFragment;
import ee.menu24.deliverymeal.app.restaurant.service.ChangeDateListener;
import ee.menu24.deliverymeal.app.restaurant.service.DelivaryData;
import ee.menu24.deliverymeal.app.restaurant.service.Garbage;
import ee.menu24.deliverymeal.app.restaurant.service.IChangeNumFlatListener;
import ee.menu24.deliverymeal.app.restaurant.service.IChangePersonalCabinetTypeListener;
import ee.menu24.deliverymeal.app.restaurant.service.IChangeStateLogoutListener;
import ee.menu24.deliverymeal.app.restaurant.service.MealList;
import ee.menu24.deliverymeal.app.restaurant.service.RegistrationData;
import ee.menu24.deliverymeal.app.restaurant.service.filter.FilterData;
import ee.menu24.deliverymeal.app.restaurant.service.filter.MealFilter;
import ee.menu24.deliverymeal.app.restaurant.thread.ChangeLanguageAsyncTask;
import ee.menu24.deliverymeal.app.restaurant.thread.PaymentAsyncTask;
import ee.menu24.deliverymeal.app.restaurant.thread.RegistrationOrLoginAsyncTask;
import ee.menu24.deliverymeal.app.restaurant.thread.UploadPageAsyncTask;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


/**
 * Created by 1 on 30.10.2015.
 */
public class SMCRestaurantActivity {


    private RegistrationData registrationData;

    private boolean onResumeFlag;
    private FragmentActivity activity;
    private RestaurantActivity restaurantActivity;

    private boolean formDataClickFlag;
    private boolean orderDataClickFlag;
    private boolean personalCabinetFlag;

    private Garbage garbage;
    private LinearLayout total;
    private TextView totalText1;
    private TextView totalText2;
    private TextView personal_cabinet_text;
    private TextView deliver_text;
    private TextView meal_text;

    private LinearLayout garbageLayout;

    private LinearLayout personalCabinet;
    private LinearLayout formDataClick;
    private LinearLayout orderClick;
    private LinearLayout paymentMethod;

    private TextView pay;
    private TextView back;
    private TextView dateOrder;

    private ImageView seb_btn;
    private ImageView swed_btn;
    private ImageView lhv_btn;
    private ImageView nordea_btn;
    private ImageView danske_btn;
    private ImageView kredit_btn;

    private LinearLayout formDataFragmentId;

    private TimePicker timePicker;
    private LinearLayout nonDeliveryBTN;
    private LinearLayout deliveryBTN;


    private RadioButton delivery;
    private RadioButton nonDelivery;

    private DelivaryData deliveryData;
    private DatePicker datePicker;

    private LinearLayout street;
    private LinearLayout numHouse;
    private LinearLayout numFlat;

    /*FormDataFragment*/
    private EditText yourName;
    private EditText yourCity;
    private EditText yourStreet;
    private EditText yourHouse;
    private EditText yourFlat;
    private EditText yourEmail;
    private EditText yourPhone;

    private TextView name;
    private TextView city;
    private TextView streetText;
    private TextView house;
    private TextView flat;
    private TextView email;
    private TextView phone;

    private TextView withDelivery;
    private TextView withoutDelivery;

    private  int _hour;
    private int _minute;

    private int year;
    private int month;
    private int day;

    /*RegisterData*/
    private TextView registrationLabel;
    private TextView loginLabel;

    private TextView nameLabel;
    private TextView emailLabel;
    private TextView passwordLabel;
    private TextView confirmPasswordLabel;
    private TextView phoneLabel;
    private TextView countryLabel;
    private TextView cityLabel;
    private TextView indexLabel;
    private TextView streetLabel;
    private TextView houseNumLabel;
    private TextView officeNumLabel;

    private TextView okRegFormButton;
    private RestaurantList restaurantList;

    private EditText nameRegData;
    private EditText emailRegData;
    private EditText passwordRegData;
    private EditText confirmPasswordRegData;
    private EditText phoneRegData;
    private EditText countryRegData;
    private EditText cityRegData;
    private EditText indexRegData;
    private EditText streetRegData;
    private EditText houseNumRegData;
    private EditText officeNumRegData;

    private LinearLayout nameFields;
    private LinearLayout streetFields;
    private LinearLayout houseFields;
    private LinearLayout flatFields;
    private LinearLayout phoneFields;
    private LinearLayout countryFields;
    private LinearLayout indexFields;
    private LinearLayout confirmPasswordFields;
    private LinearLayout cityFields;
    private LinearLayout passwordFields;

    private LinearLayout registrationButton;
    private LinearLayout enterButton;

    private RadioButton registration;
    private RadioButton enter;

    private LinearLayout personalCabinetForm;
    /*end RegisterData*/
    private DelivaryData delivaryData;

    private static SMCRestaurantActivity smcRestaurantActivity;

    /*********************************/

    private LinearLayout baseLayout;

    private FragmentTransaction ft;

    private MealFilter filter;
    private Language language;

    private LinearLayout base_layout_container;
    private EditText editText;
    private Typeface geometric;
    private Typeface arimo;

    private OrderFragment orderFragment;

    private ChangeLanguageAsyncTask changeLanguageAsyncTask;

    private Restaurant restaurant;

    private HorizontalScrollView horizontalScrollView;



    public SMCRestaurantActivity(FragmentActivity activity) {
        this.activity = activity;
        language = Language.getInstance();
        restaurantActivity = ((RestaurantActivity) activity);
        garbage = Garbage.getInstance();
        delivaryData = DelivaryData.getInstance();
        registrationData = RegistrationData.getInstance();
        smcRestaurantActivity = this;
        restaurantList = RestaurantList.getInstance();
        restaurant = restaurantList.getRestaurant();
    }



    public void initSlidingMenu(){
        geometric = Typeface.createFromAsset(activity.getAssets(), "fonts/geometric/geometric_706_black.ttf");
        arimo = Typeface.createFromAsset(activity.getAssets(), "fonts/arimo/Arimo_Regular.ttf");

        SlidingMenu menu = new SlidingMenu(activity);
        RestaurantActivity.setCustomViewAbove(menu.getmViewAbove());
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.menu);



        TextView plus = (TextView) activity.findViewById(R.id.plus);
        plus.setTypeface(geometric);
        plus.setVisibility(View.VISIBLE);

        TextView addDeliver = (TextView) activity.findViewById(R.id.add_deliver);
        addDeliver.setTypeface(geometric);
        addDeliver.setVisibility(View.VISIBLE);
        addDeliver.setText(restaurant.getCostDeliver());

        /****************************************************/
        /*RegistrationData*/
        nameLabel = (TextView) activity.findViewById(R.id.name_reg_form_label);
        nameLabel.setTypeface(arimo);

        emailLabel = (TextView) activity.findViewById(R.id.email_reg_form_label);
        emailLabel.setTypeface(arimo);

        passwordLabel = (TextView) activity.findViewById(R.id.password_reg_form_label);
        passwordLabel.setTypeface(arimo);

        passwordFields = (LinearLayout) activity.findViewById(R.id.password_fields);

        confirmPasswordLabel = (TextView) activity.findViewById(R.id.confirm_pass_reg_form_label);
        confirmPasswordLabel.setTypeface(arimo);

        phoneLabel = (TextView) activity.findViewById(R.id.phone_reg_form_label);
        phoneLabel.setTypeface(arimo);

        countryLabel = (TextView) activity.findViewById(R.id.country_reg_form_label);
        countryLabel.setTypeface(arimo);

        cityLabel = (TextView) activity.findViewById(R.id.city_reg_form_label);
        cityLabel.setTypeface(arimo);

        indexLabel = (TextView) activity.findViewById(R.id.index_reg_form_label);
        indexLabel.setTypeface(arimo);

        streetLabel = (TextView) activity.findViewById(R.id.street_reg_form_label);
        streetLabel.setTypeface(arimo);

        houseNumLabel = (TextView) activity.findViewById(R.id.num_house_reg_form_label);
        houseNumLabel.setTypeface(arimo);

        officeNumLabel = (TextView) activity.findViewById(R.id.num_flat_reg_form_label);
        officeNumLabel.setTypeface(arimo);

        okRegFormButton = (TextView) activity.findViewById(R.id.ok_reg_form_btn);
        okRegFormButton.setTypeface(geometric);


        nameRegData = (EditText) activity.findViewById(R.id.name_reg_form);
        nameRegData.setTypeface(geometric);
        nameRegData.setText(registrationData.getName());

        emailRegData = (EditText) activity.findViewById(R.id.email_reg_form);
        emailRegData.setTypeface(geometric);
        emailRegData.setText(registrationData.getEmail());

        passwordRegData = (EditText) activity.findViewById(R.id.password_reg_form);
        passwordRegData.setTypeface(geometric);

        confirmPasswordRegData = (EditText) activity.findViewById(R.id.confirm_password_reg_form);
        confirmPasswordRegData.setTypeface(geometric);

        phoneRegData = (EditText) activity.findViewById(R.id.phone_reg_form);
        phoneRegData.setTypeface(geometric);
        phoneRegData.setText(registrationData.getNumPhone());

        countryRegData = (EditText) activity.findViewById(R.id.country_reg_form);
        countryRegData.setTypeface(geometric);
        countryRegData.setText("Eesti");

        cityRegData = (EditText) activity.findViewById(R.id.city_reg_form);
        cityRegData.setTypeface(geometric);
        cityRegData.setText(registrationData.getCity());

        indexRegData = (EditText) activity.findViewById(R.id.index_reg_form);
        indexRegData.setTypeface(geometric);
        indexRegData.setText(registrationData.getIndex());

        streetRegData = (EditText) activity.findViewById(R.id.street_reg_form);
        streetRegData.setTypeface(geometric);
        streetRegData.setText(registrationData.getNumStreet());

        houseNumRegData = (EditText) activity.findViewById(R.id.house_num_reg_form);
        houseNumRegData.setTypeface(geometric);
        houseNumRegData.setText(registrationData.getNumHouse());

        officeNumRegData = (EditText) activity.findViewById(R.id.flat_num_reg_form);
        officeNumRegData.setTypeface(geometric);
        officeNumRegData.setText(registrationData.getNumFlat());

        registrationButton = (LinearLayout) activity.findViewById(R.id.registration_btn);
        enterButton = (LinearLayout) activity.findViewById(R.id.enter_btn);

        registration = (RadioButton) activity.findViewById(R.id.registration);
        enter = (RadioButton) activity.findViewById(R.id.enter);

        personalCabinetForm = (LinearLayout) activity.findViewById(R.id.personal_cabinet_form);
        personalCabinetForm.setVisibility(LinearLayout.GONE);

        registrationLabel = (TextView) activity.findViewById(R.id.registrationText);
        registrationLabel.setTypeface(geometric);

        loginLabel = (TextView) activity.findViewById(R.id.login);
        loginLabel.setTypeface(geometric);

        nameFields = (LinearLayout) activity.findViewById(R.id.name_fields);
        streetFields = (LinearLayout) activity.findViewById(R.id.street_fields);
        houseFields = (LinearLayout) activity.findViewById(R.id.num_house_fields);
        flatFields = (LinearLayout) activity.findViewById(R.id.num_flat_fields);
        phoneFields = (LinearLayout) activity.findViewById(R.id.phone_fields);
        countryFields = (LinearLayout) activity.findViewById(R.id.country_fields);
        indexFields = (LinearLayout) activity.findViewById(R.id.index_fields);
        confirmPasswordFields = (LinearLayout) activity.findViewById(R.id.confirm_password_fields);
        cityFields = (LinearLayout) activity.findViewById(R.id.city_fields);


        if (registrationData.isStateLogin()) {
            okRegFormButton.setText(R.string.quit);
            emailRegData.setEnabled(false);
            passwordFields.setVisibility(LinearLayout.GONE);

        } else {
            okRegFormButton.setText(R.string.enter);
            emailRegData.setEnabled(true);
            passwordFields.setVisibility(LinearLayout.VISIBLE);
        }

        /*end RegistrationData*/




        formDataFragmentId = (LinearLayout) activity.findViewById(R.id.formDataFragmentId);
        formDataFragmentId.setVisibility(LinearLayout.GONE);
        formDataFragmentId.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        base_layout_container = (LinearLayout) activity.findViewById(R.id.base_layout_container);
        garbageLayout = (LinearLayout) activity.findViewById(R.id.garbageLayout);


        base_layout_container.setVisibility(LinearLayout.VISIBLE);
        garbageLayout.setVisibility(LinearLayout.GONE);


        personal_cabinet_text = (TextView) activity.findViewById(R.id.personal_cabinet_text);
        personal_cabinet_text.setTypeface(geometric);

        deliver_text = (TextView) activity.findViewById(R.id.deliverText);
        deliver_text.setTypeface(geometric);

        meal_text = (TextView) activity.findViewById(R.id.mealText);
        meal_text.setTypeface(geometric);

        dateOrder = (TextView) activity.findViewById(R.id.dateOrder);
        dateOrder.setTypeface(arimo);

        pay = (TextView) activity.findViewById(R.id.textView25);
        pay.setTypeface(geometric);
//        pay.setVisibility(LinearLayout.GONE);

        back = (TextView) activity.findViewById(R.id.textView26);
        back.setTypeface(geometric);

        paymentMethod = (LinearLayout) activity.findViewById(R.id.payment_method);
        paymentMethod.setVisibility(LinearLayout.GONE);

        formDataClick = (LinearLayout) activity.findViewById(R.id.formDataClick);
        orderClick = (LinearLayout) activity.findViewById(R.id.orderClick);
        personalCabinet = (LinearLayout) activity.findViewById(R.id.personalCabinet);

        seb_btn = (ImageView) activity.findViewById(R.id.seb_btn);
        swed_btn = (ImageView) activity.findViewById(R.id.swed_btn);
        lhv_btn = (ImageView) activity.findViewById(R.id.lhv_btn);
        nordea_btn = (ImageView) activity.findViewById(R.id.nordea_btn);
        danske_btn = (ImageView) activity.findViewById(R.id.danske_btn);
        kredit_btn = (ImageView) activity.findViewById(R.id.kredit_btn);

        total = (LinearLayout) activity.findViewById(R.id.total);
        totalText1 = (TextView) activity.findViewById(R.id.total_text1);
        totalText1.setTypeface(arimo);
        totalText2 = (TextView) activity.findViewById(R.id.total_text2);
        totalText2.setTypeface(geometric);
        total.setVisibility(LinearLayout.GONE);

        timePicker = (TimePicker) activity.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        deliveryBTN = (LinearLayout) activity.findViewById(R.id.delivery_btn);
        nonDeliveryBTN = (LinearLayout) activity.findViewById(R.id.non_delivery_btn);

        delivery = (RadioButton) activity.findViewById(R.id.delivery);
        nonDelivery = (RadioButton) activity.findViewById(R.id.non_delivery);

        withDelivery = (TextView) activity.findViewById(R.id.withDelivery);
        withDelivery.setTypeface(geometric);

        withoutDelivery = (TextView) activity.findViewById(R.id.withoutDelivery);
        withoutDelivery.setTypeface(geometric);

        street = (LinearLayout) activity.findViewById(R.id.street);
        numHouse = (LinearLayout) activity.findViewById(R.id.num_house);
        numFlat = (LinearLayout) activity.findViewById(R.id.num_flat);

        yourName = (EditText) activity.findViewById(R.id.yourName);
        yourName.setTypeface(geometric);
        yourName.setText(registrationData.getName());

        yourCity = (EditText) activity.findViewById(R.id.yourCity);
        yourCity.setTypeface(geometric);
        yourCity.setText(registrationData.getCity());

        yourStreet = (EditText) activity.findViewById(R.id.yourStreet);
        yourStreet.setTypeface(geometric);
        yourStreet.setText(registrationData.getNumStreet());

        yourHouse = (EditText) activity.findViewById(R.id.yourHouseNum);
        yourHouse.setTypeface(geometric);
        yourHouse.setText(registrationData.getNumHouse());

        yourFlat = (EditText) activity.findViewById(R.id.yourFlatNum);
        yourFlat.setTypeface(geometric);
        yourFlat.setText(registrationData.getNumFlat());

        yourEmail = (EditText) activity.findViewById(R.id.yourEmail);
        yourEmail.setTypeface(geometric);
        yourEmail.setText(registrationData.getEmail());

        yourPhone = (EditText) activity.findViewById(R.id.yourPhone);
        yourPhone.setTypeface(geometric);
        yourPhone.setText(registrationData.getNumPhone());

        datePicker = (DatePicker) activity.findViewById(R.id.datePicker);
        dateOrder.setText(updateDate());

        name = (TextView) activity.findViewById(R.id.name);
        name.setTypeface(arimo);

        city = (TextView) activity.findViewById(R.id.city);
        city.setTypeface(arimo);

        streetText = (TextView) activity.findViewById(R.id.streetText);
        streetText.setTypeface(arimo);

        house = (TextView) activity.findViewById(R.id.numHouse);
        house.setTypeface(arimo);

        flat = (TextView) activity.findViewById(R.id.numFlat);
        flat.setTypeface(arimo);

        email = (TextView) activity.findViewById(R.id.email);
        email.setTypeface(arimo);

        phone = (TextView) activity.findViewById(R.id.phone);
        phone.setTypeface(arimo);


        deliveryData = DelivaryData.getInstance();
        /****************************************************/

        baseLayout = (LinearLayout) activity.findViewById(R.id.baseLayout);

        TextView searchButton = (TextView) activity.findViewById(R.id.search_button);
        searchButton.setTypeface(geometric);

        editText = (EditText) activity.findViewById(R.id.editText);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MealFilter.getSearchData().setText(String.valueOf(editText.getText()));
                MealFilter.getSearchData().setStateUse(true);

                if (changeLanguageAsyncTask != null && !changeLanguageAsyncTask.isCancelled()) {
                    changeLanguageAsyncTask.setIsCancled(true);
                    changeLanguageAsyncTask.cancel(true);
                }

                UploadPageAsyncTask uploadPageAsyncTask = MealList.getUploadPageAsyncTask();
                if (uploadPageAsyncTask != null && !uploadPageAsyncTask.isCancelled()) {
                    uploadPageAsyncTask.cancel(true);
                }

                MealFilter mealFilter = MealFilter.getInstance();
                mealFilter.setStateMealFilter(true);
                changeLanguageAsyncTask = new ChangeLanguageAsyncTask(((AppCompatActivity) activity));
                changeLanguageAsyncTask.execute(language.getURL());
            }
        });


        baseLayout.setVisibility(LinearLayout.VISIBLE);
        garbageLayout.setVisibility(LinearLayout.GONE);

        LinearLayout orderContainer = (LinearLayout) activity.findViewById(R.id.orderDataContainer);
        orderContainer.setVisibility(View.GONE);
       initListeners();
    }

    private void initListeners(){

        filter = MealFilter.getInstance();


        addFilterData();

        horizontalScrollView = (HorizontalScrollView) activity.findViewById(R.id.horizontalScrollView);
        horizontalScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                horizontalScrollView.post(new Runnable() {
                    public void run() {
                        horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
                    }
                });
            }
        });

        /*********************************************/
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                deliveryData.setDelivaryData(updateDate());
            }
        });

        datePicker.getCalendarView().setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                deliveryData.setDelivaryData(updateDate(year, month, dayOfMonth));
            }
        });

        deliveryBTN.setOnClickListener(new View.OnClickListener() {

            TextView plus = (TextView) activity.findViewById(R.id.plus);
            TextView addDeliver = (TextView) activity.findViewById(R.id.add_deliver);

            @Override
            public void onClick(View v) {

                deliveryData.setDelivaryType(true);
                delivery.setChecked(true);
                nonDelivery.setChecked(false);
                plus.setVisibility(View.VISIBLE);
                addDeliver.setVisibility(View.VISIBLE);

                street.setVisibility(LinearLayout.VISIBLE);
                numHouse.setVisibility(LinearLayout.VISIBLE);
                numFlat.setVisibility(LinearLayout.VISIBLE);

            }
        });

        nonDeliveryBTN.setOnClickListener(new View.OnClickListener() {

            TextView plus = (TextView) activity.findViewById(R.id.plus);
            TextView addDeliver = (TextView) activity.findViewById(R.id.add_deliver);

            @Override
            public void onClick(View v) {
                deliveryData.setDelivaryType(false);
                delivery.setChecked(false);
                nonDelivery.setChecked(true);
                plus.setVisibility(View.GONE);
                addDeliver.setVisibility(View.GONE);

                street.setVisibility(LinearLayout.GONE);
                numHouse.setVisibility(LinearLayout.GONE);
                numFlat.setVisibility(LinearLayout.GONE);
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (garbage.getTotal() == 0)
                    return;
                updateDelivaryData();
                if (!delivaryData.checkData())
                    return;

                new PaymentAsyncTask(restaurantActivity, R.id.payment_container_id).execute(restaurantList.getRestaurant().getMenuLink());
                pay.setVisibility(LinearLayout.GONE);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFilterFragment();
            }
        });

        personalCabinet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (orderDataClickFlag){
                    goneOrderData();
                }

                if (formDataClickFlag){
                    removeFormDataFragment();
                    formDataClickFlag = false;
                }

                if (personalCabinetFlag){
                    removePersonalCabinet();
                    personalCabinetFlag = false;
                }
                else {
                    addPersonalCabinet();
                    personalCabinetFlag = true;
                }
            }
        });

        formDataClick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (orderDataClickFlag){
                    goneOrderData();
                }

                if (personalCabinetFlag){
                    removePersonalCabinet();
                    personalCabinetFlag = false;
                }

                if (formDataClickFlag){
                    removeFormDataFragment();
                    formDataClickFlag = false;
                }
                else {
                    addFormDataFragment();
                    formDataClickFlag = true;
                }
            }
        });




        orderClick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (formDataClickFlag){
                    removeFormDataFragment();
                    formDataClickFlag = false;
                }

                if (personalCabinetFlag){
                    removePersonalCabinet();
                    personalCabinetFlag = false;
                }

                if (orderDataClickFlag){
                    goneOrderData();
                }
                else {
                    visibleOrderData();
                }
            }
        });

        seb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kredit_btn.setBackground(null);
                danske_btn.setBackground(null);
                nordea_btn.setBackground(null);
                swed_btn.setBackground(null);
                seb_btn.setBackground(activity.getResources().getDrawable(R.drawable.border));
                lhv_btn.setBackground(null);

                delivaryData.setNameBank("seb");
            }
        });
        swed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kredit_btn.setBackground(null);
                danske_btn.setBackground(null);
                nordea_btn.setBackground(null);
                seb_btn.setBackground(null);
                swed_btn.setBackground(activity.getResources().getDrawable(R.drawable.border));
                lhv_btn.setBackground(null);

                delivaryData.setNameBank("swedbank");
            }
        });
        lhv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kredit_btn.setBackground(null);
                danske_btn.setBackground(null);
                nordea_btn.setBackground(null);
                seb_btn.setBackground(null);
                swed_btn.setBackground(null);
                lhv_btn.setBackground(activity.getResources().getDrawable(R.drawable.border));

                delivaryData.setNameBank("lhv");
            }
        });

        nordea_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kredit_btn.setBackground(null);
                danske_btn.setBackground(null);
                nordea_btn.setBackground(activity.getResources().getDrawable(R.drawable.border));
                seb_btn.setBackground(null);
                swed_btn.setBackground(null);
                lhv_btn.setBackground(null);

                delivaryData.setNameBank("nordea");
            }
        });

        danske_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kredit_btn.setBackground(null);
                danske_btn.setBackground(activity.getResources().getDrawable(R.drawable.border));
                nordea_btn.setBackground(null);
                seb_btn.setBackground(null);
                swed_btn.setBackground(null);
                lhv_btn.setBackground(null);

                delivaryData.setNameBank("danske");
            }
        });

        kredit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kredit_btn.setBackground(activity.getResources().getDrawable(R.drawable.border));
                danske_btn.setBackground(null);
                nordea_btn.setBackground(null);
                seb_btn.setBackground(null);
                swed_btn.setBackground(null);
                lhv_btn.setBackground(null);

                delivaryData.setNameBank("krediidipank");
            }
        });

        delivaryData.setChangeDateListener(new ChangeDateListener() {
            @Override
            public void onChange() {
                dateOrder.setText(delivaryData.getDelivaryData());
            }
        });

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enter.setChecked(false);
                registration.setChecked(true);

                nameFields.setVisibility(LinearLayout.VISIBLE);
                streetFields.setVisibility(LinearLayout.VISIBLE);
                houseFields.setVisibility(LinearLayout.VISIBLE);
                flatFields.setVisibility(LinearLayout.VISIBLE);
                phoneFields.setVisibility(LinearLayout.VISIBLE);
                countryFields.setVisibility(LinearLayout.VISIBLE);
                indexFields.setVisibility(LinearLayout.VISIBLE);
                confirmPasswordFields.setVisibility(LinearLayout.VISIBLE);
                cityFields.setVisibility(LinearLayout.VISIBLE);

//                okRegFormButton.setText(R.string.register);

                registrationData.setPersonalCabinetType(true);
            }
        });

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enter.setChecked(true);
                registration.setChecked(false);

                nameFields.setVisibility(LinearLayout.GONE);
                streetFields.setVisibility(LinearLayout.GONE);
                houseFields.setVisibility(LinearLayout.GONE);
                flatFields.setVisibility(LinearLayout.GONE);
                phoneFields.setVisibility(LinearLayout.GONE);
                countryFields.setVisibility(LinearLayout.GONE);
                indexFields.setVisibility(LinearLayout.GONE);
                confirmPasswordFields.setVisibility(LinearLayout.GONE);
                cityFields.setVisibility(LinearLayout.GONE);

//                okRegFormButton.setText(R.string.enter);

                registrationData.setPersonalCabinetType(false);
            }
        });

        okRegFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRegisterData();
                delivaryData.setDelivaryType(true);

                RegistrationOrLoginAsyncTask registrationOrLoginAsyncTask = new RegistrationOrLoginAsyncTask((AppCompatActivity) activity, okRegFormButton, pay);
                registrationOrLoginAsyncTask.setContainerId(R.id.container_id);
                registrationOrLoginAsyncTask.execute("11");
            }
        });

        registrationData.setChangeStateLogoutListener(new IChangeStateLogoutListener() {
            @Override
            public void change() {
                if (registrationData.isStateLogin()) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            okRegFormButton.setText(R.string.quit);
                            emailRegData.setEnabled(false);
                            passwordFields.setVisibility(LinearLayout.GONE);
                        }
                    });

                } else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            okRegFormButton.setText(R.string.enter);
                            emailRegData.setEnabled(true);
                            passwordFields.setVisibility(LinearLayout.VISIBLE);
                        }
                    });
                }
            }
        });

        registrationData.setiChangePersonalCabinetTypeListener(new IChangePersonalCabinetTypeListener() {
            @Override
            public void change() {
                if (registrationData.isPersonalCabinetType()) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            okRegFormButton.setText(R.string.register);
                        }
                    });
                } else {
                    if (registrationData.isStateLogin()) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                okRegFormButton.setText(R.string.quit);
                            }
                        });
                    } else {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                okRegFormButton.setText(R.string.enter);
                            }
                        });
                    }
                }
            }
        });

        registrationData.setiChangeNumFlatListener(new IChangeNumFlatListener() {
            @Override
            public void change() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateDataFormUI();
                    }
                });
            }
        });
        /*********************************************/

    }


    public void goneOrderData(){
        LinearLayout orderContainer = (LinearLayout) activity.findViewById(R.id.orderDataContainer);
        orderContainer.setVisibility(View.GONE);
        orderDataClickFlag = false;
        total.setVisibility(LinearLayout.GONE);
        paymentMethod.setVisibility(LinearLayout.GONE);
    }

    public void visibleOrderData(){
        LinearLayout orderContainer = (LinearLayout) activity.findViewById(R.id.orderDataContainer);
        orderContainer.setVisibility(View.VISIBLE);
        orderDataClickFlag = true;
        total.setVisibility(LinearLayout.VISIBLE);
        paymentMethod.setVisibility(LinearLayout.VISIBLE);
    }

    public void addFilterData(){

        base_layout_container.removeAllViews();

        for (final FilterData filterData : filter.getFilterDataList()){
            if (filterData.getList() != null && filterData.getList().size() != 0){
                ImageView line = new ImageView(activity);
                line.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                ));
                line.setBackground(activity.getResources().getDrawable(R.drawable.line));


                ImageView line2 = new ImageView(activity);
                line2.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                ));
                line2.setBackground(activity.getResources().getDrawable(R.drawable.line));


                LinearLayout linearLayout = new LinearLayout(activity);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setLayoutParams(new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));

                TextView textView = new TextView(activity);
                textView.setTypeface(geometric);
                textView.setTextColor(Color.BLACK);
                textView.setPadding(35, 25, 10, 10);
                textView.setTextSize(dpToPx(9));
                textView.setText(filterData.getText());
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                       1
                ));

                ImageView arrow = new ImageView(activity);
                arrow.setImageDrawable(activity.getResources().getDrawable(R.drawable.arrow));
                arrow.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                arrow.setPadding(10,25,30,10);


                final LinearLayout containerLayout = new LinearLayout(activity);
                containerLayout.setOrientation(LinearLayout.VERTICAL);
                containerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                containerLayout.setVisibility(View.GONE);

                linearLayout.addView(textView);
                linearLayout.addView(arrow);
                linearLayout.setClickable(true);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (containerLayout.getVisibility() == View.GONE) {
                            containerLayout.setVisibility(View.VISIBLE);
                        } else {
                            containerLayout.setVisibility(View.GONE);
                        }
                    }
                });

                base_layout_container.addView(line);
                base_layout_container.addView(linearLayout);
                base_layout_container.addView(containerLayout);
                base_layout_container.addView(line2);

                for (final FilterData fd : filterData.getList()){

                    FilterFragment filterFragment = new FilterFragment();
                    filterFragment.setFilterData(fd);
                    fd.setFilterFragment(filterFragment);

                    TextView tv = new TextView(activity);
                    tv.setTypeface(geometric);
                    tv.setText(fd.getText());
                    tv.setClickable(true);
                    tv.setTextSize(dpToPx(8));
                    tv.setTextColor(Color.BLACK);
                    tv.setPadding(55, 5, 0, 10);
                    tv.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            if (fd.isStateUse())
                                return;

                            fd.setStateUse(true);
                            ft = activity.getSupportFragmentManager().beginTransaction();
                            ft.add(R.id.filterLayout, fd.getFilterFragment());
                            ft.commit();
                        }
                    });

                    containerLayout.addView(tv);

                }
            }
            else if (filterData.getName() != null && !filterData.getName().equals("")){


                FilterFragment filterFragment = new FilterFragment();
                filterFragment.setFilterData(filterData);
                filterData.setFilterFragment(filterFragment);


                LinearLayout linearLayout = new LinearLayout(activity);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setLayoutParams(new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));

                TextView textView = new TextView(activity);
                textView.setTypeface(geometric);
                textView.setTextColor(Color.BLACK);
                textView.setPadding(35, 25, 10, 10);
                textView.setTextSize(dpToPx(9));
                textView.setText(filterData.getText());
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                ));

                final LinearLayout containerLayout = new LinearLayout(activity);
                containerLayout.setOrientation(LinearLayout.VERTICAL);
                containerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                containerLayout.setVisibility(View.GONE);

                linearLayout.addView(textView);
                linearLayout.setClickable(true);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    private FilterData fd = filterData;
                    @Override
                    public void onClick(View v) {

                        if (fd.isStateUse())
                            return;

                        fd.setStateUse(true);
                        ft = activity.getSupportFragmentManager().beginTransaction();
                        ft.add(R.id.filterLayout, fd.getFilterFragment());
                        ft.commit();
                    }
                });

                base_layout_container.addView(linearLayout);
            }
            else {


                FilterFragment filterFragment = new FilterFragment();
                filterFragment.setFilterData(filterData);
                filterData.setFilterFragment(filterFragment);


                LinearLayout linearLayout = new LinearLayout(activity);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setLayoutParams(new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));

                TextView textView = new TextView(activity);
                textView.setTypeface(geometric);
                textView.setTextColor(Color.BLACK);
                textView.setPadding(35, 25, 10, 10);
                textView.setTextSize(dpToPx(9));
                textView.setText(filterData.getText());
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                ));

                final LinearLayout containerLayout = new LinearLayout(activity);
                containerLayout.setOrientation(LinearLayout.VERTICAL);
                containerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                containerLayout.setVisibility(View.GONE);

                linearLayout.addView(textView);
                linearLayout.setClickable(true);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    private FilterData fd = filterData;
                    @Override
                    public void onClick(View v) {

                        if (fd.isStateUse())
                            return;

                        fd.setStateUse(true);
                        ft = activity.getSupportFragmentManager().beginTransaction();
                        ft.add(R.id.filterLayout, fd.getFilterFragment());
                        ft.commit();
                    }
                });

                base_layout_container.addView(linearLayout);
            }
        }
        base_layout_container.setVisibility(View.VISIBLE);
    }

    public float dpToPx(float dp) {
        DisplayMetrics  displayMetrics = activity.getResources().getDisplayMetrics();
        return  (int)((dp * displayMetrics.density) + 0.5);
    }

    public String updateDate(){
        int day1 = datePicker.getDayOfMonth();
        int month1 = datePicker.getMonth();
        int year1 = datePicker.getYear();

        _hour = timePicker.getCurrentHour();
        _minute = timePicker.getCurrentMinute();

        return day1 + "." + (month1+1) + "." + year1 + " " + _hour + ":" + _minute;
    }

    public String updateDate(int year, int month, int dayOfMonth){

        _hour = timePicker.getCurrentHour();
        _minute = timePicker.getCurrentMinute();

        return dayOfMonth + "." + (month+1) + "." + year + " " + _hour + ":" + _minute;
    }


    private void updateDataFormUI(){
        yourName.setText(registrationData.getName());
        yourCity.setText(registrationData.getCity());
        yourStreet.setText(registrationData.getNumStreet());
        yourHouse.setText(registrationData.getNumHouse());
        yourFlat.setText(registrationData.getNumFlat());
        yourEmail.setText(registrationData.getEmail());
        yourPhone.setText(registrationData.getNumPhone());
    }

    private void updateRegisterData(){
        registrationData.setName(String.valueOf(nameRegData.getText()));
        registrationData.setEmail(String.valueOf(emailRegData.getText()));
        registrationData.setPassword(String.valueOf(passwordRegData.getText()));
        registrationData.setConfirmPassword(String.valueOf(confirmPasswordRegData.getText()));
        registrationData.setNumPhone(String.valueOf(phoneRegData.getText()));
        registrationData.setCountry("EE");
        registrationData.setCity(String.valueOf(cityRegData.getText()));
        registrationData.setIndex(String.valueOf(indexRegData.getText()));
        registrationData.setNumStreet(String.valueOf(streetRegData.getText()));
        registrationData.setNumHouse(String.valueOf(houseNumRegData.getText()));
        registrationData.setNumFlat(String.valueOf(officeNumRegData.getText()));
    }


    public void setGarbageFragment(){
        baseLayout.setVisibility(LinearLayout.GONE);
        garbageLayout.setVisibility(LinearLayout.VISIBLE);
    }


    public void setFilterFragment(){
        baseLayout.setVisibility(LinearLayout.VISIBLE);
        garbageLayout.setVisibility(LinearLayout.GONE);
    }


    private void addFormDataFragment(){

        formDataFragmentId.setVisibility(LinearLayout.VISIBLE);
    }

    private void removeFormDataFragment(){
        formDataFragmentId.setVisibility(LinearLayout.GONE);
    }

    private void removePersonalCabinet(){
        personalCabinetForm.setVisibility(LinearLayout.GONE);
    }

    private void addPersonalCabinet(){
        personalCabinetForm.setVisibility(LinearLayout.VISIBLE);
    }

    public boolean isOrderDataClickFlag() {
        return orderDataClickFlag;
    }

    public void setOrderDataClickFlag(boolean orderDataClickFlag) {
        this.orderDataClickFlag = orderDataClickFlag;
    }


    private void updateDelivaryData(){

        delivaryData.setYourName(String.valueOf(getYourName().getText()));
        delivaryData.setDelivaryCity(String.valueOf(getYourCity().getText()));
        delivaryData.setNumStreet(String.valueOf(getYourStreet().getText()));
        delivaryData.setNumHouse(String.valueOf(getYourHouse().getText()));
        delivaryData.setNumFlat(String.valueOf(getYourFlat().getText()));
        delivaryData.setEmail(String.valueOf(getYourEmail().getText()));
        delivaryData.setNumPhone(String.valueOf(getYourPhone().getText()));

        delivaryData.setDelivaryData(updateDate());
    }

    public boolean isFormDataClickFlag() {
        return formDataClickFlag;
    }

    public void setFormDataClickFlag(boolean formDataClickFlag) {
        this.formDataClickFlag = formDataClickFlag;
    }

    public boolean isPersonalCabinetClickFlag() {
        return personalCabinetFlag;
    }

    public void setPersonalCabinetClickFlag(boolean personalCabinetClickFlag) {
        this.personalCabinetFlag = personalCabinetClickFlag;
    }

    public LinearLayout getOrderClick() {
        return orderClick;
    }

    public LinearLayout getFormDataClick() {
        return formDataClick;
    }

    public TimePicker getTimePicker() {
        return timePicker;
    }

    public LinearLayout getNonDeliveryBTN() {
        return nonDeliveryBTN;
    }

    public LinearLayout getDeliveryBTN() {
        return deliveryBTN;
    }

    public RadioButton getDelivery() {
        return delivery;
    }

    public RadioButton getNonDelivery() {
        return nonDelivery;
    }

    public DelivaryData getDeliveryData() {
        return deliveryData;
    }

    public LinearLayout getStreet() {
        return street;
    }

    public LinearLayout getNumHouse() {
        return numHouse;
    }

    public LinearLayout getNumFlat() {
        return numFlat;
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public EditText getYourName() {
        return yourName;
    }

    public EditText getYourCity() {
        return yourCity;
    }

    public EditText getYourStreet() {
        return yourStreet;
    }

    public EditText getYourHouse() {
        return yourHouse;
    }

    public EditText getYourFlat() {
        return yourFlat;
    }

    public EditText getYourEmail() {
        return yourEmail;
    }

    public EditText getYourPhone() {
        return yourPhone;
    }

    public LinearLayout getPersonalCabinet() {
        return personalCabinet;
    }

    public TextView getOkRegFormButton() {
        return okRegFormButton;
    }

    public static SMCRestaurantActivity getSmcRestaurantActivity() {
        return smcRestaurantActivity;
    }

    public static void setSmcRestaurantActivity(SMCRestaurantActivity smcRestaurantActivity) {
        SMCRestaurantActivity.smcRestaurantActivity = smcRestaurantActivity;
    }

    public void remove(){
        registrationData  = null;
        activity = null;
        smcRestaurantActivity = null;
    }


}
