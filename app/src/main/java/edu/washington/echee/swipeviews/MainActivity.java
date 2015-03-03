package edu.washington.echee.swipeviews;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements View.OnTouchListener {
    ViewGroup _root;                // root view
    private int _xDelta;            // distance card dragged in X-axis
    private int screenWidth = 0;    // width of rootView
    private float pivotX;           // rotation pivot X position
    private float pivotY;           // rotation pivot Y position
    private int numCards;

    public static final float MAX_ROTATION = 17f;   // max degrees to rotate card
    public static final int MAX_CARDS = 10;         // max number of cards on screen at a time
    public static final int MIN_CARDS = 3;         // min number of cards on screen at a time

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get Root container
        _root = (ViewGroup) findViewById(R.id.root);
        this.numCards = MAX_CARDS;
    }

    /*
     * Since cannot get root width onCreate because view hasn't loaded by then, get width here
     * Set the pivot position to
     */
    @Override
    public void onWindowFocusChanged(boolean b) {
        this.screenWidth = _root.getWidth();
        addCards(MAX_CARDS);
    }

    /*
     * Adds @size number of cards to root view. Each card will be given an onClickListener
     * Requirement:
     *      Must be done AFTER root view is loaded i.e. onWindowFocus() but NOT in onCreate()
     */
    public void addCards(int size) {
        int cardWidth = this.screenWidth-32;    // width of each card
        int height = cardWidth;                       // height of each card

        // Create cards in add to root view
        for (int i = 0; i < size; i++) {
            LinearLayout card = new LinearLayout(this);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(cardWidth, height); // width & height
            layoutParams.leftMargin = 16;
            layoutParams.topMargin = 16;
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
                    int animDuration = 600;
                    float sign = 1;
                    if (v.getRotation() < 0) {
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

                Log.i("OnTouch", "Touch Released");

                break;
        }
        _root.invalidate();
        return true;
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
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            _root.removeView(v);        // removes card from root view
                            Log.i("onAnimationEnd", "Card removed");

                            MainActivity.this.numCards--; // decrement number of cards

                            // If down to MIN_CARDS, add more cards
                            if (numCards <= MIN_CARDS) {
                                int numOfAddedCards = MAX_CARDS - numCards;
                                MainActivity.this.addCards(numOfAddedCards);
                                MainActivity.this.numCards += numOfAddedCards;

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
}
