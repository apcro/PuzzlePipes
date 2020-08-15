package com.alienpants.puzzlepipes.activities;

import java.util.ArrayList;

import com.alienpants.puzzlepipes.R;
import com.alienpants.puzzlepipes.datamodels.Character;
import com.alienpants.puzzlepipes.datamodels.GameStatus;
import com.alienpants.puzzlepipes.console.*;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

public class  PlayPuzzle {

	private Paint paint,paint_difficulty;
	private Context context;
	
	private int sensitivity = 50;
	private int oldX = -1, oldY = -1;
	
	public Puzzle puzzle;
	
	private Bitmap[] a;
	private Bitmap[] b;
	
	public Rect rect;

	private int n;
	
	private Rect src,dst;

	private int ani_rawColumn;

	private Direction ani_direction;

	private int ani_moving=-1;

	private int ani_moving_per_frame;
	
	private MainView mainView;
	
	public Character startObject, goalObject;

	private int movesCount = 0;
	
	public PlayPuzzle(Context context, MainView mainView, Rect rect, int n, int gameNumber) {
		this.context = context;
		this.mainView = mainView;
		
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint_difficulty = new Paint();
		paint_difficulty.setColor(Color.BLACK);
		int scaledSize = context.getResources().getDimensionPixelSize(R.dimen.myFontSize);
		paint_difficulty.setTextSize(scaledSize);
		
		puzzle = new Puzzle(n+2,1, gameNumber);
		this.rect = rect;
		this.n = n;
		
		this.src = new Rect(0,0,140,140);
		this.dst = new Rect();
		
		Resources r = context.getResources();
		
		a = new Bitmap[8];
        a[0] = BitmapFactory.decodeResource(r, R.drawable.a0);
        a[1] = BitmapFactory.decodeResource(r, R.drawable.a1);
        a[2] = BitmapFactory.decodeResource(r, R.drawable.a2);
        a[3] = BitmapFactory.decodeResource(r, R.drawable.a3);
        a[4] = BitmapFactory.decodeResource(r, R.drawable.a4);
        a[5] = BitmapFactory.decodeResource(r, R.drawable.a5);
        a[6] = BitmapFactory.decodeResource(r, R.drawable.a6);
        a[7] = BitmapFactory.decodeResource(r, R.drawable.a7);
		b = new Bitmap[8];
        b[0] = BitmapFactory.decodeResource(r, R.drawable.b0);
        b[1] = BitmapFactory.decodeResource(r, R.drawable.b1);
        b[2] = BitmapFactory.decodeResource(r, R.drawable.b2);
        b[3] = BitmapFactory.decodeResource(r, R.drawable.b3);
        b[4] = BitmapFactory.decodeResource(r, R.drawable.b4);
        b[5] = BitmapFactory.decodeResource(r, R.drawable.b5);
        b[6] = BitmapFactory.decodeResource(r, R.drawable.b6);
        b[7] = BitmapFactory.decodeResource(r, R.drawable.b7);
        
        this.ani_moving_per_frame = rect.width() / 15;

        movesCount = 0;
	}
	
	public ArrayList<Point> routePosition() {
		ArrayList<Point> list = new ArrayList<Point>();
		int[][] route = puzzle.checkRoute(puzzle.mCells, puzzle.mStart, puzzle.mGoal);
		int index = 1;
		while(true) {
			for (int x = 0; x < n+2; x++) {
				for (int y = 0; y < n+2; y++) {
					if (route[x][y] == index) {
						int cellWidth = rect.width() / n;
						int cellHeight = rect.height() /n;
						Point p = new Point(
								rect.left + cellWidth * (x-1),
								rect.top + cellHeight * (y-1));
						list.add(p);
						index++;
						if (x == puzzle.mGoal.x && y == puzzle.mGoal.y) {
							return list;
						}
					}
				}
			}
		}
	}
	
