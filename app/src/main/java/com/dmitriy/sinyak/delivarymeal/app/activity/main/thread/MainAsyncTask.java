package com.dmitriy.sinyak.delivarymeal.app.activity.main.thread;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.dmitriy.sinyak.delivarymeal.app.activity.IActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.MainActivity;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.RestaurantBody;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.SlidingMenuConfig;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.Restaurant;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.service.RestaurantList;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.fragments.LoadBarFragment;
import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;

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
public class MainAsyncTask extends AsyncTask<String, Void, String> {

    private FragmentTransaction ft;
    private Thread th;
    private Fragment fragment;
    private Count count;
    private Thread countThread;
    private CountThread countThreadR;
    private IActivity activity;
    private LoadPage loadPage;
    private TextView dynamicTextView;
    private LoadBarFragment loadBarFragment;
    private SlidingMenuConfig slidingMenuConfig;
    private RestaurantBody restaurantBody;


    public MainAsyncTask(IActivity activity) {
        this.activity = activity;
        loadBarFragment = ((MainActivity)activity).getLoadBarFragment();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        count = new Count(5);
        loadPage = new LoadPage(count, activity);
        loadPage.start();

    }

    @Override
    protected String doInBackground(String... params) {

        while (!count.isStateLoadFragment()){
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        dynamicTextView = (TextView) loadBarFragment.getView().findViewById(R.id.dynamicText);

        countThreadR = new CountThread((MainActivity) activity, count, dynamicTextView);
        countThread = new Thread(countThreadR);
        countThread.start();

        while (true) {
            Document doc = null;
            try {
                count.complete();
                doc = Jsoup.connect(params[0]).get();
                count.complete();
//                bError = false;
                Elements elements = doc.getElementsByClass("food-item");

                if (elements.size() == 0)
                    return null; //WARNING change (pick out) ui
                count.complete();

                for (Element element : elements) {

                    Restaurant restaurant = new Restaurant();

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
//                bError = true;

                try {
                    TimeUnit.MILLISECONDS.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
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

        restaurantBody = new RestaurantBody(((MainActivity )activity));
        restaurantBody.init();
    }
}
