package ee.menu24.deliverymeal.app.restaurant.thread;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import ee.menu24.deliverymeal.app.R;
import ee.menu24.deliverymeal.app.main.service.Restaurant;
import ee.menu24.deliverymeal.app.main.service.RestaurantList;
import ee.menu24.deliverymeal.app.main.thread.Count;
import ee.menu24.deliverymeal.app.main.title.Language;
import ee.menu24.deliverymeal.app.main.title.fragments.LoadPageFragment;
import ee.menu24.deliverymeal.app.restaurant.menu.SMCRestaurantActivity;
import ee.menu24.deliverymeal.app.restaurant.service.Garbage;
import ee.menu24.deliverymeal.app.restaurant.MealBody;
import ee.menu24.deliverymeal.app.restaurant.RestaurantActivity;
import ee.menu24.deliverymeal.app.restaurant.service.Garnir;
import ee.menu24.deliverymeal.app.restaurant.service.Meal;
import ee.menu24.deliverymeal.app.restaurant.service.MealList;
import ee.menu24.deliverymeal.app.restaurant.service.filter.MealFilter;
import ee.menu24.deliverymeal.app.restaurant.service.filter.RestaurantFilter;
import ee.menu24.deliverymeal.app.tools.Tools;

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
public class SearchThread implements Runnable {

    private FragmentTransaction ft;
    private Count count;
    private AppCompatActivity activity;
    private LoadPageFragment loadPageFragment;
    private MealBody mealBody;
    private Restaurant restaurant;
    private Language language;

    private Connection connection;
    private Connection.Response response;
    private List<Meal> meals_copy;
    private Garbage garbage;
    private MealFilter mealFilter;
    private RestaurantFilter restaurantFilter;
    private RestaurantList restaurantList;



    public SearchThread(AppCompatActivity activity) {
        this.activity = activity;
        restaurantList = RestaurantList.getInstance();
        mealBody = MealBody.getInstance(activity);
        language = Language.getInstance();
    }

    public LoadPageFragment getLoadPageFragment() {
        return loadPageFragment;
    }

    public void setLoadPageFragment(LoadPageFragment loadPageFragment) {
        this.loadPageFragment = loadPageFragment;
    }

    public void setActivity(AppCompatActivity activity){
        this.activity = activity;
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

        List<Meal> meals = MealList.getMeals();
        meals_copy = new ArrayList<>(meals);

        mealBody.deleteAllFragments();
        ((RestaurantActivity) activity).removeFragment();

        count = new Count(5);
        loadPageFragment = new LoadPageFragment();
        loadPageFragment.setCount(count);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ft = activity.getSupportFragmentManager().beginTransaction();
                ft.add(R.id.restaurantPercentContainer, loadPageFragment);
                ft.commit();
            }
        });



/*********************************************************************/

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
                    break;
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
                    break;
                }

                for (Element element : elements) {

                    Meal meal = new Meal();

                    meal.setId(Tools.getNumInt(element.attr("id")));
                    meal.setName(element.getElementsByClass("and-name").text());
                    meal.setComposition(element.getElementsByClass("and-composition").text());
                    meal.setWeight(element.getElementsByClass("pull-right").text());
                    meal.setCost(element.getElementsByClass("as").get(0).text());
                    meal.setImgURL(element.getElementsByTag("img").attr("src"));

                    List<Garnir> garnirs = new ArrayList<>();

                    String temp = null;
                    int variation_id = 0;
                    Element pa_garnish = element.getElementById("pa_garnish");
                    if (pa_garnish != null) {
                        Elements option = pa_garnish.getElementsByTag("option");

                        boolean flag = false;
                        if (option != null)

                            temp = element.select("form[data-product_id =" + meal.getId() + " ]").attr("data-product_variations");
                        variation_id = Tools.getVariationId(temp);
                        for (Element element1 : option) {
                            if (flag){
                                Garnir garnir = new Garnir();
                                garnir.setGarnirName(element1.text());
                                garnir.setGarnirValue(element1.attr("value"));
                                garnir.setGarnirId(variation_id);
                                variation_id++;
                                garnirs.add(garnir);
                            }
                            flag = true;
                        }
                    }

                    meal.setGarnirs(garnirs);
                    // TODO: 02.12.2015 add id garnir

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
                break;
            } catch (MalformedURLException e) {
                System.out.println(1);
                break;
            } catch (IOException e) {
                try {
                    TimeUnit.MILLISECONDS.sleep(2000);
                } catch (InterruptedException e1) {
                    System.out.println(2);
                    break;
                }
            } catch (InterruptedException e) {
                System.out.println(3);
                break;
            }
            finally {
                MealList.setMealListCompleteFlag(true);
            }
        }


        /***************************************************/


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
            }
        });


        if (restaurant == null) {
            activity.finish();
            activity = null;
            return;
        }

        ((RestaurantActivity) activity).initFragment(restaurant);

        ((RestaurantActivity) activity).updateInfo();

        if (!mealFilter.isStateMealFilter()) {
            SMCRestaurantActivity.getSmcRestaurantActivity().addFilterData();
        }

        mealBody.init();

        mealFilter.setStateMealFilter(false);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView searchButton = (TextView) activity.findViewById(R.id.search_button);
                searchButton.setVisibility(View.VISIBLE);

                activity = null;
            }
        });
    }
}
