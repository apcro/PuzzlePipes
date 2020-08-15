package com.alienpants.puzzlepipes.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.alienpants.puzzlepipes.R;
import com.alienpants.puzzlepipes.activities.MainView;
import com.alienpants.puzzlepipes.activities.PlayPuzzle;
import com.alienpants.puzzlepipes.datamodels.GameStatus;

/*
 * Puzzle Complete Message
 */
public class Dialog {
	Rect src, dst;
	Bitmap bmpComplete, bmpHole;
	MainView mainView;

	private Context mContext;

	public Dialog(Context context, MainView mainView, int w, int h) {
		this.mainView = mainView;
		this.src = new Rect(0,0,700,1200);
		this.dst = new Rect(0,0, w, h);

		mContext = context;

		Resources r = context.getResources();
        bmpComplete = BitmapFactory.decodeResource(r, R.drawable.complete_back);
        bmpHole = BitmapFactory.decodeResource(r, R.drawable.hole_back);
	}

	public void draw(Canvas canvas){
		if (mainView.gameStatus == GameStatus.successDialog) {
			canvas.drawBitmap(bmpComplete, src, dst, null);
		} else {
			canvas.drawBitmap(bmpHole, src, dst, null);
		}
	}

	/**
	 * @param event
	 * @param n　Size of board surface
	 */
	public void touch(MotionEvent event, int n) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			// Game number update
			if (mainView.gameStatus == GameStatus.successDialog) {
				increaseScore();
			} else {
				resetScore();
			}

			mainView.playPuzzle.puzzle.init(n+2,1, getLevel());

			// Start, update mGoal object
			mainView.startObject.setPoint(mainView.playPuzzle.puzzle.mStart);
			mainView.goalObject.setPoint(mainView.playPuzzle.puzzle.mGoal);
			this.mainView.gameStatus = GameStatus.playing;
			mainView.mMovesCount.reset();

		}
	}

	public void increaseScore() {
		int count = getLevel();
		count++;

		saveScore(count);

		int highscore = getHighScore();

		// High score update
		if(count > highscore) {
            saveHighScore(count);
        }
	}

	/**
	 * Save the number of games.
	 */
	public void saveScore(int score) {
		SharedPreferences pref = mContext.getSharedPreferences( "gamecounter", Context.MODE_PRIVATE );
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt("gamecount", score);
		editor.apply();
	}

    public void saveHighScore(int score) {
        SharedPreferences pref = mContext.getSharedPreferences( "gamecounter", Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("score", score);
        editor.apply();
    }

    public void resetScore() {
        SharedPreferences pref = mContext.getSharedPreferences( "gamecounter", Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("gamecount", 1);
        editor.putInt("score", 1);
        editor.apply();
    }

	private int getLevel() {
		SharedPreferences pref = mContext.getSharedPreferences( "gamecounter", Context.MODE_PRIVATE );
		return pref.getInt("gamecount", 1);
	}

	private int getHighScore() {
		SharedPreferences pref = mContext.getSharedPreferences( "gamecounter", Context.MODE_PRIVATE );
		return pref.getInt("score", 1);
	}


}
