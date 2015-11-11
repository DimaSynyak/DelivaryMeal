package com.dmitriy.sinyak.delivarymeal.app.activity.main.thread;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.IActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.MainActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.RestaurantBody;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.SlidingMenuConfig;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.Restaurant;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.RestaurantList;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.fragments.LoadBarFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.fragments.LoadPageFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.MealBody;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.RestaurantActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.Meal;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.MealList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by 1 on 11.11.2015.
 */
public class ChangeLanguageAsyncTask extends AsyncTask<String, Void, String> {

    private FragmentTransaction ft;
    private Thread th;
    private Fragment fragment;
    private Count count;
    private Thread countThread;
    private CountThread countThreadR;
    private IActivity activity;
    private TextView dynamicTextView;
    private LoadPercent loadPercent;
    private LoadPageFragment loadPageFragment;
    private MealBody mealBody;


    public ChangeLanguageAsyncTask(IActivity activity) {
        this.activity = activity;
        loadPageFragment = ((RestaurantActivity) activity).getLoadPageFragment();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        List<Meal> meals = MealList.getMeals();
        if (meals != null && meals.size() > 0) {
            ft = ((RestaurantActivity) activity).getSupportFragmentManager().beginTransaction();
            for (Meal meal : MealList.getMeals()) {
                ft.remove(meal.getFragment());
            }
            ft.commit();

            meals.clear();
        }

        count = new Count(5);
        loadPercent = new LoadPercent(count, activity);
        loadPercent.start();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            synchronized (count){
                while (!count.isStateLoadFragment()){
                    count.wait(100);
                }
            }

            dynamicTextView = (TextView) loadPageFragment.getView().findViewById(R.id.dynamicText);

            countThreadR = new CountThread(((RestaurantActivity) activity), count, dynamicTextView);
            countThread = new Thread(countThreadR);
            countThread.start();

            Document doc = null;

            doc = Jsoup.connect(params[0]).get();
            count.complete();
//                bError = false;
            Elements elements = doc.getElementsByClass("food-item");

            if (elements.size() == 0)
                return null; //WARNING change (pick out) ui
            count.complete();

            Element element = elements.get(((RestaurantActivity)activity).getPositionRestaurant());
            if (element == null)
                return null;

            Restaurant restaurant = new Restaurant();

            restaurant.setCostMeal(element.getElementsByClass("and-cost-mil").get(0).html());
            restaurant.setCostDeliver(element.getElementsByClass("and-cost-deliver").get(0).html());
            restaurant.setTimeDeliver(element.getElementsByClass("and-time-deliver").get(0).html());

            restaurant.setCostMealStatic(((RestaurantActivity) activity).getResources().getString(R.string.min_cost_order));
            restaurant.setCostDeliverStatic(((RestaurantActivity) activity).getResources().getString(R.string.cost_deliver));
            restaurant.setTimeDeliverStatic(((RestaurantActivity) activity).getResources().getString(R.string.time_deliver));

            restaurant.setImgSRC(element.getElementsByTag("img").get(0).attr("src"));
            restaurant.setName(element.getElementsByClass("and-name").html());
            restaurant.setProfile(element.getElementsByClass("and-profile").html());
            restaurant.setStars(element.getElementsByClass("star").get(0).getElementsByTag("span").attr("style"));
            restaurant.setMenuLink(element.getElementsByClass("food-img").get(0).getElementsByTag("a").attr("href"));

            URL imgURL = new URL(restaurant.getImgSRC());
            restaurant.setImgBitmap(BitmapFactory.decodeStream(imgURL.openConnection().getInputStream()));
            ((RestaurantActivity)activity).changeFragment(restaurant);

            count.complete();

            doc = Jsoup.connect(restaurant.getMenuLink()).get();
            count.complete();
            elements = doc.getElementsByClass("item-food");

            if (elements.size() == 0)
                return null; //WARNING change (pick out) ui


            for (Element element1 :elements) {

                Meal meal = new Meal();

                meal.setName(element1.getElementsByClass("and-name").get(0).html());
                meal.setComposition(element1.getElementsByClass("and-composition").get(0).html());
                meal.setWeight(element1.getElementsByClass("pull-right").get(0).html());
                meal.setCost(element1.getElementsByClass("as").get(0).html());
                meal.setImgURL(element1.getElementsByClass("item-img").get(0).getElementsByTag("img").attr("src"));

                imgURL = new URL(meal.getImgURL());
                meal.setImg(BitmapFactory.decodeStream(imgURL.openConnection().getInputStream()));
                MealList.addMeal(meal);
            }

            count.complete();
            synchronized (count) {
                while (!count.isStateData()) {
                    count.wait(100);
                }
            }
            return null;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        ft = ((RestaurantActivity) activity).getSupportFragmentManager().beginTransaction();
        ft.remove(loadPageFragment);
        ft.commit();

        mealBody = new MealBody(((RestaurantActivity) activity));
        mealBody.init();
    }
}
