package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.thread;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.SlidingMenuConfig;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.Restaurant;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.thread.Count;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.fragments.LoadPageFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.MealBody;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.RestaurantActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.menu.SMCRestaurantActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.Meal;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.MealList;
import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by 1 on 11.11.2015.
 */
public class RestaurantAsyncTask extends AsyncTask<String, Void, String> {

    private FragmentTransaction ft;
    private Thread th;
    private Fragment fragment;
    private Count count;
    private AppCompatActivity activity;
    private LoadPageFragment loadPageFragment;
    private SMCRestaurantActivity slidingMenuConfig;
    private MealBody mealBody;

    private Connection connection;
    private Connection.Response response;

    public RestaurantAsyncTask() {
        super();
    }

    public RestaurantAsyncTask(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        connection  = Restaurant.getConnection();

        count = new Count(5);

        loadPageFragment = new LoadPageFragment(count);
        ft = activity.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.restaurantPercentContainer, loadPageFragment);
        ft.commit();
    }

    @Override
    protected String doInBackground(String... params) {

        synchronized (count) {
            while (!count.isStateLoadFragment()) {
                try {
                    count.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        count.complete();
        while (true) {
            try {

                connection.url(params[0]);
                response = connection.execute();
                connection.cookies(response.cookies());

                Document doc = null;
                doc = response.parse();

                count.complete();
                Elements elements = doc.getElementsByClass("item-food");

                if (elements.size() == 0)
                    return null; //WARNING change (pick out) ui
                count.complete();
                for (Element element : elements) {

                    Meal meal = new Meal();

                    meal.setId(element.getElementsByClass("add_to_cart_button").attr("data-product_id"));
                    meal.setName(element.getElementsByClass("and-name").get(0).html());
                    meal.setComposition(element.getElementsByClass("and-composition").get(0).html());
                    meal.setWeight(element.getElementsByClass("pull-right").get(0).html());
                    meal.setCost(element.getElementsByClass("as").get(0).html());
                    meal.setImgURL(element.getElementsByClass("item-img").get(0).getElementsByTag("img").attr("src"));

                    URL imgURL = new URL(meal.getImgURL());
                    meal.setImg(BitmapFactory.decodeStream(imgURL.openConnection().getInputStream()));
                    MealList.addMeal(meal);
                }
                count.complete();
                count.complete();
                synchronized (count) {
                    while (!count.isStateData()) {
                        count.wait(100);
                    }
                }
                return null;

            }catch(IOException e){
                try {
                    TimeUnit.MILLISECONDS.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }catch(InterruptedException e){
                return null;
            }
        }

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        ft = activity.getSupportFragmentManager().beginTransaction();
        ft.remove(loadPageFragment);
        ft.commit();

        slidingMenuConfig = new SMCRestaurantActivity(activity);
        slidingMenuConfig.initSlidingMenu();
        ((RestaurantActivity) activity).setSlidingMenuConfig(slidingMenuConfig);

        mealBody = new MealBody(activity);
        mealBody.init();
        cancel(true);
    }

    public LoadPageFragment getLoadPageFragment() {
        return loadPageFragment;
    }

    public void setLoadPageFragment(LoadPageFragment loadPageFragment) {
        this.loadPageFragment = loadPageFragment;
    }
}
