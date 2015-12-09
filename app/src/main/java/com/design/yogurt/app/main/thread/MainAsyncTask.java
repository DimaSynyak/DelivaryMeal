package com.design.yogurt.app.main.thread;

import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.design.yogurt.app.IActivity;
import com.design.yogurt.app.main.MainActivity;
import com.design.yogurt.app.main.RestaurantBody;
import com.design.yogurt.app.main.menu.SlidingMenuConfig;
import com.design.yogurt.app.main.service.Restaurant;
import com.design.yogurt.app.main.service.RestaurantList;
import com.design.yogurt.app.main.title.fragments.LoadBarFragment;
import com.design.yogurt.app.restaurant.service.filter.IFilter;
import com.design.yogurt.app.restaurant.service.filter.RestaurantFilter;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by 1 on 11.11.2015.
 */
public class MainAsyncTask extends AsyncTask<String, Void, String> {

    private FragmentTransaction ft;
    private Count count;
    private IActivity activity;
    private LoadBarFragment loadBarFragment;
    private SlidingMenuConfig slidingMenuConfig;
    private RestaurantBody restaurantBody;

    private Connection connection;
    private Connection.Response response;
    private IFilter filter;

    private RestaurantList restaurantList;

    public MainAsyncTask(IActivity activity) {
        this.activity = activity;
        restaurantList = RestaurantList.getInstance();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        connection = Restaurant.getConnection();
        count = new Count(5);
        loadBarFragment = new LoadBarFragment();
        loadBarFragment.setCount(count);
        ft = ((MainActivity)activity).getSupportFragmentManager().beginTransaction();
        ft.add(R.id.languageContainer, loadBarFragment);
        ft.commit();

    }

    @Override
    protected String doInBackground(String... params) {
        filter = RestaurantFilter.getInstance();
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
            Document doc = null;
            try {

                filter.filter(connection);
                connection.url(params[0]);
                response = connection.execute();
                connection.cookies(response.cookies());
                doc = response.parse();

                count.complete();

                Elements elements = doc.getElementsByClass("food-item");

                filter.init(doc);

                if ((elements.size() == 0)){
                    count.complete();
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

                    restaurant.setCostMealStatic(((MainActivity) activity).getResources().getString(R.string.min_cost_order));
                    restaurant.setCostDeliverStatic(((MainActivity )activity).getResources().getString(R.string.cost_deliver));
                    restaurant.setTimeDeliverStatic(((MainActivity )activity).getResources().getString(R.string.time_deliver));

                    restaurant.setImgSRC(element.getElementsByTag("img").get(0).attr("src"));
                    restaurant.setName(element.getElementsByClass("and-name").html());
                    restaurant.setProfile(element.getElementsByClass("and-profile").html());
                    restaurant.setStars(element.getElementsByClass("star").get(0).getElementsByTag("span").attr("style"));
                    restaurant.setMenuLink(element.getElementsByClass("food-img").get(0).getElementsByTag("a").attr("href"));

                    restaurantList.addRestaurant(restaurant);
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

        ft = ((MainActivity )activity).getSupportFragmentManager().beginTransaction();
        ft.remove(loadBarFragment);
        ft.commit();
        ((MainActivity )activity).getActionBarActivity().show();

        slidingMenuConfig = new SlidingMenuConfig(((MainActivity )activity));
        slidingMenuConfig.initSlidingMenu();
        ((MainActivity )activity).setSlidingMenuConfig(slidingMenuConfig);


        restaurantBody = RestaurantBody.getInstance(((MainActivity )activity));
        restaurantBody.init();
    }
}
