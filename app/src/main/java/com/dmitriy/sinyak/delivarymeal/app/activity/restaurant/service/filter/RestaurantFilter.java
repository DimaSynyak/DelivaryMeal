package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.filter;

import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Language;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dmitriy on 12/3/15.
 */
public class RestaurantFilter implements IFilter{

    private List<FilterData> categoryList;
    private List<FilterData> criteriaList;
    private static FilterData searchData;
    private Language language;
    private Map<String, String> map;

    private static RestaurantFilter restaurantFilter;

    public RestaurantFilter() {
        this.language = Language.getInstance();
        categoryList = new ArrayList<>();
        criteriaList = new ArrayList<>();
        searchData = new FilterData();
        searchData.setText("");
        map = new HashMap<>();
    }

    public static RestaurantFilter getInstance(){
        if (restaurantFilter == null){
            restaurantFilter = new RestaurantFilter();
        }
        return restaurantFilter;
    }

    @Override
    public void filter(Connection connection) {

        connection.request().data().clear();

        map.clear();

        switch(language.getLanguages()) {
            case RU:{
                connection.url("http://menu24.ee/wp-admin/admin-ajax.php?lang=ru");
                break;
            }
            case EE:{
                connection.url("http://menu24.ee/wp-admin/admin-ajax.php?lang=et");
                break;
            }
            case EN:{
                connection.url("http://menu24.ee/wp-admin/admin-ajax.php?lang=en");
                break;
            }
        }

        map.put("action", "get_restaurants");
        map.put("search", searchData.getText());
        map.put("opened", "false");

        for (FilterData filterData : categoryList) {
            if (filterData.isStateUse() == true)
                map.put("foodCategories", filterData.getName());
        }

        for (FilterData filterData : criteriaList) {
            if (filterData.isStateUse() == true)
                map.put(filterData.getId(), "true");
        }

        map.put("minOrder", "20");


        connection.data(map);
        connection.method(Connection.Method.POST);
    }

    @Override
    public void filter(Connection connection, int numPage) {
        return;
    }

    @Override
    public void init(Connection connection) {
        connection.url(language.getURL());
        // TODO: 12/3/15
    }

    @Override
    public void init(Connection.Response response) {
        // TODO: 12/3/15
    }

    @Override
    public void init(Document document) {

        Elements criterias = document.getElementsByAttributeValue("type", "checkbox");

        if (criterias == null)
            return;

        for (Element criteria : criterias){
            if (criteria.getElementsByAttribute("class").size() == 0) {
                FilterData filterData = new FilterData();
                filterData.setId(criteria.attr("id"));

                Element li = criteria.parent();
                filterData.setText(li.getElementsByAttributeValue("for", filterData.getId()).text());

                criteriaList.add(filterData);
            }
        }

        Elements categories = document.getElementsByClass("food-category");

        if (categories == null)
            return;

        for (Element category : categories){
            FilterData filterData = new FilterData();
            filterData.setName(category.attr("name"));
            filterData.setId(category.attr("id"));

            Element li = category.parent();
            filterData.setText(li.getElementsByAttributeValue("for", filterData.getId()).text());

            categoryList.add(filterData);
        }




    }

    public List<FilterData> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<FilterData> categoryList) {
        this.categoryList = categoryList;
    }

    public List<FilterData> getCriteriaList() {
        return criteriaList;
    }

    public void setCriteriaList(List<FilterData> criteriaList) {
        this.criteriaList = criteriaList;
    }

    public static FilterData getSearchData() {
        return searchData;
    }

    public static void setSearchData(FilterData searchData) {
        RestaurantFilter.searchData = searchData;
    }
}
