/*
    Oriel
    Copyright © 2021-2026 Stiftelsen The Unigrid Foundation

    This library is free software: you can redistribute it and/or modify it under the terms of the
    GNU Lesser General Public License as published by the Free Software Foundation, version 3 of the
    License (see COPYING.LESSER and COPYING).

    This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
    even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License with this library.
    If not, see <http://www.gnu.org/licenses/> and <https://github.com/unigrid-project/oriel>.
 */

package org.unigrid.oriel.window;

import java.time.Duration;
import java.util.List;

/** The fixed values the window is built on, kept in one place. */
public final class Constants {
	/** Oriel's one argument on the command line; an application should not take one of this name for itself. */
	public static final String WINDOW_SYSTEM_OPTION = "--window-system";

	/** The window system given with its value in one argument, as in {@code --window-system=wayland}. */
	public static final String WINDOW_SYSTEM_ASSIGNED = WINDOW_SYSTEM_OPTION + "=";

	/** After this argument everything is the application's, even what looks like Oriel's. */
	public static final String END_OF_OPTIONS = "--";

	/** X11 embeds the page in the window on X11 and Wayland desktops alike. */
	public static final WindowSystem DEFAULT_WINDOW_SYSTEM = WindowSystem.X11;

	/** Only a Linux desktop session leaves the engine a window system to choose. */
	public static final boolean WINDOW_SYSTEM_CHOOSABLE = System.getProperty("os.name").startsWith("Linux");

	/** The icon sizes window managers and task bars ask for. */
	public static final List<Integer> ICON_SIZES = List.of(16, 24, 32, 48, 64, 128, 256);

	/* Where the browser engine is: named by the installer, or else kept in the home folder. */
	public static final String ENGINE_FOLDER_PROPERTY = "janus.jcef";
	public static final String ENGINE_HOME_FOLDER = ".janus/jcef";

	/** Opens the engine to remote debugging on the port it names; nothing listens unless it is set. */
	public static final String DEBUGGING_PORT_PROPERTY = "janus.remote-debugging-port";

	/** How long the engine has to shut down once the window closes before the process ends anyway. */
	public static final Duration SHUTDOWN_GRACE = Duration.ofSeconds(10);

	/* The rounding of the window's corners. */
	public static final int CORNER_RADIUS = 12;
	public static final int CORNER_ARC = CORNER_RADIUS * 2;

	/** How often a drag or a resize follows the pointer, in milliseconds. */
	public static final int FOLLOW_INTERVAL_MILLIS = 10;

	private Constants() {
	}
}
