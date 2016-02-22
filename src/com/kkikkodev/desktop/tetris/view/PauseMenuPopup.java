package com.kkikkodev.desktop.tetris.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;

import com.kkikkodev.desktop.tetris.util.ScreenUtil;

public class PauseMenuPopup extends JDialog {

	private TetrisView tetrisView;

	private JButton mjbResume;
	private JButton mjbMainMenu;

	public PauseMenuPopup(TetrisView tetrisView) {
		initWholeSetting();
		initMembers(tetrisView);
		setEvents();
	}

	private void initWholeSetting() {
		setUndecorated(true);
		setModal(true);
		setLayout(null);
		setSize(200, 60);
		setLocation(ScreenUtil.getCenterPosition(this));
	}

	private void initMembers(TetrisView tetrisView) {
		this.tetrisView = tetrisView;
		mjbResume = new JButton("R E S U M E");
		mjbResume.setBounds(0, 0, 200, 30);
		add(mjbResume);
		mjbMainMenu = new JButton("M A I N M E N U");
		mjbMainMenu.setBounds(0, 30, 200, 30);
		add(mjbMainMenu);
	}

	private void setEvents() {
		mjbResume.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		mjbMainMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				tetrisView.end();
				new MainMenuPopup().setVisible(true);
			}
		});
	}
}
