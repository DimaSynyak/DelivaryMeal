package com.dmitriy.sinyak.delivarymeal.app.activity.payment.menu.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TimePicker;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.payment.thread.DelivaryData;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 1 on 20.11.2015.
 */
public class FormDataFragment extends Fragment {

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

    private EditText yourName;
    private EditText yourCity;
    private EditText yourStreet;
    private EditText yourHouse;
    private EditText yourFlat;
    private EditText yourEmail;
    private EditText yourPhone;

    private  int _hour;
    private int _minute;

    private static FormDataFragment formDataFragment;

    public String updateDate(){
        int day1 = datePicker.getDayOfMonth();
        int month1 = datePicker.getMonth();
        int year1 = datePicker.getYear();

        return day1 + "." + month1 + "." + year1 + " " + _hour + ":" + _minute;
    }

    public static FormDataFragment getInstance(){
        if (formDataFragment == null){
            formDataFragment = new FormDataFragment();
        }

        return formDataFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.form_data_fragment, container, false);

        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        deliveryBTN = (LinearLayout) view.findViewById(R.id.delivery_btn);
        nonDeliveryBTN = (LinearLayout) view.findViewById(R.id.non_delivery_btn);
        delivery = (RadioButton) view.findViewById(R.id.delivery);
        nonDelivery = (RadioButton) view.findViewById(R.id.non_delivery);

        street = (LinearLayout) view.findViewById(R.id.street);
        numHouse = (LinearLayout) view.findViewById(R.id.num_house);
        numFlat = (LinearLayout) view.findViewById(R.id.num_flat);

        yourName = (EditText) view.findViewById(R.id.yourName);
        yourCity = (EditText) view.findViewById(R.id.yourCity);
        yourStreet = (EditText) view.findViewById(R.id.yourStreet);
        yourHouse = (EditText) view.findViewById(R.id.yourHouseNum);
        yourFlat = (EditText) view.findViewById(R.id.yourFlatNum);
        yourEmail = (EditText) view.findViewById(R.id.yourEmail);
        yourPhone = (EditText) view.findViewById(R.id.yourPhone);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);

        deliveryData = DelivaryData.getInstance();

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                _hour = hourOfDay;
                _minute = minute;
            }
        });


        System.out.println(1);

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
        return view;
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
}
