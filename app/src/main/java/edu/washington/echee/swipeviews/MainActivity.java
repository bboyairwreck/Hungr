package edu.washington.echee.swipeviews;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements View.OnTouchListener {
    ViewGroup _root;
    LinearLayout cardView1;
    private int _xDelta;
    private int _yDelta;
    private int screenWidth = 0;

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

//        cardView1.setOnTouchListener(this);
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

        // set pivot location to bottom center
        cardView1.setPivotX(cardWidth / 2);
        cardView1.setPivotY(height * 2);

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.cardView1.getLayoutParams();
                _xDelta = X - (int)this.cardView1.getTranslationX();
                _yDelta = Y - (int)this.cardView1.getTranslationY();
//                Log.i("OnTouch", "DOWN == " + _yDelta);
//                Log.i("OnTouch", "LEFT == " + _xDelta);
                break;
            case MotionEvent.ACTION_MOVE:
                int dif = X - _xDelta;

                float rotation = ((float)dif / (float)screenWidth) * 15f;
//                Log.i("OnTouch", "dif = " + dif);
//                Log.i("OnTouch", "screenWidth = " + screenWidth);
                Log.i("OnTouch", "rotation = " + rotation);
                this.cardView1.setRotation(rotation);
                this.cardView1.setTranslationX(dif);
                break;
        }
        _root.invalidate();
        return true;
    }
}
