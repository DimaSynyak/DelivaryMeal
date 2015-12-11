package ee.menu24.deliverymeal.app.main.menu;

import java.util.List;

/**
 * Created by 1 on 30.10.2015.
 */
public class Country {
    private String country;
    private List<String> cities;

    public Country(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }
}
