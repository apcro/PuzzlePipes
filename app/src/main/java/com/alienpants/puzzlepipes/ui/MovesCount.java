package com.alienpants.puzzlepipes.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;


import com.alienpants.puzzlepipes.R;

import androidx.core.content.res.ResourcesCompat;

public class MovesCount {
	private int mMovesCount;
	private Paint mPaint;

	public MovesCount(Context context) {
		mMovesCount = 0;
		mPaint = new Paint();
		mPaint.setColor(Color.WHITE);
		int scaledSize = context.getResources().getDimensionPixelSize(R.dimen.myFontSize);
		mPaint.setTextSize(scaledSize);

		Typeface typeface = ResourcesCompat.getFont(context, R.font.alba);
		mPaint.setTypeface(typeface);

	}
	
	/**
	 * Reset the number of games.
	 * @return
	 */
	public void reset() {
		this.mMovesCount = 0;
	}
	
	/**
	 * @return Get current number of games
	 */
	public int getMovesCount() {
		return mMovesCount;
	}
	/**
	 * Count up games
	 */
	public void increaseMovesCount() {
		this.mMovesCount++;
	}
	
	/**
	 * Game number drawing
	 * @param canvas
	 */
	public void draw(Canvas canvas){
		canvas.drawText("Moves: " + String.valueOf(mMovesCount),460, 250, mPaint);
	}

}
