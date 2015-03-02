package edu.washington.echee.swipeviews;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements View.OnTouchListener {
    ViewGroup _root;                // root view
    LinearLayout cardView1;         // current swipable card
    private int _xDelta;            // distance card dragged in X-axis
    private int screenWidth = 0;    // width of rootView
    private float pivotX;           // rotation pivot X position
    private float pivotY;           // rotation pivot Y position

    public static final float MAX_ROTATION = 17f;   // max degrees to rotate card

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get Root container
        _root = (ViewGroup) findViewById(R.id.root);

        // Create card view
        cardView1 = new LinearLayout(this);
        cardView1.setBackgroundColor(Color.GREEN);

        // give it dummy height & width (change it later in onWindowFocusChanged())
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(300, 300); // width & height
        cardView1.setLayoutParams(layoutParams);
        _root.addView(cardView1);

        _root.setOnTouchListener(this);
    }

    /*
     * Since cannot get root width onCreate because view hasn't loaded by then, get width here
     * Set the pivot position to
     */
    @Override
    public void onWindowFocusChanged(boolean b) {
        this.screenWidth = _root.getWidth();
        int cardWidth = this.screenWidth-32;
        int height = 300;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(cardWidth, height); // width & height
        layoutParams.leftMargin = 16;
        layoutParams.topMargin = 16;
        cardView1.setLayoutParams(layoutParams);

        this.pivotX = cardWidth / 2;    // set x pivot point center
        this.pivotY = height * 2f;      // set y pivot point 2x below the height

        // set pivot location to bottom center
        cardView1.setPivotX(pivotX);
        cardView1.setPivotY(pivotY);

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int X = (int) event.getRawX();    // X position of touch event
        int dif;                                // temp xDelta
        float swipePercent;                     // % of distance from center X to edge of screen

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            // On Press Down
            case MotionEvent.ACTION_DOWN:
                _xDelta = X - (int)this.cardView1.getTranslationX();    // track how far moved x
                break;
            // On Drag
            case MotionEvent.ACTION_MOVE:
                dif = X - _xDelta;
                swipePercent = (float)dif / (float)screenWidth;

                float rotation = swipePercent * MAX_ROTATION;
                float alpha = (1f - Math.abs(swipePercent))*0.7f;

                // set rotation, alpha, & translation
                this.cardView1.setAlpha(alpha);
                this.cardView1.setRotation(rotation);
                this.cardView1.setTranslationX(dif);

                Log.i("OnTouch", "swipePercent = " + swipePercent + "; rotation = " + rotation+ "; alpha = " + alpha);
                break;
            // On Release
            case MotionEvent.ACTION_UP:
                dif = X - _xDelta;
                swipePercent = Math.abs((float) dif / (float) screenWidth);

                // Reset card position if did NOT reach swipe threshold
                if (swipePercent < 0.35f) {
                    this.cardView1.setAlpha(1);
                    this.cardView1.setRotation(0f);
                    this.cardView1.setTranslationX(0);

                // Animate card off screen if did reach swipe threshold
                } else {
                    int animDuration = 600;
                    float sign = 1;
                    if (this.cardView1.getRotation() < 0) {
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

                    this.cardView1.startAnimation(animSet);
                }

                Log.i("OnTouch", "Touch Released");
                break;
        }
        _root.invalidate();
        return true;
    }
}
