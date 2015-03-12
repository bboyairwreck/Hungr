package edu.washington.echee.swipeviews;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.InputStream;


public class RestaurantDetails extends HungrBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        super.createNavDrawer(this);

        ImageView imageView = (ImageView)findViewById(R.id.restaurantImage);

        try{
            InputStream inputStream = getAssets().open("stock_restaurant.jpg");
            Drawable drawableImage = Drawable.createFromStream(inputStream, null);
            imageView.setImageDrawable(drawableImage);
        } catch(Exception e){
            Log.e("RestaurantDetails","Couldn't load restaurant image -- probably IO exception.");
        }

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
