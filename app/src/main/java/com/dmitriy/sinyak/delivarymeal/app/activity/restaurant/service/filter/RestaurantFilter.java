package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.filter;

import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Language;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmitriy on 12/3/15.
 */
public class RestaurantFilter implements IFilter{

    private List<FilterData> categoryList;
    private List<FilterData> criteriaList;
    private Language language;

    public RestaurantFilter() {
        this.language = Language.getInstance();
        categoryList = new ArrayList<>();
        criteriaList = new ArrayList<>();
    }

    @Override
    public void filter(Connection connection) {

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
        Elements categories = document.getElementsByClass("food-category");

        if (categories == null)
            return;

        for (Element category : categories){
            FilterData filterData = new FilterData();
            filterData.setName(category.attr("name"));
            filterData.setId(category.attr("id"));

            Element li = category.parent();
            filterData.setText(li.getElementById(filterData.getId()).text());

            categoryList.add(filterData);
        }

        Elements criterias = document.getElementsByAttributeValue("type", "checkbox");

        if (criterias == null)
            return;

        for (Element criteria : criterias){
            FilterData filterData = new FilterData();
            filterData.setId(criteria.attr("id"));

            Element li = criteria.parent();
            filterData.setName(li.getElementById(filterData.getId()).text());

            criteriaList.add(filterData);
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
}
