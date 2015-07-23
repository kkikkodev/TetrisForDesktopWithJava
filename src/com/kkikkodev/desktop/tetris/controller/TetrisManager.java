package com.kkikkodev.desktop.tetris.controller;

import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;

import com.kkikkodev.desktop.tetris.constant.Constant;
import com.kkikkodev.desktop.tetris.constant.Constant.BoardType;
import com.kkikkodev.desktop.tetris.constant.Constant.GameStatus;
import com.kkikkodev.desktop.tetris.model.Block;

public class TetrisManager {

	private static final int POSITIONS_SIZE = 4;
	private static final int BOARD_ROW_SIZE = 20;
	private static final int BOARD_COL_SIZE = 14;
	private static final int INITIAL_SPEED = 500;
	private static final int SPEED_LEVEL_OFFSET = 90;
	private static final int LEVEL_UP_CONDITION = 3;

	private Constant.BoardType mBoard[][];
	private Block mBlock;
	private int mDeletedLineCount;
	private int mSpeedLevel;

	public TetrisManager(int speedLevel) {
		mBoard = new Constant.BoardType[BOARD_ROW_SIZE][BOARD_COL_SIZE];
		for (int i = 0; i < BOARD_ROW_SIZE; i++) {
			Arrays.fill(mBoard[i], BoardType.EMPTY);
		}
		clearBoard();
		mBlock = new Block(null);
		mDeletedLineCount = 0;
		mSpeedLevel = speedLevel;
	}

	public Constant.BoardType checkValidPosition(int direction) {
		Block temp = new Block();
		temp.copyOf(mBlock);
		temp.move(direction);
		for (int i = 0; i < POSITIONS_SIZE; i++) {
			int x = temp.getPositions()[temp.getDirection()][i].x;
			int y = temp.getPositions()[temp.getDirection()][i].y;

			// if press space bar key at top, block cannot rotate
			if (x <= 0) {
				return Constant.BoardType.TOP_WALL;
			}

			if (mBoard[x][y] != Constant.BoardType.EMPTY
					&& mBoard[x][y] != Constant.BoardType.MOVING_BLOCK) {
				return mBoard[x][y];
			}
		}
		return Constant.BoardType.EMPTY;
	}

	public void changeBoardByDirection(int direction) {
		int tempDirection = Constant.Direction.DOWN;
		Constant.BoardType tempCheckResult = Constant.BoardType.EMPTY;
		clearBoard();
		Constant.BoardType checkResult = checkValidPosition(direction);
		if (checkResult == Constant.BoardType.EMPTY) {
			mBlock.move(direction);
		} else {
			if (direction == Constant.Direction.UP) {
				switch (checkResult) {
				case TOP_WALL:
					tempDirection = Constant.Direction.DOWN;
					tempCheckResult = Constant.BoardType.TOP_WALL;
					break;
				case RIGHT_WALL:
					tempDirection = Constant.Direction.LEFT;
					tempCheckResult = Constant.BoardType.RIGHT_WALL;
					break;
				case LEFT_WALL:
					tempDirection = Constant.Direction.RIGHT;
					tempCheckResult = Constant.BoardType.LEFT_WALL;
					break;
				}
				do {
					mBlock.move(tempDirection);
				} while (checkValidPosition(direction) == tempCheckResult);
				mBlock.move(direction);
			} else {
				if (direction == Constant.Direction.RIGHT
						&& checkResult == Constant.BoardType.RIGHT_WALL
						|| direction == Constant.Direction.LEFT
						&& checkResult == Constant.BoardType.LEFT_WALL
						|| direction == Constant.Direction.RIGHT
						&& checkResult == Constant.BoardType.FIXED_BLOCK
						|| direction == Constant.Direction.LEFT
						&& checkResult == Constant.BoardType.FIXED_BLOCK) {
					sleep();
					mBlock.move(Constant.Direction.DOWN);
				}
			}
		}
		changeBoardByStatus(Constant.BoardType.MOVING_BLOCK);
	}

