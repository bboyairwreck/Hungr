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
    LinearLayout relativeLayout1;
    private int _xDelta;
    private int _yDelta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _root = (ViewGroup) findViewById(R.id.root);

        relativeLayout1 = new LinearLayout(this);
        relativeLayout1.setId(1);
        relativeLayout1.setBackgroundColor(Color.GREEN);

        Log.i("MainActivity", "" + _root.getWidth());

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(300, 300); // width & height
        relativeLayout1.setLayoutParams(layoutParams);
        _root.addView(relativeLayout1);

        relativeLayout1.setOnTouchListener(this);

    }

    @Override
    public void onWindowFocusChanged(boolean b) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(_root.getWidth()-32, 300); // width & height
        layoutParams.leftMargin = 16;
        layoutParams.topMargin = 16;
        relativeLayout1.setLayoutParams(layoutParams);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                _xDelta = X - layoutParams.leftMargin;
                _yDelta = Y - layoutParams.topMargin;
                Log.i("OnTouch", "DOWN == " + _yDelta);
                Log.i("OnTouch", "LEFT == " + _xDelta);
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) v.getLayoutParams();
                int dif = X - _xDelta;
                layoutParams2.leftMargin = dif;
                v.setLayoutParams(layoutParams2);
                break;
        }
        _root.invalidate();
        return true;
    }
}
