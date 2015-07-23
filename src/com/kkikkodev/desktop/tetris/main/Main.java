package com.kkikkodev.desktop.tetris.main;

import java.awt.AWTException;

import com.kkikkodev.desktop.tetris.view.InputSpeedLevelView;

public class Main {
	
	public static void main(String[] args) throws AWTException {
		new InputSpeedLevelView().setVisible(true);
	}
}
