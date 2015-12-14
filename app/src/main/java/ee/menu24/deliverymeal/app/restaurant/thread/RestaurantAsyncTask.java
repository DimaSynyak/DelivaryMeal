package ee.menu24.deliverymeal.app.restaurant.thread;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import ee.menu24.deliverymeal.app.R;
import ee.menu24.deliverymeal.app.main.service.Restaurant;
import ee.menu24.deliverymeal.app.main.service.RestaurantList;
import ee.menu24.deliverymeal.app.main.thread.Count;
import ee.menu24.deliverymeal.app.main.title.fragments.LoadPageFragment;
import ee.menu24.deliverymeal.app.restaurant.MealBody;
import ee.menu24.deliverymeal.app.restaurant.RestaurantActivity;
import ee.menu24.deliverymeal.app.restaurant.body.ReviewFragment;
import ee.menu24.deliverymeal.app.restaurant.menu.SMCRestaurantActivity;
import ee.menu24.deliverymeal.app.restaurant.service.Garnir;
import ee.menu24.deliverymeal.app.restaurant.service.Meal;
import ee.menu24.deliverymeal.app.restaurant.service.MealList;
import ee.menu24.deliverymeal.app.restaurant.service.filter.IFilter;
import ee.menu24.deliverymeal.app.restaurant.service.filter.MealFilter;
import ee.menu24.deliverymeal.app.tools.Tools;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by 1 on 11.11.2015.
 */
public class RestaurantAsyncTask implements Runnable {

    private FragmentTransaction ft;
    private Count count;
    private AppCompatActivity activity;
    private LoadPageFragment loadPageFragment;
    private SMCRestaurantActivity slidingMenuConfig;
    private MealBody mealBody;
    private RestaurantList restaurantList;

    private Connection connection;
    private Connection.Response response;
    private IFilter filter;
    private Restaurant restaurant;

    public RestaurantAsyncTask() {
        super();
    }

    public RestaurantAsyncTask(AppCompatActivity activity) {
        this();
        this.activity = activity;
        restaurantList = RestaurantList.getInstance();
        restaurant = restaurantList.getRestaurant();
    }

    public void removeActivity(){
        activity = null;
    }

    public LoadPageFragment getLoadPageFragment() {
        return loadPageFragment;
    }

    public void setLoadPageFragment(LoadPageFragment loadPageFragment) {
        this.loadPageFragment = loadPageFragment;
    }

    @Override
    public void run() {

        connection  = Restaurant.getConnection();

        count = new Count(5);

        loadPageFragment = new LoadPageFragment();
        loadPageFragment.setCount(count);
        ft = activity.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.restaurantPercentContainer, loadPageFragment);
        ft.commit();


        /********************************/
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
                connection.url(restaurant.getMenuLink());
                response = connection.execute();
                connection.cookies(response.cookies());

                Document doc = null;
                doc = response.parse();

                filter.init(doc);

                count.complete();

                /*GET RESTAURANT NAME FOR FILTER*/
                Element nameForFilter = doc.getElementById("restaurant");
                if (nameForFilter != null){
                    restaurant.setNameForFilter(nameForFilter.val());
                }
                /********************************/



                Elements elements = doc.getElementsByClass("item-food");

                Restaurant restaurant = restaurantList.getRestaurant();

                /*LOAD INFO*/
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

                Element workTime1 = doc.getElementById("android-worktime");
                if (workTime1 != null) {

                    String str = workTime1.html();
                    str = Tools.changeStroke(str, "<sup>", ":");
                    str = Tools.changeStroke(str, "</sup>", "");

                    list.add(str);
                }

                Element workTime2 = doc.getElementById("android-worktime2");
                if (workTime2 != null) {

                    String str = workTime2.html();
                    str = Tools.changeStroke(str, "<sup>", ":");
                    str = Tools.changeStroke(str, "</sup>", "");

                    list.add(str);
                }

                restaurant.setWorkTimesData(list);

                Element desc = doc.getElementById("android-about");
                if (desc != null) {
                    restaurant.setTitleDescription(desc.text());
                }
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
                /* END LOAD INFO*/

                if (elements.size() == 0) {
                    count.complete();
                    count.complete();
                    count.complete();

                    synchronized (count) {
                        while (!count.isStateData()) {
                            count.wait(100);
                        }
                    }
                    break; //WARNING change (pick out) ui
                }


                count.complete();
                for (Element element : elements) {

                    Meal meal = new Meal();

                    meal.setId(Tools.getNumInt(element.attr("id")));
                    meal.setName(element.getElementsByClass("and-name").get(0).html());
                    meal.setComposition(element.getElementsByClass("and-composition").text());
                    meal.setWeight(element.getElementsByClass("pull-right").get(0).html());
                    meal.setCost(element.getElementsByClass("as").get(0).html());
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
                count.complete();

                elements = doc.getElementsByClass("otzuvu");

                if (elements == null || elements.size() == 0){
                    count.complete();

                    synchronized (count) {
                        while (!count.isStateData()) {
                            count.wait(100);
                        }
                    }
                    break;
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

                break;
            }catch(IOException e){
                try {
                    TimeUnit.MILLISECONDS.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }catch(InterruptedException e){
                activity.finish();
                activity = null;
                return;
            }
            catch (Exception e){
                activity.finish();
                activity = null;
                return;
            }
        }
        /*******************************************/


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

                slidingMenuConfig = new SMCRestaurantActivity(activity);
                slidingMenuConfig.initSlidingMenu();
                ((RestaurantActivity) activity).setSlidingMenuConfig(slidingMenuConfig);

                mealBody = new MealBody(activity);
                mealBody.init();

                ((RestaurantActivity) activity).initInfo();
                new UploadReviews(activity).start();
                MealList.startUploadPageAsycTask(activity);
                activity = null;
            }
        });

    }
}
