package edu.washington.echee.swipeviews;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by eric on 3/11/15.
 */
public class Food {
    public String title;
    public String desc;
    public String imgName;
    public ArrayList<String> restaurants;

    public Food() {
        this.title = "Steak";
        this.desc = "This is a description";
        this.imgName = "placeholder_food";
        this.restaurants = new ArrayList<String>();
    }

    public Food(String title, String desc, String imgName, ArrayList<String> restaurants) {
        this.title = title;
        this.desc = desc;
        this.imgName = imgName;
        this.restaurants = restaurants;
    }

    //TODO implement actual price level in data.JSON instead of random num
    public String getPriceLevelString(){
        String result ="";

        // random price level
        int max = 4;
        int min = 1;
        Random random = new Random();
        int priceLevel = random.nextInt(max - min + 1) + min;

        for (int i = 0; i < priceLevel; i++) {
            result += "$";
        }

        return result;
    }
}
