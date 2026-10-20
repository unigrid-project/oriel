/*
    Oriel
    Copyright © 2026 Stiftelsen The Unigrid Foundation

    This library is free software: you can redistribute it and/or modify it under the terms of the
    GNU Lesser General Public License as published by the Free Software Foundation, version 3 of the
    License (see COPYING.LESSER and COPYING).

    This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
    even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License with this library.
    If not, see <http://www.gnu.org/licenses/> and <https://github.com/unigrid-project/oriel>.
 */

package org.unigrid.oriel.window.launch;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * What the browser engine draws the page on in a Linux desktop session. The frame around it is an X11 window
 * even on a Wayland desktop, drawn through Xwayland, and the engine can only be embedded in it from X11. Left
 * to choose, it runs on Wayland there and draws the page in a window of its own, and on GNOME it loads GTK 4,
 * which deadlocks the GTK 3 the file picker needs.
 */
public enum WindowSystem {
	X11("--ozone-platform=x11"),
	WAYLAND("--ozone-platform=wayland"),
	AUTO("--ozone-platform-hint=auto");

	/** X11 embeds the page in the window on X11 and Wayland desktops alike. */
	public static final WindowSystem DEFAULT = X11;

	/** Only a Linux desktop session leaves the engine a window system to choose. */
	public static final boolean CHOOSABLE = System.getProperty("os.name").startsWith("Linux");

	private final String engineSwitch;

	WindowSystem(final String engineSwitch) {
		this.engineSwitch = engineSwitch;
	}

	public String engineSwitch() {
		return engineSwitch;
	}

	/** The name it goes by on the command line. */
	String label() {
		return name().toLowerCase(Locale.ROOT);
	}

	/** The window system a label names, in any case. */
	static Optional<WindowSystem> labelled(final String label) {
		return Arrays.stream(values()).filter(system -> system.label().equalsIgnoreCase(label)).findFirst();
	}

	/** Every label, for a message that has to list them. */
	static String labels() {
		return Arrays.stream(values()).map(WindowSystem::label).collect(Collectors.joining(", "));
	}
}