	public void changeBoardByAuto() {
		changeBoardByDirection(Constant.Direction.DOWN);
	}

	public void processDirectDown() {
		while (!isReachedToBottom()) {
			changeBoardByDirection(Constant.Direction.DOWN);
		}
	}

	public void processDeletingLines() {
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		searchLineIndexesToDelete(indexes);
		if (indexes.size() > 0) {
			deleteLines(indexes);
			for (int i = mSpeedLevel; i <= mDeletedLineCount
					/ LEVEL_UP_CONDITION; i++) {
				upSpeedLevel();
			}
		}
	}

	public boolean isReachedToBottom() {
		for (int i = 0; i < POSITIONS_SIZE; i++) {
			int x = mBlock.getPositions()[mBlock.getDirection()][i].x;
			int y = mBlock.getPositions()[mBlock.getDirection()][i].y;
			if (mBoard[x + 1][y] != Constant.BoardType.EMPTY
					&& mBoard[x + 1][y] != Constant.BoardType.MOVING_BLOCK) {
				return true;
			}
		}
		return false;
	}

	public Constant.GameStatus processReachedCase() {
		changeBoardByStatus(Constant.BoardType.FIXED_BLOCK);
		mBlock = new Block(mBlock);
		if (isReachedToBottom()) {
			return GameStatus.END;
		} else {
			return GameStatus.PLAYING;
		}
	}

