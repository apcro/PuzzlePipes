package com.alienpants.puzzlepipes.services;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Fps {

    private long _startTime = 0;            // Measurement mStart time
    private int  _cnt = 0;                  // counter
    private Paint _paint = new Paint();     // Paint information
    private float _fps;                     // fps
    private final static int N = 60;        // Number of samples to average
    private final static int FONT_SIZE = 20;// font size
    
    public Fps() {
            _paint.setColor(Color.BLUE);    // Set font color to blue
            _paint.setTextSize(FONT_SIZE);  // Set font size
    }
    
    public boolean update() {
            if ( _cnt == 0 ) { // If the first frame, memorize the time
                    _startTime = System.currentTimeMillis();
            }
            if ( _cnt == N ) { // Calculate the average in the 60th frame
                    long t = System.currentTimeMillis();
                    _fps = 1000.f/((t-_startTime)/(float)N);
                    _cnt = 0;
                    _startTime = t;
            }
            _cnt++;
            return true;
    }

    public void onDraw(Canvas c) {
            c.drawText(String.format("%.1f", _fps), 0, FONT_SIZE-2, _paint);
    }

}