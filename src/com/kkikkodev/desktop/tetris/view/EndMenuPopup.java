package com.kkikkodev.desktop.tetris.view;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.WindowConstants;

import com.kkikkodev.desktop.tetris.util.ScreenUtil;

public class EndMenuPopup extends JDialog {

	private JButton mjbRanking;
	private JButton mjbMainMenu;
	private JButton mjbExit;

	public EndMenuPopup() {
		initWholeSetting();
		initMembers();
		setEvents();
	}

	private void initWholeSetting() {
		setTitle("TETRIS - END MENU");
		setModal(true);
		setLayout(null);
		setSize(500, 400);
		setLocation(ScreenUtil.getCenterPosition(this));
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	private void initMembers() {
		mjbRanking = new JButton("R A N K I N G");
		mjbRanking.setBounds(150, 220, 200, 30);
		add(mjbRanking);
		mjbMainMenu = new JButton("M A I N M E N U");
		mjbMainMenu.setBounds(150, 250, 200, 30);
		add(mjbMainMenu);
		mjbExit = new JButton("E X I T");
		mjbExit.setBounds(150, 280, 200, 30);
		add(mjbExit);
	}

	private void setEvents() {
		mjbRanking.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		mjbMainMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new MainMenuPopup().setVisible(true);
			}
		});
		mjbExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		// T
		g.fill3DRect(145, 90, 20, 20, true);
		g.fill3DRect(165, 90, 20, 20, true);
		g.fill3DRect(185, 90, 20, 20, true);
		g.fill3DRect(165, 110, 20, 20, true);
		g.fill3DRect(165, 130, 20, 20, true);
		g.fill3DRect(165, 150, 20, 20, true);
		g.fill3DRect(165, 170, 20, 20, true);

		// _
		g.fill3DRect(225, 170, 20, 20, true);
		g.fill3DRect(245, 170, 20, 20, true);
		g.fill3DRect(265, 170, 20, 20, true);

		// T
		g.fill3DRect(305, 90, 20, 20, true);
		g.fill3DRect(325, 90, 20, 20, true);
		g.fill3DRect(345, 90, 20, 20, true);
		g.fill3DRect(325, 110, 20, 20, true);
		g.fill3DRect(325, 130, 20, 20, true);
		g.fill3DRect(325, 150, 20, 20, true);
		g.fill3DRect(325, 170, 20, 20, true);
	}
}
