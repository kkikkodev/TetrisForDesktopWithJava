package com.kkikkodev.desktop.tetris.constant;

public interface Constant {

	public enum GameStatus {
		PLAYING, END
	}

	public interface Direction {
		public static final int SIZE = 4;

		public static final int UP = 0;
		public static final int RIGHT = 1;
		public static final int DOWN = 2;
		public static final int LEFT = 3;
	}

	public enum ProcessType {
		DIRECTION, DIRECT_DOWN, AUTO
	}

	public interface KeyCode {
		public static final int UP = 38;
		public static final int LEFT = 37;
		public static final int RIGHT = 39;
		public static final int DOWN = 40;
		public static final int SPACE_BAR = 32;
	}

	public enum BoardType {
		EMPTY, MOVING_BLOCK, FIXED_BLOCK, LEFT_WALL, RIGHT_WALL, BOTTOM_WALL, TOP_WALL, LEFT_TOP_EDGE, RIGHT_TOP_EDGE, LEFT_BOTTOM_EDGE, RIGHT_BOTTOM_EDGE
	}

	public static final int MAX_SPEED_LEVEL = 10;
	public static final int MIN_SPEED_LEVEL = 1;
}
