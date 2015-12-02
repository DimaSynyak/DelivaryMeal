package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.thread;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.lib.NumberProgressBar;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.Restaurant;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.thread.Count;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.RestaurantActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.menu.fragments.MenuFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.DelivaryData;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.Garbage;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.Meal;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.MealList;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.RegistrationData;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import example.Tools;

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

    public RegistrationOrLoginAsyncTask(AppCompatActivity activity, TextView... textViews) {
        this.activity = activity;
        ok = textViews;
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
        numberProgressBar = new NumberProgressBar(5, R.id.container_id, activity);
        numberProgressBar.init();
    }

    @Override
    protected String doInBackground(String... params) {
        try {

            numberProgressBar.levelComplete();
            if (registrationData.isStateLogin()){

                connection.url(registrationData.getLogoutLink());
                response = connection.execute();
                connection.cookies(response.cookies());

                numberProgressBar.levelComplete();
                numberProgressBar.levelComplete();
                numberProgressBar.levelComplete();
                numberProgressBar.levelComplete();

                registrationData.setStateLogin(false);
            }
            else {
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
                } else {
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
                        response = connection.execute();
                        connection.cookies(response.cookies());

                        Document parsePersonalCabinet = response.parse();
                        Elements data = parsePersonalCabinet.getElementsByAttribute("name");

                        for (int i = 0; i < data.size(); i++) {
                            if (data.get(i).equals("user_name")){
                                registrationData.setName(data.get(i).text());
                            }

                            if (data.get(i).equals("email")){
                                registrationData.setEmail(data.get(i).text());
                            }

                            if (data.get(i).equals("billing_phone")){
                                registrationData.setNumPhone(data.get(i).text());
                            }

                            if (data.get(i).equals("billing_city")){
                                registrationData.setCity(data.get(i).text());
                            }

                            if (data.get(i).equals("billing_postcode")){
                                registrationData.setIndex(data.get(i).text());
                            }

                            if (data.get(i).equals("billing_address_1")){
                                registrationData.setNumStreet(data.get(i).text());
                            }

                            if (data.get(i).equals("billing_address_2")){
                                registrationData.setNumHouse(data.get(i).text());
                            }

                            if (data.get(i).equals("billing_address_3")){
                                registrationData.setNumFlat(data.get(i).text());
                            }
                        }


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
    }



}
