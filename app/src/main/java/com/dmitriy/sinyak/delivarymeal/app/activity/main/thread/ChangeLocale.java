package com.dmitriy.sinyak.delivarymeal.app.activity.main.thread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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

import org.jsoup.Connection;
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

    private Connection connection;
    private Connection.Response response;

    public ChangeLocale(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        connection = Restaurant.getConnection();

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
                connection.url(params[0]);
                response = connection.execute();
                connection.cookies(response.cookies());

                doc = response.parse();
                count.complete();
                Elements elements = doc.getElementsByClass("food-item");

                if (elements.size() == 0) {
                    count.complete();
                    count.complete();
                    count.complete();

                    synchronized (count) {
                        while (!count.isStateData()) {
                            count.wait(100);
                        }
                    }
                    return  null;
                }

                count.complete();

                for (Element element : elements) {

                    Restaurant restaurant = new Restaurant();

                    restaurant.setId(Integer.parseInt(element.attr("data-id")));
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

                    try{
                        URL imgURL = new URL(restaurant.getImgSRC());
                        Bitmap image = BitmapFactory.decodeStream(imgURL.openConnection().getInputStream());
                        float k = image.getWidth()/image.getHeight();
                        int width = 100;
                        int height = (int) (width * k);
                        restaurant.setImgBitmap(Bitmap.createScaledBitmap(image, width, height, true));
                    }
                    catch (IOException e){
//                        restaurant.setImgBitmap(((BitmapDrawable) ((MainActivity) activity).getResources().getDrawable(R.drawable.no_image)).getBitmap());
                    }

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
