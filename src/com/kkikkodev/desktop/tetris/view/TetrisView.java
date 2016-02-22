package com.kkikkodev.desktop.tetris.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

import com.kkikkodev.desktop.tetris.constant.Constant;
import com.kkikkodev.desktop.tetris.controller.TetrisManager;
import com.kkikkodev.desktop.tetris.util.ScreenUtil;

public class TetrisView extends JFrame {

	private final Object mMonitorObject = new Object(); // to pause <-> resume

	private static final String TETRIS_BACKGROUND_MUSIC_FILE_NAME = ".\\res\\tetris_background_music.wav";
	private static final int PROCESS_REACHED_CASE_COUNT = 2;

	private long mCurrentTimeMilliSecond;
	private Clip mSoundClip;
	private TetrisManager mTetrisManager;
	private Constant.ProcessType mProcessType;
	private int mDirection;
	private Constant.GameStatus mGameStatus;
	private boolean mIsKeyPressed;
	private int mProcessReachedCaseCount; // it is used to move left or right at
											// bottom in case of space which you
											// want to move is available

	public TetrisView() {
		initWholeSetting();
		initMembers();
		setEvents();
	}

	public void start() {
		mSoundClip.start();
		mSoundClip.loop(Clip.LOOP_CONTINUOUSLY);
		repaint();
	}

	public void process(Constant.ProcessType processType, int direction) {
		if (processType == Constant.ProcessType.DIRECTION) {
			mTetrisManager.changeBoardByDirection(direction);
		} else if (processType == Constant.ProcessType.DIRECT_DOWN) {
			mTetrisManager.processDirectDown();
		} else if (processType == Constant.ProcessType.AUTO) {
			mTetrisManager.changeBoardByAuto();
		}
		if (mTetrisManager.isReachedToBottom()) {
			if (processType == Constant.ProcessType.DIRECT_DOWN) {
				mProcessReachedCaseCount = 0;
				if (mTetrisManager.processReachedCase() == Constant.GameStatus.END) {
					end();
					new EndMenuPopup().setVisible(true);
					return;
				}
			} else {
				// if you are going to move the block which has bottom wall or
				// bottom fixed block, permit the block to move the direction
				if (mProcessReachedCaseCount == PROCESS_REACHED_CASE_COUNT) {
					if (mTetrisManager.processReachedCase() == Constant.GameStatus.END) {
						end();
						new EndMenuPopup().setVisible(true);
						return;
					}
					mProcessReachedCaseCount = 0;
				} else {
					mProcessReachedCaseCount++;
				}
			}
		}
		repaint();
		mTetrisManager.processDeletingLines(getGraphics());
	}

	public void end() {
		mGameStatus = Constant.GameStatus.END;
		mSoundClip.stop();
		dispose();
	}

	public void pause() {
		mGameStatus = Constant.GameStatus.PAUSE;
		mSoundClip.stop();
		new PauseMenuPopup(this).setVisible(true);
		synchronized (mMonitorObject) {
			mMonitorObject.notify();
		}
		if (mGameStatus != Constant.GameStatus.END) {
			mGameStatus = Constant.GameStatus.PLAYING;
			mSoundClip.start();
			mSoundClip.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}

	@Override
	public void paint(Graphics g) {
		Image buffer = createImage(getWidth(), getHeight());
		Graphics graphics = buffer.getGraphics();
		graphics.setColor(Color.black);
		Font font = graphics.getFont();
		graphics.setFont(new Font(font.getName(), Font.BOLD, 30));
		mTetrisManager.print(graphics);
		g.drawImage(buffer, 0, 0, this);
	}

	private void initWholeSetting() {
		setTitle("TETRIS - ING...");
		getContentPane().setLayout(null);
		setSize(800, 700);
		setLocation(ScreenUtil.getCenterPosition(this));
		setResizable(false);
	}

	private void initMembers() {
		try {
			mSoundClip = AudioSystem.getClip();
			mSoundClip.open(AudioSystem.getAudioInputStream(new File(
					TETRIS_BACKGROUND_MUSIC_FILE_NAME)));
		} catch (LineUnavailableException lue) {
			lue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (UnsupportedAudioFileException uafe) {
			uafe.printStackTrace();
		}
		mTetrisManager = new TetrisManager(Constant.MIN_SPEED_LEVEL);
		new Thread(new Runnable() {

			@Override
			public void run() {
				start();
				mGameStatus = Constant.GameStatus.PLAYING;
				mIsKeyPressed = false;
				mCurrentTimeMilliSecond = System.currentTimeMillis();
				while (mGameStatus != Constant.GameStatus.END) {
					if (mGameStatus == Constant.GameStatus.PAUSE) {
						synchronized (mMonitorObject) {
							try {
								mMonitorObject.wait();
							} catch (InterruptedException e) {

							}
						}
					}
					mProcessType = Constant.ProcessType.AUTO;
					mDirection = Constant.Direction.DOWN;
					while (true) {
						if (mIsKeyPressed) {
							mIsKeyPressed = false;
							break;
						}
						if (!mIsKeyPressed
								&& System.currentTimeMillis()
										- mCurrentTimeMilliSecond > getDownMilliSecond()) {
							mProcessType = Constant.ProcessType.AUTO;
							mDirection = Constant.Direction.DOWN;
							mCurrentTimeMilliSecond = System
									.currentTimeMillis();
							break;
						}
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					process(mProcessType, mDirection);
				}
			}
		}).start();
	}

	private void setEvents() {
		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == Constant.KeyCode.UP) {
					mIsKeyPressed = true;
					mProcessType = Constant.ProcessType.DIRECTION;
					mDirection = Constant.Direction.UP;
				} else if (e.getKeyCode() == Constant.KeyCode.DOWN) {
					mIsKeyPressed = true;
					mProcessType = Constant.ProcessType.DIRECTION;
					mDirection = Constant.Direction.DOWN;
					mCurrentTimeMilliSecond = System.currentTimeMillis();
				} else if (e.getKeyCode() == Constant.KeyCode.LEFT) {
					mIsKeyPressed = true;
					mProcessType = Constant.ProcessType.DIRECTION;
					mDirection = Constant.Direction.LEFT;
				} else if (e.getKeyCode() == Constant.KeyCode.RIGHT) {
					mIsKeyPressed = true;
					mProcessType = Constant.ProcessType.DIRECTION;
					mDirection = Constant.Direction.RIGHT;
				} else if (e.getKeyCode() == Constant.KeyCode.SPACE_BAR) {
					mIsKeyPressed = true;
					mProcessType = Constant.ProcessType.DIRECT_DOWN;
					mCurrentTimeMilliSecond = System.currentTimeMillis();
				} else if (e.getKeyCode() == Constant.KeyCode.ESC) {
					mIsKeyPressed = true;
					mProcessType = Constant.ProcessType.AUTO;
					mCurrentTimeMilliSecond = System.currentTimeMillis();
					pause();
				}
			}
		});
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				end();
			}
		});
	}

	public long getDownMilliSecond() {
		return mTetrisManager.getDownMilliSecond();
	}
}
