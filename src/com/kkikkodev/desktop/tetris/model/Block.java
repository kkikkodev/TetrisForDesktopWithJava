package com.kkikkodev.desktop.tetris.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;

import com.kkikkodev.desktop.tetris.constant.Constant;

public class Block {

	private static final int BLOCK_EXAMPLES_SIZE = 7;
	private static final int POSITIONS_SIZE = 4;
	private static final Point BLOCK_EXAMPLES[][][] = {
			{
					{ new Point(0, 5), new Point(0, 6), new Point(0, 7),
							new Point(0, 8) },
					{ new Point(-1, 6), new Point(0, 6), new Point(1, 6),
							new Point(2, 6) },
					{ new Point(0, 5), new Point(0, 6), new Point(0, 7),
							new Point(0, 8) },
					{ new Point(-1, 6), new Point(0, 6), new Point(1, 6),
							new Point(2, 6) } },
			{
					{ new Point(0, 8), new Point(1, 6), new Point(1, 7),
							new Point(1, 8) },
					{ new Point(-1, 7), new Point(0, 7), new Point(1, 7),
							new Point(1, 8) },
					{ new Point(0, 6), new Point(0, 7), new Point(0, 8),
							new Point(1, 6) },
					{ new Point(-1, 6), new Point(-1, 7), new Point(0, 7),
							new Point(1, 7) } },
			{
					{ new Point(0, 7), new Point(0, 8), new Point(1, 6),
							new Point(1, 7) },
					{ new Point(-1, 6), new Point(0, 6), new Point(0, 7),
							new Point(1, 7) },
					{ new Point(0, 7), new Point(0, 8), new Point(1, 6),
							new Point(1, 7) },
					{ new Point(-1, 6), new Point(0, 6), new Point(0, 7),
							new Point(1, 7) } },
			{
					{ new Point(0, 6), new Point(0, 7), new Point(1, 7),
							new Point(1, 8) },
					{ new Point(-1, 8), new Point(0, 8), new Point(0, 7),
							new Point(1, 7) },
					{ new Point(0, 6), new Point(0, 7), new Point(1, 7),
							new Point(1, 8) },
					{ new Point(-1, 8), new Point(0, 8), new Point(0, 7),
							new Point(1, 7) } },
			{
					{ new Point(0, 7), new Point(1, 6), new Point(1, 7),
							new Point(1, 8) },
					{ new Point(-1, 7), new Point(0, 7), new Point(0, 8),
							new Point(1, 7) },
					{ new Point(0, 6), new Point(0, 7), new Point(0, 8),
							new Point(1, 7) },
					{ new Point(-1, 7), new Point(0, 6), new Point(0, 7),
							new Point(1, 7) } },
			{
					{ new Point(0, 6), new Point(1, 6), new Point(1, 7),
							new Point(1, 8) },
					{ new Point(-1, 8), new Point(-1, 7), new Point(0, 7),
							new Point(1, 7) },
					{ new Point(0, 6), new Point(0, 7), new Point(0, 8),
							new Point(1, 8) },
					{ new Point(-1, 7), new Point(0, 7), new Point(1, 7),
							new Point(1, 6) } },
			{
					{ new Point(0, 6), new Point(0, 7), new Point(1, 6),
							new Point(1, 7) },
					{ new Point(0, 6), new Point(0, 7), new Point(1, 6),
							new Point(1, 7) },
					{ new Point(0, 6), new Point(0, 7), new Point(1, 6),
							new Point(1, 7) },
					{ new Point(0, 6), new Point(0, 7), new Point(1, 6),
							new Point(1, 7) } } };
	private Point mPositions[][];
	private int mCurrent;
	private int mNext;
	private int mDirection;
	private int mColor;

	public Block() {
		mPositions = new Point[POSITIONS_SIZE][POSITIONS_SIZE];
		mCurrent = 0;
		mNext = 0;
		mDirection = Constant.Direction.UP;
		mColor = 0;
	}

	public Block(Block block) {
		if (block == null) {
			mCurrent = new Random().nextInt(BLOCK_EXAMPLES_SIZE);
		} else {
			mCurrent = block.getNext();
		}
		mPositions = new Point[POSITIONS_SIZE][POSITIONS_SIZE];
		for (int i = 0; i < POSITIONS_SIZE; i++) {
			for (int j = 0; j < POSITIONS_SIZE; j++) {
				mPositions[i][j] = new Point(BLOCK_EXAMPLES[mCurrent][i][j]);
			}
		}
		mNext = new Random().nextInt(BLOCK_EXAMPLES_SIZE);
		mDirection = Constant.Direction.UP;
		mColor = new Random().nextInt(Constant.COLORS.length);
	}

