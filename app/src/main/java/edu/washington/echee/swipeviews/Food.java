package edu.washington.echee.swipeviews;

import java.util.ArrayList;

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
}
