package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.thread;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.Restaurant;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.Garbage;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.Meal;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.MealList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

import example.Tools;

/**
 * Created by 1 on 23.11.2015.
 */
public class MainAsyncTask extends AsyncTask<String, String, String>{

    private Connection connection;
    private Connection.Response response;
    private AppCompatActivity activity;
    private String url;
    private DelivaryData delivaryData;
    private Garbage garbage;
    private List<Meal> mealList;

    public MainAsyncTask(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        connection = Restaurant.getConnection();
        delivaryData = DelivaryData.getInstance();
        garbage = Garbage.getInstance();
        mealList = MealList.getMeals();
    }

    @Override
    protected String doInBackground(String... params) {


        try {
            connection.url(params[0]);
            connection.ignoreContentType(true);

            for (String id : garbage.getListID()){
                Meal meal = MealList.getMeal(id);

                if (meal == null)
                    continue;

                for (int i = 0; i < meal.getCountMeal(); i++) {
                    connection.data("wc-ajax", "add_to_cart").data("lang", "ru").data("product_id", meal.getId()).method(Connection.Method.GET);
                    response = connection.execute();
                    connection.cookies(response.cookies());
                }
            }

            Document doc = response.parse();
            Elements elements = doc.select("input");
            String _wpnonce = null;
            String _wp_http_referer = null;

            boolean flag1 = false;
            boolean flag2 = false;

            for (Element element:elements){
                if (element.attr("id").contains("wpnonce")){
                    if (element.attr("name").contains("_wpnonce")){
                        if (element.attr("type").contains("hidden")) {
                            _wpnonce = Tools.getCutting(element.attr("value"));
                            flag1 = true;
                        }
                    }
                }

                if (element.attr("name").contains("_wp_http_referer")){
                    if (element.attr("type").contains("hidden")){
                        _wp_http_referer = Tools.getCutting(element.attr("value"));
                        flag2 = true;
                    }
                }

                if (flag1 && flag2){
                    break;
                }
            }

            Restaurant.set_wpnonce(_wpnonce);

            connection.url("http://menu24.ee/checkout/");



            connection.data("billing_delivery", delivaryData.getDelivaryType());
            connection.data("billing_first_name", delivaryData.getYourName());
            connection.data("billing_country", "EE");

            connection.data("billing_city", delivaryData.getDelivaryCity());

            if (delivaryData.isDelivaryType()) {
                connection.data("billing_address_1", delivaryData.getNumStreet());
                connection.data("billing_address_2", delivaryData.getNumHouse());
                connection.data("billing_address_3", delivaryData.getNumFlat());
            }

            connection.data("billing_time", delivaryData.getDelivaryData());
            connection.data("billing_email", delivaryData.getEmail());
            connection.data("billing_phone", delivaryData.getNumPhone());
            connection.data("_wpnonce", _wpnonce);
            connection.data("_wp_http_referer", _wp_http_referer);
            connection.data("payment_method", "banklinkmaksekeskus");
            connection.data("PRESELECTED_METHOD_banklinkmaksekeskus", delivaryData.getNameBank());
            connection.data("woocommerce_checkout_place_order", "Maksma");

            connection.method(Connection.Method.POST);

            response = connection.execute();
            connection.cookies(response.cookies());

            connection = Jsoup.connect(String.valueOf(connection.response().url()));


//            connection.url(connection.response().url());

            response = connection.execute();

            Elements forms = null;


            doc = response.parse();

            forms = doc.getElementsByAttribute("action");
            url = forms.get(0).attr("action");


        } catch (Exception e) {
            e.printStackTrace();// Display out data about non-internet
        }

        return null;
    }



    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(intent);
        Restaurant.setConnection("http://menu24.ee/");
//        activity.finish();
    }
}
