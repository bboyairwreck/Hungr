package edu.washington.echee.swipeviews;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
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

        DownloadService.startOrStopAlarm(this, true);
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

    /**
     * Add all restaurants that have this food to your liked list
     * @param food a Food object that contains a list of restaurants to add to the liked
     */
    public void addRestaurantsToLiked(Food food) {

        for (int i = 0; i < food.restaurants.size(); i++)
        {
            String restaurantKey = food.restaurants.get(i);
            Restaurant curRestaurant = this.restaurants.get(restaurantKey);

            if (curRestaurant == null) {
                Log.i("HungrApp", "Couldn't add to restaurant. .get(" + restaurantKey+ ") is null. might want to check in JSON");
            } else {
                Log.i("HungrApp", "Adding " + curRestaurant.title + " to the Liked set");

                this.likedRestaurants.add(curRestaurant);
            }


        }
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

    public static boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    /**
     * Gets the state of Airplane Mode.
     *
     * @param context
     * @return true if enabled.
     */
    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isAirplaneModeOn(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }
}
