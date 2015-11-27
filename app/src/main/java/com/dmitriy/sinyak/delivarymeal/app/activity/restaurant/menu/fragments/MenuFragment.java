package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.menu.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.RestaurantList;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.Garbage;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.RestaurantActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.Meal;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.MealList;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.ChangeDateListener;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.DelivaryData;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.RegistrationData;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.thread.MainAsyncTask;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.thread.RegistrationOrLoginAsyncTask;


/**
 * Created by 1 on 25.11.2015.
 */
public class MenuFragment extends Fragment {

    private RegistrationData registrationData;

    private boolean onResumeFlag;
    private FragmentActivity activity;
    private RestaurantActivity restaurantActivity;

    private boolean formDataClickFlag;
    private boolean orderDataClickFlag;
    private boolean personalCabinetFlag;

    private FragmentTransaction ft;
    private Garbage garbage;
    private LinearLayout total;
    private TextView totalText1;
    private TextView totalText2;
    private TextView personal_cabinet_text;
    private TextView deliver_text;
    private TextView meal_text;

    private LinearLayout baseLayout;
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

    private LinearLayout registrationButton;
    private LinearLayout enterButton;

    private RadioButton registration;
    private RadioButton enter;

    private LinearLayout personalCabinetForm;
    /*end RegisterData*/
    private DelivaryData delivaryData;

    public MenuFragment() {
        activity = getActivity();
        restaurantActivity = ((RestaurantActivity) activity);
        garbage = Garbage.getInstance();
        delivaryData = DelivaryData.getInstance();
        registrationData = RegistrationData.getInstance();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment, container, false);
        Typeface geometric = Typeface.createFromAsset(getActivity().getAssets(), "fonts/geometric/geometric_706_black.ttf");
        Typeface arimo = Typeface.createFromAsset(getActivity().getAssets(), "fonts/arimo/Arimo_Regular.ttf");

        /*RegistrationData*/
        nameLabel = (TextView) view.findViewById(R.id.name_reg_form_label);
        nameLabel.setTypeface(arimo);

        emailLabel = (TextView) view.findViewById(R.id.email_reg_form_label);
        emailLabel.setTypeface(arimo);

        passwordLabel = (TextView) view.findViewById(R.id.password_reg_form_label);
        passwordLabel.setTypeface(arimo);

        confirmPasswordLabel = (TextView) view.findViewById(R.id.confirm_pass_reg_form_label);
        confirmPasswordLabel.setTypeface(arimo);

        phoneLabel = (TextView) view.findViewById(R.id.phone_reg_form_label);
        phoneLabel.setTypeface(arimo);

        countryLabel = (TextView) view.findViewById(R.id.country_reg_form_label);
        countryLabel.setTypeface(arimo);

        cityLabel = (TextView) view.findViewById(R.id.city_reg_form_label);
        cityLabel.setTypeface(arimo);

        indexLabel = (TextView) view.findViewById(R.id.index_reg_form_label);
        indexLabel.setTypeface(arimo);

        streetLabel = (TextView) view.findViewById(R.id.street_reg_form_label);
        streetLabel.setTypeface(arimo);

        houseNumLabel = (TextView) view.findViewById(R.id.num_house_reg_form_label);
        houseNumLabel.setTypeface(arimo);

        officeNumLabel = (TextView) view.findViewById(R.id.num_flat_reg_form_label);
        officeNumLabel.setTypeface(arimo);

        okRegFormButton = (TextView) view.findViewById(R.id.ok_reg_form_btn);
        okRegFormButton.setTypeface(geometric);

        nameRegData = (EditText) view.findViewById(R.id.name_reg_form);
        nameRegData.setTypeface(geometric);

        emailRegData = (EditText) view.findViewById(R.id.email_reg_form);
        emailRegData.setTypeface(geometric);

        passwordRegData = (EditText) view.findViewById(R.id.password_reg_form);
        passwordRegData.setTypeface(geometric);

        confirmPasswordRegData = (EditText) view.findViewById(R.id.confirm_password_reg_form);
        confirmPasswordRegData.setTypeface(geometric);

