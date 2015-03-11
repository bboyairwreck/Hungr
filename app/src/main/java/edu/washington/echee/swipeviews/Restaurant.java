package edu.washington.echee.swipeviews;

/**
 * Created by eric on 3/11/15.
 */
public class Restaurant {
    public String title;
    public String desc;
    public String imgName;
    public int priceLevel;
    public int rating;

    public Restaurant(String title, String desc, String imgName, int priceLevel, int rating) {
        this.title = title;
        this.desc = desc;
        this.imgName = imgName;
        this.priceLevel = priceLevel;
        this.rating = rating;
    }

    public Restaurant() {
        this.title = "Restaurant Name";
        this.desc = "This is a description of some restaurant";
        this.imgName = "placeholder_restaurant";
        this.priceLevel = 1;
        this.rating = 4;
    }
}