	public void sleep() {
		int milliSecond = INITIAL_SPEED;
		for (int i = Constant.MIN_SPEED_LEVEL; i < mSpeedLevel; i++) {
			if (i < Constant.MAX_SPEED_LEVEL / 2) {
				milliSecond -= SPEED_LEVEL_OFFSET;
			} else {
				milliSecond -= (SPEED_LEVEL_OFFSET / 5);
			}
		}
		try {
			Thread.sleep(milliSecond);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void print(Graphics graphics) {
		int x;
		int y = 56;
		for (int i = 0; i < BOARD_ROW_SIZE; i++) {
			x = 6;
			for (int j = 0; j < BOARD_COL_SIZE; j++) {
				switch (mBoard[i][j]) {
				case LEFT_TOP_EDGE:
					graphics.drawString("¦Ç", x, y);
					break;
				case RIGHT_TOP_EDGE:
					graphics.drawString("¦Á", x, y);
					break;
				case LEFT_BOTTOM_EDGE:
					graphics.drawString("¦Å", x, y);
					break;
				case RIGHT_BOTTOM_EDGE:
					graphics.drawString("¦Ã", x, y);
					break;
				case EMPTY:
					graphics.drawString("  ", x, y);
					break;
				case MOVING_BLOCK:
					graphics.drawString("¡á", x, y);
					break;
				case FIXED_BLOCK:
					graphics.drawString("¢Ê", x, y);
					break;
				case LEFT_WALL:
				case RIGHT_WALL:
					graphics.drawString("£ü", x, y);
					break;
				case TOP_WALL:
				case BOTTOM_WALL:
					graphics.drawString("¡ª", x, y);
					break;
				}
				x += 28;
			}
			y += 28;
		}
		x = 460;
		y = 180;
		Font font = graphics.getFont();
		graphics.setFont(new Font(font.getName(), Font.BOLD, 20));
		graphics.drawString("          ***** Tetris *****", x, y);
		y += 50;
		graphics.drawString("Current speed level : " + mSpeedLevel + " level",
				x, y);
		y += 50;
		graphics.drawString("Deleted lines : " + mDeletedLineCount + " lines",
				x, y);
		mBlock.printNext(graphics, x, y + 100);
	}

	public void setBoard(Constant.BoardType[][] board) {
		mBoard = board;
	}

	public Constant.BoardType[][] getBoard() {
		return mBoard;
	}

	public void setBlock(Block block) {
		mBlock = block;
	}

	public Block getBlock() {
		return mBlock;
	}

	public void setDeletedLineCount(int deletedLineCount) {
		mDeletedLineCount = deletedLineCount;
	}

	public int getDeletedLineCount() {
		return mDeletedLineCount;
	}

	public void setSpeedLevel(int speedLevel) {
		mSpeedLevel = speedLevel;
	}

	public int getSpeedLevel() {
		return mSpeedLevel;
	}

	private void clearBoard() {
		for (int i = 0; i < BOARD_ROW_SIZE; i++) {
			mBoard[i][0] = Constant.BoardType.LEFT_WALL;
			mBoard[i][BOARD_COL_SIZE - 1] = Constant.BoardType.RIGHT_WALL;
		}
		for (int i = 0; i < BOARD_COL_SIZE; i++) {
			mBoard[0][i] = Constant.BoardType.TOP_WALL;
			mBoard[BOARD_ROW_SIZE - 1][i] = Constant.BoardType.BOTTOM_WALL;
		}
		for (int i = 1; i < BOARD_ROW_SIZE - 1; i++) {
			for (int j = 1; j < BOARD_COL_SIZE - 1; j++) {
				if (mBoard[i][j] != Constant.BoardType.FIXED_BLOCK) {
					mBoard[i][j] = Constant.BoardType.EMPTY;
				}
			}
		}
		mBoard[0][0] = Constant.BoardType.LEFT_TOP_EDGE;
		mBoard[0][BOARD_COL_SIZE - 1] = Constant.BoardType.RIGHT_TOP_EDGE;
		mBoard[BOARD_ROW_SIZE - 1][0] = Constant.BoardType.LEFT_BOTTOM_EDGE;
		mBoard[BOARD_ROW_SIZE - 1][BOARD_COL_SIZE - 1] = Constant.BoardType.RIGHT_BOTTOM_EDGE;
	}

	private void changeBoardByStatus(Constant.BoardType status) {
		for (int i = 0; i < POSITIONS_SIZE; i++) {
			int x = mBlock.getPositions()[mBlock.getDirection()][i].x;
			int y = mBlock.getPositions()[mBlock.getDirection()][i].y;
			mBoard[x][y] = status;
		}
	}

	private void upSpeedLevel() {
		if (mSpeedLevel < Constant.MAX_SPEED_LEVEL) {
			mSpeedLevel++;
		}
	}

	private void searchLineIndexesToDelete(ArrayList<Integer> indexes) {
		indexes.clear();
		for (int i = 1; i < BOARD_ROW_SIZE - 1; i++) {
			boolean toDelete = true;
			for (int j = 1; j < BOARD_COL_SIZE - 1; j++) {
				if (mBoard[i][j] != Constant.BoardType.FIXED_BLOCK) {
					toDelete = false;
					break;
				}
			}
			if (toDelete) {
				indexes.add(i);
			}
		}
	}

	private void deleteLines(ArrayList<Integer> indexes) {
		int k = BOARD_ROW_SIZE - 2;
		Constant.BoardType[][] temp = new Constant.BoardType[BOARD_ROW_SIZE][BOARD_COL_SIZE];
		for (int i = 0; i < BOARD_ROW_SIZE; i++) {
			Arrays.fill(temp[i], Constant.BoardType.EMPTY);
		}
		for (int i = BOARD_ROW_SIZE - 2; i > 0; i--) {
			boolean toDelete = false;
			for (int j = 0; j < indexes.size(); j++) {
				if (i == indexes.get(j)) {
					toDelete = true;
					break;
				}
			}
			if (!toDelete) {
				for (int j = 0; j < BOARD_COL_SIZE; j++) {
					temp[k][j] = mBoard[i][j];
				}
				k--;
			}
		}
		for (int i = 1; i < BOARD_ROW_SIZE - 1; i++) {
			for (int j = 1; j < BOARD_COL_SIZE - 1; j++) {
				mBoard[i][j] = temp[i][j];
			}
		}
		mDeletedLineCount += indexes.size();
	}
}
