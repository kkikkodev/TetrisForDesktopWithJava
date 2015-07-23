package com.kkikkodev.desktop.tetris.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.kkikkodev.desktop.tetris.util.ScreenUtil;

public class InputSpeedLevelView extends JDialog {

	private JLabel mjlSetSpeedLevel;
	private JSlider mjsSpeedLevel;
	private JButton mjbStart;

	private int mSpeedLevel;

	public InputSpeedLevelView() {
		initWholeSetting();
		initMembers();
		setEvents();
	}

	private void initWholeSetting() {
		setTitle("TETRIS - START");
		getContentPane().setLayout(null);
		setSize(200, 200);
		setLocation(ScreenUtil.getCenterPosition(this));
		setResizable(false);
	}

	private void initMembers() {
		mjlSetSpeedLevel = new JLabel("Set speed level");
		mjlSetSpeedLevel.setBounds(56, 25, 86, 15);
		getContentPane().add(mjlSetSpeedLevel);
		mjsSpeedLevel = new JSlider();
		mjsSpeedLevel.setMajorTickSpacing(1);
		mjsSpeedLevel.setPaintLabels(true);
		mjsSpeedLevel.setPaintTicks(true);
		mjsSpeedLevel.setPaintTrack(false);
		mjsSpeedLevel.setValue(1);
		mjsSpeedLevel.setMinimum(1);
		mjsSpeedLevel.setMaximum(10);
		mjsSpeedLevel.setBounds(23, 50, 148, 62);
		getContentPane().add(mjsSpeedLevel);
		mjbStart = new JButton("START");
		mjbStart.setBounds(23, 122, 148, 40);
		getContentPane().add(mjbStart);
	}

	private void setEvents() {
		mjsSpeedLevel.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!mjsSpeedLevel.getValueIsAdjusting()) {
					mSpeedLevel = (int) mjsSpeedLevel.getValue();
				}
			}
		});
		mjbStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new TetrisView(mSpeedLevel).setVisible(true);
				dispose();
			}
		});
	}
}
