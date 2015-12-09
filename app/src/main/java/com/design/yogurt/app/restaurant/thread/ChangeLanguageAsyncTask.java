package com.design.yogurt.app.restaurant.thread;

import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.design.yogurt.app.main.service.Restaurant;
import com.design.yogurt.app.main.service.RestaurantList;
import com.design.yogurt.app.main.thread.Count;
import com.design.yogurt.app.main.title.fragments.LoadPageFragment;
import com.design.yogurt.app.restaurant.menu.SMCRestaurantActivity;
import com.design.yogurt.app.restaurant.service.Garbage;
import com.design.yogurt.app.restaurant.MealBody;
import com.design.yogurt.app.restaurant.RestaurantActivity;
import com.design.yogurt.app.restaurant.service.Meal;
import com.design.yogurt.app.restaurant.service.MealList;
import com.design.yogurt.app.restaurant.service.filter.MealFilter;
import com.design.yogurt.app.restaurant.service.filter.RestaurantFilter;
import com.design.yogurt.app.tools.Tools;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by 1 on 11.11.2015.
 */
public class ChangeLanguageAsyncTask extends AsyncTask<String, Void, String> {

    private FragmentTransaction ft;
    private Count count;
    private AppCompatActivity activity;
    private LoadPageFragment loadPageFragment;
    private MealBody mealBody;
    private Restaurant restaurant;

    private boolean isCancled;

    private Connection connection;
    private Connection.Response response;
    private List<Meal> meals_copy;
    private Garbage garbage;
    private MealFilter mealFilter;
    private RestaurantFilter restaurantFilter;
    private RestaurantList restaurantList;



    public ChangeLanguageAsyncTask(AppCompatActivity activity) {
        this.activity = activity;
        restaurantList = RestaurantList.getInstance();
        mealBody = MealBody.getInstance(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        TextView searchButton = (TextView) activity.findViewById(R.id.search_button);
        searchButton.setVisibility(View.GONE);
        connection = Restaurant.getConnection();

        List<Meal> meals = MealList.getMeals();
        meals_copy = new ArrayList<>(meals);

        mealBody.deleteAllFragments();
        ((RestaurantActivity) activity).removeFragment();


        count = new Count(5);
        loadPageFragment = new LoadPageFragment();
        loadPageFragment.setCount(count);
        ft = activity.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.restaurantPercentContainer, loadPageFragment);
        ft.commit();
    }

    @Override
    protected String doInBackground(String... params) {

        restaurantList = RestaurantList.getInstance();
        mealFilter = MealFilter.getInstance();
        garbage = Garbage.getInstance();

        restaurant = restaurantList.getRestaurant();

        Document doc = null;
        Elements elements = null;

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

            try {
                MealList.setMealListCompleteFlag(false);


                mealFilter.filter(connection);
                response = connection.execute();
                connection.cookies(response.cookies());
                count.complete();
                doc = response.parse();

                count.complete();
                elements = doc.getElementsByClass("item-food");

                if ((elements.size() == 0)) {
                    count.complete();
                    count.complete();
                    synchronized (count) {
                        while (!count.isStateData()) {
                            count.wait(100);
                        }
                    }
                    return null;
                }

                for (Element element1 : elements) {

                    Meal meal = new Meal();

                    meal.setId(Tools.getNumInt(element1.getElementsByClass("add_to_cart_button").attr("data-product_id")));
                    meal.setName(element1.getElementsByClass("and-name").get(0).html());
                    meal.setComposition(element1.getElementsByClass("and-composition").get(0).html());
                    meal.setWeight(element1.getElementsByClass("pull-right").get(0).html());
                    meal.setCost(element1.getElementsByClass("as").get(0).html());
                    meal.setImgURL(element1.getElementsByClass("item-img").get(0).getElementsByTag("img").attr("src"));

                    MealList.addMeal(meal);
                }

                for (Meal meal : garbage.getListOrderMeal()) {
                    Meal ml = MealList.getMeal(meal.getId());
                    if (ml != null) {
                        ml.setCountMeal(meal.getCountMeal());
                    }
                }

                count.complete();
                count.complete();
                synchronized (count) {
                    while (!count.isStateData()) {
                        count.wait(100);
                    }
                }
                return "good";
            } catch (MalformedURLException e) {
                return null;
            } catch (IOException e) {
                try {
                    TimeUnit.MILLISECONDS.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                    return null;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally {
                MealList.setMealListCompleteFlag(true);
            }
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);


        ft = activity.getSupportFragmentManager().beginTransaction();
        ft.remove(loadPageFragment);
        ft.commit();

        isCancled = true;
        cancel(true);

        if (restaurant == null)
            return;

        ((RestaurantActivity) activity).initFragment(restaurant);
        ((RestaurantActivity) activity).updateInfo();

        if (!mealFilter.isStateMealFilter()) {
            SMCRestaurantActivity.getSmcRestaurantActivity().addFilterData();
        }

        mealBody.init();

        mealFilter.setStateMealFilter(false);

        TextView searchButton = (TextView) activity.findViewById(R.id.search_button);
        searchButton.setVisibility(View.VISIBLE);

        activity = null;
//        count = null;
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
