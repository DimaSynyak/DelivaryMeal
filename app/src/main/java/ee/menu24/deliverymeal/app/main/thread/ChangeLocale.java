package ee.menu24.deliverymeal.app.main.thread;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import ee.menu24.deliverymeal.app.R;
import ee.menu24.deliverymeal.app.main.RestaurantBody;
import ee.menu24.deliverymeal.app.main.menu.SlidingMenuConfig;
import ee.menu24.deliverymeal.app.main.service.Restaurant;
import ee.menu24.deliverymeal.app.main.service.RestaurantList;
import ee.menu24.deliverymeal.app.main.title.fragments.LoadPageFragment;
import ee.menu24.deliverymeal.app.restaurant.service.filter.IFilter;
import ee.menu24.deliverymeal.app.restaurant.service.filter.RestaurantFilter;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by 1 on 12.11.2015.
 */
public class ChangeLocale implements Runnable {
    private FragmentTransaction ft;
    private Count count;
    private boolean flagChangeLocale;
    private String url;

    private AppCompatActivity activity;
    private LoadPageFragment loadPageFragment;
    private RestaurantBody restaurantBody;
    private IFilter filter;
    private RestaurantList restaurantList;

    private boolean cancle;

    private Connection connection;
    private Connection.Response response;

    public ChangeLocale(AppCompatActivity activity) {
        this.activity = activity;
        restaurantList = RestaurantList.getInstance();
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LoadPageFragment getLoadPageFragment() {
        return loadPageFragment;
    }

    public void setLoadPageFragment(LoadPageFragment loadPageFragment) {
        this.loadPageFragment = loadPageFragment;
    }

    public boolean isFlagChangeLocale() {
        return flagChangeLocale;
    }

    public void setFlagChangeLocale(boolean flagChangeLocale) {
        this.flagChangeLocale = flagChangeLocale;
    }

    @Override
    public void run() {

        while(true){
            if (activity == null)
                return;

            if (!activity.hasWindowFocus()){
                activity.finish();
                activity = null;
                return;
            }
            else {
                break;
            }
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView searchButton = (TextView) activity.findViewById(R.id.search_button);
                searchButton.setVisibility(View.GONE);
            }
        });


        connection = Restaurant.getConnection();

        List<Restaurant> restaurants = restaurantList.getRestaurants();
        if (restaurants != null && restaurants.size() > 0) {
            ft = activity.getSupportFragmentManager().beginTransaction();
            for (Restaurant restaurant : restaurantList.getRestaurants()) {
                ft.remove(restaurant.getFragment());
            }
            ft.commit();

            restaurantList.clear();
        }


        count = new Count(5);
        loadPageFragment = new LoadPageFragment();
        loadPageFragment.setCount(count);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ft = activity.getSupportFragmentManager().beginTransaction();
                ft.add(R.id.languageContainer, loadPageFragment);
                ft.commit();

                if (flagChangeLocale) {
                    SlidingMenuConfig.getSlidingMenuConfig().removeFilterData();
                }
            }
        });





        /*************************************************/

        filter = RestaurantFilter.getInstance();

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


                if (flagChangeLocale) {
                    connection.url(url);
                }
                else {
                    filter.filter(connection);
                }

                response = connection.execute();
                connection.cookies(response.cookies());

                doc = response.parse();

                if (flagChangeLocale) {
                    filter.init(doc);
                }

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
                   break;
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



                    restaurantList.addRestaurant(restaurant);
                }
                count.complete();
                count.complete();
                synchronized (count) {
                    while (!count.isStateData()) {
                        count.wait(100);
                    }
                }
                break;
            } catch (IOException e) {
                try {
                    TimeUnit.MILLISECONDS.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            } catch (InterruptedException e) {
                break;
            } catch (Exception e){
              break;
            }
        }



        /***********************************************/


        while(true){
            if (activity == null)
                return;

            if (!activity.hasWindowFocus()){
                activity.finish();
                activity = null;
                return;
            }
            else {
                break;
            }
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ft = activity.getSupportFragmentManager().beginTransaction();
                ft.remove(loadPageFragment);
                ft.commit();

                restaurantBody = RestaurantBody.getInstance(activity);
                restaurantBody.init();

                if (flagChangeLocale) {
                    SlidingMenuConfig.getSlidingMenuConfig().addFilterData();
                    flagChangeLocale = false;
                }


                TextView searchButton = (TextView) activity.findViewById(R.id.search_button);
                searchButton.setVisibility(View.VISIBLE);

                activity = null;
            }
        });

        cancle = true;

    }

    public boolean isCancle() {
        return cancle;
    }

    public void setCancle(boolean cancle) {
        this.cancle = cancle;
    }
}
