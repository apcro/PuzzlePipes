package com.alienpants.puzzlepipes.console;

import java.util.*;

import android.graphics.Point;

public class Puzzle {
	private Point mMax;
	public Point mStart, mGoal;
	
	// Do not change from other than Puzzle (), move ().
	public Cell[][] mCells;
	
	private int[][] mRoute;
	
	// General variable
	private Point mPoint;
	
	/**
	 * Create a board face of the puzzle. It is guaranteed to be completed, but it has not been completed.
	 * @param max The size of the board surface (one size larger)
	 * @param mode Which mode will you make the board face?
	 * @param gameNumber What game is now?
	 * 1: Automatically determine the mStart and the position of the mGoal from above and below.
	 */
	public Puzzle(int max, int mode, int gameNumber) {
		this.mMax = new Point();
		this.mRoute = new int[max][max];
		this.mPoint = new Point();
		this.mStart = new Point();
		this.mGoal = new Point();
		
		this.init(max, mode, gameNumber);
	}
	
	/**
	 * Use this for initializing puzzles for the second time or later.
	 * Create a board face of the puzzle. It is guaranteed to be completed, but it has not been completed.
	 * @param max The size of the board surface (one size larger)
	 * @param mode Which mode will you make the board face?
	 * 1: Automatically determine the mStart and the position of the mGoal from above and below.
	 * @param gameNumber What game is now?
	 */
	public void init(int max, int mode, int gameNumber) {
		this.mMax.set(max, max);
		
		switch(mode) {
            case 1:
                int n = max-2;
                Random rand = new Random();
                mStart.set(rand.nextInt(n)+1, 0);
                mGoal.set(rand.nextInt(n)+1, n+1);
                mCells = this.makeRandomCells(gameNumber);
                break;
		}
		
		// Set the mDifficulty level
		this.setDifficulty();
	}
	
	/**
	 * Move mCells by parameter.
	 * @param r The number of rows or lines
	 * @param d Direction to move
	 */
	public void move(int r, Direction d){
		this.move(this.mCells, r, d);
	}
	
	/**
	 * Move mCells by parameter.
	 * @param cells cell gathering
	 * @param r The number of rows or lines
	 * @param d Direction to move
	 */
	private void move(Cell[][] cells, int r, Direction d) {
		Cell c;
		if (d == Direction.down) {
			c = cells[r][mMax.y-2];
			for (int y = mMax.y-3; y >= 1; y--) {
                cells[r][y + 1] = cells[r][y];
            }
			cells[r][1] = c;
		} else if(d == Direction.up) {
			c = cells[r][1];
			for (int y = 2; y<= mMax.y-2; y++) {
                cells[r][y - 1] = cells[r][y];
            }
			cells[r][mMax.y-2] = c;
		} else if (d == Direction.right) {
			c = cells[mMax.x-2][r];
			for(int x = mMax.x-3; x>=1; x--) {
                cells[x + 1][r] = cells[x][r];
            }
			cells[1][r] = c;
		} else if(d == Direction.left) {
			c = cells[1][r];
			for(int x = 2; x<= mMax.x-2; x++) {
                cells[x - 1][r] = cells[x][r];
            }
			cells[mMax.x-2][r] = c;
		}
	}

	/**
	 * Whether the puzzle is finished or not
	 */
	public boolean isComplete(){
		return this.isComplete(mCells);
	}
	
	
	/**
	 * Whether the puzzle is completed or not
	 * @param cells Set of mCells
	 */
	private boolean isComplete(Cell[][] cells) {
		int[][] a = checkRoute(cells, mStart, mGoal);
		return a[mStart.x][mStart.y] != 0 && a[mGoal.x][mGoal.y] != 0;
	}
	
	/**
	 * Whether it is connected with a pitfall.
	 * @return
	 */
	public boolean isRouteHole() {
		return isRouteHole(mCells);
	}
	/**
	 * Whether it is connected with a pitfall.
	 * @return
	 */
	private boolean isRouteHole(Cell[][] cells) {
		// Pitfall location
		Point hole = this.getHolePoint(cells);
		if(hole==null)
			return false;
		
		int[][] a = checkRoute(cells, mStart,hole);
		return a[mStart.x][mStart.y] != 0 && a[hole.x][hole.y] != 0;
	}
	
