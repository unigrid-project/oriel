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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * The window system Oriel takes from the command line, and the arguments it leaves to the application. The command
 * line belongs to the application: Oriel takes out its own argument and hands the rest back, untouched and in
 * order, to be read however the application likes. It neither prints nor exits; what a mistake leads to is the
 * application's to decide.
 */
public record LaunchOptions(WindowSystem windowSystem, List<String> remaining) {
	public LaunchOptions {
		remaining = List.copyOf(remaining);
	}

	/**
	 * Takes the window system out of the arguments. Outside Linux it is still taken and checked, so that one way of
	 * starting works everywhere, but it changes nothing there.
	 *
	 * @throws IllegalArgumentException when the window system has no value, one it does not know, or is given twice
	 */
	public static LaunchOptions of(final String... args) {
		final Iterator<String> arguments = List.of(args).iterator();
		final List<String> chosen = new ArrayList<>();
		final List<String> remaining = new ArrayList<>();

		while (arguments.hasNext()) {
			final String argument = arguments.next();

			if (Constants.END_OF_OPTIONS.equals(argument)) {
				remaining.add(argument);
				arguments.forEachRemaining(remaining::add);
			} else if (isWindowSystem(argument)) {
				chosen.add(valueOf(argument, arguments));
			} else {
				remaining.add(argument);
			}
		}

		if (chosen.size() > 1) {
			throw new IllegalArgumentException(Constants.WINDOW_SYSTEM_OPTION + " is given more than once");
		}

		return new LaunchOptions(chosen.stream().map(LaunchOptions::windowSystem).findFirst()
			.orElse(Constants.DEFAULT_WINDOW_SYSTEM), remaining);
	}

	/** A line for the application's own help; empty where there is no window system to choose. */
	public static Optional<String> help() {
		return help(Constants.WINDOW_SYSTEM_CHOOSABLE);
	}

	static Optional<String> help(final boolean windowSystemChoosable) {
		return Optional.of(Constants.WINDOW_SYSTEM_OPTION + "=<system>  What the page is drawn on in a Linux "
			+ "desktop session: " + WindowSystem.labels() + " (defaults to "
			+ Constants.DEFAULT_WINDOW_SYSTEM.label() + ", which embeds it in the window on X11 and Wayland "
			+ "desktops alike)."
		).filter(line -> windowSystemChoosable);
	}

	private static boolean isWindowSystem(final String argument) {
		return Constants.WINDOW_SYSTEM_OPTION.equals(argument)
			|| argument.startsWith(Constants.WINDOW_SYSTEM_ASSIGNED);
	}

	/* The value either follows an equals sign or is the next argument. */
	private static String valueOf(final String argument, final Iterator<String> arguments) {
		if (argument.startsWith(Constants.WINDOW_SYSTEM_ASSIGNED)) {
			return argument.substring(Constants.WINDOW_SYSTEM_ASSIGNED.length());
		}

		if (!arguments.hasNext()) {
			throw new IllegalArgumentException(Constants.WINDOW_SYSTEM_OPTION + " needs a value: "
				+ WindowSystem.labels());
		}

		return arguments.next();
	}

	private static WindowSystem windowSystem(final String label) {
		return WindowSystem.labelled(label).orElseThrow(() -> new IllegalArgumentException(
			Constants.WINDOW_SYSTEM_OPTION + " does not know '" + label + "'; it takes " + WindowSystem.labels()
		));
	}
}
