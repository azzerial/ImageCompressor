/*
 * Copyright 2019 Azzerial
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.azzerial.imagecompressor.core;

import net.azzerial.imagecompressor.components.JFileDrop;
import net.azzerial.imagecompressor.components.JImageViewer;
import net.azzerial.imagecompressor.components.ImagesList;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ApplicationFrame extends JFrame {

	private static final List<Image> icons;
	private static final Dimension minimumSize;
	private static final Dimension size;

	private ImagesList imagesList;
	private JImageViewer previewPanel;
	private JFileDrop settingsPanel;

	public ApplicationFrame(String title) throws HeadlessException {
		super(title);

		this.settingsPanel = new JFileDrop(getFileDropListener());
		settingsPanel.setText("Drop image file here");
		settingsPanel.setPreferredSize(new Dimension(10, 200));
		getContentPane().add(settingsPanel, BorderLayout.SOUTH);

		this.previewPanel = new JImageViewer(Color.BLACK);
		previewPanel.setDropTarget(JFileDrop.createDropTarget(getFileDropListener()));

		this.imagesList = new ImagesList(previewPanel);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, imagesList, previewPanel);
		getContentPane().add(splitPane, BorderLayout.CENTER);

		getContentPane().setBackground(new Color(0xC4C4C4));

		pack();

		setIconImages(icons);
		setMinimumSize(minimumSize);
		setSize(size);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setLocationRelativeTo(null);
	}

	private JFileDrop.FileDropListener getFileDropListener() {
		return (files -> {
			BufferedImage image = null;

			if (files.length != 1) {
				settingsPanel.setText("Too many files!", Color.RED);
				return;
			}
			try {
				image = ImageIO.read(files[0]);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (image == null) {
				settingsPanel.setText("Uploaded file isn't an image!", Color.RED);
				return;
			}
			settingsPanel.setText("Drop image file here");
			if (imagesList.addElement(new ImagesList.ImageElement(files[0], image)))
				previewPanel.setImage(image);
			else
				imagesList.loadElement(files[0]);
		});
	}

	static {
		icons = new ArrayList<>();
		icons.add(Toolkit.getDefaultToolkit().getImage(ApplicationFrame.class.getResource("/icons/icon_16x.png")));
		icons.add(Toolkit.getDefaultToolkit().getImage(ApplicationFrame.class.getResource("/icons/icon_32x.png")));
		icons.add(Toolkit.getDefaultToolkit().getImage(ApplicationFrame.class.getResource("/icons/icon_64x.png")));
		icons.add(Toolkit.getDefaultToolkit().getImage(ApplicationFrame.class.getResource("/icons/icon_128x.png")));
		icons.add(Toolkit.getDefaultToolkit().getImage(ApplicationFrame.class.getResource("/icons/icon_256x.png")));
		icons.add(Toolkit.getDefaultToolkit().getImage(ApplicationFrame.class.getResource("/icons/icon_512x.png")));
		minimumSize = new Dimension(1024, 768);
		size = new Dimension(1024, 768);
	}
}
