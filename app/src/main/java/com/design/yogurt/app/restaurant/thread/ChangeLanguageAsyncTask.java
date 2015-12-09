package com.design.yogurt.app.restaurant.thread;

import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

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

        restaurantFilter = RestaurantFilter.getInstance();
        mealFilter = MealFilter.getInstance();

        garbage = Garbage.getInstance();
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
                connection.url(params[0]);
//                restaurantFilter.filter(connection);
                connection.request().data().clear();

                response = connection.execute();
                connection.cookies(response.cookies());

                doc = response.parse();
                count.complete();
                elements = doc.getElementsByClass("food-item");

                if (elements.size() == 0) {
                    count.complete();
                    count.complete();
                    count.complete();


                    synchronized (count) {
                        while (!count.isStateData()) {
                            count.wait(100);
                        }
                    }
                    return null;
                }

                List<Restaurant> restaurants = restaurantList.getRestaurants();
                int sizeRestaurants = restaurants.size();
                int iter = 0;

                for (Element element : elements) {
                    if (sizeRestaurants <= 0)
                        break;

                    Restaurant restaurant = restaurants.get(iter);

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


                    iter++;
                    sizeRestaurants--;
                }

                restaurant = restaurantList.getRestaurant();

                if (restaurant == null) {
                    count.complete();
                    count.complete();
                    count.complete();

                    synchronized (count) {
                        while (!count.isStateData()) {
                            count.wait(100);
                        }
                    }
                    return null;
                }


                count.complete();

                connection.url(restaurant.getMenuLink());
                response = connection.execute();
                connection.cookies(response.cookies());

                doc = response.parse();

                if (!mealFilter.isStateMealFilter()) {
                    mealFilter.init(doc);
                } else {
                    mealFilter.filter(connection);
                }
                response = connection.execute();
                connection.cookies(response.cookies());

                doc = response.parse();


                count.complete();
                elements = doc.getElementsByClass("item-food");

                if ((elements.size() == 0)) {
                    count.complete();
                    synchronized (count) {
                        while (!count.isStateData()) {
                            count.wait(100);
                        }
                    }
                    return null;
                }


 /*LOAD INFO*/
                if (!mealFilter.isStateMealFilter()){
                    restaurant.setSpecializationField(doc.getElementsByClass("spec").text());
                    restaurant.setWorkDayField(doc.getElementsByClass("rab").text());

                    List<String> list = new ArrayList<>();
                    for (Element element : doc.getElementsByClass("vremya")) {
                        list.add(element.text());
                    }

                    restaurant.setWorkTimeFields(list);


                    Element specializationData = doc.getElementById("android-spec");
                    if (specializationData != null)
                        restaurant.setSpecializationData(specializationData.text());

                    Element workDayData = doc.getElementById("android-workday");
                    if (workDayData != null)
                        restaurant.setWorkDayData(workDayData.text());

                    list = new ArrayList<>();

                    Element workTime1 = doc.getElementById("android-worktime");
                    if (workTime1 != null) {
                        list.add(workTime1.text());
                    }

                    Element workTime2 = doc.getElementById("android-worktime2");
                    if (workTime2 != null) {
                        list.add(workTime2.text());
                    }

                    restaurant.setWorkTimesData(list);

                    Element desc = doc.getElementById("android-about");
                    if (desc != null) {
                        restaurant.setTitleDescription(desc.text());
                    }
                    StringBuilder str = new StringBuilder();

                    Element content = doc.getElementById("android-content");

                    if (content != null) {
                        for (Element element : content.getElementsByTag("p")) {
                            str.append(element.text());
                            str.append(" ");
                        }

                        restaurant.setDescription(str.toString());
                    }

                    Element titleBranchOffices = doc.getElementById("android-title-branch");

                    if (titleBranchOffices != null)
                        restaurant.setTitleBranchOffices(titleBranchOffices.text());

                    list = new ArrayList<>();

                    Elements addressBranches = doc.getElementsByClass("adres").get(0).getElementsByTag("p");
                    if (addressBranches != null) {
                        for (Element element : doc.getElementsByClass("adres").get(0).getElementsByTag("p")) {
                            list.add(element.text());
                        }
                        restaurant.setAddressBranchOffices(list);
                    }

                }
 /* END LOAD INFO*/
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
                    MealList.getMeal(meal.getId()).setCountMeal(meal.getCountMeal());
                }

                count.complete();
                synchronized (count) {
                    while (!count.isStateData()) {
                        count.wait(100);
                    }
                }
                return null;
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
