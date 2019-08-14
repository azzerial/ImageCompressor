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

import javax.swing.*;
import java.awt.*;

public class ImageCompressor {

	public static final int FORCED_EXIT;
	public static final int UNSUPPORTED_SYSTEM;

	public static void main(String[] args) {
		checkJavaVersion();

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(() -> {
			StringBuilder title = new StringBuilder(ImageCompressorInfo.NAME)
				.append(" v")
				.append(ImageCompressorInfo.VERSION);

			new ApplicationFrame(title.toString());
		});
	}

	private static void checkJavaVersion() {
		Double version = Double.parseDouble(System.getProperty("java.specification.version"));

		if(version >= 1.8)
			return;
		JOptionPane.showMessageDialog(
			null,
			"You need Java 1.8 or higher to run this program.",
			"ImageCompressor",
			JOptionPane.ERROR_MESSAGE
		);
		System.exit(UNSUPPORTED_SYSTEM);
	}

	static {
		FORCED_EXIT = 10;
		UNSUPPORTED_SYSTEM = 11;
	}
}