        phoneRegData = (EditText) view.findViewById(R.id.phone_reg_form);
        phoneRegData.setTypeface(geometric);

        countryRegData = (EditText) view.findViewById(R.id.country_reg_form);
        countryRegData.setTypeface(geometric);

        cityRegData = (EditText) view.findViewById(R.id.city_reg_form);
        cityRegData.setTypeface(geometric);

        indexRegData = (EditText) view.findViewById(R.id.index_reg_form);
        indexRegData.setTypeface(geometric);

        streetRegData = (EditText) view.findViewById(R.id.street_reg_form);
        streetRegData.setTypeface(geometric);

        houseNumRegData = (EditText) view.findViewById(R.id.house_num_reg_form);
        houseNumRegData.setTypeface(geometric);

        officeNumRegData = (EditText) view.findViewById(R.id.flat_num_reg_form);
        officeNumRegData.setTypeface(geometric);

        registrationButton = (LinearLayout) view.findViewById(R.id.registration_btn);
        enterButton = (LinearLayout) view.findViewById(R.id.enter_btn);

        registration = (RadioButton) view.findViewById(R.id.registration);
        enter = (RadioButton) view.findViewById(R.id.enter);

        personalCabinetForm = (LinearLayout) view.findViewById(R.id.personal_cabinet_form);
        personalCabinetForm.setVisibility(LinearLayout.GONE);

        registrationLabel = (TextView) view.findViewById(R.id.registrationText);
        registrationLabel.setTypeface(geometric);

        loginLabel = (TextView) view.findViewById(R.id.login);
        loginLabel.setTypeface(geometric);

        nameFields = (LinearLayout) view.findViewById(R.id.name_fields);
        streetFields = (LinearLayout) view.findViewById(R.id.street_fields);
        houseFields = (LinearLayout) view.findViewById(R.id.num_house_fields);
        flatFields = (LinearLayout) view.findViewById(R.id.num_flat_fields);
        phoneFields = (LinearLayout) view.findViewById(R.id.phone_fields);
        countryFields = (LinearLayout) view.findViewById(R.id.country_fields);
        indexFields = (LinearLayout) view.findViewById(R.id.index_fields);
        confirmPasswordFields = (LinearLayout) view.findViewById(R.id.confirm_password_fields);
        cityFields = (LinearLayout) view.findViewById(R.id.city_fields);
        /*end RegistrationData*/


        formDataFragmentId = (LinearLayout) view.findViewById(R.id.formDataFragmentId);
        formDataFragmentId.setVisibility(LinearLayout.GONE);
        formDataFragmentId.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        baseLayout = (LinearLayout) view.findViewById(R.id.baseLayout);
        garbageLayout = (LinearLayout) view.findViewById(R.id.garbageLayout);


        baseLayout.setVisibility(LinearLayout.VISIBLE);
        garbageLayout.setVisibility(LinearLayout.GONE);


        personal_cabinet_text = (TextView) view.findViewById(R.id.personal_cabinet_text);
        personal_cabinet_text.setTypeface(geometric);

        deliver_text = (TextView) view.findViewById(R.id.deliverText);
        deliver_text.setTypeface(geometric);

        meal_text = (TextView) view.findViewById(R.id.mealText);
        meal_text.setTypeface(geometric);

        dateOrder = (TextView) view.findViewById(R.id.dateOrder);
        dateOrder.setTypeface(arimo);

        pay = (TextView) view.findViewById(R.id.textView25);
        pay.setTypeface(geometric);
        pay.setVisibility(LinearLayout.GONE);

        back = (TextView) view.findViewById(R.id.textView26);
        back.setTypeface(geometric);

        paymentMethod = (LinearLayout) view.findViewById(R.id.payment_method);
        paymentMethod.setVisibility(LinearLayout.GONE);

        formDataClick = (LinearLayout) view.findViewById(R.id.formDataClick);
        orderClick = (LinearLayout) view.findViewById(R.id.orderClick);
        personalCabinet = (LinearLayout) view.findViewById(R.id.personalCabinet);

