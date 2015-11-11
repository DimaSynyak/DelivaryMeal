package com.dmitriy.sinyak.delivarymeal.app.activity.tools;

import com.dmitriy.sinyak.delivarymeal.app.activity.main.title.Languages;

/**
 * Created by 1 on 11.11.2015.
 */
public class Tools {

    private static Tools tools;

    public static Tools getInstance(){
        if (tools == null){
            tools = new Tools();
        }
        return tools;
    }


    public String getStrUrl(String url,Languages languages){ /*change url for languages*/
        StringBuilder temp = new StringBuilder();
        int start = 0;
        int end = 0;

        url = url.toLowerCase();
        start = url.indexOf("menu24.ee/");
        start += 10;
        end = url.indexOf("/restaurant/");


        switch (languages){
            case RU:{
                temp.append(url.substring(0, start));
                temp.append("ru");
                temp.append(url.substring(end));
                break;
            }
            case EN:{
                temp.append(url.substring(0, start));
                temp.append("en");
                temp.append(url.substring(end));
            }
            case EE:{
                temp.append(url.substring(0, start - 1));
                temp.append(url.substring(end));
            }
        }
        return String.valueOf(temp);
    }
}