	public ArrayList<Point> routePositionHole() {
		ArrayList<Point> list = new ArrayList<Point>();
		Point hole = puzzle.getHolePoint();
		int[][] route = puzzle.checkRoute(puzzle.mCells, puzzle.mStart,hole);
		int index = 1;
		while (true) {
			for (int x = 0; x < n+2; x++) {
				for(int y=0;y<n+2;y++) {
					if(route[x][y] ==index){
						int cellWidth = rect.width() / n;
						int cellHeight = rect.height() /n;
						Point p = new Point(
								rect.left + cellWidth*(x-1),
								rect.top  +cellHeight*(y-1));
						list.add(p);
						index++;
						if (x==hole.x && y==hole.y) {
							return list;
						}
					}
				}
			}
		}
	}

	public void draw(Canvas canvas){
		canvas.drawRect(rect, paint);
		
		int w = rect.width()/n;
		int h = rect.height()/n;
		Cell[][] cells = puzzle.mCells;
		int[][] ans = puzzle.checkRoute(puzzle.mCells, puzzle.mStart, puzzle.mGoal);
		
		if (ani_moving!=-1) {
			if (ani_direction==Direction.down) {
				dst.set(
						rect.left+ani_rawColumn*w,
						rect.top-h+ani_moving,
						rect.left+(ani_rawColumn+1)*w,
						rect.top+ani_moving);
				if (ans[ani_rawColumn+1][n] != 0) {
					canvas.drawBitmap(a[cells[ani_rawColumn + 1][n].toInt()], src, dst, paint);
				} else {
					canvas.drawBitmap(b[cells[ani_rawColumn + 1][n].toInt()], src, dst, paint);
				}
			} else if (ani_direction==Direction.up) {
				dst.set(
						rect.left + ani_rawColumn*w,
						rect.bottom - ani_moving,
						rect.left + (ani_rawColumn+1)*w,
						rect.bottom+h-ani_moving);
				if (ans[ani_rawColumn+1][1] != 0) {
					canvas.drawBitmap(a[cells[ani_rawColumn + 1][1].toInt()], src, dst, paint);
				} else {
					canvas.drawBitmap(b[cells[ani_rawColumn + 1][1].toInt()], src, dst, paint);
				}
			} else if (ani_direction==Direction.right) {
				dst.set(
						rect.left-w+ani_moving,
						rect.top + h*ani_rawColumn,
						rect.left +ani_moving,
						rect.top + h*(ani_rawColumn+1));
				if (ans[n][ani_rawColumn+1] != 0) {
					canvas.drawBitmap(a[cells[n][ani_rawColumn + 1].toInt()], src, dst, paint);
				} else {
					canvas.drawBitmap(b[cells[n][ani_rawColumn + 1].toInt()], src, dst, paint);
				}
			} else if (ani_direction==Direction.left) {
				dst.set(
						rect.right -ani_moving,
						rect.top + ani_rawColumn*h,
						rect.right+w-ani_moving,
						rect.top + (ani_rawColumn+1)*h);
				if (ans[1][ani_rawColumn+1] != 0) {
					canvas.drawBitmap(a[cells[1][ani_rawColumn + 1].toInt()], src, dst, paint);
				} else {
					canvas.drawBitmap(b[cells[1][ani_rawColumn + 1].toInt()], src, dst, paint);
				}
			}
		}
		
		for (int x=0; x<n; x++) {
			for (int y=0; y<n; y++) {
				
				dst.set(
						rect.left + w*x,
						rect.top  + h*y,
						rect.left + w*(x+1),
						rect.top+ h*(y+1));
				// animation
				if (ani_moving!=-1) {
					if (ani_direction==Direction.down && x==ani_rawColumn) {
						dst.bottom += ani_moving;
						dst.top+=ani_moving;
					}
					else if (ani_direction==Direction.up && x==ani_rawColumn) {
						dst.bottom -= ani_moving;
						dst.top-=ani_moving;
					}
					else if (ani_direction==Direction.right && y==ani_rawColumn) {
						dst.right += ani_moving;
						dst.left +=ani_moving;
					}
					else if (ani_direction==Direction.left && y==ani_rawColumn) {
						dst.right -= ani_moving;
						dst.left -=ani_moving;
					}
				}
				if (ans[x+1][y+1]!=0) {
					canvas.drawBitmap(a[cells[x+1][y+1].toInt()], src, dst, paint);
				} else {
					canvas.drawBitmap(b[cells[x + 1][y + 1].toInt()], src, dst, paint);
				}
			}
		}
		// I will erase it later.
		canvas.drawRect(0, rect.bottom, canvas.getWidth(), canvas.getHeight(), paint);
		canvas.drawRect(rect.right, 0, canvas.getWidth(), canvas.getHeight(), paint);
	}
	
