package com.alienpants.puzzlepipes.datamodels;

/**
 * Current game state
 */
public enum GameStatus {
	/**
	 * title screen
	 */
	title,

	/**
	 * During game play
	 */
	playing,

	/**
	 * Image movement state after mGoal
	 */
	characterMoving,

	/**
	 * Dialog after mGoal
	 */
	successDialog,

	/**
	 * Moving state of the image after the pitfall
	 */
	characterMovingToHole,

	/**
	 * Dialog after the pitfall
	 */
	failureDialog
}
