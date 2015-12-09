package com.design.yogurt.app.restaurant.service;

/**
 * Created by 1 on 23.11.2015.
 */
public class RegistrationData {

    private boolean personalCabinetType;
    private IChangePersonalCabinetTypeListener iChangePersonalCabinetTypeListener;

    private IChangeNumFlatListener iChangeNumFlatListener;

    private String name = "";
    private String city = "";
    private String numStreet = "";
    private String numHouse = "";
    private String numFlat = "";
    private String email;
    private String numPhone = "";
    private String country = "";
    private String index = "";
    private String password = "";
    private String confirmPassword = "";

    private boolean stateLogin;
    private String logoutLink;
    private IChangeStateLogoutListener iChangeStateLogoutListener;


    private ChangeDateListener changeDateListener;

    private static RegistrationData delivaryDataObj;

    public static RegistrationData getInstance(){
        if (delivaryDataObj == null){
            delivaryDataObj = new RegistrationData();
        }

        return delivaryDataObj;
    }

    public boolean isPersonalCabinetType() {
        return personalCabinetType;
    }

    public void setPersonalCabinetType(boolean personalCabinetType) {
        this.personalCabinetType = personalCabinetType;

        if (iChangePersonalCabinetTypeListener == null)
            return;

        iChangePersonalCabinetTypeListener.change();
    }

    public void onDestroy(){
        delivaryDataObj = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNumStreet() {
        return numStreet;
    }

    public void setNumStreet(String numStreet) {
        this.numStreet = numStreet;
    }

    public String getNumHouse() {
        return numHouse;
    }

    public void setNumHouse(String numHouse) {
        this.numHouse = numHouse;
    }

    public String getNumFlat() {
        return numFlat;
    }

    public void setNumFlat(String numFlat) {
        this.numFlat = numFlat;

        if (iChangeNumFlatListener == null)
            return;

        iChangeNumFlatListener.change();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumPhone() {
        return numPhone;
    }

    public void setNumPhone(String numPhone) {
        this.numPhone = numPhone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public ChangeDateListener getChangeDateListener() {
        return changeDateListener;
    }

    public void setChangeDateListener(ChangeDateListener changeDateListener) {
        this.changeDateListener = changeDateListener;
    }

    public static RegistrationData getDelivaryDataObj() {
        return delivaryDataObj;
    }

    public static void setDelivaryDataObj(RegistrationData delivaryDataObj) {
        RegistrationData.delivaryDataObj = delivaryDataObj;
    }

    public boolean isStateLogin() {
        return stateLogin;
    }

    public void setStateLogin(boolean stateLogin) {
        this.stateLogin = stateLogin;

        if (iChangeStateLogoutListener == null)
            return;

        iChangeStateLogoutListener.change();
    }

    public String getLogoutLink() {
        return logoutLink;
    }

    public void setLogoutLink(String logoutLink) {
        this.logoutLink = logoutLink;
    }

    public void setChangeStateLogoutListener(IChangeStateLogoutListener iChangeStateLogoutListener){
        this.iChangeStateLogoutListener = iChangeStateLogoutListener;
    }

    public void setiChangePersonalCabinetTypeListener(IChangePersonalCabinetTypeListener iChangePersonalCabinetTypeListener) {
        this.iChangePersonalCabinetTypeListener = iChangePersonalCabinetTypeListener;
    }

    public void setiChangeNumFlatListener(IChangeNumFlatListener iChangeNumFlatListener) {
        this.iChangeNumFlatListener = iChangeNumFlatListener;
    }
}
