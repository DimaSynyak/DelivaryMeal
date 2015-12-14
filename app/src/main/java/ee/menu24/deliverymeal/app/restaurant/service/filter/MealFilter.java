package ee.menu24.deliverymeal.app.restaurant.service.filter;

import ee.menu24.deliverymeal.app.main.service.Restaurant;
import ee.menu24.deliverymeal.app.main.service.RestaurantList;
import ee.menu24.deliverymeal.app.main.title.Language;

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
public class MealFilter implements IFilter {

    private static FilterData searchData;
    private Language language;
    private Map<String, String> map;
    private List<FilterData> filterDataList;
    private RestaurantList restaurantList;

    private static MealFilter mealFilter;
    private Restaurant restaurant;

    private boolean stateMealFilter;

    public MealFilter() {
        this.language = Language.getInstance();
        filterDataList = new ArrayList<>();
        searchData = new FilterData();
        searchData.setText("");
        map = new HashMap<>();
        restaurantList = RestaurantList.getInstance();
        restaurant = restaurantList.getRestaurant();
    }

    public static MealFilter getInstance(){
        if (mealFilter == null){
            mealFilter = new MealFilter();
        }
        return mealFilter;
    }

    @Override
    public void init(Document document) {

        filterDataList.clear();

        Elements categories = document.select("ul.bl-x2 > li");

        if(categories == null)
            return;

        for (Element category : categories) {

            FilterData filterData = new FilterData();

            if (category.select("ul").size() != 0) {
                filterData.setName(category.select("input[class = food-category]").attr("name"));
                filterData.setId(category.select("input[class = food-category]").attr("id"));
                filterData.setText(category.getElementsByAttributeValue("for", filterData.getId()).text());

                List<FilterData> list = new ArrayList<>();
                for (Element ct : category.select("ul > li")) {
                    FilterData fd = new FilterData();

                    fd.setName(ct.select("input[class = food-category]").attr("name"));
                    fd.setId(ct.select("input[class = food-category]").attr("id"));
                    fd.setText(ct.getElementsByAttributeValue("for", fd.getId()).text());

                    list.add(fd);
                }
                filterData.setList(list);
                filterDataList.add(filterData);
            } else if (category.select("input[class = food-category]").size() != 0) {
                FilterData fd = new FilterData();

                fd.setName(category.select("input[class = food-category]").attr("name"));
                fd.setId(category.select("input[class = food-category]").attr("id"));
                fd.setText(category.getElementsByAttributeValue("for", fd.getId()).text());
                filterDataList.add(fd);
            } else {
                FilterData fd = new FilterData();

                fd.setId(category.select("input[type = checkbox]").attr("id"));
                fd.setText(category.getElementsByAttributeValue("for", fd.getId()).text());
                filterDataList.add(fd);
            }

        }
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
                connection.url("http://menu24.ee/wp-admin/admin-ajax.php?lang=ee");
                break;
            }
            case EN:{
                connection.url("http://menu24.ee/wp-admin/admin-ajax.php?lang=en");
                break;
            }
        }

        map.put("action", "get_food");
        map.put("search", searchData.getText());
        map.put("restaurant", restaurant.getNameForFilter().toLowerCase());
        map.put("page", "1");

        StringBuilder tmp = new StringBuilder();
        boolean flag = false;
        for (FilterData filterData : filterDataList) {

            if (filterData.getList() != null && filterData.getList().size() != 0){   // filter sliding list
                for (FilterData fd :filterData.getList()) {
                    if (fd.isStateUse() == true){
                        if (flag){
                            tmp.append(",");
                        }
                        tmp.append(fd.getName());
                        flag = true;
                    }

                }
            }

            if (filterData.isStateUse() == true && filterData.getName() != null && !filterData.getName().equals("")) { //filter which have not name and food categories
                if (flag){
                    tmp.append(",");
                }

                tmp.append(filterData.getName());
                flag = true;
            }
            else if (filterData.isStateUse() == true) {
                map.put(filterData.getId(), "true");
            }

            map.put("foodCategories", tmp.toString());
        }


        connection.data(map);
        connection.method(Connection.Method.POST);
    }

    @Override
    public void filter(Connection connection, int numPage) {
        connection.request().data().clear();

        map.clear();

        switch(language.getLanguages()) {
            case RU:{
                connection.url("http://menu24.ee/wp-admin/admin-ajax.php?lang=ru");
                break;
            }
            case EE:{
                connection.url("http://menu24.ee/wp-admin/admin-ajax.php?lang=ee");
                break;
            }
            case EN:{
                connection.url("http://menu24.ee/wp-admin/admin-ajax.php?lang=en");
                break;
            }
        }

        map.put("action", "get_food");
        map.put("search", searchData.getText());
        map.put("restaurant", restaurant.getName().toLowerCase());
        map.put("page", String.valueOf(numPage));

        for (FilterData filterData : filterDataList) {

            if (filterData.getList() != null && filterData.getList().size() != 0){
                for (FilterData fd :filterData.getList()) {
                    if (fd.isStateUse() == true)
                        map.put("foodCategories", fd.getName());
                }
            }

            if (filterData.isStateUse() == true && filterData.getName() != null && !filterData.getName().equals(""))
                map.put("foodCategories", filterData.getName());
            else if (filterData.isStateUse() == true)
                map.put(filterData.getId(), "true");
        }


        connection.data(map);
        connection.method(Connection.Method.POST);
    }


    public static FilterData getSearchData() {
        return searchData;
    }

    public static void setSearchData(FilterData searchData) {
        MealFilter.searchData = searchData;
    }

    public List<FilterData> getFilterDataList() {
        return filterDataList;
    }

    public void setFilterDataList(List<FilterData> filterDataList) {
        this.filterDataList = filterDataList;
    }


    @Override
    public void init(Connection connection) {

    }

    @Override
    public void init(Connection.Response response) {

    }

    public void destroy(){
        if (map != null) {
            map.clear();
            map = null;
        }

        if (filterDataList != null) {
            filterDataList.clear();
            filterDataList = null;
        }

        mealFilter = null;
        searchData = null;
    }


    public static MealFilter getMealFilter() {
        return mealFilter;
    }

    public static void setMealFilter(MealFilter mealFilter) {
        MealFilter.mealFilter = mealFilter;
    }

    public boolean isStateMealFilter() {
        return stateMealFilter;
    }

    public void setStateMealFilter(boolean stateMealFilter) {
        this.stateMealFilter = stateMealFilter;
    }
}
