/**
 * 3/3/15
 * @author Eric Chee
 */
package edu.washington.echee.swipeviews;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class SwipeActivity extends HungrBaseActivity implements View.OnTouchListener {
    ViewGroup _root;                // root view
    HungrApp hungrApp;
    private int _xDelta;            // distance card dragged in X-axis
    private int screenWidth = 0;    // width of rootView
    private int screenHeight = 0;
    private float pivotX;           // rotation pivot X position
    private float pivotY;           // rotation pivot Y position
    private int numCards;
    private ArrayList<Food> foods;
    private int curFoodIndex;
    public static final float MAX_ROTATION = 17f;   // max degrees to rotate card
    Queue<Integer> foodIDOrder;
    ImageView imageView;
    Bitmap bitmap;
    ImageButton btnNope;
    ImageButton btnYeah;
    ImageButton btnInfo;
    ImageButton btnNoMoreFood;

    public static final int MAX_CARDS = 5;         // max number of cards on screen at a time
    public static final int MIN_CARDS = 2;         // min number of cards on screen at a time
    public static final float GO_LEFT = -1000f;   // max degrees to rotate card
    public static final float GO_RIGHT = 1000f;   // max degrees to rotate card
    public static final int cardMargin = 67;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.hungrApp = (HungrApp)getApplicationContext();

        // Get Root container
        _root = (ViewGroup) findViewById(R.id.root);

        this.foods = hungrApp.getFoods();
        this.curFoodIndex = 0;
        this.foodIDOrder = new LinkedList<Integer>();


        int buttonSize = 100;
        btnNope = (ImageButton) findViewById(R.id.btnNope);
        btnYeah = (ImageButton) findViewById(R.id.btnYeah);
        //btnInfo = (ImageButton) findViewById(R.id.btnInfo);

        btnNope.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.nope, buttonSize, buttonSize));
        btnYeah.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.yeah, buttonSize, buttonSize));
        //btnInfo.setImageBitmap(
        //        decodeSampledBitmapFromResource(getResources(), R.drawable.info, buttonSize, buttonSize));


        this.numCards = MAX_CARDS;

