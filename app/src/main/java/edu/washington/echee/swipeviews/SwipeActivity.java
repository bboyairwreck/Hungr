/**
 * 3/3/15
 * @author Eric Chee
 */
package edu.washington.echee.swipeviews;

import android.content.Context;
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

public class SwipeActivity extends ActionBarActivity implements View.OnTouchListener {
    ViewGroup _root;                // root view
    private int _xDelta;            // distance card dragged in X-axis
    private int screenWidth = 0;    // width of rootView
    private float pivotX;           // rotation pivot X position
    private float pivotY;           // rotation pivot Y position
    private int numCards;
    ImageView imageView;
    Bitmap bitmap;

    public static final float MAX_ROTATION = 17f;   // max degrees to rotate card
    public static final int MAX_CARDS = 5;         // max number of cards on screen at a time
    public static final int MIN_CARDS = 2;         // min number of cards on screen at a time
    public static final float GO_LEFT = -1000f;   // max degrees to rotate card
    public static final float GO_RIGHT = 1000f;   // max degrees to rotate card

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get Root container
        _root = (ViewGroup) findViewById(R.id.root);

        ImageButton btnNope = (ImageButton) findViewById(R.id.btnNope);
        ImageButton btnYeah = (ImageButton) findViewById(R.id.btnYeah);
        ImageButton btnInfo = (ImageButton) findViewById(R.id.btnInfo);

        btnNope.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.nope, 100, 100));
        btnYeah.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.yeah, 100, 100));
        btnInfo.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.info, 100, 100));


        this.numCards = MAX_CARDS;

        String[] stringArr = getResources().getStringArray(R.array.nav_drawer_list);
        ArrayList<String> navDrawerList = new ArrayList<String>(Arrays.asList(stringArr)); //Create nav_drawer elements list
        ListView lvNavDrawer = (ListView) findViewById(R.id.lvNavDrawer);
        lvNavDrawer.setAdapter(new NavDrawerListAdapter(this, R.layout.nav_drawer_list_layout, navDrawerList));
        lvNavDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    /*
     * Since cannot get root width onCreate because view hasn't loaded by then, get width here.
     * Add max num or cards to root view
     */
    @Override
    public void onWindowFocusChanged(boolean b) {
        this.screenWidth = _root.getWidth();
        addCards(MAX_CARDS);

        ImageButton btnNope = (ImageButton) findViewById(R.id.btnNope);
        ImageButton btnYeah = (ImageButton) findViewById(R.id.btnYeah);
        ImageButton btnInfo = (ImageButton) findViewById(R.id.btnInfo);

        btnNope.setOnClickListener(decisionButtonListener(GO_LEFT));
        btnYeah.setOnClickListener(decisionButtonListener(GO_RIGHT));
    }

    /*
     * Adds @size number of cards to root view. Each card will be given an onClickListener
     * Requirement:
     *      Must be done AFTER root view is loaded i.e. onWindowFocus() but NOT in onCreate()
     */
    public void addCards(int size) {
        int cardMargin = 67;
        int cardWidth = this.screenWidth - (cardMargin * 2);    // width of each card

        // Create cards in add to root view
        for (int i = 0; i < size; i++) {

            LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            RelativeLayout card = (RelativeLayout) vi.inflate(R.layout.card_details, null);


//            Bitmap food1Bitmap = drawableToBitmap(getResources().getDrawable(R.drawable.food1));
//            food1Bitmap.recycle();
//            imageView = (ImageView) card.findViewById(R.id.ivFoodImage);
//
//            imageView.setImageBitmap(food1Bitmap);
//
//            imageView.setLayoutParams(new RelativeLayout.LayoutParams(cardWidth, cardWidth));
            imageView = (ImageView) card.findViewById(R.id.ivFoodImage);
            imageView.setImageBitmap(
                    decodeSampledBitmapFromResource(getResources(), R.drawable.food1, 100, 100));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);



//            imageView.setImageBitmap(bitmap); // Do whatever you need to do to load the image you want.
            imageView.setLayoutParams(new RelativeLayout.LayoutParams(cardWidth, cardWidth));


            TextView foodTitle = (TextView) card.findViewById(R.id.tvFoodTitle);
            int titleHeight = foodTitle.getHeight();

            RelativeLayout rlPriceAndRatings = (RelativeLayout) card.findViewById(R.id.rlPriceAndRatings);
            int prHeight = rlPriceAndRatings.getHeight();

            int starSize = ((int) getResources().getDimension(R.dimen.starSize));
            int starPadding = ((int) getResources().getDimension(R.dimen.starPadding))*2;

            int height = cardWidth + starSize + starPadding;

            Log.i("SwipeActivity", "tileHeight + prHeight + height = " + titleHeight + " + " + prHeight + " + " + height);


//            LinearLayout card = new LinearLayout(this);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(cardWidth, height); // width & height
            layoutParams.leftMargin = cardMargin;
            layoutParams.topMargin = cardMargin;
            card.setLayoutParams(layoutParams);

            // TODO Delete. Each card alternates between Green & RED
            card.setBackgroundColor(Color.RED);
            if (i%2==0) {
                card.setBackgroundColor(Color.GREEN);
            }
            // TODO DELETE ^

            this.pivotX = cardWidth / 2;    // set x pivot point center
            this.pivotY = height * 2f;      // set y pivot point 2x below the height

            // set pivot location to bottom center
            card.setPivotX(pivotX);
            card.setPivotY(pivotY);

            // Add card to root view & set onTouchListener
            _root.addView(card, 0, layoutParams);
            card.setOnTouchListener(this);
        }
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
//                    int animDuration = 600;
//                    float sign = 1;
//                    if (v.getRotation() < 0) {
//                        sign = -1;
//                    }
//                    RotateAnimation rotateAnim = new RotateAnimation(0, MAX_ROTATION*sign, pivotX, pivotY);
//                    rotateAnim.setDuration(animDuration);
//                    rotateAnim.setRepeatCount(0);
//
//                    TranslateAnimation translateAnim = new TranslateAnimation(0, (this.screenWidth)*sign, 0, 0);
//                    translateAnim.setDuration(animDuration);
//                    translateAnim.setRepeatCount(0);
//
//                    AnimationSet animSet = new AnimationSet(true);
//                    animSet.addAnimation(rotateAnim);
//                    animSet.addAnimation(translateAnim);
//                    animSet.setFillAfter(true);
//                    animSet.setAnimationListener(new MyAnimationListener(v));
//
//                    // Animate card off screen and remove it
//                    v.startAnimation(animSet);
                    animateCardOff(v , v.getRotation());
                }

                Log.i("OnTouch", "Touch Released");

                break;
        }
        _root.invalidate();
        return true;
    }

    private void animateCardOff(View v, float rotation){
        int animDuration = 600;
        float sign = 1;
        if (rotation < 0) {
            sign = -1;
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
        public void onAnimationStart(Animation animation) {}
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
                    } else {
                        Log.i("ViewName", "curView is null");
                    }
                }
//                _root.getChildAt(_root.getChildCount());
//                Log.i("ViewName",card.getClass().getName());

                if (card != null) {
                    animateCardOff(card, direction);
                }
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.gc();
        imageView.setImageBitmap(null);
    }

}
