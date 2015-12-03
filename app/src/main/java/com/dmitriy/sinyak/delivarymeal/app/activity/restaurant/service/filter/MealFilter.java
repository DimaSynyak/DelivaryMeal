package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.filter;

import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Language;

import org.jsoup.Connection;

import java.util.List;

/**
 * Created by dmitriy on 12/3/15.
 */
public class MealFilter implements IFilter {

    private List<FilterData> filterDataList;

    @Override
    public void filter(Connection connection) {

    }

    @Override
    public void init(Connection connection) {
        connection.url();
    }

    public List<FilterData> getFilterDataList() {
        return filterDataList;
    }

    public void setFilterDataList(List<FilterData> filterDataList) {
        this.filterDataList = filterDataList;
    }
}
