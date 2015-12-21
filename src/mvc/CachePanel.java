package mvc;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import memory.Cache;
import memory.CacheConfiguration;
import common.Util;

/**
 * This class builds a panel for cache author: sunbojun
 */

public class CachePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * position of cacheline
	 */
	private int startPosX = 50;
	private int startPosY = 50;

	/**
	 * size of cacheline block
	 */
	private int blockWidth = 200 / CacheConfiguration.CACHELINE_CAPACITY;
	private int blockHeight = 200 / CacheConfiguration.CACHELINE_SIZE;

	private JLabel hitRateInfo;
	private JLabel selectedData;
	private String defaultLabelMessage = "Click the block to show binary value";

	/**
	 * the position of cacheline block
	 */

	private int hoverX = -1;
	private int hoverY = -1;

	/**
	 * use color to show huw much times the cacheline block is used
	 */

	private Color colorUnused = Color.gray;
	private Color colorJustnew = Color.red;
	private int colorDegree = 200 / Cache.Record_IN_USE_JUST_NOW;

	private Color getColorByUseDegree(int use) {
		if (use == Cache.Record_NOT_UESED)
			return colorUnused;
		if (use == Cache.Record_JUST_NEW)
			return colorJustnew;
		return new Color(10, 40 + use * colorDegree, 220 - use * colorDegree);
	}

	private void getHoveredCell(int x, int y) {

		hoverX = (x - startPosX) / blockWidth;
		if (x <= startPosX || hoverX >= CacheConfiguration.CACHELINE_CAPACITY)
			hoverX = -1;

		hoverY = (y - startPosY) / blockHeight;
		if (y <= startPosY || hoverY >= CacheConfiguration.CACHELINE_SIZE)
			hoverY = -1;

	}

	public CachePanel() {
		super();

		TitledBorder nameTitle = new TitledBorder("Cache");
		this.setBorder(nameTitle);

		this.setLayout(null);

		hitRateInfo = new JLabel("HitRate Info");
		hitRateInfo.setFont(getFont().deriveFont(12f));
		this.add(hitRateInfo);
		hitRateInfo.setBounds(10, 250, 250, 20);

		selectedData = new JLabel(defaultLabelMessage);
		selectedData.setFont(getFont().deriveFont(12f));
		this.add(selectedData);
		selectedData.setBounds(10, 17, 250, 20);


		this.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				getHoveredCell(e.getX(), e.getY());
				if (hoverX >= 0 && hoverY >= 0) {
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				} else {
					setCursor(Cursor.getDefaultCursor());
				}
				update();
			}

			@Override
			public void mouseDragged(MouseEvent e) {

			}
		});

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int[][] records = Cache.getInstance().getCacheRecord();

		for (int i = 0; i < CacheConfiguration.CACHELINE_SIZE; i++) {

			g.setColor(Color.BLACK);
			g.setFont(getFont().deriveFont(10f));// //changed from 9.5
			g.drawString("Line " + i, 10, startPosY + (i + 1) * blockHeight - 2);

			for (int j = 0; j < CacheConfiguration.CACHELINE_CAPACITY; j++) {
				g.setColor(getColorByUseDegree(records[i][j]));
				g.fillRect(startPosX + j * blockWidth, startPosY + i
						* blockHeight, blockWidth - 1, blockHeight - 1);
			}

		}

		Graphics2D g2d = (Graphics2D) g;
		Stroke stroke = new BasicStroke(1.5f);
		g2d.setStroke(stroke);

		/*
		 * * set block selected to red
		 */
		g2d.setColor(Color.red);
		if (hoverX >= 0 && hoverY >= 0) {

			g2d.drawRect(startPosX + hoverX * blockWidth - 1, startPosY
					+ hoverY * blockHeight - 1, blockWidth, blockHeight);
		}

	}

	/*
	 * update hitRateInfo and block binary value
	 */

	public void update() {
		this.repaint();

		if (hoverX >= 0 && hoverY >= 0) {
			selectedData.setText("L"
					+ hoverY
					+ "W"
					+ hoverX
					+ ":"
					+ Util.getBinaryStringFromBinaryArray(Cache.getInstance()
							.getWordByLineAndOffset(hoverY, hoverX)
							.getContent()));
		} else {
			selectedData.setText(defaultLabelMessage);
		}

		hitRateInfo.setText(Cache.getInstance().getRateInfo());
	}

}
