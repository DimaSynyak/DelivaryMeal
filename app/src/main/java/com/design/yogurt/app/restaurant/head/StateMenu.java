package com.design.yogurt.app.restaurant.head;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmitriy on 12/1/15.
 */
public class StateMenu {
    private EStateMenu state;
    private int stringID;
    private String name;

    public final static List<StateMenu> stateMenus = new ArrayList<>();

    public StateMenu(EStateMenu state, int stringID, String name) {
        this.state = state;
        this.stringID = stringID;
        this.name = name;
        stateMenus.add(this);
    }

    public static StateMenu getInstanceByStringName(String name){
        for (StateMenu stateMenu : stateMenus){
            if (stateMenu.getName().equals(name)){
                return stateMenu;
            }
        }
        return null;
    }

    public EStateMenu getState() {
        return state;
    }

    public void setState(EStateMenu state) {
        this.state = state;
    }

    public int getStringID() {
        return stringID;
    }

    public void setStringID(int stringID) {
        this.stringID = stringID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void onDestroy(){
        stateMenus.clear();
    }

}
