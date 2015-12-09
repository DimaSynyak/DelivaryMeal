package com.design.yogurt.app.restaurant.thread;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.dmitriy.sinyak.delivarymeal.app.R;
import com.design.yogurt.app.main.service.Restaurant;
import com.design.yogurt.app.main.service.RestaurantList;
import com.design.yogurt.app.restaurant.body.ReviewFragment;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by 1 on 02.12.2015.
 */
public class UploadReviews extends Thread {

    private Connection connection;
    private Connection.Response response;
    private Restaurant restaurant;
    private static int page = 2;
    private AppCompatActivity activity;
    private FragmentTransaction ft;
    private RestaurantList restaurantList;

    public UploadReviews() {
        super();
        this.connection = Restaurant.getConnection();
        restaurantList = RestaurantList.getInstance();
        restaurant = restaurantList.getRestaurant();
    }

    public UploadReviews(AppCompatActivity activity) {
        this();
        this.activity = activity;
    }

    @Override
    public void run() {
        super.run();

        while (true) {
            try {
                connection.url("http://menu24.ee/wp-admin/admin-ajax.php?lang=ru");
                connection.data("action", "get_comments");
                connection.data("filter", "all-comments");
                connection.data("restaurant", String.valueOf(restaurant.getId()));
                connection.data("page", String.valueOf(page));

                connection.method(Connection.Method.POST);
                response = connection.execute();
                connection.cookies(response.cookies());

                Document doc = response.parse();

                Elements elements = doc.getElementsByClass("otzuvu");

                if (elements == null || elements.size() == 0){
                    break;
                }

                page++;

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

                activity = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        page = 2;

    }
}
