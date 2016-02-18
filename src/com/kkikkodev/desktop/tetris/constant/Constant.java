package com.kkikkodev.desktop.tetris.constant;

import java.awt.Color;

public interface Constant {

	public enum GameStatus {
		PLAYING, END, PAUSE
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
		public static final int ESC = 27;
	}

	public enum BoardType {
		EMPTY, MOVING_BLOCK, FIXED_BLOCK, LEFT_WALL, RIGHT_WALL, BOTTOM_WALL, TOP_WALL, LEFT_TOP_EDGE, RIGHT_TOP_EDGE, LEFT_BOTTOM_EDGE, RIGHT_BOTTOM_EDGE
	}

	public interface MainMenu {
		public static final int START = 1;
		public static final int RANKING = 2;
		public static final int SETTING = 3;
		public static final int EXIT = 4;
	}

	public interface PauseMenu {
		public static final int RESUME = 1;
		public static final int MAIN_MENU = 2;
	}

	public interface EndMenu {
		public static final int RANKING = 1;
		public static final int MAIN_MENU = 2;
		public static final int EXIT = 3;
	}

	public static final int MAX_SPEED_LEVEL = 10;
	public static final int MIN_SPEED_LEVEL = 1;

	// rainbow 7 colors : red, orange, yellow, green, blue, indigo, purple
	public static final Color[] COLORS = { Color.RED, Color.ORANGE,
			Color.YELLOW, Color.GREEN, Color.BLUE, Color.decode("#4B0082"),
			Color.decode("#800080") };
}
