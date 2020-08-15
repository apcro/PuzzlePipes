package com.alienpants.puzzlepipes.activities;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.alienpants.puzzlepipes.R;
import com.alienpants.puzzlepipes.datamodels.GameStatus;
import com.alienpants.puzzlepipes.ui.Dialog;
import com.alienpants.puzzlepipes.datamodels.Character;
import com.alienpants.puzzlepipes.services.FPSManager;
import com.alienpants.puzzlepipes.ui.MovesCount;

public class MainView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
	
	private SurfaceHolder holder;
	private Thread thread;
	
	private long interval = 1;
	private Handler handler = new Handler();
	public Context mContext;
	
	FPSManager fPSManager;
	public PlayPuzzle playPuzzle;
	
	private Bitmap backGround;
	private Rect backGroundSrc, backGroundDst;
	
	public Character goalObject, startObject;
	
	private Dialog dialog;
	
	private Bitmap title;
	
	private int mBoardSize;

	public int mCount, mLevel;

    public MovesCount mMovesCount;

	public GameStatus gameStatus;

	public boolean mIsRandom = false;

	public int mWidth, mHeight;

	public MainView(Context context) {
		super(context);

		this.mContext = context;
        this.mMovesCount  = new MovesCount(mContext);

		getLevel();
		
		fPSManager = new FPSManager(30);

        SharedPreferences pref = mContext.getSharedPreferences( "gamecounter", Context.MODE_PRIVATE );
        mBoardSize = pref.getInt("difficulty", 4);

        if (mBoardSize == 99) {
			mIsRandom = true;
			getRandomBoardSize();
		}

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		mWidth = size.x;
		mHeight = size.y;

		this.backGroundSrc = new Rect(0,0,700,1200);
		this.backGroundDst = new Rect(0,0, mWidth, mHeight);
		
        dialog = new Dialog(context,this, mWidth, mHeight);

		playPuzzle = new PlayPuzzle(
				context,this,
				new Rect(mWidth/14,mHeight/3,mWidth*13/14,mHeight*5/6),
				mBoardSize,
				mLevel);
		
        startObject = new Character(context, playPuzzle.puzzle.mStart, playPuzzle.rect, mBoardSize, R.drawable.person,this);
        goalObject = new Character(context, playPuzzle.puzzle.mGoal, playPuzzle.rect, mBoardSize, R.drawable.flag,this);
        
        playPuzzle.startObject = startObject;
        playPuzzle.goalObject = goalObject;
        
		Resources r = context.getResources();
        backGround = BitmapFactory.decodeResource(r, R.drawable.background_game);

        title = BitmapFactory.decodeResource(r, R.drawable.title);
        gameStatus = GameStatus.title;

		getHolder().addCallback(this);

		Runnable runnable = new Runnable() {
			public void run() {
				TimerEvent();
				handler.postDelayed(this, interval);
			}
		};
		handler.postDelayed(runnable, interval);
	}

	private void getRandomBoardSize() {
		Random r = new Random();
		mBoardSize = r.nextInt(4) + 4;
	}

	private void update() {
		if (gameStatus == GameStatus.playing) {
			// Puzzle in progress
			playPuzzle.update();
		} else if (gameStatus == GameStatus.characterMoving || gameStatus == GameStatus.characterMovingToHole) {
			// After the puzzle, during the animation after the pitfall
			startObject.update();
		}
	}
	
	private void TimerEvent() {
	}

	public void surfaceCreated(SurfaceHolder holder) {
		this.holder = holder;
		thread = new Thread(this);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		if (thread != null) {
			thread.start();
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		thread = null;
	}

	public void run() {

		while (thread != null) {
			update();

			Canvas canvas = holder.lockCanvas();
			if (canvas != null) {
                this.draw(canvas);
                holder.unlockCanvasAndPost(canvas);
            }

			try {
				TimeUnit.NANOSECONDS.sleep(fPSManager.state());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (canvas == null) {
			return;
		}
		if (gameStatus == GameStatus.title) {
			canvas.drawBitmap(title, this.backGroundSrc, this.backGroundDst, null);
			return;
		}

		playPuzzle.draw(canvas);
		canvas.drawBitmap(backGround,this.backGroundSrc,this.backGroundDst, null);
		goalObject.draw(canvas);
		startObject.draw(canvas);
		if (gameStatus == GameStatus.successDialog || gameStatus == GameStatus.failureDialog) {
			dialog.draw(canvas);
		}
        mMovesCount.draw(canvas);

	}

    @SuppressWarnings("ClickableViewAccessibility")
	public boolean onTouchEvent(MotionEvent event) {
		if (gameStatus == GameStatus.playing) {
			playPuzzle.touch(event, mContext);
		} else if (gameStatus == GameStatus.successDialog || gameStatus == GameStatus.failureDialog) {
			dialog.touch(event, mBoardSize);
		} else if (gameStatus == GameStatus.title) {
			this.gameStatus = GameStatus.playing;
		}
		return true;
	}

	private void getLevel() {
		SharedPreferences pref = mContext.getSharedPreferences( "gamecounter", Context.MODE_PRIVATE );
		mCount = pref.getInt("gamecount", 1);
	}

}
