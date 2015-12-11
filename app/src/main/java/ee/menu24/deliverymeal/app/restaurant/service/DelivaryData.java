package ee.menu24.deliverymeal.app.restaurant.service;

/**
 * Created by 1 on 23.11.2015.
 */
public class DelivaryData {

    private boolean delivaryType;
    private String yourName;
    private String delivaryCity;
    private String numStreet;
    private String numHouse;
    private String numFlat;
    private String email;
    private String numPhone;
    private String delivaryData;
    private String nameBank;

    private ChangeDateListener changeDateListener;

    private static DelivaryData delivaryDataObj;

    public static DelivaryData getInstance(){
        if (delivaryDataObj == null){
            delivaryDataObj = new DelivaryData();
        }

        return delivaryDataObj;
    }

    public DelivaryData() {
        this.delivaryType = true;
    }

    public boolean checkData(){
        if (delivaryType){
            if (yourName == null
                    || delivaryCity == null
                    || numStreet == null
                    || numHouse == null
                    || numFlat == null
                    || email == null
                    || numPhone == null
                    || delivaryData == null){
                return false;
            }
            return true;
        }
        else {
            if (yourName == null
                    || delivaryCity == null
                    || email == null
                    || numPhone == null
                    || delivaryData == null){
                return false;
            }
            return true;
        }
    }

    public String getDelivaryType() {
        if (delivaryType)
            return "с доставкой";
        else
            return "самовывоз";
    }

    public void onDestroy(){
        delivaryDataObj = null;
    }


    public boolean isDelivaryType(){
        return delivaryType;
    }

    public void setDelivaryType(boolean delivaryType) {
        this.delivaryType = delivaryType;
    }

    public String getYourName() {
        return yourName;
    }

    public void setYourName(String yourName) {
        this.yourName = yourName;
    }

    public String getDelivaryCity() {
        return delivaryCity;
    }

    public void setDelivaryCity(String delivaryCity) {
        this.delivaryCity = delivaryCity;
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

    public String getDelivaryData() {
        return delivaryData;
    }

    public void setDelivaryData(String delivaryData) {
        this.delivaryData = delivaryData;
        if (changeDateListener == null)
            return;

        changeDateListener.onChange();
    }

    public String getNameBank() {
        return nameBank;
    }

    public void setNameBank(String nameBank) {
        this.nameBank = nameBank;
    }

    public void setChangeDateListener(ChangeDateListener changeDateListener){
        this.changeDateListener = changeDateListener;
    }
}