        seb_btn = (ImageView) view.findViewById(R.id.seb_btn);
        swed_btn = (ImageView) view.findViewById(R.id.swed_btn);
        lhv_btn = (ImageView) view.findViewById(R.id.lhv_btn);
        nordea_btn = (ImageView) view.findViewById(R.id.nordea_btn);
        danske_btn = (ImageView) view.findViewById(R.id.danske_btn);
        kredit_btn = (ImageView) view.findViewById(R.id.kredit_btn);

        total = (LinearLayout) view.findViewById(R.id.total);
        totalText1 = (TextView) view.findViewById(R.id.total_text1);
        totalText1.setTypeface(arimo);
        totalText2 = (TextView) view.findViewById(R.id.total_text2);
        totalText2.setTypeface(geometric);
        total.setVisibility(LinearLayout.GONE);

        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        deliveryBTN = (LinearLayout) view.findViewById(R.id.delivery_btn);
        nonDeliveryBTN = (LinearLayout) view.findViewById(R.id.non_delivery_btn);

        delivery = (RadioButton) view.findViewById(R.id.delivery);
        nonDelivery = (RadioButton) view.findViewById(R.id.non_delivery);

        withDelivery = (TextView) view.findViewById(R.id.withDelivery);
        withDelivery.setTypeface(geometric);

        withoutDelivery = (TextView) view.findViewById(R.id.withoutDelivery);
        withoutDelivery.setTypeface(geometric);

        street = (LinearLayout) view.findViewById(R.id.street);
        numHouse = (LinearLayout) view.findViewById(R.id.num_house);
        numFlat = (LinearLayout) view.findViewById(R.id.num_flat);

        yourName = (EditText) view.findViewById(R.id.yourName);
        yourName.setTypeface(geometric);

        yourCity = (EditText) view.findViewById(R.id.yourCity);
        yourCity.setTypeface(geometric);

        yourStreet = (EditText) view.findViewById(R.id.yourStreet);
        yourStreet.setTypeface(geometric);

        yourHouse = (EditText) view.findViewById(R.id.yourHouseNum);
        yourHouse.setTypeface(geometric);

        yourFlat = (EditText) view.findViewById(R.id.yourFlatNum);
        yourFlat.setTypeface(geometric);

        yourEmail = (EditText) view.findViewById(R.id.yourEmail);
        yourEmail.setTypeface(geometric);

        yourPhone = (EditText) view.findViewById(R.id.yourPhone);
        yourPhone.setTypeface(geometric);

        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        dateOrder.setText(updateDate());

        name = (TextView) view.findViewById(R.id.name);
        name.setTypeface(arimo);

        city = (TextView) view.findViewById(R.id.city);
        city.setTypeface(arimo);

        streetText = (TextView) view.findViewById(R.id.streetText);
        streetText.setTypeface(arimo);

        house = (TextView) view.findViewById(R.id.numHouse);
        house.setTypeface(arimo);

        flat = (TextView) view.findViewById(R.id.numFlat);
        flat.setTypeface(arimo);

        email = (TextView) view.findViewById(R.id.email);
        email.setTypeface(arimo);

        phone = (TextView) view.findViewById(R.id.phone);
        phone.setTypeface(arimo);


        deliveryData = DelivaryData.getInstance();

        initListeners();

        return view;
    }


    private void initListeners(){


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
            @Override
            public void onClick(View v) {
                deliveryData.setDelivaryType(true);
                delivery.setChecked(true);
                nonDelivery.setChecked(false);

                street.setVisibility(LinearLayout.VISIBLE);
                numHouse.setVisibility(LinearLayout.VISIBLE);
                numFlat.setVisibility(LinearLayout.VISIBLE);
            }
        });

        nonDeliveryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveryData.setDelivaryType(false);
                delivery.setChecked(false);
                nonDelivery.setChecked(true);

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
                new MainAsyncTask(restaurantActivity).execute(RestaurantList.getRestaurant().getMenuLink());
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
                    removeOrderFragment();
                    total.setVisibility(LinearLayout.GONE);
                    paymentMethod.setVisibility(LinearLayout.GONE);
