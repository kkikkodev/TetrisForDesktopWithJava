package com.kkikkodev.desktop.tetris.util;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;

public class ScreenUtil {

	public static Point getCenterPosition(Window window) {
		Dimension wholeScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension currentWindowSize = window.getSize();
		int left = (wholeScreenSize.width / 2) - (currentWindowSize.width / 2);
		int top = (wholeScreenSize.height / 2) - (currentWindowSize.height / 2);
		return new Point(left, top);
	}
}
