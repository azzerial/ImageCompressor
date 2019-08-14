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

package net.azzerial.imagecompressor.components;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public final class JFileDrop extends JPanel {

	private static Boolean canFileDrop;

	private Border defaultBorder;
	private Border hoverBorder;
	private DropTargetListener dropListener;
	private JLabel text;

	public JFileDrop(final FileDropListener listener) {
		this(new LineBorder(new Color(0x4D88FF), 5), listener);
	}

	public JFileDrop(final Border border, final FileDropListener fileDropListener) {
		if (!isFileDropSupported())
			throw new UnsupportedOperationException("[JFileDrop] File dropping is unsupported by this OS.");
		setLayout(new BorderLayout());

		this.hoverBorder = border;
		this.dropListener = new DropTargetListener() {
			@Override
			public void dragEnter(DropTargetDragEvent event) {
				boolean isValid = false;
				DataFlavor[] dataFlavors = event.getCurrentDataFlavors();

				for (int i = 0; !isValid && i != dataFlavors.length; i += 1)
					if (dataFlavors[i].isFlavorJavaFileListType())
						isValid = true;
				if (isValid) {
					defaultBorder = getBorder();
					setBorder(hoverBorder);
					event.acceptDrag(1);
				} else
					event.rejectDrag();
			}

			@Override
			public void dragOver(DropTargetDragEvent event) {}

			@Override
			public void dropActionChanged(DropTargetDragEvent event) {}

			@Override
			public void dragExit(DropTargetEvent event) {
				setBorder(defaultBorder);
			}

			@Override
			public void drop(DropTargetDropEvent event) {
				setBorder(defaultBorder);
				try {
					Transferable transferable = event.getTransferable();
					File[] files = null;
					Object list;

					if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
						event.acceptDrop(1);
						list = transferable.getTransferData(DataFlavor.javaFileListFlavor);
						files = new File[((List) list).size()];
						((List) list).toArray(files);
						event.dropComplete(true);
					}
					if (files != null)
						fileDropListener.filesDropped(files);
				} catch (IOException | UnsupportedFlavorException e) {
					e.printStackTrace();
				}
			}
		};

		this.text = new JLabel("", SwingConstants.CENTER);
		add(text, BorderLayout.CENTER);

		DropTarget dropTarget = new DropTarget();

		try {
			dropTarget.addDropTargetListener(dropListener);
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}
		setDropTarget(dropTarget);
	}

	public boolean isActive() {
		return (getDropTarget().isActive());
	}

	public JFileDrop setActive(final boolean enabled) {
		getDropTarget().setActive(enabled);
		return (this);
	}

	public JFileDrop setHoverBorder(final Border border) {
		this.hoverBorder = border;
		return (this);
	}

	public JFileDrop setText(final String text) {
		this.text.setText(text);
		this.text.setForeground(Color.BLACK);
		return (this);
	}

	public JFileDrop setText(final String text, final Color color) {
		this.text.setText(text);
		this.text.setForeground(color);
		return (this);
	}

	public static DropTarget createDropTarget(final FileDropListener listener) {
		if (!isFileDropSupported())
			throw new UnsupportedOperationException("[JFileDrop] File dropping is unsupported by this OS.");

		DropTarget dropTarget = new DropTarget();
		try {
			dropTarget.addDropTargetListener(new DropTargetListener() {
				@Override
				public void dragEnter(DropTargetDragEvent event) {
					boolean isValid = false;
					DataFlavor[] dataFlavors = event.getCurrentDataFlavors();

					for (int i = 0; !isValid && i != dataFlavors.length; i += 1)
						if (dataFlavors[i].isFlavorJavaFileListType())
							isValid = true;
					if (isValid)
						event.acceptDrag(1);
					else
						event.rejectDrag();
				}

				@Override
				public void dragOver(DropTargetDragEvent event) {}

				@Override
				public void dropActionChanged(DropTargetDragEvent event) {}

				@Override
				public void dragExit(DropTargetEvent event) {}

				@Override
				public void drop(DropTargetDropEvent event) {
					try {
						Transferable transferable = event.getTransferable();
						File[] files = null;
						Object list;

						if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
							event.acceptDrop(1);
							list = transferable.getTransferData(DataFlavor.javaFileListFlavor);
							files = new File[((List) list).size()];
							((List) list).toArray(files);
							event.dropComplete(true);
						}
						if (files != null)
							listener.filesDropped(files);
					} catch (IOException | UnsupportedFlavorException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}
		return (dropTarget);
	}

	public static boolean isFileDropSupported() {
		boolean supported;

		if (canFileDrop == null) {
			try {
				Class.forName("java.awt.dnd.DropTarget");
				supported = true;
			} catch (ClassNotFoundException e) {
				supported = false;
			}
			canFileDrop = new Boolean(supported);
		}
		return (canFileDrop.booleanValue());
	}

	public interface FileDropListener {

		void filesDropped(final File[] files);
	}
}
