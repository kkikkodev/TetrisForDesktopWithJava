package com.kkikkodev.desktop.tetris.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.Timer;

import com.kkikkodev.desktop.tetris.constant.Constant;
import com.kkikkodev.desktop.tetris.constant.Constant.BoardType;
import com.kkikkodev.desktop.tetris.constant.Constant.GameStatus;
import com.kkikkodev.desktop.tetris.model.Block;

public class TetrisManager {

	private static final int POSITIONS_SIZE = 4;
	private static final int BOARD_ROW_SIZE = 24;
	private static final int BOARD_COL_SIZE = 14;
	private static final int INITIAL_SPEED = 300;
	private static final int SPEED_LEVEL_OFFSET = 40;
	private static final int LEVEL_UP_CONDITION = 3;
	private static final int LINES_TO_DELETE_HIGHLIGHTING_MILLISECOND = 10;

	private Constant.BoardType mBoard[][];
	private Block mBlock;
	private int mDeletedLineCount;
	private int mSpeedLevel;
	private int mColorIndex = 0; // color array index to highlight deleting
									// lines

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

			if (!(mBoard[x][y] == Constant.BoardType.EMPTY || mBoard[x][y] == Constant.BoardType.MOVING_BLOCK)) {
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
			if (direction == Constant.Direction.UP
					&& checkResult != Constant.BoardType.FIXED_BLOCK) {
				if (checkResult == Constant.BoardType.TOP_WALL) {
					tempDirection = Constant.Direction.DOWN;
					tempCheckResult = Constant.BoardType.TOP_WALL;
				} else if (checkResult == Constant.BoardType.RIGHT_WALL) {
					tempDirection = Constant.Direction.LEFT;
					tempCheckResult = Constant.BoardType.RIGHT_WALL;
				} else if (checkResult == Constant.BoardType.LEFT_WALL) {
					tempDirection = Constant.Direction.RIGHT;
					tempCheckResult = Constant.BoardType.LEFT_WALL;
				}
				do {
					mBlock.move(tempDirection);
				} while (checkValidPosition(direction) == tempCheckResult);
				mBlock.move(direction);
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

	public void processDeletingLines(Graphics g) {
		Color highlightingColors[] = { Color.GRAY, Color.WHITE };
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		searchLineIndexesToDelete(indexes);
		if (indexes.size() > 0) {
			Timer timer = new Timer(LINES_TO_DELETE_HIGHLIGHTING_MILLISECOND,
					new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							highlightLinesToDelete(g,
									highlightingColors[mColorIndex], indexes);
							mColorIndex = 1 - mColorIndex;
						}
					});
			timer.start();
			try {
				Thread.sleep(LINES_TO_DELETE_HIGHLIGHTING_MILLISECOND * 40);
			} catch (InterruptedException e1) {

			}
			timer.stop();
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
		try {
			Thread.sleep(getDownMilliSecond());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void print(Graphics graphics) {
		int x;
		int y = 60;
		for (int i = 0; i < BOARD_ROW_SIZE; i++) {
			x = 30;
			for (int j = 0; j < BOARD_COL_SIZE; j++) {
				switch (mBoard[i][j]) {
				case LEFT_TOP_EDGE:
				case RIGHT_TOP_EDGE:
				case LEFT_BOTTOM_EDGE:
				case RIGHT_BOTTOM_EDGE:
				case LEFT_WALL:
				case RIGHT_WALL:
				case TOP_WALL:
				case BOTTOM_WALL:
					graphics.fill3DRect(x, y, 25, 25, true);
					break;
				case EMPTY:
					break;
				case MOVING_BLOCK:
					graphics.setColor(Constant.COLORS[mBlock.getColor()]);
					graphics.fill3DRect(x, y, 25, 25, true);
					graphics.setColor(Color.BLACK);
					break;
				case FIXED_BLOCK:
					graphics.setColor(Color.GRAY);
					graphics.fill3DRect(x, y, 25, 25, true);
					graphics.setColor(Color.BLACK);
					break;
				}
				x += 25;
			}
			y += 25;
		}
		x = 460;
		y = 150;
		Font font = graphics.getFont();
		graphics.setFont(new Font(font.getName(), Font.BOLD, 20));
		graphics.drawString("[" + mSpeedLevel + " level / " + mDeletedLineCount
				+ " lines]", x, y);
		y += 80;
		graphics.drawString("[Key Description]", x, y);
		y += 30;
		graphics.drawString("←", x, y);
		x = 560;
		graphics.drawString(": move left", x, y);
		x = 460;
		y += 30;
		graphics.drawString("→", x, y);
		x = 560;
		graphics.drawString(": move right", x, y);
		x = 460;
		y += 30;
		graphics.drawString("↓", x, y);
		x = 560;
		graphics.drawString(": move down", x, y);
		x = 460;
		y += 30;
		graphics.drawString("↑", x, y);
		x = 560;
		graphics.drawString(": rotate", x, y);
		x = 460;
		y += 30;
		graphics.drawString("SpaceBar", x, y);
		x = 560;
		graphics.drawString(": direct down", x, y);
		x = 460;
		y += 30;
		graphics.drawString("ESC", x, y);
		x = 560;
		graphics.drawString(": pause", x, y);
		x = 460;
		mBlock.printNext(graphics, x, y + 80);
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

	public long getDownMilliSecond() {
		long milliSecond = INITIAL_SPEED;
		for (int i = Constant.MIN_SPEED_LEVEL; i < mSpeedLevel; i++) {
			if (i < Constant.MAX_SPEED_LEVEL / 2) {
				milliSecond -= SPEED_LEVEL_OFFSET;
			} else {
				milliSecond -= (SPEED_LEVEL_OFFSET / 5);
			}
		}
		return milliSecond;
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

	private void highlightLinesToDelete(Graphics g, Color color,
			ArrayList<Integer> indexes) {
		g.setColor(color);
		int x = 55;
		int y = 60 + indexes.get(0) * 25;
		g.fill3DRect(x, y, 25 * (BOARD_COL_SIZE - 2), 25 * indexes.size(), true);
	}
}
