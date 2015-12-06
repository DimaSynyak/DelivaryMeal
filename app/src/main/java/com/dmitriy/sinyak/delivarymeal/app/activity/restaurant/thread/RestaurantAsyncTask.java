package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.thread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.fragments.AddressDataFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.SlidingMenuConfig;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.Restaurant;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.RestaurantList;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.thread.Count;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.fragments.LoadPageFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.MealBody;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.RestaurantActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.body.ReviewFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.menu.SMCRestaurantActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.Garnir;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.Meal;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.MealList;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.filter.IFilter;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.filter.MealFilter;
import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
    private URL imgURL;

    private Connection connection;
    private Connection.Response response;
    private IFilter filter;

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

        filter = MealFilter.getInstance();

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

                filter.init(doc);

                count.complete();
                Elements elements = doc.getElementsByClass("item-food");

                Restaurant restaurant = RestaurantList.getRestaurant();

                /**/
                restaurant.setSpecializationField(doc.getElementsByClass("spec").text());
                restaurant.setWorkDayField(doc.getElementsByClass("rab").text());

                List<String> list = new ArrayList<>();
                for (Element element : doc.getElementsByClass("vremya")) {
                        list.add(element.text());
                }

                restaurant.setWorkTimeFields(list);

                restaurant.setSpecializationData(doc.getElementById("android-spec").text());
                restaurant.setWorkDayData(doc.getElementById("android-workday").text());

                list = new ArrayList<>();

                list.add(doc.getElementById("android-worktime").text());
                list.add(doc.getElementById("android-worktime2").text());

                restaurant.setWorkTimesData(list);

                Element desc = doc.getElementById("android-about");
                restaurant.setTitleDescription(desc.text());
                StringBuilder str = new StringBuilder();

                Element content = doc.getElementById("android-content");

                for (Element element : content.getElementsByTag("p")){
                    str.append(element.text());
                    str.append(" ");
                }

                restaurant.setDescription(str.toString());

                restaurant.setTitleBranchOffices(doc.getElementById("android-title-branch").text());

                list = new ArrayList<>();
                for (Element element : doc.getElementsByClass("adres").get(0).getElementsByTag("p")) {
                    list.add(element.text());
                }
                restaurant.setAddressBranchOffices(list);
                /**/

                if (elements.size() == 0) {
                    count.complete();
                    count.complete();
                    count.complete();

                    synchronized (count) {
                        while (!count.isStateData()) {
                            count.wait(100);
                        }
                    }
                    return null; //WARNING change (pick out) ui
                }


                count.complete();
                for (Element element : elements) {

                    Meal meal = new Meal();

                    meal.setId(element.getElementsByClass("add_to_cart_button").attr("data-product_id"));
                    meal.setName(element.getElementsByClass("and-name").get(0).html());
                    meal.setComposition(element.getElementsByClass("and-composition").get(0).html());
                    meal.setWeight(element.getElementsByClass("pull-right").get(0).html());
                    meal.setCost(element.getElementsByClass("as").get(0).html());
                    meal.setImgURL(element.getElementsByClass("item-img").get(0).getElementsByTag("img").attr("src"));

                    List<Garnir> garnirs = new ArrayList<>();


                    Element pa_garnish = element.getElementById("pa_garnish");
                    if (pa_garnish != null) {
                        Elements option = pa_garnish.getElementsByTag("option");

                        if (option != null)
                            for (Element element1 : option) {
                                Garnir garnir = new Garnir();
                                garnir.setGarnirName(element1.text());
                                garnir.setGarnirValue(element1.attr("value"));
                                garnirs.add(garnir);
                            }
                    }

                    meal.setGarnirs(garnirs);
                    // TODO: 02.12.2015 add id garnir

                    MealList.addMeal(meal);
                }
                count.complete();

                elements = doc.getElementsByClass("otzuvu");

                if (elements == null || elements.size() == 0){
                    count.complete();

                    synchronized (count) {
                        while (!count.isStateData()) {
                            count.wait(100);
                        }
                    }
                    return null;
                }

                for (Element element : elements){
                    ft = activity.getSupportFragmentManager().beginTransaction();
                    ReviewFragment reviewFragment = new ReviewFragment();
                    reviewFragment.setNameText(element.getElementsByTag("span").get(2).text());
                    reviewFragment.setReviewText(element.getElementsByTag("p").text());
                    reviewFragment.setTimeText(element.getElementsByClass("time").text());
                    reviewFragment.setRatingBarText(element.getElementsByClass("star").get(0).getElementsByTag("span").attr("style"));

                    ft.add(R.id.reviews_container, reviewFragment);
                    ft.commit();
                }


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

        ((RestaurantActivity) activity).initInfo();
        new UploadReviews(activity).start();
        MealList.startUploadPageAsycTask(activity);
        cancel(true);
    }

    public LoadPageFragment getLoadPageFragment() {
        return loadPageFragment;
    }

    public void setLoadPageFragment(LoadPageFragment loadPageFragment) {
        this.loadPageFragment = loadPageFragment;
    }
}
