package com.alienpants.puzzlepipes.services;

import java.util.concurrent.TimeUnit;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Classes to display / adjust FPS
 */
public class FPSManager {
	// 1 second (nanoseconds)
	private static long ONE_SEC_TO_NANO = TimeUnit.SECONDS.toNanos(1L); 

	// 1 millisecond (nanoseconds)
	private static long ONE_MILLI_TO_NANO = TimeUnit.MILLISECONDS.toNanos(1L); 

	private int maxFps;
	private int[] fpsBuffer;
	private int fpsCnt;
	private long startTime;
	private long elapsedTime;
	private long sleepTime;
	private long oneCycle; 
	
	private final int FONT_SIZE = 20;
	private Paint paint;

	/**
	 * constructor
	 * @param fps operation FPS
	 */
	public FPSManager(int fps) {
		maxFps = fps;
		fpsBuffer = new int[maxFps];
		fpsCnt = 0;
		startTime = System.nanoTime();
		oneCycle = (long)(Math.floor((double)ONE_SEC_TO_NANO / maxFps));
		
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTextSize(FONT_SIZE);
	} 

	/**
	 * State update
	 * @return Time to sleep (nanoseconds)
	 */
	public long state() {
		fpsCnt++;
		if (maxFps <= fpsCnt) {
			fpsCnt = 0;
		} 

		elapsedTime = System.nanoTime() - startTime;
		sleepTime = oneCycle - elapsedTime; 

		// Even if there is no room to sleep 1 millisecond
		if (sleepTime < ONE_MILLI_TO_NANO) {
			sleepTime = ONE_MILLI_TO_NANO;
		} 

		int fps = (int)(ONE_SEC_TO_NANO / (elapsedTime + sleepTime));
		fpsBuffer[fpsCnt] = fps; 

		startTime = System.nanoTime() + sleepTime; 

		return sleepTime;
	} 

	/**
	 * Acquisition of FPS
	 * @return Current FPS
	 */
	public double getFps() {
		int allFps = 0;
		for (int i = 0; i < maxFps; i++) {
			allFps += fpsBuffer[i];
		} 

		return allFps / (double)maxFps;
	}
	
	public void draw(Canvas canvas){
            canvas.drawText(String.format("%.1f", getFps()), 0, FONT_SIZE-2, paint);
	}
}