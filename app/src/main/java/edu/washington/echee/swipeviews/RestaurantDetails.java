package edu.washington.echee.swipeviews;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        super.createNavDrawer(this);

        HungrApp hungrApp = new HungrApp().getInstance();
        Intent intent = getIntent();
        Restaurant restaurant = hungrApp.getRestaurants().get(intent.getStringExtra("restaurant"));

        imageView = (ImageView)findViewById(R.id.restaurantImage);
        TextView title = (TextView)findViewById(R.id.restaurant_title);
        TextView price = (TextView)findViewById(R.id.priceRange);
        RatingBar ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        TextView descr = (TextView)findViewById(R.id.description);

        String restImgDrawableName = restaurant.imgName;
        int restaurantImageID = getResources().getIdentifier(restImgDrawableName , "drawable", getPackageName());

        if (restaurantImageID <= 0) {
            restaurantImageID = getResources().getIdentifier("stock_restaurant" , "drawable", getPackageName());
        }

        imageView.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), restaurantImageID, 100, 100));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

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

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.gc();
        imageView.setImageBitmap(null);
    }
}
