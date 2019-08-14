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
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ListUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class ImagesList extends JPanel {

	private final JImageViewer viewer;

	private final DefaultListModel<String> model;
	private final JList<String> list;

	private final List<ImageElement> elements;

	public ImagesList(final JImageViewer viewer) {
		this.viewer = viewer;
		setLayout(new BorderLayout());
		setMinimumSize(new Dimension(250, 10));
		setPreferredSize(new Dimension(250, 10));

		this.elements = new LinkedList<>();
		this.model = new DefaultListModel<>();

		this.list = new JList<>(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.addListSelectionListener(getListSelectionListener());

		JScrollPane listScroller = new JScrollPane(list);
		add(listScroller, BorderLayout.CENTER);
	}

	public boolean addElement(final ImageElement element) {
		for (ImageElement e : elements)
			if (e.getFile().getAbsolutePath().equals(element.getFile().getAbsolutePath()))
				return (false);
		model.addElement(element.getFile().getName());
		elements.add(element);
		list.setSelectedIndex(model.getSize() - 1);
		return (true);
	}

	public void loadElement(final File file) {
		for (int i = 0; i != elements.size(); i += 1)
			if (elements.get(i).getFile().equals(file))
				list.setSelectedIndex(i);
	}

	private ListSelectionListener getListSelectionListener() {
		return (s -> viewer.setImage(elements.get(list.getSelectedIndex()).getImage()));
	}


	public static final class ImageElement {

		private final File file;
		private final BufferedImage image;
		private final List<BufferedImage> generatedImages;

		public ImageElement(final File file, final BufferedImage image) {
			this.file = file;
			this.image = image;
			this.generatedImages = new ArrayList<>();
		}

		public File getFile() {
			return (file);
		}

		public BufferedImage getImage() {
			return (image);
		}

		public List<BufferedImage> getGeneratedImages() {
			return (generatedImages);
		}
	}
}
