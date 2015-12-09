package com.design.yogurt.app.restaurant.thread;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.design.yogurt.app.lib.NumberProgressBar;
import com.design.yogurt.app.main.service.Restaurant;
import com.design.yogurt.app.main.thread.Count;
import com.design.yogurt.app.restaurant.service.RegistrationData;
import com.dmitriy.sinyak.delivarymeal.app.R;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by 1 on 27.11.2015.
 */
public class RegistrationOrLoginAsyncTask extends AsyncTask<String, String, String> {
    private Connection connection;
    private Connection.Response response;
    private RegistrationData registrationData;
    private ProgressDialog progressDialog;
    private AppCompatActivity activity;
    private Count count;
    private NumberProgressBar numberProgressBar;
    private TextView[] ok;
    private  int containerId;

    public RegistrationOrLoginAsyncTask(AppCompatActivity activity, TextView... textViews) {
        this.activity = activity;
        ok = textViews;
    }

    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        for (TextView text: ok) {
            text.setVisibility(TextView.GONE);
        }


        // TODO: 27.11.2015
        
        connection = Restaurant.getConnection();
        registrationData = RegistrationData.getInstance();
        numberProgressBar = new NumberProgressBar(5, containerId, activity);
        numberProgressBar.init();
    }

    @Override
    protected String doInBackground(String... params) {
        try {

            numberProgressBar.levelComplete();
            if (registrationData.isStateLogin()){                       // state LOGOUT

                connection.url(registrationData.getLogoutLink());
                response = connection.execute();
                connection.cookies(response.cookies());

                numberProgressBar.levelComplete();
                numberProgressBar.levelComplete();
                numberProgressBar.levelComplete();
                numberProgressBar.levelComplete();

                registrationData.setStateLogin(false);
            }
            else {                                                      //state REGISTRATION
                if (registrationData.isPersonalCabinetType()) {
                    connection.url("http://menu24.ee/ru/registration/");
                    connection.data("user_name", registrationData.getName());
                    connection.data("email", registrationData.getEmail());
                    connection.data("password", registrationData.getPassword());
                    connection.data("confirmation-password", registrationData.getConfirmPassword());
                    connection.data("phone", registrationData.getNumPhone());
                    connection.data("country", "EE");
                    connection.data("city", registrationData.getCity());
                    connection.data("billing_postcode", registrationData.getIndex());
                    connection.data("billing_address_1", registrationData.getNumStreet());
                    connection.data("billing_address_2", registrationData.getNumHouse());
                    connection.data("billing_address_3", registrationData.getNumFlat());

                    numberProgressBar.levelComplete();
                    connection.method(Connection.Method.POST);

                    response = connection.execute();
                    connection.cookies(response.cookies());
                    numberProgressBar.levelComplete();

                    Document parse = response.parse();
                    Elements errors = parse.getElementsByClass("error");

                    if (errors == null || errors.size() == 0) {
                        numberProgressBar.levelComplete();
                        numberProgressBar.levelComplete();
                        numberProgressBar.waitLoadNumber();
                        return null;
                    }

                    numberProgressBar.levelComplete();
                    numberProgressBar.levelComplete();
                } else {                                                    //state LOGIN
                    connection.url("http://menu24.ee/wp-login.php?lang=ru");
                    numberProgressBar.levelComplete();
                    connection.data("log", registrationData.getEmail());
                    numberProgressBar.levelComplete();
                    connection.data("pwd", registrationData.getPassword());
                    numberProgressBar.levelComplete();
                    connection.method(Connection.Method.POST);
                    response = connection.execute();
                    connection.cookies(response.cookies());

                    Document parse = response.parse();
                    Elements errors = parse.getElementsByAttribute("aria-describedby");

                    if (errors != null && errors.size() != 0) {
                        numberProgressBar.levelComplete();
                        numberProgressBar.waitLoadNumber();
                        return null;
                    }

                    Element logout = parse.getElementById("android-logout");

                    if (logout == null) {
                        numberProgressBar.levelComplete();
                        numberProgressBar.waitLoadNumber();
                        return null;
                    }


                    try {
                        connection.url("http://menu24.ee/ru/my-account/");
                        connection.method(Connection.Method.GET);

                        connection.request().data().clear();

                        response = connection.execute();
                        connection.cookies(response.cookies());

                        Document parsePersonalCabinet = response.parse();

                        Elements data = parsePersonalCabinet.select("input[name = user_name]");
                        if (data != null)
                            registrationData.setName(data.val());

                        data = parsePersonalCabinet.select("input[name = billing_phone]");
                        if (data != null)
                            registrationData.setNumPhone(data.val());

                        data = parsePersonalCabinet.select("input[name = billing_city]");
                        if (data != null)
                            registrationData.setCity(data.val());

                        data = parsePersonalCabinet.select("input[name = billing_postcode]");
                        if (data != null)
                            registrationData.setIndex(data.val());

                        data = parsePersonalCabinet.select("input[name = billing_address_1]");
                        if (data != null)
                            registrationData.setNumStreet(data.val());

                        data = parsePersonalCabinet.select("input[name = billing_address_2]");
                        if (data != null)
                            registrationData.setNumHouse(data.val());

                        data = parsePersonalCabinet.select("input[name = billing_address_3]");
                        if (data != null)
                            registrationData.setNumFlat(data.val());
                    }
                    catch (IOException e){
                        System.out.println(e);
                    }




                    registrationData.setLogoutLink(logout.attr("href"));

                    numberProgressBar.levelComplete();
                }

                registrationData.setStateLogin(true);

            }


            numberProgressBar.waitLoadNumber();
        } catch (Exception e) {
            numberProgressBar.levelComplete();
            numberProgressBar.levelComplete();
            numberProgressBar.levelComplete();

            try {
                numberProgressBar.waitLoadNumber();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        for (TextView text : ok) {
            text.setVisibility(TextView.VISIBLE);
        }

        EditText nameRegData = (EditText) activity.findViewById(R.id.name_reg_form);
        nameRegData.setText(registrationData.getName());

        EditText phoneRegData = (EditText) activity.findViewById(R.id.phone_reg_form);
        phoneRegData.setText(registrationData.getNumPhone());

        EditText countryRegData = (EditText) activity.findViewById(R.id.country_reg_form);
        countryRegData.setText("Eesti");

        EditText cityRegData = (EditText) activity.findViewById(R.id.city_reg_form);
        cityRegData.setText(registrationData.getCity());

        EditText indexRegData = (EditText) activity.findViewById(R.id.index_reg_form);
        indexRegData.setText(registrationData.getIndex());

        EditText streetRegData = (EditText) activity.findViewById(R.id.street_reg_form);
        streetRegData.setText(registrationData.getNumStreet());

        EditText houseNumRegData = (EditText) activity.findViewById(R.id.house_num_reg_form);
        houseNumRegData.setText(registrationData.getNumHouse());

        EditText officeNumRegData = (EditText) activity.findViewById(R.id.flat_num_reg_form);
        officeNumRegData.setText(registrationData.getNumFlat());

        activity = null;
    }



}
