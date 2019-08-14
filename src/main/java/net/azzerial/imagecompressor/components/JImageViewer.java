/*
 * Copyright 2019 Azzerial
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.azzerial.imagecompressor.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public final class JImageViewer extends JPanel {

	private final ImageCanvas canvas;
	private final ScaleOptionBar scaleOptionBar;

	public JImageViewer(final Color color) {
		setLayout(new BorderLayout());

		this.canvas = new ImageCanvas(color, ScaleType.SCALE_TO_FIT);
		add(canvas, BorderLayout.CENTER);

		this.scaleOptionBar = new ScaleOptionBar(canvas);
		add(scaleOptionBar, BorderLayout.SOUTH);
	}

	public JImageViewer setImage(BufferedImage image) {
		if (image == null)
			throw new NullPointerException("[JImageViewer] Image can't be null.");
		canvas.setImage(image);
		scaleOptionBar.setEnabled(true);
		return (this);
	}

	private static final class ImageCanvas extends JPanel {

		private final Color color;

		private BufferedImage image;
		private ScaleType scaleType;

		public ImageCanvas(final Color color, final ScaleType scaleType) {
			this.color = color;
			this.scaleType = scaleType;

			setBackground(color);
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);

			if (image == null)
				return;

			int dx = 0;
			int dy = 0;
			int dx2 = 0;
			int dy2 = 0;
			int width, height;
			double scale;

			switch (scaleType) {
				case MANUAL:
					width = image.getWidth();
					height = image.getHeight();
					if (image.getWidth() > getWidth()) {
						width = getWidth();
						height = getWidth() * image.getHeight() / image.getWidth();
					}
					if (image.getHeight() > getHeight()) {
						height = getHeight();
						width = getHeight() * image.getWidth() / image.getHeight();
					}
					dx = (getWidth() - width) / 2;
					dx2 = (getWidth() + width) / 2;
					dy = (getHeight() - height) / 2;
					dy2 = (getHeight() + height) / 2;
					break;
				case FIT_TO_WIDTH:
					height = image.getHeight() * getWidth() / image.getWidth();
					dy = (getHeight() - height) / 2;
					dy2 = (getHeight() + height) / 2;
					dx2 = getWidth();
					break;
				case FIT_TO_HEIGHT:
					width = image.getWidth() * getHeight() / image.getHeight();
					dx = (getWidth() - width) / 2;
					dx2 = (getWidth() + width) / 2;
					dy2 = getHeight();
					break;
				case SCALE_TO_FIT:
					scale = Math.min((getWidth() * 1.0) / (image.getWidth() * 1.0), (getHeight() * 1.0) / (image.getHeight() * 1.0));
					width = (int) ((image.getWidth() * 1.0) * scale);
					height = (int) ((image.getHeight() * 1.0) * scale);
					dx = (getWidth() - width) / 2;
					dx2 = (getWidth() + width) / 2;
					dy = (getHeight() - height) / 2;
					dy2 = (getHeight() + height) / 2;
					break;
				case STRETCH_TO_FIT:
					dx2 = getWidth();
					dy2 = getHeight();
					break;
			}
			g.drawImage(image, dx, dy, dx2, dy2, 0, 0, image.getWidth(), image.getHeight(), color, this);
		}

		public BufferedImage getImage() {
			return (image);
		}

		public ImageCanvas setImage(BufferedImage image) {
			this.image = image;
			repaint();
			return (this);
		}

		public ImageCanvas setScaleType(ScaleType scaleType) {
			this.scaleType = scaleType;
			repaint();
			return (this);
		}
	}

	private static final class ScaleOptionBar extends JMenuBar {

		private final ImageCanvas canvas;
		private final ButtonGroup group;

		public ScaleOptionBar(final ImageCanvas canvas) {
			this.canvas = canvas;
			this.group = new ButtonGroup();

			add(Box.createHorizontalGlue());
			addScaleOption( "Scale to fit", e -> canvas.setScaleType(ScaleType.SCALE_TO_FIT));
			addScaleOption( "Fit to width", e -> canvas.setScaleType(ScaleType.FIT_TO_WIDTH));
			addScaleOption( "Fit to height", e -> canvas.setScaleType(ScaleType.FIT_TO_HEIGHT));
			addScaleOption( "Stretch to fit", e -> canvas.setScaleType(ScaleType.STRETCH_TO_FIT));
			add(Box.createHorizontalGlue());
		}

		private void addScaleOption(String title, ActionListener listener) {
			JToggleButton button = new JToggleButton(title);

			button.addActionListener(listener);
			if (!group.getElements().hasMoreElements())
				button.setSelected(true);
			button.setFocusable(false);
			button.setBackground(Color.LIGHT_GRAY);
			group.add(button);
			add(button);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;

			g2.setColor(canvas.getBackground());
			g2.fillRect(0, 0, getWidth(), getHeight());
		}
	}

	public enum ScaleType {
		FIT_TO_HEIGHT("Fit to height"),
		FIT_TO_WIDTH("Fit to width"),
		MANUAL("Manual"),
		STRETCH_TO_FIT("Stretch to fit"),
		SCALE_TO_FIT("Scale to fit");

		private final String type;

		ScaleType(String type) {
			this.type = type;
		}

		public String getType() {
			return (type);
		}
	}
}
