package edu.washington.echee.swipeviews;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class RestaurantList extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        ListView restaurantListView = (ListView) findViewById(R.id.restaurantList);
        HungrApp hungrApp = (HungrApp)getApplicationContext();
        Set<Restaurant> restaurants = hungrApp.getLikedRestaurants();
        final List<String> restaurantNames = new ArrayList<String>();

        for(Restaurant restaurant : restaurants){
            if(restaurant == null){
                Log.e("RestaurantList", "RESTAURANT IS NULL");
            } else if (restaurant != null){
                Log.e("RestaurantList", restaurant.title);
            }
            restaurantNames.add(restaurant.title);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, restaurantNames);
        restaurantListView.setAdapter(arrayAdapter);

        restaurantListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RestaurantList.this, RestaurantDetails.class);
                intent.putExtra("restaurant", restaurantNames.get(position));
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_restaurant_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