//                    pay.setVisibility(LinearLayout.GONE);
                    orderDataClickFlag = false;
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
                    removeOrderFragment();
                    total.setVisibility(LinearLayout.GONE);
                    paymentMethod.setVisibility(LinearLayout.GONE);
//                    pay.setVisibility(LinearLayout.GONE);
                    orderDataClickFlag = false;
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
                    removeOrderFragment();
                    orderDataClickFlag = false;
                    total.setVisibility(LinearLayout.GONE);
                    paymentMethod.setVisibility(LinearLayout.GONE);
//                    pay.setVisibility(LinearLayout.GONE);
                }
                else {
                    addOrderFragment();
                    orderDataClickFlag = true;
                    total.setVisibility(LinearLayout.VISIBLE);
                    paymentMethod.setVisibility(LinearLayout.VISIBLE);
//                    pay.setVisibility(LinearLayout.VISIBLE);
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
                seb_btn.setBackground(getActivity().getResources().getDrawable(R.drawable.border));
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
                swed_btn.setBackground(getActivity().getResources().getDrawable(R.drawable.border));
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
                lhv_btn.setBackground(getActivity().getResources().getDrawable(R.drawable.border));

                delivaryData.setNameBank("lhv");
            }
        });

        nordea_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kredit_btn.setBackground(null);
                danske_btn.setBackground(null);
                nordea_btn.setBackground(getActivity().getResources().getDrawable(R.drawable.border));
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
                danske_btn.setBackground(getActivity().getResources().getDrawable(R.drawable.border));
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
                kredit_btn.setBackground(getActivity().getResources().getDrawable(R.drawable.border));
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

                okRegFormButton.setText(R.string.register);

                registrationData.setPersonalCabinetType(true);
            }
        });

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enter.setChecked(true);
                registration.setChecked(false);

                nameFields.setVisibility(LinearLayout.GONE);
                streetFields.setVisibility(LinearLayout.GONE) ;
                houseFields.setVisibility(LinearLayout.GONE) ;
                flatFields.setVisibility(LinearLayout.GONE) ;
                phoneFields.setVisibility(LinearLayout.GONE) ;
                countryFields.setVisibility(LinearLayout.GONE) ;
                indexFields.setVisibility(LinearLayout.GONE) ;
                confirmPasswordFields.setVisibility(LinearLayout.GONE);
                cityFields.setVisibility(LinearLayout.GONE) ;

                okRegFormButton.setText(R.string.enter);

                registrationData.setPersonalCabinetType(false);
            }
        });

        okRegFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new RegistrationOrLoginAsyncTask((AppCompatActivity) getActivity(), okRegFormButton, pay).execute("11");
            }
        });
    }
    /*End initListeners*/

    @Override
    public void onResume() {
        super.onResume();

        if (!onResumeFlag){
            onResumeFlag = true;
            return;
        }

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

    private void addOrderFragment(){
        ft = getActivity().getSupportFragmentManager().beginTransaction();

        for (String id : garbage.getListID()) {
            Meal meal = MealList.getMeal(id);
            ft.add(R.id.orderDataContainer, new OrderFragment(meal));
        }

        ft.commit();
    }

    private void removeOrderFragment(){
        ft = getActivity().getSupportFragmentManager().beginTransaction();
        for (String id : garbage.getListID()) {
            Meal meal = MealList.getMeal(id);

            if (meal.getOrderFragment() == null)
                continue;

            ft.remove(meal.getOrderFragment());
            meal.setOrderFragment(null);
        }
        ft.commit();
    }

    public boolean isOrderDataClickFlag() {
        return orderDataClickFlag;
    }

    public void setOrderDataClickFlag(boolean orderDataClickFlag) {
        this.orderDataClickFlag = orderDataClickFlag;
    }


    private void updateDelivaryData(){

        if (!this.isAdded())
            return;

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
}
