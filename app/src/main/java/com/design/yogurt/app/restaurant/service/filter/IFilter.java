package com.design.yogurt.app.restaurant.service.filter;


import org.jsoup.Connection;
import org.jsoup.nodes.Document;

/**
 * Created by dmitriy on 12/3/15.
 */
public interface IFilter {
    void filter(Connection connection);
    void filter(Connection connection, int numPage);

    void init(Connection connection);
    void init(Connection.Response response);
    void init(Document document);
}
