package com.design.yogurt.app.tools;

import com.design.yogurt.app.main.title.Languages;

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


    public static Float getNum(String str){
        StringBuilder tmp = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) >= '0' && str.charAt(i) <= '9' || str.charAt(i) == '.'){
                tmp.append(str.charAt(i));
            }
        }


        return Float.parseFloat(tmp.toString());
    }

    public static int getNumInt(String str){
        StringBuilder tmp = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) >= '0' && str.charAt(i) <= '9'){
                tmp.append(str.charAt(i));
            }
        }


        return Integer.parseInt(tmp.toString());
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

    public static int getVariationId(String str){
        StringBuilder builder = new StringBuilder();
        int i = 0;
        int j = 0;
        if(str.contains("variation_id")){
            i = str.indexOf("variation_id");

            boolean flag = false;
            while (true){
                if (str.charAt(i) >= '0' && str.charAt(i) <= '9'){
                    if (!flag){
                        j = i;
                        flag = true;
                    }

                    if (i > j){
                        break;
                    }
                    builder.append(str.charAt(i));
                    j++;
                }
                i++;
            }
        }
        return Integer.parseInt(builder.toString());
    }
}
