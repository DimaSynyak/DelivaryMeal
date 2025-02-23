package ee.menu24.deliverymeal.app.restaurant.service;

import android.graphics.Bitmap;

import ee.menu24.deliverymeal.app.restaurant.body.RestaurantMealFragment;
import ee.menu24.deliverymeal.app.restaurant.menu.fragments.OrderFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 06.11.2015.
 */
public class Meal {

    private int id;
    private String name;
    private String cost;
    private String weight;
    private String composition;
    private static Bitmap img;
    private String imgURL;
    private RestaurantMealFragment fragment;
    private int countMeal;
    private Garbage garbage;
    private OrderFragment orderFragment;

    private List<Garnir> garnirs;
    private List<Garnir> orderGarnirs;


    private int remove_id;

    public Meal() {
        this.garbage = Garbage.getInstance();
    }

    public OrderFragment getOrderFragment() {
        return orderFragment;
    }

    public void setOrderFragment(OrderFragment orderFragment) {
        this.orderFragment = orderFragment;
    }

    public RestaurantMealFragment getFragment() {
        return fragment;
    }

    public void setFragment(RestaurantMealFragment fragment) {
        this.fragment = fragment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public static Bitmap getImg() {
        return img;
    }

    public static void setImg(Bitmap img) {
        Meal.img = img;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public int getCountMeal() {
        return countMeal;
    }

    public void setCountMeal(int countMeal) {
        this.countMeal = countMeal;
    }

    public void add(){
        this.countMeal++;
        garbage.add(this);
    }

    public void remove(){
        if (countMeal > 0){
            countMeal--;
            garbage.remove(this);
        }
    }

    public void removeAll(){
        countMeal = 0;
        garbage.removeAll();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public synchronized List<Garnir> getGarnirs() {
        return garnirs;
    }

    public synchronized void setGarnirs(List<Garnir> garnirs) {
        this.garnirs = garnirs;
    }

    public List<Garnir> getOrderGarnirs() {
        return orderGarnirs;
    }

    public void setOrderGarnirs(List<Garnir> orderGarnirs) {
        this.orderGarnirs = orderGarnirs;
    }

    public void addGarnir(Garnir orderGarnir) {
        if (orderGarnirs == null){
            orderGarnirs = new ArrayList<>();
        }

        this.orderGarnirs.add(orderGarnir);
    }

    public void addGarnir(String nameGarnir) {
        if (garnirs == null || garnirs.size() == 0)
            return;

        for (Garnir garnir : garnirs) {
            if (garnir.getGarnirName().equals(nameGarnir)){
                if (orderGarnirs == null){
                    orderGarnirs = new ArrayList<>();
                }
                this.orderGarnirs.add(garnir);
                return;
            }
        }
    }
}
