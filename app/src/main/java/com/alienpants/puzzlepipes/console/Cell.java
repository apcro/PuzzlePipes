package com.alienpants.puzzlepipes.console;

import java.util.Random;

/**
 *
 * It is one cell.
 */
public class Cell {
	public Boolean mUp, mDown, mLeft, mRight;
	
	/**
	 * @param up The upward direction is valid
	 * @param right Is mRight direction valid?
	 * @param down Downward direction is valid
	 * @param left Is the mLeft direction valid?
	 */
	public Cell(boolean up, boolean right, boolean down, boolean left) {
		this.mUp = up;
		this.mDown = down;
		this.mLeft = left;
		this.mRight = right;
	}
	
	/**
	 * All false
	 */
	public Cell() {
		this.mUp = false;
		this.mDown = false;
		this.mLeft = false;
		this.mRight = false;
	}
	
	/**
	 * Make this cell a pitfall.
	 * Pitfalls are all true squares
	 */
	public void setHole() {
		this.mUp = true;
		this.mDown = true;
		this.mLeft = true;
		this.mRight = true;
	}
	
	/**
	 * Whether this cell is a pitfall.
	 * (Whether this cell is all true or not)
	 * @return
	 */
	public boolean isHole(){
		return (mUp && mDown && mRight && mLeft);
	}
	
	/**
	 * Give the cell a random direction. (However, it can be false at all.)
	 * @param gameNumber What game is it?
	 */
	public void setRandom(int gameNumber) {
		
		// There is no change in the degree of difficulty after the 8th game
		if (gameNumber >= 8) {
            gameNumber = 8;
        }
		
		Random rand = new Random();
		int n = 4 + gameNumber/2;
		int x = rand.nextInt(n);
		switch(x) {
            case 0:
                mUp = true;
                mDown = true;
                break;
            case 1:
                mUp = true;
                mRight = true;
                break;
            case 2:
                mRight = true;
                mLeft = true;
                break;
            case 3:
                mUp = true;
                mLeft = true;
                break;
            case 4:
                mRight = true;
                mDown = true;
                break;
            case 5:
                mDown = true;
                mLeft = true;
                break;
            default:
                mRight = false;
                mLeft = false;
                mUp = false;
                mDown =false;
                break;
		}
	}
	
	/**
	 * @return Convert to int type. What you can not do is -1
	 */
	public int toInt() {
		if (mDown && mUp && mLeft && mRight) {
            return 7;
        } else if (mUp && mRight) {
            return 0;
        } else if (mUp && mDown) {
            return 1;
        } else if (mUp && mLeft) {
            return 2;
        } else if (mRight && mDown) {
            return 3;
        } else if (mRight && mLeft) {
            return 4;
        } else if (mDown && mLeft) {
            return 5;
        } else if(!mDown &&!mUp &&!mLeft &&!mRight) {
            return 6;
        } else {
            return -1;
        }
	}
	
	/**
	 * @param direction direction
	 */
	public void set(Direction direction) {
		if (direction == Direction.up) {
            mUp = true;
        } else if (direction == Direction.down) {
            mDown = true;
        } else if (direction == Direction.right) {
            mRight = true;
        } else if (direction == Direction.left) {
            mLeft = true;
        }
	}
	
	/**
	 * @return Is it all false?
	 */
	public boolean isAllFalse() {
		return (!mUp && !mDown && !mRight && !mLeft);
	}
	
	/**
	 * Whether it is a straight line or not
	 * @return
	 */
	public boolean isStraight() {
		return (mUp && mDown || mLeft && mRight);
	}
}