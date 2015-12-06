package com.dmitriy.sinyak.delivarymeal.app.activity.restaurant.service.filter;


import com.dmitriy.sinyak.delivarymeal.app.activity.main.menu.fragments.FilterFragment;

import java.util.List;

/**
 * Created by dmitriy on 12/3/15.
 */
public class FilterData {
    private String name;
    private String id;
    private String text;
    private boolean stateUse;
    private List<FilterData> list;
    private FilterFragment filterFragment;


    public boolean isStateUse() {
        return stateUse;
    }

    public void setStateUse(boolean stateUse) {
        this.stateUse = stateUse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<FilterData> getList() {
        return list;
    }

    public void setList(List<FilterData> list) {
        this.list = list;
    }

    public FilterFragment getFilterFragment() {
        return filterFragment;
    }

    public void setFilterFragment(FilterFragment filterFragment) {
        this.filterFragment = filterFragment;
    }
}