	/**
	 * Find out which row.
	 * @param oldy
	 * @param newy
	 * @return Number of lines 0 or more, but if it is not any line it returns -1.
	 */
	private int checkRaw(int oldy, int newy) {
		int h = rect.height()/n;
		for (int y=0; y<n; y++) {
            if (rect.top + h * y < oldy && oldy < rect.top + h * (y + 1) && rect.top + h * y < newy && newy < rect.top + h * (y + 1)) {
                return y;
            }
        }
		return -1;
	}
	
	/**
	 * Find out what number column.
	 * @param oldx
	 * @param newx
	 * @return Several columns or more, but if it is not any column it returns -1.
	 */
	private int checkColumn(int oldx, int newx) {
		int w = rect.width()/n;
		for (int x = 0; x < n; x++) {
            if (rect.left + w * x < oldx && oldx < rect.left + w * (x + 1) && rect.left + w * x < newx && newx < rect.left + w * (x + 1)) {
                return x;
            }
        }
		return -1;
	}

	public void touch(MotionEvent event, Context context) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				oldX = (int) event.getX();
				oldY = (int) event.getY();
				break;

			case MotionEvent.ACTION_MOVE:
				if (oldX != -1 && oldY != -1) {
					int raw = this.checkRaw(oldY, (int)event.getY());
					int column = this.checkColumn(oldX, (int)event.getX());
					int dx = (int)event.getX() - oldX;
					int dy = (int)event.getY() - oldY;
					if (dx > sensitivity && raw!=-1) {
						// mRight
						oldX = -1;
						oldY = -1;

						ani_rawColumn = raw;
						ani_direction = Direction.right;
						ani_moving = 0;
						mainView.mMovesCount.increaseMovesCount();
					} else if(dx < -sensitivity && raw!=-1) {
						// mLeft
						oldX = -1;
						oldY = -1;

						ani_rawColumn = raw;
						ani_direction = Direction.left;
						ani_moving = 0;
						mainView.mMovesCount.increaseMovesCount();
					} else if(dy > sensitivity && column!=-1) {
						// mDown
						oldX = -1;
						oldY = -1;

						ani_rawColumn = column;
						ani_direction = Direction.down;
						ani_moving = 0;
						mainView.mMovesCount.increaseMovesCount();
					} else if (dy < -sensitivity && column!=-1) {
						// increaseScore
						oldX = -1;
						oldY = -1;

						ani_rawColumn = column;
						ani_direction = Direction.up;
						ani_moving = 0;
						mainView.mMovesCount.increaseMovesCount();
					}
				}
				break;
		}
	}

	// Update processing
	public void update() {

		if (ani_moving != -1) {
			ani_moving += ani_moving_per_frame;
			if (ani_moving >= rect.width()/n) {
				// Animation end
				ani_moving = -1;
				// Actual movement
				puzzle.move(ani_rawColumn+1, ani_direction);
				// Puzzle completion
				if (puzzle.isComplete()) {
					// Designate the way to follow.
					startObject.setPositions(this.routePosition());
					this.mainView.gameStatus = GameStatus.characterMoving;
				}
				// Pitfall completion
				if (puzzle.isRouteHole()) {
					// Designate the way to follow.
					startObject.setPositions(this.routePositionHole());
					this.mainView.gameStatus = GameStatus.characterMovingToHole;
				}
			}
		}

	}
}
