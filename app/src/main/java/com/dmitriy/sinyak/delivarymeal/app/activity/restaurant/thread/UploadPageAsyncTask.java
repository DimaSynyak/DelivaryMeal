package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.thread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.Restaurant;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.RestaurantList;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.thread.Count;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.fragments.LoadPageFragment;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.IRestaurantActivityDestroy;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.MealBody;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.RestaurantActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.menu.SMCRestaurantActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.Meal;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.MealList;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.filter.MealFilter;
import com.dmitriy.sinyak.delivarymeal.app.activity.tools.Tools;

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
public class UploadPageAsyncTask extends AsyncTask<String, Void, String> {

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
    private int numPage;
    int numMealBeforeUpload;
    private MealFilter filter;

    public UploadPageAsyncTask() {
        super();
        RestaurantActivity.setiDestroy(new IRestaurantActivityDestroy() {
            @Override
            public void change() {
                UploadPageAsyncTask.this.cancel(true);
                Restaurant.setNumPage(2);
            }
        });
    }

    public UploadPageAsyncTask(AppCompatActivity activity) {
        this();
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        connection = Restaurant.getConnection();

        numPage = Restaurant.getNumPage();

    }

    @Override
    protected String doInBackground(String... params) {

        filter = MealFilter.getInstance();

        while (true) {
            try {

                if (Restaurant.getNumPage() < 0)
                    return null;

                while (true) {

                    filter.filter(connection, Restaurant.getNumPage());

                    response = connection.execute();

                    numMealBeforeUpload = MealList.getMeals().size();

                    Document doc = null;
                    doc = response.parse();

                    Elements elements = doc.getElementsByClass("item-food");

                    if (elements.size() == 0) {
                        Restaurant.setNumPage(-1);
                        break;
                    } else {
                        int num = Restaurant.getNumPage();
                        Restaurant.setNumPage(++num);
                    }

                    for (Element element : elements) {

                        Meal meal = new Meal();

                        meal.setId(Tools.getNumInt(element.getElementsByClass("add_to_cart_button").attr("data-product_id")));
                        meal.setName(element.getElementsByClass("and-name").get(0).html());
                        meal.setComposition(element.getElementsByClass("and-composition").get(0).html());
                        meal.setWeight(element.getElementsByClass("pull-right").get(0).html());
                        meal.setCost(element.getElementsByClass("as").get(0).html());
                        meal.setImgURL(element.getElementsByClass("item-img").get(0).getElementsByTag("img").attr("src"));

                        if (isCancelled())
                            return null;
                        MealList.addMeal(meal);
                    }
                }
                Restaurant.setNumPage(2);
                return null;

            }catch(IOException e){
                try {
                    TimeUnit.MILLISECONDS.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }catch (Exception e){
                System.out.println(1);
            }
        }

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        cancel(true);
    }

    public LoadPageFragment getLoadPageFragment() {
        return loadPageFragment;
    }

    public void setLoadPageFragment(LoadPageFragment loadPageFragment) {
        this.loadPageFragment = loadPageFragment;
    }
}
