package com.dmitriy.sinyak.delivarymeal.app.activity.main.service;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;

import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.ILanguageListener;
import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Language;
import com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.IChange;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 05.11.2015.
 */
public class Restaurant {

    private static boolean loginState;

    private  int id;
    private String name;
    private String profile; //sostav edi
    private String stars;
    private String costMeal;
    private String costDeliver;
    private String timeDeliver;

    private String costMealStatic;
    private String costDeliverStatic;
    private String timeDeliverStatic;

    private String specializationField;
    private String workDayField;
    private List<String> workTimeFields;
    private String specializationData;
    private String workDayData;
    private List<String> workTimesData;

    private String titleDescription;
    private String description;
    private String titleBranchOffices;
    private List<String> addressBranchOffices;

    private String imgSRC;
    private Bitmap imgBitmap;
    private String menuLink;
    private Fragment fragment;
    private static int numPage = 2;

    private List<String> reviewNames;
    private List<String> reviewReviews;
    private List<String> reviewStars;
    private List<String> reviewTimes;
    private List<IChange> changes;

    static {
        Language.setiLanguage(new ILanguageListener() {
            @Override
            public void change() {
                numPage = 1;
            }
        });
    }

    private static Connection connection;
    private static String _wpnonce;

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        Restaurant.connection = connection;
    }

    public static void setConnection(String url) {
        Restaurant.connection = Jsoup.connect(url);
    }

    public static String get_wpnonce() {
        return _wpnonce;
    }

    public static void set_wpnonce(String _wpnonce) {
        Restaurant._wpnonce = _wpnonce;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Float getStars() {
        char[] str = stars.toCharArray();
        Float m = 0f;
        StringBuilder stringBuilder = new StringBuilder();
        for (char ch :str){
            if (ch >= '0' && ch <= '9' || ch == '.'){
                stringBuilder.append(ch);
            }
        }
        m =5 * Float.parseFloat(String.valueOf(stringBuilder))/100;

        return roundDownScale2(m);
    }

    public float roundDownScale2(float aValue) {
        BigDecimal decimal = new BigDecimal(aValue);
//        decimal = decimal.setScale(1,BigDecimal.ROUND_UP);
        decimal = decimal.setScale(1,BigDecimal.ROUND_DOWN);
        return  decimal.floatValue();
    }

    public void setStars(String stars) {
        this.stars = stars;  //Warning
    }

    public String getCostMeal() {
        return costMeal;
    }

    public void setCostMeal(String costMeal) {
        this.costMeal = costMeal;
    }

    public String getCostDeliver() {
        return costDeliver;
    }

    public void setCostDeliver(String costDeliver) {
        this.costDeliver = costDeliver;
    }

    public String getTimeDeliver() {
        return timeDeliver;
    }

    public void setTimeDeliver(String timeDeliver) {
        this.timeDeliver = timeDeliver;
    }

    public String getImgSRC() {
        return imgSRC;
    }

    public void setImgSRC(String imgSRC) {
        this.imgSRC = imgSRC;
    }

    public Bitmap getImgBitmap() {
        return imgBitmap;
    }

    public void setImgBitmap(Bitmap imgBitmap) {
        this.imgBitmap = imgBitmap;
    }

    public String getMenuLink() {
        return menuLink;
    }

    public void setMenuLink(String menuLink) {
        this.menuLink = menuLink;
    }

    public String getCostMealStatic() {
        return costMealStatic;
    }

    public void setCostMealStatic(String costMealStatic) {
        this.costMealStatic = costMealStatic;
    }

    public String getCostDeliverStatic() {
        return costDeliverStatic;
    }

    public void setCostDeliverStatic(String costDeliverStatic) {
        this.costDeliverStatic = costDeliverStatic;
    }

    public String getTimeDeliverStatic() {
        return timeDeliverStatic;
    }

    public void setTimeDeliverStatic(String timeDeliverStatic) {
        this.timeDeliverStatic = timeDeliverStatic;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static int getNumPage() {
        return numPage;
    }

    public static void setNumPage(int numPage) {
        Restaurant.numPage = numPage;
    }

    public String getSpecializationField() {
        return specializationField;
    }

    public void setSpecializationField(String specializationField) {
        this.specializationField = specializationField;
    }

    public String getWorkDayField() {
        return workDayField;
    }

    public void setWorkDayField(String workDayField) {
        this.workDayField = workDayField;
    }

    public List<String> getWorkTimeFields() {
        return workTimeFields;
    }

    public void setWorkTimeFields(List<String> workTimeFields) {
        this.workTimeFields = workTimeFields;
    }

    public String getTitleDescription() {
        return titleDescription;
    }

    public void setTitleDescription(String titleDescription) {
        this.titleDescription = titleDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitleBranchOffices() {
        return titleBranchOffices;
    }

    public void setTitleBranchOffices(String titleBranchOffices) {
        this.titleBranchOffices = titleBranchOffices;
    }

    public List<String> getAddressBranchOffices() {
        return addressBranchOffices;
    }

    public void setAddressBranchOffices(List<String> addressBranchOffices) {
        this.addressBranchOffices = addressBranchOffices;
    }

    public String getSpecializationData() {
        return specializationData;
    }

    public void setSpecializationData(String specializationData) {
        this.specializationData = specializationData;
    }

    public String getWorkDayData() {
        return workDayData;
    }

    public void setWorkDayData(String workDayData) {
        this.workDayData = workDayData;
    }

    public List<String> getWorkTimesData() {
        return workTimesData;
    }

    public void setWorkTimesData(List<String> workTimesData) {
        this.workTimesData = workTimesData;
    }

    public List<String> getReviewNames() {
        return reviewNames;
    }

    public void setReviewNames(List<String> reviewNames) {
        this.reviewNames = reviewNames;
    }

    public List<String> getReviewReviews() {
        return reviewReviews;
    }

    public void setReviewReviews(List<String> reviewReviews) {
        this.reviewReviews = reviewReviews;
    }

    public List<String> getReviewStars() {
        return reviewStars;
    }

    public void setReviewStars(List<String> reviewStars) {
        this.reviewStars = reviewStars;

        if (changes == null)
            return;

        for (IChange change:changes){
            change.change();
        }
    }

    public List<String> getReviewTimes() {
        return reviewTimes;
    }

    public void setReviewTimes(List<String> reviewTimes) {
        this.reviewTimes = reviewTimes;
    }

    public void setChanges(IChange change) {
        if (this.changes == null){
            this.changes = new ArrayList<>();
        }

        this.changes.add(change);
    }

    public static boolean isLoginState() {
        return loginState;
    }

    public static void setLoginState(boolean loginState) {
        Restaurant.loginState = loginState;
    }
}