	/*
	 * I return the one of the pitfalls.
	 */
	public Point getHolePoint(){
		return getHolePoint(mCells);
	}
	
	/*
	 * I return the one of the pitfalls.
	 */
	private Point getHolePoint(Cell[][] cells) {
		// Pitfall location
		Point hole = null;
		for(int x = 0; x< mMax.x; x++) {
            for (int y = 0; y < mMax.y; y++) {
                if (cells[x][y].isHole()) {
                    hole = new Point(x, y);
                }
            }
        }
		return hole;
	}
		
	/**
	 * Ordered to the mRoute connected from the mStart.
	 * @param cells Set of mCells
	 * @param s Starting point
	 * @param g Goal spot
	 * @return Rounded routes numbered 1 and 2. (0 where it does not pass)
	 */
	public int[][] checkRoute(Cell[][] cells, Point s, Point g) {
		int i = 1;
		for(int x = 0; x< mMax.x; x++) {
            for (int y = 0; y < mMax.y; y++) {
                mRoute[x][y] = 0;
            }
        }
		mPoint.set(s.x, s.y);
		while(true) {
			mRoute[mPoint.x][mPoint.y] = i++;
			
			// Reach the mGoal
			if (mPoint.equals(g)) {
				break;
			}
			// Pitfall
			else if (cells[mPoint.x][mPoint.y].isHole()) {
				break;
			}
			else if (mPoint.x==0) {
				// Move to the mRight
				if (cells[mPoint.x+1][mPoint.y].mLeft) {
					mPoint.x++;
				} else {
					break;
				}
			} else if (mPoint.x== mMax.x-1) {
				// Move to the mLeft
				if (cells[mPoint.x-1][mPoint.y].mRight) {
					mPoint.x--;
				} else {
					break;
				}
			} else if(mPoint.y==0) {
				// Move mDown
				if (cells[mPoint.x][mPoint.y+1].mUp) {
					mPoint.y++;
				} else {
					break;
				}
			} else if (mPoint.y == mMax.y-1) {
					// Move increaseScore
				if (cells[mPoint.x][mPoint.y-1].mDown) {
					mPoint.y--;
				} else {
					break;
				}
			} else if (cells[mPoint.x][mPoint.y].mRight &&
					(cells[mPoint.x+1][mPoint.y].mLeft || mPoint.x+1==g.x && mPoint.y==g.y) &&
					mRoute[mPoint.x+1][mPoint.y] == 0) {
				// Move to the mRight
				mPoint.x++;
			} else if (cells[mPoint.x][mPoint.y].mLeft &&
					(cells[mPoint.x-1][mPoint.y].mRight || mPoint.x-1==g.x && mPoint.y==g.y) &&
					mRoute[mPoint.x-1][mPoint.y] == 0) {
				// Move to the mLeft
				mPoint.x--;
			} else if(cells[mPoint.x][mPoint.y].mUp &&
					(cells[mPoint.x][mPoint.y-1].mDown || mPoint.x==g.x && mPoint.y-1==g.y) &&
					mRoute[mPoint.x][mPoint.y-1] == 0) {
				// Move increaseScore
				mPoint.y--;
			} else if(cells[mPoint.x][mPoint.y].mDown &&
					(cells[mPoint.x][mPoint.y+1].mUp || mPoint.x==g.x && mPoint.y+1==g.y) &&
					mRoute[mPoint.x][mPoint.y+1] == 0) {
				// Move mDown
				mPoint.y++;
			} else {
				break;
			}
			
				
		}
		return mRoute;
	}
	
