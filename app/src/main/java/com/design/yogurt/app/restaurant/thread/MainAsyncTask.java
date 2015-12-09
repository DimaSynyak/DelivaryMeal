package com.design.yogurt.app.restaurant.thread;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.design.yogurt.app.lib.NumberProgressBar;
import com.design.yogurt.app.main.service.Restaurant;
import com.design.yogurt.app.restaurant.service.Garbage;
import com.design.yogurt.app.restaurant.service.DelivaryData;
import com.design.yogurt.app.restaurant.service.Garnir;
import com.design.yogurt.app.restaurant.service.Meal;
import com.design.yogurt.app.restaurant.service.MealList;

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
    private NumberProgressBar numberProgressBar;
    private int containerId;

    public MainAsyncTask(AppCompatActivity activity, int conteinerId) {
        this.activity = activity;
        this.containerId = conteinerId;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        connection = Restaurant.getConnection();
        delivaryData = DelivaryData.getInstance();
        garbage = Garbage.getInstance();
        mealList = MealList.getMeals();

        numberProgressBar = new NumberProgressBar(5, containerId, activity);
        numberProgressBar.init();
    }

    @Override
    protected String doInBackground(String... params) {


        try {
            numberProgressBar.levelComplete();
            connection.url(params[0]);
            connection.ignoreContentType(true);
            numberProgressBar.levelComplete();
            for (Meal meal : garbage.getListOrderMeal()){

                if (meal == null)
                    continue;

                if (meal.getGarnirs() == null || meal.getGarnirs().size() == 0) {
                    for (int i = 0; i < meal.getCountMeal(); i++) {
                        connection.request().data().clear();
                        connection.data("wc-ajax", "add_to_cart").data("lang", "ru").data("product_id", String.valueOf(meal.getId())).method(Connection.Method.GET);
                        response = connection.execute();
                        connection.cookies(response.cookies());
                    }
                } else {
                    for (int i = 0; i < meal.getCountMeal(); i++) {

                        connection.request().data().clear();

                        Garnir garnir = meal.getOrderGarnirs().get(i);
                        connection
                                .data("attribute_pa_garnish", garnir.getGarnirValue())
                                .data("quantity", "1")
                                .data("add-to-cart",String.valueOf(meal.getId()))
                                .data("variation_id", String.valueOf(garnir.getGarnirId()))
                                .data("product_id", String.valueOf(meal.getId()))
                                .method(Connection.Method.POST);
                        response = connection.execute();
                        connection.cookies(response.cookies());
                    }
                }
            }
            numberProgressBar.levelComplete();
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
            numberProgressBar.levelComplete();
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
            numberProgressBar.levelComplete();
            forms = doc.getElementsByAttribute("action");
            url = forms.get(0).attr("action");

            numberProgressBar.waitLoadNumber();
        } catch (Exception e) {
            e.printStackTrace();// Display out data about non-internet
            numberProgressBar.levelComplete();
            numberProgressBar.levelComplete();
            numberProgressBar.levelComplete();

            try {
                numberProgressBar.waitLoadNumber();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }

        return "good";
    }



    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s == null)
            return;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(intent);
        Restaurant.setConnection("http://menu24.ee/");
//        activity.finish();
        activity = null;
    }
}
