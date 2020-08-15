package com.alienpants.puzzlepipes.datamodels;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.alienpants.puzzlepipes.activities.MainView;

public class Character {
	Paint paint;
	Bitmap bmp;
	Context context;
	Rect dst,src;
	
	// Puzzle drawing area (not this image)
	Rect rect;
	// n Mass * n Mass
	int n;
	
	//MainView
	MainView mainView;
	
	/**
	 * A list of coordinates to be followed
	 */
	ArrayList<Point> positions;
	
	/**
	 * The next coordinates to go
	 */
	int nextIndex;
	
	// Amount of movement per frame
	int moving_per_frame = 12;
	
	// General variable
	Point now,next,d;
	
	public Character(Context context, Point point, Rect rect, int n, int id, MainView mainView){
		this.mainView = mainView;
		this.context = context;
		this.src = new Rect(0,0,140,140);
		this.dst = new Rect();
		this.paint = new Paint(Color.WHITE);
		this.rect = rect;
		this.n  = n;
		
		this.now = new Point();
		this.next = new Point();
		this.d = new Point();
		
		Resources r = context.getResources();
        bmp = BitmapFactory.decodeResource(r, id);
  
        this.setPoint(point);
	}
	
	public void setPoint(Point point){
        int cellWidth  = (rect.right - rect.left) /n;
        int cellHeight = (rect.bottom - rect.top) /n;
        dst.set(
        		rect.left + cellWidth * (point.x-1),
        		rect.top + cellHeight * (point.y-1),
        		rect.left + cellWidth * point.x,
        		rect.top + cellHeight * point.y);
	}
	
	public void setPositon(Point point){
        int cellWidth  = (rect.right - rect.left) /n;
        int cellHeight = (rect.bottom - rect.top) /n;
        dst.set(
        		point.x,
        		point.y,
        		point.x + cellWidth,
        		point.y + cellHeight);
	}
	
	public void setPositions(ArrayList<Point> positions){
		this.positions = positions;
		this.nextIndex = 1;
	}
	
	public void update() {
		
		now.set(dst.left, dst.top);
		next = this.positions.get(nextIndex);
		d.set(next.x - now.x , next.y-now.y);
		if(d.x!=0){
			d.x /= Math.abs(d.x);
			d.x *= this.moving_per_frame;
		}
		if(d.y!=0){
			d.y /= Math.abs(d.y);
			d.y *= this.moving_per_frame;
		}
		now.set(now.x+d.x, now.y+d.y);
		this.setPositon(now);

		if(Math.abs(now.x - next.x) < moving_per_frame &&
		   Math.abs(now.y - next.y) < moving_per_frame){

			setPositon(next);
			nextIndex++;
		}

		if(nextIndex == positions.size()){
			if(mainView.gameStatus ==GameStatus.characterMoving)
				mainView.gameStatus = GameStatus.successDialog;
			else
				mainView.gameStatus = GameStatus.failureDialog;
		}
	}
	
	public void draw(Canvas canvas){
		canvas.drawBitmap(bmp, src, dst, paint);
	}

}