	/**
	 * @return A group of mCells containing the correct way of traveling, and a mixture of it randomly
	 * @param gameNumber What game is now?
	 */
	private Cell[][] makeRandomCells(int gameNumber) {
		Cell[][] cells = makeCells(gameNumber);
		Random rand = new Random();
		int r;
		Direction d;
		
		// Number of times to mix
		int n = rand.nextInt(20);
		for(int i=0; i<n; i++) {
			if (rand.nextInt(2) == 1) {
				d = Direction.down;
				r = rand.nextInt(mMax.x-2) + 1;
				this.move(cells, r, d);
			} else {
				d = Direction.left;
				r = rand.nextInt(mMax.y-2) + 1;
				this.move(cells, r, d);
			}
		}
		// Prevent sudden success
		while(isComplete(cells) || this.isRouteHole(cells)) {
			if (rand.nextInt(2)==1){
				d = Direction.down;
				r = rand.nextInt(mMax.x-2)+1;
				this.move(cells, r, d);
			} else {
				d = Direction.left;
				r = rand.nextInt(mMax.y-2)+1;
				this.move(cells, r, d);
			}
			
		}
		
		return cells;
	}

	/**
	 * @return A collection of completed mCells, containing some direction.
	 * @param gameNumber What game is now?
	 */
	private Cell[][] makeCells(int gameNumber) {
		Random rand = new Random();
		// Whether you made a pitfall
		boolean hole = false;
		if (gameNumber <= 2 || rand.nextInt()%2 == 0) {
			hole = true;
		}
		
		Cell[][] cells = makeIncompleteCells();
		for(int x = 0; x <= mMax.x-1; x++) {
            for (int y = 0; y <= mMax.y - 1; y++) {
                if (cells[x][y] == null) {
                    Cell cell = new Cell();
                    if (x != 0 && x != mMax.x - 1 && y != 0 && y != mMax.y - 1) {
                        cell.setRandom(gameNumber);
                        if (!hole) {
                            cell.setHole();
                            hole = true;
                        }
                    }
                    cells[x][y] = cell;
                }
            }
        }
		return cells;
	}
	
	/**
	 * @return Collection of mCells with only paths, else null
	 */
	private Cell[][] makeIncompleteCells() {
		Cell[][] cells = new Cell[mMax.x][mMax.y];
		Vector<Point> state = makeState();
		
		for (int i = 1; i <= state.size()-2; i++) {
			Point pivot = state.get(i);
			// Write two directions to the cell
			Cell cell = new Cell();
			Direction d1 = relation(pivot,state.get(i-1));
			cell.set(d1);
			Direction d2 = relation(pivot,state.get(i+1));
			cell.set(d2);
			
			cells[pivot.x][pivot.y] = cell;
		}
		return cells;
	}
	
	/**
	 * @param pivot criteria
	 * @param p Another coordinate
	 * @return Direction of another coordinate relative to the reference, null on error
	 */
	private Direction relation(Point pivot,Point p) {
		if (pivot.y == p.y) {
			if (pivot.x-1==p.x)		return Direction.left;
			else if (pivot.x+1==p.x)	return Direction.right;
		}
		else if (pivot.x==p.x) {
			if (pivot.y-1==p.y)		return Direction.up;
			else if (pivot.y+1==p.y)	return Direction.down;
		}	
		
		return null;	
	}
	
	/**
	 * @return If the set of directions coordinates fail null
	 */
	@SuppressWarnings("unchecked")
	private Vector<Point> makeState() {
		Random random = new Random();
		
		// Exploring the starting state
		Vector<Point> vector = new Vector<Point>();
		vector.add(this.mStart);
		
		// Depth-first search
		ArrayList< Vector<Point> > stack = new ArrayList< Vector<Point> >();
		stack.add(vector);
		
		while(!stack.isEmpty()) {
			// Take one state at random.
			int index = random.nextInt(stack.size());
			Vector<Point> state = stack.get(index);
			stack.remove(index);
			
			// The last coordinate of the state
			Point p = state.lastElement();
			
			// Explored
			if (p.equals(mGoal)) {
                return state;
            }
			
			// New state
			Vector<Point> u = (Vector<Point>) state.clone();
			u.add(new Point(p.x,p.y-1));
			Vector<Point> r = (Vector<Point>) state.clone();
			r.add(new Point(p.x+1,p.y));
			Vector<Point> d = (Vector<Point>) state.clone();
			d.add(new Point(p.x,p.y+1));
			Vector<Point> l = (Vector<Point>) state.clone();
			l.add(new Point(p.x-1,p.y));
			
			if (safe(u)) {
                stack.add(u);
            }
			if (safe(r)) {
                stack.add(r);
            }
			if (safe(d)) {
                stack.add(d);
            }
			if (safe(l)) {
                stack.add(l);
            }
			
		}
			
		// Creation failed
		return null;
	}
	
