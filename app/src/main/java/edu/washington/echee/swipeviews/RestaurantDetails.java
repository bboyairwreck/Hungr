package edu.washington.echee.swipeviews;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStream;


public class RestaurantDetails extends HungrBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        super.createNavDrawer(this);

        ImageView imageView = (ImageView)findViewById(R.id.restaurantImage);
        TextView title = (TextView)findViewById(R.id.restaurant_title);
        TextView price = (TextView)findViewById(R.id.priceRange);
        RatingBar ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        TextView descr = (TextView)findViewById(R.id.description);

        try{
            InputStream inputStream = getAssets().open("stock_restaurant.jpg");
            Drawable drawableImage = Drawable.createFromStream(inputStream, null);
            imageView.setImageDrawable(drawableImage);
        } catch(Exception e){
            Log.e("RestaurantDetails","Couldn't load restaurant image -- probably IO exception.");
        }

        HungrApp hungrApp = new HungrApp().getInstance();
        Intent intent = getIntent();
        Restaurant restaurant = hungrApp.getRestaurants().get(intent.getStringExtra("restaurant"));

        title.setText(restaurant.title);
        String priceBuilder = "";
        for(int i = 0; i < restaurant.priceLevel; i++){
            priceBuilder += "$";
        }
        price.setText(priceBuilder);
        ratingBar.setNumStars(5);
        ratingBar.setRating((float)restaurant.rating);
        descr.setText(restaurant.desc);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_restaurant_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (super.onNavDrawerButtonPress(item)) { //Required to activate drawer
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
