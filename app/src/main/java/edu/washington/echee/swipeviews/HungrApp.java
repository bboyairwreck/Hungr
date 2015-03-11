package edu.washington.echee.swipeviews;

import android.app.Application;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Created by eric on 3/10/15.
 */
public class HungrApp extends Application {
    private static HungrApp instance; // singleton
    private ArrayList<Food> foods;
    private HashMap<String, Restaurant> restaurants;
    public Set<Restaurant> likedRestaurants;


    public HungrApp() {
        if (instance == null) {
            instance = this;
        } else {
            Log.e("HungrApp", "There is an error. You tried to create more than 1 QuizApp");
        }
    }

    public HungrApp getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("HungrApp", "The Hungr app is loaded and running");

        this.likedRestaurants = new HashSet<Restaurant>();
        this.foods = new ArrayList<Food>();
        this.restaurants = new HashMap<String, Restaurant>();

        File myFile = new File(getFilesDir().getAbsolutePath(), "/data.json");
        String json = null;

        try {

            // check if data.json file exists in the files directory
            if (myFile.exists()) {
                Log.i("HungrApp", "data.json DOES exist");

                FileInputStream fis = openFileInput("data.json");
                json = readJSONFile(fis);
            } else {
                Log.i("HungrApp", "data.json does NOT exist. Loading file from assets");

                // Can't find data.json file. Fetch data.json in assets
                InputStream inputStream = getAssets().open("data.json");
                json = readJSONFile(inputStream);
            }

            JSONObject jsonData = new JSONObject(json);

            // Add the all the foods to respository
            JSONArray jsonFoods = jsonData.getJSONArray("foods");
            for (int i = 0; i <jsonFoods.length(); i++) {
                JSONObject jsonFood = jsonFoods.getJSONObject(i);
                addFood(jsonFood);
            }

            JSONArray jsonRestaurants = jsonData.getJSONArray("restaurants");
            for (int i = 0; i < jsonRestaurants.length(); i++) {
                JSONObject jsonRestaurant = jsonRestaurants.getJSONObject(i);
                addRestaurant(jsonRestaurant);
            }

            Log.i("HungrApp JSON", json);   // TODO delete this. Prints out the raw JSON used


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /**
     * Converts a JSONObject of a food, converts it to a Food object and adds it to the
     * list of foods repository
     *
     * @param jsonFood  a JSONObject of the food to be added
     * @throws JSONException
     */
    private void addFood(JSONObject jsonFood) throws JSONException {
        String title = jsonFood.getString("title");
        String desc = jsonFood.getString("desc");
        String imgName = jsonFood.getString("img");

        // Get list of restaurants related to this food from the JSON
        ArrayList<String> restaurantsOfFood = new ArrayList<String>();
        JSONArray jsonRestaurants = jsonFood.getJSONArray("restaurants");

        for (int i = 0; i < jsonRestaurants.length(); i++) {
            String restaurantKey = jsonRestaurants.getString(i);
            restaurantsOfFood.add(restaurantKey);
        }

        // Create food obj and add to repository
        Food food = new Food(title, desc, imgName, restaurantsOfFood);
        this.foods.add(food);
    }


    private void addRestaurant(JSONObject jsonRestaurant) throws JSONException{
        String title = jsonRestaurant.getString("title");
        String desc = jsonRestaurant.getString("desc");
        String imgName = jsonRestaurant.getString("img");
        int price = jsonRestaurant.getInt("price");
        int rating = jsonRestaurant.getInt("rating");

        Restaurant restaurant = new Restaurant(title, desc, imgName, price, rating);
        this.restaurants.put(title, restaurant);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    // reads InputStream of JSON file and returns the file in JSON String format
    public String readJSONFile(InputStream inputStream) throws IOException {

        int size = inputStream.available();
        byte[] buffer = new byte[size];
        inputStream.read(buffer);
        inputStream.close();

        return new String(buffer, "UTF-8");
    }

    public ArrayList<Food> getFoods(){
        return this.foods;
    }

    public HashMap<String, Restaurant> getRestaurants() {
        return this.restaurants;
    }

    public Set<Restaurant> getLikedRestaurants() {
        return this.likedRestaurants;
    }

    /**
     * Add all restaurants that have this food to your liked list
     * @param food a Food object that contains a list of restaurants to add to the liked
     */
    public void addRestaurantsToLiked(Food food) {

        for (int i = 0; i < food.restaurants.size(); i++) {
            Restaurant curRestaurant = this.restaurants.get(food.restaurants.get(i));
            this.likedRestaurants.add(curRestaurant);
        }
    }

    // reads InputStream of JSON file and returns the file in JSON String format
    public String readJSONFile(FileInputStream fileInputStream) throws IOException {
        int size = fileInputStream.available();
        byte[] buffer = new byte[size];
        fileInputStream.read(buffer);
        fileInputStream.close();

        return new String(buffer, "UTF-8");
    }

    // Writes String to a file called quizData.json
    public void writeToFile(String data) {
        try {
            Log.i("HungrApp", "writing downloaded to file");

            // Create a file with path, a name
            File file = new File(getFilesDir().getAbsolutePath(), "data.json");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