//        String[] stringArr = getResources().getStringArray(R.array.nav_drawer_list);
//        ArrayList<String> navDrawerList = new ArrayList<String>(Arrays.asList(stringArr)); //Create nav_drawer elements list
//        ListView lvNavDrawer = (ListView) findViewById(R.id.lvNavDrawer);
//        lvNavDrawer.setAdapter(new NavDrawerListAdapter(this, R.layout.nav_drawer_list_layout, navDrawerList));
//        lvNavDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
        createNavDrawer(this);

        ImageButton btnResults = (ImageButton) findViewById(R.id.ibResults);
        btnResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRestaurantList();
            }
        });

    }

    /*
     * Since cannot get root width onCreate because view hasn't loaded by then, get width here.
     * Add max num or cards to root view
     */
    @Override
    public void onWindowFocusChanged(boolean b) {
        this.screenHeight = ((ViewGroup) findViewById(R.id.fullscreen)).getHeight();
        this.screenWidth = _root.getWidth();
        addNoMoreFoodButton();
        addCards(MAX_CARDS);

        btnNope.setOnClickListener(decisionButtonListener(GO_LEFT));
        btnYeah.setOnClickListener(decisionButtonListener(GO_RIGHT));
    }

    /*
     * Adds @size number of cards to root view. Each card will be given an onClickListener
     * Requirement:
     *      Must be done AFTER root view is loaded i.e. onWindowFocus() but NOT in onCreate()
     */
    public void addCards(int size) {
        int cardWidth = this.screenWidth - (cardMargin * 2);    // width of each card
        int leftMargin = cardMargin;

        int bottomButtonsHeight = (int) getResources().getDimension(R.dimen.yeahNope_plus_results);
        int maxWidth = screenHeight - (bottomButtonsHeight + cardMargin);
        if (cardWidth > maxWidth) {
            cardWidth = maxWidth;
            leftMargin = (screenWidth - cardWidth) / 2;
        }


        // Create cards in add to root view
        for (int i = 0; i < size; i++) {
            if (curFoodIndex < foods.size()) {
                // Get a Food from the repository and add it to the queue
                Food food = foods.get(curFoodIndex);
                this.foodIDOrder.add(curFoodIndex);

                LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                RelativeLayout card = (RelativeLayout) vi.inflate(R.layout.card_details, null);

                imageView = (ImageView) card.findViewById(R.id.ivFoodImage);
                imageView.setImageBitmap(
                        decodeSampledBitmapFromResource(getResources(), R.drawable.food1, 100, 100));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);


//            imageView.setImageBitmap(bitmap); // Do whatever you need to do to load the image you want.
                imageView.setLayoutParams(new RelativeLayout.LayoutParams(cardWidth, cardWidth));


                TextView foodTitle = (TextView) card.findViewById(R.id.tvFoodTitle);
                foodTitle.setText(food.title);
                int titleHeight = foodTitle.getHeight();

                TextView tvPriceLevel = (TextView) card.findViewById(R.id.tvPriceLevel);
                tvPriceLevel.setText(food.getPriceLevelString());


                RelativeLayout rlPriceAndRatings = (RelativeLayout) card.findViewById(R.id.rlPriceAndRatings);
                int prHeight = rlPriceAndRatings.getHeight();

                int starSize = ((int) getResources().getDimension(R.dimen.starSize));
                int starPadding = ((int) getResources().getDimension(R.dimen.starPadding)) * 2;

                int height = cardWidth + starSize + starPadding;

                Log.i("SwipeActivity", "tileHeight + prHeight + height = " + titleHeight + " + " + prHeight + " + " + height);


//            LinearLayout card = new LinearLayout(this);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(cardWidth, height); // width & height
                layoutParams.leftMargin = leftMargin;
                layoutParams.topMargin = cardMargin;
                card.setLayoutParams(layoutParams);

                // TODO Delete. Each card alternates between Green & RED
                card.setBackgroundColor(Color.RED);
                if (i % 2 == 0) {
                    card.setBackgroundColor(Color.GREEN);
                }
                // TODO DELETE ^

                this.pivotX = cardWidth / 2;    // set x pivot point center
                this.pivotY = height * 2f;      // set y pivot point 2x below the height

                // set pivot location to bottom center
                card.setPivotX(pivotX);
                card.setPivotY(pivotY);

                // Add card to root view & set onTouchListener
                _root.addView(card, 1, layoutParams);       // NOTE: added it to index 1 cuz no_more_food is 0th child
                card.setOnTouchListener(this);

                curFoodIndex++;
            } else {
                break;
            }
        }
    }

    private void addNoMoreFoodButton(){
        int cardWidth = this.screenWidth - (cardMargin * 2);    // width of each card
        int leftMargin = cardMargin;

        int bottomButtonsHeight = (int) getResources().getDimension(R.dimen.yeahNope_plus_results);
        int maxWidth = screenHeight - (bottomButtonsHeight + cardMargin);
        if (cardWidth > maxWidth) {
            cardWidth = maxWidth;
            leftMargin = (screenWidth - cardWidth) / 2;
        }

        btnNoMoreFood = new ImageButton(this);
        btnNoMoreFood.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.no_more_food, 100, 100));
        btnNoMoreFood.setScaleType(ImageView.ScaleType.CENTER_CROP);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(cardWidth, cardWidth); // width & height
        layoutParams.leftMargin = leftMargin;
        layoutParams.topMargin = cardMargin;
        btnNoMoreFood.setLayoutParams(layoutParams);
        _root.addView(btnNoMoreFood, 0, layoutParams);

        btnNoMoreFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRestaurantList();
            }
        });
    }

    public void showRestaurantList(){
        Log.i("SwipeActivity", "loading RestaurantList activity");
//        Intent restaurantListIntent = new Intent(this, RestaurantList.class);
//        startActivity(restaurantListIntent);
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

    /*
     * If detecting card swipe, this method will determine if current card should be removed or reset
     */
    @Override
    public boolean onTouch(final View v, MotionEvent event) {
        final int X = (int) event.getRawX();    // X position of touch event
        int dif;                                // temp xDelta
        float swipePercent;                     // % of distance from center X to edge of screen

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            // On Press Down
            case MotionEvent.ACTION_DOWN:
                _xDelta = X - (int)v.getTranslationX();    // track how far moved x
                break;
            // On Drag
            case MotionEvent.ACTION_MOVE:
                dif = X - _xDelta;
                swipePercent = (float)dif / (float)screenWidth;

                float rotation = swipePercent * MAX_ROTATION;
                float alpha = (1f - Math.abs(swipePercent));

                // set rotation, alpha, & translation
                v.setAlpha(alpha);
                v.setRotation(rotation);
                v.setTranslationX(dif);

                Log.i("OnTouch", "swipePercent = " + swipePercent + "; rotation = " + rotation + "; alpha = " + alpha);
                break;
            // On Release
            case MotionEvent.ACTION_UP:
                dif = X - _xDelta;
                swipePercent = Math.abs((float) dif / (float) screenWidth);

                // Reset card position if did NOT reach swipe threshold
                if (swipePercent < 0.3f) {
                    v.setAlpha(1);
                    v.setRotation(0f);
                    v.setTranslationX(0);

                // Animate card off screen if did reach swipe threshold
                } else {
                    animateCardOff(v , v.getRotation(), 600);
                }

                Log.i("OnTouch", "Touch Released");

                break;
        }
        _root.invalidate();
        return true;
    }

    private void animateCardOff(View v, float rotation, int animDuration){
        int foodID = foodIDOrder.remove();

        float sign = 1;
        if (rotation < 0) {
            sign = -1;
        } else {
            // If Liked food, add food's restaurants to Liked list
            Food food = foods.get(foodID);
            hungrApp.addRestaurantsToLiked(food);
        }
        RotateAnimation rotateAnim = new RotateAnimation(0, MAX_ROTATION*sign, pivotX, pivotY);
        rotateAnim.setDuration(animDuration);
        rotateAnim.setRepeatCount(0);

        TranslateAnimation translateAnim = new TranslateAnimation(0, (this.screenWidth)*sign, 0, 0);
        translateAnim.setDuration(animDuration);
        translateAnim.setRepeatCount(0);

        AnimationSet animSet = new AnimationSet(true);
        animSet.addAnimation(rotateAnim);
        animSet.addAnimation(translateAnim);
        animSet.setFillAfter(true);
        animSet.setAnimationListener(new MyAnimationListener(v));

        // Animate card off screen and remove it
        v.startAnimation(animSet);
    }

    /*
     * If end of animation, removes card view from the root view.
     * If number of cards in root < MIN_CARDS, then it will add more cards to the root view
     */
    public class MyAnimationListener implements Animation.AnimationListener {
        View v;

        public MyAnimationListener(View v) {
            this.v = v;
        }

        /*
         * Fired when Animation finishes
         */
        @Override
        public void onAnimationEnd(Animation animation) {
            Log.i("onAnimationEnd", "Animation ended. Removing card");
            v.setOnTouchListener(null);     // unregister onTouchListener

            // Can only remove a view in Runnable UI Thread in AnimationListener
            _root.post(new Runnable() {
                public void run() {
                    // it works without the runOnUiThread, but all UI updates must be done on the UI thread
                    SwipeActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            _root.removeView(v);        // removes card from root view
                            Log.i("onAnimationEnd", "Card removed");

                            btnNope.setEnabled(true);
                            btnYeah.setEnabled(true);

                            SwipeActivity.this.numCards--; // decrement number of cards

                            // If down to MIN_CARDS, add more cards
                            if (numCards <= MIN_CARDS) {
                                int numOfAddedCards = MAX_CARDS - numCards;
                                SwipeActivity.this.addCards(numOfAddedCards);
                                SwipeActivity.this.numCards += numOfAddedCards;

                                Log.i("onAnimationEnd", "Added " + numOfAddedCards + " cards to root view");
                            }
                        }
                    });
                }
            });
        }

        @Override
        public void onAnimationStart(Animation animation) {
            btnNope.setEnabled(false);
            btnYeah.setEnabled(false);
        }
        @Override
        public void onAnimationRepeat(Animation animation) {}
    }

    private View.OnClickListener decisionButtonListener(final float direction){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View card = null;
                for (int i = _root.getChildCount()-1; i >= 0; i--) {
                    View curView = _root.getChildAt(i);
//                    if (curView instanceof LinearLayout)
                    if (curView != null && curView.getTag() != null && curView.getTag().toString().equals("card")) {
                        card = curView;
                        break;
                    }
                }
//                _root.getChildAt(_root.getChildCount());
//                Log.i("ViewName",card.getClass().getName());

                if (card != null) {
                    animateCardOff(card, direction, 400);
                }
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.gc();
        imageView.setImageBitmap(null);
        btnNope.setImageBitmap(null);
        btnYeah.setImageBitmap(null);
        //btnInfo.setImageBitmap(null);
        btnNoMoreFood.setImageBitmap(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event

        if (super.onNavDrawerButtonPress(item)) { //Required to activate drawer
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