	public void copyOf(Block src) {
		for (int i = 0; i < POSITIONS_SIZE; i++) {
			for (int j = 0; j < POSITIONS_SIZE; j++) {
				mPositions[i][j] = new Point(src.getPositions()[i][j]);
			}
		}
		mCurrent = src.getCurrent();
		mNext = src.getNext();
		mDirection = src.getDirection();
	}

	public void move(int direction) {
		switch (direction) {
		case Constant.Direction.LEFT:
			moveToLeft();
			break;
		case Constant.Direction.RIGHT:
			moveToRight();
			break;
		case Constant.Direction.DOWN:
			moveToDown();
			break;
		case Constant.Direction.UP:
			rotateRight();
			break;
		}
	}

	public void printNext(Graphics graphics, int x, int y) {
		graphics.drawString("[Next block]", x, y);
		y += 30;
		graphics.setColor(Color.LIGHT_GRAY);
		switch (mNext) {
		case 0:
			graphics.fill3DRect(x, y, 20, 20, true);
			graphics.fill3DRect(x + 20, y, 20, 20, true);
			graphics.fill3DRect(x + 40, y, 20, 20, true);
			graphics.fill3DRect(x + 60, y, 20, 20, true);
			break;
		case 1:
			graphics.fill3DRect(x + 40, y, 20, 20, true);
			graphics.fill3DRect(x, y + 20, 20, 20, true);
			graphics.fill3DRect(x + 20, y + 20, 20, 20, true);
			graphics.fill3DRect(x + 40, y + 20, 20, 20, true);
			break;
		case 2:
			graphics.fill3DRect(x + 20, y, 20, 20, true);
			graphics.fill3DRect(x + 40, y, 20, 20, true);
			graphics.fill3DRect(x, y + 20, 20, 20, true);
			graphics.fill3DRect(x + 20, y + 20, 20, 20, true);
			break;
		case 3:
			graphics.fill3DRect(x, y, 20, 20, true);
			graphics.fill3DRect(x + 20, y, 20, 20, true);
			graphics.fill3DRect(x + 20, y + 20, 20, 20, true);
			graphics.fill3DRect(x + 40, y + 20, 20, 20, true);
			break;
		case 4:
			graphics.fill3DRect(x + 20, y, 20, 20, true);
			graphics.fill3DRect(x, y + 20, 20, 20, true);
			graphics.fill3DRect(x + 20, y + 20, 20, 20, true);
			graphics.fill3DRect(x + 40, y + 20, 20, 20, true);
			break;
		case 5:
			graphics.fill3DRect(x, y, 20, 20, true);
			graphics.fill3DRect(x, y + 20, 20, 20, true);
			graphics.fill3DRect(x + 20, y + 20, 20, 20, true);
			graphics.fill3DRect(x + 40, y + 20, 20, 20, true);
			break;
		case 6:
			graphics.fill3DRect(x, y, 20, 20, true);
			graphics.fill3DRect(x + 20, y, 20, 20, true);
			graphics.fill3DRect(x, y + 20, 20, 20, true);
			graphics.fill3DRect(x + 20, y + 20, 20, 20, true);
			break;
		}
		graphics.setColor(Color.BLACK);
	}

	public void setPositions(Point[][] positions) {
		mPositions = positions;
	}

	public Point[][] getPositions() {
		return mPositions;
	}

	public void setCurrent(int current) {
		mCurrent = current;
	}

	public int getCurrent() {
		return mCurrent;
	}

	public void setNext(int next) {
		mNext = next;
	}

	public int getNext() {
		return mNext;
	}

	public void setDirection(int direction) {
		mDirection = direction;
	}

	public int getDirection() {
		return mDirection;
	}

	public void setColor(int color) {
		mColor = color;
	}

	public int getColor() {
		return mColor;
	}

	private void moveToDown() {
		for (int i = 0; i < POSITIONS_SIZE; i++) {
			for (int j = 0; j < POSITIONS_SIZE; j++) {
				mPositions[i][j].x++;
			}
		}
	}

	private void moveToLeft() {
		for (int i = 0; i < POSITIONS_SIZE; i++) {
			for (int j = 0; j < POSITIONS_SIZE; j++) {
				mPositions[i][j].y--;
			}
		}
	}

	private void moveToRight() {
		for (int i = 0; i < POSITIONS_SIZE; i++) {
			for (int j = 0; j < POSITIONS_SIZE; j++) {
				mPositions[i][j].y++;
			}
		}
	}

	private void rotateRight() {
		mDirection = (mDirection + 1) % Constant.Direction.SIZE;
	}
}
