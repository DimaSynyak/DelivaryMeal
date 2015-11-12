package com.dmitriy.sinyak.delivarymeal.app.activity.main.thread;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.MainActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.RestaurantBody;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.Restaurant;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.RestaurantList;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.fragments.LoadPageFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by 1 on 12.11.2015.
 */
public class ChangeLocale extends AsyncTask<String, Void, String> {
    private FragmentTransaction ft;
    private Count count;

    private AppCompatActivity activity;
    private LoadPageFragment loadPageFragment;
    private RestaurantBody restaurantBody;

    private boolean isCancled;

    public ChangeLocale(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        List<Restaurant> restaurants = RestaurantList.getRestaurants();
        if (restaurants != null && restaurants.size() > 0) {
            ft = activity.getSupportFragmentManager().beginTransaction();
            for (Restaurant restaurant : RestaurantList.getRestaurants()) {
                ft.remove(restaurant.getFragment());
            }
            ft.commit();

            restaurants.clear();
        }


        count = new Count(5);
        loadPageFragment = new LoadPageFragment(count);
        ft = activity.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.languageContainer, loadPageFragment);
        ft.commit();
    }

    @Override
    protected String doInBackground(String... params) {

        synchronized (count){
            while (!count.isStateLoadFragment()){
                try {
                    count.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        count.complete();
        while (true) {
            Document doc = null;
            try {
                doc = Jsoup.connect(params[0]).get();
                count.complete();
                Elements elements = doc.getElementsByClass("food-item");

                if (elements.size() == 0)
                    return null; //WARNING change (pick out) ui
                count.complete();

                for (Element element : elements) {

                    Restaurant restaurant = new Restaurant();

                    restaurant.setCostMeal(element.getElementsByClass("and-cost-mil").get(0).html());
                    restaurant.setCostDeliver(element.getElementsByClass("and-cost-deliver").get(0).html());
                    restaurant.setTimeDeliver(element.getElementsByClass("and-time-deliver").get(0).html());

                    restaurant.setCostMealStatic(activity.getResources().getString(R.string.min_cost_order));
                    restaurant.setCostDeliverStatic(activity.getResources().getString(R.string.cost_deliver));
                    restaurant.setTimeDeliverStatic(activity.getResources().getString(R.string.time_deliver));

                    restaurant.setImgSRC(element.getElementsByTag("img").get(0).attr("src"));
                    restaurant.setName(element.getElementsByClass("and-name").html());
                    restaurant.setProfile(element.getElementsByClass("and-profile").html());
                    restaurant.setStars(element.getElementsByClass("star").get(0).getElementsByTag("span").attr("style"));
                    restaurant.setMenuLink(element.getElementsByClass("food-img").get(0).getElementsByTag("a").attr("href"));

                    URL imgURL = new URL(restaurant.getImgSRC());
                    restaurant.setImgBitmap(BitmapFactory.decodeStream(imgURL.openConnection().getInputStream()));
                    RestaurantList.addRestaurant(restaurant);
                }
                count.complete();
                count.complete();
                synchronized (count) {
                    while (!count.isStateData()) {
                        count.wait(100);
                    }
                }
                return null;
            } catch (IOException e) {
                try {
                    TimeUnit.MILLISECONDS.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            } catch (InterruptedException e) {
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

            restaurantBody = RestaurantBody.getInstance(activity);
            restaurantBody.init();

            isCancled = true;
     cancel(true);
    }

    public boolean isCancled() {
        return isCancled;
    }

    public void setIsCancled(boolean isCancled) {
        this.isCancled = isCancled;
    }

    public LoadPageFragment getLoadPageFragment() {
        return loadPageFragment;
    }

    public void setLoadPageFragment(LoadPageFragment loadPageFragment) {
        this.loadPageFragment = loadPageFragment;
    }
}