	/**
	 * @param state state
	 * @return Whether it's safe or not
	 */
	private boolean safe(Vector<Point> state) {
		boolean r = false;
		Point p = state.lastElement();
		state.remove(p);
		
		if (p.x >= 1 && p.y >= 1 && p.x <= mMax.x - 2 && p.y <= mMax.y - 2 && !state.contains(p) || p.equals(mGoal)) {
            r = true;
        }
			
		state.add(p);
		return r;
	}
	
	/**
	 * @param ans answer
	 */
	private void debugAnswer(boolean[][] ans) {
		for (int y = 0; y <= mMax.y - 1; y++) {
			for (int x = 0; x <= mMax.x - 1; x++) {
				if (ans[x][y])
					System.out.print(" * ");
				else 
					System.out.print(" _ ");
			}
			System.out.print("\n");
		}
	}
	
	/**
	 * Calculate the degree of mDifficulty.
	 */
	private void setDifficulty() {
		// Number of all elements
		int n = (this.mMax.x - 2) * (this.mMax.y - 2);

		// Number of blanks
		int noun = 0;

		// Straight
		int straight_v = 0, straight_h = 0;

		// Turned
		int curb = 0;

		// hole
		int hole = 0;
	
		for(int y = 1; y <= mMax.y - 2; y++) {
			for(int x = 1; x <= mMax.x - 2; x++) {
				if (mCells[x][y].isAllFalse()) {
					noun++;
				} else if (mCells[x][y].isHole()) {
					hole++;
				} else if (mCells[x][y].mLeft && mCells[x][y].mRight) {
					straight_v ++;
				} else if (mCells[x][y].mUp && mCells[x][y].mDown) {
					straight_h ++;
				} else {
					curb ++;
				}
			}
		}
	}
	
	/**
	 * @param cells Set of mCells
	 */
	private void debugCells(Cell[][] cells) {
		for(int y = 0; y <= mMax.y - 1; y++){
			for(int x = 0; x <= mMax.x - 1; x++){
				debugCell(cells[x][y]);
				System.out.print(" ");
			}
			System.out.print("\n");
		}
	}
	/**
	 * @param route root
	 */
	public void debugRoute(int[][] route) {
		for(int y = 0; y <= mMax.y - 1; y++) {
			for(int x = 0; x <= mMax.x - 1 ; x++) {
				if(route[x][y]==0)
					System.out.print("_");
				else 
					System.out.print(route[x][y]);
					
			}
			System.out.print("\n");
		}
	}
	/**
	 * @param cell cell
	 */
	private void debugCell(Cell cell) {
		if(cell==null)
			System.out.print("ER");
		else if(cell.isAllFalse())
			System.out.print("**");
		else {
			if (cell.mUp)
				System.out.print("u");
			if (cell.mRight)
				System.out.print("r");
			if (cell.mDown)
				System.out.print("d");
			if (cell.mLeft)
				System.out.print("l");
		}
	}
	
	/**
	 * @param State state
	 */
	private void debugState(Vector<Point> State) {
		for(Point p : State) {
			debugPoint(p);
		}
		System.out.print("\n");
	}
	
	/**
	 * @param p Coordinate
	 */
	private void debugPoint(Point p){
			System.out.print("(" + p.x + "," + p.y + ") ");
	}
}